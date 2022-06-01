/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class ReceivedNotReceivedAwardsForOrgReportValue
{
  private String orgName;
  private double receivedPct;
  private double notreceivedPct;

  public ReceivedNotReceivedAwardsForOrgReportValue()
  {
    super();
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public double getReceivedPct()
  {
    return receivedPct;
  }

  public void setReceivedPct( double receivedPct )
  {
    this.receivedPct = receivedPct;
  }

  public double getNotreceivedPct()
  {
    return notreceivedPct;
  }

  public void setNotreceivedPct( double notreceivedPct )
  {
    this.notreceivedPct = notreceivedPct;
  }

}
