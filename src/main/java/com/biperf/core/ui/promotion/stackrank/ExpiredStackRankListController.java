/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.promotion.hibernate.ProductClaimPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.ui.BaseController;

/**
 * ExpiredStackRankListController.
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
 * <td>zahler</td>
 * <td>Mar 9, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ExpiredStackRankListController extends BaseController
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
    List promotionStackRankList = null;
    List stackRankFormBeanList = null;

    // create the constraint
    ProductClaimPromotionQueryConstraint constraint = new ProductClaimPromotionQueryConstraint();
    constraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) } );
    constraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    constraint.setPromotionPayoutTypesIncluded( new PromotionPayoutType[] { PromotionPayoutType.lookup( PromotionPayoutType.STACK_RANK ) } );

    promotionStackRankList = getPromotionService().getPromotionList( constraint );
    stackRankFormBeanList = new ArrayList();

    // now get the count (of StackRank obj, by passing state(StackRankState),promotionId
    // using StackRankQueryConstraint for above promotion list
    for ( Iterator iter = promotionStackRankList.iterator(); iter.hasNext(); )
    {
      ProductClaimPromotion promotion = (ProductClaimPromotion)iter.next();
      Long promotionId = promotion.getId();

      // now create stack rank query constraint
      StackRankQueryConstraint queryConstraint = new StackRankQueryConstraint();
      // set the promotionId
      queryConstraint.setPromotionIdsIncluded( new Long[] { promotionId } );
      // set the state - history
      queryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) } );
      // get the count now,
      int historyCount = getStackRankService().getStackRankListCount( queryConstraint );

      // create the bean and set all the values
      StackRankFormBean bean = new StackRankFormBean();
      bean.setPromotionId( promotionId );
      bean.setPromotionName( promotion.getName() );
      bean.setDisplayHistory( historyCount > 0 ? true : false );

      // finally add the bean to the list
      stackRankFormBeanList.add( bean );
    }

    request.setAttribute( "stackRankFormBeanList", stackRankFormBeanList );

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

  /**
   * Get the StackRankService from the beanLocator.
   * 
   * @return StackRankService
   */
  private StackRankService getStackRankService()
  {
    return (StackRankService)getService( StackRankService.BEAN_NAME );
  }
}
