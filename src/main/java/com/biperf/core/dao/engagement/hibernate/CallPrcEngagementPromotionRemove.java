
package com.biperf.core.dao.engagement.hibernate;

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

public class CallPrcEngagementPromotionRemove extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_delete_engagement_promo";

  public CallPrcEngagementPromotionRemove( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Long promotionId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_promotion_id", promotionId );
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