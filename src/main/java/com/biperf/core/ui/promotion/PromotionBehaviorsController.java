/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionBehaviorsController.java,v $
 */

package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

/*
 * PromotionBehaviorsController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Oct
 * 11, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PromotionBehaviorsController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( PromotionBehaviorsController.class );

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    LOG.debug( ">>> execute" );
    PromotionBehaviorsForm behaviorsForm = (PromotionBehaviorsForm)request.getAttribute( "promotionBehaviorsForm" );

    if ( ObjectUtils.equals( behaviorsForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    request.setAttribute( "promotionStatus", behaviorsForm.getPromotionStatus() );
    if ( behaviorsForm.isRecognitionPromotion() )
    {
      request.setAttribute( "promotionBehaviorTypeList", PromoRecognitionBehaviorType.getList() );
    }
    if ( behaviorsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      RecognitionPromotion promo = (RecognitionPromotion)getPromotionService().getPromotionById( new Long( behaviorsForm.getPromotionId() ) );
      if ( promo.isIncludePurl() )
      {
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }
      else
      {
        request.setAttribute( "pageNumber", "6" );
      }

      if ( promo.isIncludeCelebrations() )
      {
        request.setAttribute( "pageNumber", 72 );
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }
    else if ( behaviorsForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "pageNumber", "4" );
    }
    else
    {
      if ( !behaviorsForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
      {
        request.setAttribute( "pageNumber", "6" );
      }
    }

    LOG.debug( "<<< execute" );
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
