
package com.biperf.core.value.ots.v1.reedeem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RedeemRequest implements Serializable
{
  private String cardNumber;
  private String campaignNumber;
  private String clientId;
  private String participantId;
  private String participantAccount;
  private List<String> programNumbers;
  private Map<String, String> participantValues;

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

  public String getParticipantAccount()
  {
    return participantAccount;
  }

  public void setParticipantAccount( String participantAccount )
  {
    this.participantAccount = participantAccount;
  }

  public List<String> getProgramNumbers()
  {
    return programNumbers;
  }

  public void setProgramNumbers( List<String> programNumbers )
  {
    this.programNumbers = programNumbers;
  }

  public Map<String, String> getParticipantValues()
  {
    return participantValues;
  }

  public void setParticipantValues( Map<String, String> participantValues )
  {
    this.participantValues = participantValues;
  }

}
