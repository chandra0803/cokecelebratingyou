/**
 *
 */

package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

/**
 * 
 * SSIPromotionAwardsController.
 * 
 * @author chowdhur
 * @since Oct 22, 2014
 */
public class PromotionSSIAwardsController extends BaseController
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
    PromotionSSIAwardsForm promotionSSIAwardsForm = (PromotionSSIAwardsForm)request.getAttribute( "promotionSSIAwardsForm" );
    request.setAttribute( "promotionStatus", promotionSSIAwardsForm.getPromotionStatus() );
    request.setAttribute( "pageNumber", "3" );
    request.setAttribute( "isLastPage", Boolean.FALSE );
    request.setAttribute( "isPageEditable", Boolean.TRUE );
    request.setAttribute( "promotionAwardsTypeList", PromotionAwardsType.getList() );
    request.setAttribute( "badgeList", getPromotionService().buildBadgeLibraryList() );
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
