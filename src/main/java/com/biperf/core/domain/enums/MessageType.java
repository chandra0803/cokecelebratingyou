/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/MessageType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * MessageType is a concrete instance of a PickListItem which wrapes a type save enum object
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
 * <td>gadapa</td>
 * <td>Jan 16, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class MessageType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.message.type";

  public static final String GENERAL = "general";
  public static final String APPROVER_RECOGNITION_SUBMITTED = "approver_recognition_submitted";
  public static final String GIVER_RECOGNITION_APPROVAL_DECISION = "giver_recognition_approval_decision";
  public static final String APPROVER_REMINDER = "approver_reminder";
  public static final String PROMOTION_LAUNCH = "promotion_launch";
  public static final String PARTICIPANT_INACTIVITY = "participant_inactivity";
  public static final String PROMOTION_END = "promotion_end";
  public static final String NON_REDEMPTION_REMINDER = "non_redemption_reminder";
  public static final String GQ_NON_REDEMPTION_REMINDER = "gq_non_redemption_reminder";
  public static final String NOMINEE_MGR_NOMINATION_SUBMITTED = "nominee_mgr_nomination_submitted";
  public static final String NOMINEE_NOMINATION_SUBMITTED = "nominee_nomination_submitted";
  public static final String APPROVER_NOMINATION_SUBMITTED = "approver_nomination_submitted";
  public static final String NOMINEE_MGR_WINNER = "nominee_mgr_winner";
  public static final String NOMINATOR_WINNER = "nominator_winner";
  public static final String NOMINEE_WINNER = "nominee_winner";
  public static final String NOMINATOR_NON_WINNER = "to_nominator_non_winner";
  public static final String PUBLIC_RECOG_ADD_POINTS_FOR_NOMINATION = "public_recog_received_for_nomination";
  public static final String APPROVER_REMINDER_TIME_PERIOD_EXPIRED = "approver_reminder_tp_expired";
  public static final String APPROVER_REMINDER_APPROVAL_END_DATE = "approver_reminder_approval_end_date";
  public static final String APPROVER_REQUEST_MORE_INFORMATION = "approver_request_more_information";
  public static final String NOMINATOR_REQUEST_MORE_INFORMATION = "nominator_request_more_information";
  public static final String NOMINATION_REQUEST_MORE_BUDGET = "nomination_request_more_budget";
  public static final String PARTICIPANT_CLAIM_REVIEWED = "participant_claim_reviewed";
  public static final String APPROVER_CLAIM_SUBMITTED = "approver_claim_submitted";
  public static final String PARTICIPANT_GOAL_SELECTED = "participant_goal_selected";
  public static final String PARTICIPANT_GOAL_NOT_SELECTED = "participant_goal_not_selected";
  public static final String PARTICIPANT_PROGRESS_UPDATED = "participant_progress_updated";
  public static final String PARTICIPANT_GOAL_ACHIEVED = "participant_goal_achieved";
  public static final String PARTICIPANT_GOAL_NOT_ACHIEVED = "participant_goal_not_achieved";
  public static final String PARTNER_GOAL_ACHIEVED = "partner_goal_achieved";
  public static final String PARTNER_GOAL_NOT_ACHIEVED = "partner_goal_not_achieved";
  public static final String PARTNER_GOAL_SELECTED = "partner_goal_selected";
  public static final String PARTNER_PROGRESS_UPDATED = "partner_progress_updated";
  public static final String BUDGET_SWEEP = "budget_sweep";
  public static final String WELCOME_MESSAGE = "welcome_message";
  public static final String BUDGET_END = "budget_end";
  public static final String BUDGET_REMINDER = "budget_reminder";
  public static final String DEPOSIT = "deposit";

  public static final String RECOGNITION_RECEIVED = "recognition_received";
  public static final String BADGE_RECEIVED = "badge_received";

  public static final String PURL_MANAGER_NOTIFICATION = "purl_manager_notification";
  public static final String PURL_CONTRIBUTOR_INVITATION = "purl_contributor_invitation";
  public static final String PURL_RECIPIENT_INVITATION = "purl_recipient_invitation";
  public static final String PURL_MANAGER_NONRESPONSE = "purl_manager_nonresponse";
  public static final String PURL_CONTRIBUTOR_NONRESPONSE = "purl_contributor_nonresponse";

  /*Customization for WIP 32479 starts here*/
  public static final String  PURL_EXTERNAL_CONTRIBUTOR_INVITATION = "purl_external_contributor_invitation";
  /*Customization for WIP 32479 ends here*/
  
  /*Customization for WIP 46293 starts here*/
  public static final String  COKE_PURL_MANAGER_CONTRIBUTOR_INVITATION = "coke_purl_manager_contribution_invitation";
  /*Customization for WIP 46293 ends here*/
  public static final String PARTICIPANT_CHALLENGEPOINT_SELECTED = "participant_challengepoint_selected";
  public static final String PARTICIPANT_CHALLENGEPOINT_NOT_SELECTED = "participant_challengepoint_not_selected";
  public static final String PARTICIPANT_CHALLENGEPOINT_ACHIEVED = "participant_challengepoint_achieved";
  public static final String PARTICIPANT_CHALLENGEPOINT_NOT_ACHIEVED = "participant_challengepoint_not_achieved";
  public static final String PARTICIPANT_CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED = "participant_challengepoint_interim_payout_processed";
  public static final String PARTICIPANT_CHALLENGEPOINT_PROGRESS_UPDATED = "participant_challengepoint_progress_updated";

  // SK - Promotion Type for GQ Survey

  public static final String CP_PARTNER_GOAL_ACHIEVED = "cp_partner_goal_achieved";
  public static final String CP_PARTNER_GOAL_NOT_ACHIEVED = "cp_partner_goal_not_achieved";
  public static final String CP_PARTNER_GOAL_SELECTED = "cp_partner_goal_selected";
  public static final String CP_PARTNER_PROGRESS_UPDATED = "cp_partner_progress_updated";

  public static final String CP_NON_REDEMPTION_REMINDER = "cp_non_redemption_reminder";

  public static final String TD_NEXT_ROUND = "next_round";
  public static final String TD_MATCH_OUTCOME = "match_outcome";
  public static final String TD_MATCH_OUTCOME_TIE = "match_outcome_tie";
  public static final String TD_MATCH_OUTCOME_TRAIL = "match_outcome_trail";
  public static final String TD_PROGRESS_UPDATED = "throwdown_progress_updated";
  public static final String TD_ROUND_EXTRACT = "throwdown_round_extract";
  public static final String TD_PROGRESS_TIE = "throwdown_progress_tie";
  public static final String TD_PROGRESS_TRAIL = "throwdown_progress_trail";
  public static final String TD_POINTS_DEPOSITED = "throwdown_points_deposited";
  public static final String TD_MANAGER_PROMOTION_LAUNCH = "throwdown_manager_promotion_launch";
  public static final String TD_MANAGER_MATCH_ANNOUNCEMENT = "throwdown_manager_match_announcement";
  public static final String TD_MANAGER_MATCH_PROGRESS_UPDATE = "throwdown_manager_progress_updated";
  public static final String TD_MANAGER_ROUND_END = "throwdown_manager_round_end";
  public static final String TD_MANAGER_PROMOTION_LAUNCH_WITH_LOGIN_DETAILS = "throwdown_manager_promotion_launch_with_login_detail";
  public static final String TD_PROMOTION_LAUNCH_WITH_LOGIN_DETAILS = "throwdown_promotion_launch_with_login_detail";

  // KPM(Engagement)
  public static final String KPM_PARTICIPANT_UPDATE = "participant_kpm_metric_update";
  public static final String KPM_MANAGERS_UPDATE = "manager_kpm_metric_update";

  public static final String PUBLIC_RECOGNITION_ADD_POINTS = "public_recognition_received";

  // Celebration
  public static final String CELEBRATION_MANAGER_NOTIFICATION = "celebration_manager_notification";
  public static final String CELEBRATION_MANAGER_NONRESPONSE = "celebration_manager_nonresponse";
  public static final String CELEBRATION_RECOGNITION_RECEIVED = "celebration_recognition_received";
  public static final String CELEBRATION_PURL_RECIPIENT_INVITATION = "celebration_purl_recipient_invitation";

  // SSI

  // Common Creator Notifications For All Contest Types
  public static final String CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR = "contest_setup_launch_notify_creator";
  public static final String CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR = "contest_progress_status_notify_creator";
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

  public static final String CONTEST_FINAL_RESULT_CREATOR_NOTIFY_OBJECTIVES = "contest_final_result_notify_creator_objectives";
  public static final String CONTEST_FINAL_RESULT_MGR_NOTIFY_OBJECTIVES = "contest_final_result_notify_mgr_objectives";
  public static final String CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES = "contest_final_result_notify_pax_objectives";

  // SSI - Contest Type: Stack Rank
  public static final String CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK = "contest_launch_notify_creator_stack_rank";
  public static final String CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK = "contest_launch_notify_mgr_stack_rank";
  public static final String CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK = "contest_launch_notify_pax_stack_rank";

  public static final String CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK = "contest_progress_notify_creator_stack_rank";
  public static final String CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK = "contest_progress_notify_mgr_stack_rank";
  public static final String CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK = "contest_progress_notify_pax_stack_rank";

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
  
  //Client customizations for WIP #59461
  public static final String  NOMINATOR_SUBMITTED = "nominator_submitted";

  

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MessageType()
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
    return getPickListFactory().getPickList( MessageType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static MessageType lookup( String code )
  {
    return (MessageType)getPickListFactory().getPickListItem( MessageType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static MessageType getDefaultItem()
  // {
  // return (MessageType)getPickListFactory().getDefaultPickListItem( MessageType.class );
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
} // end MessageType
