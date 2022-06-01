
package com.biperf.core.ui.approvals;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.SSIContestUtil;

public class ApprovalsController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    if ( request.getSession().getAttribute( SSIContestUtil.SHOW_MODAL ) != null )
    {
      request.setAttribute( SSIContestUtil.SHOW_MODAL, request.getSession().getAttribute( SSIContestUtil.SHOW_MODAL ) );
      request.setAttribute( SSIContestUtil.MODAL_MESSAGE, request.getSession().getAttribute( SSIContestUtil.MODAL_MESSAGE ) );
      request.getSession().removeAttribute( SSIContestUtil.SHOW_MODAL );
      request.getSession().removeAttribute( SSIContestUtil.MODAL_MESSAGE );
    }
  }

}
