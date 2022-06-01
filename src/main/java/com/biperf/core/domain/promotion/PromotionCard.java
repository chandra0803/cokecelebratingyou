/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionCard.java,v $
 */

package com.biperf.core.domain.promotion;

import java.io.Serializable;

import com.biperf.core.domain.BaseDomain;

/**
 * PromotionCard.
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
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class PromotionCard extends BaseDomain implements Serializable
{
  /** Promotion */
  private Promotion promotion;

  private String orderNumber;

  /**
   * Public constructor will manage the AuditCreateInformation.
   */
  public PromotionCard()
  {
    // empty constructor
  }

  /**
   * Get the promotion.
   * 
   * @return Promotion
   */
  public Promotion getPromotion()
  {
    return this.promotion;
  }

  /**
   * Set the promotion.
   * 
   * @param promotion
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getOrderNumber()
  {
    return orderNumber;
  }

  public void setOrderNumber( String orderNumber )
  {
    this.orderNumber = orderNumber;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "PromotionCard [" );
    buf.append( "{promotion.id=" + this.getPromotion().getId() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

}
