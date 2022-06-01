
package com.biperf.core.value.underArmour.v1.actigraphy;

import com.biperf.core.value.underArmour.v1.BaseRestRequestObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ActigraphyRequest extends BaseRestRequestObject
{

  private String fromDate;
  private String toDate;

  public ActigraphyRequest()
  {

  }

  public ActigraphyRequest( String appCode, String fromDate, String toDate )
  {
    super( appCode );
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public ActigraphyRequest( String appCode, long clientPaxId, String fromDate, String toDate )
  {
    super( appCode, clientPaxId );
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public ActigraphyRequest( String appCode, long clientPaxId, String securityToken, String fromDate, String toDate )
  {
    super( appCode, clientPaxId, securityToken );
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public String getFromDate()
  {
    return fromDate;
  }

  public void setFromDate( String fromDate )
  {
    this.fromDate = fromDate;
  }

  public String getToDate()
  {
    return toDate;
  }

  public void setToDate( String toDate )
  {
    this.toDate = toDate;
  }

}
