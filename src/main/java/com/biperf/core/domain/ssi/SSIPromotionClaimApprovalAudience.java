
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.promotion.PromotionAudience;

/**
 * 
 * SSIPromotionClaimApprovalAudience.
 * 
 * @author kandhi
 * @since Oct 23, 2014
 * @version 1.0
 */
public class SSIPromotionClaimApprovalAudience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "SSI_CLAIM_APPROVAL";

  public SSIPromotionClaimApprovalAudience( SSIPromotionClaimApprovalAudience SSIPromotionClaimApprovalAudienceToCopy )
  {
    super();
    this.setAudience( SSIPromotionClaimApprovalAudienceToCopy.getAudience() );
    this.setPromotion( SSIPromotionClaimApprovalAudienceToCopy.getPromotion() );
  }

  public SSIPromotionClaimApprovalAudience()
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
    SSIPromotionClaimApprovalAudience clonedSSIPromotionClaimApprovalAudience = (SSIPromotionClaimApprovalAudience)super.clone();
    clonedSSIPromotionClaimApprovalAudience.setPromotion( null );
    clonedSSIPromotionClaimApprovalAudience.resetBaseDomain();
    return clonedSSIPromotionClaimApprovalAudience;

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
    sb.append( "SSIPromotionClaimApprovalAudience = " + SSIPromotionClaimApprovalAudience.PROMOTION_AUDIENCE_TYPE );
    return sb.toString();
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof SSIPromotionClaimApprovalAudience )
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
