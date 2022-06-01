
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.promotion.PromotionAudience;

/**
 * 
 * SSIPromotionContestApprovalLevel2Audience.
 * 
 * @author kandhi
 * @since Oct 23, 2014
 * @version 1.0
 */
public class SSIPromotionContestApprovalLevel2Audience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "SSI_CONTEST_APPROVAL_LEVEL2";

  public SSIPromotionContestApprovalLevel2Audience( SSIPromotionContestApprovalLevel2Audience SSIPromotionContestApprovalLevel2AudienceToCopy )
  {
    super();
    this.setAudience( SSIPromotionContestApprovalLevel2AudienceToCopy.getAudience() );
    this.setPromotion( SSIPromotionContestApprovalLevel2AudienceToCopy.getPromotion() );
  }

  public SSIPromotionContestApprovalLevel2Audience()
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
    SSIPromotionContestApprovalLevel2Audience clonedSSIPromotionContestApprovalLevel2Audience = (SSIPromotionContestApprovalLevel2Audience)super.clone();
    clonedSSIPromotionContestApprovalLevel2Audience.setPromotion( null );
    clonedSSIPromotionContestApprovalLevel2Audience.resetBaseDomain();
    return clonedSSIPromotionContestApprovalLevel2Audience;

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
    sb.append( "SSIPromotionContestApprovalLevel2Audience = " + SSIPromotionContestApprovalLevel2Audience.PROMOTION_AUDIENCE_TYPE );
    return sb.toString();
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof SSIPromotionContestApprovalLevel2Audience )
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
