package com.biperf.core.ui.admin.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

public class EventAdminAction extends BaseDispatchAction
{
  protected ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm aForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

}
