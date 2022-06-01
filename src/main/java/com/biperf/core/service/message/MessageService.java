/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/message/MessageService.java,v $
 */

package com.biperf.core.service.message;

import java.util.List;
import java.util.Locale;

import com.biperf.core.domain.message.Message;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

/**
 * MessageService.
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
 * <td>Sep 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface MessageService extends SAO
{
  /** BEAN_NAME for referencing in tests and spring config files. */
  public final String BEAN_NAME = "messageService";

  public static final String BUDGET_APPROVAL_MESSAGE_CM_ASSET_CODE = "message_data.message.12356";
  public static final String COMM_LOG_ESCALATED_TO_MESSAGE_CM_ASSET_CODE = "message_data.message.12874";
  public static final String COMM_LOG_ESCALATED_FROM_MESSAGE_CM_ASSET_CODE = "message_data.message.12868";
  public static final String DEPOSIT_NOTICE_MESSAGE_CM_ASSET_CODE = "message_data.message.12548";
  public static final String PASSED_QUIZ_MESSAGE_CM_ASSET_CODE = "message_data.message.12352";
  public static final String RECOGNITION_RECEIVED_MESSAGE_CM_ASSET_CODE = "message_data.message.13807";
  public static final String WINNING_NOMINEE_MESSAGE_CM_ASSET_CODE = "message_data.message.103090";
  public static final String WINNING_NOMINEES_NOMINATOR_MESSAGE_CM_ASSET_CODE = "message_data.message.104099";
  public static final String WINNING_NOMINEE_MANAGER_MESSAGE_CM_ASSET_CODE = "message_data.message.103091";
  public static final String ECARD_SELECTION_MESSAGE_CM_ASSET_CODE = "message_data.message.15662";
  public static final String WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.15567";
  public static final String GOAL_QUEST_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE = "message_data.message.111202";
  public static final String GOAL_QUEST_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.111201";
  public static final String PRODUCT_CLAIM_MESSAGE_CM_ASSET_CODE = "message_data.message.12338";
  public static final String MANAGER_OVERRIDE_MESSAGE_CM_ASSET_CODE = "message_data.message.106361";
  public static final String SWEEPSTAKES_MESSAGE_CM_ASSET_CODE = "message_data.message.15971";
  public static final String BOUNCEBACK_EMAIL_VERIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.105983";
  public static final String WELCOME_EMAIL_COUNT_ADMIN_MESSAGE_CM_ASSET_CODE = "message_data.message.100823";

  // Process Message Asset Code
  public static final String BATCH_PROMOTION_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.13042";
  public static final String DELAYED_APPROVALS_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.13054";
  public static final String DEPOSIT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.15594";
  public static final String POST_PROCESS_RETRY_SUMMARY_MESSAGE = "message_data.message.20000101";
  public static final String ENROLLMENT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.13034";
  public static final String FILE_LOAD_VERIFY_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.17240";
  public static final String MANAGER_OVERRIDE_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.12892";
  public static final String PARTICIPANT_UPDATE_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.13038";
  public static final String REPORT_TABLE_REFRESH_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.17681";
  public static final String REPORT_EXTRACT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.104710";
  public static final String MAILING_ATTACHMENT_CLEANUP_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.105204";
  public static final String WELCOME_EMAIL_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.13050";
  public static final String STACK_RANK_CREATION_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.102238";
  public static final String BREAK_THE_BANK_BUDGET_MESSAGE_CM_ASSET_CODE = "message_data.message.103829";
  public static final String AUDIENCE_EXTRACTION_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.104061";
  public static final String ESTATEMENT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.13046";
  public static final String PURL_AWARD_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000849";
  public static final String PURL_ARCHIVE_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000848";
  public static final String GOALQUEST_DATA_REPOSITORY_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.100822";

  public static final String BUDGET_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.101449";
  public static final String DEPOSIT_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.101450";
  public static final String HIERARCHY_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.101451";
  public static final String PARTICIPANT_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.17078";
  public static final String LEADERBOARD_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000005";
  public static final String PRODUCT_CLAIM_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.102261";
  public static final String PRODUCT_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.101387";
  public static final String SURVEY_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.103981";

  public static final String INSTANT_POLL_NOTIFY_PARTICIPANT_MESSAGE_CM_ASSET_CODE = "message_data.message.152552";

  public static final String QUIZ_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.104281";
  public static final String PAXBASE_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.107641";
  public static final String PAXGOAL_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.107642";
  public static final String PROGRESS_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.107681";
  public static final String AUTO_VIN_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.107685";
  public static final String AWARD_LEVEL_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000941";
  public static final String GOALQUEST_GOALACHIEVED_MESSAGE_CM_ASSET_CODE = "message_data.message.106847";
  public static final String GOALQUEST_GOALNOTACHIEVED_MESSAGE_CM_ASSET_CODE = "message_data.message.106848";
  public static final String GOALQUEST_GOALSELECTED_MESSAGE_CM_ASSET_CODE = "message_data.message.106842";

  public static final String THROWDOWN_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.107684";

  // Add Partenr Messages here
  public static final String GOALQUEST_PARTNER_GOALSELECTED_MESSAGE_CM_ASSET_CODE = "message_data.message.118001";
  public static final String GOALQUEST_PARTNER_PROGRESSUPDATED_MESSAGE_CM_ASSET_CODE = "message_data.message.118002";
  public static final String GOAL_QUEST_PARTNER_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE = "message_data.message.118003";
  public static final String GOALQUEST_PARTNER_GOAL_ACHIEVED_WITH_PAYOUT_MESSAGE_CM_ASSET_CODE = "message_data.message.118004";
  public static final String GOALQUEST_PARTNER_GOAL_ACHIEVED_NO_PAYOUT_MESSAGE_CM_ASSET_CODE = "message_data.message.118361";
  public static final String GOALQUEST_PARTNER_GOAL_NOT_ACHIEVED_MESSAGE_CM_ASSET_CODE = "message_data.message.118005";
  public static final String GOALQUEST_DETAIL_EXTRACT_MESSAGE_CM_ASSET_CODE = "message_data.message.107141";
  public static final String GOALQUEST_GIFT_CODE_REFUND_MESSAGE_CM_ASSET_CODE = "message_data.message.107861";
  public static final String MERCH_ORDER_GIFT_CODE_REFUND_MESSAGE_CM_ASSET_CODE = "message_data.message.114541";
  public static final String GOALQUEST_PAX_PROGRESS_IMPORT_MESSAGE_CM_ASSET_CODE = "message_data.message.106844";

  public static final String SUMMARY_CM_ASSET_CODE = "message_data.message.13030";
  public static final String RECOGNITION_BUDGET_REMINDER_SUMMARY_CM_ASSET_CODE = "message_data.message.13040";
  public static final String RECOGNITION_GIVER_BUDGET_INACTIVITY_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.14090";
  public static final String BUDGET_REMINDER_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000933";

  public static final String BUDGET_SWEEP_PROMOTION_MESSAGE_CM_ASSETCODE = "message_data.message.123162";
  public static final String BUDGET_SWEEP_NO_PROMOTIONS_MESSAGE_CM_ASSETCODE = "message_data.message.123161";

  // Challengepoint Messages here
  public static final String CHALLENGEPOINT_ACHIEVED_MESSAGE_CM_ASSET_CODE = "message_data.message.124405";
  public static final String CHALLENGEPOINT_NOTACHIEVED_MESSAGE_CM_ASSET_CODE = "message_data.message.124406";
  public static final String CHALLENGEPOINT_DETAIL_EXTRACT_MESSAGE_CM_ASSET_CODE = "message_data.message.124401";
  public static final String CHALLENGEPOINT_SELECTED_MESSAGE_CM_ASSET_CODE = "message_data.message.124408";
  public static final String CHALLENGEPOINT_PROGRESSUPDATED_MESSAGE_CM_ASSET_CODE = "message_data.message.124404";
  public static final String CHALLENGEPOINT_INTERIMPAYOUTPROCESSED_MESSAGE_CM_ASSET_CODE = "message_data.message.124410";
  public static final String CHALLENGEPOINT_WELCOME_MESSAGE_CM_ASSET_CODE = "message_data.message.124403";

  public static final String PAXCPBASE_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.124411";
  public static final String PAXCPLEVEL_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.124421";
  public static final String CPPROGRESS_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.124422";

  public static final String PURL_MANAGER_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000841";
  public static final String PURL_CONTRIBUTOR_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000842";
  public static final String PURL_CONTRIBUTOR_NONRESPONSE_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000845";
  public static final String PURL_MANAGERS_NONRESPONSE_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000844";
  public static final String PURL_RECIPIENT_INVITATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000843";
  public static final String PURL_AUTO_SEND_INVITES_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000825";

  public static final String BUDGET_REALLOCATION_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000881";
  public static final String BUDGET_REALLOCATION_RECIPIENT_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.129001";

  public static final String CAMPAIGN_TRANSFER_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000821";
  public static final String GLOBAL_FILE_UPLOAD_MESSAGE_CM_ASSET_CODE = "message_data.message.129002";
  public static final String BADGE_RECEIVED_MESSAGE_CM_ASSET_CODE = "message_data.message.10000822";

  public static final String LEADERBOARD_CREATE_UPDATE_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000850";

  public static final String BADGE_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000860";

  public static final String MANAGER_ALERT_MESSAGE_CM_ASSET_CODE = "message_data.message.17711";

  public static final String PROCESS_FAILED_MESSAGE_CM_ASSET_CODE = "message_data.message.10000893";

  public static final String REPORT_EXTRACT_MESSAGE_CM_ASSET_CODE = "message_data.message.10000872";

  public static final String ENGAGEMENT_SCORE_REFRESH_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.156489";

  public static final String PLATEAU_AWARDS_REMINDER_MESSAGE_CM_ASSET_CODE = "message_data.message.10000823";
  public static final String RECOGNITION_PLATEAU_AWARDS_REMINDER_MESSAGE_CM_ASSET_CODE = "message_data.message.110961";

  public static final String AWARD_FILE_EXTRACT_MESSAGE_CM_ASSET_CODE = "message_data.message.10000861";
  public static final String AWARD_FILE_LAUNCH_MANAGER_MESSAGE_CM_ASSET_CODE = "message_data.message.100862";
  public static final String AWARD_FILE_LAUNCH_ADMIN_MESSAGE_CM_ASSET_CODE = "message_data.message.100863";

  public static final String CHALLENGEPOINT_PARTNER_GOALSELECTED_MESSAGE_CM_ASSET_CODE = "message_data.message.128001";
  public static final String CHALLENGEPOINT_PARTNER_PROGRESSUPDATED_MESSAGE_CM_ASSET_CODE = "message_data.message.128002";
  public static final String CHALLENGEPOINT_PARTNER_GOAL_ACHIEVED_WITH_PAYOUT_MESSAGE_CM_ASSET_CODE = "message_data.message.128004";
  public static final String CHALLENGEPOINT_PARTNER_GOAL_NOT_ACHIEVED_MESSAGE_CM_ASSET_CODE = "message_data.message.128005";
  public static final String CHALLENGEPOINT_PARTNER_GOAL_ACHIEVED_NO_PAYOUT_MESSAGE_CM_ASSET_CODE = "message_data.message.128006";

  public static final String THROWDOWN_ROUND_PAYOUT_EXTRACT_MESSAGE_CM_ASSET_CODE = "message_data.message.10000894";
  public static final String THROWDOWN_PROGRESSUPDATED_LEAD_MESSAGE_CM_ASSET_CODE = "message_data.message.110824";
  public static final String THROWDOWN_PROGRESSUPDATED_TIE_MESSAGE_CM_ASSET_CODE = "message_data.message.110827";
  public static final String THROWDOWN_PROGRESSUPDATED_TRAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.110828";

  public static final String THROWDOWN_MATCH_OUTCOME_LEAD_MESSAGE_CM_ASSET_CODE = "message_data.message.110826";
  public static final String THROWDOWN_MATCH_OUTCOME_TIE_MESSAGE_CM_ASSET_CODE = "message_data.message.110830";
  public static final String THROWDOWN_MATCH_OUTCOME_TRAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.110831";
  public static final String THROWDOWN_POINTS_DEPOSITED_MESSAGE_CM_ASSET_CODE = "message_data.message.110829";

  public static final String THROWDOWN_MANAGER_PROMO_LAUNCH_MESSAGE_CM_ASSET_CODE = "message_data.message.110832";
  public static final String THROWDOWN_MANAGER_MATCH_ANNOUNCEMENT_MESSAGE_CM_ASSET_CODE = "message_data.message.110833";
  public static final String THROWDOWN_MANAGER_MATCH_PROGRESS_UPDATE_MESSAGE_CM_ASSET_CODE = "message_data.message.110834";
  public static final String THROWDOWN_MANAGER_ROUND_END_MESSAGE_CM_ASSET_CODE = "message_data.message.110835";

  public static final String DIY_QUIZ_NOTIFY_PARTICIPANT_MESSAGE_CM_ASSET_CODE = "message_data.message.10000826";
  public static final String QUIZ_NOTIFY_PARTICIPANT_MESSAGE_CM_ASSET_CODE = "message_data.message.17710";
  public static final String SWEEPSTAKES_AWARD_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.28585";
  public static final String THROWDOWN_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE = "message_data.message.110836";
  public static final String THROWDOWN_MANAGER_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE = "message_data.message.110837";

  public static final String PUBLIC_RECOGNITION_ADD_POINTS = "message_data.message.138807";

  public static final String CELEBRATION_MANAGER_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10021841";
  public static final String CELEBRATION_MANAGER_NONRESPONSE_MESSAGE_CM_ASSET_CODE = "message_data.message.10021842";
  public static final String CELEBRATION_RECOGNITION_RECEIVED_MESSAGE_CM_ASSET_CODE = "message_data.message.10021843";
  public static final String CELEBRATION_PURL_RECIPIENT_INVITATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10021844";

  public static final String RPM_NOTIFICATION_EMAIL_MESSAGE_FOR_TEAM_CM_ASSET_CODE = "message_data.message.10001620";

  public static final String RPM_NOTIFICATION_EMAIL_MESSAGE_FOR_USER_CM_ASSET_CODE = "message_data.message.10001640";
  public static final String ENGAGEMENT_NOTIFICATION_SUMMARY = "message_data.message.10001720";

  // SSI Promotion

  // Common Creator Notifications For All Contest Types
  public static final String SSI_CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR = "message_data.message.10000006";
  public static final String SSI_CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR = "message_data.message.10000101";
  public static final String SSI_CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR = "message_data.message.10000090";
  public static final String SSI_CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR = "message_data.message.10000091";
  public static final String SSI_CONTEST_EDIT_NOTIFY_CREATOR = "message_data.message.10000191";

  // Common Approver Notifications For All Contest Types
  public static final String SSI_CONTEST_APPROVAL_NOTIFY_APPROVER = "message_data.message.10000092";
  public static final String SSI_CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER = "message_data.message.10000093";
  public static final String SSI_CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER = "message_data.message.10000094";

  // SSI - Contest Type: Award Them Now
  public static final String SSI_CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW = "message_data.message.10000095";
  public static final String SSI_CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW = "message_data.message.10000096";
  public static final String SSI_CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW = "message_data.message.10000097";
  public static final String SSI_CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW = "message_data.message.10000100";

  // SSI - Contest Type: Do This Get That
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT = "message_data.message.10000070";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT = "message_data.message.10000017";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT = "message_data.message.10000018";

  public static final String SSI_CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT = "message_data.message.10000074";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT = "message_data.message.10000078";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT = "message_data.message.10000019";

  public static final String SSI_CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT = "message_data.message.10000082";
  public static final String SSI_CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT = "message_data.message.10000086";
  public static final String SSI_CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT = "message_data.message.10000020";

  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT = "message_data.message.10000063";
  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT = "message_data.message.10000062";
  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT = "message_data.message.10000021";

  // SSI - Contest Type: Objectives
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_CREATOR_MGR_OBJECTIVES = "message_data.message.10000071";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES = "message_data.message.10000027";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES = "message_data.message.10000028";

  public static final String SSI_CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES = "message_data.message.10000075";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES = "message_data.message.10000079";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES = "message_data.message.10000029";

  public static final String SSI_CONTEST_END_NOTIFY_CREATOR_OBJECTIVES = "message_data.message.10000083";
  public static final String SSI_CONTEST_END_NOTIFY_MGR_OBJECTIVES = "message_data.message.10000087";
  public static final String SSI_CONTEST_END_NOTIFY_PAX_OBJECTIVES = "message_data.message.10000030";

  public static final String SSI_CONTEST_FINAL_RESULT_CREATOR_NOTIFY_OBJECTIVES = "message_data.message.10000064";
  public static final String SSI_CONTEST_FINAL_RESULT_MGR_NOTIFY_OBJECTIVES = "message_data.message.10000065";
  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES = "message_data.message.10000031";

  // SSI - Contest Type: Stack Rank
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK = "message_data.message.10000072";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK = "message_data.message.10000037";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK = "message_data.message.10000038";

  public static final String SSI_CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK = "message_data.message.10000076";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK = "message_data.message.10000080";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK = "message_data.message.10000039";

  public static final String SSI_CONTEST_END_NOTIFY_CREATOR_STACK_RANK = "message_data.message.10000084";
  public static final String SSI_CONTEST_END_NOTIFY_MGR_STACK_RANK = "message_data.message.10000088";
  public static final String SSI_CONTEST_END_NOTIFY_PAX_STACK_RANK = "message_data.message.10000040";

  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK = "message_data.message.10000067";
  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK = "message_data.message.10000066";
  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK = "message_data.message.10000041";

  // SSI - Contest Type: Step It Up
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP = "message_data.message.10000073";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP = "message_data.message.10000047";
  public static final String SSI_CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP = "message_data.message.10000048";

  public static final String SSI_CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP = "message_data.message.10000077";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP = "message_data.message.10000081";
  public static final String SSI_CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP = "message_data.message.10000049";

  public static final String SSI_CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP = "message_data.message.10000085";
  public static final String SSI_CONTEST_END_NOTIFY_MGR_STEP_IT_UP = "message_data.message.10000089";
  public static final String SSI_CONTEST_END_NOTIFY_PAX_STEP_IT_UP = "message_data.message.10000050";

  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP = "message_data.message.10000068";
  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP = "message_data.message.10000069";
  public static final String SSI_CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP = "message_data.message.10000051";

  public static final String SSI_CONTEST_STACKRANK_UPDATE_MESSAGE_CM_ASSETCODE = "message_data.message.10000099";

  public static final String SSI_CONTEST_ARCHIVAL_PROCESS_MESSAGE_CM_ASSETCODE = "message_data.message.10000199";

  // Budget Redistribution
  public static final String BUDGET_REDISTRIBUTION_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000104";

  // campaign-balance transfer
  public static final String CAMPAIGN_TRANS_ACCT_BAL_TRANSFER_SUMMARY_MESSAGE_CM_ASSET_CODE = "message_data.message.10000109";

  public static final String WORK_HAPPIER_REPORT_EXTRACT_MESSAGE_CM_ASSET_CODE = "message_data.message.10000108";

  public static final String UPDATE_CASH_CURRENCIES_SUMMARY_MESSAGE_CM_ASSET_CODE = "message_data.message.10000110";

  public static final String REFRESH_PENDING_NOMINATION_APPROVER_SUMMARY_MESSAGE_CM_ASSET_CODE = "message_data.message.10000129";

  public static final String NOMINATION_CUSTOM_APPROVER_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000111";
  public static final String NOMINATION_APPROVER_NOTIFICATION = "message_data.message.10000128";
  public static final String NOMINATION_NOMINEE_NOTIFICATION = "message_data.message.103084";
  public static final String NOMINATION_SUBMITTED_NOTIFICATION = "message_data.message.103089";
  public static final String MAILING_RETRY_PROCESS_CM_ASSET_CODE = "message_data.message.20000100";
  public static final String NOMINATOR_REQUEST_MORE_INFO = "message_data.message.104095";
  public static final String APPROVER_REQUEST_MORE_INFO_RECEIVED = "message_data.message.104094";
  public static final String NOMINATOR_NON_WINNER = "message_data.message.104098";
  public static final String NOMINATION_REQUEST_MORE_BUDGET = "message_data.message.104062";
  // public static final String NOMINEE_NON_WINNER = "message_data.message.103097";

  public static final String SSI_CONTEST_ATN_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000201";
  public static final String SSI_CONTEST_DTGT_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000202";
  public static final String SSI_CONTEST_SIU_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000203";
  public static final String SSI_CONTEST_SR_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000204";
  public static final String SSI_CONTEST_OBJ_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000205";

  public static final String OPT_OUT_OF_AWARDS_NOTIFY_PAX = "message_data.message.10000301";
  public static final String OPT_OUT_OF_AWARDS_NOTIFY_MANAGER = "message_data.message.10000302";

  public static final String FORGOT_LOGIN_ID = "message_data.message.10000303";
  public static final String FORGOT_PASSWORD = "message_data.message.10000304";
  public static final String PASSWORD_CHANGE = "message_data.message.10000305";
  // public static final String REISSUE_SEND_PASSWORD_MESSAGE_CM_ASSET_CODE =
  // "message_data.message.105020";
  public static final String ACCOUNT_ACTIVATION_EMAIL = "message_data.message.10000306";
  public static final String FORGOT_LOGIN_ID_ALERT = "message_data.message.10000310";
  public static final String ACCOUNT_ACTIVATED = "message_data.message.10000311";

  public static final String MERCH_ORDER_GIFT_CODE_RETRY_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000320";
