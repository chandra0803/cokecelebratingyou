
package com.biperf.core.dao.engagement.hibernate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PublicRecognitionCard;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.domain.publicrecognition.PublicRecognitionParticipantView;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.BudgetValueBean;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;

import oracle.jdbc.OracleTypes;

/**
 * 
 * CallPrcEngagementDashboardRecognitionList.
 * 
 * @author kandhi
 * @since Jun 18, 2014
 * @version 1.0
 */
public class CallPrcEngagementDashboardRecognitionList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENG_USER_DASHBOARD";

  public CallPrcEngagementDashboardRecognitionList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // NOTE: Calls to declareParameter must be made in the same order as they appear
    // in the database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_userid", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_Listtype", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_node_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_start_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_rownum_start", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_rownum_end", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new PublicRecognitionResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_result_set_recipients", OracleTypes.CURSOR, new RecipientsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_result_set_pub_likes", OracleTypes.CURSOR, new PublicLikesResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_result_set_pub_comments", OracleTypes.CURSOR, new PublicCommentsResultSetExtractor() ) );

    compile();
  }

  public Map executeProcedure( Map<String, Object> searchParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_userid", searchParameters.get( "userId" ) );
    inParams.put( "p_in_Listtype", searchParameters.get( "type" ) );
    inParams.put( "p_in_node_id", searchParameters.get( "nodeId" ) );
    inParams.put( "p_in_start_date", DateUtils.toDisplayString( (Date)searchParameters.get( "startDate" ), Locale.ENGLISH ) );
    inParams.put( "p_in_end_date", DateUtils.toDisplayString( (Date)searchParameters.get( "endDate" ), Locale.ENGLISH ) );
    inParams.put( "p_rownum_start", searchParameters.get( "rowNumStart" ) );
    inParams.put( "p_rownum_end", searchParameters.get( "rowNumEnd" ) );

    Map<String, Object> outParams = execute( inParams );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode == 0 )
    {
      processRecognitionResults( outParams );

      // remove the secondary result sets from the output; they should now be in the primary list
      outParams.remove( "p_out_result_set_recipients" );
      outParams.remove( "p_out_result_set_pub_likes" );
      outParams.remove( "p_out_result_set_pub_comments" );
    }

    return outParams;
  }

  /**
   * PublicRecognitionResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class PublicRecognitionResultSetExtractor implements ResultSetExtractor
  {

    public List<PublicRecognitionFormattedValueBean> extractData( ResultSet rsPublicRecognitions ) throws SQLException, DataAccessException
    {
      ArrayList<PublicRecognitionFormattedValueBean> publicRecList = new ArrayList<PublicRecognitionFormattedValueBean>();

      while ( rsPublicRecognitions.next() )
      {
        PublicRecognitionFormattedValueBean pubRecBean = new PublicRecognitionFormattedValueBean();

        pubRecBean.setTeamId( rsPublicRecognitions.getLong( "team_id" ) );
        pubRecBean.setTeamName( rsPublicRecognitions.getString( "team_name" ) );
        pubRecBean.setClaimApprovedDate( rsPublicRecognitions.getTimestamp( "date_approved" ) );
        pubRecBean.setSubmitterFirstName( rsPublicRecognitions.getString( "giver_first_name" ) );
        pubRecBean.setSubmitterLastName( rsPublicRecognitions.getString( "giver_last_name" ) );
        pubRecBean.setSubmitterAvatarSmall( rsPublicRecognitions.getString( "giver_avatar" ) );
        pubRecBean.setSubmitterComments( rsPublicRecognitions.getString( "submitter_comments" ) );
        pubRecBean.setSubmitterCommentsLanguageType( LanguageType.getLanguageFrom( rsPublicRecognitions.getString( "submitter_comments_lang_id" ) ) );
        pubRecBean.setReceiverCount( rsPublicRecognitions.getLong( "receiver_count" ) );
        pubRecBean.setSubmitterId( rsPublicRecognitions.getLong( "giver_user_id" ) );
        // add points elig: 11

        // budget data csv: 12
        String budgetData = rsPublicRecognitions.getString( "budget_data" );
        if ( budgetData != null )
        {
          String[] budgetTokens = budgetData.split( "," );
          if ( budgetTokens[0].equalsIgnoreCase( "y" ) && budgetTokens.length == 4 )
          {
            BudgetValueBean budgetValueBean = new BudgetValueBean();
            if ( budgetTokens[1] != null && budgetTokens[1].trim().length() > 0 )
            {
              budgetValueBean.setId( Long.valueOf( budgetTokens[1].trim() ) );
            }
            if ( budgetTokens[2] != null && budgetTokens[2].trim().length() > 0 )
            {
              budgetValueBean.setRemaining( BigDecimal.valueOf( Double.valueOf( budgetTokens[2].trim() ) ) );
            }
            if ( budgetTokens[3] != null )
            {
              budgetValueBean.setName( budgetTokens[3] );
            }
            pubRecBean.getBudgets().add( budgetValueBean );
          }
        }
        pubRecBean.setCountryRatio( rsPublicRecognitions.getDouble( "budget_conversion_ratio" ) );
        pubRecBean.setLikesCount( rsPublicRecognitions.getLong( "likes_count" ) );
        pubRecBean.setPublicCommentsCount( rsPublicRecognitions.getLong( "comments_count" ) );
        pubRecBean.setPromotionId( rsPublicRecognitions.getLong( "promotion_id" ) );
        pubRecBean.setPromotionName( rsPublicRecognitions.getString( "promotion_name" ) );
        pubRecBean.setPromotionType( rsPublicRecognitions.getString( "promotion_type" ) );
        pubRecBean.setIncludePurl( rsPublicRecognitions.getBoolean( "is_include_purl" ) );
        pubRecBean.setPurlRecipientId( rsPublicRecognitions.getLong( "purl_recipient_id" ) );
        if ( "Y".equals( rsPublicRecognitions.getString( "add_points_elig" ) ) )
        {
          pubRecBean.setAllowAddPoints( rsPublicRecognitions.getBoolean( "allow_public_recog_points" ) );
        }
        pubRecBean.setAwardAmountFixed( rsPublicRecognitions.getBoolean( "public_rec_award_type_fixed" ) );
        pubRecBean.setAwardAmountMin( rsPublicRecognitions.getLong( "public_rec_award_amount_min" ) );
        pubRecBean.setAwardAmountMax( rsPublicRecognitions.getLong( "public_rec_award_amount_max" ) );
        pubRecBean.setFixedAwardAmount( rsPublicRecognitions.getLong( "public_rec_award_amount_fixed" ) );
        pubRecBean.seteCardUsed( rsPublicRecognitions.getBoolean( "card_active" ) );
        if ( pubRecBean.iseCardUsed() )
        {
          PublicRecognitionCard eCard = new PublicRecognitionCard();
          eCard.setId( rsPublicRecognitions.getLong( "card_id" ) );
          eCard.setImgUrl( rsPublicRecognitions.getString( "card_image_name" ) );
          pubRecBean.setCard( eCard );
        }

        publicRecList.add( pubRecBean );
      }

      return publicRecList;
    }
  }

  /**
   * PublicRecognitionResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class RecipientsResultSetExtractor implements ResultSetExtractor
  {

    public List<PublicRecognitionParticipantView> extractData( ResultSet rsRecipients ) throws SQLException, DataAccessException
    {
      ArrayList<PublicRecognitionParticipantView> recipients = new ArrayList<PublicRecognitionParticipantView>();

      while ( rsRecipients.next() )
      {
        PublicRecognitionParticipantView recipient = new PublicRecognitionParticipantView();

        recipient.setTeamId( rsRecipients.getLong( "team_id" ) );
        recipient.setClaimId( rsRecipients.getLong( "claim_id" ) );
        recipient.setId( rsRecipients.getLong( "recvr_user_id" ) );
        recipient.setFirstName( rsRecipients.getString( "recvr_first_name" ) );
        recipient.setLastName( rsRecipients.getString( "recvr_last_name" ) );
        recipient.setAvatarSmall( rsRecipients.getString( "recvr_avatar" ) );
        recipient.setOptOutAwards( rsRecipients.getBoolean( "is_opt_out_of_awards" ) );

        recipients.add( recipient );
      }

      return recipients;
    }
  }

  /**
   * PublicRecognitionResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class PublicLikesResultSetExtractor implements ResultSetExtractor
  {

    public List<PublicRecognitionLike> extractData( ResultSet rsPublicRecLikes ) throws SQLException, DataAccessException
    {
      ArrayList<PublicRecognitionLike> publicLikesList = new ArrayList<PublicRecognitionLike>();

      while ( rsPublicRecLikes.next() )
      {
        PublicRecognitionLike recLike = new PublicRecognitionLike();

        recLike.setTeamId( rsPublicRecLikes.getLong( "team_id" ) );

        RecognitionClaim claim = new RecognitionClaim();
        claim.setId( rsPublicRecLikes.getLong( "claim_id" ) );
        recLike.setClaim( claim );

        Participant likerPax = new Participant();
        likerPax.setId( rsPublicRecLikes.getLong( "liker_user_id" ) );
        likerPax.setFirstName( rsPublicRecLikes.getString( "liker_first_name" ) );
        likerPax.setLastName( rsPublicRecLikes.getString( "liker_last_name" ) );
        recLike.setUser( likerPax );

        publicLikesList.add( recLike );
      }

      return publicLikesList;
    }
  }

  /**
   * PublicRecognitionResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class PublicCommentsResultSetExtractor implements ResultSetExtractor
  {

    public List<PublicRecognitionComment> extractData( ResultSet rsPublicRecComments ) throws SQLException, DataAccessException
    {
      ArrayList<PublicRecognitionComment> publicCommentList = new ArrayList<PublicRecognitionComment>();

      while ( rsPublicRecComments.next() )
      {
        PublicRecognitionComment publicComment = new PublicRecognitionComment();

        publicComment.setTeamId( rsPublicRecComments.getLong( "team_id" ) );

        Claim claim = new RecognitionClaim();
        claim.setId( rsPublicRecComments.getLong( "claim_id" ) );
        publicComment.setClaim( claim );

        publicComment.setComments( rsPublicRecComments.getString( "public_comments" ) );
        publicComment.setCommentsLanguageType( LanguageType.lookup( rsPublicRecComments.getString( "public_comments_language" ) ) );

        Participant commenter = new Participant();
        commenter.setId( rsPublicRecComments.getLong( "commenter_user_id" ) );
        commenter.setFirstName( rsPublicRecComments.getString( "commenter_first_name" ) );
        commenter.setLastName( rsPublicRecComments.getString( "commenter_last_name" ) );
        publicComment.setUser( commenter );

        publicComment.setId( rsPublicRecComments.getLong( "public_recognition_comment_id" ) );

        publicCommentList.add( publicComment );
      }

      return publicCommentList;
    }
  }

  @SuppressWarnings( "unchecked" )
  private void processRecognitionResults( Map<String, Object> recognitionResults )
  {
    if ( recognitionResults == null )
    {
      return;
    }

    ArrayList<PublicRecognitionFormattedValueBean> recognitionList;
    ArrayList<PublicRecognitionParticipantView> recipientList;
    ArrayList<PublicRecognitionLike> publicLikesList;
    ArrayList<PublicRecognitionComment> publicCommentsList;

    recognitionList = (ArrayList<PublicRecognitionFormattedValueBean>)recognitionResults.get( "p_out_result_set" );
    recipientList = (ArrayList<PublicRecognitionParticipantView>)recognitionResults.get( "p_out_result_set_recipients" );
    publicLikesList = (ArrayList<PublicRecognitionLike>)recognitionResults.get( "p_out_result_set_pub_likes" );
    publicCommentsList = (ArrayList<PublicRecognitionComment>)recognitionResults.get( "p_out_result_set_pub_comments" );

    for ( PublicRecognitionFormattedValueBean recBean : recognitionList )
    {
      if ( recBean.getReceiverCount() > 0 )
      {
        recBean.setRecipients( getPublicRecognitionRecipients( recBean.getTeamId(), recipientList ) );
        if ( recBean.getRecipients().size() > 0 )
        {
          PublicRecognitionParticipantView recipient = recBean.getRecipients().get( 0 );
          recBean.setClaimId( recipient.getClaimId() );
          recBean.setRecipientId( recipient.getId() );
          recBean.setRecipientFirstName( recipient.getFirstName() );
          recBean.setRecipientLastName( recipient.getLastName() );
          recBean.setRecipientAvatarSmall( recipient.getAvatarSmall() );
        }
      }

      if ( recBean.getLikesCount() > 0 )
      {
        recBean.setUserLikes( getPublicRecognitionLikes( recBean.getTeamId(), publicLikesList ) );
      }

      if ( recBean.getPublicCommentsCount() > 0 )
      {
        recBean.setUserComments( getPublicRecognitionCommments( recBean.getTeamId(), publicCommentsList ) );
      }

    }

  }

  private List<PublicRecognitionParticipantView> getPublicRecognitionRecipients( Long teamId, List<PublicRecognitionParticipantView> allRecipients )
  {
    ArrayList<PublicRecognitionParticipantView> recipients = new ArrayList<PublicRecognitionParticipantView>();

    if ( teamId == null || allRecipients == null )
    {
      return null;
    }

    for ( PublicRecognitionParticipantView recipient : allRecipients )
    {
      if ( recipient.getTeamId().equals( teamId ) )
      {
        recipients.add( recipient );
      }
    }

    return recipients;
  }

  private List<PublicRecognitionLike> getPublicRecognitionLikes( Long teamId, List<PublicRecognitionLike> allPublicLikesList )
  {
    ArrayList<PublicRecognitionLike> recognitionLikes = new ArrayList<PublicRecognitionLike>();

    if ( teamId == null || allPublicLikesList == null )
    {
      return null;
    }

    for ( PublicRecognitionLike recLike : allPublicLikesList )
    {
      if ( recLike.getTeamId().equals( teamId ) )
      {
        recognitionLikes.add( recLike );
      }
    }

    return recognitionLikes;
  }

  private List<PublicRecognitionComment> getPublicRecognitionCommments( Long teamId, List<PublicRecognitionComment> allPublicCommentList )
  {
    ArrayList<PublicRecognitionComment> recognitionCommentsList = new ArrayList<PublicRecognitionComment>();

    if ( teamId == null || allPublicCommentList == null )
    {
      return null;
    }

    for ( PublicRecognitionComment recognitionComment : allPublicCommentList )
    {
      if ( recognitionComment.getTeamId().equals( teamId ) )
      {
        recognitionCommentsList.add( recognitionComment );
      }
    }

    return recognitionCommentsList;
  }

}
