/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

public class FilterSetupType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.homepage.setup"; // FilterSetupType

  // Picklist codes
  public static final String HOME = "recognition";
  public static final String INFORMATION = "information";
  public static final String PROGRAMS = "programs";
  public static final String MANAGER = "manager";
  public static final String THROWDOWN = "throwdown";
  public static final String REPORTS = "reports";
  public static final String MISSIONS = "missions";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected FilterSetupType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of FilterSetupType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( FilterSetupType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Get the pick list from content manager sorted by Name.
   * 
   * @return List of FilterSetupType
   */
  public static List getGenericList()
  {
    return getPickListFactory().getPickList( FilterSetupType.class, new PickListItemNameComparator() );
  }

  /**
   * Get the pick list from content manager sorted by Name
   * 
   * @return List of FilterSetupType
   */
  @SuppressWarnings( "unchecked" )
  public static List<FilterSetupType> getAdjustableFilterSetupList()
  {
    List<FilterSetupType> fullList = getPickListFactory().getPickList( FilterSetupType.class, new PickListItemNameComparator() );
    List<FilterSetupType> adjustableList = new ArrayList<FilterSetupType>();

    for ( FilterSetupType filterSetupType : fullList )
    {
      if ( !REPORTS.equals( filterSetupType.getCode() ) )
      {
        adjustableList.add( filterSetupType );
      }
    }

    return adjustableList;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return FilterSetupType
   */
  public static FilterSetupType lookup( String code )
  {
    return (FilterSetupType)getPickListFactory().getPickListItem( FilterSetupType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default FilterSetupType
   */
  public static FilterSetupType getDefaultItem()
  {
    return (FilterSetupType)getPickListFactory().getDefaultPickListItem( FilterSetupType.class );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public boolean isHomeFilter()
  {
    return getCode().equals( HOME );
  }

  public boolean isInformationFilter()
  {
    return getCode().equals( INFORMATION );
  }

  public boolean isProgramsFilter()
  {
    return getCode().equals( PROGRAMS );
  }

  public boolean isManagerFilter()
  {
    return getCode().equals( MANAGER );
  }

  public boolean isReportFilter()
  {
    return getCode().equals( REPORTS );
  }

  public boolean isThrowdownFilter()
  {
    return getCode().equals( THROWDOWN );
  }
}
