
package com.biperf.core.ui.reports.behavior;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.reports.BehaviorReportsService;
import com.biperf.core.ui.reports.ReportController;
import com.biperf.core.ui.reports.ReportParametersForm;

/**
 * BehaviorReportController
 */
public class BehaviorReportController extends ReportController
{
  @SuppressWarnings( "unused" )
  private static final Log LOG = LogFactory.getLog( BehaviorReportController.class );

  /**
   * Fetches generic data for Report Display pages. 
   * Set the list of behavior types
   */
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    super.onExecute( tileContext, request, response, servletContext );
    // request.setAttribute( "promotionRecognitionBehaviorTypeList",
    // PromoRecognitionBehaviorType.getList() );
    ReportParametersForm reportParametersForm = (ReportParametersForm)request.getSession().getAttribute( "reportParametersForm" );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    String promotionId = reportParameters.get( "promotionId" ) == null ? null : (String)reportParameters.get( "promotionId" );
    List<String> promotionBehaviorTypeList = getBehaviorReportsService().getBehaviorsByPromo( promotionId, (String)reportParameters.get( "languageCode" ) );
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

  protected BehaviorReportsService getBehaviorReportsService()
  {
    return (BehaviorReportsService)getService( BehaviorReportsService.BEAN_NAME );
  }

}
