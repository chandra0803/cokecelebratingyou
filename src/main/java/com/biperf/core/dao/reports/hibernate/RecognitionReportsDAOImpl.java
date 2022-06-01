/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/RecognitionReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.RecognitionReportsDAO;

/**
 * 
 * RecognitionReportsDAOImpl.
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
 * </table>
 * 
 */
public class RecognitionReportsDAOImpl extends BaseReportsDAO implements RecognitionReportsDAO
{
  private static final Log log = LogFactory.getLog( RecognitionReportsDAOImpl.class );

  private DataSource dataSource;

  /* RECOGNITION GIVEN - PARTICIPATION BY ORGANIZATION */

  @Override
  public Map<String, Object> getRecognitionGivenPieResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGGIVENPIERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenByTimeBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGGIVENTIMERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGPOINTSBARRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenByTimeBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGPOINTSTIMERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenParticipationRatePieResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGRATEPIERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenParticipationRateBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGRATEBARRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionSummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_BYORGSUMMARYRESULTS";
    String sortColName = "org_name";
    String[] sortColNames = { "org_name", "", "eligible_cnt", "actual_cnt", "per_gave_receive", "total_recognition_cnt", "points", "plateau_earned", "sweepstakes_won" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenDetailResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_GIVEN.PRC_GET_RECOGDETAILRESULTS";
    String sortColName = "giver_name";
    String[] sortColNames = { "giver_name", "recognitions_cnt", "recognition_points", "plateau_earned_count", "sweepstakes_won_count" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionGivenByOrgReport procedure = new CallPrcRecognitionGivenByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /* RECOGNITION RECEIVED - PARTICIPATION BY ORGANIZATION */

  @Override
  public Map<String, Object> getRecognitionReceivedPieResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRECVDPIERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedByTimeBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRECVDTIMERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedParticipationRateBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRATEBARRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedParticipationRatePieResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGRATEPIERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsReceivedBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGPOINTSBARRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsByTimeReceivedBarResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGPOINTSTIMERESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedSummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_BYORGSUMMARYRESULTS";
    String sortColName = "org_name";
    String[] sortColNames = { "org_name", "", "eligible_cnt", "actual_cnt", "per_gave_receive", "total_recognition_cnt", "points", "plateau_earned", "sweepstakes_won" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedDetailResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECEIVED.PRC_GET_RECOGDETAILRESULTS";
    String sortColName = "receiver_name";
    String[] sortColNames = { "receiver_name", "recognitions_cnt", "recognition_points", "plateau_earned_count", "sweepstakes_won_count" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionReceivedByOrgReport procedure = new CallPrcRecognitionReceivedByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /* RECOGNITION GIVEN - LIST OF GIVERS */

  @Override
  public Map<String, Object> getRecognitionsGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETRECOGSGIVENBYPAX";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETPOINTSGIVENBYPAX";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsGivenByPromotionResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETRECOGSGIVENBYPROMO";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenByPromotionResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETPOINTSGIVENBYPROMO";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsGivenMetricsResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETMETRICS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsGivenParticipationRateByPaxResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETPARTICIPATIONRATEBYPAX";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionSummaryGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETSUMMARYGIVENBYPAX";
    String sortColName = "GIVER_NAME";
    String[] sortColNames = { "giver_name", "", "", "giver_country", "giver_node", "giver_department", "giver_job_position", "recog_count", "recog_points", "plateau_earned_count" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionActivityGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_LIST_OF_GIVERS.PRC_GETACTIVITYGIVENBYPAX";
    String sortColName = "DATE_APPROVED";
    String[] sortColNames = { "date_approved", "", "giver_name", "giver_country", "giver_node_name", "department", "job_position", "promotion_name", "", "recognition_points", "plateau_earned_count" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionGivenByPaxReport procedure = new CallPrcRecognitionGivenByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  /* RECOGNITION RECEIVED - LIST OF RECIPIENTS */

  @Override
  public Map<String, Object> getRecognitionsReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDBYPAX";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETPOINTSRECEIVEDBYPAX";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedByPromotionResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDBYPROMO";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsReceivedByPromotionResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETPOINTSRECEIVEDBYPROMO";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedMetricsResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDMETRICS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedParticipationRateByPaxResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETPARTICIPATIONRATEBYPAX";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedScatterPlotResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETRECOGSRECEIVEDSCATTER";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionSummaryReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETSUMMARYRECEIVEDBYPAX";
    String sortColName = "RECEIVER_NAME";
    String[] sortColNames = { "RECEIVER_NAME", "", "RECEIVER_COUNTRY", "RECEIVER_NODE", "RECVR_DEPARTMENT", "RECVR_JOB_POSITION", "RECOG_COUNT", "RECOG_POINTS", "PLATEAU_EARNED_COUNT" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionActivityReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_RECIPIENTS.PRC_GETACTIVITYRECEIVEDBYPAX";
    String sortColName = "RECEIVED_DATE";
    String[] sortColNames = { "DATE_APPROVED", "", "RECEIVER_NAME", "RECEIVER_COUNTRY", "RECVR_NODE_NAME", "DEPARTMENT", "JOB_POSITION", "SENDER_NAME", "PROMOTION_NAME", "RECOGNITION_POINTS", "PLATEAU_EARNED_COUNT" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcRecognitionReceivedByPaxReport procedure = new CallPrcRecognitionReceivedByPaxReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getRecognitionGiverActivityReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcRecognitionGiverActivityExtract procedure = new CallPrcRecognitionGiverActivityExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getRecognitionReceiverActivityReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcRecognitionReceiverActivityExtract procedure = new CallPrcRecognitionReceiverActivityExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

}
