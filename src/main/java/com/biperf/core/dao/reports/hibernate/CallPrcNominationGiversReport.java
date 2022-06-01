/**
 * 
 */

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

import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.NominationReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcNominationGiversReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_NOMI_GIVEN_T.PRC_SUMMARY_NOMINATOR";

  public CallPrcNominationGiversReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_parent_node_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_job_type_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotion_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participant_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale_date_pattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_viewer_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_start", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_end", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sort_col_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sorted_by", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationGiversReport.NominationGiverNominatorsListMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcNominationGiversReport.NominationGiverNominatorsListTotalsMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_parent_node_id_list", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_department_list", reportParameters.get( "department" ) );
    inParams.put( "p_in_job_type_list", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_country_id_list", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_promotion_id_list", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_participant_status", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_locale_date_pattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_locale", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_viewer_user_id", UserManager.getUserId() );
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
        reportValue.setGiverUserId( rs.getLong( "GIVER_USER_ID" ) );
        reportValue.setNominator( rs.getString( "NOMINATOR" ) );
        reportValue.setGiverCountryName( rs.getString( "GIVER_COUNTRY_NAME" ) );
        reportValue.setGiverNodeName( rs.getString( "GIVER_NODE_NAME" ) );
        reportValue.setSubmittedCnt( rs.getLong( "SUBMITTED_CNT" ) );
        reportValue.setPointsIssued( rs.getLong( "POINTS_ISSUED" ) );
        reportValue.setCashIssued( rs.getLong( "CASH_ISSUED" ) );
        reportValue.setOtherQytIssued( rs.getLong( "OTHER_QTY_ISSUED" ) );
        reportValue.setOtherAmtIssued( rs.getLong( "OTHER_AMT_ISSUED" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        reportValue.setPromotionId( rs.getString( "PROMOTION_ID" ) );
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
        reportValue.setSubmittedCnt( rs.getLong( "SUBMITTED_CNT" ) );
        reportValue.setPointsIssued( rs.getLong( "POINTS_ISSUED" ) );
        reportValue.setCashIssued( rs.getLong( "CASH_ISSUED" ) );
        reportValue.setOtherQytIssued( rs.getLong( "OTHER_QTY_ISSUED" ) );
        reportValue.setOtherAmtIssued( rs.getLong( "OTHER_AMT_Issued" ) );
      }

      return reportValue;
    }
  }

}
