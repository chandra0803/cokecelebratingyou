/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/ChangePasswordAction.java,v $
 */

package com.biperf.core.ui.profile;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Used for the change password screen(s).
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
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChangePasswordAction extends BaseDispatchAction
{
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ChangePasswordForm changePasswordForm = (ChangePasswordForm)form;
    User sessionUser = getUserService().getUserById( UserManager.getUserId() );
    changePasswordForm.setDisplayOldPassword( !StringUtils.isEmpty( sessionUser.getPassword() ) );
    if ( sessionUser.getSecretQuestionType() != null )
    {
      changePasswordForm.setSecretQuestion( sessionUser.getSecretQuestionType().getCode() );
      changePasswordForm.setSecretAnswer( sessionUser.getSecretAnswer() );
    }
    if ( getUserService().getPasswordPolicyStrategy().isPasswordExpired( sessionUser ) )
    {

      ActionMessages errors = new ActionMessages();
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "login.forgotpwd.EXPIRED_PWD_MESG" ) );
      saveErrors( request, errors );
      request.setAttribute( "serverReturnedErrored", true );
    }
    request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward saveForceChangePassword( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ChangePasswordForm changePasswordForm = (ChangePasswordForm)form;
    try
    {
      getProfileService().changePassword( UserManager.getUserId(), changePasswordForm.getNewPassword(), changePasswordForm.getSecretQuestion(), changePasswordForm.getSecretAnswer(), true );
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      for ( Object obj : serviceErrors )
      {
        ServiceError error = (ServiceError)obj;
        String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
        if ( StringUtils.isNotEmpty( error.getArg1() ) )
        {
          errorMessage = errorMessage.replace( "{0}", error.getArg1() );
        }
        if ( StringUtils.isNotEmpty( error.getArg2() ) )
        {
          errorMessage = errorMessage.replace( "{1}", error.getArg2() );
        }
        error.setArg4( errorMessage );
      }
      request.setAttribute( "serviceValidationErrors", serviceErrors );
      request.setAttribute( "serverReturnedErrored", true );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward savePassword( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse ) throws IOException
  {
    return savePassword( actionMapping, actionForm, httpServletRequest, httpServletResponse, false, true );
  }

  /**
   * @param alwaysSaveRecovery If true, recovery information will be saved (regardless of whether it's the combined password / recovery page)
   * @param successModal If true, send response as command type modal. If false, send response as type success
   */
  public ActionForward savePassword( ActionMapping actionMapping,
                                     ActionForm actionForm,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse,
                                     boolean alwaysSaveRecovery,
                                     boolean successModal )
      throws IOException
  {
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    ChangePasswordForm form = (ChangePasswordForm)actionForm;
    try
    {
      ProfileService profileService = getProfileService();

      boolean changePassword = isPasswordChangeRequested( form );
      if ( isPasswordChangeRequested( form ) )
      {
        profileService.changePassword( UserManager.getUserId(), form.getNewPassword(), form.getOldPassword(), form.getEmailAddress(), form.getPhoneNumber(), form.getCountryPhoneCode(), true );
      }

      // Save recovery if it wasn't a new password save, or if we're being told to save recovery
      // regardless
      if ( !changePassword || alwaysSaveRecovery )
      {
        profileService.saveAccRecoveryInfo( UserManager.getUserId(), form.getEmailAddress(), form.getPhoneNumber(), form.getCountryPhoneCode() );
      }

      // TODO This maybe could be cleaner. Like extracting Acegi code from our UI code.
      // Need to update the password on the Acegi Authentication object so it matches
      // what is in the DB.
      SecurityContext acegiContext = (SecurityContext)httpServletRequest.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
      Authentication currentAuthentication = acegiContext.getAuthentication();

      Authentication authentication = new UsernamePasswordAuthenticationToken( currentAuthentication.getPrincipal(), form.getNewPassword(), currentAuthentication.getAuthorities() );

      acegiContext.setAuthentication( authentication );

      httpServletRequest.getSession().setAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT, acegiContext );
      if ( successModal )
      {
        message = WebErrorMessage.addServerCmd( message );
        message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
      }
      else
      {
        message.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
      }
      message.setName( CmsResourceBundle.getCmsBundle().getString( "system.general.SUCCESS" ) );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PASSWORD_UPDATED" ) );
      messages.getMessages().add( message );
      // message.setUrl( RequestUtils.getBaseURI( httpServletRequest ) +
      // PageConstants.HOME_PAGE_G5_REDIRECT_URL );
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
          if ( StringUtils.isNotEmpty( ( (ServiceError)obj ).getArg2() ) )
          {
            errorMessage = errorMessage.replace( "{1}", ( (ServiceError)obj ).getArg2() );
          }
          errMessage.setText( errorMessage );
          errMessage.setName( CmsResourceBundle.getCmsBundle().getString( "login.loginpage.PASSWORD" ) );
          errMessage.setCode( WebResponseConstants.RESPONSE_CODE_VALIDATION_ERROR );
          errMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        }
        messages.getMessages().add( errMessage );
      }
    }
    super.writeAsJsonToResponse( messages, httpServletResponse );
    return null;
  }

  protected boolean isPasswordChangeRequested( ChangePasswordForm form )
  {
    return form.getNewPassword() != null && !form.getNewPassword().isEmpty() && form.getNewPassword().length() != 0;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return ActionForward
   */
  public ActionForward saveSecurityInfo( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
  {
    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( httpServletRequest ) )
    {
      actionForward = actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
      return actionForward;
    }

    ChangePasswordForm form = (ChangePasswordForm)actionForm;

    try
    {
      ProfileService profileService = getProfileService();
      profileService.setSecretQuestionDetails( UserManager.getUserId(), form.getSecretQuestion(), form.getSecretAnswer() );
    }
    catch( ServiceErrorException e )
    {
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( httpServletRequest, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return actionForward;
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}
