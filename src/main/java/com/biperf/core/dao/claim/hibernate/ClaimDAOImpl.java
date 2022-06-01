/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/ClaimDAOImpl.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PublicRecognitionUserConnections;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.ProductClaimPromotionsValueBean;
import com.biperf.core.value.ProductClaimStatusCountsBean;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.biperf.core.value.PurlContributorValueBean;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ClaimDAOImpl implements the ClaimDAO interface to satisfy the requirements for processing
 * claimForm submissions.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ClaimDAOImpl extends BaseDAO implements ClaimDAO
{

  /**
   * Returns a list of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimQueryConstraint
   * @return List the claim list
   */
  public List getClaimList( ClaimQueryConstraint claimQueryConstraint )
  {
    return HibernateUtil.getObjectList( claimQueryConstraint );
  }

  /**
   * Returns a count of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimQueryConstraint
   * @return int the claim list count
   */
  public int getClaimListCount( ClaimQueryConstraint claimQueryConstraint )
  {

    return HibernateUtil.getObjectListCount( claimQueryConstraint );

  }

  /**
   * Returns the claims specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned {@link Claim} objects.
   * @return the specified claims, as a <code>List</code> of {@link Claim} objects.
   */
  public List getClaimList( JournalClaimQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    List claimList = HibernateUtil.getObjectList( queryConstraint );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( claimList );
    }

    return claimList;
  }

  /**
   * Get the claim from the database using its Id. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#getClaimById(java.lang.Long)
   * @param claimId
   * @return Claim
   */
  public Claim getClaimById( Long claimId )
  {
    return (Claim)getSession().get( Claim.class, claimId );
  }

  public Long getOpenClaimByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getOpenClaimIdWithPromotionIdAndUserId" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    return (Long)query.uniqueResult();
  }

  public Long getOpenClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getOpenClaimByPromotionIdQuizIdAndUserId" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    query.setParameter( "quizId", quizId );
    return (Long)query.uniqueResult();
  }

  public Long getPassedQuizClaimByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getPassedQuizClaimByPromotionIdAndUserId" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    return (Long)query.uniqueResult();
  }

  public Long getPassedQuizClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getPassedQuizClaimByPromotionIdQuizIdAndUserId" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    query.setParameter( "quizId", quizId );
    return (Long)query.uniqueResult();
  }

  @Override
  public List<Participant> getAllPaxWhoHaveGivenOrReceivedRecognition( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getPurlSocialGroup" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( new PreSelectSocialGroupContributorResultTransformer() );
    List<Participant> results = query.list();
    return results;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#getClaimByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param claimId
   * @param associationRequestCollection
   * @return Claim
   */
  public Claim getClaimByIdWithAssociations( Long claimId, AssociationRequestCollection associationRequestCollection )
  {
    Claim claim = (Claim)getSession().get( Claim.class, claimId );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( claim );
    }

    return claim;
  }

  /**
   * Deletes the given claim.
   * 
   * @param claim the claim to delete.
   */
  public void deleteClaim( Claim claim )
  {
    // Below code is added to fix Bug# 16950 --START
    if ( claim.isOpen() )
    {
      Query getByClaimId = getSession().createQuery( "from ClaimApproverSnapshot ca where ca.claimId = :claimId" );
      getByClaimId.setLong( "claimId", claim.getId().longValue() );
      List approverSnapshotList = getByClaimId.list();
      ClaimApproverSnapshot approverSnapshot = null;
      for ( int i = 0; i < approverSnapshotList.size(); i++ )
      {
        approverSnapshot = (ClaimApproverSnapshot)approverSnapshotList.get( i );
        getSession().delete( approverSnapshot );
      }
    }
    // Bug# 16950 --END
    getSession().delete( claim );
  }

  /**
   * 
   */
  public void excecuteOnReversal( String claimId, String promotionType )
  {
    if ( promotionType != null && promotionType.equals( PromotionType.NOMINATION ) )
    {
      NominationClaim nominationClaim = (NominationClaim)getSession().get( NominationClaim.class, new Long( claimId ) );
      if ( nominationClaim != null )
      {
        nominationClaim.setReversal( true );
        getSession().save( nominationClaim );
      }
    }
    else
    {
      RecognitionClaim recognitionClaim = (RecognitionClaim)getSession().get( RecognitionClaim.class, new Long( claimId ) );
      if ( recognitionClaim != null )
      {
        recognitionClaim.setReversal( true );
        getSession().save( recognitionClaim );
      }
    }
  }

  /**
   * Update the claim. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#saveClaim(com.biperf.core.domain.claim.Claim)
   * @param claim
   * @return Claim
   */
  public Claim saveClaim( Claim claim )
  {

    Claim savedClaim = (Claim)HibernateUtil.saveOrUpdateOrDeepMerge( claim );
    // run lock to insure that sub-objects are reattached to this session, since some of
    // the subobjects may have come from another session.
    getSession().flush();
    getSession().refresh( savedClaim );

    return savedClaim;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#isClaimElementValueUniqueWithinHierarchy(com.biperf.core.domain.claim.ClaimElement,
   *      com.biperf.core.domain.hierarchy.Node, com.biperf.core.domain.promotion.Promotion)
   * @param claimElement
   * @param node
   * @param promotion
   * @return boolean
   */
  public boolean isClaimElementValueUniqueWithinHierarchy( ClaimElement claimElement, Node node, Promotion promotion )
  {
    if ( null == claimElement.getValue() )
    {
      return false;
    }
    int uniqueCount = 0;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimElementWithinHierarchyCount" );

    query.setParameter( "value", claimElement.getValue().toUpperCase() );
    query.setParameter( "nodeId", node.getId() );
    query.setParameter( "claimFormStepElementId", claimElement.getClaimFormStepElement().getId() );
    query.setParameter( "promotionId", promotion.getId() );

    Integer count = (Integer)query.uniqueResult();

    return count.intValue() == uniqueCount;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#isClaimElementValueUniqueWithinNode(com.biperf.core.domain.claim.ClaimElement,
   *      com.biperf.core.domain.hierarchy.Node, com.biperf.core.domain.promotion.Promotion)
   * @param claimElement
   * @param node
   * @param promotion
   * @return boolean
   */
  public boolean isClaimElementValueUniqueWithinNode( ClaimElement claimElement, Node node, Promotion promotion )
  {
    boolean isUnique = true;
    int uniqueCount = 0;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimElementWithinNodeCount" );

    query.setParameter( "value", claimElement.getValue().toUpperCase() );
    query.setParameter( "nodeId", node.getId() );
    query.setParameter( "claimFormStepElementId", claimElement.getClaimFormStepElement().getId() );
    query.setParameter( "promotionId", promotion.getId() );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == uniqueCount;

    return isUnique;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#isClaimElementValueUniqueWithinNodeType(com.biperf.core.domain.claim.ClaimElement,
   *      com.biperf.core.domain.hierarchy.Node, com.biperf.core.domain.promotion.Promotion)
   * @param claimElement
   * @param node
   * @param promotion
   * @return boolean
   */
  public boolean isClaimElementValueUniqueWithinNodeType( ClaimElement claimElement, Node node, Promotion promotion )
  {
    boolean isUnique = true;
    int uniqueCount = 0;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimElementWithinNodeTypeCount" );

    query.setParameter( "value", claimElement.getValue().toUpperCase() );
    query.setParameter( "nodeId", node.getId() );
    query.setParameter( "claimFormStepElementId", claimElement.getClaimFormStepElement().getId() );
    query.setParameter( "promotionId", promotion.getId() );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == uniqueCount;

    return isUnique;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#isClaimProductCharacteristicUnique(com.biperf.core.domain.claim.ClaimProductCharacteristic,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param claimProductCharacteristic
   * @param promotion
   * @return boolean
   */
  public boolean isClaimProductCharacteristicUnique( ClaimProductCharacteristic claimProductCharacteristic, Promotion promotion )
  {
    boolean isUnique = true;

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimProductCharacteristicCount" );

    query.setParameter( "characteristicValue", claimProductCharacteristic.getValue().toUpperCase() );
    query.setParameter( "characteristicId", claimProductCharacteristic.getProductCharacteristicType().getId() );
    query.setParameter( "promotionId", promotion.getId() );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;

    return isUnique;
  }

  /**
   * returns the total earnings for this claim (uses journal entries)
   * 
   * @param claimId
   * @param userId
   * @return Long
   */
  public Long getEarningsForClaim( Long claimId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.FindEarningsForClaim" );

    query.setParameter( "claimId", claimId );
    query.setParameter( "userId", userId );
    return (Long)query.uniqueResult();
  }

  /**
   * returns the total earnings for this product claim (uses journal entries)
   * 
   * @param claimId
   * @param userId
   * @return Long
   */
  public Long getEarningsForProductClaim( Long claimId, Long userId, Long productId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.FindEarningsForProductClaim" );

    query.setParameter( "claimId", claimId );
    query.setParameter( "userId", userId );
    query.setParameter( "productId", productId );
    return (Long)query.uniqueResult();
  }

  /**
   * Method returns the number of claims submitted given a promotion id. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#getClaimSubmittedCount(Long)
   * @param promotionId Promotion id
   * @return Number of claims submitted
   */
  public int getClaimSubmittedCount( Long promotionId )
  {
    Long count = (Long)getSession().getNamedQuery( "com.biperf.core.domain.claim.GetClaimSubmittedCount" ).setLong( "promotionId", promotionId.longValue() ).uniqueResult();

    return count.intValue();
  }

  /**
   * Get a list of userIds from CLAIM where promoId matches value passed and submission date is
   * within a given range
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsByPromoIdWithinRange( Long promoId, Date startDate, Date endDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimUserIdsByPromoIdForTimePeriod" );

    query.setParameter( "promoId", promoId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return query.list();
  }

  public long getUserIdByPromoIdWithinRange( Long promoId, Long paxId, Date startDate, Date endDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimUserIdByPromoIdForTimePeriod" );

    query.setParameter( "promoId", promoId );
    query.setParameter( "paxId", paxId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    List results = query.list();
    if ( null == results || results.isEmpty() )
    {
      return 0;
    }
    else
    {
      return ( (Long)results.get( 0 ) ).longValue();
    }
  }

  /**
   * Get a list of userIds from CLAIM where promoId matches value passed, isOpen matches value
   * passed and submission date is within a given range
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @param isOpen
   * @return List
   */
  public List getUserIdsByPromoIdWithinRange( Long promoId, Date startDate, Date endDate, boolean isOpen )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimUserIdsByPromoIdForTimePeriodWithOpen" );

    query.setParameter( "promoId", promoId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );
    query.setParameter( "isOpen", new Boolean( isOpen ) );

    return query.list();
  }

  /**
   * Get a list of userIds from CLAIM where promoId matches value passed and submission date is
   * within a given range and PASS on QUIZ_CLAIM = true
   * 
   * @param promoId
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsWhoPassedQuizWithinRange( Long promoId, Date startDate, Date endDate, boolean pass )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimUserIdsWhoPassedQuizForTimePeriod" );

    query.setParameter( "promoId", promoId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );
    query.setParameter( "isOpen", new Boolean( false ) );
    query.setParameter( "didPass", new Boolean( pass ) );

    return query.list();
  }

  public List getOpenClaimsWithNoMatchingNodeInApproverHierarchy( ApproverType approverType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.OpenClaimsWithNoMatchingNodeInApproverHierarchy" );

    query.setParameter( "approverType", approverType.getCode() );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#getNonPostedJournalCount(java.lang.Long,
   *      java.lang.Long)
   * @param recipientId
   * @param claimId
   * @return int Number of non-posted journals for this claim.
   */
  public int getNonPostedJournalCount( Long recipientId, Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.GetNonPostedJournalCount" );
    query.setLong( "userId", recipientId.longValue() );
    query.setLong( "claimId", claimId.longValue() );

    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  /**
   * Overridden from @see com.biperf.core.dao.claim.ClaimDAO#saveClaimitem(com.biperf.core.domain.claim.ClaimRecipient)
   * @param cr
   * @return ClaimRecipient
   */
  public ClaimRecipient saveClaimitem( ClaimRecipient cr )
  {
    /*
     * Using Direct sql since in hibernate 3.0, hibernate bulk updates can't be run with native SQL
     * and HQL bulk updates don't work with the classic parser (which we use). Flush initially to
     * bring the database up to date
     */
    String queryString = "UPDATE CLAIM_ITEM SET APPROVAL_STATUS_TYPE= ? WHERE  CLAIM_ITEM_ID = ?";
    getSession().flush();
    PreparedStatement preparedStatement = null;
    try
    {
      preparedStatement = getSession().connection().prepareStatement( queryString );
      int index = 1;
      preparedStatement.setString( index++, cr.getApprovalStatusType().getCode() );
      preparedStatement.setLong( index++, cr.getId().longValue() );

      preparedStatement.executeUpdate();
    }
    catch( HibernateException e )
    {
      throw new BeaconRuntimeException( "Exception running claim Item update", e );
    }
    catch( SQLException e )
    {
      throw new BeaconRuntimeException( "Exception running claim item update", e );
    }
    finally
    {
      if ( preparedStatement != null )
      {
        try
        {
          preparedStatement.close();
        }
        catch( SQLException e )
        {
          // ignore
        }
      }
    }
    return cr;
  }

  public ClaimRecipient getClaimRecipientById( Long claimRecipientId )
  {
    return (ClaimRecipient)getSession().get( ClaimRecipient.class, claimRecipientId );
  }

  /* Fix 26324 */
  /**
   * Get the claimElement from the database using its Id. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimDAO#getClaimElementById(java.lang.Long)
   * @param claimElementId
   * @return Claim ClaimElement
   */
  public ClaimElement getClaimElementById( Long claimElementId )
  {
    return (ClaimElement)getSession().get( ClaimElement.class, claimElementId );
  }

  @Override
  public int getPublicRecognitionClaimsSentByUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getPublicRecognitionClaimsSentByUserId" );
    if ( promoId == null )
    {
      query.setParameter( "promoId", "-1" );
      query.setParameter( "all", "N" );
    }
    else
    {
      query.setParameter( "promoId", promoId );
      query.setParameter( "all", "Y" );
    }
    query.setParameter( "paxId", paxId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );
    query.setParameter( "promoType", promotionType );
    query.setParameter( "approStatusType", approvalStatus );

    List results = query.list();
    if ( null == results || results.isEmpty() )
    {
      return 0;
    }
    else
    {
      return ( (Integer)results.get( 0 ) ).intValue();
    }
  }

  @Override
  public int getPublicRecognitionClaimsReceivedbyUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getPublicRecognitionClaimsReceivedbyUserId" );
    if ( promoId == null )
    {
      query.setParameter( "promoId", "-1" );
      query.setParameter( "all", "N" );
    }
    else
    {
      query.setParameter( "promoId", promoId );
      query.setParameter( "all", "Y" );
    }
    query.setParameter( "paxId", paxId );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );
    query.setParameter( "promoType", promotionType );
    query.setParameter( "approStatusType", approvalStatus );

    List results = query.list();
    if ( null == results || results.isEmpty() )
    {
      return 0;
    }
    else
    {
      return ( (Integer)results.get( 0 ) ).intValue();
    }
  }

  public Long getNextTeamId()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getNextTeamId" );
    return (Long)query.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<AbstractRecognitionClaim> getTeamClaimsByClaimId( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getTeamClaimsByClaimId" );
    query.setParameter( "claimId", claimId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<AbstractRecognitionClaim> getClaimsByTeamId( Long teamId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getClaimsByTeamId" );
    query.setParameter( "teamId", teamId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ProductClaimPromotionsValueBean> getEligibleProductClaimPromotions( Long userId )
  {
    String languageCode = UserManager.getLocale().toString();
    String sql = buildEligibleProductClaimPromotionsQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "userId", userId );
    query.setParameter( "languageCode", languageCode );
    query.setResultTransformer( new EligibleProductClaimPromotionsValueBeanMapper() );
    return query.list();
  }

  private String buildEligibleProductClaimPromotionsQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " WITH promotions AS     " );
    sql.append( "  (SELECT * FROM (SELECT * FROM " );
    sql.append( "   (SELECT promotion_id,  " );
    sql.append( "     (SELECT cms_value  FROM vw_cms_asset_value WHERE locale   = :languageCode " );
    sql.append( "      AND asset_code = promo_name_asset_code ) Promotion_Name, " );
    sql.append( "      promotion_start_date, promotion_end_date, " );
    sql.append( "      date_created FROM promotion WHERE promotion_type ='product_claim' " );
    sql.append( "      AND promotion_status ='live' AND  " );
    sql.append( "      primary_audience_type = 'allactivepaxaudience' " );
    sql.append( "      UNION SELECT p.promotion_id,  " );
    sql.append( "      (SELECT cms_value FROM vw_cms_asset_value WHERE locale = :languageCode AND " );
    sql.append( "      asset_code = promo_name_asset_code) Promotion_Name, " );
    sql.append( "      promotion_start_date, promotion_end_date, p.date_created FROM " );
    sql.append( " promo_audience pa, " );
    sql.append( "      promotion p,participant_audience pax WHERE pax.user_id =:userId AND " );
    sql.append( " pax.audience_id = pa.audience_id " );
    sql.append( "      AND pa.promotion_id = p.promotion_id AND p.promotion_type ='product_claim' " );
    sql.append( "      AND p.promotion_status ='live') " );
    sql.append( "      ORDER BY date_created DESC) " );
    sql.append( "       ) " );
    sql.append( "      SELECT promotions.promotion_id,  " );
    sql.append( "         promotions.Promotion_Name,    promotions.promotion_start_date,    " );
    sql.append( " promotions.promotion_end_date, " );
    sql.append( "             COUNT(claim_id) AS claims_submitted,    " );
    sql.append( " SUM(DECODE(IS_OPEN,0,1,0)) claims_approved  FROM claim, " );
    sql.append( "             promotions  WHERE claim.promotion_id(+) = promotions.promotion_id   " );
    sql.append( "             AND claim.submitter_id(+)   = :userId  GROUP BY " );
    sql.append( " promotions.promotion_id,    " );
    sql.append( "              promotions.Promotion_Name,    promotions.promotion_start_date,    " );
    sql.append( " promotions.promotion_end_date,   " );
    sql.append( "                promotions.date_created  ORDER BY promotions.date_created DESC " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class EligibleProductClaimPromotionsValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ProductClaimPromotionsValueBean productClaimPromotionsValueBean = new ProductClaimPromotionsValueBean();

      productClaimPromotionsValueBean.setPromotionId( extractLong( tuple[0] ) );
      productClaimPromotionsValueBean.setPromotionName( extractString( tuple[1] ) );
      productClaimPromotionsValueBean.setPromotionStartDate( extractDate( tuple[2] ) );
      productClaimPromotionsValueBean.setPromotionEndDate( extractDate( tuple[3] ) );
      productClaimPromotionsValueBean.setNumberSubmitted( extractInt( tuple[4] ) );
      productClaimPromotionsValueBean.setNumberOfApprovals( extractInt( tuple[5] ) );

      return productClaimPromotionsValueBean;
    }
  }

  @SuppressWarnings( "serial" )
  private class PreSelectContributorResultTransformer extends BaseResultTransformer
  {
    @Override
    public Participant transformTuple( Object[] tuple, String[] aliases )
    {
      Participant bean = new Participant();
      bean.setId( extractLong( tuple[0] ) ); // USER_ID
      bean.setFirstName( extractString( tuple[1] ) ); // FIRST_NAME
      bean.setLastName( extractString( tuple[2] ) ); // LAST_NAME
      bean.setAvatarSmall( extractString( tuple[3] ) ); // AVATAR_SMALL
      bean.setAllowPublicRecognition( extractBoolean( tuple[4] ) ); // ALLOW_PUBLIC_RECOGNITION
      bean.setAllowPublicInformation( extractBoolean( tuple[5] ) ); // ALLOW_PUBLIC_INFORMATION

      String nodeName = extractString( tuple[6] );// NAME
      Long nodeId = extractLong( tuple[7] );// NODE_ID
      if ( nodeId != null )
      {
        Set<UserNode> userNodes = new HashSet<UserNode>();
        UserNode userNode = new UserNode();
        Node node = new Node();
        node.setId( nodeId );
        node.setName( nodeName );
        userNode.setNode( node );
        userNode.setIsPrimary( Boolean.TRUE );
        userNodes.add( userNode );
        bean.setUserNodes( userNodes );
      }

      String countryCode = extractString( tuple[8] );// COUNTRY_CODE
      String nameCmKey = extractString( tuple[9] );// NAME_CM_KEY
      String countryCMAssetCode = extractString( tuple[10] );// CM_ASSET_CODE
      if ( !StringUtils.isEmpty( countryCode ) )
      {
        Set<UserAddress> userAddresses = new HashSet<UserAddress>();
        UserAddress userAddress = new UserAddress();
        Address address = new Address();
        Country country = new Country();
        country.setCountryCode( countryCode );
        country.setNameCmKey( nameCmKey );
        country.setCmAssetCode( countryCMAssetCode );
        address.setCountry( country );
        userAddress.setAddress( address );
        userAddress.setIsPrimary( Boolean.TRUE );
        userAddresses.add( userAddress );
        bean.setUserAddresses( userAddresses );
      }
      bean.setPositionType( extractString( tuple[11] ) );// POSITION_TYPE
      bean.setDepartmentType( extractString( tuple[12] ) );// DEPARTMENT_TYPE
      String userEmailAddress = extractString( tuple[13] );// EMAIL_ADDRESS
      if ( !StringUtils.isEmpty( userEmailAddress ) )
      {
        UserEmailAddress emailAddress = new UserEmailAddress();
        emailAddress.setIsPrimary( Boolean.TRUE );
        emailAddress.setEmailAddr( userEmailAddress );
      }
      bean.setSourceType( extractString( tuple[14] ) );
      return bean;
    }
  }

  @SuppressWarnings( "serial" )
  private class PreSelectSocialGroupContributorResultTransformer extends BaseResultTransformer
  {
    @Override
    public Participant transformTuple( Object[] tuple, String[] aliases )
    {
      Participant bean = new Participant();
      bean.setId( extractLong( tuple[0] ) ); // USER_ID
      bean.setFirstName( extractString( tuple[1] ) ); // FIRST_NAME
      bean.setLastName( extractString( tuple[2] ) ); // LAST_NAME
      bean.setAvatarSmall( extractString( tuple[3] ) ); // AVATAR_SMALL
      bean.setAllowPublicRecognition( extractBoolean( tuple[4] ) ); // ALLOW_PUBLIC_RECOGNITION
      bean.setAllowPublicInformation( extractBoolean( tuple[5] ) ); // ALLOW_PUBLIC_INFORMATION

      String nodeName = extractString( tuple[6] );// NAME
      Long nodeId = extractLong( tuple[7] );// NODE_ID
      if ( nodeId != null )
      {
        Set<UserNode> userNodes = new HashSet<UserNode>();
        UserNode userNode = new UserNode();
        Node node = new Node();
        node.setId( nodeId );
        node.setName( nodeName );
        userNode.setNode( node );
        userNode.setIsPrimary( Boolean.TRUE );
        userNodes.add( userNode );
        bean.setUserNodes( userNodes );
      }

      String countryCode = extractString( tuple[8] );// COUNTRY_CODE
      String nameCmKey = extractString( tuple[9] );// NAME_CM_KEY
      String countryCMAssetCode = extractString( tuple[10] );// CM_ASSET_CODE
      if ( !StringUtils.isEmpty( countryCode ) )
      {
        Set<UserAddress> userAddresses = new HashSet<UserAddress>();
        UserAddress userAddress = new UserAddress();
        Address address = new Address();
        Country country = new Country();
        country.setCountryCode( countryCode );
        country.setNameCmKey( nameCmKey );
        country.setCmAssetCode( countryCMAssetCode );
        address.setCountry( country );
        userAddress.setAddress( address );
        userAddress.setIsPrimary( Boolean.TRUE );
        userAddresses.add( userAddress );
        bean.setUserAddresses( userAddresses );
      }
      bean.setPositionType( extractString( tuple[11] ) );// POSITION_TYPE
      bean.setDepartmentType( extractString( tuple[12] ) );// DEPARTMENT_TYPE
      String userEmailAddress = extractString( tuple[13] );// EMAIL_ADDRESS
      if ( !StringUtils.isEmpty( userEmailAddress ) )
      {
        UserEmailAddress emailAddress = new UserEmailAddress();
        emailAddress.setIsPrimary( Boolean.TRUE );
        emailAddress.setEmailAddr( userEmailAddress );
      }
      return bean;
    }
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
      bean.setSubmitterId( extractLong( tuple[2] ) ); // SUBMITTER_ID
      bean.setSubmitterFirstName( extractString( tuple[3] ) ); // SUBMITTER_FIRST_NAME
      bean.setSubmitterLastName( extractString( tuple[4] ) ); // SUBMITTER_LAST_NAME
      bean.setSubmitterAvatarSmall( extractString( tuple[5] ) ); // SUBMITTER_AVATAR_SMALL
      bean.setPromotionId( extractLong( tuple[6] ) ); // PROMOTION_ID
      bean.setPromotionName( extractString( tuple[7] ) ); // PROMOTION_NAME
      String eligible = extractString( tuple[30] );
      if ( "N".equals( eligible ) )
      {
        bean.setAllowAddPoints( Boolean.FALSE ); // ALLOW_PUBLIC_RECOG_POINTS
      }
      else
      {
        bean.setAllowAddPoints( Boolean.TRUE );
      }

      // populate claim info
      Long nominationClaimId = extractLong( tuple[9] ); // NOM_CLAIM_ID
      Long recognitionClaimId = extractLong( tuple[14] ); // REC_CLAIM_ID
      if ( nominationClaimId != null )
      {
        bean.setPromotionType( PromotionType.NOMINATION );
        bean.setSubmitterComments( extractString( tuple[10] ) ); // NOM_CLAIM_SUBMITTER_COMMENTS
        bean.setHidePublicRecognition( extractBoolean( tuple[11] ) ); // NOM_CLAIM_HIDE_PUB_REC
        bean.setTeamId( extractLong( tuple[12] ) ); // NOM_CLAIM_TEAM_ID
        bean.setTeamName( extractString( tuple[13] ) ); // NOM_CLAIM_TEAM_NAME
        bean.setSubmitterCommentsLanguageType( LanguageType.getLanguageFrom( extractString( tuple[27] ) ) ); // NOM_CLAIM_SUBM_COMMENTS_LANG
        bean.seteCardUsed( extractBoolean( tuple[29] ) );// E_CARD_USED_NOMINATION
      }
      else if ( recognitionClaimId != null )
      {
        bean.setPromotionType( PromotionType.RECOGNITION );
        bean.setSubmitterComments( extractString( tuple[15] ) ); // REC_CLAIM_SUBMITTER_COMMENTS
        bean.setHidePublicRecognition( extractBoolean( tuple[16] ) ); // REC_CLAIM_HIDE_PUB_REC
        bean.setTeamId( extractLong( tuple[17] ) ); // REC_CLAIM_TEAM_ID
        bean.setSubmitterCommentsLanguageType( LanguageType.getLanguageFrom( extractString( tuple[28] ) ) ); // REC_CLAIM_SUBM_COMMENTS_LANG
        bean.seteCardUsed( extractBoolean( tuple[26] ) );// E_CARD_USED_RECOGNITION
      }

      // populate recipient info
      Long claimParticipantId = extractLong( tuple[18] ); // CLAIM_PARTICIPANT_ID
      Long claimRecipientId = extractLong( tuple[22] ); // CLAIM_RECIPIENT_ID
      if ( claimParticipantId != null )
      {
        bean.setRecipientId( claimParticipantId );
        bean.setRecipientFirstName( extractString( tuple[19] ) ); // CLAIM_PARTICIPANT_FIRST_NAME
        bean.setRecipientLastName( extractString( tuple[20] ) ); // CLAIM_PARTICIPANT_LAST_NAME
        bean.setRecipientAvatarSmall( extractString( tuple[21] ) ); // CLAIM_PARTICIPANT_AVATAR_SMALL
      }
      else if ( claimRecipientId != null )
      {
        bean.setRecipientId( claimRecipientId );
        bean.setRecipientFirstName( extractString( tuple[23] ) ); // CLAIM_RECIPIENT_FIRST_NAME
        bean.setRecipientLastName( extractString( tuple[24] ) ); // CLAIM_RECIPIENT_LAST_NAME
        bean.setRecipientAvatarSmall( extractString( tuple[25] ) ); // CLAIM_RECIPIENT_AVATAR_SMALL
      }

      return bean;
    }
  }

  @Override
  public List<ProductClaimStatusCountsBean> getProductClaimStatusCount( Long id )
  {
    String sql = buildProductClaimStatusCountListQuery( id );
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "id", id );
    query.setResultTransformer( new ProductClaimStatusCountValueBeanMapper() );
    return query.list();
  }

  private String buildProductClaimStatusCountListQuery( Long id )
  {
    StringBuilder sql = new StringBuilder();
    sql.append( " SELECT COUNT ( claim_id) Total_submitted_claims, " );
    sql.append( "   SUM ( Total_pending_claims) Total_pending_claims, " );
    sql.append( "   SUM (Total_submitted_products) Total_submitted_products, " );
    sql.append( "   SUM (Total_approv_products) Total_approv_products, " );
    sql.append( "   SUM (Total_denied_products) Total_denied_products, " );
    sql.append( "   SUM (Total_pending_products) Total_pending_products " );
    sql.append( " FROM " );
    sql.append( "   (SELECT promotion_name, " );
    sql.append( "     claim_id, " );
    sql.append( "     SUM (Total_submitted_products) Total_submitted_products, " );
    sql.append( "     SUM (Total_approv_products) Total_approv_products, " );
    sql.append( "     DECODE (is_open, 1, 1, 0) AS Total_pending_claims, " );
    sql.append( "     SUM (Total_denied_products) Total_denied_products, " );
    sql.append( "     SUM (Total_pending_products) Total_pending_products " );
    sql.append( "   FROM " );
    sql.append( "     (SELECT p.promotion_name, " );
    sql.append( "       c.claim_id, " );
    sql.append( "       c.is_open, " );
    sql.append( "       DECODE (ci.approval_status_type, 'pend', cp.product_qty, 0)   AS Total_pending_products, " );
    sql.append( "       DECODE (ci.approval_status_type, 'deny', cp.product_qty, 0)   AS Total_denied_products, " );
    sql.append( "       DECODE (ci.approval_status_type, 'approv', cp.product_qty, 0) AS Total_approv_products, " );
    sql.append( "       cp.product_qty                                                AS Total_submitted_products " );
    sql.append( "     FROM claim_item ci, " );
    sql.append( "       claim c, " );
    sql.append( "       claim_product cp, " );
    sql.append( "       promotion p " );
    sql.append( "     WHERE ci.claim_id    = c.claim_id " );
    sql.append( "     AND c.promotion_id   = :id " );
    sql.append( "     AND c.promotion_id   = p.promotion_id " );
    sql.append( "     AND ci.claim_item_id = cp.claim_item_id " );
    sql.append( "     ) " );
    sql.append( "   GROUP BY promotion_name, " );
    sql.append( "     claim_id, " );
    sql.append( "     is_open " );
    sql.append( "   ) " );
    sql.append( " GROUP BY promotion_name " );
    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class ProductClaimStatusCountValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ProductClaimStatusCountsBean reportValue = new ProductClaimStatusCountsBean();

      reportValue.setClaimsSubmitted( extractInt( tuple[0] ) );
      reportValue.setClaimsPending( extractInt( tuple[1] ) );
      reportValue.setProductsSubmitted( extractInt( tuple[2] ) );
      reportValue.setProductsApproved( extractInt( tuple[3] ) );
      reportValue.setProductsDenied( extractInt( tuple[4] ) );
      reportValue.setProductsPending( extractInt( tuple[5] ) );

      return reportValue;
    }
  }

  @SuppressWarnings( "serial" )
  private class PurlContributorBeanResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      PurlContributorValueBean purlContributorBean = new PurlContributorValueBean();
      String id = tuple[0] != null ? tuple[0].toString() : "";

      purlContributorBean.setId( id );
      if ( !StringUtils.isEmpty( id ) )
      {
        purlContributorBean.setCountryCode( tuple[1] != null ? extractString( tuple[1] ) : "" );
        purlContributorBean.setCountryName( getI18nCountryName( extractString( tuple[3] ), extractString( tuple[2] ) ) );
        purlContributorBean.setFirstName( extractString( tuple[4] ) );
        purlContributorBean.setLastName( extractString( tuple[5] ) );
        purlContributorBean.setOrgName( extractString( tuple[7] ) );
        if ( null != extractString( tuple[8] ) && !StringUtils.isEmpty( extractString( tuple[8] ) ) )
        {
          purlContributorBean.setDepartmentName( DepartmentType.lookup( extractString( tuple[8] ) ).getName() );
        }
        if ( null != extractString( tuple[9] ) && !StringUtils.isEmpty( extractString( tuple[9] ) ) )
        {
          purlContributorBean.setJobName( PositionType.lookup( extractString( tuple[9] ) ).getName() );
        }
        purlContributorBean.setContribType( "preselected" );
      }
      else
      {
        purlContributorBean.setContribType( "other" );
      }

      purlContributorBean.setEmail( extractString( tuple[6] ) );
      purlContributorBean.setSourceType( tuple[11] != null ? extractString( tuple[10] ) : "" );
      purlContributorBean.setInvitationSentDate( extractString( tuple[11] ) );

      return purlContributorBean;
    }

    private String getI18nCountryName( String cmAssetCode, String nameCmKey )
    {
      return ContentReaderManager.getText( cmAssetCode, nameCmKey );
    }
  }

  @Override
  public int getProductClaimPromotionTeamMaxCount( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getProductClaimPromotionTeamMaxCount" );
    query.setParameter( "promotionId", promotionId );
    return (Integer)query.uniqueResult();
  }

  public List<Long> getDelayedApprovalClaimIds()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getDelayedApprovalClaimIds" );
    List<Long> results = query.list();
    return results;
  }

  @Override
  public List<ClaimRecipient> findMostRecentRecipientsFor( Long submitterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimRecipient.findMostRecentForUser" );
    query.setParameter( "submitterId", submitterId );
    return query.list();
  }

  @Override
  public List<Participant> getGiversForParticipant( Long recepientId, int count )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getGiversForParticipant" );
    query.setParameter( "recepientId", recepientId );
    query.setParameter( "count", count );

    List<Participant> givers = query.list();
    return givers;
  }

  @Override
  public List<Long> getCelebrationClaims( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getCelebrationClaims" );
    query.setParameter( "participantId", participantId );

    List<Long> claimIds = query.list();
    return claimIds;
  }

  @Override
  public int getEligibleUsersCountForCelebrationModule( Long claimId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getEligibleUsersCountForCelebrationModule" );
    if ( claimId != null )
    {
      query.setParameter( "claimId", claimId );
    }
    else
    {
      query.setParameter( "claimId", "" );
    }
    query.setParameter( "participantId", participantId );
    return (Integer)query.uniqueResult();
  }

  @Override
  public Long getExistingTeamIdForClaim( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getExistingTeamIdForClaim" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionId", promotionId );
    return (Long)query.uniqueResult();
  }

  @Override
  public Date getMostRecentWinDate( Long promotionId, Long participantId, Long approvalLevel )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getMostRecentWinDate" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "approvalLevel", approvalLevel );
    return (Date)query.uniqueResult();
  }

  @Override
  public List<Participant> getAllPreSelectedContributors( Long recipientId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getAllPreSelectedContributors" );
    query.setParameter( "userId", recipientId );
    query.setResultTransformer( new PreSelectContributorResultTransformer() );
    List<Participant> results = query.list();
    return results;
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public List<PurlContributorValueBean> getAllExistingContributors( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getAllExistingContributorsByRecipientId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( new PurlContributorBeanResultTransformer() );
    List<PurlContributorValueBean> results = query.list();
    return results;
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public List<Long> getClaimIdList( Long submitterId, String approvalStatusType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getAllClaimIdList" );
    query.setParameter( "submitterId", submitterId );
    List<Long> results = query.list();
    return results;
  }

  public Long getMinQualifierId( Long claimId, Long productId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.GetMinQualifierId" );

    query.setParameter( "claimId", claimId );
    query.setParameter( "productId", productId );
    return (Long)query.uniqueResult();

  }

  public void saveUserConnection( PublicRecognitionUserConnections publicRecognitionUserConnections )
  {
    getSession().save( publicRecognitionUserConnections );
  }

  @Override
  public boolean pastApprovalExist( Long approverId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.pastApprovalExist" );
    query.setParameter( "p_in_approver_id", approverId );
    return BooleanUtils.toBoolean( (Integer)query.uniqueResult() );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public String getClaimIdByApproverAndpromotion( Long approverId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getClaimIdByApproverAndPromotion" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "approverUserId", approverId );
    List<Long> list = query.list();
    String result = list.stream().map( Object::toString ).collect( Collectors.joining( ", " ) );
    return result.equals( "" ) || result.equals( null ) ? null : result;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ClaimInfoBean> getActivityTimePeriod( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getActivityTimePeriod" );
    query.setParameter( "p_claim_id", claimId );
    query.setResultTransformer( Transformers.aliasToBean( ClaimInfoBean.class ) );
    return query.list();
  }

  @Override
  public String getPromoTimePeriodNameById( Long timePeriodId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getTimePeriodNameById" );
    query.setParameter( "t_period_id", timePeriodId );
    return (String)query.uniqueResult();

  }

  @Override
  public String getDBTimeZone()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getDBTimeZone" );
    return (String)query.uniqueResult();

  }

  @Override
  public boolean isCardMapped( Long cardId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getRecognitionCountByCardId" );
    query.setParameter( "cardId", cardId );
    Integer result = (Integer)query.uniqueResult();
    return result > 0 ? true : false;
  }
  
  // Client customization for WIP #39189 starts
  @Override
  public List<TcccClaimFileValueBean> getClaimFiles( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.client.getClaimFilesValueBean" );
    query.setParameter( "claimId", claimId );
    query.setResultTransformer( new TcccClaimFileValueBeanResultTransformer() );
    return query.list();
  }

  /* coke customization start */
  public List<Long> getNomTeamClaimsWithoutJournalForActivityHistoryReceivedTab( Date startDate, Date endDate, Long promotionId, Long recipientId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getNomTeamClaimsWithoutJournalForActivityHistoryReceivedTab" );
    query.setDate( "startDate", startDate );
    query.setDate( "endDate", endDate );
    if (promotionId == null)
    {
      query.setParameter( "promotionId", new Long("0") );
    }
    else
    {
      query.setParameter( "promotionId", promotionId );
    }
    query.setParameter( "recipientId", recipientId );
    List<Long> results = query.list();
    return results;
  }
  /* coke customization end */
  private class TcccClaimFileValueBeanResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      TcccClaimFileValueBean valueBean = new TcccClaimFileValueBean();
      valueBean.setClaimFileId( extractLong( tuple[0] ) );
      valueBean.setFileName( extractString( tuple[1] ) );
      valueBean.setUrl( extractString( tuple[2] ) );
      return valueBean;
    }
  }
  // Client customization for WIP #39189 end
}
