/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/hibernate/PublicRecognitionDAOImpl.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.engagement.hibernate.CallPrcEngagementDashboardRecognitionList;
import com.biperf.core.dao.promotion.PublicRecognitionDAO;
import com.biperf.core.dao.publicrecognition.CallPrcPublicRecognitionList;
import com.biperf.core.dao.publicrecognition.CallPrcSAPublicRecognitionList;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;

/**
 * 
 * @author veeramas
 * @since Jul 24, 2012
 * @version 1.0
 */
public class PublicRecognitionDAOImpl extends BaseDAO implements PublicRecognitionDAO
{
  private NamedParameterJdbcTemplate jdbcTemplate;
  private static final Log log = LogFactory.getLog( PublicRecognitionDAOImpl.class );

  private DataSource dataSource;

  // Use this value to show profile on the pop at once.
  private static final int pageSize = 5;

  /**
   * Save Public Recognition Comment
   * @param savePublicRecognitionComment    
   * @return PublicRecognitionComment
   */
  public PublicRecognitionComment savePublicRecognitionComment( PublicRecognitionComment savePublicRecognitionComment )
  {
    getSession().saveOrUpdate( savePublicRecognitionComment );
    return savePublicRecognitionComment;
  }

  /**
   * Save Public Recognition Like
   * @param savePublicRecognitionLike    
   * @return PublicRecognitionComment
   */
  public PublicRecognitionLike savePublicRecognitionLike( PublicRecognitionLike savePublicRecognitionLike )
  {
    getSession().saveOrUpdate( savePublicRecognitionLike );
    return savePublicRecognitionLike;
  }

  public void deletePublicRecognitionLike( PublicRecognitionLike toDelete )
  {
    getSession().delete( toDelete );
  }

  /**
   * Get All User who commented Claim
   * @param teamId
   * @return PublicRecognitionComment
   */
  public List<PublicRecognitionComment> getUserCommentsByTeam( Long teamId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionComment.userCommentsPerTeam" );
    query.setParameter( "teamId", teamId );
    return query.list();
  }

  /**
   * Get All User who likes Claim
   * @param claimId    
   * @return PublicRecognitionLike
   */
  public List<PublicRecognitionLike> getUserLikesByClaim( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionLike.userLikesPerClaim" );
    query.setParameter( "claimId", claimId );
    return query.list();
  }

  /**
   * Get All User who likes by Team
   * @param teamId
   * @return PublicRecognitionLike
   */
  public List<PublicRecognitionLike> getUserLikesByTeamId( Long teamId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionLike.userLikesByTeam" );
    query.setParameter( "teamId", teamId );
    return query.list();
  }

