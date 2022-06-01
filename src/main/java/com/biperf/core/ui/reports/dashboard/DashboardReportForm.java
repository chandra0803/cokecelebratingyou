
package com.biperf.core.ui.reports.dashboard;

import com.biperf.core.ui.reports.ReportParametersForm;

public class DashboardReportForm extends ReportParametersForm
{

  private static final long serialVersionUID = 149587116962173295L;

  private Long reportDashboardId;
  private Long reportDashboardItemId;
  private int newIndex;
  private Long chartId;

  public Long getReportDashboardId()
  {
    return reportDashboardId;
  }

  public void setReportDashboardId( Long reportDashboardId )
  {
    this.reportDashboardId = reportDashboardId;
  }

  public Long getReportDashboardItemId()
  {
    return reportDashboardItemId;
  }

  public void setReportDashboardItemId( Long reportDashboardItemId )
  {
    this.reportDashboardItemId = reportDashboardItemId;
  }

  public int getNewIndex()
  {
    return newIndex;
  }

  public void setNewIndex( int newIndex )
  {
    this.newIndex = newIndex;
  }

  public Long getChartId()
  {
    return chartId;
  }

  public void setChartId( Long chartId )
  {
    this.chartId = chartId;
  }

}
