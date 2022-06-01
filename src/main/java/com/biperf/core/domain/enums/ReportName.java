/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ReportName.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ReportName <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 */
public class ReportName extends ModuleAwarePickListItem
{

  private static final String PICKLIST_ASSET = "picklist.reportname";

  public static final String AWARD_ISSUANCE = "awardmedia";
  public static final String BUDGETS = "budget";
  public static final String ENROLLMENT = "enrollment";
  public static final String ENROLLMENT_SUMMARY = "enrollmentsummary";
  public static final String ENROLLMENT_DETAILS = "enrollmentdetails";
  public static final String TOTAL_ENROLLMENT = "totalenrollment";
  public static final String ENROLLMENT_STATUS_PERCENTAGE = "enrollmentstatuspercentage";
  public static final String ENROLLMENT_ACTIVE_INACTIVE = "enrollmentactiveinactive";
  public static final String IDEAS = "idea";
  public static final String NEW_HIRE_REFERRALS = "referral";
  public static final String OVERALL_ACTIVITY = "activity";
  public static final String CLAIM_SUBMITTED = "claimssubmitted";
  public static final String PRODUCT_CLAIMS = "productclaim";
  public static final String QUIZ_ACTIVITY = "quiz";
  public static final String QUIZ_ANALYSIS = "quizanalysis";
  public static final String RECOGNITION_PURL_ACITIVTY = "recognitionpurlactivity";

  public static final String RECOGNITION_ACTIVITY = "recognition";
  public static final String RECOGNITION_GIVEN_BY_TIME = "recognitiongivenbytime";
  public static final String RECOGNITION_GIVEN_PARTICIPATION_RATE_BY_ORG = "recognitiongivenparticipationratebyorg";
  public static final String RECOGNITION_GIVEN_PARTICIPATION_RATE = "recognitiongivenparticipationrate";
  public static final String RECOGNITION_POINTS_GIVEN_BY_TIME = "recognitionpointsgivenbytime";
  public static final String RECOGNITION_POINTS_GIVEN = "recognitionpointsgiven";
  public static final String RECOGNITION_SUMMARY = "recognitionsummary";

  public static final String RECOGNITIONS_GIVEN_BY_PARTICIPANT = "recognitionsgivenbyparticipant";
  public static final String RECOGNITION_POINTS_GIVEN_BY_PARTICIPANT = "recognitionpointsgivenbyparticipant";
  public static final String RECOGNITIONS_GIVEN_BY_PROMOTION = "recognitionsgivenbypromotion";
  public static final String RECOGNITION_POINTS_GIVEN_BY_PROMOTION = "recognitionpointsgivenbypromotion";
  public static final String RECOGNITIONS_GIVEN_METRICS = "recognitionsgivenmetrics";
  public static final String RECOGNITIONS_GIVEN_PARTICIPATION_RATE_BY_PAX = "recognitionsgivenparticipationratebypax";
  public static final String RECOGNITION_SUMMARY_GIVEN_BY_PARTICIPANT = "recognitionsummarygivenbyparticipant";
  public static final String RECOGNITION_DETAILS_GIVEN_BY_PARTICIPANT = "recognitiondetailsgivenbyparticipant";

  public static final String RECOGNITIONS_RECEIVED_BY_PARTICIPANT = "recognitionsreceivedbyparticipant";
  public static final String RECOGNITION_POINTS_RECEIVED_BY_PARTICIPANT = "recognitionpointsreceivedbyparticipant";
  public static final String RECOGNITIONS_RECEIVED_BY_PROMOTION = "recognitionsreceivedbypromotion";
  public static final String RECOGNITION_POINTS_RECEIVED_BY_PROMOTION = "recognitionpointsreceivedbypromotion";
  public static final String RECOGNITIONS_RECEIVED_METRICS = "recognitionsreceivedmetrics";
  public static final String RECOGNITIONS_RECEIVED_PARTICIPATION_RATE_BY_PAX = "recognitionsreceivedparticipationratebypax";
  public static final String RECOGNITION_SUMMARY_RECEIVED_BY_PARTICIPANT = "recognitionsummaryreceivedbyparticipant";
  public static final String RECOGNITION_DETAILS_RECEIVED_BY_PARTICIPANT = "recognitiondetailsreceivedbyparticipant";

