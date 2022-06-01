
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.promotion.PromotionAudience;

/**
 * 
 * SSIPromotionContestApprovalLevel1Audience
 * 
 * @author kandhi
 * @since Oct 23, 2014
 * @version 1.0
 */
public class SSIPromotionContestApprovalLevel1Audience extends PromotionAudience implements Cloneable
{

  public static final String PROMOTION_AUDIENCE_TYPE = "SSI_CONTEST_APPROVAL_LEVEL1";

  public SSIPromotionContestApprovalLevel1Audience( SSIPromotionContestApprovalLevel1Audience SSIPromotionContestApprovalLevel1AudienceToCopy )
  {
    super();
    this.setAudience( SSIPromotionContestApprovalLevel1AudienceToCopy.getAudience() );
    this.setPromotion( SSIPromotionContestApprovalLevel1AudienceToCopy.getPromotion() );
  }

  public SSIPromotionContestApprovalLevel1Audience()
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
    SSIPromotionContestApprovalLevel1Audience clonedSSIPromotionContestApprovalLevel1Audience = (SSIPromotionContestApprovalLevel1Audience)super.clone();
    clonedSSIPromotionContestApprovalLevel1Audience.setPromotion( null );
    clonedSSIPromotionContestApprovalLevel1Audience.resetBaseDomain();
    return clonedSSIPromotionContestApprovalLevel1Audience;

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
    sb.append( "SSIPromotionContestApprovalLevel1Audience = " + SSIPromotionContestApprovalLevel1Audience.PROMOTION_AUDIENCE_TYPE );
    return sb.toString();
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof SSIPromotionContestApprovalLevel1Audience )
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
