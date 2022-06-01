
package com.biperf.core.dao.gamification.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcBadgeVerifyImport extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PKG_BADGE_VERIFY_IMPORT.p_badge_verify_load";

  public CallPrcBadgeVerifyImport( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_import_file_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_load_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_earned_date", Types.DATE ) );
    declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    compile();

  }

  public Map executeProcedure( Long importFileId, String loadType, Long userId, Long badgeId, Date earnedDate )
  {

    HashMap inParams = new HashMap();
    inParams.put( "p_import_file_id", importFileId );
    inParams.put( "p_load_type", loadType );
    inParams.put( "p_promotion_id", badgeId );
    inParams.put( "p_user_id", userId );
    inParams.put( "p_earned_date", earnedDate );

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
