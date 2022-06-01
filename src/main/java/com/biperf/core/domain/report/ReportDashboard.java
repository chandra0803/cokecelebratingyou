/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/report/ReportDashboard.java,v $
 */

package com.biperf.core.domain.report;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

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
public class ReportDashboard extends BaseDomain
{
  private User user;
  private String highlights;
  private List<ReportDashboardItem> reportDashboardItems = new ArrayList<ReportDashboardItem>();

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public String getHighlights()
  {
    return highlights;
  }

  public void setHighlights( String highlights )
  {
    this.highlights = highlights;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return
   */
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( highlights == null ? 0 : highlights.hashCode() );
    result = prime * result + ( user == null ? 0 : user.hashCode() );
    return result;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param obj
   * @return
   */
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    ReportDashboard other = (ReportDashboard)obj;
    if ( highlights == null )
    {
      if ( other.highlights != null )
      {
        return false;
      }
    }
    else if ( !highlights.equals( other.highlights ) )
    {
      return false;
    }
    if ( user == null )
    {
      if ( other.user != null )
      {
        return false;
      }
    }
    else if ( !user.equals( other.user ) )
    {
      return false;
    }
    return true;
  }

  public List<ReportDashboardItem> getReportDashboardItems()
  {
    return reportDashboardItems;
  }

  public void setReportDashboardItems( List<ReportDashboardItem> reportDashboardItems )
  {
    this.reportDashboardItems = reportDashboardItems;
  }

  public void addReportDashboardItem( ReportDashboardItem reportDashboardItem )
  {
    reportDashboardItem.setReportDashboard( this );
    this.reportDashboardItems.add( reportDashboardItem );
  }

}
