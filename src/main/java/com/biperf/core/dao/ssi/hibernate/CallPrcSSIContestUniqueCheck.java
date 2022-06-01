
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
 * CallPrcSSIContestUniqueCheck.
 * 
 * @author kandhi
 * @since Dec 18, 2014
 * @version 1.0
 */
public class CallPrcSSIContestUniqueCheck extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_CONTEST_UNIQUENESS";

  public CallPrcSSIContestUniqueCheck( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_issuance_number", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_boolean_activity_desc", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_boolean_obj_amount", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_boolean_obj_payout", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_boolean_obj_payout_desc", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_boolean_obj_bonus", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_boolean_obj_bonus_cap", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_activity_description", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_obj_amount", Types.DOUBLE ) );
    declareParameter( new SqlOutParameter( "p_out_obj_payout", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "p_out_obj_payout_desc", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_obj_bonus_increment", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "p_out_obj_bonus_payout", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "p_out_obj_bonus_cap", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "p_out_total_obj_amount", Types.DOUBLE ) );
    declareParameter( new SqlOutParameter( "p_out_total_obj_payout", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "p_out_total_obj_bonus_cap", Types.BIGINT ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long contestId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_issuance_number", null ); // issuance number N.A for non Award them contest
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  public Map<String, Object> executeProcedure( Long contestId, Short issuanceNumber )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_issuance_number", issuanceNumber );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }
}
