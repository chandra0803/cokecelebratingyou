
package com.biperf.core.value.ots.v1.reedeem;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RedeemResponse implements Serializable
{
  private String id;
  private String programNumber;
  private String batchNumber;
  private String cardNumber;
  private String cardReference;
  private String value;
  private String clientId;
  private String participantId;
  private String date;
  private String description;

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

  public String getCardReference()
  {
    return cardReference;
  }

  public void setCardReference( String cardReference )
  {
    this.cardReference = cardReference;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
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

}
