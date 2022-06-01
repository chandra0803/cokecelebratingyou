
package com.biperf.core.service.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Predicate;

import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.ModuleApp;

public class ModuleAppFilterMap implements java.io.Serializable
{
  private ModuleApp module = null;
  private Map<FilterSetupType, FilterAppSetup> filterAppSetups = new TreeMap<FilterSetupType, FilterAppSetup>( new PickListItemSortOrderComparator() );
  private ModuleDisplay moduleDisplay = null;
  private List<FilterAppSetup> userFilterAppSetupList = null;

  private static String HERO_SEARCH = "Hero Search";

  public FilterAppSetup getHighestPriority()
  {
    // find the first priority 1 Filter mapping
    for ( Entry<FilterSetupType, FilterAppSetup> entry : filterAppSetups.entrySet() )
    {
      if ( entry.getValue() != null && entry.getValue().getPriority() == 1 )
      {
        return entry.getValue();
      }
    }
    // no priority 1s, find the first priority 2 Filter mapping
    for ( Entry<FilterSetupType, FilterAppSetup> entry : filterAppSetups.entrySet() )
    {
      if ( entry.getValue() != null && entry.getValue().getPriority() == 2 )
      {
        return entry.getValue();
      }
    }
    return null;
  }

  public ModuleApp getModule()
  {
    return module;
  }

  public void setModule( ModuleApp module )
  {
    this.module = module;
  }

  public Map<FilterSetupType, FilterAppSetup> getFilterAppSetups()
  {
    return filterAppSetups;
  }

  public void setFilterAppSetups( Map<FilterSetupType, FilterAppSetup> filterAppSetups )
  {
    this.filterAppSetups = filterAppSetups;
  }

  public ModuleDisplay getModuleDisplay()
  {
    return moduleDisplay;
  }

  public void setModuleDisplay( ModuleDisplay moduleDisplay )
  {
    this.moduleDisplay = moduleDisplay;
  }

  public void setUserFilterAppSetupList( List<FilterAppSetup> userFilters )
  {
    this.userFilterAppSetupList = userFilters;
  }

  public FilterAppSetup getUserFilterAppSetup( FilterSetupType filterSetupType )
  {
    if ( this.userFilterAppSetupList == null )
    {
      return new FilterAppSetup();
    }

    Predicate<FilterAppSetup> predicate = c -> c.getFilterSetupType().getCode().equals( filterSetupType.getCode() );
    FilterAppSetup filterAppSetup = this.userFilterAppSetupList.stream().filter( predicate ).findFirst().orElse( null );
    return filterAppSetup == null ? new FilterAppSetup() : filterAppSetup;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "\nType: " + module.getName() + "\n{\n" );
    for ( FilterSetupType type : filterAppSetups.keySet() )
    {
      FilterAppSetup filter = filterAppSetups.get( type );
      FilterAppSetup filterAppSetup = getUserFilterAppSetup( type );

      boolean heroSearch = false;
      if ( filterAppSetup != null && filterAppSetup.getModuleApp() != null && HERO_SEARCH.equals( module.getName() ) )
      {
        heroSearch = true;
      }

      if ( null != filter )
      {
        if ( heroSearch )
        {
          sb.append( "    '" + type.getCode() + "' :  { order: '" + filter.getOrderNumber() + "', searchEnabled: '" + filterAppSetup.isSearchEnabled() + "'},\n" );
        }
        else
        {
          sb.append( "    '" + type.getCode() + "' :  { order: '" + filter.getOrderNumber() + "' },\n" );
        }
      }
      else
      {
        if ( heroSearch )
        {
          sb.append( "    '" + type.getCode() + "' :  { order: 'hidden', searchEnabled: '" + filterAppSetup.isSearchEnabled() + "'},\n" );
        }
        else
        {
          sb.append( "    '" + type.getCode() + "' :  { order: 'hidden' },\n" );
        }
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  public void buildModuleDisplayMappings()
  {
    if ( null == moduleDisplay )
    {
      moduleDisplay = new ModuleDisplay();
      List<ModuleDisplayMapping> displayMappings = new ArrayList<ModuleDisplayMapping>();
      for ( FilterSetupType type : filterAppSetups.keySet() )
      {
        ModuleDisplayMapping mapping = new ModuleDisplayMapping();
        FilterAppSetup filter = filterAppSetups.get( type );
        mapping.setFilterName( type.getCode() );
        FilterAppSetup filterAppSetup = getUserFilterAppSetup( type );

        mapping.setSearchEnabled( filterAppSetup.isSearchEnabled() );

        if ( null != filter )
        {
          mapping.setOrder( String.valueOf( filter.getOrderNumber() ) );
          mapping.setActiveForUser( true );
        }
        else
        {
          mapping.setOrder( "hidden" );
        }
        displayMappings.add( mapping );
      }
      moduleDisplay.setModuleDisplayMappings( displayMappings );
    }
  }
}
