
package com.biperf.core.ui.homepage;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class ManagerToolKitController extends BaseController
{
  /**
   * Overridden from @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    AuthenticatedUser user = UserManager.getUser();
    if ( user.isParticipant() )
    {
      Participant participant = getParticipantService().getParticipantById( user.getUserId() );
      Map<String, Object> options = getFilterAppSetupService().getToolkitOptions( participant, true );

      request.setAttribute( "displayLeaderBoard", options.get( "displayLeaderBoard" ) );
      request.setAttribute( "displayBudgetTransfer", options.get( "displayBudgetTransfer" ) );
      request.setAttribute( "displayRosterMgmt", options.get( "displayRosterMgmt" ) );
      request.setAttribute( "displayManageQuizzes", options.get( "displayManageQuizzes" ) );
      request.setAttribute( "displayManageContests", options.get( "displayManageContests" ) );
      request.setAttribute( "showResource", options.get( "showResource" ) );
      request.setAttribute( "showDIYCommunication", options.get( "showDIYCommunication" ) );
      request.setAttribute( "showToolkit", options.get( "showToolkit" ) );
    }
  }

  public static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  public static FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

}
