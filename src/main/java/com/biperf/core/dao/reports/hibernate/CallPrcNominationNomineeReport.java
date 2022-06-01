
package com.biperf.core.dao.reports.hibernate;

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

import com.biperf.core.value.nomination.NominationReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcNominationNomineeReport extends StoredProcedure
{

  public CallPrcNominationNomineeReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale_date_pattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotion_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rownum_start", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_end", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sort_col_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sorted_by", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationNomineeReport.NominationGiverNominatorsListMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcNominationNomineeReport.NominationGiverNominatorsListTotalsMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", reportParameters.get( "userId" ) );
    inParams.put( "p_in_locale_date_pattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_promotion_id_list", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_locale", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rownum_start", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rownum_end", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sort_col_name", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sorted_by", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationGiverNominatorsListMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationReportValue> reportData = new ArrayList<NominationReportValue>();

      while ( rs.next() )
      {
        NominationReportValue reportValue = new NominationReportValue();
        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setDateSubmitted( rs.getDate( "DATE_SUBMITTED" ) );
        reportValue.setNominee( rs.getString( "NOMINEE" ) );
        reportValue.setRecvrCountryName( rs.getString( "RECVR_COUNTRY_NAME" ) );
        reportValue.setRecvrNodeName( rs.getString( "RECVR_NODE_NAME" ) );
        reportValue.setTeamName( rs.getString( "TEAM_NAME" ) );
        reportValue.setNominator( rs.getString( "NOMINATOR" ) );
        reportValue.setGiverCountryName( rs.getString( "GIVER_COUNTRY_NAME" ) );
        reportValue.setGiverNodeName( rs.getString( "GIVER_NODE_NAME" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setClaimApprovalStatus( rs.getString( "CLAIM_ITEM_STATUS" ) );
        reportValue.setPointsAwardAmt( rs.getLong( "POINTS_AWARD_AMT" ) );
        reportValue.setCashAwardAmt( rs.getLong( "CASH_AWARD_AMT" ) );
        reportValue.setOtherAwardDesc( rs.getString( "OTHER_AWARD_DESC" ) );
        reportValue.setOtherAwardAmt( rs.getLong( "OTHER_AWARD_AMT" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        reportValue.setRecseq( rs.getLong( "REC_SEQ" ) );

        reportData.add( reportValue );
      }
      return reportData;
    }

  }

  private class NominationGiverNominatorsListTotalsMapper implements ResultSetExtractor
  {
    @Override
    public NominationReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      NominationReportValue reportValue = new NominationReportValue();
      while ( rs.next() )
      {
        reportValue.setPointsAwardAmt( rs.getLong( "POINTS_AWARD_AMT" ) );
        reportValue.setCashAwardAmt( rs.getLong( "CASH_AWARD_AMT" ) );
        reportValue.setOtherAwardAmt( rs.getLong( "OTHER_AWARD_AMT" ) );
      }
      return reportValue;
    }

  }

}
