
package com.biperf.core.service.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.FilterAppSetupComparator;

@SuppressWarnings( "serial" )
public class HomePageFilterHolder implements java.io.Serializable
{
  public static final String P1_4x4 = "4x4";
  public static final String P1_4x2 = "4x2";

  public static final String P2_2x2 = "2x2";
  public static final String P2_2x1 = "2x1";

  private List<ModuleAppFilterMap> moduleAppFilterMap = null;

  public List<ModuleAppFilterMap> getModuleAppFilterMap()
  {
    return moduleAppFilterMap;
  }

  public void setModuleAppFilterMap( List<ModuleAppFilterMap> moduleAppFilterMap )
  {
    this.moduleAppFilterMap = moduleAppFilterMap;
  }

  public ModuleAppFilterMap getModuleByTileMappingType( String code )
  {
    return getModuleByType( TileMappingType.lookup( code ) );
  }

  @SuppressWarnings( { "unchecked" } )
  public void buildAllDimensions()
  {
    List<FilterSetupType> types = new ArrayList<FilterSetupType>();
    types.addAll( (List<FilterSetupType>)FilterSetupType.getList() );
    Collections.sort( types, new PickListItemSortOrderComparator() );
    for ( FilterSetupType type : types )
    {
      buildDimensions( type );
    }
    for ( ModuleAppFilterMap map : moduleAppFilterMap )
    {
      map.buildModuleDisplayMappings();
    }
  }

  /*
   * Business logic to determine the actual sizes of each tile on each filter
   */
  public void buildDimensions( FilterSetupType type )
  {
    List<FilterAppSetup> filtersPriority1 = new ArrayList<FilterAppSetup>();
    List<FilterAppSetup> filtersPriority2 = new ArrayList<FilterAppSetup>();
    for ( ModuleAppFilterMap map : moduleAppFilterMap )
    {
      FilterAppSetup filter = map.getFilterAppSetups().get( type );
      if ( filter != null )
      {
        // priority 1 filters
        if ( filter.getPriority() == 1 )
        {
          filtersPriority1.add( filter );
        }
        // priority 2 filters
        if ( filter.getPriority() == 2 )
        {
          filtersPriority2.add( filter );
        }
        // ok, default to 2x1 but this shouldn't happen
        if ( filter.getPriority() > 2 )
        {
          filter.setSize( P2_2x1 );
        }
      }
    }
    Comparator<FilterAppSetup> filterAppSetupComparator = new FilterAppSetupComparator();
    Collections.sort( filtersPriority1, filterAppSetupComparator );
    Collections.sort( filtersPriority2, filterAppSetupComparator );
    // set the sizes based on this comprised list
    // PRIORITY 1 Tiles dimensions:
    if ( !filtersPriority1.isEmpty() )
    {
      if ( filtersPriority1.size() == 1 )
      {
        filtersPriority1.get( 0 ).setSize( P1_4x4 );
      }
      else
      {
        // they are all 4x2
        for ( FilterAppSetup filter : filtersPriority1 )
        {
          filter.setSize( P1_4x2 );
        }
      }
    }
    // PRIORITY 2 Tiles
    if ( !filtersPriority2.isEmpty() )
    {
      // up to 9 filters with no P1s
      if ( filtersPriority1.isEmpty() )
      {
        for ( int i = 0; i < filtersPriority2.size(); i++ )
        {
          FilterAppSetup filter = filtersPriority2.get( i );
          if ( i <= 5 )
          {
            filter.setSize( P2_2x2 );
          }
          else
          {
            filter.setSize( P2_2x1 );
          }
        }
      }
      // up to 9 filters with 1 or 2 P1s
      if ( !filtersPriority1.isEmpty() && filtersPriority1.size() <= 2 )
      {
        if ( filtersPriority2.size() <= 2 )
        {
          for ( FilterAppSetup filter : filtersPriority2 )
          {
            filter.setSize( P2_2x2 );
          }
        }
        else
        {
          for ( int i = 0; i < filtersPriority2.size(); i++ )
          {
            FilterAppSetup filter = filtersPriority2.get( i );
            if ( i < filtersPriority1.size() )
            {
              filter.setSize( P2_2x2 );
            }
            else
            {
              filter.setSize( P2_2x1 );
            }
          }
        }
      }
      // up to 9 filters with 3 P1s
      if ( !filtersPriority1.isEmpty() && filtersPriority1.size() >= 3 )
      {
        if ( filtersPriority2.size() <= 2 )
        {
          for ( FilterAppSetup filter : filtersPriority2 )
          {
            filter.setSize( P2_2x2 );
          }
        }
        else
        {
          for ( int i = 0; i < filtersPriority2.size(); i++ )
          {
            FilterAppSetup filter = filtersPriority2.get( i );
            if ( i < 2 )
            {
              filter.setSize( P2_2x2 );
            }
            else
            {
              filter.setSize( P2_2x1 );
            }
          }
        }
      }
    }
  }

  public ModuleAppFilterMap getModuleByType( TileMappingType type )
  {
    for ( ModuleAppFilterMap moduleMap : moduleAppFilterMap )
    {
      if ( moduleMap.getModule().getTileMappingType().getCode().equals( type.getCode() ) )
      {
        return moduleMap;
      }
    }
    return null;
  }

  public List<ModuleAppFilterMap> getModuleByFilterSetupType( FilterSetupType type )
  {
    List<ModuleAppFilterMap> modulesByFilterType = new ArrayList<ModuleAppFilterMap>();
    for ( ModuleAppFilterMap moduleMap : moduleAppFilterMap )
    {
      if ( moduleMap.getFilterAppSetups().get( type ) != null )
      {
        modulesByFilterType.add( moduleMap );
      }
    }
    return modulesByFilterType;
  }

  public List<FilterSetupType> getActiveFilters()
  {

    Set<FilterSetupType> filters = new HashSet<FilterSetupType>();
    for ( ModuleAppFilterMap moduleMap : this.getModuleAppFilterMap() )
    {
      for ( Entry<FilterSetupType, FilterAppSetup> entry : moduleMap.getFilterAppSetups().entrySet() )
      {
        if ( entry.getValue() != null )
        {
          filters.add( entry.getKey() );
        }
      }
    }
    return new ArrayList<FilterSetupType>( filters );
  }

}
