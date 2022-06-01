/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionFormRulesController.java,v $
 */

package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionClaimFormStepElementValidationType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

/**
 * PromotionFormRulesController.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionFormRulesController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionFormRulesForm promoFormRulesForm = (PromotionFormRulesForm)request.getAttribute( "promotionFormRulesForm" );

    request.setAttribute( "hasParent", Boolean.valueOf( promoFormRulesForm.isHasParent() ) );
    request.setAttribute( "promotionStatus", promoFormRulesForm.getPromotionStatus() );
    request.setAttribute( "pageNumber", "2" );
    if ( promoFormRulesForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      RecognitionPromotion promo = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( promoFormRulesForm.getPromotionId() ) );
      if ( promo.isIncludePurl() )
      {
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }
      if ( promo.isIncludeCelebrations() )
      {
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }

    if ( ObjectUtils.equals( promoFormRulesForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) || promoFormRulesForm.isHasParent()
        || ObjectUtils.equals( promoFormRulesForm.getPromotionStatus(), PromotionStatusType.LIVE ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    request.setAttribute( "promotionValidationType", PromotionClaimFormStepElementValidationType.getList() );
  }

  /**
   * Returns a reference to Promotion Service
   *
   * @return reference to Promotion Service
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
