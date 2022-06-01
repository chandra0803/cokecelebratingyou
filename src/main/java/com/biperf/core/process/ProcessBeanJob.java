/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ProcessBeanJob.java,v $
 */

package com.biperf.core.process;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.process.ProcessInvocationParameter;
import com.biperf.core.domain.process.ProcessInvocationParameterValue;
import com.biperf.core.domain.process.ProcessParameter;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.CmsAuthorizationUtil;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.process.impl.ProcessInvocationAssociationRequest;
import com.biperf.core.service.util.ProcessUtil;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.objectpartners.cms.domain.enums.ContentStatusEnum;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.service.impl.ContentReaderImpl;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.ContentReaderManager;

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
public class ProcessBeanJob implements InterruptableJob
{
  public static final String BEAN_NAME = "processBeanJob";

  private static final Log log = LogFactory.getLog( ProcessBeanJob.class );

  // Injected beans
  protected ProcessService processService;
  protected UserService userService;
  protected AudienceService audienceService;
  protected ProcessInvocationService processInvocationService;
  protected CmsConfiguration cmsConfiguration = null;

  // jobdatamap system properties
  protected String processBeanName;
  protected String runByUserId;

  // instance variables
  protected BaseProcess processBean;

  /**
   * Overridden from
   * 
   * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
   * @param context
   * @throws JobExecutionException
   */
  @Override
  public void execute( JobExecutionContext context ) throws JobExecutionException
  {
    try
    {
      exposeJobParametersAsGetters( context );

      // Explicit Injection of dependencies - Start
      this.processService = (ProcessService)BeanLocator.getBean( "processService" );
      this.userService = (UserService)BeanLocator.getBean( "userService" );
      this.processInvocationService = (ProcessInvocationService)BeanLocator.getBean( "processInvocationService" );
      this.audienceService = (AudienceService)BeanLocator.getBean( "audienceService" );
      this.cmsConfiguration = (CmsConfiguration)BeanLocator.getBean( "cmsConfiguration" );
      // Explicit Injection of dependencies - end

      startSystemResources();

      Long processInvocationId = null;
      try
      {
        Map filteredDataMap = ProcessUtil.filterSystemParameters( context.getMergedJobDataMap() );

        processInvocationId = saveInitialProcessInvocation( context, filteredDataMap );

        processBean = (BaseProcess)BeanLocator.getBean( processBeanName );

        log.warn( "Executing process: " + processBean.toString() );
        processBean.execute( filteredDataMap, processInvocationId );
      }
      catch( Throwable e )
      {
        log.error( "Uncaught Exception running Process with process bean name: (" + processBeanName + ") and  processInvocationId: (" + processInvocationId + ")", e );
        StringBuilder msg = new StringBuilder( "Process Failed: Uncaught Exception running Process " + "with process bean name: (" );
        msg.append( processBeanName ).append( "). The error caused by: " );
        msg.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
        processInvocationService.addComment( processInvocationId, msg.toString() );
      }
      finally
      {
        if ( processInvocationId != null )
        {
          saveCompletedProcessInvocation( processInvocationId );
        }

        endSystemResources();
      }
    }
    catch( Exception e )
    {
      log.error( "Exception Initializaing Job", e );
    }

  }

