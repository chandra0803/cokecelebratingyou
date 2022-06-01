
package com.biperf.core.ui;

import java.io.Serializable;

public class CMMessage implements Serializable
{
  private String key;
  private String code;

  public CMMessage()
  {
    super();
  }

  public CMMessage( String code, String key )
  {
    super();
    this.code = code;
    this.key = key;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
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
