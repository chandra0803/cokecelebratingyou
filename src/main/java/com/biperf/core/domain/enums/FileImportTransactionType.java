
package com.biperf.core.domain.enums;

import java.util.List;

public class FileImportTransactionType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.fileimport.transaction.type";

  public static final String CREATE_RECOGNITION = "createrecognition";
  public static final String DEPOSIT_ONLY = "depositonly";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected FileImportTransactionType()
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
    return getPickListFactory().getPickList( FileImportTransactionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static FileImportTransactionType lookup( String code )
  {
    return (FileImportTransactionType)getPickListFactory().getPickListItem( FileImportTransactionType.class, code );
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
