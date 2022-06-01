
package com.biperf.core.ui.homepage;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;

public class TrackFilterSelectionAction extends BaseDispatchAction
{

  /**
   * to prepare the recognition print on demand jasper report
   */

  /**
   * Overridden from @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String filterPage = request.getParameter( "filter" );
    if ( filterPage != null )
    {
      request.getSession().setAttribute( "filterName", filterPage );
    }
    response.setContentType( "application/json" );
    PrintWriter pw = response.getWriter();
    pw.write( "{}" );
    pw.flush();
    pw.close();
    return null;
  }
}
