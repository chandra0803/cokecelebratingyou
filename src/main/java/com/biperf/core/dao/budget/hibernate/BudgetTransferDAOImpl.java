package com.biperf.core.dao.budget.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.budget.BudgetTransferDAO;

public class BudgetTransferDAOImpl extends BaseDAO implements BudgetTransferDAO
{

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  public List getBudgetGivers( )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetGivers" );
    return query.list();
  }
  
  public List getBudgetReceivers( )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getBudgetReceivers" );
    return query.list();
  }
  
  public Long getFromNodeOwnerBudgetAmount( )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.getFromNodeOwnerBudgetAmount" );
    return (Long)query.uniqueResult();
  }
  
  public int updateReminderSent( Long mgrUserId )
  {
    String dateSent = null;
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" );
    dateSent = sdf.format( now );
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.budget.updateReminderSent" );
    query.setParameter( "dateSent", dateSent );
    query.setParameter( "mgrUserId", mgrUserId );
    int result = query.executeUpdate();
    return result;
  }
}
