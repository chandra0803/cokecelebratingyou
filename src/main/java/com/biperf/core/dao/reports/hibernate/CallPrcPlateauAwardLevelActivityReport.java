/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.plateauawards.PlateauAwardLevelActivityReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcPlateauAwardLevelActivityReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETAWARDLEVELSUMMARY";
  private static final String PROC2 = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETAWARDLEVELTEAMLEVEL";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETPLATEAUAWARD";
  private static final String PROC4 = "PKG_QUERY_PLATEAU_LEVELS.PRC_GETPERCENTAGEPLATEAUAWARD";

  public CallPrcPlateauAwardLevelActivityReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_awardLevel", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_awardStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcPlateauAwardLevelActivityReport.AwardLevelActivitySummaryResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcPlateauAwardLevelActivityReport.AwardLevelActivitySummaryResultsTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcPlateauAwardLevelActivityReport.AwardLevelActivityTeamLevelResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcPlateauAwardLevelActivityReport.AwardLevelActivityTeamLevelResultsTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcPlateauAwardLevelActivityReport.PlateauAwardActivityChartResultsMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcPlateauAwardLevelActivityReport.PercentagePlateauAwardActivityChartResultsMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_awardLevel", reportParameters.get( "awardLevel" ) );
    inParams.put( "p_in_awardStatus", reportParameters.get( "awardStatus" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    if ( ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class AwardLevelActivitySummaryResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<PlateauAwardLevelActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PlateauAwardLevelActivityReportValue> reportData = new ArrayList<PlateauAwardLevelActivityReportValue>();

      while ( rs.next() )
      {
        PlateauAwardLevelActivityReportValue reportValue = new PlateauAwardLevelActivityReportValue();
        reportValue.setOrgName( rs.getString( "node_name" ) );
        reportValue.setIssuedCnt( rs.getLong( "codes_issued" ) );
        reportValue.setRedeemedCnt( rs.getLong( "codes_redeemed" ) );
        reportValue.setRedeemedPct( rs.getBigDecimal( "perc_redeemed" ) );
        reportValue.setNotRedeemedCnt( rs.getLong( "codes_unredeemed" ) );
        reportValue.setNotRedeemedPct( rs.getBigDecimal( "perc_unredeemed" ) );
        reportValue.setExpiredCnt( rs.getLong( "codes_expired" ) );
        reportValue.setExpiredPct( rs.getBigDecimal( "perc_expired" ) );
        reportValue.setNodeId( rs.getLong( "node_id" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class AwardLevelActivitySummaryResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public PlateauAwardLevelActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      PlateauAwardLevelActivityReportValue reportValue = new PlateauAwardLevelActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setIssuedCnt( rs.getLong( "codes_issued" ) );
        reportValue.setRedeemedCnt( rs.getLong( "codes_redeemed" ) );
        reportValue.setRedeemedPct( rs.getBigDecimal( "perc_redeemed" ) );
        reportValue.setNotRedeemedCnt( rs.getLong( "codes_unredeemed" ) );
        reportValue.setNotRedeemedPct( rs.getBigDecimal( "perc_unredeemed" ) );
        reportValue.setExpiredCnt( rs.getLong( "codes_expired" ) );
        reportValue.setExpiredPct( rs.getBigDecimal( "perc_expired" ) );
      }
      return reportValue;
    }
  }

  private class AwardLevelActivityTeamLevelResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<PlateauAwardLevelActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PlateauAwardLevelActivityReportValue> reportData = new ArrayList<PlateauAwardLevelActivityReportValue>();

      while ( rs.next() )
      {
        PlateauAwardLevelActivityReportValue reportValue = new PlateauAwardLevelActivityReportValue();
        reportValue.setLevelName( rs.getString( "level_name" ) );
        reportValue.setIssuedCnt( rs.getLong( "Codes_Issued" ) );
        reportValue.setRedeemedCnt( rs.getLong( "Codes_Redeemed" ) );
        reportValue.setRedeemedPct( rs.getBigDecimal( "perc_redeemed" ) );
        reportValue.setNotRedeemedCnt( rs.getLong( "Codes_Unredeemed" ) );
        reportValue.setNotRedeemedPct( rs.getBigDecimal( "perc_Unredeemed" ) );
        reportValue.setExpiredCnt( rs.getLong( "Codes_Expired" ) );
        reportValue.setExpiredPct( rs.getBigDecimal( "perc_expired" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class AwardLevelActivityTeamLevelResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public PlateauAwardLevelActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      PlateauAwardLevelActivityReportValue reportValue = new PlateauAwardLevelActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setIssuedCnt( rs.getLong( "Codes_Issued" ) );
        reportValue.setRedeemedCnt( rs.getLong( "Codes_Redeemed" ) );
        reportValue.setRedeemedPct( rs.getBigDecimal( "perc_redeemed" ) );
        reportValue.setNotRedeemedCnt( rs.getLong( "Codes_Unredeemed" ) );
        reportValue.setNotRedeemedPct( rs.getBigDecimal( "perc_Unredeemed" ) );
        reportValue.setExpiredCnt( rs.getLong( "Codes_Expired" ) );
        reportValue.setExpiredPct( rs.getBigDecimal( "perc_expired" ) );
      }
      return reportValue;
    }
  }

  private class PlateauAwardActivityChartResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<PlateauAwardLevelActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PlateauAwardLevelActivityReportValue> reportData = new ArrayList<PlateauAwardLevelActivityReportValue>();

      while ( rs.next() )
      {
        PlateauAwardLevelActivityReportValue reportValue = new PlateauAwardLevelActivityReportValue();
        reportValue.setLevelName( rs.getString( "level_name" ) );
        reportValue.setRedeemedCnt( rs.getLong( "codes_redeemed" ) );
        reportValue.setNotRedeemedCnt( rs.getLong( "Codes_Unredeemed" ) );
        reportValue.setExpiredCnt( rs.getLong( "Codes_Expired" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class PercentagePlateauAwardActivityChartResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<PlateauAwardLevelActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PlateauAwardLevelActivityReportValue> reportData = new ArrayList<PlateauAwardLevelActivityReportValue>();

      while ( rs.next() )
      {
        PlateauAwardLevelActivityReportValue reportValue = new PlateauAwardLevelActivityReportValue();
        reportValue.setLevelName( rs.getString( "level_name" ) );
        reportValue.setRedeemedPct( rs.getBigDecimal( "perc_redeemed" ) );
        reportValue.setNotRedeemedPct( rs.getBigDecimal( "perc_Unredeemed" ) );
        reportValue.setExpiredPct( rs.getBigDecimal( "perc_expired" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
