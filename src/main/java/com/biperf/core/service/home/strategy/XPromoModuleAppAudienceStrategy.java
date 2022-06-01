
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.home.FilterAppSetupService;

public class XPromoModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private static final Log logger = LogFactory.getLog( XPromoModuleAppAudienceStrategy.class );

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    FilterAppSetupService filterAppSetupService = getFilterAppSetupService();
    List<CrossPromotionalModuleApp> xPromoModuleApps = filterAppSetupService.getCrossPromotionalModuleApps();

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Checking " + xPromoModuleApps.size() + " Cross Promotional Module Apps..." );
    }

    for ( CrossPromotionalModuleApp xPromoModuleApp : xPromoModuleApps )
    {
      // if even one is good, return true
      if ( filterAppSetupService.getModuleAppAudienceStrategyFactory().getModuleAppAudienceStrategyDelegate( xPromoModuleApp ).isUserInAudience( participant, filter, parameterMap ) )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Returning 'true' based on successfull audience inclusion of  " + xPromoModuleApp.getClass().getName() );
        }
        return true;
      }
    }

    return false;
  }
}
