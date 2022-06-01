
package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import oracle.jdbc.OracleTypes;

public class CallPrcRecognitionReceiverActivityExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_EXTRACTS.PRC_RECOGNITION_RECVD_EXTRACT";

  public CallPrcRecognitionReceiverActivityExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_pax_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_is_team", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_paxid", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_is_behavior", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_promotion_Id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promo_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_position_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country_Ids", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_header", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcRecognitionReceiverActivityExtract.DataMapper() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_pax_status", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_is_team", ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() ? 0 : 1 );
    inParams.put( "p_in_paxid", "pax".equals( reportParameters.get( "exportLevel" ) ) ? reportParameters.get( "userId" ) : null );
    inParams.put( "p_in_is_behavior", reportParameters.get( "isBehavior" ) != null ? 1 : 0 );
    inParams.put( "p_in_promotion_Id", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_promo_status", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_position_type", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_department", reportParameters.get( "department" ) );
    inParams.put( "p_in_country_Ids", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_locale", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_file_name", reportParameters.get( "internalFilename" ) );
    inParams.put( "p_in_header", reportParameters.get( "csHeaders" ) );
    Map outParams = execute( inParams );
    return outParams;
  }

  /**
   * DataMapper is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class DataMapper implements RowMapper
  {

    public String mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }
}
