
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.value.PromotionMenuBean;

public class WhatsNewModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    // only show to participants
    if ( !participant.isParticipant() )
    {
      return false;
    }

    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    Country country = getUserService().getPrimaryUserAddressCountry( participant.getId() );
    // only show this if there are items in the what's new list
    if ( eligiblePromotions != null && eligiblePromotions.size() > 0 && country != null )
    {
      @SuppressWarnings( "rawtypes" )
      List whatsNew = getMainContentService().getWhatsNewList( eligiblePromotions, country );
      if ( null != whatsNew && !whatsNew.isEmpty() )
      {
        return true;
      }
    }
    return false;
  }
}
