/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.reports.AwardsReportsDAO;
import com.biperf.core.dao.reports.BaseReportsDAO;

/**
 * @author poddutur
 *
 */
public class AwardsReportsDAOImpl extends BaseReportsDAO implements AwardsReportsDAO
{
  private DataSource dataSource;

  /* AWARDS RECEIVED - PARTICIPATION BY ORGANIZATION */

  /* CHARTS */

  @Override
  public Map<String, Object> getTotalPointsIssuedForOrgBarResults( Map<String, Object> reportParameters )// 1
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETTOTPTSISS_ORGBARRES";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByOrg procedure = new CallPrcAwardsByOrg( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getReceivedNotReceivedAwardsForOrgBarResults( Map<String, Object> reportParameters )// 2
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETRCVNOTRCVAWD_ORGBARRES";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByOrg procedure = new CallPrcAwardsByOrg( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getPersonsReceivingAwardsForOrgBarResults( Map<String, Object> reportParameters )// 3
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETPERRCVAWD_ORGBARRES";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByOrg procedure = new CallPrcAwardsByOrg( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /* TABLES */

  @Override
  public Map<String, Object> getAwardsSummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSSUMMARY";
    String sortColName = "ORG_NAME";
    String[] sortColNames = { "ORG_NAME",
                              "",
                              "ELIGIBLE_PARTICIPANTS",
                              "RECEIVED_AWARD",
                              "RECEIVED_AWARD_PCT",
                              "HAVE_NOTRECEIVED_AWARD",
                              "HAVE_NOTRECEIVED_AWARD_PCT",
                              "POINTS_RECEIVED",
                              "PLATEAU_EARNED_COUNT",
                              "SWEEPSTAKES_WON_COUNT",
                              "ON_THE_SPOT_DEPOSITED_COUNT",
                              "OTHER" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByOrg procedure = new CallPrcAwardsByOrg( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsFirstDetailResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSFIRSTDETAIL";
    String sortColName = "PARTICIPANT_NAME";
    String[] sortColNames = { "PARTICIPANT_NAME", "", "ORG_NAME", "COUNTRY", "DEPARTMENT", "JOB_POSITION", "POINTS_RECEIVED_COUNT", "SWEEPSTAKES_WON_COUNT", "ON_THE_SPOT_DEPOSITED_COUNT" };

    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByOrg procedure = new CallPrcAwardsByOrg( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsSecondDetailResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSSECONDDETAIL";
    String sortColName = "PARTICIPANT_NAME";
    String[] sortColNames = { "AWARD_DATE",
                              "PARTICIPANT_NAME",
                              "ORG_NAME",
                              "COUNTRY",
                              "DEPARTMENT",
                              "JOB_POSITION",
                              "PROMOTION_NAME",
                              "POINTS_RECEIVED_COUNT",
                              "SWEEPSTAKES_WON_COUNT",
                              "ON_THE_SPOT_SERIAL",
                              "OTHER",
                              "OTHER_VALUE",
                              "CASH_RECEIVED",
                              "LEVEL_NAME" };

    if ( reportParameters.get( "sortedOn" ) != null && ( (Integer)reportParameters.get( "sortedOn" ) ).intValue() < 13 )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByOrg procedure = new CallPrcAwardsByOrg( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /* AWARDS RECEIVED - LIST OF RECIPIENTS */

  /* CHARTS */

  @Override
  public Map<String, Object> getTotalPointsIssuedByPaxBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETTOTPTSISS_PAXBARRES";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByPax procedure = new CallPrcAwardsByPax( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getReceivedNotReceivedAwardsPaxPieResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETRECVNOTRCVAWD_PAXPIERES";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByPax procedure = new CallPrcAwardsByPax( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsSummaryByPaxResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSSUMMARYPAX";
    String sortColName = "PARTICIPANT_NAME";
    String[] sortColNames = { "PARTICIPANT_NAME",
                              "",
                              "ORG_NAME",
                              "COUNTRY",
                              "DEPARTMENT",
                              "JOB_POSITION",
                              "POINTS_RECEIVED_COUNT",
                              "SWEEPSTAKES_WON_COUNT",
                              "ON_THE_SPOT_DEPOSITED_COUNT",
                              "CASH_RECEIVED_COUNT",
                              "OTHER",
                              "PLATEAU_EARNED_COUNT" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByPax procedure = new CallPrcAwardsByPax( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardsDetailByPaxResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSDETAILPAXRES";
    String sortColName = "PARTICIPANT_NAME";
    String[] sortColNames = { "AWARD_DATE",
                              "PARTICIPANT_NAME",
                              "ORG_NAME",
                              "COUNTRY",
                              "DEPARTMENT",
                              "JOB_POSITION",
                              "PROMOTION_NAME",
                              "POINTS_RECEIVED_COUNT",
                              "SWEEPSTAKES_WON_COUNT",
                              "ON_THE_SPOT_SERIAL",
                              "CASH_RECEIVED_COUNT",
                              "OTHER",
                              "OTHER_VALUE",
                              "LEVEL_NAME" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcAwardsByPax procedure = new CallPrcAwardsByPax( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /* EXTRACT */

  @Override
  public Map getAwardsActivityReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcAwardsExtract procedure = new CallPrcAwardsExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

}
