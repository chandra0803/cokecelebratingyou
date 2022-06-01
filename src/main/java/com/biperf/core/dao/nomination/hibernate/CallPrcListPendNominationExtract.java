
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

import oracle.jdbc.OracleTypes;

public class CallPrcListPendNominationExtract extends StoredProcedure
{
  /* Tables */
  private static final String STORED_PROC_NAME = "PKG_LIST_PEND_NOMINATIONS.PRC_LIST_PEND_NOMINATION_EXTR";

  public CallPrcListPendNominationExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_level_number", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_approver_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_time_period_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_submit_start_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_submit_end_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_pend_claim_dtl", OracleTypes.CURSOR, new DataMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotion_id", parameters.get( "promotionId" ) );
    inParams.put( "p_in_level_number", parameters.get( "levelNumber" ) );
    inParams.put( "p_in_approver_id", parameters.get( "approverUserId" ) );
    inParams.put( "p_in_time_period_id", parameters.get( "timePeriodId" ) );
    inParams.put( "p_in_submit_start_date", parameters.get( "startDate" ) );
    inParams.put( "p_in_submit_end_date", parameters.get( "endDate" ) );
    inParams.put( "p_in_status", parameters.get( "status" ) );
    inParams.put( "p_in_locale", parameters.get( "locale" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  @SuppressWarnings( "rawtypes" )
  private class DataMapper implements RowMapper
  {
    public String mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }
}
