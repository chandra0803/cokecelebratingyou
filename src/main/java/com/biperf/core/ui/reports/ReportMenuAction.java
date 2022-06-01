
package com.biperf.core.ui.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

/**
 * ReportMenuAction.
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
 * <td>robinsra</td>
 * <td>Dec 14, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ReportMenuAction extends BaseDispatchAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( ReportMenuAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    List<Report> reportList = getFilteredReports();
    for ( Report report : reportList )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && report.getCmAssetCode().equalsIgnoreCase( "report.recognition.purlactivity" ) )
      {
        report.setUrl( getDMSAManagerDashboardUrl() );
        report.setName( "CELEBRATION_ACTIVITY_REPORT" );
      }
      else
      {
        report.setUrl( systemUrl + report.getUrl() + "&resultsPerPage=" + Report.MAX_ROWS_TO_DISPLAY );
      }
    }

    request.setAttribute( "reportList", reportList );
    boolean largeAudience = getSystemVariableService().getPropertyByName( SystemVariableService.REPORT_LARGE_AUDIENCE ).getBooleanVal();
    request.setAttribute( "largeAudience", largeAudience );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private List<Report> getFilteredReports()
  {
    Boolean plateauPlatformOnly = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
    List<Report> reportList = getMainContentService().getReportsAccessibleToUser();
    boolean isBIAdmin = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN );
    return ReportsUtils.getFilteredReports( reportList, plateauPlatformOnly, isBIAdmin );
  }

  private String getDMSAManagerDashboardUrl()
  {
    return "https://" + getSystemVariableService().getContextName() + "." + Environment.getEnvironmentSuffix() + ".myawardsite.com/dashboard";
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

}
