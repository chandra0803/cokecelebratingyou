
package com.biperf.core.dao.participant.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcRematchParticipantForAllCriteriaAudiences extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_BUILD_AUDIENCE.PRC_SYNC_PAX_CRITERIA_AUDIENCE";

  public CallPrcRematchParticipantForAllCriteriaAudiences( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "pi_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "pi_import_file_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "po_returncode", Types.NUMERIC ) );
    compile();
  }

  public Map executeProcedure( Long participantId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "pi_user_id", participantId );
    inParams.put( "pi_import_file_id", null );
    Map outParams = execute( inParams );
    return outParams;
  }

  /**
   * DataMapper is an Inner class which implements the RowMapper.
   */
  private class DataMapper implements RowMapper
  {
    public Object mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }

}
