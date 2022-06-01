
package com.biperf.core.ui.mobilerecogapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.mobileapp.recognition.service.MobileNotificationService;
import com.biperf.core.ui.BaseDispatchAction;

public abstract class BaseMobileDeviceAction extends BaseDispatchAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    return null;
  }

  protected MobileNotificationService getMobileNotificationService()
  {
    return (MobileNotificationService)getService( MobileNotificationService.BEAN_NAME );
  }

}
