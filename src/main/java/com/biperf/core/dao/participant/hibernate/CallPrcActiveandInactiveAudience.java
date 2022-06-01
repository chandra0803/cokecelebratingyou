
package com.biperf.core.dao.participant.hibernate;

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

public class CallPrcActiveandInactiveAudience extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_ref_criteria_aud_inactive";

  public CallPrcActiveandInactiveAudience( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "pi_audience_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "pi_resend_flag", Types.NUMERIC ) );

    declareParameter( new SqlOutParameter( "p_out_refcursor", OracleTypes.CURSOR, new DataMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inputParameters )
  {
    HashMap<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "pi_audience_id", inputParameters.get( "audienceId" ) );
    inParams.put( "pi_resend_flag", inputParameters.get( "resendFlag" ) );

    Map<String, Object> outParams = execute( inParams );

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
