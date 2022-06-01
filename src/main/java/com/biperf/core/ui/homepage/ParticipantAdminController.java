
package com.biperf.core.ui.homepage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.maincontent.Menu;
import com.biperf.core.ui.BaseController;

/**
 * ParticipantAdminController.
 * 
 *
 */
public class ParticipantAdminController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**  
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    Menu paxAdminMenu = getMainContentService().buildG3ReduxParticipantAdminMenuPage();
    request.setAttribute( "paxAdminMenu", paxAdminMenu );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }
}
