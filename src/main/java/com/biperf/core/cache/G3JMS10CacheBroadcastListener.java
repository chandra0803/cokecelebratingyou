
package com.biperf.core.cache;

import java.util.Arrays;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.oscache.cluster.jms.Jms10BroadcastListener;
import com.biperf.core.utils.Environment;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.FinalizationException;
import com.opensymphony.oscache.base.InitializationException;
import com.opensymphony.oscache.plugins.clustersupport.ClusterNotification;

/**
 * G3CacheBroadcastListener
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
 * <td>Brian Repko</td>
 * <td>Dec 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class G3JMS10CacheBroadcastListener extends Jms10BroadcastListener
{

  private static final Log log = LogFactory.getLog( G3JMS10CacheBroadcastListener.class );

  protected String[] clusterData;

  /**
   * Overridden from
   * 
   * @see com.biperf.cache.oscache.OSCacheJms10BroadcastListener#initialize(com.opensymphony.oscache.base.Cache,
   *      com.opensymphony.oscache.base.Config)
   * @param cache
   * @param config
   * @throws InitializationException
   */
  public void initialize( Cache cache, Config config ) throws InitializationException
  {
    if ( isClusteredCachingEnvironment() )
    {
      super.initialize( cache, config );
      clusterData = splitNodeName( clusterNode );
      log.debug( "Cluster node data is " + Arrays.asList( clusterData ) );
    }
    else
    {
      log.warn( "No clustered caching for unknown, dev or local environment" );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.cache.oscache.OSCacheJms10BroadcastListener#sendNotification(com.opensymphony.oscache.plugins.clustersupport.ClusterNotification)
   * @param message
   */
  protected void sendNotification( ClusterNotification message )
  {
    if ( isClusteredCachingEnvironment() )
    {
      super.sendNotification( message );
    }
  }

  /**
   * Checks that the application is the same but the server is different. Overridden from
   * 
   * @see com.biperf.cache.oscache.OSCacheJms10BroadcastListener#doClusterNotification(java.lang.String)
   * @param messageNode
   * @return true to handle the cluster notification
   */
  protected boolean doClusterNotification( String messageNode )
  {
    String[] messageData = splitNodeName( messageNode );
    if ( clusterData.length != 2 || messageData.length != 2 )
    {
      return false;
    }
    log.debug( "Message node data is " + Arrays.asList( messageData ) );
    return messageData[1].equals( clusterData[1] ) && !messageData[0].equals( clusterData[0] );
  }

  /**
   * Do not throw an InitializationException - Overridden from
   * 
   * @see com.biperf.cache.oscache.OSCacheJms10BroadcastListener#handleInitializationException(java.lang.Exception)
   * @param e
   * @throws InitializationException
   */
  protected void handleInitializationException( Exception e ) throws InitializationException
  {
    log.error( "Unable to initialize BroadcastListener", e );
  }

  /**
   * Do not throw a FinalizationException - Overridden from
   * 
   * @see com.biperf.cache.oscache.OSCacheJms10BroadcastListener#handleFinalizationException(java.lang.Exception)
   * @param e
   * @throws FinalizationException
   */
  protected void handleFinalizationException( Exception e ) throws FinalizationException
  {
    log.error( "Unable to finalize BroadcastListener", e );
  }

  protected String[] splitNodeName( String input )
  {
    int index = input.indexOf( "_" );
    if ( index < 0 )
    {
      log.error( "Invalid format for node string " + input );
      return new String[] { input };
    }
    return new String[] { input.substring( 0, index ), input.substring( index + 1 ) };
  }

  protected boolean isClusteredCachingEnvironment()
  {
    return Environment.isCtech();
  }

  public void doShutDown()
  {
    try
    {
      this.cache = null;

      if ( this.connection != null )
      {
        this.connection.close();
      }

      if ( this.publisher != null )
      {
        this.publisher.close();
      }

      if ( this.publisherSession != null )
      {
        this.publisherSession.close();
      }

      if ( this.subscriber != null )
      {
        this.subscriber.close();
      }
    }
    catch( JMSException jmse )
    {
      log.error( "Unable to do JMS shutdown: " + jmse.getMessage(), jmse );
    }

    log.debug( "==== JMS shutDown completed ====" );
  }
}
