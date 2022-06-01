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

import com.biperf.core.domain.enums.GivenType;
import com.biperf.core.domain.enums.GiverReceiverType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.value.recognitionbypax.RecognitionPointsGivenByParticipantValue;
import com.biperf.core.value.recognitionbypax.RecognitionPointsGivenByPromotionValue;
import com.biperf.core.value.recognitionbypax.RecognitionSummaryGivenByParticipantValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenByParticipantValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenByPromotionValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenMetricsValue;
import com.biperf.core.value.recognitionbypax.RecognitionsGivenParticipationRateByPaxValue;

/**
 * @author poddutur
 *
 */
public class RecognitionGivenByPaxReportAction extends RecognitionReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.RECOGNITION_GIVEN_BY_PAX;
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
        Map<String, Object> output = getRecognitionReportsService().getRecognitionSummaryGivenByParticipantResults( reportParameters );

        List<RecognitionSummaryGivenByParticipantValue> reportData = (List<RecognitionSummaryGivenByParticipantValue>)output.get( OUTPUT_DATA );

        Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
        request.setAttribute( "maxRows", maxRows );

        if ( isLastPage( maxRows, reportParameters ) )
        {
          RecognitionSummaryGivenByParticipantValue totalsRowData = (RecognitionSummaryGivenByParticipantValue)output.get( OUTPUT_TOTALS_DATA );
          request.setAttribute( "totalsRowData", totalsRowData );
        }
        request.setAttribute( "reportData", reportData );
      }
    }
    return mapping.findForward( "display_recognition_summary_given_byparticipant_report" );
  }

  public ActionForward displayActivityDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Long giverNodeId = null;
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
      giverNodeId = (Long)clientStateMap.get( "giverNodeId" );
      reportParameters.put( "parentNodeId", giverNodeId.toString() );
    }
    catch( Exception exception )
    {
      // do-nothing
    }

    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      if ( !isHaveNotFilterSelected( reportParameters, request ) )
      {
        Map<String, Object> output = getRecognitionReportsService().getRecognitionActivityGivenByParticipantResults( reportParameters );

        List<RecognitionSummaryGivenByParticipantValue> reportData = (List<RecognitionSummaryGivenByParticipantValue>)output.get( OUTPUT_DATA );

        Integer maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
        request.setAttribute( "maxRows", maxRows );

        if ( isLastPage( maxRows, reportParameters ) )
        {
          RecognitionSummaryGivenByParticipantValue totalsRowData = (RecognitionSummaryGivenByParticipantValue)output.get( OUTPUT_TOTALS_DATA );
          request.setAttribute( "totalsRowData", totalsRowData );
        }
        request.setAttribute( "reportData", reportData );
      }
    }
    return mapping.findForward( "display_recognition_activity_given_byparticipant_report" );
  }

  private boolean isHaveNotFilterSelected( Map<String, Object> reportParameters, HttpServletRequest request )
  {
    boolean isHaveNotFilterSelected = false;
    if ( GivenType.HAVE_NOT.equals( reportParameters.get( "givenType" ) ) )
    {
      isHaveNotFilterSelected = true;
      request.setAttribute( "displayHaveNotFilterExportMessage", isHaveNotFilterSelected );
      request.setAttribute( "hideCharts", Boolean.TRUE );
      request.setAttribute( "maxRows", 1 );
    }
    return isHaveNotFilterSelected;
  }

  public ActionForward displayRecognitionsGivenByParticipantBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsGivenByParticipantResults( reportParameters );
    List<RecognitionsGivenByParticipantValue> reportData = (List<RecognitionsGivenByParticipantValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_given_byparticipant_barchart" );
  }

  public ActionForward displayRecognitionPointsGivenByParticipantBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsGivenByParticipantResults( reportParameters );
    List<RecognitionPointsGivenByParticipantValue> reportData = (List<RecognitionPointsGivenByParticipantValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_given_byparticipant_barchart" );
  }

  public ActionForward displayRecognitionsGivenByPromotionBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsGivenByPromotionResults( reportParameters );
    List<RecognitionsGivenByPromotionValue> reportData = (List<RecognitionsGivenByPromotionValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_given_bypromotion_barchart" );
  }

  public ActionForward displayRecognitionPointsGivenByPromotionBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionPointsGivenByPromotionResults( reportParameters );
    List<RecognitionPointsGivenByPromotionValue> reportData = (List<RecognitionPointsGivenByPromotionValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognition_points_given_bypromotion_barchart" );
  }

  public ActionForward displayRecognitionsGivenMetricsBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsGivenMetricsResults( reportParameters );
    List<RecognitionsGivenMetricsValue> reportData = (List<RecognitionsGivenMetricsValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_given_metrics_barchart" );
  }

  public ActionForward displayRecognitionsGivenParticipationRateByPaxPiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    reportParameters.put( "giverReceiver", getGiverReceiverType() );
    Map<String, Object> output = getRecognitionReportsService().getRecognitionsGivenParticipationRateByPaxResults( reportParameters );
    List<RecognitionsGivenParticipationRateByPaxValue> reportData = (List<RecognitionsGivenParticipationRateByPaxValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_recognitions_given_participation_rate_bypax_piechart" );
  }

  @Override
  protected String getGiverReceiverType()
  {
    return GiverReceiverType.GIVER;
  }
}
