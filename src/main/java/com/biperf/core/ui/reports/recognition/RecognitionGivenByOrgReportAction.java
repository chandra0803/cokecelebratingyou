/**
 * 
 */

package com.biperf.core.ui.reports.recognition;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.GiverReceiverType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.recognition.RecognitionGivenByTimeValue;
import com.biperf.core.value.recognition.RecognitionGivenDetailTotalsValue;
import com.biperf.core.value.recognition.RecognitionGivenDetailValue;
import com.biperf.core.value.recognition.RecognitionGivenParticipationRateBarValue;
import com.biperf.core.value.recognition.RecognitionGivenParticipationRatePieValue;
import com.biperf.core.value.recognition.RecognitionPointsGivenByTimeValue;
import com.biperf.core.value.recognition.RecognitionPointsGivenValue;
import com.biperf.core.value.recognition.RecognitionSummaryValue;
import com.biperf.core.value.recognition.RecognitionValue;

/**
 * @author poddutur
 *
 */
public class RecognitionGivenByOrgReportAction extends RecognitionReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.RECOGNITION_GIVEN_BY_ORG;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getRecognitionReportsService().getRecognitionSummaryResults( reportParameters );
      List<RecognitionSummaryValue> reportData = (List<RecognitionSummaryValue>)output.get( OUTPUT_DATA );

      Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        RecognitionSummaryValue totalsRowData = (RecognitionSummaryValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportData", reportData );
    }
    return mapping.findForward( "display_recognition_given_summary_report" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionGivenDetailResults( reportParameters );
    List<RecognitionGivenDetailValue> reportData = (List<RecognitionGivenDetailValue>)output.get( OUTPUT_DATA );

    Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      RecognitionGivenDetailTotalsValue totalsRowData = (RecognitionGivenDetailTotalsValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_given_detail_report" );
  }

  public ActionForward displayRecognitionGivenPiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionGivenPieResults( reportParameters );
    List<RecognitionValue> reportData = (List<RecognitionValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_given_piechart" );
  }

  public ActionForward displayRecognitionGivenByTimeBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionGivenByTimeBarResults( reportParameters );
    List<RecognitionGivenByTimeValue> reportData = (List<RecognitionGivenByTimeValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_given_bytime_barchart" );
  }

  public ActionForward displayRecognitionGivenParticipationrateByOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionGivenParticipationRateBarResults( reportParameters );
    List<RecognitionGivenParticipationRateBarValue> reportData = (List<RecognitionGivenParticipationRateBarValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_given_participationrate_barchart" );
  }

  public ActionForward displayRecognitionGivenParticipationRatePiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionGivenParticipationRatePieResults( reportParameters );
    List<RecognitionGivenParticipationRatePieValue> reportData = (List<RecognitionGivenParticipationRatePieValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_given_participationrate_piechart" );
  }

  public ActionForward displayRecognitionPointsGivenBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsGivenBarResults( reportParameters );
    List<RecognitionPointsGivenValue> reportData = (List<RecognitionPointsGivenValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_given_barchart" );
  }

  public ActionForward displayRecognitionPointsGivenByTimeBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsGivenByTimeBarResults( reportParameters );
    List<RecognitionPointsGivenByTimeValue> reportData = (List<RecognitionPointsGivenByTimeValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_given_bytime_barchart" );
  }

  @Override
  protected String getGiverReceiverType()
  {
    return GiverReceiverType.GIVER;
  }

}
