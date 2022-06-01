
package com.biperf.core.dao.nomination.hibernate;

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

public class CallPrcNominationApproverVerifyImport extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "pkg_nom_approver_verify_import.p_nom_approver_verify_load";

  public CallPrcNominationApproverVerifyImport( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    declareParameter( new SqlParameter( "p_import_file_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_load_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    compile();

  }

  public Map<String, Object> executeProcedure( Long importFileId, String loadType, Long userId, Long promtionId )
  {
    HashMap<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_import_file_id", importFileId );
    inParams.put( "p_load_type", loadType );
    inParams.put( "p_promotion_id", promtionId );
    inParams.put( "p_user_id", userId );
    return execute( inParams );
  }

  @SuppressWarnings( { "unused", "rawtypes" } )
  private class DataMapper implements RowMapper
  {
    public Object mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }

}
