
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

import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

import oracle.jdbc.OracleTypes;

/**
 * CallPrcSSIContestDownloadData.
 * @author dudam
 * @since Jan 13, 2015
 * @version 1.0
 */
public class CallPrcSSIContestDownloadData extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_CONTEST_DOWNLOAD";

  public CallPrcSSIContestDownloadData( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new SSIContestDataResultSetExtractor() ) );
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
  private class SSIContestDataResultSetExtractor implements ResultSetExtractor
  {

    public List<SSIContestParticipantValueBean> extractData( ResultSet rsSSIContestData ) throws SQLException, DataAccessException
    {
      ArrayList<SSIContestParticipantValueBean> ssiContestData = new ArrayList<SSIContestParticipantValueBean>();
      while ( rsSSIContestData.next() )
      {
        SSIContestParticipantValueBean participantBean = new SSIContestParticipantValueBean();

        participantBean.setContestId( rsSSIContestData.getString( 1 ) );
        if ( rsSSIContestData.getString( 2 ).startsWith( "0" ) )
        {

          participantBean.setLoginId( "\"\t" + rsSSIContestData.getString( 2 ) + SSIContestUtil.REGEX_DOUBLE_QUOTE );
        }
        else
        {
          participantBean.setLoginId( rsSSIContestData.getString( 2 ) );
        }
        participantBean.setFirstName( rsSSIContestData.getString( 3 ) );
        participantBean.setLastName( rsSSIContestData.getString( 4 ) );
        if ( rsSSIContestData.getString( 5 ).contains( "," ) )
        {
          participantBean.setOrgName( SSIContestUtil.REGEX_DOUBLE_QUOTE + rsSSIContestData.getString( 5 ) + SSIContestUtil.REGEX_DOUBLE_QUOTE );
        }
        else
        {
          participantBean.setOrgName( rsSSIContestData.getString( 5 ) );
        }
        participantBean.setEmailAddress( rsSSIContestData.getString( 6 ) );
        if ( rsSSIContestData.getString( 7 ).contains( "," ) )
        {
          participantBean.setActivityDescription( SSIContestUtil.REGEX_DOUBLE_QUOTE + rsSSIContestData.getString( 7 ) + SSIContestUtil.REGEX_DOUBLE_QUOTE );
        }
        else
        {
          participantBean.setActivityDescription( rsSSIContestData.getString( 7 ) );
        }

        ssiContestData.add( participantBean );
      }
      return ssiContestData;
    }
  }

}
