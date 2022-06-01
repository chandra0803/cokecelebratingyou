
package com.biperf.core.ui.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public abstract class BaseShopBannerModuleController extends BaseController
{
  public abstract String getTargetTileMappingTypeCode();

  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List<Content> allShopBanners = (List<Content>)contentReader.getContent( "home.shopbanners" );

    for ( Content shopBanner : allShopBanners )
    {
      String tileMappingType = (String)shopBanner.getContentDataMap().get( "TILE_MAPPING_TYPE" );
      if ( getTargetTileMappingTypeCode().equalsIgnoreCase( tileMappingType ) )
      {
        request.setAttribute( "moduleContent", shopBanner );

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put( "page", shopBanner.getContentDataMap().get( "ECOMMERCE_TARGET" ) );
        request.setAttribute( "shoppingLink", ClientStateUtils.generateEncodedLink( "", "submitRewardOffering.do?method=displayInternal", parameterMap ) );
        break;
      }
    }
  }
}
