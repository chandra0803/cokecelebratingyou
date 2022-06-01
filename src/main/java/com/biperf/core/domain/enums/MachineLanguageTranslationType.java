
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * MachineLanguageTranslationType.
 * 
 * @author kandhi
 * @since Sep 22, 2014
 * @version 1.0
 */
public class MachineLanguageTranslationType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.machine.language";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MachineLanguageTranslationType()
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
    return getPickListFactory().getPickList( MachineLanguageTranslationType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static MachineLanguageTranslationType lookup( String code )
  {
    return (MachineLanguageTranslationType)getPickListFactory().getPickListItem( MachineLanguageTranslationType.class, code );
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
