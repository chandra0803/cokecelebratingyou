/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ClaimFormStepEmailNotificationType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * ClaimFormStepEmailNotificationType.
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
 * <td>crosenquest</td>
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepEmailNotificationType extends PickListItem
{

  public static final String CLAIM_SUBMITTED = "submitted"; // product claim's email approver on
                                                            // claim submitted <--
  public static final String CLAIM_APPROVED = "approved"; // product claim's product claim'semail
                                                          // participant on claim approved <--
  public static final String CLAIM_DENIED = "denied"; // product claim's email participant on claim
                                                      // denied

  public static final String RECOGNITION_SUBMITTED = "recognitionsubmitted";
  public static final String RECOGNITION_APPROVED_EMAIL = "recognitionapprovedemail";
  public static final String TO_NOMINEE_WHEN_CLAIM_SUBMITTED = "to_nominee_submitted";
  public static final String TO_NOMINEES_MANAGER_WHEN_CLAIM_SUBMITTED = "to_nominee_mgr_submitted";
  public static final String TO_APPROVER_WHEN_CLAIM_SUBMITTED = "to_approver_submitted";
  public static final String TO_NOMINEE_WHEN_WINNER = "to_nominee_winner";
  public static final String TO_NOMINEES_MANAGER_WHEN_WINNER = "to_nominees_mgr_winner";
  public static final String TO_NOMINATOR_WHEN_WINNER = "to_nominator_winner";
  //Client customizations for WIP #59461
  public static final String TO_NOMINATOR_WHEN_SUBMITTED = "to_nominator_submitted";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.claimformemailnotificiationtype"; 
  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ClaimFormStepEmailNotificationType()
  {
    super();
  }

  /**
   * @return boolean
   */
  public boolean isSubmitted()
  {
    return getCode().equals( CLAIM_SUBMITTED );
  }

  public boolean isApproved()
  {
    return getCode().equals( CLAIM_APPROVED );
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ClaimFormStepEmailNotificationType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ClaimFormStepEmailNotificationType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ClaimFormStepEmailNotificationType
   */
  public static ClaimFormStepEmailNotificationType lookup( String code )
  {
    return (ClaimFormStepEmailNotificationType)getPickListFactory().getPickListItem( ClaimFormStepEmailNotificationType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return ClaimFormStepEmailNotificationType
   */
  public static ClaimFormStepEmailNotificationType getDefaultItem()
  {
    return (ClaimFormStepEmailNotificationType)getPickListFactory().getDefaultPickListItem( ClaimFormStepEmailNotificationType.class );
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
   * Get the pick list from content manager.
   * 
   * @return List of ClaimFormStepEmailNotificationType specific to claim forms
   */
  public static List getClaimNotificationList()
  {
    List claimNotificationList = new ArrayList();

    claimNotificationList.add( ClaimFormStepEmailNotificationType.lookup( CLAIM_SUBMITTED ) );
    claimNotificationList.add( ClaimFormStepEmailNotificationType.lookup( CLAIM_APPROVED ) );
    claimNotificationList.add( ClaimFormStepEmailNotificationType.lookup( CLAIM_DENIED ) );

    return claimNotificationList;
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ClaimFormStepEmailNotificationType specific to recognition forms
   */
  public static List getRecognitionNotificationList()
  {
    List recognitionNotificationList = new ArrayList();

    recognitionNotificationList.add( ClaimFormStepEmailNotificationType.lookup( RECOGNITION_SUBMITTED ) );
    recognitionNotificationList.add( ClaimFormStepEmailNotificationType.lookup( RECOGNITION_APPROVED_EMAIL ) );

    return recognitionNotificationList;
  }
  
  /**
   * Get the pick list from content manager.
   * 
   * @return List of ClaimFormStepEmailNotificationType specific to nomination forms
   */
  public static List getNominationNotificationList()
  {
    List nominationNotificationList = new ArrayList();
    
      //Client customizations for WIP #59461
    nominationNotificationList.add( ClaimFormStepEmailNotificationType
                                              .lookup( TO_NOMINATOR_WHEN_SUBMITTED ) );

    return nominationNotificationList;
  }
}
