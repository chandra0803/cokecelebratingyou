
package com.biperf.core.ui.mobilerecogapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.profile.PersonalInfoAction;

public class AvatarUploadAction extends BaseMobileDeviceAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PersonalInfoAction pia = new PersonalInfoAction();
    return pia.uploadAvatar( mapping, form, request, response );
  }

}
