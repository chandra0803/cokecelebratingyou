
package com.biperf.core.ui.shop;

import com.biperf.core.domain.enums.TileMappingType;

public class ShopEnvoyTravelModuleController extends BaseShopBannerModuleController
{
  @Override
  public String getTargetTileMappingTypeCode()
  {
    return TileMappingType.TRAVEL_ENVOY;
  }
}
