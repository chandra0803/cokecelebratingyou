
package com.biperf.core.dao.reports.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.SurveyReportsDAO;

public class SurveyReportsDAOImpl extends BaseReportsDAO implements SurveyReportsDAO
{
  private static final Log log = LogFactory.getLog( SurveyReportsDAOImpl.class );

  private DataSource dataSource;

  @Override
  public Map<String, Object> getSurveyAnalysisByOrgResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYANALYSISBYORG";
    String sortColName = "";
    String[] sortVariableColNames = { "NODE_NAME",
                                      "",
                                      "",
                                      "",
                                      "ELIGIBLE_PARTICIPANTS",
                                      "SURVEYS_TAKEN",
                                      "PERC_SURVEY_TAKEN",
                                      "RESP_1_SELECTED",
                                      "PERC_RESP_1_SELECTED",
                                      "RESP_2_SELECTED",
                                      "PERC_RESP_2_SELECTED",
                                      "RESP_3_SELECTED",
                                      "PERC_RESP_3_SELECTED",
                                      "RESP_4_SELECTED",
                                      "PERC_RESP_4_SELECTED",
                                      "RESP_5_SELECTED",
                                      "PERC_RESP_5_SELECTED",
                                      "RESP_6_SELECTED",
                                      "PERC_RESP_6_SELECTED",
                                      "RESP_7_SELECTED",
                                      "PERC_RESP_7_SELECTED",
                                      "RESP_8_SELECTED",
                                      "PERC_RESP_8_SELECTED",
                                      "RESP_9_SELECTED",
                                      "PERC_RESP_9_SELECTED",
                                      "RESP_10_SELECTED",
                                      "PERC_RESP_10_SELECTED", };

    Map<String, String> sortColNames = new HashMap<String, String>();
    sortColNames.put( "100", "MEAN" );
    sortColNames.put( "101", "STANDARD_DEVIATION" );

    Object sortedOnStr = reportParameters.get( "sortedOn" );
    int sortedOn = sortedOnStr != null ? ( (Integer)reportParameters.get( "sortedOn" ) ).intValue() : 0;
    if ( sortedOn > 0 && sortedOn < 28 ) // for mandatory + variable column names
    {
      sortColName = sortVariableColNames[sortedOn - 1];
    }
    else if ( sortedOn > 99 && sortedOn < 102 ) // for mandatory column names
    {
      sortColName = sortColNames.get( sortedOnStr.toString() );
    }
    else
    {
      sortColName = "NODE_NAME";
    }

    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcSurveyAnalysisReport procedure = new CallPrcSurveyAnalysisReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getSurveyAnalysisByQuestionsResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYANALYSISBYQUES";
    String sortColName = "survey_question_id";
    String[] sortColNames = { "survey_question_id", "SURVEYQUESTION" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcSurveyAnalysisReport procedure = new CallPrcSurveyAnalysisReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getSurveyStandardDeviationBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYSTANDARDDEVIATION";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcSurveyAnalysisReport procedure = new CallPrcSurveyAnalysisReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getSurveyTotalResponsePercentBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYTOTALRESPONSEBAR";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcSurveyAnalysisReport procedure = new CallPrcSurveyAnalysisReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getSurveyResponseList( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_SURVEY_REPORTS.PRC_GETSURVEYRESPONSELIST";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcSurveyAnalysisReport procedure = new CallPrcSurveyAnalysisReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public Map getSurveyAnalysisOptionsReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcSurveyOptionsExtract procedure = new CallPrcSurveyOptionsExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getSurveyAnalysisOpenEndedReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcSurveyOpenEndedExtract procedure = new CallPrcSurveyOpenEndedExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public boolean getSurveyResponseType( String promotionId )
  {
    Query query = null;
    query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getSurveyResponseType" );
    query.setParameter( "promotionId", promotionId );
    List results = query.list();
    if ( null == results || results.isEmpty() || results.get( 0 ) == null )
    {
      return false;
    }
    else
    {
      boolean isstandardResponse = results.contains( "standardResponse" );
      return isstandardResponse;
    }
  }
}
