/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/DashboardReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.reports.DashboardReportDAO;
import com.biperf.core.dao.system.SystemVariableDAO;
import com.biperf.core.domain.report.ReportChart;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportDashboardItemParam;
import com.biperf.core.domain.report.ReportParameter;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.reports.DashboardReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * DashboardReportsServiceImpl.
 * 
 * @author kandhi
 * @since Aug 15, 2012
 * @version 1.0
 */
public class DashboardReportsServiceImpl implements DashboardReportsService
{

  private static final String BEGIN_DAY_MONTH = "01/01/";
  private static final String FILTER_TO_DATE = "toDate";
  private static final String FILTER_FROM_DATE = "fromDate";

  private DashboardReportDAO dashboardReportDAO;
  private UserDAO userDAO;
  private SystemVariableDAO systemVariableDAO;

  @Override
  public ReportDashboard getUserDashboard( Long paxId )
  {
    return dashboardReportDAO.getUserDashboard( paxId );
  }

  public void deleteUserDashboard( Long userId )
  {
    ReportDashboard reportDashbaord = this.dashboardReportDAO.getUserDashboard( userId );
    if ( reportDashbaord != null )
    {
      dashboardReportDAO.deleteUserDashboard( reportDashbaord );
    }
  }

  @Override
  public ReportDashboard removeChart( Long reportDashboardId, Long reportDashboardItemId )
  {
    ReportDashboard reportDashboard = dashboardReportDAO.getReportDashBoardById( reportDashboardId );

    ReportDashboardItem reportDashboardItem = dashboardReportDAO.getReportDashBoardItemById( reportDashboardItemId );

    reportDashboard.getReportDashboardItems().remove( reportDashboardItem );

    return reportDashboard;
  }

  @Override
  public ReportDashboard reOrder( Long reportDashboardId, Long reportDashboardItemId, int newIndex )
  {
    ReportDashboard reportDashboard = dashboardReportDAO.getReportDashBoardById( reportDashboardId );

    ReportDashboardItem reportDashboardItem = dashboardReportDAO.getReportDashBoardItemById( reportDashboardItemId );

    reportDashboard.getReportDashboardItems().remove( reportDashboardItem );

    if ( newIndex < reportDashboard.getReportDashboardItems().size() )
    {
      reportDashboard.getReportDashboardItems().add( newIndex, reportDashboardItem );
    }
    else
    {
      reportDashboard.getReportDashboardItems().add( reportDashboardItem );
    }
    return reportDashboard;
  }

  @Override
  public ReportDashboardItem getUserDashboardItem( Long paxId, int seqNum )
  {
    ReportDashboardItem reportDashboardItem = null;
    ReportDashboard reportDashboard = dashboardReportDAO.getUserDashboard( paxId );
    if ( reportDashboard != null )
    {
      List<ReportDashboardItem> reportDashBoardItemList = reportDashboard.getReportDashboardItems();
      if ( reportDashBoardItemList != null )
      {
        for ( ReportDashboardItem attachedReportDashboardItem : reportDashBoardItemList )
        {
          // Use the attachedReportDashBoardItem if the report chart for that item is active
          if ( attachedReportDashboardItem != null && attachedReportDashboardItem.getSequenceNum() == seqNum && attachedReportDashboardItem.getReportChart().getReport().isActive() )
          {
            reportDashboardItem = attachedReportDashboardItem;
            break;
          }
          // If the report is not active for the attachedReportDashboardItem, then set the default
          // reports
          else if ( attachedReportDashboardItem != null && attachedReportDashboardItem.getSequenceNum() == seqNum && !attachedReportDashboardItem.getReportChart().getReport().isActive() )
          {
            // Get all the defaultDashBoardChartIds and use the first chartIds as the reportChart
            String defaultDashboardChartIds = systemVariableDAO.getPropertyByName( SystemVariableService.DEFAULT_DASHBOARD_CHARTS ).getStringVal();
            String[] defaultChartId = defaultDashboardChartIds.split( "," );
            ReportChart reportChart = null;
            // Find the first available report chart from the list of default chart ids.
            for ( int i = 0; i < defaultChartId.length; i++ )
            {
              reportChart = dashboardReportDAO.getReportChartById( new Long( defaultChartId[i] ) );
              if ( reportChart != null && reportChart.getReport().isActive() )
              {
                break;
              }
            }
            attachedReportDashboardItem.setReportChart( reportChart );
            reportDashboardItem = attachedReportDashboardItem;
            break;
          }
        }
      }
    }
    return reportDashboardItem;
  }

