
package com.biperf.core.dao.gamification.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.ujac.util.StringUtils;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeBehaviorPromotion;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgePromotionLevels;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.SqlQueryBuilder;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class GamificationDAOImpl extends BaseDAO implements GamificationDAO
{
  // Gamification DAO Impl Methods Starts Here
  private static final Log logger = LogFactory.getLog( GamificationDAOImpl.class );
  private JdbcTemplate jdbcTemplate;
  private DataSource dataSource;
  private static final String PROMOTION_LEVELS_BY_COUNTRY = "SELECT pmc.country_id as countryId,pmc.PROGRAM_ID as programId, pgl.promo_merch_program_level_id as levelId,cmlevel.cm_value as levelName,upper(c.country_code) as countryCode "
      + " FROM PROMO_MERCH_PROGRAM_LEVEL pgl, PROMO_MERCH_country pmc,country c,"
      + " (SELECT  ca.code, to_char(substr(cd.VALUE, 1, 4000)) AS cm_value FROM cms_content_data cd,cms_content cc,cms_content_key ck,cms_asset_type caa,cms_asset ca "
      + "   WHERE cd.content_id = cc.ID AND cc.content_key_id = ck.ID AND cc.content_status = 'Live' AND ck.asset_id = ca.ID AND ca.asset_type_id = caa.ID) cmlevel"
      + " WHERE  pmc.promo_merch_country_id = pgl.promo_merch_country_id   AND pgl.program_id = pmc.program_id  AND cmlevel.code = pgl.cm_asset_key and pmc.country_id=c.country_id  and pmc.promotion_id=? "
      + " order by pmc.promotion_id, pmc.country_id,pgl.promo_merch_program_level_id ";

  private static final String GOALQUEST_LEVELS = "select 0 as countryId, 0 as programId, goallevel_id as levelId," + " vw.cms_value as levelName,' ' as countryCode" + " from goalquest_goallevel ggl,"
      + " vw_cms_asset_value vw" + " WHERE vw.asset_code = ggl.goal_level_cm_asset_code" + " AND ggl.promotion_id=?"
      + " AND locale=(select String_val from os_propertyset where entity_name = 'default.language')" + " AND vw.KEY='GOALS'" + " order by goallevel_id";

  private static final String PROMOTION_RECOGNITION_BEHAVIORS = "select distinct a.cms_code AS behaviorCode,a.CMS_NAME AS behaviorName  from vw_cms_code_value a, promo_behavior b where  a.asset_code like 'picklist.promo.recognition.behavior.items' and a.CMS_code = b.BEHAVIOR_TYPE and a.locale = (select String_val from os_propertyset where entity_name = 'default.language') ";

  private static final String PROMOTION_NOMINATION_BEHAVIORS = "select distinct a.cms_code AS behaviorCode,a.CMS_NAME AS behaviorName  from vw_cms_code_value a, promo_behavior b where  a.asset_code like 'picklist.promo.nomination.behavior.items' and a.CMS_code = b.BEHAVIOR_TYPE and a.locale = (select String_val from os_propertyset where entity_name = 'default.language') ";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBadgeById(java.lang.Long)
   * 
   * @param id
   * @return Badge
   */
  public Badge getBadgeById( Long id )
  {
    return (Badge)getSession().get( Badge.class, id );
  }

  public Badge getBadgeByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Badge badge = (Badge)getSession().get( Badge.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( badge );
    }

    return badge;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBadgeRuleById(java.lang.Long)
   * 
   * @param id
   * @return BadgeRule
   */
  public BadgeRule getBadgeRuleById( Long id )
  {
    return (BadgeRule)getSession().get( BadgeRule.class, id );
  }

  /**
   * Gets the Badge Rule by the name.
   * 
   * @param id
   * @return BadgeRule
   */

  public BadgeRule getBadgeRuleByBadgeName( String badgeName, Long badePromotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeRuleByBadgeName" );
    query.setParameter( "badgeName", badgeName );
    query.setParameter( "badePromotionId", badePromotionId );
    query.setMaxResults( 1 );
    BadgeRule b = (BadgeRule)query.list().get( 0 );
    return b;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBadgeByParticipantId(java.lang.Long)
   * 
   * @param userId
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeByParticipantId( Long userId )
  {
    Criteria crit = getSession().createCriteria( ParticipantBadge.class );
    crit.add( Restrictions.eq( "participant.id", userId ) );
    // crit.addOrder( Order.desc( "badge.badgeType.code" ) );
    crit.addOrder( Order.desc( "isEarned" ) );
    crit.addOrder( Order.desc( "earnedDate" ) );
    List partBadgeList = crit.list();

    return partBadgeList;
  }
  
  public List<Long> getBadgeEligiblePromotionIds()
  {
    Criteria crit = getSession().createCriteria( BadgePromotion.class );
    crit.setProjection( Projections.distinct( Projections.property( "eligiblePromotion.id" ) ) );
    
    return crit.list();
    
  }
  // Methods to pull pax badges for a recognition promotion

  public List<ParticipantBadge> getBadgeByParticipantPromotionEarnedHighLight( Long userId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeByParticipantPromotionEarnedHighLight" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.BEHAVIOR );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantPromotionProgress( Long userId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeByParticipantPromotionProgress" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedProgress( Long userId, int rowNumber )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesProgressEarned" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedHighLight( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesEarnedHighlight" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.BEHAVIOR );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedOld( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesEarnedOld" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.BEHAVIOR );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantProgress( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesProgressStarted" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantNotEarnedProgress( Long userId, int rowNumber )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesProgressNotEarned" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantBehavior( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesBehavior" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.BEHAVIOR );
    return query.list();
  }

  public List<BadgeRule> getBadgeByParticipantBehaviorNotEarned( Long[] promotionId, Long userId )
  {
    List<BadgeRule> badgeRuleList = new ArrayList<BadgeRule>();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgeBehaviorNotEarned" );
    query.setParameter( "userId", userId );
    query.setParameterList( "promotionId", promotionId );
    query.setParameter( "badgeType", BadgeType.BEHAVIOR );

    if ( promotionId != null && promotionId.length > 0 )
    {
      badgeRuleList = query.list();
    }

    return badgeRuleList;
  }

  public List<BadgeRule> getBadgeByParticipantProgressNotStarted( Long[] promotionId, Long userId )
  {
    List<BadgeRule> badgeRuleList = new ArrayList<BadgeRule>();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeRulesProgressNotStarted" );
    query.setParameter( "userId", userId );
    query.setParameterList( "promotionId", promotionId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );

    if ( promotionId != null && promotionId.length > 0 )
    {
      badgeRuleList = query.list();
    }
    return badgeRuleList;
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedFileLoad( Long userId, int rowNumber )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesEarnedFileLoad" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType1", BadgeType.EARNED_OR_NOT_EARNED );
    query.setParameter( "badgeType2", BadgeType.FILELOAD );
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBadgesForRecognitionConfirmationScreen(java.lang.Long,java.lang.Long)
   * 
   * @param promotionId
   * @param userId
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgesForRecognitionConfirmationScreen( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgesForRecognitionConfirmationScreen" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    query.setParameter( "badgeCountType1", "total" );
    query.setParameter( "badgeCountType2", "given" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBadgeParticipantHistory(java.lang.Long)
   * 
   * @param userId
   * @return List<ParticipantBadge>
   */
  public List<ParticipantBadge> getBadgeParticipantHistory( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgesHistory" );
    query.setParameter( "userId", userId );
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBadgeByStatus(java.lang.String)
   * 
   * @param id
   * @return List<Badge>
   */
  public List<Badge> getBadgeByStatus( String status )
  {
    List<Badge> badgeList = new ArrayList<Badge>();
    Criteria crit = getSession().createCriteria( Badge.class );
    crit.add( Restrictions.eq( "status", status ) );
    crit.addOrder( Order.asc( "promotionName" ).ignoreCase() );
    badgeList = crit.list();
    return badgeList;
  }

  /**
   * Gets the list of Badge by status and type
   * 
   * @param status
   * @param type
   * @return List<Badge>
   */
  public List<Badge> getBadgeByTypeAndStatus( String type, String status )
  {
    BadgeQueryConstraint queryConstraint = new BadgeQueryConstraint();
    queryConstraint.setBadgeStatusType( status );
    queryConstraint.setBadgeType( BadgeType.lookup( type ) );
    return HibernateUtil.getObjectList( queryConstraint );

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBadgeByPromotion(java.lang.Long)
   * @param promotionId
   * @return List<Badge>
   */
  public List<Badge> getBadgeByPromotion( Long promotionId )
  {
    BadgeQueryConstraint queryConstraint = new BadgeQueryConstraint();
    // queryConstraint.setPromotionId( promotionId );
    queryConstraint.setBadgePromoId( promotionId );
    queryConstraint.setBadgeStatusType( Badge.BADGE_ACTIVE );
    return HibernateUtil.getObjectList( queryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetBehaviorForSelectedPromotions(java.lang.String)
   * @param promotionIds
   * @return List
   */

  public List getBehaviorForSelectedPromotions( String promotionIds, String promotionTypeCode )
  {
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
    if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      queryBuilder.append( PROMOTION_RECOGNITION_BEHAVIORS );
    }
    else
    {
      queryBuilder.append( PROMOTION_NOMINATION_BEHAVIORS );
    }
    queryBuilder.append( " and b.promotion_id in(" + promotionIds + ")" );
    String queryString = queryBuilder.toString();
    RowMapper rowMapper = new GetBehaviorPromotionRowMapper();
    List<BadgeBehaviorPromotion> results = jdbcTemplate.query( queryString, rowMapper );

    return results;

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetPromotionsNameForBehavior(java.lang.String,java.lang.String)
   * @param behavior
   * @param promotionIds
   * @return List
   */
  public List getPromotionsNameForBehavior( String behavior, String promotionIds )
  {
    StringBuilder queryString = new StringBuilder( "" );
    queryString.append( "select distinct p.promotion_name as promotionName from promotion p,promo_behavior pb where p.promotion_id=pb.promotion_id and pb.behavior_type='" + behavior + "'" );
    if ( !com.biperf.core.utils.StringUtil.isEmpty( promotionIds ) )
    {
      queryString.append( " and pb.promotion_id in(" + promotionIds + ")" );
    }
    Query query = getSession().createSQLQuery( queryString.toString() );
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.gamification.GamificationDao#getgetLevelsByPromotions(long,java.lang.String)
   * @param promotionId
   * @param promotionType
   * @return List<BadgePromotionLevels>
   */
  public List<BadgePromotionLevels> getLevelsByPromotions( long promotionId, String promotionType )
  {
    List bindVariables = new ArrayList();
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
    if ( promotionType.equalsIgnoreCase( "goalQuest" ) || promotionType.equalsIgnoreCase( "challengepoint" ) )
    {
      queryBuilder.append( GOALQUEST_LEVELS );
    }
    else
    {
      queryBuilder.append( PROMOTION_LEVELS_BY_COUNTRY );
    }
    bindVariables.add( promotionId );
    String queryString = queryBuilder.toString();
    RowMapper rowMapper = new GetLevelsPromotionRowMapper();
    List<BadgePromotionLevels> results = jdbcTemplate.query( queryString, bindVariables.toArray(), rowMapper );
    return results;
  }

  private class GetLevelsPromotionRowMapper implements RowMapper<BadgePromotionLevels>
  {

    /**
     * Overridden from
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     * @param rs
     * @param rowNum
     * @return Map valueMapByColumn
     * @throws SQLException
     */
    public BadgePromotionLevels mapRow( ResultSet rs, int rowNum ) throws SQLException
    {

      BadgePromotionLevels promoLevels = new BadgePromotionLevels();
      promoLevels.setCountryId( rs.getLong( "countryId" ) );
      promoLevels.setProgramId( rs.getString( "programId" ) );
      promoLevels.setLevelId( rs.getLong( "levelId" ) );
      promoLevels.setLevelName( rs.getString( "levelName" ) );
      promoLevels.setCountryCode( rs.getString( "countryCode" ) );
      return promoLevels;
    }
  }

  private class GetBehaviorPromotionRowMapper implements RowMapper<BadgeBehaviorPromotion>
  {

    /**
     * Overridden from
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     * @param rs
     * @param rowNum
     * @return Map valueMapByColumn
     * @throws SQLException
     */
    public BadgeBehaviorPromotion mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      String behaviorCode = rs.getString( "behaviorCode" );
      BadgeBehaviorPromotion promoBehaviors = new BadgeBehaviorPromotion();
      promoBehaviors.setBehaviorCode( behaviorCode );
      promoBehaviors.setBehaviorName( rs.getString( "behaviorName" ) );
      return promoBehaviors;
    }
  }

  /**
   * Overridden from
   * @see com.biperf.core.dao.gamification.GamificationDao#saveBadge(com.biperf.core.domain.gamification.Badge)
   * @param badge
   * @return Badge
   */
  public Badge saveBadge( Badge badge )
  {
    return (Badge)HibernateUtil.saveOrUpdateOrShallowMerge( badge );
  }

  /**
   * Overridden from
   * @see com.biperf.core.dao.gamification.GamificationDao#saveBadge(com.biperf.core.domain.gamification.BadgeRule)
   * @param badgeRule
   * @return BadgeRule
   */
  public BadgeRule saveBadgeRule( BadgeRule badgeRule )
  {
    return (BadgeRule)HibernateUtil.saveOrUpdateOrShallowMerge( badgeRule );
  }

  /**
   * Overridden from
   * @see com.biperf.core.dao.gamification.GamificationDao#saveBadgePromotion(com.biperf.core.domain.gamification.BadgePromotion)
   * @param badgePromotion
   * @return BadgePromotion
   */
  public BadgePromotion saveBadgePromotion( BadgePromotion badgePromotion )
  {
    return (BadgePromotion)HibernateUtil.saveOrUpdateOrShallowMerge( badgePromotion );
  }

  public void getSessionLockForParticipantBadge( BadgeRule badgeRule, Long userId ) throws ServiceErrorException
  {
    long sleepTime = 5000;
    int option = 1;
    while ( option <= 7 )
    {
      try
      {
        Query query = getSession()
            .createSQLQuery( "select * from participant_badge where participant_Id=:userId and badge_rule_id=:badgeRuleId and promotion_id in (select promotion_id from badge where badge_type = :badgeType) for update" );
        query.setParameter( "userId", userId );
        query.setParameter( "badgeRuleId", badgeRule.getId() );
        query.setParameter( "badgeType", BadgeType.PROGRESS );
        query.list();
        break;
      }
      catch( Exception se )
      {
        try
        {
          switch ( option )
          {
            case 1:
              Thread.sleep( sleepTime );
              option++;
              break;
            case 2:
              Thread.sleep( (long) ( 10000 + Math.abs( Math.random() * 20 ) * 1000 ) );
              option++;
              break;
            case 3:
              Thread.sleep( (long) ( 60000 + Math.abs( Math.random() * 20 ) * 1000 ) );
              option++;
              break;
            case 4:
              Thread.sleep( (long) ( 600000 + Math.abs( Math.random() * 20 ) * 60000 ) );
              option++;
              break;
            case 5:
              Thread.sleep( (long) ( 1800000 + Math.abs( Math.random() * 20 ) * 60000 ) );
              option++;
              break;
            case 6:
              Thread.sleep( (long) ( 3600000 + Math.abs( Math.random() * 20 ) * 60000 ) );
              option++;
              break;
            default:
              ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.SAVE_PAX_BADGE_ERR );
              throw new ServiceErrorException( serviceError );
          }
        }
        catch( InterruptedException e1 )
        {
          e1.printStackTrace();
        }
        logger.error( se.getMessage(), se );
      }
    }
  }

  /**
   * Overridden from
   * @see com.biperf.core.dao.gamification.GamificationDao#saveParticipantBadge(com.biperf.core.domain.gamification.ParticipantBadge)
   * @param participantBadge
   * @return ParticipantBadge
   * @throws ServiceErrorException 
   * @throws ServiceErrorExceptionWithRollback 
   */
  public ParticipantBadge saveParticipantBadge( ParticipantBadge participantBadge )
  {
    // return (ParticipantBadge)HibernateUtil.saveOrUpdateOrShallowMerge(participantBadge);
    // getSession().lock(ParticipantBadge.class, LockMode.UPGRADE);

    ParticipantBadge participantBadge1 = null;
    // getSession().saveOrUpdate(participantBadge);
    participantBadge1 = (ParticipantBadge)HibernateUtil.saveOrUpdateOrShallowMerge( participantBadge );

    return participantBadge1;
  }

  /**
   * Overridden from
   * @see com.biperf.core.dao.gamification.GamificationDao#isBadgeNameExists(java.lang.String)
   * @param badgeSetupName
   * @return String
   */
  public String isBadgeNameExists( String badgeSetupName, String badgeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamfication.getBadgenameCount" );
    query.setParameter( "badgeSetupName", badgeSetupName.toLowerCase() );
    query.setParameter( "badgeId", StringUtils.isEmpty( badgeId ) ? 0 : badgeId );
    Integer badgeCount = (Integer)query.uniqueResult();
    return badgeCount.intValue() > 0 ? "Y" : "N";
  }

  /**
   * Overridden from
   * @see com.biperf.core.dao.gamification.GamificationDao#getParticipantBadgeByBadgeLib(java.lang.String,java.lang.Long)
   * @param badgeLibCmKey
   * @param userId
   * @return ParticipantBadge
   */
  public ParticipantBadge getParticipantBadgeByBadgeLib( BadgeRule rule, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgeByBadgeLibrary" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeLibCM", rule.getBadgeLibraryCMKey() );
    query.setParameter( "currentRuleId", rule.getId() );
    ParticipantBadge partcipantBadge = null;
    if ( query.list() != null && query.list().size() > 0 )
    {
      partcipantBadge = (ParticipantBadge)query.list().get( 0 );
    }
    return partcipantBadge;
  }

  /**
   * Overridden from
   * @see com.biperf.core.dao.gamification.GamificationDao#getParticipantBadgeByBadgeRule(BadgeRule,java.lang.Long)
   * @param badgeRule
   * @param userId
   * @return ParticipantBadge
   */
  public ParticipantBadge getParticipantBadgeByBadgeRule( BadgeRule badgeRule, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgeByBadgeRule" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeRuleId", badgeRule.getId() );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    ParticipantBadge partcipantBadge = null;
    if ( query.list() != null && query.list().size() > 0 )
    {
      partcipantBadge = (ParticipantBadge)query.list().get( 0 );
    }
    return partcipantBadge;

  }

  public Integer isUserHasActiveBadges( Long userId, String promotionIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.isUserHasActiveBadges" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionIds", promotionIds );
    return (Integer)query.uniqueResult();
  }

  public List<ParticipantBadge> getBadgesForRecognitionEmailProgress( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgesForRecognitionConfirmationScreen" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    query.setParameter( "badgeCountType1", "total" );
    query.setParameter( "badgeCountType2", "received" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  public List<ParticipantBadge> getBadgesForRecognitionEmailBehavior( Long promotionId, Long userId, String behaviorName )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgesForRecognitionEmailBehavior" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.BEHAVIOR );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "behaviorName", behaviorName );
    return query.list();
  }

  public List<ParticipantBadge> getParticipansEarnedHighestLevel( Long badgeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipansEarnedHighestLevel" );
    query.setParameter( "badgeId", badgeId );
    query.setParameter( "badgeType", BadgeType.PROGRESS );
    List<ParticipantBadge> paxList = new ArrayList<ParticipantBadge>();
    paxList = query.list();
    return paxList;
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedForPromotion( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeByParticipantEarnedForPromotion" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.EARNED_OR_NOT_EARNED );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedForDIYQuiz( Long quizId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeByParticipantEarnedForDIYQuiz" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.EARNED_OR_NOT_EARNED );
    query.setParameter( "quizId", quizId );
    return query.list();
  }

  public List<ParticipantBadge> getBadgeByParticipantEarnedForThrowdown( Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeByParticipantEarnedForThrowdown" );
    query.setParameter( "userId", userId );
    query.setParameter( "badgeType", BadgeType.EARNED_OR_NOT_EARNED );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  /**
   * Calls the stored procedure to verify participant file
   * 
   * @param importFileId
   * @param loadType
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, String loadType, Long userId, Long badgeId, Date earnedDate )
  {
    CallPrcBadgeVerifyImport badgeVerifyProc = new CallPrcBadgeVerifyImport( dataSource );
    return badgeVerifyProc.executeProcedure( importFileId, loadType, userId, badgeId, earnedDate );
  }

  /**
   * Gets the Badge Rule by the Behavior.
   * 
   * @param behaviorName
   * @param promotionId
   * @return BadgeRule
   */
  public BadgeRule getBadgeRuleByBehaviorName( String behaviorName, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeRuleByBehaviorName" );
    query.setParameter( "behaviorName", behaviorName );
    query.setParameter( "promotionId", promotionId );
    query.setMaxResults( 1 );
    BadgeRule b = null;
    if ( query.list() != null && query.list().size() > 0 )
    {
      b = (BadgeRule)query.list().get( 0 );
    }
    return b;
  }

  /**
   * Gets the list of Badge by diy quiz
   * 
   * @param promotionId
   * @return List<Badge>
   */
  public List<Badge> getBadgeByQuiz( Long badgeRulePromoId )
  {
    BadgeQueryConstraint bqc = new BadgeQueryConstraint();
    bqc.setBadgeRulePromoId( badgeRulePromoId );
    bqc.setBadgeStatusType( "A" );
    return HibernateUtil.getObjectList( bqc );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }
  // Gamification DAO Impl Methods Ends Here

  public void deleteBadgePromotion( BadgePromotion badgePromotion )
  {
    getSession().delete( badgePromotion );
  }

  public void deleteBadgeRule( BadgeRule badgeRule )
  {
    getSession().delete( badgeRule );
  }

  public List<Badge> getBadgeByBadgeIdAndPromotionId( Long promotionId )
  {
    BadgeQueryConstraint queryConstraint = new BadgeQueryConstraint();
    queryConstraint.setBadgePromoId( promotionId );
    queryConstraint.setBadgeStatusType( Badge.BADGE_ACTIVE );
    return HibernateUtil.getObjectList( queryConstraint );

  }

  public List<ParticipantBadge> getParticipantBadgeByPromotionId( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgeByPromotionId" );
    query.setParameter( "promotionId", promotionId );
    return query.list();
  }

  public List<Badge> getAllEligibleBadges()
  {
    BadgeQueryConstraint queryConstraint = new BadgeQueryConstraint();
    queryConstraint.setBadgeStatusType( Badge.BADGE_ACTIVE );
    queryConstraint.setBadgeSweepEnabled( Boolean.TRUE );
    return HibernateUtil.getObjectList( queryConstraint );
  }

  public int canCreateJournal( Long userId, Long ruleId, Long promotionId, String badgeType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.canCreateJournal" );
    query.setParameter( "userId", userId );
    query.setParameter( "ruleId", ruleId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "badgeType", badgeType );
    return query.list().size();
  }

  public Integer getBehaviorEarnedCount( Long promotionId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBehaviorEarnedCount" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "promotionId", promotionId );
    return (Integer)query.uniqueResult();
  }

  public Integer getBadgesEarnedCount( Long badgeRuleId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeEarnCountBybadgeId" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "badgeRuleId", badgeRuleId );
    return (Integer)query.uniqueResult();
  }

  public List<Long> getBadgeNotificationList()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getBadgeNotificationList" );

    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Set<BadgeRule> getSortedBadgeRulesById( Long badgeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getSortedBadgeRulesById" );
    query.setParameter( "badgeId", badgeId );
    return new LinkedHashSet<BadgeRule>( query.list() );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ParticipantBadge> getBehaviorParticipantBadges( Long userId, Long promotionId, List<String> behaviorNames )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.gamification.getParticipantBadgeByBehaviorNames" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionId", promotionId );
    query.setParameterList( "behaviorNames", behaviorNames );
    return query.list();
  }

}
