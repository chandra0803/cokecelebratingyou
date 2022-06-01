/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/NominationReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.NominationReportsDAO;
import com.biperf.core.utils.UserManager;

/**
 * 
 * NominationReportsDAOImpl.
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
 * <td>drahn</td>
 * <td>Aug 3, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * <tr>
 * <td>kandhi</td>
 * <td>Oct 3, 2012</td>
 * <td>1.32</td>
 * <td>Added multi-select</td>
 * </tr>
 * </table>
 * 
 */
public class NominationReportsDAOImpl extends BaseReportsDAO implements NominationReportsDAO
{
  private static final Log log = LogFactory.getLog( NominationReportsDAOImpl.class );

  private DataSource dataSource;

  // =======================================
  // NOMINATION GIVER BY ORG REPORT
  // =======================================

  @Override
  public Map<String, Object> getNominationsGivenByOrgSummaryTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMINATION.PRC_SUMMARY_BY_ORG";
    String sortColName = "ORG_NAME";
    String[] sortColNames = { "ORG_NAME",
                              "",
                              "ELIGIBLE_NOMINATORS",
                              "ACTUAL_NOMINATORS",
                              "PCT_ELIGIBLE_NOMINATORS",
                              "ELIGIBLE_NOMINEES",
                              "ACTUAL_NOMINEES",
                              "PCT_ELIGIBLE_NOMINEES",
                              "NOMINATIONS_SUBMITTED",
                              "NOMINATIONS_RECEIVED",
                              "NOMINATIONS_WON",
                              "POINTS_RECEIVED",
                              "CASH_RECEIVED",
                              "OTHER_QTY_RECEIVED",
                              "OTHER_VALUE_RECEIVED" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    reportParameters.put( "userId", UserManager.getUserId() ); // JIRA 1893
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcNominationGivenByOrgReport procedure = new CallPrcNominationGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getNominatorsByOrgChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMINATION.PRC_NOMINATORS_BY_ORG";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcNominationGivenByOrgReport procedure = new CallPrcNominationGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getNomineesByOrgChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMINATION.PRC_NOMINEES_BY_ORG";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcNominationGivenByOrgReport procedure = new CallPrcNominationGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationsByMonthChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMINATION.PRC_NOMINATIONS_BY_MONTH";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcNominationGivenByOrgReport procedure = new CallPrcNominationGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // NOMINATION GIVER REPORT
  // =======================================

  @Override
  public Map<String, Object> getNominationGiverNominatorsListResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMI_GIVEN.PRC_SUMMARY_NOMINATOR";
    String sortColName = "NOMINATOR";
    String[] sortColNames = { "NOMINATOR", "", "GIVER_COUNTRY_NAME", "GIVER_NODE_NAME", "SUBMITTED_CNT", "POINTS_ISSUED", "CASH_ISSUED", "OTHER_QTY_ISSUED", "OTHER_AMT_ISSUED" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcNominationGiversReport procedure = new CallPrcNominationGiversReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationGiverNominationsListResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMI_GIVEN.PRC_NOMINATION_NOMINATOR";
    String sortColName = "DATE_SUBMITTED";
    String[] sortColNames = { "DATE_SUBMITTED",
                              "",
                              "NOMINATOR",
                              "GIVER_COUNTRY_NAME",
                              "GIVER_NODE_NAME",
                              "TEAM_NAME",
                              "NOMINEE",
                              "RECVR_COUNTRY_NAME",
                              "RECVR_NODE_NAME",
                              "PROMOTION_NAME",
                              "CLAIM_ITEM_STATUS",
                              "POINTS_AWARD_AMT",
                              "CASH_AWARD_AMT",
                              "OTHER_AWARD_DESC",
                              "OTHER_AWARD_AMT" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcNominationNominatorsReport procedure = new CallPrcNominationNominatorsReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getTopNominatorsChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMI_GIVEN.PRC_TOP_NOMINATOR";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcNominationGiversTopNominatorsReport procedure = new CallPrcNominationGiversTopNominatorsReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // NOMINATION RECEIVER REPORT
  // =======================================

  @Override
  public Map<String, Object> getNominationReceiverTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMI_RECEIVE.PRC_SUMMARY_NOMINEE";
    String sortColName = "NOMINEE";
    String[] sortColNames = { "NOMINEE", "", "RECVR_COUNTRY_NAME", "RECVR_NODE_NAME", "NOMINATED_CNT", "POINTS_RECEIVED", "CASH_RECEIVED", "OTHER_QTY_RECEIVED", "OTHER_AMT_RECEIVED" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcNominationReceiversReport procedure = new CallPrcNominationReceiversReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationNomineeTabularResults( Map<String, Object> reportParameters, boolean nodeAndBelow )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMI_RECEIVE.PRC_NOMINATION_NOMINEE";
    String sortColName = "DATE_SUBMITTED";
    String[] sortColNames = { "DATE_SUBMITTED",
                              "",
                              "NOMINEE",
                              "RECVR_COUNTRY_NAME",
                              "RECVR_NODE_NAME",
                              "TEAM_NAME",
                              "NOMINATOR",
                              "GIVER_COUNTRY_NAME",
                              "GIVER_NODE_NAME",
                              "PROMOTION_NAME",
                              "CLAIM_ITEM_STATUS",
                              "POINTS_AWARD_AMT",
                              "CASH_AWARD_AMT",
                              "OTHER_AWARD_DESC",
                              "OTHER_AWARD_AMT" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcNominationNomineeReport procedure = new CallPrcNominationNomineeReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getNominationTopNomineeChartResult( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_NOMI_RECEIVE.PRC_TOP_NOMINEE";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcNominationTopNomineeReport procedure = new CallPrcNominationTopNomineeReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // NOMINATION EXTRACT REPORT
  // =======================================

  @Override
  public Map getNominationExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcNominationDetailExtract procedure = new CallPrcNominationDetailExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getNominationSummaryExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcNominationSummaryExtract procedure = new CallPrcNominationSummaryExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // NOMINATION AGING REPORT
  // =======================================

  @Override
  public Map<String, Object> getNominationAgingTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "pkg_query_nomi_aging.prc_summary_aging";
    String sortColName = "date_submitted";
    String[] sortColNames = { "promotion_name",
                              "time_period_name",
                              "r1_pending_cnt",
                              "r1_avg_pending_days",
                              "r1_approved_cnt",
                              "r1_winner_cnt",
                              "r1_non_winner_cnt",
                              "r1_more_info_cnt",
                              "r2_pending_cnt",
                              "r2_avg_pending_days",
                              "r2_approved_cnt",
                              "r2_winner_cnt",
                              "r2_non_winner_cnt",
                              "r2_more_info_cnt",
                              "r3_pending_cnt",
                              "r3_avg_pending_days",
                              "r3_approved_cnt",
                              "r3_winner_cnt",
                              "r3_non_winner_cnt",
                              "r3_more_info_cnt",
                              "r4_pending_cnt",
                              "r4_approved_cnt",
                              "r4_avg_pending_days",
                              "r4_winner_cnt",
                              "r4_non_winner_cnt",
                              "r4_more_info_cnt",
                              "r5_pending_cnt",
                              "r5_avg_pending_days",
                              "r5_winner_cnt",
                              "r5_non_winner_cnt",
                              "r5_more_info_cnt" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcNominationAgingReport procedure = new CallPrcNominationAgingReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters, STORED_PROC_NAME );
  }

  @Override
  public Map<String, Object> getNominationAgingBarchartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "pkg_query_nomi_aging.prc_nominations_by_status";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcNominationAgingReport procedure = new CallPrcNominationAgingReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters, STORED_PROC_NAME );
  }

  @Override
  public Map getNominationAgingReportDetailExtract( Map<String, Object> reportParameters )
  {
    CallPrcNominationAgingDetailExtract procedure = new CallPrcNominationAgingDetailExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getNominationAgingReportSummaryExtract( Map<String, Object> reportParameters )
  {
    CallPrcNominationAgingSummaryExtract procedure = new CallPrcNominationAgingSummaryExtract( dataSource );
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

  @Override
  public Map<String, Object> getTotalNominationsChartResults( Map<String, Object> reportParameters )
  {
    return null;
  }
}
