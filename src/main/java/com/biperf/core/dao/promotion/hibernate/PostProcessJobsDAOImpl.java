
package com.biperf.core.dao.promotion.hibernate;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.PostProcessJobsDAO;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class PostProcessJobsDAOImpl extends BaseDAO implements PostProcessJobsDAO
{

  private DataSource dataSource;

  public PostProcessJobs savePostProcessJobs( PostProcessJobs postProcessJobs )
  {
    return (PostProcessJobs)HibernateUtil.saveOrUpdateOrShallowMerge( postProcessJobs );

  }

  public PostProcessJobs getPostProcessJobsById( Long id )
  {
    return (PostProcessJobs)getSession().get( PostProcessJobs.class, id );
  }

  public List<Long> getPostProcessJobsIdsForRetryProcess( int retryAttempts )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PostProcessJobs.GetPostProcessJobsIdByBeanName" );
    query.setInteger( "retryAttempts", retryAttempts );
    List<Long> results = query.list();
    return results;
  }

  public Long getStuckJournals()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PostProcessJobs.GetPostProcessJobsStuckJournals" );
    Long result = (Long)query.uniqueResult();
    return result;
  }

  public Long getScheduledJobsDelayedJobs()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PostProcessJobs.GetPostProcessJobsDelayedJobs" );
    Long result = (Long)query.uniqueResult();
    return result;
  }

  public Long getQrtzSchedulerStateCount()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PostProcessJobs.GetQrtzSchedulerStateCount" );
    Long result = (Long)query.uniqueResult();
    return result;
  }

  public Long getUnsentMailingsCount()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PostProcessJobs.GetUnsentMailingsCount" );
    Long result = (Long)query.uniqueResult();
    return result;
  }

  public Long getMaxAttemptsReachedCount( int retryAttempts )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PostProcessJobs.GetMaxAttemptsReachedCount" );
    query.setInteger( "retryAttempts", retryAttempts );
    Long result = (Long)query.uniqueResult();
    return result;
  }

  public boolean isJournalIdExists( Long journalId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PostProcessJobs.IsJournalIdExists" );
    query.setParameter( "journalId", journalId );
    Long result = (Long)query.uniqueResult();
    if ( result > 0 )
    {
      return true;
    }
    return false;
  }

  public Map<String, Object> runPostProcessJobsCleanUp()
  {
    CallPostProcessJobsCleanUp prc = new CallPostProcessJobsCleanUp( dataSource );
    return prc.executeProcedure();
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  public DataSource getDataSource()
  {
    return dataSource;
  }
}
