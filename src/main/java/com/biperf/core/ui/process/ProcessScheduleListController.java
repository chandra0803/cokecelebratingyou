/**
 * 
 */

package com.biperf.core.ui.process;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseController;

/**
 * ProcessScheduleListController.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>asondgeroth</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessScheduleListController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ProcessScheduleListForm form = (ProcessScheduleListForm)request.getAttribute( "processScheduleListForm" );
    if ( form != null && StringUtils.isNotBlank( form.getProcessId() ) )
    {
      request.setAttribute( "scheduleList", getProcessService().getSchedules( new Long( form.getProcessId() ) ) );
    }
  }

  /**
   * Get the processService from the applicationContext.
   * 
   * @return ProcessService
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

}
