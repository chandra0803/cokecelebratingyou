
package com.biperf.core.cache;

import java.io.Serializable;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.opensymphony.oscache.plugins.clustersupport.ClusterNotification;

public class G3CacheClusterNotification extends ClusterNotification
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String appName;

  public G3CacheClusterNotification( int type, Serializable data )
  {
    super( type, data );
    appName = getSystemVariableService().getContextName();
  }

  public String getAppName()
  {
    return appName;
  }

  public void setAppName( String appName )
  {
    this.appName = appName;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
