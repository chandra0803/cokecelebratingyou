/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/loginreport/LoginByTimeReportValue.java,v $
 */

package com.biperf.core.value.loginreport;

/**
 * LoginByTimeReportValue.
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
public class LoginByTimeReportValue
{
  private int totalCnt1;
  private int totalCnt2;

  /**
   * For Trend Charts
   * 
   * @param total Logins
   * @param total not Logins
   */

  public LoginByTimeReportValue( int totalCnt1, int totalCnt2 )
  {
    super();
    this.totalCnt1 = totalCnt1;
    this.totalCnt2 = totalCnt2;
  }

  public LoginByTimeReportValue()
  {
    super();
  }

  public int getTotalCnt1()
  {
    return totalCnt1;
  }

  public void setTotalCnt1( int totalCnt1 )
  {
    this.totalCnt1 = totalCnt1;
  }

  public int getTotalCnt2()
  {
    return totalCnt2;
  }

  public void setTotalCnt2( int totalCnt2 )
  {
    this.totalCnt2 = totalCnt2;
  }
}
