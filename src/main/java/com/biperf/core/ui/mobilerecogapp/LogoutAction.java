
package com.biperf.core.ui.mobilerecogapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.mobileapp.recognition.service.MobileNotificationService;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

public class LogoutAction extends BaseDispatchAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    AuthenticatedUser authenticatedUser = UserManager.getUser();

    if ( authenticatedUser != null )
    {
      // delete the user's mobile login token
      getUserService().deleteLoginTokenFor( authenticatedUser.getUserId() );

      // delete devices registered to that user so notifications are no longer
      // sent to those devices
      getMobileNotificationService().deleteUserDevices( authenticatedUser.getUserId() );
    }

    request.getSession().invalidate();

    WebErrorMessageList responseMessages = new WebErrorMessageList();
    super.writeAsJsonToResponse( responseMessages, response );

    return null;

  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private MobileNotificationService getMobileNotificationService()
  {
    return (MobileNotificationService)BeanLocator.getBean( MobileNotificationService.BEAN_NAME );
  }
}
