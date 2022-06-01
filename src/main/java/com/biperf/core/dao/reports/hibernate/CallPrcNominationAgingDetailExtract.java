
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

import com.biperf.core.domain.enums.GiverReceiverType;
import com.biperf.core.domain.enums.NominationAudienceType;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationAgingDetailExtract extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_EXTRACTS.PRC_NOMI_AGING_DTL_EXTRACT";

  public CallPrcNominationAgingDetailExtract( DataSource ds )
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
    declareParameter( new SqlParameter( "p_in_approval_level_list", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participant_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_giver_recvr_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_from_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_to_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_file_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_header", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcNominationAgingDetailExtract.DataMapper() ) );

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
    inParams.put( "p_in_nomi_status_list", reportParameters.get( "nominationApprovalStatus" ) );
    inParams.put( "p_in_approval_level_list", reportParameters.get( "nominationApprovalLevel" ) );
    inParams.put( "p_in_participant_status", reportParameters.get( "participantStatus" ) );

    if ( NominationAudienceType.NOMINATOR.equals( reportParameters.get( "nominationAudienceType" ) ) )
    {
      inParams.put( "p_in_giver_recvr_type", GiverReceiverType.GIVER );
    }
    else if ( NominationAudienceType.NOMINEE.equals( reportParameters.get( "nominationAudienceType" ) ) )
    {
      inParams.put( "p_in_giver_recvr_type", GiverReceiverType.RECEIVER );
    }
    else
    {
      inParams.put( "p_in_giver_recvr_type", reportParameters.get( "nominationAudienceType" ) );
    }

    inParams.put( "p_in_from_date", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_to_date", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_locale", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_file_name", reportParameters.get( "internalFilename" ) );
    inParams.put( "p_in_header", reportParameters.get( "csHeaders" ) );

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
