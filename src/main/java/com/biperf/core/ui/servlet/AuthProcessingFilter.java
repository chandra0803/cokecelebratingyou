/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/AuthProcessingFilter.java,v $
 */

package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserTypeEnum;
import com.biperf.core.mobileapp.recognition.service.LoginService;
import com.biperf.core.mobileapp.recognition.service.MobileLoginToken;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.UsernamePasswordAuthenticationToken;
import com.biperf.core.security.credentials.ClientSeamlessLogonCredentials;
import com.biperf.core.security.credentials.QuestionAnswerCredentials;
import com.biperf.core.security.credentials.StandardLoginIdSeamlessLogonCredentials;
import com.biperf.core.security.credentials.StandardSSOIdSeamlessLogonCredentials;
import com.biperf.core.security.exception.AccountHardLockoutException;
import com.biperf.core.security.exception.PaxLockoutException;
import com.biperf.core.security.exception.WarningAlertFailedLoginException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.impl.NoPointsException;
import com.biperf.core.service.security.impl.NonActiveNonUSNoCataLogURLException;
import com.biperf.core.service.security.impl.NonPaxMobileAppLoginException;
import com.biperf.core.service.security.impl.NotEligibleShopException;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SecurityUtils;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Used to handle Authentication Processing.
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
 * <td>zahler</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class AuthProcessingFilter extends UsernamePasswordAuthenticationFilter
{
  private static Log log = LogFactory.getLog( AuthProcessingFilter.class );

  private static final String ONE_TIME_PASSWORD = "One Time Password";

  private String questionAnswerLoginFailureUrl;
  private List<String> acceptedRedirectUrls;
  private String inactivatedUserUrl;
  private String seamlessLogonFailureUrl;

  private String multipleSupplierUrl;
  private String externalSupplierUrl;
  private ShoppingService shoppingService;
  private String defaultTargetUrl;
  private String authenticationFailureUrl;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  @Autowired
  private RewardOfferingsService rewardOfferingsService;

  // created locally
  private ObjectMapper mapper = new ObjectMapper().configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

  private enum LoginResult
  {
    SUCCESS, FAILURE, FIRST_TIME_LOGIN, CHANGE_PASSWORD, SHOPPING_FOR_INACTIVE
  }

  public void setMultipleSupplierUrl( String multipleSupplierUrl )
  {
    this.multipleSupplierUrl = multipleSupplierUrl;
  }

  public void setExternalSupplierUrl( String externalSupplierUrl )
  {
    this.externalSupplierUrl = externalSupplierUrl;
  }

  public void setShoppingService( ShoppingService shoppingService )
  {
    this.shoppingService = shoppingService;
  }

  /**
   * Overridden from
   *
   * @see org.acegisecurity.ui.webapp.AuthenticationProcessingFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest)
   * @param request
   * @return Authentication
   * @throws AuthenticationException
   */
  @Override
  @SuppressWarnings( "deprecation" )
  public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException
  {
    request.changeSessionId();
    UserCreds userCreds = buildUserCreds( request );
    String username = userCreds.getJ_username();
    String usernameencrypted = request.getSession().getAttribute( "j_username_encrypted" ) != null ? (String)request.getSession().getAttribute( "j_username_encrypted" ) : "";
    String password = userCreds.getJ_password();
    String sessionId = request.getSession().getId();
    Object loginCredentials = request.getSession().getAttribute( "LOGIN_CREDENTIALS" );

    if ( username == null )
    {
      username = "";
    }
    else
    {
      username = username.trim();
      if ( username.length() > 40 )
      {
        throw new BadCredentialsException( "Username is too long." );
      }
    }

    if ( password == null )
    {
      password = "";
    }
    else if ( password.length() > 40 )
    {
      throw new BadCredentialsException( "Password is too long." );
    }

    if ( ( Objects.isNull( loginCredentials ) || Objects.isNull( usernameencrypted ) ) && Objects.nonNull( request.getParameter( "classObjName" ) ) )
    {
      String ssoClassName = request.getParameter( "classObjName" );

      usernameencrypted = request.getParameter( "jusernameEncrypted" );

      if ( ssoClassName.equalsIgnoreCase( "SSO_ID" ) )
      {
        StandardSSOIdSeamlessLogonCredentials standardSSOIdSeamlessLogonCredentials = new StandardSSOIdSeamlessLogonCredentials();
        standardSSOIdSeamlessLogonCredentials.setUniqueId( request.getParameter( "uniqueId" ) );
        standardSSOIdSeamlessLogonCredentials.setTimeStamp( request.getParameter( "timeStamp" ) );
        standardSSOIdSeamlessLogonCredentials.setHashString( request.getParameter( "hashString" ) );

        loginCredentials = standardSSOIdSeamlessLogonCredentials;
      }
      else if ( ssoClassName.equalsIgnoreCase( "Login_ID" ) )
      {
        StandardLoginIdSeamlessLogonCredentials standardLoginIdSeamlessLogonCredentials = new StandardLoginIdSeamlessLogonCredentials();
        standardLoginIdSeamlessLogonCredentials.setUniqueId( request.getParameter( "uniqueId" ) );
        standardLoginIdSeamlessLogonCredentials.setTimeStamp( request.getParameter( "timeStamp" ) );
        standardLoginIdSeamlessLogonCredentials.setHashString( request.getParameter( "hashString" ) );

        loginCredentials = standardLoginIdSeamlessLogonCredentials;
      }
    }

    Object credentials = null;

    if ( loginCredentials != null )
    {
      credentials = loginCredentials;
      request.getSession().removeAttribute( "LOGIN_CREDENTIALS" );
    }
    else
    {
      credentials = password;
    }

    UsernamePasswordAuthenticationToken authRequest = null;
    // validate user against vulnerable sso attack.
    if ( credentials instanceof ClientSeamlessLogonCredentials || credentials instanceof StandardLoginIdSeamlessLogonCredentials || credentials instanceof StandardSSOIdSeamlessLogonCredentials )
    {
      String usernamedecrypted = getDecryptedValue( usernameencrypted );

      if ( !usernamedecrypted.equalsIgnoreCase( username ) )
      {
        authRequest = new UsernamePasswordAuthenticationToken( username, credentials, sessionId, true );
      }
      else
      {
        authRequest = new UsernamePasswordAuthenticationToken( username, credentials, sessionId );
      }
    }
    else
    {
      authRequest = new UsernamePasswordAuthenticationToken( username, credentials, sessionId );
    }

    // remove j_username_encrypted from session
    request.getSession().removeAttribute( "j_username_encrypted" );

    // Allow subclasses to set the "details" property
    setDetails( request, authRequest );

    // Place the last username attempted into HttpSession for views
    request.getSession().setAttribute( SPRING_SECURITY_LAST_USERNAME_KEY, username );

    Authentication authentication = null;
    try
    {
      authentication = this.getAuthenticationManager().authenticate( authRequest );

      // One time password has been removed in 6.3 with the new activation flow
      // if ( authentication.isAuthenticated() )
      // {
      // User user = getUserService().getUserByUserName( username );
      //
      // if ( user.isOneTimePassword() )
      // {
      // Date otpDate = user.getOneTimePasswordDate();
      // long otpExpiredPeriod = getSystemVariableService().getPropertyByName(
      // SystemVariableService.USER_OTP_PASSWORD_EXPIRY_DAYS ).getLongVal() *
      // DateUtils.MILLIS_PER_DAY;
      // if ( System.currentTimeMillis() - otpDate.getTime() > otpExpiredPeriod )
      // {
      // throw new CredentialsExpiredException( ONE_TIME_PASSWORD );
      // }
      // }
      // }
    }
    catch( AuthenticationException authenticationException )
    {
      if ( authenticationException instanceof LockedException )
      {
        boolean isPaxInactiveWithNoPoints = authenticationException.getMessage().equals( "Pax inactive with no points" );
        if ( isPaxInactiveWithNoPoints )
        {
          request.getSession().setAttribute( "isSSO", true );
        }
      }
      throw authenticationException;
    }
    return authentication;
  }

  /**
   * Overridden from
   *
   * @see org.acegisecurity.ui.AbstractProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, org.acegisecurity.Authentication)
   * @param request
   * @param response
   * @param authResult
   * @throws IOException
   */
  @Override
  protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response, Authentication authResult ) throws IOException
  {
    LoginResult result = LoginResult.SUCCESS;

    String page = request.getParameter( "page" );
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Authentication success: " + authResult.toString() );
    }

    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication( authResult );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Updated SecurityContextImpl to contain the following Authentication: '" + authResult + "'" );
    }

    String targetUrl = RequestUtils.getBaseURI( request ) + getDefaultTargetUrl();

    AuthenticatedUser authUser = (AuthenticatedUser)authResult.getPrincipal();
    User userObj = null;

    Object principal = authResult.getPrincipal();

    if ( principal instanceof UserDetails )
    {
      UserDetails user = (UserDetails)principal;
      userObj = getUserService().getUserById( authUser.getUserId() );

      boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );

      // if first time user redirect to
      if ( !userObj.isProfileSetupDone() && userObj.isParticipant() && !isLoginAs )
      {
        targetUrl = RequestUtils.getBaseURI( request ) + PageConstants.FIRST_TIME_LOGIN_PAGE;
        result = LoginResult.FIRST_TIME_LOGIN;
      }
      else
      {
        if ( getUserService().getPasswordPolicyStrategy().isPasswordExpired( userObj ) && userObj.isParticipant()
            && ( Objects.nonNull( request.getSession().getAttribute( "ssoLogin" ) ) ? false : true ) )
        {
          targetUrl = RequestUtils.getBaseURI( request ) + PageConstants.CHANGE_PWD_URL;
          result = LoginResult.CHANGE_PASSWORD;
        }
        else
        {
          // ----- Change Password page URL -----
          if ( !user.isCredentialsNonExpired() )
          {
            if ( userObj.isForcePasswordChange() && userObj.isParticipant() )
            {
              targetUrl = RequestUtils.getBaseURI( request ) + PageConstants.CHANGE_PWD_URL;
              result = LoginResult.CHANGE_PASSWORD;
            }
          }
        }
      }
    }
    if ( authUser.getUserType().getCode().equals( UserTypeEnum.PARTICIPANT_CODE ) && Objects.nonNull( authUser.getPaxTerminationDate() ) )
    {

      if ( !getSystemVariableService().getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() )
      {
        getRememberMeServices().loginFail( request, response );
        AuthenticationException failed = new NotEligibleShopException( "" );
        WebErrorMessageList messages = convertErrors( failed, response );
        String postType = request.getHeader( "post-type" );
        if ( postType == null || !postType.equalsIgnoreCase( "ajax" ) )
        {
          response.sendRedirect( response.encodeRedirectURL( RequestUtils.getBaseURI( request ) + "/login.do" ) );
          return;
        }

        processJSONResponse( messages, response );

      }
      else
      {
        if ( !isUserHasPoints( authUser ) )
        {
          getRememberMeServices().loginFail( request, response );
          AuthenticationException failed = new NoPointsException( "" );
          WebErrorMessageList messages = convertErrors( failed, response );

          String postType = request.getHeader( "post-type" );
          if ( postType == null || !postType.equalsIgnoreCase( "ajax" ) )
          {
            response.sendRedirect( response.encodeRedirectURL( RequestUtils.getBaseURI( request ) + "/login.do" ) );
            return;
          }

          processJSONResponse( messages, response );

        }
      }
    }
    // Participant redirects
    if ( authUser.getUserType().getCode().equals( UserTypeEnum.PARTICIPANT_CODE ) )
    {
      Long balance = null;

      if ( authUser.isPaxInactive() )
      {
        String shoppingUrlForInactiveUser = getShoppingUrlForInactiveUser( authUser.getUserId() );
        targetUrl = RequestUtils.getBaseURI( request ) + shoppingUrlForInactiveUser;
        result = LoginResult.SHOPPING_FOR_INACTIVE;
        if ( shoppingUrlForInactiveUser.contains( "https" ) )
        {
          targetUrl = shoppingUrlForInactiveUser;
        }
      }

      // if show T&Cs, overrides the AwardLinQ site, redirect to T&Cs page
      if ( authUser.isShowTermsAndConditions() )
      {
        // ----- Terms & Conditions Page ---
        targetUrl = RequestUtils.getBaseURI( request ) + PageConstants.FIRST_TIME_LOGIN_PAGE;
        result = LoginResult.FIRST_TIME_LOGIN;
      }

      if ( authUser.getUserId() != null )
      {
        Date terminationDate = authUser.getPaxTerminationDate();

        if ( terminationDate != null && terminationDate.before( new Date() ) )
        {
          if ( userObj == null )
          {
            userObj = getUserService().getUserById( authUser.getUserId() );
          }
          if ( !userObj.isActive() )
          {
            // do this last, since getting the account balance is expensive and blocks the login
            balance = awardBanQServiceFactory.getAwardBanQService().getAccountBalanceForParticipantId( userObj.getId() );
            if ( balance != null && balance.longValue() > 0 )
            {
              String shoppingUrlForInactiveUser = getShoppingUrlForInactiveUser( authUser.getUserId() );
              targetUrl = RequestUtils.getBaseURI( request ) + shoppingUrlForInactiveUser;
              result = LoginResult.SHOPPING_FOR_INACTIVE;
              if ( shoppingUrlForInactiveUser.contains( "https" ) )
              {
                targetUrl = shoppingUrlForInactiveUser;
              }
            }
          }
        }
      }

      if ( ParticipantTermsAcceptance.DECLINED.equals( authUser.getPaxTermsAcceptance() ) )
      {
        if ( userObj == null )
        {
          userObj = getUserService().getUserById( authUser.getUserId() );
        }

        // Need to know the balance, declined will always go to shop when the pax has points
        if ( balance == null )
        {
          balance = awardBanQServiceFactory.getAwardBanQService().getAccountBalanceForParticipantId( userObj.getId() );
        }

        // Oops. Balance still null. Bank likely down, but cannot let a declined person slip in.
        if ( balance == null )
        {
          AuthenticationException authException = new AuthenticationServiceException( "" );
          unsuccessfulAuthentication( request, response, authException );
          return;
        }
        // Go to shop
        else if ( balance.longValue() > 0 )
        {
          String shoppingUrlForInactiveUser = getShoppingUrlForInactiveUser( authUser.getUserId() );
          targetUrl = RequestUtils.getBaseURI( request ) + shoppingUrlForInactiveUser;
          result = LoginResult.SHOPPING_FOR_INACTIVE;
          if ( shoppingUrlForInactiveUser.contains( "https" ) )
          {
            targetUrl = shoppingUrlForInactiveUser;
          }
        }
        // Go to T&C page
        else
        {
          targetUrl = RequestUtils.getBaseURI( request ) + PageConstants.FIRST_TIME_LOGIN_PAGE;
          result = LoginResult.FIRST_TIME_LOGIN;
        }
      }
    }

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Redirecting to target URL from HTTP Session (or default): " + targetUrl );
    }

    request.getSession().removeAttribute( Globals.ERROR_KEY );

    onSuccessfulAuthentication( request, response, authResult );

    getRememberMeServices().loginSuccess( request, response, authResult );

    MobileLoginToken mobileLoginToken = null;
    if ( fromMobile( request ) )
    {
      // If the logged in user is not a participant they should not be allowed into the mobile app
      if ( !authUser.getUserType().getCode().equals( UserTypeEnum.PARTICIPANT_CODE ) )
      {
        AuthenticationException authException = new NonPaxMobileAppLoginException( "" );
        unsuccessfulAuthentication( request, response, authException );
        return;
      }
      mobileLoginToken = getLoginService().onSuccessfulAuthentication( authUser.getUserId() );
    }

    if ( isJSONResponse( request ) )
    {
      WebErrorMessageList messages = new WebErrorMessageList();
      if ( page == null )
      {
        WebErrorMessage message = new WebErrorMessage();
        message = WebErrorMessage.addServerCmd( message );
        message.setUrl( targetUrl );
        message.setCode( result.name() );
        if ( mobileLoginToken != null )
        {
          message.setText( mobileLoginToken.getToken() );
        }
        messages.getMessages().add( message );
      }

      processJSONResponse( messages, response );
    }
    else
    {
      response.sendRedirect( response.encodeRedirectURL( targetUrl ) );
      return;
    }
  }

  private boolean isJSONResponse( HttpServletRequest request )
  {
    String postType = request.getHeader( "post-type" );
    String responseType = request.getParameter( "responseType" );
    return "ajax".equalsIgnoreCase( postType ) && !"html".equalsIgnoreCase( responseType );
  }

  private boolean fromMobile( HttpServletRequest request )
  {
    return "mobile".equals( request.getParameter( "source" ) );
  }

  private String getShoppingUrlForInactiveUser( Long userId )
  {
    String shoppingType = ShoppingService.NONE;
    boolean multiplieSupplier = false;

    // if country has more than one supplier or one supplier and has display travel award true then
    // takes to multiple supplier awards page.
    UserAddress userAddress = getUserService().getPrimaryUserAddress( userId );
    if ( userAddress != null )
    {
      Address address = userAddress.getAddress();
      if ( address != null )
      {
        // Validating if only travel or not
        String programId = getUserService().getCountryProgramId( userId );
        if ( programId != null )
        {
          List<RewardOffering> rewardsList = rewardOfferingsService.getRewardOfferings( programId );
          if ( rewardsList.size() == 1 && rewardsList.get( 0 ).getType().equalsIgnoreCase( "travel" ) )
          {
            // travelOnlyEnabled = true;
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put( "page", rewardsList.get( 0 ).getSsoDestination() );
            String travelUrl = ClientStateUtils.generateEncodedLink( "", PageConstants.TRAVEL_PAGE_URL, parameterMap );
            return travelUrl;
          }
        }
        if ( address.getCountry().getCountrySuppliers() != null && address.getCountry().getCountrySuppliers().size() > 1
            || address.getCountry().getCountrySuppliers() != null && address.getCountry().getCountrySuppliers().size() == 1 && address.getCountry().getDisplayTravelAward() )
        {
          multiplieSupplier = true;
        }
        else
        {
          shoppingType = shoppingService.checkShoppingType( userId );

          if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
          {
            boolean isBiiSupplier = checkCurrentUserSupplierIsBii( address.getCountry().getCountryCode() );
            if ( isBiiSupplier )
            {
              Map<String, Object> biiParameterMap = new HashMap<>();
              biiParameterMap.put( "externalSupplierId", getSupplierService().getSupplierByName( Supplier.BII ).getId() );
              externalSupplierUrl = "/" + ClientStateUtils.generateEncodedLink( "", PageConstants.SHOPPING_EXTERNAL, biiParameterMap );
            }
          }
        }
      }
    }

    if ( multiplieSupplier )
    {
      return multipleSupplierUrl;
    }
    else if ( shoppingType.equals( ShoppingService.INTERNAL ) )
    {
      return inactivatedUserUrl;
    }
    else if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
    {
      return externalSupplierUrl;
    }
    return inactivatedUserUrl;
  }

  /**
   * On a successful authentication we need to set the SecurityContext on the contextIntegrationFilter
   * in cm.
   *
   * @param request
   * @param response
   * @param authResult
   * @throws IOException
   */
  protected void onSuccessfulAuthentication( HttpServletRequest request, HttpServletResponse response, Authentication authResult ) throws IOException
  {
    // Commented as tomcat code cannot get filter or context of CM.
    /*
     * ContextByIdCache cmsContextByIdCache = (ContextByIdCache)ContentReaderManager
     * .getContentReader().getApplicationContext().getBean( CM_CONTEXT_INTEGRATION_FILTER ); if (
     * cmsContextByIdCache != null ) { cmsContextByIdCache.addContext( request.getSession().getId(),
     * SecurityContextHolder .getContext() ); }
     */
  }

  /**
   * Overridden from
   *
   * @see org.acegisecurity.ui.AbstractProcessingFilter#unsuccessfulAuthentication(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, org.acegisecurity.AuthenticationException)
   * @param request
   * @param response
   * @param failed
   * @throws IOException
   */
  @Override
  @SuppressWarnings( "deprecation" )
  protected void unsuccessfulAuthentication( HttpServletRequest request, HttpServletResponse response, AuthenticationException failed ) throws IOException
  {
    /*
     * avoid redeployments issues by clearing the context instead of creating a new one! SEC-159
     */
    SecurityContextHolder.clearContext();
    SecurityContextHolder.getContext().setAuthentication( null );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Updated SecurityContextImpl to contain null Authentication" );
    }

    WebErrorMessageList messages = new WebErrorMessageList();
    Authentication auth = failed.getAuthentication();
    String failureUrl = getFilterProcessesUrl(); // = getAuthenticationFailureUrl();
    if ( auth != null )
    {
      Object credentials = auth.getCredentials();

      if ( credentials instanceof QuestionAnswerCredentials )
      {
        failureUrl = questionAnswerLoginFailureUrl;
      }

      else if ( credentials instanceof ClientSeamlessLogonCredentials || credentials instanceof StandardLoginIdSeamlessLogonCredentials
          || credentials instanceof StandardSSOIdSeamlessLogonCredentials )
      {
        failureUrl = seamlessLogonFailureUrl;
      }
    }
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Authentication request failed: " + failed.toString() );
    }

    request.getSession().setAttribute( SPRING_SECURITY_LAST_EXCEPTION_KEY, failed );

    // unsuccessfulAuthentication( request, response, failed );

    getRememberMeServices().loginFail( request, response );

    messages = convertErrors( failed, response );

    String postType = request.getHeader( "post-type" );
    if ( postType == null || !postType.equalsIgnoreCase( "ajax" ) )
    {
      response.sendRedirect( response.encodeRedirectURL( RequestUtils.getBaseURI( request ) + failureUrl ) );
      return;
    }

    processJSONResponse( messages, response );
  }

  private void processJSONResponse( WebErrorMessageList messages, HttpServletResponse response ) throws IOException
  {
    Writer responseStrWriter = new StringWriter();
    mapper.writeValue( responseStrWriter, messages );
    String responseMesg = responseStrWriter.toString();
    response.setContentType( "application/json" );
    PrintWriter out = response.getWriter();
    out.print( responseMesg );
    out.flush();
    out.close();
  }

  private WebErrorMessageList convertErrors( AuthenticationException failed, HttpServletResponse response )
  {
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    message.setCode( LoginResult.FAILURE.name() );

    if ( failed instanceof AuthenticationServiceException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.GENERIC_LOGIN_ERROR" ) );
    }
    else if ( failed instanceof BadCredentialsException || failed.getCause() instanceof WarningAlertFailedLoginException )
    {
      message = WebErrorMessage.addErrorMessageNoName( message );
      if ( failed.getCause() instanceof WarningAlertFailedLoginException )
      {
        WarningAlertFailedLoginException ex = (WarningAlertFailedLoginException)failed.getCause();
        String msg = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "login.errors.BAD_CREDENTIALS_ATTEMPTS_REMAINING_ERROR" ), new Object[] { ex.getAttemptsRemaining() } );
        message.setText( msg );
      }
      else
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.BAD_CREDENTIALS_ERROR" ) );
      }
    }
    else if ( failed instanceof DisabledException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.ACCOUNT_DISABLED_ERROR" ) );
    }
    else if ( failed instanceof LockedException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      if ( failed.getCause() instanceof PaxLockoutException )
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.PAX_LOCKED_ERROR" ) );
      }
      else if ( failed.getCause() instanceof AccountHardLockoutException )
      {
        message = WebErrorMessage.addServerCmd( message );
        message.setUrl( buildRedirect() );
        message.setCode( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
      }
      else
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.ACCOUNT_LOCKED_ERROR" ) );
      }
    }
    /*
     * else if ( failed instanceof ProxyUntrustedException ) { message =
     * WebErrorMessage.addErrorMessage( message ); message.setText(
     * CmsResourceBundle.getCmsBundle().getString( "login.errors.PROXY_UNTRUSTED_ERROR" ) ); }
     */
    else if ( failed instanceof CredentialsExpiredException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      if ( failed.getMessage().equalsIgnoreCase( ONE_TIME_PASSWORD ) )
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.ONE_TIME_PASSWORD_EXPIRED_ERROR" ) );
      }
      else
      {
        message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.CREDENTIALS_EXPIRED_ERROR" ) );
      }
    }
    else if ( failed instanceof NonActiveNonUSNoCataLogURLException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.INACTIVE_NONUS_NOCATALOGURL" ) );
    }
    else if ( failed instanceof NonPaxMobileAppLoginException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.NON_PAX_MOBILE_APP_LOGIN" ) );
    }
    else if ( failed instanceof NotEligibleShopException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "login.account.activation.messages.TERMED_USER_SHOPPING_OFF" ) );
    }
    else if ( failed instanceof NoPointsException )
    {
      message = WebErrorMessage.addErrorMessage( message );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "login.account.activation.messages.TERMED_USER_SHOPPING_ON_NO_POINTS" ) );
    }
    messages.getMessages().add( message );

    return messages;
  }

  private String buildRedirect()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    sb.append( "/login.do?accountLock=true&key=true&isEmail=false" );

    return sb.toString();
  }

  private String getDecryptedValue( String encryptedValue )
  {
    String aesKey = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
    String aesInitVector = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();
    String decryptedValue = null;
    try
    {
      decryptedValue = SecurityUtils.decryptAES( encryptedValue, aesKey, aesInitVector );
    }
    catch( Exception e )
    {
      logger.error( "Unable to parse SSO parameter: " + encryptedValue, e );
    }
    return decryptedValue;
  }

  /**
   * @param questionAnswerLoginFailureUrl value for questionAnswerLoginFailureUrl property
   */
  public void setQuestionAnswerLoginFailureUrl( String questionAnswerLoginFailureUrl )
  {
    this.questionAnswerLoginFailureUrl = questionAnswerLoginFailureUrl;
  }

  public void setInactivatedUserUrl( String inactivatedUserUrl )
  {
    this.inactivatedUserUrl = inactivatedUserUrl;
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  private static AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  public String getSeamlessLogonFailureUrl()
  {
    return seamlessLogonFailureUrl;
  }

  public void setSeamlessLogonFailureUrl( String seamlessLogonFailureUrl )
  {
    this.seamlessLogonFailureUrl = seamlessLogonFailureUrl;
  }

  private LoginService getLoginService()
  {
    return (LoginService)BeanLocator.getBean( LoginService.BEAN_NAME );
  }

  public String getDefaultTargetUrl()
  {
    return defaultTargetUrl;
  }

  public void setDefaultTargetUrl( String defaultTargetUrl )
  {
    this.defaultTargetUrl = defaultTargetUrl;
  }

  public String getAuthenticationFailureUrl()
  {
    return authenticationFailureUrl;
  }

  public void setAuthenticationFailureUrl( String authenticationFailureUrl )
  {
    this.authenticationFailureUrl = authenticationFailureUrl;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public List<String> getAcceptedRedirectUrls()
  {
    return acceptedRedirectUrls;
  }

  public void setAcceptedRedirectUrls( List<String> acceptedRedirectUrls )
  {
    this.acceptedRedirectUrls = acceptedRedirectUrls;
  }

  private boolean checkCurrentUserSupplierIsBii( String countryCode )
  {
    return getCountryService().checkUserSupplier( countryCode, Supplier.BII );
  }

  private CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  private SupplierService getSupplierService()
  {
    return (SupplierService)BeanLocator.getBean( SupplierService.BEAN_NAME );
  }

  protected UserCreds buildUserCreds( HttpServletRequest request )
  {
    UserCreds userCreds = new UserCreds();
    try
    {
      if ( MediaType.APPLICATION_JSON.equalsIgnoreCase( request.getContentType() ) ) // JSON
                                                                                     // response
      {
        userCreds = mapper.readValue( request.getInputStream(), UserCreds.class );
        userCreds.setJ_password( new String( Base64.decode( userCreds.getJ_password().getBytes() ) ) );

      }
      else // standard form post
      {
        userCreds.setJ_username( request.getParameter( SPRING_SECURITY_FORM_USERNAME_KEY ) );
        userCreds.setJ_password( obtainPassword( request ) );
      }
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }

    return userCreds;
  }

  private static class UserCreds
  {
    private String j_username = null;
    private String j_password = null;

    public UserCreds()
    {
    }

    public String getJ_username()
    {
      return j_username;
    }

    public void setJ_username( String j_username )
    {
      this.j_username = j_username;
    }

    public String getJ_password()
    {
      return j_password;
    }

    public void setJ_password( String j_password )
    {
      this.j_password = j_password;
    }

  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return factory.getAwardBanQService();
  }

  // Facility for the termed user to do the account activation, to redeem his/her points.
  protected boolean isUserHasPoints( AuthenticatedUser pax )
  {
    // Get awardBanQ Balance for the Pax
    Long balance = getAwardBanQService().getAccountBalanceForParticipantId( pax.getUserId() );

    if ( Objects.nonNull( balance ) && balance.longValue() > 0 )
    {
      return true;
    }

    return false;
  }

}
