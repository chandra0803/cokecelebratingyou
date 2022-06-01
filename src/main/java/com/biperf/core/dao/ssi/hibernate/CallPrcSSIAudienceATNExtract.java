
package com.biperf.core.dao.ssi.hibernate;

import java.math.BigDecimal;
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

import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcSSIAudienceATNExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_ssi_contest_extract";

  public CallPrcSSIAudienceATNExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestDataResultSetExtractor() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Map<String, Object> searchParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", new BigDecimal( (Long)searchParameters.get( "contestId" ) ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private String withoutFirstLastCharacter( String originalString )
  {
    if ( originalString.trim().isEmpty() )
    {
      return originalString.trim();
    }
    originalString.trim();
    return originalString.substring( 1 ).substring( 0, originalString.length() - 2 );
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

        String[] reslutRowArray = rsSSIContestData.getString( 1 ).split( "," );

        participantBean.setLoginId( withoutFirstLastCharacter( reslutRowArray[0] ) );
        participantBean.setFirstName( withoutFirstLastCharacter( reslutRowArray[1] ) );
        participantBean.setLastName( withoutFirstLastCharacter( reslutRowArray[2] ) );
        participantBean.setRole( withoutFirstLastCharacter( reslutRowArray[3] ) );
        participantBean.setActivityDescription( withoutFirstLastCharacter( reslutRowArray[4] ) );
        participantBean.setActivityAmount( withoutFirstLastCharacter( reslutRowArray[5] ) );
        participantBean.setPayoutPoints( withoutFirstLastCharacter( reslutRowArray[6] ) );
        participantBean.setPayoutDescription( withoutFirstLastCharacter( reslutRowArray[7] ) );
        participantBean.setValue( withoutFirstLastCharacter( reslutRowArray[8] ) );
        ssiContestData.add( participantBean );
      }
      return ssiContestData;
    }
  }
}
