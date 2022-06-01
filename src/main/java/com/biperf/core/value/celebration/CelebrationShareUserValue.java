/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/celebration/CelebrationShareUserValue.java,v $
 */

package com.biperf.core.value.celebration;

import java.io.Serializable;

public class CelebrationShareUserValue implements Serializable
{

  private String firstName;
  private String lastName;
  private String displayInfo;

  public CelebrationShareUserValue()
  {
    super();
  }

  public CelebrationShareUserValue( String firstName, String lastName, String displayInfo )
  {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.displayInfo = displayInfo;
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

  public String getDisplayInfo()
  {
    return displayInfo;
  }

  public void setDisplayInfo( String displayInfo )
  {
    this.displayInfo = displayInfo;
  }

}
