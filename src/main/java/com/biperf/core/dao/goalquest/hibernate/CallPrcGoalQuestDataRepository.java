
package com.biperf.core.dao.goalquest.hibernate;

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

public class CallPrcGoalQuestDataRepository extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_rpt_repository_extract";

  public CallPrcGoalQuestDataRepository( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    declareParameter( new SqlParameter( "p_in_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_report_path", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    compile();

  }

  public Map executeProcedure( Long promotionId, String locale )
  {
    HashMap inParams = new HashMap();
    inParams.put( "p_in_promotion_id", promotionId );
    inParams.put( "p_in_locale", locale );
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
