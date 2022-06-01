
package com.biperf.core.ui.homepage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class TextMsgController extends BaseController
{
  /**
   * 
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    // SMSOnOff change request start
    Long userId = UserManager.getUserId();
    UserAddress userPrimaryAddress = getUserService().getPrimaryUserAddress( userId );
    if ( userPrimaryAddress != null )
    {
      request.getSession().setAttribute( "allowTextMessages", userPrimaryAddress.getAddress().getCountry().getAllowSms() );
    }
    // SMSOnOff change request end
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
