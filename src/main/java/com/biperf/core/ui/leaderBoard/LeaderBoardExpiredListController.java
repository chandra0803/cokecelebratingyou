/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/leaderBoard/LeaderBoardExpiredListController.java,v $
 */

package com.biperf.core.ui.leaderBoard;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the ExpiredPromotionList page.
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
 * <td>sharafud</td>
 * <td>Aug 13, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LeaderBoardExpiredListController extends BaseController
{
  /**
   * Tiles controller for the LeaderBoardList page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // use a set so we don't have to worry about duplicate promotions getting added
    List expiredLeaderBoards = new ArrayList();
    // get the list of all expired leader boards
    expiredLeaderBoards = getLeaderBoardService().getLeaderBoardByStatus( LeaderBoard.EXPIRED );
    request.setAttribute( "expiredList", expiredLeaderBoards );
  }

  /**
   * Get the LeaderBoardService from the beanLocator.
   * 
   * @return LeaderBoardService
   */
  private LeaderBoardService getLeaderBoardService()
  {
    return (LeaderBoardService)getService( LeaderBoardService.BEAN_NAME );
  }
}
