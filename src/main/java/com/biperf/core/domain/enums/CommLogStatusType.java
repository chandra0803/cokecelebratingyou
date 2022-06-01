/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CommLogStatusType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CommLogStatusType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CommLogStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.commlog.status.type";

  public static final String OPEN_CODE = "open";
  public static final String CLOSED_CODE = "closed";
  public static final String DEFERRED_CODE = "deferred";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CommLogStatusType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CommLogStatusType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( CommLogStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogStatusType
   */
  public static CommLogStatusType lookup( String code )
  {
    return (CommLogStatusType)getPickListFactory().getPickListItem( CommLogStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogStatusType
   */
  public static CommLogStatusType getDefaultItem()
  {
    return (CommLogStatusType)getPickListFactory().getDefaultPickListItem( CommLogStatusType.class );
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

  public boolean isOpen()
  {
    return getCode().equals( OPEN_CODE );
  }

  public boolean isClosed()
  {
    return getCode().equals( CLOSED_CODE );
  }

  public boolean isDeferred()
  {
    return getCode().equals( DEFERRED_CODE );
  }
}
