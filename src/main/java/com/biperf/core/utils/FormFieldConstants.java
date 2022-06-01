
package com.biperf.core.utils;

/*
 * These constants are actually form fields
 * Naming convention is Form_Name_Field
 * These constants are used for json response to highlight the form field in the browser with error mesg
 */
public interface FormFieldConstants
{

  // ContactUsForm field constants
  public static final String CONTACT_US_FORM_COMMENTS = "comments";
  public static final String CONTACT_US_FORM_SUBJECT = "subject";

  // LeaderboardForm field constants
  public static final String LEADERBOARD_FORM_LEADERBOARD_NAME = "leaderBoardName";
  public static final String LEADERBOARD_FORM_LEADERBOARD_ACTIVITY_TITLE = "activityTitle";
  public static final String LEADERBOARD_FORM_LEADERBOARD_RULES_TEXT = "leaderBoardRulesText";

  // ForgotPasswordForm field constants
  public static final String FORGOT_PWD_FORM_USERNAME = "userName";
  public static final String FORGOT_PWD_FORM_SECRET_ANSWER = "secretAnswer";

  // ChangePasswordForm field constants
  public static final String CHANGE_PWD_FORM_NEW_PWD = "newPassword";
  public static final String CHANGE_PWD_FORM_CONFIRM_PWD = "confirmNewPassword";

  // PersonalInfoConstants
  public static final String PERSONAL_INFO_FORM_PROFILE_IMAGE = "profileImage";
  public static final String PERSONAL_INFO_FORM_UPLOAD_LINK = "personalInformationUploadImageLink";
  public static final String PERSONAL_INFO_FORM_UPLOADED_IMAGE = "personalInformationAvatar";

  // ParticipantSearchCriteria field constants
  public static final String PAX_SEARCH_CRITERIA_FIRSTNAME = "firstName";
  public static final String PAX_SEARCH_CRITERIA_LASTNAME = "lastName";
  public static final String PAX_SEARCH_CRITERIA_USERNAME = "userName";
  public static final String PAX_SEARCH_CRITERIA_COUNTRY = "country";
  public static final String PAX_SEARCH_CRITERIA_DEPT = "department";
  public static final String PAX_SEARCH_CRITERIA_JOB_POSITION = "jobPosition";
  public static final String PAX_SEARCH_CRITERIA_JOB_TITLE = "jobTitle";
  public static final String PAX_SEARCH_CRITERIA_LOCATION = "location";
  public static final String PAX_SEARCH_CRITERIA_GROUP_NAME = "paxGroup";
  // nodeId;

  // ParticipantForm field condtants
  public static final String PARTICIPANT_FORM_NUMBER_FORMAT = "numberFormat";
  public static final String PARTICIPANT_FORM_PHONE_NUMBER = "textPhoneNbr";
  public static final String PARTICIPANT_FORM_TERMS_AND_CONDITIONS = "acceptTermsOnTextMessages";

  // GlobalForm field constants
  // These constants field values are same for many form beans
  public static final String GLOBAL_FORM_START_DATE = "startDate";
  public static final String GLOBAL_FORM_END_DATE = "endDate";
  public static final String GLOBAL_FORM_ACTIVITY_DATE = "activityDate";

}
