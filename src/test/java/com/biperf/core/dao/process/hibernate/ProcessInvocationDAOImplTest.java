/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/process/hibernate/ProcessInvocationDAOImplTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.process.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.process.ProcessDAO;
import com.biperf.core.dao.process.ProcessInvocationDAO;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.process.ProcessInvocationComment;
import com.biperf.core.domain.process.ProcessInvocationParameter;
import com.biperf.core.domain.process.ProcessInvocationParameterValue;
import com.biperf.core.domain.process.ProcessRoleEdit;
import com.biperf.core.domain.process.ProcessRoleLaunch;
import com.biperf.core.domain.process.ProcessRoleViewLog;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * ProcessInvocationDAOImplTest.
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
 * <td>leep</td>
 * <td>November 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessInvocationDAOImplTest extends BaseDAOTest
{
  /**
   * Get the ProcessDAO through bean look-up.
   * 
   * @return ProcessDAO
   */
  protected ProcessInvocationDAO getProcessInvocationDAO()
  {
    return (ProcessInvocationDAO)ApplicationContextFactory.getApplicationContext().getBean( "processInvocationDAO" );
  }

  /**
   * Get the ProcessDAO through bean look-up.
   * 
   * @return ProcessDAO
   */
  protected ProcessDAO getProcessDAO()
  {
    return (ProcessDAO)ApplicationContextFactory.getApplicationContext().getBean( "processDAO" );
  }

  /**
   * Get the roleDAO from the beanFactory.
   * 
   * @return RoleDAO
   */
  protected RoleDAO getRoleDAO()
  {
    return (RoleDAO)ApplicationContextFactory.getApplicationContext().getBean( "roleDAO" );
  }

  /**
   * Get the userDAO from the beanFactory.
   * 
   * @return UserDAO
   */
  protected UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  /**
   * Saves a ProductCategory in database
   */
  public void testSaveProcessInvocation()
  {
    String suffix = "7";
    String status = "active";

    Role editRole = buildRole( "edit" + suffix );
    Role launchRole = buildRole( "launch" + suffix );
    Role viewLogRole = buildRole( "viewLog" + suffix );

    getRoleDAO().saveRole( editRole );
    getRoleDAO().saveRole( launchRole );
    getRoleDAO().saveRole( viewLogRole );

    Process process = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    User runAsUser = buildStaticUser( "testUSERNAME-2S4", "b", "c" );

    getProcessDAO().save( process );
    getUserDAO().saveUser( runAsUser );
    HibernateSessionManager.getSession().flush();

    assertNotNull( process.getId() );

    ProcessInvocation processInvocation = buildProcessInvocation( process, runAsUser, suffix );

    getProcessInvocationDAO().save( processInvocation );
    HibernateSessionManager.getSession().flush();

    assertNotNull( processInvocation.getId() );
  }

  /**
   * Tests create, update and selecting the product by the Id.
   */
  public void testGetProcessInvocationById()
  {
    String suffix = "7";
    String status = "active";

    Role editRole = buildRole( "edit" + suffix );
    Role launchRole = buildRole( "launch" + suffix );
    Role viewLogRole = buildRole( "viewLog" + suffix );

    getRoleDAO().saveRole( editRole );
    getRoleDAO().saveRole( launchRole );
    getRoleDAO().saveRole( viewLogRole );

    Process process = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    User runAsUser = buildStaticUser( "testUSERNAME-2S4", "b", "c" );

    getProcessDAO().save( process );
    getUserDAO().saveUser( runAsUser );
    HibernateSessionManager.getSession().flush();

    assertNotNull( process.getId() );

    ProcessInvocation expectedProcessInvocation = buildProcessInvocation( process, runAsUser, suffix );

    getProcessInvocationDAO().save( expectedProcessInvocation );

    flushAndClearSession();

    ProcessInvocation savedProcessInvocation = getProcessInvocationDAO().getProcessInvocationById( expectedProcessInvocation.getId() );

    assertEquals( "Actual ProcessInvocation id doesn't match with expected", expectedProcessInvocation.getId(), savedProcessInvocation.getId() );
  }

  /**
   * Saves a ProductCategory in database
   */
  public void testGetProcessInvocationByProcess()
  {
    String roleSuffix = "4";
    String processSuffix1 = "5";
    String processSuffix2 = "6";
    String processInvocationSuffix1 = "7";
    String processInvocationSuffix2 = "8";
    String processInvocationSuffix3 = "9";
    String status = "active";

    Role editRole = buildRole( "edit" + roleSuffix );
    Role launchRole = buildRole( "launch" + roleSuffix );
    Role viewLogRole = buildRole( "viewLog" + roleSuffix );

    getRoleDAO().saveRole( editRole );
    getRoleDAO().saveRole( launchRole );
    getRoleDAO().saveRole( viewLogRole );

    Process process1 = buildProcess( processSuffix1, status, editRole, launchRole, viewLogRole );
    Process process2 = buildProcess( processSuffix2, status, editRole, launchRole, viewLogRole );
    User runAsUser = buildStaticUser( "testUSERNAME-2S4", "b", "c" );

    getProcessDAO().save( process1 );
    getProcessDAO().save( process2 );
    getUserDAO().saveUser( runAsUser );
    HibernateSessionManager.getSession().flush();

    assertNotNull( process1.getId() );
    assertNotNull( process2.getId() );

    ProcessInvocation processInvocation1 = buildProcessInvocation( process1, runAsUser, processInvocationSuffix1 );
    ProcessInvocation processInvocation2 = buildProcessInvocation( process1, runAsUser, processInvocationSuffix2 );
    ProcessInvocation processInvocation3 = buildProcessInvocation( process2, runAsUser, processInvocationSuffix3 );

    getProcessInvocationDAO().save( processInvocation1 );
    getProcessInvocationDAO().save( processInvocation2 );
    getProcessInvocationDAO().save( processInvocation3 );
    HibernateSessionManager.getSession().flush();

    List processInvocationList = getProcessInvocationDAO().getProcessInvocationsByProcess( process1 );

    assertEquals( processInvocationList.size(), 2 );
  }

  /**
   * Saves a ProductCategory in database
   */
  public static ProcessInvocation buildProcessInvocation( Process process, User runAsUser, String suffix )
  {
    ProcessInvocation processInvocation = new ProcessInvocation();
    processInvocation.setProcess( process );
    processInvocation.setStartDate( new Date() );
    processInvocation.setEndDate( new Date() );

    processInvocation.setRunAsUser( runAsUser );

    ProcessInvocationComment processInvocationComment = new ProcessInvocationComment();
    processInvocationComment.setComments( "Comments" );
    List comments = new ArrayList();
    comments.add( processInvocationComment );
    processInvocation.setProcessInvocationComments( comments );

    ProcessInvocationParameterValue processInvocationParameterValue = new ProcessInvocationParameterValue();
    processInvocationParameterValue.setValue( "25" );

    ProcessInvocationParameter processInvocationParameter = new ProcessInvocationParameter();
    processInvocationParameter.setProcessParameterName( "param1" );
    processInvocationParameter.addProcessInvocationParameterValue( processInvocationParameterValue );
    processInvocation.addProcessInvocationParameter( processInvocationParameter );

    return processInvocation;
  }

  /**
   * Saves a ProductCategory in database
   */
  public static Process buildProcess( String suffix, String status, Role editRole, Role launchRole, Role viewLogRole )
  {
    Process process = new Process();

    process.setName( "testProcess" + suffix );
    process.setProcessLastExecutedDate( new Date() );
    process.setProcessBeanName( "testProcessBeanName" + suffix );
    if ( status.equals( "active" ) )
    {
      process.setProcessStatusType( ProcessStatusType.lookup( ProcessStatusType.ACTIVE ) );
    }
    else
    {
      process.setProcessStatusType( ProcessStatusType.lookup( ProcessStatusType.INACTIVE ) );
    }
    process.setDescription( "testProcessDescription" + suffix );

    ProcessRoleEdit processRoleEdit = new ProcessRoleEdit();
    processRoleEdit.setProcess( process );
    processRoleEdit.setRole( editRole );
    LinkedHashSet processRoleSetEdit = new LinkedHashSet();
    processRoleSetEdit.add( processRoleEdit );

    ProcessRoleLaunch processRoleLaunch = new ProcessRoleLaunch();
    processRoleLaunch.setProcess( process );
    processRoleLaunch.setRole( launchRole );
    LinkedHashSet processRoleSetLaunch = new LinkedHashSet();
    processRoleSetLaunch.add( processRoleLaunch );

    ProcessRoleViewLog processRoleViewLog = new ProcessRoleViewLog();
    processRoleViewLog.setProcess( process );
    processRoleViewLog.setRole( viewLogRole );
    LinkedHashSet processRoleSetViewLog = new LinkedHashSet();
    processRoleSetViewLog.add( processRoleViewLog );

    process.setEditRoles( processRoleSetEdit );
    process.setLaunchRoles( processRoleSetLaunch );
    process.setViewLogRoles( processRoleSetViewLog );

    return process;
  }

  /**
   * Builds a static representation of a productCategory.
   * 
   * @param suffix
   * @return ProductCategory
   */
  public static Role buildRole( String suffix )
  {
    Role editRole = new Role();
    editRole.setCode( "testCode" + suffix );
    editRole.setHelpText( "testHELPTEXT" + suffix );
    editRole.setName( "testNAME" + suffix );
    editRole.setActive( Boolean.valueOf( true ) );

    return editRole;
  }

  /**
   * Builds a static user with the params.
   * 
   * @param userName
   * @param firstName
   * @param lastName
   * @return User
   */
  public static User buildStaticUser( String userName, String firstName, String lastName )
  {

    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setUserName( userName );
    user.setFirstName( firstName );
    user.setLastName( lastName );
    user.setPassword( "testPASSWORD" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.TRUE );

    return user;
  }
}
