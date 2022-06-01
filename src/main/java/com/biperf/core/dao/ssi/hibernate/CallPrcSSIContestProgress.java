
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

import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankTeamValueBean;
import com.biperf.core.value.ssi.SSIPaxContestLevelValueBean;

import oracle.jdbc.OracleTypes;

/**
 * CallPrcSSIContestProgress is used to get the contest progress data for creator and manager in contest detail page.
 * Input params for userId will be null for creator user. 
 * It is mainly used to get the contest progress data(goal,activity,percentage,togo etc) creator/manager
 * 
 * @author dudam
 * @since Jan 27, 2015
 * @version 1.0
 */
public class CallPrcSSIContestProgress extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST_DATA.PRC_SSI_CONTEST_PROGRESS";

  public CallPrcSSIContestProgress( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.

    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_contest_type", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressSiuResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_level_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressSiuLevelsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressSrResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_payout_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressSrPayoutsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_stackrank_cursor", OracleTypes.CURSOR, new SSIContestProgressStackRankResultSetExtractor() ) );
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
   * SSIContestManagerResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestProgressValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestProgressValueBean> contestProgresses = new ArrayList<SSIContestProgressValueBean>();
      while ( rs.next() )
      {
        SSIContestProgressValueBean contestProgress = new SSIContestProgressValueBean();
        contestProgress.setContestId( rs.getLong( "SSI_CONTEST_ID" ) );
        contestProgress.setActivityId( rs.getLong( "SSI_CONTEST_ACTIVITY_ID" ) );
        contestProgress.setActivityDescription( rs.getString( "ACTIVITY_DESCRIPTION" ) );
        contestProgress.setGoal( rs.getDouble( "GOAL" ) );
        contestProgress.setTeamActivity( rs.getDouble( "TEAM_ACTIVITY" ) );
        contestProgress.setTogo( rs.getDouble( "TO_GO" ) );
        contestProgress.setPercentageAcheived( rs.getLong( "PERC_ACHIEVED" ) );
        contestProgress.setParticipantAchieved( rs.getInt( "PAX_ACHIEVED" ) );
        contestProgress.setTotalParticipant( rs.getInt( "TOTAL_PAX" ) );
        contestProgress.setPotentialPayout( rs.getLong( "POTENTIAL_PAYOUT" ) );
        contestProgress.setMaximumPayout( rs.getLong( "MAXIMUM_PAYOUT" ) );
        contestProgress.setPayoutCapAmount( rs.getDouble( "PAYOUT_CAP_AMOUNT" ) );
        contestProgress.setMaximumPayoutWithBonus( rs.getLong( "MAXIMUM_PAYOUT_WITH_BONUS" ) );
        contestProgress.setMinQualifier( rs.getDouble( "MIN_QUALIFIER" ) );
        contestProgress.setForEvery( rs.getDouble( "FOR_EVERY" ) );
        contestProgress.setWillEarn( rs.getLong( "WILL_EARN" ) );
        contestProgress.setTotalObjectiveAmount( rs.getDouble( "TOTAL_OBJECTIVE_AMOUNT" ) );
        contestProgress.setPayoutQuantity( rs.getLong( "PAYOUT_QUANTITY" ) );
        contestProgress.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        contestProgresses.add( contestProgress );
      }
      return contestProgresses;
    }
  }

  /**
   * SSIContestManagerResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressStackRankResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestStackRankTeamValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ArrayList<SSIContestStackRankTeamValueBean> contestProgressStackRanks = new ArrayList<SSIContestStackRankTeamValueBean>();
      while ( rs.next() )
      {
        SSIContestStackRankTeamValueBean contestProgressStackRank = new SSIContestStackRankTeamValueBean();
        contestProgressStackRank.setActivityId( rs.getLong( "SSI_CONTEST_ACTIVITY_ID" ) );
        contestProgressStackRank.setRank( rs.getInt( "STACK_RANK" ) );
        contestProgressStackRank.setParticipantId( rs.getLong( "PARTICIPANT_ID" ) );
        contestProgressStackRank.setFirstName( rs.getString( "FIRST_NAME" ) );
        contestProgressStackRank.setLastName( rs.getString( "LAST_NAME" ) );
        contestProgressStackRank.setAvatarUrl( rs.getString( "AVATAR" ) );
        contestProgressStackRank.setScore( rs.getDouble( "PAX_ACTIVITY" ) );
        contestProgressStackRank.setPayout( rs.getLong( "PAYOUT_AMOUNT" ) );
        contestProgressStackRank.setPayoutDescription( rs.getString( "PAYOUT_DESC" ) );
        contestProgressStackRank.setTeamMember( Boolean.TRUE );
        contestProgressStackRanks.add( contestProgressStackRank );
      }
      return contestProgressStackRanks;
    }
  }

  /**
   * SSIContestProgressSiuResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressSiuResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestProgressValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestProgressValueBean contestProgress = new SSIContestProgressValueBean();
      while ( rs.next() )
      {
        contestProgress.setContestGoal( rs.getDouble( "CONTEST_GOAL" ) );
        contestProgress.setBaselineType( rs.getString( "SIT_INDV_BASELINE_TYPE" ) );
        contestProgress.setActivity( rs.getDouble( "ACTIVITY" ) );
        contestProgress.setTogo( rs.getDouble( "TO_GO" ) );
        contestProgress.setTotalPayout( rs.getLong( "TOTAL_PAYOUT" ) );
        contestProgress.setPotentialPayout( rs.getLong( "POTENTIAL_PAYOUT" ) );
        contestProgress.setTotalPotentialPayout( rs.getLong( "TOTAL_POTENTIAL_PAYOUT" ) );
        contestProgress.setRemainingPayout( rs.getLong( "REMAINING_PAYOUT" ) );
        contestProgress.setPercPayout( rs.getLong( "PERC_PAYOUT" ) );
      }
      return contestProgress;
    }
  }

  /**
   * SSIContestProgressSiuResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressSiuLevelsResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIPaxContestLevelValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ArrayList<SSIPaxContestLevelValueBean> contestProgressLevels = new ArrayList<SSIPaxContestLevelValueBean>();
      while ( rs.next() )
      {
        Long sequenceNumber = rs.getLong( "SEQUENCE_NUMBER" );
        Double goalAmount = rs.getDouble( "GOAL_AMOUNT" );
        Long payout = rs.getLong( "PAYOUT_AMOUNT" );
        Integer paxCount = rs.getInt( "PAX_COUNT" );
        String payoutDescription = rs.getString( "PAYOUT_DESCRIPTION" );
        contestProgressLevels.add( new SSIPaxContestLevelValueBean( sequenceNumber, goalAmount, paxCount, payout, payoutDescription ) );
      }
      return contestProgressLevels;
    }
  }

  /**
   * SSIContestProgressSrResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressSrResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestProgressValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestProgressValueBean contestProgress = new SSIContestProgressValueBean();
      while ( rs.next() )
      {
        contestProgress.setGoal( rs.getDouble( "CONTEST_GOAL" ) );
        contestProgress.setProgress( rs.getDouble( "ACTIVITY_AMT" ) );
        contestProgress.setTogo( rs.getDouble( "TO_GO" ) );
        contestProgress.setPayoutCap( rs.getLong( "MAXIMUM_POINTS" ) );
        contestProgress.setMinQualifier( rs.getDouble( "STACK_RANK_QUALIFIER_AMOUNT" ) );
        contestProgress.setPotentialPayout( rs.getLong( "POTENTIAL_PAYOUT" ) );
        contestProgress.setParticipantAchieved( rs.getInt( "PAX_ACHIEVED" ) );
        contestProgress.setTotalParticipant( rs.getInt( "TOTAL_PAX" ) );
      }
      return contestProgress;
    }
  }

  /**
   * SSIContestProgressSrPayoutsResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressSrPayoutsResultSetExtractor implements ResultSetExtractor
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
        contestProgressPayouts.add( new SSIContestStackRankPayoutValueBean( rank, payout, payoutDescription, badgeId, badgeName, badgeUrl ) );
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
        stackRank.setTeamMember( rs.getBoolean( "IS_TEAMMEMBER" ) );
        stackRank.setPayout( rs.getLong( "PAYOUT_AMOUNT" ) );
        stackRank.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        stackRanks.add( stackRank );
      }
      return stackRanks;
    }
  }

}
