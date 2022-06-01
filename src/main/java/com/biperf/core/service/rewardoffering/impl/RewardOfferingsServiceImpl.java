/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/rewardoffering/impl/RewardOfferingsServiceImpl.java,v $
 */

package com.biperf.core.service.rewardoffering.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.Cache;
import com.biperf.cache.CacheFactory;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.services.rest.client.rewardoffering.RewardOfferingsClient;
import com.biperf.services.rest.client.rewardoffering.impl.RewardOfferingsClientImpl;
import com.biperf.services.rest.core.audit.ClientInfo;
import com.biperf.services.rest.rewardoffering.domain.ProgramRewardOfferings;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;

public class RewardOfferingsServiceImpl implements RewardOfferingsService
{
  private static final Log logger = LogFactory.getLog( RewardOfferingsServiceImpl.class );

  private Cache programRewardOfferingsCache;
  private SystemVariableService systemVariableService;

  private ClientInfo clientInfo = null;
  private RewardOfferingsClient client = null;
  private boolean initialized = false;
  private Properties cacheRegionProperties = null;

  public List<RewardOffering> getRewardOfferings( String programId )
  {
    // YUCK!
    if ( !initialized )
    {
      initClient();
    }

    ProgramRewardOfferings offerings = (ProgramRewardOfferings)programRewardOfferingsCache.get( programId );
    if ( null == offerings )
    {
      // get the stuff from the client
      try
      {
        offerings = client.getRewardOfferings( programId );
        programRewardOfferingsCache.put( programId, offerings );
      }
      catch( Exception e )
      {
        logger.error( "Program not setup properly for reward offerings: programId:" + programId + ", Error:" + e.getMessage() );
      }
    }
    if ( null != offerings )
    {
      return offerings.getRewardOfferings();
    }
    else
    {
      return null;
    }
  }

  public void clearProgramRewardOfferingsCache( String programId )
  {
    programRewardOfferingsCache.remove( programId );
  }

  public void setCacheFactory( CacheFactory cacheFactory )
  {
    Map<String, Properties> regionProperties = new HashMap<String, Properties>();
    regionProperties.put( "programRewardOfferings", cacheRegionProperties );
    cacheFactory.setRegionProperties( regionProperties );
    programRewardOfferingsCache = cacheFactory.getCache( "programRewardOfferings" );
  }

  /*
   * for some mapping reason, I cannot get this method called to initialize this client. Tried
   * adding an init-method mapping in the spring config, tried getting this class from the bean
   * locator in the InitializationServlet adn the ContextListener to call this method just once..all
   * failed. So now I've got a yuck "check" for the initialization....
   */
  public void initClient()
  {
    String serviceEndpoint = systemVariableService.getPropertyByNameAndEnvironment( "reward.offerings.endpoint" ).getStringVal();
    String applicationName = systemVariableService.getPropertyByName( "client.name" ).getStringVal();
    clientInfo = new ClientInfo( serviceEndpoint, applicationName, null );
    client = new RewardOfferingsClientImpl( clientInfo );
    initialized = true;
    if ( logger.isInfoEnabled() )
    {
      logger.info( "constructing rewardOfferingsService client information attaching to " + serviceEndpoint + " with applicationName " + applicationName );
    }
  }

  public Properties getCacheRegionProperties()
  {
    return cacheRegionProperties;
  }

  public void setCacheRegionProperties( Properties cacheRegionProperties )
  {
    this.cacheRegionProperties = cacheRegionProperties;
  }

  public void destroy()
  {
    if ( logger.isInfoEnabled() )
    {
      logger.info( "destroying rewardOfferingsService cache" );
    }
    programRewardOfferingsCache.clear();

  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }
}
