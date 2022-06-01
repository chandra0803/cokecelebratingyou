/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/process/impl/ProcessServiceImplTest.java,v $
 */

package com.biperf.core.service.process.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.easymock.EasyMock;
import org.jmock.Mock;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.process.ProcessDAO;
import com.biperf.core.domain.enums.DayOfMonthType;
import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessRoleEdit;
import com.biperf.core.domain.process.ProcessRoleLaunch;
import com.biperf.core.domain.process.ProcessRoleViewLog;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.process.ScheduledProcess;
import com.biperf.core.domain.user.Role;
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
public class ProcessServiceImplTest extends BaseServiceTest
{
  /**
   * 2:20 am
   */
  private static final Long TEST_TIME_OF_DAY_MILLIS = new Long( ( DateUtils.MILLIS_PER_HOUR * 2 ) + ( DateUtils.MILLIS_PER_MINUTE * 20 ) );

  private Scheduler schedulerMock;
  private OracleSequenceDAO oracleSequenceDAOMock;
  private ProcessServiceImpl processServiceImpl = new ProcessServiceImpl();

  private Mock mockProcessDAO = null;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    schedulerMock = EasyMock.createMock( Scheduler.class );
    processServiceImpl.setScheduler( schedulerMock );

    oracleSequenceDAOMock = EasyMock.createMock( OracleSequenceDAO.class );
    processServiceImpl.setOracleSequenceDAO( oracleSequenceDAOMock );

    mockProcessDAO = new Mock( ProcessDAO.class );
    processServiceImpl.setProcessDAO( (ProcessDAO)mockProcessDAO.proxy() );
    if ( !TransactionSynchronizationManager.isSynchronizationActive() )
    {
      // Used for testing scheduleProcess()
      TransactionSynchronizationManager.initSynchronization();
    }

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

    Process expectedProcess = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    expectedProcess.setId( new Long( 1 ) );

    // ProcessDAO expected to call save once with the Process
    // which will return the Process expected
    mockProcessDAO.expects( once() ).method( "save" ).with( same( expectedProcess ) ).will( returnValue( expectedProcess ) );

    // make the service call
    Process actualProcess = processServiceImpl.save( expectedProcess );

    assertEquals( "Actual Process didn't match with what is expected", expectedProcess, actualProcess );

    mockProcessDAO.verify();
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
    launchRole.setId( new Long( 2000 ) );
    editRole.setId( new Long( 3000 ) );

    Process expectedProcess = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    expectedProcess.setId( new Long( 1 ) );
    expectedProcess.setVersion( new Long( 1 ) );

    mockProcessDAO.expects( once() ).method( "getProcessById" ).with( same( expectedProcess.getId() ) ).will( returnValue( expectedProcess ) );

    Process processFromDatabase = processServiceImpl.getProcessById( expectedProcess.getId() );

    assertEquals( "Actual Process does not match to what was expected", processFromDatabase, expectedProcess );

