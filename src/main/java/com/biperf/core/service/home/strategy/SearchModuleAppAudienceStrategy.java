
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.value.PromotionMenuBean;

public class SearchModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Autowired
  private ParticipantService participantService;

  @Override
  @SuppressWarnings( "unchecked" )
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean isValid = false;
    String moduleName = participantService.getHeroModuleAudienceTypeByUserId( participant.getId() );
    if ( moduleName != null && moduleName.equalsIgnoreCase( "heroModule" ) )
    {
      isValid = true;
    }
    else if ( parameterMap != null && parameterMap.containsKey( "eligiblePromotions" ) )
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)parameterMap.get( "eligiblePromotions" );
      Optional<PromotionMenuBean> promotionMenuBean = eligiblePromotions.stream().filter( new Predicate<PromotionMenuBean>()
      {
        @Override
        public boolean test( PromotionMenuBean p )
        {
          boolean isValid = p.getPromotion().isRecognitionPromotion() || p.getPromotion().isNominationPromotion();
          String moduleName = participantService.getHeroModuleAudienceTypeByUserId( participant.getId() );
          if ( moduleName != null && moduleName.equalsIgnoreCase( "heroModule" ) )
          {
            isValid = true;
          }
          return isValid;
        }
      } ).filter( e -> e.isCanSubmit() ).findFirst();

      if ( promotionMenuBean.isPresent() )
      {
        return true;
      }

      return false;
    }
    return isValid;
  }
}
