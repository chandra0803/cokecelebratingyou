
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class ContentManagerModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @SuppressWarnings( "unchecked" )
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( participant.isParticipant() )
    {
      ContentReader contentReader = ContentReaderManager.getContentReader();
      List<Content> allShopBanners = (List<Content>)contentReader.getContent( "home.shopbanners" );

      for ( Content shopBanner : allShopBanners )
      {
        String tileMappingType = (String)shopBanner.getContentDataMap().get( "TILE_MAPPING_TYPE" );
        if ( filter.getModuleApp().getTileMappingType().getCode().equalsIgnoreCase( tileMappingType ) )
        {
          // content reader automatically filters out content not eligible for user - so if in the
          // list we know user belongs to the audience
          return true;
        }
      }
    }

    return false;
  }
}
