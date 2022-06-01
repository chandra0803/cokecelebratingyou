/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The QuickSearchSearchForField is a concrete instance of a PickListItem which wrappes a type save
 * enum object of a PickList from content manager.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>wadzinsk</td>
 * <td>August 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuickSearchSearchForField extends ModuleAwarePickListItem
{
  public static final String PARTICIPANT = "pax";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "home.quicksearch.search.for.field";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected QuickSearchSearchForField()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( QuickSearchSearchForField.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static QuickSearchSearchForField lookup( String code )
  {
    return (QuickSearchSearchForField)getPickListFactory().getPickListItem( QuickSearchSearchForField.class, code );
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
