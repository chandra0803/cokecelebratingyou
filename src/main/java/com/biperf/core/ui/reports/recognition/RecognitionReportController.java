/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/recognition/RecognitionReportController.java,v $
 *
 */

package com.biperf.core.ui.reports.recognition;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.reports.RecognitionReportsService;
import com.biperf.core.ui.reports.ReportController;

/**
 * RecognitionReportController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 15, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public class RecognitionReportController extends ReportController
{
  @SuppressWarnings( "unused" )
  private static final Log LOG = LogFactory.getLog( RecognitionReportController.class );

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

  protected RecognitionReportsService getRecognitionReportsService()
  {
    return (RecognitionReportsService)getService( RecognitionReportsService.BEAN_NAME );
  }

}
