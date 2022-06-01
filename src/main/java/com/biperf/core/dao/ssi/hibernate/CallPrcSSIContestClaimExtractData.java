
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

import oracle.jdbc.OracleTypes;

/**
 * 
 * 
 * @author Ravi Kancherla
 * @since Jun 5, 2015
 * @version 1.0
 */

public class CallPrcSSIContestClaimExtractData extends StoredProcedure
{
  private static final String SSI_EXPORT_PROC_NAME = "pkg_ssi_contest_data.prc_ssi_contest_claims_export";
  private static final String LOCALE_EN_US = "en_US";

  public CallPrcSSIContestClaimExtractData( DataSource ds )
  {
    super( ds, SSI_EXPORT_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestClaimDataExtractResultSetExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", inParameters.get( "contestId" ) );
    inParams.put( "p_in_locale", inParameters.get( "p_in_locale" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /**
   * SSIContestClaimDataExtractResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestClaimDataExtractResultSetExtractor implements ResultSetExtractor
  {
    public List<String> extractData( ResultSet rsSSIContestClaimData ) throws SQLException, DataAccessException
    {
      List<String> ssiContestClaimData = new ArrayList<String>();
      while ( rsSSIContestClaimData.next() )
      {
        ssiContestClaimData.add( rsSSIContestClaimData.getString( 1 ) );
      }
      return ssiContestClaimData;
    }
  }
}
