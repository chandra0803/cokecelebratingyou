/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/shopping/SpotlightOnlineShoppingAction.java,v $
 */

package com.biperf.core.ui.shopping;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.SsoLoginEnum;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.merchandise.MerchOrderBillCode;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.merchorder.MerchOrderBillCodeService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.SecurityUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.web.singlesignon.SingleSignOnException;
import com.biperf.web.singlesignon.SingleSignOnRequest;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * SpotlightOnlineShoppingAction.
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
 * <td>babu</td>
 * <td>Aug 16, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          21:05:22 babu Exp $
 */
public class SpotlightOnlineShoppingAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( SpotlightOnlineShoppingAction.class );

  /**
   * Prepare the display for ThanqOnline Shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws ServiceErrorException 
   */
  public ActionForward displayThanqOnline( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServiceErrorException
  {

    validatedUserAccess();

    ActionMessages errors = new ActionMessages();

    String isPax = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "isPax" );

    if ( isPax != null && isPax.equals( "true" ) && mapping.getPath().equals( "/merchLevelShopping" ) )
    {
      response.sendRedirect( "secureShopping.do" + "?" + request.getQueryString() );
      return null;
    }

    String giftCode = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "giftCode" );
    if ( giftCode == null )
    {
      throw new IllegalArgumentException( "required parameter Gift Code was missing" );
    }

    String pageName = null;
    String productSetId = null;
    String catalogId = null;

    if ( ClientStateUtils.getClientStateMap( request ) != null )
    {
      productSetId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "productSetId" );
      catalogId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "catalogId" );
      pageName = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "pageName" );
    }

    SingleSignOnRequest signOnRequest = getSingleSignOnRequest();

    MerchOrder merchOrder = getMerchOrderService().getMerchOrderByGiftCode( giftCode );
    if ( merchOrder == null )
    {
      throw new IllegalArgumentException( "required parameter Gift Code was invalid" );
    }
    Promotion promotion = null;
    if ( merchOrder.getClaim() != null )
    {
      promotion = merchOrder.getClaim().getPromotion();
    }
    if ( promotion != null && promotion.isAbstractRecognitionPromotion() )
    {
      signOnRequest.setParameter( "allowPointConversion", ( (AbstractRecognitionPromotion)promotion ).isApqConversion() + "" );
    }
    signOnRequest.setParameter( "giftcode", merchOrder.getFullGiftCode() );
    /* WIP# 25128 Start */
    MerchOrderBillCode merchOrderBillCode = getMerchOrderBillCodeService().getMerchOrderBillCodes( merchOrder.getId() );
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

    }
    /* WIP# 25128 End */
    // Broadleaf catalog expects the bill Profile parameters. If Billcodes are set to false, send
    // null values
    else
    {
      signOnRequest.setParameter( "billProf01", null );
      signOnRequest.setParameter( "billProf02", null );
      signOnRequest.setParameter( "billProf03", null );
      signOnRequest.setParameter( "billProf04", null );
      signOnRequest.setParameter( "billProf05", null );
      signOnRequest.setParameter( "billProf06", null );
      signOnRequest.setParameter( "billProf07", null );
      signOnRequest.setParameter( "billProf08", null );
      signOnRequest.setParameter( "billProf09", null );
      signOnRequest.setParameter( "billProf10", null );
    }
    if ( Objects.nonNull( UserManager.getUser() ) )
    {
      signOnRequest.setParameter( "loginId", UserManager.getUser().getUsername() );
    }
    Participant pax = merchOrder.getParticipant();
    if ( null != pax )
    {
      if ( promotion != null && promotion.isAbstractRecognitionPromotion() && ( (AbstractRecognitionPromotion)promotion ).isApqConversion() )
      {
        pax = enrollParticipantWithoutAccountInAwardBanQ( pax );
      }
      signOnRequest = loadParticipantAddressInfo( signOnRequest, pax );
    }
    AuthenticatedUser user = UserManager.getUser();
    HttpSession session = request.getSession();
    Long loginUserId = (Long)session.getAttribute( "loginUserId" );
    if ( user.isLaunched() && loginUserId != null )
    {
      PlateauRedemptionTracking plateauRedemptionTracking = new PlateauRedemptionTracking();
      plateauRedemptionTracking.setUserId( pax.getId() );
      plateauRedemptionTracking.setMerchOrderId( merchOrder.getId() );
      plateauRedemptionTracking.setCreatedBy( loginUserId );
      plateauRedemptionTracking.setDateCreated( Timestamp.from( Instant.now() ) );
      plateauRedemptionTracking.setVersion( new Long( 0 ) );
      getMerchOrderService().savePlateauRedemptionTracking( plateauRedemptionTracking );

      User loggedInUser = getUserService().getUserById( loginUserId );
      signOnRequest.setParameter( "actingUserId", loggedInUser.getId().toString() );
      signOnRequest.setParameter( "actingName", loggedInUser.getFirstName() + " " + loggedInUser.getLastName() );
    }

    signOnRequest = loadLevelLabels( signOnRequest, merchOrder.getPromoMerchProgramLevel().getPromoMerchCountry().getId() );

    // additional settings to go to the actual product for featured items
    if ( !StringUtils.isEmpty( pageName ) )
    {
      signOnRequest.setParameter( "pageName", pageName );
      signOnRequest.setParameter( "productSetId", productSetId );
      signOnRequest.setParameter( "catalogId", catalogId );
    }

    return performSignOn( signOnRequest, mapping, errors, request, response );
  }

  /**
   * Prepare the display for ThanqOnline Shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws ServiceErrorException 
   */
  public ActionForward shopOnline( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServiceErrorException
  {
    validateUserAccess();

    ActionMessages errors = new ActionMessages();
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      return redirectToSAAwardPage( mapping, request, response );
    }
    String giftCodeEncrypted = null;
    String queryString = request.getQueryString();
    String[] split = queryString.split( "gc=" );
    String paramValue = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "giftCodeEncrypted" );
    if ( paramValue != null && !StringUtils.isEmpty( paramValue ) )
    {
      giftCodeEncrypted = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "giftCodeEncrypted" );
    }
    else
    {
      // second substring is our encrypted code
      giftCodeEncrypted = split[1];
    }

    if ( giftCodeEncrypted == null )
    {
      throw new IllegalArgumentException( "required parameter Gift Code was missing" );
    }

    // We are not sure about the the gift code from the request Param is encrypted or not.
    // Client is already having some gift codes issues and the email are already sitting in the
    // users Inbox.
    // So based on the gift code length decide weather the request Param gc is encrypted one or not.
    String giftCode = null;
    if ( giftCodeEncrypted.length() > 20 )
    {
      String aesKey = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
      String aesInitVector = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();
      try
      {
        giftCode = SecurityUtils.decryptAES( giftCodeEncrypted, aesKey, aesInitVector );
      }
      catch( Exception e )
      {
        logger.error( "Unable to decrypted gift code: " + giftCodeEncrypted, e );
      }
    }
    else
    {
      giftCode = giftCodeEncrypted;
    }

    SingleSignOnRequest signOnRequest = getSingleSignOnRequest();

    MerchOrder merchOrder = getMerchOrderService().getMerchOrderByGiftCode( giftCode );
    if ( merchOrder == null )
    {
      throw new IllegalArgumentException( "required parameter Gift Code was invalid" );
    }

    Promotion promotion = null;
    if ( merchOrder.getClaim() != null )
    {
      promotion = merchOrder.getClaim().getPromotion();
    }
    if ( promotion != null && promotion.isAbstractRecognitionPromotion() )
    {
      signOnRequest.setParameter( "allowPointConversion", ( (AbstractRecognitionPromotion)promotion ).isApqConversion() + "" );
    }
    signOnRequest.setParameter( "giftcode", merchOrder.getFullGiftCode() );
    MerchOrderBillCode merchOrderBillCode = getMerchOrderBillCodeService().getMerchOrderBillCodes( merchOrder.getId() );
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

    }
    else
    {
      signOnRequest.setParameter( "billProf01", null );
      signOnRequest.setParameter( "billProf02", null );
      signOnRequest.setParameter( "billProf03", null );
      signOnRequest.setParameter( "billProf04", null );
      signOnRequest.setParameter( "billProf05", null );
      signOnRequest.setParameter( "billProf06", null );
      signOnRequest.setParameter( "billProf07", null );
      signOnRequest.setParameter( "billProf08", null );
      signOnRequest.setParameter( "billProf09", null );
      signOnRequest.setParameter( "billProf10", null );
    }

    if ( Objects.nonNull( UserManager.getUser() ) )
    {
      signOnRequest.setParameter( "loginId", UserManager.getUser().getUsername() );
    }
    /* WIP# 25128 Start */
    /*
     * MerchOrderBillCode merchOrderBillCode =
     * getMerchOrderBillCodeService().getMerchOrderBillCodes( giftCode );
     * signOnRequest.setParameter( "username", UserManager.getUser().getUserId().toString() );
     * signOnRequest.setParameter( "billcode1", merchOrderBillCode.getBillCode1());
     * signOnRequest.setParameter( "billcode2", merchOrderBillCode.getBillCode2());
     * signOnRequest.setParameter( "billcode3", merchOrderBillCode.getBillCode3());
     * signOnRequest.setParameter( "billcode4", merchOrderBillCode.getBillCode4());
     * signOnRequest.setParameter( "billcode5", merchOrderBillCode.getBillCode5());
     * signOnRequest.setParameter( "billcode6", merchOrderBillCode.getBillCode6());
     * signOnRequest.setParameter( "billcode7", merchOrderBillCode.getBillCode7());
     * signOnRequest.setParameter( "billcode8", merchOrderBillCode.getBillCode8());
     * signOnRequest.setParameter( "billcode9", merchOrderBillCode.getBillCode9());
     * signOnRequest.setParameter( "billcode10", merchOrderBillCode.getBillCode10());
     */
    /* WIP# 25128 End */
    Participant pax = merchOrder.getParticipant();
    if ( null != pax )
    {
      if ( UserManager.isUserLoggedIn() )
      {
        if ( !pax.getId().equals( UserManager.getUserId() ) )
        {
          throw new ServiceErrorException( "Access Denied" );
        }
      }
      if ( promotion != null && promotion.isAbstractRecognitionPromotion() && ( (AbstractRecognitionPromotion)promotion ).isApqConversion() )
      {
        pax = enrollParticipantWithoutAccountInAwardBanQ( pax );
      }
      signOnRequest = loadParticipantAddressInfo( signOnRequest, pax );
    }

    signOnRequest = loadLevelLabels( signOnRequest, merchOrder.getPromoMerchProgramLevel().getPromoMerchCountry().getId() );

    return performSignOn( signOnRequest, mapping, errors, request, response );

  }

  private ActionForward performSignOn( SingleSignOnRequest signOnRequest, ActionMapping mapping, ActionMessages errors, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      // perform SSO signoff to invalidate any previous SSO requests
      signOnRequest.signOff( request, response );

      // perform SSO signon
      signOnRequest.signOn( request, response );
    }
    catch( SingleSignOnException e )
    {
      logger.error( "SingleSignOnException for userId:" + UserManager.getUser().getUserId() + " giftcode:" + signOnRequest.getParameterMap().get( "giftcode" ) + " - ", e );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    return null;
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
       * billingName=""; String promotionCodes=""; Set userCharacteristics =
       * pax.getUserCharacteristics(); Iterator itr = userCharacteristics.iterator();
       * while(itr.hasNext()) { UserCharacteristic userChar= (UserCharacteristic) itr.next();
       * billingName = userChar.getUserCharacteristicType().getCharacteristicName();
       * if(billingName.equalsIgnoreCase( getSystemVariableService().getPropertyByName(
       * SystemVariableService.MERCHANDISE_BILLING_CODE_CHAR ).getStringVal() )) { if (
       * userChar.getCharacteristicValue() != null ){ signOnRequest.setParameter("promotionCodes",
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

  private SingleSignOnRequest getSingleSignOnRequest()
  {
    ILoggerImpl loggerImpl = new ILoggerImpl();
    SingleSignOnRequest signOnRequest = buildSSORequest( getMerchlinqSsoURL(), loggerImpl );
    return signOnRequest;
  }

  private SingleSignOnRequest buildSSORequest( String url, ILoggerImpl iLogger )
  {
    String proxyHost = Environment.getProxy();
    if ( logger.isInfoEnabled() )
    {
      logger.info( "Creating SSO for " + url );
    }
    if ( null != proxyHost )
    {
      return new SingleSignOnRequest( url, Environment.getProxy(), Environment.getProxyPort(), iLogger );
    }
    else
    {
      return new SingleSignOnRequest( url, iLogger );
    }
  }

  /**
   * Get the Merchlinq SSO URL for the current environment (dev, qa, preprod,
   * prod)
   * 
   * @return String
   */
  private String getMerchlinqSsoURL()
  {
    StringBuffer systemVariableName = new StringBuffer();
    systemVariableName.append( SystemVariableService.GOALQUEST_MERCHLINQ_SSO_URL_PREFIX );
    systemVariableName.append( "." );
    systemVariableName.append( Environment.getEnvironment() );
    String remoteURL = getSystemVariableService().getPropertyByName( systemVariableName.toString() ).getStringVal();
    return remoteURL;
  }

  /**
   * Get the SystemVariableService from the beanLocator.
   * 
   * @return SystemVariableService
   */
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

  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
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

  private ActionForward redirectToSAAwardPage( ActionMapping mapping, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    Long claimId = null;
    Long recipientId = null;
    try
    {
      claimId = Long.parseLong( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "claimId" ) );
      recipientId = Long.parseLong( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "recipientId" ) );

      if ( null != claimId && null != recipientId )
      {
        int numOfDays = getSystemVariableService().getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();

        String celebrationId = getServiceAnniversaryService().getCelebrationIdByClaim( claimId, recipientId, numOfDays );
        if ( !StringUtil.isEmpty( celebrationId ) )
        {
          String url = getServiceAnniversaryService().getGiftCodePageUrl( celebrationId );
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
    if ( UserManager.getUserId() == null )
    {
      String ssoUrl = getSSORedirectUrl();
      if ( StringUtil.isEmpty( ssoUrl ) )
      {
        // redirect to the login page.
        response.sendRedirect( request.getContextPath() + "/login.do" );
      }
      else
      {
        // redirect to the SSO login page.
        response.sendRedirect( ssoUrl );
      }
      return null;
    }
    return mapping.findForward( "invalid" );
  }

  private String getSSORedirectUrl()
  {
    String ssoUrl = "";
    if ( SsoLoginEnum.SSO.toString().equalsIgnoreCase( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_LOGIN_TYPE ).getStringVal() )
        || SsoLoginEnum.MIXED.toString().equalsIgnoreCase( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_LOGIN_TYPE ).getStringVal() ) )
    {
      ssoUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SSO_LOGIN_URL ).getStringVal();
    }
    return ssoUrl;
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

  /* WIP# 25128 Start */
  private MerchOrderBillCodeService getMerchOrderBillCodeService()
  {
    return (MerchOrderBillCodeService)getService( MerchOrderBillCodeService.BEAN_NAME );
  }
  /* WIP# 25128 End */

  /*
   * Delegates and Launched-as users are not allowed access to the Catalog
   */
  private void validateUserAccess() throws ServiceErrorException
  {
    if ( UserManager.isUserDelegateOrLaunchedAs() )
    {
      throw new ServiceErrorException( "Access Denied" );
    }
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private void validatedUserAccess() throws ServiceErrorException
  {
    if ( UserManager.getUser().isDelegate() )
    {
      throw new ServiceErrorException( "Access Denied" );
    }
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)getService( ServiceAnniversaryService.BEAN_NAME );
  }
}
