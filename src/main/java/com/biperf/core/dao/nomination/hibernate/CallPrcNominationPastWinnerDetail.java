
package com.biperf.core.dao.nomination.hibernate;

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

import com.biperf.core.utils.UserManager;
import com.biperf.core.value.pastwinners.PastWinnersBehaviorDetails;
import com.biperf.core.value.pastwinners.PastWinnersCustomDetails;
import com.biperf.core.value.pastwinners.PastWinnersNominatorDetails;
import com.biperf.core.value.pastwinners.PastWinnersNomineeDetails;
import com.biperf.core.value.pastwinners.PastWinnersTeamMembersDetail;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationPastWinnerDetail extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "pkg_list_past_win_nominations.prc_past_winner_detail";

  public CallPrcNominationPastWinnerDetail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_winner_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_team_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_logged_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_activity_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_time_period_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", OracleTypes.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_nominee_dtl_res", OracleTypes.CURSOR, new CallPrcNominationPastWinnerDetail.PastWinnersNomieeDetailsMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_team_memb_dtl_res", OracleTypes.CURSOR, new CallPrcNominationPastWinnerDetail.PastWinnersTeamMembersMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_behavior_dtl_res", OracleTypes.CURSOR, new CallPrcNominationPastWinnerDetail.PastWinnersBehaviorDetailsMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_custom_dtl_res", OracleTypes.CURSOR, new CallPrcNominationPastWinnerDetail.PastWinnersCustomDetailsMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_nominator_dtl_res", OracleTypes.CURSOR, new CallPrcNominationPastWinnerDetail.PastWinnersNominatorDetailsMapper() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_winner_user_id", parameters.get( "winnerId" ) );
    inParams.put( "p_in_team_id", parameters.get( "teamId" ) );
    inParams.put( "p_in_logged_in_user_id", parameters.get( "participantId" ) );
    inParams.put( "p_in_activity_id", parameters.get( "activityId" ) );
    inParams.put( "p_in_time_period_id", parameters.get( "timePeriodId" ) );
    inParams.put( "p_in_locale", UserManager.getLocale().toString() );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class PastWinnersNomieeDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersNomineeDetails> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersNomineeDetails> reportData = new ArrayList<PastWinnersNomineeDetails>();

      while ( rs.next() )
      {
        PastWinnersNomineeDetails reportValue = new PastWinnersNomineeDetails();

        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setClaimGroupId( rs.getLong( "CLAIM_GROUP_ID" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setDateApproved( rs.getDate( "DATE_APPROVED" ) );
        reportValue.setCurrencyLabel( rs.getString( "CURRENCY_LABEL" ) );
        reportValue.seteCardName( rs.getString( "ECARD_NAME" ) );
        reportValue.setCardVideoUrl( rs.getString( "CARD_VIDEO_URL" ) );
        reportValue.setCardVideoImageUrl( rs.getString( "CARD_VIDEO_IMAGE_URL" ) );
        reportValue.setTeamNomination( rs.getBoolean( "TEAM_NOMINATION" ) );
        reportValue.setTimePeriodName( rs.getString( "TIME_PERIOD_NAME" ) );
        reportValue.setTeamName( rs.getString( "TEAM_NAME" ) );
        reportValue.setNomineeFirstName( rs.getString( "NOMINEE_FIRST_NAME" ) );
        reportValue.setNomineeLastName( rs.getString( "NOMINEE_LAST_NAME" ) );
        reportValue.setAwardAmount( rs.getBigDecimal( "AWARD_AMOUNT" ) );
        reportValue.setNomineeHierarchyId( rs.getLong( "NOMINEE_HIERARCHY_ID" ) );
        reportValue.setNomineeOrgName( rs.getString( "NOMINEE_ORG_NAME" ) );
        reportValue.setNomineePosition( rs.getString( "NOMINEE_POSITION" ) );
        reportValue.setNomineeCountryCode( rs.getString( "NOMINEE_COUNTRY_CODE" ) );
        reportValue.setNomineeCountryName( rs.getString( "NOMINEE_COUNTRY_NAME" ) );
        reportValue.setNomineeAvatarUrl( rs.getString( "NOMINEE_AVATAR_URL" ) );
        reportValue.setCertificateId( rs.getLong( "CERTIFICATE_ID" ) == 0 ? null : rs.getLong( "CERTIFICATE_ID" ) );
        reportValue.setDateSubmitted( rs.getDate( "DATE_SUBMITTED" ) );
        reportValue.setOwnCardName( rs.getString( "OWN_CARD_NAME" ) );
        reportValue.setLevelNumber( rs.getLong( "LEVEL_NUMBER" ) );
        reportValue.setLevelName( rs.getString( "LEVEL_NAME" ) );
        reportValue.setOtherDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        reportValue.setNomineeUserId( rs.getLong( "NOMINEE_USER_ID" ) );
        reportValue.setSubmitterLangId( rs.getString( "SUBMITTER_COMMENTS_LANG_ID" ) );
        reportValue.setTeamId( rs.getLong( "TEAM_ID" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class PastWinnersTeamMembersMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersTeamMembersDetail> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersTeamMembersDetail> reportData = new ArrayList<PastWinnersTeamMembersDetail>();

      while ( rs.next() )
      {
        PastWinnersTeamMembersDetail reportValue = new PastWinnersTeamMembersDetail();

        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setFirstName( rs.getString( "NOMINEE_FIRST_NAME" ) );
        reportValue.setLastName( rs.getString( "NOMINEE_LAST_NAME" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class PastWinnersBehaviorDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersBehaviorDetails> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersBehaviorDetails> reportData = new ArrayList<PastWinnersBehaviorDetails>();

      while ( rs.next() )
      {
        PastWinnersBehaviorDetails reportValue = new PastWinnersBehaviorDetails();

        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setBehaviorName( rs.getString( "BEHAVIOR_NAME" ) );
        reportValue.setBadgeName( rs.getString( "BADGE_NAME" ) );
        reportValue.setBadgeId( rs.getLong( "BADGE_ID" ) );

        reportData.add( reportValue );
      }
      return reportData;
    }
  }

  private class PastWinnersCustomDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersCustomDetails> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersCustomDetails> reportData = new ArrayList<PastWinnersCustomDetails>();

      while ( rs.next() )
      {
        PastWinnersCustomDetails reportValue = new PastWinnersCustomDetails();

        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setClaimFormStepElementId( rs.getLong( "CLAIM_FORM_STEP_ELEMENT_ID" ) );
        reportValue.setClaimFormStepElementName( rs.getString( "CLAIM_FORM_STEP_ELEMENT_NAME" ) );
        reportValue.setClaimFormStepElementDesc( rs.getString( "CLAIM_FORM_STEP_ELEMENT_DESC" ) );
        reportValue.setClaimFormValue( rs.getString( "CLAIM_FORM_VALUE" ) );
        reportValue.setSequenceNumber( rs.getLong( "SEQUENCE_NUM" ) );
        reportValue.setWhy( rs.getBoolean( "WHY_FIELD" ) );

        reportData.add( reportValue );
      }
      return reportData;
    }
  }

  private class PastWinnersNominatorDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersNominatorDetails> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersNominatorDetails> reportData = new ArrayList<PastWinnersNominatorDetails>();

      while ( rs.next() )
      {
        PastWinnersNominatorDetails reportValue = new PastWinnersNominatorDetails();

        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setClaimGroupId( rs.getLong( "CLAIM_GROUP_ID" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setDateApproved( rs.getDate( "DATE_APPROVED" ) );
        reportValue.setCurrencyLabel( rs.getString( "CURRENCY_LABEL" ) );
        reportValue.seteCardName( rs.getString( "ECARD_NAME" ) );
        reportValue.setCardVideoUrl( rs.getString( "CARD_VIDEO_URL" ) );
        reportValue.setCardVideoImageUrl( rs.getString( "CARD_VIDEO_IMAGE_URL" ) );
        reportValue.setTeamNomination( rs.getBoolean( "TEAM_NOMINATION" ) );
        reportValue.setTimePeriodName( rs.getString( "TIME_PERIOD_NAME" ) );
        reportValue.setTeamName( rs.getString( "TEAM_NAME" ) );
        reportValue.setAwardAmount( rs.getBigDecimal( "AWARD_AMOUNT" ) );
        reportValue.setNominatorUserId( rs.getLong( "NOMINATOR_USER_ID" ) );
        reportValue.setNominatorFirstName( rs.getString( "NOMINATOR_FIRST_NAME" ) );
        reportValue.setNominatorLastName( rs.getString( "NOMINATOR_LAST_NAME" ) );
        reportValue.setNominatorHierarchyId( rs.getLong( "NOMINATOR_HIERARCHY_ID" ) );
        reportValue.setNominatorOrgName( rs.getString( "NOMINATOR_ORG_NAME" ) );
        reportValue.setNominatorPosition( rs.getString( "NOMINATOR_JOB_POSITION" ) );
        reportValue.setNominatorCountryCode( rs.getString( "NOMINATOR_COUNTRY_CODE" ) );
        reportValue.setNominatorCountryName( rs.getString( "NOMINATOR_COUNTRY_NAME" ) );
        reportValue.setNominatorAvatarUrl( rs.getString( "NOMINATOR_AVATAR_URL" ) );
        reportValue.setSubmitterComments( rs.getString( "SUBMITTER_COMMENTS" ) );
        reportValue.setSubmitterLangId( rs.getString( "SUBMITTER_COMMENTS_LANG_ID" ) );
        reportValue.setDateSubmitted( rs.getDate( "DATE_SUBMITTED" ) );
        reportValue.setCertificateId( rs.getLong( "CERTIFICATE_ID" ) == 0 ? null : rs.getLong( "CERTIFICATE_ID" ) );
        reportValue.setOwnCardName( rs.getString( "OWN_CARD_NAME" ) );
        reportValue.setNominatorDprtName( rs.getString( "NOMINATOR_DEPARTMENT" ) );
        reportValue.setLevelNumber( rs.getLong( "LEVEL_NUMBER" ) );
        reportValue.setLevelName( rs.getString( "LEVEL_NAME" ) );
        reportValue.setPayOutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );

        reportData.add( reportValue );
      }
      return reportData;
    }
  }
}
