
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
 * CallPrcSSIContestUpdateGoalPercentage.
 * 
 * @author Patel
 * @since Febc 2, 2015
 */

public class CallPrcSSIContestUpdateGoalPercentage extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_UPD_SSI_CONTEST_GOAL_PERC";

  public CallPrcSSIContestUpdateGoalPercentage( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_error_message", Types.VARCHAR ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Long contestId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }
}
