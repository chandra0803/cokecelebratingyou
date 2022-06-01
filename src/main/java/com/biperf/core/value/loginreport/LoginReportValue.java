/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/loginreport/LoginReportValue.java,v $
 */

package com.biperf.core.value.loginreport;

/**
 * LoginReportValue.
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
public class LoginReportValue
{
  private String nodeName;
  private Long nodeId;
  private String month;
  private String fullName;
  private int totalCnt;

  // Participation report - open for active and total for total audience
  public LoginReportValue()
  {
  }

  /**
   * For Trend Charts
   * 
   * @param nodeName
   * @param periodName
   * @param total
   */
  public LoginReportValue( String month, int totalCnt )
  {
    super();
    this.month = month;
    this.totalCnt = totalCnt;

  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getFullName()
  {
    return fullName;
  }

  public void setFullName( String fullName )
  {
    this.fullName = fullName;
  }

  public int getTotalCnt()
  {
    return totalCnt;
  }

  public void setTotalCnt( int totalCnt )
  {
    this.totalCnt = totalCnt;
  }

  public String getMonth()
  {
    return month;
  }

  public void setMonth( String month )
  {
    this.month = month;
  }
}
