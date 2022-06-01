
package com.biperf.core.dao.ssi.hibernate;

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

public class CallPrcSSIAllContestsVerifyImport extends StoredProcedure
{
  private static final String STORED_PROC_NAME_OBJ = "pkg_ssi_objective_load .p_ssi_objective_verify_import";
  private static final String STORED_PROC_NAME_DTGT = "pkg_ssi_dtgt_load. p_ssi_dtgt_verify_import";
  private static final String STORED_PROC_NAME_SIU = "pkg_ssi_step_it_up_load .p_ssi_step_it_up_verify_import";
  private static final String STORED_PROC_NAME_SR = "pkg_ssi_stack_rank_load .p_ssi_stack_rank_verify_import";
  private static final String STORED_PROC_NAME_ATN = "pkg_ssi_atn_load. p_ssi_atn_verify_import";

  public CallPrcSSIAllContestsVerifyImport( DataSource dataSource, String STORED_PROC_NAME )
  {

    super( dataSource, STORED_PROC_NAME );

    declareParameter( new SqlParameter( "p_import_file_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_load_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_ssi_contest_id", Types.NUMERIC ) );
    switch ( STORED_PROC_NAME )
    {
      case STORED_PROC_NAME_OBJ:
        declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
        break;
      case STORED_PROC_NAME_DTGT:
        declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
        break;
      case STORED_PROC_NAME_SIU:
        declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
        break;
      case STORED_PROC_NAME_SR:
        declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
        break;
      case STORED_PROC_NAME_ATN:
        declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
        break;
      default:
        break;
    }

    compile();

  }

  public Map<String, Object> executeProcedure( Long importFileId, String loadType, Long contestId )
  {
    HashMap<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_import_file_id", importFileId );
    inParams.put( "p_load_type", loadType );
    inParams.put( "p_ssi_contest_id", contestId );

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
