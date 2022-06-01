/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/loginreport/ReportLogonActivityListOfParticipantsValue.java,v $
 */

package com.biperf.core.value.loginreport;

import java.util.Date;

/**
 * ReportLogonActivityListOfParticipantsValue.
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
public class ReportLogonActivityListOfParticipantsValue
{
  private String nodeName;
  private Long nodeId;
  private String fullName;
  private String department;
  private String position;
  private Date lastLoggedIn;
  private int totalCnt;
  private Long userId;

  // Participation report - open for active and total for total audience
  public ReportLogonActivityListOfParticipantsValue()
  {
  }

  /**
   * For Trend Charts
   * 
   * @param nodeName
   * @param periodName
   * @param total
   */
  public ReportLogonActivityListOfParticipantsValue( String nodeName, String fullName, String department, String position, Date lastLoggedIn, int totalCnt, Long userId )
  {
    super();
    this.nodeName = nodeName;
    this.fullName = fullName;
    this.department = department;
    this.position = position;
    this.totalCnt = totalCnt;
    this.lastLoggedIn = lastLoggedIn;
    this.userId = userId;

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

  public Date getLastLoggedIn()
  {
    return lastLoggedIn;
  }

  public void setLastLoggedIn( Date lastLoggedIn )
  {
    this.lastLoggedIn = lastLoggedIn;
  }

  public int getTotalCnt()
  {
    return totalCnt;
  }

  public void setTotalCnt( int totalCnt )
  {
    this.totalCnt = totalCnt;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }
}
