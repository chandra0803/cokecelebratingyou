
package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BadgeReportsDAO;
import com.biperf.core.dao.reports.BaseReportsDAO;

/**
 * 
 * BadgeReportsDAOImpl.
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
 * <td>kandhi</td>
 * <td>Nov 29, 2012</td>
 * <td>1.0</td>
 * <td>Initial version</td>
 * </tr>
 * </table>
 * 
 */
public class BadgeReportsDAOImpl extends BaseReportsDAO implements BadgeReportsDAO
{
  private static final Log log = LogFactory.getLog( BadgeReportsDAOImpl.class );

  private DataSource dataSource;

  @Override
  public Map<String, Object> getBadgeActivityByOrgSummaryTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETACTIVITYBYORGSUMMARY";
    String sortColName = "NAME";
    String[] sortColNames = { "NAME", "", "BADGES_EARNED" };

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
    CallPrcBadgeActivityByOrgReport procedure = new CallPrcBadgeActivityByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getBadgeActivityTeamLevelTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETACTIVITYTEAMLEVEL";
    String sortColName = "PAX_NAME";
    String[] sortColNames = { "PAX_NAME", "", "COUNTRY", "BADGES_EARNED" };

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
    CallPrcBadgeActivityByOrgReport procedure = new CallPrcBadgeActivityByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getBadgeActivityParticipantLevelTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETPAXLEVEL";
    String sortColName = "EARNED_DATE";
    String[] sortColNames = { "EARNED_DATE", "", "PARTICIPANT_NAME", "ORG_NAME", "PROMOTION_NAME", "BADGE_NAME" };

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
    CallPrcBadgeActivityByOrgReport procedure = new CallPrcBadgeActivityByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getBadgesEarnedBarChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETBADGESEARNEDBARCHART";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bindVariables: " + reportParameters );
    CallPrcBadgeActivityByOrgReport procedure = new CallPrcBadgeActivityByOrgReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getBadgeActivityExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcBadgeExtract procedure = new CallPrcBadgeExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}
