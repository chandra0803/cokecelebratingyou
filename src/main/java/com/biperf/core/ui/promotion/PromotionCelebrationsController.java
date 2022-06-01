
package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionCelebrationGenericEcardsType;
import com.biperf.core.domain.enums.PromotionCelebrationsImageType;
import com.biperf.core.domain.enums.PromotionCelebrationsVideoType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

public class PromotionCelebrationsController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "isLastPage", Boolean.FALSE );
    request.setAttribute( "isPageEditable", Boolean.TRUE );
    PromotionCelebrationsForm promoCelebrationsForm = (PromotionCelebrationsForm)request.getAttribute( "promotionCelebrationsForm" );

    RecognitionPromotion promo = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( promoCelebrationsForm.getPromotionId() ) );

    if ( promo.isIncludeCelebrations() )
    {
      request.setAttribute( "pageNumber", 70 );
    }
    if ( promo.isIncludePurl() )
    {
      request.setAttribute( "isPurlIncluded", Boolean.TRUE );
    }
    request.setAttribute( "celebrationsVideoList", PromotionCelebrationsVideoType.getList() );
    request.setAttribute( "celebrationsImageList", PromotionCelebrationsImageType.getList() );
    request.setAttribute( "celebrationsEcardList", PromotionCelebrationGenericEcardsType.getList() );
  }

  /**
   * Does a Bean lookup for the PromotionService
   * 
   * @return PromotionService
   */
  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
