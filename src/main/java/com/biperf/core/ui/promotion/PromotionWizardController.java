
package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

public class PromotionWizardController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    PromotionWizardForm promotionWizardForm = (PromotionWizardForm)request.getAttribute( "promotionWizardForm" );

    Promotion promotion = getPromotionService().getPromotionById( promotionWizardForm.getPromotionId() );

    if ( promotion.isNominationPromotion() )
    {
      request.setAttribute( "isLastPage", Boolean.TRUE );
    }

    request.setAttribute( "pageNumber", "14" );
    if ( promotion.isExpired() )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
