
package com.biperf.core.ui.reports;

public class ReportBeanMethod
{
  public static final ReportBeanMethod LOGIN_REPORT = new ReportBeanMethod( "loginReportsService", "getLoginExtractResults" );
  public static final ReportBeanMethod AWARDS_REPORT = new ReportBeanMethod( "awardsReportsService", "getAwardsActivityReportExtract" );
  public static final ReportBeanMethod BADGES_REPORT = new ReportBeanMethod( "badgeReportsService", "getBadgeActivityExtractResults" );
  public static final ReportBeanMethod POINTS_BUDGET_REPORT = new ReportBeanMethod( "budgetReportsService", "getPointsBudgetExtractResults" );
  public static final ReportBeanMethod POINTS_BUDGET_ISSUANCE_REPORT = new ReportBeanMethod( "budgetReportsService", "getPointsBudgetIssuanceExtractResults" );
  public static final ReportBeanMethod BUDGET_REPORT = new ReportBeanMethod( "budgetReportsService", "getBudgetExtractResults" );
  public static final ReportBeanMethod POINTS_BUDGET_SECONDARY_REPORT = new ReportBeanMethod( "budgetReportsService", "getPointsBudgetSecondLevelExtractResults" );
  public static final ReportBeanMethod CASH_BUDGET_REPORT = new ReportBeanMethod( "budgetReportsService", "getCashBudgetExtractResults" );
  public static final ReportBeanMethod CASH_BUDGET_ISSUANCE_REPORT = new ReportBeanMethod( "budgetReportsService", "getCashBudgetIssuanceExtractResults" );
  public static final ReportBeanMethod CASH_BUDGET_SECONDARY_REPORT = new ReportBeanMethod( "budgetReportsService", "getCashBudgetSecondLevelExtractResults" );
  public static final ReportBeanMethod CLAIM_REPORT = new ReportBeanMethod( "claimReportsService", "getClaimExtractResults" );
  public static final ReportBeanMethod CLAIM_ITEM_REPORT = new ReportBeanMethod( "claimReportsService", "getItemsClaimExtractResults" );
  public static final ReportBeanMethod ENROLLMENT_REPORT = new ReportBeanMethod( "enrollmentReportsService", "getEnrollmentExtractResults" );
  public static final ReportBeanMethod GQ_SELECTION_REPORT = new ReportBeanMethod( "goalQuestReportsService", "getGoalQuestSelectionExtractResults" );
  public static final ReportBeanMethod GQ_ACHIEVEMENT_REPORT = new ReportBeanMethod( "goalQuestReportsService", "getGoalQuestAchievementExtractResults" );
  public static final ReportBeanMethod GQ_PROGRESS_REPORT = new ReportBeanMethod( "goalQuestReportsService", "getGoalQuestProgressExtractResults" );
  public static final ReportBeanMethod HIERARCHY_REPORT = new ReportBeanMethod( "hierarchyExportReportService", "getHierarchyExportReportExtract" );
  public static final ReportBeanMethod NOMINATION_REPORT = new ReportBeanMethod( "nominationReportsService", "getNominationExtractResults" );
  public static final ReportBeanMethod NOMINATION_SUMMARY_REPORT = new ReportBeanMethod( "nominationReportsService", "getNominationSummaryExtractResults" );
  public static final ReportBeanMethod NOMINATION_AGING_REPORT = new ReportBeanMethod( "nominationReportsService", "getNominationAgingReportSummaryExtract" );
  public static final ReportBeanMethod NOMINATION_AGING_DETAIL_REPORT = new ReportBeanMethod( "nominationReportsService", "getNominationAgingReportDetailExtract" );
  public static final ReportBeanMethod PARTICIPANT_REPORT = new ReportBeanMethod( "participantExportReportService", "getParticipantExportReportExtract" );
  public static final ReportBeanMethod PLATEAU_AWARD_LEVEL_ACTIVITY_REPORT = new ReportBeanMethod( "plateauAwardReportsService", "getParticipantAwardLevelExtract" );
  public static final ReportBeanMethod PARTICIPANT_AWARD_ACTIVITY_REPORT = new ReportBeanMethod( "participantAwardActivityExportReportService", "getParticipantAwardActivityExportReportExtract" );
  public static final ReportBeanMethod QUIZ_ACTIVITY_REPORT = new ReportBeanMethod( "quizReportsService", "getQuizActivityReportExtract" );
  public static final ReportBeanMethod QUIZ_ANALYSIS_REPORT = new ReportBeanMethod( "quizReportsService", "getQuizAnalysisReportExtract" );
  public static final ReportBeanMethod RECOGNITION_ACTIIVTY_REPORT = new ReportBeanMethod( "recognitionReportsService", "getRecognitionActivityReportExtract" );
  public static final ReportBeanMethod RECOGNITION_PURL_ACTIVITY_REPORT = new ReportBeanMethod( "recognitionPurlActivityReportsService", "getExtractResults" );
  public static final ReportBeanMethod BEHAVIOR_REPORT = new ReportBeanMethod( "behaviorReportsService", "getBehaviorReportExtract" );
  // START IndividualReportAction mappings
  public static final ReportBeanMethod INDIVIDUAL_ACTIVITY = new ReportBeanMethod( "individualReportsService", "getIndividualActivityReportExtract" );
  public static final ReportBeanMethod CP_SELECTION_REPORT = new ReportBeanMethod( "challengePointReportsService", "getChallengePointSelectionExtractResults" );
  public static final ReportBeanMethod CP_PROGRESS_REPORT = new ReportBeanMethod( "challengePointReportsService", "getChallengePointProgressExtractResults" );
  public static final ReportBeanMethod CP_ACHIEVEMENT_REPORT = new ReportBeanMethod( "challengePointReportsService", "getChallengePointAchievementExtractResults" );
  public static final ReportBeanMethod CP_MANAGER_ACHIEVEMENT_REPORT = new ReportBeanMethod( "challengePointReportsService", "getChallengePointManagerAchievementExtractReport" );
  public static final ReportBeanMethod CP_PROGRAM_SUMMARY_REPORT = new ReportBeanMethod( "challengePointReportsService", "getChallengePointProgramSummaryExtractReport" );
  public static final ReportBeanMethod GQ_PROGRAM_SUMMARY_REPORT = new ReportBeanMethod( "goalQuestReportsService", "getGoalQuestProgramSummaryExtractReport" );
  public static final ReportBeanMethod GQ_MANAGER_ACHIEVEMENT_REPORT = new ReportBeanMethod( "goalQuestReportsService", "getGoalQuestManagerAchievementExtractReport" );
  public static final ReportBeanMethod THROWDOWN_ACTIVITY_BY_PAX_REPORT = new ReportBeanMethod( "throwdownActivityReportsService", "getThrowdownActivityByPaxReportExtract" );
  public static final ReportBeanMethod ENGAGEMENT_PARTICIPATION_SCORE_REPORT = new ReportBeanMethod( "engagementReportsService", "getParticipationScoreReportExtract" );

