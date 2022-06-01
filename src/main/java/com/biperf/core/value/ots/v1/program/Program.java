
package com.biperf.core.value.ots.v1.program;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Program implements Serializable
{
  private String programNumber;
  private String programDescription;
  private String clientName;
  private boolean hasTransactions;
  private boolean isApproved;
  private boolean isMediaMismatch;
  private List<Batch> batches;
  private List<String> campaignNumbers;

  public List<String> getCampaignNumbers()
  {
    return campaignNumbers;
  }

  public void setCampaignNumbers( List<String> campaignNumbers )
  {
    this.campaignNumbers = campaignNumbers;
  }

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public String getProgramDescription()
  {
    return programDescription;
  }

  public void setProgramDescription( String programDescription )
  {
    this.programDescription = programDescription;
  }

  public String getClientName()
  {
    return clientName;
  }

  public void setClientName( String clientName )
  {
    this.clientName = clientName;
  }

  public boolean isHasTransactions()
  {
    return hasTransactions;
  }

  public void setHasTransactions( boolean hasTransactions )
  {
    this.hasTransactions = hasTransactions;
  }

  public boolean isApproved()
  {
    return isApproved;
  }

  public void setApproved( boolean isApproved )
  {
    this.isApproved = isApproved;
  }

  public boolean isMediaMismatch()
  {
    return isMediaMismatch;
  }

  public void setMediaMismatch( boolean isMediaMismatch )
  {
    this.isMediaMismatch = isMediaMismatch;
  }

  public List<Batch> getBatches()
  {
    return batches;
  }

  public void setBatches( List<Batch> batches )
  {
    this.batches = batches;
  }
}