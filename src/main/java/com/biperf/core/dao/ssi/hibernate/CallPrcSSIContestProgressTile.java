
package com.biperf.core.dao.ssi.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.ssi.SSIContestProgressValueBean;

import oracle.jdbc.OracleTypes;

/**
 * CallPrcSSIContestProgressTile.
 * 
 * @author dudam
 * @since Apr 14, 2015
 * @version 1.0
 */
public class CallPrcSSIContestProgressTile extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST_DATA.PRC_SSI_CONTEST_CREATOR_TILE";

  public CallPrcSSIContestProgressTile( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.

    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_contest_type", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_obj_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressSiuResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_ref_cursor", OracleTypes.CURSOR, new SSIContestProgressSrResultSetExtractor() ) );
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
   * SSIContestManagerResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestProgressValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestProgressValueBean contestProgress = new SSIContestProgressValueBean();
      while ( rs.next() )
      {
        contestProgress.setContestId( rs.getLong( "SSI_CONTEST_ID" ) );
        contestProgress.setActivity( rs.getDouble( "TEAM_ACTIVITY" ) );
        contestProgress.setParticipantAchieved( rs.getInt( "PAX_ACHIEVED" ) );
        contestProgress.setTotalParticipant( rs.getInt( "TOTAL_PAX" ) );
      }
      return contestProgress;
    }
  }

  /**
   * SSIContestProgressSiuResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressSiuResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestProgressValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestProgressValueBean contestProgress = new SSIContestProgressValueBean();
      while ( rs.next() )
      {
        contestProgress.setContestId( rs.getLong( "SSI_CONTEST_ID" ) );
        contestProgress.setContestGoal( rs.getDouble( "CONTEST_GOAL" ) );
        contestProgress.setActivity( rs.getDouble( "ACTIVITY" ) );
      }
      return contestProgress;
    }
  }

  /**
   * SSIContestProgressSrResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestProgressSrResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestProgressValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestProgressValueBean contestProgress = new SSIContestProgressValueBean();
      while ( rs.next() )
      {
        contestProgress.setScore( rs.getDouble( "SCORE" ) );
        contestProgress.setActivity( rs.getDouble( "ACTIVITY_AMT" ) );
        contestProgress.setRank( rs.getInt( "STACK_RANK_POSITION" ) );
        contestProgress.setParticipantId( rs.getLong( "USER_ID" ) );
        contestProgress.setFirstName( rs.getString( "FIRST_NAME" ) );
        contestProgress.setLastName( rs.getString( "LAST_NAME" ) );
        contestProgress.setAvatarUrl( rs.getString( "AVATAR_URL" ) );
      }
      return contestProgress;
    }
  }

}
