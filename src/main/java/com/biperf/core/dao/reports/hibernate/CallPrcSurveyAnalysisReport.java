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

import com.biperf.core.value.survey.StandardDeviationReportValue;
import com.biperf.core.value.survey.SurveyAnalysisByQuestionReportValue;
import com.biperf.core.value.survey.SurveyAnalysisHeaderListReportValue;
import com.biperf.core.value.survey.SurveyAnalysisSummaryReportValue;
import com.biperf.core.value.survey.TotalResponsePercentValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcSurveyAnalysisReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYANALYSISBYORG";
  private static final String PROC2 = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYANALYSISBYQUES";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYSTANDARDDEVIATION";
  private static final String PROC4 = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYTOTALRESPONSEBAR";

  private static final String PROC5 = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYRESPONSELIST";

  public CallPrcSurveyAnalysisReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_minimumResponse", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcSurveyAnalysisReport.BasicSurveyAnalysisByOrgMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcSurveyAnalysisReport.BasicSurveyAnalysisByQuestionsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcSurveyAnalysisReport.BasicSurveyAnalysisByQuestionsTotalMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcSurveyAnalysisReport.BasicSurveyStandardDeviationChartMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcSurveyAnalysisReport.BasicSurveyTotalResponsePercentChartMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcSurveyAnalysisReport.SurveyResponseListMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_minimumResponse", reportParameters.get( "minimumResponse" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class BasicSurveyAnalysisByOrgMapper implements ResultSetExtractor
  {
    @Override
    public List<SurveyAnalysisSummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SurveyAnalysisSummaryReportValue> reportData = new ArrayList<SurveyAnalysisSummaryReportValue>();

      while ( rs.next() )
      {
        SurveyAnalysisSummaryReportValue reportValue = new SurveyAnalysisSummaryReportValue();

        reportValue.setNodeId( rs.getLong( "NODE_ID" ) );
        reportValue.setNodeName( rs.getString( "NODE_NAME" ) );
        reportValue.setEligiblePax( rs.getLong( "ELIGIBLE_PARTICIPANTS" ) );
        reportValue.setSurveysTaken( rs.getLong( "SURVEYS_TAKEN" ) );
        reportValue.setSurveysTakenPerc( rs.getBigDecimal( "PERC_SURVEY_TAKEN" ) );
        reportValue.setResponse1Selected( rs.getLong( "RESP_1_SELECTED" ) );
        reportValue.setResponse1SelectedPerc( rs.getBigDecimal( "PERC_RESP_1_SELECTED" ) );
        reportValue.setResponse2Selected( rs.getLong( "RESP_2_SELECTED" ) );
        reportValue.setResponse2SelectedPerc( rs.getBigDecimal( "PERC_RESP_2_SELECTED" ) );
        reportValue.setResponse3Selected( rs.getLong( "RESP_3_SELECTED" ) );
        reportValue.setResponse3SelectedPerc( rs.getBigDecimal( "PERC_RESP_3_SELECTED" ) );
        reportValue.setResponse4Selected( rs.getLong( "RESP_4_SELECTED" ) );
        reportValue.setResponse4SelectedPerc( rs.getBigDecimal( "PERC_RESP_4_SELECTED" ) );
        reportValue.setResponse5Selected( rs.getLong( "RESP_5_SELECTED" ) );
        reportValue.setResponse5SelectedPerc( rs.getBigDecimal( "PERC_RESP_5_SELECTED" ) );
        reportValue.setResponse6Selected( rs.getLong( "RESP_6_SELECTED" ) );
        reportValue.setResponse6SelectedPerc( rs.getBigDecimal( "PERC_RESP_6_SELECTED" ) );
        reportValue.setResponse7Selected( rs.getLong( "RESP_7_SELECTED" ) );
        reportValue.setResponse7SelectedPerc( rs.getBigDecimal( "PERC_RESP_7_SELECTED" ) );
        reportValue.setResponse8Selected( rs.getLong( "RESP_8_SELECTED" ) );
        reportValue.setResponse8SelectedPerc( rs.getBigDecimal( "PERC_RESP_8_SELECTED" ) );
        reportValue.setResponse9Selected( rs.getLong( "RESP_9_SELECTED" ) );
        reportValue.setResponse9SelectedPerc( rs.getBigDecimal( "PERC_RESP_9_SELECTED" ) );
        reportValue.setResponse10Selected( rs.getLong( "RESP_10_SELECTED" ) );
        reportValue.setResponse10SelectedPerc( rs.getBigDecimal( "PERC_RESP_10_SELECTED" ) );
        reportValue.setMeanValue( rs.getBigDecimal( "MEAN" ) );
        reportValue.setStandardDeviation( rs.getBigDecimal( "STANDARD_DEVIATION" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicSurveyAnalysisByQuestionsMapper implements ResultSetExtractor
  {
    @Override
    public List<SurveyAnalysisByQuestionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SurveyAnalysisByQuestionReportValue> reportData = new ArrayList<SurveyAnalysisByQuestionReportValue>();

      while ( rs.next() )
      {
        SurveyAnalysisByQuestionReportValue reportValue = new SurveyAnalysisByQuestionReportValue();
        reportValue.setSurveyQuestionId( rs.getLong( "survey_question_id" ) );
        reportValue.setSurveyQuestion( rs.getString( "SURVEYQUESTION" ) );
        reportValue.setEligiblePax( rs.getLong( "ELIGIBLE_PARTICIPANTS" ) );
        reportValue.setSurveysTaken( rs.getLong( "SURVEYS_TAKEN" ) );
        reportValue.setSurveysTakenPerc( rs.getBigDecimal( "PERC_SURVEY_TAKEN" ) );
        reportValue.setResponse1Selected( rs.getLong( "RESP_1_SELECTED" ) );
        reportValue.setResponse1SelectedPerc( rs.getBigDecimal( "PERC_RESP_1_SELECTED" ) );
        reportValue.setResponse2Selected( rs.getLong( "RESP_2_SELECTED" ) );
        reportValue.setResponse2SelectedPerc( rs.getBigDecimal( "PERC_RESP_2_SELECTED" ) );
        reportValue.setResponse3Selected( rs.getLong( "RESP_3_SELECTED" ) );
        reportValue.setResponse3SelectedPerc( rs.getBigDecimal( "PERC_RESP_3_SELECTED" ) );
        reportValue.setResponse4Selected( rs.getLong( "RESP_4_SELECTED" ) );
        reportValue.setResponse4SelectedPerc( rs.getBigDecimal( "PERC_RESP_4_SELECTED" ) );
        reportValue.setResponse5Selected( rs.getLong( "RESP_5_SELECTED" ) );
        reportValue.setResponse5SelectedPerc( rs.getBigDecimal( "PERC_RESP_5_SELECTED" ) );
        reportValue.setResponse6Selected( rs.getLong( "RESP_6_SELECTED" ) );
        reportValue.setResponse6SelectedPerc( rs.getBigDecimal( "PERC_RESP_6_SELECTED" ) );
        reportValue.setResponse7Selected( rs.getLong( "RESP_7_SELECTED" ) );
        reportValue.setResponse7SelectedPerc( rs.getBigDecimal( "PERC_RESP_7_SELECTED" ) );
        reportValue.setResponse8Selected( rs.getLong( "RESP_8_SELECTED" ) );
        reportValue.setResponse8SelectedPerc( rs.getBigDecimal( "PERC_RESP_8_SELECTED" ) );
        reportValue.setResponse9Selected( rs.getLong( "RESP_9_SELECTED" ) );
        reportValue.setResponse9SelectedPerc( rs.getBigDecimal( "PERC_RESP_9_SELECTED" ) );
        reportValue.setResponse10Selected( rs.getLong( "RESP_10_SELECTED" ) );
        reportValue.setResponse10SelectedPerc( rs.getBigDecimal( "PERC_RESP_10_SELECTED" ) );
        reportValue.setMeanValue( rs.getBigDecimal( "MEAN" ) );
        reportValue.setStandardDeviation( rs.getBigDecimal( "STANDARD_DEVIATION" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicSurveyAnalysisByQuestionsTotalMapper implements ResultSetExtractor
  {
    @Override
    public SurveyAnalysisByQuestionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SurveyAnalysisByQuestionReportValue reportValue = new SurveyAnalysisByQuestionReportValue();

      while ( rs.next() )
      {
        reportValue.setEligiblePax( rs.getLong( "ELIGIBLE_SURVEYS" ) );
        reportValue.setSurveysTaken( rs.getLong( "SURVEYS_TAKEN" ) );
        reportValue.setSurveysTakenPerc( rs.getBigDecimal( "PERC_SURVEY_TAKEN" ) );
        reportValue.setResponse1Selected( rs.getLong( "RESP_1_SELECTED" ) );
        reportValue.setResponse1SelectedPerc( rs.getBigDecimal( "PERC_RESP_1_SELECTED" ) );
        reportValue.setResponse2Selected( rs.getLong( "RESP_2_SELECTED" ) );
        reportValue.setResponse2SelectedPerc( rs.getBigDecimal( "PERC_RESP_2_SELECTED" ) );
        reportValue.setResponse3Selected( rs.getLong( "RESP_3_SELECTED" ) );
        reportValue.setResponse3SelectedPerc( rs.getBigDecimal( "PERC_RESP_3_SELECTED" ) );
        reportValue.setResponse4Selected( rs.getLong( "RESP_4_SELECTED" ) );
        reportValue.setResponse4SelectedPerc( rs.getBigDecimal( "PERC_RESP_4_SELECTED" ) );
        reportValue.setResponse5Selected( rs.getLong( "RESP_5_SELECTED" ) );
        reportValue.setResponse5SelectedPerc( rs.getBigDecimal( "PERC_RESP_5_SELECTED" ) );
        reportValue.setResponse6Selected( rs.getLong( "RESP_6_SELECTED" ) );
        reportValue.setResponse6SelectedPerc( rs.getBigDecimal( "PERC_RESP_6_SELECTED" ) );
        reportValue.setResponse7Selected( rs.getLong( "RESP_7_SELECTED" ) );
        reportValue.setResponse7SelectedPerc( rs.getBigDecimal( "PERC_RESP_7_SELECTED" ) );
        reportValue.setResponse8Selected( rs.getLong( "RESP_8_SELECTED" ) );
        reportValue.setResponse8SelectedPerc( rs.getBigDecimal( "PERC_RESP_8_SELECTED" ) );
        reportValue.setResponse9Selected( rs.getLong( "RESP_9_SELECTED" ) );
        reportValue.setResponse9SelectedPerc( rs.getBigDecimal( "PERC_RESP_9_SELECTED" ) );
        reportValue.setResponse10Selected( rs.getLong( "RESP_10_SELECTED" ) );
        reportValue.setResponse10SelectedPerc( rs.getBigDecimal( "PERC_RESP_10_SELECTED" ) );
        reportValue.setMeanValue( rs.getBigDecimal( "MEAN" ) );
        reportValue.setStandardDeviation( rs.getBigDecimal( "STANDARD_DEVIATION" ) );
      }

      return reportValue;
    }
  }

  private class BasicSurveyStandardDeviationChartMapper implements ResultSetExtractor
  {
    @Override
    public List<StandardDeviationReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<StandardDeviationReportValue> reportData = new ArrayList<StandardDeviationReportValue>();

      while ( rs.next() )
      {
        StandardDeviationReportValue reportValue = new StandardDeviationReportValue();

        reportValue.setNodeName( rs.getString( "NODE_NAME" ) );
        reportValue.setMeanValue( rs.getBigDecimal( "MEAN" ) );
        reportValue.setStandardDeviation( rs.getBigDecimal( "STANDARD_DEVIATION" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicSurveyTotalResponsePercentChartMapper implements ResultSetExtractor
  {
    @Override
    public List<TotalResponsePercentValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<TotalResponsePercentValue> reportData = new ArrayList<TotalResponsePercentValue>();

      while ( rs.next() )
      {
        TotalResponsePercentValue reportValue = new TotalResponsePercentValue();

        reportValue.setNodeName( rs.getString( "NODE_NAME" ) );
        reportValue.setResponse1SelectedPerc( rs.getBigDecimal( "PERC_RESP_1_SELECTED" ) );
        reportValue.setResponse2SelectedPerc( rs.getBigDecimal( "PERC_RESP_2_SELECTED" ) );
        reportValue.setResponse3SelectedPerc( rs.getBigDecimal( "PERC_RESP_3_SELECTED" ) );
        reportValue.setResponse4SelectedPerc( rs.getBigDecimal( "PERC_RESP_4_SELECTED" ) );
        reportValue.setResponse5SelectedPerc( rs.getBigDecimal( "PERC_RESP_5_SELECTED" ) );
        reportValue.setResponse6SelectedPerc( rs.getBigDecimal( "PERC_RESP_6_SELECTED" ) );
        reportValue.setResponse7SelectedPerc( rs.getBigDecimal( "PERC_RESP_7_SELECTED" ) );
        reportValue.setResponse8SelectedPerc( rs.getBigDecimal( "PERC_RESP_8_SELECTED" ) );
        reportValue.setResponse9SelectedPerc( rs.getBigDecimal( "PERC_RESP_9_SELECTED" ) );
        reportValue.setResponse10SelectedPerc( rs.getBigDecimal( "PERC_RESP_10_SELECTED" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class SurveyResponseListMapper implements ResultSetExtractor
  {
    @Override
    public List<SurveyAnalysisHeaderListReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SurveyAnalysisHeaderListReportValue> reportData = new ArrayList<SurveyAnalysisHeaderListReportValue>();

      while ( rs.next() )
      {
        SurveyAnalysisHeaderListReportValue reportValue = new SurveyAnalysisHeaderListReportValue();

        reportValue.setHeaderValue( rs.getString( "response" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
