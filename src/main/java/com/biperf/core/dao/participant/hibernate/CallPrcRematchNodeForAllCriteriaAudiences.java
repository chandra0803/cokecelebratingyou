
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

public class CallPrcRematchNodeForAllCriteriaAudiences extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_BUILD_AUDIENCE.PRC_SYNC_AUDIENCE_HIERARCHY";

  public CallPrcRematchNodeForAllCriteriaAudiences( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "pi_target_node_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "pi_destination_node_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "pi_child_dest_node_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "po_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_user_data", OracleTypes.CURSOR, new CallPrcRematchNodeForAllCriteriaAudiences.DataMapper() ) );
    compile();
  }

  public Map executeProcedure( Long sourceNodeId, Long destinationPaxNodeId, Long destinationChildNodeId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "pi_target_node_id", sourceNodeId );
    inParams.put( "pi_destination_node_id", destinationPaxNodeId );
    inParams.put( "pi_child_dest_node_id", destinationChildNodeId );

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
