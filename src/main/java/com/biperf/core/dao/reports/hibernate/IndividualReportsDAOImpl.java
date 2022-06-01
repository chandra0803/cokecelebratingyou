
package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.IndividualReportsDAO;

public class IndividualReportsDAOImpl extends BaseReportsDAO implements IndividualReportsDAO
{
  private static final Log log = LogFactory.getLog( IndividualReportsDAOImpl.class );

  private NamedParameterJdbcTemplate jdbcTemplate;

  private DataSource dataSource;

  // =======================================
  // INDIVIDUAL ACTIVITY REPORT
  // =======================================

  @Override
  public Map<String, Object> getIndividualActivityTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTABULARRESULTS";
    String sortColName = "activity";
    String[] sortColNames = { "Activity", "", "Points", "Plateau_Earned", "Sweepstakes_Won", "Other_Awards", "Received", "Sent" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityPointsReceivedChart( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETPOINTSRECEIVED";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityPointsGivenReceivedChart( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETPOINTSGIVENRECEIVED";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityTotalActivityChart( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTOTALACTIVITYCHART";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityMetricsChart( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETMETRICSCHART";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // =============================================
  // INDIVIDUAL ACTIVITY REPORT - AWARDS RECEIVED
  // =============================================

  @Override
  public Map<String, Object> getIndividualActivityAwardsReceivedTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETAWARDSRECEIVED";
    String sortColName = "date_submitted";
    String[] sortColNames = { "date_submitted", "recipient", "sender", "org_name", "promotion_name", "points", "plateau_earned", "sweepstakes_won", "onthespot" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - NOMINATIONS RECEIVED
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityNominationsReceivedTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETNOMINATIONSRECEIVED";
    String sortColName = "promo_name";
    String[] sortColNames = { "promo_name", "date_approved", "", "nominator", "points", "sweepstakes_won" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================

  // INDIVIDUAL ACTIVITY REPORT - NOMINATIONS GIVEN
  // ==================================================
  @Override
  public Map<String, Object> getIndividualActivityNominationsGivenTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETNOMINATIONSGIVEN";
    String sortColName = "promo_name";
    String[] sortColNames = { "promo_name", "date_approved", "nominee", "points", "sweepstakes_won" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - PRODUCT CLAIM
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityProductClaimTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETPRODUCTCLAIM";
    String sortColName = "claim_number";
    String[] sortColNames = { "claim_number", "", "promotion", "date_submitted", "sold_to", "claim_status" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS GIVEN
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityRecognitionsGivenTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETRECOGNITIONSGIVEN";
    String sortColName = "promo_name";
    String[] sortColNames = { "promo_name", "date_submitted", "", "receiver_name", "points_cnt", "plateau_earned_cnt", "sweepstakes_won_cnt", "status" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS RECEIVED
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityRecognitionsReceivedTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETRECOGNITIONSRECEIVED";
    String sortColName = "promo_name";
    String[] sortColNames = { "promo_name", "date_submitted", "", "giver_name", "points_cnt", "plateau_earned_cnt", "sweepstakes_won_cnt" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - THROWDOWN SUMMARY
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityThrowdownTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTHROWDOWNTABULARRESULTS";
    String sortColName = "promotion_name";
    String[] sortColNames = { "promotion_name", "", "wins", "losses", "ties", "activity", "rank", "payout" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - THROWDOWN DETAIL
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityThrowdownDetailResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTHROWDOWNDETAILRESULTS";
    String sortColName = "round_number";
    String[] sortColNames = { "promotion_name", "round_number", "wins", "losses", "ties", "activity", "rank", "payout" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - QUIZ
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityQuizTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETQUIZTABULARRESULTS";
    String sortColName = "promo_name";
    String[] sortColNames = { "promo_name", "", "quiz_attempts", "quiz_failed", "quiz_passed", "points", "sweepstakes_won" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityQuizDetailResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETQUIZDETAILRESULTS";
    String sortColName = "quiz_name";
    String[] sortColNames = { "promotion", "quiz_date", "", "quiz_result", "points", "sweepstakes_won" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - GOALQUEST
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityGoalQuestTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETGOALQUESTTABULARRESULTS";
    String sortColName = "Org_Name";
    String[] sortColNames = { "Org_Name", "Promotion", "Level_Selected", "Base", "Goal", "Actual_Results", "pct_of_Goal", "Achieved", "Points", "Plateau_Earned" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - CHALLENGEPOINT
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityChallengePointTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETCPTABULARRESULTS";
    String sortColName = "Org_Name";
    String[] sortColNames = { "Org_Name", "Promotion", "Level_Selected", "Base", "Challengepoint", "Actual_Results", "pct_of_CP", "Achieved", "Points", "Plateau_Earned" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - ONTHESPOT
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityOnTheSpotTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETONTHESPOTTABULARRESULTS";
    String sortColName = "Trans_Date";
    String[] sortColNames = { "Trans_Date", "Org_Name", "Promotion_Name", "Points", "OnTheSpot" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - BADGE
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityBadgeTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETBADGETABULARRESULTS";
    String sortColName = "promotion_name";
    String[] sortColNames = { "promotion_name", "media_date", "points" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  public Map<String, Object> getIndividualActivitySSIContestTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETSSICONTESTS";
    String sortColName = "SSI_CONTEST_NAME";
    String[] sortColNames = { "SSI_CONTEST_NAME", "", "PAYOUT_ISSUE_DATE", "POINTS", "OTHER", "OTHER_VALUE" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcIndividualActivityReport procedure = new CallPrcIndividualActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeSSIProcedure( reportParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - EXTRACT
  // ==================================================

  @Override
  public Map getIndividualActivityReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcIndividualActivityExtract procedure = new CallPrcIndividualActivityExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

}
