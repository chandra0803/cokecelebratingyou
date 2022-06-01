
package com.biperf.core.dao.ssi.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;

import oracle.jdbc.OracleTypes;

/**
 * 
 * This procedure returns all the ranks in the contest, it's payouts and, badge info.
 * 
 * @author chowdhur
 * @since May 28, 2015
 */
public class CallPrcSSIContestSRPayoutList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_CONTEST_SR_PAYOUTS";

  public CallPrcSSIContestSRPayoutList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.

    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_sr_payout_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressSrPayoutsResultSetExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", inParameters.get( "contestId" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /**
   * SSIContestProgressSrPayoutsResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressSrPayoutsResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestStackRankPayoutValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestStackRankPayoutValueBean> contestProgressPayouts = new ArrayList<SSIContestStackRankPayoutValueBean>();
      while ( rs.next() )
      {
        Long rank = rs.getLong( "RANK_POSITION" );
        Long payout = rs.getLong( "PAYOUT_AMOUNT" );
        String payoutDescription = rs.getString( "PAYOUT_DESC" );
        Long badgeId = rs.getLong( "BADGE_RULE_ID" );
        String badgeName = rs.getString( "BADGE_NAME" );
        String badgeUrl = rs.getString( "BADGE_IMAGE" );
        contestProgressPayouts.add( new SSIContestStackRankPayoutValueBean( rank, payout, payoutDescription, badgeId, badgeName, badgeUrl ) );
      }
      return contestProgressPayouts;
    }
  }

}
