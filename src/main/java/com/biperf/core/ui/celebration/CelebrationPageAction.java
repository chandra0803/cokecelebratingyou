
package com.biperf.core.ui.celebration;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.plateauawards.PlateauMerchProducts;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.awardbanq.impl.ProductEntryVO;
import com.biperf.core.service.celebration.CelebrationManagerMessageAssociationRequest;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.facebook.FacebookService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlRecipientAssociationRequest;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.claim.RecognitionDetailBean.Ecard;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.celebration.CelebrationManagerMessageValue;
import com.biperf.core.value.celebration.CelebrationPageValue;
import com.biperf.core.value.celebration.CelebrationShareLinkValue;
import com.biperf.core.value.celebration.CelebrationShareUserValue;
import com.biperf.core.value.celebration.CelebrationYearThatWasValue;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * CelebrationPageAction
 * Main action to load the secondary page with all the tiles.
 * 
 */
public class CelebrationPageAction extends BaseCelebrationAction
{
  private static final Log logger = LogFactory.getLog( CelebrationPageAction.class );
  private static final int MESSAGE_LENGTH = 75;
  private static final String CELEBRATION = "celebration";
  private static final String IMAGETYPE = "_lg";

  private static final String IMAGE_SERVICE_FLASH_IMG = "/images/size/864/864/";

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // Redirect to SA URL from mail link
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      return redirectToSACelebrationPage( mapping, request, response );
    }
    else
    {
      return this.display( mapping, form, request, response );
    }
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long claimId = getClaimId( request );
    Long recipientId = getRecipientId( request );
    Long sendToUserId = getSendToUserId( request );
    Long userId = UserManager.getUserId();
    User user = getUserById( userId );

    if ( Objects.isNull( user ) )
    {
      request.getSession().setAttribute( "celebrationFlow", "celebrationPage" );
      request.getSession().setAttribute( "clientStateCelebPage", RequestUtils.getOptionalParamString( request, "clientState" ) );
      request.getSession().setAttribute( "cryptoPassCelebPage", RequestUtils.getOptionalParamString( request, "cryptoPass" ) );
      // redirect to the login page.
      response.sendRedirect( request.getContextPath() + "/login.do" );
      return null;
    }

    boolean displayCelebration = Boolean.FALSE;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    RecognitionClaim claim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

    Set claimRecipients = claim.getClaimRecipients();

    boolean isUserSame = Boolean.FALSE;
    for ( Iterator iter = claimRecipients.iterator(); iter.hasNext(); )
    {
      ClaimRecipient recipient = (ClaimRecipient)iter.next();
      if ( recipient.getRecipient().getId().equals( userId ) )
      {
        isUserSame = Boolean.TRUE;
      }
    }

    if ( isUserSame || user.isManager() || user.isOwner() )
    {
      if ( !claim.isOpen() )
      {
        displayCelebration = Boolean.TRUE;
      }
    }
    if ( !displayCelebration )
    {
      return mapping.findForward( "invalid" );
    }
    else
    {
      ActionMessages errors = new ActionMessages();
      try
      {
        request.getSession().removeAttribute( CELEBRATION_CLAIM_MAP );
        request.getSession().removeAttribute( CELEBRATION_VALUEBEAN_MAP );
        if ( claimId != null )
        {
          RecognitionClaim recognitionClaim = getRecognitionClaim( claimId );
          Map<Long, Object> celebrationClaimMap = new HashMap<Long, Object>();
          celebrationClaimMap.put( claimId, recognitionClaim );
          request.getSession().setAttribute( CELEBRATION_CLAIM_MAP, celebrationClaimMap );

          CelebrationPageValue celebrationValue = null;
          if ( recognitionClaim != null )
          {
            Participant participant = null;
            if ( sendToUserId != null && sendToUserId.equals( userId ) && ( user.isManager() || user.isOwner() ) )
            {
              if ( recipientId != null )
              {
                participant = getClaimRecipient( recognitionClaim, recipientId );
              }
            }
            else
            {
              participant = getClaimRecipient( recognitionClaim, userId );
            }
            if ( participant == null )
            {
              return mapping.findForward( "invalid" );
            }
            else
            {
              celebrationValue = populateCelebrationValue( participant, recognitionClaim, request );
            }
          }
        //28250 customization start
          if( user != null && user.getSvcVideoURL() != null )
          {
        	  celebrationValue.setCorporateVideoUrl(user.getSvcVideoURL());
        	  celebrationValue.setCustomUrl(Boolean.TRUE);
          }
          //28250 customization end
          // determine fillerCount, following scenarios are possible
          // two 4x2 Tiles one 4x2 Tile No 4x2 tile
          // one 2x2 tile 0 fillers - 3 rows 2 fillers - 3 rows 1 filler - 2 rows
          // two 2x2 tiles 2 fillers - 4 rows 1 filler - 3 rows 0 fillers - 2 rows
          // three 2x2 tiles 1 filler - 4 rows 0 fillers - 3 rows 2 fillers - 3 rows

          int totalBlocksNeeded = 0;
          int fillerCount = 0;
          // Defining one block for size of a 2x2 tile, it's going to need 2 blocks for a 4x2 tile.
          // Calculate total blocks needed for a given scenario
          if ( !celebrationValue.getManagerMessageList().isEmpty() )
          {
            totalBlocksNeeded += 2;
          }
          if ( celebrationValue.isDisplayPurl() )
          {
            totalBlocksNeeded += 2;
          }
          if ( celebrationValue.isDisplayYearTile() )
          {
            totalBlocksNeeded += 1;
          }
          if ( celebrationValue.isDisplayTimelineTile() )
          {
            totalBlocksNeeded += 1;
          }
          if ( celebrationValue.isDisplayVideoTile() )
          {
            totalBlocksNeeded += 1;
          }

          // assign fillers needed for calculated number of blocks to present a complete row
          switch ( totalBlocksNeeded )
          {
            case 1:
              fillerCount = 1;
              break;
            case 3:
              fillerCount = 2;
              break;
            case 4:
              fillerCount = 1;
              break;
            case 6:
              fillerCount = 2;
              break;
            case 7:
              fillerCount = 1;
              break;
            default:// for 2 or 5 blocks we don't need fillers
              fillerCount = 0;
              break;
          }

          celebrationValue.setFillerCount( fillerCount );

          request.setAttribute( CELEBRATION_VALUE, celebrationValue );
          Map<Long, Object> celebrationValueMap = new HashMap<Long, Object>();
          celebrationValueMap.put( claimId, celebrationValue );
          request.getSession().setAttribute( CELEBRATION_VALUEBEAN_MAP, celebrationValueMap );

          String celebrationRecognitionPurlUrl = getCelebrationRecognitionPurlUrl( claimId, request );
          request.setAttribute( CELEBRATION_PURL_URL, celebrationRecognitionPurlUrl );
          String celebrationImageFillerUrl = getCelebrationImageFillerUrl( claimId, request );
          request.setAttribute( CELEBRATION_IMAGE_FILLER_URL, celebrationImageFillerUrl );
        }
      }
      catch( NumberFormatException nfe )
      {
        logger.error( "Error loading celebration page, NumberFormatException: " + nfe );
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "celebration.page.invalid.ERROR_LOADING_PAGE" ) );
        saveErrors( request, errors );
      }
      catch( Exception e )
      {
        logger.error( "Error loading celebration page, Exception: " + e );
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "celebration.page.invalid.ERROR_LOADING_PAGE" ) );
        saveErrors( request, errors );
      }
      if ( !errors.isEmpty() )
      {
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
      else
      {
        return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
      }
    }
  }

  public ActionForward expired( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "expired" );
  }

  private boolean displayCelebration( Long claimId, Long userId )
  {
    return getClaimService().displayCelebration( claimId, userId );
  }

  private Claim getClaim( Long claimId )
  {
    return getClaimService().getClaimById( claimId );
  }

  private Participant getClaimRecipient( RecognitionClaim recognitionClaim, Long userId )
  {
    Set<ClaimRecipient> recipients = recognitionClaim.getClaimRecipients();

    if ( recipients != null )
    {
      for ( ClaimRecipient claimRecipient : recipients )
      {
        if ( claimRecipient.getRecipient().getId().equals( userId ) )
        {
          return claimRecipient.getRecipient();
        }
      }
    }
    return null;
  }

  @SuppressWarnings( "unchecked" )
  private RecognitionClaim getRecognitionClaim( Long claimId )
  {
    RecognitionClaim recognitionClaim = null;
    Claim claim = null;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CARD ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    claim = getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

    if ( claim instanceof RecognitionClaim )
    {
      recognitionClaim = (RecognitionClaim)claim;
    }
    return recognitionClaim;
  }

  private CelebrationPageValue populateCelebrationValue( Participant participant, RecognitionClaim recognitionClaim, HttpServletRequest request ) throws InvalidClientStateException
  {
    Long userId = UserManager.getUserId();
    Long claimId = recognitionClaim.getId();
    Long recipientId = getRecipientId( request );
    User user = getUserById( userId );
    Long sendToUserId = getSendToUserId( request );
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionClaim.getPromotion();
    boolean awardTypePoints = isAwardTypePoints( recognitionPromotion.getAwardType() );
    boolean awardTypePlateau = isAwardTypePlateau( recognitionPromotion.getAwardType() );
    Boolean anniversaryInYears = null;
    CelebrationPageValue celebrationValue = new CelebrationPageValue();
    if ( recognitionPromotion.getAnniversaryInYears() != null )
    {
      anniversaryInYears = recognitionPromotion.getAnniversaryInYears().booleanValue();
      celebrationValue.setAnniversaryInYears( anniversaryInYears );
    }
    if ( anniversaryInYears != null && anniversaryInYears )
    {
      celebrationValue.setAnniversaryNumberOfYears( recognitionClaim.getAnniversaryNumberOfYears() );
    }
    else
    {
      celebrationValue.setAnniversaryNumberOfDays( recognitionClaim.getAnniversaryNumberOfDays() );
    }

    boolean includePurl = false;
    AssociationRequestCollection asc = new AssociationRequestCollection();
    asc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
    asc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR_COMMENT ) );
    PurlRecipient purlRecipient = getPurlService().getPurlRecipientByClaimIdWithAssociations( claimId, asc );
    if ( recognitionPromotion.isIncludePurl() && isContributionsAvailable( purlRecipient ) )
    {
      includePurl = true;
    }
    Country country = getUserService().getPrimaryUserAddressCountry( participant.getId() );

    celebrationValue.setClaimId( claimId );
    celebrationValue.setRecipientId( userId );
    celebrationValue.setRecipientFirstName( participant.getFirstName() );
    celebrationValue.setRecipientLastName( participant.getLastName() );
    celebrationValue.setPromotionId( recognitionPromotion.getId() );
    celebrationValue.setPromotionName( recognitionPromotion.getName() );
    celebrationValue.seteCardUrl( getECardUrl( recognitionClaim, request ) );
    celebrationValue.setAwardActive( recognitionPromotion.isAwardActive() );
    celebrationValue.setDisplayPurl( includePurl );

    String displayInfo = "";
    if ( includePurl )
    {
      if ( purlRecipient != null )
      {
        displayInfo = purlRecipient.getContributorDisplayInfo();

        // populate purl url
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put( "purlRecipientId", purlRecipient.getId().toString() );
        paramMap.put( "claimId", claimId.toString() );
        String purlUrl = ClientStateUtils.generateEncodedLink( getSiteUrlPrefix(), "/purl/purlRecipient.do?method=display", paramMap );
        celebrationValue.setPurlUrl( purlUrl );
      }

    }
    else
    {
      displayInfo = recognitionPromotion.getName();
    }
    celebrationValue.setDisplayInfo( displayInfo );

    if ( recognitionPromotion.isVideoTileEnabled() )
    {
      celebrationValue.setDisplayVideoTile( true );
      String mediaName = recognitionPromotion.getVideoPath().getCode();
      String videoImage = mediaName + ".jpg";
      celebrationValue.setCorporateVideoUrl( getCorporateVideoUrl( mediaName ) );
      celebrationValue.setVideoImage( getCorporateVideoImageUrl( videoImage ) );
    }

    // Company Timeline
    if ( recognitionPromotion.isTimelineTileEnabled() )
    {
      celebrationValue.setDisplayTimelineTile( true );
    }

    if ( recognitionPromotion.isYearTileEnabled() )
    {
      Date hireDate = getParticipantService().getActiveHireDate( userId );
      Integer year = 0;
      if ( recognitionPromotion.isServiceAnniversary() )
      {
        if ( recognitionPromotion.isIncludePurl() )
        {
          Integer numOfYears = null;
          if ( anniversaryInYears != null && anniversaryInYears )
          {
            numOfYears = celebrationValue.getAnniversaryNumberOfYears();
          }
          else
          {
            numOfYears = (int)Math.floor( celebrationValue.getAnniversaryNumberOfDays() / 365 );
          }
          if ( Objects.nonNull( sendToUserId ) && sendToUserId.equals( userId ) && Objects.nonNull( recipientId ) && ( user.isManager() || user.isOwner() ) )
          {
            year = getPurlService().getPurlAwardDate( recipientId, purlRecipient.getId() );

          }
          else
          {
            year = getPurlService().getPurlAwardDate( userId, purlRecipient.getId() );
          }
          year = year - numOfYears;

        }
        else
        {
          if ( Objects.nonNull( sendToUserId ) && sendToUserId.equals( userId ) && ( user.isManager() || user.isOwner() ) && Objects.nonNull( participant ) )
          {
            year = getPurlService().getCelebrationAwardDate( participant.getId(), claimId );

          }
          else
          {
            year = getPurlService().getCelebrationAwardDate( userId, claimId );
          }
        }
      }
      else if ( hireDate != null )
      {
        year = DateUtils.getYearFromDate( hireDate );
      }

      if ( year != 0 )
      {
        celebrationValue.setYearThatWasList( populateYearThatWasList( celebrationValue.getAnniversaryNumberOfYears(), country.getCountryCode(), year ) );
      }

      if ( celebrationValue.getYearThatWasList() != null && !celebrationValue.getYearThatWasList().isEmpty() )
      {
        celebrationValue.setDisplayYearTile( true );
      }
      else
      {
        celebrationValue.setDisplayYearTile( false );
      }
    }
    // Choose Award and Browse Awards
    MerchOrder merchOrder = null;
    if ( recognitionPromotion.isAwardActive() )
    {
      if ( awardTypePlateau )
      {
        merchOrder = getMerchOrderService().getMerchOrderByClaimIdUserId( userId, claimId );
      }

      if ( awardTypePlateau && merchOrder != null && !merchOrder.isRedeemed() )
      {
        celebrationValue.setDisplayPlateauChooseAward( true );
        celebrationValue.setDisplayPlateauBrowseAwards( true );
        celebrationValue.setAwardRedeemEndDate( getAwardRedeemEndDate( merchOrder ) );
        celebrationValue.setMerchProducts( getMerchProducts( recognitionPromotion, merchOrder, participant, country ) );
        String merchLinqShoppingUrl = getMerchlinqShoppingUrl( recognitionPromotion, merchOrder, participant, country );
        celebrationValue.setMerchLinqShoppingUrl( merchLinqShoppingUrl );
        celebrationValue.setPlateauAward( awardTypePlateau );
        celebrationValue.setAwardLink( merchLinqShoppingUrl );
      }
      else if ( awardTypePoints )
      {
        celebrationValue.setDisplayPointsChooseAward( true );
        celebrationValue.setPointsAward( awardTypePoints );
        celebrationValue.setAwardAmount( getAwardAmount( claimId, userId ) );
        celebrationValue.setAwardLink( buildAwardLink( request, claimId ) );
      }
    }

    // Social Links
    if ( recognitionPromotion.isShareToMedia() )
    {
      celebrationValue.setDisplayShareOptions( true );
      CelebrationShareUserValue celebrationShareUserValue = new CelebrationShareUserValue( participant.getFirstName(), participant.getLastName(), displayInfo );
      celebrationValue.setShareLinkBeanList( getSocialLinks( celebrationShareUserValue ) );
    }

    // manager message tile
    List<CelebrationManagerMessageValue> messageList = new ArrayList<CelebrationManagerMessageValue>();
    if ( recognitionPromotion.isAllowOwnerMessage() )
    {
      List<CelebrationManagerMessageValue> managerMessageList = buildManagerMessage( recognitionClaim, recognitionPromotion );
      messageList.addAll( managerMessageList );
    }

    // giver comments
    String submitterMessage = recognitionClaim.getSubmitterComments();
    if ( isMessageNotEmpty( submitterMessage ) )
    {
      CelebrationManagerMessageValue submitterMessageValue = buildSubmitterMessage( recognitionClaim, replaceHTML( submitterMessage ) );
      messageList.add( submitterMessageValue );
    }

    // default message
    if ( recognitionPromotion.isAllowDefaultMessage() )
    {
      String defaultMessage = recognitionPromotion.getCelebrationsDefaultMessageText();
      String avatar = recognitionPromotion.getDefaultCelebrationAvatar();
      String defaultName = recognitionPromotion.getDefaultCelebrationName();
      if ( isMessageNotEmpty( defaultMessage ) )
      {
        CelebrationManagerMessageValue defaultMessageValue = buildDefaultMessage( replaceHTML( defaultMessage ), avatar, defaultName );
        messageList.add( defaultMessageValue );
      }
    }

    if ( messageList.size() > 0 )
    {
      celebrationValue.setManagerMessageList( messageList );
    }

    return celebrationValue;
  }

  private List<CelebrationManagerMessageValue> buildManagerMessage( RecognitionClaim recognitionClaim, RecognitionPromotion recognitionPromotion )
  {
    CelebrationManagerMessage celebrationManagerMessage = null;
    if ( recognitionClaim.getCelebrationManagerMessage() != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new CelebrationManagerMessageAssociationRequest( CelebrationManagerMessageAssociationRequest.CELEBRATION_MANAGER ) );
      associationRequestCollection.add( new CelebrationManagerMessageAssociationRequest( CelebrationManagerMessageAssociationRequest.CELEBRATION_MANAGER_ABOVE ) );
      celebrationManagerMessage = getCelebrationService().getCelebrationManagerMessageById( recognitionClaim.getCelebrationManagerMessage().getId(), associationRequestCollection );
    }

    List<CelebrationManagerMessageValue> managerMessageList = new ArrayList<CelebrationManagerMessageValue>();
    if ( celebrationManagerMessage != null )
    {
      // manager
      String managerMessage = celebrationManagerMessage.getManagerMessage();
      if ( isMessageNotEmpty( managerMessage ) )
      {
        CelebrationManagerMessageValue messageValue = buildManagerMessage( celebrationManagerMessage, replaceHTML( managerMessage ) );
        if ( messageValue != null )
        {
          managerMessageList.add( messageValue );
        }
      }
      // manager above
      String managerAboveMessage = celebrationManagerMessage.getManagerAboveMessage();
      if ( isMessageNotEmpty( managerAboveMessage ) )
      {
        CelebrationManagerMessageValue messageValue = buildManagerAboveMessage( celebrationManagerMessage, replaceHTML( managerAboveMessage ) );
        if ( messageValue != null )
        {
          managerMessageList.add( messageValue );
        }
      }
    }

    return managerMessageList;
  }

  private boolean isContributionsAvailable( PurlRecipient purlRecipient )
  {
    for ( PurlContributor purlContributor : purlRecipient.getContributors() )
    {
      if ( isContributionsAvailable( purlContributor ) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isContributionsAvailable( PurlContributor purlContributor )
  {
    return null != purlContributor && !purlContributor.getComments().isEmpty();
  }

  private boolean isMessageNotEmpty( String message )
  {
    if ( StringUtils.isNotEmpty( message ) && !message.trim().equals( "" ) )
    {
      return true;
    }
    return false;
  }

  private String replaceHTML( String message )
  {
    String updatedMessage = message;
    updatedMessage = updatedMessage.replaceAll( "<p>", "" );
    updatedMessage = updatedMessage.replaceAll( "</p>", "" );
    return updatedMessage;
  }

  private CelebrationManagerMessageValue buildManagerMessage( CelebrationManagerMessage celebrationManagerMessage, String managerMessage )
  {
    CelebrationManagerMessageValue messageValue = null;
    User manager = celebrationManagerMessage.getManager();
    if ( manager != null )
    {
      String trimmedManagerMessage = (String)StringUtil.skipHTML( managerMessage );
      messageValue = new CelebrationManagerMessageValue();
      messageValue.setUserId( manager.getId() );
      messageValue.setFirstName( manager.getFirstName() );
      messageValue.setLastName( manager.getLastName() );

      Participant managerPax = getParticipantService().getParticipantById( manager.getId() );
      String managerAvatarUrl = managerPax.getAvatarSmallFullPath();
      messageValue.setAvatarUrl( managerAvatarUrl );
      messageValue.setMessage( trimmedManagerMessage );
      String shortMessage = getMessageTrimmed( trimmedManagerMessage );
      messageValue.setShortMessage( shortMessage );
      messageValue.setDisplayReadMoreOrLess( displayReadMoreOrLess( trimmedManagerMessage ) );
      messageValue.setDisplayOrder( 1 );
    }
    return messageValue;
  }

  private CelebrationManagerMessageValue buildManagerAboveMessage( CelebrationManagerMessage celebrationManagerMessage, String managerAboveMessage )
  {
    CelebrationManagerMessageValue messageValue = null;
    User managerAbove = celebrationManagerMessage.getManagerAbove();
    if ( managerAbove != null )
    {
      String trimmedManagerAboveMessage = (String)StringUtil.skipHTML( managerAboveMessage );
      messageValue = new CelebrationManagerMessageValue();
      messageValue.setUserId( managerAbove.getId() );
      messageValue.setFirstName( managerAbove.getFirstName() );
      messageValue.setLastName( managerAbove.getLastName() );

      Participant managerAbovePax = getParticipantService().getParticipantById( managerAbove.getId() );
      String managerAboveAvatarUrl = managerAbovePax.getAvatarSmallFullPath();
      messageValue.setAvatarUrl( managerAboveAvatarUrl );
      messageValue.setMessage( trimmedManagerAboveMessage );
      String shortMessage = getMessageTrimmed( trimmedManagerAboveMessage );
      messageValue.setShortMessage( shortMessage );
      messageValue.setDisplayReadMoreOrLess( displayReadMoreOrLess( trimmedManagerAboveMessage ) );
      messageValue.setDisplayOrder( 2 );
    }
    return messageValue;
  }

  private CelebrationManagerMessageValue buildSubmitterMessage( RecognitionClaim recognitionClaim, String submitterMessage )
  {
    CelebrationManagerMessageValue messageValue = new CelebrationManagerMessageValue();
    String trimmedSubmitterMessage = (String)StringUtil.skipHTML( submitterMessage );
    Participant submitter = recognitionClaim.getSubmitter();
    messageValue.setUserId( submitter.getId() );
    messageValue.setFirstName( submitter.getFirstName() );
    messageValue.setLastName( submitter.getLastName() );
    messageValue.setAvatarUrl( submitter.getAvatarSmallFullPath() );
    messageValue.setMessage( trimmedSubmitterMessage );
    String shortMessage = getMessageTrimmed( trimmedSubmitterMessage );
    messageValue.setShortMessage( shortMessage );
    messageValue.setDisplayReadMoreOrLess( displayReadMoreOrLess( trimmedSubmitterMessage ) );
    messageValue.setDisplayOrder( 3 );
    return messageValue;
  }

  private String getMessageTrimmed( String message )
  {
    String shortMessage = "";
    if ( StringUtils.isNotEmpty( message ) )
    {
      if ( message.length() > MESSAGE_LENGTH )
      {
        shortMessage = message.substring( 0, MESSAGE_LENGTH ) + "...";
      }
      else
      {
        shortMessage = message.substring( 0, message.length() );
      }
    }
    return shortMessage;
  }

  private boolean displayReadMoreOrLess( String managerMessage )
  {
    boolean displayReadMoreOrLess = false;
    if ( StringUtils.isNotEmpty( managerMessage ) )
    {
      if ( managerMessage.length() > MESSAGE_LENGTH )
      {
        displayReadMoreOrLess = true;
      }
    }
    return displayReadMoreOrLess;
  }

  private CelebrationManagerMessageValue buildDefaultMessage( String defaultMessage, String avatar, String defaultName )
  {
    CelebrationManagerMessageValue messageValue = new CelebrationManagerMessageValue();
    String trimmedDefaultMessage = (String)StringUtil.skipHTML( defaultMessage );
    messageValue.setUserId( null );
    messageValue.setFirstName( defaultName );
    messageValue.setLastName( "" );
    messageValue.setAvatarUrl( avatar );
    messageValue.setMessage( trimmedDefaultMessage );
    String shortMessage = getMessageTrimmed( trimmedDefaultMessage );
    messageValue.setShortMessage( shortMessage );
    messageValue.setDisplayReadMoreOrLess( displayReadMoreOrLess( trimmedDefaultMessage ) );
    messageValue.setDisplayOrder( 4 );
    return messageValue;
  }

  private List<CelebrationShareLinkValue> getSocialLinks( CelebrationShareUserValue celebrationShareUserValue )
  {
    List<CelebrationShareLinkValue> shareLinkBeanList = new ArrayList<CelebrationShareLinkValue>();

    String message = null;
    message = celebrationShareUserValue.getFirstName() + " " + celebrationShareUserValue.getLastName() + " " + CmsResourceBundle.getCmsBundle().getString( "celebration.tile.page.PUBLIC_SHARE_MSG1" )
        + " " + celebrationShareUserValue.getDisplayInfo() + " " + CmsResourceBundle.getCmsBundle().getString( "celebration.tile.page.PUBLIC_SHARE_MSG2" );

    if ( isPublicRecognitionFacebookAllowed() )
    {
      shareLinkBeanList.add( new CelebrationShareLinkValue( "facebook", "facebook", getFacebookFeedDialogUrlForCelebrationPost( message ) ) );
    }

    String formattedMesg = message.replace( "<center></center>", "" );

    if ( isPublicRecognitionLinkedInAllowed() )
    {
      shareLinkBeanList.add( new CelebrationShareLinkValue( "linkedin", "linkedin", getLinkedinFeedDialogUrlForCelebrationPost( formattedMesg ) ) );
    }

    if ( isPublicRecognitionTwitterAllowed() )
    {
      shareLinkBeanList.add( new CelebrationShareLinkValue( "twitter", "twitter", getTwitterFeedDialogUrlForCelebrationPost( formattedMesg ) ) );
    }

    if ( isPublicRecognitionChatterAllowed() )
    {
      shareLinkBeanList.add( new CelebrationShareLinkValue( "chatter", "chatter", getChatterFeedDialogForCelebrationPost( formattedMesg ) ) );
    }
    return shareLinkBeanList;
  }

  // Facebook Feed Url for public recognition post
  private String getFacebookFeedDialogUrlForCelebrationPost( String message )
  {
    String DIALOG_URL_FORMAT = "http://{0}.facebook.com/dialog/feed" + "?app_id={1}" + "&redirect_uri={2}" + "&caption={3}";

    StringBuilder sb = new StringBuilder( DIALOG_URL_FORMAT );
    sb.append( "&name={4}" );
    sb.append( "&link={5}" );

    String trimmedMessage = StringUtils.isEmpty( message ) ? message : message.length() > 420 ? message.substring( 0, 415 ) + " ... " : message;
    final MessageFormat formatter = new MessageFormat( sb.toString() );
    String[] parameters = new String[6];
    try
    {
      // for reference go to http://developers.facebook.com/docs/reference/dialogs/feed/
      parameters[0] = "www";
      parameters[1] = getFacebookService().getAppId();
      parameters[2] = URLEncoder.encode( "http://facebook.com", "UTF-8" );
      parameters[3] = trimmedMessage;
      parameters[4] = "My Recognition";
      parameters[5] = URLEncoder.encode( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(), "UTF-8" );
    }
    catch( Throwable t )
    {
      // do nothing.. ?
    }

    String url = formatter.format( parameters );

    return url;
  }

  /**
   * Chatter Infor for post
   * @param purlUrl
   * @param eCardImageUrl
   * @param formattedMesg
   * @return
   */
  private String getChatterFeedDialogForCelebrationPost( String formattedMesg )
  {
    // Chatter call is failing if state is more than 1530 characters
    if ( formattedMesg.length() > 1000 )
    {
      formattedMesg = formattedMesg.substring( 0, 1000 ) + "...";
    }
    String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String chatterUrl = systemUrl + "/participant/chatterAuthorizationSubmit.do?method=getChatterAuthorizationCode&state=" + formattedMesg;
    return chatterUrl;
  }

  /**
   *  Twitter Info
   * @param recipientId
   * @param isMobile
   * @param message
   * @return List<PublicRecognitionShareLinkBean>
   * @throws ServiceErrorException
   */
  private String getTwitterFeedDialogUrlForCelebrationPost( String message )
  {
    String twitterShareUrl = "http://twitter.com/share?text={0}";
    String[] parameters = new String[1];

    try
    {
      String trimmedMessage = StringUtils.isEmpty( message ) ? message : message.length() > 140 ? message.substring( 0, 135 ) + " ... " : message;
      parameters[0] = URLEncoder.encode( trimmedMessage, "UTF-8" );
    }
    catch( Throwable t )
    {
      // do nothing.... ?
    }
    return new MessageFormat( twitterShareUrl ).format( parameters );
  }

  /**
   * Linkedin Info    
   * @param recipientId
   * @param isMobile
   * @param message
   * @return List<PublicRecognitionShareLinkBean>
   * @throws ServiceErrorException
   */
  private String getLinkedinFeedDialogUrlForCelebrationPost( String message )
  {

    final String LINKEDIN_SHARE_URL = "http://www.linkedin.com/shareArticle?mini={0}&url={1}&title={2}";
    final MessageFormat formatter = new MessageFormat( LINKEDIN_SHARE_URL );
    String[] parameters = new String[3];
    try
    {
      parameters[0] = "true";
      parameters[1] = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      parameters[2] = URLEncoder.encode( message, "UTF-8" );

    }
    catch( Throwable t )
    {
      // do nothing.. ?
    }
    String url = formatter.format( parameters );

    return url;
  }

  private List<CelebrationYearThatWasValue> populateYearThatWasList( Integer numYears, String countryCode, int year )
  {
    List<CelebrationYearThatWasValue> localFunFacts = new ArrayList<CelebrationYearThatWasValue>();
    List<CelebrationYearThatWasValue> generalFunFacts = new ArrayList<CelebrationYearThatWasValue>();

    List<Content> contents = (List<Content>)ContentReaderManager.getContentReader().getContent( "celebration.year_that_was" );
    if ( contents != null )
    {
      for ( Content content : contents )
      {
        Map<String, String> contentDataMap = content.getContentDataMap();
        Integer factYear = new Integer( contentDataMap.get( "YEAR" ) );
        if ( factYear != null && factYear.equals( year ) )
        {
          String imagePath = contentDataMap.get( "IMAGE_PATH" );
          String factCountryCode = contentDataMap.get( "COUNTRY_CODE" );
          String factContent = contentDataMap.get( "CONTENT" );
          if ( factCountryCode.equalsIgnoreCase( "INTERNATIONAL" ) )
          {
            CelebrationYearThatWasValue generalFunFact = new CelebrationYearThatWasValue();
            generalFunFact.setImg( imagePath );
            generalFunFact.setCountryCode( factCountryCode );
            generalFunFact.setYear( factYear );
            generalFunFact.setContent( factContent );
            generalFunFacts.add( generalFunFact );
          }
          if ( factCountryCode.equalsIgnoreCase( countryCode ) )
          {
            CelebrationYearThatWasValue localFunFact = new CelebrationYearThatWasValue();
            localFunFact.setImg( imagePath );
            localFunFact.setCountryCode( factCountryCode );
            localFunFact.setYear( factYear );
            localFunFact.setContent( factContent );
            localFunFacts.add( localFunFact );
          }
        }
      }
    }
    if ( !localFunFacts.isEmpty() )
    {
      return localFunFacts;
    }
    else
    {
      return generalFunFacts;
    }
  }

  private User getUserById( Long userId )
  {
    User recipient = null;
    if ( userId != null )
    {
      recipient = getUserService().getUserById( userId );
    }
    return recipient;
  }

  boolean isAwardTypePoints( PromotionAwardsType awardType )
  {
    boolean awardTypePoints = false;
    if ( PromotionAwardsType.POINTS.equals( awardType.getCode() ) )
    {
      awardTypePoints = true;
    }
    return awardTypePoints;
  }

  boolean isAwardTypePlateau( PromotionAwardsType awardType )
  {
    boolean awardTypePoints = false;
    if ( PromotionAwardsType.MERCHANDISE.equals( awardType.getCode() ) )
    {
      awardTypePoints = true;
    }
    return awardTypePoints;
  }

  private String getCorporateVideoUrl( String mediaName )
  {
    String corporateVideoUrl = getSiteUrlPrefix() + "/videos/celebration/" + mediaName + "/" + mediaName;
    return corporateVideoUrl;
  }

  private String getCorporateVideoImageUrl( String videoImage )
  {
    String corporateimageVideoUrl = getSiteUrlPrefix() + "/assets/img/celebration/video_img/" + videoImage;
    return corporateimageVideoUrl;
  }

  private Long getAwardAmount( Long claimId, Long userId )
  {
    Long awardAmount = getJournalService().getAwardAmountByClaimIdByUserId( claimId, userId );
    return awardAmount;
  }

  private long getAwardRedeemEndDate( MerchOrder merchOrder )
  {
    long endDate = 0;
    if ( merchOrder != null && merchOrder.getExpirationDate() != null )
    {
      endDate = merchOrder.getExpirationDate().getTime();
    }
    return endDate;
  }

  private String buildAwardLink( HttpServletRequest request, Long claimId )
  {
    return ClientStateUtils.generateEncodedLink( request.getContextPath(), "/celebration/celebrationShopping.do?method=displayShopping", buildParameterMap( claimId ) );
  }

  private Map<String, Object> buildParameterMap( Long claimId )
  {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "claimId", claimId );
    return params;
  }

  private String getECardUrl( RecognitionClaim recognitionClaim, HttpServletRequest request )
  {
    String eCardUrl = "";
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionClaim.getPromotion();
    if ( recognitionClaim.getCard() != null )
    {
      String flashName = null;
      Card card = recognitionClaim.getCard();
      if ( card instanceof ECard )
      {
        ECard eCard = (ECard)card;
        flashName = eCard.getFlashNameLocale( UserManager.getLocale().toString() );
      }
      eCardUrl = flashName;
    }
    else if ( recognitionClaim.getOwnCardName() != null && recognitionClaim.getOwnCardName().trim().length() > 0 )
    {
      Ecard ecard = new Ecard( recognitionClaim.getOwnCardName() );
      eCardUrl = ecard.getImgUrl();
    }
    else if ( recognitionPromotion.isServiceAnniversary() )
    {
      Integer anniversaryNumberOfDaysOrYears;
      if ( recognitionPromotion.getAnniversaryInYears() )
      {
        anniversaryNumberOfDaysOrYears = recognitionClaim.getAnniversaryNumberOfYears();
      }
      else
      {
        anniversaryNumberOfDaysOrYears = recognitionClaim.getAnniversaryNumberOfDays();
      }
      String ecardFlashName = CELEBRATION + anniversaryNumberOfDaysOrYears + IMAGETYPE;
      String ecard = getCelebrationService().getServiceAnniversaryEcardOrDefaultCelebrationEcard( ecardFlashName, recognitionPromotion.getId(), UserManager.getLocale().toString() );
      eCardUrl = ecard;
    }
    else
    {
      String ecard = getCelebrationService().getServiceAnniversaryEcardOrDefaultCelebrationEcard( null, recognitionPromotion.getId(), UserManager.getLocale().toString() );
      eCardUrl = ecard;
    }
    return eCardUrl;
  }

  private String getMerchlinqShoppingUrl( RecognitionPromotion recognitionPromotion, MerchOrder merchOrder, Participant participant, Country country )
  {
    String url = getMerchOrderService().getOnlineShoppingUrl( recognitionPromotion, merchOrder.getId(), country, participant, false );
    return url;
  }

  private String getMerchlinqShoppingProductDetailUrl( RecognitionPromotion recognitionPromotion,
                                                       MerchOrder merchOrder,
                                                       Participant participant,
                                                       Country country,
                                                       String productSetId,
                                                       String catalogId )
  {
    String url = getMerchOrderService().getOnlineShoppingProductDetailUrl( recognitionPromotion, merchOrder.getId(), country, participant, false, productSetId, catalogId );
    return url;
  }

  private List<PlateauMerchProducts> getMerchProducts( RecognitionPromotion recognitionPromotion, MerchOrder merchOrder, Participant participant, Country country )
  {
    Long userId = participant.getId();
    List<PlateauMerchProducts> products = null;
    AwardBanqMerchResponseValueObject data = getMerchlinqLevelData( recognitionPromotion, participant, country );
    if ( data != null )
    {
      String levelName = null;
      if ( merchOrder != null )
      {
        levelName = merchOrder.getPromoMerchProgramLevel().getLevelName();
        products = getMerchLevelProducts( data, levelName, recognitionPromotion, merchOrder, participant, country );
      }
    }
    return products;
  }

  private AwardBanqMerchResponseValueObject getMerchlinqLevelData( RecognitionPromotion recognitionPromotion, Participant participant, Country country )
  {
    AwardBanqMerchResponseValueObject merchlinqLevelData = null;

    PromoMerchCountry promoMerchCountry = recognitionPromotion.getPromoMerchCountryForCountryCode( country.getCountryCode() );
    String programId = null;
    if ( promoMerchCountry == null && recognitionPromotion.getPromoMerchCountries() != null && recognitionPromotion.getPromoMerchCountries().size() > 0 )
    {
      promoMerchCountry = (PromoMerchCountry)recognitionPromotion.getPromoMerchCountries().iterator().next();
      programId = promoMerchCountry.getProgramId();
    }
    else
    {
      if ( promoMerchCountry != null )
      {
        programId = promoMerchCountry.getProgramId();
      }
    }
    try
    {
      merchlinqLevelData = getMerchLevelService().getMerchlinqLevelDataWebService( programId, true, true );
    }
    catch( ServiceErrorException e )
    {
      logger.error( "Error getting merch linq data with programId: " + programId, e );
      e.printStackTrace();
    }

    return merchlinqLevelData;
  }

  private List<PlateauMerchProducts> getMerchLevelProducts( AwardBanqMerchResponseValueObject data,
                                                            String levelName,
                                                            RecognitionPromotion recognitionPromotion,
                                                            MerchOrder merchOrder,
                                                            Participant participant,
                                                            Country country )
  {

    List list = (List)data.getMerchLevel();
    Iterator listItr = null;
    List<PlateauMerchProducts> productsList = new ArrayList<PlateauMerchProducts>();
    PlateauMerchProducts levelProducts = null;
    if ( list != null )
    {
      listItr = list.iterator();
    }
    if ( listItr != null )
    {
      while ( listItr.hasNext() )
      {
        MerchLevelValueObject merchLevel = (MerchLevelValueObject)listItr.next();
        if ( merchLevel.getName().equalsIgnoreCase( levelName ) )
        {
          Collection col = (Collection)merchLevel.getMerchLevelProduct();
          if ( col != null )
          {
            Iterator itr = col.iterator();
            while ( itr.hasNext() )
            {
              MerchLevelProductValueObject mlp = (MerchLevelProductValueObject)itr.next();
              levelProducts = new PlateauMerchProducts();
              levelProducts.setId( mlp.getProductSetId() );
              Locale userLocale = UserManager.getLocale();

              for ( Iterator<ProductEntryVO> entryIter = mlp.getProductGroupDescriptions().getEntry().iterator(); entryIter.hasNext(); )
              {
                ProductEntryVO entryVO = entryIter.next();

                if ( entryVO.getValue().getLocale().equals( UserManager.getUserLocale() ) )
                {
                  if ( entryVO != null )
                  {
                    levelProducts.setDesc( entryVO.getValue().getCopy() );
                    levelProducts.setName( entryVO.getValue().getDescription() );
                  }
                }
              }

              levelProducts.setThumbnail( mlp.getThumbnailImageURL() + "" );
              levelProducts.setImg( mlp.getDetailImageURL() + "" );

              // update image urls with https
              if ( !StringUtils.isEmpty( levelProducts.getThumbnail() ) )
              {
                levelProducts.setThumbnail( levelProducts.getThumbnail().toString().replaceFirst( "http:", "https:" ) );
              }
              if ( !StringUtils.isEmpty( levelProducts.getImg() ) )
              {
                levelProducts.setImg( levelProducts.getImg().toString().replaceFirst( "http:", "https:" ) );
              }

              String productDetailUrl = getMerchlinqShoppingProductDetailUrl( recognitionPromotion, merchOrder, participant, country, mlp.getProductSetId(), mlp.getCatalogId() );
              levelProducts.setUrl( productDetailUrl );

              productsList.add( levelProducts );
            }
          }
        }
      }
    }
    return productsList;
  }

  private String getCelebrationRecognitionPurlUrl( Long claimId, HttpServletRequest request )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "claimId", claimId.toString() );
    String celebrationRecognitionPurlUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/celebration/displayRecognitionPurl.do", paramMap );
    return celebrationRecognitionPurlUrl;
  }

  private String getCelebrationImageFillerUrl( Long claimId, HttpServletRequest request )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "claimId", claimId.toString() );
    String celebrationRecognitionPurlUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/celebration/displayImageFiller.do", paramMap );
    return celebrationRecognitionPurlUrl;
  }

  private String escapeColumnValue( Object rawValue )
  {
    String stringValue = StringUtils.trim( rawValue.toString() );

    if ( stringValue == null )
    {
      return null;
    }
    stringValue = StringUtils.replace( stringValue, "\"", "&quot" );

    return stringValue;
  }

  private ActionForward redirectToSACelebrationPage( ActionMapping mapping, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    Long claimId = null;
    Long recipientId = null;
    try
    {
      claimId = getClaimId( request );
      recipientId = getRecipientId( request );

      if ( null != claimId && null != recipientId && Objects.nonNull( UserManager.getUserId() ) )
      {
        int numOfDays = getSystemVariableService().getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();

        String celebrationId = getServiceAnniversaryService().getCelebrationIdByClaim( claimId, recipientId, numOfDays );
        if ( !StringUtil.isEmpty( celebrationId ) )
        {
          String url = getServiceAnniversaryService().getContributePageUrl( celebrationId, getUserService().getRosterUserIdByUserId( recipientId ).toString() );
          if ( !StringUtil.isEmpty( url ) )
          {
            response.sendRedirect( url );
            return null;
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      logger.error( "Error While reading client state : " + e.getMessage() );
      e.printStackTrace();
    }
    catch( DataException e )
    {
      logger.error( "Data Exception while getting SA  page url claim id: " + claimId + " " + e.getMessage() );
    }
    catch( Exception e )
    {
      logger.error( "Error while getting SA page url claim id: " + claimId + " " + e.getMessage() );
    }
    // if user not logged in
    if ( Objects.isNull( UserManager.getUserId() ) )
    {
      // redirect to the login page.
      response.sendRedirect( request.getContextPath() + "/login.do" );
      return null;
    }
    return mapping.findForward( "invalid" );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }

  private MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private FacebookService getFacebookService()
  {
    return (FacebookService)getService( FacebookService.BEAN_NAME );
  }

  private CelebrationService getCelebrationService()
  {
    return (CelebrationService)getService( CelebrationService.BEAN_NAME );
  }

  private Boolean isPublicRecognitionFacebookAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_FACEBOOK );
  }

  private Boolean isPublicRecognitionTwitterAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_TWITTER );
  }

  private Boolean isPublicRecognitionLinkedInAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_LINKED_IN );
  }

  private Boolean isPublicRecognitionChatterAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_CHATTER );
  }

  private Boolean getBooleanPropertyValue( String propertyName )
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( propertyName );
    return property != null ? new Boolean( property.getBooleanVal() ) : Boolean.FALSE;
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)getService( ServiceAnniversaryService.BEAN_NAME );
  }

}
