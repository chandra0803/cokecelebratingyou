/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/constants/ActionConstants.java,v $
 */

package com.biperf.core.ui.constants;

import com.biperf.core.service.util.ServiceErrorMessageKeys;

/**
 * Interface that defined all the Action level constants.
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
 * <td>Mar 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ActionConstants
{
  /** FORWARD */
  public static final String DISPLAY_FORWARD = "display";
  public static final String EDIT_FORWARD = "edit";
  public static final String CREATE_FORWARD = "create";
  public static final String UPDATE_FORWARD = "update";
  public static final String LOGIN_FORWARD = "login";
  public static final String SEARCH_FORWARD = "search";
  public static final String DETAILS_FORWARD = "details";
  public static final String CANCEL_FORWARD = "cancel";
  public static final String DELETE_FORWARD = "delete";
  public static final String REVIEW_FORWARD = "review";
  public static final String COPY_FORWARD = "copy";
  public static final String PRINTER_FRIENDLY_FORWARD = "printerFriendly";
  public static final String VALIDATED_FORWARD = "validated";

  public static final String DEFAULT_PAGE_FORWARD = "defaultPage";
  /** SUCCESS_FORWARD */
  public static final String SUCCESS_FORWARD = "success";
  public static final String SUCCESS_TEST_PROCESS_FORWARD = "successTestProcess";
  public static final String SUCCESS_SEARCH = "success_search";
  public static final String SUCCESS_UPDATE = "success_update";
  public static final String SUCCESS_CREATE = "success_create";
  public static final String SUCCESS_DELETE = "success_delete";
  public static final String SUCCESS_DETAILS = "success_details";
  public static final String SUCCESS_COPY = "success_copy";
  public static final String SUCCESS_TO_HOMEPAGE = "success_to_homepage";
  public static final String SENT_WELCOME_EMAIL = "sentWelcomeEmail";

  /** FAIL_FORWARD */
  public static final String FAIL_FORWARD = "failure";
  public static final String FAIL_UPDATE = "failure_update";
  public static final String FAIL_CREATE = "failure_create";
  public static final String FAIL_DELETE = "failure_delete";
  public static final String FAIL_SEARCH = "failure_search";
  public static final String FAIL_COPY = "failure_copy";
  public static final String FAIL_ERRORS_ONLY = "failure_errors_only";

  /** OTHERS */
  public static final String CANCEL_TO_HOMEPAGE = "cancel_to_homepage";
  public static final String CANCEL_TO_LOGIN = "cancel_to_login";
  public static final String ERROR_MESSAGE_PROPERTY = "errorMessage";
  public static final String ERROR_SERVICE_EXCEPTION = "serviceErrorException";
  public static final String LEAVE_SITE = "leave_site";
  public static final String CANCEL_TO_TC_VIEW = "back_to_view";
  public static final String FORWARD_TO_CHANGE_PASSWORD = "change_password";
  public static final String WIZARD_BACK_TO_AWARDS = "backToAwards";
  public static final String WIZARD_BACK_TO_SWEEPSTAKES = "backToSweepStakes";
  public static final String WIZARD_BACK_TO_APPROVAL = "backToApproval";

  /** STRUTS TOKEN PROCESSING */
  public static final String ERROR_TOKEN_FAILURE = "tokenFailure";
  public static final String ERROR_TOKEN_EXCEPTION = ServiceErrorMessageKeys.SYSTEM_ERROR_ROOT + "TOKEN_EXCEPTION";

  /** WIZARD_FORWARD CONSTANTS */
  public static final String WIZARD_SAVE_AND_EXIT_FORWARD = "saveAndExit";
  public static final String WIZARD_SAVE_AND_CONTINUE_FORWARD = "saveAndContinue";
  public static final String WIZARD_SAVE_AND_CONTINUE_DIY_QUIZ_FORWARD = "saveAndContinueDIYQuiz";
  public static final String WIZARD_FAIL_FORWARD = "failure";
  public static final String WIZARD_CANCEL_FORWARD = "cancelWizard";
  public static final String WIZARD_SUCCESS_FORWARD = "success";
  public static final String WIZARD_NEXT_FORWARD = "next";
  public static final String WIZARD_BACK_FORWARD = "back";
  public static final String WIZARD_BACK_DIY_QUIZ_FORWARD = "backDIYQuiz";
  public static final String WIZARD_CONTINUE_FORWARD = "continue";
  public static final String WIZARD_SAVE_AND_EXIT_ATTRIBUTE = "saveAndExit";
  public static final String WIZARD_SAVE_AND_CONTINUE_ATTRIBUTE = "saveAndContinue";

  /** Saves Warnings and FYIs that needs to be displayed even after doing sendRedirect **/
  public static final String SESSION_WARNINGS = "forwardWarningMessages";

  /** PUBLIC PROFILE FORWARD */
  public static final String FULL_VIEW = "fullView";
  public static final String SHEET_VIEW = "sheetView";
  public static final String THROWDOWN_VIEW = "throwdownView";
  public static final String ADMIN_HOME = "adminHome";
  public static final String DECLINE = "declineTNC";

  /** For removing change security Q & A drop down for first time user in applications. Dummy purpose */
  public static final String NOT_APPLICABLE = "NA";
  public static final String CODE_FOR_SECRETQUESTION = "ice";

  /* For Video Transcoder */
  public static final String REQUEST_ID = "Request id :";
  /* GDPR Compliance */
  public static final String PAX_FULL_VIEW = "paxFullView";

  public static final String XLSX = "xlsx";
  public static final String XLS = "xls";
  public static final String FILEPATH = "filePath";
  public static final String EXCEL_DOWNLOAD_ACTION = "/resourceCenterAction.do";
  public static final String SYS_APPDATADIR = "appdatadir";
}
