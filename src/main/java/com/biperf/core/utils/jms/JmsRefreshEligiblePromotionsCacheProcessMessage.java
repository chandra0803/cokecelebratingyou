
package com.biperf.core.utils.jms;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.cms.ContentReaderUtils;
import com.objectpartners.cms.util.ContentReaderManager;

public class JmsRefreshEligiblePromotionsCacheProcessMessage extends JmsMessage
{

  private static final long serialVersionUID = 1L;
  private static final Log logger = LogFactory.getLog( JmsRefreshEligiblePromotionsCacheProcessMessage.class );

  @Override
  public void processMessage() throws JMSException
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Entering processMessage()" );
    }
    try
    {
      // need this for picklist content....
      ContentReaderUtils.prepareContentReader();
      getMainContentService().refreshAllLivePromotionCache();
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Successfully refreshed all live promotions in the cache." );
      }
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
    finally
    {
      // cleanup
      ContentReaderManager.removeContentReader();
    }
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Leaving processMessage()" );
    }
  }

  protected MainContentService getMainContentService()
  {
    return (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
  }
}
