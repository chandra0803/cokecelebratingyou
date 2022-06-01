
package com.biperf.core.dao.client.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CokeFindServiceAnniversarySP extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "pkg_adih_service_anniv_award.prc_adih_service_anniversary";

  public CokeFindServiceAnniversarySP( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_start_days", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_end_days", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.BIGINT ) );
    declareParameter( new SqlOutParameter( "p_out_message", Types.VARCHAR ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Long startDays, Long endDays )
  {
    Map<String, Object> inParams = new HashMap<>();
    inParams.put( "p_start_days", startDays );
    inParams.put( "p_end_days", endDays );
    return execute( inParams );
  }

}
