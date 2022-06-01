/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/CmsContentReaderFilter.java,v $
 *
 */

package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.properties.TableProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.biperf.core.utils.ApplicationContextFactory;
import com.objectpartners.cms.aop.HibernateLogChangeInterceptor;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.ContentReaderFactory;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * CmsContentReaderFilter <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Aug 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CmsContentReaderFilter implements Filter
{
  private static final String CM_WAR_CONTEXT_PATH_SUFFIX = "-cm";

  private ApplicationContext cmsAppContext = null;
  private ContentReaderFactory readerFactory = null;
  
  private boolean init = false;
  
  /**
   * @param filterConfig
   * @throws javax.servlet.ServletException
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  @Override
  public void init( FilterConfig filterConfig ) throws ServletException
  {
  }

  /**
   * Create the contentReader and store in the the ContentReaderManager.
   * 
   * @param servletRequest ServletRequest
   * @param servletResponse ServletResponse
   * @param filterChain FilterChain
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
  {
    
    boolean exceptionThrown = false;
    try
    {
      HttpServletRequest request = (HttpServletRequest)servletRequest;
      
      /*
       * This should really only be done once, previous code was doing this for every user, every request...unfortunately, we can't do this in the 
       * init() method because this class is initialized prior to the InitiliazationServlet.init() which starts up the Spring configuration
       */
      if ( !init )
      {
        cmsAppContext = (ApplicationContext)request.getServletContext().getAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE );
        readerFactory = (ContentReaderFactory)ApplicationContextFactory.getApplicationContext().getBean( "cmsContentReaderFactory" );
        // set the cmsConfiguration bean on the performanceMonitorInterceptor. We need to do this
        // since cms does not know about beacon beans.
        CmsConfiguration cmsConfig = (CmsConfiguration)ApplicationContextFactory.getApplicationContext().getBean( "cmsConfiguration" );
        ( (HibernateLogChangeInterceptor)cmsAppContext.getBean( "hibernateLogChangeInterceptor" ) ).setCmsConfiguration( cmsConfig );

        init = true;
      }
      
      // create the ThreadLocal ContentReader for this user
      buildContentReader( request );   
      /*
       * Both g5 and cm5 uses display tag and displaytag.properties is part of g5 and cm5's
       * displaytag.properties is removed to avoid conflict. This introduces a bug. After server
       * restart, if first display table loaded is from CM5 application, subsequent G5 display table
       * header will be missing unless server is restarted and g5 loads first display table. It also
       * mess up display table of CM also since adapter mentioned in g5's displaytag.properties is
       * not available as part of CM war. Below is the hook resolve this class loader issue. This
       * will ensure that property is always loaded from G5 WAR class loader and it can get adapter
       * instantiated for both locale resolver and locale provider.
       */
      TableProperties instance = TableProperties.getInstance( request );
      instance.geResourceProvider();
     
      if ( !StringUtils.isEmpty( request.getParameter( "changeLanguageForwardUrl" ) ) )
      {
        // Bug 50810 - Reload the same page with the changed language if user is not logged in
        request.getRequestDispatcher( getChanageLanguageForwardUrl( request ) ).forward( servletRequest, servletResponse );
      }
      else
      {
        filterChain.doFilter( servletRequest, servletResponse );
      }

    }
    catch( CmsServiceException e )
    {
      e.printStackTrace();
      exceptionThrown = true;
      throw new ServletException( e.getMessage(), e );
    }
    catch( BeansException e )
    {
      e.printStackTrace();
      exceptionThrown = true;
      throw new ServletException( e.getMessage(), e );
    }
    finally
    {
      try
      {
        // we need to do this always...
        cleanup();
      }
      catch( IllegalStateException ise )
      {
        if ( !exceptionThrown )
        {
          throw new ServletException( ise.getMessage(), ise );
        }
        // ignore this so we don't lose any original exception
      }
    }
  }

  private String getChanageLanguageForwardUrl( HttpServletRequest request )
  {
    String queryString = request.getQueryString();
    return queryString.substring( queryString.indexOf( "changeLanguageForwardUrl=" ) + 25 );
  }

  protected void buildContentReader( HttpServletRequest request ) throws CmsServiceException
  {
    ContentReader reader = readerFactory.createContentReader( request );
    reader.setApplicationContext( cmsAppContext );
    ContentReaderManager.setContentReader( reader );
  }
  
  protected void cleanup()
  {
    ContentReaderManager.removeContentReader();
  }
  
  /**
   * @see javax.servlet.Filter#destroy()
   */
  @Override
  public void destroy()
  {
    // empty
  }

}
