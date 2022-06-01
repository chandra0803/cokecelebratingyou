/**
 * 
 */

package com.biperf.core.dao.awardgenerator.hibernate;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * @author poddutur
 *
 */
public class CallPrcGenerateAndSaveBatch extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_award_generator";

  public CallPrcGenerateAndSaveBatch( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_award_generator_id", Types.NUMERIC ) );
    declareParameter( new SqlInOutParameter( "p_award_generator_batch_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_batch_start_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_batch_end_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_use_issue_date", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_issue_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_award_type", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_notify_manager", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> awardGenParams )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_award_generator_id", awardGenParams.get( "awardGeneratorId" ) );
    inParams.put( "p_award_generator_batch_id", awardGenParams.get( "awardGeneratorBatchId" ) );
    inParams.put( "p_batch_start_date", awardGenParams.get( "batchStartDate" ) );
    inParams.put( "p_batch_end_date", awardGenParams.get( "batchEndDate" ) );
    if ( ( (Boolean)awardGenParams.get( "useIssueDate" ) ).booleanValue() )
    {
      inParams.put( "p_use_issue_date", 1 );
    }
    else
    {
      inParams.put( "p_use_issue_date", 0 );
    }
    inParams.put( "p_issue_date", awardGenParams.get( "issueDate" ) );
    inParams.put( "p_award_type", awardGenParams.get( "awardType" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

}
