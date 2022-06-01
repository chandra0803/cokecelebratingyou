
package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.service.system.SystemVariableService;

public enum ModuleType
{

  PRODUCTCLAIMS( "productclaims", SystemVariableService.INSTALL_PRODUCTCLAIMS, PromotionType.PRODUCT_CLAIM ), CHALLENGEPOINT( "challengepoint", SystemVariableService.INSTALL_CHALLENGEPOINT,
      PromotionType.CHALLENGE_POINT ), ENGAGEMENT( "engagement", SystemVariableService.INSTALL_ENGAGEMENT, PromotionType.ENGAGEMENT ), UNDEFINED_MODULE( "undefined", "undefined", "undefined" );

  private String systemPropertyName;
  private String moduleName;
  private String promotionTypeCode;

  private ModuleType( String moduleName, String systemPropertName, String promotionTypeCode )
  {
    this.moduleName = moduleName;
    this.systemPropertyName = systemPropertName;
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getSystemPropertyName()
  {
    return systemPropertyName;
  }

  public String getModuleName()
  {
    return moduleName;
  }

  public static List<ModuleType> getAllModuleType()
  {
    List<ModuleType> allModuleTypes = new ArrayList<ModuleType>();
    for ( ModuleType t : values() )
    {
      allModuleTypes.add( t );
    }
    return allModuleTypes;
  }

  public static ModuleType getModuleTypeByModuleName( String moduleName )
  {

    for ( ModuleType t : values() )
    {
      if ( t.getModuleName().equals( moduleName ) )
      {
        return t;
      }
    }

    return UNDEFINED_MODULE;
  }

  public static ModuleType getModuleTypeByPromotionTypeCode( String promotionTypeCode )
  {

    for ( ModuleType t : values() )
    {
      if ( t.getPromotionTypeCode().equals( promotionTypeCode ) )
      {
        return t;
      }
    }

    return UNDEFINED_MODULE;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

}
