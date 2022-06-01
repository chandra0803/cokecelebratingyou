
package com.biperf.core.ui.reports;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.export.ExportData;
import com.biperf.core.service.export.ExportService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.transcoder.TranscoderType;

/**
 * FusionChartExportAction.
 */
public class FusionChartExportAction extends BaseDispatchAction
{
  private static final Log LOG = LogFactory.getLog( FusionChartExportAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      response.setContentType( "application/pdf" );
      getExportService().export( buildExportData( request, response ) );
    }
    catch( Exception e )
    {
      LOG.error( e.getMessage(), e );
    }
    return null;
  }

  private ExportData buildExportData( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    // String svg = request.getParameter( "svg" ) ;
    String svg = request.getParameter( "stream" );
    ExportData data = new ExportData( svg, response.getOutputStream(), TranscoderType.PDF );
    return data;
  }

  protected ExportService getExportService()
  {
    return (ExportService)getService( ExportService.BEAN_NAME );
  }
}