//For Sea  

  public static final String SEA_EMAIL_DEAR_CUSTOMER_MESSAGE = "message_data.message.1068001";
  public static final String SEA_EMAIL_DEAR_MESSAGE = "message_data.message.1068002";
  public static final String SEA_EMAIL_NOT_ENOUGH_POINT_MESSAGE = "message_data.message.1068003";
  // public static final String SEA_EMAIL_STILL_WISH_TO_SEND_MESSAGE =
  // "message_data.message.1068004";
  public static final String SEA_EMAIL_SEND_FIXEDPOINT = "message_data.message.1068005";
  public static final String SEA_EMAIL_OUTOF_POINT_RANGE = "message_data.message.1068006";
  public static final String SEA_SYSTEM_REQUIRE_BEHAVIOUR = "message_data.message.1068007";
  public static final String SEA_EMAIL_BOX_RECEIVED_RECOGNITION_MESSAGE = "message_data.message.1068008";
  public static final String SEA_EMAIL_CONFIRMATION_TO_ISSUE_POINT_MESSAGE = "message_data.message.1068009";
  public static final String SEA_EMAIL_FOR_MULTIPLE_PERSON_MESSAGE = "message_data.message.1068010";
  public static final String SEA_EMAIL_INELIGIBLE_FOR_ISSUE_PROMO_MESSAGE = "message_data.message.1068011";
  public static final String SEA_EMAIL_INELIGIBLE_TO_RECEIVE_RECOGNITION_MESSAGE = "message_data.message.1068012";
  public static final String SEA_EMAIL_INVALID_MESSAGE = "message_data.message.1068016";
  public static final String SEA_EMAIL_INVALID_HASHTAG_MESSAGE = "message_data.message.1068014";
  public static final String SEA_EMAIL_NO_REPLY_RECEIVED_MESSAGE = "message_data.message.1068015";
  public static final String SEA_EMAIL_NON_FOUND_RECIPIENTS_MESSAGE = "message_data.message.1068013";
  // public static final String SEA_EMAIL_NON_POSTABLE_MESSAGE = "message_data.message.1068017";
  public static final String SEA_EMAIL_OUT_OF_BUDGET_PROMOTION_MESSAGE = "message_data.message.1068018";
  public static final String SEA_EMAIL_POINT_BALANCE_MESSAGE = "message_data.message.1068019";
  // public static final String SEA_EMAIL_TO_SAVE_RECOGNITION_MESSAGE =
  // "message_data.message.1068021";
  public static final String SEA_EMAIL_VALID_RECIPIENT_MESSAGE = "message_data.message.1068022";
  public static final String SEA_EMAIL_RECIPIENT_NOT_FOUND = "message_data.message.1068023";
  public static final String SEA_EMAIL_RECIPIENT_NOT_UNIQUE = "message_data.message.1068024";
  public static final String SEA_EMAIL_SEND_BEHAVIOURS = "message_data.message.1068025";
  public static final String SEA_EMAIL_STILL_WISH_ADJUST_POINTS = "message_data.message.1068026";
  public static final String SEA_EMAIL_FIXEDPOINT = "message_data.message.1068027";
  public static final String SEA_EMAIL_POINT_RANGE = "message_data.message.1068028";
  public static final String SEA_EMPTY_BODY_MESSAGE = "message_data.message.1068029";
  public static final String SEA_EMAIL_NO_PUBLIC_RECOGNITION = "message_data.message.1068030";
  public static final String SEA_NO_EMAIL_ENABLED_PROMOTION = "message_data.message.1068031";
  public static final String SEA_TO_SAVE_THIS_PROMOTION = "message_data.message.1068032";
  public static final String SEA_BEHAVIOR_INACTIVE = "message_data.message.1068033";
  public static final String SEA_EMAIL_FOOTER = "message_data.message.1068034";
  public static final String SEA_EMAIL_CORRECTABLE_ERROR_PREFIX = "message_data.message.1068035";
  public static final String SEA_EMAIL_NOT_CORRECTABLE_ERROR_PREFIX = "message_data.message.1068036";
  public static final String SEA_EMAIL_NOT_CORRECTABLE_ERROR_SUFFIX = "message_data.message.1068037";
  public static final String SEA_EMAIL_MISSING_END_TAG = "message_data.message.1068038";
  public static final String SEA_EMAIL_CORRECTABLE_ERROR_SUFFIX = "message_data.message.1068039";
  public static final String SEA_EMAIL_NO_UNIQUE_RECIPIENTS = "message_data.message.1068040";
  // public static final String SEA_EMAIL_GENERIC_ERROR = "message_data.message.1068041" ;

  // newly added for sea
  public static final String SEA_EMAIL_NO_RECIPIENT_INCLUDED = "message_data.message.1068042";
  public static final String SEA_EMAIL_SELF_RECOGNITION_NOT_ALLOWED = "message_data.message.1068043";
  public static final String SEA_EMAIL_SYSTEM_DOWN = "message_data.message.1068044";
 
  // Recognition Advisor
  public static final String RECOGNITION_ADVISOR_TEAM_MEMBER_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.10000491";
  public static final String RECOGNITION_ADVISOR_NEW_HIRE_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.10000492";
  public static final String RECOGNITION_ADVISOR_WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.64000001";

  // Recovery contact method verification
  public static final String RECOVERY_VERIFICATION_EMAIL = "message_data.message.155660000";
  public static final String RECOVERY_CHANGE_NOTIFICATION = "message_data.message.155660001";

  public static final String INACTIVE_BIW_USERS_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000501";
  public static final String ACCOUNT_UN_LOCKED = "message_data.message.64000002";

  // Auto Rollup Budget Transfer Mails
  public static final String BUDGET_TRANSFER_GIVER_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.20000102";
  public static final String BUDGET_TRANSFER_RECIVER_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.20000103";
  public static final String BUDGET_TRANSFER_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.20000104";

  public static final String KINESISLISTENER_AUTORETRY_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.20000105";
  public static final String KINESIS_EVENTS_STATUS_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.20000106";
  
  //Client customizations for wip #23129 starts
  public static final String CLIENT_GIFT_CODE_SWEEP_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.20001721";
  // Client customizations for wip #23129 ends

  //Client customization for WIP #44575 starts
  public static final String COKE_DAY_EMAIL_MESSAGE_CM_ASSET_CODE = "message_data.message.20008601";
  //Client customization for WIP #44575 ends 

  // Client customization for WIP #41580 starts
  public static final String COKE_NON_MILESTONE_SERVICE_ANNIVERSARY_MESSAGE_CM_ASSET_CODE = "message_data.message.10000114";
  // Client customization for WIP #41580 ends
  public static final String GENERATE_BUDGET_FILE_WD_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000112";
  
  // Client customization for WIP #57733 starts
  public static final String COKE_PUSH_REPORT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.client111";
  public static final String COKE_PUSH_REPORT_RECIPIENT_MESSAGE_CM_ASSET_CODE = "message_data.message.client112";
  // Client customization for WIP #57733 ends
  public static final String COKE_PAYROLL_FILE_EXTRACT_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.coke100001";
  // Client customization for WIP #25836 starts
  public static final String GENERATE_BUDGET_FILE_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000111";
  // Client customization for WIP #25836 ends
  // Client customization for WIP #41580 starts
  public static final String COKE_FIND_SERVICE_ANNIVERSARY_PROCESS_MESSAGE_CM_ASSET_CODE = "message_data.message.10000113";
  // Client customization for WIP #41580 ends
  public static final String PENDING_AWARD_APPROVAL_MESSAGE_CM_ASSET_CODE ="message_data.message.12356"; 
  
  // wip # 46293 custom
  public static final String COKE_PURL_MANAGER_INVITE_CONTRIBUTORS_CM_ASSET_CODE = "message_data.message.client105";
  // wip # 46293 custom
  /* TCCC  - customization start - wip 46975 */
  public static final String COKE_SEND_RECOGNIZE_ANYONE_MESSAGE_CM_ASSET_CODE ="message_data.message.client106";
  /* TCCC  - customization end */
  /* Customization for WIP 32479 starts here */
  public static final String PURL_EXTERNAL_CONTRIBUTOR_NOTIFICATION_MESSAGE_CM_ASSET_CODE = "message_data.message.10000847";
  /* Customization for WIP 32479 ends here */
  /* TCCC - customization start */
  public static final String COKE_RECOGNIZED_RECEIVED_CUSTOM_MESSAGE_CM_ASSET_CODE = "message_data.message.client107";
  public static final String COKE_RECOGNIZED_RECEIVED_POINTS_ONLY_CUSTOM_MESSAGE_CM_ASSET_CODE = "message_data.message.client108";
    // Client customization for WIP 58122 starts
  public static final String CUSTOM_NOMINATION_WINNER_CM_ASSET_CODE = "message_data.message.client113";
  // Client customization for WIP 58122 ends
  
  public static final String CUSTOM_LIKES_COMMENTS_NOTIFICATION = "message_data.message.client120";//message_data.message.20017740
  /* TCCC - customization end */
 public static final String MC_INACTIVE_BIW_USERS_PROCESS_MESSAGE_CM_ASSET_CODE ="message_data.message.client104";
 public static final String TCC_ADIH_EXPIRE_TEMP_PWD = "message_data.message.10000110";
  
  
  /**
   * Get the Message for a given Message Id.
   * 
   * @param messageId
   * @return Message
   */
  public Message getMessageById( Long messageId );

  /**
   * Gets all messages
   * 
   * @return list of messages
   */
  public List getAll();

  /**
   * Get all active messages
   * 
   * @return List of all messages for the given status code.
   */
  public List getAllActiveMessages();

  /**
   * Get all active messages by MessageModuleType
   * 
   * @param code
   * @return List of all active messages for the given module type code.
   */
  public List getAllActiveMessagesByModuleType( String code );

  public List getAllActiveMessagesByTypecode( String code );

  /**
   * Get all active text messages
   * 
   * @return List of all active messages that contain a text message
   */
  public List getAllActiveTextMessages();

  /**
   * Get all inactive messages
   * 
   * @return List of all messages for the given status code.
   */
  public List getAllInactiveMessages();

  /**
   * Get the Message for a given Message name
   * 
   * @param cmAssetCode
   * @return Message
   */
  public Message getMessageByCMAssetCode( String cmAssetCode );

  /**
   * Get All messages based on status code
   * 
   * @param statusCode
   * @return List of all messages for that status
   */
  public List getAllMessagesByStatus( String statusCode );

  public List getAllActiveTextMessagesBySMSGroupType( String messageSMSGroupTypeCode );

  /**
   * Saves a message. Places subject, htmlMsg, plainTextMsg, and textMsg into CM - for current locale
   * 
   * @param message
   * @param subject
   * @param htmlMsg
   * @param plainTextMsg
   * @param textMsg
   * @param strongMailSubject
   * @param strongMailMsg
   * @return Message
   * @throws ServiceErrorException
   */
  public Message saveMessage( Message message, String subject, String htmlMsg, String plainTextMsg, String textMsg, String strongMailSubject, String strongMailMsg ) throws ServiceErrorException;

  /**
   * Saves a message. Places subject, htmlMsg, plainTextMsg, and textMsg into CM - for given locale
   * 
   * @param message
   * @param subject
   * @param htmlMsg
   * @param plainTextMsg
   * @param textMsg
   * @param strongMailSubject
   * @param strongMailMsg
   * @param locale
   * @return Message
   * @throws ServiceErrorException
   */
  public Message saveMessage( Message message, String subject, String htmlMsg, String plainTextMsg, String textMsg, String strongMailSubject, String strongMailMsg, Locale locale )
      throws ServiceErrorException;

  /**
   * Places subject, htmlMsg, plainTextMsg, and textMsg into CM for an message - for given locale
   * 
   * @param message
   * @param subject
   * @param htmlMsg
   * @param plainTextMsg
   * @param textMsg
   * @param strongMailSubject
   * @param strongMailMsg
   * @param locale
   * @return Message
   * @throws ServiceErrorException
   */
  public void saveMessageCmText( Message message, String subject, String htmlMsg, String plainTextMsg, String textMsg, String strongMailSubject, String strongMailMsg, Locale locale )
      throws ServiceErrorException;

}
