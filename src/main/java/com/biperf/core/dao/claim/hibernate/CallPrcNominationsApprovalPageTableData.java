/**
 * 
 */

package com.biperf.core.dao.claim.hibernate;

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

import com.biperf.core.value.NominationsApprovalAwardDetailsValueBean;
import com.biperf.core.value.NominationsApprovalBehaviorsValueBean;
import com.biperf.core.value.NominationsApprovalBoxValueBean;
import com.biperf.core.value.NominationsApprovalCustomValueBean;
import com.biperf.core.value.NominationsApprovalDetailsValueBean;
import com.biperf.core.value.NominationsApprovalPageNextLevelApproversValueBean;
import com.biperf.core.value.NominationsApprovalPagePreviousLevelApproversValueBean;
import com.biperf.core.value.NominationsApprovalTeamMembersValueBean;
import com.biperf.core.value.NominationsApprovalTimePeriodsValueBean;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcNominationsApprovalPageTableData extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_LIST_PEND_NOMINATIONS.PRC_LIST_APPROVER_DETAIL_PAGE";

  public CallPrcNominationsApprovalPageTableData( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_level_number", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_approver_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_time_period_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_submit_start_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_submit_end_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_total_nomi_count", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_prev_lvl_approvers", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper6() ) );
    declareParameter( new SqlOutParameter( "p_out_next_lvl_approvers", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper8() ) );
    declareParameter( new SqlOutParameter( "p_out_nomin_detail", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper1() ) );
    declareParameter( new SqlOutParameter( "p_out_team_members_dtl", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper2() ) );
    declareParameter( new SqlOutParameter( "p_out_behaviors_dtl", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper3() ) );
    declareParameter( new SqlOutParameter( "p_out_custom_dtl", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper4() ) );
    declareParameter( new SqlOutParameter( "p_out_award_amount_dtl", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper5() ) );
    declareParameter( new SqlOutParameter( "p_out_max_win_claim_dtl", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper7() ) );
    declareParameter( new SqlOutParameter( "p_out_dtl", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.PendingNominationClaimsMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_cummula_nomi_dtl", OracleTypes.CURSOR, new CallPrcNominationsApprovalPageTableData.CumulativeNominationClaimsMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotion_id", parameters.get( "promotionId" ) );
    inParams.put( "p_in_level_number", parameters.get( "levelNumber" ) );
    inParams.put( "p_in_approver_id", parameters.get( "approverUserId" ) );
    inParams.put( "p_in_time_period_id", parameters.get( "timePeriodId" ) );
    inParams.put( "p_in_submit_start_date", parameters.get( "startDate" ) );
    inParams.put( "p_in_submit_end_date", parameters.get( "endDate" ) );
    inParams.put( "p_in_status", parameters.get( "status" ) );
    inParams.put( "p_in_locale", parameters.get( "locale" ) );
    inParams.put( "p_in_rowNumStart", parameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", parameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortedOn", parameters.get( "sortedOn" ) );
    inParams.put( "p_in_sortedBy", parameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class PendingNominationClaimsMapper1 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalDetailsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalDetailsValueBean> valueBeanData = new ArrayList<NominationsApprovalDetailsValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalDetailsValueBean valueBean = new NominationsApprovalDetailsValueBean();

        valueBean.setClaimId( rs.getString( "claim_id" ) );
        valueBean.setStatus( rs.getString( "status" ) );
        valueBean.setTeamId( rs.getLong( "team_id" ) );
        valueBean.setPaxId( rs.getLong( "nominee_pax_id" ) );
        valueBean.setNomineeName( rs.getString( "nominee_name" ) );
        valueBean.setOrgName( rs.getString( "org_name" ) );
        valueBean.setJobPositionName( rs.getString( "job_position_name" ) );
        valueBean.setCountryName( rs.getString( "country_name" ) );
        valueBean.setWonFlag( rs.getBoolean( "won_flag" ) );
        valueBean.setNumberOfTimesWon( rs.getInt( "no_of_times_won" ) );
        valueBean.setExceedFlag( rs.getBoolean( "exceed_flag" ) );
        valueBean.setSubmittedDate( rs.getDate( "submitted_date" ) );
        valueBean.setNominatorPaxId( rs.getLong( "nominator_pax_id" ) );
        valueBean.setNominatorName( rs.getString( "nominator_name" ) );
        valueBean.setLevelIndex( rs.getInt( "level_index" ) );
        valueBean.setAvatarUrl( rs.getString( "avator_url" ) );
        valueBean.setTeamCount( rs.getInt( "team_cnt" ) );
        valueBean.setTeam( rs.getBoolean( "is_team" ) );
        valueBean.setAwardAmount( rs.getBigDecimal( "award_amount" ) );
        valueBean.setEcardImage( rs.getString( "ecard_image" ) );
        valueBean.setEcardUrl( rs.getString( "own_card_name" ) );
        valueBean.setCardVideoUrl( rs.getString( "card_video_url" ) );
        valueBean.setCardImageUrl( rs.getString( "card_video_image_url" ) );
        valueBean.setSubmitterCommentsLangId( rs.getString( "submitter_comments_lang_id" ) );
        valueBean.setSubmitterComments( rs.getString( "submitter_comments" ) );
        valueBean.setMoreInfoComments( rs.getString( "more_info_comments" ) );
        valueBean.setWhyAttachmentName( rs.getString( "why_attachment_name" ) );
        valueBean.setWhyAttachmentUrl( rs.getString( "why_attachment_url" ) );
        valueBean.setCertificateId( rs.getLong( "certificate_id" ) );
        valueBean.setRecentTimePeriodWon( rs.getString( "recent_time_period_won" ) );
        valueBean.setMostRecentTimeDate( rs.getDate( "most_recent_date_won" ) );
        valueBean.setDepartmentName( rs.getString( "department_name" ) );
        valueBean.setDenialReason( rs.getString( "denial_reason" ) );
        valueBean.setWinnerMessage( rs.getString( "winner_message" ) );
        valueBean.setMoreInfoMessage( rs.getString( "moreinfo_message" ) );
        valueBean.setNotificationDate( rs.getDate( "notification_date" ) );
        valueBean.setLevelName( rs.getString( "level_name" ) );
        valueBean.setOptOutAwards( rs.getBoolean( "nominee_is_opt_out_of_awards" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class CumulativeNominationClaimsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalDetailsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalDetailsValueBean> valueBeanData = new ArrayList<NominationsApprovalDetailsValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalDetailsValueBean valueBean = new NominationsApprovalDetailsValueBean();

        valueBean.setClaimId( rs.getString( "claim_id" ) );
        valueBean.setStatus( rs.getString( "status" ) );
        valueBean.setPaxId( rs.getLong( "nominee_pax_id" ) );
        valueBean.setNomineeName( rs.getString( "nominee_name" ) );
        valueBean.setOrgName( rs.getString( "org_name" ) );
        valueBean.setJobPositionName( rs.getString( "job_position_name" ) );
        valueBean.setCountryName( rs.getString( "country_name" ) );
        valueBean.setWonFlag( rs.getBoolean( "won_flag" ) );
        valueBean.setNumberOfTimesWon( rs.getInt( "no_of_times_won" ) );
        valueBean.setExceedFlag( rs.getBoolean( "exceed_flag" ) );
        valueBean.setLevelIndex( rs.getInt( "level_index" ) );
        valueBean.setAvatarUrl( rs.getString( "avator_url" ) );
        valueBean.setAwardAmount( rs.getBigDecimal( "award_amount" ) );
        valueBean.setRecentTimePeriodWon( rs.getString( "recent_time_period_won" ) );
        valueBean.setMostRecentTimeDate( rs.getDate( "most_recent_date_won" ) );
        valueBean.setDepartmentName( rs.getString( "department_name" ) );
        valueBean.setNominatorCount( rs.getInt( "nominator_count" ) );
        valueBean.setDenialReason( rs.getString( "denial_reason" ) );
        valueBean.setWinnerMessage( rs.getString( "winner_message" ) );
        valueBean.setMoreInfoMessage( rs.getString( "moreinfo_message" ) );
        valueBean.setNotificationDate( rs.getDate( "notification_date" ) );
        valueBean.setLevelName( rs.getString( "level_name" ) );
        valueBean.setClaimGroupId( rs.getLong( "claim_group_id" ) );
        valueBean.setOptOutAwards( rs.getBoolean( "nominee_is_opt_out_of_awards" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper2 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalTeamMembersValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalTeamMembersValueBean> valueBeanData = new ArrayList<NominationsApprovalTeamMembersValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalTeamMembersValueBean valueBean = new NominationsApprovalTeamMembersValueBean();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setUserId( rs.getLong( "user_id" ) );
        valueBean.setUserName( rs.getString( "user_name" ) );
        valueBean.setCountryName( rs.getString( "country_name" ) );
        valueBean.setOrgName( rs.getString( "org_name" ) );
        valueBean.setJobPositionName( rs.getString( "job_position_name" ) );
        valueBean.setDepartmentName( rs.getString( "department_type" ) );
        valueBean.setAwardAmount( rs.getBigDecimal( "award_amount" ) );
        valueBean.setOptOutAwards( rs.getBoolean( "nominee_is_opt_out_of_awards" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper3 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalBehaviorsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalBehaviorsValueBean> valueBeanData = new ArrayList<NominationsApprovalBehaviorsValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalBehaviorsValueBean valueBean = new NominationsApprovalBehaviorsValueBean();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setBehaviorName( rs.getString( "behavior_name" ) );
        valueBean.setBadgeName( rs.getString( "badge_name" ) );
        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper4 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalCustomValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalCustomValueBean> valueBeanData = new ArrayList<NominationsApprovalCustomValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalCustomValueBean valueBean = new NominationsApprovalCustomValueBean();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setNomineePaxId( rs.getLong( "nominee_pax_id" ) );
        valueBean.setClaimFormStepElementId( rs.getLong( "claim_form_step_element_id" ) );
        valueBean.setClaimFormStepElementName( rs.getString( "claim_form_step_element_name" ) );
        valueBean.setDescription( rs.getString( "claim_form_step_element_desc" ) );
        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper5 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalAwardDetailsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalAwardDetailsValueBean> valueBeanData = new ArrayList<NominationsApprovalAwardDetailsValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalAwardDetailsValueBean valueBean = new NominationsApprovalAwardDetailsValueBean();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setLevelIndex( rs.getInt( "level_index" ) );
        valueBean.setAwardAmountTypeFixed( rs.getBoolean( "award_amount_type_fixed" ) );
        valueBean.setAwardAmountMin( rs.getBigDecimal( "award_amount_min" ) );
        valueBean.setAwardAmountMax( rs.getBigDecimal( "award_amount_max" ) );
        valueBean.setCalculatorId( rs.getLong( "calcualtor_id" ) );
        valueBean.setAwardAmountTypeRange( rs.getBoolean( "award_amount_type_range" ) );
        valueBean.setAwardAmountTypeNone( rs.getBoolean( "award_amount_type_none" ) );
        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper6 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalPagePreviousLevelApproversValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalPagePreviousLevelApproversValueBean> valueBeanData = new ArrayList<NominationsApprovalPagePreviousLevelApproversValueBean>();

      while ( rs.next() )
      {
        NominationsApprovalPagePreviousLevelApproversValueBean valueBean = new NominationsApprovalPagePreviousLevelApproversValueBean();

        valueBean.setLastName( rs.getString( "last_name" ) );
        valueBean.setFirstName( rs.getString( "first_name" ) );
        valueBean.setUserId( rs.getLong( "approver_id" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper7 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalTimePeriodsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalTimePeriodsValueBean> valueBeanData = new ArrayList<NominationsApprovalTimePeriodsValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalTimePeriodsValueBean valueBean = new NominationsApprovalTimePeriodsValueBean();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setTimePeriodId( rs.getLong( "nomination_time_period_id" ) );
        valueBean.setTimePeriodName( rs.getString( "time_period_name" ) );
        valueBean.setMaxWinsllowed( rs.getLong( "max_wins_allowed" ) );
        valueBean.setNoOfWinnners( rs.getLong( "no_of_winners" ) );
        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper8 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalPageNextLevelApproversValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalPageNextLevelApproversValueBean> valueBeanData = new ArrayList<NominationsApprovalPageNextLevelApproversValueBean>();

      while ( rs.next() )
      {
        NominationsApprovalPageNextLevelApproversValueBean valueBean = new NominationsApprovalPageNextLevelApproversValueBean();

        valueBean.setLastName( rs.getString( "last_name" ) );
        valueBean.setFirstName( rs.getString( "first_name" ) );
        valueBean.setApproverUserId( rs.getLong( "approver_id" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class PendingNominationClaimsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalBoxValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalBoxValueBean> valueBeanData = new ArrayList<NominationsApprovalBoxValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalBoxValueBean valueBean = new NominationsApprovalBoxValueBean();

        valueBean.setBudgetBalance( rs.getBigDecimal( "budget_balance" ) );
        if ( rs.getBigDecimal( "budget_exceeded" ).intValue() == 0 )
        {
          valueBean.setPotentialBudgetExceeded( false );
        }
        else
        {
          valueBean.setPotentialBudgetExceeded( true );
        }
        valueBean.setCumulativeNomination( rs.getBoolean( "is_cumulative_nomination" ) );
        valueBean.setLastBudgetRequestDate( rs.getDate( "last_budget_request_date" ) );
        valueBean.setOtherAwardQuantity( rs.getString( "other_payout_quantity" ) );
        valueBean.setPayoutDescription( rs.getString( "payout_description_asset_code" ) );
        valueBean.setBudgetPeriodName( rs.getString( "budget_period_name" ) );
        valueBean.setAwardPayoutType( rs.getString( "award_payout_type" ) );
        valueBean.setPreviousLevelName( rs.getString( "previous_level_name" ) );
        valueBean.setNextLevelName( rs.getString( "next_level_name" ) );
        valueBean.setPendingNominationsCount( rs.getInt( "total_promotion_count" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

}
