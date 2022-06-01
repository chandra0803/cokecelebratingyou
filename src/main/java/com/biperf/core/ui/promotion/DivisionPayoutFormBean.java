
package com.biperf.core.ui.promotion;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class DivisionPayoutFormBean implements Serializable, Cloneable
{
  private Long divisionPayoutId;

  private String divisionId;
  private String payoutAmount;
  private String outcomeCode;

  private String removePayout;
  private String createdBy = "";
  private long dateCreated;
  private Long version;

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public Long getDivisionPayoutId()
  {
    return divisionPayoutId;
  }

  public void setDivisionPayoutId( Long divisionPayoutId )
  {
    this.divisionPayoutId = divisionPayoutId;
  }

  public String getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( String payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getRemovePayout()
  {
    return removePayout;
  }

  public void setRemovePayout( String removePayout )
  {
    this.removePayout = removePayout;
  }

  public String getOutcomeCode()
  {
    return outcomeCode;
  }

  public void setOutcomeCode( String outcomeCode )
  {
    this.outcomeCode = outcomeCode;
  }

  public String getDivisionId()
  {
    return divisionId;
  }

  public void setDivisionId( String divisionId )
  {
    this.divisionId = divisionId;
  }

}
