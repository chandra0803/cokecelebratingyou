
package com.biperf.core.value.hc;

import java.util.HashMap;
import java.util.Map;

public class AccountSyncParticipantDetails
{

  // These fields are redundant, but still explicitly kept
  private Long userId;
  private String userName;
  private String firstName;
  private String lastName;

  // First map is [DB Table] --> [Second Map]. Second map is [Column Name] --> [Value]
  private Map<String, Map<String, Object>> attributeMaps = new HashMap<>();

  public Long getUserId()
  {
    return userId;
  }
  
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }
  
  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
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

  public Map<String, Map<String, Object>> getAttributeMaps()
  {
    return attributeMaps;
  }

  public void setAttributeMaps( Map<String, Map<String, Object>> attributeMaps )
  {
    this.attributeMaps = attributeMaps;
  }

}
