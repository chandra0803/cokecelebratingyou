
package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.service.system.SystemVariableService;

public enum ModuleType
{

  PRODUCTCLAIMS( "productclaims", SystemVariableService.INSTALL_PRODUCTCLAIMS, PromotionType.PRODUCT_CLAIM ),
  // CHALLENGEPOINT( "goalquest", SystemVariableService.INSTALL_CHALLENGEPOINT,
  // PromotionType.CHALLENGE_POINT ), // challangepoint IS goalquest
  ENGAGEMENT( "engagement", SystemVariableService.INSTALL_ENGAGEMENT, PromotionType.ENGAGEMENT ), GOALQUEST( "goalquest", SystemVariableService.INSTALL_GOAL_QUEST, PromotionType.GOALQUEST ), DIY_QUIZ(
      "diyQuizes", SystemVariableService.INSTALL_QUIZZES,
      PromotionType.DIY_QUIZ ), INSTANT_POLL( "instantPoll", SystemVariableService.INSTANT_POLL, PromotionType.INSTANTPOLL ), NOMINATIONS( "nomination", SystemVariableService.INSTALL_NOMINATIONS,
          PromotionType.NOMINATION ), QUIZES( "quizes", SystemVariableService.INSTALL_QUIZZES, PromotionType.QUIZ ), RECOGNITION( "recognition", SystemVariableService.INSTALL_RECOGNITION,
              PromotionType.RECOGNITION ), SSI( "ssi", SystemVariableService.INSTALL_RECOGNITION, PromotionType.SELF_SERV_INCENTIVES ), SURVEY( "surveys", SystemVariableService.INSTALL_SURVEYS,
                  PromotionType.SURVEY ), THROWDOWN( "throwdown", SystemVariableService.INSTALL_THROWDOWN, PromotionType.THROWDOWN ),

  // non-system variable
  REPORTS( "reports", "undefined", "undefined" ), DIY_COMMUNICATIONS( "diy_communications", "undefined", "undefined" ), UNDEFINED_MODULE( "undefined", "undefined", "undefined" );

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
    // ugh
    if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )// challengepoint IS goalquest
    {
      return ModuleType.GOALQUEST;
    }
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
