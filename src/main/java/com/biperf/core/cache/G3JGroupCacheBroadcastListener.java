
package com.biperf.core.cache;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.oscache.cluster.jgroups.JGroupsBroadcastListener;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
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
public class G3JGroupCacheBroadcastListener extends JGroupsBroadcastListener
{

  private static final Log log = LogFactory.getLog( G3JGroupCacheBroadcastListener.class );

  protected String appName = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getContextName();

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
      log.debug( "Cluster node data is " + Arrays.asList( appName ) );
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
  @Override
  protected void sendNotification( ClusterNotification message )
  {
    if ( isClusteredCachingEnvironment() )
    {
      G3CacheClusterNotification g3Message = new G3CacheClusterNotification( message.getType(), message.getData() );
      g3Message.setAppName( appName );
      super.sendNotification( g3Message );
    }
  }

  @Override
  public void handleClusterNotification( ClusterNotification message )
  {
    if ( message instanceof G3CacheClusterNotification )
    {
      G3CacheClusterNotification g3Message = (G3CacheClusterNotification)message;
      if ( g3Message.getAppName() != null && g3Message.getAppName().equals( appName ) )
      {
        super.handleClusterNotification( message );
      }
    }
    return;
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

  protected boolean isClusteredCachingEnvironment()
  {
    return Environment.isCtech();
  }

}
