
package com.biperf.core.service.home.strategy;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.ModuleApp;

public interface ModuleAppAudienceStrategyFactory
{
  public static final String BEAN_NAME = "moduleAppAudienceStrategyFactory";

  public ModuleAppAudienceStrategy getModuleAppAudienceStrategy( FilterAppSetup filterAppSetup );

  public ModuleAppAudienceStrategy getModuleAppAudienceStrategyDelegate( ModuleApp module );

}
