/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.participant.Audience;

/**
 * PromotionPrimaryAudience.
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
public class PromotionPrimaryAudience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "PRIMARY";

  public PromotionPrimaryAudience()
  {
    super();
  }

  /**
   * @param audience
   * @param promotion
   */
  public PromotionPrimaryAudience( Audience audience, Promotion promotion )
  {
    super();
    setAudience( audience );
    setPromotion( promotion );
  }

  public PromotionPrimaryAudience( PromotionPrimaryAudience promotionSubmitterAudienceToCopy )
  {
    super();
    setAudience( promotionSubmitterAudienceToCopy.getAudience() );
    setPromotion( promotionSubmitterAudienceToCopy.getPromotion() );
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
    PromotionPrimaryAudience clonedPromotionSubmitterAudience = (PromotionPrimaryAudience)super.clone();

    clonedPromotionSubmitterAudience.setPromotion( null );
    clonedPromotionSubmitterAudience.resetBaseDomain();

    return clonedPromotionSubmitterAudience;
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
    sb.append( "promotionAudienceType = " + PromotionPrimaryAudience.PROMOTION_AUDIENCE_TYPE );
    sb.append( " ] " );

    return sb.toString();

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof PromotionPrimaryAudience )
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
