/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/process/impl/ProcessServiceImpl.java,v $
 */

package com.biperf.core.service.process.impl;

import static org.quartz.JobBuilder.newJob;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.process.ProcessDAO;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessRoleEdit;
import com.biperf.core.domain.process.ProcessRoleViewLog;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.process.ScheduledProcess;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.domain.promotion.ScheduledRecognition;
import com.biperf.core.domain.user.Role;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.process.ProcessBeanJob;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.util.ProcessUtil;
import com.biperf.core.service.util.QuartzProcessUtil;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.ProcessProxyFactoryBean;
import com.biperf.core.utils.QuartzScheduleJobTransactionSynchronization;

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
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessServiceImpl implements ProcessService
{
  private static final Log log = LogFactory.getLog( ProcessServiceImpl.class );

  private Scheduler scheduler;
  private ProcessDAO processDAO;
  private OracleSequenceDAO oracleSequenceDAO;
  private RoleService roleService;

  /**
   * @param id
   * @return Process
   */
  @Override
  public Process getProcessById( Long id )
  {
    return this.processDAO.getProcessById( id );
  }

  @Override
  public Process getProcessById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.processDAO.getProcessByIdWithAssociations( id, associationRequestCollection );
  }

  @Override
  public Process getProcessByName( String name )
  {
    return processDAO.getProcessByName( name );
  }

  @Override
  public Process getProcessByBeanName( String name )
  {
    return processDAO.getProcessByBeanName( name );
  }

  /**
   * @param status
   * @return List
   */
  @Override
  public List getProcessListByStatus( String status )
  {
    return processDAO.getProcessListByStatus( status );
  }

  @Override
  public List getProcessListByStatus( String status, AssociationRequestCollection associationRequestCollection, String processType )
  {
    return processDAO.getProcessListByStatus( status, associationRequestCollection, processType );
  }

  /**
   * @param process
   * @return Process
   */
  @Override
  public Process save( Process process )
  {
    // Save the process.
    Process savedProcess = processDAO.save( process );

    // If the process is inactive, remove its schedules.
    if ( process.getId() != null && !process.isActive() )
    {
      List scheduledProcesses = getSchedules( process.getId() );
      for ( Iterator iter = scheduledProcesses.iterator(); iter.hasNext(); )
      {
        ScheduledProcess scheduledProcess = (ScheduledProcess)iter.next();
        removeProcessSchedule( process.getId(), scheduledProcess.getName() );
      }
    }

    return savedProcess;
  }

  /**
   * Create or load a system process. A system process is a process such as "real-time" promo engine
   * running, where tasks are offloaded to the background to be run immediately. These processes are
   * not set up via the ui, so there is no existing Process object to tie them to.
   * 
   * @param processName
   * @param processBeanName
   * @return Process
   */
  @Override
  public Process createOrLoadSystemProcess( String processName, String processBeanName )
  {
    // Prefix process name to avoid the same process name via the UI.
    processName = Process.SYSTEM_PROCESS_PREFIX + processName;

    Process process = processDAO.getProcessByName( processName );

    if ( process == null )
    {
      process = new Process();
      process.setName( processName );
      process.setProcessBeanName( processBeanName );
      process.setProcessStatusType( ProcessStatusType.lookup( ProcessStatusType.ACTIVE ) );

      Role role = roleService.getRoleByCode( AuthorizationService.ROLE_CODE_BI_ADMIN );
      if ( role != null )
      {
        ProcessRoleEdit processRoleEdit = new ProcessRoleEdit( role, process );
        process.getEditRoles().add( processRoleEdit );

        ProcessRoleViewLog processRoleViewLog = new ProcessRoleViewLog( role, process );
        process.getViewLogRoles().add( processRoleViewLog );
      }

      process = save( process );
    }
    return process;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.process.ProcessService#removeProcessSchedule(java.lang.Long,
   *      java.lang.String)
   * @param processId
   * @param scheduleName
   */
  @Override
  public void removeProcessSchedule( Long processId, String scheduleName )
  {
    try
    {
      scheduler.unscheduleJob( TriggerKey.triggerKey( scheduleName, buildTriggerGroupName( processId ) ) );
    }
    catch( SchedulerException e )
    {
      throw new BeaconRuntimeException( "SchedulerException removing schedule", e );
    }

  }

  /**
   * Overridden from
   * 
   * @param processId
   * @see com.biperf.core.service.process.ProcessService#getSchedules(java.lang.Long)
   * @return List of {@link ScheduledProcess} objects each representing the schedule and
   *         processParameters for the given process.
   */
  @Override
  public List getSchedules( Long processId )
  {
    Process process = processDAO.getProcessById( processId );
    List schedules = new ArrayList();
    try
    {
      List<? extends Trigger> triggers = scheduler.getTriggersOfJob( JobKey.jobKey( buildJobName( processId ), buildJobGroupName( processId ) ) );
      for ( Trigger trigger : triggers )
      {
        ScheduledProcess scheduledProcess = new ScheduledProcess();
        scheduledProcess.setProcess( process );
        JobDataMap jobDataMap = trigger.getJobDataMap();

        Map filteredDataMap = ProcessUtil.filterSystemParameters( jobDataMap );

        scheduledProcess.setProcessParameterStringArrayMap( filteredDataMap );
        scheduledProcess.setProcessSchedule( QuartzProcessUtil.buildProcessSchedule( trigger ) );
        scheduledProcess.setName( trigger.getKey().getName() );
        schedules.add( scheduledProcess );
      }
    }
    catch( SchedulerException e )
    {
      throw new BeaconRuntimeException( "SchedulerException fetching schedules", e );
    }

    return schedules;
  }

  public void launchProcessSlow( Process process, Map processParameterMap, Long runByUserId, long waitMillis )
  {
    launchProcess( process, processParameterMap, runByUserId );

    try
    {
      Thread.sleep( waitMillis );
    }
    catch( InterruptedException e )
    {
      log.error( e.getMessage(), e );
    }

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.process.ProcessService#launchProcess(com.biperf.core.domain.process.Process,
   *      java.util.Map, java.lang.Long)
   * @param process
   * @param processParameterMap
   * @param runByUserId
   */
  @Override
  public void launchProcess( Process process, Map processParameterMap, Long runByUserId )
  {

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );

    scheduleProcess( process, processSchedule, processParameterMap, runByUserId );

  }

  @Override
  public void launchProcess( Process process, Map processParameterMap, Long runByUserId, Long waitMillis )
  {

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( waitMillis );

    scheduleProcess( process, processSchedule, processParameterMap, runByUserId );

  }

  @Override
  public void scheduleProcess( Process process, ProcessSchedule processSchedule, Map processParameterMap, Long runByUserId )
  {
    // Using durable jobs to avoid situation where
    // job completes and is deleted after it
    // has been fetched for another trigger and hasn't yet been stored.

    // Define job instance
    try
    {
      String jobName = buildJobName( process.getId() );
      String jobGroupName = buildJobGroupName( process.getId() );
      JobDetail job = newJob( getProcessBeanJobClass() ).withIdentity( jobName, jobGroupName ).storeDurably().build();

      // Define Trigger Instance
      String triggerGroupName = buildTriggerGroupName( process.getId() );
      String triggerName = buildTriggerName();

      Trigger trigger = QuartzProcessUtil.buildTrigger( processSchedule, job, triggerName, triggerGroupName, jobGroupName );

      JobDataMap triggerJobDataMap = new JobDataMap();
      if ( processParameterMap != null )
      {
        triggerJobDataMap = new JobDataMap( processParameterMap );
      }
      // JobDataMap triggerJobDataMap = new JobDataMap(processParameterMap);
      triggerJobDataMap.put( PROCESS_BEAN_NAME, process.getProcessBeanName() );
      if ( runByUserId != null )
      {
        triggerJobDataMap.put( RUN_BY_USER_ID_PARAM_NAME, runByUserId.toString() );
      }
      trigger.getJobDataMap().putAll( triggerJobDataMap );

      if ( TransactionSynchronizationManager.isSynchronizationActive() )
      {
        // try to find a synchronization for the job
        QuartzScheduleJobTransactionSynchronization quartzTransactionSynchronization = null;
        for ( Iterator i = TransactionSynchronizationManager.getSynchronizations().iterator(); i.hasNext(); )
        {
          Object o = i.next();
          if ( o instanceof QuartzScheduleJobTransactionSynchronization )
          {
            QuartzScheduleJobTransactionSynchronization synch = (QuartzScheduleJobTransactionSynchronization)o;
            JobDetail otherJob = synch.getJob();
            if ( otherJob.getKey().getGroup().equals( job.getKey().getGroup() ) && otherJob.getKey().getName().equals( job.getKey().getName() ) )
            {
              quartzTransactionSynchronization = synch;
              break;
            }
          }
        }
        if ( quartzTransactionSynchronization == null )
        {
          // job not found in trx - create it and register it
          quartzTransactionSynchronization = new QuartzScheduleJobTransactionSynchronization( scheduler, job, trigger );
          TransactionSynchronizationManager.registerSynchronization( quartzTransactionSynchronization );
        }
        else
        {
          // job found in trx - add the new trigger
          quartzTransactionSynchronization.addTrigger( trigger );
        }
      }
      else
      {
        // sychronization is not active
        throw new BeaconRuntimeException( "TransactionSynchronizationManager.isSynchronizationActive() returned " + "false, process can't be scheduled." );
      }
    }
    catch( Exception e )
    {
      log.error( "An error occured scheduleProcess method:." + "For process: " + process.getName(), e );
    }
  }

  @Override
  public void interruptJob( Long processId )
  {
    String jobName = buildJobName( processId );
    String jobGroupName = buildJobGroupName( processId );
    try
    {
      scheduler.interrupt( JobKey.jobKey( jobName, jobGroupName ) );
    }
    catch( UnableToInterruptJobException e )
    {
      throw new BeaconRuntimeException( "SchedulerException scheduling process", e );

    }
  }

  private String buildTriggerName()
  {
    return PROCESS_BEAN_TRIGGER_NAME_PREFIX + oracleSequenceDAO.getOracleSequenceNextValue( "process_trigger_sq" );
  }

  private String buildTriggerGroupName( Long processId )
  {
    return PROCESS_BEAN_TRIGGER_GROUP_PREFIX + processId;
  }

  private String buildJobGroupName( Long processId )
  {
    return PROCESS_BEAN_JOB_GROUP_PREFIX + processId;
  }

  private String buildJobName( Long processId )
  {
    return PROCESS_BEAN_JOB_NAME_PREFIX + processId;
  }

  /**
   * Get all Process Job Spring Bean names.
   * 
   * @return list of String objects
   */
  @Override
  public List getProcessJobBeanNames()
  {
    List processJobBeanNames = new ArrayList();
    String[] beanNamesForType = ApplicationContextFactory.getApplicationContext().getBeanNamesForType( ProcessProxyFactoryBean.class );
    for ( String beanNamesForType1 : beanNamesForType )
    {
      String transformedBeanName = BeanFactoryUtils.transformedBeanName( beanNamesForType1 );
      processJobBeanNames.add( transformedBeanName );
    }
    return processJobBeanNames;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.process.ProcessService#testListTriggers()
   */
  @Override
  public void testListTriggers()
  {
    try
    {
      List<String> triggerGroups = scheduler.getTriggerGroupNames();
      for ( String groupName : triggerGroups )
      {
        log.error( "Group: " + groupName + " contains the following triggers" );

        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys( GroupMatcher.triggerGroupEquals( groupName ) );
        for ( TriggerKey triggerKey : triggerKeys )
        {
          log.error( "- " + triggerKey.getName() );
        }
      }
    }
    catch( SchedulerException e )
    {
      log.error( e.getMessage(), e );
    }
  }

  public ScheduledRecognition populateScheduleRecognitionTriggerName( ScheduledRecognition scheduledRecognition, Process process )
  {
    // Define Trigger Instance
    String triggerGroupName = buildTriggerGroupName( process.getId() );
    String triggerName = buildTriggerName();

    scheduledRecognition.setTriggerName( triggerName );
    scheduledRecognition.setTriggerGroup( triggerGroupName );

    return scheduledRecognition;
  }

  public PostProcessJobs populatePostProcessTriggerName( PostProcessJobs postProcessJobs, Process process )
  {
    // Define Trigger Instance
    String triggerGroupName = buildTriggerGroupName( process.getId() );
    String triggerName = buildTriggerName();

    postProcessJobs.setTriggerName( triggerName );
    postProcessJobs.setTriggerGroup( triggerGroupName );

    return postProcessJobs;
  }

  @Override
  public ScheduledRecognition scheduleRecognitionProcess( ScheduledRecognition scheduledRecognition, Process process, ProcessSchedule processSchedule, Map processParameterMap, Long runByUserId )
  {
    // Using durable jobs to avoid situation where
    // job completes and is deleted after it
    // has been fetched for another trigger and hasn't yet been stored.

    // Define job instance
    String jobName = buildJobName( process.getId() );
    String jobGroupName = buildJobGroupName( process.getId() );
    JobDetail job = newJob( getProcessBeanJobClass() ).withIdentity( jobName, jobGroupName ).storeDurably().build();

    scheduledRecognition.setIsScheduled( "true" );

    Trigger trigger = QuartzProcessUtil.buildTrigger( processSchedule, job, scheduledRecognition.getTriggerName(), scheduledRecognition.getTriggerGroup(), jobGroupName );

    JobDataMap triggerJobDataMap = new JobDataMap();
    if ( processParameterMap != null )
    {
      processParameterMap.put( "triggerName", new String[] { scheduledRecognition.getTriggerName() } );
      triggerJobDataMap = new JobDataMap( processParameterMap );
    }
    // JobDataMap triggerJobDataMap = new JobDataMap(processParameterMap);
    triggerJobDataMap.put( PROCESS_BEAN_NAME, process.getProcessBeanName() );
    if ( runByUserId != null )
    {
      triggerJobDataMap.put( RUN_BY_USER_ID_PARAM_NAME, runByUserId.toString() );
    }
    trigger.getJobDataMap().putAll( triggerJobDataMap );

    if ( TransactionSynchronizationManager.isSynchronizationActive() )
    {
      // try to find a synchronization for the job
      QuartzScheduleJobTransactionSynchronization quartzTransactionSynchronization = null;
      for ( Iterator i = TransactionSynchronizationManager.getSynchronizations().iterator(); i.hasNext(); )
      {
        Object o = i.next();
        if ( o instanceof QuartzScheduleJobTransactionSynchronization )
        {
          QuartzScheduleJobTransactionSynchronization synch = (QuartzScheduleJobTransactionSynchronization)o;
          JobDetail otherJob = synch.getJob();
          if ( otherJob.getKey().getGroup().equals( job.getKey().getGroup() ) && otherJob.getKey().getName().equals( job.getKey().getName() ) )
          {
            quartzTransactionSynchronization = synch;
            break;
          }
        }
      }
      if ( quartzTransactionSynchronization == null )
      {
        // job not found in trx - create it and register it
        quartzTransactionSynchronization = new QuartzScheduleJobTransactionSynchronization( scheduler, job, trigger );
        TransactionSynchronizationManager.registerSynchronization( quartzTransactionSynchronization );
      }
      else
      {
        // job found in trx - add the new trigger
        quartzTransactionSynchronization.addTrigger( trigger );
      }
    }
    else
    {
      // sychronization is not active
      throw new BeaconRuntimeException( "TransactionSynchronizationManager.isSynchronizationActive() returned " + "false, process can't be scheduled." );
    }

    return scheduledRecognition;
  }

  public PostProcessJobs schedulePostProcess( PostProcessJobs postProcessJobs, Process process, ProcessSchedule processSchedule, Map processParameterMap, Long runByUserId )
  {
    // Using durable jobs to avoid situation where
    // job completes and is deleted after it
    // has been fetched for another trigger and hasn't yet been stored.

    // Define job instance
    String jobName = buildJobName( process.getId() );
    String jobGroupName = buildJobGroupName( process.getId() );
    JobDetail job = newJob( getProcessBeanJobClass() ).withIdentity( jobName, jobGroupName ).storeDurably().build();

    // postProcessJobs.setIsScheduled( "true" );

    Trigger trigger = QuartzProcessUtil.buildTrigger( processSchedule, job, postProcessJobs.getTriggerName(), postProcessJobs.getTriggerGroup(), jobGroupName );

    JobDataMap triggerJobDataMap = new JobDataMap();
    if ( processParameterMap != null )
    {
      processParameterMap.put( "triggerName", new String[] { postProcessJobs.getTriggerName() } );
      triggerJobDataMap = new JobDataMap( processParameterMap );
    }
    // JobDataMap triggerJobDataMap = new JobDataMap(processParameterMap);
    triggerJobDataMap.put( PROCESS_BEAN_NAME, process.getProcessBeanName() );
    if ( runByUserId != null )
    {
      triggerJobDataMap.put( RUN_BY_USER_ID_PARAM_NAME, runByUserId.toString() );
    }
    trigger.getJobDataMap().putAll( triggerJobDataMap );

    if ( TransactionSynchronizationManager.isSynchronizationActive() )
    {
      // try to find a synchronization for the job
      QuartzScheduleJobTransactionSynchronization quartzTransactionSynchronization = null;
      for ( Iterator i = TransactionSynchronizationManager.getSynchronizations().iterator(); i.hasNext(); )
      {
        Object o = i.next();
        if ( o instanceof QuartzScheduleJobTransactionSynchronization )
        {
          QuartzScheduleJobTransactionSynchronization synch = (QuartzScheduleJobTransactionSynchronization)o;
          JobDetail otherJob = synch.getJob();
          if ( otherJob.getKey().getGroup().equals( job.getKey().getGroup() ) && otherJob.getKey().getName().equals( job.getKey().getName() ) )
          {
            quartzTransactionSynchronization = synch;
            break;
          }
        }
      }
      if ( quartzTransactionSynchronization == null )
      {
        // job not found in trx - create it and register it
        quartzTransactionSynchronization = new QuartzScheduleJobTransactionSynchronization( scheduler, job, trigger );
        TransactionSynchronizationManager.registerSynchronization( quartzTransactionSynchronization );
      }
      else
      {
        // job found in trx - add the new trigger
        quartzTransactionSynchronization.addTrigger( trigger );
      }
    }
    else
    {
      // sychronization is not active
      throw new BeaconRuntimeException( "TransactionSynchronizationManager.isSynchronizationActive() returned " + "false, process can't be scheduled." );
    }

    return postProcessJobs;
  }

  /**
   * @param scheduler value for scheduler property
   */
  public void setScheduler( Scheduler scheduler )
  {
    this.scheduler = scheduler;
  }

  public void setProcessDAO( ProcessDAO processDAO )
  {
    this.processDAO = processDAO;
  }

  /**
   * @param oracleSequenceDAO value for oracleSequenceDAO property
   */
  public void setOracleSequenceDAO( OracleSequenceDAO oracleSequenceDAO )
  {
    this.oracleSequenceDAO = oracleSequenceDAO;
  }

  public void setRoleService( RoleService roleService )
  {
    this.roleService = roleService;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.process.ProcessService#getProcessParameterValueChoices(java.lang.String)
   * @param namedQueryName
   * @return List of {@link com.biperf.core.value.FormattedValueBean} objects.
   */
  @Override
  public List getProcessParameterValueChoices( String namedQueryName )
  {
    return processDAO.getProcessParameterValueChoices( namedQueryName );
  }

  /**
   * Get the "time of day" millisecond value for the first schedule found for the process associated
   * with the given processBeanName. If no schedule exists or the first schedule has no "time of
   * day", null will be returned.
   * 
   * @param processBeanName
   * @return time of day in milliseconds
   */
  @Override
  public Long getTimeOfDayMillisOfFirstSchedule( String processBeanName )
  {
    List processList = getProcessListByStatus( ProcessStatusType.ACTIVE );
    Long timeOfDayMillis = null;
    for ( Iterator iter = processList.iterator(); iter.hasNext(); )
    {
      Process process = (Process)iter.next();

      if ( process.getProcessBeanName().equals( processBeanName ) )
      {
        List scheduleList = getSchedules( process.getId() );
        if ( !scheduleList.isEmpty() )
        {
          ProcessSchedule processSchedule = ( (ScheduledProcess)scheduleList.get( 0 ) ).getProcessSchedule();
          timeOfDayMillis = processSchedule.getTimeOfDayMillis();
          break;
        }
      }
    }
    return timeOfDayMillis;
  }

  /**
   * @param processBeanName
   * @return date
   */
  @Override
  public Date getTimeOfDayOfFirstScheduleDate( String processBeanName )
  {
    List processList = getProcessListByStatus( ProcessStatusType.ACTIVE );
    Date timeOfDayAsDate = null;
    for ( Iterator iter = processList.iterator(); iter.hasNext(); )
    {
      Process process = (Process)iter.next();

      if ( process.getProcessBeanName().equals( processBeanName ) )
      {
        List scheduleList = getSchedules( process.getId() );
        if ( !scheduleList.isEmpty() )
        {
          ProcessSchedule processSchedule = ( (ScheduledProcess)scheduleList.get( 0 ) ).getProcessSchedule();
          timeOfDayAsDate = processSchedule.getTimeOfDayAsDate();
          break;
        }
      }
    }
    return timeOfDayAsDate;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.process.ProcessService#updateLastExecutedTime(java.util.Date,
   *      java.lang.Long)
   * @param lastExecutedTime
   * @param processId
   */
  @Override
  public void updateLastExecutedTime( Date lastExecutedTime, Long processId )
  {
    processDAO.updateLastExecutedDate( lastExecutedTime, processId );

  }

  @Override
  public List getCurrentlyExecutingProcessess()
  {
    return processDAO.getAllCurrentlyExecutingProcesses();
  }

  @Override
  public void interruptProcess( Long processId )
  {
    String jobName = buildJobName( processId );
    String jobGroupName = buildJobGroupName( processId );

    // Delete Jobs which are scheduled but currently NOT executing
    try
    {
      scheduler.deleteJob( JobKey.jobKey( jobName, jobGroupName ) );
    }
    catch( SchedulerException e )
    {
      // Do nothing;
    }

    // Interrupt Jobs which are scheduled and currently executing
    try
    {
      scheduler.interrupt( JobKey.jobKey( jobName, jobGroupName ) );
    }
    catch( UnableToInterruptJobException e )
    {
      // Do nothing;
      log.error( "UnableToInterruptJobException e)", e );
    }
  }

  @Override
  public void interruptAllProcesses()
  {
    // Delete ALL scheduled data (Jobs, Triggers, Calendars), currently NOT executing
    try
    {
      scheduler.clear();
    }
    catch( SchedulerException e )
    {
      // Do nothing;
    }

    // Interrupt Jobs which are scheduled and currently executing
    try
    {
      List<JobExecutionContext> jobs = scheduler.getCurrentlyExecutingJobs();
      for ( JobExecutionContext job : jobs )
      {
        try
        {
          if ( job != null && job.getJobDetail() != null )
          {
            scheduler.interrupt( job.getJobDetail().getKey() );
          }
        }
        catch( UnableToInterruptJobException e )
        {
          // Do nothing;
          log.error( "*******Debugging the interruptAllProcesses issue: UnableToInterruptJobException e) " + ( job != null ? job.toString() : "null" ), e );
        }
      }
    }
    catch( SchedulerException e )
    {
      // Do nothing;
    }
  }
  // Client customizations for wip #23129 starts
  public List getClientGiftCodeSweepPromotions()
  {
    List beanList = processDAO.getClientGiftCodeSweepPromotions();
    return beanList;
  }

  public List getClientGiftCodeSweepBean( Long promoId )
  {
    List beanList = processDAO.getClientGiftCodeSweepBean( promoId );
    return beanList;
  }
  // Client customizations for wip #23129 ends

  @Override
  public void clearHibernateSecondLevelCache()
  {
    processDAO.clearSecondLevelCache();
  }

  protected Class getProcessBeanJobClass()
  {
    return ProcessBeanJob.class;
  }

}
