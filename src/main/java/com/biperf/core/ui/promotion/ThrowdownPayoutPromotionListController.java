
package com.biperf.core.ui.promotion;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.PromotionRoundValue;

public class ThrowdownPayoutPromotionListController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Set<PromotionRoundValue> promtoions = getThrowdownService().getPromotionsForPayout();
    request.setAttribute( "promotions", promtoions );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

}
