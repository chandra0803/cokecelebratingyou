
package com.biperf.core.dao.client.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * 
 * AwbqCertCostCtrSP.
 * 
 * @author bethke
 * @since Dec 3, 2013
 */
public class AwbqCertCostCtrSP extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PRC_ADIH_AWBQ_CERT_COST_CTR";

  /**
   * PayrollCashFileExtractSP constructor.
   * 
   * @param ds
   */
  public AwbqCertCostCtrSP( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_cert_nbr", Types.VARCHAR ) );
   /* declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );*/
    declareParameter( new SqlOutParameter( "p_out_billing_code_1", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_billing_code_2", Types.VARCHAR ) );
    compile();
  }

  public Map executeProcedure( String certNumber, Long userId )
  {
    HashMap inParams = new HashMap();
    /*inParams.put( "p_in_user_id", userId );*/
    inParams.put( "p_in_cert_nbr", certNumber );
    Map outParams = execute( inParams );
    return outParams;
  }

}
