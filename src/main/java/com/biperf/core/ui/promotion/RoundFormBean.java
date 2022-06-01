
package com.biperf.core.ui.promotion;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class RoundFormBean implements Serializable, Cloneable
{
  private String roundNumber;
  private String startDate;
  private String endDate;
  private boolean scheduled = false;
  private boolean payoutComplete = false;

  public boolean isPayoutComplete()
  {
    return payoutComplete;
  }

  public void setPayoutComplete( boolean payoutComplete )
  {
    this.payoutComplete = payoutComplete;
  }

  public boolean isScheduled()
  {
    return scheduled;
  }

  public void setScheduled( boolean scheduled )
  {
    this.scheduled = scheduled;
  }

  public String getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( String roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

}