  /**
   * get Like Count by claimId
   * @param claimId    
   * @return PublicRecognitionComment
   */
  public long getLikeCountByClaimId( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionLike.likeCount" );
    query.setParameter( "claimId", claimId );
    if ( query.uniqueResult() != null )
    {
      return ( (Long)query.uniqueResult() ).longValue();
    }
    else
    {
      return 0;
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  public boolean isCurrentUserLikedClaim( Long claimId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionLike.isCurrentUserLikedClaim" );
    query.setParameter( "claimId", claimId );
    query.setParameter( "userId", userId );
    return ( (Long)query.uniqueResult() ).longValue() > 0;
  }

  public List<PublicRecognitionLike> getLikedPaxListByClaimId( Long claimId, int pageNumber )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionLike.getLikedPaxListByClaimId" );
    query.setParameter( "claimId", claimId );
    query.setParameter( "startIndex", ( pageNumber - 1 ) * pageSize );
    query.setParameter( "lastIndex", ( pageNumber - 1 ) * pageSize + 5 );
    return query.list();
  }

  public int getLikedPaxCount( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionLike.getLikedPaxCount" );
    query.setParameter( "claimId", claimId );
    return ( (Integer)query.uniqueResult() ).intValue();
  }

  /**
   * Get Datasource
   * 
   * @param dataSource    
   * 
   */
  public void setDataSource( DataSource dataSource )
  {
    // TODO what if you want to use a JdbcTemplate by preference,
    // for a native extractor?
    this.jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
    this.dataSource = dataSource;
  }

  public PublicRecognitionComment getPublicRecognitionCommentBy( Long publicRecogntionCommentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionComment.getById" );
    query.setParameter( "id", publicRecogntionCommentId );
    return (PublicRecognitionComment)query.uniqueResult();
  }

  @Override
  public void deletePublicRecognitionComment( PublicRecognitionComment prc )
  {
    getSession().delete( prc );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PublicRecognitionFormattedValueBean> getPublicRecognitionList( Long userId, String listType, int rowNumStart, int rowNumEnd, String listValue )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "userId", userId );
    inParams.put( "listType", listType );
    inParams.put( "rowNumStart", rowNumStart );
    inParams.put( "rowNumEnd", rowNumEnd );
    inParams.put( "listValue", listValue );

    CallPrcPublicRecognitionList procedure = null;
    CallPrcSAPublicRecognitionList saProcedure = null;
    Map<String, Object> outParams = null;

    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      saProcedure = new CallPrcSAPublicRecognitionList( dataSource );
      outParams = saProcedure.executeProcedure( inParams );
    }
    else
    {
      procedure = new CallPrcPublicRecognitionList( dataSource );
      outParams = procedure.executeProcedure( inParams );
    }

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      return null;
    }

    ArrayList<PublicRecognitionFormattedValueBean> publicRecList = (ArrayList<PublicRecognitionFormattedValueBean>)outParams.get( "p_out_result_set" );

    return publicRecList;
  }

  @Override
  public List<PublicRecognitionFormattedValueBean> getDashboardRecognitionClaimsByType( Map<String, Object> queryParams )
  {
    CallPrcEngagementDashboardRecognitionList procedure = new CallPrcEngagementDashboardRecognitionList( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( queryParams );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      return null;
    }
    return (ArrayList<PublicRecognitionFormattedValueBean>)outParams.get( "p_out_result_set" );
  }

  @Override
  public List<PublicRecognitionLike> getUserLikesByTeam( Long teamId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionLike.getUserLikesByTeam" );
    query.setParameter( "teamId", teamId );
    return query.list();
  }

  @Override
  public List<PublicRecognitionComment> getUserCommentsByTeamId( Long teamId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PublicRecognitionComment.getUserCommentsByTeamId" );
    query.setParameter( "teamId", teamId );
    return query.list();
  }

  /**
   * Return a list of public recognition formatted beans that are associated for the specified claim.
   *
   * @param claimId
   * @param userId
   * @return List<PublicRecognitionFormattedValueBean>
   */
  @SuppressWarnings( "unchecked" )
  public List<PublicRecognitionFormattedValueBean> getPublicRecognitionClaimsByClaimId( Long claimId, Long userId )
  {
    if ( claimId == null )
    {
      return new ArrayList<PublicRecognitionFormattedValueBean>();
    }

    StringBuilder sql = new StringBuilder();
    sql.append( " SELECT claim.claim_id                            AS CLAIM_ID, " );
    sql.append( "   claim.submission_date                          AS CLAIM_SUBMISSION_DATE, " );
    sql.append( "   NVL(claim_item.date_approved,cg.date_approved) AS CLAIM_APPROVED_DATE, " );
    sql.append( "   submitter_usr.user_id                          AS SUBMITTER_ID, " );
    sql.append( "   submitter_usr.first_name                       AS SUBMITTER_FIRST_NAME, " );
    sql.append( "   submitter_usr.last_name                        AS SUBMITTER_LAST_NAME, " );
    sql.append( "   submitter_pax.avatar_small                     AS SUBMITTER_AVATAR_SMALL, " );
    sql.append( "   promotion.promotion_id                         AS PROMOTION_ID, " );
    sql.append( "   promotion.promotion_name                       AS PROMOTION_NAME, " );
    sql.append( "   promo_recognition.allow_public_recog_points    AS ALLOW_PUBLIC_RECOG_POINTS, " );
    sql.append( "   NULL                                           AS NOM_CLAIM_ID, " );
    sql.append( "   NULL                                           AS NOM_CLAIM_SUBMITTER_COMMENTS, " );
    sql.append( "   NULL                                           AS NOM_CLAIM_HIDE_PUB_REC, " );
    sql.append( "   NULL                                           AS NOM_CLAIM_TEAM_ID, " );
    sql.append( "   NULL                                           AS NOM_CLAIM_TEAM_NAME, " );
    sql.append( "   recognition_claim.claim_id                     AS REC_CLAIM_ID, " );
    sql.append( "   recognition_claim.submitter_comments           AS REC_CLAIM_SUBMITTER_COMMENTS, " );
    sql.append( "   recognition_claim.hide_public_recognition      AS REC_CLAIM_HIDE_PUB_REC, " );
    sql.append( "   recognition_claim.team_id                      AS REC_CLAIM_TEAM_ID, " );
    sql.append( "   NULL                                           AS CLAIM_PARTICIPANT_ID, " );
    sql.append( "   NULL                                           AS CLAIM_PARTICIPANT_FIRST_NAME, " );
    sql.append( "   NULL                                           AS CLAIM_PARTICIPANT_LAST_NAME, " );
    sql.append( "   NULL                                           AS CLAIM_PARTICIPANT_AVATAR_SMALL, " );
    sql.append( "   recognition_participant.user_id                AS CLAIM_RECIPIENT_ID, " );
    sql.append( "   recognition_usr.first_name                     AS CLAIM_RECIPIENT_FIRST_NAME, " );
    sql.append( "   recognition_usr.last_name                      AS CLAIM_RECIPIENT_LAST_NAME, " );
    sql.append( "   recognition_participant.avatar_small           AS CLAIM_RECIPIENT_AVATAR_SMALL, " );
    sql.append( "   promo_recognition.card_active                  AS E_CARD_USED, " );
    sql.append( "   NULL                                           AS NOM_CLAIM_SUBM_COMMENTS_LANG, " );
    sql.append( "   recognition_claim.submitter_comments_lang_id   AS REC_CLAIM_SUBM_COMMENTS_LANG, " );
    sql.append( "   NULL                                           AS E_CARD_USED_NOMINATION, " );
    sql.append( "   CASE " );
    sql.append( "     WHEN (COUNT(1) OVER() ) > 1 " );
    sql.append( "     THEN 'N' " );
    sql.append( "     ELSE DECODE(promo_recognition.public_rec_audience_type,'allactivepaxaudience','Y',NVL(pa.elig,'N')) " );
    sql.append( "   END ADD_POINTS_ELIGIBLE " );
    sql.append( " FROM claim " );
    sql.append( " INNER JOIN promotion " );
    sql.append( " ON promotion.promotion_id = claim.promotion_id " );
    sql.append( " INNER JOIN claim_item " );
    sql.append( " ON claim_item.claim_id = claim.claim_id " );
    sql.append( " INNER JOIN claim_recipient " );
    sql.append( " ON claim_recipient.claim_item_id = claim_item.claim_item_id " );
    sql.append( " INNER JOIN application_user submitter_usr " );
    sql.append( " ON submitter_usr.user_id = claim.submitter_id " );
    sql.append( " INNER JOIN participant submitter_pax " );
    sql.append( " ON submitter_pax.user_id = claim.submitter_id " );
    sql.append( " INNER JOIN recognition_claim " );
    sql.append( " ON recognition_claim.claim_id=claim.claim_id " );
    sql.append( " INNER JOIN promo_recognition " );
    sql.append( " ON promo_recognition.promotion_id = promotion.promotion_id " );
    sql.append( " LEFT OUTER JOIN " );
    // sql.append( " (SELECT 'Y' AS elig, " );
    sql.append( " (SELECT SUBSTR(PKG_LIST_RECOGNITION_WALL.FNC_CHECK_PUBLIC_RECOG_ELIG(user_id,pa.promotion_id),1,1) AS elig," );
    sql.append( "     pa.promotion_id " );
    sql.append( "   FROM promo_audience pa, " );
    sql.append( "     participant_audience paa " );
    sql.append( "   WHERE promo_audience_type = 'PUBLIC_RECOGNITION' " );
    sql.append( "   AND pa.audience_id        = paa.audience_id " );
    sql.append( "   AND user_id               = :userId " );
    sql.append( "   ) pa " );
    sql.append( " ON pa.promotion_id = promo_recognition.promotion_id " );
    sql.append( " INNER JOIN participant recognition_participant " );
    sql.append( " ON recognition_participant.user_id = claim_recipient.participant_id " );
    sql.append( " INNER JOIN application_user recognition_usr " );
    sql.append( " ON recognition_usr.user_id = recognition_participant.user_id " );
    sql.append( " LEFT OUTER JOIN claim_group cg " );
    sql.append( " ON cg.claim_group_id             = claim.claim_group_id " );
    sql.append( " WHERE recognition_claim.team_id IN " );
    sql.append( "   (SELECT team_id FROM recognition_claim WHERE claim_id = :claimId " );
    sql.append( "   ) " );
    sql.append( " AND recognition_participant.allow_public_recognition = 1 " );
    sql.append( " AND claim_item.approval_status_type                  = 'approv' " );
    sql.append( " AND (claim_recipient.notification_date              IS NULL " );
    sql.append( " OR claim_recipient.notification_date                 <= TRUNC(SYSDATE)) " );
    sql.append( " UNION ALL " );
    sql.append( " SELECT claim.claim_id                            AS CLAIM_ID, " );
    sql.append( "   claim.submission_date                          AS CLAIM_SUBMISSION_DATE, " );
    sql.append( "   NVL(claim_item.date_approved,cg.date_approved) AS CLAIM_APPROVED_DATE, " );
    sql.append( "   submitter_usr.user_id                          AS SUBMITTER_ID, " );
    sql.append( "   submitter_usr.first_name                       AS SUBMITTER_FIRST_NAME, " );
    sql.append( "   submitter_usr.last_name                        AS SUBMITTER_LAST_NAME, " );
    sql.append( "   submitter_pax.avatar_small                     AS SUBMITTER_AVATAR_SMALL, " );
    sql.append( "   promotion.promotion_id                         AS PROMOTION_ID, " );
    sql.append( "   promotion.promotion_name                       AS PROMOTION_NAME, " );
    sql.append( "   NULL                                           AS ALLOW_PUBLIC_RECOG_POINTS, " );
    sql.append( "   nomination_claim.claim_id                      AS NOM_CLAIM_ID, " );
    sql.append( "   nomination_claim.submitter_comments            AS NOM_CLAIM_SUBMITTER_COMMENTS, " );
    sql.append( "   nomination_claim.hide_public_recognition       AS NOM_CLAIM_HIDE_PUB_REC, " );
    sql.append( "   nomination_claim.team_id                       AS NOM_CLAIM_TEAM_ID, " );
    sql.append( "   nomination_claim.team_name                     AS NOM_CLAIM_TEAM_NAME, " );
    sql.append( "   NULL                                           AS REC_CLAIM_ID, " );
    sql.append( "   NULL                                           AS REC_CLAIM_SUBMITTER_COMMENTS, " );
    sql.append( "   NULL                                           AS REC_CLAIM_HIDE_PUB_REC, " );
    sql.append( "   NULL                                           AS REC_CLAIM_TEAM_ID, " );
    sql.append( "   nomination_participant.user_id                 AS CLAIM_PARTICIPANT_ID, " );
    sql.append( "   nomination_usr.first_name                      AS CLAIM_PARTICIPANT_FIRST_NAME, " );
    sql.append( "   nomination_usr.last_name                       AS CLAIM_PARTICIPANT_LAST_NAME, " );
    sql.append( "   nomination_participant.avatar_small            AS CLAIM_PARTICIPANT_AVATAR_SMALL, " );
    sql.append( "   NULL                                           AS CLAIM_RECIPIENT_ID, " );
    sql.append( "   NULL                                           AS CLAIM_RECIPIENT_FIRST_NAME, " );
    sql.append( "   NULL                                           AS CLAIM_RECIPIENT_LAST_NAME, " );
    sql.append( "   NULL                                           AS CLAIM_RECIPIENT_AVATAR_SMALL, " );
    sql.append( "   NULL                                           AS E_CARD_USED, " );
    sql.append( "   nomination_claim.submitter_comments_lang_id    AS NOM_CLAIM_SUBM_COMMENTS_LANG, " );
    sql.append( "   NULL                                           AS REC_CLAIM_SUBM_COMMENTS_LANG, " );
    sql.append( "   promo_nomination.card_active                   AS E_CARD_USED_NOMINATION, " );
    sql.append( "   'N' ADD_POINTS_ELIGIBLE " );
    sql.append( " FROM claim " );
    sql.append( " INNER JOIN promotion " );
    sql.append( " ON promotion.promotion_id = claim.promotion_id " );
    sql.append( " INNER JOIN claim_item " );
    sql.append( " ON claim_item.claim_id = claim.claim_id " );
    sql.append( " INNER JOIN claim_recipient " );
    sql.append( " ON claim_recipient.claim_item_id = claim_item.claim_item_id " );
    sql.append( " INNER JOIN application_user submitter_usr " );
    sql.append( " ON submitter_usr.user_id = claim.submitter_id " );
    sql.append( " INNER JOIN participant submitter_pax " );
    sql.append( " ON submitter_pax.user_id = claim.submitter_id " );
    sql.append( " INNER JOIN nomination_claim " );
    sql.append( " ON nomination_claim.claim_id=claim.claim_id " );
    sql.append( " INNER JOIN promo_nomination " );
    sql.append( " ON promo_nomination.promotion_id = promotion.promotion_id " );
    sql.append( " INNER JOIN participant nomination_participant " );
    sql.append( " ON nomination_participant.user_id = claim_recipient.participant_id " );
    sql.append( " INNER JOIN application_user nomination_usr " );
    sql.append( " ON nomination_usr.user_id = nomination_participant.user_id " );
    sql.append( " LEFT OUTER JOIN claim_group cg " );
    sql.append( " ON cg.claim_group_id            = claim.claim_group_id " );
    sql.append( " WHERE nomination_claim.team_id IN " );
    sql.append( "   (SELECT team_id FROM nomination_claim WHERE claim_id = :claimId " );
    sql.append( "   ) " );
    sql.append( " AND nomination_participant.allow_public_recognition = 1 " );
    sql.append( " AND claim_item.approval_status_type                 = 'winner' " );
    sql.append( " AND (claim_recipient.notification_date             IS NULL " );
    sql.append( " OR claim_recipient.notification_date                <= TRUNC(SYSDATE)) " );

    Query query = getSession().createSQLQuery( sql.toString() );
    query.setParameter( "claimId", claimId );
    query.setParameter( "userId", userId );
    query.setResultTransformer( new PublicRecognitionResultTransformer() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class PublicRecognitionResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      PublicRecognitionFormattedValueBean bean = new PublicRecognitionFormattedValueBean();

      bean.setClaimId( extractLong( tuple[0] ) ); // CLAIM_ID
      bean.setClaimSubmissionDate( extractDate( tuple[1] ) ); // CLAIM_SUBMISSION_DATE
      bean.setClaimApprovedDate( extractDate( tuple[2] ) ); // CLAIM_APPROVED_DATE
      bean.setSubmitterId( extractLong( tuple[3] ) ); // SUBMITTER_ID
      bean.setSubmitterFirstName( extractString( tuple[4] ) ); // SUBMITTER_FIRST_NAME
      bean.setSubmitterLastName( extractString( tuple[5] ) ); // SUBMITTER_LAST_NAME
      bean.setSubmitterAvatarSmall( extractString( tuple[6] ) ); // SUBMITTER_AVATAR_SMALL
      bean.setPromotionId( extractLong( tuple[7] ) ); // PROMOTION_ID
      bean.setPromotionName( extractString( tuple[8] ) ); // PROMOTION_NAME
      String eligible = extractString( tuple[31] ); // ADD_POINTS_ELIGIBLE
      if ( eligible.equals( "N" ) )
      {
        bean.setAllowAddPoints( Boolean.FALSE ); // ALLOW_PUBLIC_RECOG_POINTS
      }
      else
      {
        bean.setAllowAddPoints( extractBoolean( tuple[9] ) ); // ALLOW_PUBLIC_RECOG_POINTS
      }

      // populate claim info
      Long nominationClaimId = extractLong( tuple[10] ); // NOM_CLAIM_ID
      Long recognitionClaimId = extractLong( tuple[15] ); // REC_CLAIM_ID
      if ( nominationClaimId != null )
      {
        bean.setPromotionType( PromotionType.NOMINATION );
        bean.setSubmitterComments( extractString( tuple[11] ) ); // NOM_CLAIM_SUBMITTER_COMMENTS
        bean.setHidePublicRecognition( extractBoolean( tuple[12] ) ); // NOM_CLAIM_HIDE_PUB_REC
        bean.setTeamId( extractLong( tuple[13] ) ); // NOM_CLAIM_TEAM_ID
        bean.setTeamName( extractString( tuple[14] ) ); // NOM_CLAIM_TEAM_NAME
        bean.setSubmitterCommentsLanguageType( LanguageType.getLanguageFrom( extractString( tuple[28] ) ) ); // NOM_CLAIM_SUBM_COMMENTS_LANG
        bean.seteCardUsed( extractBoolean( tuple[30] ) );// E_CARD_USED_NOMINATION
      }
      else if ( recognitionClaimId != null )
      {
        bean.setPromotionType( PromotionType.RECOGNITION );
        bean.setSubmitterComments( extractString( tuple[16] ) ); // REC_CLAIM_SUBMITTER_COMMENTS
        bean.setHidePublicRecognition( extractBoolean( tuple[17] ) ); // REC_CLAIM_HIDE_PUB_REC
        bean.setTeamId( extractLong( tuple[18] ) ); // REC_CLAIM_TEAM_ID
        bean.setSubmitterCommentsLanguageType( LanguageType.getLanguageFrom( extractString( tuple[29] ) ) ); // REC_CLAIM_SUBM_COMMENTS_LANG
        bean.seteCardUsed( extractBoolean( tuple[27] ) );// E_CARD_USED_RECOGNITION
      }

      // populate recipient info
      Long claimParticipantId = extractLong( tuple[19] ); // CLAIM_PARTICIPANT_ID
      Long claimRecipientId = extractLong( tuple[23] ); // CLAIM_RECIPIENT_ID
      if ( claimParticipantId != null )
      {
        bean.setRecipientId( claimParticipantId );
        bean.setRecipientFirstName( extractString( tuple[20] ) ); // CLAIM_PARTICIPANT_FIRST_NAME
        bean.setRecipientLastName( extractString( tuple[21] ) ); // CLAIM_PARTICIPANT_LAST_NAME
        bean.setRecipientAvatarSmall( extractString( tuple[22] ) ); // CLAIM_PARTICIPANT_AVATAR_SMALL
      }
      else if ( claimRecipientId != null )
      {
        bean.setRecipientId( claimRecipientId );
        bean.setRecipientFirstName( extractString( tuple[24] ) ); // CLAIM_RECIPIENT_FIRST_NAME
        bean.setRecipientLastName( extractString( tuple[25] ) ); // CLAIM_RECIPIENT_LAST_NAME
        bean.setRecipientAvatarSmall( extractString( tuple[26] ) ); // CLAIM_RECIPIENT_AVATAR_SMALL
      }

      return bean;
    }
  }

}
