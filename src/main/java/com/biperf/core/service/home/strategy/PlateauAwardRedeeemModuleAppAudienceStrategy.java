/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/home/strategy/PlateauAwardRedeeemModuleAppAudienceStrategy.java,v $
 */

package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;

/**
 * 
 * @author poddutur
 * @since Oct 9, 2015
 */
public class PlateauAwardRedeeemModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( participant.isParticipant() )
    {
      List awardReminders = getMerchOrderService().getMerchAwardReminders( participant.getId() );
      if ( awardReminders.size() > 0 )
      {
        return true;
      }
    }
    return false;
  }

}
