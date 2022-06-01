
package com.biperf.core.dao.participant.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import oracle.jdbc.OracleTypes;

/*
 * Returns non-unique user ids for users with the given phone number
 */
public class CallPrcGetUserIdByPhone extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_get_userid_by_phone";
  public static final String P_IN_PHONE_NUMBER = "p_in_phone_number";
  public static final String P_OUT_RESULT_SET = "p_out_result_set";
  public static final String P_OUT_RETURN_CODE = "p_out_return_code";

  public CallPrcGetUserIdByPhone( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( P_IN_PHONE_NUMBER, Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RESULT_SET, OracleTypes.CURSOR, new SingleColumnRowMapper<>( Long.class ) ) );
    compile();
  }

  public Map<String, Object> executeProcedure( String phoneNumber )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_PHONE_NUMBER, phoneNumber );
    return execute( inParams );
  }

}
