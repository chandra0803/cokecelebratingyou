
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
 * Updates all Contest's claims 
 * 
 * @author patelp
 * @since June 6, 2015
 */
public class CallPrcSSIContestClaimsUpdate extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_CONTEST_CLAIMS_UPDATE";

  public CallPrcSSIContestClaimsUpdate( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_count_claims", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_error_message", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long contestId )
  {
    Map<String, Object> inputParams = new HashMap<String, Object>();
    inputParams.put( "p_in_ssi_contest_id", contestId );
    return execute( inputParams );
  }
}
