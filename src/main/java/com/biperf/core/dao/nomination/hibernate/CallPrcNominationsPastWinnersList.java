
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
import com.biperf.core.value.pastwinners.PastWinnersActivityList;
import com.biperf.core.value.pastwinners.PastWinnersCustomDetails;
import com.biperf.core.value.pastwinners.PastWinnersNominatorSummary;
import com.biperf.core.value.pastwinners.PastWinnersNomineeSummary;
import com.biperf.core.value.pastwinners.PastWinnersTeamMembersSummary;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationsPastWinnersList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "pkg_list_past_win_nominations.prc_past_winner_summary";

  public CallPrcNominationsPastWinnersList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_logged_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_lastname", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_firstname", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_teamname", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_startdate", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_enddate", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", OracleTypes.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_nominee_dtl_res", OracleTypes.CURSOR, new CallPrcNominationsPastWinnersList.PastWinnersDetailsMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_team_memb_res", OracleTypes.CURSOR, new CallPrcNominationsPastWinnersList.PastWinnersTeamMembersMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_nominator_res", OracleTypes.CURSOR, new CallPrcNominationsPastWinnersList.PastWinnersNominatorDetailsMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_custom_dtl_res", OracleTypes.CURSOR, new CallPrcNominationsPastWinnersList.PastWinnersCustomDetailsMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_activity_list", OracleTypes.CURSOR, new CallPrcNominationsPastWinnersList.PastWinnersActivityListMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_logged_in_user_id", parameters.get( "participantId" ) );
    inParams.put( "p_in_promotion_id", parameters.get( "promotionId" ) );
    inParams.put( "p_in_lastname", parameters.get( "lastName" ) );
    inParams.put( "p_in_firstname", parameters.get( "firstName" ) );
    inParams.put( "p_in_country", parameters.get( "countryId" ) );
    inParams.put( "p_in_department", parameters.get( "department" ) );
    inParams.put( "p_in_teamname", parameters.get( "teamName" ) );
    inParams.put( "p_in_startdate", parameters.get( "startDate" ) );
    inParams.put( "p_in_enddate", parameters.get( "endDate" ) );
    inParams.put( "p_in_locale", UserManager.getLocale().toString() );
    inParams.put( "p_in_rowNumStart", parameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", parameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortedBy", parameters.get( "sortedBy" ) );
    inParams.put( "p_in_sortedOn", parameters.get( "sortedOn" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class PastWinnersDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersNomineeSummary> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersNomineeSummary> reportData = new ArrayList<PastWinnersNomineeSummary>();

      while ( rs.next() )
      {
        PastWinnersNomineeSummary reportValue = new PastWinnersNomineeSummary();

        reportValue.setActivityId( rs.getLong( "ACTIVITY_ID" ) == 0 ? null : rs.getLong( "ACTIVITY_ID" ) );
        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setTeamNomination( rs.getBoolean( "TEAM_NOMINATION" ) );
        reportValue.setFirstName( rs.getString( "NOMINEE_FIRST_NAME" ) );
        reportValue.setLastName( rs.getString( "NOMINEE_LAST_NAME" ) );
        reportValue.setUserId( rs.getLong( "NOMINEE_USER_ID" ) );
        reportValue.setTimePeriodId( rs.getLong( "TIME_PERIOD_ID" ) == 0 ? null : rs.getLong( "TIME_PERIOD_ID" ) );
        reportValue.setTimePeriodName( rs.getString( "TIME_PERIOD_NAME" ) );
        reportValue.setHierarchyId( rs.getLong( "NOMINEE_HIERARCHY_ID" ) );
        reportValue.setHierarchyName( rs.getString( "NOMINEE_HIERARCHY_NAME" ) );
        reportValue.setPosition( rs.getString( "NOMINEE_POSITION" ) );
        reportValue.setCountryCode( rs.getString( "NOMINEE_COUNTRY_CODE" ) );
        reportValue.setCountryName( rs.getString( "NOMINEE_COUNTRY_NAME" ) );
        reportValue.setAvatar( rs.getString( "NOMINEE_AVATAR" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setTeamId( rs.getLong( "TEAM_ID" ) == 0 ? null : rs.getLong( "TEAM_ID" ) );
        reportValue.setTeamName( rs.getString( "TEAM_NAME" ) );
        reportValue.setLevelName( rs.getString( "LEVEL_NAME" ) );
        reportValue.setLevelId( rs.getLong( "LEVEL_ID" ) );
        reportValue.setPromotionId( rs.getLong( "PROMOTION_ID" ) );
        reportValue.setDepartmentName( rs.getString( "NOMINEE_DEPARTMENT" ) );
        reportValue.setClaimGroupId( rs.getLong( "claim_group_id" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class PastWinnersTeamMembersMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersTeamMembersSummary> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersTeamMembersSummary> reportData = new ArrayList<PastWinnersTeamMembersSummary>();

      while ( rs.next() )
      {
        PastWinnersTeamMembersSummary reportValue = new PastWinnersTeamMembersSummary();

        reportValue.setActivityId( rs.getLong( "ACTIVITY_ID" ) );
        reportValue.setMemberId( rs.getString( "NOMINEE_USER_ID" ) );
        reportValue.setFirstName( rs.getString( "NOMINEE_FIRST_NAME" ) );
        reportValue.setLastName( rs.getString( "NOMINEE_LAST_NAME" ) );
        reportValue.setTeamId( rs.getLong( "team_id" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class PastWinnersNominatorDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersNominatorSummary> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersNominatorSummary> reportData = new ArrayList<PastWinnersNominatorSummary>();

      while ( rs.next() )
      {
        PastWinnersNominatorSummary reportValue = new PastWinnersNominatorSummary();

        reportValue.setActivityId( rs.getLong( "ACTIVITY_ID" ) );
        reportValue.setFirstName( rs.getString( "NOMINATOR_FIRST_NAME" ) );
        reportValue.setLastName( rs.getString( "NOMINATOR_LAST_NAME" ) );
        reportValue.setUserId( rs.getLong( "NOMINATOR_USER_ID" ) );
        reportValue.setHierarchyId( rs.getLong( "NOMINATOR_HIERARCHY_ID" ) );
        reportValue.setOrganisationName( rs.getString( "NOMINATOR_ORG_NAME" ) );
        reportValue.setPosition( rs.getString( "NOMINATOR_JOB_POSITION" ) );
        reportValue.setCountryCode( rs.getString( "NOMINATOR_COUNTRY_CODE" ) );
        reportValue.setCountryName( rs.getString( "NOMINATOR_COUNTRY_NAME" ) );
        reportValue.setCommentText( rs.getString( "COMMENTS" ) );
        reportValue.setDepartmentName( rs.getString( "NOMINATOR_DEPARTMENT" ) );
        reportValue.setTimePeriodId( rs.getLong( "TIME_PERIOD_ID" ) == 0 ? null : rs.getLong( "TIME_PERIOD_ID" ) );
        reportValue.setTimePeriodName( rs.getString( "TIME_PERIOD_NAME" ) );
        reportValue.setClaimGroupId( rs.getLong( "claim_group_id" ) );
        reportValue.setTeamId( rs.getLong( "TEAM_ID" ) );
        reportValue.setTeamName( rs.getString( "TEAM_NAME" ) );

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
        reportData.add( reportValue );
      }
      return reportData;
    }
  }

  private class PastWinnersActivityListMapper implements ResultSetExtractor
  {
    @Override
    public List<PastWinnersActivityList> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PastWinnersActivityList> reportData = new ArrayList<PastWinnersActivityList>();

      while ( rs.next() )
      {
        PastWinnersActivityList reportValue = new PastWinnersActivityList();

        reportValue.setActivityId( rs.getLong( "activity_id" ) );
        reportValue.setTimePeriodId( rs.getLong( "time_period_id" ) );
        reportValue.setClaimGroupId( rs.getLong( "claim_group_id" ) );
        reportValue.setTeamId( rs.getLong( "TEAM_ID" ) );
        reportValue.setTeamName( rs.getString( "TEAM_NAME" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }
  }
}
