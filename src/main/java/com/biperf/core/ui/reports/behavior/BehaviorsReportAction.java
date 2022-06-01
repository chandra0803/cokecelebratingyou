
package com.biperf.core.ui.reports.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.BehaviorReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.behavior.BehaviorsReportValue;
import com.biperf.util.StringUtils;

/**
 * Action class for Behaviors report
 */
public class BehaviorsReportAction extends BaseReportsAction
{

  protected static final String[] EXTRACT_ALL_COLUMN_NAMES = { "DATE_SUBMITTED",
                                                               "PROMOTION_NAME",
                                                               "BEHAVIOR",
                                                               "RECIPIENT_LOGIN_ID",
                                                               "RECIPIENT_FIRST_NAME",
                                                               "RECIPIENT_MIDDLE_NAME",
                                                               "RECIPIENT_LAST_NAME",
                                                               "RECIPIENT_COUNTRY",
                                                               "RECIPIENT_PRIMARY_ORG_UNIT",
                                                               "RECIPIENT_DEPARTMENT",
                                                               "RECIPIENT_JOB_POSITION",
                                                               "GIVER_LOGIN_ID",
                                                               "GIVER_FIRST_NAME",
                                                               "GIVER_MIDDLE_NAME",
                                                               "GIVER_LAST_NAME",
                                                               "DELEGATE_FIRST_NAME",
                                                               "DELEGATE_LAST_NAME",
                                                               "GIVER_COUNTRY",
                                                               "GIVER_PRIMARY_ORG_UNIT",
                                                               "GIVER_DEPARTMENT",
                                                               "GIVER_JOB_POSITION",
                                                               "PAX_CHAR1",
                                                               "PAX_CHAR2",
                                                               "PAX_CHAR3",
                                                               "PAX_CHAR4",
                                                               "PAX_CHAR5",
                                                               "PAX_CHAR6",
                                                               "PAX_CHAR7",
                                                               "PAX_CHAR8",
                                                               "PAX_CHAR9",
                                                               "PAX_CHAR10",
                                                               "PAX_CHAR11",
                                                               "PAX_CHAR12",
                                                               "PAX_CHAR13",
                                                               "PAX_CHAR14",
                                                               "PAX_CHAR15",
                                                               "PAX_CHAR16",
                                                               "PAX_CHAR17",
                                                               "PAX_CHAR18",
                                                               "PAX_CHAR19",
                                                               "PAX_CHAR20" };

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportCode()
  {
    return Report.BEHAVIORS;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.behavior.extract";
  }

  protected String getGiverReceiverType()
  {
    return "";
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    List<String> requiredFields = new ArrayList<>();
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, false ) )
    {
      return mapping.findForward( "display_behavior_summary" );
    }

    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) ) || isDashboardPage( request ) )
    {
      Map<String, Object> output = getBehaviorReportsService().getBehaviorsTabularResults( reportParameters );
      List<BehaviorsReportValue> reportData = (List<BehaviorsReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      List<Node> childNodeList = new ArrayList<Node>();
      for ( BehaviorsReportValue reportItem : reportData )
      {
        Node childNode = new Node( reportItem.getDetailNodeId() );
        childNode.setName( reportItem.getNodeName() );
        if ( !childNodeList.contains( childNode ) )
        {
          childNodeList.add( childNode );
        }
      }
      request.setAttribute( "childNodeList", childNodeList );

      if ( isLastPage( maxRows.intValue(), reportParameters ) )
      {
        List<BehaviorsReportValue> totalsRowData = (List<BehaviorsReportValue>)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }

    return mapping.findForward( "display_behavior_summary" );
  }

  public ActionForward displayBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getBehaviorReportsService().getBehaviorsBarchartResults( reportParameters );
    List<BehaviorsReportValue> reportData = (List<BehaviorsReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    if ( reportParameters.get( "promotionId" ) != null )
    {
      List<String> promotionBehaviorTypeList = getBehaviorReportsService().getBehaviorsByPromo( (String)reportParameters.get( "promotionId" ), (String)reportParameters.get( "languageCode" ) );
      if ( promotionBehaviorTypeList.size() > 0 )
      {
        Collections.sort( promotionBehaviorTypeList, new Comparator<String>()

        {
          @Override
          public int compare( String s1, String s2 )
          {
            return s1.compareToIgnoreCase( s2 );
          }
        } );
      }
      request.setAttribute( "promotionBehaviorTypeList", promotionBehaviorTypeList );
    }

    Map<String, Long> reportTotalsMap = createReportTotalsMap( reportData );
    request.setAttribute( "reportTotals", reportTotalsMap );

    return mapping.findForward( "display_behavior_barchart_report" );
  }

  public ActionForward displayPiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );

    Map<String, Object> output = getBehaviorReportsService().getBehaviorsPiechartResults( reportParameters );
    List<BehaviorsReportValue> reportData = (List<BehaviorsReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    boolean isZeroBehaviorsCnt = false;

    for ( BehaviorsReportValue behaviorsReportValue : reportData )
    {
      if ( behaviorsReportValue.getbCnt() > 0 )
      {
        isZeroBehaviorsCnt = false;
        break;
      }
      isZeroBehaviorsCnt = true;
    }

    if ( !isZeroBehaviorsCnt )
    {
      if ( reportParameters.get( "promotionId" ) != null )
      {
        List<String> promotionBehaviorTypeList = getBehaviorReportsService().getBehaviorsByPromo( (String)reportParameters.get( "promotionId" ), (String)reportParameters.get( "languageCode" ) );
        if ( promotionBehaviorTypeList.size() > 0 )
        {
          Collections.sort( promotionBehaviorTypeList, new Comparator<String>()

          {
            @Override
            public int compare( String s1, String s2 )
            {
              return s1.compareToIgnoreCase( s2 );
            }
          } );
        }
      }

      Map<String, Long> reportTotalsMap = createReportTotalsMap( reportData );
      request.setAttribute( "reportTotals", reportTotalsMap );
    }

    return mapping.findForward( "display_behavior_piechart_report" );
  }

  private Map<String, Long> createReportTotalsMap( List<BehaviorsReportValue> reportData )
  {
    Map<String, Long> reportTotalsMap = new HashMap<String, Long>();
    for ( BehaviorsReportValue reportValue : reportData )
    {
      if ( !reportTotalsMap.containsKey( reportValue.getBehavior() ) )
      {
        reportTotalsMap.put( reportValue.getBehavior(), 0L );
      }

      reportTotalsMap.put( reportValue.getBehavior(), reportTotalsMap.get( reportValue.getBehavior() ) + reportValue.getbCnt() );
    }

    return reportTotalsMap;
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();

    Map reportExtractData = getBehaviorReportsService().getBehaviorReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.BEHAVIOR_REPORT;
  }

  protected BehaviorReportsService getBehaviorReportsService()
  {
    return (BehaviorReportsService)getService( BehaviorReportsService.BEAN_NAME );
  }
}
