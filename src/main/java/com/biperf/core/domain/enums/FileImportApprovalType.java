/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * . <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>wadzinsk</td>
 * <td>June 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author wadzinsk
 *
 */
public class FileImportApprovalType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.fileimport.approval.type";

  public static final String MANUAL = "manual";
  public static final String AUTOMATIC = "automatic";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected FileImportApprovalType()
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
    return getPickListFactory().getPickList( FileImportApprovalType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static FileImportApprovalType lookup( String code )
  {
    return (FileImportApprovalType)getPickListFactory().getPickListItem( FileImportApprovalType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static NodeIncludeType getDefaultItem()
  // {
  // return (NodeIncludeType)getPickListFactory().getDefaultPickListItem( NodeIncludeType.class );
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
