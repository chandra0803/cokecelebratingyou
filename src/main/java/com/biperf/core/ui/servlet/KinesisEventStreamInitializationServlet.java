
package com.biperf.core.ui.servlet;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.event.KinesisEventScheduler;
import com.biperf.core.utils.BeanLocator;

@SuppressWarnings( "serial" )
public class KinesisEventStreamInitializationServlet extends CMSAwareBaseServlet
{
  private static Log log = LogFactory.getLog( KinesisEventStreamInitializationServlet.class );

  @Override
  public void init() throws ServletException
  {
    try
    {
      super.init();

      KinesisEventScheduler kinesisEventScheduler = (KinesisEventScheduler)BeanLocator.getBean( KinesisEventScheduler.BEAN_NAME );
      kinesisEventScheduler.startKinesisConsumer();
    }
    catch( Exception e )
    {
      e.printStackTrace();
      log.error( e.getMessage(), e );
    }

  }

}
