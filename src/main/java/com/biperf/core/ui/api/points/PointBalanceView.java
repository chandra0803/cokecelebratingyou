
package com.biperf.core.ui.api.points;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PointBalanceView
{
  @JsonProperty
  private Long pointBalance;
  @JsonProperty
  private Long returnCode;
  @JsonProperty
  private String returnMessage;

  public Long getPointBalance()
  {
    return pointBalance;
  }

  public void setPointBalance( Long pointBalance )
  {
    this.pointBalance = pointBalance;
  }

  public Long getReturnCode()
  {
    return returnCode;
  }

  public void setReturnCode( Long returnCode )
  {
    this.returnCode = returnCode;
  }

  public String getReturnMessage()
  {
    return returnMessage;
  }

  public void setReturnMessage( String returnMessage )
  {
    this.returnMessage = returnMessage;
  }
}