  protected void startSystemResources()
  {
    // CMS Reader setup
    String applicationCode = cmsConfiguration.getDefaultApplication();
    String parentApplicationCode = cmsConfiguration.getDefaultParentApplication();
    Locale locale = cmsConfiguration.getDefaultLocale();
    ContentStatusEnum contentStatus = ContentStatusEnum.LIVE;
    Set audienceNames = new HashSet( cmsConfiguration.getDefaultAudienceNames() );

    ContentReader reader = null;
    try
    {
      reader = new ContentReaderImpl( applicationCode, parentApplicationCode, locale, locale, contentStatus, audienceNames );
      reader.setApplicationContext( ApplicationContextFactory.getContentManagerApplicationContext() );
    }
    catch( CmsServiceException e )
    {
      throw new BeaconRuntimeException( e.getMessage(), e );
    }

    ContentReaderManager.setContentReader( reader );

    if ( runByUserId != null )
    {
      // content reader has to be set before you run your user service.
      User user = userService.getUserById( new Long( runByUserId ) );

      // User set for db audit fields
      AuthenticatedUser schedulerUser = new AuthenticatedUser();
      schedulerUser.setUsername( user.getUserName() );
      schedulerUser.setUserId( user.getId() );

      UserManager.setUser( schedulerUser );

      if ( user instanceof Participant )
      {

        audienceNames.add( CmsAuthorizationUtil.PARTICIPANT_AUDIENCE_NAME );
        audienceNames.add( CmsAuthorizationUtil.DEFAULT_AUDIENCE_NAME );
        Participant pax = (Participant)user;
        for ( Iterator iterator = audienceService.getAllParticipantAudiences( pax ).iterator(); iterator.hasNext(); )
        {
          Audience audience = (Audience)iterator.next();
          audienceNames.add( audience.getName() );
        }
      }
      else
      {

        audienceNames.add( CmsAuthorizationUtil.DEFAULT_AUDIENCE_NAME );
        audienceNames.add( CmsAuthorizationUtil.USER_AUDIENCE_NAME );
      }

    }
    else
    {
      // User set for db audit fields
      AuthenticatedUser schedulerUser = new AuthenticatedUser();
      schedulerUser.setUsername( "scheduler" );
      schedulerUser.setUserId( new Long( -1 ) );
      UserManager.setUser( schedulerUser );
    }

    try
    {
      reader = new ContentReaderImpl( applicationCode, parentApplicationCode, locale, locale, contentStatus, audienceNames );
      reader.setApplicationContext( ApplicationContextFactory.getContentManagerApplicationContext() );
    }
    catch( CmsServiceException e )
    {
      throw new BeaconRuntimeException( e.getMessage(), e );
    }

    ContentReaderManager.setContentReader( reader );
  }

  protected void endSystemResources()
  {
    UserManager.removeUser();
    ContentReaderManager.removeContentReader();
  }

  /**
   * Notes: Since ProcessJobBean is non-transactional the initial ProcessInvocation will be
   * persisted immediately since the tx boundary will be around the processInvocationService.save()
   * call.
   */
  protected Long saveInitialProcessInvocation( JobExecutionContext context, Map filteredDataMap )
  {
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // MD5Hash secretHasher = new MD5Hash();
    SHA256Hash secretHasher = new SHA256Hash();
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    ProcessInvocation processInvocation = new ProcessInvocation();
    processInvocation.setStartDate( new Date() );
    Process process = processService.getProcessById( extractProcessId( context.getJobDetail().getKey().getName() ) );
    Map processParameters = process.getProcessParameters();
    processInvocation.setProcess( process );
    processInvocation.setStartDate( new Date() );
    if ( runByUserId != null )
    {
      processInvocation.setRunAsUser( userService.getUserById( new Long( runByUserId ) ) );
    }

    for ( Iterator iter = filteredDataMap.keySet().iterator(); iter.hasNext(); )
    {
      String paramName = (String)iter.next();
      boolean secretValues = false;
      if ( processParameters != null )
      {
        ProcessParameter pparam = (ProcessParameter)processParameters.get( paramName );
        if ( pparam != null && pparam.isSecret() )
        {
          secretValues = true;
        }
      }
      // Values are GENERALLLY all expected to be String[].
      String[] paramValue = null;
      Object paramObject = null;

      if ( filteredDataMap.get( paramName ) instanceof String[] )
      {
        paramValue = (String[])filteredDataMap.get( paramName );
      }
      else
      {
        paramObject = filteredDataMap.get( paramName );
      }

      ProcessInvocationParameter processInvocationParameter = new ProcessInvocationParameter();
      processInvocationParameter.setProcessParameterName( paramName );
      if ( paramValue != null )
      {
        for ( int i = 0; i < paramValue.length; i++ )
        {
          ProcessInvocationParameterValue processInvocationParameterValue = new ProcessInvocationParameterValue();
          if ( !secretValues )
          {
            processInvocationParameterValue.setValue( paramValue[i] );
          }
          else
          {
            processInvocationParameterValue.setValue( secretHasher.encrypt( paramValue[i], false ) );
          }
          processInvocationParameter.addProcessInvocationParameterValue( processInvocationParameterValue );

        }
      }
      // we expect the Process to accept a method that will take an object, the process should know
      // what
      // that object is
      if ( paramObject != null )
      {
        ProcessInvocationParameterValue processInvocationParameterValue = new ProcessInvocationParameterValue();
        processInvocationParameterValue.setObjectValue( paramObject );
        processInvocationParameter.addProcessInvocationParameterValue( processInvocationParameterValue );
      }
      processInvocation.addProcessInvocationParameter( processInvocationParameter );
    }
    ProcessInvocation savedProcessInvocation = processInvocationService.save( processInvocation );

    Long processInvocationId = savedProcessInvocation.getId();
    return processInvocationId;
  }

