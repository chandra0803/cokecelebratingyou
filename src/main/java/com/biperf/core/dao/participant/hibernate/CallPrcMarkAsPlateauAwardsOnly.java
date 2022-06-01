
package com.biperf.core.dao.participant.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcMarkAsPlateauAwardsOnly extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_mark_pax_plateau_award";

  public CallPrcMarkAsPlateauAwardsOnly( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    compile();

  }

  public Map executeProcedure()
  {
    HashMap inParams = new HashMap();
    Map outParams = execute( inParams );
    return outParams;
  }
}
