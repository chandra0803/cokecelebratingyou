package com.biperf.core.dao.client.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallGenerateBudgetFileWDProc extends StoredProcedure
{
	private static final String STORED_PROC_NAME = "PRC_ADIH_GENERATE_BUDGET_WD";
	public CallGenerateBudgetFileWDProc( DataSource ds )
	  {
	    super( ds, STORED_PROC_NAME );
	    declareParameter( new SqlParameter( "p_node_names", Types.VARCHAR) );
	    declareParameter( new SqlParameter( "p_level", Types.NUMERIC) );	    
	    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );	    
	    compile();
	  }

	  @SuppressWarnings({ "rawtypes", "unchecked" })
	public Map executeProcedure( String orgUnits, Integer level )
	  {
	    HashMap inParams = new HashMap();
	    
	    inParams.put( "p_node_names", orgUnits );
	    inParams.put( "p_level", level );	    	    
	    Map outParams = execute( inParams );
	    return outParams;
	  }

}
