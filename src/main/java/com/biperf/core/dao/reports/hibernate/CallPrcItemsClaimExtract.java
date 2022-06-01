/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.claim.ClaimDynamicReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcItemsClaimExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_EXTRACTS.PRC_CLAIM_REPORT_EXTRACT";

  public CallPrcItemsClaimExtract( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotion_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_claim_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_submitted", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromdate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_todate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_organization_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_filterpromo", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_country_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_participant_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_job_position", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_department", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_header", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new DataMapper() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public Map executeProcedure( Map<String, Object> reportParameters )
  {
    HashMap inParams = new HashMap();
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_promotion_status", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_claim_status", reportParameters.get( "claimStatus" ) );
    inParams.put( "p_in_submitted", reportParameters.get( "submittedType" ) );
    inParams.put( "p_in_fromdate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_todate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_organization_id", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_filterpromo", null );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_country_id", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_participant_status", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_job_position", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_department", reportParameters.get( "department" ) );
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
    public List<ClaimDynamicReportValue> mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      ResultSetMetaData rsmd = rs.getMetaData();
      int numCols = rsmd.getColumnCount();
      List<ClaimDynamicReportValue> row = new ArrayList<ClaimDynamicReportValue>();
      for ( int i = 1; i <= numCols; i++ )
      {
        ClaimDynamicReportValue reportValue = new ClaimDynamicReportValue();
        reportValue.setColumnName( rsmd.getColumnName( i ) );
        reportValue.setValue( rs.getString( i ) );
        row.add( reportValue );
      }
      return row;
    }
  }

}
