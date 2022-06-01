/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ApprovalStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * ApprovalStatusType.
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
 * <td>zahler</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.approval.status";

  // shared codes
  public static final String PENDING = "pend";
  public static final String APPROVED = "approv";

  // Non-nomination codes
  public static final String DENIED = "deny";
  public static final String HOLD = "hold";
  public static final String EXPIRED = "expired";

  // nomination codes
  public static final String WINNER = "winner";
  public static final String NON_WINNER = "non_winner";
  public static final String MORE_INFO = "more_info";

  // quiz claim item code
  public static final String PASSED = "passed";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ApprovalStatusType()
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
    return getPickListFactory().getPickList( ApprovalStatusType.class );
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList( String promotionTypeCode, Comparator optionalPickListComparator )
  {
    List pickList;
    if ( optionalPickListComparator != null )
    {
      pickList = getPickListFactory().getPickList( ApprovalStatusType.class, optionalPickListComparator );
    }
    else
    {
      pickList = getPickListFactory().getPickList( ApprovalStatusType.class );
    }

    List filteredPickList = new ArrayList();

    for ( Iterator iter = pickList.iterator(); iter.hasNext(); )
    {
      ApprovalStatusType approvalStatusType = (ApprovalStatusType)iter.next();
      String approvalStatusTypeCode = approvalStatusType.getCode();
      if ( PENDING.equals( approvalStatusTypeCode ) )
      {
        filteredPickList.add( approvalStatusType );
      }

      if ( !PromotionType.NOMINATION.equals( promotionTypeCode ) )
      {
        if ( APPROVED.equals( approvalStatusTypeCode ) || DENIED.equals( approvalStatusTypeCode ) || HOLD.equals( approvalStatusTypeCode ) )
        {
          filteredPickList.add( approvalStatusType );
        }
      }
      else
      {
        // Nomination case
        if ( WINNER.equals( approvalStatusTypeCode ) || NON_WINNER.equals( approvalStatusTypeCode ) || EXPIRED.equals( approvalStatusTypeCode ) )
        {
          filteredPickList.add( approvalStatusType );
        }
      }
    }

    return filteredPickList;
  }

  /**
   * Get the subset of pick list to be displayed when the claimed product is currently set to
   * denied.
   * 
   * @return List
   */
  public static List getListWhenCurrentlyDenied()
  {
    List filteredList = new ArrayList();
    List allPickList = getPickListFactory().getPickList( ApprovalStatusType.class );
    for ( Iterator iter = allPickList.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( pickListItem.getCode().equals( DENIED ) )
      {
        filteredList.add( pickListItem );
      }
      /*
       * if ( pickListItem.getCode().equals( PENDING ) ) { filteredList.add( pickListItem ); }
       */
    }
    return filteredList;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ApprovalStatusType lookup( String code )
  {
    return (ApprovalStatusType)getPickListFactory().getPickListItem( ApprovalStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ApprovalStatusType getDefaultItem()
  {
    return (ApprovalStatusType)getPickListFactory().getDefaultPickListItem( ApprovalStatusType.class );
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

  /**
   * Return a promotion type specific notion of denied
   */
  public boolean isAbstractDenied()
  {
    return getCode().equals( ApprovalStatusType.DENIED ) || getCode().equals( ApprovalStatusType.NON_WINNER );
  }

  /**
   * Return a promotion type specific notion of denied
   */
  public boolean isAbstractApproved()
  {
    return getCode().equals( ApprovalStatusType.APPROVED ) || getCode().equals( ApprovalStatusType.WINNER );
  }

  public boolean isPendingDeniedHold()
  {
    return getCode().equals( ApprovalStatusType.PENDING ) || getCode().equals( ApprovalStatusType.DENIED ) || getCode().equals( ApprovalStatusType.HOLD );
  }
}
