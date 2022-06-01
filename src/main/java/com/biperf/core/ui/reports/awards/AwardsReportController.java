/**
 * 
 */

package com.biperf.core.ui.reports.awards;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.reports.ReportController;

/**
 * @author poddutur
 *
 */
public class AwardsReportController extends ReportController
{
  @SuppressWarnings( "unused" )
  private static final Log LOG = LogFactory.getLog( AwardsReportController.class );

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
  }

}
