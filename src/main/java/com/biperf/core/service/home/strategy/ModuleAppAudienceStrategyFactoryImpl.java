
package com.biperf.core.service.home.strategy;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.ModuleApp;

@SuppressWarnings( "rawtypes" )
public class ModuleAppAudienceStrategyFactoryImpl implements ModuleAppAudienceStrategyFactory
{
  private Logger logger = Logger.getLogger( getClass() );

  private Map entries = new HashMap();

  public ModuleAppAudienceStrategy getModuleAppAudienceStrategy( FilterAppSetup filter )
  {
    ModuleAppAudienceStrategy strategy = (ModuleAppAudienceStrategy)entries.get( filter.getModuleApp().getAppAudienceType().getCode() );
    if ( null == strategy )
    {
      // either return the DisabledModuleAppAudienceStrategy that always returns false....
      logger.warn( "No ModuleAppAudienceStrategy mapped to " + filter.getModuleApp().getAppAudienceType() );
      return (ModuleAppAudienceStrategy)entries.get( "disabled" );
    }
    return strategy;
  }

  public ModuleAppAudienceStrategy getModuleAppAudienceStrategyDelegate( ModuleApp module )
  {
    ModuleAppAudienceStrategy strategy = (ModuleAppAudienceStrategy)entries.get( module.getAppAudienceType().getCode() );
    if ( null == strategy )
    {
      // either return the DisabledModuleAppAudienceStrategy that always returns false....
      logger.warn( "No ModuleAppAudienceStrategy mapped to " + module.getAppAudienceType() );
      return (ModuleAppAudienceStrategy)entries.get( "disabled" );
    }
    return strategy;
  }

  public Map getEntries()
  {
    return entries;
  }

  public void setEntries( Map entries )
  {
    this.entries = entries;
  }
}
