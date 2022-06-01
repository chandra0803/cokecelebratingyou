
package com.biperf.core.value;

import java.util.List;

import com.biperf.core.utils.ModuleType;

public class ModuleResources
{

  private ModuleType moduleType;
  private List<String> resources;

  public ModuleResources( ModuleType moduleType, List<String> resources )
  {
    this.moduleType = moduleType;
    this.resources = resources;
  }

  public ModuleType getModuleType()
  {
    return moduleType;
  }

  public List<String> getResources()
  {
    return resources;
  }

}
