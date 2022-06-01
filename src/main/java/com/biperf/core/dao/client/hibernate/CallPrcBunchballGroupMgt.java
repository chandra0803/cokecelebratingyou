
package com.biperf.core.dao.client.hibernate;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcBunchballGroupMgt extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_ADIH_BB_GROUP_MGT";

  public CallPrcBunchballGroupMgt( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // NOTE: Calls to declareParameter must be made in the same order as
    // they appear
    // in the database's stored procedure parameter list.

    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    compile();
  }

  public Map executeProcedure()
  {
    Map<String, Object> outParams = execute();

    return outParams;
  }

}
