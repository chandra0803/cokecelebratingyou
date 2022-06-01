/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PayoutCalculationAuditReasonType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The PayoutCalculationAuditReasonType is a concrete instance of a PickListItem which wrappes a
 * type save enum object of a PickList from content manager.
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
 * <td>wadzinsk</td>
 * <td>August 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutCalculationAuditReasonType extends PickListItem
{
  public static final String STACK_RANK_SUCCESS = "stack_rank_success";
  public static final String SWEEPSTAKES_SUCCESS = "sweepstakes_success";
  public static final String RECOGNITION_SUCCESS = "recognition_success";
  public static final String NOMINATION_SUCCESS = "nomination_success";
  public static final String QUIZ_SUCCESS = "quiz_success";
  public static final String QUIZ_FAILURE_DID_NOT_PASS = "quiz_failure_did_not_pass";
  public static final String GOALQUEST_SUCCESS = "goalquest_success";
  public static final String CHALLENGEPOINT_SUCCESS = "challengepoint_success";
  public static final String CP_BASIC_SUCCESS = "cp_basic_success";
  public static final String SALES_SUCCESS = "sales_success";
  public static final String CROSS_SELL_INSUFFICIENT_QUANTITY = "cs_insuf_quant";
  public static final String SALES_ZERO_PAYOUT_GROUP_PAYOUT = "sales_0_pay_gr";
  public static final String CROSS_SELL_UNMATCHED_PRODUCT = "cs_unmatch_prod";
  public static final String TIERED_INSUFFICIENT_QUANTITY = "ti_insuf_quant";
  public static final String MIN_QUALIFIER_NOT_EXCEEDED_RETRO_OFF_FAILURE = "min_qual_retro_off";
  public static final String MANAGER_OVERRIDE = "mo_success";
  public static final String MIN_QUALIFIER_NOT_EXCEEDED_RETRO_OFF_TEAM_MEMBER_FAILURE = "min_qual_retro_off_tm";
  public static final String MIN_QUALIFIER_NOT_EXCEEDED_CARRYOVER_OFF_FAILURE = "min_qual_carrover_off";
  public static final String BUDGET_OVERDRAWN = "budget_overdrawn";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.payoutcalculationauditreasontype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PayoutCalculationAuditReasonType()
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
    return getPickListFactory().getPickList( PayoutCalculationAuditReasonType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PayoutCalculationAuditReasonType lookup( String code )
  {
    return (PayoutCalculationAuditReasonType)getPickListFactory().getPickListItem( PayoutCalculationAuditReasonType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PayoutCalculationAuditReasonType getDefaultItem()
  {
    return (PayoutCalculationAuditReasonType)getPickListFactory().getDefaultPickListItem( PayoutCalculationAuditReasonType.class );
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

  public boolean isSuccessMessage()
  {
    if ( this.getCode().equals( SWEEPSTAKES_SUCCESS ) || this.getCode().equals( RECOGNITION_SUCCESS ) || this.getCode().equals( QUIZ_SUCCESS ) || this.getCode().equals( SALES_SUCCESS )
        || this.getCode().equals( MANAGER_OVERRIDE ) || this.getCode().equals( GOALQUEST_SUCCESS ) || this.getCode().equals( CHALLENGEPOINT_SUCCESS ) )
    {
      return true;
    }
    return false;
  }
}
