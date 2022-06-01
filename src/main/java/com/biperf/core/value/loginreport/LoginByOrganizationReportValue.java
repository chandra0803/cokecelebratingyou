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
public class LoginByOrganizationReportValue
{
  private String nodeName;
  private Long nodeId;
  private int totalCnt;

  // Participation report - open for active and total for total audience
  public LoginByOrganizationReportValue()
  {
  }

  /**
   * For Trend Charts
   * 
   * @param nodeName
   * @param total
   */
  public LoginByOrganizationReportValue( String nodeName, int totalCnt )
  {
    super();
    this.nodeName = nodeName;
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

  public int getTotalCnt()
  {
    return totalCnt;
  }

  public void setTotalCnt( int totalCnt )
  {
    this.totalCnt = totalCnt;
  }
}
