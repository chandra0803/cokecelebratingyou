/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/loginreport/LoginByOrganizationReportValue.java,v $
 */

package com.biperf.core.value.loginreport;

/**
 * LoginByOrganizationReportValue.
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
 * <td>Mar 29, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class LoginByPaxReportValue
{
  private String name;
  private int totalCnt;

  // Participation report - open for active and total for total audience
  public LoginByPaxReportValue()
  {
  }

  /**
   * For Trend Charts
   * 
   * @param nodeName
   * @param total
   */
  public LoginByPaxReportValue( String name, int totalCnt )
  {
    super();
    this.name = name;
    this.totalCnt = totalCnt;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public int getTotalCnt()
  {
    return totalCnt;
  }

  public void setTotalCnt( int totalCnt )
  {
    this.totalCnt = totalCnt;
  }
}
