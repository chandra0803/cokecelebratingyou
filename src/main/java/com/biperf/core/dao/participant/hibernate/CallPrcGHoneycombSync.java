
package com.biperf.core.dao.participant.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.dao.HashMapColumnRowMapper;

import oracle.jdbc.OracleTypes;

public class CallPrcGHoneycombSync extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_g_to_honeycomb_sync";

  public CallPrcGHoneycombSync( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id_list", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_application_user", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_participant", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_participant_address", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_participant_email", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_participant_phone", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_org_unit_participant", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_participant_employer", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_participant_char", OracleTypes.CURSOR, new HashMapColumnRowMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( List<Long> userIds )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id_list", StringUtils.join( userIds, "," ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }
  
}
