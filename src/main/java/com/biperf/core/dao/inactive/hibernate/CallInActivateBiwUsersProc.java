
package com.biperf.core.dao.inactive.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * 
 * CallInActivateBiwUsersProc.
 * 
 */
public class CallInActivateBiwUsersProc extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_inactivate_biw_users";

  /**
   * CallInActivateBiwUsersProc constructor.
   * 
   * @param ds
   */
  public CallInActivateBiwUsersProc( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.INTEGER ) );
    compile();
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public Map executeProcedure( Long runByUserId )
  {
    HashMap inParams = new HashMap();
    inParams.put( "p_in_user_id", runByUserId );
    Map outParams = execute( inParams );
    return outParams;
  }

}