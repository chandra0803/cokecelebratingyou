
package com.biperf.core.dao.nomination.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcRefreshPendingNominationApprover extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_refresh_pend_nom_approver";

  public CallPrcRefreshPendingNominationApprover( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", parameters.get( "userId" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

}
