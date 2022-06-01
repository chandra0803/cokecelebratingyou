/**
 *
 */

package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;

/**
 * 
 * SSIPromotionAwardsController.
 * 
 * @author chowdhur
 * @since Oct 22, 2014
 */
public class PromotionSSITranslationsController extends BaseController
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
    PromotionSSITranslationsForm promotionSSITranslationsForm = (PromotionSSITranslationsForm)request.getAttribute( "promotionSSITranslationsForm" );
    request.setAttribute( "promotionStatus", promotionSSITranslationsForm.getPromotionStatus() );
    request.setAttribute( "pageNumber", "7" );
    request.setAttribute( "isLastPage", Boolean.TRUE );
    request.setAttribute( "isPageEditable", Boolean.TRUE );
  }

}
