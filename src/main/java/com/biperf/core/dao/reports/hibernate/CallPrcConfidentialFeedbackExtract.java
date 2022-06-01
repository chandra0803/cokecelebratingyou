
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

public class CallPrcConfidentialFeedbackExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_EXTRACTS.PRC_CONFIDENTIAL_FEEDB_EXTRACT";

  public CallPrcConfidentialFeedbackExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_timeframe", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new DataMapper() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    HashMap inParams = new HashMap();

    inParams.put( "p_in_timeframe", reportParameters.get( "selectMonth" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_file_name", reportParameters.get( "internalFilename" ) );
    inParams.put( "p_header", reportParameters.get( "csHeaders" ) );
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