
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

public class CallPrcLoginExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_EXTRACTS.PRC_LOGIN_EXTRACT";

  public CallPrcLoginExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_pax_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_paxid", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "Is_team", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_role", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country_Ids", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "login_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcLoginExtract.DataMapper() ) );

    compile();
  }

  public Map executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_pax_status", reportParameters.get( "participantStatus" ) );
    if ( "pax".equals( reportParameters.get( "exportLevel" ) ) )
    {
      inParams.put( "p_paxid", reportParameters.get( "userId" ) );
    }
    else
    {
      inParams.put( "p_paxid", null );
    }
    if ( ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() )
    {
      inParams.put( "Is_team", 0 );
    }
    else
    {
      inParams.put( "Is_team", 1 );
    }

    inParams.put( "p_in_role", reportParameters.get( "role" ) );
    inParams.put( "p_in_department", reportParameters.get( "department" ) );
    inParams.put( "p_in_country_Ids", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "login_type", reportParameters.get( "loginType" ) );
    inParams.put( "locale", reportParameters.get( "languageCode" ) );
    inParams.put( "p_file_name", reportParameters.get( "internalFilename" ) );
    inParams.put( "p_header", reportParameters.get( "csHeaders" ) );
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
