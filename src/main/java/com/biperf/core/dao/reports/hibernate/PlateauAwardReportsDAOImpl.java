
package com.biperf.core.dao.reports.hibernate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.PlateauAwardReportsDAO;
import com.biperf.core.value.plateauawards.PlateauAwardCodeIssuanceReportValue;

public class PlateauAwardReportsDAOImpl extends BaseReportsDAO implements PlateauAwardReportsDAO
{

  private DataSource dataSource;

  private static final Log log = LogFactory.getLog( PlateauAwardReportsDAOImpl.class );

  // ***************************************//
  // -------- AWARD LEVEL ACTIVITY -------- //
  // ***************************************//

  @Override
  public Map<String, Object> getAwardLevelActivitySummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETAWARDLEVELSUMMARY";
    String sortColName = "node_name";
    String[] sortColNames = { "node_name", "", "codes_issued", "codes_redeemed", "perc_redeemed", "codes_unredeemed", "perc_unredeemed", "codes_expired", "perc_expired" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcPlateauAwardLevelActivityReport procedure = new CallPrcPlateauAwardLevelActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardLevelActivityTeamLevelResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETAWARDLEVELTEAMLEVEL";
    String sortColName = "level_name";
    String[] sortColNames = { "level_name", "Codes_Issued", "Codes_Redeemed", "perc_redeemed", "Codes_Unredeemed", "perc_Unredeemed", "Codes_Expired", "perc_expired" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcPlateauAwardLevelActivityReport procedure = new CallPrcPlateauAwardLevelActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getPlateauAwardActivityChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETPLATEAUAWARD";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcPlateauAwardLevelActivityReport procedure = new CallPrcPlateauAwardLevelActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getPercentagePlateauAwardActivityChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETPERCENTAGEPLATEAUAWARD";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcPlateauAwardLevelActivityReport procedure = new CallPrcPlateauAwardLevelActivityReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // ***************************************//
  // -------- AWARD ITEM SELECTION -------- //
  // ***************************************//

  @Override
  public Map<String, Object> getItemSelectionSummaryResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_PLATEAU_ITEM_SELECT.PRC_GETITEMSELECTIONSUMMARY";
    String sortColName = "level_name";
    String[] sortColNames = { "level_name", "item_name", "inv_item_id", "selection_cnt" };
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      sortColName = sortColNames[sortedOn - 1];
    }
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    CallPrcPlateauAwardItemSelectionReport procedure = new CallPrcPlateauAwardItemSelectionReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getPlateauAwardSelectionChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_PLATEAU_ITEM_SELECT.PRC_GETPLATEAUAWARDSELECTION";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcPlateauAwardItemSelectionReport procedure = new CallPrcPlateauAwardItemSelectionReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getTopRedeemedAwardsChartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_PLATEAU_ITEM_SELECT.PRC_GETTOPREDEEMEDAWARDS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcPlateauAwardItemSelectionReport procedure = new CallPrcPlateauAwardItemSelectionReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  // **************************************//
  // -------- AWARD CODE ISSUANCE --------
  // THIS REPORT IS SCRAPPED FOR NOW. THIS IS NOT FULLY IMPLEMENTED//
  // **************************************//

  @Override
  public List<PlateauAwardCodeIssuanceReportValue> getCodeIssuanceSummaryResults( Map<String, Object> reportParameters )
  {
    String sortColName = "report_date";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "report_date";
          break;
        case 2:
          sortColName = "reddemed_cnt";
          break;
        case 3:
          sortColName = "not_reddemed_cnt";
          break;
        case 4:
          sortColName = "expired_cnt";
          break;
        default:
          sortColName = "report_date";
          break;
      }
    }

