
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

public class ThrowdownModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    boolean activeContentFound = false;
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      if ( promotionMenuBean.getPromotion().isThrowdownPromotion() )
      {
        ThrowdownPromotion promotion = (ThrowdownPromotion)promotionMenuBean.getPromotion();
        boolean inTileDisplayDate = isDateWithinTileDisplayDate( promotion );
        // skip this one if the current date is not within the tile display date times
        if ( inTileDisplayDate )
        {
          activeContentFound = true;
        }
        else
        {
          continue;
        }
      }
    }
    if ( !activeContentFound )
    {
      return false;
    }
    if ( UserManager.getUser().isParticipant() )
    {
      if ( filter.getModuleApp().getTileMappingType().getCode().equalsIgnoreCase( TileMappingType.TRAINING_CAMP ) )
      {
        List<Content> contents = (List)ContentReaderManager.getContentReader().getContent( "home.throwdown.trainingCamp" );
        activeContentFound = false;
        if ( contents != null )
        {
          activeContentFound = true;
        }
      }
    }

    return activeContentFound;
  }
}
