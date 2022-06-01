
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

public class UserJSResources
{

  private List<ModuleResources> moduleResource = new ArrayList<ModuleResources>();

  public UserJSResources()
  {
  }

  public UserJSResources( List<ModuleResources> moduleResource )
  {
    this.moduleResource = moduleResource;
  }

  public List<ModuleResources> getModuleResource()
  {
    return moduleResource;
  }

  public void setModuleResource( List<ModuleResources> moduleResource )
  {
    this.moduleResource = moduleResource;
  }

  public void addModuleResource( ModuleResources moduleResource )
  {
    this.moduleResource.add( moduleResource );
  }

}
