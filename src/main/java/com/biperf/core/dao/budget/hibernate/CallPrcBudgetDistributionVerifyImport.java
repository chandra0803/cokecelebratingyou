
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

public class CallPrcBudgetDistributionVerifyImport extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "pkg_inactive_budget_rd.p_load_inactive_budgets";

  private static final String P_IN_IMPORT_FILE_ID = "p_in_import_file_id";
  private static final String P_IN_BUDGET_MASTER_ID = "p_in_budget_master_id";
  private static final String P_IN_BUDGET_SEGMENT_ID = "p_in_budget_segment_id";
  private static final String P_IN_LOAD_TYPE = "p_in_load_type";
  private static final String P_OUT_FILE_RECORDS_COUNT = "p_file_records_count";
  private static final String P_OUT_PROCESSED_RECORDS_COUNT = "p_processed_records_count";
  private static final String P_OUT_RETURNCODE = "p_out_returncode";

  public CallPrcBudgetDistributionVerifyImport( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( P_IN_IMPORT_FILE_ID, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_BUDGET_MASTER_ID, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_BUDGET_SEGMENT_ID, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_LOAD_TYPE, Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( P_OUT_FILE_RECORDS_COUNT, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_PROCESSED_RECORDS_COUNT, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURNCODE, Types.NUMERIC ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long importFileId, Long budgetMasterId, Long budgetSegmentId, String loadType )
  {

    HashMap<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_IMPORT_FILE_ID, importFileId );
    inParams.put( P_IN_BUDGET_MASTER_ID, budgetMasterId );
    inParams.put( P_IN_BUDGET_SEGMENT_ID, budgetSegmentId );
    inParams.put( P_IN_LOAD_TYPE, loadType );

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
