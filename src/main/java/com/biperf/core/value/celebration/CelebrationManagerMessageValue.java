
package com.biperf.core.value.celebration;

import java.io.Serializable;

public class CelebrationManagerMessageValue implements Serializable
{
  private Long userId;
  private String avatarUrl;
  private String firstName;
  private String lastName;
  private String message;
  private String shortMessage;
  private boolean displayReadMoreOrLess;

  public String getShortMessage()
  {
    return shortMessage;
  }

  public void setShortMessage( String shortMessage )
  {
    this.shortMessage = shortMessage;
  }

  private int displayOrder;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public int getDisplayOrder()
  {
    return displayOrder;
  }

  public void setDisplayOrder( int displayOrder )
  {
    this.displayOrder = displayOrder;
  }

  public boolean isDisplayReadMoreOrLess()
  {
    return displayReadMoreOrLess;
  }

  public void setDisplayReadMoreOrLess( boolean displayReadMoreOrLess )
  {
    this.displayReadMoreOrLess = displayReadMoreOrLess;
  }

}
