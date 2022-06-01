/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class ReceivedNotReceivedAwardsPaxReportValue
{
  private double receivedPct;
  private double notreceivedPct;

  public ReceivedNotReceivedAwardsPaxReportValue()
  {
    super();
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
