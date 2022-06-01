
package com.biperf.core.dao.purl.hibernate;

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

import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.purl.PurlCelebrationsView;

import oracle.jdbc.OracleTypes;

/**
 * 
 * CallPrcFetchRecognitionPurlsList.
 * 
 * @author johnch
 * @since May 19, 2016
 * @version 1.0
 */
public class CallPrcFetchRecognitionPurlsList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_PURL_CELEBRATION_PAGE";

  public CallPrcFetchRecognitionPurlsList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // NOTE: Calls to declareParameter must be made in the same order as
    // they appear
    // in the database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_past_present", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_tab_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_list_value", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rownum_start", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_end", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sort_by", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sort_on", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new FetchRecognitionPurlsListExtractor() ) );

    compile();
  }

  public Map executeProcedure( Map<String, Object> searchParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_past_present", searchParameters.get( "pastOrUpcoming" ) );
    inParams.put( "p_in_tab_type", searchParameters.get( "tabType" ) );
    inParams.put( "p_in_list_value", searchParameters.get( "listValue" ) );
    inParams.put( "p_in_locale", searchParameters.get( "userLocale" ) );
    inParams.put( "p_in_user_id", searchParameters.get( "userId" ) );
    inParams.put( "p_in_user_name", searchParameters.get( "lastName" ) );
    inParams.put( "p_in_rownum_start", searchParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rownum_end", searchParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sort_by", searchParameters.get( "sortedBy" ) );
    inParams.put( "p_in_sort_on", searchParameters.get( "sortedOn" ) );

    Map<String, Object> outParams = execute( inParams );

    return outParams;
  }

  /**
   * PublicRecognitionResultSetExtractor is an Inner class which implements
   * the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class FetchRecognitionPurlsListExtractor implements ResultSetExtractor
  {
    public List<PurlCelebrationsView> extractData( ResultSet purlCelebrations ) throws SQLException, DataAccessException
    {
      ArrayList<PurlCelebrationsView> purlCelebrationsList = new ArrayList<PurlCelebrationsView>();

      while ( purlCelebrations.next() )
      {
        PurlCelebrationsView publicComment = new PurlCelebrationsView();
        publicComment.setUserId( purlCelebrations.getLong( "USER_ID" ) );
        publicComment.setRecipientId( purlCelebrations.getLong( "RECIPIENT_ID" ) );
        publicComment.setFirstName( purlCelebrations.getString( "FIRST_NAME" ) );
        publicComment.setLastName( purlCelebrations.getString( "LAST_NAME" ) );
        publicComment.setAnniversary( purlCelebrations.getString( "ANNIVERSARY" ) );
        publicComment.setRecipientAvatar( purlCelebrations.getString( "AVATAR_SMALL" ) );
        publicComment.setPromotionId( purlCelebrations.getLong( "PROMOTION_ID" ) );
        publicComment.setPromotionNameFromCM( purlCelebrations.getString( "PROMOTION_NAME" ) );
        // publicComment.setContributorId( purlCelebrations.getLong(
        // "PURL_CONTRIBUTOR_ID" ) );
        publicComment.setAwardDate( purlCelebrations.getDate( "EXPIRATION_DATE" ) );
        // publicComment.setCfseID( purlCelebrations.getLong(
        // "CLAIM_FORM_STEP_ELEMENT_ID" ) );
        publicComment.setTotalRecords( purlCelebrations.getInt( "total_records" ) );
        publicComment.setPositionType( purlCelebrations.getString( "position_type" ) != null ? PositionType.lookup( purlCelebrations.getString( "position_type" ) ).getName() : "" );
        publicComment.setPrimaryColor( purlCelebrations.getString( "primary_color" ) );
        publicComment.setSecondaryColor( purlCelebrations.getString( "secondary_color" ) );
        publicComment.setCelebrationId( purlCelebrations.getString( "celebration_id" ) );
        publicComment.setCelebLabelCmxCode( purlCelebrations.getString( "cmx_asset_code" ) );
        publicComment.setProgramNameCmxCode( purlCelebrations.getString( "prog_cmx_value" ) );

        purlCelebrationsList.add( publicComment );
      }

      return purlCelebrationsList;
    }
  }

}
