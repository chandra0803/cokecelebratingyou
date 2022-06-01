/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionWebRulesAudience.java,v $
 */

package com.biperf.core.domain.promotion;

/**
 * PromotionWebRulesAudience.
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
 * <td>Aug 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWebRulesAudience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "WEB_RULES";

  public PromotionWebRulesAudience( PromotionWebRulesAudience promotionWebRulesAudienceToCopy )
  {
    super();
    this.setAudience( promotionWebRulesAudienceToCopy.getAudience() );
    this.setPromotion( promotionWebRulesAudienceToCopy.getPromotion() );
  }

  public PromotionWebRulesAudience()
  {
    // empty default constructor
  }

  /**
   * Clones this and returns it without the promotion. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    PromotionWebRulesAudience clonedPromotionWebRulesAudience = (PromotionWebRulesAudience)super.clone();

    clonedPromotionWebRulesAudience.setPromotion( null );
    clonedPromotionWebRulesAudience.resetBaseDomain();

    return clonedPromotionWebRulesAudience;

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
    sb.append( "promotionAudienceType = " + PromotionWebRulesAudience.PROMOTION_AUDIENCE_TYPE );

    return sb.toString();

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof PromotionWebRulesAudience )
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
