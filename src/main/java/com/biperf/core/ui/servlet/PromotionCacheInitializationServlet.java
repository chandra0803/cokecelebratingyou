/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/PromotionCacheInitializationServlet.java,v $
 */

package com.biperf.core.ui.servlet;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.utils.ApplicationContextFactory;
import com.objectpartners.cms.util.BeanLocator;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * InitializationServlet.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>kumars</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class PromotionCacheInitializationServlet extends CMSAwareBaseServlet
{
  private static Log log = LogFactory.getLog( PromotionCacheInitializationServlet.class );

  /**
   * Beacon system startup
   * 
   * @see javax.servlet.GenericServlet#init()
   */
  public void init() throws ServletException
  {
    try
    {
      super.init();
      buildAvailablePromotionCache();
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }
    finally
    {
      ContentReaderManager.removeContentReader();
    }
  }

  private void buildAvailablePromotionCache()
  {
    log.info( "InitializationServlet.buildAvailablePromotionCache: Starting" );
    long start = System.currentTimeMillis();
    try
    {
      BeanLocator.setApplicationContext( ApplicationContextFactory.getApplicationContext() );
      ( (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME ) ).refreshAllLivePromotionCache();
    }
    catch( Exception e )
    {
      log.error( "InitializationServlet.buildAvailablePromotionCache: Error " + e.getMessage(), e );
    }
    log.info( "InitializationServlet.buildAvailablePromotionCache: Done (" + ( System.currentTimeMillis() - start ) + ")" );
  }
}
