
package com.biperf.core.ui.reports;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.report.Report;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the ReportManageController page.
 *
 */
public class ReportManageController extends BaseController
{
  /**
   * Tiles controller for the ReportManageController page
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
    String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    List<Report> reportList = getFilteredReports();
    for ( Report report : reportList )
    {
      report.setUrl( systemUrl + report.getUrl() + "&resultsPerPage=" + Report.MAX_ROWS_TO_DISPLAY );
    }

    request.setAttribute( "reportList", reportList );

  }

  private List<Report> getFilteredReports() throws Exception
  {
    Boolean plateauPlatformOnly = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
    List<Report> reportList = getMainContentService().getAllReports();
    boolean isBIAdmin = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN );
    return ReportsUtils.getFilteredReports( reportList, plateauPlatformOnly, isBIAdmin );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

  /**
   * Get the SystemVariableService from the beanLocator.
   * 
   * @return SystemVariableService
   * @throws Exception
   */
  private SystemVariableService getSystemVariableService() throws Exception
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
  
  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }
}
