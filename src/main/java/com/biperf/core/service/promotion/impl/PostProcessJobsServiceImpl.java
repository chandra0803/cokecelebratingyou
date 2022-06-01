/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/scheduledrecognition/impl/ScheduledRecognitionServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;

import com.biperf.core.dao.promotion.PostProcessJobsDAO;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.domain.promotion.PostProcessPayoutCalculation;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PostProcessJobsService;
import com.biperf.core.utils.UserManager;

public class PostProcessJobsServiceImpl implements PostProcessJobsService
{
  private static final Log logger = LogFactory.getLog( PostProcessJobsServiceImpl.class );
  private static final String CLASS_NAME = "PostProcessJobsServiceImpl";

  private Scheduler scheduler;
  private PostProcessJobsDAO postProcessJobsDAO;
  private ProcessService processService;

  public PostProcessJobs createPostProcessJobs( PostProcessJobs postProcessJobs, Map inputParameters )
  {
    String processBeanName = (String)inputParameters.get( "processBeanName" );
    String processName = (String)inputParameters.get( "processName" );
    String promotionType = (String)inputParameters.get( "promotionType" );
    Long claimId = (Long)inputParameters.get( "claimId" );
    Long journalId = (Long)inputParameters.get( "journalId" );
    Set payOutCalculationResult = (Set)inputParameters.get( "payOutCalculationResult" );
    boolean fired = false;
    int retryCount = 0;

    postProcessJobs.setProcessBeanName( processBeanName );
    postProcessJobs.setPromotionType( promotionType );
    postProcessJobs.setClaimId( claimId );
    postProcessJobs.setJournalId( journalId );
    // clear out children for clean insert of postProcessJobs
    postProcessJobs.setPayOutCalculationResult( new LinkedHashSet() );
    postProcessJobs.setFired( fired );
    postProcessJobs.setRetryCount( retryCount );

    Process process = processService.createOrLoadSystemProcess( processName, processBeanName );

    postProcessJobs = processService.populatePostProcessTriggerName( postProcessJobs, process );

    try
    {
      if ( payOutCalculationResult != null )
      {
        Iterator payOutCalculationResultIterator = payOutCalculationResult.iterator();
        while ( payOutCalculationResultIterator.hasNext() )
        {
          postProcessJobs.addPayOutCalculationResult( (PostProcessPayoutCalculation)payOutCalculationResultIterator.next() );
        }
      }
      postProcessJobs = postProcessJobsDAO.savePostProcessJobs( postProcessJobs );
    }
    catch( Exception e )
    {
      logger.error( CLASS_NAME + " .createPostProcessJobs - Unable to save PostProcessJobs. Exception: " + e );
    }

    return postProcessJobs;
  }

  public void schedulePostProcess( PostProcessJobs postProcessJobs, String processName, String processBeanName )
  {
    // schedule job
    Process process = processService.createOrLoadSystemProcess( processName, processBeanName );

    Map parameterValueMap = new LinkedHashMap();

    ProcessSchedule processSchedule = new ProcessSchedule();
    // processSchedule.setStartDate( deliveryDate );
    processSchedule.setStartDate( new Date() );
    // processSchedule.setTimeOfDayMillis( new Long( DateUtils.MILLIS_PER_HOUR ) +timeGap);
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );

    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    parameterValueMap.put( "postProcessJobsId", new String[] { postProcessJobs.getId().toString() } );

    postProcessJobs = processService.schedulePostProcess( postProcessJobs, process, processSchedule, parameterValueMap, UserManager.getUserId() );

    postProcessJobsDAO.savePostProcessJobs( postProcessJobs );
  }

  public void scheduleRetryPostProcess( PostProcessJobs postProcessJobs, String processName )
  {
    // schedule job
    Process process = processService.createOrLoadSystemProcess( processName, postProcessJobs.getProcessBeanName() );

    Map parameterValueMap = new LinkedHashMap();

    ProcessSchedule processSchedule = new ProcessSchedule();
    // processSchedule.setStartDate( deliveryDate );
    processSchedule.setStartDate( new Date() );
    // processSchedule.setTimeOfDayMillis( new Long( DateUtils.MILLIS_PER_HOUR ) +timeGap);
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );

    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    parameterValueMap.put( "postProcessJobsId", new String[] { postProcessJobs.getId().toString() } );

    postProcessJobs = processService.populatePostProcessTriggerName( postProcessJobs, process );

    postProcessJobs.setRetryCount( postProcessJobs.getRetryCount() + 1 );
    postProcessJobs.setRetryCountChangeDate( new Date() );
    // Update the database that a retry attempt was made and create a new qrtz trigger
    postProcessJobsDAO.savePostProcessJobs( postProcessJobs );

    // Get the saved postProcessJobs and schedule the quartz job
    PostProcessJobs postProcessJobsSaved = getPostProcessJobsById( postProcessJobs.getId() );

    postProcessJobs = processService.schedulePostProcess( postProcessJobsSaved, process, processSchedule, parameterValueMap, UserManager.getUserId() );

  }

  public List<Long> getPostProcessJobsIdsForRetryProcess( int retryAttempts )
  {
    List<Long> postProcessJobsIdList = null;
    postProcessJobsIdList = postProcessJobsDAO.getPostProcessJobsIdsForRetryProcess( retryAttempts );
    return postProcessJobsIdList;
  }

  public Long getStuckJournals()
  {
    Long postProcessJobsStuckJournals = null;
    postProcessJobsStuckJournals = postProcessJobsDAO.getStuckJournals();
    return postProcessJobsStuckJournals;
  }

  public Long getScheduledJobsDelayedJobs()
  {
    Long postProcessJobsDelayedJobs = null;
    postProcessJobsDelayedJobs = postProcessJobsDAO.getScheduledJobsDelayedJobs();
    return postProcessJobsDelayedJobs;
  }

  public Long getQrtzSchedulerStateCount()
  {
    Long qrtzSchedulerStateCount = null;
    qrtzSchedulerStateCount = postProcessJobsDAO.getQrtzSchedulerStateCount();
    return qrtzSchedulerStateCount;
  }

  public Long getUnsentMailingsCount()
  {
    Long unsentMailingsCount = null;
    unsentMailingsCount = postProcessJobsDAO.getUnsentMailingsCount();
    return unsentMailingsCount;
  }

  public Long getMaxAttemptsReachedCount( int retryAttempts )
  {
    Long maxAttemptsReachedCount = null;
    maxAttemptsReachedCount = postProcessJobsDAO.getMaxAttemptsReachedCount( retryAttempts );
    return maxAttemptsReachedCount;
  }

  public boolean isJournalIdExists( Long journalId )
  {
    return postProcessJobsDAO.isJournalIdExists( journalId );
  }

  public PostProcessJobs savePostProcessJobs( PostProcessJobs postProcessJobs )
  {
    return this.postProcessJobsDAO.savePostProcessJobs( postProcessJobs );
  }

  public PostProcessJobs getPostProcessJobsById( Long id )
  {
    return this.postProcessJobsDAO.getPostProcessJobsById( id );
  }

  public Map<String, Object> runPostProcessJobsCleanUp()
  {
    return this.postProcessJobsDAO.runPostProcessJobsCleanUp();
  }

  public Scheduler getScheduler()
  {
    return scheduler;
  }

  public void setScheduler( Scheduler scheduler )
  {
    this.scheduler = scheduler;
  }

  public void setPostProcessJobsDAO( PostProcessJobsDAO postProcessJobsDAO )
  {
    this.postProcessJobsDAO = postProcessJobsDAO;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}
