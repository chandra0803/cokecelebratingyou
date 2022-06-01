
package com.biperf.core.dao.ssi.hibernate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestParticipantPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestPaxPayoutBadgeValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcSSIContestSavePaxPayouts extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST_DATA.PRC_SSI_CONTEST_PAX_PAYOUT";
  private SSIContestPayoutsValueBean contestPayoutsValueBean = new SSIContestPayoutsValueBean();

  protected static final Log log = LogFactory.getLog( CallPrcSSIContestSavePaxPayouts.class );

  public CallPrcSSIContestSavePaxPayouts( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // NOTE: Calls to declareParameter must be made in the same order as they appear
    // in the database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_csv_user_ids", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_csv_payout_amounts", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_csv_payout_desc", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_pax_count", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestPaxPayoutResultSetExtractor() ) );

    compile();
  }

  public SSIContestPayoutsValueBean executeProcedure( Long contestId,
                                                      String userIds,
                                                      String payoutAmounts,
                                                      String payoutDesc,
                                                      String sortColumnName,
                                                      String sortBy,
                                                      Integer rowNumStart,
                                                      Integer rowNumEnd )
      throws ServiceErrorException
  {
    contestPayoutsValueBean = new SSIContestPayoutsValueBean();
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_csv_user_ids", userIds );
    inParams.put( "p_in_csv_payout_amounts", payoutAmounts );
    inParams.put( "p_in_csv_payout_desc", payoutDesc );
    inParams.put( "p_in_sortColName", sortColumnName );
    inParams.put( "p_in_sortedBy", sortBy );
    inParams.put( "p_in_rowNumStart", rowNumStart );
    inParams.put( "p_in_rowNumEnd", rowNumEnd );

    Map<String, Object> outParams = execute( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != null && returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "Stored procedure returned error. Procedure returned: " + returnCode ); // TODO
    }
    contestPayoutsValueBean.setTotalParticipantCount( ( (BigDecimal)outParams.get( "p_out_pax_count" ) ).intValue() );
    return contestPayoutsValueBean;
  }

  protected void setParticipantDetails( ResultSet rs, SSIContestParticipantPayoutsValueBean participantPayout ) throws SQLException
  {
    participantPayout.setId( rs.getString( "USER_ID" ) );
    participantPayout.setLastName( rs.getString( "LAST_NAME" ) );
    participantPayout.setFirstName( rs.getString( "FIRST_NAME" ) );
    participantPayout.setOptOutAwards( rs.getBoolean( "IS_OPT_OUT_OF_AWARDS" ) );
  }

  protected void setParticipantBadgeDetails( ResultSet rs, SSIContestParticipantPayoutsValueBean participantPayout ) throws SQLException
  {
    participantPayout.setBadge( new SSIContestPaxPayoutBadgeValueBean( rs.getString( "BADGE_ID" ), rs.getString( "BADGE_NAME" ), rs.getString( "BADGE_IMAGE" ) ) );
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestPaxPayoutResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestParticipantPayoutsValueBean> participantsPayoutList = contestPayoutsValueBean.getPartiticpantPayoutsList();
      while ( rs.next() )
      {
        SSIContestParticipantPayoutsValueBean participantPayout = new SSIContestParticipantPayoutsValueBean();
        setParticipantDetails( rs, participantPayout );
        setParticipantBadgeDetails( rs, participantPayout );
        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        participantPayout.setProgress( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        participantPayout.setRank( SSIContestUtil.getFormattedValue( rs.getLong( "STACK_RANK" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        participantPayout.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        participantPayout.setPayout( totalPayout );
        participantPayout.setPayoutValue( totalPayout );
        participantsPayoutList.add( participantPayout );
      }
      return contestPayoutsValueBean;
    }
  }

}
