
package com.biperf.core.dao.publicrecognitionwall;

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

import com.biperf.core.domain.promotion.PublicRecognitionCard;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallBean;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallGiverBean;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallReceiverBean;

import oracle.jdbc.OracleTypes;

public class CallPrcPublicRecognitionWallList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_ccs_recognition_wall";

  public CallPrcPublicRecognitionWallList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_number_of_records", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcPublicRecognitionWallList.GiversResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_result_set_recipients", OracleTypes.CURSOR, new CallPrcPublicRecognitionWallList.ReceiversResultSetExtractor() ) );
    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Map<String, Object> searchParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_number_of_records", searchParameters.get( "pageCount" ) );
    Map<String, Object> outParams = execute( inParams );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode == 0 )
    {
      processRecognitionResults( outParams );
      // remove the secondary resultsets from the output; they should now
      // be in the primary list
      outParams.remove( "p_out_result_set_recipients" );
    }

    return outParams;
  }

  /**
   * GiversResultSetExtractor is an Inner class which implements the
   * RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class GiversResultSetExtractor implements ResultSetExtractor
  {
    @Override
    public List<PublicRecognitionWallBean> extractData( ResultSet rsGviersList ) throws SQLException, DataAccessException
    {
      List<PublicRecognitionWallBean> publicRecognitionWallBeanList = new ArrayList<PublicRecognitionWallBean>();

      while ( rsGviersList.next() )
      {
        PublicRecognitionWallBean publicRecognitionWallBean = new PublicRecognitionWallBean();
        PublicRecognitionWallGiverBean publicRecognitionWallGiverBean = new PublicRecognitionWallGiverBean();
        publicRecognitionWallGiverBean.setGiverId( rsGviersList.getLong( "TEAM_ID" ) );
        publicRecognitionWallGiverBean.setGiverTeamName( rsGviersList.getString( "TEAM_NAME" ) );
        publicRecognitionWallGiverBean.setDateApproved( rsGviersList.getTimestamp( "DATE_APPROVED" ) );
        publicRecognitionWallGiverBean.setGiverFirstName( rsGviersList.getString( "GIVER_FIRST_NAME" ) );
        publicRecognitionWallGiverBean.setGiverUserId( rsGviersList.getLong( "GIVER_USER_ID" ) );
        publicRecognitionWallGiverBean.setGiverLastName( rsGviersList.getString( "GIVER_LAST_NAME" ) );
        publicRecognitionWallGiverBean.setGiverAvatarURL( rsGviersList.getString( "GIVER_AVATAR" ) );
        publicRecognitionWallGiverBean.setGiverCountry( rsGviersList.getString( "GIVER_COUNTRY" ) );
        publicRecognitionWallGiverBean.setGiverOrgUnit( rsGviersList.getString( "GIVER_ORG_UNIT" ) );
        publicRecognitionWallGiverBean.setGiverLocation( rsGviersList.getString( "GIVER_DEPARTMENT" ) );
        publicRecognitionWallGiverBean.setGiverJobTitle( rsGviersList.getString( "GIVER_JOB_TITLE" ) );

        publicRecognitionWallBean.setPublicRecognitionWallGiverBean( publicRecognitionWallGiverBean );

        publicRecognitionWallBean.setComments( rsGviersList.getString( "SUBMITTER_COMMENTS" ) );
        publicRecognitionWallBean.setGiverCommentLangId( rsGviersList.getString( "SUBMITTER_COMMENTS_LANG_ID" ) );
        publicRecognitionWallBean.setReceiverCount( rsGviersList.getLong( "RECEIVER_COUNT" ) );
        publicRecognitionWallBean.setPromotionName( rsGviersList.getString( "PROMOTION_NAME" ) );
        publicRecognitionWallBean.setPromotionType( rsGviersList.getString( "PROMOTION_TYPE" ) );
        publicRecognitionWallBean.setIsIncludPurl( rsGviersList.getBoolean( "IS_INCLUDE_PURL" ) );
        publicRecognitionWallBean.setPurlRecipientId( rsGviersList.getLong( "PURL_RECIPIENT_ID" ) );

        PublicRecognitionCard eCard = new PublicRecognitionCard();
        eCard.setId( rsGviersList.getLong( "CARD_ID" ) );
        eCard.setImgUrl( rsGviersList.getString( "CARD_IMAGE_NAME" ) );
        publicRecognitionWallBean.seteCard( eCard );

        publicRecognitionWallBean.setHidePubRecognition( rsGviersList.getBoolean( "HIDE_PUBLIC_RECOGNITION" ) );
        publicRecognitionWallBean.setIsCumulative( rsGviersList.getBoolean( "IS_CUMULATIVE" ) );
        publicRecognitionWallBean.setBehavior( rsGviersList.getString( "BEHAVIOR" ) );

        publicRecognitionWallBeanList.add( publicRecognitionWallBean );
      }
      return publicRecognitionWallBeanList;
    }
  }

  /**
   * GiversResultSetExtractor is an Inner class which implements the
   * RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class ReceiversResultSetExtractor implements ResultSetExtractor
  {
    @Override
    public List<PublicRecognitionWallReceiverBean> extractData( ResultSet receiversResultSet ) throws SQLException, DataAccessException
    {
      List<PublicRecognitionWallReceiverBean> publicRecognitionWallReceiverBeanList = new ArrayList<PublicRecognitionWallReceiverBean>();
      while ( receiversResultSet.next() )
      {
        PublicRecognitionWallReceiverBean publicRecognitionWallReceiverBean = new PublicRecognitionWallReceiverBean();
        publicRecognitionWallReceiverBean.setGiverteamId( receiversResultSet.getLong( "TEAM_ID" ) );
        publicRecognitionWallReceiverBean.setClaimId( receiversResultSet.getLong( "CLAIM_ID" ) );
        publicRecognitionWallReceiverBean.setReceiverUserId( receiversResultSet.getLong( "RECVR_USER_ID" ) );
        publicRecognitionWallReceiverBean.setReceiverFirstName( receiversResultSet.getString( "RECVR_FIRST_NAME" ) );
        publicRecognitionWallReceiverBean.setReceiverLastName( receiversResultSet.getString( "RECVR_LAST_NAME" ) );
        publicRecognitionWallReceiverBean.setReceiverAvatarURL( receiversResultSet.getString( "RECVR_AVATAR" ) );
        publicRecognitionWallReceiverBean.setReceiverJobTitle( receiversResultSet.getString( "RECEIVER_JOB_TITLE" ) );
        publicRecognitionWallReceiverBean.setReceiverLocation( receiversResultSet.getString( "RECEIVER_DEPARTMENT" ) );
        publicRecognitionWallReceiverBean.setReceiverCountry( receiversResultSet.getString( "RECEIVER_COUNTRY" ) );
        publicRecognitionWallReceiverBean.setReceiverOrgUnit( receiversResultSet.getNString( "RECEIVER_ORG_UNIT" ) );
        publicRecognitionWallReceiverBeanList.add( publicRecognitionWallReceiverBean );
      }

      return publicRecognitionWallReceiverBeanList;
    }
  }

  @SuppressWarnings( "unchecked" )
  private void processRecognitionResults( Map<String, Object> publicRecognitionWallBeanResults )
  {
    if ( publicRecognitionWallBeanResults == null )
    {
      return;
    }
    List<PublicRecognitionWallBean> publicRecognitionWallBeanList = (List<PublicRecognitionWallBean>)publicRecognitionWallBeanResults.get( "p_out_result_set" );

    List<PublicRecognitionWallReceiverBean> publicRecognitionWallReceiverBeanList = (List<PublicRecognitionWallReceiverBean>)publicRecognitionWallBeanResults.get( "p_out_result_set_recipients" );

    for ( PublicRecognitionWallBean publicRecognitionWallBean : publicRecognitionWallBeanList )
    {

      publicRecognitionWallBean.setPublicRecognitionWallReceiverBean( getPublicRecognitionWallReceiverList( publicRecognitionWallBean.getPublicRecognitionWallGiverBean().getGiverId(),
                                                                                                            publicRecognitionWallReceiverBeanList ) );

    }
  }

  private List<PublicRecognitionWallReceiverBean> getPublicRecognitionWallReceiverList( Long teamId, List<PublicRecognitionWallReceiverBean> publicRecognitionWallReceiverBeanList )
  {
    List<PublicRecognitionWallReceiverBean> receiverSubList = new ArrayList<PublicRecognitionWallReceiverBean>();

    if ( teamId == null || publicRecognitionWallReceiverBeanList == null )
    {
      return null;
    }

    for ( PublicRecognitionWallReceiverBean publicRecognitionWallReceiverBean : publicRecognitionWallReceiverBeanList )
    {
      if ( publicRecognitionWallReceiverBean.getGiverteamId().equals( teamId ) )
      {
        receiverSubList.add( publicRecognitionWallReceiverBean );
      }
    }
    return receiverSubList;
  }
}
