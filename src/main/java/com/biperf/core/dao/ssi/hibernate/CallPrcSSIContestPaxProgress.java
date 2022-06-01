
package com.biperf.core.dao.ssi.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankTeamValueBean;
import com.biperf.core.value.ssi.SSIPaxContestLevelValueBean;
import com.biperf.core.value.ssi.SSIPaxDTGTActivityProgressValueBean;

import oracle.jdbc.OracleTypes;

/**
 * CallPrcSSIContestProgress is used to get the contest progress data for participant. 
 * Input param userId is must because it is for participant detail page.
 * 
 * @author dudam
 * @since Jan 29, 2015
 * @version 1.0
 */
public class CallPrcSSIContestPaxProgress extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST_DATA.PRC_SSI_CONTEST_PAX_PROGRESS";

  public CallPrcSSIContestPaxProgress( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_contest_type", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_obj_ref_cursor", OracleTypes.CURSOR, new SSIContestPaxProgressObjectivesResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_ref_cursor", OracleTypes.CURSOR, new SSIContestPaxProgressStepItUpResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_DTGT_cursor", OracleTypes.CURSOR, new SSIContestProgressDtgtResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_ref_cursor", OracleTypes.CURSOR, new SSIContestPaxProgressStackRankResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_payout_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressStackRankPayoutsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_pax_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressStackRankPaxResultSetExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", inParameters.get( "contestId" ) );
    inParams.put( "p_in_user_id", inParameters.get( "userId" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /**
   * SSIContestPaxProgressObjectivesResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestPaxProgressObjectivesResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPaxProgressDetailValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestPaxProgressDetailValueBean valueBean = new SSIContestPaxProgressDetailValueBean();
      while ( rs.next() )
      {
        valueBean.setContestId( rs.getLong( "SSI_CONTEST_ID" ) );
        valueBean.setObjectiveAmount( rs.getDouble( "OBJECTIVE_AMOUNT" ) );
        // rs.getDouble returns 0 if SQL value is null. So, we are checking it against BigDecimal to
        // differentiate between null and 0.0
        valueBean.setActivityAmount( rs.getBigDecimal( "ACTIVITY_AMT" ) != null ? rs.getDouble( "ACTIVITY_AMT" ) : null );
        valueBean.setToGoAmount( rs.getDouble( "TO_GO" ) );
        valueBean.setPercentageAcheived( rs.getInt( "PERC_ACHIEVED" ) );
        valueBean.setPotentialPayout( rs.getLong( "POTENTIAL_PAYOUT" ) );
        valueBean.setObjectivePayout( rs.getLong( "OBJECTIVE_PAYOUT" ) );
        valueBean.setObjectiveBonusPayout( rs.getLong( "OBJECTIVE_BONUS_PAYOUT" ) );
        valueBean.setBonusEarned( rs.getLong( "BONUS_PAYOUT" ) );
        valueBean.setObjectiveBonusIncrement( rs.getLong( "OBJECTIVE_BONUS_INCREMENT" ) );
        valueBean.setStackRank( rs.getInt( "STACK_RANK" ) );
        valueBean.setTotalPax( rs.getInt( "TOTAL_PAX" ) );
        valueBean.setLastProgressDate( rs.getDate( "LAST_PROGRESS_UPDATE_DATE" ) );
        valueBean.setAvatarUrl( rs.getString( "AVATAR_URL" ) );
        valueBean.setObjectiveDescription( rs.getString( "ACTIVITY_DESCRIPTION" ) );
        valueBean.setObjectivePayoutDescription( rs.getString( "OBJECTIVE_PAYOUT_DESCRIPTION" ) );
        valueBean.setObjectiveBonusCap( rs.getLong( "OBJECTIVE_BONUS_CAP" ) );
      }
      return valueBean;
    }
  }

  /**
   * SSIContestPaxProgressStepItUpResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestPaxProgressStepItUpResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIPaxContestLevelValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIPaxContestLevelValueBean> levels = new ArrayList<SSIPaxContestLevelValueBean>();
      while ( rs.next() )
      {
        SSIPaxContestLevelValueBean level = new SSIPaxContestLevelValueBean();
        // rs.getLong( "SSI_CONTEST_ID" );
        // rs.getLong( "USER_ID" );
        // rs.getDouble returns 0 if SQL value is null. So, we are checking it against BigDecimal to
        // differentiate between null and 0.0
        level.setProgress( rs.getBigDecimal( "PROGRESS" ) != null ? rs.getDouble( "PROGRESS" ) : null );
        level.setName( rs.getString( "SEQUENCE_NUMBER" ) );
        level.setPayout( rs.getLong( "PAYOUT_AMOUNT" ) );
        level.setGoalAmount( rs.getDouble( "GOAL_AMOUNT" ) );
        level.setGoal( rs.getDouble( "GOAL_AMOUNT" ) );
        level.setCompleted( rs.getBoolean( "LEVEL_COMPLETED" ) );
        level.setCurrentLevel( rs.getBoolean( "IS_CURRENT_LEVEL" ) );
        level.setGoalPercent( String.valueOf( rs.getDouble( "GOAL_PERC" ) ) );
        level.setRemaining( rs.getDouble( "REMAINING" ) );
        level.setBaseline( rs.getLong( "SIU_BASELINE_AMOUNT" ) );
        level.setAvatarUrl( rs.getString( "AVATAR_URL" ) );
        level.setParticipantsCount( rs.getInt( "TOTAL_PAX" ) );
        level.setStackRank( rs.getInt( "STACK_RANK" ) );
        level.setBadgeRuleId( rs.getLong( "BADGE_RULE_ID" ) );
        level.setBadgeName( rs.getString( "BADGE_NAME" ) );
        level.setBadgeUrl( rs.getString( "BADGE_IMAGE" ) );
        level.setBonusEarned( rs.getLong( "BONUS_PAYOUT" ) );
        level.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        levels.add( level );
      }
      return levels;
    }
  }

  /**
   * SSIContestProgressDtgtResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressDtgtResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIPaxDTGTActivityProgressValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ArrayList<SSIPaxDTGTActivityProgressValueBean> activities = new ArrayList<SSIPaxDTGTActivityProgressValueBean>();
      while ( rs.next() )
      {
        SSIPaxDTGTActivityProgressValueBean activity = new SSIPaxDTGTActivityProgressValueBean();
        activity.setContestId( rs.getLong( "SSI_CONTEST_ID" ) );
        activity.setActivityId( rs.getLong( "SSI_CONTEST_ACTIVITY_ID" ) );
        activity.setActivityDescription( rs.getString( "DESCRIPTION" ) );
        activity.setGoal( rs.getDouble( "GOAL" ) );
        // rs.getDouble returns 0 if SQL value is null. So, we are checking it against BigDecimal to
        // differentiate between null and 0.0
        activity.setProgress( rs.getBigDecimal( "PROGRESS" ) != null ? rs.getDouble( "PROGRESS" ) : null );
        activity.setPercentProgress( rs.getLong( "PERC_OF_PROGRESS" ) );
        activity.setMinQualifier( rs.getDouble( "MIN_QUALIFIER" ) );
        activity.setForEvery( rs.getDouble( "FOR_EVERY" ) );
        activity.setWillEarn( rs.getLong( "WILL_EARN" ) );
        activity.setPayoutCap( rs.getDouble( "PAYOUT_CAP_AMOUNT" ) );
        activity.setPayout( rs.getLong( "PAYOUT_VALUE" ) );
        activity.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        activity.setPayoutQuantity( rs.getLong( "PAYOUT_QUANTITY" ) );
        Integer paxCount = rs.getInt( "TOTAL_PAX" );
        String avatarUrl = rs.getString( "AVATAR_URL" );
        activity.setStackRank( new SSIContestStackRankPaxValueBean( rs.getInt( "STACK_RANK" ), paxCount, avatarUrl ) );
        activities.add( activity );
      }
      return activities;
    }
  }

  /**
   * SSIContestPaxProgressStackRankResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestPaxProgressStackRankResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPaxProgressDetailValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestPaxProgressDetailValueBean valueBean = new SSIContestPaxProgressDetailValueBean();
      while ( rs.next() )
      {
        valueBean.setAvatarUrl( rs.getString( "AVATAR" ) );
        String activityAmount = rs.getString( "ACTIVITY_AMT" );
        valueBean.setActivityAmount( StringUtil.isNullOrEmpty( activityAmount ) ? null : Double.parseDouble( activityAmount ) );
        valueBean.setStackRank( rs.getInt( "STACK_RANK_POSITION" ) );
        valueBean.setTotalPax( rs.getInt( "MAX_STACK_RANK" ) );
        valueBean.setBehindLeader( rs.getLong( "BEHIND_LEADER" ) );
        valueBean.setMinQualifier( rs.getDouble( "STACK_RANK_QUALIFIER_AMOUNT" ) );
        valueBean.setPayoutAmount( rs.getLong( "PAYOUT_AMOUNT" ) );
        valueBean.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
      }
      return valueBean;
    }
  }

  /**
   * SSIContestProgressStackRankPayoutsResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressStackRankPayoutsResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestStackRankPayoutValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestStackRankPayoutValueBean> contestProgressPayouts = new ArrayList<SSIContestStackRankPayoutValueBean>();
      while ( rs.next() )
      {
        Long rank = rs.getLong( "RANK_POSITION" );
        Long payout = rs.getLong( "PAYOUT_AMOUNT" );
        String payoutDescription = rs.getString( "PAYOUT_DESC" );
        Long badgeId = rs.getLong( "BADGE_RULE_ID" );
        String badgeName = rs.getString( "BADGE_NAME" );
        String badgeUrl = rs.getString( "BADGE_IMAGE" );
        SSIContestStackRankPayoutValueBean payoutValueBean = new SSIContestStackRankPayoutValueBean( rank, payout, payoutDescription, badgeId, badgeName, badgeUrl );
        contestProgressPayouts.add( payoutValueBean );
      }
      return contestProgressPayouts;
    }
  }

  /**
   * SSIContestProgressStackRankPaxResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressStackRankPaxResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestStackRankTeamValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestStackRankTeamValueBean> stackRanks = new ArrayList<SSIContestStackRankTeamValueBean>();
      while ( rs.next() )
      {
        SSIContestStackRankTeamValueBean stackRank = new SSIContestStackRankTeamValueBean();
        stackRank.setRank( rs.getInt( "RANK_POSITION" ) );
        stackRank.setParticipantId( rs.getLong( "USER_ID" ) );
        stackRank.setFirstName( rs.getString( "FIRST_NAME" ) );
        stackRank.setLastName( rs.getString( "LAST_NAME" ) );
        stackRank.setAvatarUrl( rs.getString( "AVATAR_URL" ) );
        stackRank.setScore( rs.getDouble( "ACTIVITY_AMT" ) );
        stackRank.setPayout( rs.getLong( "PAYOUT_AMOUNT" ) );
        stackRank.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        stackRanks.add( stackRank );
      }
      return stackRanks;
    }
  }

}
