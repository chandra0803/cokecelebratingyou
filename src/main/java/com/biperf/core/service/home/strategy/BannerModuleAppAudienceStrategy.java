
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.home.HomePageContentService;
import com.objectpartners.cms.domain.Content;

public class BannerModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private HomePageContentService homePageContentService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean displayFilter = false;
    boolean displayDiyBannerAds = false;

    if ( super.isUserInAudience( participant, filter, parameterMap ) )
    {
      if ( homePageContentService.getGenericBannerAds() != null && !homePageContentService.getGenericBannerAds().isEmpty() )
      {
        displayFilter = true;
      }
    }

    List<Content> activeDiyBannerAdsList = getParticipantDIYCommunicationsService().getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_BANNER, DIYCommunications.DIY_BANNER_SECTION_CODE );

    if ( activeDiyBannerAdsList != null && activeDiyBannerAdsList.size() > 0 )
    {
      displayDiyBannerAds = !activeDiyBannerAdsList.isEmpty();
    }

    if ( displayFilter || displayDiyBannerAds )
    {
      return true;
    }

    return false;
  }

  public void setHomePageContentService( HomePageContentService homePageContentService )
  {
    this.homePageContentService = homePageContentService;
  }

}
