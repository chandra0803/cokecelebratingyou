
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

import oracle.jdbc.OracleTypes;

public class CallPrcRecreateCriteriaAudienceParticipants extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_BUILD_AUDIENCE.PRC_REFRESH_CRITERIA_AUDIENCE";

  public CallPrcRecreateCriteriaAudienceParticipants( DataSource dataSource )
  {
    super( dataSource, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "pi_audience_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "po_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_user_data", OracleTypes.CURSOR, new CallPrcRecreateCriteriaAudienceParticipants.DataMapper() ) );
    compile();
  }

  public Map executeProcedure( Long audienceId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "pi_audience_id", audienceId );
    Map outParams = execute( inParams );
    return outParams;
  }

  /**
   * DataMapper is an Inner class which implements the RowMapper.
   */
  private class DataMapper implements RowMapper<Long>
  {
    public Long mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getLong( 1 );
    }
  }

}
