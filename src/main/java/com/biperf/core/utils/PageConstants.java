
package com.biperf.core.utils;

public interface PageConstants
{
  // redirect url related to login page
  public static final String LOGIN_PAGE_URL = "/login.do";

  // redirect url related to g5 home page
  public static final String HOME_PAGE_G5_REDIRECT_URL = "/homePage.do";

  // redirect urls related to leaderboard
  public static final String LEADERBOARD_DETAIL_PAGE_REDIRECT_URL = "/leaderBoardsDetailPage.do?method=getUserRole";
  public static final String LEADERBOARD_LIST_PAGE_REDIRECT_URL = "/leaderBoardsList.do";
  public static final String LEADERBOARD_EDIT_ACTION = "/leaderBoardEditAction.do?method=prepareEdit";
  public static final String LEADERBOARD_COPY_ACTION = "/leaderBoardCopyAction.do?method=copyLeaderBoard";

  // redirect url related to approvals
  public static final String APPROVALS_RECOG_LIST = "/claim/approvalsRecognitionList.do";
  public static final String APPROVALS_NOMI_LIST = "/claim/approvalsNominationList.do";
  public static final String APPROVALS_CLAIMS_LIST = "/claim/approvalsProductClaimList.do";

  // redirect url related to public profile
  public static final String PUBLIC_PROFILE_VIEW = "/publicProfileView.do?method=display";

  // redirect url related to public profile
  public static final String PUBLIC_PROFILE_VIEW_MORE_PAX = "/publicProfileView.do?method=display";

  // url related to participant profile page
  public static final String PRIVATE_PROFILE_VIEW = "/participantProfilePage.do";
  public static final String PARTICIPANT_PREFERENCES = "/participantProfilePage.do#tab/Preferences";
  public static final String PARTICIPANT_STATEMENTS = "/participantProfilePage.do#tab/Statement";
  public static final String PARTICIPANT_SECURITY = "/participantProfilePage.do#profile/Security";
  public static final String PARTICIPANT_PROXY_EDIT = "/profilePageProxiesTabEdit.do?method=prepareEdit";
  public static final String PARTICIPANT_PROXY_REMOVE = "/profilePageRemoveProxy.do?method=delete";
  public static final String PARTICIPANT_PROXY_NEW = "profilePageProxiesTabNew.do?method=prepareNew";
  public static final String DELEGATE_LAUNCH = "/actAsDelegate.do";

  // redirect url related to first time login page
  public static final String FIRST_TIME_LOGIN_PAGE = "/loginPageFirstTime.do?method=display";

  // redirect url related to change password page on login success
  public static final String CHANGE_PWD_URL = "/changePasswordViewNew.do?method=display";

  // url related to messageDetail page.
  public static final String MESSAGE_DETAIL_URL = "/messageDetail.do";

  // Urls related to alert and messages tab in profile page.
  public static final String ALERT_DETAIL_GOALQUEST_SELECT_YOUR_GOAL = "/goalquest/selectYourGoalDisplay.do";
  public static final String ALERT_DETAIL_CHALLENGEPOINT_SELECT_YOUR_CHALLANGE = "/challengepoint/selectYourChallengePointDisplay.do";
  public static final String ALERT_DETAIL_RECOGNITION_APPROVAL_LIST = "/claim/approvalsRecognitionList.do";
  public static final String ALERT_DETAIL_CLAIM_SUBMISSION = "/claim/claimSubmissionInitial.do";
  public static final String ALERT_DETAIL_NOMINATION_APPROVAL_LIST = "/claim/nominationsApprovalPage.do?method=display";
  public static final String ALERT_NOMINATION_APPROVAL_LIST = "/claim/nominationsApprovalPromoList.do?method=display";
  public static final String ALERT_DETAIL_CLAIM_PRODUCT_APPROVE_LIST = "/claim/approvalsProductClaimList.do";
  public static final String ALERT_DETAIL_QUIZ_CLAIM_SUBMISSION = "/quiz/quizPage.do";
  public static final String ALERT_DETAIL_PURL_MAINTENANCE_LIST = "/purl/purlMaintenanceList.do";
  public static final String ALERT_DETAIL_PURL_CONTRIBUTION_LIST = "/purl/purlContributionList.do";
  public static final String ALERT_DETAIL_SA_CONTRIBUTION_LIST = "/purl/saContributionList.do"; // NEW-SA
  public static final String ALERT_DETAIL_PURL_RECIPIENT = "/purl/purlRecipient.do?method=display";
  public static final String ALERT_DETAIL_PURL_CONTRIBUTION_SINGLE = "/purl/purlTNC.do?method=display";
  public static final String ALERT_DETAIL_SUBMIT_RECOGNITION = "/recognitionWizard/sendRecognitionBudget.do?method=getRecognitionForm&isBudgetAlert=true";
  public static final String ALERT_DETAIL_FILE_DOWNLOAD = "/reports/file/download.do";
  public static final String ALERT_DETAIL_PLATEAU_AWARD_REMINDERS = "/plateauAwardsReminder.do";
  public static final String ALERT_DETAIL_MANAGER_REMINDER_OF_AWARD_FILE_GENERATOR = "/awardsFileGeneratorManagerReminder.do";
  public static final String ALERT_SURVEY = "/goalquest/takeGQSurvey.do?method=display";
  public static final String AWARD_GENERATOR_MANAGER_DISMISS_REMINDER = "/admin/awardsFileGeneratorManagerReminder.do?method=dismissAlert";
  public static final String ALERT_CELEBRATION_MANAGER_MESSAGE = "/celebration/managerMessageCollect.do?method=display";

