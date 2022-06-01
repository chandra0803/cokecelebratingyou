
package com.biperf.core.dao.goalquest.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.goalquest.GoalQuestDataRepositoryDAO;

public class GoalQuestDataRepositoryDAOImpl extends BaseDAO implements GoalQuestDataRepositoryDAO
{

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  public Map goalQuestDataRepositoryExtract( Long promotionId, String locale )
  {
    CallPrcGoalQuestDataRepository callPrcGoalQuestDataRepository = new CallPrcGoalQuestDataRepository( dataSource );
    return callPrcGoalQuestDataRepository.executeProcedure( promotionId, locale );

  }

}
