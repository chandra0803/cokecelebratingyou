
package com.biperf.core.value;

import java.io.Serializable;

public class NameableBean implements Serializable
{
  private Long id;
  private String name;

  public NameableBean()
  {
  }

  public NameableBean( Long id, String name )
  {
    this.id = id;
    this.name = name;
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  protected void setId( Long id )
  {
    this.id = id;
  }

  protected void setName( String name )
  {
    this.name = name;
  }
}
