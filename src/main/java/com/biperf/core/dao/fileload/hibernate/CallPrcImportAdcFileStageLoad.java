
package com.biperf.core.dao.fileload.hibernate;

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

public class CallPrcImportAdcFileStageLoad extends StoredProcedure
{

  public CallPrcImportAdcFileStageLoad( DataSource ds, String prcName )
  {
    super( ds, prcName );

    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_file_name", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    // declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new
    // DataMapper() ) );

    compile();
  }

  public Map executeProcedure( String fileName )
  {
    HashMap inParams = new HashMap();
    inParams.put( "p_file_name", fileName );

    Map outParams = execute( inParams );

    return outParams;
  }

  /**
   * DataMapper is an Inner class which implements the RowMapper.
   */
  private class DataMapper implements RowMapper
  {
    public Object mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }

}
