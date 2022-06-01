
package com.biperf.core.domain.report;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ReportCategoryType;

public class Report extends BaseDomain

{
  public static final String LOGIN_ACTIVITY = "loginActivityOrg";
  public static final String LOGIN_ACTIVITY_BY_PAX = "loginActivityPax";
  public static final String ENROLLMENT_ACTIVITY = "enrollment";
  public static final String INDIVIDUAL_ACTIVITY = "individualActivity";
  public static final String BEHAVIORS = "behaviors";
  public static final String NOMINATION_RECEIVER_SUMMARY = "nomReceiverSummary";
  public static final String NOMINATION_RECEIVED_BY_ORG = "nomReceivedByOrg";
  public static final String NOMINATION_GIVER_SUMMARY = "nomGiverSummary";
  public static final String NOMINATION_GIVEN_BY_ORG = "nomGivenByOrg";
  public static final String NOMINATION_AGING = "nomAging";
  public static final String RECOGNITION_GIVEN_BY_ORG = "recGivenByOrg";
  public static final String RECOGNITION_RECEIVED_BY_ORG = "recReceivedByOrg";
  public static final String RECOGNITION_GIVEN_BY_PAX = "recGivenByPax";
  public static final String RECOGNITION_RECEIVED_BY_PAX = "recReceivedByPax";
  public static final String CLAIM_BY_ORG = "claimByOrg";
  public static final String CLAIM_BY_PAX = "claimByPax";
  public static final String BUDGET_BALANCE = "budgetBalance";
  public static final String CASH_BUDGET_BALANCE = "cashBudgetBalance";
  public static final String USER_DASHBOARD = "userDashboard";
  public static final String AWARDS_BY_ORG = "awardsByOrg";
  public static final String AWARDS_BY_PAX = "awardsByPax";
  public static final String QUIZ_ACTIVITY = "quizActivity";
  public static final String QUIZ_ANALYSIS = "quizAnalysis";
  public static final String RECOGNITION_PURL_ACTIVITY = "recPurlActivity";
  public static final String GOAL_QUEST_PROGRESS = "gcProgress";
  public static final String GOAL_QUEST_SELECTION = "gcSelection";
  public static final String GOAL_QUEST_ACHIEVEMENT = "gcAchievement";
  public static final String GOAL_QUEST_SUMMARY = "gcSummary";
  public static final String GOAL_QUEST_MANAGER = "gcManager";
  public static final String HIERARCHY_EXPORT = "hierarchyExport";
  public static final String PARTICIPANT_EXPORT = "participantExport";
  public static final String BADGE_AVTIVITY_BY_ORG = "badgeActivityByOrg";
  public static final String BADGE_ACTIVITY = "badgeActivity";
  public static final String PLATEAU_AWARD_LEVEL_ACTIVITY = "awardLevelActivityByOrg";
  public static final String PLATEAU_AWARD_ITEM_SELECTION = "awardItemSelectionByOrg";
  public static final String PARTICIPANT_AWARD_ACTIVITY_EXPORT = "participantAwardActivityExport";
  public static final String CHALLENGE_POINT_SELECTION = "cpSelection";
  public static final String CHALLENGE_POINT_PROGRESS = "cpProgress";
  public static final String CHALLENGE_POINT_ACHIEVEMENT = "cpAchievement";
  public static final String CHALLENGE_POINT_MANAGER = "cpManager";
  public static final String CHALLENGE_POINT_SUMMARY = "cpSummary";
  public static final String THROWDOWN_ACTIVITY_BY_PAX = "throwdownActivityByPax";
  public static final String SURVEY_ANALYSIS = "surveyAnalysis";
  public static final String WORK_HAPPIER_HAPPINESS_PULSE = "happinessPulse";
  public static final String WORK_HAPPIER_CONFIDENTIAL_FEEDBACK = "confidentialFeedback";

  public static final String PLATEAU_AWARD = "merchandise";
  public static final String POINTS_AWARD = "points";
  public static final String CASH_AWARD = "cash";
  public static final String OTHER_AWARD = "other";
  public static final String PLATEAU = "plateau";

  public static final int MAX_ROWS_TO_DISPLAY = 100;

  String name;
  String reportCode;
  private String cmAssetCode;
  ReportCategoryType categoryType;
  String url;
  String description;
  boolean active;
  String module;
  boolean forceParameters;
  boolean exportOnly;
  private List<ReportChart> charts = new ArrayList<ReportChart>();
  private List<ReportParameter> reportParameters = new ArrayList<ReportParameter>();
  private boolean includedInPlateau;

  public Report()
  {
    super();
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public ReportCategoryType getCategoryType()
  {
    return categoryType;
  }

  public void setCategoryType( ReportCategoryType categoryType )
  {
    this.categoryType = categoryType;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getCode()
  {
    return url;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getModule()
  {
    return module;
  }

  public void setModule( String module )
  {
    this.module = module;
  }

  public List<ReportChart> getCharts()
  {
    return charts;
  }

  public void setCharts( List<ReportChart> charts )
  {
    this.charts = charts;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( reportCode == null ? 0 : reportCode.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    Report other = (Report)obj;
    if ( reportCode == null )
    {
      if ( other.reportCode != null )
      {
        return false;
      }
    }
    else if ( !reportCode.equals( other.reportCode ) )
    {
      return false;
    }
    return true;
  }

  public String getReportCode()
  {
    return reportCode;
  }

  public void setReportCode( String reportCode )
  {
    this.reportCode = reportCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public List<ReportParameter> getReportParameters()
  {
    return reportParameters;
  }

  public void setReportParameters( List<ReportParameter> reportParameters )
  {
    this.reportParameters = reportParameters;
  }

  public boolean isForceParameters()
  {
    return forceParameters;
  }

  public void setForceParameters( boolean forceParameters )
  {
    this.forceParameters = forceParameters;
  }

  public boolean isExportOnly()
  {
    return exportOnly;
  }

  public void setExportOnly( boolean exportOnly )
  {
    this.exportOnly = exportOnly;
  }

  public boolean isIncludedInPlateau()
  {
    return includedInPlateau;
  }

  public void setIncludedInPlateau( boolean includedInPlateau )
  {
    this.includedInPlateau = includedInPlateau;
  }

}
