
package com.biperf.core.value;

import java.io.Serializable;

public class DailyTipValueBean implements Serializable
{
  private Long id;
  private String text;
  private String code;

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }
}
