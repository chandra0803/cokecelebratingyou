
package com.biperf.core.value.hc;

public class GoalquestSSOResponse
{
  private String programUrl;
  private String hashString;
  private String applicationId;
  private String userName;
  private String signature;

  public String getProgramUrl()
  {
    return programUrl;
  }

  public void setProgramUrl( String programUrl )
  {
    this.programUrl = programUrl;
  }
  
  public String getHashString()
  {
    return hashString;
  }

  public void setHashString( String hashString )
  {
    this.hashString = hashString;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getApplicationId()
  {
    return applicationId;
  }

  public void setApplicationId( String url )
  {
    this.applicationId = url;
  }

  public String getSignature()
  {
    return signature;
  }

  public void setSignature( String signature )
  {
    this.signature = signature;
  }

}
