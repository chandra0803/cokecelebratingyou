/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/shopping/ShoppingAction.java,v $
 */

package com.biperf.core.ui.shopping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.commlog.CommLogComment;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.enums.CommLogCategoryType;
import com.biperf.core.domain.enums.CommLogReasonType;
import com.biperf.core.domain.enums.CommLogSourceType;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.domain.enums.CommLogUrgencyType;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.merchandise.MerchOrderBillCode;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceNotAvailableException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.service.goalquest.GoalLevelService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.merchorder.MerchOrderBillCodeService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.api.mobile.shop.RedeemMenuView;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.RedeemConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ExternalSupplierValue;
import com.biperf.core.value.ShoppingValueBean;
import com.biperf.partnerservices.ws.catalog.v2.CatalogSSOWSRequest;
import com.biperf.partnerservices.ws.catalog.v2.CatalogSSOWSResponse;
import com.biperf.partnerservices.ws.catalog.v2.NameValue;
import com.biperf.web.singlesignon.SingleSignOnException;
import com.biperf.web.singlesignon.SingleSignOnRequest;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;

/**
 * ShoppingAction.
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
 * <td>robinsra</td>
 * <td>Sep 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ShoppingAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ShoppingAction.class );
  private static final int SUCCESS = 0;

  private Long getUserId( HttpServletRequest request )
  {
    String userIdStr = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    if ( !StringUtils.isEmpty( userIdStr ) )
    {
      return new Long( userIdStr );
    }
    return UserManager.getUserId();
  }

  private Long getProxyUserId( HttpServletRequest request )
  {
    String proxyUserIdStr = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "proxyUserId" );
    if ( !StringUtils.isEmpty( proxyUserIdStr ) )
    {
      return new Long( proxyUserIdStr );
    }
    return null;
  }

  private void createCommLog( User user, User proxyUser, String comments )
  {
    CommLog commLog = new CommLog();
    commLog.setCommLogSourceType( CommLogSourceType.lookup( CommLogSourceType.SYS_GEN ) );
    commLog.setCommLogCategoryType( CommLogCategoryType.lookup( CommLogCategoryType.PROMO_MAILING ) );// TODO
    commLog.setCommLogReasonType( CommLogReasonType.lookup( CommLogReasonType.EMAIL_OTHER ) );// TODO
    commLog.setCommLogStatusType( CommLogStatusType.lookup( CommLogStatusType.CLOSED_CODE ) );
    commLog.setCommLogUrgencyType( CommLogUrgencyType.lookup( CommLogUrgencyType.NORMAL_CODE ) );
    commLog.setUser( user );
    Set commentsHistorySet = new LinkedHashSet();
    CommLogComment commLogComment = new CommLogComment();
    commLogComment.setComments( comments );
    commLogComment.setCommentUser( proxyUser );
    commLogComment.setCommLog( commLog );
    commentsHistorySet.add( commLogComment );
    commLog.setComments( commentsHistorySet );
    getCommLogService().saveCommLog( commLog );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private CommLogService getCommLogService()
  {
    return (CommLogService)getService( CommLogService.BEAN_NAME );
  }

  /**
   * Prepare the display for Internal Shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayInternal( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ILoggerImpl loggerImpl = new ILoggerImpl();

    String mygoal = request.getParameter( "mygoal" );

    String page = null;
    String featuredItem = null;
    String productSetId = null;
    String catalogId = null;

    if ( ClientStateUtils.getClientStateMap( request ) != null )
    {
      featuredItem = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "featuredItem" );
      productSetId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "productSetId" );
      catalogId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "catalogId" );
      page = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "page" );
    }

    // ----------------------------------
    // Setup things for Internal Shopping
    // -----------------------------------
    try
    {
      ShoppingValueBean shoppingValue = getShoppingService().setupInternalShopping( UserManager.getUser().getUserId() );

      // ----------------------------
      // Create SingleSignOnRequest
      // ----------------------------
      SingleSignOnRequest signOnRequest = buildSSORequest( shoppingValue.getRemoteURL(), loggerImpl );
      if ( shoppingValue.isCanShop() )
      {
        // -----------
        // Shop Mode
        // ----------
        signOnRequest.setParameter( "program_id", shoppingValue.getProgramId() );
        signOnRequest.setParameter( "firstname", shoppingValue.getFirstName() );
        signOnRequest.setParameter( "lastname", shoppingValue.getLastName() );
        signOnRequest.setParameter( "account", shoppingValue.getAccount() );
        signOnRequest.setParameter( "locale", shoppingValue.getLanguagePreference() );
        signOnRequest.setParameter( "seamless_registration", "true" );
        // Bug 3076 - Add address to SSO
        Participant pax = getParticipantService().getParticipantById( UserManager.getUser().getUserId() );
        signOnRequest = loadParticipantAddressInfo( signOnRequest, pax, true );

        if ( mygoal != null && mygoal.equals( "true" ) )
        {
          // additional settings to go to the my goal page vs. the homepage
          signOnRequest.setParameter( "MODE", "GOAL_SELECTION" );
          signOnRequest.setPostLoginUrl( shoppingValue.getPostLoginURL() );
        }
        else if ( featuredItem != null && ( featuredItem.equals( "featuredItem" ) || featuredItem.equals( "greatDeals" ) ) && productSetId != null && catalogId != null )
        {
          // additional settings to go to the actual product for featured items
          signOnRequest.setParameter( "pageName", "productDetail" );
          signOnRequest.setParameter( "productSetId", productSetId );
          signOnRequest.setParameter( "catalogId", catalogId );
        }
        if ( page != null )
        {
          signOnRequest.setParameter( "pageName", page );
        }
      }
      else
      {
        // -------------
        // Browse Mode
        // -------------
        // Cannot shop - Browse Only mode
        // Only set the program password
        signOnRequest.setParameter( "program_password", shoppingValue.getProgramPassword() );
      }

      // -----------------------
      // Sign on to Awardslinq
      // -----------------------
      try
      {
        signOnRequest.signOn( request, response );
      }
      catch( SingleSignOnException e )
      {
        logger.error( "SingleSignOnException for userId:" + UserManager.getUser().getUserId() + " program_id:" + shoppingValue.getProgramId() + " program_password:"
            + shoppingValue.getProgramPassword() + " - ", e );

        // TODO - make new generic error
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
      }
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      // PaxCloseOut: (Inactive Pax is to be routed to AwardlinQ and shut out of this site)
      SecurityContext acegiContext = (SecurityContext)request.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
      AuthenticatedUser authUser = (AuthenticatedUser)acegiContext.getAuthentication().getPrincipal();
      if ( authUser.isPaxInactive() )
      {
        acegiContext.setAuthentication( null );
        UserManager.removeUser();
        request.getSession().invalidate();
      }
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    // PaxCloseOut: (Inactive Pax is to be routed to AwardlinQ and shut out of this site)
    SecurityContext acegiContext = (SecurityContext)request.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
    AuthenticatedUser authUser = (AuthenticatedUser)acegiContext.getAuthentication().getPrincipal();
    if ( authUser.isPaxInactive() )
    {
      acegiContext.setAuthentication( null );
      UserManager.removeUser();
      request.getSession().invalidate();
    }

    // If successful - control would have been passed to
    // awardslinq in the 'signon' command, so do not go anywhere.
    return null;
  }

  public ActionForward checkRedeem( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    writeAsJsonToResponse( new RedeemMenuView(), response );
    return null;
  }

  public ActionForward displayExperience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Map<String, Object> reqPayLoadMap = new HashMap<String, Object>();
    try
    {

      ShoppingValueBean shoppingValue = getShoppingService().setupInternalShopping( UserManager.getUser().getUserId() );

      // ----------------------------
      // Create Travel Param Request
      // ----------------------------

      if ( shoppingValue.isCanShop() )
      {
        Participant participant = getParticipantService().getParticipantById( UserManager.getUser().getUserId() );
        reqPayLoadMap.put( RedeemConstants.PROGRAM_ID_TEXT, shoppingValue.getProgramId() );
        reqPayLoadMap = loadParticipantAddressInfo( reqPayLoadMap, participant );

        try
        {
          byte[] postDataBytes = buildReqPayLoad( reqPayLoadMap );

          String redirectUrl = buildExperienceRedirectUrl( postDataBytes );

          // PaxCloseOut: (Inactive Pax is to be routed to AwardlinQ and shut out of this site)
          SecurityContext acegiContext = (SecurityContext)request.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
          AuthenticatedUser authUser = (AuthenticatedUser)acegiContext.getAuthentication().getPrincipal();
          if ( authUser.isPaxInactive() )
          {
            acegiContext.setAuthentication( null );
            UserManager.removeUser();
            request.getSession().invalidate();
          }
          if ( !Objects.isNull( redirectUrl ) )
          {
            response.sendRedirect( redirectUrl );
          }
          else
          {
            logger.error( " Redirection to Experience MarketPlace failed : URL empty " + HttpStatus.NOT_FOUND );
            throw new ServiceErrorException( " Redirection to Experience MarketPlace failed : URL empty " + HttpStatus.NOT_FOUND );
          }

        }
        catch( Exception e )
        {
          logger.error( "Exception from venture for userId: " + UserManager.getUser().getUserId() + " program_id:" + shoppingValue.getProgramId() + " - ", e );

        }
      }
    }
    catch( Exception e )
    {
      logger.error( "Bad Request to Venture Out on clicking Experiences. Status code : " + HttpStatus.BAD_REQUEST );
    }

    return null;
  }

  private String getExperienceVariableName()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append( SystemVariableService.EXPERIENCE_SYSTEM_VARIABLE );
    strBuilder.append( "." );
    strBuilder.append( Environment.getEnvironment() );
    return strBuilder.toString();

  }

  private Map<String, Object> loadParticipantAddressInfo( Map<String, Object> reqPayLoadMap, Participant participant )
  {
    if ( Objects.nonNull( participant ) )
    {
      AssociationRequestCollection userAssociations = new AssociationRequestCollection();
      userAssociations.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ALL ) );

      participant = getParticipantService().getParticipantByIdWithAssociations( participant.getId(), userAssociations );
      reqPayLoadMap.put( RedeemConstants.FIRST_NAME, participant.getFirstName() );
      reqPayLoadMap.put( RedeemConstants.LAST_NAME, participant.getLastName() );

      if ( participant.getPrimaryAddress() != null )
      {
        reqPayLoadMap.put( RedeemConstants.ACCOUNT_NUMBER, participant.getAwardBanqNumber() );
        if ( Objects.nonNull( participant.getLanguageType() ) )
        {
          reqPayLoadMap.put( RedeemConstants.USER_LOCALE, participant.getLanguageType().getCode() );
        }
        else
        {
          reqPayLoadMap.put( RedeemConstants.USER_LOCALE, getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() );
        }
        reqPayLoadMap.put( RedeemConstants.MEDIA_LABEL,
                           getCMAssetService().getString( RedeemConstants.MEDIA_ASSET, RedeemConstants.MEDIA_LABEL_KEY, CmsUtil.getLocale( reqPayLoadMap.get( "user_locale" ).toString() ), true ) );

        Country country = null;
        if ( Objects.nonNull( participant.getPrimaryAddress().getAddress() ) )
        {
          country = participant.getPrimaryAddress().getAddress().getCountry();
        }

        UserAddress userAddress = participant.getPrimaryAddress();
        Address address = userAddress.getAddress();

        if ( Objects.nonNull( address ) )
        {
          reqPayLoadMap.put( RedeemConstants.ADDRESS1, address.getAddr1() );
          reqPayLoadMap.put( RedeemConstants.ADDRESS2, address.getAddr2() );
          reqPayLoadMap.put( RedeemConstants.ADDRESS3, address.getAddr3() );
          reqPayLoadMap.put( RedeemConstants.CITY, address.getCity() );

          if ( Objects.nonNull( address.getStateType() ) )
          {
            reqPayLoadMap.put( RedeemConstants.STATE, address.getStateType().getAbbr() );
          }

          reqPayLoadMap.put( RedeemConstants.ZIP, address.getPostalCode() );

          if ( Objects.nonNull( address.getCountry() ) )
          {
            reqPayLoadMap.put( RedeemConstants.COUNTRY, address.getCountry().getAwardbanqAbbrev() );
          }

          if ( Objects.nonNull( participant.getPrimaryEmailAddress() ) )
          {
            reqPayLoadMap.put( RedeemConstants.EMAIL, participant.getPrimaryEmailAddress().getEmailAddr() );
          }

          if ( Objects.nonNull( participant.getPrimaryPhone() ) )
          {
            reqPayLoadMap.put( RedeemConstants.DAYTIME_PHONE, participant.getPrimaryPhone().getPhoneNbr() );
          }
          reqPayLoadMap.put( RedeemConstants.PRODUCT_TYPE_TEXT, RedeemConstants.PRODUCT_TYPE );
          reqPayLoadMap.put( RedeemConstants.ACTION_TYPE_TEXT, RedeemConstants.ACTION_TYPE );
        }
      }
    }
    return reqPayLoadMap;
  }

  protected String convertMapToParam( Map<String, Object> omSSOParamsMap )
  {
    StringBuilder stringBuilder = new StringBuilder();

    omSSOParamsMap.forEach( ( key, value ) ->
    {
      if ( Objects.nonNull( value ) )
      {
        if ( stringBuilder.length() > 0 )
        {
          stringBuilder.append( "&" );
        }

        stringBuilder.append( key != null ? getEncodedString( key ) : "" );
        stringBuilder.append( "=" );
        stringBuilder.append( value != null ? getEncodedString( value.toString() ) : "" );
      }
    } );

    return stringBuilder.toString();

  }

  private String getEncodedString( String input )
  {
    if ( StringUtils.isEmpty( input ) )
    {
      return input;
    }

    try
    {
      String encodedValue = URLEncoder.encode( input, "UTF-8" );
      String modifiedEncodedValue = null;
      if ( input.indexOf( " " ) != -1 )
      {
        modifiedEncodedValue = encodedValue.replace( "+", "%20" );
      }

      if ( null != modifiedEncodedValue )
      {
        return modifiedEncodedValue;
      }

      else
      {
        return encodedValue;
      }

    }
    catch( UnsupportedEncodingException e )
    {
      // do nothing
    }
    return input;
  }

  /**
   * Prepare the display for external supplier shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayExternal( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String page = request.getParameter( "page" );

    // if request comes from multi supplier page.
    String externalSupplierId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "externalSupplierId" );

    Long userId = getUserId( request );
    Long proxyUserId = getProxyUserId( request );
    if ( null != proxyUserId )
    {
      User user = getUserService().getUserById( userId );
      User proxyUser = getUserService().getUserById( proxyUserId );
      String comment = proxyUser.getNameLFMWithComma() + " redeeming award for " + user.getNameLFMWithComma();
      createCommLog( user, proxyUser, comment );
    }

    if ( externalSupplierId == null )
    {
      UserAddress userAddress = getUserService().getPrimaryUserAddress( UserManager.getUserId() );
      if ( userAddress != null )
      {
        Address address = userAddress.getAddress();
        if ( address != null )
        {
          if ( address.getCountry() != null && address.getCountry().getCountrySuppliers() != null )
          {
            Set suppliers = address.getCountry().getCountrySuppliers();
            Iterator iterator = suppliers.iterator();
            while ( iterator.hasNext() )
            {
              CountrySupplier countrySupplier = (CountrySupplier)iterator.next();
              if ( countrySupplier != null && countrySupplier.getSupplier() != null )
              {
                externalSupplierId = String.valueOf( countrySupplier.getSupplier().getId() );
              }
            }
          }
        }
      }
    }

    // ----------------------------------
    // Setup things for External Shopping
    // -----------------------------------
    try
    {
      ExternalSupplierValue externalSupplierValue = getShoppingService().setupExternalSupplier( userId, externalSupplierId );
      Boolean allowPartnerSSO = getSupplierService().getSupplierById( new Long( externalSupplierId ) ).getAllowPartnerSso();

      // If partner sso enabled for external suppliers
      if ( allowPartnerSSO )
      {
        return displayExternalPSV2( mapping, form, request, response, externalSupplierValue );
      }
      else
      {
        // transfer to form
        displayExternalLegacy( mapping, form, request, response, externalSupplierValue );
      }
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      if ( UserManager.getUser().isPaxInactive() )
      {
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication( null );
        UserManager.removeUser();
        request.getSession().invalidate();
      }
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }
    if ( UserManager.getUser().isPaxInactive() )
    {
      SecurityContext sc = SecurityContextHolder.getContext();
      sc.setAuthentication( null );
      UserManager.removeUser();
      request.getSession().invalidate();
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );

  }

  public void displayExternalLegacy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ExternalSupplierValue externalSupplierValue )
  {
    String page = request.getParameter( "page" );
    ExternalSupplierForm externalSupplierForm = (ExternalSupplierForm)form;
    externalSupplierForm.setBankAccountSystemNumber( externalSupplierValue.getBankAccountSystemNumber() );
    externalSupplierForm.setCampaignID( externalSupplierValue.getCampaignID() );
    externalSupplierForm.setCountryCode( externalSupplierValue.getCountryCode() );
    externalSupplierForm.setLanguageCode( externalSupplierValue.getLanguageCode() );
    if ( Objects.nonNull( page ) && page.equals( "account" ) )
    {
      externalSupplierForm.setActionURL( externalSupplierValue.getStatementActionURL() );
      externalSupplierForm.setTargetPageId( externalSupplierValue.getStatementTargetPageId() );
    }
    else
    {
      externalSupplierForm.setActionURL( externalSupplierValue.getActionURL() );
      externalSupplierForm.setTargetPageId( externalSupplierValue.getTargetPageId() );
    }
    externalSupplierForm.setEncryptionType( "0" );
    externalSupplierForm.setCharset( "iso-8859-1" );
    externalSupplierForm.setErrorPage( "" );
    externalSupplierForm.setAccessDeniedPage( "" );
  }

  public ActionForward displayExternalPSV2( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ExternalSupplierValue externalSupplierValue )
  {

    ActionMessages errors = new ActionMessages();

    CatalogSSOWSResponse ssoResponse = null;
    ExternalSupplierForm externalSupplierForm;
    if ( Objects.nonNull( form ) )
    {
      externalSupplierForm = (ExternalSupplierForm)form;
    }
    else
    {
      externalSupplierForm = new ExternalSupplierForm();
    }
    // ----------------------------------
    // Setup things for External Shopping
    // -----------------------------------
    try
    {

      externalSupplierForm.setCountryCode( externalSupplierValue.getCountryCode() );
      externalSupplierForm.setLanguageCode( externalSupplierValue.getLanguageCode() );
      externalSupplierForm.setEncryptionType( "0" );
      externalSupplierForm.setCharset( "iso-8859-1" );
      externalSupplierForm.setErrorPage( "" );
      externalSupplierForm.setAccessDeniedPage( "" );

      String customerId = getSystemVariableService().getPropertyByName( SystemVariableService.PARTNER_SERVICES_CATALOG_SSO_CUSTOMER_ID ).getStringVal();
      String catalogType = getSystemVariableService().getPropertyByName( SystemVariableService.PARTNER_SERVICES_CATALOG_SSO_CATALOG_TYPE ).getStringVal();
      UserAddress userAddress = getUserService().getPrimaryUserAddress( UserManager.getUser().getUserId() );
      Address address = userAddress.getAddress();
      String countryCode = StringUtils.upperCase( externalSupplierValue.getCountryCode() );
      Participant participant = (Participant)getUserService().getUserById( UserManager.getUser().getUserId() );

      CatalogWebServiceClient ssoClient = getCatalogWebServiceClient();

      boolean sso = false;

      if ( !externalSupplierValue.getBankAccountNumber().equals( "*" ) )
      {
        sso = true;
      }

      if ( sso )
      {
        UserPhone userPhone = getUserService().getPrimaryUserPhone( UserManager.getUser().getUserId() );
        UserEmailAddress userEmail = getUserService().getPrimaryUserEmailAddress( UserManager.getUser().getUserId() );
        String bankAccountNumber = externalSupplierValue.getBankAccountNumber();

        CatalogSSOWSRequest requestParam = populateCatalogRequestObject( participant, address, userPhone, userEmail );
        ssoResponse = ssoClient.doSSO( customerId, countryCode, catalogType, bankAccountNumber, requestParam );
      }
      else
      {
        ssoResponse = ssoClient.doSSOBrowseOnly( customerId, countryCode, catalogType );
      }
      validateResponseExternal( ssoResponse );

    }

    catch( DataException de )
    {
      logger.error( "Client Data exception ", de );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }
    catch( ServiceNotAvailableException ex )
    {
      logger.error( "Web service unavailable", ex );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }

    if ( !errors.isEmpty() || Objects.isNull( ssoResponse ) || Objects.isNull( ssoResponse.getRedirectURL() ) )
    {
      saveErrors( request, errors );
      if ( UserManager.getUser().isPaxInactive() )
      {
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication( null );
        UserManager.removeUser();
        request.getSession().invalidate();
      }
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    try
    {
      response.sendRedirect( ssoResponse.getRedirectURL() );
    }
    catch( IOException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if ( UserManager.getUser().isPaxInactive() )
    {
      SecurityContext sc = SecurityContextHolder.getContext();
      sc.setAuthentication( null );
      UserManager.removeUser();
      request.getSession().invalidate();
    }
    return null;
  }

  /**
   * Prepare the display for MerchLinq Shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayMerchLinq( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ILoggerImpl loggerImpl = new ILoggerImpl();
    String ssourl = getMerchlinqSsoURL();
    String giftCode = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "giftcode" );
    if ( giftCode == null )
    {
      throw new IllegalArgumentException( "required parameter Gift Code was missing" );
    }
    String productSetId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "productSetId" );
    String catalogId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "catalogId" );
    // CR - Convert to PerQs - START
    String promotionId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" );
    // CR - Convert to PerQs - END
    MerchOrder merchOrder = getMerchOrderService().getMerchOrderByGiftCode( giftCode );

    SingleSignOnRequest signOnRequest = null;

    signOnRequest = buildSSORequest( ssourl, loggerImpl );
    /* WIP# 25128 Start */
    MerchOrderBillCode merchOrderBillCode = getMerchOrderBillCodeService().getMerchOrderBillCodesByGiftCode( giftCode.substring( 0, 8 ) );

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

    signOnRequest.setParameter( "giftcode", merchOrder.getFullGiftCode() );
    signOnRequest.setParameter( "productSetId", productSetId );
    signOnRequest.setParameter( "catalogId", catalogId );

    // CR - Convert to PerQs - START
    Promotion promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
    if ( promotion.isGoalQuestPromotion() )
    {
      signOnRequest.setParameter( "allowPointConversion", ( (GoalQuestPromotion)promotion ).isApqConversion() + "" );
    }
    // Participant pax = getParticipantService().getParticipantById(
    // UserManager.getUser().getUserId() );
    Participant pax = merchOrder.getParticipant();
    if ( null != pax )
    {
      if ( promotion.isGoalQuestPromotion() && ( (GoalQuestPromotion)promotion ).isApqConversion() )
      {
        pax = enrollParticipantWithoutAccountInAwardBanQ( pax );
      }
      signOnRequest = loadParticipantAddressInfo( signOnRequest, pax, false );
    }
    // CR - Convert to PerQs - END
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

    try
    {
      // perform SSO signoff to invalidate any previous SSO requests
      signOnRequest.signOff( request, response );

      // perform SSO signon
      signOnRequest.signOn( request, response );
    }
    catch( SingleSignOnException e )
    {
      logger.error( "SingleSignOnException for userId:" + UserManager.getUser().getUserId() + " giftcode:" + giftCode + " productSetId:" + productSetId + " - ", e );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }
    return null;
  }

  /**
   * Prepare the display for ThanqOnline Shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayThankqOnline( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ILoggerImpl loggerImpl = new ILoggerImpl();
    String ssourl = getMerchlinqSsoURL();
    String giftCode = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "giftcode" );
    if ( giftCode == null )
    {
      throw new IllegalArgumentException( "required parameter Gift Code was missing" );
    }
    String productSetId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "productSetId" );
    String catalogId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "catalogId" );
    String conversionFlag = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "conversionFlag" );
    String accountNum = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "accountNum" );
    String centraxId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "centraxId" );
    String locale = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "locale" );
    String campaignId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "campaignId" );
    String campaignPassword = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "campaignPassword" );
    String promoMerchCountryId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promoMerchCountryId" );
    PromoMerchCountry promoMerchCountry = null;
    if ( promoMerchCountryId != null && promoMerchCountryId.length() > 0 )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
      promoMerchCountry = getPromoMerchCountryService().getPromoMerchCountryByIdWithAssociations( Long.valueOf( promoMerchCountryId ), associationRequestCollection );
    }
    SingleSignOnRequest signOnRequest = null;
    signOnRequest = buildSSORequest( ssourl, loggerImpl );
    signOnRequest.setParameter( "giftcode", giftCode );
    signOnRequest.setParameter( "productSetId", productSetId );
    signOnRequest.setParameter( "conversionFlag", conversionFlag );
    signOnRequest.setParameter( "accountNum", accountNum );
    signOnRequest.setParameter( "centraxId", centraxId );
    signOnRequest.setParameter( "locale", locale );
    signOnRequest.setParameter( "campaignId", campaignId );
    signOnRequest.setParameter( "campaignPassword", campaignPassword );
    if ( promoMerchCountry != null && promoMerchCountry.getLevels() != null )
    {
      for ( Iterator levelIter = promoMerchCountry.getLevels().iterator(); levelIter.hasNext(); )
      {
        PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelIter.next();
        signOnRequest.setParameter( "levelLabel_" + promoMerchProgramLevel.getLevelName(), CmsResourceBundle.getCmsBundle().getString( promoMerchProgramLevel.getCmAssetKey() + ".LEVEL_NAME" ) );
      }
    }
    try
    {
      signOnRequest.signOn( request, response );
    }
    catch( SingleSignOnException e )
    {
      logger.error( "SingleSignOnException for userId:" + UserManager.getUser().getUserId() + " giftcode:" + giftCode + " productSetId:" + productSetId + " - ", e );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }
    return null;
  }

  /**
   * Prepare the display for ThanqOnline Shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayChallengepointOnline( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ILoggerImpl loggerImpl = new ILoggerImpl();
    String ssourl = getMerchlinqSsoURL();
    String giftCode = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "giftcode" );
    MerchOrder merchOrder = getMerchOrderService().getMerchOrderByGiftCode( giftCode );
    Participant pax = merchOrder.getParticipant();
    if ( giftCode == null )
    {
      throw new IllegalArgumentException( "required parameter Gift Code was missing" );
    }

    String conversionFlag = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "conversionFlag" );
    String accountNum = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "accountNum" );
    String centraxId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "centraxId" );
    String locale = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "locale" );
    String campaignId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "campaignId" );
    String campaignPassword = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "campaignPassword" );
    String promotionId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );

    SingleSignOnRequest signOnRequest = null;

    signOnRequest = buildSSORequest( ssourl, loggerImpl );

    signOnRequest.setParameter( "giftcode", merchOrder.getFullGiftCode() );
    signOnRequest.setParameter( "conversionFlag", conversionFlag );
    signOnRequest.setParameter( "accountNum", accountNum );
    signOnRequest.setParameter( "centraxId", centraxId );
    signOnRequest.setParameter( "locale", locale );
    signOnRequest.setParameter( "campaignId", campaignId );
    signOnRequest.setParameter( "campaignPassword", campaignPassword );
    ChallengePointPromotion promotion = (ChallengePointPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), associationRequestCollection );

    if ( promotion.getGoalLevels() != null )
    {
      for ( Iterator<AbstractGoalLevel> levelIter = promotion.getGoalLevels().iterator(); levelIter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)levelIter.next();
        signOnRequest.setParameter( "levelLabel_LEVEL" + goalLevel.getSequenceNumber(),

                                    goalLevel.getGoalLevelName() );
      }
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
      plateauRedemptionTracking.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
      plateauRedemptionTracking.setVersion( new Long( 0 ) );
      getMerchOrderService().savePlateauRedemptionTracking( plateauRedemptionTracking );

      User loggedInUser = getUserService().getUserById( loginUserId );
      signOnRequest.setParameter( "actingUserId", loggedInUser.getId().toString() );
      signOnRequest.setParameter( "actingName", loggedInUser.getFirstName() + " " + loggedInUser.getLastName() );
    }

    logger.error( signOnRequest.getParameterMap().keySet() );
    try
    {
      signOnRequest.signOn( request, response );
    }
    catch( SingleSignOnException e )
    {
      logger.error( "SingleSignOnException for userId:" + UserManager.getUser().getUserId() + " giftcode:" + giftCode + " - ", e );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }
    return null;
  }

  private SingleSignOnRequest loadParticipantAddressInfo( SingleSignOnRequest signOnRequest, Participant pax, boolean isInternal )
  {
    if ( null != pax )
    {
      AssociationRequestCollection userAssociations = new AssociationRequestCollection();
      userAssociations.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ALL ) );

      pax = getParticipantService().getParticipantByIdWithAssociations( pax.getId(), userAssociations );
      signOnRequest.setParameter( "firstName", pax.getFirstName() );
      signOnRequest.setParameter( "lastName", pax.getLastName() );

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
          if ( isInternal )
          {
            signOnRequest.setParameter( "address1", address.getAddr1() );
            signOnRequest.setParameter( "address2", address.getAddr2() );
            signOnRequest.setParameter( "address3", address.getAddr3() );
            signOnRequest.setParameter( "city", address.getCity() );

            if ( address.getStateType() != null )
            {
              signOnRequest.setParameter( "state", address.getStateType().getAbbr() );
            }

            signOnRequest.setParameter( "zip", address.getPostalCode() );

            if ( address.getCountry() != null )
            {
              signOnRequest.setParameter( "country_code", address.getCountry().getAwardbanqAbbrev() );
            }

            if ( null != pax.getPrimaryEmailAddress() )
            {
              signOnRequest.setParameter( "email_address", pax.getPrimaryEmailAddress().getEmailAddr() );
            }
          }
          else
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

            if ( null != pax.getPrimaryEmailAddress() )
            {
              signOnRequest.setParameter( "emailAddress", pax.getPrimaryEmailAddress().getEmailAddr() );
            }
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

  /**
     * Get the Merchlinq SSO URL for the current environment (dev, qa, preprod, prod)
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

  private SingleSignOnRequest buildSSORequest( String url, ILoggerImpl iLogger )
  {
    if ( UserManager.isUserDelegateOrLaunchedAs() )
    {
      Long originalUserId = UserManager.getOriginalUserId();
      if ( !getRoleService().getUserRoleBypassingUserIdAndRoleCode( originalUserId ) )
      {
        throw new BeaconRuntimeException( "Invalid Access" );
      }
    }

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
   * Get the SystemVariableService from the beanLocator.
   * 
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * Get the ShoppingService From the bean factory through a locator.
   * 
   * @return ShoppingService
   */
  private ShoppingService getShoppingService()
  {
    return (ShoppingService)getService( ShoppingService.BEAN_NAME );
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

  /**
   * Get the PromotionService From the bean factory through a locator.
   * 
   * @return PromoMerchCountryService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService From the bean factory through a locator.
   * 
   * @return PromoMerchCountryService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the PaxGoalService From the bean factory through a locator.
   * 
   * @return PromoMerchCountryService
   */
  private PaxGoalService getPaxGoalService()
  {
    return (PaxGoalService)getService( PaxGoalService.BEAN_NAME );
  }

  /**
   * Get the GoalLevelService From the bean factory through a locator.
   * 
   * @return PromoMerchCountryService
   */
  private GoalLevelService getGoalLevelService()
  {
    return (GoalLevelService)getService( GoalLevelService.BEAN_NAME );
  }

  /**
   * Get the ChallengePointService From the bean factory through a locator.
   * 
   * @return PromoMerchCountryService
   */
  private ChallengePointService getChallengepointService()
  {
    return (ChallengePointService)getService( ChallengePointService.BEAN_NAME );
  }

  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
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

  private void validateResponse( CatalogSSOWSResponse response ) throws DataException
  {
    if ( Objects.isNull( response ) )
    {
      throw new DataException( "External service didnt send proper response." );
    }
    if ( response.getReturnCode() == SUCCESS )
    {
      return;
    }
    throw new DataException( " USER ID: " + UserManager.getUser().getUserId() + ", Response code: " + response.getReturnCode() + " Response Message : " + response.getReturnMessage() );
  }

  private CatalogWebServiceClient getCatalogWebServiceClient()
  {
    return new CatalogWebServiceClient( getPartnerServicesCatalogWsSsoURL(), "g", Environment.getEnvironment() );
  }

  private String getPartnerServicesCatalogWsSsoURL()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PARTNER_SERVICES_CATALOG_SSO_WS_URL_PREFIX ).getStringVal();
  }

  private CatalogSSOWSRequest populateCatalogRequestObject( Participant participant, Address address, UserPhone userPhone, UserEmailAddress userEmail )
  {
    CatalogSSOWSRequest catalogObject = new CatalogSSOWSRequest();
    //
    // names
    //
    catalogObject.setFirstname( UserManager.getUser().getFirstName() );
    catalogObject.setLastname( UserManager.getUser().getLastName() );
    if ( Objects.nonNull( UserManager.getUser().getMiddleName() ) && UserManager.getUser().getMiddleName().length() > 0 )
    {
      catalogObject.setMiddleInitial( UserManager.getUser().getMiddleName().substring( 0, 1 ) );
    }
    catalogObject.setUsername( UserManager.getUserName() );
    //
    // address
    //
    if ( Objects.nonNull( UserManager.getUser().getPrimaryCountryCode() ) )
    {
      catalogObject.setCountryCode( StringUtils.upperCase( UserManager.getUser().getPrimaryCountryCode() ) );
    }
    else
    {
      catalogObject.setCountryCode( StringUtils.upperCase( address.getCountry().getCountryCode() ) );
    }
    catalogObject.setAddress1( address.getAddr1() );
    catalogObject.setAddress2( address.getAddr2() );
    catalogObject.setAddress3( address.getAddr3() );
    catalogObject.setAddress1( address.getAddr1() );
    catalogObject.setCity( address.getCity() );
    if ( Objects.nonNull( address.getStateType() ) )
    {
      catalogObject.setStateProvinceCode( StringUtils.upperCase( address.getStateType().getAbbr() ) );

    }
    catalogObject.setPostalCode( address.getPostalCode() );

    //
    // contacts
    //

    if ( Objects.nonNull( userEmail ) && Objects.nonNull( userEmail.getEmailAddr() ) )
    {
      catalogObject.setEmailAddress( userEmail.getEmailAddr() );
    }
    if ( Objects.nonNull( userPhone ) && Objects.nonNull( userPhone.getPhoneNbr() ) )
    {
      catalogObject.setPhoneNumber( userPhone.getPhoneNbr() );
    }

    //
    // other
    //
    catalogObject.setLocale( UserManager.getUserLocale() );
    // Additional parameters - centrax id
    catalogObject.getAdditionalParameters().add( populateNameValue( "om_participant_id", participant.getCentraxId() ) );

    return catalogObject;
  }

  private NameValue populateNameValue( String key, String value )
  {
    NameValue nv = new NameValue();
    nv.setName( key );
    nv.setValue( value );
    return nv;
  }

  private void validateResponseExternal( CatalogSSOWSResponse response ) throws DataException
  {
    validateResponse( response );
    if ( StringUtils.isEmpty( response.getRedirectURL() ) )
    {
      throw new DataException( "No redirect URL available in response" );
    }
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory awardBanQServiceFactory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return awardBanQServiceFactory.getAwardBanQService();
  }

  private SupplierService getSupplierService()
  {
    SupplierService awardBanQServiceFactory = (SupplierService)BeanLocator.getBean( SupplierService.BEAN_NAME );
    return awardBanQServiceFactory;
  }
  
  private byte[] buildReqPayLoad( Map<String, Object> reqPayLoadMap ) throws ServiceErrorException
  {
    StringBuilder postData = new StringBuilder();
    try
    {
      for ( Map.Entry<String, Object> param : reqPayLoadMap.entrySet() )
      {
        if ( postData.length() != 0 )
        {
          postData.append( '&' );
        }
        postData.append( URLEncoder.encode( param.getKey(), RedeemConstants.UTF8 ) );
        postData.append( '=' );
        postData.append( URLEncoder.encode( String.valueOf( param.getValue() ), RedeemConstants.UTF8 ) );
      }
      return postData.toString().getBytes( RedeemConstants.UTF8 );
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( " Error building parameter for URL " );
    }
  }

  private String buildExperienceRedirectUrl( byte[] postDataBytes ) throws IOException
  {
    URL expUrl = new URL( getSystemVariableService().getPropertyByName( getExperienceVariableName() ).getStringVal() );

    HttpURLConnection connection = (HttpURLConnection)expUrl.openConnection( Environment.buildProxy() );
    connection.setConnectTimeout( 300000 );
    connection.setReadTimeout( 300000 );
    connection.setInstanceFollowRedirects( false );
    connection.setRequestMethod( RedeemConstants.POST );
    connection.setRequestProperty( RedeemConstants.CONTENT_TYPE_TEXT, RedeemConstants.CONTENT_TYPE );
    connection.setRequestProperty( RedeemConstants.CONTENT_LENGTH_TEXT, String.valueOf( postDataBytes.length ) );
    connection.setDoOutput( true );
    connection.setUseCaches( false );
    connection.getOutputStream().write( postDataBytes );

    String redirectUrl = null;
    logger.info( " Response Code " + connection.getResponseCode() );
    if ( connection.getResponseCode() == 302 )
    {
      redirectUrl = connection.getHeaderField( RedeemConstants.LOCATION );
    }
    return redirectUrl;
  }

  /* WIP# 25128 Start */
  private MerchOrderBillCodeService getMerchOrderBillCodeService()
  {
    return (MerchOrderBillCodeService)getService( MerchOrderBillCodeService.BEAN_NAME );
  }
  /* WIP# 25128 End */

  private MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)getService( MerchOrderService.BEAN_NAME );
  }
  
  private CMAssetService getCMAssetService()
  {
      return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }
}
