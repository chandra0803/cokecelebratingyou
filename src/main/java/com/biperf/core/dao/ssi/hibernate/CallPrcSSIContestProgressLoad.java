
package com.biperf.core.dao.ssi.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CallPrcSSIContestProgressLoad extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_ssi_progress_load";
  public static final String P_OUT_SSI_CONTEST_ID = "p_out_ssi_contest_id";
  public static final String P_OUT_IMPORT_FILE_ID = "p_out_import_file_id";
  public static final String P_FILE_RECORDS_COUNT = "p_file_records_count";
  public static final String P_PROCESSED_RECORDS_COUNT = "p_processed_records_count";
  public static final String P_OUT_RETURNCODE = "p_out_returncode";
  public static final String P_IN_FILE_NAME = "p_file_name";

  public CallPrcSSIContestProgressLoad( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( P_IN_FILE_NAME, Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( P_OUT_SSI_CONTEST_ID, Types.BIGINT ) );
    declareParameter( new SqlOutParameter( P_OUT_IMPORT_FILE_ID, Types.BIGINT ) );
    declareParameter( new SqlOutParameter( P_FILE_RECORDS_COUNT, Types.INTEGER ) );
    declareParameter( new SqlOutParameter( P_PROCESSED_RECORDS_COUNT, Types.INTEGER ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURNCODE, Types.INTEGER ) );
    compile();
  }

  public Map<String, Object> executeProcedure( String fileName )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_FILE_NAME, fileName );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }
}
