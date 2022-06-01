
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
 * 
 * @author simhadri
 * @since Dec 15, 2014
 *
 */
public class CallPrcSSIContestCopy extends StoredProcedure
{
  public static final String P_OUT_DESTINATION_CONTEST_ID = "p_out_destination_contest_id";
  public static final String P_OUT_RETURN_CODE = "p_out_return_code";
  private static final String P_IN_LOCALE = "p_in_locale";
  private static final String P_IN_USER_ID = "p_in_user_id";
  private static final String P_IN_DESTINATION_CONTEST_NAME = "p_in_destination_contest_name";
  private static final String P_IN_SSI_CONTEST_ID = "p_in_ssi_contest_id";
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.prc_ssi_contest_copy";

  public CallPrcSSIContestCopy( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( P_IN_SSI_CONTEST_ID, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_DESTINATION_CONTEST_NAME, Types.VARCHAR ) );
    declareParameter( new SqlParameter( P_IN_LOCALE, Types.VARCHAR ) );
    declareParameter( new SqlParameter( P_IN_USER_ID, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.INTEGER ) );
    declareParameter( new SqlOutParameter( P_OUT_DESTINATION_CONTEST_ID, Types.NUMERIC ) );
    compile();
  }

  /**
   * Copy details from source contest to a new contest with the name  destinationContestName
   * @param sourceContestId
   * @param destinationContestName
   * @param locale
   * @return map containing error code and destination contest id
   */
  public Map<String, Object> executeProcedure( Long sourceContestId, String destinationContestName, String locale, Long userId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_SSI_CONTEST_ID, sourceContestId );
    inParams.put( P_IN_DESTINATION_CONTEST_NAME, destinationContestName );
    inParams.put( P_IN_LOCALE, locale );
    inParams.put( P_IN_USER_ID, userId );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

}
