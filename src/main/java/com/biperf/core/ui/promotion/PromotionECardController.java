/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionECardController.java,v $
 */

package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

/**
 * PromotionEcardController.
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
 * <td>sedey</td>
 * <td>Oct 05, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionECardController extends BaseController
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

    PromotionECardForm ecardForm = (PromotionECardForm)request.getAttribute( "promotionECardForm" );

    request.setAttribute( "promotionStatus", ecardForm.getPromotionStatus() );
    // Wizard needs screen needs page number
    if ( !PromotionType.NOMINATION.equals( ecardForm.getPromotionTypeCode() ) )
    {
      request.setAttribute( "pageNumber", "7" );
    }

    if ( ObjectUtils.equals( ecardForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    if ( PromotionType.RECOGNITION.equals( ecardForm.getPromotionTypeCode() ) )
    {
      Promotion promo = getPromotionService().getPromotionById( new Long( ecardForm.getPromotionId() ) );
      if ( promo.isRecognitionPromotion() && ( (RecognitionPromotion)promo ).isIncludePurl() )
      {
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }

      if ( promo.isRecognitionPromotion() && ( (RecognitionPromotion)promo ).isIncludeCelebrations() )
      {
        request.setAttribute( "pageNumber", 73 );
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }

    if ( PromotionType.NOMINATION.equals( ecardForm.getPromotionTypeCode() ) )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)getPromotionService().getPromotionById( new Long( ecardForm.getPromotionId() ) );
      if ( NominationEvaluationType.CUMULATIVE.equals( nominationPromotion.getEvaluationType().getCode() ) )
      {
        // when cumulative, ecards disabled.
        request.setAttribute( "disableEcards", Boolean.TRUE );
      }
      request.setAttribute( "pageNumber", "5" );
    }
    request.setAttribute( "eCard", Boolean.TRUE );
    request.setAttribute( "hasParent", "false" );
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
