
package com.biperf.core.dao.client;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import oracle.jdbc.OracleTypes;

public class CallPrcRecognizeAnyoneExtract extends StoredProcedure {
	private static final String STORED_PROC_NAME = "PRC_ADIH_RECOG_ANY_EXTRACT";

	public CallPrcRecognizeAnyoneExtract(DataSource ds) {
		super(ds, STORED_PROC_NAME);
		declareParameter(new SqlParameter("p_in_start_date", Types.VARCHAR));
		declareParameter(new SqlParameter("p_in_end_date", Types.VARCHAR));
		declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
		declareParameter(new SqlParameter("p_in_file_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("p_out_return_code", Types.VARCHAR));
		declareParameter(new SqlOutParameter("p_out_result_set", OracleTypes.CURSOR));
		compile();
	}

	public Map<String, Object> executeProcedure(Map<String, Object> reportParameters) {
		String file = (String)reportParameters.get("internalFilename");
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("p_in_start_date", (String) reportParameters.get("fromDate"));
		inParams.put("p_in_end_date", (String) reportParameters.get("toDate"));
		inParams.put( "p_in_locale", (String)reportParameters.get( "languageCode" ));
		//inParams.put( "p_in_locale", reportParameters.get( "localeDatePattern" ) );
		inParams.put("p_in_file_name", file != null ? file: "");
		Map<String, Object> outParams = execute(inParams);
		return outParams;
	}

}
