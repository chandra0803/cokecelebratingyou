
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

import oracle.jdbc.OracleTypes;

public class CallPrcInactiveBudgetsExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_INACTIVE_BUDGET_RD.P_INACTIVE_BUDGETS_EXTRACT";

  public CallPrcInactiveBudgetsExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_budget_master_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_budget_segment_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new DataMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> extractParameters )
  {
    HashMap<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_budget_master_id", extractParameters.get( "budgetMasterId" ) );
    inParams.put( "p_in_budget_segment_id", extractParameters.get( "budgetSegmentId" ) );

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
