
package com.biperf.core.ui.reports.survey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.CollectionUtils;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.survey.StandardDeviationReportValue;
import com.biperf.core.value.survey.SurveyAnalysisByQuestionReportValue;
import com.biperf.core.value.survey.SurveyAnalysisHeaderListReportValue;
import com.biperf.core.value.survey.SurveyAnalysisSummaryReportValue;
import com.biperf.core.value.survey.TotalResponsePercentValue;
import com.biperf.util.StringUtils;

public class SurveyAnalysisReportAction extends SurveyReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.SURVEY_ANALYSIS;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    reportParameters.put( "minimumResponse", getMinResponses() );

    List<String> requiredFields = Arrays.asList( new String[] { FILTER_PROMOTION_ID } );
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, true ) )
    {
      return mapping.findForward( "display_survey_analysis_summary_report" );
    }
    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) ) || isDashboardPage( request ) )
    {
      Map<String, Object> output = getSurveyReportsService().getSurveyAnalysisByOrgResults( reportParameters );
      List<SurveyAnalysisSummaryReportValue> reportData = (List<SurveyAnalysisSummaryReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );
      String promotionId = (String)reportParameters.get( "promotionId" );
      boolean isStdResType = getSurveyReportsService().getSurveyResponseType( promotionId );
      request.setAttribute( "isShowView", isStdResType );
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }

    return mapping.findForward( "display_survey_analysis_summary_report" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    reportParametersForm.setQqId( null );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    reportParameters.put( "minimumResponse", getMinResponses() );
    Map<String, Object> output = getSurveyReportsService().getSurveyAnalysisByQuestionsResults( reportParameters );
    List<SurveyAnalysisByQuestionReportValue> reportData = (List<SurveyAnalysisByQuestionReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      SurveyAnalysisByQuestionReportValue totalsRowData = (SurveyAnalysisByQuestionReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    return mapping.findForward( "display_survey_analysis_by_question_report" );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected String getExtractReportData( Map<String, Object> extractParameters )
  {
    extractParameters.put( "isExtract", Boolean.TRUE );
    extractParameters.put( "minimumResponse", getMinResponses() );
    extractParameters.put( "sortColName", "NODE_NAME" );

    Map<String, Object> reportExtractData = getSurveyReportsService().getSurveyAnalysisByOrgResults( extractParameters );

    List<SurveyAnalysisSummaryReportValue> reportData = (List<SurveyAnalysisSummaryReportValue>)reportExtractData.get( OUTPUT_DATA );

    return buildCSVSurveyAnalysisByOrgResults( reportData );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getSecondaryExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "minimumResponse", getMinResponses() );
    Map reportExtractData = getSurveyReportsService().getSurveyAnalysisOpenEndedReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_OPEN_ENDED_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected String getSecondExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "SurveyAnalysis_" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.SURVEY_ANALYSIS_REPORT;
  }

  @Override
  protected ReportBeanMethod getSecondaryReportBeanMethod()
  {
    return ReportBeanMethod.SURVEY_OPEN_ENDED_REPORT;
  }

  public ActionForward displayStandardDeviationBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "minimumResponse", getMinResponses() );
    Map<String, Object> output = getSurveyReportsService().getSurveyStandardDeviationBarResults( reportParameters );
    List<StandardDeviationReportValue> reportData = (List<StandardDeviationReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_survey_attempts_status_for_org_report" );
  }

  public ActionForward displayTotalResponsePercentBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "minimumResponse", getMinResponses() );
    Map<String, Object> output = getSurveyReportsService().getSurveyTotalResponsePercentBarResults( reportParameters );
    List<TotalResponsePercentValue> reportData = (List<TotalResponsePercentValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_survey_attempts_percent_for_org_report" );
  }

  public static final String[] EXTRACT_OPEN_ENDED_COLUMN_NAMES = { "ORG_UNIT", "QUESTIONS_ASKED", "RESPONSE" };

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.survey.extract";
  }

  private void getHeaders( Map<String, Object> reportParameters, StringBuffer contentBuf )
  {
    if ( (String)reportParameters.get( "promotionId" ) != null )
    {
      List<SurveyAnalysisHeaderListReportValue> surveyResponseList = new ArrayList<SurveyAnalysisHeaderListReportValue>();

      Map<String, Object> output = getSurveyReportsService().getSurveyResponseList( reportParameters );
      surveyResponseList = (List<SurveyAnalysisHeaderListReportValue>)output.get( OUTPUT_DATA );
      String[] constantString = new String[surveyResponseList.size() + 7];
      constantString[0] = "ORG_UNIT";
      constantString[1] = "QUESTIONS_ASKED";
      constantString[2] = "ELIGIBLE_PAX";
      constantString[3] = "TOTAL_RESPONSE";
      constantString[4] = "TOTAL_RESPONSE_PERC";
      constantString[constantString.length - 2] = "MEAN";
      constantString[constantString.length - 1] = "STD_DEVIATION";

      reportParameters.put( "csHeadersForSurvey", buildCSVColumnNamesForSurvey( contentBuf, constantString, surveyResponseList ) );
    }
  }

  private int getMinResponses()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.SURVEY_REPORT_RESPONSE_COUNT ).getIntVal();
  }

  @Override
  protected String[] getColumnHeaders()
  {
    throw new RuntimeException( "Method not supported" );
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private String buildCSVSurveyAnalysisByOrgResults( List<SurveyAnalysisSummaryReportValue> reportData )
  {
    StringBuilder reportBuilder = new StringBuilder();

    reportBuilder.append( "ORG UNIT" + REGEX_COMMA + "QUESTIONS ASKED" + REGEX_COMMA + "ELIGIBLE PARTICIPANTS" + REGEX_COMMA + "SURVEYS TAKEN" + REGEX_COMMA + "SURVEYS TAKEN %" + REGEX_COMMA + "MEAN"
        + REGEX_COMMA + "STANDARD DEVIATION" );

    if ( !CollectionUtils.isEmpty( reportData ) )
    {
      reportData.forEach( e ->
      {
        reportBuilder.append( NEW_LINE + e.getNodeName() + REGEX_COMMA + "View" + REGEX_COMMA + e.getEligiblePax() + REGEX_COMMA + e.getSurveysTaken() + REGEX_COMMA + e.getSurveysTakenPerc()
            + REGEX_COMMA + e.getMeanValue() + REGEX_COMMA + e.getStandardDeviation() );

      } );
    }

    return reportBuilder.toString();
  }

}
