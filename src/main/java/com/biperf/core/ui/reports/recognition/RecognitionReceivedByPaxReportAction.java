/**
 * 
 */

package com.biperf.core.ui.reports.recognition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.GiverReceiverType;
import com.biperf.core.domain.enums.ReceivedType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionPointsReceivedByParticipantValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionPointsReceivedByPromotionValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionSummaryReceivedByParticipantValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedByParticipantValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedByPromotionValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedMetricsValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedParticipationRateByPaxValue;
import com.biperf.core.value.recognitionreceivedbypax.RecognitionsReceivedScatterPlotValue;

/**
 * @author poddutur
 *
 */
public class RecognitionReceivedByPaxReportAction extends RecognitionReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.RECOGNITION_RECEIVED_BY_PAX;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      if ( !isHaveNotFilterSelected( reportParameters, request ) )
      {
        Map<String, Object> output = getRecognitionReportsService().getRecognitionSummaryReceivedByParticipantResults( reportParameters );
        List<RecognitionSummaryReceivedByParticipantValue> reportData = (List<RecognitionSummaryReceivedByParticipantValue>)output.get( OUTPUT_DATA );

        Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
        request.setAttribute( "maxRows", maxRows );

        if ( isLastPage( maxRows, reportParameters ) )
        {
          RecognitionSummaryReceivedByParticipantValue totalsRowData = (RecognitionSummaryReceivedByParticipantValue)output.get( OUTPUT_TOTALS_DATA );
          request.setAttribute( "totalsRowData", totalsRowData );
        }
        request.setAttribute( "reportData", reportData );
      }
    }
    return mapping.findForward( "display_recognition_summary_received_byparticipant_report" );
  }

  public ActionForward displayActivityDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParameters.put( "giverReceiver", getGiverReceiverType() );

    Long receiverNodeId = null;
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    try
    {
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      receiverNodeId = (Long)clientStateMap.get( "receiverNodeId" );
      reportParameters.put( "parentNodeId", receiverNodeId.toString() );
    }
    catch( Exception exception )
    {
      // do-nothing
    }

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getRecognitionReportsService().getRecognitionActivityReceivedByParticipantResults( reportParameters );
      List<RecognitionSummaryReceivedByParticipantValue> reportData = (List<RecognitionSummaryReceivedByParticipantValue>)output.get( OUTPUT_DATA );

      Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        RecognitionSummaryReceivedByParticipantValue totalsRowData = (RecognitionSummaryReceivedByParticipantValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportData", reportData );
    }
    return mapping.findForward( "display_recognition_activity_received_byparticipant_report" );
  }

  private boolean isHaveNotFilterSelected( Map<String, Object> reportParameters, HttpServletRequest request )
  {
    boolean isHaveNotFilterSelected = false;
    if ( ReceivedType.NOT_RECEIVED.equals( reportParameters.get( "receivedType" ) ) )
    {
      isHaveNotFilterSelected = true;
      request.setAttribute( "displayHaveNotFilterExportMessage", isHaveNotFilterSelected );
      request.setAttribute( "hideCharts", Boolean.TRUE );
      request.setAttribute( "maxRows", 1 );
    }
    return isHaveNotFilterSelected;
  }

  public ActionForward displayRecognitionsReceivedByParticipantBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsReceivedByParticipantResults( reportParameters );
    List<RecognitionsReceivedByParticipantValue> reportData = (List<RecognitionsReceivedByParticipantValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_received_byparticipant_barchart" );
  }

  public ActionForward displayRecognitionPointsReceivedByParticipantBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsReceivedByParticipantResults( reportParameters );
    List<RecognitionPointsReceivedByParticipantValue> reportData = (List<RecognitionPointsReceivedByParticipantValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_received_byparticipant_barchart" );
  }

  public ActionForward displayRecognitionsReceivedByPromotionBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsReceivedByPromotionResults( reportParameters );
    List<RecognitionsReceivedByPromotionValue> reportData = (List<RecognitionsReceivedByPromotionValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_received_bypromotion_barchart" );
  }

  public ActionForward displayRecognitionPointsReceivedByPromotionBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsReceivedByPromotionResults( reportParameters );
    List<RecognitionPointsReceivedByPromotionValue> reportData = (List<RecognitionPointsReceivedByPromotionValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_received_bypromotion_barchart" );
  }

  public ActionForward displayRecognitionsReceivedMetricsBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsReceivedMetricsResults( reportParameters );
    List<RecognitionsReceivedMetricsValue> reportData = (List<RecognitionsReceivedMetricsValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_received_metrics_barchart" );
  }

  public ActionForward displayRecognitionsReceivedParticipationRateByPaxPiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsReceivedParticipationRateByPaxResults( reportParameters );
    List<RecognitionsReceivedParticipationRateByPaxValue> reportData = (List<RecognitionsReceivedParticipationRateByPaxValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_received_participation_rate_bypax_piechart" );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displayRecognitionsReceivedScatterPlot( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsReceivedScatterPlotResults( reportParameters );
    List<RecognitionsReceivedScatterPlotValue> reportData = (List<RecognitionsReceivedScatterPlotValue>)output.get( OUTPUT_DATA );

    Map<String, RecognitionsReceivedScatterPlotValue> valueToNamesMap = new HashMap<String, RecognitionsReceivedScatterPlotValue>();
    for ( RecognitionsReceivedScatterPlotValue chartData : reportData )
    {
      RecognitionsReceivedScatterPlotValue existingPlotValue = valueToNamesMap.get( chartData.getRecognitionCount() + "," + chartData.getDaysSinceLastRec() );
      if ( existingPlotValue != null )
      {
        existingPlotValue.getParticipants().add( chartData.getParticipantName() );
      }
      else
      {
        RecognitionsReceivedScatterPlotValue newPlotValue = new RecognitionsReceivedScatterPlotValue();
        newPlotValue.setDaysSinceLastRec( chartData.getDaysSinceLastRec() );
        newPlotValue.setRecognitionCount( chartData.getRecognitionCount() );
        newPlotValue.getParticipants().add( chartData.getParticipantName() );
        valueToNamesMap.put( chartData.getRecognitionCount() + "," + chartData.getDaysSinceLastRec(), newPlotValue );
      }
    }

    reportData = new ArrayList<RecognitionsReceivedScatterPlotValue>( valueToNamesMap.values() );

    request.setAttribute( "reportData", reportData );
    request.setAttribute( "siteUrlPrefix", getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    return mapping.findForward( "display_recognitions_received_scatter_plot" );
  }

  @Override
  protected String getGiverReceiverType()
  {
    return GiverReceiverType.RECEIVER;
  }
}
