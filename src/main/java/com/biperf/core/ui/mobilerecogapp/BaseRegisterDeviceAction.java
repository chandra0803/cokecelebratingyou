
package com.biperf.core.ui.mobilerecogapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.mobileapp.recognition.domain.DeviceType;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.utils.UserManager;

public abstract class BaseRegisterDeviceAction extends BaseUserDeviceAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String registrationId = request.getParameter( "registrationId" );
    String debug = request.getParameter( "isDebug" );

    boolean isDebug = Boolean.parseBoolean( debug );

    UserDevice userDevice = getMobileNotificationService().createUserDeviceFor( UserManager.getUserId(), getDeviceType(), registrationId, isDebug );

    writeAsJsonToResponse( new SuccessfulResponse(), response );
    return null;
  }

  protected abstract DeviceType getDeviceType();
}
