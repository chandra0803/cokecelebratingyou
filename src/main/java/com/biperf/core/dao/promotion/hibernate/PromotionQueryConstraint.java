/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/hibernate/PromotionQueryConstraint.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.HibernateSessionManager;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * if null, include master and child promos; if true, only include master promos; if false, only
   * include child promos
   */
  private Boolean masterOrChildConstraint;

  /**
   * if null, include promotions with and without sweepstakes, if true, include only promotions with
   * sweepstakes, if false, includes promotions without sweepstakes.
   */
  private Boolean hasSweepstakes;

  /**
   * Only include promos matching any of the types
   */
  private PromotionStatusType[] promotionStatusTypesIncluded;

  /**
   * Only include promos with the following IDs
   */
  private Long[] promotionIds;

  /**
   * Only include promos not matching any of the types
   */
  private PromotionStatusType[] promotionStatusTypesExcluded;

  private PromotionType[] promotionTypesIncluded;

  private PromotionType[] promotionTypesExcluded;

  private PromotionAwardsType[] promotionAwardsTypeIncluded;

  private PromotionAwardsType[] promotionAwardsTypeExcluded;

  /**
   * Only include promos matching the given promotionClaimFormIncluded
   */
  private ClaimForm promotionClaimFormIncluded;

  /**
   * Only include promos matching the given approvalType.
   */
  private ApprovalType approvalType;

  /**
   *  Add Order to Criteria - order result sort by promotion name case insensitive ascending
   */
  private boolean orderByPromotionNameCaseInsensitive;
  
  /*START Client customization (home page tuning)*/
  private Boolean onlineEntryOrMerchOrEngagementOrSSI;
  
  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Boolean getOnlineEntryOrMerchOrEngagementOrSSI()
  {
    return onlineEntryOrMerchOrEngagementOrSSI;
  }

  public void setOnlineEntryOrMerchOrEngagementOrSSI( Boolean onlineEntryOrMerchOrEngagementOrSSI )
  {
    this.onlineEntryOrMerchOrEngagementOrSSI = onlineEntryOrMerchOrEngagementOrSSI;
  }

  /*END Client customization (home page tuning)*/

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "promotion" );
    if ( promotionIds != null && promotionIds.length > 0 )
    {
      criteria.add( Restrictions.in( "promotion.id", promotionIds ) );
    }

    if ( masterOrChildConstraint != null )
    {
      if ( masterOrChildConstraint.booleanValue() )
      {
        // Only master promos
        criteria.add( Restrictions.isNull( "promotion.parentPromotion" ) );
      }
      else
      {
        // only child promos
        criteria.add( Restrictions.isNotNull( "promotion.parentPromotion" ) );
      }
    }

    if ( hasSweepstakes != null )
    {
      criteria.add( Restrictions.eq( "promotion.sweepstakesActive", hasSweepstakes ) );
    }

    if ( promotionStatusTypesIncluded != null && promotionStatusTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "promotion.promotionStatus", promotionStatusTypesIncluded ) );
    }

    if ( promotionStatusTypesExcluded != null && promotionStatusTypesExcluded.length > 0 )
    {
      criteria.add( Restrictions.not( Restrictions.in( "promotion.promotionStatus", promotionStatusTypesExcluded ) ) );
    }

    if ( promotionTypesIncluded != null && promotionTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "promotion.promotionType", promotionTypesIncluded ) );
    }

    if ( promotionTypesExcluded != null && promotionTypesExcluded.length > 0 )
    {
      criteria.add( Restrictions.not( Restrictions.in( "promotion.promotionType", promotionTypesExcluded ) ) );
    }

    if ( promotionAwardsTypeIncluded != null && promotionAwardsTypeIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "promotion.awardType", promotionAwardsTypeIncluded ) );
    }

    if ( promotionAwardsTypeExcluded != null && promotionAwardsTypeExcluded.length > 0 )
    {
      criteria.add( Restrictions.not( Restrictions.in( "promotion.awardType", promotionAwardsTypeExcluded ) ) );
    }

    if ( approvalType != null )
    {
      criteria.add( Restrictions.eq( "promotion.approvalType", approvalType ) );
    }

    if ( promotionClaimFormIncluded != null )
    {
      criteria.add( Restrictions.eq( "promotion.claimForm", promotionClaimFormIncluded ) );
    }

    // Never include "deleted"
    criteria.add( Restrictions.ne( "promotion.deleted", Boolean.TRUE ) );

    // Hibernate's Order class will not understand something like
    // Order.asc("upper(property_name)") and there is no flag to pass to indicate case insensitive
    // sorting.
    // So, added a new property to promotion class mapping for the uppercase property

    criteria.addOrder( Order.asc( "promotion.upperCaseName" ) );

    return criteria;
  }

  /**
   * Build a PromotionQueryConstraint that represents expired promotions.
   * 
   * @return PromotionQueryConstraint
   */
  public static PromotionQueryConstraint buildExpiredPromotionsConstraint()
  {
    PromotionQueryConstraint promotionQueryConstraint = new PromotionQueryConstraint();
    promotionQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

    return promotionQueryConstraint;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Return the result object type - Should be overridden by subclasses.
   * 
   * @return Class
   */
  public Class getResultClass()
  {
    return Promotion.class;
  }

  /**
   * @return value of hasSweepstakes property
   */
  public Boolean getHasSweepstakes()
  {
    return hasSweepstakes;
  }

  /**
   * @param hasSweepstakes value for hasSweepstakes property
   */
  public void setHasSweepstakes( Boolean hasSweepstakes )
  {
    this.hasSweepstakes = hasSweepstakes;
  }

  public Long[] getPromotionIds()
  {
    return promotionIds;
  }

  public void setPromotionIds( Long[] promotionIds )
  {
    this.promotionIds = promotionIds;
  }

  /**
   * @return value of masterOrChildConstraint property
   */
  public Boolean getMasterOrChildConstraint()
  {
    return masterOrChildConstraint;
  }

  /**
   * @param masterOrChildConstraint value for masterOrChildConstraint property
   */
  public void setMasterOrChildConstraint( Boolean masterOrChildConstraint )
  {
    this.masterOrChildConstraint = masterOrChildConstraint;
  }

  /**
   * @return value of promotionStatusTypesExcluded property
   */
  public PromotionStatusType[] getPromotionStatusTypesExcluded()
  {
    return promotionStatusTypesExcluded;
  }

  /**
   * @param promotionStatusTypesExcluded value for promotionStatusTypesExcluded property
   */
  public void setPromotionStatusTypesExcluded( PromotionStatusType[] promotionStatusTypesExcluded )
  {
    this.promotionStatusTypesExcluded = promotionStatusTypesExcluded;
  }

  /**
   * @return value of promotionStatusTypesIncluded property
   */
  public PromotionStatusType[] getPromotionStatusTypesIncluded()
  {
    return promotionStatusTypesIncluded;
  }

  /**
   * @param promotionStatusTypesIncluded value for promotionStatusTypesIncluded property
   */
  public void setPromotionStatusTypesIncluded( PromotionStatusType[] promotionStatusTypesIncluded )
  {
    this.promotionStatusTypesIncluded = promotionStatusTypesIncluded;
  }

  public PromotionType[] getPromotionTypesExcluded()
  {
    return promotionTypesExcluded;
  }

  public void setPromotionTypesExcluded( PromotionType[] promotionTypesExcluded )
  {
    this.promotionTypesExcluded = promotionTypesExcluded;
  }

  public PromotionType[] getPromotionTypesIncluded()
  {
    return promotionTypesIncluded;
  }

  public void setPromotionTypesIncluded( PromotionType[] promotionTypesIncluded )
  {
    this.promotionTypesIncluded = promotionTypesIncluded;
  }

  public PromotionAwardsType[] getPromotionAwardsTypeExcluded()
  {
    return promotionAwardsTypeExcluded;
  }

  public void setPromotionAwardsTypeExcluded( PromotionAwardsType[] promotionAwardsTypeExcluded )
  {
    this.promotionAwardsTypeExcluded = promotionAwardsTypeExcluded;
  }

  public PromotionAwardsType[] getPromotionAwardsTypeIncluded()
  {
    return promotionAwardsTypeIncluded;
  }

  public void setPromotionAwardsTypeIncluded( PromotionAwardsType[] promotionAwardsTypeIncluded )
  {
    this.promotionAwardsTypeIncluded = promotionAwardsTypeIncluded;
  }

  /**
   * @return value of approvalType property
   */
  public ApprovalType getApprovalType()
  {
    return approvalType;
  }

  /**
   * @param approvalType value for approvalType property
   */
  public void setApprovalType( ApprovalType approvalType )
  {
    this.approvalType = approvalType;
  }

  public ClaimForm getPromotionClaimFormIncluded()
  {
    return promotionClaimFormIncluded;
  }

  public void setPromotionClaimFormIncluded( ClaimForm promotionClaimFormIncluded )
  {
    this.promotionClaimFormIncluded = promotionClaimFormIncluded;
  }

  public boolean isOrderByPromotionNameCaseInsensitive()
  {
    return orderByPromotionNameCaseInsensitive;
  }

  public void setOrderByPromotionNameCaseInsensitive( boolean orderByPromotionNameCaseInsensitive )
  {
    this.orderByPromotionNameCaseInsensitive = orderByPromotionNameCaseInsensitive;
  }

}
