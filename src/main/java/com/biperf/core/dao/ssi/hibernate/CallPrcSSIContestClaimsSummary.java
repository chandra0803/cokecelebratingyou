
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

import com.biperf.core.value.ssi.SSIContestPaxClaimValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcSSIContestClaimsSummary extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "pkg_ssi_contest_data.ssi_contest_claims_sort";

  public CallPrcSSIContestClaimsSummary( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_claims_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_claims_submitted_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_claims_pending_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_claims_approved_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_claims_denied_count", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestClaimsResultSetExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inParams )
  {
    return execute( inParams );
  }

  private class SSIContestClaimsResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestPaxClaimValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestPaxClaimValueBean> ssiContestPaxClaimValueBeanList = new ArrayList<SSIContestPaxClaimValueBean>();
      SSIContestPaxClaimValueBean ssiContestPaxClaimValueBean = null;
      while ( rs.next() )
      {
        ssiContestPaxClaimValueBean = new SSIContestPaxClaimValueBean();
        ssiContestPaxClaimValueBean.setClaimNumber( rs.getString( 2 ) );
        ssiContestPaxClaimValueBean.setSubmissionDate( rs.getDate( 3 ) );
        ssiContestPaxClaimValueBean.setStatus( rs.getString( 4 ) );
        ssiContestPaxClaimValueBean.setStatusDescription( rs.getString( 5 ) );
        ssiContestPaxClaimValueBean.setSubmittedBy( rs.getString( 6 ) );
        ssiContestPaxClaimValueBean.setApprovedBy( rs.getString( 7 ) );
        ssiContestPaxClaimValueBean.setId( rs.getLong( 8 ) );
        ssiContestPaxClaimValueBean.setContestId( rs.getLong( 9 ) );
        ssiContestPaxClaimValueBean.setActivityDescription( rs.getString( 10 ) );

        ssiContestPaxClaimValueBeanList.add( ssiContestPaxClaimValueBean );
      }
      return ssiContestPaxClaimValueBeanList;
    }
  }

}
