/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.participant.Audience;

/**
 * PromotionSecondaryAudience.
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
public class PromotionSecondaryAudience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "SECONDARY";

  public PromotionSecondaryAudience( PromotionSecondaryAudience promotionTeamAudiencesToCopy )
  {
    this.setAudience( promotionTeamAudiencesToCopy.getAudience() );
    this.setPromotion( promotionTeamAudiencesToCopy.getPromotion() );
  }

  public PromotionSecondaryAudience()
  {
    super();
  }

  /**
   * @param audience
   * @param promotion
   */
  public PromotionSecondaryAudience( Audience audience, Promotion promotion )
  {
    setAudience( audience );
    setPromotion( promotion );
  }

  /**
   * Clones this object, resets the id, version, audit info and promotion. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    PromotionSecondaryAudience clonedPromotionTeamAudience = (PromotionSecondaryAudience)super.clone();

    clonedPromotionTeamAudience.setPromotion( null );
    clonedPromotionTeamAudience.resetBaseDomain();

    return clonedPromotionTeamAudience;
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
    sb.append( "promotionAudienceType = " + PromotionSecondaryAudience.PROMOTION_AUDIENCE_TYPE );

    return sb.toString();

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof PromotionSecondaryAudience )
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
