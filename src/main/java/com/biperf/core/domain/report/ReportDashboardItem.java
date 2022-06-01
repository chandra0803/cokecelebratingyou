/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/report/ReportDashboardItem.java,v $
 */

package com.biperf.core.domain.report;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

/**
 * ReportDashboard.
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
 * <td>babu</td>
 * <td>May 8, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ReportDashboardItem extends BaseDomain
{
  private ReportDashboard reportDashboard;
  private ReportChart reportChart;
  private int sequenceNum;
  private Boolean nodeAndBelow = true;
  private Set<ReportDashboardItemParam> reportDashboardItemParams = new LinkedHashSet<ReportDashboardItemParam>();

  public Boolean getNodeAndBelow()
  {
    return nodeAndBelow;
  }

  public void setNodeAndBelow( Boolean nodeAndBelow )
  {
    this.nodeAndBelow = nodeAndBelow;
  }

  public ReportDashboard getReportDashboard()
  {
    return reportDashboard;
  }

  public void setReportDashboard( ReportDashboard reportDashboard )
  {
    this.reportDashboard = reportDashboard;
  }

  public ReportChart getReportChart()
  {
    return reportChart;
  }

  public void setReportChart( ReportChart reportChart )
  {
    this.reportChart = reportChart;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public Set<ReportDashboardItemParam> getReportDashboardItemParams()
  {
    return reportDashboardItemParams;
  }

  public void setReportDashboardItemParams( Set<ReportDashboardItemParam> reportDashboardItemParams )
  {
    this.reportDashboardItemParams = reportDashboardItemParams;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( reportChart == null ? 0 : reportChart.hashCode() );
    result = prime * result + ( reportDashboard == null ? 0 : reportDashboard.hashCode() );
    result = prime * result + sequenceNum;
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    ReportDashboardItem other = (ReportDashboardItem)obj;
    if ( reportChart == null )
    {
      if ( other.reportChart != null )
      {
        return false;
      }
    }
    else if ( !reportChart.equals( other.reportChart ) )
    {
      return false;
    }
    if ( reportDashboard == null )
    {
      if ( other.reportDashboard != null )
      {
        return false;
      }
    }
    else if ( !reportDashboard.equals( other.reportDashboard ) )
    {
      return false;
    }
    if ( sequenceNum != other.sequenceNum )
    {
      return false;
    }
    return true;
  }

  public void addReportDashboardItemParam( ReportDashboardItemParam reportDashboardItemParam )
  {
    reportDashboardItemParam.setReportDashboardItem( this );
    this.reportDashboardItemParams.add( reportDashboardItemParam );
  }
}
