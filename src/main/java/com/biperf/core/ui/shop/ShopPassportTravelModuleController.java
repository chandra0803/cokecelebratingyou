
package com.biperf.core.ui.shop;

import com.biperf.core.domain.enums.TileMappingType;

public class ShopPassportTravelModuleController extends BaseShopBannerModuleController
{
  @Override
  public String getTargetTileMappingTypeCode()
  {
    return TileMappingType.PASSPORT_TRAVEL;
  }
}
