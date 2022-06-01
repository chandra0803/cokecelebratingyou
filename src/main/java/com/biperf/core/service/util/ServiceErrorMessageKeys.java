/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/util/ServiceErrorMessageKeys.java,v $
 */

package com.biperf.core.service.util;

/**
 * ServiceErrorMessageKeys.
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
 * <td>tennant</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public interface ServiceErrorMessageKeys
{
  // Audience Duplicate Error
  public static final String AUDIENCE_DUPLICATE = "system.errors.DUPLICATE_AUDIENCE";

  // Audience Duplicate Error
  public static final String AUDIENCE_PRECALCULATION_ERROR = "system.errors.AUDIENCE_PRECALCULATION_ERROR";

  // BudgetMaster Errors
  public static final String BUDGET_MASTER_CANNOT_DELETE = "admin.budgetmaster.errors.CANNOT_DELETE";

  // Save Participant Badge Error
  public static final String SAVE_PAX_BADGE_ERR = "system.errors.SAVE_PAX_BADGE_ERR";

  // Budget Errors
  public static final String BUDGET_DUPLICATE = "admin.budget.errors.DUPLICATE";
  public static final String BUDGET_NOT_FOUND = "admin.budget.errors.NOT_FOUND";
  public static final String BUDGET_NONE_TO_TRANSFER = "system.errors.BUDGET_NONE_TO_TRANSFER";
  public static final String BUDGET_NONE_TO_TRANSFER_FOR_SELECTED_SEGMENT = "admin.budget.errors.BUDGET_NONE_TO_TRSFR_SELECTED_SEGMENT";
  public static final String HARD_CAP_BUDGET_EXCEEDED_NOMINATION = "nomination.approval.list.HARD_CAP_EXCEEDED_ERROR";
  public static final String HARD_CAP_BUDGET_EXCEEDED_RECOGNITION = "recognition.approval.list.HARD_CAP_EXCEEDED_ERROR";
  public static final String BUDGET_INSUFFICIENT = "system.general.INSUFFICIENT_BUDGET";
  public static final String DUPLICATE_BUDGET_PERIOD_NAME = "admin.budget.errors.DUPLICATE_PERIOD_NAME";

  // LeaderBoard Errors
  public static final String LEADERBOARD_DUPLICATE = "admin.leaderboard.errors.DUPLICTAE";

  // Country Errors
  public static final String COUNTRY_DUPLICATE_CODE = "admin.country.errors.DUPLICATE_CODE";
  public static final String COUNTRY_DUPLICATE_NAME = "admin.country.errors.DUPLICATE_NAME";
  public static final String COUNTRY_DUPLICATE_AWARDBANQ_ABBREV = "admin.country.errors.DUPLICATE_AWARDBANQ_ABBREV";
  public static final String CAMPAIGN_NUMBER_REQUIRED = "admin.country.errors.CAMPAIGN_NUMBER_REQUIRED";
  public static final String NOT_SMS_CAPABLE = "admin.country.errors.NOT_SMS_CAPABLE";

  // Message Errors
  public static final String MESSAGE_NOT_FOUND = "admin.message.errors.NOT_FOUND";
  public static final String MESSAGE_DUPLICATE_NAME = "admin.message.errors.DUPLICATE_NAME";
  public static final String MESSAGE_TEXT_TOO_LONG = "admin.message.errors.TEXT_TOO_LONG";
  public static final String MESSAGE_AT_LEAST_ONE_REQUIRED = "admin.message.errors.AT_LEAST_ONE_REQUIRED";

  // Calculator
  public static final String CALCULATOR_DUPLICATE_ERR = "calculator.errors.DUPLICATE_CALCULATOR";
  public static final String CALCULATOR_DELETE_STATUS_ERR = "calculator.errors.DELETE_STATUS_ERROR";

  public static final String CLAIM_FORM_DUPLICATE_ERR = "claims.claimform.errors.CLAIM_FORM_DUPLICATE_ERR";
  public static final String CLAIM_FORM_DELETE_STATUS_ERR = "claims.claimform.errors.DELETE_STATUS_ERR";
  public static final String CLAIM_FORM_DELETE_LINKED_ERR = "claims.claimform.errors.DELETE_LINKED_ERR";
  public static final String CLAIM_FORM_STEP_DUPLICATE_ERR = "claims.claimform.errors.CLAIM_FORM_STEP_DUPLICATE_ERR";
  public static final String CLAIM_FORM_STEP_ELEMENT_DUPLICATE_ERR = "claims.claimform.errors.CLAIM_FORM_STEP_ELEMENT_DUPLICATE_ERR";
  public static final String CLAIM_FORM_UNEDITABLE_ERR = "claims.claimform.errors.CLAIM_FORM_UNEDITABLE_ERR";
  public static final String CLAIM_FORM_STEP_UNEDITABLE_ERR = "claims.claimform.errors.CLAIM_FORM_STEP_UNEDITABLE_ERR";
  public static final String CLAIM_FORM_PREVIEW_FORM_NO_STEPS_ERR = "claims.claimform.errors.PREVIEW_FORM_NO_STEPS";
  public static final String CLAIM_FORM_PREVIEW_FORM_NO_ELEMENTS_ERR = "claims.claimform.errors.PREVIEW_FORM_NO_ELEMENTS";

  public static final String CLAIM_PRODUCT_CHARACTERISTIC_NOT_UNIQUE = "claims.submission.errors.CLAIM_PRODUCT_CHARACTERISTIC_NOT_UNIQUE";
  public static final String CLAIM_PRODUCT_CHARACTERISTIC_NOT_VALID_FOR_PRODUCT = "claims.submission.errors.CLAIM_PRODUCT_CHARACTERISTIC_NOT_VALID";
  public static final String CLAIM_ELEMENT_VALUE_NOT_UNIQUE_WITHIN_NODE = "claims.submission.errors.VALUE_NOT_UNIQUE_WITHIN_NODE";
  public static final String CLAIM_ELEMENT_VALUE_NOT_UNIQUE_WITHIN_NODE_TYPE = "claims.submission.errors.VALUE_NOT_UNIQUE_WITHIN_NODE_TYPE";
  public static final String CLAIM_ELEMENT_VALUE_NOT_UNIQUE_WITHIN_HIERARCHY = "claims.submission.errors.VALUE_NOT_UNIQUE_WITHIN_HIERARCHY";
  public static final String CLAIM_ELEMENT_VALUE_NOT_VALID = "claims.submission.errors.VALUE_NOT_VALID";
  public static final String CLAIM_DATE_NOT_VALID_FOR_PROMOTION = "claims.submission.errors.CLAIM_DATE_NOT_VALID_FOR_PROMOTION";
  public static final String CLAIM_SUBMITTER_INELIGIBLE_FOR_PROMOTION = "claims.submission.errors.CLAIM_SUBMITTER_INELIGIBLE_FOR_PROMOTION";
  public static final String CLAIM_SUBMITTER_NOT_PART_OF_PRIMARY_HIERARCHY = "claims.submission.errors.CLAIM_SUBMITTER_NOT_IN_PRIMARY_HIERARCHY";
  public static final String CLAIM_TRACK_TO_NODE_NOT_IN_USERS_NODE = "claims.submission.errors.CLAIM_TRACK_TO_NODE_NOT_IN_USERS_NODE";
  public static final String CLAIM_TRACK_TO_NODE_NOT_IN_PRIMARY_HIERARCHY = "claims.submission.errors.CLAIM_TRACK_TO_NODE_NOT_IN_PRIM_HIER";
  public static final String CLAIM_PRODUCT_NOT_ELIGIBLE_FOR_PROMOTION = "claims.submission.errors.CLAIM_PRODUCT_NOT_ELIGIBLE_FOR_PROMOTION";
  public static final String CLAIM_FORM_ELEMENT_NOT_PART_OF_FORM_FOR_PROMOTION = "claims.submission.errors.CLAIM_FORM_ELEMENT_NOT_PART_OF_FORM";

  public static final String HIERARCHY_IMPORT_RECORD_ERROR = "hierarchy.errors.IMPORT_RECORD";

  public static final String PRODUCT_CANNOT_DELETE = "product.errors.CANNOT_DELETE_PRODUCT";
  public static final String PRODUCT_CANNOT_DELETE_HAS_SUBCATEGORIES = "product.errors.PRODUCT_CANNOT_DELETE_HAS_SUBCATEGORIES";
  public static final String PRODUCT_CANNOT_DELETE_HAS_PRODUCTS = "product.errors.PRODUCT_CANNOT_DELETE_HAS_PRODUCTS";
  public static final String PRODUCT_CANT_DEL_AS_PROMO_ASSOCIATION = "product.errors.PRODUCT_CANT_DEL_AS_PROMO_ASSOCIATION";
  public static final String PRODUCT_EXISTS_IN_CATEGORY = "product.errors.UNIQUE_CONSTRAINT";
  public static final String PRODUCT_SUBCATEGORY_ALREADY_EXISTS_AS_PARENT = "product.errors.SUBCATEGORY_ALREADY_EXISTS_AS_PARENT";
  public static final String PRODUCT_CATEGORY_HAS_PARENT = "product.errors.CATEGORY_HAS_PARENT";

  // Promotion
  public static final String PROMOTION_INVALID_SUBMISSION_START_DATE = "promotion.errors.INVALID_SUBMISSION_START_DATE";
  public static final String PROMOTION_INVALID_SUBMISSION_END_DATE = "promotion.errors.INVALID_SUBMISSION_END_DATE";
  public static final String PROMOTION_CHILD_PROMOTION_HAS_NO_SUBMITTER_AUDIENCE = "promotion.errors.CHILD_PROMO_HAS_NO_SUBMITTER_AUDIENCE";
  public static final String PROMOTION_CHILD_PROMOTION_HAS_NO_TEAM_AUDIENCE = "promotion.errors.CHILD_PROMO_HAS_NO_TEAM_AUDIENCE";
  public static final String PROMOTION_EMPTY_PROMOTION_PAYOUT_GROUP = "promotion.errors.CHILD_PROMO_HAS_EMPTY_PROMO_PAYOUT_GROUP";

  // Participant Errors
  public static final String PARTICIPANT_ADDRESS_DELETE_PRIMARY = "participant.address.errors.DELETE_PRIMARY_ERROR";
  public static final String PARTICIPANT_ADDRESS_CHANGE_PRIMARY_COUNTRY = "participant.address.errors.CHANGE_PRIMARY_COUNTRY_ERROR";
  public static final String PARTICIPANT_ADDRESS_UPDATE_PRIMARY_TO_NONPRIMARY = "participant.address.errors.UPDATE_PRIMARY_TO_NONPRIMARY_ERROR";
  public static final String PARTICIPANT_TERMINATION_DATE_ON_NEW_ADD = "participant.errors.TERM_DATE_ON_ADD";
  public static final String PARTICIPANT_DUPLICATE_USER_NAME = "participant.errors.DUPLICATE_USER_NAME";
  public static final String JSON_PARTICIPANT_DUPLICATE_USER_NAME = "participant.errors.JSON_DUPLICATE_USER_NAME";
  public static final String PARTICIPANT_DUPLICATE_SSN = "participant.errors.DUPLICATE_SSN";
  public static final String PARTICIPANT_INVALID_COUNTRY_UPDATE = "participant.errors.INVAL_COUNTRY_UPDATE";
  public static final String PARTICIPANT_NODE_ALREADY_HAS_OWNER = "participant.errors.NODE_ALREADY_HAS_OWNER";
  public static final String PARTICIPANT_COUNTRY_INACTIVE = "participant.errors.COUNTRY_INACTIVE";

  /** Password must be alphanumeric */
  public static final String PASSWORD_MUST_BE_MIXED = "login.forgotpwd.MUST_BE_MIXED";
  /** Password must contain only characters from allowed character types */
  public static final String PASSWORD_USED_DISALLOWED_CHARACTERS = "login.forgotpwd.PASSWORD_USED_DISALLOWED_CHARACTERS";
  /** Password must match the regex rule */
  public static final String PASSW0RD_MUST_MATCH_REGEX = "login.forgotpwd.REGEX_PASSWORD_HINT";
  /** Password must be longer than a set value */
  public static final String PASSWORD_TOO_SHORT = "login.forgotpwd.TO_SHORT";
  /** Old passwords cannot be reused */
  public static final String PASSWORD_CANT_BE_REUSED = "login.forgotpwd.CANT_REUSE";

  public static final String PREVIOUS_PASSWORD_CANT_BE_REUSED = "login.forgotpwd.CANT_REUSE_PREVIOUS_PASSWORDS";

  /** Password variations not allowed */
  public static final String PWD_INVALID = "login.forgotpwd.PWD_INVALID";

  /** If a node type is deleted, but there are nodes associated with the node type * */
  public static final String NODE_TYPE_HAS_NODES_ASSIGNED = "node_type.node.type.NODE_TYPE_HAS_NODES_ASSIGNED";

  /** If they try to create a node type with a name already in use * */
  public static final String NODE_TYPE_NAME_IN_USE = "node_type.node.type.NAME_ALREADY_USED";

  public static final String NODE_TYPE_DELETE_ERROR = "node_type.node.type.DELETE_ERROR";

  /** if a node is not found */
  public static final String NODE_BY_NAME_NOT_FOUND = "node.errors.NAME_NOT_FOUND";

  /**
   * When trying to delete a node pax link, if the pax is not assigned to another node, we can't
   * delete *
   */
  public static final String NODE_PARTICIPANT_ORPHAN = "node.participant.NODE_PARTICIPANT_ORPHAN";

  /** Unique Contrtaint Violation for SystemVariable (OS_PROPERTYSET) */
  public static final String SYSTEM_VARIABLE_DUPLICATE = "admin.sys.var.errors.DUPLICATE";

  /** if a node is not found */
  public static final String PRIMARY_EMAIL_DELETE_ERR = "user.email.errors.CANNOT_DELETE";

  public static final String PRIMARY_USER_NODE_DELETE_ERR = "node.errors.CANNOT_DELETE";

  // Approval errors
  public static final String APPROVAL_ATTEMPTED_APPROVER_IS_NOT_CURRENT_APPROVER = "claims.product.approval.NOT_APPROVER";

  // Shopping errors
  public static final String SINGLE_SIGNON_TO_AWARDSLINQ_ERROR = "home.shopping.errors.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR";
  public static final String SHOPPING_INVALID_PARTICIPANT = "home.shopping.errors.SHOPPING_INVALID_PARTICIPANT";
  public static final String SHOPPING_PROGRAM_ID_SETUP_ERROR = "home.shopping.errors.SHOPPING_PROGRAM_ID_SETUP_ERROR";
  public static final String SHOPPING_PROGRAM_PASSWORD_SETUP_ERROR = "home.shopping.errors.SHOPPING_PROGRAM_PASSWORD_SETUP_ERROR";
  public static final String SHOPPING_REMOTE_URL_SETUP_ERROR = "home.shopping.errors.SHOPPING_REMOTE_URL_SETUP_ERROR";
  public static final String SHOPPING_PROXY_SETUP_ERROR = "home.shopping.errors.SHOPPING_PROXY_SETUP_ERROR";
  public static final String SHOPPING_PROXY_PORT_SETUP_ERROR = "home.shopping.errors.SHOPPING_PROXY_PORT_SETUP_ERROR";
  public static final String SHOPPING_POST_LOGIN_URL_SETUP_ERROR = "home.shopping.errors.SHOPPING_POST_LOGIN_URL_SETUP_ERROR";
  // External Supplier errors
  public static final String EXTERNAL_SUPPLIER_CATALOG_URL_ERROR = "home.shopping.errors.EXTERNAL_SUPPLIER_CATALOG_URL_ERROR";

  // Goalquest Errors
  public static final String REGISTRATION_CODE_INVALID_ERROR = "login.errors.REGI_CODE_INVALID";
  public static final String REGISTRATION_CODE_INVALID_FORMAT = "login.errors.REGI_FORMAT";

  // PayoutTranasctionDetail errors
  public static final String JOURNAL_NOT_FOUND = "participant.payout.transaction.detail.errors.JOURNAL_NOT_FOUND";
  public static final String PARTICIPANT_NOT_FOUND = "participant.payout.transaction.detail.errors.PARTICIPANT_NOT_FOUND";

  // CM Service errors
  public static final String CM_SERVICE_SAVE_ERROR = "system.cmservice.errors.SAVE_ERR";
  public static final String CM_SERVICE_FETCH_ERROR = "system.cmservice.errors.FETCH_ERR";
  public static final String CM_SERVICE_GENERATED_ASSET_LENGTH_ERROR = "system.cmservice.errors.ASSET_LENGTH_ERR";
  public static final String CM_SERVICE_GENERATED_KEY_LENGTH_ERROR = "system.cmservice.errors.KEY_LENGTH_ERR";
  public static final String CM_SERVICE_EMPTY_ASSET_PREFIX_ERROR = "system.cmservice.errors.ASSET_PREFIX_ERR";
  public static final String CM_SERVICE_DELETE_ERROR = "system.cmservice.errors.DELETE_ERR";

  // CM
  public static final String SYSTEM_ERROR_ROOT = "system.errors.";
  public static final String SYSTEM_ERRORS_REQUIRED = SYSTEM_ERROR_ROOT + "REQUIRED";
  public static final String SYSTEM_ERRORS_EMPTY = SYSTEM_ERROR_ROOT + "EMPTY";
  public static final String SYSTEM_ERRORS_SYSTEM_EXCEPTION = SYSTEM_ERROR_ROOT + "SYSTEM_EXCEPTION";
  public static final String SYSTEM_ERRORS_INTEGER = SYSTEM_ERROR_ROOT + "INTEGER";
  public static final String SYSTEM_ERRORS_NON_NEGATIVE_INTEGER = SYSTEM_ERROR_ROOT + "NON_NEGATIVE_INTEGER";
  public static final String SYSTEM_ERRORS_NON_ZERO_POSITIVE_INTEGER = SYSTEM_ERROR_ROOT + "NON_ZERO_POSITIVE_INTEGER";
  public static final String SYSTEM_ERRORS_DATE = SYSTEM_ERROR_ROOT + "DATE";
  public static final String SYSTEM_ERRORS_TIME = SYSTEM_ERROR_ROOT + "TIME";
  public static final String SYSTEM_ERRORS_DATE_RANGE = SYSTEM_ERROR_ROOT + "DATE_RANGE";
  public static final String SYSTEM_ERRORS_DATE_RANGE_PROMOTION = SYSTEM_ERROR_ROOT + "DATE_RANGE_PROMOTION";
  public static final String SYSTEM_ERRORS_DATE_RANGE_GOALSELECTION = SYSTEM_ERROR_ROOT + "DATE_RANGE_GOALSELECTION";
  public static final String SYSTEM_ERRORS_DATE_RANGE_TILEDISPLAY = SYSTEM_ERROR_ROOT + "DATE_RANGE_TILEDISPLAY";
  public static final String SYSTEM_ERRORS_DATE_RANGE_TILEDISPLAY_START_TO_DATE = SYSTEM_ERROR_ROOT + "DATE_RANGE_TILEDISPLAY_START_TO_DATE";
  public static final String SYSTEM_ERRORS_TD_TILESTART_BEFORE_PROMO_START = SYSTEM_ERROR_ROOT + "TD_TILESTART_BEFORE_PROMO_START";
  public static final String SYSTEM_ERRORS_DATE_RANGE_TILEDISPLAY_END_TO_DATE = SYSTEM_ERROR_ROOT + "DATE_RANGE_TILEDISPLAY_END_TO_DATE";
  public static final String SYSTEM_ERRORS_END_BEFORE_START_DATE = SYSTEM_ERROR_ROOT + "END_BEFORE_START_DATE";
  public static final String SYSTEM_ERRORS_END_BEFORE_TO_DATE = SYSTEM_ERROR_ROOT + "END_BEFORE_TO_DATE";
  public static final String SYSTEM_ERRORS_START_BEFORE_TO_DATE = SYSTEM_ERROR_ROOT + "START_BEFORE_TO_DATE";
  public static final String SYSTEM_ERRORS_END_BEFORE_TO_DATE_PROMOTION = SYSTEM_ERROR_ROOT + "END_BEFORE_TO_DATE_PROMOTION";
  public static final String SYSTEM_ERRORS_END_BEFORE_TO_DATE_GOALSELECTION = SYSTEM_ERROR_ROOT + "END_BEFORE_TO_DATE_GOALSELECTION";
  public static final String SYSTEM_ERRORS_END_BEFORE_TO_DATE_TILEDISPLAY = SYSTEM_ERROR_ROOT + "END_BEFORE_TO_DATE_TILEDISPLAY";
  public static final String SYSTEM_ERRORS_INVALID_CALL = SYSTEM_ERROR_ROOT + "INVALID_STATUS";
  public static final String SYSTEM_ERROR_RANGE = SYSTEM_ERROR_ROOT + "RANGE";
  public static final String SYSTEM_ERROR_MAXCOUNT = SYSTEM_ERROR_ROOT + "MAXCOUNT";
  public static final String SYSTEM_ERROR_MINCOUNT = SYSTEM_ERROR_ROOT + "MINCOUNT";
  public static final String SYSTEM_ERRORS_PHONE_FORMAT = SYSTEM_ERROR_ROOT + "PHONE_FORMAT";
  public static final String SYSTEM_ERRORS_INVALID = SYSTEM_ERROR_ROOT + "INVALID";
  public static final String SYSTEM_ERRORS_MAXLENGTH = SYSTEM_ERROR_ROOT + "MAXLENGTH";
  public static final String DATE_OLDER_THAN_START_DATE = "promotion.basics.DATE_OLDER_THAN_START_DATE";

  /**
   * This is for Import deposit Adjustments min/max and fixed amount check up
   */
  public static final String PROMOTION_AWARD_AMOUNT_INVALID = SYSTEM_ERROR_ROOT + "PROMOTION_AWARD_AMOUNT_INVALID";
  public static final String PROMOTION_AWARD_AMOUNT_NOT_IN_RANGE = SYSTEM_ERROR_ROOT + "PROMOTION_AWARD_AMOUNT_NOT_IN_RANGE";

  public static final String AWARDBANQ_NO_ADDRESS = "participant.errors.ADDRESS_MISSING";
  public static final String AWARDBANQ_NO_CAMPAIGN = "participant.errors.CAMPAIGN_MISSING";
  public static final String AWARDBANQ_NOT_ENROLLED = "participant.errors.NOT_ENROLLED";
  public static final String AWARDBANQ_ERROR = "participant.errors.GENERAL_AWARDBANQ";
  public static final String AWARDBANQ_INVALID_CERT = "hometile.onTheSpotCard.CERTIFICATE_NUMBER_INVALID";
  public static final String AWARDBANQ_CERT_ALREADY_USED = "hometile.onTheSpotCard.CERTIFICATE_BALANCE_IS_ZERO";
  public static final String AWARDBANQ_CERT_EXPIRED = "hometile.onTheSpotCard.STATUS_EXPIRED_OR_LOST";
  public static final String AWARDBANQ_CERT_ERROR = "hometile.onTheSpotCard.OTHER_CERT_ERROR";
  public static final String AWARDBANQ_GIFTCODE_ERROR = "promotion.errors.GIFTCODE_ERROR";
  public static final String AWARDBANQ_GIFTCODE_ERROR_MESSAGE = "promotion.errors.GIFTCODE_MESSAGE_CODE_";

  // Quiz
  public static final String QUIZ_DUPLICATE_ERR = "quiz.errors.QUIZ_DUPLICATE_ERR";
  public static final String QUIZ_DELETE_STATUS_ERR = "quiz.errors.DELETE_STATUS_ERR";
  public static final String QUIZ_DELETE_LINKED_ERR = "quiz.errors.DELETE_LINKED_ERR";
  public static final String NO_WINNER_FOR_REPLACEMENT_ERROR = "quiz.errors.NO_WINNER_FOR_REPLACEMENT_ERROR";

  // Survey
  public static final String SURVEY_DUPLICATE_ERR = "survey.errors.SURVEY_DUPLICATE_ERR";
  public static final String SURVEY_DELETE_STATUS_ERR = "survey.errors.DELETE_STATUS_ERR";
  public static final String SURVEY_DELETE_LINKED_ERR = "survey.errors.DELETE_LINKED_ERR";

  // General Error messages
  public static final String DEPENDENCY_REQUIRED = "system.errors.DEPENDENCY_REQUIRED";

  // Spotlight Errors
  // Merch Level Service Errors
  public static final String SPOTLIGHT_MERCHLEVEL_INVALIDGIFTCODE_ERROR = "spotlight.merchlevel.errors.MERCHLEVEL_INVALIDGIFTCODE_ERROR";
  public static final String SPOTLIGHT_MERCHLEVEL_DATARETRIEVER_ERROR = "spotlight.merchlevel.errors.MERCHLEVEL_DATARETRIEVER_ERROR";

  // SweepStake Error Messages(BugFix 15761)
  public static final String NO_GIVER_FOR_REPLACEMENT_ERROR = "recognition.errors.NO_GIVER_FOR_REPLACEMENT";
  public static final String NO_RECEIVER_FOR_REPLACEMENT_ERROR = "recognition.errors.NO_RECEIVER_FOR_REPLACEMENT";
  public static final String NO_GIVER_RECEIVER_FOR_REPLACEMENT_ERROR = "recognition.errors.NO_GIVER_RECEIVER_FOR_REPLACEMENT";

  // Chatter errors
  public static final String CHATTER_ACCESS_ERROR = "recognition.errors.CHATTER_ACCESS_ERROR";

  // Recognition OM order errors
  public static final String RECOGNITION_OM_ORDER_DEFAULT_ERROR = "recognition.om.errors.DEFAULT_ERROR";
  public static final String RECOGNITION_OM_PAX_SERVICE_ERROR = "recognition.om.errors.OM_PAX_SERVICE_ERROR";
  public static final String RECOGNITION_OM_INVALID_ORDER_ERROR = "recognition.om.errors.OM_INVALID_ORDER_ERROR";
  public static final String RECOGNITION_OM_ORDER_PO_BOX_ERROR = "recognition.om.errors.PO_BOX_ERROR";
  public static final String RECOGNITION_OM_POSTAL_CODE_ERROR = "recognition.om.errors.POSTAL_CODE_ERROR";
  public static final String RECOGNITION_OM_EMAIL_ERROR = "recognition.om.errors.EMAIL_ERROR";
  public static final String RECOGNITION_OM_STATE_ERROR = "recognition.om.errors.STATE_ERROR";
  public static final String RECOGNITION_OM_FAST_SHIP_ERROR = "recognition.om.errors.FAST_SHIP_ERROR";

  // General OM errors
  public static final String OM_NOT_AVAILABLE = "promotion.goalquest.replacegiftcode.OM_NOT_AVAILABLE";
  public static final String OM_NO_REPLACEMENT_GIFTCODE = "promotion.goalquest.replacegiftcode.NO_NEW_GIFT_CODE";

  // Filter Setup errors
  public static final String DUPLICATE_TILE_PRIORITY1 = "filtersetup.setup.DUPLICATE_TILE_NAME_IN_PRIORITY_ONE";
  public static final String DUPLICATE_TILE_PRIORITY2 = "filtersetup.setup.DUPLICATE_TILE_NAME_IN_PRIORITY_TWO";
  public static final String DUPLICATE_TILE_PRIORITY = "filtersetup.setup.DUPLICATE_TILE_NAME_PRIORITY";

  // TD Stack ranking errors
  public static final String ROUND_APPROVED_RANKING_EXISTS = "promotion.payout.errors.ROUND_APPROVED_RANKING_EXISTS";
  public static final String PROMOTION_APPROVED_RANKING_EXISTS = "promotion.payout.errors.PROMOTION_APPROVED_RANKING_EXISTS";

  // Instant Poll errors
  public static final String INSTANT_POLL_DUPLICATE_ERR = "survey.errors.INSTANT_POLL_DUPLICATE_ERR";

  // SSI Errors
  public static final String SSI_CONTEST_DELETE_STATUS_ERR = "ssi_contest.generalInfo.SSI_CONTEST_DELETE_STATUS_ERR";
  public static final String SSI_CONTEST_DELETE_ERR = "ssi_contest.generalInfo.SSI_CONTEST_DELETE_ERR";
  public static final String SSI_CONTEST_COPY_ERR = "ssi_contest.generalInfo.SSI_CONTEST_COPY_ERR";
  public static final String SSI_CONTEST_APPPROVAL_DENY_ERROR = "ssi_contest.approvals.summary.SSI_CONTEST_APPROVAL_DENY_ERROR";
  public static final String INVALID_USER_FOR_CONTEST_APPROVER_ERROR = "ssi_contest.approvals.summary.INVALID_USER_FOR_CONTEST_APPROVER_ERROR";
  public static final String SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERROR = "ssi_contest.approvals.summary.SSI_CONTEST_UNAVAILABLE_FOR_APPROVAL_ERR";

  public static final String SSI_CONTEST_PROGRESS_DATA_ERR = "ssi_contest.generalInfo.CONTEST_PROGRESS_ERR";
  public static final String SSI_CONTEST_PAYOUTS_DATA_ERR = "ssi_contest.generalInfo.CONTEST_PAYOUTS_DATA_ERR";

  public static final String SSI_CONTEST_GOAL_PERC_ERR = "ssi_contest.creator.CONTEST_GOAL_PERC_ERR";
  public static final String SSI_CONTEST_UPD_STACKRANK_ERR = "ssi_contest.participant.CONTEST_UPD_STACKRANK_ERR";
  public static final String SSI_CONTEST_APPROVE_PAYOUT_ERR = "ssi_contest.participant.CONTEST_APPROVE_PAYOUT_ERR";
  public static final String SSI_CONTEST_PROGRESS_LOAD_MAIL_ERR = "ssi_contest.participant.CONTEST_PROGRESS_LOAD_MAIL_ERR";
  public static final String SSI_PROMOTION_CONTEST_REMOVED_ERR = "ssi_contest.generalInfo.REMOVED_CONTEST";
  public static final String SSI_CONTEST_ARCHIVAL_PROCESS_ERR = "ssi_contest.approvals.summary.CONTEST_ARCHIVAL_ERR";
  public static final String SSI_CONTEST_CLAIM_APPROVAL_ERR = "ssi_contest.claims.SSI_CONTEST_CLAIM_APPROVAL_ERR";
  public static final String SSI_CONTEST_UPD_CLAIM_ERR = "ssi_contest.claims.SSI_CONTEST_CLAIM_UPDATE_ERR";
  public static final String SSI_ADMIN_SEARCH_CREATOR_REQUIRED = "ssi_contest.administration.SEARCH_CREATOR_REQUIRED";
  public static final String SSI_ADMIN_SELECT_CREATOR_REQUIRED = "ssi_contest.administration.SELECT_CREATOR_REQUIRED";

  // PURL errors
  public static final String PURL_COMMENT_LENGTH_ERROR = "purl.contributor.MAX_MSG_LENGTH";

  // Group errors
  public static final String GROUP_SIZE_ERROR = "system.errors.GROUP_SIZE_ERROR";
  public static final String GROUP_NAME_REQUIRED = "system.errors.GROUP_NAME_REQUIRED";
  public static final String GROUP_ALREADY_EXISTS = "system.errors.GROUP_ALREADY_EXISTS";
  // Security Profile Page error
  public static final String INVALID_PASSWORD_ENTERED = "login.forgotpwd.CURRENT_PWD_INVALID";
  public static final String PHONE_CANT_REUSE = "login.forgotpwd.PHONE_CANT_REUSE";
  public static final String EMAIL_CANT_REUSE = "login.forgotpwd.EMAIL_CANT_REUSE";

  // Client customization for WIP #43735 starts
  public static final String COKE_CLAIM_UNAVAIALBLE_ERR = "coke.cash.recognition.UNAVAILABLE";
  public static final String COKE_CLAIM_AWARD_REDEEM_UNAUTHORIZED_ERR = "coke.cash.recognition.NOT_AUTHORIZED";
  public static final String COKE_CLAIM_AWARD_ALREADY_REDEEMED_ERR = "coke.cash.recognition.ALREADY_REDEEMED";
  public static final String COKE_CLAIM_AWARD_CASH_NOT_ALLOWED_ERR = "coke.cash.recognition.CASH_NOT_ALLOWED";
  public static final String COKE_CLAIM_AWARD_UNKNOWN_AWARD_ERR = "coke.cash.recognition.UNKNOWN_AWARD";
  // Client customization for WIP #43735 ends
  // public static final String REISSUE_SEND_PWD_LIMIT_EXCEEDED =
  // "reissue.send.password.LIMIT_EXCEEDED";
}
