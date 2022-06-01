
package com.biperf.core.ui.ssi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.ui.BaseDispatchAction;

public class SSIContestProgressLoadAction extends BaseDispatchAction
{

  /**
   * Upload Contest Progress
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadProgress( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // String fileName = request.getParameter( "fileName" );
    // Map<String, Object> queryParams = new HashMap<String, Object>();
    // queryParams.put( "fileName", fileName );
    // try
    // {
    // getSSIContestService().loadContestProgress( queryParams );
    // }
    // catch( Exception e )
    // {
    // log.error( e );
    // }
    return null;
  }

  protected SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

}
