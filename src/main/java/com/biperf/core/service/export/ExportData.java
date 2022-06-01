
package com.biperf.core.service.export;

import java.io.InputStream;
import java.io.OutputStream;

import com.biperf.core.utils.transcoder.TranscoderType;

public class ExportData
{
  private String svgContent = null;
  private transient InputStream inputStream = null;
  private transient OutputStream outputStream = null;
  private TranscoderType exportType = TranscoderType.PDF;

  public ExportData( String svgContent, InputStream inputStream, OutputStream outputStream, TranscoderType exportType )
  {
    this.svgContent = svgContent;
    this.inputStream = inputStream;
    this.outputStream = outputStream;

    if ( null != exportType )
    {
      this.exportType = exportType;
    }
  }

  public ExportData( String svgContent, OutputStream outputStream, TranscoderType exportType )
  {
    this( svgContent, null, outputStream, exportType );
  }

  public ExportData( String svgContent, OutputStream outputStream )
  {
    this( svgContent, null, outputStream, null );
  }

  public String getSvgContent()
  {
    return svgContent;
  }

  public void setSvgContent( String svgContent )
  {
    this.svgContent = svgContent;
  }

  public InputStream getInputStream()
  {
    return inputStream;
  }

  public void setInputStream( InputStream inputStream )
  {
    this.inputStream = inputStream;
  }

  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  public void setOutputStream( OutputStream outputStream )
  {
    this.outputStream = outputStream;
  }

  public TranscoderType getExportType()
  {
    return exportType;
  }

  public void setExportType( TranscoderType exportType )
  {
    this.exportType = exportType;
  }

}
