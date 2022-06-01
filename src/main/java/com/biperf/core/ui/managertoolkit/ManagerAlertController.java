
package com.biperf.core.ui.managertoolkit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.AlertDurationType;
import com.biperf.core.domain.enums.NodeIncludeType;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;

public class ManagerAlertController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ManagerAlertForm managerAlertForm = (ManagerAlertForm)request.getAttribute( "managerAlertForm" );

    Long userId = null;
    String userIdString = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    if ( userIdString != null )
    {
      userId = new Long( userIdString );
    }
    if ( userId == null || userId.longValue() == 0 )
    {
      userId = UserManager.getUserId();
    }

    request.setAttribute( "userId", userId );
    request.setAttribute( "nodeIncludeList", NodeIncludeType.getList() );
    request.setAttribute( "durationList", AlertDurationType.getList() );
  }

}
