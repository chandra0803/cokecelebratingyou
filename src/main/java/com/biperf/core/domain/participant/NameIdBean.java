
package com.biperf.core.domain.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class NameIdBean
{
  private Long id;
  private String code;
  private String name;
  private String value;
  private String description;

  public NameIdBean()
  {

  }

  public NameIdBean( Long id, String name, String description )
  {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public NameIdBean( String code, String name )
  {
    this.code = code;
    this.name = name;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

}