  // Urls related to quiz page.
  public static final String QUIZ_PAGE_CHECK_QUIZ_ELIGIBILITY = "/quiz/checkQuizEligibilityAction.do?method=checkQuizEligibility";
  public static final String QUIZ_CERTIFICATE_URL = "/claim/displayCertificate.do?method=showCertificateQuizDetail";
  public static final String QUIZ_TAKE_PAGE_URL = "/quiz/quizPageTake.do";
  public static final String QUIZ_LIST_PAGE_URL = "/quiz/quizPage.do";

  // url related to public recognition detail page
  public static final String CLAIM_DETAIL_URL = "/claim/claimDetail.do";

  // url related to purl celebration page
  public static final String PURL_CELEBRATION_PAGE_URL = "/purl/purlCelebrateRecognitionPurlsActivity.do?method=fetchRecognitionPurls";

  // url related to Forum page
  public static final String FORUM_PAGE_URL = "/forum/forumDiscussionListMaintainDisplay.do?method=prepareUpdate";

  public static final String FORUM_DISCUSSIONS_URL = "/forum/forumPageDiscussions.do";

  public static final String FORUM_START_DISCUSSION = "/forum/forumDiscussionStart.do?method=createDiscussion";

  public static final String FORUM_DISCUSSION_AJAX_URL = "/forum/forumDiscussionsDisplayTableAjaxResponse.do";

  // url related to product claim preview page
  public static final String PRODUCT_CLAIM_PREVIEW_PAGE_URL = "/claim/previewClaimSubmissionRedirect.do";

  // url related to participant overview page
  public static final String PARTICIPANT_OVERVIEW = "/participant/participantDisplay.do";

  // url related to shop international page
  public static final String SHOPPING_EXTERNAL = "externalSupplier.do?method=displayExternal";

  // url related to self enrollment module. Note: few url's are used in selfEnrollment.jsp &
  // selfEnrollmentCodes.jsp
  public static final String SELF_ENROLLMENT_DISPLAY = "/admin/selfEnrollment.do?method=display";
  public static final String SELF_ENROLLMENT_GENERATE_CODES = "/admin/generateEnrollmentCodes.do?method=generateCodes";
  public static final String SELF_ENROLLMENT_VIEW_CODES = "/admin/viewEnrollmentCodes.do?method=viewCodes";
  public static final String SELF_ENROLLMENT_PAX_REG_DISPLAY = "/selfEnrollmentPaxRegistration.do?method=display";

  // throwdown URL's
  public static final String THROWDOWN_MATCH_DETAIL = "/throwdown/matchDetail.do?method=detail";
  public static final String THROWDOWN_RANKING_DETAIL = "/throwdown/rankingsDetail.do?method=detail";
  public static final String THROWDOWN_PARTICIPANT_STATS = "/participant/throwdownParticipantStatistic.do?method=display";
  public static final String ALERT_SURVEY_LIST = "/surveyPage.do";
  public static final String TAKE_SURVEY = "/takeSurvey.do?method=display";

