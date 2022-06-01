
package com.biperf.core.ui.reports.survey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.reports.SurveyReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.ReportController;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.survey.SurveyAnalysisHeaderListReportValue;

public class SurveyReportController extends ReportController
{
  @SuppressWarnings( "unused" )
  private static final Log LOG = LogFactory.getLog( SurveyReportController.class );

  /**
   * Fetches generic data for Report Display pages
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   * @throws Exception 
   */
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    super.onExecute( tileContext, request, response, servletContext );

    ReportParametersForm reportParametersForm = (ReportParametersForm)request.getSession().getAttribute( "reportParametersForm" );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( reportParameters.get( "promotionId" ) != null )
    {
      List<SurveyAnalysisHeaderListReportValue> surveyResponseList = new ArrayList<SurveyAnalysisHeaderListReportValue>();
      List<Long> surveyResponseSeqList = new ArrayList<Long>();

      Map<String, Object> output = getSurveyReportsService().getSurveyResponseList( reportParameters );
      surveyResponseList = (List<SurveyAnalysisHeaderListReportValue>)output.get( "p_out_data" );
      request.setAttribute( "surveyResponseList", surveyResponseList );
      request.setAttribute( "surveyResponseListSize", surveyResponseList.size() );

      Long index = new Long( 1 );
      Iterator promoIter = surveyResponseList.iterator();
      while ( promoIter.hasNext() )
      {
        SurveyAnalysisHeaderListReportValue reportValue = (SurveyAnalysisHeaderListReportValue)promoIter.next();
        request.setAttribute( "reportHeader" + index, reportValue.getHeaderValue() );
        surveyResponseSeqList.add( index );
        index++;
      }
      request.setAttribute( "response1", surveyResponseSeqList.contains( 1L ) );
      request.setAttribute( "response2", surveyResponseSeqList.contains( 2L ) );
      request.setAttribute( "response3", surveyResponseSeqList.contains( 3L ) );
      request.setAttribute( "response4", surveyResponseSeqList.contains( 4L ) );
      request.setAttribute( "response5", surveyResponseSeqList.contains( 5L ) );
      request.setAttribute( "response6", surveyResponseSeqList.contains( 6L ) );
      request.setAttribute( "response7", surveyResponseSeqList.contains( 7L ) );
      request.setAttribute( "response8", surveyResponseSeqList.contains( 8L ) );
      request.setAttribute( "response9", surveyResponseSeqList.contains( 9L ) );
      request.setAttribute( "response10", surveyResponseSeqList.contains( 10L ) );

      int surveyMinResponseCount = getSystemVariableService().getPropertyByName( SystemVariableService.SURVEY_REPORT_RESPONSE_COUNT ).getIntVal();
      request.setAttribute( "surveyMinResponse", surveyMinResponseCount );
    }
  }

  protected SurveyReportsService getSurveyReportsService()
  {
    return (SurveyReportsService)getService( SurveyReportsService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
