package com.biperf.core.dao.client.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;
/*
* @author Ramesh jaligama
* @since Sep 5, 2016
* @version 1.0
*/
public class CallPrcAdihExpireTempPwCalcSP extends StoredProcedure{
	


	  private static final String STORED_PROC_NAME = "PRC_ADIH_EXPIRE_TEMP_PW";

	  public CallPrcAdihExpireTempPwCalcSP( DataSource ds )
	  {
	    super( ds, STORED_PROC_NAME );
	    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
		declareParameter(new SqlOutParameter("p_error_message",Types.VARCHAR));
	    compile();
	  }

	  public Map executeProcedure()
	  {
	    HashMap inParams = new HashMap();
	    Map outParams = execute( inParams );
	    return outParams;
	  }

}
