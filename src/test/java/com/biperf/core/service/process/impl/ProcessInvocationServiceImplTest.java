/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/process/impl/ProcessInvocationServiceImplTest.java,v $
 */

package com.biperf.core.service.process.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.process.ProcessInvocationDAO;
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
import com.biperf.core.service.BaseServiceTest;

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
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessInvocationServiceImplTest extends BaseServiceTest
{
  private ProcessInvocationServiceImpl processInvocationServiceImpl = new ProcessInvocationServiceImpl();

  private Mock mockProcessInvocationDAO = null;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockProcessInvocationDAO = new Mock( ProcessInvocationDAO.class );
    processInvocationServiceImpl.setProcessInvocationDAO( (ProcessInvocationDAO)mockProcessInvocationDAO.proxy() );
  }

  /**
   * Test to save a process.
   */
  public void testSave()
  {
    String suffix = "100";
    String status = "active";
    Role editRole;
    Role launchRole;
    Role viewLogRole;

    editRole = buildRole( "edit" + suffix );
    launchRole = buildRole( "launch" + suffix );
    viewLogRole = buildRole( "viewLog" + suffix );

    editRole.setId( new Long( 1000 ) );
    launchRole.setId( new Long( 1000 ) );
    editRole.setId( new Long( 1000 ) );

    Process process = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    process.setId( new Long( 1 ) );
    User runAsUser = buildStaticUser( "testUSERNAME-2S4", "b", "c" );

    ProcessInvocation processInvocation = buildProcessInvocation( process, runAsUser, suffix );

    // ProcessDAO expected to call save once with the Process
    // which will return the Process expected
    mockProcessInvocationDAO.expects( once() ).method( "save" ).with( same( processInvocation ) ).will( returnValue( processInvocation ) );

    // make the service call
    ProcessInvocation savedProcessInvocation = processInvocationServiceImpl.save( processInvocation );

    assertSame( "Actual ProcessInvocation didn't match with what is expected", savedProcessInvocation, processInvocation );

    mockProcessInvocationDAO.verify();
  }

  /**
   * Test to save a process and get it back by id.
   */
  public void testGetById()
  {
    String suffix = "100";
    String status = "active";
    Role editRole;
    Role launchRole;
    Role viewLogRole;

    editRole = buildRole( "edit" + suffix );
    launchRole = buildRole( "launch" + suffix );
    viewLogRole = buildRole( "viewLog" + suffix );

    editRole.setId( new Long( 1000 ) );
    launchRole.setId( new Long( 1000 ) );
    editRole.setId( new Long( 1000 ) );

    Process process = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    process.setId( new Long( 1 ) );
    User runAsUser = buildStaticUser( "testUSERNAME-2S4", "b", "c" );

    ProcessInvocation processInvocation = buildProcessInvocation( process, runAsUser, suffix );

    mockProcessInvocationDAO.expects( once() ).method( "getProcessInvocationById" ).with( same( processInvocation.getId() ) ).will( returnValue( processInvocation ) );

    ProcessInvocation dbProcessInvocation = processInvocationServiceImpl.getProcessInvocationById( processInvocation.getId() );

    assertSame( "Actual ProcessInvocation does not match to what was expected", dbProcessInvocation, processInvocation );

    mockProcessInvocationDAO.verify();
  }

  /**
   * Test to save a process and get it back by id.
   */
  public void testGetProcessInvocationsByProcess()
  {
    List expectedList = new ArrayList();
    String suffix = "100";
    String status = "active";
    Role editRole;
    Role launchRole;
    Role viewLogRole;

    editRole = buildRole( "edit" + suffix );
    launchRole = buildRole( "launch" + suffix );
    viewLogRole = buildRole( "viewLog" + suffix );

    editRole.setId( new Long( 1000 ) );
    launchRole.setId( new Long( 1000 ) );
    editRole.setId( new Long( 1000 ) );

    Process process = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    process.setId( new Long( 1 ) );
    User runAsUser = buildStaticUser( "testUSERNAME-2S4", "b", "c" );

    ProcessInvocation processInvocation = buildProcessInvocation( process, runAsUser, suffix );

    expectedList.add( processInvocation );

    mockProcessInvocationDAO.expects( once() ).method( "getProcessInvocationsByProcess" ).will( returnValue( expectedList ) );

    List actualList = processInvocationServiceImpl.getProcessInvocationsByProcess( process );

    assertSame( expectedList, actualList );

    mockProcessInvocationDAO.verify();

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

    LinkedHashSet parameterValueSet = new LinkedHashSet();
    ProcessInvocationParameterValue processInvocationParameterValue = new ProcessInvocationParameterValue();
    processInvocationParameterValue.setValue( "25" );
    parameterValueSet.add( processInvocationParameterValue );

    ProcessInvocationParameter processInvocationParameter = new ProcessInvocationParameter();
    processInvocationParameter.setProcessInvocationParameterValues( parameterValueSet );
    LinkedHashSet processInvocationParameterSet = new LinkedHashSet();
    processInvocationParameterSet.add( processInvocationParameter );
    processInvocation.setProcessInvocationParameters( processInvocationParameterSet );

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

    return user;
  }

}
