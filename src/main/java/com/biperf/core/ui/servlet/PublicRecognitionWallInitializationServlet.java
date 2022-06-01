/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/PromotionCacheInitializationServlet.java,v $
 */

package com.biperf.core.ui.servlet;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.publicrecognitionwall.PublicRecognitionWallService;
import com.objectpartners.cms.util.BeanLocator;
import com.objectpartners.cms.util.ContentReaderManager;

@SuppressWarnings( "serial" )
public class PublicRecognitionWallInitializationServlet extends CMSAwareBaseServlet
{
  private static Log log = LogFactory.getLog( PublicRecognitionWallInitializationServlet.class );

  public void init() throws ServletException
  {
    try
    {
      super.init();
      PublicRecognitionWallService publicRecognitionWallService = getPublicRecognitionWallService();
      if ( publicRecognitionWallService.isPublicRecognitionWallFeedEnabled() )
      {
        publicRecognitionWallService.refresh();
      }
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

  private PublicRecognitionWallService getPublicRecognitionWallService()
  {
    return (PublicRecognitionWallService)BeanLocator.getBean( PublicRecognitionWallService.BEAN_NAME );
  }
}
