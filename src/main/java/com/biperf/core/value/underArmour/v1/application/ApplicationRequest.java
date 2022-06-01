
package com.biperf.core.value.underArmour.v1.application;

import com.biperf.core.value.underArmour.v1.BaseRestRequestObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ApplicationRequest extends BaseRestRequestObject
{

  private String name;
  private String cronExpression;
  private String encryptionSalt;
  private String clientId;
  private String clientSecret;

  public ApplicationRequest()
  {
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getCronExpression()
  {
    return cronExpression;
  }

  public void setCronExpression( String cronExpression )
  {
    this.cronExpression = cronExpression;
  }

  public String getEncryptionSalt()
  {
    return encryptionSalt;
  }

  public void setEncryptionSalt( String encryptionSalt )
  {
    this.encryptionSalt = encryptionSalt;
  }

  public String getClientId()
  {
    return clientId;
  }

  public void setClientId( String clientId )
  {
    this.clientId = clientId;
  }

  public String getClientSecret()
  {
    return clientSecret;
  }

  public void setClientSecret( String clientSecret )
  {
    this.clientSecret = clientSecret;
  }

}
