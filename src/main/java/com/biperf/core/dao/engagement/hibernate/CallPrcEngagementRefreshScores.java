
package com.biperf.core.dao.engagement.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * This process will refresh the engagement scores
 * Can be run from the promotion overview page
 * CallPrcEngagementRefreshScores.
 * 
 * @author kandhi
 * @since Jun 9, 2014
 * @version 1.0
 */
public class CallPrcEngagementRefreshScores extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT_EXTRACT.P_ENGAGEMENT_SCORE_WRAPPER";

  public CallPrcEngagementRefreshScores( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "pi_requested_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "po_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "po_error_message", Types.VARCHAR ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long userId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "pi_requested_user_id", userId );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

}
