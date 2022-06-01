
package com.biperf.core.cache;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.oscache.cluster.jms.SpringJmsBroadcastListener;
import com.biperf.core.utils.Environment;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.FinalizationException;
import com.opensymphony.oscache.base.InitializationException;
import com.opensymphony.oscache.plugins.clustersupport.ClusterNotification;

/**
 * G3SpringJmsBroadcastListener
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
 * <td>Rachel Robinson</td>
 * <td>September 22, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class G3SpringJmsBroadcastListener extends SpringJmsBroadcastListener
{

  private static final Log log = LogFactory.getLog( G3SpringJmsBroadcastListener.class );

  protected String[] clusterData;

  /**
   * 
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
    log.debug( "messageNode=" + messageNode );
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

  @Override
  public void finalize() throws FinalizationException
  {
    // Note: we could not call super.finalize() since we have to handle throwable instead of
    // FinalizationException
    // So, please note that we are doing the same thing in this method as super.finalize().
    try
    {
      destroyJmsObjects();
      doShutDown();
    }
    catch( Exception e )
    {
      handleFinalizationException( e );
    }
  }

  public void doShutDown()
  {
    log.debug( "==== JMS shutDown starting ====" );

    this.cache = null;

    log.debug( "==== JMS shutDown completed ====" );
  }
}
