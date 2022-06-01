
package com.biperf.core.domain.multimedia;

import java.io.Serializable;

public class EcardFormBean implements Serializable
{

  private static final long serialVersionUID = -5278700329383047336L;

  private Long cardId;
  private String displayName;
  private String locale;

  public Long getCardId()
  {
    return cardId;
  }

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

}
