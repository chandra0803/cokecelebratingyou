
package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.BeanLocator;

public class PurlImageRenderServlet extends HttpServlet
{
  private static final Log logger = LogFactory.getLog( PurlImageRenderServlet.class );

  public void init( ServletConfig servletConfig ) throws ServletException
  {
    super.init( servletConfig );
  }

  protected void doGet( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse ) throws ServletException, IOException
  {
    byte[] data = null;

    ServletContext cnx = getServletContext();
    String imageUrl = httpServletRequest.getParameter( "imageUrl" );
    String filename = cnx.getRealPath( imageUrl );
    String imageContentType = cnx.getMimeType( filename );

    if ( null != imageUrl )
    {
      try
      {
        data = getAppDataDirFileUploadStrategy().getFileData( imageUrl );
      }
      catch( ServiceErrorException e )
      {
        logger.error( "Error getting image data from AppDataDir, Cause: " + e.getMessage() );
        try
        {
          data = getWebdavFileUploadStrategy().getFileData( imageUrl );
        }
        catch( ServiceErrorException e1 )
        {
          logger.error( "Error getting image data from WebDav, Cause: " + e1.getMessage() );
        }
      }
    }

    // flush it in the response
    httpServletResponse.setHeader( "Cache-Control", "no-store" );
    httpServletResponse.setHeader( "Pragma", "no-cache" );
    httpServletResponse.setDateHeader( "Expires", 0 );
    httpServletResponse.setContentType( imageContentType );
    ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
    responseOutputStream.write( data );
    responseOutputStream.flush();
    responseOutputStream.close();
  }

  private static FileUploadStrategy getAppDataDirFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.APPDATADIR );
  }

  private static FileUploadStrategy getWebdavFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.WEBDAV );
  }

}
