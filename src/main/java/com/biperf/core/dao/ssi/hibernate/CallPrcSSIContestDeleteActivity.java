
package com.biperf.core.dao.ssi.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * Deletes the activity record and re-order the sequence number on all the activities for this contest
 * 
 * @author kandhi
 * @since Dec 30, 2014
 * @version 1.0
 */
public class CallPrcSSIContestDeleteActivity extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_REMOVE_ACTIVITY";

  public CallPrcSSIContestDeleteActivity( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_contest_activity_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long contestId, Long contestActivityId, Long userId )
  {
    Map<String, Object> inputParams = new HashMap<String, Object>();
    inputParams.put( "p_in_ssi_contest_id", contestId );
    inputParams.put( "p_in_contest_activity_id", contestActivityId );
    inputParams.put( "p_in_user_id", userId );
    return execute( inputParams );
  }

}
