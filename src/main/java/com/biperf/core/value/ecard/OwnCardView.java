
package com.biperf.core.value.ecard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class OwnCardView implements Serializable
{

  private static final long serialVersionUID = 477190820210303705L;
  private boolean success;

  private String cardUrl;

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess( boolean success )
  {
    this.success = success;
  }

  public String getCardUrl()
  {
    return cardUrl;
  }

  public void setCardUrl( String cardUrl )
  {
    this.cardUrl = cardUrl;
  }

}
