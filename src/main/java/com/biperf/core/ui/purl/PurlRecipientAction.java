
package com.biperf.core.ui.purl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import org.json.JSONObject;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.merchandise.MerchOrderBillCode;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.purl.PurlActivityFeedCollectionView;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlContributorMedia;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.merchorder.MerchOrderBillCodeService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.shopping.ILoggerImpl;
import com.biperf.core.ui.utils.JsonResponse;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.RequestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ShoppingValueBean;
import com.biperf.web.singlesignon.SingleSignOnException;
import com.biperf.web.singlesignon.SingleSignOnRequest;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PurlRecipientAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PurlRecipientAction.class );

  public void showPurlInPublicRecognition( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String purlUrl = null;
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
      try
      {
        purlUrl = (String)clientStateMap.get( "purlUrl" );
      }
      catch( ClassCastException cce )
      {
        purlUrl = (String)clientStateMap.get( "purlUrl" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    response.sendRedirect( purlUrl + "?viewingId=" + UserManager.getUserId() );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlRecipientId = getPurlRecipientId( request );
    Long loggedinUserId = getLoggedInUserId( request );
    String userLang = null;
    if ( loggedinUserId == null && UserManager.isUserLoggedIn() )
    {
      loggedinUserId = UserManager.getUserId();
    }

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "PurlRecipientId : " + purlRecipientId );
      logger.debug( "LoggedinUserId : " + loggedinUserId );
    }

    PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( purlRecipientId );

    // coming from ThankYou forward and loggedIn user is not same as purl recipient
    if ( isThankYouForward( request ) )
    {
      if ( purlRecipient != null && !purlRecipient.getUser().getId().equals( UserManager.getUserId() ) )
      {
        return mapping.findForward( "invalid" );
      }
    }

    if ( purlRecipient != null )
    {
      if ( purlRecipient.getState() != null )
      {
        if ( PurlRecipientState.RECOGNITION.equals( purlRecipient.getState().getCode() ) )
        {
          purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.COMPLETE ) );
          purlRecipient = getPurlService().savePurlRecipient( purlRecipient );
        }
      }
    }

    List<PurlContributorMedia> photos = getPurlService().getPhotoUploads( purlRecipientId );
    List<PurlContributorMedia> videos = getPurlService().getVideoUploads( purlRecipientId );
    List<PurlContributorComment> comments = getPurlService().getComments( purlRecipientId );

    request.setAttribute( "purlRecipient", purlRecipient );
    request.setAttribute( "photos", photos );
    request.setAttribute( "videos", videos );
    request.setAttribute( "comments", comments );

    Participant participant = null;
    // Get the participant logged in
    if ( null != loggedinUserId )
    {
      participant = getParticipantService().getParticipantById( loggedinUserId );

      if ( participant != null && participant.getId().equals( purlRecipient.getUser().getId() ) )
      {
        request.setAttribute( "isRecipient", Boolean.TRUE );
        request.setAttribute( "isUserLoggedIn", Boolean.TRUE );
        request.setAttribute( "loggedinUserId", loggedinUserId );
      }

      if ( participant != null )
      {
        if ( participant.getLanguageType() == null )
        {
          participant.setLanguageType( LanguageType.lookup( getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() ) );
          userLang = participant.getLanguageType().getCode();
        }
        else
        {
          userLang = participant.getLanguageType().getCode();
        }
      }

      else
      {
        userLang = LanguageType.lookup( getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() ).getCode();
      }

    }

    boolean isOtherLang = false;
    for ( PurlContributorComment purlContributorComment : comments )
    {

      if ( purlContributorComment.getCommentsLanguageType() != null && !purlContributorComment.getCommentsLanguageType().getCode().equals( userLang ) )
      {
        isOtherLang = true;
      }
    }

    int daysToExpire = getSystemVariableService().getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    if ( purlRecipient != null && purlRecipient.getAwardDate() != null )
    {
      Date awardExpirationDate = new Date( purlRecipient.getAwardDate().getTime() + daysToExpire * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
      request.setAttribute( "awardExpirationDate", awardExpirationDate );
    }

    if ( isPurlFacebookAllowed() && purlRecipient.isAwardProcessed() )
    {
      request.setAttribute( "facebookUrl", getPurlService().getFacebookFeedDialogUrlForPurlPost( purlRecipient, true ) );
    }
    if ( isPurlTwitterAllowed() && purlRecipient.isAwardProcessed() )
    {
      request.setAttribute( "twitterUrl", getPurlService().createPostPurlToTwitterUrl( purlRecipient ) );
    }
    if ( isPurlLinkedInAllowed() && purlRecipient.isAwardProcessed() )
    {
      request.setAttribute( "linkedInUrl", getPurlService().createPostPurlToLinkedInUrl( request, purlRecipient ) );
    }
    if ( isPurlChatterAllowed() && purlRecipient.isAwardProcessed() )
    {
      request.setAttribute( "chatterUrl", getPurlService().createPostPurlToChatterUrl( request, purlRecipient ) );
    }
    if ( purlRecipient.isAwardProcessed() )
    {
      request.setAttribute( "awardProcess", Boolean.TRUE );
    }
    request.setAttribute( "displayRedeemLink", new Boolean( isShowRedeemLink( participant, purlRecipient ) ) );
    request.setAttribute( "displayThankyouLink", new Boolean( isShowThankyouLink( participant, purlRecipient ) ) );

    request.setAttribute( "openThankYouModal", Boolean.FALSE );
    if ( isOpenThankYouModal( request, participant, purlRecipient ) )
    {
      request.setAttribute( "openThankYouModal", Boolean.TRUE );
    }

    // Load image upload prefixes
    request.setAttribute( "finalPrefixURL", getFinalImagePrefixUrl() );
    request.setAttribute( "stagerPrefixURL", getStageImagePrefixUrl() );

    Map paramMap = new HashMap();
    if ( purlRecipient != null )
    {
      paramMap.put( "purlRecipientId", purlRecipient.getId() );
    }
    paramMap.put( "loggedinUserId", UserManager.getUserId() );
    request.setAttribute( "purlRecipientUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_RECIPIENT, paramMap ) );
    request.setAttribute( "userLoggedIn", UserManager.isUserLoggedIn() ? Boolean.TRUE : Boolean.FALSE );

    request.setAttribute( "siteUrl", getSiteUrl() );

    if ( !UserManager.isUserLoggedIn() )
    {
      // Bug 50810 - Reload the same page when change language is clicked and user is not logged in
      Map<String, Object> purlContributionParamMap = new HashMap<String, Object>();
      if ( purlRecipient != null )
      {
        purlContributionParamMap.put( "purlRecipientId", purlRecipient.getId() );
        purlContributionParamMap.put( "loggedinUserId", loggedinUserId );
      }
      request.setAttribute( "changeLanguageForwardUrl", ClientStateUtils.generateEncodedLink( "", PageConstants.ALERT_DETAIL_PURL_RECIPIENT, purlContributionParamMap ) );
    }
    request.setAttribute( "isOtherlocale", Boolean.FALSE );
    boolean machineLanguageAllowTranslation = getSystemVariableService().getPropertyByName( SystemVariableService.MACHINE_LANGUAGE_ALLOW_TRANSLATION ).getBooleanVal();

    if ( isOtherLang && machineLanguageAllowTranslation )
    {
      request.setAttribute( "isOtherlocale", Boolean.TRUE );
      request.setAttribute( "translateClientState",
                            "clientState=" + URLEncoder.encode( RequestUtils.getRequiredParamString( request, "clientState" ), "UTF-8" ) + "&cryptoPass="
                                + RequestUtils.getOptionalParamString( request, "cryptoPass" ) );
    }

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private Boolean isShowRedeemLink( Participant participant, PurlRecipient purlRecipient )
  {
    boolean isRecipientViewing = false;
    if ( null == participant )
    {
      isRecipientViewing = true;
    }
    else if ( purlRecipient != null && participant != null && participant.getId().equals( purlRecipient.getUser().getId() ) )
    {
      isRecipientViewing = true;
    }

    if ( purlRecipient != null )
    {
      return purlRecipient.isAwardProcessed() && purlRecipient.isAwardEligible() && isRecipientViewing;
    }
    else
    {
      return false;
    }
  }

  private Boolean isShowThankyouLink( Participant participant, PurlRecipient purlRecipient )
  {
    boolean isRecipientViewing = false;
    if ( null == participant )
    {
      isRecipientViewing = true;
    }
    else if ( purlRecipient != null && participant != null && participant.getId().equals( purlRecipient.getUser().getId() ) )
    {
      isRecipientViewing = true;
    }

    if ( purlRecipient != null )
    {
      return purlRecipient.isAwardProcessed() && isRecipientViewing;
    }
    else
    {
      return false;
    }
  }

  private Boolean isOpenThankYouModal( HttpServletRequest request, Participant participant, PurlRecipient purlRecipient ) throws Exception
  {
    boolean isRecipientViewing = null == participant || participant.getId().equals( purlRecipient.getUser().getId() );
    String purlShowThankYou = getPurlShowThankYou( request );

    return isRecipientViewing && null != purlShowThankYou && "true".equals( purlShowThankYou );
  }

  private Boolean isPurlFacebookAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PURL_ALLOW_FACEBOOK );
  }

  private Boolean isPurlTwitterAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PURL_ALLOW_TWITTER );
  }

  private Boolean isPurlLinkedInAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PURL_ALLOW_LINKED_IN );
  }

  private Boolean isPurlChatterAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PURL_ALLOW_CHATTER );
  }

  private Boolean getBooleanPropertyValue( String propertyName )
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( propertyName );
    return property != null ? new Boolean( property.getBooleanVal() ) : Boolean.FALSE;
  }

  public ActionForward invalid( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "invalid" );
  }

  public ActionForward expired( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "expired" );
  }

  public ActionForward viewComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlCommentId = getPurlCommentId( request );

    PurlContributorComment purlComment = getPurlService().getPurlContributorCommentById( purlCommentId );
    request.setAttribute( "purlComment", purlComment );

    return mapping.findForward( "view_comment" );
  }

  public ActionForward printComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlRecipientId = getPurlRecipientId( request );

    List<PurlContributorComment> purlComments = getPurlService().getComments( purlRecipientId );
    request.setAttribute( "purlComments", purlComments );

    return mapping.findForward( "print_comment" );
  }

  public ActionForward viewPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlMediaId = getPurlMediaId( request );

    PurlContributorMedia purlMedia = getPurlService().getPurlContributorMediaById( purlMediaId );
    request.setAttribute( "purlMedia", purlMedia );

    return mapping.findForward( "view_photo" );
  }

  public ActionForward printPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlRecipientId = getPurlRecipientId( request );

    List<PurlContributorMedia> purlMedias = getPurlService().getPhotoUploads( purlRecipientId );
    request.setAttribute( "purlMedias", purlMedias );

    return mapping.findForward( "print_photo" );
  }

  public ActionForward viewVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlMediaId = getPurlMediaId( request );

    PurlContributorMedia purlMedia = getPurlService().getPurlContributorMediaById( purlMediaId );
    request.setAttribute( "purlMedia", purlMedia );

    return mapping.findForward( "view_video" );
  }

  public ActionForward viewThankyou( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = UserManager.getUserId();
    if ( null == userId )
    {
      throw new BeaconRuntimeException( "Invalid Access" );
    }

    Long purlRecipientId = getPurlRecipientId( request );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "PurlRecipientId : " + purlRecipientId );
    }

    PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( purlRecipientId );

    request.setAttribute( "purlRecipient", purlRecipient );

    return mapping.findForward( "view_thankyou" );
  }

  public ActionForward sendThankyou( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = UserManager.getUserId();
    if ( null == userId )
    {
      throw new BeaconRuntimeException( "Invalid Access" );
    }

    String status = JsonResponse.STATUS_FAILED;
    PurlRecipientForm purlRecipientForm = (PurlRecipientForm)form;

    try
    {
      JSONObject thankYouJson = new JSONObject( purlRecipientForm.getData() );
      String clickLabel = "";
      String thankyouMessage = "";
            
      clickLabel = CmsResourceBundle.getCmsBundle().getString( "purl.common.CLICK_HERE_VIEW_PURL" );
      thankyouMessage = thankYouJson.getString( "thanksMessage" );
      Long purlRecipientId = thankYouJson.getLong( "purlRecipientId" );

      PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( purlRecipientId );
      Long promotionId = purlRecipient.getPromotion().getId();
      Promotion promotion = getPromotionService().getPromotionById( promotionId );
      String promotionName = promotion.getPromotionName();  
      //tccc customization start
      Participant participant = (Participant)purlRecipient.getUser();
      //tccc customization end

  		
      StringBuilder subject = new StringBuilder( "" );
      subject.append( CmsResourceBundle.getCmsBundle().getString( "purl.recipient.THANKYOU_SUBJECT_DEFAULT" ) );
      subject.append( " " );
      subject.append( purlRecipient.getUser().getFirstName() );
      subject.append( " " );
      subject.append( purlRecipient.getUser().getLastName() );

      String purlUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
      StringBuffer purlRecipientLink = new StringBuffer( purlUrl );
      purlRecipientLink.append( '/' );
      purlRecipientLink.append( purlRecipient.getUser().getFirstName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
      purlRecipientLink.append( '.' );
      purlRecipientLink.append( purlRecipient.getUser().getLastName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
      purlRecipientLink.append( '.' );
      purlRecipientLink.append( purlRecipient.getId() );
      // setting viewingid as a junk value so that contributors won't get redeem award and thank you
      // links
      // the best way to do is set the viewingid as each contributor id, but since it need more
      // refactoring with current implementation I am adding it as 1234.
      purlRecipientLink.append( "?viewingId=1234" );

      String viewPurlLink = "";
      viewPurlLink = "<a href='" + purlRecipientLink.toString().trim() + "'>" + clickLabel + "</a> ";

      StringBuilder thanksEmail = new StringBuilder( "" );
      thanksEmail.append( CmsResourceBundle.getCmsBundle().getString( "purl.recipient.THANK_YOU_FOR_CONTRIBUTION" ) );
      thanksEmail.append( " " );
      thanksEmail.append( promotionName );
      thanksEmail.append( " " );
      thanksEmail.append( CmsResourceBundle.getCmsBundle().getString( "purl.recipient.RECOGNITION_PURL" ) );
      thanksEmail.append( "<br/><br/>" );
      thanksEmail.append( thankyouMessage );
      thanksEmail.append( "<br/><br/>" );
      //tccc customization start
      if(participant.isAllowPurlContributionsToSeeOthers())
      {
        thanksEmail.append(viewPurlLink);
      }
      //tccc customization end

      // String comments = thankYouJson.getString( "staticText" ) + "\n" + thankYouJson.getString(
      // "thanksMessage" );

      getPurlService().sendThankyouToContributors( purlRecipientId, subject.toString(), thanksEmail.toString() );
      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( ServiceErrorException e )
    {
      logger.error( "error during send thank you" );
    }

    request.setAttribute( "status", status );
    return mapping.findForward( "confirm_thankyou_ajax_response" );
  }

  public ActionForward postToFacebook( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = UserManager.getUserId();
    if ( null == userId )
    {
      throw new BeaconRuntimeException( "Invalid Access" );
    }

    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlRecipientForm purlRecipientForm = (PurlRecipientForm)form;

      Long purlRecipientId = purlRecipientForm.getPurlRecipientId();

      getPurlService().postPurlUrlOnFacebook( userId, purlRecipientId );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during post facebook" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "confirm_thankyou_ajax_response" );
  }

  public ActionForward postToTwitter( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = UserManager.getUserId();
    if ( null == userId )
    {
      throw new BeaconRuntimeException( "Invalid Access" );
    }

    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlRecipientForm purlRecipientForm = (PurlRecipientForm)form;

      Long purlRecipientId = purlRecipientForm.getPurlRecipientId();

      getPurlService().postPurlUrlOnTwitter( userId, purlRecipientId );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during post twitter" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "confirm_thankyou_ajax_response" );
  }

  public ActionForward redeemAward( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlRecipientId = getPurlRecipientId( request );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "PurlRecipientId : " + purlRecipientId );
    }

    ActionMessages errors = new ActionMessages();
    ILoggerImpl loggerImpl = new ILoggerImpl();
    try
    {
      PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( purlRecipientId );
      Long userId = purlRecipient.getUser().getId();

      if ( !userId.equals( UserManager.getUserId() ) )
      {
        throw new BeaconRuntimeException( "Invalid Access" );
      }

      String promotionAwardType = purlRecipient.getPromotion().getAwardType().getCode();

      if ( PromotionAwardsType.POINTS.equals( promotionAwardType ) )
      {
        String shoppingType = getShoppingService().checkShoppingType( userId );

        if ( shoppingType.equals( ShoppingService.INTERNAL ) )
        {
          ShoppingValueBean shoppingValue = getShoppingService().setupInternalShopping( userId );
          String ssoUrl = shoppingValue.getRemoteURL();
          singleSignOn( ssoUrl, userId, purlRecipient, null, shoppingValue, loggerImpl, request, response, errors );
        }
        else if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
        {
          Map paramMap = new HashMap();
          paramMap.put( "externalSupplierId", getSupplierService().getSupplierByName( Supplier.BII ).getId() );
          String externalUrl = ClientStateUtils.generateEncodedLink( request.getContextPath(), "/externalSupplier.do?method=displayExternal", paramMap );
          response.sendRedirect( externalUrl );
        }
      }
      else
      // merch type promotion
      {
        String giftcode = getPurlService().getGiftcodeForRecipient( purlRecipient.getId() );
        if ( null != giftcode )
        {
          String ssoUrl = getMerchlinqSsoURL();
          singleSignOn( ssoUrl, userId, purlRecipient, giftcode, null, loggerImpl, request, response, errors );
        }
      }
    }
    catch( ServiceErrorException se )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return null;
  }

  private void singleSignOn( String ssoUrl,
                             Long userId,
                             PurlRecipient purlRecipient,
                             String giftcode,
                             ShoppingValueBean shoppingValue,
                             ILoggerImpl loggerImpl,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             ActionMessages errors )
  {
    SingleSignOnRequest signOnRequest = null;
    if ( Environment.isCtech() && shoppingValue != null )
    {
      // Apply Proxy settings in CTECH environment
      signOnRequest = new SingleSignOnRequest( ssoUrl, shoppingValue.getProxy(), shoppingValue.getProxyPort(), loggerImpl );
    }
    else
    {
      // For NON CTECH environments do not setup Proxy details
      signOnRequest = new SingleSignOnRequest( ssoUrl, loggerImpl );
    }

    if ( null != giftcode )
    {
      signOnRequest.setParameter( "allowPointConversion", purlRecipient.getPromotion().isApqConversion() + "" );
      signOnRequest.setParameter( "giftcode", giftcode );
      /* WIP# 25128 Start */
      MerchOrderBillCode merchOrderBillCode = getMerchOrderBillCodeService().getMerchOrderBillCodesByGiftCode( giftcode.substring( 0, 8 ) );
      if ( Objects.nonNull( merchOrderBillCode ) )
      {
        signOnRequest.setParameter( "billProf01", merchOrderBillCode.getBillCode1() );
        signOnRequest.setParameter( "billProf02", merchOrderBillCode.getBillCode2() );
        signOnRequest.setParameter( "billProf03", merchOrderBillCode.getBillCode3() );
        signOnRequest.setParameter( "billProf04", merchOrderBillCode.getBillCode4() );
        signOnRequest.setParameter( "billProf05", merchOrderBillCode.getBillCode5() );
        signOnRequest.setParameter( "billProf06", merchOrderBillCode.getBillCode6() );
        signOnRequest.setParameter( "billProf07", merchOrderBillCode.getBillCode7() );
        signOnRequest.setParameter( "billProf08", merchOrderBillCode.getBillCode8() );
        signOnRequest.setParameter( "billProf09", merchOrderBillCode.getBillCode9() );
        signOnRequest.setParameter( "billProf10", merchOrderBillCode.getBillCode10() );
        signOnRequest.setParameter( "loginId", UserManager.getUser().getUsername() );
      }
      /* WIP# 25128 End */
      if ( purlRecipient.getUser() instanceof Participant )
      {
        Participant pax = (Participant)purlRecipient.getUser();
        if ( null != pax )
        {
          if ( purlRecipient.getPromotion().isApqConversion() )
          {
            pax = enrollParticipantWithoutAccountInAwardBanQ( pax );
          }
          signOnRequest = loadParticipantAddressInfo( signOnRequest, pax );
        }

        signOnRequest = loadLevelLabels( signOnRequest, purlRecipient.getAwardLevel().getPromoMerchCountry().getId() );
      }
    }
    else
    {
      if ( shoppingValue.isCanShop() )
      {
        signOnRequest.setParameter( "program_id", shoppingValue.getProgramId() );
        signOnRequest.setParameter( "firstname", shoppingValue.getFirstName() );
        signOnRequest.setParameter( "lastname", shoppingValue.getLastName() );
        signOnRequest.setParameter( "account", shoppingValue.getAccount() );
        signOnRequest.setParameter( "locale", shoppingValue.getLanguagePreference() );
        signOnRequest.setParameter( "seamless_registration", "true" );
      }
    }

    try
    {
      // perform SSO signoff to invalidate any previous SSO requests
      signOnRequest.signOff( request, response );

      // perform SSO signon
      signOnRequest.signOn( request, response );
    }
    catch( SingleSignOnException e )
    {
      logger.error( "SingleSignOnException for userId:" + userId + " program_id:" + shoppingValue.getProgramId() + " program_password:" + shoppingValue.getProgramPassword() + " - ", e );

      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }
  }

  public ActionForward loadActivityFeed( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PurlRecipientForm purlRecipientForm = (PurlRecipientForm)form;    
    List<PurlContributorComment> comments = new ArrayList<PurlContributorComment>();
    
    JSONObject activityJson = new JSONObject( purlRecipientForm.getData() );
    Long purlRecipientId = activityJson.getLong( "purlRecipientId" );
    Boolean isCommentOrderDescending = activityJson.getBoolean( "commentOrderDescending" );
  //tccc customization start
    PurlRecipient purlRecipient = getPurlService().getPurlRecipientById(purlRecipientId);
    Participant user = (Participant) purlRecipient.getUser();
    
    if(user!=null && user.isAllowPurlContributionsToSeeOthers())
    {
    	comments = getPurlService().getComments( purlRecipientId, isCommentOrderDescending, 0, 0 );
    }
    if(user.getId().equals(UserManager.getUserId()))
    {
    	comments = getPurlService().getComments( purlRecipientId, isCommentOrderDescending, 0, 0 );
    }
    //tccc customization end
	 String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
	 PurlActivityFeedCollectionView bean = new PurlActivityFeedCollectionView( comments, siteUrl, getUserLanguageCode( request ) );
	 this.writeAsJsonToResponse( bean, response );

    return null;
  }

  private String getUserLanguageCode( HttpServletRequest request )
  {
    String languageCode = "";

    // first check to see if the user is logged in...
    if ( UserManager.isUserLoggedIn() )
    {
      // ... then get the language from the UserManager
      languageCode = UserManager.getUserLanguage();
    }
    else
    {
      // not logged in so use the browser's Accept-Language header
      List<String> languages = RequestUtil.getPreferredLanguageListFrom( request );
      if ( languages != null && !languages.isEmpty() )
      {
        languageCode = languages.get( 0 );
      }
    }

    return languageCode;
  }

  private SingleSignOnRequest loadLevelLabels( SingleSignOnRequest signOnRequest, Long promoMerchCountryId )
  {
    PromoMerchCountry promoMerchCountry = null;
    if ( promoMerchCountryId != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
      promoMerchCountry = getPromoMerchCountryService().getPromoMerchCountryByIdWithAssociations( promoMerchCountryId, associationRequestCollection );
    }

    if ( promoMerchCountry != null && promoMerchCountry.getLevels() != null )
    {
      for ( Iterator levelIter = promoMerchCountry.getLevels().iterator(); levelIter.hasNext(); )
      {
        PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelIter.next();
        signOnRequest.setParameter( "levelLabel_" + promoMerchProgramLevel.getLevelName(), CmsResourceBundle.getCmsBundle().getString( promoMerchProgramLevel.getCmAssetKey() + ".LEVEL_NAME" ) );
      }
    }

    return signOnRequest;
  }

  private SingleSignOnRequest loadParticipantAddressInfo( SingleSignOnRequest signOnRequest, Participant pax )
  {
    if ( null != pax )
    {
      AssociationRequestCollection userAssociations = new AssociationRequestCollection();
      userAssociations.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ALL ) );

      pax = getParticipantService().getParticipantByIdWithAssociations( pax.getId(), userAssociations );
      signOnRequest.setParameter( "firstName", pax.getFirstName() );
      signOnRequest.setParameter( "lastName", pax.getLastName() );

      // if ( pax.getPrimaryAddress()!=null && pax.getPrimaryAddress().getAddress()!=null)

      /* WIP# 25128 Start */
      // pass billing code
      /*
       * if ( pax.getUserCharacteristics() != null && getSystemVariableService().getPropertyByName(
       * SystemVariableService.MERCHANDISE_BILLING_CODE_CHAR ).getStringVal() != null ) { String
       * billingName = ""; String promotionCodes = ""; Set userCharacteristics =
       * pax.getUserCharacteristics(); Iterator itr = userCharacteristics.iterator(); while (
       * itr.hasNext() ) { UserCharacteristic userChar = (UserCharacteristic)itr.next(); billingName
       * = userChar.getUserCharacteristicType().getCharacteristicName(); if (
       * billingName.equalsIgnoreCase( getSystemVariableService().getPropertyByName(
       * SystemVariableService.MERCHANDISE_BILLING_CODE_CHAR ).getStringVal() ) ) { if (
       * userChar.getCharacteristicValue() != null ) { signOnRequest.setParameter( "promotionCodes",
       * userChar.getCharacteristicValue() ); } break; } } }
       */
      /* WIP# 25128 End */

      if ( pax.getPrimaryAddress() != null )
      {
        signOnRequest.setParameter( "accountNum", pax.getAwardBanqNumber() );
        signOnRequest.setParameter( "centraxId", pax.getCentraxId() );
        signOnRequest.setParameter( "omPaxId", pax.getCentraxId() );
        if ( null != pax.getLanguageType() )
        {
          signOnRequest.setParameter( "locale", pax.getLanguageType().getCode() );
        }

        Country country = null;
        if ( pax.getPrimaryAddress().getAddress() != null )
        {
          country = pax.getPrimaryAddress().getAddress().getCountry();
        }

        if ( country != null )
        {
          signOnRequest.setParameter( "campaignId", country.getCampaignNbr() );
          signOnRequest.setParameter( "campaignPassword", country.getCampaignPassword() );
        }

        UserAddress userAddress = pax.getPrimaryAddress();
        Address address = userAddress.getAddress();

        if ( address != null )
        {
          signOnRequest.setParameter( "addressLine1", address.getAddr1() );
          signOnRequest.setParameter( "addressLine2", address.getAddr2() );
          signOnRequest.setParameter( "addressLine3", address.getAddr3() );
          signOnRequest.setParameter( "city", address.getCity() );

          if ( address.getStateType() != null )
          {
            signOnRequest.setParameter( "stateCode", address.getStateType().getAbbr() );
          }

          signOnRequest.setParameter( "zipCode", address.getPostalCode() );

          if ( address.getCountry() != null )
          {
            signOnRequest.setParameter( "country", address.getCountry().getAwardbanqAbbrev() );
          }

          /*
           * AddressType businessAddressType = AddressType.lookup( AddressType.BUSINESS_TYPE ) ; //
           * we don't currently have this info, so lets just leave if for now // determine address
           * type signOnRequest.setParameter( "addressType", (userAddress.getAddressType().equals(
           * businessAddressType )?"B":"R") );
           */
          if ( null != pax.getPrimaryEmailAddress() )
          {
            signOnRequest.setParameter( "emailAddress", pax.getPrimaryEmailAddress().getEmailAddr() );
          }

          if ( null != pax.getPrimaryPhone() )
          {
            signOnRequest.setParameter( "daytimePhone", pax.getPrimaryPhone().getPhoneNbr() );
          }
        }
      }
    }
    return signOnRequest;
  }

  private String getMerchlinqSsoURL()
  {
    StringBuffer systemVariableName = new StringBuffer();
    systemVariableName.append( SystemVariableService.GOALQUEST_MERCHLINQ_SSO_URL_PREFIX );
    systemVariableName.append( "." );
    systemVariableName.append( Environment.getEnvironment() );
    return getSystemVariableService().getPropertyByName( systemVariableName.toString() ).getStringVal();
  }

  private Long getPurlRecipientId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "purlRecipientId" );
  }

  private String getPurlShowThankYou( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsString( request, "showThankYou" );
  }

  private Long getLoggedInUserId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "loggedinUserId" );
  }

  private Long getPurlCommentId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "purlCommentId" );
  }

  private Long getPurlMediaId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "purlMediaId" );
  }

  private String getReturnToUrl( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsString( request, "purlReturnToUrl" );
  }

  private boolean isThankYouForward( HttpServletRequest request )
  {
    String purlUrlForward = request.getParameter( "PurlUrlForward" );
    if ( !StringUtils.isEmpty( purlUrlForward ) )
    {
      return true;
    }
    return false;
  }

  private Long getClientStateParameterValueAsLong( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    Object paramValue = getClientStateParameterValue( request, parameter );
    Long parameterValue = null;
    try
    {
      parameterValue = (Long)paramValue;
    }
    catch( ClassCastException cce )
    {
      parameterValue = new Long( (String)paramValue );
    }
    return parameterValue;
  }

  private String getClientStateParameterValueAsString( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    Object paramValue = getClientStateParameterValue( request, parameter );
    String parameterValue = null;
    try
    {
      parameterValue = (String)paramValue;
    }
    catch( ClassCastException cce )
    {
      parameterValue = String.valueOf( paramValue );
    }
    return parameterValue;
  }

  private Object getClientStateParameterValue( HttpServletRequest request, String parameter ) throws InvalidClientStateException
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
    return clientStateMap.get( parameter );
  }

  private static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private ShoppingService getShoppingService()
  {
    return (ShoppingService)getService( ShoppingService.BEAN_NAME );
  }

  private SupplierService getSupplierService()
  {
    return (SupplierService)getService( SupplierService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * Get the PromoMerchCountryService From the bean factory through a locator.
   * 
   * @return PromoMerchCountryService
   */
  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  private Participant enrollParticipantWithoutAccountInAwardBanQ( Participant participant )
  {
    // ----------------------------
    // Awardbanq Enrollment check
    // ----------------------------
    if ( isAwardbanQMode() && StringUtils.isEmpty( participant.getAwardBanqNumber() ) )
    {
      // need to enroll into awardbanq
      try
      {
        participant = getAwardBanQService().enrollParticipantInAwardBanQ( participant.getId() );
      }
      catch( ServiceErrorException e )
      {
        // TODO Send an email to let admin know enrollment failed for some reason.
        log.error( "enrollment to bank failed in PURL redeemAward", e );
      }
    }
    return participant;
  }

  private boolean isAwardbanQMode()
  {
    PropertySetItem sysVar = getSystemVariableService().getPropertyByName( SystemVariableService.AWARDBANQ_MODE );
    return sysVar != null && AwardBanQServiceFactory.AWARDBANQ.equals( sysVar.getStringVal() );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory awardBanQServiceFactory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return awardBanQServiceFactory.getAwardBanQService();
  }

  public ActionForward saveReturnToUrlAndDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String returnToUrl = getReturnToUrl( request );

    // Set URL in session to return back
    setPurlReturnToUrlInSession( request, returnToUrl );

    return display( mapping, form, request, response );
  }

  public ActionForward returnToUrl( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    response.sendRedirect( RequestUtils.getBaseURI( request ) + getPurlReturnToUrlFromSession( request ) );
    return null;
  }

  private static final String SESSION_PURL_RETURN_TO_URL = "SESSION_PURL_RETURN_TO_URL";

  public void setPurlReturnToUrlInSession( HttpServletRequest request, String purlAccessUrl )
  {
    request.getSession().setAttribute( SESSION_PURL_RETURN_TO_URL, purlAccessUrl );
  }

  public String getPurlReturnToUrlFromSession( HttpServletRequest request )
  {
    return (String)request.getSession().getAttribute( SESSION_PURL_RETURN_TO_URL );
  }

  private String getFinalImagePrefixUrl()
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    return siteUrlPrefix + "-cm/cm3dam" + '/';
  }

  private String getStageImagePrefixUrl()
  {
    return "purlTempImage.do?imageUrl=";
  }

  private String getSiteUrl()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  /* WIP# 25128 Start */
  private MerchOrderBillCodeService getMerchOrderBillCodeService()
  {
    return (MerchOrderBillCodeService)getService( MerchOrderBillCodeService.BEAN_NAME );
  }
  /* WIP# 25128 End */

}
