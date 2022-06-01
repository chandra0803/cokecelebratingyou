/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * @author poddutur
 *
 */
public class PurlCelebrationTabType extends PickListItem
{
  /**
   * 
   */
  private static final long serialVersionUID = -6173974360750404434L;

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.purlcelebration.tab";

  public static final String GLOBAL_TAB = "global";
  public static final String TEAM_TAB = "team";
  public static final String FOLLOWED_TAB = "followed";
  public static final String SEARCH_TAB = "search";
  public static final String RECOMMENDED_TAB = "recommended";
  public static final String COUNTRY_TAB = "country";
  public static final String DEPARTMENT_TAB = "department";
  public static final String DIVISION_TAB = "division";
  
  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlCelebrationTabType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of AddressType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PurlCelebrationTabType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return AddressType
   */
  public static PurlCelebrationTabType lookup( String code )
  {
    return (PurlCelebrationTabType)getPickListFactory().getPickListItem( PurlCelebrationTabType.class, code );
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

}
