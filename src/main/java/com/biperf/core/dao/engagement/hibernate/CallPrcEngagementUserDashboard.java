
package com.biperf.core.dao.engagement.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.EngagementAveragesValueBean;
import com.biperf.core.value.EngagementBehaviorValueBean;
import com.biperf.core.value.EngagementSiteVisitsLoginValueBean;
import com.biperf.core.value.EngagementSummaryValueBean;
import com.biperf.core.value.EngagementTeamMembersValueBean;
import com.biperf.core.value.EngagementTeamSumValueBean;
import com.biperf.core.value.EngagementTeamsValueBean;
import com.biperf.core.value.NodeBean;

import oracle.jdbc.OracleTypes;

/**
 * 
 * CallPrcEngagementUserDashboard.
 * 
 * @author kandhi
 * @since Jun 16, 2014
 * @version 1.0
 */
public class CallPrcEngagementUserDashboard extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENGAGEMENT_MANAGER_SCORE";

  public CallPrcEngagementUserDashboard( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_mode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_time_frame", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_node_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_month", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_end_year", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_start", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_end", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sort_mem_col", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sort_mem_order", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sort_team_col", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sort_team_order", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_start_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_date", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set1", OracleTypes.CURSOR, new EngagementSummaryDataExtractor() ) );// Summary
    declareParameter( new SqlOutParameter( "p_out_result_set2", OracleTypes.CURSOR, new EngagementTeamMembersDataExtractor() ) );// Team-Members
    declareParameter( new SqlOutParameter( "p_out_result_set3", OracleTypes.CURSOR, new EngagementTeamsDataExtractor() ) );// Teams
    declareParameter( new SqlOutParameter( "p_out_result_set4", OracleTypes.CURSOR, new EngagementTeamSumDataExtractor() ) );// Team-Sum
    declareParameter( new SqlOutParameter( "p_out_result_set5", OracleTypes.CURSOR, new EngagementBehaviorDataExtractor() ) );// Behavior
    declareParameter( new SqlOutParameter( "p_out_result_set6", OracleTypes.CURSOR, new EngagementNodesDataExtractor() ) );// Nodes
    declareParameter( new SqlOutParameter( "p_out_result_set7", OracleTypes.CURSOR, new EngagementAveragesDataExtractor() ) );// Team-Company-Average
    declareParameter( new SqlOutParameter( "p_out_result_set8", OracleTypes.CURSOR, new EngagementSiteVisitsDataExtractor() ) );// Login-Visits
    declareParameter( new SqlOutParameter( "p_out_result_set9", OracleTypes.CURSOR, new EngagementManagerNodesDataExtractor() ) );// Manager-Nodes
    declareParameter( new SqlOutParameter( "p_out_user_name", Types.VARCHAR ) );// User-Name
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );// Output-return-code
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> extractParams )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", extractParams.get( "userId" ) );
    inParams.put( "p_in_mode", extractParams.get( "mode" ) );
    inParams.put( "p_in_time_frame", extractParams.get( "timeframeType" ) );
    inParams.put( "p_in_node_id", extractParams.get( "nodeId" ) );
    inParams.put( "p_in_locale", extractParams.get( "locale" ) );
    inParams.put( "p_in_end_month", extractParams.get( "endMonth" ) );
    inParams.put( "p_in_end_year", extractParams.get( "endYear" ) );
    inParams.put( "p_in_rownum_start", 0 );
    inParams.put( "p_in_rownum_end", 100 );
    inParams.put( "p_in_sort_mem_col", null );
    inParams.put( "p_in_sort_mem_order", null );
    inParams.put( "p_in_sort_team_col", null );
    inParams.put( "p_in_sort_team_order", null );
    // Always send the date in English locale for the procedure input
    inParams.put( "p_in_start_date", DateUtils.toDisplayString( (Date)extractParams.get( "startDate" ), Locale.ENGLISH ) );
    inParams.put( "p_in_end_date", DateUtils.toDisplayString( (Date)extractParams.get( "endDate" ), Locale.ENGLISH ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class EngagementTeamSumDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementTeamSumValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      return new ArrayList<EngagementTeamSumValueBean>();
    }
  }

  private class EngagementTeamMembersDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementTeamMembersValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      return new ArrayList<EngagementTeamMembersValueBean>();
    }
  }

  private class EngagementTeamsDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementTeamsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      return new ArrayList<EngagementTeamsValueBean>();
    }
  }

  private class EngagementNodesDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<NodeBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      return new ArrayList<NodeBean>();
    }
  }

  private class EngagementManagerNodesDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<NodeBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      return new ArrayList<NodeBean>();
    }
  }

  private class EngagementBehaviorDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementBehaviorValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementBehaviorValueBean> engagementBehaviorValueBeanList = new ArrayList<EngagementBehaviorValueBean>();
      while ( rs.next() )
      {
        EngagementBehaviorValueBean engagementBehaviorValueBean = new EngagementBehaviorValueBean();
        engagementBehaviorValueBean.setBehavior( rs.getString( "behavior" ) );
        engagementBehaviorValueBean.setSentCnt( rs.getInt( "sent_count" ) );
        engagementBehaviorValueBean.setReceivedCnt( rs.getInt( "received_count" ) );
        engagementBehaviorValueBean.setBadgeImageUrl( rs.getString( "behavior_badge" ) );
        engagementBehaviorValueBeanList.add( engagementBehaviorValueBean );
      }
      return engagementBehaviorValueBeanList;
    }
  }

  private class EngagementAveragesDataExtractor implements ResultSetExtractor
  {
    @Override
    public EngagementAveragesValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      EngagementAveragesValueBean engagementAveragesValueBean = null;
      while ( rs.next() )
      {
        engagementAveragesValueBean = new EngagementAveragesValueBean();
        engagementAveragesValueBean.setConnectedFromCompanyAvg( rs.getInt( "v_conn_from_company_avg" ) );
        engagementAveragesValueBean.setConnectedFromTeamAvg( rs.getInt( "v_conn_from_team_avg" ) );
        engagementAveragesValueBean.setConnectedToCompanyAvg( rs.getInt( "v_conn_to_company_avg" ) );
        engagementAveragesValueBean.setConnectedToTeamAvg( rs.getInt( "v_conn_to_team_avg" ) );
        engagementAveragesValueBean.setLoginActivityCompanyAvg( rs.getInt( "v_login_company_avg" ) );
        engagementAveragesValueBean.setLoginActivityTeamAvg( rs.getInt( "v_login_team_avg" ) );
        engagementAveragesValueBean.setRecRecvCompanyAvg( rs.getInt( "v_rec_recv_company_avg" ) );
        engagementAveragesValueBean.setRecRecvTeamAvg( rs.getInt( "v_rec_recv_team_avg" ) );
        engagementAveragesValueBean.setRecSentCompanyAvg( rs.getInt( "v_rec_sent_company_avg" ) );
        engagementAveragesValueBean.setRecSentTeamAvg( rs.getInt( "v_rec_sent_team_avg" ) );
        engagementAveragesValueBean.setScoreCompanyAvg( rs.getInt( "v_score_company_avg" ) );
      }
      return engagementAveragesValueBean;
    }
  }

  private class EngagementSummaryDataExtractor implements ResultSetExtractor
  {
    @Override
    public EngagementSummaryValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      EngagementSummaryValueBean engagementSummaryValueBean = null;
      while ( rs.next() )
      {
        engagementSummaryValueBean = new EngagementSummaryValueBean();
        engagementSummaryValueBean.setAsofDate( rs.getTimestamp( "as_of_date" ) );
        engagementSummaryValueBean.setCompanyGoal( rs.getInt( "company_goal" ) );
        boolean isScoreActive = rs.getBoolean( "is_score_active" );
        engagementSummaryValueBean.setScoreActive( isScoreActive );
        if ( !isScoreActive )
        {
          engagementSummaryValueBean.setSelectedBenchmarks( rs.getLong( "selected_benchmarks" ) );
        }
        engagementSummaryValueBean.setDisplayTarget( rs.getBoolean( "display_target" ) );
        engagementSummaryValueBean.setScoreActive( rs.getBoolean( "is_score_active" ) );
        engagementSummaryValueBean.setScore( rs.getInt( "score" ) );
        engagementSummaryValueBean.setReceivedCnt( rs.getInt( "received_count" ) );
        engagementSummaryValueBean.setSentCnt( rs.getInt( "sent_count" ) );
        engagementSummaryValueBean.setConnectedToCnt( rs.getInt( "connected_to_count" ) );
        engagementSummaryValueBean.setConnectedFromCnt( rs.getInt( "connected_from_count" ) );
        engagementSummaryValueBean.setLoginActivityCnt( rs.getInt( "login_activity_count" ) );
        engagementSummaryValueBean.setReceivedTarget( rs.getInt( "received_target" ) );
        engagementSummaryValueBean.setSentTarget( rs.getInt( "sent_target" ) );
        engagementSummaryValueBean.setConnectedToTarget( rs.getInt( "connected_target" ) );
        engagementSummaryValueBean.setConnectedFromTarget( rs.getInt( "connected_from_target" ) );
        engagementSummaryValueBean.setLoginActivityTarget( rs.getInt( "login_activity_target" ) );
        engagementSummaryValueBean.setConnectedToPaxCnt( rs.getInt( "connected_to_count" ) );
        engagementSummaryValueBean.setConnectedFromPaxCnt( rs.getInt( "connected_from_count" ) );
        // Fix for 59693
        engagementSummaryValueBean.setDate( rs.getString( "as_of_date_str" ) );
        engagementSummaryValueBean.setTime( rs.getString( "as_of_time_str" ) );
        engagementSummaryValueBean.setTimeZoneId( rs.getString( "timezone_id" ) );
      }
      return engagementSummaryValueBean;
    }
  }

  private class EngagementSiteVisitsDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementSiteVisitsLoginValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementSiteVisitsLoginValueBean> engagementSiteVisitsLoginValueBeanList = new ArrayList<EngagementSiteVisitsLoginValueBean>();
      while ( rs.next() )
      {
        EngagementSiteVisitsLoginValueBean engagementSiteVisitsLoginValueBean = new EngagementSiteVisitsLoginValueBean();
        engagementSiteVisitsLoginValueBean.setDate( rs.getString( "login_date" ) );
        engagementSiteVisitsLoginValueBean.setTime( rs.getString( "login_time" ) );
        // Fix for 59693
        engagementSiteVisitsLoginValueBean.setTimeZoneId( rs.getString( "timezone_id" ) );
        String localeSpecificTime = DateUtils.convertTimeToTimezone( engagementSiteVisitsLoginValueBean.getTime(), engagementSiteVisitsLoginValueBean.getTimeZoneId() );
        engagementSiteVisitsLoginValueBean.setLocaleTime( localeSpecificTime );
        engagementSiteVisitsLoginValueBeanList.add( engagementSiteVisitsLoginValueBean );
      }
      return engagementSiteVisitsLoginValueBeanList;
    }
  }
}
