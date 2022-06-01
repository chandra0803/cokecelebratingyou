
package com.biperf.core.service.throwdown;

import java.math.BigDecimal;

public class TeamProgress implements java.io.Serializable
{
  private long userId;
  private BigDecimal progress;

  public long getUserId()
  {
    return userId;
  }

  public void setUserId( long userId )
  {
    this.userId = userId;
  }

  public BigDecimal getProgress()
  {
    return progress;
  }

  public void setProgress( BigDecimal progress )
  {
    this.progress = progress;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "User ID: " + userId + "\n" );
    sb.append( "progress: " + progress );
    return sb.toString();
  }

}