  public static final ReportBeanMethod SURVEY_ANALYSIS_REPORT = new ReportBeanMethod( "surveyReportsService", "getSurveyAnalysisOptionsReportExtract" );
  public static final ReportBeanMethod SURVEY_OPEN_ENDED_REPORT = new ReportBeanMethod( "surveyReportsService", "getSurveyAnalysisOpenEndedReportExtract" );

  public static final ReportBeanMethod SSI_CONTEST_DATA_EXTRACT = new ReportBeanMethod( "ssiContestService", "downloadContestData" );
  public static final ReportBeanMethod SSI_CONTEST_CREATOR_EXTRACT = new ReportBeanMethod( "ssiContestService", "downloadContestCreatorExtractData" );

  public static final ReportBeanMethod WORK_HAPPIER_CONFIDENTIAL_FEEDBACK_REPORT = new ReportBeanMethod( "workHappierReportsService", "getConfidentialFeedbackReportExtract" );
  public static final ReportBeanMethod WORK_HAPPIER_HAPPINESS_PULSE = new ReportBeanMethod( "workHappierReportsService", "getHappinessPulseReportExtract" );

  // END IndividualReportAction mappings

  private String beanName = null;
  private String methodName = null;

  public ReportBeanMethod( String beanName, String methodName )
  {
    this.beanName = beanName;
    this.methodName = methodName;
  }

  public String getBeanName()
  {
    return beanName;
  }

  public void setBeanName( String beanName )
  {
    this.beanName = beanName;
  }

  public String getMethodName()
  {
    return methodName;
  }

  public void setMethodName( String methodName )
  {
    this.methodName = methodName;
  }

}
