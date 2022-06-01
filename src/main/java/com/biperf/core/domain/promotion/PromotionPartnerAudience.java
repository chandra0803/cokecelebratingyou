/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionPartnerAudience.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.participant.Audience;

/**
 * PromotionPartnerAudience.
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
 * <td>reddy</td>
 * <td>Feb 21, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionPartnerAudience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "PARTNER";

  public PromotionPartnerAudience()
  {
    super();
  }

  /**
   * @param audience
   * @param promotion
   */
  public PromotionPartnerAudience( Audience audience, Promotion promotion )
  {
    super();
    setAudience( audience );
    setPromotion( promotion );
  }

  public PromotionPartnerAudience( PromotionPartnerAudience promotionPartnerAudience )
  {
    super();
    setAudience( promotionPartnerAudience.getAudience() );
    setPromotion( promotionPartnerAudience.getPromotion() );
  }

  /**
   * Clones this and removes the promotion. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {
    PromotionPartnerAudience clonedPromotionPartnerAudience = (PromotionPartnerAudience)super.clone();

    clonedPromotionPartnerAudience.setPromotion( null );
    clonedPromotionPartnerAudience.resetBaseDomain();

    return clonedPromotionPartnerAudience;
  }

  /**
   * Builds a String representations of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( super.toString() );
    sb.append( " [ " );
    sb.append( "promotionAudienceType = " + PromotionPartnerAudience.PROMOTION_AUDIENCE_TYPE );
    sb.append( " ] " );

    return sb.toString();

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof PromotionPartnerAudience )
    {
      return super.equals( object );
    }

    return false;
  }

  public int hashCode()
  {
    return super.hashCode();
  }

}
