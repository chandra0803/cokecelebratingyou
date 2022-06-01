
package com.biperf.core.utils.jms;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.indexing.BIElasticSearchAdminService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.cms.ContentReaderUtils;
import com.objectpartners.cms.util.ContentReaderManager;

@SuppressWarnings( "serial" )
public class ResetElasticSearchClientFactoryMessage extends JmsMessage
{
  private static final Log logger = LogFactory.getLog( ResetElasticSearchClientFactoryMessage.class );

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
      getBIElasticSearchAdminService().resetClientFactory();
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

  protected BIElasticSearchAdminService getBIElasticSearchAdminService()
  {
    return (BIElasticSearchAdminService)BeanLocator.getBean( BIElasticSearchAdminService.BEAN_NAME );
  }

}
