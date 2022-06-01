
package com.biperf.core.ui.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class ShopInternationalMerchModuleController extends BaseController
{

  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List<Content> allShopBanners = (List<Content>)contentReader.getContent( "home.shopbanners" );

    String targetTileMappingType = "N/A";
    Country userCountry = getUserService().getPrimaryUserAddressCountry( UserManager.getUserId() );
    boolean isBiiSupplier = checkCurrentUserSupplierIsBii( userCountry.getCountryCode() );

    if ( isBiiSupplier )
    {
      targetTileMappingType = TileMappingType.INTL_CATALOGUE;
    }

    for ( Content shopBanner : allShopBanners )
    {
      String tileMappingType = (String)shopBanner.getContentDataMap().get( "TILE_MAPPING_TYPE" );
      if ( targetTileMappingType.equalsIgnoreCase( tileMappingType ) )
      {
        request.setAttribute( "moduleContent", shopBanner );

        if ( isBiiSupplier )
        {
          Map<String, Object> biiParameterMap = new HashMap<String, Object>();
          biiParameterMap.put( "externalSupplierId", Supplier.ID_BII );
          request.setAttribute( "intlShopUrl", ClientStateUtils.generateEncodedLink( "", PageConstants.SHOPPING_EXTERNAL, biiParameterMap ) );
        }
        break;
      }
    }
  }

  private boolean checkCurrentUserSupplierIsBii( String countryCode )
  {
    return getCountryService().checkUserSupplier( countryCode, Supplier.BII );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
