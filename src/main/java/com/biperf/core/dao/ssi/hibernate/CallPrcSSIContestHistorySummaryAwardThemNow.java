
package com.biperf.core.dao.ssi.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.ssi.SSIContestAwardHistoryValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

import oracle.jdbc.OracleTypes;

/**
 * 
 * CallPrcSSIContestHistorySummaryAwardThemNow.
 * 
 * @author kandhi
 * @since Feb 11, 2015
 * @version 1.0
 */
public class CallPrcSSIContestHistorySummaryAwardThemNow extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "Pkg_ssi_contest_data.prc_ssi_atn_issuance_sum";

  public CallPrcSSIContestHistorySummaryAwardThemNow( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_issuance_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestAwardHistoryResultSetExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inParams )
  {
    return execute( inParams );
  }

  private class SSIContestAwardHistoryResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestAwardHistoryValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestAwardHistoryValueBean> awardHistoryList = new ArrayList<SSIContestAwardHistoryValueBean>();
      SSIContestAwardHistoryValueBean historyBean = null;
      while ( rs.next() )
      {
        historyBean = new SSIContestAwardHistoryValueBean();
        historyBean.setDateCreated( rs.getDate( 2 ) );
        historyBean.setAmount( rs.getDouble( 3 ) );
        historyBean.setPayoutAmount( rs.getDouble( 4 ) );
        historyBean.setStatus( rs.getString( 5 ) );
        historyBean.setParticipantsCount( rs.getInt( 7 ) );
        historyBean.setDenialReason( rs.getString( 9 ) );
        boolean isPayoutDescriptionSame = rs.getBoolean( 10 );
        if ( isPayoutDescriptionSame )
        {
          historyBean.setPayoutDescription( rs.getString( 6 ) );
        }
        else
        {
          historyBean.setPayoutDescription( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.atn.summary.PAYOUT_DESCRIPTION_VARIES" ) );
        }
        historyBean.setPayoutDescriptionSame( isPayoutDescriptionSame );
        historyBean.setIssuanceNumber( rs.getShort( 11 ) );
        historyBean.setApprovalLevelActionTaken( rs.getInt( 12 ) );
        historyBean.setApprovedByLevel1( rs.getLong( 13 ) );
        awardHistoryList.add( historyBean );
      }
      return awardHistoryList;
    }
  }
}
