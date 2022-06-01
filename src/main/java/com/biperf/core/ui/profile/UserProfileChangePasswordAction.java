
package com.biperf.core.ui.profile;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

public class UserProfileChangePasswordAction extends ChangePasswordAction
{

  /** Assumes changing only password - not recovery information */
  public ActionForward changePassword( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse ) throws IOException
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( httpServletRequest, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( httpServletRequest, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }    
    ActionForward forward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );

    if ( isCancelled( httpServletRequest ) )
    {
      forward = actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    forward = super.savePassword( actionMapping, actionForm, httpServletRequest, httpServletResponse, false, true );
    if ( httpServletRequest.getAttribute( Globals.ERROR_KEY ) == null )
    {
      httpServletRequest.setAttribute( "passwordChanged", "true" );
    }
    return forward;
  }

  /** Save password if a new one has been entered, and always save recovery fields */
  public ActionForward changePasswordAndRecovery( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
      throws IOException
  {
    ActionForward forward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();
    ChangePasswordForm form = (ChangePasswordForm)actionForm;

    if ( isCancelled( httpServletRequest ) )
    {
      forward = actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    // only validate the password if it's being updated
    if ( isPasswordChangeRequested( form ) )
    {
      List<ServiceError> passwordErrors = getProfileService().validateOldPassword( UserManager.getUserId(), form.getOldPassword() );
      if ( !passwordErrors.isEmpty() )
      {
        writeValidationErrorsAsJsonResponse( httpServletResponse, passwordErrors );
        return null;
      }
    }

    forward = super.savePassword( actionMapping, actionForm, httpServletRequest, httpServletResponse, true, false );
    if ( httpServletRequest.getAttribute( Globals.ERROR_KEY ) == null )
    {
      httpServletRequest.setAttribute( "passwordChanged", "true" );
    }
    return forward;
  }

  public ActionForward cancel( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  private void writeValidationErrorsAsJsonResponse( HttpServletResponse response, List<ServiceError> errors ) throws IOException
  {
    WebErrorMessageList messages = new WebErrorMessageList();
    for ( ServiceError error : errors )
    {
      WebErrorMessage errMessage = new WebErrorMessage();
      String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
      errMessage.setText( errorMessage );
      errMessage.setName( CmsResourceBundle.getCmsBundle().getString( "login.loginpage.PASSWORD" ) );
      errMessage.setCode( WebResponseConstants.RESPONSE_CODE_VALIDATION_ERROR );
      errMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      messages.getMessages().add( errMessage );
    }
    super.writeAsJsonToResponse( messages, response );
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)BeanLocator.getBean( ProfileService.BEAN_NAME );
  }

}
