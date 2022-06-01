/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.ui.BaseController;

/**
 * StackRankHistoryController.
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
 * <td>Mar 14, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankHistoryController extends BaseController
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
    StackRankService stackRankService = (StackRankService)getService( StackRankService.BEAN_NAME );

    StackRankHistoryForm stackRankHistoryForm = (StackRankHistoryForm)request.getAttribute( StackRankHistoryForm.FORM_NAME );

    Long promotionId = stackRankHistoryForm.getPromotionId();
    Long stackRankId = stackRankHistoryForm.getStackRankId();
    Long nodeTypeId = stackRankHistoryForm.getNodeTypeId();

    StackRankQueryConstraint stackRankQueryConstraint = new StackRankQueryConstraint();
    stackRankQueryConstraint.setPromotionIdsIncluded( new Long[] { promotionId } );
    stackRankQueryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) } );

    List stackRankList = stackRankService.getStackRankList( stackRankQueryConstraint );
    request.setAttribute( "stackRankList", stackRankList );

    Set nodeTypeList = new LinkedHashSet();
    List nodeList = new ArrayList();

    if ( stackRankId != null )
    {
      nodeTypeList = stackRankService.getNodeTypesByStackRankId( stackRankId );
      if ( nodeTypeId != null )
      {
        nodeList = stackRankService.getNodesByStackRankIdAndNodeTypeId( stackRankId, nodeTypeId );
      }
    }

    request.setAttribute( "nodeTypeList", nodeTypeList );
    request.setAttribute( "nodeList", nodeList );

  }

}
