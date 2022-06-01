
package com.biperf.core.service.home.strategy;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.value.PromotionMenuBean;

public class ChallengePointModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    Date now = new Date();

    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      if ( promotionMenuBean.getPromotion().isChallengePointPromotion() )
      {
        ChallengePointPromotion promotion = (ChallengePointPromotion)promotionMenuBean.getPromotion();
        boolean inTileDisplayDate = isDateWithinTileDisplayDate( promotion );
        // skip this one if the current date is not within the tile display date times
        if ( !inTileDisplayDate )
        {
          continue;
        }

        if ( promotionMenuBean.isCanSubmit() && inTileDisplayDate )
        {
          return true;
        }
        // if a partner - they need to have the owner
        // made a selection before it's visible
        // and the date must be past the end date for selections,
        // cause the owner might unselect a user.
        if ( promotionMenuBean.isPartner() && now.after( promotion.getGoalCollectionEndDate() ) && now.before( promotion.getTileDisplayEndDate() ) )
        {
          return true;
        }

      }
    }

    return false;
  }
}
