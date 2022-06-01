
package com.biperf.core.service.home;

import java.util.ArrayList;
import java.util.List;

public class ModuleDisplay implements java.io.Serializable
{
  private List<ModuleDisplayMapping> moduleDisplayMappings = new ArrayList<ModuleDisplayMapping>();

  public boolean isActive()
  {
    for ( ModuleDisplayMapping mapping : moduleDisplayMappings )
    {
      if ( mapping.isActiveForUser() )
      {
        return true;
      }
    }
    return false;
  }

  public boolean hasModuleDisplayMappingForFilter( String filterName )
  {
    for ( ModuleDisplayMapping moduleDisplayMapping : moduleDisplayMappings )
    {
      if ( moduleDisplayMapping.getFilterName().equals( filterName ) )
      {
        return true;
      }
    }
    return false;
  }

  public List<ModuleDisplayMapping> getModuleDisplayMappings()
  {
    return moduleDisplayMappings;
  }

  public void setModuleDisplayMappings( List<ModuleDisplayMapping> moduleDisplayMappings )
  {
    this.moduleDisplayMappings = moduleDisplayMappings;
  }
}
