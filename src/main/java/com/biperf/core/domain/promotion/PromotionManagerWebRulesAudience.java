/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/Attic/PromotionManagerWebRulesAudience.java,v $
 */

package com.biperf.core.domain.promotion;

/**
 * PromotionManagerWebRulesAudience.
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
public class PromotionManagerWebRulesAudience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "WEB_RULES_MANAGER";

  public PromotionManagerWebRulesAudience( PromotionManagerWebRulesAudience promotionManagerWebRulesAudienceToCopy )
  {
    super();
    this.setAudience( promotionManagerWebRulesAudienceToCopy.getAudience() );
    this.setPromotion( promotionManagerWebRulesAudienceToCopy.getPromotion() );
  }

  public PromotionManagerWebRulesAudience()
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

    PromotionManagerWebRulesAudience clonedPromotionManagerWebRulesAudience = (PromotionManagerWebRulesAudience)super.clone();

    clonedPromotionManagerWebRulesAudience.setPromotion( null );
    clonedPromotionManagerWebRulesAudience.resetBaseDomain();

    return clonedPromotionManagerWebRulesAudience;

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
    sb.append( "promotionManagerAudienceType = " + PromotionManagerWebRulesAudience.PROMOTION_AUDIENCE_TYPE );

    return sb.toString();

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof PromotionManagerWebRulesAudience )
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
