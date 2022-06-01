
package com.biperf.core.value.underArmour.v1.participant;

import com.biperf.core.value.underArmour.v1.BaseRestRequestObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ParticipantRequest extends BaseRestRequestObject
{

  private String authCode;
  private String paxAuthorizedDate;

  public ParticipantRequest()
  {
  }

  public ParticipantRequest( String appCode, String authCode )
  {
    super( appCode );
    this.authCode = authCode;
  }

  public ParticipantRequest( String appCode, long clientPaxId, String authCode )
  {
    super( appCode, clientPaxId );
    this.authCode = authCode;
  }

  public ParticipantRequest( String appCode, long clientPaxId, String securityToken, String authCode )
  {
    super( appCode, clientPaxId, securityToken );
    this.authCode = authCode;
  }

  public String getAuthCode()
  {
    return authCode;
  }

  public void setAuthCode( String authCode )
  {
    this.authCode = authCode;
  }

  public String getPaxAuthorizedDate()
  {
    return paxAuthorizedDate;
  }

  public void setPaxAuthorizedDate( String paxAuthorizedDate )
  {
    this.paxAuthorizedDate = paxAuthorizedDate;
  }
}
