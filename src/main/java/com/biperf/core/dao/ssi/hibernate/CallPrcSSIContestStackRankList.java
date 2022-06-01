
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

import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;

import oracle.jdbc.OracleTypes;

/**
 * CallPrcSSIContestStackRankList is used to get the stack rank participants list for all the contests types and for all the users(creator/manager/participant).
 * This proc will call for different contest type like objectives/step it up only when user clicks on view all stack rank button.
 * 
 * @author dudam
 * @since Feb 12, 2015
 * @version 1.0
 */
public class CallPrcSSIContestStackRankList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST_DATA.PRC_SSI_CONTEST_STACKRANK_LIST";

  public CallPrcSSIContestStackRankList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "is_team", Types.INTEGER ) );
    declareParameter( new SqlParameter( "is_in_include_all", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_contest_activity_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_pax_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestPaxStackRankResultSetExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", inParameters.get( "contestId" ) );
    inParams.put( "p_in_user_id", inParameters.get( "userId" ) );
    inParams.put( "is_team", inParameters.get( "isTeam" ) );
    inParams.put( "is_in_include_all", inParameters.get( "isIncludeAll" ) );
    inParams.put( "p_in_contest_activity_id", inParameters.get( "activityId" ) );
    inParams.put( "p_in_rowNumStart", inParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", inParameters.get( "rowNumEnd" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /**
   * SSIContestPaxStackRankResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestPaxStackRankResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestStackRankPaxValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestStackRankPaxValueBean> stackRanks = new ArrayList<SSIContestStackRankPaxValueBean>();
      while ( rs.next() )
      {
        SSIContestStackRankPaxValueBean stackRank = new SSIContestStackRankPaxValueBean();
        // rs.getLong( "RN" );
        stackRank.setRank( rs.getInt( "STACK_RANK" ) );
        stackRank.setParticipantId( rs.getLong( "PARTICIPANT_ID" ) );
        stackRank.setFirstName( rs.getString( "FIRST_NAME" ) );
        stackRank.setLastName( rs.getString( "LAST_NAME" ) );
        stackRank.setAvatarUrl( rs.getString( "AVATAR" ) );
        String paxActivity = rs.getString( "PAX_ACTIVITY" );
        stackRank.setScore( StringUtil.isNullOrEmpty( paxActivity ) ? null : Double.parseDouble( paxActivity ) );
        stackRank.setTeamMember( rs.getBoolean( 8 ) );// IS_TEAM_MEMEBER wrong spelling
        stackRanks.add( stackRank );
      }
      return stackRanks;
    }
  }

}