    mockProcessDAO.verify();
  }

  /**
   * Test to save a process and get it back by id.
   */
  public void testGetProcessListByStatus()
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
    launchRole.setId( new Long( 2000 ) );
    editRole.setId( new Long( 3000 ) );

    Process expectedProcess = buildProcess( suffix, status, editRole, launchRole, viewLogRole );
    expectedProcess.setId( new Long( 1 ) );
    expectedProcess.setVersion( new Long( 1 ) );

    List processList = new ArrayList();
    processList.add( expectedProcess );

    mockProcessDAO.expects( once() ).method( "getProcessListByStatus" ).with( same( status ) ).will( returnValue( processList ) );

    List returnedProcessList = processServiceImpl.getProcessListByStatus( status );

    assertEquals( "Actual Process does not match to what was expected", returnedProcessList.size(), 1 );

    mockProcessDAO.verify();
  }

  /*
   * Test method for 'com.biperf.core.service.process.impl.ProcessServiceImpl.getSchedules(Long)'
   */
  public void testGetSchedules() throws Exception
  {
    Long processId = new Long( 2 );

    String expectedJobName = "ProcessBeanJob-2";
    String expectedGroupName = "ProcessBeanJobGroup-2";
    Trigger[] mockTriggers = new Trigger[2];

    SimpleTriggerImpl mockTrigger1 = new SimpleTriggerImpl();
    mockTrigger1.setName( "trigger1" );
    mockTrigger1.setJobDataMap( new JobDataMap() );
    mockTrigger1.setStartTime( new Date() );

    SimpleTriggerImpl mockTrigger2 = new SimpleTriggerImpl();
    mockTrigger2.setName( "trigger2" );
    mockTrigger2.setJobDataMap( new JobDataMap() );
    mockTrigger2.setStartTime( new Date() );

    mockTriggers[0] = mockTrigger1;
    mockTriggers[1] = mockTrigger2;

    List mockTriggerList = Arrays.asList( mockTriggers );

    EasyMock.expect( schedulerMock.getTriggersOfJob( new JobKey( expectedJobName, expectedGroupName ) ) ).andReturn( mockTriggerList );
    EasyMock.replay( schedulerMock );

    mockProcessDAO.expects( once() ).method( "getProcessById" ).with( same( processId ) ).will( returnValue( new Process() ) );

    List actualSchedules = processServiceImpl.getSchedules( processId );

    assertEquals( 2, actualSchedules.size() );

    for ( Iterator iter = actualSchedules.iterator(); iter.hasNext(); )
    {
      ScheduledProcess scheduledProcess = (ScheduledProcess)iter.next();
      assertNotNull( scheduledProcess.getName() );
      assertNotNull( scheduledProcess.getProcessParameterStringArrayMap() );
      assertNotNull( scheduledProcess.getProcessSchedule() );
      assertNotNull( scheduledProcess.getProcess() );
    }
  }

  /*
   * Test method for 'com.biperf.core.service.process.impl.ProcessServiceImpl.launchProcess(Process,
   * Map)'
   */
  public void testLaunchProcess()
  {
    long nextVal = 1L;

    EasyMock.expect( oracleSequenceDAOMock.getOracleSequenceNextValue( "process_trigger_sq" ) ).andReturn( nextVal );
    EasyMock.replay( oracleSequenceDAOMock );

    // nothing to assert other than no exception, since returns a void
    processServiceImpl.launchProcess( new Process(), new HashMap(), null );

  }

  /*
   * Test method for
   * 'com.biperf.core.service.process.impl.ProcessServiceImpl.scheduleProcess(Process,
   * ProcessSchedule, Map)'
   */
  public void testScheduleProcess()
  {
    long nextVal = 1L;

    EasyMock.expect( oracleSequenceDAOMock.getOracleSequenceNextValue( "process_trigger_sq" ) ).andReturn( nextVal );
    EasyMock.replay( oracleSequenceDAOMock );

    ProcessSchedule processSchedule = buildMonthlyProcessSchedule();

    // nothing to assert other than no exception, since returns a void
    processServiceImpl.scheduleProcess( new Process(), processSchedule, new HashMap(), null );
  }

  public static void assertProcessScheduleEquals( ProcessSchedule expectedProcessSchedule, ProcessSchedule actualProcessSchedule )
  {
    assertEquals( expectedProcessSchedule.getStartDate(), actualProcessSchedule.getStartDate() );
    assertEquals( expectedProcessSchedule.getTimeOfDayMillis(), actualProcessSchedule.getTimeOfDayMillis() );
    assertEquals( expectedProcessSchedule.getEndDate(), actualProcessSchedule.getEndDate() );
    assertEquals( expectedProcessSchedule.getDayOfMonthType(), actualProcessSchedule.getDayOfMonthType() );
    assertEquals( expectedProcessSchedule.getDayOfWeekType(), actualProcessSchedule.getDayOfWeekType() );
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

  public static ProcessSchedule buildMonthlyProcessSchedule()
  {
    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.MONTHLY ) );
    processSchedule.setDayOfMonthType( DayOfMonthType.lookup( "1" ) );
    processSchedule.setTimeOfDayMillis( TEST_TIME_OF_DAY_MILLIS );
    processSchedule.setStartDate( com.biperf.core.utils.DateUtils.toStartDate( new Date() ) );
    processSchedule.setEndDate( new Date( new Date().getTime() + ( 31 * DateUtils.MILLIS_PER_DAY ) ) );
    return processSchedule;
  }

  public static ProcessSchedule buildWeeklyProcessSchedule()
  {
    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.WEEKLY ) );
    processSchedule.setDayOfWeekType( DayOfWeekType.lookup( "1" ) );
    processSchedule.setTimeOfDayMillis( TEST_TIME_OF_DAY_MILLIS );
    processSchedule.setStartDate( com.biperf.core.utils.DateUtils.toStartDate( new Date() ) );
    processSchedule.setEndDate( new Date( new Date().getTime() + ( 14 * DateUtils.MILLIS_PER_DAY ) ) );
    return processSchedule;
  }

  public static ProcessSchedule buildDailyProcessSchedule()
  {
    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.DAILY ) );
    processSchedule.setTimeOfDayMillis( TEST_TIME_OF_DAY_MILLIS );
    processSchedule.setStartDate( com.biperf.core.utils.DateUtils.toStartDate( new Date() ) );
    processSchedule.setEndDate( new Date( new Date().getTime() + ( 14 * DateUtils.MILLIS_PER_DAY ) ) );
    return processSchedule;
  }

  public static ProcessSchedule buildOneTimeOnlyProcessSchedule()
  {
    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );
    processSchedule.setTimeOfDayMillis( TEST_TIME_OF_DAY_MILLIS );
    Date tomorrow = new Date( new Date().getTime() + DateUtils.MILLIS_PER_DAY );
    processSchedule.setStartDate( com.biperf.core.utils.DateUtils.toStartDate( tomorrow ) );
    return processSchedule;
  }

}
