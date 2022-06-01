/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PromotionPayoutType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The PromotionPayoutType is a concrete instance of a PickListItem which wrappes a type save enum
 * object of a PickList from content manager.
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
 * <td>sedey</td>
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutType extends PickListItem
{

  public static final String ONE_TO_ONE = "one_to_one";
  public static final String TIERED = "tiered";
  public static final String CROSS_SELL = "cross_sell";
  public static final String MANAGER_OVERRIDE = "manager_over";
  public static final String SWEEPSTAKES = "sweepstakes";
  public static final String STACK_RANK = "stack_rank";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotion.payout.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionPayoutType()
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
    List list = getPickListFactory().getPickList( PromotionPayoutType.class );
    List pickList = new ArrayList();
    Iterator iter = list.iterator();
    while ( iter.hasNext() )
    {
      PromotionPayoutType type = (PromotionPayoutType)iter.next();
      if ( type.isUserSelectable() )
      {
        pickList.add( type );
      }
    }
    return Collections.unmodifiableList( pickList );
  }

  private boolean isUserSelectable()
  {
    if ( getCode().equals( ONE_TO_ONE ) || getCode().equals( TIERED ) || getCode().equals( CROSS_SELL ) || getCode().equals( STACK_RANK ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionPayoutType lookup( String code )
  {
    return (PromotionPayoutType)getPickListFactory().getPickListItem( PromotionPayoutType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionPayoutType getDefaultItem()
  // {
  // return (PromotionPayoutType)getPickListFactory().getDefaultPickListItem(
  // PromotionPayoutType.class );
  // }
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
