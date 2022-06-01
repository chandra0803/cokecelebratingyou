/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionBehavior.java,v $
 *
 */

package com.biperf.core.domain.promotion;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.utils.UserManager;

/**
 * PromotionBehavior <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PromotionBehavior implements AuditCreateInterface, Serializable
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final long serialVersionUID = 8618059577580176768L;
  private AuditCreateInfo auditCreateInfo;

  /**
   * The promotion to which the promotionBehaviorType is bound.
   */
  private Promotion promotion;

  /**
   * The promotionBehaviorType bound to the promotion.
   */
  private PromotionBehaviorType promotionBehaviorType;

  private String behaviorOrder;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>PromotionBehavior</code> object.
   */
  public PromotionBehavior()
  {
    auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( UserManager.getUserId() );
    auditCreateInfo.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Constructs a <code>PromotionBehavior</code> object.
   *
   */
  public PromotionBehavior( Promotion promotion, PromotionBehaviorType behavior )
  {
    this();

    this.promotion = promotion;
    this.promotionBehaviorType = behavior;
  }

  public PromotionBehavior( Promotion promotion, PromotionBehaviorType behavior, String behaviorOrder )
  {
    this();

    this.promotion = promotion;
    this.promotionBehaviorType = behavior;
    this.behaviorOrder = behaviorOrder;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public AuditCreateInfo getAuditCreateInfo()
  {
    return auditCreateInfo;
  }

  public PromotionBehaviorType getPromotionBehaviorType()
  {
    return promotionBehaviorType;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  public void setPromotionBehaviorType( PromotionBehaviorType promotionBehaviorType )
  {
    this.promotionBehaviorType = promotionBehaviorType;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hash Code, and To String Methods
  // ---------------------------------------------------------------------------

  public String getBehaviorOrder()
  {
    return behaviorOrder;
  }

  public void setBehaviorOrder( String behaviorOrder )
  {
    this.behaviorOrder = behaviorOrder;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }

    if ( ! ( o instanceof PromotionBehavior ) )
    {
      return false;
    }

    final PromotionBehavior behavior = (PromotionBehavior)o;

    if ( behavior.getPromotionBehaviorType() == null || !promotionBehaviorType.equals( behavior.getPromotionBehaviorType() ) )
    {
      return false;
    }

    /*
     * if ( ( behavior != null ) ? !behaviorOrder.equals( behavior.getBehaviorOrder() ) :
     * behavior.getBehaviorOrder() != null ) { return false; }
     */

    if ( promotion != null ? !promotion.equals( behavior.getPromotion() ) : behavior.getPromotion() != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = promotionBehaviorType != null ? promotionBehaviorType.hashCode() : 0;
    result = 29 * result + ( promotion != null ? promotion.hashCode() : 0 );

    return result;
  }

  public String toString()
  {
    return new StringBuffer().append( "PromotionBehavior" ).append( "{promotionBehaviorType = " ).append( promotionBehaviorType ).append( ", promotion = " ).append( promotion ).append( '}' )
        .toString();
  }
}
