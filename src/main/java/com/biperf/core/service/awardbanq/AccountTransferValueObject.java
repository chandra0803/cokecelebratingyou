
package com.biperf.core.service.awardbanq;

import java.io.Serializable;

public class AccountTransferValueObject implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String fromAccountNumber;
  private String toAccountNumber;
  private String fromCampaignNumber;
  private String toCampaignNumber;
  private int transferStatus;

  public AccountTransferValueObject()
  {

  }

  public String getFromAccountNumber()
  {
    return fromAccountNumber;
  }

  public void setFromAccountNumber( String fromAccountNumber )
  {
    this.fromAccountNumber = fromAccountNumber;
  }

  public String getToAccountNumber()
  {
    return toAccountNumber;
  }

  public void setToAccountNumber( String toAccountNumber )
  {
    this.toAccountNumber = toAccountNumber;
  }

  public String getFromCampaignNumber()
  {
    return fromCampaignNumber;
  }

  public void setFromCampaignNumber( String fromCampaignNumber )
  {
    this.fromCampaignNumber = fromCampaignNumber;
  }

  public String getToCampaignNumber()
  {
    return toCampaignNumber;
  }

  public void setToCampaignNumber( String toCampaignNumber )
  {
    this.toCampaignNumber = toCampaignNumber;
  }

  public int getTransferStatus()
  {
    return transferStatus;
  }

  public void setTransferStatus( int transferStatus )
  {
    this.transferStatus = transferStatus;
  }

}
