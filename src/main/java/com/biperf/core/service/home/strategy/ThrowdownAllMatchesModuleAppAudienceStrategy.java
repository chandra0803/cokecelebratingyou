
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.value.PromotionMenuBean;

public class ThrowdownAllMatchesModuleAppAudienceStrategy extends ThrowdownModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    // need to determine if this is a manager
    if ( !participant.isManager() )
    {
      return false;
    }

    boolean showTile = super.isUserInAudience( participant, filter, parameterMap );

    if ( !showTile )
    {
      return false;
    }

    // now, even if this is a manager, he MAY be in the competitor audience, in which case they
    // should not see it
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );

    // if the Manager is NOT a competitor in at least one of the throwdown promotions, he/she can
    // see the tile. The promotion selector is then used to determine if the tile should show/hide
    // based on the specifically selected promotion selected
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      if ( promotionMenuBean.getPromotion().isThrowdownPromotion() )
      {
        ThrowdownPromotion promotion = (ThrowdownPromotion)promotionMenuBean.getPromotion();

        for ( Division division : promotion.getDivisions() )
        {
          if ( !getAudienceService().isUserInPromotionDivisionAudiences( participant, division.getCompetitorsAudience() ) )
          {
            return true;
          }
        }
      }
    }
    return false;
  }
}
