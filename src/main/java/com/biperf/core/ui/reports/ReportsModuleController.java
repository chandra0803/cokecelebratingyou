
package com.biperf.core.ui.reports;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.reports.DashboardReportsService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class ReportsModuleController extends BaseController
{
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    String defaultDashboardChartIds = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_DASHBOARD_CHARTS ).getStringVal();

    // Save dashboard when empty
    Locale locale = UserManager.getLocale();
    getDashboardReportsService().saveUserDashboard( UserManager.getUserId(), defaultDashboardChartIds, locale );
  }

  protected DashboardReportsService getDashboardReportsService()
  {
    return (DashboardReportsService)getService( DashboardReportsService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

}
