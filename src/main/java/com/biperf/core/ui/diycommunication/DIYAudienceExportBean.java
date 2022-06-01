
package com.biperf.core.ui.diycommunication;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.value.diycommunication.ParticipantList;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class DIYAudienceExportBean
{
  private static final Log logger = LogFactory.getLog( DIYAudienceExportBean.class );

  private List<ParticipantList> exportList;

  protected String buildCsvFileName()
  {
    return "diy_audiences.csv";
  }

  protected String buildCsvHeader()
  {
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "diyCommunications.common.labels" ) );

    StringBuilder csvHeader = new StringBuilder();
    csvHeader.append( content.getContentDataMap().get( "FIRST_NAME" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "LAST_NAME" ) );

    return csvHeader.toString();
  }

  protected String buildCsvRow( ParticipantList paxList )
  {
    StringBuffer csvRow = new StringBuffer();

    String[] nameList = paxList.getName().split( " " );
    csvRow.append( "\"" );
    csvRow.append( nameList[0] );
    csvRow.append( "\",\"" );
    csvRow.append( nameList[1] );
    csvRow.append( "\"" );

    return csvRow.toString();
  }

  public void extractAsCsv( HttpServletResponse response )
  {
    try
    {
      prepareHeader( response, buildCsvFileName(), "csv" );
      OutputStream output = response.getOutputStream();

      StringBuilder sBuf = new StringBuilder();

      // output.write( DELIMITER_HEADER.getBytes() );
      if ( !exportList.isEmpty() )
      {
        sBuf.append( buildCsvHeader() );
      }

      sBuf.append( "\n" );

      for ( ParticipantList paxList : exportList )
      {
        sBuf.append( buildCsvRow( paxList ) );
        sBuf.append( "\n" );
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

  protected final void prepareHeader( HttpServletResponse response, String filename, String fileType )
  {
    response.setContentType( "application/ms-excel; charset=UTF-8" );
    response.setCharacterEncoding( "UTF-8" );

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

  public List<ParticipantList> getExportList()
  {
    return exportList;
  }

  public void setExportList( List<ParticipantList> exportList )
  {
    this.exportList = exportList;
  }

}
