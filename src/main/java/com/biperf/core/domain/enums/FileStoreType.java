
package com.biperf.core.domain.enums;

import java.util.List;

public class FileStoreType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.filestore.filestoretype";

  // Picklist codes
  public static final String CSV = "csv";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected FileStoreType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromotionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( FileStoreType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionStatusType
   */
  public static FileStoreType lookup( String code )
  {
    return (FileStoreType)getPickListFactory().getPickListItem( FileStoreType.class, code );
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
   * Returns true if this object represents the promotion status "under construction"; returns false
   * otherwise.
   * 
   * @return true if this object represents the promotion status "under construction"; false
   *         otherwise.
   */
  public boolean isCSV()
  {
    return getCode().equalsIgnoreCase( CSV );
  }
}