  public static final String RECOGNITION_PROGRAM_REFERENCE = "recognitionProgramReference";
  public static final String RECOGNITION_BEHAVIOR = "recognitionbehaviors";
  public static final String SIX_SIGMA = "sixsigma";
  public static final String NOMINATION_ACTIVITY = "nomination";
  public static final String NOMINATION_GIVER_SUMMARY = "nominationgiversummary";
  public static final String NOMINATION_BEHAVIOR = "nominationbehaviors";
  public static final String SURVEYS = "surveys";
  public static final String CERTIFICATE = "certificate";
  public static final String GOAL_SELECTION = "goalselection";
  public static final String GOAL_ACHIEVEMENT = "goalachievement";
  public static final String GOAL_ROI = "goalroi";
  public static final String GOAL_PROGRESS = "goalprogress";
  public static final String MANAGER_GOAL_ACHIEVEMENT = "managergoalachievement";
  public static final String AWARD_ITEM_ACTIVITY = "awarditemactivity";
  public static final String AWARD_ITEM_SELECTION = "awarditemselection";
  public static final String GOALQUEST_PARTNER = "partner";
  public static final String AWARD_ORDER_DETAIL = "awardOrderDetail";
  public static final String AWARD_EARNING_DETAIL = "awardearning";
  public static final String CHALLENGEPOINT_SELECTION = "challengepointselection";
  public static final String CHALLENGEPOINT_PROGRESS = "challengepointprogress";
  public static final String CHALLENGEPOINT_ACHIEVEMENT = "challengepointachievement";
  public static final String CHALLENGEPOINT_PRODUCTION = "challengepointproduction";
  public static final String MANAGER_CHALLENGEPOINT_ACHIEVEMENT = "managerchallengepointachievement";
  public static final String HIERARCHY_EXPORT = "hierarchyexport";
  public static final String PARTICIPANT_EXPORT = "participantexport";
  public static final String LOGIN_ACTIVITY = "loginactivity";
  public static final String LOGIN_ACTIVITY_BY_TIME = "loginactivitybytime";
  public static final String LOGIN_ACTIVITY_BY_ORGANIZATION = "loginactivitybyorganization";
  public static final String LOGIN_ACTIVITY_PERCENTAGE = "loginactivitypercentage";
  public static final String LOGIN_ACTIVITY_AVERAGE = "loginactivityaverage";
  public static final String LOGIN_ACTIVITY_LIST_OF_PARTICIPANTS_BY_ORGANIZATION = "loginactivitylistofparticipantsbyorganization";
  public static final String LOGIN_ACTIVITY_TOP_LEVEL = "loginactivitytoplevel";
  public static final String PARTCIPANT_LOGON_ACTIVITY = "partcipantlogonactivity";
  public static final String INDIVIDUAL_ACTIVITY = "individualactivity";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportName()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ReportName
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReportName.class, new PickListItemNameComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ReportName
   */
  public static ReportName lookup( String code )
  {
    return (ReportName)getPickListFactory().getPickListItem( ReportName.class, code );
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

  public boolean isAwardIssuanceReport()
  {
    return AWARD_ISSUANCE.equals( getCode() );
  }

  public boolean isBudgetReport()
  {
    return BUDGETS.equals( getCode() );
  }

  public boolean isEnrollmentReport()
  {
    return ENROLLMENT.equals( getCode() );
  }

  public boolean isEnrollmentSummaryReport()
  {
    return ENROLLMENT_SUMMARY.equals( getCode() );
  }

  public boolean isEnrollmentDetailsReport()
  {
    return ENROLLMENT_DETAILS.equals( getCode() );
  }

  public boolean isTotalEnrollmentReport()
  {
    return TOTAL_ENROLLMENT.equals( getCode() );
  }

  public boolean isEnrollmentStatusPercentageReport()
  {
    return ENROLLMENT_STATUS_PERCENTAGE.equals( getCode() );
  }

  public boolean isEnrollmentActiveInactiveReport()
  {
    return ENROLLMENT_ACTIVE_INACTIVE.equals( getCode() );
  }

  public boolean isIdeasReport()
  {
    return IDEAS.equals( getCode() );
  }

  public boolean isNewHireReferralsReport()
  {
    return NEW_HIRE_REFERRALS.equals( getCode() );
  }

  public boolean isOverallActivityReport()
  {
    return OVERALL_ACTIVITY.equals( getCode() );
  }

  public boolean isClaimSubmittedReport()
  {
    return CLAIM_SUBMITTED.equals( getCode() );
  }

  public boolean isProductClaimsReport()
  {
    return PRODUCT_CLAIMS.equals( getCode() );
  }

  public boolean isQuizActivityReport()
  {
    return QUIZ_ACTIVITY.equals( getCode() );
  }

  public boolean isQuizAnalysisReport()
  {
    return QUIZ_ANALYSIS.equals( getCode() );
  }

  public boolean isRecognitionPurlActivityReport()
  {
    return RECOGNITION_PURL_ACITIVTY.equals( getCode() );
  }

  public boolean isRecognitionActivityReport()
  {
    return RECOGNITION_ACTIVITY.equals( getCode() );
  }

  public boolean isRecognitionGivenByTimeReport()
  {
    return RECOGNITION_GIVEN_BY_TIME.equals( getCode() );
  }

  public boolean isRecognitionGivenParticipationRateByOrgReport()
  {
    return RECOGNITION_GIVEN_PARTICIPATION_RATE_BY_ORG.equals( getCode() );
  }

  public boolean isRecognitionGivenParticipationRateReport()
  {
    return RECOGNITION_GIVEN_PARTICIPATION_RATE.equals( getCode() );
  }

  public boolean isRecognitionPointsGivenByTimeReport()
  {
    return RECOGNITION_POINTS_GIVEN_BY_TIME.equals( getCode() );
  }

  public boolean isRecognitionPointsGivenReport()
  {
    return RECOGNITION_POINTS_GIVEN.equals( getCode() );
  }

  public boolean isRecognitionsGivenByParticipantReport()
  {
    return RECOGNITIONS_GIVEN_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionPointsGivenByParticipantReport()
  {
    return RECOGNITION_POINTS_GIVEN_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionsGivenByPromotionReport()
  {
    return RECOGNITIONS_GIVEN_BY_PROMOTION.equals( getCode() );
  }

  public boolean isRecognitionPointsGivenByPromotionReport()
  {
    return RECOGNITION_POINTS_GIVEN_BY_PROMOTION.equals( getCode() );
  }

  public boolean isRecognitionsGivenMetricsReport()
  {
    return RECOGNITIONS_GIVEN_METRICS.equals( getCode() );
  }

  public boolean isRecognitionsGivenParticipationRateByPaxReport()
  {
    return RECOGNITIONS_GIVEN_PARTICIPATION_RATE_BY_PAX.equals( getCode() );
  }

  public boolean isRecognitionSummaryGivenByParticipantReport()
  {
    return RECOGNITION_SUMMARY_GIVEN_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionDetailsGivenByParticipantReport()
  {
    return RECOGNITION_DETAILS_GIVEN_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionsReceivedByParticipantReport()
  {
    return RECOGNITIONS_RECEIVED_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionPointsReceivedByParticipantReport()
  {
    return RECOGNITION_POINTS_RECEIVED_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionsReceivedByPromotionReport()
  {
    return RECOGNITIONS_RECEIVED_BY_PROMOTION.equals( getCode() );
  }

  public boolean isRecognitionPointsReceivedByPromotionReport()
  {
    return RECOGNITION_POINTS_RECEIVED_BY_PROMOTION.equals( getCode() );
  }

  public boolean isRecognitionsReceivedMetricsReport()
  {
    return RECOGNITIONS_RECEIVED_METRICS.equals( getCode() );
  }

  public boolean isRecognitionsReceivedParticipationRateByPaxReport()
  {
    return RECOGNITIONS_RECEIVED_PARTICIPATION_RATE_BY_PAX.equals( getCode() );
  }

  public boolean isRecognitionSummaryReceivedByParticipantReport()
  {
    return RECOGNITION_SUMMARY_RECEIVED_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionDetailsReceivedByParticipantReport()
  {
    return RECOGNITION_DETAILS_RECEIVED_BY_PARTICIPANT.equals( getCode() );
  }

  public boolean isRecognitionBehaviorReport()
  {
    return RECOGNITION_BEHAVIOR.equals( getCode() );
  }

  public boolean isRecognitionSummaryReport()
  {
    return RECOGNITION_SUMMARY.equals( getCode() );
  }

  public boolean isRecognitionProgramReferenceExtract()
  {
    return RECOGNITION_PROGRAM_REFERENCE.equals( getCode() );
  }

  public boolean isNominationActivityReport()
  {
    return NOMINATION_ACTIVITY.equals( getCode() );
  }

  public boolean isNominationBehaviorReport()
  {
    return NOMINATION_BEHAVIOR.equals( getCode() );
  }

  public boolean isNominationGiverSummaryReport()
  {
    return NOMINATION_GIVER_SUMMARY.equals( getCode() );
  }

  public boolean isSixSigmaReport()
  {
    return SIX_SIGMA.equals( getCode() );
  }

  public boolean isSurveyReport()
  {
    return SURVEYS.equals( getCode() );
  }

  public boolean isCertificateReport()
  {
    return CERTIFICATE.equals( getCode() );
  }

  public boolean isGoalSelectionReport()
  {
    return GOAL_SELECTION.equals( getCode() );
  }

  public boolean isGoalAchievementReport()
  {
    return GOAL_ACHIEVEMENT.equals( getCode() );
  }

  public boolean isGoalROIReport()
  {
    return GOAL_ROI.equals( getCode() );
  }

  public boolean isGoalProgressReport()
  {
    return GOAL_PROGRESS.equals( getCode() );
  }

  public boolean isManagerGoalAchievementReport()
  {
    return MANAGER_GOAL_ACHIEVEMENT.equals( getCode() );
  }

  public boolean isManagerChallengepointAchievementReport()
  {
    return MANAGER_CHALLENGEPOINT_ACHIEVEMENT.equals( getCode() );
  }

  public boolean isAwardItemActivityReport()
  {
    return AWARD_ITEM_ACTIVITY.equals( getCode() );
  }

  public boolean isAwardItemSelectionReport()
  {
    return AWARD_ITEM_SELECTION.equals( getCode() );
  }

  public boolean isGoalQuestPartnerReport()
  {
    return GOALQUEST_PARTNER.equals( getCode() );
  }

  public boolean isAwardOrderDetailExtract()
  {
    return AWARD_ORDER_DETAIL.equals( getCode() );
  }

  public boolean isAwardEarningDetailExtract()
  {
    return AWARD_EARNING_DETAIL.equals( getCode() );
  }

  public boolean isChallengepointSelectionReport()
  {
    return CHALLENGEPOINT_SELECTION.equals( getCode() );
  }

  public boolean isChallengepointProgressReport()
  {
    return CHALLENGEPOINT_PROGRESS.equals( getCode() );
  }

  public boolean isChallengepointAchievementReport()
  {
    return CHALLENGEPOINT_ACHIEVEMENT.equals( getCode() );
  }

  public boolean isChallengepointProduction()
  {
    return CHALLENGEPOINT_PRODUCTION.equals( getCode() );
  }

  public boolean isLoginActivityReport()
  {
    return LOGIN_ACTIVITY.equals( getCode() );
  }

  public boolean isLoginActivityByTimeReport()
  {
    return LOGIN_ACTIVITY_BY_TIME.equals( getCode() );
  }

  public boolean isLoginActivityByOrganizationReport()
  {
    return LOGIN_ACTIVITY_BY_ORGANIZATION.equals( getCode() );
  }

  public boolean isLoginActivityPercentageReport()
  {
    return LOGIN_ACTIVITY_PERCENTAGE.equals( getCode() );
  }

  public boolean isLoginActivityAverageReport()
  {
    return LOGIN_ACTIVITY_AVERAGE.equals( getCode() );
  }

  public boolean isLoginActivityListOfParticipantsByOrganizationReport()
  {
    return LOGIN_ACTIVITY_LIST_OF_PARTICIPANTS_BY_ORGANIZATION.equals( getCode() );
  }

  public boolean isLoginActivityTopLevelReport()
  {
    return LOGIN_ACTIVITY_TOP_LEVEL.equals( getCode() );
  }

  public boolean isParticipantLogonActivityReport()
  {
    return PARTCIPANT_LOGON_ACTIVITY.equals( getCode() );
  }

  public boolean isIndividualActivity()
  {
    return INDIVIDUAL_ACTIVITY.equals( getCode() );
  }

  public boolean isHierarchyExportReport()
  {
    return HIERARCHY_EXPORT.equals( getCode() );
  }

  public boolean isParticipantExportReport()
  {
    return PARTICIPANT_EXPORT.equals( getCode() );
  }

  public boolean isReportExtractable()
  {
    return isRecognitionActivityReport() || isRecognitionPurlActivityReport() || isRecognitionBehaviorReport() || isQuizActivityReport() || isNominationActivityReport() || isNominationBehaviorReport()
        || isAwardIssuanceReport() || isEnrollmentReport() || isClaimSubmittedReport() || isCertificateReport() || isGoalAchievementReport() || isGoalSelectionReport() || isGoalProgressReport()
        || isChallengepointSelectionReport() || isChallengepointProgressReport() || isChallengepointAchievementReport() || isBudgetReport() || isAwardItemActivityReport() || isGoalQuestPartnerReport()
        || isManagerChallengepointAchievementReport();
  }

}
