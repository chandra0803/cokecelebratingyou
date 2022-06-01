/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reporttablerefresh/hibernate/CallPrcCoreRptRefresh.java,v $
 */

package com.biperf.core.dao.reporttablerefresh.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * CallSyncAppUsers
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>Dec 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class CallPrcCoreRptRefresh extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PRC_REPORT_REFRESH";

  public CallPrcCoreRptRefresh( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "pi_requested_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "po_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "po_error_message", Types.VARCHAR ) );

    compile();
  }

  /**
   * If using params the call would look like below public String executeProcedure( param1, param2,
   * param3, ...) inParams.put("p_param1", param1); inParams.put("p_param2", param2);
   * inParams.put("p_param3", param3); Map outParams = execute(inParams); if (outParams.size() > 0) {
   * return outParams.get("p_out_param").toString(); } else { return null; }
   */
  public Map executeProcedure( Long userId )
  {
    HashMap inParams = new HashMap();
    inParams.put( "pi_requested_user_id", userId );

    Map outParams = execute( inParams );

    return outParams;
  }
}
