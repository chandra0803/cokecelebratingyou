
package com.biperf.core.service.home.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.PromotionMenuBean;

public class BadgeModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private GamificationService gamificationService;

  private SystemVariableService systemVariableService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( participant.isParticipant() && getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_BADGES ).getBooleanVal() )
    {
      List<PromotionMenuBean> badgeReceivablePromotionList = new ArrayList<PromotionMenuBean>();
      List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.isCanReceive() )
        {
          badgeReceivablePromotionList.add( promotionMenuBean );
        }
      }

      if ( getGamificationService().isUserHasActiveBadges( participant.getId(), badgeReceivablePromotionList ) > 0 )
      {
        return true;
      }
    }

    return false;
  }

  public GamificationService getGamificationService()
  {
    return gamificationService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  /**
   *@return SystemVariableService
   */
  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  /**
   * @param systemVariableService
   */
  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

}
