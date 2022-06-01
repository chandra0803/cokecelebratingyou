
package com.biperf.core.dao.reports.hibernate;

import java.math.BigDecimal;
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

import com.biperf.core.value.claim.ClaimByOrgReportValue;
import com.biperf.core.value.claim.ClaimByPaxReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcClaimsReport extends StoredProcedure
{
  private static final String ORG_TAB_RES = "pkg_query_claims_activity.prc_getByOrgTabRes";
  private static final String ORG_STATUS = "pkg_query_claims_activity.prc_getByOrgStatus";
  private static final String ORG_MONTHLY = "pkg_query_claims_activity.prc_getByOrgMonthly";
  private static final String ORG_PART_RATE = "pkg_query_claims_activity.prc_getByOrgPartRate";
  private static final String ORG_PART_LEVEL = "pkg_query_claims_activity.prc_getByOrgPartLevel";
  private static final String ORG_ITEM_STATUS = "pkg_query_claims_activity.prc_getByOrgItemStatus";
  private static final String ORG_TOTALS = "pkg_query_claims_activity.prc_getByOrgTotals";
  private static final String PAX_TAB_RES = "pkg_query_claims_activity.prc_getByPaxTabRes";
  private static final String PAX_CLAIM_LIST_TAB_RES = "pkg_query_claims_activity.prc_getByPaxClaimListTabRes";
  private static final String PAX_SUBMITTED_CLAIMS = "pkg_query_claims_activity.prc_getByPaxSubmittedClaims";
  private static final String PAX_CLAIM_STATUS = "pkg_query_claims_activity.prc_getByPaxClaimStatus";
  private static final String PAX_ITEM_STATUS = "pkg_query_claims_activity.prc_getByPaxItemStatus";

  public CallPrcClaimsReport( DataSource ds, String storedProcName )
  {
    super( ds, storedProcName );
    declareParameter( new SqlParameter( "p_in_paxId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_claimStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_includeChildNodes", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( storedProcName )
    {
      case ORG_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByOrgReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ClaimByOrgReportTotalsMapper() ) );
        break;
      case ORG_STATUS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByOrgSubmissionStatusReportMapper() ) );
        break;
      case ORG_MONTHLY:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByOrgMonthlyReportMapper() ) );
        break;
      case ORG_PART_RATE:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByOrgParticipationRateReportMapper() ) );
        break;
      case ORG_PART_LEVEL:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByOrgParticipationLevelReportMapper() ) );
        break;
      case ORG_ITEM_STATUS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByOrgItemStatusReportMapper() ) );
        break;
      case ORG_TOTALS:
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ClaimByOrgTotalsReportMapper() ) );
        break;
      case PAX_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByPaxReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ClaimByPaxTotalsReportMapper() ) );
        break;
      case PAX_CLAIM_LIST_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByPaxClaimListReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ClaimByPaxClaimListTotalsReportMapper() ) );
        break;
      case PAX_SUBMITTED_CLAIMS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByPaxSubmittedClaimsReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_average_data", OracleTypes.CURSOR, new PaxAvgClaimsSubmittedReportMapper() ) ); // size
                                                                                                                                      // mapper?
        break;
      case PAX_CLAIM_STATUS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByPaxClaimStatusReportMapper() ) );
        break;
      case PAX_ITEM_STATUS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ClaimByPaxItemStatusReportMapper() ) );
        break;
      default:
        break;
    }
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters ) throws DataAccessException
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_paxId", reportParameters.get( "paxId" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_claimStatus", reportParameters.get( "claimStatus" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_includeChildNodes", reportParameters.get( "includeChildNodes" ) );
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  // MAPPERS//

  protected class ClaimByPaxTotalsReportMapper implements ResultSetExtractor<ClaimByPaxReportValue>
  {
    @Override
    public ClaimByPaxReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ClaimByPaxReportValue result = new ClaimByPaxReportValue();

      while ( rs.next() )
      {
        result.setTotalClaims( rs.getLong( "total_claims" ) );
        result.setOpenClaims( rs.getLong( "open_claims" ) );
        result.setClosedClaims( rs.getLong( "closed_claims" ) );
        result.setApprovedItems( rs.getLong( "approved_items" ) );
        result.setDeniedItems( rs.getLong( "denied_items" ) );
        result.setHeldItems( rs.getLong( "held_items" ) );
        result.setPoints( rs.getLong( "points" ) );
        result.setSweepstakesWon( rs.getLong( "sweepstakes_won" ) );
      }
      return result;
    }
  }

  protected class ClaimByOrgReportTotalsMapper implements ResultSetExtractor<ClaimByOrgReportValue>
  {
    @Override
    public ClaimByOrgReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ClaimByOrgReportValue result = new ClaimByOrgReportValue();
      if ( rs.next() )
      {
        result.setEligSubmitters( rs.getLong( "elig_sub" ) );
        result.setActualSubmitters( rs.getLong( "act_sub" ) );
        result.setParticipationRate( rs.getBigDecimal( "PARTICIPATION_RATE" ) );
        result.setTotalClaims( rs.getLong( "TOTAL_CLAIMS" ) );
        result.setOpenClaims( rs.getLong( "CLAIMS_OPEN" ) );
        result.setClosedClaims( rs.getLong( "CLAIMS_CLOSED" ) );
        result.setApprovedItems( rs.getLong( "ITEMS_APPROVED" ) );
        result.setDeniedItems( rs.getLong( "ITEMS_DENIED" ) );
        result.setHoldItems( rs.getLong( "ITEMS_HOLD" ) );
        result.setPoints( rs.getLong( "POINTS" ) );
        result.setSweepstakesWon( rs.getLong( "SWEEPSTAKES_WON" ) );
      }
      return result;
    }
  }

  protected class ClaimByOrgReportMapper implements ResultSetExtractor<List<ClaimByOrgReportValue>>
  {
    @Override
    public List<ClaimByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByOrgReportValue> results = new ArrayList<ClaimByOrgReportValue>();
      while ( rs.next() )
      {
        ClaimByOrgReportValue result = new ClaimByOrgReportValue();
        result.setOrgId( rs.getLong( "ORG_ID" ) );
        result.setOrgName( rs.getString( "ORG_NAME" ) );
        result.setEligSubmitters( rs.getLong( "elig_sub" ) );
        result.setActualSubmitters( rs.getLong( "act_sub" ) );
        result.setParticipationRate( rs.getBigDecimal( "PARTICIPATION_RATE" ) );
        result.setTotalClaims( rs.getLong( "TOTAL_CLAIMS" ) );
        result.setOpenClaims( rs.getLong( "CLAIMS_OPEN" ) );
        result.setClosedClaims( rs.getLong( "CLAIMS_CLOSED" ) );
        result.setApprovedItems( rs.getLong( "ITEMS_APPROVED" ) );
        result.setDeniedItems( rs.getLong( "ITEMS_DENIED" ) );
        result.setHoldItems( rs.getLong( "ITEMS_HOLD" ) );
        result.setPoints( rs.getLong( "POINTS" ) );
        result.setSweepstakesWon( rs.getLong( "SWEEPSTAKES_WON" ) );
        result.setIsLeaf( rs.getBoolean( "is_leaf" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByOrgSubmissionStatusReportMapper implements ResultSetExtractor<List<ClaimByOrgReportValue>>
  {
    @Override
    public List<ClaimByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByOrgReportValue> results = new ArrayList<ClaimByOrgReportValue>();
      while ( rs.next() )
      {
        ClaimByOrgReportValue result = new ClaimByOrgReportValue();
        result.setOrgName( rs.getString( "ORG_NAME" ) );
        result.setOpenStatusCount( rs.getLong( "CLAIMS_OPEN" ) );
        result.setClosedStatusCount( rs.getLong( "CLAIMS_CLOSED" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByOrgMonthlyReportMapper implements ResultSetExtractor<List<ClaimByOrgReportValue>>
  {
    @Override
    public List<ClaimByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByOrgReportValue> results = new ArrayList<ClaimByOrgReportValue>();
      while ( rs.next() )
      {
        ClaimByOrgReportValue result = new ClaimByOrgReportValue();
        result.setMonthName( rs.getString( "MONTH_NAME" ) );
        result.setClaimCount( rs.getLong( "CLAIM_COUNT" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByOrgParticipationRateReportMapper implements ResultSetExtractor<List<ClaimByOrgReportValue>>
  {
    @Override
    public List<ClaimByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByOrgReportValue> results = new ArrayList<ClaimByOrgReportValue>();
      while ( rs.next() )
      {
        ClaimByOrgReportValue result = new ClaimByOrgReportValue();
        result.setSubmitterCount( rs.getLong( "SUBMITTERS" ) );
        result.setNonSubmitterCount( rs.getLong( "NON_SUBMITTERS" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByOrgParticipationLevelReportMapper implements ResultSetExtractor<List<ClaimByOrgReportValue>>
  {
    @Override
    public List<ClaimByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByOrgReportValue> results = new ArrayList<ClaimByOrgReportValue>();
      while ( rs.next() )
      {
        ClaimByOrgReportValue result = new ClaimByOrgReportValue();
        result.setSubmitterCount( rs.getLong( "SUBMITTERS" ) );
        result.setNonSubmitterCount( rs.getLong( "NON_SUBMITTERS" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByOrgItemStatusReportMapper implements ResultSetExtractor<List<ClaimByOrgReportValue>>
  {
    @Override
    public List<ClaimByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByOrgReportValue> results = new ArrayList<ClaimByOrgReportValue>();
      while ( rs.next() )
      {
        ClaimByOrgReportValue result = new ClaimByOrgReportValue();
        result.setOrgName( rs.getString( "ORG_NAME" ) );
        result.setItemsApproved( rs.getLong( "ITEMS_APPROVED" ) );
        result.setItemsDenied( rs.getLong( "ITEMS_DENIED" ) );
        result.setItemsHeld( rs.getLong( "ITEMS_HOLD" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByOrgTotalsReportMapper implements ResultSetExtractor<List<ClaimByOrgReportValue>>
  {
    @Override
    public List<ClaimByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByOrgReportValue> results = new ArrayList<ClaimByOrgReportValue>();
      while ( rs.next() )
      {
        ClaimByOrgReportValue result = new ClaimByOrgReportValue();
        result.setOrgName( rs.getString( "ORG_NAME" ) );
        result.setClaimsPerSubmitter( rs.getBigDecimal( "CLAIMS_PER_SUBMITTER" ) );
        result.setItemsPerSubmitter( rs.getBigDecimal( "ITEMS_PER_SUBMITTER" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByPaxReportMapper implements ResultSetExtractor<List<ClaimByPaxReportValue>>
  {
    @Override
    public List<ClaimByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByPaxReportValue> results = new ArrayList<ClaimByPaxReportValue>();
      while ( rs.next() )
      {
        ClaimByPaxReportValue result = new ClaimByPaxReportValue();
        result.setPaxId( rs.getLong( "submitter_id" ) );
        result.setPaxName( rs.getString( "submitter" ) );
        result.setCountry( rs.getString( "country" ) );
        result.setOrgName( rs.getString( "org_name" ) );
        result.setDepartment( rs.getString( "DEPARTMENT" ) );
        result.setJobPosition( rs.getString( "JOB_POSITION" ) );
        result.setPromotionName( rs.getString( "promotion" ) );
        result.setTotalClaims( rs.getLong( "total_claims" ) );
        result.setOpenClaims( rs.getLong( "open_claims" ) );
        result.setClosedClaims( rs.getLong( "closed_claims" ) );
        result.setApprovedItems( rs.getLong( "approved_items" ) );
        result.setDeniedItems( rs.getLong( "denied_items" ) );
        result.setHeldItems( rs.getLong( "held_items" ) );
        result.setPoints( rs.getLong( "points" ) );
        result.setSweepstakesWon( rs.getLong( "sweepstakes_won" ) );
        result.setPromotionId( rs.getLong( "promotion_id" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByPaxClaimListReportMapper implements ResultSetExtractor<List<ClaimByPaxReportValue>>
  {
    @Override
    public List<ClaimByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByPaxReportValue> results = new ArrayList<ClaimByPaxReportValue>();
      while ( rs.next() )
      {
        ClaimByPaxReportValue result = new ClaimByPaxReportValue();
        result.setClaimNumber( rs.getString( "CLAIM" ) );
        result.setClaimStatus( rs.getString( "CLAIM_STATUS" ) );
        result.setDateSubmitted( rs.getDate( "DATE_SUBMITTED" ) );
        result.setOrgName( rs.getString( "ORG_NAME" ) );
        result.setPromotionName( rs.getString( "PROMOTION" ) );
        result.setPoints( rs.getLong( "POINTS" ) );
        result.setSweepstakesWon( rs.getLong( "SWEEPSTAKES_WON" ) );
        result.setSubmitterUserId( rs.getLong( "SUBMITTER_USER_ID" ) );
        result.setClaimId( rs.getLong( "CLAIM_ID" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByPaxClaimListTotalsReportMapper implements ResultSetExtractor<ClaimByPaxReportValue>
  {
    @Override
    public ClaimByPaxReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ClaimByPaxReportValue result = new ClaimByPaxReportValue();
      if ( rs.next() )
      {
        result.setPoints( rs.getLong( "POINTS" ) );
        result.setSweepstakesWon( rs.getLong( "SWEEPSTAKES_WON" ) );
      }
      return result;
    }
  }

  protected class ClaimByPaxSubmittedClaimsReportMapper implements ResultSetExtractor<List<ClaimByPaxReportValue>>
  {
    @Override
    public List<ClaimByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByPaxReportValue> results = new ArrayList<ClaimByPaxReportValue>();
      while ( rs.next() )
      {
        ClaimByPaxReportValue result = new ClaimByPaxReportValue();
        result.setPaxName( rs.getString( "submitter" ) );
        result.setTotalClaims( rs.getLong( "total_claims" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByPaxClaimStatusReportMapper implements ResultSetExtractor<List<ClaimByPaxReportValue>>
  {
    @Override
    public List<ClaimByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByPaxReportValue> results = new ArrayList<ClaimByPaxReportValue>();
      while ( rs.next() )
      {
        ClaimByPaxReportValue result = new ClaimByPaxReportValue();
        result.setPaxId( rs.getLong( "submitter_id" ) );
        result.setPaxName( rs.getString( "submitter" ) );
        result.setOpenClaims( rs.getLong( "open_claims" ) );
        result.setClosedClaims( rs.getLong( "closed_claims" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ClaimByPaxItemStatusReportMapper implements ResultSetExtractor<List<ClaimByPaxReportValue>>
  {
    @Override
    public List<ClaimByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ClaimByPaxReportValue> results = new ArrayList<ClaimByPaxReportValue>();
      while ( rs.next() )
      {
        ClaimByPaxReportValue result = new ClaimByPaxReportValue();
        result.setPaxId( rs.getLong( "submitter_id" ) );
        result.setPaxName( rs.getString( "submitter" ) );
        result.setApprovedItems( rs.getLong( "approved_items" ) );
        result.setDeniedItems( rs.getLong( "denied_items" ) );
        result.setHeldItems( rs.getLong( "held_items" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class PaxAvgClaimsSubmittedReportMapper implements ResultSetExtractor<BigDecimal>
  {
    @Override
    public BigDecimal extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      BigDecimal size = new BigDecimal( 0 );
      if ( rs.next() )
      {
        size = rs.getBigDecimal( "AVERAGE_SUBMITTED" );
      }
      return size;
    }

  }
}
