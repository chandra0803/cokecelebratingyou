
package com.biperf.core.dao.merchandise.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.utils.HibernateSessionManager;

public class MerchOrderActivityQueryConstraint extends BaseQueryConstraint
{

  /**
   * Include only merch orders for the specified claim.
   */
  private Long claimId;

  /**
   * Include only merch orders for the specified promotion.
   */
  private Long promotionId;

  /**
   * Include only merch orders for the specified participant.
   */
  private Long participantId;

  private Boolean redeemed;

  private String giftCodeDecrypted;
  private String giftCodeKeyDecrypted;

  private String referenceNumber;
  private Long gqCpPromotionId;
  private Boolean isGiftCodeNull;

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "merchOrder" );

    if ( claimId != null )
    {
      criteria.add( Restrictions.eq( "merchOrder.claim.id", claimId ) );
    }

    if ( promotionId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "merchOrder.claim", "claim" );
      criteria.add( Restrictions.eq( "claim.promotion.id", promotionId ) );
    }

    /*
     * if( gqCpPromotionId != null) { createAliasIfNotAlreadyCreated( criteria,
     * "merchOrder.paxGoal", "paxGoal" ); criteria.add( Restrictions.eq(
     * "paxGoal.goalQuestPromotion.id", gqCpPromotionId ) ); }
     */

    if ( participantId != null && participantId.longValue() > 0 )
    {
      criteria.add( Restrictions.eq( "merchOrder.participant.id", participantId ) );
    }

    if ( redeemed != null )
    {
      criteria.add( Restrictions.eq( "merchOrder.redeemed", redeemed ) );
    }

    if ( referenceNumber != null )
    {
      criteria.add( Restrictions.eq( "merchOrder.referenceNumber", referenceNumber ) );
    }

    if ( giftCodeDecrypted != null )
    {
      criteria.add( Restrictions.sqlRestriction( " GIFT_CODE = FNC_JAVA_ENCRYPT(?)", giftCodeDecrypted, StandardBasicTypes.STRING ) );
    }

    if ( giftCodeKeyDecrypted != null )
    {
      criteria.add( Restrictions.sqlRestriction( " GIFT_CODE_KEY = FNC_JAVA_ENCRYPT(?)", giftCodeKeyDecrypted, StandardBasicTypes.STRING ) );
    }

    if ( isGiftCodeNull != null )
    {
      criteria.add( Restrictions.isNull( "merchOrder.giftCode" ) );
      criteria.add( Restrictions.isNull( "merchOrder.giftCodeKey" ) );
      criteria.add( Restrictions.isNull( "merchOrder.referenceNumber" ) );
    }

    return criteria;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Return the class of the objects selected by this query constraint.
   *
   * @return the class of the objects selected by this query constraint.
   */
  public Class getResultClass()
  {
    return MerchOrder.class;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setRedeemed( Boolean redeemed )
  {
    this.redeemed = redeemed;
  }

  public String getGiftCodeDecrypted()
  {
    return giftCodeDecrypted;
  }

  public void setGiftCodeDecrypted( String giftCodeDecrypted )
  {
    this.giftCodeDecrypted = giftCodeDecrypted;
  }

  public String getGiftCodeKeyDecrypted()
  {
    return giftCodeKeyDecrypted;
  }

  public void setGiftCodeKeyDecrypted( String giftCodeKeyDecrypted )
  {
    this.giftCodeKeyDecrypted = giftCodeKeyDecrypted;
  }

  public String getReferenceNumber()
  {
    return referenceNumber;
  }

  public void setReferenceNumber( String referenceNumber )
  {
    this.referenceNumber = referenceNumber;
  }

  public Long getGqCpPromotionId()
  {
    return gqCpPromotionId;
  }

  public void setGqCpPromotionId( Long gqCpPromotionId )
  {
    this.gqCpPromotionId = gqCpPromotionId;
  }

  public Boolean getIsGiftCodeNull()
  {
    return isGiftCodeNull;
  }

  public void setIsGiftCodeNull( Boolean isGiftCodeNull )
  {
    this.isGiftCodeNull = isGiftCodeNull;
  }
}
