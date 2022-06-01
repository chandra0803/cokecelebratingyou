/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ParticipantStatus.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class CharacteristicVisibility extends PickListItem
{
  public static final String VISIBLE = "visible";
  public static final String HIDDEN = "hidden";
  public static final String MASKED = "masked";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.charvisibility.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CharacteristicVisibility()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  @SuppressWarnings( "unchecked" )
  public static List<CharacteristicVisibility> getList()
  {
    return getPickListFactory().getPickList( CharacteristicVisibility.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CharacteristicVisibility lookup( String code )
  {
    return (CharacteristicVisibility)getPickListFactory().getPickListItem( CharacteristicVisibility.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static CharacteristicVisibility getDefaultItem()
  {
    return (CharacteristicVisibility)getPickListFactory().getDefaultPickListItem( CharacteristicVisibility.class );
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