  /**
   * @param processInvocationId
   */
  protected void saveCompletedProcessInvocation( Long processInvocationId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    ProcessInvocationAssociationRequest processInvocationAssociationRequest = new ProcessInvocationAssociationRequest( ProcessInvocationAssociationRequest.PROCESS );
    associationRequestCollection.add( processInvocationAssociationRequest );
    ProcessInvocation processInvocation = processInvocationService.getProcessInvocationById( processInvocationId, associationRequestCollection );
    processInvocation.setEndDate( new Date() );
    // TODO: some kind of success/failure status?

    processInvocationService.save( processInvocation );

    // Also set last execute time on process
    processService.updateLastExecutedTime( new Date(), processInvocation.getProcess().getId() );
  }

  /**
   * @param runByUserId value for runByUserId property
   */
  public void setRunByUserId( String runByUserId )
  {
    this.runByUserId = runByUserId;
  }

  /**
   * @param processBeanName value for processBeanName property
   */
  public void setProcessBeanName( String processBeanName )
  {
    this.processBeanName = processBeanName;
  }

  /**
   * @param processService value for processService property
   */
  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  /**
   * @param processInvocationService value for processInvocationService property
   */
  public void setProcessInvocationService( ProcessInvocationService processInvocationService )
  {
    this.processInvocationService = processInvocationService;
  }

  /**
   * @param userService value for userService property
   */
  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  /**
   * @param audienceService value for audienceService property
   */
  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setCmsConfiguration( CmsConfiguration cmsConfig )
  {
    cmsConfiguration = cmsConfig;
  }

  private Long extractProcessId( String jobName )
  {
    String processIdString = StringUtils.substringAfter( jobName, ProcessService.PROCESS_BEAN_JOB_NAME_PREFIX );
    return new Long( processIdString );
  }

  /**
   * Allow job data to be accesible via getters rather than through the job hashmap. Copied from
   * QuartzJobBean which hasn't yet been updated to the quartz 1.5.2 trigger job maps
   * 
   * @param context
   * @throws SchedulerException
   */
  protected void exposeJobParametersAsGetters( JobExecutionContext context ) throws SchedulerException
  {
    BeanWrapper bw = new BeanWrapperImpl( this );
    MutablePropertyValues pvs = new MutablePropertyValues();
    pvs.addPropertyValues( context.getScheduler().getContext() );
    pvs.addPropertyValues( context.getJobDetail().getJobDataMap() );
    pvs.addPropertyValues( context.getTrigger().getJobDataMap() );
    bw.setPropertyValues( pvs, true );
  }

  /**
   * Overridden from
   * 
   * @see org.quartz.InterruptableJob#interrupt()
   * @throws UnableToInterruptJobException
   */
  @Override
  public void interrupt() throws UnableToInterruptJobException
  {
    // Hmm how to notify processBean.execute(); should process job itself be a spring bean?
    if ( processBean != null )
    {
      log.warn( "interrupting process: " + processBean.toString() );
      processBean.interrupt();
    }
    else
    {
      log.error( "Attempted to interrupt job before the process bean was created, job won't be interrupted." );
    }
  }
}
