
package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.utils.UserManager;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationDetailExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_EXTRACTS.PRC_NOMINATION_DTL_EXTRACT";

  public CallPrcNominationDetailExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_parent_node_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_job_type_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotion_id_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nomi_status_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participant_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_is_team", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_include_not_given", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_include_comment", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_has_given", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_has_recvd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_viewer_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_header", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcNominationDetailExtract.DataMapper() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_parent_node_id_list", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_department_list", reportParameters.get( "department" ) );
    inParams.put( "p_in_job_type_list", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_country_id_list", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_promotion_id_list", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_nomi_status_list", null ); // Not a filter param - null will become 'all'
    inParams.put( "p_in_participant_status", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_locale", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_is_team", ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() ? 0 : 1 );
    inParams.put( "p_in_has_given", ( (Boolean)reportParameters.get( "hasGiven" ) ).booleanValue() ? 1 : 0 );
    inParams.put( "p_in_has_recvd", ( (Boolean)reportParameters.get( "hasReceived" ) ).booleanValue() ? 1 : 0 );

    if ( reportParameters.get( "nomCommentAvailable" ) != null )
    {
      if ( reportParameters.get( "nomCommentAvailable" ).equals( "yes" ) )
      {
        inParams.put( "p_in_include_comment", 1 );
      }
      else
      {
        inParams.put( "p_in_include_comment", 0 );
      }
    }
    else
    {
      inParams.put( "p_in_include_comment", 0 );
    }

    inParams.put( "p_in_viewer_user_id", UserManager.getUserId() );
    inParams.put( "p_in_user_id", reportParameters.get( "userId" ) );
    inParams.put( "p_in_file_name", reportParameters.get( "internalFilename" ) );
    inParams.put( "p_in_header", reportParameters.get( "csHeaders" ) );
    inParams.put( "p_in_include_not_given", ( (Boolean)reportParameters.get( "includeNotGiven" ) ).booleanValue() ? 1 : 0 );
    Map outParams = execute( inParams );
    return outParams;
  }

  /**
   * DataMapper is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class DataMapper implements RowMapper
  {

    @Override
    public String mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }
}
