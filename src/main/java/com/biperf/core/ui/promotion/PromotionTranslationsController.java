/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

/**
 * PromotionTranslationsController.
 * 
 * @author arasi
 * @since 16-Aug-2012
 * @version 1.0
 */
public class PromotionTranslationsController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
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
    PromotionTranslationsForm promoTranslationsForm = (PromotionTranslationsForm)request.getAttribute( "promotionTranslationsForm" );
    Promotion promotion = getPromotionService().getPromotionById( new Long( promoTranslationsForm.getPromotionId() ) );

    if ( !promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "isLastPage", Boolean.TRUE );
    }

    List localeItems = getCMAssetService().getSupportedLocales( true );
    request.setAttribute( "localeItems", localeItems );

    if ( ObjectUtils.equals( promoTranslationsForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    request.setAttribute( "promotionStatus", promoTranslationsForm.getPromotionStatus() );

    if ( !promotion.isRecognitionPromotion() && !promotion.isNominationPromotion() && !promotion.isQuizPromotion() )
    {
      request.setAttribute( "isLastPage", Boolean.TRUE );
    }

    if ( promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
    {
      request.setAttribute( "pageNumber", "10" );
    }
    else if ( promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) || promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      if ( promotion.isRecognitionPromotion() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            request.setAttribute( "pageNumber", "84" );
          }
          else
          {
            request.setAttribute( "pageNumber", "68" );
          }
          request.setAttribute( "isPurlIncluded", Boolean.TRUE );
        }
        else
        {
          if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            request.setAttribute( "pageNumber", "79" );
          }
          else
          {
            request.setAttribute( "pageNumber", "13" );
          }
        }
        if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
        {
          request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
        }
      }
      else
      {
        request.setAttribute( "pageNumber", "13" );
      }
    }
    else if ( promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
    {
      request.setAttribute( "pageNumber", "8" );
    }
    else if ( promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      if ( promotion.getAwardType() != null && promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) && ( (GoalQuestPromotion)promotion ).getPartnerAudienceType() != null )
      {
        request.setAttribute( "pageNumber", "9" );
        request.setAttribute( "isPartnersEnabled", "true" );
      }
      else
      {
        request.setAttribute( "pageNumber", "8" );
        request.setAttribute( "isPartnersEnabled", "false" );
      }
    }
    else if ( promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      if ( promotion.getAwardType() != null && promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) && promotion.getPartnerAudienceType() != null )
      {
        request.setAttribute( "pageNumber", "24" );
        request.setAttribute( "isPartnersEnabled", "true" );
      }
      else
      {
        request.setAttribute( "pageNumber", "8" );
        request.setAttribute( "isPartnersEnabled", "false" );
      }

    }
    else if ( promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
    {
      request.setAttribute( "pageNumber", "6" );
    }
    else if ( promoTranslationsForm.getPromotionTypeCode().equals( PromotionType.SURVEY ) )
    {
      request.setAttribute( "pageNumber", "5" );
    }
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
