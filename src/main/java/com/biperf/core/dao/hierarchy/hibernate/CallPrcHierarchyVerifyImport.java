
package com.biperf.core.dao.hierarchy.hibernate;

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

public class CallPrcHierarchyVerifyImport extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PKG_HIERARCHY_VERIFY_IMPORT.P_HIERARCHY_VERIFY_LOAD";

  public CallPrcHierarchyVerifyImport( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_import_file_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_load_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_hierarchy_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_user_data", OracleTypes.CURSOR, new CallPrcHierarchyVerifyImport.DataMapper() ) );

    compile();

  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public Map executeProcedure( Long importFileId, String loadType, Long hierarchyId, Long userId )
  {

    HashMap inParams = new HashMap();
    inParams.put( "p_import_file_id", importFileId );
    inParams.put( "p_load_type", loadType );
    inParams.put( "p_hierarchy_id", hierarchyId );
    inParams.put( "p_user_id", userId );

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
