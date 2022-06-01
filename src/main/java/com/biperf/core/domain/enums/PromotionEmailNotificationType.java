/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PromotionEmailNotificationType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The PromotionEmailNotificationType is a concrete instance of a PickListItem which wrappes a type
 * save enum object of a PickList from content manager.
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
 * <td>sedey</td>
 * <td>Aug 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionEmailNotificationType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promo.email.notification.type";

  public static final String PROGRAM_LAUNCH = "program_launch";
  public static final String PROGRAM_END = "program_end";
  public static final String PARTICIPANT_INACTIVITY = "pax_inactivity";
  public static final String PARTICIPANT_INACTIVITY_RECOGNITION = "pax_inactivity_recognition";
  public static final String PARTICIPANT_INACTIVITY_NOMINATION = "pax_inactivity_nomination";
  public static final String APPROVER_REMINDER = "approver_reminder";

  // Nominations specific promotion notification types
  public static final String TO_NOMINEE_WHEN_CLAIM_SUBMITTED = "to_nominee_submitted";
  public static final String TO_NOMINEES_MANAGER_WHEN_CLAIM_SUBMITTED = "to_nominee_mgr_submitted";
  public static final String TO_APPROVER_WHEN_CLAIM_SUBMITTED = "to_approver_submitted";
  public static final String TO_NOMINEE_WHEN_WINNER = "to_nominee_winner";
  public static final String TO_NOMINEES_MANAGER_WHEN_WINNER = "to_nominees_mgr_winner";
  public static final String TO_NOMINATOR_WHEN_WINNER = "to_nominator_winner";
  public static final String APPROVER_REMINDER_TIME_PERIOD_EXPIRED = "approver_reminder_tp_expired";
  public static final String APPROVER_REMINDER_APPROVAL_END_DATE = "approver_reminder_approval_end_date";
  public static final String APPROVER_REQUEST_MORE_INFORMATION = "approver_request_more_information";
  public static final String NOMINATOR_REQUEST_MORE_INFORMATION = "nominator_request_more_information";
  public static final String PUBLIC_RECOG_ADD_POINTS_FOR_NOMINATION = "public_recog_received_for_nomination";
  public static final String TO_NOMINATOR_WHEN_NON_WINNER = "to_nominator_non_winner";

  // GQ specific promo notification types
  public static final String GOAL_SELECTED = "goal_selected";
  public static final String GOAL_NOT_SELECTED = "goal_not_selected";
  public static final String PROGRESS_UPDATED = "progress_updated";
  public static final String GOAL_ACHIEVED = "goal_achieved";
  public static final String GOAL_NOT_ACHIEVED = "goal_not_achieved";
  public static final String PARTNER_SELECTED = "partner_goal_selected";
  public static final String PARTNER_PROGRESS_UPDATED = "partner_progress_updated";
  public static final String PARTNER_GOAL_ACHIEVED = "partner_goal_achieved";
  public static final String PARTNER_GOAL_NOT_ACHIEVED = "partner_goal_not_achieved";

  // CP specific promo notification types
  public static final String CHALLENGEPOINT_SELECTED = "challengepoint_selected";
  public static final String CHALLENGEPOINT_NOT_SELECTED = "challengepoint_not_selected";
  public static final String CHALLENGEPOINT_ACHIEVED = "challengepoint_achieved";
  public static final String CHALLENGEPOINT_NOT_ACHIEVED = "challengepoint_not_achieved";
  public static final String CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED = "challengepoint_interim_payout_processed";
  public static final String CHALLENGEPOINT_PROGRESS_UPDATED = "challengepoint_progress_updated";
  public static final String CP_PARTNER_GOAL_ACHIEVED = "cp_partner_goal_achieved";
  public static final String CP_PARTNER_GOAL_NOT_ACHIEVED = "cp_partner_goal_not_achieved";
  public static final String CP_PARTNER_GOAL_SELECTED = "cp_partner_goal_selected";
  public static final String CP_PARTNER_PROGRESS_UPDATED = "cp_partner_progress_updated";

  // Merchandise/GiftCode notication types
  public static final String NON_REDEMPTION_REMINDER = "non_redemption_reminder";
  public static final String NON_REDEMP_DESCRIMINATOR_ISSUANCE = "every_days_after_issuance";
  public static final String NON_REDEMP_DESCRIMINATOR_PROMO_END = "days_after_promo_end";
  public static final String GQ_NON_REDEMPTION_REMINDER = "gq_non_redemption_reminder";

  public static final String BUDGET_SWEEP = "budget_sweep";
  public static final String BUDGET_END = "budget_end";
  public static final String BUDGET_REMINDER = "budget_reminder";

  public static final String PURL_MANAGER_NOTIFICATION = "purl_manager_notification";
  public static final String PURL_CONTRIBUTOR_INVITATION = "purl_contributor_invitation";
  public static final String PURL_RECIPIENT_INVITATION = "purl_recipient_invitation";
  public static final String PURL_MANAGER_NONRESPONSE = "purl_manager_nonresponse";
  public static final String PURL_CONTRIBUTOR_NONRESPONSE = "purl_contributor_nonresponse";
  /*Customization for WIP 32479 starts here*/
  public static final String  PURL_EXTERNAL_CONTRIBUTOR_INVITATION = "purl_external_contributor_invitation";
  /*Customization for WIP 32479 ends here*/
  
  /*Customization for WIP 46293
  starts here*/
   public static final String  COKE_PURL_MANAGER_CONTRIBUTOR_INVITATION = "coke_purl_manager_contribution_invitation";
   /*Customization for WIP 46293 ends here*/
  // Recognition received
  public static final String RECOGNITION_RECEIVED = "recognition_received";

  // Badge received
  public static final String BADGE_RECEIVED = "badge_received";

  public static final String CP_NON_REDEMPTION_REMINDER = "cp_non_redemption_reminder";

  public static final String TD_NEXT_ROUND = "next_round";
  public static final String TD_MATCH_OUTCOME = "match_outcome";
  public static final String TD_PROGRESS_UPDATED = "throwdown_progress_updated";

  public static final String PUBLIC_RECOGNITION_ADD_POINTS = "public_recognition_received";

  // Engagement
  public static final String KPM_PARTICIPANT_UPDATE = "participant_kpm_metric_update";
  public static final String KPM_MANAGERS_UPDATE = "manager_kpm_metric_update";

  // Celebration
  public static final String CELEBRATION_MANAGER_NOTIFICATION = "celebration_manager_notification";
  public static final String CELEBRATION_MANAGER_NONRESPONSE = "celebration_manager_nonresponse";
  public static final String CELEBRATION_RECOGNITION_RECEIVED = "celebration_recognition_received";
  public static final String CELEBRATION_PURL_RECIPIENT_INVITATION = "celebration_purl_recipient_invitation";

  // SSI

  // Common Creator Notifications For All Contest Types
  public static final String CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR = "contest_setup_launch_notify_creator";
  public static final String CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR = "contest_approval_status_notify_creator";
  public static final String CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR = "contest_final_result_rem_notify_creator";

  // Common Approver Notifications For All Contest Types
  public static final String CONTEST_APPROVAL_NOTIFY_APPROVER = "contest_approval_notify_aprvr";
  public static final String CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER = "contest_approval_rem_notify_aprvr";
  public static final String CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER = "contest_upd_after_aprvl_stat_notify_aprvr";

  // Common Contest Claim Notifications For All Contest Types
  public static final String CONTEST_CLAIM_APPROVAL_NOTIFY_APPROVER = "contest_claim_approval_notify_approver";
  public static final String CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER = "contest_claim_approval_notify_submitter";

  // SSI - Contest Type: Award Them Now
  public static final String CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW = "contest_award_issuance_to_pax";
  public static final String CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW = "contest_award_issuance_to_creator";
  public static final String CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW = "contest_award_issuance_to_manager";
  public static final String CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW = "contest_notify_approver_award_them_now";

  // SSI - Contest Type: Do This Get That
  public static final String CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT = "contest_launch_notify_creator_do_this_get_that";
  public static final String CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT = "contest_launch_notify_mgr_do_this_get_that";
  public static final String CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT = "contest_launch_notify_pax_do_this_get_that";

  public static final String CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT = "contest_progress_notify_creator_do_this_get_that";
  public static final String CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT = "contest_progress_notify_mgr_do_this_get_that";
  public static final String CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT = "contest_progress_notify_pax_do_this_get_that";

  public static final String CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT = "contest_end_notify_creator_do_this_get_that";
  public static final String CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT = "contest_end_notify_mgr_do_this_get_that";
  public static final String CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT = "contest_end_notify_pax_do_this_get_that";

  public static final String CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT = "contest_final_result_notify_creator_do_this_get_that";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT = "contest_final_result_notify_mgr_do_this_get_that";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT = "contest_final_result_notify_pax_do_this_get_that";

  // SSI - Contest Type: Objectives
  public static final String CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES = "contest_launch_notify_creator_objectives";
  public static final String CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES = "contest_launch_notify_mgr_objectives";
  public static final String CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES = "contest_launch_notify_pax_objectives";

  public static final String CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES = "contest_progress_notify_creator_objectives";
  public static final String CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES = "contest_progress_notify_mgr_objectives";
  public static final String CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES = "contest_progress_notify_pax_objectives";

  public static final String CONTEST_END_NOTIFY_CREATOR_OBJECTIVES = "contest_end_notify_creator_objectives";
  public static final String CONTEST_END_NOTIFY_MGR_OBJECTIVES = "contest_end_notify_mgr_objectives";
  public static final String CONTEST_END_NOTIFY_PAX_OBJECTIVES = "contest_end_notify_pax_objectives";

  public static final String CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES = "contest_final_result_notify_creator_objectives";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES = "contest_final_result_notify_mgr_objectives";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES = "contest_final_result_notify_pax_objectives";

  // SSI - Contest Type: Stack Rank
  public static final String CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK = "contest_launch_notify_creator_stack_rank";
  public static final String CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK = "contest_launch_notify_mgr_stack_rank";
  public static final String CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK = "contest_launch_notify_pax_stack_rank";

  public static final String CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK = "contest_progress_notify_creator_stack_rank";
  public static final String CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK = "contest_progress_notify_mgr_stack_rank";
  public static final String CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK = "contest_progress_notify_pax_stack_rank";

  public static final String CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR = "contest_progress_status_notify_creator";

  public static final String CONTEST_END_NOTIFY_CREATOR_STACK_RANK = "contest_end_notify_creator_stack_rank";
  public static final String CONTEST_END_NOTIFY_MGR_STACK_RANK = "contest_end_notify_mgr_stack_rank";
  public static final String CONTEST_END_NOTIFY_PAX_STACK_RANK = "contest_end_notify_pax_stack_rank";

  public static final String CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK = "contest_final_result_notify_creator_stack_rank";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK = "contest_final_result_notify_mgr_stack_rank";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK = "contest_final_result_notify_pax_stack_rank";

  // SSI - Contest Type: Step It Up
  public static final String CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP = "contest_launch_notify_creator_step_it_up";
  public static final String CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP = "contest_launch_notify_mgr_step_it_up";
  public static final String CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP = "contest_launch_notify_pax_step_it_up";

  public static final String CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP = "contest_progress_notify_creator_step_it_up";
  public static final String CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP = "contest_progress_notify_mgr_step_it_up";
  public static final String CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP = "contest_progress_notify_pax_step_it_up";

  public static final String CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP = "contest_end_notify_creator_step_it_up";
  public static final String CONTEST_END_NOTIFY_MGR_STEP_IT_UP = "contest_end_notify_mgr_step_it_up";
  public static final String CONTEST_END_NOTIFY_PAX_STEP_IT_UP = "contest_end_notify_pax_step_it_up";

  public static final String CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP = "contest_final_result_notify_creator_step_it_up";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP = "contest_final_result_notify_mgr_step_it_up";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP = "contest_final_result_notify_pax_step_it_up";

  public static final String NOMINATION_APPROVER_NOTIFICATION = "approver_notification";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionEmailNotificationType()
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
    return getPickListFactory().getPickList( PromotionEmailNotificationType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionEmailNotificationType lookup( String code )
  {
    return (PromotionEmailNotificationType)getPickListFactory().getPickListItem( PromotionEmailNotificationType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionEmailNotificationType getDefaultItem()
  // {
  // return (PromotionEmailNotificationType)getPickListFactory().getDefaultPickListItem(
  // PromotionEmailNotificationType.class );
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