  // ssi url's
  public static final String SSI_APPROVAL_SUMMARY = "/ssi/loadSSIContestApprovalSummary.do?method=display";
  public static final String SSI_ISSUANCE_APPROVAL_SUMMARY = "/ssi/loadSSIContestIssuanceApprovalSummary.do?method=display";
  public static final String SSI_CREATOR_DETAIL_URL = "/ssi/creatorContestList.do?method=display";
  public static final String SSI_MGR_DETAIL_URL = "/ssi/managerContestList.do?method=display";
  public static final String SSI_PAX_DETAIL_URL = "/ssi/participantContestList.do?method=display";
  public static final String SSI_CREATOR_DETAIL_ATN_URL = "/ssi/displayContestSummaryAwardThemNow.do?method=load";

  // redirect urls for email
  public static final String SSI_CREATOR_DETAIL_REDIRECT_URL = "/ssi/creatorContestListRedirect.do?method=display";
  public static final String SSI_MGR_DETAIL_REDIRECT_URL = "/ssi/managerContestListRedirect.do?method=display";
  public static final String SSI_PAX_DETAIL_REDIRECT_URL = "/ssi/participantContestListRedirect.do?method=display";

  public static final String SSI_MANAGE_OBJECTIVES = "/ssi/manageContestPayoutObjectives.do";
  public static final String SSI_MANAGE_DO_THIS_GET_THAT = "/ssi/manageContestPayoutDoThisGetThat.do";
  public static final String SSI_MANAGE_STEP_IT_UP = "/ssi/manageContestPayoutStepItUp.do";
  public static final String SSI_MANAGE_STACK_RANK = "/ssi/manageContestPayoutStackRank.do";

  public static final String SSI_NOTIFIY_APPROVERS = "/ssi/createGeneralInfo.do?method=notifyApprovers";

  public static final String SSI_CREATOR_LIST_PAGE = "/ssi/creatorContestList.do?method=display&id=";
  public static final String SSI_MANAGER_LIST_PAGE = "/ssi/managerContestList.do?method=display&id=";
  public static final String SSI_PARTICIPANT_LIST_PAGE = "/ssi/participantContestList.do?method=display&id=";

  public static final String SSI_PARTICIPANT_SUBMIT_CLAIM = "/ssi/participantSubmitClaim.do?method=displayClaimForm";

  public static final String SSI_CLAIM_APPROVAL_LIST = "/ssi/approveContestClaims.do?method=display";
  public static final String SSI_CLAIM_DETAIL_PAGE = "/ssi/approveClaimDetail.do?method=displayClaimDetail";

  public static final String SSI_VIEW_CLAIM_DETAILS = "/ssi/contestClaimDetail.do?method=viewClaim";

  public static final String NOMINATOR_REQUEST_MORE_INFO = "/claim/nominationsMoreInfoPage.do?method=display";

  public static final String NOMINATOR_MY_WINNER = "/nomination/viewNominationPastWinnersList.do?method=nominationWinnerDetailsPage";
  public static final String NOMINATION_PAX_SAVED_SUBMISSIONS = "/nomination/nomInProgress.do?method=display";

  public static final String FIRST_TIME_UA_LOGIN_PAGE = "/underArmourFirstTime.do";
  
  public static final String TRAVEL_PAGE_URL = "submitRewardOffering.do?method=displayExperience";

  public static final String INACTIVE_SHOPPING_URL = "/shopping.do?method=displayInternal";
  public static final String MULTISUPPLIER_SHOPPING_URL = "/multipleSuppliers.do";

  // recognition advisor
  public static final String RA_SEND_RECOG_LONG_FORM = "/ra/sendRecognitionFromAlerts.action";
  public static final String RA_DETAILS_PAGE = "/raDetails.do";

  // Client customization for WIP 58122
  public static final String TCCC_CLAIM_AWARD_DISPLAY       = "/claimAwardAction.do?method=display";
  public static final String TCCC_CLAIM_AWARD_NOMINATION_DISPLAY       = "/claimAwardNominationAction.do?method=display";

}
