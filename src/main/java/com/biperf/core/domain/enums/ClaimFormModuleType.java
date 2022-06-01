/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ClaimFormModuleType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ClaimFormModuleType is a concrete instance of a PickListItem which wrapes a type save enum object
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
 * <td>robinsra</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormModuleType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.claimformmoduletype";

  public static final String MODULE_PRODUCT_CLAIMS = "prd";
  public static final String MODULE_RECOGNITION = "rec";
  public static final String MODULE_QUIZ = "quiz";
  public static final String MODULE_NOMINATION = "nom";
  public static final String MODULE_GOALQUEST = "gq";
  public static final String MODULE_SSI = "ssi";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ClaimFormModuleType()
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
    return getPickListFactory().getPickList( ClaimFormModuleType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ClaimFormModuleType lookup( String code )
  {
    return (ClaimFormModuleType)getPickListFactory().getPickListItem( ClaimFormModuleType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ClaimFormModuleType getDefaultItem()
  {
    return (ClaimFormModuleType)getPickListFactory().getDefaultPickListItem( ClaimFormModuleType.class );
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

  public boolean isProductClaim()
  {
    return MODULE_PRODUCT_CLAIMS.equals( getCode() );
  }

  public boolean isRecognition()
  {
    return MODULE_RECOGNITION.equals( getCode() );
  }

  public boolean isQuiz()
  {
    return MODULE_QUIZ.equals( getCode() );
  }

  public boolean isNomination()
  {
    return MODULE_NOMINATION.equals( getCode() );
  }

  public boolean isGoalquest()
  {
    return MODULE_GOALQUEST.equals( getCode() );
  }

  public boolean isSsi()
  {
    return MODULE_SSI.equals( getCode() );
  }

} // end ClaimFormModuleType
