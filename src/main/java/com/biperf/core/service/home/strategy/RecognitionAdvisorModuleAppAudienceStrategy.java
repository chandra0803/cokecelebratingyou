
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.recognitionadvisor.RecognitionAdvisorService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

public class RecognitionAdvisorModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  @Autowired
  private RecognitionAdvisorService recognitionAdvisorService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {

    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );

    Predicate<PromotionMenuBean> isGiverCondition = promotionMenuBean -> promotionMenuBean.getPromotion().isRecognitionPromotion() && promotionMenuBean.isCanSubmit();
    boolean isGiver = eligiblePromotions.stream().anyMatch( isGiverCondition );

    boolean raFlag = getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal();

    boolean isHavingMembers = recognitionAdvisorService.isHavingMembers( participant.getId() );

    AuthenticatedUser user = UserManager.getUser();

    if ( Objects.isNull( user ) )
    {
      return false;
    }

    if ( ( participant.isManager() || participant.isOwner() ) && raFlag && isGiver && !user.isDelegate() && !isHavingMembers )
    {
      return true;
    }
    return false;
  }

}
