
package com.biperf.core.dao.budget.hibernate;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.budget.BudgetHistoryDAO;
import com.biperf.core.domain.budget.BudgetHistory;
import com.biperf.core.utils.UserManager;

public class BudgetHistoryDAOImpl extends BaseDAO implements BudgetHistoryDAO
{
  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private static String INSERT_BUDGET_HISTORY = "INSERT INTO BUDGET_HISTORY (BUDGET_HISTORY_ID, BUDGET_ID, "
      + "ORIGINAL_VALUE_BEFORE_XACTION, CURRENT_VALUE_BEFORE_XACTION, ORIGINAL_VALUE_AFTER_XACTION, CURRENT_VALUE_AFTER_XACTION, ACTION_TYPE,"
      + "CREATED_BY, DATE_CREATED, FROM_BUDGET_ID) VALUES ( BUDGET_HISTORY_PK_SQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, sysdate, ?)";

  private static int[] BUDGET_HISTORY_TYPES = new int[] { Types.INTEGER, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.VARCHAR, 
                                                          Types.INTEGER, Types.NUMERIC };

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  @Override
  public void create( BudgetHistory history )
  {
    Object[] args = buildArgumentArray( history );
    jdbcTemplate.update( INSERT_BUDGET_HISTORY, args, BUDGET_HISTORY_TYPES );
  }

  private Object[] buildArgumentArray( BudgetHistory history )
  {
    return new Object[] { history.getBudgetId(),
                          history.getOriginalValueBeforeTransaction(),
                          history.getCurrentValueBeforeTransaction(),
                          history.getOriginalValueAfterTransaction(),
                          history.getCurrentValueAfterTransaction(),
                          history.getActionType().getCode(),
                          UserManager.getUserId(),
                          history.getFromBudgetId() };
  }

  public DataSource getDataSource()
  {
    return dataSource;
  }
}