    String sql = buildCodeIssuanceSummaryResultsQuery( reportParameters, sortColName );
    log.debug( " plateau awards item selection report summary tabular results sql: " + sql );
    log.debug( " bind variables: " + reportParameters );

    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new PlateauAwardsCodeIssuanceSummaryResultsMapper() );
    populateQueryParameters( query, reportParameters );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class PlateauAwardsCodeIssuanceSummaryResultsMapper extends BaseReportsResultTransformer
  {
    @Override
    public PlateauAwardCodeIssuanceReportValue transformTuple( Object[] tuple, String[] aliases )
    {
      PlateauAwardCodeIssuanceReportValue reportValue = new PlateauAwardCodeIssuanceReportValue();
      reportValue.setReportDate( extractDate( tuple[1] ) );
      reportValue.setRedeemedCnt( extractLong( tuple[2] ) );
      reportValue.setNotRedeemedCnt( extractLong( tuple[3] ) );
      reportValue.setExpiredCnt( extractLong( tuple[4] ) );
      return reportValue;
    }
  }

  private String buildCodeIssuanceSummaryResultsQuery( Map<String, Object> reportParameters, String sortColName )
  {
    return null;
  }

  @Override
  public int getCodeIssuanceSummaryResultsSize( Map<String, Object> reportParameters )
  {
    String sql = buildCodeIssuanceSummaryResultsSizeQuery();
    log.debug( " plateau awards code issuance report summary tabular results size sql: " + sql );
    log.debug( " bindVariables: " + reportParameters );
    Query query = getSession().createSQLQuery( sql );
    populateQueryParameters( query, reportParameters );
    return ( (BigDecimal)query.uniqueResult() ).intValue();
  }

  private String buildCodeIssuanceSummaryResultsSizeQuery()
  {
    return null;
  }

  @Override
  public PlateauAwardCodeIssuanceReportValue getCodeIssuanceSummaryResultsTotals( Map<String, Object> reportParameters )
  {
    String sql = buildCodeIssuanceSummaryResultsTotalsQuery( reportParameters );
    log.debug( " plateau awards code issuance report summary tabular results totals sql: " + sql );
    log.debug( " bind variables: " + reportParameters );
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new CodeIssuanceSummaryResultsTotalsMapper() );
    populateQueryParameters( query, reportParameters );
    return (PlateauAwardCodeIssuanceReportValue)query.uniqueResult();
  }

  @SuppressWarnings( "serial" )
  private class CodeIssuanceSummaryResultsTotalsMapper extends BaseReportsResultTransformer
  {
    @Override
    public PlateauAwardCodeIssuanceReportValue transformTuple( Object[] tuple, String[] aliases )
    {
      PlateauAwardCodeIssuanceReportValue reportValue = new PlateauAwardCodeIssuanceReportValue();
      reportValue.setRedeemedCnt( extractLong( tuple[0] ) );
      reportValue.setNotRedeemedCnt( extractLong( tuple[1] ) );
      reportValue.setExpiredCnt( extractLong( tuple[2] ) );
      return reportValue;
    }
  }

  private String buildCodeIssuanceSummaryResultsTotalsQuery( Map<String, Object> reportParameters )
  {
    return null;
  }

  @Override
  public List<PlateauAwardCodeIssuanceReportValue> getAwardCodeStatusByPercentageChartResults( Map<String, Object> reportParameters )
  {
    String sql = buildAwardCodeStatusChartResultsQuery();
    log.debug( " plateau awards code issuance report award code status by percentage chart results sql: " + sql );
    log.debug( " bind variables: " + reportParameters );
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new AwardCodeStatusByCountChartResultsMapper() );
    populateQueryParameters( query, reportParameters );
    return query.list();
  }

  @Override
  public List<PlateauAwardCodeIssuanceReportValue> getAwardCodeStatusByCountChartResults( Map<String, Object> reportParameters )
  {
    String sql = buildAwardCodeStatusChartResultsQuery();
    log.debug( " plateau awards code issuance report award code status by count chart results sql: " + sql );
    log.debug( " bind variables: " + reportParameters );
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new AwardCodeStatusByCountChartResultsMapper() );
    populateQueryParameters( query, reportParameters );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class AwardCodeStatusByCountChartResultsMapper extends BaseReportsResultTransformer
  {
    @Override
    public PlateauAwardCodeIssuanceReportValue transformTuple( Object[] tuple, String[] aliases )
    {
      PlateauAwardCodeIssuanceReportValue reportValue = new PlateauAwardCodeIssuanceReportValue();
      reportValue.setRedeemedCnt( extractLong( tuple[0] ) );
      reportValue.setNotRedeemedCnt( extractLong( tuple[1] ) );
      reportValue.setExpiredCnt( extractLong( tuple[2] ) );
      return reportValue;
    }
  }

  public Map getParticipantAwardLevelExtract( Map<String, Object> reportParameters )
  {
    CallPrcPlateauAwardsActivityExtract procedure = new CallPrcPlateauAwardsActivityExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  private String buildAwardCodeStatusChartResultsQuery()
  {
    return null;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}
