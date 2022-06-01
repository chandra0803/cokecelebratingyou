/**
 * 
 */

package com.biperf.core.value;

import java.io.Serializable;

public class PickListValueBean implements Serializable
{
  private String code;
  private String name;

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }
}