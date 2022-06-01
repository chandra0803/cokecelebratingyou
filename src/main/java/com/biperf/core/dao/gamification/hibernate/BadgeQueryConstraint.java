
package com.biperf.core.dao.gamification.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.utils.HibernateSessionManager;

public class BadgeQueryConstraint extends BaseQueryConstraint
{

  private Long[] promotionIds;

  private BadgeType badgeType;

  private String badgeStatusType;

  private Long promotionId;

  private Long badgePromoId;

  private Boolean badgeSweepEnabled;

  private Long badgeRulePromoId;

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "badge" );

    if ( promotionIds != null && promotionIds.length > 0 )
    {
      criteria.add( Restrictions.in( "badge.id", promotionIds ) );
    }

    if ( badgeStatusType != null )
    {
      criteria.add( Restrictions.eq( "badge.status", badgeStatusType ) );
    }

    if ( promotionId != null )
    {
      criteria.add( Restrictions.eq( "badge.id", promotionId ) );
    }

    if ( badgePromoId != null )
    {
      criteria.createAlias( "badge.badgePromotions", "badgePromotion" ).add( Restrictions.eq( "badgePromotion.eligiblePromotion.id", badgePromoId ) );
    }

    if ( badgeRulePromoId != null )
    {
      criteria.createAlias( "badge.badgeRules", "badgePromotion" ).add( Restrictions.eq( "badgePromotion.id", badgeRulePromoId ) );
    }

    if ( badgeSweepEnabled != null )
    {
      Criterion eligibleBadgesCriterion = null;
      eligibleBadgesCriterion = Restrictions.sqlRestriction( "{alias}.promotion_id in (select distinct br.promotion_id from promotion p, badge_rule br" + " where p.promotion_status = 'live'"
          + " and p.promotion_type = 'badge'" + " and p.promotion_id = br.promotion_id" + " and br.eligible_for_sweepstake = 1)" );

      criteria.add( eligibleBadgesCriterion );
    }

    if ( badgeType != null )
    {
      criteria.add( Restrictions.eq( "badge.badgeType", badgeType ) );
    }

    criteria.add( Restrictions.eq( "badge.promotionStatus", PromotionStatusType.lookup( PromotionStatusType.LIVE ) ) );

    return criteria;

  }

  public Class getResultClass()
  {
    return Badge.class;
  }

  public Long[] getPromotionIds()
  {
    return promotionIds;
  }

  public void setPromotionIds( Long[] promotionIds )
  {
    this.promotionIds = promotionIds;
  }

  public String getBadgeStatusType()
  {
    return badgeStatusType;
  }

  public void setBadgeStatusType( String badgeStatusType )
  {
    this.badgeStatusType = badgeStatusType;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public BadgeType getBadgeType()
  {
    return badgeType;
  }

  public void setBadgeType( BadgeType badgeType )
  {
    this.badgeType = badgeType;
  }

  public Boolean getBadgeSweepEnabled()
  {
    return badgeSweepEnabled;
  }

  public Long getBadgePromoId()
  {
    return badgePromoId;
  }

  public void setBadgePromoId( Long badgePromoId )
  {
    this.badgePromoId = badgePromoId;
  }

  public void setBadgeSweepEnabled( Boolean badgeSweepEnabled )
  {
    this.badgeSweepEnabled = badgeSweepEnabled;
  }

  public Long getBadgeRulePromoId()
  {
    return badgeRulePromoId;
  }

  public void setBadgeRulePromoId( Long badgeRulePromoId )
  {
    this.badgeRulePromoId = badgeRulePromoId;
  }

}
