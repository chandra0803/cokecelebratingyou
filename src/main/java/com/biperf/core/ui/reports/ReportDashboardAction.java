/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/ReportDashboardAction.java,v $
 */

package com.biperf.core.ui.reports;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.UserManager;

/**
 * ReportDashboardAction.
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
 * <td>arasi</td>
 * <td>May 14, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ReportDashboardAction extends BaseDispatchAction
{
  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ReportDashboardForm dashboardForm = (ReportDashboardForm)form;
    ReportDashboard reportDashboard = getReportsService().getUserDashboardById( UserManager.getUserId() );
    dashboardForm.load( reportDashboard );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ReportDashboardForm dashboardForm = (ReportDashboardForm)form;
    ReportDashboard reportDashboard = getReportsService().getUserDashboardById( UserManager.getUserId() );
    reportDashboard.setHighlights( dashboardForm.getHighlights() );
    List dashboardItemList = new ArrayList();
    dashboardItemList.addAll( reportDashboard.getReportDashboardItems() );
    reportDashboard.getReportDashboardItems().clear();
    getReportsService().saveReportDashboard( reportDashboard );
    dashboardForm.load( reportDashboard );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws Exception
   * @return ActionForward
   */
  public ActionForward removeItem( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ReportDashboardForm dashboardForm = (ReportDashboardForm)form;

    if ( dashboardForm.getDashboardItemId() != null )
    {
      dashboardForm.getDashboardItems().remove( Integer.parseInt( dashboardForm.getDashboardItemId() ) );
    }
    dashboardForm.setDashboardItemId( "" );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  private ReportsService getReportsService()
  {
    return (ReportsService)getService( ReportsService.BEAN_NAME );
  }

}
