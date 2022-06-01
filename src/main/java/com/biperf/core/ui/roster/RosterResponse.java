
package com.biperf.core.ui.roster;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class RosterResponse
{
  private boolean success;
  @JsonUnwrapped
  private Object data;

  public RosterResponse( boolean success, Object data )
  {
    this.success = success;
    this.data = data;
  }

  public Object getData()
  {
    return data;
  }

  public void setData( Object data )
  {
    this.data = data;
  }

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess( boolean success )
  {
    this.success = success;
  }

}
