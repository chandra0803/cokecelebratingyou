
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.value.PromotionMenuBean;

public class ManagerChallengePointModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private AudienceService audienceService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( !participant.isManager() )
    {
      return false;
    }

    // get all nodes for which participant is manager/owner
    List<Node> nodeList = participant.getActiveManagerNodes();

    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      Promotion promotion = promotionMenuBean.getPromotion();
      if ( promotion.isChallengePointPromotion() && promotion.isLive() && ( (GoalQuestPromotion)promotion ).isWebRulesActive() )
      {
        if ( ( (GoalQuestPromotion)promotion ).getManagerWebRulesAudienceType().equals( ManagerWebRulesAudienceType.lookup( ManagerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
        {
          return true;
        }
        else
        {
          boolean isPromotionPrimaryAudienceInManagerNode = getAudienceService().isPromotionPrimaryAudienceInManagerNode( nodeList, promotion.getId() );

          if ( isPromotionPrimaryAudienceInManagerNode )
          {
            return true;
          }
        }

      }
    }

    return false;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }
}
