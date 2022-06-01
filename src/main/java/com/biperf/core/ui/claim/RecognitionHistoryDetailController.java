
package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.reports.ReportsUtils;
import com.biperf.core.ui.utils.CardUtilties;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.claim.ClaimRecipientValueObject;
import com.biperf.core.value.claim.RecognitionClaimValueObject;
import com.objectpartners.cms.domain.Content;

/**
 * RecognitionHistoryDetailController.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Tammy Cheng</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionHistoryDetailController extends BaseController
{
  private static final String RECEIVED = "received";
  private static final String SENT = "sent";

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Nomination History Detail page and the Recognition Detail History page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    // Get parameters.
    Long claimId = null;
    Long claimRecipientId = null;
    Long queryPromotionId = null;
    Long userId = null;
    String queryStartDate = null;
    String queryEndDate = null;
    String mode = null;

    // These are used by the reports
    RequestWrapper requestWrapper = new RequestWrapper( request );
    claimId = requestWrapper.getClaimId();
    claimRecipientId = requestWrapper.getClaimRecipientId();

    if ( claimId == null || claimRecipientId == null )
    {
      try
      {
        String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        queryPromotionId = (Long)clientStateMap.get( "queryPromotionId" );
        queryStartDate = (String)clientStateMap.get( "queryStartDate" );
        queryEndDate = (String)clientStateMap.get( "queryEndDate" );
        try
        {
          claimId = (Long)clientStateMap.get( "claimId" );
        }
        catch( ClassCastException cce )
        {
          claimId = new Long( (String)clientStateMap.get( "claimId" ) );
        }
        try
        {
          userId = (Long)clientStateMap.get( "userId" );
        }
        catch( ClassCastException cce )
        {
          String uid = (String)clientStateMap.get( "userId" );
          if ( uid != null && uid.length() > 0 )
          {
            userId = new Long( uid );
          }
        }
        try
        {
          claimRecipientId = (Long)clientStateMap.get( "claimRecipientId" );
        }
        catch( ClassCastException cce )
        {
          claimRecipientId = new Long( (String)clientStateMap.get( "claimRecipientId" ) );
        }

        mode = RequestUtils.getOptionalParamString( request, "mode" );
        if ( StringUtils.isEmpty( mode ) && clientStateMap != null )
        {
          mode = (String)clientStateMap.get( "mode" );
        }
        if ( StringUtils.isEmpty( mode ) && request.getAttribute( "recognitionHistoryForm" ) != null )
        {
          RecognitionHistoryForm form = (RecognitionHistoryForm)request.getAttribute( "recognitionHistoryForm" );
          mode = form.getMode();
        }
        // START-g3Redux
        if ( clientStateMap.get( "returnURL" ) != null )
        {
          request.setAttribute( "returnURL", (String)clientStateMap.get( "returnURL" ) );
        }

        // End-g3Redux

      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    // Get the claim.
    AbstractRecognitionClaim claim = getClaim( claimId );

    // Get the claim recipient.
    ClaimRecipient claimRecipient = getClaimRecipient( claim, claimRecipientId );
    if ( claimRecipient != null && claimRecipient.getApprovalStatusType() != null && claimRecipient.getApprovalStatusType().getCode().equalsIgnoreCase( "winner" ) )
    {
      request.setAttribute( "approvalStatus", Boolean.TRUE );
    }

    if ( claim.isRecognitionClaim() && ( (RecognitionPromotion)claim.getPromotion() ).isAwardActive() && ( (RecognitionPromotion)claim.getPromotion() ).getAwardType().isMerchandiseAwardType() )
    {
      // List activities = getActivityService()
      // .getMerchOrderActivitiesByClaimIdAndUserId( claim.getId(), userId );
      List activities = null;
      boolean flag = false;
      if ( claimRecipient.getRecipient() != null )
      {
        if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
          if ( recPromotion.isSelfRecognitionEnabled() )
          {
            if ( claimRecipient.getRecipient().getId().longValue() == claimRecipient.getClaim().getSubmitter().getId().longValue() )
            {
              flag = true;
            }
          }
        }

        if ( flag )
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId(), null );
        }
        else
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId() );
        }
      }
      if ( activities != null && !activities.isEmpty() )
      {
        MerchOrderActivity activity = (MerchOrderActivity)activities.get( 0 );
        if ( activity.getMerchOrder() != null )
        {
          MerchOrder merchOrder = activity.getMerchOrder();
          if ( RECEIVED.equalsIgnoreCase( mode ) && !merchOrder.isRedeemed() )
          {
            try
            {
              merchOrder = getMerchOrderService().updateOrderStatus( merchOrder );
            }
            catch( ServiceErrorException see )
            {
              /*
               * If one of the giftcode is invalid, continue updating other giftcodes, log that
               * giftcode If error is other than invalid giftcode, stop processing.
               */
              if ( see.getServiceErrors().size() != 1 || ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey() == null
                  || ! ( ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey().equals( ServiceErrorMessageKeys.RECOGNITION_OM_INVALID_ORDER_ERROR )
                      || ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey().equals( ServiceErrorMessageKeys.RECOGNITION_OM_PAX_SERVICE_ERROR )
                      || ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey().equals( ServiceErrorMessageKeys.RECOGNITION_OM_ORDER_DEFAULT_ERROR ) ) )
              {
                throw see;
              }
            }

          }
          request.setAttribute( "merchOrder", merchOrder );
          if ( RECEIVED.equalsIgnoreCase( mode ) && claimRecipient.getPromoMerchCountry() == null )
          {
            UserAddress userAddress = getUserService().getPrimaryUserAddress( claimRecipient.getRecipient().getId() );
            Country country = userAddress.getAddress().getCountry();

            String shoppingUrl = "";
            if ( merchOrder.getOrderStatus() == null )
            {
              shoppingUrl = getMerchOrderService().getOnlineShoppingUrl( (RecognitionPromotion)claim.getPromotion(), merchOrder.getId(), country, claimRecipient.getRecipient(), true );
            }
            if ( StringUtils.isNotBlank( shoppingUrl ) )
            {
              request.setAttribute( "shoppingUrl", shoppingUrl );
            }
          }
          else if ( RECEIVED.equalsIgnoreCase( mode ) && claimRecipient.getPromoMerchCountry().getCountry() != null )
          {
            String shoppingUrl = "";
            if ( merchOrder.getOrderStatus() == null )
            {
              shoppingUrl = getMerchOrderService().getOnlineShoppingUrl( (RecognitionPromotion)claim.getPromotion(),
                                                                         merchOrder.getId(),
                                                                         claimRecipient.getPromoMerchCountry().getCountry(),
                                                                         claimRecipient.getRecipient(),
                                                                         true );
            }
            if ( StringUtils.isNotBlank( shoppingUrl ) )
            {
              request.setAttribute( "shoppingUrl", shoppingUrl );
            }
          }
        }
      }
    }

    // Get the team members names if it's a team-based Nomination claim.
    if ( claim.isNominationClaim() )
    {
      NominationClaim nominationClaim = (NominationClaim)claim;
      // Bug#39568 Start
      // displayNomiCert -defaults to true
      request.setAttribute( "displayNomiCert", new Boolean( true ) );
      ClaimRecipient nomiClaimRecipient = null;
      if ( nominationClaim.getClaimRecipients().size() == 1 )
      {
        nomiClaimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();
        if ( nomiClaimRecipient.getApprovalStatusType().getCode().equals( "non_winner" ) )
        {
          request.setAttribute( "displayNomiCert", new Boolean( false ) );
        }
      }
      else
      {
        for ( Iterator iter = nominationClaim.getClaimRecipients().iterator(); iter.hasNext(); )
        {
          ClaimRecipient tempClaimRecipient = (ClaimRecipient)iter.next();
          if ( tempClaimRecipient.getApprovalStatusType().getCode().equals( "non_winner" ) )
          {
            request.setAttribute( "displayNomiCert", new Boolean( false ) );
            break;
          }
        }
      }
      // Bug#39568 End
      if ( nominationClaim.getClaimRecipients() != null && nominationClaim.getClaimRecipients().size() > 0 )
      {
        Set nomTeamMembers = nominationClaim.getClaimRecipients();
        Iterator nomTeamMembersIter = nomTeamMembers.iterator();
        StringBuffer teamMembers = new StringBuffer();
        while ( nomTeamMembersIter.hasNext() )
        {
          ClaimRecipient teamMember = (ClaimRecipient)nomTeamMembersIter.next();
          teamMembers.append( teamMember.getRecipient().getFirstName() + " " + teamMember.getRecipient().getLastName() );
          teamMembers.append( ", " );
        }
        String formattedTeamMembers = teamMembers.toString();
        formattedTeamMembers = formattedTeamMembers.substring( 0, formattedTeamMembers.lastIndexOf( ',' ) );
        if ( !StringUtils.isEmpty( formattedTeamMembers ) )
        {
          request.setAttribute( "nomTeamMembers", formattedTeamMembers );
        }
      }
    }

    // Get the card.
    Card card = claim.getCard();

    tileContext.putAttribute( "cardInsert", "" );
    request.setAttribute( "flashNeeded", new Boolean( false ) );
    request.setAttribute( "cardPresent", new Boolean( false ) );

    // Setup the environment.
    if ( card != null && card instanceof ECard )
    {
      ECard eCard = (ECard)card;

      request.setAttribute( "eCard", eCard );
      request.setAttribute( "flashNeeded", new Boolean( eCard.isFlashNeeded() ) );

      if ( eCard.isSwf() )
      {
        String flashRequestString = CardUtilties.getFlashRequestString( eCard, request );
        request.setAttribute( "flashRequestString", flashRequestString );
        tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewSWFCard.jsp" );
        request.setAttribute( "cardPresent", new Boolean( true ) );
      }
      else
      {
        String staticRequestString = CardUtilties.getStaticRequestString( eCard, request );
        request.setAttribute( "staticRequestString", staticRequestString );
        tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewNonSWFCard.jsp" );
        request.setAttribute( "cardPresent", new Boolean( true ) );
      }
    }
    else if ( claim.isRecognitionClaim() && ( (RecognitionClaim)claim ).getCertificateId() != null )
    {
      Content certificateContent = ReportsUtils.getCertificateContent( PromotionType.RECOGNITION, ( (RecognitionClaim)claim ).getCertificateId().toString() );
      if ( certificateContent != null )
      {
        String imageName = (String)certificateContent.getContentDataMap().get( "LARGE_IMAGE" );
        if ( StringUtils.isNotBlank( imageName ) )
        {
          String staticRequestString = CardUtilties.getStaticRequestString( imageName, request, true, false );
          request.setAttribute( "staticRequestString", staticRequestString );
          tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewNonSWFCard.jsp" );
          request.setAttribute( "cardPresent", new Boolean( true ) );
        }
      }
    }
    if ( claim.getOwnCardName() != null )
    {
      String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      String filePathAndName = siteUrlPrefix + "-cm/cm3dam/ecard" + '/' + claim.getOwnCardName();
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append( "<img src=\"" + filePathAndName + "\" usemap=\"#script\" WIDTH=\"360\" HEIGHT=\"360\" BORDER=\"0\">" );
      /*
       * stringBuffer.append( "<img src=\"" ); stringBuffer.append( filePathAndName );
       * stringBuffer.append( "\" " ); stringBuffer.append( "\" usemap=\"#script\" BORDER=\"0\">" );
       */
      request.setAttribute( "staticRequestString", stringBuffer.toString() + "" );
      tileContext.putAttribute( "cardInsert", "/claim/nomrec/viewNonSWFCard.jsp" );
      request.setAttribute( "cardPresent", new Boolean( true ) );
    }

    request.setAttribute( "mode", mode );
    request.setAttribute( "recognitionClaim", claim );
    request.setAttribute( "claimRecipient", claimRecipient );
    request.setAttribute( "submitter", claim.getSubmitter() );
    request.setAttribute( "promotionId", claim.getPromotion().getId() );
    request.setAttribute( "queryPromotionId", queryPromotionId );
    request.setAttribute( "queryStartDate", queryStartDate );
    request.setAttribute( "queryEndDate", queryEndDate );
    request.setAttribute( "userId", userId );
    request.setAttribute( "recognitionClaimValueObject", getRecognitionClam( claim, mode, claimRecipient ) );
    request.setAttribute( "claimRecipientValueObject", getClaimRecipientValueObject( claimRecipient ) );

    // these are for printerfriendly and cerificate version
    requestWrapper = new RequestWrapper( request );
    request.setAttribute( "isPrinterFriendly", new Boolean( requestWrapper.isPrinterFriendly() ) );
    request.setAttribute( "printerClaimId", claimId );
    request.setAttribute( "printerClaimRecipientId", claimRecipientId );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the specified claim.
   * 
   * @return the specified claim.
   */
  private AbstractRecognitionClaim getClaim( Long claimId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();

    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CARD ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ELEMENTS ) );

    AbstractRecognitionClaim claim = (AbstractRecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
    getClaimElementDomainObjects( claim );
    return claim;

  }

  public AbstractRecognitionClaim getClaimElementDomainObjects( AbstractRecognitionClaim claim )
  {
    List claimElementList = new ArrayList();
    for ( Iterator iter = claim.getClaimElements().iterator(); iter.hasNext(); )
    {
      ClaimElement claimElement = (ClaimElement)iter.next();
      if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isMultiSelectField() || claimElement.getClaimFormStepElement().getClaimFormElementType().isSelectField() )
      {
        List pickListItems = new ArrayList();
        // convert the comma delimited list of selected pickListItems to a list of strings
        Iterator pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
        while ( pickListCodes.hasNext() )
        {
          String code = (String)pickListCodes.next();
          pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ) );
        }
        claimElement.setPickListItems( pickListItems );
      }
    }
    return claim;
  }

  /**
   * Returns the specified claim recipient.
   * 
   * @return the specified claim recipient.
   */
  private ClaimRecipient getClaimRecipient( AbstractRecognitionClaim claim, Long claimRecipientId )
  {
    ClaimRecipient claimRecipient = null;
    // if there is only 1 claim Recipient then return it. NOTE: mostly used for reports because it
    // doesn't pass the recipient id but the participant id instead and we can't compare by that
    // because we have no participantId on a claimrecipient if the claim is team based
    if ( claim.getClaimRecipients().size() == 1 )
    {
      claimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();
    }
    else
    {
      for ( Iterator iter = claim.getClaimRecipients().iterator(); iter.hasNext(); )
      {
        ClaimRecipient tempClaimRecipient = (ClaimRecipient)iter.next();
        if ( tempClaimRecipient.getId().equals( claimRecipientId ) )
        {
          claimRecipient = tempClaimRecipient;
          break;
        }
      }
    }
    return claimRecipient;
  }

  private ClaimRecipientValueObject getClaimRecipientValueObject( ClaimRecipient claimRecipient )
  {
    ClaimRecipientValueObject claimValueObject = new ClaimRecipientValueObject();
    if ( !Objects.isNull( claimRecipient.getNode() ) )
    {
      claimValueObject.setNodeName( claimRecipient.getNode().getName() );
    }
    claimValueObject.setDisplayName( claimRecipient.getRecipientDisplayName() );
    claimValueObject.setPositionTypepicklistName( claimRecipient.getRecipient().getPositionTypePickList().getName() );
    claimValueObject.setDepartmentTypePickListName( claimRecipient.getRecipient().getDepartmentTypePickList().getName() );
    claimValueObject.setRecipientItemId( claimRecipient.getRecipient().getId() );
    claimValueObject.setClaimItemId( claimRecipient.getId() );
    claimValueObject.setCalculatorScore( claimRecipient.getCalculatorScore() );
    if ( !Objects.isNull( claimRecipient.getClaim().getPromotion().getAwardType() ) )
    {
      claimValueObject.setAwardTypeName( claimRecipient.getClaim().getPromotion().getAwardType().getName() );

    }
    claimValueObject.setAwardQuantity( claimRecipient.getAwardQuantity() );
    return claimValueObject;
  }

  private RecognitionClaimValueObject getRecognitionClam( AbstractRecognitionClaim claim, String mode, ClaimRecipient claimRecipient )
  {
    RecognitionClaimValueObject recognitionHistoryValueObject = new RecognitionClaimValueObject();

    recognitionHistoryValueObject.setRecognitionClaim( claim.isRecognitionClaim() );
    recognitionHistoryValueObject.setPromotionName( claim.getPromotion().getName() );
    recognitionHistoryValueObject.setEarnings( claim.getEarnings() );
    recognitionHistoryValueObject.setOpen( claim.isOpen() );
    recognitionHistoryValueObject.setPromotionTypeCode( claim.getPromotion().getPromotionType().getCode() );
    recognitionHistoryValueObject.setSubmissionDate( claim.getSubmissionDate() );
    recognitionHistoryValueObject.setSubmitterLastName( claim.getSubmitter().getLastName() );
    recognitionHistoryValueObject.setSubmitterFirstName( claim.getSubmitter().getFirstName() );
    recognitionHistoryValueObject.setPositionTypePickListName( claim.getSubmitter().getPositionTypePickList().getName() );
    recognitionHistoryValueObject.setDepartmentTypePickListName( claim.getSubmitter().getDepartmentTypePickList().getName() );
    recognitionHistoryValueObject.setSubmitterId( claim.getSubmitter().getId() );
    recognitionHistoryValueObject.setNominationPromotion( claim.getPromotion().isNominationPromotion() );
    recognitionHistoryValueObject.setRecognitionClaim( claim.getPromotion().isRecognitionPromotion() );
    recognitionHistoryValueObject.setCertificate( claim.getPromotion().getCertificate() );
    recognitionHistoryValueObject.setCopySender( claim.isCopySender() );
    recognitionHistoryValueObject.setPromotionId( claim.getPromotion().getId() );
    recognitionHistoryValueObject.setClaimId( claim.getId() );
    recognitionHistoryValueObject.setPromotionType( claim.getPromotion().getPromotionType().toString() );
    recognitionHistoryValueObject.setSubmitterComments( claim.getSubmitterComments() );
    recognitionHistoryValueObject.setBehavior( claim.getBehavior() );

    if ( !Objects.isNull( claim.getNode() ) )
    {
      recognitionHistoryValueObject.setRecognitionClaimNodeName( claim.getNode().getName() );
    }

    if ( !Objects.isNull( claim.isRecognitionClaim() ) && claim.isRecognitionClaim() )
    {
      recognitionHistoryValueObject.setCertificateId( ( (RecognitionClaim)claim ).getCertificateId() );
    }
    else
    {
      recognitionHistoryValueObject.setCertificateId( ( (NominationClaim)claim ).getCertificateId() );
    }

    if ( !Objects.isNull( claim.getPromotion().getScoreBy() ) )
    {
      recognitionHistoryValueObject.setScoreByGiver( claim.getPromotion().getScoreBy().isScoreByGiver() );
    }
    if ( !Objects.isNull( claim.getPromotion().getCalculator() ) )
    {
      recognitionHistoryValueObject.setDisplayScores( claim.getPromotion().getCalculator().isDisplayScores() );
    }
    if ( !Objects.isNull( claim.getBehavior() ) )
    {
      recognitionHistoryValueObject.setBehaviorName( claim.getBehavior().getName() );
    }
    if ( !Objects.isNull( claim.getClaimGroup() ) )
    {
      recognitionHistoryValueObject.setClamGroupAwardQuantity( claim.getClaimGroup().getAwardQuantity() );
    }

    if ( mode != null && mode.equalsIgnoreCase( RECEIVED ) )
    {
      if ( !Objects.isNull( claim.getSubmitter() ) )
      {
        recognitionHistoryValueObject.setPaxFirstName( claimRecipient.getRecipient().getFirstName() );
        recognitionHistoryValueObject.setPaxLastName( claimRecipient.getRecipient().getLastName() );
        recognitionHistoryValueObject.setPaxId( claimRecipient.getRecipient().getId() );
      }

    }
    else
    {
      if ( !Objects.isNull( claim.getSubmitter() ) )
      {
        recognitionHistoryValueObject.setPaxFirstName( claim.getSubmitter().getFirstName() );
        recognitionHistoryValueObject.setPaxLastName( claim.getSubmitter().getLastName() );
        recognitionHistoryValueObject.setPaxId( claim.getSubmitter().getId() );
      }

    }
    return recognitionHistoryValueObject;
  }
  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the claim service.
   * 
   * @return a reference to the claim service.
   */
  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adds behavior specific to this controller to an <code>HttpServletRequest</code> object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    /**
     * Keys to request attributes and parameters.
     */
    private static final String CLAIM_ID = "claimid";
    private static final String CLAIM_RECIPIENT_ID = "claimrecipientid";

    /**
     * Constructs a <code>RequestWrapper</code> object.
     * 
     * @param request
     */
    public RequestWrapper( HttpServletRequest request )
    {
      super( request );
    }

    public Long getClaimId()
    {
      Long claimId = null;

      String claimIdString = getParameter( CLAIM_ID );
      if ( claimIdString != null && claimIdString.length() > 0 )
      {
        claimId = new Long( claimIdString );
      }

      return claimId;
    }

    public Long getClaimRecipientId()
    {
      Long claimRecipientId = null;

      String claimRecipientIdString = getParameter( CLAIM_RECIPIENT_ID );
      if ( claimRecipientIdString != null && claimRecipientIdString.length() > 0 )
      {
        claimRecipientId = new Long( claimRecipientIdString );
      }

      return claimRecipientId;
    }

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Returns true if the application is displaying the printer friendly version of this page;
     * returns false otherwise.
     * 
     * @return true if the application is displaying the printer friendly version of this page;
     *         return false otherwise.
     */
    public boolean isPrinterFriendly()
    {
      return getServletPath().indexOf( "printerFriendlyLayout" ) != -1;
    }
  }

  /**
   * Get the ActivityService from the beanLocator.
   * 
   * @return ActivityService
   */
  private ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  /**
   * Get the MerchOrderService from the beanLocator.
   * 
   * @return MerchOrderService
   */
  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }

  protected static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
