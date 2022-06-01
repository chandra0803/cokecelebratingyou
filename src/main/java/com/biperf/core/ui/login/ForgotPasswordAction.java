/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/login/ForgotPasswordAction.java,v $
 */

package com.biperf.core.ui.login;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.biperf.core.domain.Fields;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.credentials.QuestionAnswerCredentials;
import com.biperf.core.security.exception.PaxLockoutException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.FormFieldConstants;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Used for the forgot password screen(s).
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
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ForgotPasswordAction extends BaseDispatchAction
{

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    WebErrorMessageList messagesNew = new WebErrorMessageList();
    super.writeAsJsonToResponse( messagesNew, response );
    return null;
  }

  /**
   * forgotPwdNameValidate Looks up the user based on User name, and checks if the user is locked
   * out. If successful, it will put FORGOT_PWD_USER onto the session.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException 
   */
  public ActionForward forgotPwdNameValidate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    log.info( ">>> ForgotPasswordAction.forgotPwdNameValidate" );
    ForgotPasswordForm forgotPwdForm = (ForgotPasswordForm)actionForm;
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    HttpSession session = request.getSession();
    session.removeAttribute( ViewAttributeNames.FORGOT_PWD_USER );

    if ( StringUtils.isEmpty( forgotPwdForm.getUserName() ) )
    {
      Fields loginIdField = new Fields();
      loginIdField.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
      loginIdField.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.LOGIN_ID_REQ" ) );
      message.getFields().add( loginIdField );
    }
    else
    {
      UserService userService = getUserService();
      AuthenticationService authService = getAuthenticationService();
      User user = userService.getUserByUserName( forgotPwdForm.getUserName() );

      if ( user == null )
      {
        Fields loginIdField = new Fields();
        loginIdField.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
        loginIdField.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.LOGIN_ID_INVALID" ) );
        message.getFields().add( loginIdField );
      }
      else
      {
        // Check if the user is locked out
        if ( authService.isUserLockedOut( user ) )
        {
          log.debug( "ForgotPasswordAction.forgotPwdNameValidate - user locked out" );
          Fields loginIdField = new Fields();
          loginIdField.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
          loginIdField.setText( CmsResourceBundle.getCmsBundle().getString( "login.forgotpwd.USER_LOCKED_OUT_ERROR" ) );
          message.getFields().add( loginIdField );
        }
        else
        {
          if ( user.getSecretQuestionType() == null || user.getSecretQuestionType().getName() == null || user.getSecretQuestionType().getName().equals( "" ) )
          {
            Fields loginIdField = new Fields();
            loginIdField.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
            loginIdField.setText( CmsResourceBundle.getCmsBundle().getString( "login.forgotpwd.SECURITY_QUESTION_NOT_SET" ) );
            message.getFields().add( loginIdField );
          }
          session.setAttribute( ViewAttributeNames.FORGOT_PWD_USER, user );
        }

      }
    }
    if ( message.getFields().size() > 0 )
    {
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setCode( WebResponseConstants.RESPONSE_CODE_VALIDATION_ERROR );
      messages.getMessages().add( message );
      super.writeAsJsonToResponse( messages, response );

    }
    else
    {
      WebErrorMessageList messagesNew = new WebErrorMessageList();
      super.writeAsJsonToResponse( messagesNew, response );
    }
    return null;
  }

  public ActionForward getSecurityQuestion( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {

    log.info( ">>> ForgotPasswordAction.forgotPwdAnswer" );

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );

    User user = (User)request.getSession().getAttribute( ViewAttributeNames.FORGOT_PWD_USER );
    if ( user == null )
    {
      log.debug( "ForgotPasswordAction.forgotPwdAnswer - Session is Expired" );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    log.info( "<<< ForgotPasswordAction.forgotPwdAnswer = " + actionForward.getName() );
    return actionForward;
  }

  public ActionForward getChangePassword( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {

    log.info( ">>> ForgotPasswordAction.forgotPwdAnswer" );

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );

    User user = (User)request.getSession().getAttribute( ViewAttributeNames.FORGOT_PWD_USER );
    if ( user == null )
    {
      log.debug( "ForgotPasswordAction.forgotPwdAnswer - Session is Expired" );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    ForgotPasswordForm form = (ForgotPasswordForm)actionForm;
    if ( user != null )
    {
      form.setSecretQuestion( user.getSecretQuestionType().getCode() );
      form.setSecretAnswer( user.getSecretAnswer() );
    }
    log.info( "<<< ForgotPasswordAction.forgotPwdAnswer = " + actionForward.getName() );
    return actionForward;
  }

  @SuppressWarnings( "rawtypes" )
  public ActionForward savePassword( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException, PaxLockoutException
  {
    int errorCount = 0;
    ForgotPasswordForm form = (ForgotPasswordForm)actionForm;
    ProfileService profileService = getProfileService();
    User targetUser = (User)request.getSession().getAttribute( ViewAttributeNames.FORGOT_PWD_USER );

    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    message = WebErrorMessage.addServerCmd( message );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );

    try
    {
      if ( form.getNewPassword() != null && !form.getNewPassword().isEmpty() && form.getNewPassword().length() != 0 )
      {
        profileService.changePassword( targetUser.getId(), form.getNewPassword(), form.getSecretQuestion(), form.getSecretAnswer(), true );
      }
      else
      {
        profileService.setSecretQuestionDetails( targetUser.getId(), form.getSecretQuestion(), form.getSecretAnswer() );
      }
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      for ( Object obj : serviceErrors )
      {
        WebErrorMessage errMessage = new WebErrorMessage();
        if ( obj instanceof ServiceError )
        {
          String errorMessage = CmsResourceBundle.getCmsBundle().getString( ( (ServiceError)obj ).getKey() );
          if ( StringUtils.isNotEmpty( ( (ServiceError)obj ).getArg1() ) )
          {
            errorMessage = errorMessage.replace( "{0}", ( (ServiceError)obj ).getArg1() );
          }
          errMessage.setText( errorMessage );
          errMessage.setName( CmsResourceBundle.getCmsBundle().getString( "login.loginpage.PASSWORD" ) );
          errMessage.setCode( WebResponseConstants.RESPONSE_CODE_VALIDATION_ERROR );
          errMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        }
        messages.getMessages().add( errMessage );
        errorCount++;
      }
    }
    // Bug 3089 - Add authenticated user to the security context only when the password change is
    // successful.
    if ( errorCount == 0 )
    {
      AuthenticatedUser newAuthenticatedUser = getAuthenticationService().buildAuthenticatedUser( targetUser, null );
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( newAuthenticatedUser, null, newAuthenticatedUser.getAuthorities() );
      authentication.setDetails( newAuthenticatedUser );
      SecurityContext sc = SecurityContextHolder.getContext();
      sc.setAuthentication( authentication );
      AuthenticationService authenticationService = getAuthenticationService();
      UserService userService = getUserService();
      AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
      userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ALL ) );

      User user = getUserService().getUserByIdWithAssociations( targetUser.getId(), userAssociationRequestCollection );

      boolean isPaxInactiveNoBalance = authenticationService.isPaxInactiveWithNoAwardBanQPoints( user );
      if ( user instanceof Participant )
      {
        if ( newAuthenticatedUser.isPaxInactive() && !isPaxInactiveNoBalance )
        {
          String shoppingUrlForInactiveUser = getAuthenticationService().getShoppingUrlForInactiveUser( PageConstants.INACTIVE_SHOPPING_URL,
                                                                                                        PageConstants.MULTISUPPLIER_SHOPPING_URL,
                                                                                                        newAuthenticatedUser.getUserId() );
          String targetUrl = RequestUtils.getBaseURI( request ) + shoppingUrlForInactiveUser;
          if ( shoppingUrlForInactiveUser.contains( "https" ) )
          {
            targetUrl = shoppingUrlForInactiveUser;
          }
          message.setUrl( targetUrl );
        }
        else if ( newAuthenticatedUser.isPaxInactive() && isPaxInactiveNoBalance )
        {
          message = WebErrorMessage.addErrorMessage( message );
          message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.PAX_LOCKED_ERROR" ) );
          // Invalidating user as he is not eligible for this program
          request.getSession().removeAttribute( "userObj" );
          request.getSession().invalidate();

        }
        else
        {
          message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );
        }
      }
      else
      {
        if ( user.getActive() )
        {
          message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );
        }
        else
        {
          message = WebErrorMessage.addErrorMessage( message );
          message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.PAX_LOCKED_ERROR" ) );
          // Invalidating user as he is not eligible for this program
          request.getSession().removeAttribute( "userObj" );
          request.getSession().invalidate();

        }
      }
    }

    if ( messages.getMessages().isEmpty() )
    {
      messages.getMessages().add( message );
    }

    this.writeAsJsonToResponse( messages, response );
    return null;
  }

  /**
   * forgotPwdCancel Removes FORGOT_PWD_USER from the Session
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward forgotPwdCancel( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    log.info( ">>> ForgotPasswordAction.forgotPwdCancel" );

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
    request.getSession().removeAttribute( ViewAttributeNames.FORGOT_PWD_USER );

    log.info( "<<< ForgotPasswordAction.forgotPwdCancel = " + actionForward.getName() );
    return actionForward;
  } // end forgotPwdCancel

  /**
   * forgotPwdAnswer Verifies that FORGOT_PWD_USER is still in the session to verify that the
   * session has not expired yet.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  // TODO get rid of this method VD
  public ActionForward forgotPwdAnswer( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    log.info( ">>> ForgotPasswordAction.forgotPwdAnswer" );

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );

    User user = (User)request.getSession().getAttribute( ViewAttributeNames.FORGOT_PWD_USER );
    if ( user == null )
    {
      log.debug( "ForgotPasswordAction.forgotPwdAnswer - Session is Expired" );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    log.info( "<<< ForgotPasswordAction.forgotPwdAnswer = " + actionForward.getName() );
    return actionForward;
  } // end forgotPwdAnswer

  /**
   * forgotPwdAnswerValidate Gets the user out of the session, validates that the user is not locked
   * out, Then authenticates based on username, question, answer
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException 
   */
  public ActionForward forgotPwdAnswerValidate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    log.info( ">>> ForgotPasswordAction.forgotPwdAnswerValidate" );
    ForgotPasswordForm forgotPwdForm = (ForgotPasswordForm)actionForm;
    HttpSession session = request.getSession();
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();

    try
    {
      AuthenticationService authService = getAuthenticationService();
      User user = (User)session.getAttribute( ViewAttributeNames.FORGOT_PWD_USER );
      if ( user == null )
      {
        // session has expired, forward to login
        message = WebErrorMessage.addErrorMessage( message );
        Fields userNameField = new Fields();
        userNameField.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
        userNameField.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.BAD_CREDENTIALS_ERROR" ) );
        message.getFields().add( userNameField );
        messages.getMessages().add( message );
      }
      else
      {
        // check for dummy user
        if ( user.getId().longValue() == -100 )
        {
          message = WebErrorMessage.addErrorMessage( message );
          Fields userNameField = new Fields();
          userNameField.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
          userNameField.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.BAD_CREDENTIALS_ERROR" ) );
          message.getFields().add( userNameField );
          messages.getMessages().add( message );
        }
        else
        {
          // Check if the user is locked out
          if ( authService.isUserLockedOut( user ) )
          {
            message = WebErrorMessage.addErrorMessage( message );
            Fields userNameField = new Fields();
            userNameField.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
            userNameField.setText( CmsResourceBundle.getCmsBundle().getString( "login.forgotpwd.USER_LOCKED_OUT_ERROR" ) );
            message.getFields().add( userNameField );
            messages.getMessages().add( message );
          }
          // Authenticate
          QuestionAnswerCredentials credentials = new QuestionAnswerCredentials( user.getSecretQuestionType().getCode(), forgotPwdForm.getSecretAnswer() );
          boolean credentialsValid = false;
          if ( user.getSecretAnswer() == null || !user.getSecretAnswer().equalsIgnoreCase( credentials.getSecretAnswer() ) || user.getSecretQuestionType().getCode() == null
              || !user.getSecretQuestionType().getCode().equalsIgnoreCase( credentials.getSecretQuestion() ) )
          {
            credentialsValid = false;
          }
          else
          {
            credentialsValid = true;
            session.setAttribute( "LOGIN_CREDENTIALS", credentials );
          }
          if ( credentialsValid )
          {
            messages = new WebErrorMessageList();
            UsernamePasswordAuthenticationToken authRequest = new com.biperf.core.security.UsernamePasswordAuthenticationToken( user.getUserName(), credentials, request.getSession().getId() );
            // TODO do the authentication
            // Allow subclasses to set the "details" property
            // setDetails( request, authRequest );
            // Place the last username attempted into HttpSession for views
            request.getSession().setAttribute( UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY, user.getUserName() );

          }
          else
          {
            message = WebErrorMessage.addErrorMessage( message );
            Fields secretAnsField = new Fields();
            secretAnsField.setName( FormFieldConstants.FORGOT_PWD_FORM_SECRET_ANSWER );
            secretAnsField.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.SECRET_ANS_INVALID" ) );
            message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.SECRET_ANS_INVALID" ) );
            message.getFields().add( secretAnsField );
            messages.getMessages().add( message );
          }
          super.writeAsJsonToResponse( messages, response );
          return null;
        }
      }
    }
    catch( IOException e )
    {
      log.info( "Exception", e );
      message = WebErrorMessage.addErrorMessage( message );
      Fields secretAnsField = new Fields();
      secretAnsField.setName( FormFieldConstants.FORGOT_PWD_FORM_SECRET_ANSWER );
      secretAnsField.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.SECRET_ANS_INVALID" ) );
      message.getFields().add( secretAnsField );
      messages.getMessages().add( message );
      super.writeAsJsonToResponse( messages, response );
      return null;
    }
    return null;
  }

  /**
   * Gets the AuthenticationService
   * 
   * @return AuthenticationService
   */
  private AuthenticationService getAuthenticationService()
  {
    return (AuthenticationService)getService( AuthenticationService.BEAN_NAME );
  }

  /**
   * Gets the User Service
   * 
   * @return User Service
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

} // end class ForgotPasswordAction
