/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/scheduledrecognition/ScheduledRecognitionService.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.service.SAO;

public interface PostProcessJobsService extends SAO
{
  /**
   * BEAN_NAME for referencing in tests and spring config files.
   */
  public final String BEAN_NAME = "postProcessJobsService";

  public PostProcessJobs createPostProcessJobs( PostProcessJobs postProcessJobs, Map inputParameters );

  public void schedulePostProcess( PostProcessJobs postProcessJobs, String processName, String processBeanName );

  public void scheduleRetryPostProcess( PostProcessJobs postProcessJobs, String processName );

  public PostProcessJobs savePostProcessJobs( PostProcessJobs postProcessJobs );

  public PostProcessJobs getPostProcessJobsById( Long id );

  public List<Long> getPostProcessJobsIdsForRetryProcess( int retryAttempts );

  public Long getStuckJournals();

  public Long getScheduledJobsDelayedJobs();

  public Long getQrtzSchedulerStateCount();

  public Long getUnsentMailingsCount();

  public Long getMaxAttemptsReachedCount( int retryAttempts );

  public Map<String, Object> runPostProcessJobsCleanUp();

  public boolean isJournalIdExists( Long journalId );

}
