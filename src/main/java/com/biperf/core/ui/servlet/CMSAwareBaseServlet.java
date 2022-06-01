
package com.biperf.core.ui.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.cms.ContentReaderUtils;
import com.objectpartners.cms.util.BeanLocator;

@SuppressWarnings( "serial" )
public class CMSAwareBaseServlet extends HttpServlet
{
  private static Log log = LogFactory.getLog( CMSAwareBaseServlet.class );

  public void init() throws ServletException
  {
    prepareContentReader();
    try
    {
      long start = System.currentTimeMillis();
      BeanLocator.setApplicationContext( ApplicationContextFactory.getApplicationContext() );

      if ( log.isInfoEnabled() )
      {
        log.info( "BaseServlet loaded: " + ( System.currentTimeMillis() - start ) );
      }
    }
    catch( Exception e )
    {
      // Do nothing
    }
  }

  public void prepareContentReader()
  {
    BeanLocator.setApplicationContext( ApplicationContextFactory.getApplicationContext() );
    ContentReaderUtils.prepareContentReader();
  }
}
