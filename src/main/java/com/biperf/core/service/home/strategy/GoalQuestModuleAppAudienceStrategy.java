
package com.biperf.core.service.home.strategy;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.value.PromotionMenuBean;

public class GoalQuestModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private static final Log log = LogFactory.getLog( GoalQuestModuleAppAudienceStrategy.class );
  
  private GoalQuestService goalQuestService;
  
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    Date now = new Date();
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      if ( promotionMenuBean.getPromotion().isGoalQuestPromotion() )
      {
        GoalQuestPromotion promotion = (GoalQuestPromotion)promotionMenuBean.getPromotion();
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
        if ( promotionMenuBean.isPartner() && promotionMenuBean.isViewTile() && now.after( promotion.getGoalCollectionEndDate() ) && now.before( promotion.getTileDisplayEndDate() ) )
        {
          return true;
        }

      }
    }
    
    // Try honeycomb second.  Expensive call on login...
    // No concept of display date here, they all display all the time.  So if any exist, we show the module
    if ( !goalQuestService.getHoneycombProgramDetails( participant ).isEmpty() )
    {
      return true;
    }

    return false;
  }

  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }
}
