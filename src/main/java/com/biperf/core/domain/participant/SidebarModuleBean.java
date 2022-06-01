
package com.biperf.core.domain.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SidebarModuleBean
{
  @JsonProperty
  private String name;
  @JsonProperty
  private String appName;

  public SidebarModuleBean()
  {
  }

  public SidebarModuleBean( String name, String appName )
  {
    this.name = name;
    this.appName = appName;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getAppName()
  {
    return appName;
  }

  public void setAppName( String appName )
  {
    this.appName = appName;
  }
}
