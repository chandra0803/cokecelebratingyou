/**
 * 
 */

package com.biperf.core.ui.process;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ScheduledProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.util.ProcessUtil;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

/**
 * ProcessScheduleListAction.
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
public class ProcessScheduleListAction extends BaseDispatchAction
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

    ProcessScheduleListForm processScheduleListForm = (ProcessScheduleListForm)actionForm;

    processScheduleListForm.setMethod( "save" );

    String processId = processScheduleListForm.getProcessId();

    if ( processId != null && processId.length() > 0 )
    {

      List scheduleList = getProcessService().getSchedules( new Long( processId ) );
      request.setAttribute( "scheduleList", scheduleList );

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BaseAssociationRequest()
      {
        public void execute( Object domainObject )
        {
          Process process = (Process)domainObject;
          initialize( process.getLaunchRoles() );
        }
      } );

      Process process = getProcessService().getProcessById( new Long( processId ), associationRequestCollection );
      processScheduleListForm.setProcessName( process.getName() );
      request.setAttribute( "process", process );

      // Format the parameter values for display
      for ( Iterator iter = scheduleList.iterator(); iter.hasNext(); )
      {
        ScheduledProcess scheduledProcess = (ScheduledProcess)iter.next();
        Map unformattedProcessParameterMap = scheduledProcess.getProcessParameterStringArrayMap();
        Map formattedParameterValueMap = ProcessUtil.getFormattedParameterValueMap( process, unformattedProcessParameterMap );
        scheduledProcess.setProcessParameterStringArrayMap( formattedParameterValueMap );
      }

    }

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
  public ActionForward removeSchedules( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    ProcessScheduleListForm processScheduleListForm = (ProcessScheduleListForm)actionForm;

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    String[] removeSchedules = processScheduleListForm.getRemoveSchedules();
    if ( null != removeSchedules && removeSchedules.length > 0 )
    {
      Long processId = new Long( processScheduleListForm.getProcessId() );
      for ( int i = 0; i < removeSchedules.length; i++ )
      {
        getProcessService().removeProcessSchedule( processId, removeSchedules[i] );
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return display( mapping, actionForm, request, response );
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
