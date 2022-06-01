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
import com.biperf.core.value.recognition.RecognitionPointsReceivedByTimeValue;
import com.biperf.core.value.recognition.RecognitionPointsReceivedValue;
import com.biperf.core.value.recognition.RecognitionReceivedByTimeValue;
import com.biperf.core.value.recognition.RecognitionReceivedDetailTotalsValue;
import com.biperf.core.value.recognition.RecognitionReceivedDetailValue;
import com.biperf.core.value.recognition.RecognitionReceivedParticipationRateBarValue;
import com.biperf.core.value.recognition.RecognitionReceivedParticipationRatePieValue;
import com.biperf.core.value.recognition.RecognitionReceivedSummaryValue;
import com.biperf.core.value.recognition.RecognitionReceivedValue;

/**
 * @author poddutur
 *
 */
public class RecognitionReceivedByOrgReportAction extends RecognitionReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.RECOGNITION_RECEIVED_BY_ORG;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getRecognitionReportsService().getRecognitionReceivedSummaryResults( reportParameters );

      List<RecognitionReceivedSummaryValue> reportData = (List<RecognitionReceivedSummaryValue>)output.get( OUTPUT_DATA );

      Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        RecognitionReceivedSummaryValue totalsRowData = (RecognitionReceivedSummaryValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportData", reportData );
    }
    return mapping.findForward( "display_recognition_received_summary_report" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParameters.put( "giverReceiver", getGiverReceiverType() );

    Map<String, Object> output = getRecognitionReportsService().getRecognitionReceivedDetailResults( reportParameters );

    List<RecognitionReceivedDetailValue> reportData = (List<RecognitionReceivedDetailValue>)output.get( OUTPUT_DATA );

    Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      RecognitionReceivedDetailTotalsValue totalsRowData = (RecognitionReceivedDetailTotalsValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_received_detail_report" );
  }

  public ActionForward displayRecognitionReceivedPiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionReceivedPieResults( reportParameters );
    List<RecognitionReceivedValue> reportData = (List<RecognitionReceivedValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_received_piechart" );
  }

  public ActionForward displayRecognitionReceivedByTimeBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionReceivedByTimeBarResults( reportParameters );
    List<RecognitionReceivedByTimeValue> reportData = (List<RecognitionReceivedByTimeValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_received_bytime_barchart" );
  }

  public ActionForward displayRecognitionReceivedParticipationrateByOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionReceivedParticipationRateBarResults( reportParameters );
    List<RecognitionReceivedParticipationRateBarValue> reportData = (List<RecognitionReceivedParticipationRateBarValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_received_participationrate_barchart" );
  }

  public ActionForward displayRecognitionReceivedParticipationRatePiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionReceivedParticipationRatePieResults( reportParameters );
    List<RecognitionReceivedParticipationRatePieValue> reportData = (List<RecognitionReceivedParticipationRatePieValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_received_participationrate_piechart" );
  }

  public ActionForward displayRecognitionPointsReceivedBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsReceivedBarResults( reportParameters );
    List<RecognitionPointsReceivedValue> reportData = (List<RecognitionPointsReceivedValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_received_barchart" );
  }

  public ActionForward displayRecognitionPointsByTimeReceivedBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsByTimeReceivedBarResults( reportParameters );
    List<RecognitionPointsReceivedByTimeValue> reportData = (List<RecognitionPointsReceivedByTimeValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_received_bytime_barchart" );
  }

  @Override
  protected String getGiverReceiverType()
  {
    return GiverReceiverType.RECEIVER;
  }

}
