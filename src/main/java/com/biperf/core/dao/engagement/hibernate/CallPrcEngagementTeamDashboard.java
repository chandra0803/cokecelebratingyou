
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
 * CallPrcEngagementTeamDashboard.
 * 
 * @author kandhi
 * @since Jun 11, 2014
 * @version 1.0
 */
public class CallPrcEngagementTeamDashboard extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENGAGEMENT_MANAGER_SCORE";

  public CallPrcEngagementTeamDashboard( DataSource ds )
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
    inParams.put( "p_in_rownum_start", extractParams.get( "rowNumStart" ) );
    inParams.put( "p_in_rownum_end", extractParams.get( "rowNumEnd" ) );
    // Default sort
    inParams.put( "p_in_sort_mem_col", "last_name" );
    inParams.put( "p_in_sort_mem_order", "asc" );
    inParams.put( "p_in_sort_team_col", "node_name" );
    inParams.put( "p_in_sort_team_order", "asc" );

    // Always send the date in English locale for the procedure input
    inParams.put( "p_in_start_date", DateUtils.toDisplayString( (Date)extractParams.get( "startDate" ), Locale.ENGLISH ) );
    inParams.put( "p_in_end_date", DateUtils.toDisplayString( (Date)extractParams.get( "endDate" ), Locale.ENGLISH ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
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

  private class EngagementNodesDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<NodeBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NodeBean> nodeBeanList = new ArrayList<NodeBean>();
      while ( rs.next() )
      {
        NodeBean nodeBean = new NodeBean();
        nodeBean.setId( rs.getLong( "node_id" ) );
        nodeBean.setName( rs.getString( "node_name" ) );
        nodeBeanList.add( nodeBean );
      }
      return nodeBeanList;
    }
  }

  private class EngagementManagerNodesDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<NodeBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NodeBean> nodeBeanList = new ArrayList<NodeBean>();
      while ( rs.next() )
      {
        NodeBean nodeBean = new NodeBean();
        nodeBean.setId( rs.getLong( "node_id" ) );
        nodeBean.setName( rs.getString( "node_name" ) );
        nodeBeanList.add( nodeBean );
      }
      return nodeBeanList;
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

  private class EngagementTeamSumDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementTeamSumValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementTeamSumValueBean> engagementTeamSumValueBeanList = new ArrayList<EngagementTeamSumValueBean>();
      while ( rs.next() )
      {
        EngagementTeamSumValueBean engagementTeamSumValueBean = new EngagementTeamSumValueBean();
        engagementTeamSumValueBean.setCompanyGoal( rs.getInt( "company_goal" ) );
        engagementTeamSumValueBean.setScore( rs.getInt( "score" ) );
        engagementTeamSumValueBean.setReceivedCnt( rs.getInt( "received_count" ) );
        engagementTeamSumValueBean.setSentCnt( rs.getInt( "sent_count" ) );
        engagementTeamSumValueBean.setConnectedToCnt( rs.getBigDecimal( "connected_to_count" ) != null ? rs.getBigDecimal( "connected_to_count" ).intValue() : null );
        engagementTeamSumValueBean.setConnectedFromCnt( rs.getBigDecimal( "connected_from_count" ) != null ? rs.getBigDecimal( "connected_from_count" ).intValue() : null );
        engagementTeamSumValueBean.setLoginActivityCnt( rs.getInt( "login_activity_count" ) );
        engagementTeamSumValueBean.setReceivedTarget( rs.getInt( "received_target" ) );
        engagementTeamSumValueBean.setSentTarget( rs.getInt( "sent_target" ) );
        engagementTeamSumValueBean.setConnectedToTarget( rs.getBigDecimal( "connected_target" ) != null ? rs.getBigDecimal( "connected_target" ).intValue() : null );
        engagementTeamSumValueBean.setConnectedFromTarget( rs.getBigDecimal( "connected_from_target" ) != null ? rs.getBigDecimal( "connected_from_target" ).intValue() : null );
        engagementTeamSumValueBean.setLoginActivityTarget( rs.getInt( "login_activity_target" ) );
        engagementTeamSumValueBeanList.add( engagementTeamSumValueBean );
      }
      return engagementTeamSumValueBeanList;
    }
  }

  private class EngagementTeamMembersDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementTeamMembersValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList = new ArrayList<EngagementTeamMembersValueBean>();
      while ( rs.next() )
      {
        EngagementTeamMembersValueBean engagementTeamMembersValueBean = new EngagementTeamMembersValueBean();
        engagementTeamMembersValueBean.setCompanyGoal( rs.getInt( "company_goal" ) );
        engagementTeamMembersValueBean.setNodeId( rs.getLong( "node_id" ) );
        engagementTeamMembersValueBean.setNodeName( rs.getString( "node_name" ) );
        engagementTeamMembersValueBean.setUserId( rs.getLong( "user_id" ) );
        engagementTeamMembersValueBean.setFirstName( rs.getString( "first_name" ) );
        engagementTeamMembersValueBean.setLastName( rs.getString( "last_name" ) );
        engagementTeamMembersValueBean.setAvatarUrl( rs.getString( "avatar_url" ) );
        engagementTeamMembersValueBean.setScore( rs.getInt( "score" ) );
        engagementTeamMembersValueBean.setReceivedTarget( rs.getInt( "received_target" ) );
        engagementTeamMembersValueBean.setSentTarget( rs.getInt( "sent_target" ) );
        engagementTeamMembersValueBean.setConnectedToTarget( rs.getInt( "connected_target" ) );
        engagementTeamMembersValueBean.setConnectedFromTarget( rs.getInt( "connected_from_target" ) );
        engagementTeamMembersValueBean.setLoginActivityTarget( rs.getInt( "login_activity_target" ) );
        engagementTeamMembersValueBean.setReceivedCnt( rs.getInt( "received_count" ) );
        engagementTeamMembersValueBean.setSentCnt( rs.getInt( "sent_count" ) );
        engagementTeamMembersValueBean.setConnectedToCnt( rs.getInt( "connected_to_count" ) );
        engagementTeamMembersValueBean.setConnectedFromCnt( rs.getInt( "connected_from_count" ) );
        engagementTeamMembersValueBean.setLoginActivityCnt( rs.getInt( "login_activity_count" ) );
        engagementTeamMembersValueBeanList.add( engagementTeamMembersValueBean );
      }
      return engagementTeamMembersValueBeanList;
    }
  }

  private class EngagementTeamsDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementTeamsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementTeamsValueBean> engagementTeamsValueBeanList = new ArrayList<EngagementTeamsValueBean>();
      while ( rs.next() )
      {
        EngagementTeamsValueBean engagementTeamsValueBean = new EngagementTeamsValueBean();
        engagementTeamsValueBean.setCompanyGoal( rs.getInt( "company_goal" ) );
        engagementTeamsValueBean.setNodeId( rs.getLong( "node_id" ) );
        engagementTeamsValueBean.setNodeName( rs.getString( "node_name" ) );
        engagementTeamsValueBean.setParentNodeId( rs.getLong( "parent_node_id" ) );
        engagementTeamsValueBean.setScore( rs.getInt( "score" ) );
        engagementTeamsValueBean.setReceivedTarget( rs.getInt( "received_target" ) );
        engagementTeamsValueBean.setSentTarget( rs.getInt( "sent_target" ) );
        engagementTeamsValueBean.setConnectedToTarget( rs.getBigDecimal( "connected_target" ) != null ? rs.getBigDecimal( "connected_target" ).intValue() : null );
        engagementTeamsValueBean.setConnectedFromTarget( rs.getBigDecimal( "connected_from_target" ) != null ? rs.getBigDecimal( "connected_from_target" ).intValue() : null );
        engagementTeamsValueBean.setLoginActivityTarget( rs.getInt( "login_activity_target" ) );
        engagementTeamsValueBean.setReceivedCnt( rs.getInt( "received_count" ) );
        engagementTeamsValueBean.setSentCnt( rs.getInt( "sent_count" ) );
        engagementTeamsValueBean.setConnectedToCnt( rs.getBigDecimal( "connected_to_count" ) != null ? rs.getBigDecimal( "connected_to_count" ).intValue() : null );
        engagementTeamsValueBean.setConnectedFromCnt( rs.getBigDecimal( "connected_from_count" ) != null ? rs.getBigDecimal( "connected_from_count" ).intValue() : null );
        engagementTeamsValueBean.setLoginActivityCnt( rs.getInt( "login_activity_count" ) );
        engagementTeamsValueBean.setManagerName( rs.getString( "manager_name" ) );

        engagementTeamsValueBeanList.add( engagementTeamsValueBean );
      }
      return engagementTeamsValueBeanList;
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
        engagementSummaryValueBean.setScoreAchievedCnt( rs.getInt( "score_achieved_count" ) );
        engagementSummaryValueBean.setReceivedAchievedCnt( rs.getInt( "recv_achieved_count" ) );
        engagementSummaryValueBean.setSentAchievedCnt( rs.getInt( "sent_achieved_count" ) );
        engagementSummaryValueBean.setConnectedToAchievedCnt( rs.getInt( "conn_to_achieved_count" ) );
        engagementSummaryValueBean.setConnectedFromAchievedCnt( rs.getInt( "conn_from_achieved_count" ) );
        engagementSummaryValueBean.setLoginAchievedCnt( rs.getInt( "login_achieved_count" ) );
        engagementSummaryValueBean.setTotalParticipantCount( rs.getInt( "total_participant_count" ) );
        engagementSummaryValueBean.setTotalTeamsAvailable( rs.getInt( "child_node_count" ) );
        engagementSummaryValueBean.setTotalMembersAvailable( rs.getInt( "team_mem_count" ) );
        engagementSummaryValueBean.setConnectedToPaxCnt( rs.getInt( "connected_to_pax_count" ) );
        engagementSummaryValueBean.setConnectedFromPaxCnt( rs.getInt( "connected_from_pax_count" ) );
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
      return new ArrayList<EngagementSiteVisitsLoginValueBean>();
    }
  }
}
