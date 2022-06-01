
package com.biperf.core.value.ecard;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ECardMigrateResponse implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String cardId;
  private String result;
  private List<ECardMigrateView> response;

  public String getCardId()
  {
    return cardId;
  }

  public void setCardId( String cardId )
  {
    this.cardId = cardId;
  }

  public String getResult()
  {
    return result;
  }

  public void setResult( String result )
  {
    this.result = result;
  }

  public List<ECardMigrateView> getResponse()
  {
    return response;
  }

  public void setResponse( List<ECardMigrateView> response )
  {
    this.response = response;
  }

}
