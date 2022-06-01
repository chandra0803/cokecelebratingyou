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

import com.biperf.core.value.plateauawards.PlateauAwardItemSelectionReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcPlateauAwardItemSelectionReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_PLATEAU_ITEM_SELECT.PRC_GETITEMSELECTIONSUMMARY";

  /* Charts */
  private static final String PROC2 = "PKG_QUERY_PLATEAU_ITEM_SELECT.PRC_GETPLATEAUAWARDSELECTION";
  private static final String PROC3 = "PKG_QUERY_PLATEAU_ITEM_SELECT.PRC_GETTOPREDEEMEDAWARDS";

  public CallPrcPlateauAwardItemSelectionReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_awardLevel", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcPlateauAwardItemSelectionReport.PlateauAwardsItemSelectionSummaryResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcPlateauAwardItemSelectionReport.ItemSelectionSummaryResultsTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcPlateauAwardItemSelectionReport.PlateauAwardSelectionChartResultsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcPlateauAwardItemSelectionReport.TopRedeemedAwardsChartResultsMapper() ) );
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
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class PlateauAwardsItemSelectionSummaryResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<PlateauAwardItemSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PlateauAwardItemSelectionReportValue> reportData = new ArrayList<PlateauAwardItemSelectionReportValue>();

      while ( rs.next() )
      {
        PlateauAwardItemSelectionReportValue reportValue = new PlateauAwardItemSelectionReportValue();
        reportValue.setLevelName( rs.getString( "level_name" ) );
        reportValue.setItemName( rs.getString( "item_name" ) );
        reportValue.setItemNum( rs.getString( "inv_item_id" ) );
        reportValue.setSelectionCnt( rs.getLong( "selection_cnt" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class ItemSelectionSummaryResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public PlateauAwardItemSelectionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      PlateauAwardItemSelectionReportValue reportValue = new PlateauAwardItemSelectionReportValue();
      while ( rs.next() )
      {
        reportValue.setSelectionCnt( rs.getLong( "selection_cnt" ) );
      }
      return reportValue;
    }
  }

  private class PlateauAwardSelectionChartResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<PlateauAwardItemSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PlateauAwardItemSelectionReportValue> reportData = new ArrayList<PlateauAwardItemSelectionReportValue>();

      while ( rs.next() )
      {
        PlateauAwardItemSelectionReportValue reportValue = new PlateauAwardItemSelectionReportValue();
        reportValue.setItemName( rs.getString( "item_name" ) );
        reportValue.setSelectionPct( rs.getBigDecimal( "item_percentage" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class TopRedeemedAwardsChartResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<PlateauAwardItemSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PlateauAwardItemSelectionReportValue> reportData = new ArrayList<PlateauAwardItemSelectionReportValue>();

      while ( rs.next() )
      {
        PlateauAwardItemSelectionReportValue reportValue = new PlateauAwardItemSelectionReportValue();
        reportValue.setItemName( rs.getString( "item_name" ) );
        reportValue.setSelectionCnt( rs.getLong( "item_count" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
