
package com.biperf.core.service.export.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.export.ExportData;
import com.biperf.core.service.export.ExportService;
import com.biperf.core.utils.transcoder.TranscoderType;

public class ExportServiceImpl implements ExportService
{
  @Override
  /*
   * Note, it's up to the client to close/release output streams. The input stream is of no use once
   * the output stream is written to - so that is expicitly closed here
   */
  public void export( ExportData exportData )
  {
    Transcoder transcoder = null;

    if ( TranscoderType.PDF == exportData.getExportType() )
    {
      transcoder = new PDFTranscoder();
    }
    else
    {
      throw new IllegalArgumentException( "'" + exportData.getExportType() + "' is not a supported Transcoder" );
    }

    InputStream is = null;
    try
    {
      if ( null != exportData.getSvgContent() )
      {
        // hack due to HighCharts producing crummy SVG content
        // exportData.setSvgContent( SVGUtil.getValidSVG( exportData.getSvgContent() ) ) ;
        // exportData.setSvgContent( exportData.getSvgContent() );
        is = new ByteArrayInputStream( exportData.getSvgContent().getBytes() );
      }
      else
      {
        is = exportData.getInputStream();
      }

      TranscoderOutput output = new TranscoderOutput( exportData.getOutputStream() );
      TranscoderInput input = new TranscoderInput( is );
      transcoder.transcode( input, output );
    }
    catch( Exception e )
    {
      throw new BeaconRuntimeException( e.getMessage(), e );
    }
    finally
    { // ugghh..can we use the
      if ( null != is )
      {
        try
        {
          is.close();
        }
        catch( IOException e )
        {
          is = null;
        }
      }
    }
  }
}
