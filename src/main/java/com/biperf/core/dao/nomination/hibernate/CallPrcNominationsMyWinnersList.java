
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

import com.biperf.core.value.pastwinners.NominationMyWinners;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationsMyWinnersList extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_nominations_my_win_list";

  public CallPrcNominationsMyWinnersList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", OracleTypes.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_win_count", OracleTypes.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationsMyWinnersList.NominationsMyWinnersMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", parameters.get( "participantId" ) );
    inParams.put( "p_in_rowNumStart", parameters.get( "rowNumberStart" ) );
    inParams.put( "p_in_rowNumEnd", parameters.get( "rowNumberEnd" ) );
    inParams.put( "p_in_sortedBy", parameters.get( "sortedBy" ) );
    inParams.put( "p_in_sortedOn", parameters.get( "sortedOn" ) );
    inParams.put( "p_in_locale", parameters.get( "locale" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationsMyWinnersMapper implements ResultSetExtractor<List<NominationMyWinners>>
  {
    @Override
    public List<NominationMyWinners> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationMyWinners> reportData = new ArrayList<NominationMyWinners>();

      while ( rs.next() )
      {
        NominationMyWinners reportValue = new NominationMyWinners();

        reportValue.setTeamId( rs.getLong( "TEAM_ID" ) );
        reportValue.setApproverUserId( rs.getLong( "APPROVER_USER_ID" ) );
        reportValue.setActivityId( rs.getLong( "ACTIVITY_ID" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setDateWon( rs.getDate( "DATE_WON" ) );
        reportValue.setLevelNumber( rs.getLong( "LEVEL_NUMBER" ) );
        reportValue.setLevelName( rs.getString( "LEVEL_NAME" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }
}
