
package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.nomination.NominationReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcNominationTopNomineeReport extends StoredProcedure
{

  public CallPrcNominationTopNomineeReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_parent_node_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_job_type_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotion_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participant_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale_date_pattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_top_n_count", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationTopNomineeReport.TopNomineeMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", reportParameters.get( "userId" ) );
    inParams.put( "p_in_parent_node_id_list", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_department_list", reportParameters.get( "department" ) );
    inParams.put( "p_in_job_type_list", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_country_id_list", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_promotion_id_list", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_participant_status", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_locale_date_pattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_top_n_count", 20 );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class TopNomineeMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationReportValue> reportData = new ArrayList<NominationReportValue>();

      while ( rs.next() )
      {
        NominationReportValue reportValue = new NominationReportValue();
        reportValue.setNominee( rs.getString( "NOMINATEE" ) );
        reportValue.setNominatedCnt( rs.getLong( "NOMINATED_CNT" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }
  }

}
