/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionSweepstakesListController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.value.PromotionSweepstakesListValueBean;

/**
 * Implements the controller for the PromotionList page.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * </tr>
 * <tr>
 * <td>potosky</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakesListController extends BaseController
{
  /**
   * Tiles controller for the PromotionSweepstakesList page Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    // TODO look for the request param which defines the promotions with a specific type to be
    // displayed.
    List<Promotion> promotionList = new ArrayList<Promotion>();
    List<Badge> badgeList = new ArrayList<Badge>();

    /* ***** Bug # 34020 start - speed up list */
    /*
     * AssociationRequestCollection associationRequestCollection = new
     * AssociationRequestCollection(); associationRequestCollection .add( new
     * PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
     * promotionList = getPromotionService() .getAllWithSweepstakesWithAssociations(
     * associationRequestCollection );
     */
    promotionList = getPromotionService().getAllPromotionsWithSweepstakes();
    badgeList = getGamificationService().getAllEligibleBadges();

    for ( Badge badge : badgeList )
    {
      promotionList.add( (Promotion)badge );
    }

    /* ***** Bug # 34020 end */

    // Only show promotions that have sweepstakes active, iterate over the list of all promotions
    // and if it is a quiz or recognition promo, check to see if sweepstakes are active, if not
    // remove them from the list.

    Iterator promoListIter = promotionList.iterator();
    while ( promoListIter.hasNext() )
    {
      Promotion promo = (Promotion)promoListIter.next();
      // do not show any under construction or complete promos, must be live or expired
      if ( promo.isUnderConstruction() || promo.isComplete() )
      {
        promoListIter.remove();
      }
    }

    /* ***** Bug # 34020 start */
    List newPromotionList = new ArrayList();
    Iterator promoListIter2 = promotionList.iterator();
    while ( promoListIter2.hasNext() )
    {

      Promotion promo = (Promotion)promoListIter2.next();

      PromotionSweepstakesListValueBean ListValueBean = new PromotionSweepstakesListValueBean();

      ListValueBean.setPromotion( promo );

      int notProcessedCount = getPromotionSweepstakeService().getPromotionSweepstakesNotProcessedCount( promo.getId() );
      if ( notProcessedCount > 0 )
      {
        ListValueBean.setHasSweepstakesToProcess( true );
      }
      else
      {
        ListValueBean.setHasSweepstakesToProcess( false );
      }

      int historyCount = getPromotionSweepstakeService().getPromotionSweepstakesHistoryCount( promo.getId() );
      if ( historyCount > 0 )
      {
        ListValueBean.setHasSweepstakesHistory( true );
      }
      else
      {
        ListValueBean.setHasSweepstakesHistory( false );
      }
      newPromotionList.add( ListValueBean );

    }

    request.setAttribute( "promotionList", newPromotionList );

    // This is used if the user navigates to "view parameters"
    request.getSession().setAttribute( ViewAttributeNames.PAGE_MODE, ViewAttributeNames.SWEEPS_MODE );

  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /* ***** Bug # 34020 start */
  /**
   * Get the PromotionSweepstakeService from the beanLocator.
   * 
   * @return PromotionSweepstakeService
   */
  private PromotionSweepstakeService getPromotionSweepstakeService()
  {
    return (PromotionSweepstakeService)getService( PromotionSweepstakeService.BEAN_NAME );
  }
  /* ***** Bug # 34020 end */

  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

}
