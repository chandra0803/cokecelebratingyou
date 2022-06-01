
package com.biperf.core.dao.reports.hibernate;

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

/**
 * 
 * CallPrcEngagementReportsExtract.
 * Used to get the participation score extract in the team dash board page.
 * 
 * @author kandhi
 * @since Aug 22, 2014
 * @version 1.0
 */
public class CallPrcEngagementReportsExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_PARTICIPATION_EXTRACT";

  public CallPrcEngagementReportsExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_time_frame", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_node_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_month", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_end_year", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new DataMapper() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> extractParams )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_file_name", extractParams.get( "internalFilename" ) );
    inParams.put( "p_header", extractParams.get( "csHeaders" ) );
    inParams.put( "p_in_time_frame", extractParams.get( "timeframeType" ) );
    inParams.put( "p_in_user_id", extractParams.get( "userId" ) );
    inParams.put( "p_in_node_id", extractParams.get( "nodeId" ) );
    inParams.put( "p_in_end_month", extractParams.get( "endMonth" ) );
    inParams.put( "p_in_end_year", extractParams.get( "endYear" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  @SuppressWarnings( "rawtypes" )
  private class DataMapper implements RowMapper
  {
    public String mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }
}
