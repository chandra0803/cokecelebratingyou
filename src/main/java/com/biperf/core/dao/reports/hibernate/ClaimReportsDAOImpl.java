/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/ClaimReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.ClaimReportsDAO;

/**
 * 
 * ClaimReportsDAOImpl.
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
public class ClaimReportsDAOImpl extends BaseReportsDAO implements ClaimReportsDAO
{
  private DataSource dataSource;

  @SuppressWarnings( "unused" )
  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  // =======================================
  // CLAIM BY ORG REPORT
  // =======================================

  @Override
  public Map<String, Object> getClaimByOrgTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "submitter";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "ORG_NAME";
          break;
        case 3:
          sortColName = "elig_sub";
          break;
        case 4:
          sortColName = "act_sub";
          break;
        case 5:
          sortColName = "PARTICIPATION_RATE";
          break;
        case 6:
          sortColName = "TOTAL_CLAIMS";
          break;
        case 7:
          sortColName = "CLAIMS_OPEN";
          break;
        case 8:
          sortColName = "CLAIMS_CLOSED";
          break;
        case 9:
          sortColName = "ITEMS_APPROVED";
          break;
        case 10:
          sortColName = "ITEMS_DENIED";
          break;
        case 11:
          sortColName = "ITEMS_HOLD";
          break;
        case 12:
          sortColName = "POINTS";
          break;
        case 13:
          sortColName = "SWEEPSTAKES_WON";
          break;
        default:
          sortColName = "ORG_NAME";
          break;
      }
    }
    String procName = "pkg_query_claims_activity.prc_getByOrgTabRes";
    reportParameters.put( "sortColName", sortColName );
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgSubmissionStatusChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByOrgStatus";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgMonthlyChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByOrgMonthly";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgParticipationRateChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByOrgPartRate";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgParticipationLevelChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByOrgPartLevel";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgItemStatusChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByOrgItemStatus";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByOrgTotalsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByOrgTotals";
    return callProc( procName, reportParameters );
  }

  // =======================================
  // CLAIM BY PAX REPORT
  // =======================================

  @Override
  public Map<String, Object> getClaimByPaxTabularResults( Map<String, Object> reportParameters, boolean includeChildNodes )
  {
    String sortColName = "submitter";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "submitter";
          break;
        case 3:
          sortColName = "country";
          break;
        case 4:
          sortColName = "org_name";
          break;
        case 5:
          sortColName = "department";
          break;
        case 6:
          sortColName = "job_position";
          break;
        case 7:
          sortColName = "promotion";
          break;
        case 8:
          sortColName = "total_claims";
          break;
        case 9:
          sortColName = "open_claims";
          break;
        case 10:
          sortColName = "closed_claims";
          break;
        case 11:
          sortColName = "approved_items";
          break;
        case 12:
          sortColName = "denied_items";
          break;
        case 13:
          sortColName = "held_items";
          break;
        case 14:
          sortColName = "points";
          break;
        case 15:
          sortColName = "sweepstakes_won";
          break;
        default:
          sortColName = "submitter";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    reportParameters.put( "includeChildNodes", includeChildNodes );
    String procName = "pkg_query_claims_activity.prc_getByPaxTabRes";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByPaxClaimListTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "rcd.claim_number";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "rcd.claim_number";
          break;
        case 3:
          sortColName = "rcd.claim_status";
          break;
        case 4:
          sortColName = "rcd.date_submitted";
          break;
        case 5:
          sortColName = "rcd.node_name";
          break;
        case 6:
          sortColName = "rcd.promotion_name";
          break;
        case 7:
          sortColName = "rcd.award_amount";
          break;
        case 8:
          sortColName = "rcd.submitter_sweepstakes_won";
          break;
        default:
          sortColName = "rcd.claim_number";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_claims_activity.prc_getByPaxClaimListTabRes";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByPaxSubmittedClaims( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByPaxSubmittedClaims";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByPaxClaimStatusChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByPaxClaimStatus";
    return callProc( procName, reportParameters );
  }

  @Override
  public Map<String, Object> getClaimByPaxItemStatusChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_claims_activity.prc_getByPaxItemStatus";
    return callProc( procName, reportParameters );
  }

  // =======================================
  // CLAIM EXTRACT REPORT
  // =======================================
  @Override
  public Map getClaimExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcClaimsExtract procedure = new CallPrcClaimsExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getItemsClaimExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcItemsClaimExtract itemsClaimProc = new CallPrcItemsClaimExtract( dataSource );
    return itemsClaimProc.executeProcedure( reportParameters );
  }

  private Map<String, Object> callProc( String procName, Map<String, Object> reportParameters )
  {
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcClaimsReport procedure = new CallPrcClaimsReport( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }
}
