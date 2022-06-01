/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/DashboardReportDAOImpl.java,v $
 */

package com.biperf.core.dao.reports.hibernate;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.reports.DashboardReportDAO;
import com.biperf.core.domain.report.ReportChart;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportParameter;

/**
 * DashboardReportDAOImpl.
 * 
 * @author kandhi
 * @since Aug 15, 2012
 * @version 1.0
 */
public class DashboardReportDAOImpl extends BaseDAO implements DashboardReportDAO
{

  public ReportDashboard getUserDashboard( Long paxId )
  {
    if ( paxId == null || paxId.longValue() <= 0 )
    {
      return null;
    }
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getUserDashboard" );
    query.setLong( "paxId", paxId.longValue() );
    return (ReportDashboard)query.uniqueResult();
  }

  public void deleteUserDashboard( ReportDashboard reportDashboard )
  {

    getSession().delete( reportDashboard );
  }

  @Override
  public ReportDashboard getReportDashBoardById( Long reportDashboardId )
  {
    return (ReportDashboard)getSession().get( ReportDashboard.class, reportDashboardId );
  }

  @Override
  public ReportDashboardItem getReportDashBoardItemById( Long reportDashboardItemId )
  {
    return (ReportDashboardItem)getSession().get( ReportDashboardItem.class, reportDashboardItemId );
  }

  @Override
  public ReportChart getReportChartById( Long chartId )
  {
    return (ReportChart)getSession().get( ReportChart.class, chartId );
  }

  @Override
  public ReportParameter getReportParameterById( Long reportParameterId )
  {
    return (ReportParameter)getSession().get( ReportParameter.class, reportParameterId );
  }

  @Override
  public boolean dashboardExistsForUser( Long paxId )
  {
    if ( paxId == null || paxId.longValue() <= 0 )
    {
      return false;
    }
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getUserDashboardExists" );
    query.setLong( "paxId", paxId.longValue() );
    Integer count = (Integer)query.uniqueResult();
    return count > 0;
  }

}
