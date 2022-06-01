
package com.biperf.core.dao.workhappier.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.workhappier.WorkHappierDAO;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierFeedback;
import com.biperf.core.domain.workhappier.WorkHappierScore;

public class WorkHappierDAOImpl extends BaseDAO implements WorkHappierDAO
{

  private DataSource dataSource;

  @Override
  public List<WorkHappier> getWorkHappier()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.workhappier.getAllWorkHappier" );
    return query.list();
  }

  @Override
  public WorkHappierScore saveScore( WorkHappierScore whScore )
  {
    getSession().saveOrUpdate( whScore );
    return whScore;
  }

  @Override
  public List<WorkHappierScore> getWHScore( Long userId, int numberOfScores )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.workhappier.getWHScore" );
    query.setParameter( "userId", userId );
    query.setParameter( "numberOfScores", numberOfScores );
    return query.list();
  }

  @Override
  public void saveFeedcack( WorkHappierFeedback whFeedback )
  {
    getSession().saveOrUpdate( whFeedback );
  }

  @Override
  public WorkHappierScore getScoreById( Long whScoreId )
  {
    return (WorkHappierScore)getSession().get( WorkHappierScore.class, whScoreId );
  }

  @Override
  public WorkHappierFeedback getFeedcackById( Long whFeedbackId )
  {
    return (WorkHappierFeedback)getSession().get( WorkHappierFeedback.class, whFeedbackId );
  }

  @Override
  public WorkHappier geWorkHappierById( Long workHappierId )
  {
    return (WorkHappier)getSession().get( WorkHappier.class, workHappierId );
  }

  @Override
  public WorkHappier geWorkHappierByScore( Long score )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.workhappier.geWorkHappierByScore" );
    query.setParameter( "score", score );
    return (WorkHappier)query.uniqueResult();
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}
