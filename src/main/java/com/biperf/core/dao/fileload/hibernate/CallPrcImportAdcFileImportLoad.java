
package com.biperf.core.dao.fileload.hibernate;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.utils.UserManager;

public class CallPrcImportAdcFileImportLoad extends StoredProcedure
{

  public CallPrcImportAdcFileImportLoad( DataSource ds, String prcName )
  {
    super( ds, prcName );

    declareParameter( new SqlParameter( "p_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_total_error_rec", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_import_file_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );

    compile();
  }

  public Map executeProcedure( String fileName, Long contestId )
  {
    HashMap inParams = new HashMap();
    inParams.put( "p_file_name", fileName );
    inParams.put( "p_ssi_contest_id", new BigDecimal( contestId ) );
    inParams.put( "p_in_user_id", UserManager.getUser().getUserId() );
    Map outParams = execute( inParams );

    return outParams;
  }
}
