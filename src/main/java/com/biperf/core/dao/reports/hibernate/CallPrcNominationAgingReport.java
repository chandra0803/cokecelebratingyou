
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

import com.biperf.core.domain.enums.NominationApprovalStatusType;
import com.biperf.core.value.nomination.NominationAgingReportValue;
import com.biperf.core.value.nomination.NominationAgingReportValue.NominationAgingLevelReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationAgingReport extends StoredProcedure
{
  private static final String TAB_RESULTS_PROC = "pkg_query_nomi_aging.prc_summary_aging";

  /* Charts */
  private static final String BARCHART_RESULTS_PROC = "pkg_query_nomi_aging.prc_nominations_by_status";

  public CallPrcNominationAgingReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_parent_node_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_job_type_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotion_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nomi_status_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_approval_level_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participant_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_giver_recvr_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale_date_pattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );

    switch ( STORED_PROC_NAME )
    {
      case TAB_RESULTS_PROC:
        declareParameter( new SqlParameter( "p_in_rownum_start", Types.VARCHAR ) );
        declareParameter( new SqlParameter( "p_in_rownum_end", Types.VARCHAR ) );
        declareParameter( new SqlParameter( "p_in_sort_col_name", Types.VARCHAR ) );
        declareParameter( new SqlParameter( "p_in_sorted_by", Types.VARCHAR ) );
        declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationAgingReport.NominationAgingSummaryResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcNominationAgingReport.NominationAgingSummaryTotalsResultsMapper() ) );
        break;
      case BARCHART_RESULTS_PROC:
        declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationAgingReport.NominationAgingByStatusResultsMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters, String STORED_PROC_NAME )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_parent_node_id_list", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_department_list", reportParameters.get( "department" ) );
    inParams.put( "p_in_job_type_list", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_country_id_list", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_promotion_id_list", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_nomi_status_list", reportParameters.get( "nominationApprovalStatus" ) );
    inParams.put( "p_in_approval_level_list", reportParameters.get( "nominationApprovalLevel" ) );
    inParams.put( "p_in_participant_status", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_giver_recvr_type", reportParameters.get( "nominationAudienceType" ) );
    inParams.put( "p_in_locale_date_pattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );

    switch ( STORED_PROC_NAME )
    {
      case TAB_RESULTS_PROC:
        inParams.put( "p_in_rownum_start", reportParameters.get( "rowNumStart" ) );
        inParams.put( "p_in_rownum_end", reportParameters.get( "rowNumEnd" ) );
        inParams.put( "p_in_sort_col_name", reportParameters.get( "sortColName" ) );
        inParams.put( "p_in_sorted_by", reportParameters.get( "sortedBy" ) );
        break;
      default:
        break;
    }

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /* TABLES */
  /**
   * Mapper for summary results table
   */
  private class NominationAgingSummaryResultsMapper implements ResultSetExtractor<List<NominationAgingReportValue>>
  {
    @Override
    public List<NominationAgingReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationAgingReportValue> reportData = new ArrayList<>();

      while ( rs.next() )
      {
        NominationAgingReportValue reportValue = new NominationAgingReportValue();

        reportValue.setPromotionId( rs.getLong( "promotion_id" ) );
        reportValue.setPromotionName( rs.getString( "promotion_name" ) );
        reportValue.setTimePeriod( rs.getString( "time_period_name" ) );

        // There are currently 5 approver levels. Each one is structured the same way. So... loops.
        for ( int i = 1; i <= 5; ++i )
        {
          NominationAgingLevelReportValue levelValue = new NominationAgingLevelReportValue();

          levelValue.setNumberPending( rs.getLong( "r" + i + "_pending_cnt" ) );
          levelValue.setAverageWait( rs.getDouble( "r" + i + "_avg_pending_days" ) );
          levelValue.setNumberWinner( rs.getLong( "r" + i + "_winner_cnt" ) );
          levelValue.setNumberApproved( rs.getLong( "r" + i + "_approved_cnt" ) );
          levelValue.setNumberDenied( rs.getLong( "r" + i + "_non_winner_cnt" ) );
          levelValue.setNumberMoreInfo( rs.getLong( "r" + i + "_more_info_cnt" ) );

          reportValue.getLevelData().add( levelValue );
        }

        reportValue.setTotalRecords( rs.getLong( "total_records" ) );
        reportValue.setRecordSequence( rs.getLong( "rec_seq" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  /**
   * For summary results totals row
   */
  private class NominationAgingSummaryTotalsResultsMapper implements ResultSetExtractor<List<NominationAgingReportValue>>
  {
    @Override
    public List<NominationAgingReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationAgingReportValue> reportData = new ArrayList<>();

      while ( rs.next() )
      {
        NominationAgingReportValue reportValue = new NominationAgingReportValue();

        // There are currently 5 approver levels. Each one is structured the same way. So... loops.
        for ( int i = 1; i <= 5; ++i )
        {
          NominationAgingLevelReportValue levelValue = new NominationAgingLevelReportValue();

          levelValue.setNumberPending( rs.getLong( "r" + i + "_pending_cnt" ) );
          levelValue.setAverageWait( rs.getDouble( "r" + i + "_avg_pending_days" ) );
          levelValue.setNumberWinner( rs.getLong( "r" + i + "_winner_cnt" ) );
          levelValue.setNumberApproved( rs.getLong( "r" + i + "_approved_cnt" ) );
          levelValue.setNumberDenied( rs.getLong( "r" + i + "_non_winner_cnt" ) );
          levelValue.setNumberMoreInfo( rs.getLong( "r" + i + "_more_info_cnt" ) );

          reportValue.getLevelData().add( levelValue );
        }

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  /**
   * For nominations by status bar chart
   */
  private class NominationAgingByStatusResultsMapper implements ResultSetExtractor<Map<String, Long>>
  {
    @Override
    public Map<String, Long> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      Map<String, Long> reportData = new HashMap<>();

      // Pre-filling with all possible keys so they show even with 0
      reportData.put( NominationApprovalStatusType.PENDING, 0L );
      reportData.put( NominationApprovalStatusType.APPROVED, 0L );
      reportData.put( NominationApprovalStatusType.WINNER, 0L );
      reportData.put( NominationApprovalStatusType.NON_WINNER, 0L );
      reportData.put( NominationApprovalStatusType.MORE_INFO, 0L );

      while ( rs.next() )
      {
        String status = rs.getString( "CLAIM_ITEM_STATUS" );
        Long count = rs.getLong( "NOMINATION_CNT" );

        reportData.put( status, count );
      }

      return reportData;
    }
  }

}
