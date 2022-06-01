/**
 * 
 */

package com.biperf.core.dao.budget.hibernate;

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

/**
 * @author poddutur
 *
 */
public class CallPrBudgetLoadImport extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_BUDGET_VERIFY_IMPORT.P_BUDGET_LOAD";

  public CallPrBudgetLoadImport( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_import_file_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    compile();

  }

  public Map<String, Object> executeProcedure( Long importFileId, Long userId )
  {

    HashMap<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_import_file_id", importFileId );
    inParams.put( "p_user_id", userId );

    Map<String, Object> outParams = execute( inParams );
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
