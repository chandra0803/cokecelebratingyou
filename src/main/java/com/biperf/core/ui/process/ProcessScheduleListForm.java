/**
 * 
 */

package com.biperf.core.ui.process;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseForm;

/**
 * ProcessScheduleListForm.
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
public class ProcessScheduleListForm extends BaseForm
{
  private String processId;
  private String method;
  private String processName;
  private String[] removeSchedules = null;

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    removeSchedules = request.getParameterValues( "removeSchedules" );
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getProcessId()
  {
    return processId;
  }

  public void setProcessId( String processId )
  {
    this.processId = processId;
  }

  public String getProcessName()
  {
    return processName;
  }

  public void setProcessName( String processName )
  {
    this.processName = processName;
  }

  public String[] getRemoveSchedules()
  {
    return removeSchedules;
  }

  public void setRemoveSchedules( String[] removeSchedules )
  {
    this.removeSchedules = removeSchedules;
  }

}
