
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.value.PromotionMenuBean;

public class ManagerGoalQuestModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private static final Log log = LogFactory.getLog( ManagerGoalQuestModuleAppAudienceStrategy.class );
  
  private AudienceService audienceService;
  
  private GoalQuestService goalQuestService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( !participant.isManager() && !participant.isOwner() )
    {
      return false;
    }

    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      Promotion promotion = promotionMenuBean.getPromotion();
      if ( promotion.isGoalQuestPromotion() && promotion.isLive() && ( (GoalQuestPromotion)promotion ).isWebRulesActive() )
      {
        if ( ( (GoalQuestPromotion)promotion ).getManagerWebRulesAudienceType().equals( ManagerWebRulesAudienceType.lookup( ManagerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
        {
          return true;
        }
        if ( ( (GoalQuestPromotion)promotion ).getManagerWebRulesAudienceType().equals( ManagerWebRulesAudienceType.lookup( ManagerWebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) ) )
        {
          Long promotionId = promotion.getId();
          AssociationRequestCollection webRulesAudienceAssociation = new AssociationRequestCollection();
          webRulesAudienceAssociation.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );

          GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, webRulesAudienceAssociation );

          if ( goalQuestPromotion.getPrimaryAudienceType() != null && goalQuestPromotion.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
          {
            return true;
          }
          else
          {
            boolean isUserInPromotionPrimaryAudiences = getAudienceService().isUserInPromotionAudiences( participant, goalQuestPromotion.getPromotionPrimaryAudiences() );

            if ( isUserInPromotionPrimaryAudiences )
            {
              return true;
            }
          }
        }
        if ( ( (GoalQuestPromotion)promotion ).getManagerWebRulesAudienceType().equals( ManagerWebRulesAudienceType.lookup( ManagerWebRulesAudienceType.CREATE_AUDIENCE_CODE ) ) )
        {
          Long promotionId = promotion.getId();
          AssociationRequestCollection webRulesAudienceAssociation = new AssociationRequestCollection();
          webRulesAudienceAssociation.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );

          GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, webRulesAudienceAssociation );

          if ( goalQuestPromotion.getPromotionManagerWebRulesAudience() != null && !goalQuestPromotion.getPromotionManagerWebRulesAudience().isEmpty() )
          {
            boolean isUserInPromotionManagerWebRulesAudiences = getAudienceService().isUserInPromotionAudiences( participant, goalQuestPromotion.getPromotionManagerWebRulesAudience() );

            if ( isUserInPromotionManagerWebRulesAudiences )
            {
              return true;
            }
          }
        }
      }
    }
    
    // Try honeycomb second.  Expensive call on login, maybe we've already returned true from cache
    // No concept of display date here, they all display all the time.  So if any exist, we show the module
    if ( !goalQuestService.getHoneycombManagerPrograms( participant.getHoneycombUserId() ).isEmpty() )
    {
      return true;
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
  
  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }
}
