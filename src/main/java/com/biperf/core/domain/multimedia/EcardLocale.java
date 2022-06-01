
package com.biperf.core.domain.multimedia;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.biperf.core.domain.BaseDomain;

public class EcardLocale extends BaseDomain
{
  private Long cardId;
  private String displayName;
  private String locale;
  // private Card card;

  public Long getCardId()
  {
    return cardId;
  }

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }

  /**public Card getCard()
  {
    return card;
  }
  
  public void setCard( Card card )
  {
    this.card = card;
  }*/

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.displayName ).append( this.locale ).toHashCode();
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( ! ( obj instanceof EcardLocale ) )
    {
      return false;
    }
    if ( this == obj )
    {
      return true;
    }
    final EcardLocale otherObject = (EcardLocale)obj;

    return new EqualsBuilder().append( this.displayName, otherObject.displayName ).append( this.locale, otherObject.locale ).isEquals();
  }
}
