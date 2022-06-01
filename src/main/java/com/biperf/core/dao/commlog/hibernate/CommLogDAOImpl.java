/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/commlog/hibernate/CommLogDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.commlog.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.commlog.CommLogDAO;
import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.SqlQueryBuilder;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.CommLogValueBean;

/**
 * CommLogDAOImpl <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 */
public class CommLogDAOImpl extends BaseDAO implements CommLogDAO
{
  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    // TODO what if you want to use a JdbcTemplate by preference,
    // for a native extractor?
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  /**
   * Get all commLogs for this user.
   * 
   * @param userId
   * @return List
   */
  public List getCommLogsByUser( Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.dao.commlog.CommLogsByUser" ).setLong( "userId", userId.longValue() ).list();
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getNonFailureCommLogsByUser( Long userId, int pageNumber, int pageSize, Date startDate, Date endDate, Integer sortedOn, String sortedBy )
  {
    List bindVariables = new ArrayList();
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();

    int startIndex = 0;
    int endIndex = 0;

    if ( pageNumber == 1 )
    {
      startIndex = 0;
      endIndex = pageSize;
    }
    else
    {
      startIndex = ( pageNumber - 1 ) * pageSize + 1;
      endIndex = startIndex + 24;
    }

    bindVariables.add( userId );
    bindVariables.add( startDate );
    bindVariables.add( endDate );
    bindVariables.add( userId );
    bindVariables.add( startDate );
    bindVariables.add( endDate );
    bindVariables.add( startIndex );
    bindVariables.add( endIndex );

    if ( sortedOn != null )
    {
      String sortField = null;
      switch ( sortedOn )
      {
        case 0:
          sortField = "subject";
          break;
        case 1:
          sortField = "date_created";
          break;
        default:
          sortField = "subject";
          break;
      }
      final String ORDER_BY_CLAUSE = " order by " + sortField + " " + sortedBy;
      queryBuilder.append( getNonFailureCommLogsByUserQUERY( ORDER_BY_CLAUSE ) );

    }
    String queryString = queryBuilder.toString();
    RowMapper rowMapper = new CommLogRowMapper();
    List results = jdbcTemplate.query( queryString, bindVariables.toArray(), rowMapper );

    return results;
  }

  private String getNonFailureCommLogsByUserQUERY( String orderByClass )
  {
    StringBuilder query = new StringBuilder();
    query.append( "select * " );
    query.append( "from (select ROWNUM RN,DTL.* from ( " );
    query.append( "SELECT DBMS_LOB.substr(cl.subject,500,1) subject, " );
    query.append( "cl.DATE_CREATED, " );
    query.append( "cl.CREATED_BY, " );
    query.append( "cl.comm_log_id,DBMS_LOB.substr(cl.plain_message, 2000, 1) plain_message_1, DBMS_LOB.SUBSTR (cl.plain_message, 2000, 2001) plain_message_2 " );
    query.append( "FROM comm_log cl, Mailing m, Mailing_recipient mr, Claim_recipient cr " );
    query.append( "WHERE m.mailing_id = cl.mailing_id " );
    query.append( "AND m.MAILING_ID = mr.MAILING_ID " );
    query.append( "AND mr.CLAIM_ITEM_ID = cr.CLAIM_ITEM_ID " );
    query.append( "AND mr.USER_ID = cl.USER_ID " );
    query.append( "AND cl.USER_ID = cr.PARTICIPANT_ID " );
    query.append( " AND cl.message_type ='email' " );
    query.append( "AND cl.user_id = ? " );
    query.append( "AND m.message_id IN ('7032') " );
    query.append( "AND cl.subject not like '%Notice: %' " );
    query.append( "AND trunc(cl.date_created) BETWEEN ? AND ? " );
    query.append( "UNION " );
    query.append( "SELECT DBMS_LOB.substr(cl.subject,500,1) subject, " );
    query.append( "cl.DATE_CREATED, " );
    query.append( "cl.CREATED_BY, " );
    query.append( "cl.comm_log_id,DBMS_LOB.substr(cl.plain_message, 2000, 1) plain_message_1, DBMS_LOB.SUBSTR (cl.plain_message, 2000, 2001) plain_message_2 " );
    query.append( "FROM comm_log cl, Mailing m, Mailing_recipient mr " );
    query.append( "WHERE m.mailing_id = cl.mailing_id " );
    query.append( "AND m.MAILING_ID = mr.MAILING_ID " );
    query.append( "AND mr.USER_ID = cl.USER_ID " );
    query.append( "AND cl.user_id = ? " );
    query.append( " AND cl.message_type ='email' " );
    query.append( "AND m.message_id NOT IN ('7032') " );
    query.append( "AND cl.reason_type_code IN ('emailwizard','emailpromotion','emailother') " );
    query.append( "AND m.mailing_type NOT IN ('loginemail','passwordemail','bothemail') " );
    query.append( "AND trunc(cl.date_created) BETWEEN ? AND ? " );
    query.append( orderByClass );
    query.append( " )DTL ) " );
    query.append( "WHERE RN BETWEEN ? AND ? " );

    return query.toString();
  }

  @Override
  public Long getNonFailureCommLogCountByUser( Long userId, Date startDate, Date endDate )
  {
    Session session = HibernateSessionManager.getSession();
    SQLQuery query = (SQLQuery)session.getNamedQuery( "com.biperf.core.dao.commlog.NonFailureCommLogCountByUser" );
    query.setParameter( "userId", userId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return (Long)query.uniqueResult();
  }

  public String getUserLocale()
  {
    Locale locale = UserManager.getLocale();

    String userLocale = locale.toString();

    return userLocale;
  }

  /**
   * Get all commLogs assigned to this user.
   * 
   * @param userId
   * @return List
   */
  public List getCommLogsAssignedToUser( Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.dao.commlog.CommLogsAssignedToUser" ).setLong( "userId", userId.longValue() ).list();
  }

  /**
   * Get all open commLogs assigned to this user.
   * 
   * @param userId
   * @return List
   */
  public List getOpenCommLogsAssignedToUser( Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.dao.commlog.OpenCommLogsAssignedToUser" ).setLong( "userId", userId.longValue() ).setString( "statusTypeCode", CommLogStatusType.OPEN_CODE ).list();
  }

  /**
   * Get all open commLogs.
   * 
   * @return List
   */
  public List getAllOpenCommLogs()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.dao.commlog.AllOpenCommLogs" ).setString( "statusTypeCode", CommLogStatusType.OPEN_CODE ).list();
  }

  /**
   * Save this commLog.
   * 
   * @param commLog
   * @return CommLog
   */
  public CommLog saveCommLog( CommLog commLog )
  {
    if ( commLog.getId() != null )
    { // this is an update so do a merge
      CommLog dbCommLog = getCommLogById( commLog.getId() );
      dbCommLog.getComments().addAll( commLog.getComments() );
      dbCommLog.setAssignedByUser( commLog.getAssignedByUser() );
      dbCommLog.setAssignedToUser( commLog.getAssignedToUser() );
      dbCommLog.setCommLogCategoryType( commLog.getCommLogCategoryType() );
      dbCommLog.setCommLogReasonType( commLog.getCommLogReasonType() );
      dbCommLog.setCommLogSourceType( commLog.getCommLogSourceType() );
      dbCommLog.setCommLogStatusType( commLog.getCommLogStatusType() );
      dbCommLog.setCommLogUrgencyType( commLog.getCommLogUrgencyType() );
      dbCommLog.setDateEscalated( commLog.getDateEscalated() );
      dbCommLog.setUser( commLog.getUser() );
      return (CommLog)HibernateUtil.saveOrUpdateOrDeepMerge( dbCommLog );
    }
    return (CommLog)HibernateUtil.saveOrUpdateOrDeepMerge( commLog );
  }

  /**
   * Get CommLog by id.
   * 
   * @param commLogId
   * @return CommLog
   */
  public CommLog getCommLogById( Long commLogId )
  {
    return (CommLog)getSession().get( CommLog.class, commLogId );
  }

  private class CommLogRowMapper implements RowMapper<CommLogValueBean>
  {
    /**
     * Overridden from
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     * @param rs
     * @param rowNum
     * @return Map valueMapByColumn
     * @throws SQLException
     */
    public CommLogValueBean mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      CommLogValueBean commLog = new CommLogValueBean();
      commLog.setRowNum( rs.getLong( "RN" ) );
      commLog.setSubject( rs.getString( "SUBJECT" ) );
      commLog.setDateCreated( rs.getTimestamp( "DATE_CREATED" ) );
      commLog.setCommLogId( rs.getLong( "COMM_LOG_ID" ) );
      commLog.setPlainMessage( rs.getString( "PLAIN_MESSAGE_1" ) + rs.getString( "PLAIN_MESSAGE_2" ) );
      commLog.setCreatedBy( rs.getLong( "CREATED_BY" ) );

      return commLog;
    }
  }

}
