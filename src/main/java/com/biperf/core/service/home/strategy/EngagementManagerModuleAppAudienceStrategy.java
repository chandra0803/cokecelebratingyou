
package com.biperf.core.service.home.strategy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.enums.ModuleAppAudienceCodeType;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.EngagementPromotionData;

/**
 * 
 * EngagementManagerModuleAppAudienceStrategy.
 * 
 * @author kandhi
 * @since Jun 5, 2014
 * @version 1.0
 */
public class EngagementManagerModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  @Autowired
  private EngagementService engagementService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean isUserInAudience = false;
    String moduleName = engagementService.getEngagementManagerModuleAppAudienceTypeByUserId( participant.getId() );
    if ( moduleName != null && moduleName.equalsIgnoreCase( ModuleAppAudienceCodeType.ENGAGEMENT_MANAGER ) )
    {
      if ( participant.isManager() || participant.isOwner() )
      {
        EngagementPromotionData engagementPromotionData = getEngagementService().getLiveEngagementPromotionData();
        if ( engagementPromotionData != null && engagementPromotionData.getTileDisplayStartDate().compareTo( DateUtils.getCurrentDateTrimmed() ) <= 0 )
        {
          boolean isParticipantInManagerAudience = getEngagementService().isParticipantInManagerAudience( participant.getId() );
          if ( isParticipantInManagerAudience )
          {
            isUserInAudience = true;
          }
        }
      }
    }
    return isUserInAudience;
  }
}
