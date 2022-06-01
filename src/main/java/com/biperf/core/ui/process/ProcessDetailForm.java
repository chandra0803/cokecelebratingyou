/**
 * 
 */

package com.biperf.core.ui.process;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessRole;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ProcessDetailForm.
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
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessDetailForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String processId;
  private String name;
  private String program;
  private String status;
  private String description;
  private String[] editRoles = null;
  private String[] launchRoles = null;
  private String[] viewLogRoles = null;

  // Non domain data
  private boolean isSystemProcess;
  private String method;

  // ---------------------------------------------------------------------------
  // Reset, Load, and To Domain Methods
  // ---------------------------------------------------------------------------

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    editRoles = request.getParameterValues( "editRoles" );
    launchRoles = request.getParameterValues( "launchRoles" );
    viewLogRoles = request.getParameterValues( "viewLogRoles" );
    program = "";
    processId = "";
    name = "";
    program = "";
    status = "";
    description = "";
    isSystemProcess = false;
  }

  public void load( Process process )
  {
    this.name = process.getName();
    this.program = process.getProcessBeanName();
    this.status = process.getProcessStatusType().getCode();
    this.description = process.getDescription();
    this.isSystemProcess = process.isSystemProcess();

    Iterator editRolesIter = process.getEditRoles().iterator();
    editRoles = new String[process.getEditRoles().size()];
    for ( int i = 0; editRolesIter.hasNext(); i++ )
    {
      ProcessRole role = (ProcessRole)editRolesIter.next();
      editRoles[i] = role.getRole().getId().toString();
    }

    Iterator launchRolesIter = process.getLaunchRoles().iterator();
    launchRoles = new String[process.getLaunchRoles().size()];
    for ( int i = 0; launchRolesIter.hasNext(); i++ )
    {
      ProcessRole role = (ProcessRole)launchRolesIter.next();
      launchRoles[i] = role.getRole().getId().toString();
    }

    Iterator viewLogRolesIter = process.getViewLogRoles().iterator();
    viewLogRoles = new String[process.getViewLogRoles().size()];
    for ( int i = 0; viewLogRolesIter.hasNext(); i++ )
    {
      ProcessRole role = (ProcessRole)viewLogRolesIter.next();
      viewLogRoles[i] = role.getRole().getId().toString();
    }
  }

  public Process toDomain()
  {
    return toDomain( new Process() );
  }

  public Process toDomain( Process process )
  {

    process.setName( this.name );
    process.setDescription( this.description );
    process.setProcessStatusType( ProcessStatusType.lookup( this.status ) );
    process.setProcessBeanName( this.program );

    // NOTE: Parameters are read-only

    return process;
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the data in this form.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return <code>ActionErrors</code> object that encapsulates any validation errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( !isSystemProcess )
    {
      actionErrors = validateUserProcess( actionErrors );
    }

    return actionErrors;
  }

  private ActionErrors validateUserProcess( ActionErrors actionErrors )
  {
    // A user process must have a name.
    if ( name == null || name.length() == 0 )
    {
      actionErrors.add( "name", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "process.detail", "NAME" ) ) );
    }

    // A user process cannot have a name that is reserved for a system process.
    if ( name != null && name.toUpperCase().startsWith( Process.SYSTEM_PROCESS_PREFIX.toUpperCase() ) )
    {
      actionErrors.add( "name", new ActionMessage( "process.detail.INVALID_NAME_PREFIX", Process.SYSTEM_PROCESS_PREFIX ) );
    }

    // A process name must be unique.
    if ( name != null && name.length() > 0 )
    {
      if ( processId == null || processId.length() == 0 )
      {
        // add process
        Process existingProcess = getProcessService().getProcessByName( name );
        if ( existingProcess != null )
        {
          actionErrors.add( "name", new ActionMessage( "system.errors.NOT_UNIQUE", CmsResourceBundle.getCmsBundle().getString( "process.detail", "NAME" ) ) );
        }
      }
      else
      {
        // edit process
        Process existingProcess = getProcessService().getProcessByName( name );
        if ( existingProcess != null && !existingProcess.getId().equals( new Long( processId ) ) )
        {
          actionErrors.add( "name", new ActionMessage( "system.errors.NOT_UNIQUE", CmsResourceBundle.getCmsBundle().getString( "process.detail", "NAME" ) ) );
        }
      }
    }

    // A user process must have a program.
    if ( program == null || program.length() == 0 )
    {
      actionErrors.add( "program", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "process.detail", "PROGRAM" ) ) );
    }

    // A user process must have a status.
    if ( status == null || status.length() == 0 )
    {
      actionErrors.add( "status", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "process.detail", "STATUS" ) ) );
    }

    // A user process must have one or more launch roles.
    if ( launchRoles == null || launchRoles.length == 0 )
    {
      actionErrors.add( "launchRoles", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "process.detail", "LAUNCH_RIGHTS" ) ) );
    }

    return actionErrors;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String[] getEditRoles()
  {
    return editRoles;
  }

  public void setEditRoles( String[] editRoles )
  {
    this.editRoles = editRoles;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String[] getLaunchRoles()
  {
    return launchRoles;
  }

  public void setLaunchRoles( String[] launchRoles )
  {
    this.launchRoles = launchRoles;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getProgram()
  {
    return program;
  }

  public void setProgram( String program )
  {
    this.program = program;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String[] getViewLogRoles()
  {
    return viewLogRoles;
  }

  public void setViewLogRoles( String[] viewLogRoles )
  {
    this.viewLogRoles = viewLogRoles;
  }

  public boolean getIsSystemProcess()
  {
    return isSystemProcess;
  }

  public void setIsSystemProcess( boolean isSystemProcess )
  {
    this.isSystemProcess = isSystemProcess;
  }

  public boolean getIsInactiveProcess()
  {
    return status != null && status.length() > 0 && status.equalsIgnoreCase( ProcessStatusType.INACTIVE );
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

  // ---------------------------------------------------------------------------
  // Service Locator Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the Process service.
   * 
   * @return a reference to the Process service.
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
  }
}
