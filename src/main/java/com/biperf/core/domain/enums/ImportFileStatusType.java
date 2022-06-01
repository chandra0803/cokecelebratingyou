/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ImportFileStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The FileLoadType is a concrete instance of a PickListItem which wrappes a type save enum object
 * of a PickList from content manager.
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
 * <td>dunne</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ImportFileStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.importfile.status";

  public static final String STAGE_IN_PROCESS = "stg_in_process";
  public static final String STAGED = "stg";
  public static final String VERIFY_IN_PROCESS = "ver_in_process";
  public static final String VERIFIED = "ver";
  public static final String IMPORT_IN_PROCESS = "imp_in_process";
  public static final String IMPORTED = "imp";
  public static final String STAGE_FAILED = "stg_fail";
  public static final String VERIFY_FAILED = "ver_fail";
  public static final String IMPORT_FAILED = "imp_fail";
  public static final String COUNTRY_CHANGE = "country_chg";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ImportFileStatusType()
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
    return getPickListFactory().getPickList( ImportFileStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ImportFileStatusType lookup( String code )
  {
    return (ImportFileStatusType)getPickListFactory().getPickListItem( ImportFileStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ImportFileStatusType getDefaultItem()
  {
    return (ImportFileStatusType)getPickListFactory().getDefaultPickListItem( ImportFileStatusType.class );
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

  public boolean isStageInProcess()
  {
    return STAGE_IN_PROCESS.equals( getCode() );
  }

  public boolean isStaged()
  {
    return STAGED.equals( getCode() );
  }

  public boolean isVerifyInProcess()
  {
    return VERIFY_IN_PROCESS.equals( getCode() );
  }

  public boolean isVerified()
  {
    return VERIFIED.equals( getCode() );
  }

  public boolean isImportInProcess()
  {
    return IMPORT_IN_PROCESS.equals( getCode() );
  }

  public boolean isStageFailed()
  {
    return STAGE_FAILED.equals( getCode() );
  }

  public boolean isVerifyFailed()
  {
    return VERIFY_FAILED.equals( getCode() );
  }

  public boolean isImportFailed()
  {
    return IMPORT_FAILED.equals( getCode() );
  }

}
