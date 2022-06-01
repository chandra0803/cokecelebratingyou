
package com.biperf.core.value.underArmour.v1.participant;

import com.biperf.core.value.underArmour.v1.BaseRestResponseObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PaxAuthStatusResponse extends BaseRestResponseObject
{

  private boolean isAuthorized;

  public PaxAuthStatusResponse()
  {
  }

  public PaxAuthStatusResponse( int returnCode, String returnMessage, boolean isAuthorized )
  {
    super( returnCode, returnMessage );
    this.isAuthorized = isAuthorized;
  }

  public boolean isAuthorized()
  {
    return isAuthorized;
  }

  public void setAuthorized( boolean isAuthorized )
  {
    this.isAuthorized = isAuthorized;
  }

}
