
package com.biperf.core.dao.promotion.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPostProcessJobsCleanUp extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_qrtz_post_process_job_del";

  public CallPostProcessJobsCleanUp( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    compile();
  }

  public Map<String, Object> executeProcedure()
  {
    return execute( new HashMap<String, Object>() );
  }

}
