
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
import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

public class ReportManageAction extends BaseDispatchAction
{
  /** Log */
  private static final Log LOG = LogFactory.getLog( ReportManageAction.class );

  public ActionForward activateInactivateReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ReportManageForm reportManageForm = (ReportManageForm)form;
    List<Report> reportList = getFilteredReports();

    try
    {
      // Go through all the report list and make updates for all the report's status that was
      // changed
      for ( Report report : reportList )
      {
        boolean foundNotUpdated = false;

        for ( int i = 0; i < reportManageForm.getUpdateValues().length; i++ )
        {
          // If the report is changed from inactive to active, update the report status in db
          if ( report.getId() == Long.parseLong( reportManageForm.getUpdateValues()[i] ) && !report.isActive() )
          {
            report.setActive( true );
            getReportsService().updateReports( report );
            break;
          }
          // If the report status did not get changed, don't update it
          else if ( report.getId() == Long.parseLong( reportManageForm.getUpdateValues()[i] ) )
          {
            foundNotUpdated = true;
          }
          // If the report is changed from active to inactive, update the report status in db
          else if ( i == reportManageForm.getUpdateValues().length - 1 && report.isActive() && !foundNotUpdated )
          {
            report.setActive( false );
            getReportsService().updateReports( report );
          }
        }
      }
    }
    catch( Exception e )
    {
      LOG.error( "ReportManageAction.update: failed to get service " + e.getMessage() );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );
  }

  private List<Report> getFilteredReports()
  {
    Boolean plateauPlatformOnly = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
    List<Report> reportList = getMainContentService().getAllReports();
    boolean isBIAdmin = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN );
    return ReportsUtils.getFilteredReports( reportList, plateauPlatformOnly, isBIAdmin );
  }

  protected ReportsService getReportsService()
  {
    return (ReportsService)getService( ReportsService.BEAN_NAME );
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
