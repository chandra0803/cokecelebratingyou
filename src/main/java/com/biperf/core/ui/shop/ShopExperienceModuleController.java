
package com.biperf.core.ui.shop;

import com.biperf.core.domain.enums.TileMappingType;

public class ShopExperienceModuleController extends BaseShopBannerModuleController
{
  @Override
  public String getTargetTileMappingTypeCode()
  {
    return TileMappingType.EXPERIENCES;
  }
}
