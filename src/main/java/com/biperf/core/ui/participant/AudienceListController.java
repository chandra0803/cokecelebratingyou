/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/AudienceListController.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.PresentationUtils;

/**
 * Implements the controller for the ListBuilder page.
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
 * <td>leep</td>
 * <td>June 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceListController extends BaseController
{
  /**
   * Tiles controller for the ListBuilder page
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
    // set the list of Positions in the request
    List audienceList = getAudienceService().getAudienceList();
    request.setAttribute( "allAudiences", audienceList );
    request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( audienceList.size() ) ) );
  }

  /**
   * Gets a ProductCategoryService
   * 
   * @return ProductCategoryService
   * @throws Exception
   */
  private AudienceService getAudienceService() throws Exception
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  } // end
}
