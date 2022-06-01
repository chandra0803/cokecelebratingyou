/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/BaseExportBean.java,v $
 */

package com.biperf.core.ui;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ujac.print.DocumentPrinter;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.utils.CustomUjacHttpResourceLoader;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;

public abstract class BaseExportBean<T>
{
  private static final Log logger = LogFactory.getLog( BaseExportBean.class );

  private List<T> exportList;
  private List<Approvable> exportPdfList;
  private List<AbstractRecognitionClaim> exportPdfListRecognition;
  private NodeService nodeService;
  private String submissionStartDate;
  private String submissionEndDate;

  protected abstract String buildCsvFileName();

  protected abstract String buildPdfFileName();

  protected abstract String buildCsvHeader( Promotion promotion, T exportItem );

  protected abstract String buildCsvRow( T exportItem );

  protected abstract String buildXMLString( Promotion promotion, List<Approvable> approvables );

  protected abstract String buildXMLStringRecognition( List<AbstractRecognitionClaim> approvables );

  DateFormat originalFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
  DateFormat targetFormat = new SimpleDateFormat( "MMMM dd YYYY" );

  public void extractAsCsv( Promotion promotion, HttpServletResponse response )
  {
    boolean isTeamNomination = false;
    try
    {
      prepareHeader( response, buildCsvFileName(), "csv" );
      OutputStream output = response.getOutputStream();

      StringBuilder sBuf = new StringBuilder();

      // output.write( DELIMITER_HEADER.getBytes() );
      if ( !exportList.isEmpty() )
      {
        T exportItem = exportList.get( 0 );
        sBuf.append( buildCsvHeader( promotion, exportItem ) );
      }

      sBuf.append( "\n" );

      for ( T exportItem : exportList )
      {
        sBuf.append( buildCsvRow( exportItem ) );
        if ( exportItem instanceof NominationClaim )
        {
          NominationClaim nomClaim = (NominationClaim)exportItem;
          if ( nomClaim.isTeam() )
          {
            isTeamNomination = true;
          }

        }
        if ( !isTeamNomination )
        {
          sBuf.append( "\n" );
        }
      }
      // Added for bug 51356 - Handle special characters
      output.write( new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF } );
      output.write( sBuf.toString().getBytes( Charset.forName( "UTF-8" ) ) );
      output.close();
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
  }

  public void extractAsPdf( Promotion promotion, HttpServletResponse response, String type )
  {
    String xmlString = "";
    String outputFileName = "";
    try
    {
      prepareHeader( response, buildPdfFileName(), "pdf" );
      outputFileName = buildPdfFileName();

      String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      if ( type.equalsIgnoreCase( "nomination" ) )
      {
        xmlString = buildXMLString( promotion, exportPdfList );
      }
      else
      {
        xmlString = buildXMLStringRecognition( exportPdfListRecognition );
      }

      logger.debug( "*********XML for pdf export approvals:" + xmlString );

      InputStream templateStream = new ByteArrayInputStream( xmlString.getBytes() );

      Map documentProperties = new HashMap();

      // instantiating the document printer
      DocumentPrinter documentPrinter = new DocumentPrinter( templateStream, documentProperties );
      documentPrinter.setXmlReaderClass( "org.apache.xerces.parsers.SAXParser" );
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
        FileOutputStream pdfStream = new FileOutputStream( Environment.getTmpDir() + "/" + outputFileName );
        documentPrinter.printDocument( pdfStream );

        FileInputStream fis = new FileInputStream( Environment.getTmpDir() + "/" + outputFileName );
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
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }

  }

  protected final void prepareHeader( HttpServletResponse response, String filename, String fileType )
  {
    // Use the correct CSV Content-Type
    if ( fileType.equalsIgnoreCase( "csv" ) )
    {
      response.setContentType( "application/ms-excel; charset=UTF-8" );
      response.setCharacterEncoding( "UTF-8" );
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
      userDisplayString.append( user.getNameLFMWithComma() );
      if ( iter.hasNext() )
      {
        userDisplayString.append( "; " );
      }
    }

    return userDisplayString.toString();
  }

  protected StringBuilder buildTableRowsPdf( StringBuilder xmlString, Map<String, String> paxdetailMap )
  {
    for ( String key : paxdetailMap.keySet() )
    {
      xmlString.append( "<table-row>" );
      xmlString.append( "<cell valign=\"top\">" + key + "</cell>" );
      xmlString.append( "<cell valign=\"top\">" + paxdetailMap.get( key ) + "</cell>" );
      xmlString.append( "</table-row>" );
    }

    return xmlString;
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

  public String getSubmissionStartDate()
  {
    String formattedStartDate = "";
    if ( !StringUtils.isEmpty( submissionStartDate ) )
    {
      try
      {
        Date dateStart = originalFormat.parse( submissionStartDate );
        formattedStartDate = targetFormat.format( dateStart );
      }
      catch( Exception e )
      {
        logger.error( "Error while parsing the submission dates:" + e );
      }

    }
    return formattedStartDate;
  }

  public void setSubmissionStartDate( String submissionStartDate )
  {
    this.submissionStartDate = submissionStartDate;
  }

  public String getSubmissionEndDate()
  {
    String formattedEndDate = "";
    if ( !StringUtils.isEmpty( submissionEndDate ) )
    {
      try
      {
        Date dateEnd = originalFormat.parse( submissionEndDate );
        formattedEndDate = targetFormat.format( dateEnd );
      }
      catch( Exception e )
      {
        logger.error( "Error while parsing the submission dates:" + e );
      }

    }
    return formattedEndDate;
  }

  public void setSubmissionEndDate( String submissionEndDate )
  {
    this.submissionEndDate = submissionEndDate;
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
