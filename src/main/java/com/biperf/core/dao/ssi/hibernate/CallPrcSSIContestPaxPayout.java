
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
 * @since Mar 03, 2015
 *
 */
public class CallPrcSSIContestPaxPayout extends StoredProcedure
{

  private static final String P_IN_SSI_CONTEST_ID = "p_in_ssi_contest_id";
  private static final String P_IN_USER_ID = "p_in_user_id";
  private static final String P_IN_AWARD_ISSUANCE_NUMBER = "p_in_award_issuance_number";
  private static final String P_IN_CSV_USER_IDS = "p_in_csv_user_ids";
  private static final String P_IN_CSV_PAYOUT_AMOUNTS = "p_in_csv_payout_amounts";

  public static final String P_OUT_RETURN_CODE = "p_out_return_code";

  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_CONTEST_PAX_PAYOUT";

  public CallPrcSSIContestPaxPayout( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( P_IN_SSI_CONTEST_ID, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_USER_ID, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_AWARD_ISSUANCE_NUMBER, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_CSV_USER_IDS, Types.VARCHAR ) );
    declareParameter( new SqlParameter( P_IN_CSV_PAYOUT_AMOUNTS, Types.VARCHAR ) );

    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.INTEGER ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long ssiContestId, Long userId, Short awardIssuanceNumber, String csvUserIds, String csvPayoutAmounts )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_SSI_CONTEST_ID, ssiContestId );
    inParams.put( P_IN_USER_ID, userId );
    inParams.put( P_IN_AWARD_ISSUANCE_NUMBER, awardIssuanceNumber );
    inParams.put( P_IN_CSV_USER_IDS, csvUserIds );
    inParams.put( P_IN_CSV_PAYOUT_AMOUNTS, csvPayoutAmounts );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

}
