
package com.biperf.core.dao.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcAdihPayrollExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_ADIH_PAYROLL_EXTRACT";

  public CallPrcAdihPayrollExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    compile();
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public Map<String, Object> executeProcedure()
  {
    Map outParams = execute();

    return outParams;
  }

 

}
