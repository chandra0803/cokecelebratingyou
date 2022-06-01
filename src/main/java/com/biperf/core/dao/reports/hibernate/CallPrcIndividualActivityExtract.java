
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

import oracle.jdbc.OracleTypes;

public class CallPrcIndividualActivityExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_EXTRACTS.PRC_INDIVIDUAL_ACT_EXTRACT";

  public CallPrcIndividualActivityExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_summary", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_allAwards", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_onTheSpot", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_goalquest", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_challengepoint", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_nominationsReceived", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_nominationsGiven", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_productClaims", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_recognitionsGiven", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_recognitionsReceived", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_quizSummary", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_quizDetail", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_throwdown", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_badge", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_ssi_contests", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header_ssi_contests_sec", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_module_type", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new DataMapper() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_user_id", reportParameters.get( "paxId" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "locale", reportParameters.get( "languageCode" ) );
    inParams.put( "p_file_name", reportParameters.get( "internalFilename" ) );
    inParams.put( "p_header_summary", reportParameters.get( "csHeaders_summary" ) );
    inParams.put( "p_header_allAwards", reportParameters.get( "csHeaders_allAwards" ) );
    inParams.put( "p_header_onTheSpot", reportParameters.get( "csHeaders_onTheSpot" ) );
    inParams.put( "p_header_goalquest", reportParameters.get( "csHeaders_goalquest" ) );
    inParams.put( "p_header_challengepoint", reportParameters.get( "csHeaders_challengepoint" ) );
    inParams.put( "p_header_nominationsReceived", reportParameters.get( "csHeaders_nominationsReceived" ) );
    inParams.put( "p_header_nominationsGiven", reportParameters.get( "csHeaders_nominationsGiven" ) );
    inParams.put( "p_header_productClaims", reportParameters.get( "csHeaders_productClaims" ) );
    inParams.put( "p_header_recognitionsGiven", reportParameters.get( "csHeaders_recognitionsGiven" ) );
    inParams.put( "p_header_recognitionsReceived", reportParameters.get( "csHeaders_recognitionsReceived" ) );
    inParams.put( "p_header_quizSummary", reportParameters.get( "csHeaders_quizSummary" ) );
    inParams.put( "p_header_quizDetail", reportParameters.get( "csHeaders_quizDetail" ) );
    inParams.put( "p_header_throwdown", reportParameters.get( "csHeaders_throwdown" ) );
    inParams.put( "p_header_badge", reportParameters.get( "csHeaders_badge" ) );
    inParams.put( "p_header_ssi_contests", reportParameters.get( "csHeaders_ssicontests" ) );
    inParams.put( "p_header_ssi_contests_sec", reportParameters.get( "csHeaders_ssicontests_sec" ) );
    if ( reportParameters.get( "exportLevel" ) != null && "summary".equals( (String)reportParameters.get( "exportLevel" ) ) )
    {
      inParams.put( "p_module_type", null );
    }
    else
    {
      inParams.put( "p_module_type", reportParameters.get( "exportLevel" ) );
    }

    Map outParams = execute( inParams );
    return outParams;
  }

  /**
   * DataMapper is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class DataMapper implements RowMapper
  {

    public String mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      return rs.getString( 1 );
    }
  }
}
