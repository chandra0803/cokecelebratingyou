/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.process;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.user.Role;
import com.biperf.core.service.util.ProcessUtil;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Process extends BaseDomain
{
  public static final String SYSTEM_PROCESS_PREFIX = "__SYSTEM_PROCESS__";

  private String name;
  private Date processLastExecutedDate;
  private String processBeanName;
  private ProcessStatusType processStatusType;
  private String description;

  private Set editRoles = new LinkedHashSet();
  private Set launchRoles = new LinkedHashSet();
  private Set viewLogRoles = new LinkedHashSet();

  /**
   * @return value of description property
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description value for description property
   */
  public void setDescription( String description )
  {
    this.description = description;
  }

  /**
   * @return value of editRoles property
   */
  public Set getEditRoles()
  {
    return editRoles;
  }

  /**
   * @param editRoles value for editRoles property
   */
  public void setEditRoles( Set editRoles )
  {
    this.editRoles = editRoles;
  }

  /**
   * @param processRoleEdit role to add
   */
  public void addEditRole( ProcessRoleEdit processRoleEdit )
  {
    this.editRoles.add( processRoleEdit );
  }

  /**
   * Returns a comma-separated list of edit role names.
   * 
   * @return a comma-separated list of edit role names.
   */
  public String getEditRoleNames()
  {
    StringBuffer editRoleNames = new StringBuffer();

    boolean isFirst = true;
    for ( Iterator iter = editRoles.iterator(); iter.hasNext(); )
    {
      Role role = ( (ProcessRoleEdit)iter.next() ).getRole();
      if ( isFirst )
      {
        isFirst = false;
      }
      else
      {
        editRoleNames.append( "," );
      }
      editRoleNames.append( role.getCode() );
    }

    return editRoleNames.toString();
  }

  /**
   * @return value of launchRoles property
   */
  public Set getLaunchRoles()
  {
    return launchRoles;
  }

  /**
   * @param launchRoles value for launchRoles property
   */
  public void setLaunchRoles( Set launchRoles )
  {
    this.launchRoles = launchRoles;
  }

  /**
   * @param processRoleLaunch role to add
   */
  public void addLaunchRole( ProcessRoleLaunch processRoleLaunch )
  {
    this.launchRoles.add( processRoleLaunch );
  }

  /**
   * Returns a comma-separated list of launch role names.
   * 
   * @return a comma-separated list of launch role names.
   */
  public String getLaunchRoleNames()
  {
    StringBuffer launchRoleNames = new StringBuffer();

    boolean isFirst = true;
    for ( Iterator iter = launchRoles.iterator(); iter.hasNext(); )
    {
      Role role = ( (ProcessRoleLaunch)iter.next() ).getRole();
      if ( isFirst )
      {
        isFirst = false;
      }
      else
      {
        launchRoleNames.append( "," );
      }
      launchRoleNames.append( role.getCode() );
    }

    return launchRoleNames.toString();
  }

  /**
   * @return value of name property
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name value for name property
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * @return value of processLastExecutedDate property
   */
  public Date getProcessLastExecutedDate()
  {
    return processLastExecutedDate;
  }

  /**
   * @param processLastExecutedDate value of processLastExecutedDate property
   */
  public void setProcessLastExecutedDate( Date processLastExecutedDate )
  {
    this.processLastExecutedDate = processLastExecutedDate;
  }

  /**
   * @return value of processBeanName property
   */
  public String getProcessBeanName()
  {
    return processBeanName;
  }

  /**
   * @param processBeanName value for processBeanName property
   */
  public void setProcessBeanName( String processBeanName )
  {
    this.processBeanName = processBeanName;
  }

  /**
   * @return Map of ProcessParameter objects keyed by ProcessParameter name.
   */
  public Map getProcessParameters()
  {
    return ProcessUtil.getProcessParameterDefinitions( processBeanName );
  }

  // Not used since params come from spring metadata - left here to make this point.
  // public void setProcessParameters( Map processParameters )
  // {
  // this.processParameters = processParameters;
  // }

  /**
   * @return value of processStatusType property
   */
  public ProcessStatusType getProcessStatusType()
  {
    return processStatusType;
  }

  /**
   * @param processStatusType value for processStatusType property
   */
  public void setProcessStatusType( ProcessStatusType processStatusType )
  {
    this.processStatusType = processStatusType;
  }

  /**
   * Returns true if this process is active; returns false otherwise.
   * 
   * @return true if this process is active; returns false otherwise.
   */
  public boolean isActive()
  {
    return processStatusType != null && processStatusType.getCode().equalsIgnoreCase( ProcessStatusType.ACTIVE );
  }

  /**
   * @return value of viewLogRoles property
   */
  public Set getViewLogRoles()
  {
    return viewLogRoles;
  }

  /**
   * @param viewLogRoles value for viewLogRoles property
   */
  public void setViewLogRoles( Set viewLogRoles )
  {
    this.viewLogRoles = viewLogRoles;
  }

  /**
   * @param processRoleViewLog role to add
   */
  public void addViewLogRole( ProcessRoleViewLog processRoleViewLog )
  {
    this.viewLogRoles.add( processRoleViewLog );
  }

  /**
   * Returns a comma-separated list of view log role names.
   * 
   * @return a comma-separated list of view log role names.
   */
  public String getViewLogRoleNames()
  {
    StringBuffer viewLogRoleNames = new StringBuffer();

    boolean isFirst = true;
    for ( Iterator iter = viewLogRoles.iterator(); iter.hasNext(); )
    {
      Role role = ( (ProcessRoleViewLog)iter.next() ).getRole();
      if ( isFirst )
      {
        isFirst = false;
      }
      else
      {
        viewLogRoleNames.append( "," );
      }
      viewLogRoleNames.append( role.getCode() );
    }

    return viewLogRoleNames.toString();
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @param object
   * @return boolean
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Process ) )
    {
      return false;
    }

    final Process rhs = (Process)object;

    if ( name != null )
    {
      if ( !name.equals( rhs.getName() ) )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @return int
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    return name != null ? name.hashCode() : 0;
  }

  /**
   * Returns true if this is a system process and false otherwise.
   * 
   * @return true if this is a system process and false otherwise.
   */
  public boolean isSystemProcess()
  {
    return name.startsWith( SYSTEM_PROCESS_PREFIX );
  }
}
