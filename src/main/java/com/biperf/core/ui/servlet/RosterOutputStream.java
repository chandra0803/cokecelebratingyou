
package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class RosterOutputStream extends ServletOutputStream
{
  private ServletOutputStream originalOutputStream = null;

  public RosterOutputStream( final ServletOutputStream originalOutputStream )
  {
    this.originalOutputStream = originalOutputStream;
  }

  @Override
  public void write( int b ) throws IOException
  {
    originalOutputStream.write( b );
  }

  public ServletOutputStream getOriginalOutputStream()
  {
    return originalOutputStream;
  }

  @Override
  public boolean isReady()
  {
    return false;
  }

  @Override
  public void setWriteListener( WriteListener arg0 )
  {
    // no write listener registered
  }
}
