
package com.biperf.core.dao.promotion;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PostProcessJobs;

public interface PostProcessJobsDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "postProcessJobsDAO";

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
