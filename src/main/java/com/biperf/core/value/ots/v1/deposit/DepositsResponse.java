
package com.biperf.core.value.ots.v1.deposit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class DepositsResponse
{
  private String id;
  private String programNumber;
  private String batchNumber;
  private String cardNumber;
  private String campaignNumber;
  private String clientId;
  private String participantId;
  private String date;
  private String description;
  private int value;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public String getBatchNumber()
  {
    return batchNumber;
  }

  public void setBatchNumber( String batchNumber )
  {
    this.batchNumber = batchNumber;
  }

  public String getCardNumber()
  {
    return cardNumber;
  }

  public void setCardNumber( String cardNumber )
  {
    this.cardNumber = cardNumber;
  }

  public String getCampaignNumber()
  {
    return campaignNumber;
  }

  public void setCampaignNumber( String campaignNumber )
  {
    this.campaignNumber = campaignNumber;
  }

  public String getClientId()
  {
    return clientId;
  }

  public void setClientId( String clientId )
  {
    this.clientId = clientId;
  }

  public String getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( String participantId )
  {
    this.participantId = participantId;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public int getValue()
  {
    return value;
  }

  public void setValue( int value )
  {
    this.value = value;
  }
}
