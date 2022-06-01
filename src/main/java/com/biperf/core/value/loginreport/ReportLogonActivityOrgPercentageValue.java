
package com.biperf.core.value.loginreport;

import java.math.BigDecimal;

/**
 * ReportLogonActivityOrgPercentageValue.
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
 * <td>kandhi</td>
 * <td>Jul 05, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ReportLogonActivityOrgPercentageValue
{

  private String nodeName;
  private BigDecimal loggedInPct;
  private BigDecimal notLoggedInPct;

  public ReportLogonActivityOrgPercentageValue()
  {
    super();
  }

  public ReportLogonActivityOrgPercentageValue( String nodeName, BigDecimal loggedInPct, BigDecimal notLoggedInPct )
  {
    super();
    this.nodeName = nodeName;
    this.loggedInPct = loggedInPct;
    this.notLoggedInPct = notLoggedInPct;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public BigDecimal getLoggedInPct()
  {
    return loggedInPct;
  }

  public void setLoggedInPct( BigDecimal loggedInPct )
  {
    this.loggedInPct = loggedInPct;
  }

  public BigDecimal getNotLoggedInPct()
  {
    return notLoggedInPct;
  }

  public void setNotLoggedInPct( BigDecimal notLoggedInPct )
  {
    this.notLoggedInPct = notLoggedInPct;
  }

}
