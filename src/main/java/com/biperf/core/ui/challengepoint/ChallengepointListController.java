/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/challengepoint/ChallengepointListController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.challengepoint;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.promotion.hibernate.ChallengepointPromotionQueryConstraint;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateUtils;

/**
 * ChallengepointListController.
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
 * <td>babu</td>
 * <td>Jul 25, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengepointListController extends BaseController
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
    ChallengepointPromotionQueryConstraint queryConstraint = new ChallengepointPromotionQueryConstraint();
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.CHALLENGE_POINT ) } );
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setEndSelectionDate( DateUtils.getCurrentDate() );
    List challengepointList = getPromotionService().getPromotionList( queryConstraint );
    request.setAttribute( "challengepointList", challengepointList );
    return;
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

}
