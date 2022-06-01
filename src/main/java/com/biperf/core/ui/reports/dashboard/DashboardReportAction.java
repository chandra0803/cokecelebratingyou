
package com.biperf.core.ui.reports.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ReportParameterType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportDashboardItemParam;
import com.biperf.core.domain.report.ReportParameter;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.DashboardReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParameterInfo;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

public class DashboardReportAction extends BaseReportsAction
{

  @Override
  protected String getReportCode()
  {
    return Report.USER_DASHBOARD;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.dashboard.extract";
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    return "NOT IMPLEMENTED";
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return null;
  }

  protected String[] getColumnHeaders()
  {
    return null;
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    String actionForward = "display_dashboard";
    DashboardReportForm dashboardReportForm = (DashboardReportForm)form;
    if ( request.getParameter( "newIndex" ) != null )
    {
      ReportDashboardItem reportDashboardItem = getDashboardReportsService().getUserDashboardItem( UserManager.getUserId(), dashboardReportForm.getNewIndex() );
      ReportDashboard reportDashboard = new ReportDashboard();
      List<ReportDashboardItem> reportDashboardItems = new ArrayList<ReportDashboardItem>();
      reportDashboardItems.add( reportDashboardItem );
      reportDashboard.setReportDashboardItems( reportDashboardItems );
      request.setAttribute( "reportData", reportDashboard );
      actionForward = "display_individual_dashboard_item";
    }
    else
    {
      ReportDashboard reportDashboard = getDashboardReportsService().getUserDashboard( UserManager.getUserId() );
      request.setAttribute( "reportData", reportDashboard );
    }
    return mapping.findForward( actionForward );
  }

  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    DashboardReportForm dashboardReportForm = (DashboardReportForm)form;
    ReportDashboard reportDashboard = getDashboardReportsService().removeChart( dashboardReportForm.getReportDashboardId(), dashboardReportForm.getReportDashboardItemId() );
    request.setAttribute( "reportData", reportDashboard );
    return mapping.findForward( "display_update_dashboard_item" );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    DashboardReportForm dashboardReportForm = (DashboardReportForm)form;
    ReportParametersForm reportParametersForm = (ReportParametersForm)request.getSession().getAttribute( ReportParametersForm.FORM_NAME );
    List<ReportDashboardItemParam> reportDashboardItemParams = new ArrayList<ReportDashboardItemParam>();
    for ( ReportParameterInfo reportParameterInfo : reportParametersForm.getReportParameterInfoList() )
    {
      ReportDashboardItemParam reportDashboardItemParam = new ReportDashboardItemParam();
      ReportParameter reportParameter = new ReportParameter();
      reportParameter.setId( reportParameterInfo.getId() );
      reportDashboardItemParam.setReportParameter( reportParameter );
      if ( ReportParameterType.DATE_PICKER.equals( reportParameterInfo.getType() ) )
      {
        Date dateVal = DateUtils.toDate( reportParameterInfo.getParameterValue() );
        String value = DateUtils.toDisplayString( dateVal, Locale.US );
        reportDashboardItemParam.setValue( value );
      }
      else
      {
        reportDashboardItemParam.setValue( reportParameterInfo.getParameterValue() );
      }
      reportDashboardItemParam.setAutoUpdate( reportParameterInfo.getAutoUpdate() );
      reportDashboardItemParams.add( reportDashboardItemParam );
    }

    ReportDashboard reportDashboard = getDashboardReportsService().saveReportDashBoardItem( dashboardReportForm.getChartId(),
                                                                                            UserManager.getUserId(),
                                                                                            reportDashboardItemParams,
                                                                                            reportParametersForm.isNodeAndBelow() );
    request.setAttribute( "reportData", reportDashboard );
    return mapping.findForward( "display_dashboard" );
  }

  public ActionForward reOrder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    DashboardReportForm dashboardReportForm = (DashboardReportForm)form;
    ReportDashboard reportDashboard = getDashboardReportsService().reOrder( dashboardReportForm.getReportDashboardId(),
                                                                            dashboardReportForm.getReportDashboardItemId(),
                                                                            dashboardReportForm.getNewIndex() );
    request.setAttribute( "reportData", reportDashboard );
    return mapping.findForward( "display_update_dashboard_item" );
  }

  protected DashboardReportsService getDashboardReportsService()
  {
    return (DashboardReportsService)getService( DashboardReportsService.BEAN_NAME );
  }

}
