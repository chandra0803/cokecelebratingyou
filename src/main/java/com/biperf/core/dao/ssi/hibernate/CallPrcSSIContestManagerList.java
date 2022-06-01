
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

import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcSSIContestManagerList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST.PRC_SSI_CONTEST_MANAGERS";

  public CallPrcSSIContestManagerList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // NOTE: Calls to declareParameter must be made in the same order as they appear
    // in the database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_count_mgr_owner", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor", OracleTypes.CURSOR, new SSIContestManagerResultSetExtractor() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Map<String, Object> searchParameters )
  {
    Map<String, Object> outParams = execute( searchParameters );
    return outParams;
  }

  /**
   * SSIContestManagerResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestManagerResultSetExtractor implements ResultSetExtractor
  {

    public List<SSIContestParticipantValueBean> extractData( ResultSet rsSSIContestManagers ) throws SQLException, DataAccessException
    {
      ArrayList<SSIContestParticipantValueBean> ssiContestManagerList = new ArrayList<SSIContestParticipantValueBean>();

      while ( rsSSIContestManagers.next() )
      {
        SSIContestParticipantValueBean participantBean = new SSIContestParticipantValueBean();

        participantBean.setFirstName( rsSSIContestManagers.getString( 1 ) );
        participantBean.setLastName( rsSSIContestManagers.getString( 2 ) );
        participantBean.setId( rsSSIContestManagers.getLong( 3 ) );
        participantBean.setOrgName( rsSSIContestManagers.getString( 4 ) );
        participantBean.setOrgType( rsSSIContestManagers.getString( 5 ) );
        participantBean.setDepartmentName( rsSSIContestManagers.getString( 6 ) );
        participantBean.setJobName( rsSSIContestManagers.getString( 7 ) );
        participantBean.setCountryName( rsSSIContestManagers.getString( 8 ) );
        participantBean.setCountryCode( rsSSIContestManagers.getString( 9 ) );

        ssiContestManagerList.add( participantBean );
      }

      return ssiContestManagerList;
    }
  }

}
