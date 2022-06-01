
package com.biperf.core.dao.nonredemptionreminder.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
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
 * <td>Krishna Mattam</td>
 * <td>June 02, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class CallPrcNonRedemptionExtract extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PRC_POPULATE_REMINDER_EMAIL";

  public CallPrcNonRedemptionExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_environment_name", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_error_message", Types.VARCHAR ) );

    compile();
  }

  public Map executeProcedure( Long promotionId, String mediaType )
  {
    HashMap inParams = new HashMap();

    inParams.put( "p_promotion_id", promotionId );
    inParams.put( "p_environment_name", mediaType );

    Map outParams = execute( inParams );

    return outParams;
  }

}
