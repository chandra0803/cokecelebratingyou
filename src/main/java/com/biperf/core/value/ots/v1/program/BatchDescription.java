
package com.biperf.core.value.ots.v1.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class BatchDescription
{
  private String cmText;
  private String displayName;
  private String locale;
  private int count;

  public String getCmText()
  {
    return cmText;
  }

  public void setCmText( String cmText )
  {
    this.cmText = cmText;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

}
