
package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class RosterResponseWrapper extends HttpServletResponseWrapper
{

  private RosterOutputStream stream = null;

  public RosterResponseWrapper( HttpServletResponse response )
  {
    super( response );
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException
  {
    if ( Objects.isNull( stream ) )
    {
      stream = new RosterOutputStream( super.getOutputStream() );
    }
    return stream;
  }

}
