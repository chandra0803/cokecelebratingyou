
package com.biperf.core.ui;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ujac.print.DocumentPrinter;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.claim.RecognitionDetailBean;
import com.biperf.core.ui.utils.CustomUjacHttpResourceLoader;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;

public abstract class BaseRecognitionsExportBean<T>
{
  private static final Log logger = LogFactory.getLog( BaseRecognitionsExportBean.class );

  private List<T> exportList;
  private List<Approvable> exportPdfList;
  private List<AbstractRecognitionClaim> exportPdfListRecognition;
  private NodeService nodeService;
  private String submissionStartDate;
  private String submissionEndDate;

  protected abstract String buildPdfFileName();

  protected abstract String buildXMLStringRecognition( List approvables, String siteUrl );

  DateFormat originalFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
  DateFormat targetFormat = new SimpleDateFormat( "MMMM dd YYYY" );

  public void extractAsPdf( List<RecognitionDetailBean> recognitions, HttpServletResponse response, String type )
  {
    String xmlString = "";
    String outputFileName = buildPdfFileName();
    Reader reader = null;
    InputStream templateStream = null;
    try
    {
      prepareHeader( response, outputFileName, "pdf" );

      String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      xmlString = buildXMLStringRecognition( recognitions, siteUrl );

      logger.debug( "*********XML for pdf export approvals:" + xmlString );

      templateStream = new ByteArrayInputStream( xmlString.getBytes() );

      Map documentProperties = new HashMap();

      // instantiating the document printer
      reader = new InputStreamReader( templateStream, "UTF-8" );
      DocumentPrinter documentPrinter = new DocumentPrinter( reader, documentProperties );
      documentPrinter.setXmlReaderClass( "org.apache.xerces.parsers.SAXParser" );
      FileOutputStream pdfStream = null;
      FileInputStream fis = null;
      try
      {
        if ( Environment.isCtech() )
        {
          documentPrinter.setResourceLoader( new CustomUjacHttpResourceLoader( Environment.buildProxy(), siteUrl ) );
        }
        else
        {
          documentPrinter.setResourceLoader( new CustomUjacHttpResourceLoader( siteUrl ) );
        }
        pdfStream = new FileOutputStream( Environment.getTmpDir() + "/" + outputFileName );
        documentPrinter.printDocument( pdfStream );

        fis = new FileInputStream( Environment.getTmpDir() + "/" + outputFileName );
        byte[] b;
        int x = fis.available();
        b = new byte[x];
        fis.read( b );

        OutputStream os = response.getOutputStream();
        os.write( b );
        os.flush();
      }
      catch( Exception e )
      {
        logger.error( e.getMessage(), e );
      }
      finally
      {
        if ( pdfStream != null )
        {
          pdfStream.close();
        }
        if ( fis != null )
        {
          fis.close();
        }
      }
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
    finally
    {
      try
      {
        if ( templateStream != null )
        {
          templateStream.close();
        }
        if ( reader != null )
        {
          reader.close();
        }
      }
      catch( IOException e )
      {
        logger.error( "Error while closing streams while writing PDF file:", e );
      }
    }
  }

  protected final void prepareHeader( HttpServletResponse response, String filename, String fileType )
  {
    // Use the correct CSV Content-Type
    if ( fileType.equalsIgnoreCase( "csv" ) )
    {
      response.setContentType( "application/ms-excel" );
    }
    else if ( fileType.equalsIgnoreCase( "pdf" ) )
    {
      response.setContentType( "application/pdf" );
    }

    // Begin writing headers
    response.setHeader( "Pragma", "public" );
    response.setHeader( "Expires", "0" );
    response.setHeader( "Cache-Control", "no-store, no-cache" );
    response.setHeader( "Cache-Control", "must-revalidate, post-check=0, pre-check=0" );
    response.setHeader( "Cache-Control", "public" );
    response.setHeader( "Content-Description", "File Transfer" );

    // Force the download
    response.setHeader( "Content-Disposition", "attachment; filename=" + filename );
    response.setHeader( "Content-Transfer-Encoding", "binary" );
  }

  protected String buildUserDisplayString( Collection<User> users )
  {
    StringBuilder userDisplayString = new StringBuilder();

    for ( Iterator<User> iter = users.iterator(); iter.hasNext(); )
    {
      User user = iter.next();
      userDisplayString.append( user.getNameFLNoComma() );
      if ( iter.hasNext() )
      {
        userDisplayString.append( ", " );
      }
    }
    return userDisplayString.toString();
  }

  protected StringBuilder buildTableRowsPdf( StringBuilder xmlString, Map<String, String> paxdetailMap )
  {
    if ( paxdetailMap != null )
    {
      for ( String key : paxdetailMap.keySet() )
      {
        xmlString.append( "<table-row>" );
        xmlString.append( "<cell halign=\"right\" valign=\"middle\">" + key + "</cell>" );
        xmlString.append( "<cell halign=\"right\" valign=\"middle\">" + paxdetailMap.get( key ) + "</cell>" );
        xmlString.append( "</table-row>" );
      }
    }
    return xmlString;
  }

  public void setSubmissionStartDate( String submissionStartDate )
  {
    this.submissionStartDate = submissionStartDate;
  }

  public void setSubmissionEndDate( String submissionEndDate )
  {
    this.submissionEndDate = submissionEndDate;
  }

  public void setExportList( List<T> exportList )
  {
    this.exportList = exportList;
  }

  public List<T> getExportList()
  {
    return exportList;
  }

  public NodeService getNodeService()
  {
    return nodeService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public List<Approvable> getExportPdfList()
  {
    return exportPdfList;
  }

  public void setExportPdfList( List<Approvable> exportPdfList )
  {
    this.exportPdfList = exportPdfList;
  }

  public List<AbstractRecognitionClaim> getExportPdfListRecognition()
  {
    return exportPdfListRecognition;
  }

  public void setExportPdfListRecognition( List<AbstractRecognitionClaim> exportPdfListRecognition )
  {
    this.exportPdfListRecognition = exportPdfListRecognition;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
}
