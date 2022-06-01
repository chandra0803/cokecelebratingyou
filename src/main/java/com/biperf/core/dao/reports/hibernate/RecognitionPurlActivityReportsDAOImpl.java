
package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.RecognitionPurlActivityReportsDAO;

/**
 * RecognitionPurlActivityReportsDAOImpl
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
 * <td>poddutur</td>
 * <td>Aug 31, 2012</td>
 * <td>1.0</td>
 * <td>Intital version</td>
 * </tr>
 * </table>
 * 
 * @author poddutur
 */
public class RecognitionPurlActivityReportsDAOImpl extends BaseReportsDAO implements RecognitionPurlActivityReportsDAO
{

  private static final Log log = LogFactory.getLog( RecognitionPurlActivityReportsDAOImpl.class );

  private DataSource dataSource;

  @Override
  public Map<String, Object> getSummaryTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETSUMMARYTABULARRESULTS";
    String sortColName = "node_name";
    String[] sortColNames = { "node_name", "", "recipient_count", "contributors_invited", "contributed", "percent_contributed", "contributions_posted" };

    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bindVariables: " + reportParameters );
    CallPrcRecognitionPurlActivityReport procedure = new CallPrcRecognitionPurlActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  public Map<String, Object> getParticipantTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETPAXTABULARRESULTS";
    String sortColName = "recipient_name";
    String[] sortColNames = { "recipient_name",
                              "promotion_name",
                              "recipient_country",
                              "manager_name",
                              "award_date",
                              "purl_status",
                              "award",
                              "contributors_invited",
                              "actual_contributors",
                              "contributions_percentage",
                              "contributions_posted" };

    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bindVariables: " + reportParameters );
    CallPrcRecognitionPurlActivityReport procedure = new CallPrcRecognitionPurlActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getPurlActivityChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETACTIVITYCHARTRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bindVariables: " + reportParameters );
    CallPrcRecognitionPurlActivityReport procedure = new CallPrcRecognitionPurlActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getOverallContributorsChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETCONTRIBCHARTRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bindVariables: " + reportParameters );
    CallPrcRecognitionPurlActivityReport procedure = new CallPrcRecognitionPurlActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getPurlReceipientsChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_RECOG_PURL_ACTIVITY.PRC_GETRECIPCHARTRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bindVariables: " + reportParameters );
    CallPrcRecognitionPurlActivityReport procedure = new CallPrcRecognitionPurlActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcPurlActivityExtract procedure = new CallPrcPurlActivityExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}
