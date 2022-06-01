
package com.biperf.core.dao.ssi.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * 
 * CallPrcSSIContestTotals.
 * 
 * @author chowdhur
 * @since Dec 9, 2014
 */
public class CallPrcSSIContestTotals extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_CONTEST_TOTALS";

  public CallPrcSSIContestTotals( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_issuance_number", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "total_objective_amount", Types.DOUBLE ) );
    declareParameter( new SqlOutParameter( "total_objective_payout", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "total_objective_bonus_payout", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "total_objective_bonus_cap", Types.BIGINT ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long contestId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_issuance_number", null );// issuance number Not applicable for Non - award
                                                 // them contest.
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  public Map<String, Object> executeProcedure( Long contestId, Short IssuanceNumber )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_issuance_number", IssuanceNumber );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }
}
