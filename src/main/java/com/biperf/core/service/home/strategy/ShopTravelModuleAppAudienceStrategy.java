
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;

public class ShopTravelModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( participant.isParticipant() )
    {

      if ( !canShowShopTile( participant, parameterMap ) )
      {
        return false;
      }

      String programId = getUserService().getCountryProgramIdByUserId( participant.getId() );
      if ( programId != null )
      {
        List<RewardOffering> rewardsList = getRewardOfferingsService().getRewardOfferings( programId );
        if ( rewardsList != null )
        {
          for ( RewardOffering rewards : rewardsList )
          {
            if ( "travel".equalsIgnoreCase( rewards.getType() ) )
            {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private boolean canShowShopTile( Participant participant, Map<String, Object> parameterMap )
  {
    return getMainContentService().checkShowShopORTravel();
  }
}