  @Override
  public void saveUserDashboard( Long paxId, String chartIds, Locale locale )
  {
    ReportDashboardItem reportDashboardItem = null;
    ReportDashboard reportDashboard = null;
    // ReportDashboard reportDashboard = dashboardReportDAO.getUserDashboard( paxId );
    // if ( reportDashboard == null )
    if ( !dashboardReportDAO.dashboardExistsForUser( paxId ) )
    { // Create the dashboard with default charts
      ReportDashboard reportDashboards = dashboardReportDAO.getUserDashboard( paxId );
      // Doing a null check to see if report dash boards are not null. If not, clearing them.
      if ( reportDashboards != null )
      {
        dashboardReportDAO.deleteUserDashboard( reportDashboards );
      }
      String[] chartIdsStr = chartIds.split( "," );
      reportDashboard = new ReportDashboard();
      for ( String chartId : chartIdsStr )
      {
        reportDashboardItem = new ReportDashboardItem();
        ReportChart reportChart = dashboardReportDAO.getReportChartById( new Long( chartId ) );
        for ( ReportParameter reportParameter : reportChart.getReport().getReportParameters() )
        {
          ReportDashboardItemParam reportDashboardItemParam = new ReportDashboardItemParam();
          /* START tuning of saving dashboard */
          /*
           * it is taking very long to add item param to report parameter. in
           * report_parameter.hbm.xml , inverse is set to true on reportDashboardItemParams so that
           * save is managed separately. Also in ReportDashboadItemParam.hbm.xml insert and update
           * are set to true on REPORT_PARAMETER_ID so that it can be updated without adding to the
           * set
           */
          // reportParameter.addReportDashboardItemParam( reportDashboardItemParam );
          /* END tuning of saving dashboard */
          reportDashboardItemParam.setReportParameter( reportParameter );
          reportDashboardItemParam.setReportDashboardItem( reportDashboardItem );
          String value = reportParameter.getDefaultValue();
          if ( FILTER_FROM_DATE.equals( reportParameter.getParameterName() ) )
          {
            value = getFirstDateOfTheYear();
          }
          else if ( FILTER_TO_DATE.equals( reportParameter.getParameterName() ) )
          {
            value = DateUtils.toDisplayString( DateUtils.getCurrentDate(), locale );
            reportDashboardItemParam.setAutoUpdate( Boolean.TRUE );
          }
          reportDashboardItemParam.setValue( value );
          reportDashboardItem.addReportDashboardItemParam( reportDashboardItemParam );
        }
        reportDashboardItem.setReportChart( reportChart );
        reportDashboard.addReportDashboardItem( reportDashboardItem );
      }
      User user = userDAO.getUserById( paxId );
      reportDashboard.setUser( user );
      reportDashboard = (ReportDashboard)HibernateUtil.saveOrUpdateOrDeepMerge( reportDashboard );
    }
  }

  private static String getFirstDateOfTheYear()
  {
    return BEGIN_DAY_MONTH + Calendar.getInstance().get( Calendar.YEAR );
  }

  @Override
  public ReportDashboardItem getUserDashboardItemById( Long reportDashboardItemId )
  {
    return dashboardReportDAO.getReportDashBoardItemById( reportDashboardItemId );
  }

  @Override
  public ReportDashboard saveReportDashBoardItem( Long chartId, Long paxId, List<ReportDashboardItemParam> managedReportDashboardItemParams, Boolean nodeAndBelow )
  {
    ReportDashboard attachedReportDashboard = dashboardReportDAO.getUserDashboard( paxId );
    if ( attachedReportDashboard == null )
    {
      attachedReportDashboard = new ReportDashboard();
    }
    ReportChart reportChart = dashboardReportDAO.getReportChartById( chartId );

    ReportDashboardItem reportDashboardItem = new ReportDashboardItem();
    for ( ReportDashboardItemParam reportDashboardItemParam : managedReportDashboardItemParams )
    {
      ReportParameter reportParameter = dashboardReportDAO.getReportParameterById( reportDashboardItemParam.getReportParameter().getId() );
      /* START tuning of saving dashboard */
      /*
       * it is taking very long to add item param to report parameter. in report_parameter.hbm.xml ,
       * inverse is set to true on reportDashboardItemParams so that save is managed separately.
       * Also in ReportDashboadItemParam.hbm.xml insert and update are set to true on
       * REPORT_PARAMETER_ID so that it can be updated without adding to the set
       */
      // reportParameter.addReportDashboardItemParam( reportDashboardItemParam );
      /* END tuning of saving dashboard */
      reportDashboardItemParam.setReportParameter( reportParameter );
      reportDashboardItem.addReportDashboardItemParam( reportDashboardItemParam );
    }
    reportDashboardItem.setReportChart( reportChart );
    reportDashboardItem.setNodeAndBelow( nodeAndBelow );
    attachedReportDashboard.addReportDashboardItem( reportDashboardItem );
    if ( attachedReportDashboard.getId() == null )
    {
      attachedReportDashboard.setUser( userDAO.getUserById( paxId ) );
      attachedReportDashboard = (ReportDashboard)HibernateUtil.saveOrUpdateOrDeepMerge( attachedReportDashboard );
    }
    return attachedReportDashboard;
  }

  public boolean dashboardExistsForUser( Long userId )
  {

    return this.dashboardReportDAO.dashboardExistsForUser( userId );
  }

  public void setDashboardReportDAO( DashboardReportDAO dashboardReportDAO )
  {
    this.dashboardReportDAO = dashboardReportDAO;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  public void setSystemVariableDAO( SystemVariableDAO systemVariableDAO )
  {
    this.systemVariableDAO = systemVariableDAO;
  }

}
