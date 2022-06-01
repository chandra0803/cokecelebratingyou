
package com.biperf.core.ui.mobilerecogapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.utils.UserManager;

public class UnregisterDeviceAction extends BaseUserDeviceAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String registrationId = request.getParameter( "registrationId" );
    int deleted = getMobileNotificationService().deleteUserDevice( UserManager.getUserId(), registrationId );

    writeAsJsonToResponse( new BaseUserDeviceAction.SuccessfulResponse(), response );
    return null;
  }
}
