
package com.biperf.core.ui.awardgenerator;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the AwardList page.
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
 * <td>chwhodhur</td>
 * <td>Jul 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class AwardGeneratorListController extends BaseController
{
  /**
   * Tiles controller for the AwardGeneratorList page Overridden from
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
    List awardGeneratorList = null;
    awardGeneratorList = getAwardGeneratorService().getAllActiveAwardGenerators();
    request.setAttribute( "awardGeneratorList", awardGeneratorList );
  }

  private AwardGeneratorService getAwardGeneratorService()
  {
    return (AwardGeneratorService)getService( AwardGeneratorService.BEAN_NAME );
  }

}
