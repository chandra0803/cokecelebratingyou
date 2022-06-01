/**
 * 
 */

package com.biperf.core.ui.process;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;

/**
 * ProcessSchedulesAction.
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
public class ScheduleProcessAction extends BaseDispatchAction
{
  /**
   * unspecified will display list
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  }

  /**
   * unspecified will display list
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.DISPLAY_FORWARD;

    ScheduleProcessForm scheduleProcessForm = (ScheduleProcessForm)actionForm;

    scheduleProcessForm.setMethod( "save" );

    String processId = scheduleProcessForm.getProcessId();

    if ( processId != null && processId.length() > 0 )
    {
      Process process = getProcessService().getProcessById( new Long( processId ) );
      scheduleProcessForm.setProcessName( process.getName() );
    }
    request.setAttribute( ScheduleProcessForm.FORM_NAME, scheduleProcessForm );
    return mapping.findForward( forwardTo );
  }

  /**
   * save the process information
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    ScheduleProcessForm scheduleProcessForm = (ScheduleProcessForm)actionForm;

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ProcessSchedule schedule = scheduleProcessForm.toDomain();

    Process process = getProcessService().getProcessById( new Long( scheduleProcessForm.getProcessId() ) );
    Map valueProcessParameterMap = scheduleProcessForm.returnParametersAsMap();

    getProcessService().scheduleProcess( process, schedule, valueProcessParameterMap, UserManager.getUserId() );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "processId", process.getId() );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ), "method=display" } );
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
