
package com.biperf.core.ui.leaderBoard;

import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class LeaderBoardController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    Date date = new Date();
    String todayDate = DateUtils.toDisplayString( date );
    String activityAsofLabelCMKey = CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.ACTIVITY_AS_OF" );
    MessageFormat formatter = new MessageFormat( activityAsofLabelCMKey );
    String activityAsofLabelCMDesc = formatter.format( new String[] { todayDate } );
    request.setAttribute( "activityAsof", activityAsofLabelCMDesc );

    boolean displayLeaderBoard = getSystemVariableService().getPropertyByName( SystemVariableService.LEADERBOARD_SHOW_HIDE ).getBooleanVal();
    request.setAttribute( "displayLeaderBoard", displayLeaderBoard );

    // set the role in the scope
    request.setAttribute( "isAdmin", isAdmin() );
    request.setAttribute( "isDelegate", isDelegate() );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
