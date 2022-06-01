
package com.biperf.core.dao.welcomemail.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcWelcomeEmail extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_WELCOME_EMAIL_PREP";

  public CallPrcWelcomeEmail( DataSource ds )
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
