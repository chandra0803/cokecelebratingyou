
package com.biperf.core.ui.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.ujac.util.io.ResourceLoader;

public class CustomUjacHttpResourceLoader implements ResourceLoader
{
  private String urlRoot = null;
  private Proxy proxy = null;

  public CustomUjacHttpResourceLoader()
  {
  }

  public CustomUjacHttpResourceLoader( Proxy proxy, String urlRoot )
  {
    this.proxy = proxy;
    this.urlRoot = urlRoot;
  }

  public CustomUjacHttpResourceLoader( String urlRoot )
  {
    this.urlRoot = urlRoot;
  }

  public byte[] loadResource( String location ) throws IOException
  {
    if ( location == null || location.length() == 0 )
    {
      throw new IOException( "The given resource location must not be null and non empty." );
    }

    URL url = buildURL( location );

    URLConnection cxn = proxy != null ? url.openConnection( proxy ) : url.openConnection();
    InputStream is = null;
    try
    {
      byte[] byteBuffer = new byte[2048];
      ByteArrayOutputStream bos = new ByteArrayOutputStream( 2048 );
      is = cxn.getInputStream();
      int bytesRead = 0;
      while ( ( bytesRead = is.read( byteBuffer, 0, 2048 ) ) >= 0 )
      {
        bos.write( byteBuffer, 0, bytesRead );
      }
      return bos.toByteArray();
    }
    finally
    {
      if ( is != null )
      {
        is.close();
      }
    }
  }

  public boolean resourceExists( String location )
  {
    if ( location == null || location.length() == 0 )
    {
      return false;
    }
    try
    {
      URL url = buildURL( location );

      URLConnection cxn = proxy != null ? url.openConnection( proxy ) : url.openConnection();
      InputStream is = null;
      try
      {
        is = cxn.getInputStream();
        return true;
      }
      finally
      {
        if ( is != null )
        {
          is.close();
        }
      }
    }
    catch( IOException ex )
    {
    }
    return false;
  }

  private URL buildURL( String location ) throws MalformedURLException
  {
    URL url = null;
    if ( this.urlRoot == null )
    {
      url = new URL( location );
    }
    else
    {
      int firstColonIdx = location.indexOf( ':' );
      int firstSlashIdx = location.indexOf( '/' );
      if ( firstColonIdx > 0 && ( firstSlashIdx < 0 || firstColonIdx < firstSlashIdx ) )
      {
        url = new URL( location );
      }
      else if ( this.urlRoot.endsWith( "/" ) )
      {
        url = new URL( this.urlRoot + location );
      }
      else
      {
        url = new URL( this.urlRoot + "/" + location );
      }
    }
    return url;
  }
}
