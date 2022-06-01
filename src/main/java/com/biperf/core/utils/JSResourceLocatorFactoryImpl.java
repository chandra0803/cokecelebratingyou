
package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;

import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.value.ModuleResources;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.UserJSResources;

public class JSResourceLocatorFactoryImpl implements JSResourceLocatorFactory
{

  public static final String SESSION_VARIABLE_ELIGIBLE_PROMOTIONS = "eligiblePromotions";
  private Map<ModuleType, List<String>> jsResourcesMap;

  private MainContentService mainContentService;

  @SuppressWarnings( "unchecked" )
  @Override
  public UserJSResources getUserJSResources( HttpServletRequest httpRequest )
  {

    Object attribute = httpRequest.getSession().getAttribute( SESSION_VARIABLE_ELIGIBLE_PROMOTIONS );

    if ( Objects.isNull( attribute ) )
    {
      return getUserJSResourcesByAuthenticatedUser();
    }
    else
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)attribute;
      return getEligibleUserResourceByPromotionMenuBean( eligiblePromotions );

    }
  }

  @Override
  public UserJSResources getUserJSResourcesByAuthenticatedUser()
  {
    List<PromotionMenuBean> promotionMenuBeanList = mainContentService.buildEligiblePromoList( UserManager.getUser() );

    return getEligibleUserResourceByPromotionMenuBean( promotionMenuBeanList );
  }

  private UserJSResources getEligibleUserResourceByPromotionMenuBean( List<PromotionMenuBean> eligiblePromotions )
  {
    UserJSResources userResource = new UserJSResources();

    if ( CollectionUtils.isEmpty( eligiblePromotions ) )
    {
      return userResource;
    }
    else
    {

      List<ModuleType> userEligibleModuleTypes = new ArrayList<ModuleType>();
      eligiblePromotions.forEach( menuBean ->
      {
        if ( menuBean.isViewTile() && menuBean.isCanSubmit() )
        {
          userEligibleModuleTypes.add( ModuleType.getModuleTypeByPromotionTypeCode( menuBean.getPromotion().getPromotionType().getCode() ) );
        }
      } );

      for ( ModuleType moduleType : userEligibleModuleTypes )
      {
        userResource.addModuleResource( new ModuleResources( moduleType, jsResourcesMap.get( moduleType ) ) );
      }

    }
    return userResource;
  }

  public void setMainContentService( MainContentService mainContentService )
  {
    this.mainContentService = mainContentService;
  }

  public void setJsResourcesMap( Map<ModuleType, List<String>> jsResourcesMap )
  {
    this.jsResourcesMap = jsResourcesMap;
  }

}
