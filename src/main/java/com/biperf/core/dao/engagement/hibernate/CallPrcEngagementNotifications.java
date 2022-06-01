
package com.biperf.core.dao.engagement.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.EngagementNotificationValueBean;

import oracle.jdbc.OracleTypes;

/**
 * Procedure to get the Engagement Notifications Data
 * CallPrcEngagementNotifications.
 * 
 * @author kandhi
 * @since Sep 12, 2014
 * @version 1.0
 */
public class CallPrcEngagementNotifications extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENGAGEMENT_NOTIFICATION";

  public CallPrcEngagementNotifications( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_mode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_month", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_end_year", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_result_set1", OracleTypes.CURSOR, new EngagementNotificationDataExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> extractParams )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", extractParams.get( "userId" ) );
    inParams.put( "p_in_mode", extractParams.get( "mode" ) );
    inParams.put( "p_in_end_month", extractParams.get( "endMonth" ) );
    inParams.put( "p_in_end_year", extractParams.get( "endYear" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class EngagementNotificationDataExtractor implements ResultSetExtractor
  {
    @Override
    public EngagementNotificationValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      EngagementNotificationValueBean engagementNotificationValueBean = null;
      while ( rs.next() )
      {

        engagementNotificationValueBean = new EngagementNotificationValueBean();
        engagementNotificationValueBean.setCompanyGoal( rs.getInt( "company_goal" ) );
        engagementNotificationValueBean.setScoreActive( rs.getBoolean( "is_score_active" ) );
        engagementNotificationValueBean.setRecvCompanyAvg( rs.getInt( "recv_company_avg" ) );
        engagementNotificationValueBean.setSentCompanyAvg( rs.getInt( "sent_company_avg" ) );
        engagementNotificationValueBean.setConnFromCompanyAvg( rs.getInt( "conn_from_company_avg" ) );
        engagementNotificationValueBean.setConnToCompanyAvg( rs.getInt( "conn_to_company_avg" ) );
        engagementNotificationValueBean.setLoginCompanyAvg( rs.getInt( "login_company_avg" ) );
        engagementNotificationValueBean.setRecvTeamAvg( rs.getInt( "recv_team_avg" ) );
        engagementNotificationValueBean.setSentTeamAvg( rs.getInt( "sent_team_avg" ) );
        engagementNotificationValueBean.setConnFromTeamAvg( rs.getInt( "conn_from_team_avg" ) );
        engagementNotificationValueBean.setConnToTeamAvg( rs.getInt( "conn_to_team_avg" ) );
        engagementNotificationValueBean.setLoginTeamAvg( rs.getInt( "login_team_avg" ) );
        engagementNotificationValueBean.setCompanyAvg( rs.getInt( "company_avg" ) );
        engagementNotificationValueBean.setFirstName( rs.getString( "user_name" ) );
        engagementNotificationValueBean.setUserLocale( rs.getString( "user_locale" ) );
        engagementNotificationValueBean.setEmailAddress( rs.getString( "email_address" ) );
        engagementNotificationValueBean.setOrgLevel( rs.getString( "node_name" ) );
        engagementNotificationValueBean.setScore( rs.getInt( "score" ) );
        engagementNotificationValueBean.setReceivedCnt( rs.getInt( "received_count" ) );
        engagementNotificationValueBean.setSentCnt( rs.getInt( "sent_count" ) );
        engagementNotificationValueBean.setConnectedToCnt( rs.getInt( "connected_to_count" ) );
        engagementNotificationValueBean.setConnectedByCnt( rs.getInt( "connected_from_count" ) );
        engagementNotificationValueBean.setLoginActivityCnt( rs.getInt( "login_activity_count" ) );
        engagementNotificationValueBean.setReceivedTarget( rs.getInt( "received_target" ) );
        engagementNotificationValueBean.setSentTarget( rs.getInt( "sent_target" ) );
        engagementNotificationValueBean.setConnectedToTarget( rs.getInt( "connected_target" ) );
        engagementNotificationValueBean.setConnectedByTarget( rs.getInt( "connected_from_target" ) );
        engagementNotificationValueBean.setLoginActivityTarget( rs.getInt( "login_activity_target" ) );
        engagementNotificationValueBean.setAchievedCnt( rs.getInt( "score_achieved_count" ) );
        engagementNotificationValueBean.setTotalMembersAvailable( rs.getInt( "total_participant_count" ) );

      }
      return engagementNotificationValueBean;
    }
  }
}
