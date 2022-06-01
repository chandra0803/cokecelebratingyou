
package com.biperf.core.dao.nomination.hibernate;

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

import com.biperf.core.utils.UserManager;
import com.biperf.core.value.pastwinners.NominationModalNomineeDetails;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationModalWindow extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_nomination_modal_window";

  public CallPrcNominationModalWindow( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_nominee_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", OracleTypes.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_nomniee_dtl", OracleTypes.CURSOR, new CallPrcNominationModalWindow.NominationModalNomineeDetailsMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_nominee_user_id", parameters.get( "userId" ) );
    inParams.put( "p_in_locale", UserManager.getLocale().toString() );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationModalNomineeDetailsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationModalNomineeDetails> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationModalNomineeDetails> reportData = new ArrayList<NominationModalNomineeDetails>();

      while ( rs.next() )
      {
        NominationModalNomineeDetails reportValue = new NominationModalNomineeDetails();

        reportValue.setTimePeriodName( rs.getString( "time_period_name" ) );
        reportValue.setPromotionName( rs.getString( "promotion_name" ) );
        reportValue.setWinCout( rs.getLong( "win_count" ) );
        reportValue.setPointsWon( rs.getLong( "points_won" ) );
        reportValue.setCurrencyCode( rs.getString( "currency_code" ) );
        reportValue.setCashWon( rs.getBigDecimal( "cash_won" ) );
        reportValue.setTeamId( rs.getLong( "team_id" ) == 0 ? null : rs.getLong( "team_id" ) );
        reportValue.setApproverUserId( rs.getLong( "approver_user_id" ) );
        reportValue.setActivityId( rs.getLong( "activity_id" ) );
        reportValue.setPayoutDescription( rs.getString( "payout_description_asset_code" ) ); // It's
                                                                                             // the
                                                                                             // text,
                                                                                             // not
                                                                                             // the
                                                                                             // code.

        reportData.add( reportValue );
      }

      return reportData;
    }
  }
}
