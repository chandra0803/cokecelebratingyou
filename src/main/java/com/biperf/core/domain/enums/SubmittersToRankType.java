/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * SubmittersToRankType.
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
 * <td>gaddam</td>
 * <td>Mar 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SubmittersToRankType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.submitterstoranktype";

  public static final String SELECTED_NODES = "pax_on_selected_node";

  /**
   * Really represents "pax on selected node and below". 
   */
  public static final String ALL_NODES = "pax_on_all_nodes";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SubmittersToRankType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SubmittersToRankType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SubmittersToRankType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SubmittersToRankType
   */
  public static SubmittersToRankType lookup( String code )
  {
    return (SubmittersToRankType)getPickListFactory().getPickListItem( SubmittersToRankType.class, code );
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
