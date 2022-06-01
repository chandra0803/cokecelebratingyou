/**
 *
 */

package com.biperf.core.dao.promotion.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionBean;
import com.biperf.core.value.PromotionsValueBean;
import com.biperf.core.value.celebration.CelebrationImageFillerValue;
import com.biperf.core.value.nomination.NominationAdminApprovalsBean;
import com.biperf.core.value.nomination.NominationApproverValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataColorSettingValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataDrawSettingsValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;
import com.biperf.core.value.participant.PromoRecImageData;
import com.biperf.core.value.participant.PromoRecPictureData;
import com.biperf.core.value.promotion.RecognitionAdvisorPromotionValueBean;

/**
 * PromotionDAOImpl.
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
 * <td>asondgeroth</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class PromotionDAOImpl extends BaseDAO implements PromotionDAO
{
  private String promotionQueryCacheName = null;
  private DataSource dataSource;
  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";
  public static final String P_OUT_DATA = "p_out_user_data";
  private JdbcTemplate jdbcTemplate;

  private static final Log log = LogFactory.getLog( PromotionDAOImpl.class );

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.dao.promotion.PromotionDAO#isPromotionNameUnique(java.lang.String,
   *      java.lang.Long)
   * @param promotionName
   * @param currentPromotionId
   * @return boolean
   */
  public boolean isPromotionNameUnique( String promotionName, Long currentPromotionId )
  {
    boolean isUnique = true;

    if ( currentPromotionId == null )
    {
      currentPromotionId = new Long( 0 );
    }

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionByNameCount" );

    query.setParameter( "promotionName", promotionName.toLowerCase() );
    query.setParameter( "promotionId", currentPromotionId );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;

    return isUnique;
  }

  /**
   * Deletes the Promotion from the database.
   *
   * @param promotion
   */
  public void delete( Promotion promotion )
  {
    getSession().delete( promotion );
  }

  /**
   * Retrieves all the Promotions from the database of all types.
   *
   * @return List a list of Promotions
   */
  public List getAll()
  {
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );
    return getPromotionList( promoQueryConstraint );
  }

  /**
   * Returns a list of promotions that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   *
   * @param queryConstraint
   * @return List the promotion list
   */
  @SuppressWarnings( "null" )
  public List getPromotionList( PromotionQueryConstraint queryConstraint )
  {
    // ******** Remove the purl & celebration promotions. **********
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      return getExcludedPurlAndCelebPromotionList( (List<Promotion>)HibernateUtil.getObjectList( queryConstraint ) );
    }
    return HibernateUtil.getObjectList( queryConstraint );
  }

  /**
   * Returns a count of promotions that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   *
   * @param queryConstraint
   * @return int the promotion list count
   */
  public int getPromotionListCount( PromotionQueryConstraint queryConstraint )
  {
    return HibernateUtil.getObjectListCount( queryConstraint );
  }

  /**
   * Get all promotions with sweepstakes
   *
   * @return List
   */
  public List getAllWithSweepstakes()
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setHasSweepstakes( Boolean.TRUE );

    return getPromotionList( queryConstraint );
  }

  // Eagerly load the entities and dependent objects, then hydrate other items based on the
  // association request collection
  @SuppressWarnings( { "unchecked", "null" } )
  public List<Promotion> getPromotionListWithAssociationsForRecognitions( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    Criteria criteria = queryConstraint.buildCriteria();
    criteria.setFetchMode( "budgetMaster", FetchMode.JOIN );
    criteria.setFetchMode( "promotionWebRulesAudiences", FetchMode.JOIN );
    criteria.setFetchMode( "promotionPrimaryAudiences", FetchMode.JOIN );
    criteria.setFetchMode( "promotionSecondaryAudiences", FetchMode.JOIN );
    criteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );

    List<Promotion> promotionList = criteria.list();

    Iterator<Promotion> iter = promotionList.iterator();
    while ( iter.hasNext() )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( iter.next() );
      }
    }

    // ******** Remove the purl & celebration promotions. **********
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      return getExcludedPurlAndCelebPromotionList( (List<Promotion>)promotionList );
    }

    return promotionList;
  }

  // Eagerly load the entities and dependent objects, then hydrate other items based on the
  // association request collection
  @SuppressWarnings( { "unchecked", "null" } )
  public List<Promotion> getPromotionListWithAssociationsForHomePage( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    Criteria criteria = queryConstraint.buildCriteria();

    criteria.setFetchMode( "budgetMaster", FetchMode.JOIN ); // added like 100 millis
    criteria.setFetchMode( "promotionWebRulesAudiences", FetchMode.JOIN );// no change (good)
    criteria.setFetchMode( "promotionPrimaryAudiences", FetchMode.JOIN );// no change (good)
    criteria.setFetchMode( "promotionSecondaryAudiences", FetchMode.JOIN ); // no change (good)
    // criteria.setFetchMode( "promotionNotifications", FetchMode.JOIN ); // added like 180 millis!!
    // (60 on solo)
    // criteria.setFetchMode( "promoMerchCountries", FetchMode.JOIN ); // added like 10-15 millis
    criteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );

    // Removing these three lines to see if it eliminates the classCastExceptions we see sometimes.
    // criteria.setCacheable( true ) ;
    // criteria.setCacheRegion( promotionQueryCacheName ) ;
    // criteria.setCacheMode( CacheMode.NORMAL ) ;

    List<Promotion> promotionList = criteria.list();

    Iterator<Promotion> iter = promotionList.iterator();
    while ( iter.hasNext() )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( iter.next() );
      }
    }

    // ******** Remove the purl & celebration promotions. **********
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      return getExcludedPurlAndCelebPromotionList( (List<Promotion>)promotionList );
    }

    return promotionList;

  }

  public List getPromotionListWithAssociations( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    List promotionList = getPromotionList( queryConstraint );

    Iterator iter = promotionList.iterator();
    while ( iter.hasNext() )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( (Promotion)iter.next() );
      }

    }
    return promotionList;
  }

  /**
   * Get all promotions with sweepstakes with associations
   *
   * @param associationRequestCollection
   * @return List
   */
  public List getAllWithSweepstakesWithAssociations( AssociationRequestCollection associationRequestCollection )
  {

    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setHasSweepstakes( Boolean.TRUE );

    List promotionList = getPromotionList( queryConstraint );

    Iterator iter = promotionList.iterator();
    while ( iter.hasNext() )
    {

      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( (Promotion)iter.next() );
      }

    }
    return promotionList;
  }

  /**
   * Retrieves all the live and expired promotions from the database
   *
   * @return List list of Promotions
   */
  public List getAllLiveAndExpired()
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

    return getPromotionList( queryConstraint );
  }

  /**
   * Retrieves all the live and expired promotions given type from the database
   *
   * @param promotionType
   * @return List list of Promotions
   */
  public List getAllLiveAndExpiredByType( String promotionType )
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( promotionType ) } );

    return getPromotionList( queryConstraint );
  }

  /**
   * Retrieves all the Promotion from the database.
   *
   * @return List a list of Promotions
   */
  public List getAllLive()
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    queryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    return getPromotionList( queryConstraint );
  }

  /**
   * Retrieves all the promotions given type from the database
   *
   * @param promotionType
   * @return List list of Promotions
   */
  public List getAllLiveByType( String promotionType )
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( promotionType ) } );

    return getPromotionList( queryConstraint );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.dao.promotion.PromotionDAO#getAllNonExpired()
   * @return List
   */
  public List getAllNonExpired()
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesExcluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setPromotionTypesExcluded( new PromotionType[] { PromotionType.lookup( PromotionType.BADGE ) } );

    return getPromotionList( queryConstraint );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.dao.promotion.PromotionDAO#getAllExpired()
   * @return List
   */
  public List getAllExpired()
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setPromotionTypesExcluded( new PromotionType[] { PromotionType.lookup( PromotionType.BADGE ) } );

    return getPromotionList( queryConstraint );
  }

  /**
   * Get the Promotion from the database by the id.
   *
   * @param id
   * @return Promotion
   */
  public Promotion getPromotionById( Long id )
  {
    return getPromotionByIdWithAssociations( id, null );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return Promotion
   */
  public Promotion getPromotionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    Promotion promotion = (Promotion)session.get( Promotion.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( promotion );
    }

    return promotion;
  }

  /**
   * Saves the promotion to the database.
   *
   * @param promotion
   * @return Promotion
   */
  public Promotion save( Promotion promotion )
  {
    return (Promotion)HibernateUtil.saveOrUpdateOrShallowMerge( promotion );
  }

  /**
   * Get the promotion claimFormStepElement validation object by the id.
   *
   * @param id
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation getPromotionClaimFormStepElementValidationById( Long id )
  {
    return (PromotionClaimFormStepElementValidation)getSession().get( PromotionClaimFormStepElementValidation.class, id );
  }

  /**
   * Retrieves all the promotion claimFormStepElement validation object.
   *
   * @return List of PromotionClaimFormStepElementValidation objects
   */
  public List getAllPromotionClaimFormStepElementValidations()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllPromotionClaimFormStepElementValidations" );

    return query.list();
  }

  /**
   * Retrieves all the promotion claimFormStepElement validation object.
   *
   * @param promotion
   * @param claimFormStep
   * @return List
   */
  public List getAllPromotionClaimFormStepElementValidations( Promotion promotion, ClaimFormStep claimFormStep )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidationsByStepIdPromoId" );

    query.setLong( "promoId", promotion.getId().longValue() );
    query.setLong( "claimFormStepId", claimFormStep.getId().longValue() );

    return query.list();
  }

  /**
   * saves the promotion claim form step element validation object
   *
   * @param promoCFSEValidation
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation savePromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation promoCFSEValidation )
  {
    return (PromotionClaimFormStepElementValidation)HibernateUtil.saveOrUpdateOrShallowMerge( promoCFSEValidation );
  }

  /**
   * Delete the calimFormStepElement validation param. Overridden from
   *
   * @see com.biperf.core.dao.promotion.PromotionDAO#deletePromotionClaimFormStepElementValidation(com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation)
   * @param pcfsev
   */
  public void deletePromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation pcfsev )
  {
    getSession().delete( pcfsev );
  }

  /**
   * Get a list of validations for all steps for a promotion. Overridden from
   *
   * @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionClaimFormStepElementValidationsByPromotion(com.biperf.core.domain.promotion.Promotion)
   * @param promotion
   * @return List
   */
  public List getPromotionClaimFormStepElementValidationsByPromotion( Promotion promotion )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidationsByPromotion" );

    query.setLong( "promotionId", promotion.getId().longValue() );

    return query.list();
  }

  /**
   * Gets a {@link PromotionClaimFormStepElementValidation} object by promotion and claim form step
   * element.
   *
   * @param promotion
   * @param claimFormStepElement
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation getPromotionClaimFormStepElementValidation( Promotion promotion, ClaimFormStepElement claimFormStepElement )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidationsByPromotionAndClaimFormStepElement" );

    query.setLong( "promotionId", promotion.getId().longValue() );
    query.setLong( "claimFormStepElementId", claimFormStepElement.getId().longValue() );

    return (PromotionClaimFormStepElementValidation)query.uniqueResult();
  }

  /**
   * Retrieves all the child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getChildPromotions( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getChildPromotions" );

    query.setLong( "promotionId", promotionId.longValue() );

    return query.list();
  }

  /**
   * Retrieves all the non expired child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getNonExpiredChildPromotions( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getNonExpiredChildPromotions" );

    query.setLong( "promotionId", promotionId.longValue() );

    return query.list();
  }

  /**
   * Retrieves the promotion for the specified enrollProgramCode.
   * enrollProgramCode should be unique for each promotion.
   *
   * Overridden from @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByEnrollProgramCode(java.lang.String)
   * @param enrollProgramCode
   * @return List
   */
  public List getPromotionByEnrollProgramCode( String enrollProgramCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionByEnrollProgramCode" );

    query.setString( "enrollProgramCode", enrollProgramCode.toLowerCase() );

    return query.list();
  }

  public long getRecognitionsSubmittedForPromotion( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.RecognitionsSubmittedForPromotion" );
    query.setParameter( "promotionId", promotionId );

    if ( query.uniqueResult() != null )
    {
      return ( (Long)query.uniqueResult() ).longValue();
    }
    else
    {
      return 0;
    }
  }

  public long getNominationsSubmittedForPromotion( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.NominationsSubmittedForPromotion" );
    query.setParameter( "promotionId", promotionId );

    if ( query.uniqueResult() != null )
    {
      return ( (Long)query.uniqueResult() ).longValue();
    }
    else
    {
      return 0;
    }

  }

  public long getProductClaimsSubmittedForPromotion( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.ProductClaimsSubmittedForPromotion" );
    query.setParameter( "promotionId", promotionId );

    if ( query.uniqueResult() != null )
    {
      return ( (Long)query.uniqueResult() ).longValue();
    }
    else
    {
      return 0;
    }
  }

  public long getQuizSubmittedForPromotion( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.QuizSubmittedForPromotion" );
    query.setParameter( "promotionId", promotionId );

    if ( query.uniqueResult() != null )
    {
      return ( (Long)query.uniqueResult() ).longValue();
    }
    else
    {
      return 0;
    }
  }

  public BigDecimal getPartnerAwardAmountByPromoitonAndSequenceNo( Long promotionId, int seqNo )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.GetPartnerAwardAmountBySeqNoAndPromotion" );
    query.setParameter( "promotionId", promotionId );
    query.setInteger( "sequenceNo", seqNo );

    BigDecimal result = (BigDecimal)query.uniqueResult();
    return result;

  }

  /* Bug # 34020 start */
  /**
   * @return list
   */
  public List<Promotion> getAllPromotionsWithSweepstakes()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getAllPromotionsWithSweepstakes" );
    return query.list();
  }

  /* Bug # 34020 end */

  public List getPublicRecognitionPromotionsWithClaims()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getPublicRecognitionPromotionsWithClaims" );
    return query.list();
  }

  public String getPromotionQueryCacheName()
  {
    return promotionQueryCacheName;
  }

  public void setPromotionQueryCacheName( String promotionQueryCacheName )
  {
    this.promotionQueryCacheName = promotionQueryCacheName;
  }

  public List getMerchandisePromotionIds()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getMerchandisePromotionIds" );
    return query.list();
  }

  public QuizPromotion getLiveDIYQuizPromotion()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.LiveDIYQuizPromotion" );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    Object obj = query.uniqueResult();
    if ( obj != null )
    {
      return (QuizPromotion)obj;
    }
    return null;
  }

  public QuizPromotion getLiveOrCompletedDIYQuizPromotion()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.LiveOrCompletedDIYQuizPromotion" );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    Object obj = query.uniqueResult();
    if ( obj != null )
    {
      return (QuizPromotion)obj;
    }
    return null;
  }

  public EngagementPromotion getLiveOrCompletedEngagementPromotion()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.LiveOrCompletedEngagementPromotion" );
    Object obj = query.uniqueResult();
    if ( obj != null )
    {
      return (EngagementPromotion)obj;
    }
    return null;
  }

  @Override
  public PromotionCert getPromoCertificateById( Long certificateId )
  {
    Session session = HibernateSessionManager.getSession();
    return (PromotionCert)session.get( PromotionCert.class, certificateId );
  }

  public List<FormattedValueBean> getAwardGenEligiblePromotionList()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.RecognitionPromotion.awardGenPromotion.allLiveRecNonPurlFileLoadPromotions" );
    query.setResultTransformer( Transformers.aliasToBean( FormattedValueBean.class ) );
    return query.list();
  }

  public List<FormattedValueBean> getEngagementEligiblePromotionList()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.engagement.allEngagementEligiblePromotions" );
    query.setResultTransformer( Transformers.aliasToBean( FormattedValueBean.class ) );
    return query.list();
  }

  public List<FormattedValueBean> getEngagementRecognitionPromotionsList()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.reportParameterValueChoices.allEngagementRecognitionPromotions" );
    query.setResultTransformer( Transformers.aliasToBean( FormattedValueBean.class ) );
    return query.list();
  }

  @Override
  public List<Promotion> getAllBadges()
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.BADGE ) } );

    return getPromotionList( queryConstraint );
  }

  public List<Long> getEligiblePromotionsFromPromoBadgeId( Long promoBadgeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getEligiblePromotionsFromPromoBadgeId" );
    query.setParameter( "promoBadgeId", promoBadgeId );
    return query.list();
  }

  public Integer getBadgePromotionCountForPromoId( Long promoId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamfication.getBadgePromotionCount" );
    query.setParameter( "eligPromoId", promoId );
    Integer result = (Integer)query.uniqueResult();
    return result;
  }

  public Integer getPromotionSelfRecognition( Long promoId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.select_is_self_recognition_enabled" );
    query.setParameter( "promotionId", promoId );
    Integer result = (Integer)query.uniqueResult();
    return result;
  }

  public List<CelebrationImageFillerValue> getCelebrationImageFillersForPromotion( Long promotionId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.promotion.CelebrationImageFillersForPromotion" );
    query.setParameter( "promotionId", promotionId );
    query.setResultTransformer( new CelebrationImageFillerResultTransformer() );
    return query.list();
  }

  private class CelebrationImageFillerResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      CelebrationImageFillerValue bean = new CelebrationImageFillerValue();
      bean.setImage1NumberEnabled( ( (Boolean)tuple[0] ).booleanValue() );
      bean.setImage1Name( extractString( tuple[1] ) );
      bean.setImage2NumberEnabled( ( (Boolean)tuple[2] ).booleanValue() );
      bean.setImage2Name( extractString( tuple[3] ) );
      bean.setImage3NumberEnabled( ( (Boolean)tuple[4] ).booleanValue() );
      bean.setImage3Name( extractString( tuple[5] ) );
      bean.setImage4NumberEnabled( ( (Boolean)tuple[6] ).booleanValue() );
      bean.setImage4Name( extractString( tuple[7] ) );
      bean.setImage5NumberEnabled( ( (Boolean)tuple[8] ).booleanValue() );
      bean.setImage5Name( extractString( tuple[9] ) );
      return bean;
    }
  }

  @Override
  public EngagementPromotion getLiveEngagementPromotion()
  {
    PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.ENGAGEMENT ) } );
    List promotionList = getPromotionList( queryConstraint );
    if ( promotionList != null && promotionList.size() > 0 )
    {
      return (EngagementPromotion)promotionList.get( 0 );
    }
    return null;
  }

  @Override
  public List getPurlRecipinentListOnPurlTile()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getPurlRecognitionPromotionsListOnPurlTile" );
    return query.list();
  }

  public boolean isRecogPromotionInRPM( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.isRecogPromotionInRPM" );
    query.setParameter( "promotionId", promotionId );
    return query.list().size() > 0;
  }

  public List<MerchOrder> getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    return query.list();
  }

  public boolean getMerchOrderByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getMerchOrderByPromotionIdAndUserId" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    return (Integer)query.uniqueResult() > 0;
  }

  @Override
  public List<PromotionsValueBean> getAllSortedApproverPromotions( Long userId, String promotionType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getAllSortedApproverPromotions" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionType", promotionType );
    query.setResultTransformer( new PromotionsValueBeanResultTransformer() );
    return query.list();
  }

  private class PromotionsValueBeanResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      PromotionsValueBean bean = new PromotionsValueBean();
      bean.setId( (Long)tuple[0] );
      bean.setName( extractString( tuple[1] ) );
      bean.setApprovalEndDate( extractString( tuple[2] ) );
      return bean;
    }
  }

  @Override
  public List getPromotionBillCodes( Long promotionId, boolean sweepstakes )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromoBillCodesByBillCodeType" );
    query.setLong( "promotionId", promotionId.longValue() );
    query.setLong( "sweepsBillCode", sweepstakes ? 1 : 0 );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<NominationSubmitDataECardValueBean> getECards( Long promotionId, String userLocale )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getNominationECards" );

    query.setLong( "promotionId", promotionId.longValue() );
    query.setString( "userLocale", userLocale );
    query.setResultTransformer( new NominationsECardResultTransformer() );

    return query.list();
  }

  @SuppressWarnings( "serial" )
  private static class NominationsECardResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NominationSubmitDataECardValueBean bean = new NominationSubmitDataECardValueBean();

      bean.setId( (Long)tuple[0] );
      bean.setName( (String)tuple[1] );
      bean.setSmallImage( (String)tuple[2] );
      bean.setLargeImage( (String)tuple[3] );
      bean.setCanEdit( (Boolean)tuple[4] );
      bean.setCardType( (String)tuple[5] );

      bean.setTranslatable( (Boolean)tuple[6] );
      bean.setLocale( (String)tuple[7] );

      return bean;
    }
  }

  @Override
  public NominationSubmitDataDrawSettingsValueBean getDrawToolSettings( Long promotionId )
  {
    NominationSubmitDataDrawSettingsValueBean settings = new NominationSubmitDataDrawSettingsValueBean();

    // Only the two boolean flags come from the query - sizes and colors are hardcoded
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getCardSettings" );

    query.setLong( "promotionId", promotionId.longValue() );

    // The query should give a single result - a few setting values.
    Object[] results = (Object[])query.uniqueResult();
    settings.setCanUpload( (Boolean)results[0] );
    settings.setCanDraw( (Boolean)results[1] );

    // Sizes and colors are hardcoded settings

    Integer[] is = new Integer[] { 4, 8, 16, 32 };
    settings.setSizes( is );

    settings.addColor( new NominationSubmitDataColorSettingValueBean( "000000", "black" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "ffffff", "white" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "676767", "dark gray" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "ADADAD", "gray" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "E9E9E9", "light gray" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "960000", "burgundy" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "F50008", "red" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "FB5A15", "orange" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "FCA605", "tangerine" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "FEE150", "gold" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "F8F338", "yellow" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "67CF31", "green" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "26821F", "grass" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "0B4E1B", "pine" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "73B592", "seafoam" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "2AA3E5", "sky" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "1747BE", "blue" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "0B2666", "navy" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "5B3999", "indigo" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "AB4BCB", "purple" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "FB77A9", "pink" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "664629", "brown" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "B28E48", "tan" ) );
    settings.addColor( new NominationSubmitDataColorSettingValueBean( "F2D992", "sand" ) );

    return settings;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<String> getBehaviorTypes( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getBehaviorTypes" );

    query.setLong( "promotionId", promotionId.longValue() );

    // Each result is a single String - the behavior type code
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Long> getPromotionIdsForBehavior( String behaviorType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getPromotionIdsForBehavior" );

    query.setString( "behaviorType", behaviorType );

    return query.list();
  }

  public List getApprovalOptionsByApproverId( Long approverId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getApprovalOptionsByApproverId" );

    query.setLong( "approverId", approverId );

    return query.list();
  }

  public ApproverOption getApproverTypeByLevel( Long approvalLevel, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getApprovalOptionsByPromotionId" );

    query.setLong( "approvalLevel", approvalLevel );
    query.setLong( "promotionId", promotionId );
    return (ApproverOption)query.uniqueResult();
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public List<ApproverOption> getApproverOptions( int optionId )
  {
    Query query = getSession().createQuery( "from ApproverOption o where  o.id = :optionId" );
    query.setParameter( "optionId", optionId );
    return query.list();

  }

  @Override
  @SuppressWarnings( "unchecked" )
  public List<NominationApproverValueBean> getApproverOptions( int levelId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getApproverList" );
    query.setParameter( "levelId", levelId );
    query.setParameter( "promotionId", promotionId );
    query.setResultTransformer( new NominationViewCustomApproverResultTransformer() );
    return query.list();

  }

  @Override
  public String getLastWizardStepName( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getLastStepName" );
    query.setParameter( "promotionId", promotionId );
    return (String)query.uniqueResult();
  }

  public BigDecimal getTotalUnapprovedAwardQuantity( Long promotionId, Long userId, Long nodeId, Long budgetMasterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getTotalUnapprovedAwardQuantity" );
    String queryString = query.getQueryString();
    if ( Objects.isNull( budgetMasterId ) )
    {
      queryString = queryString + " AND c.promotion_id = :promotionId";
    }
    else
    {
      queryString = queryString + " AND p.award_budget_master_id = :budgetMasterId";
    }
    if ( userId == null && nodeId == null )
    {
      Query updatedQuery = getSession().createSQLQuery( queryString );
      if ( Objects.isNull( budgetMasterId ) )
      {
        updatedQuery.setParameter( "promotionId", promotionId );
      }
      else
      {
        updatedQuery.setParameter( "budgetMasterId", budgetMasterId );
      }
      return (BigDecimal)updatedQuery.uniqueResult();
    }
    else if ( userId != null && nodeId == null )
    {
      queryString = queryString + " AND c.submitter_id = :userId";
      Query updatedQuery = getSession().createSQLQuery( queryString );
      if ( Objects.isNull( budgetMasterId ) )
      {
        updatedQuery.setParameter( "promotionId", promotionId );
      }
      else
      {
        updatedQuery.setParameter( "budgetMasterId", budgetMasterId );
      }
      updatedQuery.setParameter( "userId", userId );
      return (BigDecimal)updatedQuery.uniqueResult();
    }
    else if ( userId == null && nodeId != null )
    {
      queryString = queryString + " AND c.node_id = :nodeId";
      Query updatedQuery = getSession().createSQLQuery( queryString );
      if ( Objects.isNull( budgetMasterId ) )
      {
        updatedQuery.setParameter( "promotionId", promotionId );
      }
      else
      {
        updatedQuery.setParameter( "budgetMasterId", budgetMasterId );
      }
      updatedQuery.setParameter( "nodeId", nodeId );
      return (BigDecimal)updatedQuery.uniqueResult();
    }
    return new BigDecimal( 0 );
  }

  public long getClaimAwardQuantity( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getClaimAwardQty" );
    query.setParameter( "claimId", claimId );

    return (long)query.uniqueResult();
  }

  public BigDecimal getTotalUnapprovedAwardQuantityPurl( Long promotionId, Long userId, Long nodeId, Long budgetMasterId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getTotalUnapprovedAwardQuantityPurl" );
    String queryString = query.getQueryString();
    if ( Objects.isNull( budgetMasterId ) )
    {
      queryString = queryString + " AND pr.promotion_id = :promotionId";
    }
    else
    {
      queryString = queryString + " AND p.AWARD_BUDGET_MASTER_ID = :budgetMasterId";
    }
    if ( userId == null && nodeId == null )
    {
      Query updatedQuery = getSession().createSQLQuery( queryString );
      if ( Objects.isNull( budgetMasterId ) )
      {
        updatedQuery.setParameter( "promotionId", promotionId );
      }
      else
      {
        updatedQuery.setParameter( "budgetMasterId", budgetMasterId );
      }
      return (BigDecimal)updatedQuery.uniqueResult();
    }
    else if ( userId != null && nodeId == null )
    {
      queryString = queryString + " AND pr.submitter_id = :userId";
      Query updatedQuery = getSession().createSQLQuery( queryString );
      if ( Objects.isNull( budgetMasterId ) )
      {
        updatedQuery.setParameter( "promotionId", promotionId );
      }
      else
      {
        updatedQuery.setParameter( "budgetMasterId", budgetMasterId );
      }
      updatedQuery.setParameter( "userId", userId );

      return (BigDecimal)updatedQuery.uniqueResult();
    }
    else if ( userId == null && nodeId != null )
    {
      queryString = queryString + " AND pr.submitter_node_id = :nodeId";
      Query updatedQuery = getSession().createSQLQuery( queryString );
      if ( Objects.isNull( budgetMasterId ) )
      {
        updatedQuery.setParameter( "promotionId", promotionId );
      }
      else
      {
        updatedQuery.setParameter( "budgetMasterId", budgetMasterId );
      }
      updatedQuery.setParameter( "nodeId", nodeId );

      return (BigDecimal)updatedQuery.uniqueResult();
    }
    return new BigDecimal( 0 );
  }

  public long getTotalImportPaxAwardQuantity( Long importFileId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.getTotalImportPaxAwardQuantity" );
    query.setParameter( "importFileId", importFileId );
    return (long)query.uniqueResult();

  }

  @SuppressWarnings( "serial" )
  private static class NominationViewCustomApproverResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NominationApproverValueBean bean = new NominationApproverValueBean();

      bean.setUsername( (String)tuple[0] );
      bean.setLastname( (String)tuple[1] );
      bean.setFirstname( (String)tuple[2] );
      bean.setApproverType( (String)tuple[3] );
      bean.setApproverValue( (String)tuple[4] );
      return bean;
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<NameableBean> getNominationPromotionListForApproverFileLoad()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getNominationPromotionListForApproverFileLoad" );
    query.setResultTransformer( new NominationPromotionListForApproverFileLoadResultTransformer() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private static class NominationPromotionListForApproverFileLoadResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NameableBean bean = new NameableBean( (long)tuple[0], (String)tuple[1] );

      return bean;
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<NominationAdminApprovalsBean> getNominationApprovalClaimPromotions()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getNominationApprovalClaimPromotions" );
    query.setResultTransformer( new NominationAdminApproverResultTransformer() );
    return query.list();
  }

  @Override
  public Date getUAGoalQuestPromotionStartDate( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getUAGoalQuestPromotionStartDate" );
    query.setParameter( "userId", userId );
    return (Date)query.uniqueResult();
  }

  @SuppressWarnings( "serial" )
  private static class NominationAdminApproverResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NominationAdminApprovalsBean bean = new NominationAdminApprovalsBean();

      bean.setId( (Long)tuple[0] );
      bean.setPromotionName( (String)tuple[1] );
      bean.setLiveDate( DateUtils.toDisplayString( (Date)tuple[2] ) );
      return bean;
    }
  }

  @Override
  public boolean isSSILivePromotionAvailable()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = null;
    query = session.createSQLQuery( "select PROMOTION_ID from PROMOTION where PROMOTION_TYPE ='self_serv_incentives' and PROMOTION_STATUS='live'" );
    return query.uniqueResult() != null;
  }

  @Override
  public boolean checkIfAnyPointsContestsByPaxId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.checkIfAnyPointsContestsByPaxId" );

    query.setParameter( "userId", userId );

    return query.list() != null && query.list().size() > 0;

  }

  @Override
  public List<String> getAllUniqueBillCodes( Long promotionId )
  {
    Criteria criteria = getSession().createCriteria( PromotionBillCode.class );
    criteria.setProjection( Projections.distinct( Projections.property( "billCode" ) ) );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    return criteria.list();
  }

  // Alerts Performance Tuning
  @Override
  @SuppressWarnings( "unchecked" )
  public List<PromotionBean> getExpiredPromotions()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getExpiredPromotionBeans" );
    query.setResultTransformer( new ExpiredPromotionsResultTransformer() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private static class ExpiredPromotionsResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      PromotionBean bean = new PromotionBean();
      bean.setPromotionId( (Long)tuple[0] );
      bean.setPromotionType( (String)tuple[1] );
      return bean;
    }
  }

  @Override
  public List<RecognitionAdvisorPromotionValueBean> getPromotionListForRA( Long giverId, Long receiverId )
  {
    List<RecognitionAdvisorPromotionValueBean> raValueBeanList = null;
    Map results = new HashMap();
    CallPrcPromotionForRA promoForRA = new CallPrcPromotionForRA( dataSource );
    results = promoForRA.executeProcedure( giverId, receiverId );
    List<RecognitionAdvisorPromotionValueBean> recAdvisorPromoValueBean = validateOutput( raValueBeanList, results );
    return recAdvisorPromoValueBean;
  }

  private List<RecognitionAdvisorPromotionValueBean> validateOutput( List<RecognitionAdvisorPromotionValueBean> raValueBeanList, @SuppressWarnings( "rawtypes" ) Map results )
  {
    if ( BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
    }
    else
    {
      raValueBeanList = (List<RecognitionAdvisorPromotionValueBean>)results.get( "p_out_user_data" );
    }
    return raValueBeanList;
  }

  public boolean isBehaviorBasedApproverTypeExist( Long promotionId )
  {
    boolean found = false;

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromoApproverOptionBehaviorCount" );
    query.setParameter( "promotionId", promotionId );

    Integer count = (Integer)query.uniqueResult();
    found = count.intValue() > 0;

    return found;
  }

  public int deletePromoCard( Long cardId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromotionECard.Delete" );
    query.setLong( "cardId", cardId );
    query.setLong( "promotionId", promotionId );

    return query.executeUpdate();

  }

  @Override
  public List<PromoRecImageData> getNotMigratedPromRecogAvatarData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.RecognitionPromotion.getNotMigratedPromRecogAvatarData" );
    query.setResultTransformer( Transformers.aliasToBean( PromoRecImageData.class ) );
    return (List<PromoRecImageData>)query.list();
  }

  @Override
  public void updatePromRecAvatar( Long promotionId, String defaultCelebrationAvatar, String defaultCcontributorAavatar )
  {
    String query = "UPDATE PROMO_RECOGNITION SET DEFAULT_CELEBRATION_AVATAR = ?, DEFAULT_CONTRIBUTOR_AVATAR = ? WHERE PROMOTION_ID = ?";

    Object[] params = { defaultCelebrationAvatar, defaultCcontributorAavatar, promotionId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "DAO Layer, the promotionId id : " + promotionId + " : " + e );
    }

  }

  @Override
  public List<String> getNonPurlAndCelebPromotionsName()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getNonPurlAndCelebPromotionsName" );
    return (List<String>)query.list();
  }

  private List<Promotion> getExcludedPurlAndCelebPromotionList( List<Promotion> promotionList )
  {
    List<Promotion> promoListExptPurlAndCeleb = new ArrayList<>();

    for ( Promotion promotion : promotionList )
    {
      if ( promotion instanceof RecognitionPromotion )
      {
        RecognitionPromotion recPromotion = (RecognitionPromotion)promotion;
        if ( recPromotion.isIncludePurl() || recPromotion.isIncludeCelebrations() )
        {
          continue;
        }
      }
      promoListExptPurlAndCeleb.add( promotion );
    }

    return promoListExptPurlAndCeleb;
  }

  @Override
  public void updatePromRecCeleAvatar( Long promotionId, String defaultCelebrationAvatar )
  {
    String query = "UPDATE PROMO_RECOGNITION SET DEFAULT_CELEBRATION_AVATAR = ? WHERE PROMOTION_ID = ?";

    Object[] params = { defaultCelebrationAvatar, promotionId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "DAO Layer, the promotionId id : " + promotionId + " : " + e );
    }

  }

  @Override
  public void updatePromRecContrAvatar( Long promotionId, String defaultContributorAavatar )
  {
    String query = "UPDATE PROMO_RECOGNITION SET DEFAULT_CONTRIBUTOR_AVATAR = ? WHERE PROMOTION_ID = ?";

    Object[] params = { defaultContributorAavatar, promotionId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "DAO Layer, the promotionId id : " + promotionId + " : " + e );
    }

  }

  @Override
  public List<PromoRecPictureData> getNotMigratedPromRecogPictureData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getNotMigratedPromRecogPictureData" );
    query.setResultTransformer( Transformers.aliasToBean( PromoRecPictureData.class ) );
    return (List<PromoRecPictureData>)query.list();
  }

  @Override
  public void updateContResPic( Long promotionId )
  {
    String query = "UPDATE PROMO_RECOGNITION SET IS_CONT_RES_MIGRATED = 1 WHERE PROMOTION_ID = ?";

    Object[] params = { promotionId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "DAO Layer, the promotionId id : " + promotionId + " : " + e );
    }

  }
  // Client customizations for WIP #42701 starts
  public boolean isCashPromo( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.isCashPromo" );
    query.setLong( "promotionId", promotionId.longValue() );
    Boolean result = (Boolean)query.uniqueResult();
    if ( result == null )
      return false;
    else
      return result.booleanValue();
  }
  // Client customizations for WIP #42701 ends
  
//Client customization for WIP #39189 starts
 public Long getPromotionIdByClaimId( Long claimId )
 {
   Query query = getSession().createSQLQuery( "SELECT promotion_id FROM claim WHERE claim_id=:claimId" );
   query.setParameter( "claimId", claimId );
   return ( (BigDecimal)query.uniqueResult() ).longValue();
 }
 // Client customization for WIP #39189 ends
 
  // Client customizations for WIP #62128 starts
  public Long getCheersPromotionId() throws EmptyResultDataAccessException
  {
    String fetchCheersIdSql = "SELECT p.promotion_id from PROMOTION p , PROMO_RECOGNITION pr WHERE p.PROMOTION_ID = pr.PROMOTION_ID AND p.PROMOTION_STATUS ='live' AND p.PROMOTION_TYPE = 'recognition' AND pr.ADIH_IS_CHEERS = 1";
    return jdbcTemplate.queryForObject( fetchCheersIdSql, Long.class );
  }
  // Client customizations for WIP #62128 ends
}
