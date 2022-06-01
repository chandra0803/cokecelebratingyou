
package com.biperf.core.domain.promotion;

public class PromotionPublicRecognitionAudience extends PromotionAudience implements Cloneable
{
  private static final long serialVersionUID = 1L;

  public static final String PROMOTION_AUDIENCE_TYPE = "PUBLIC_RECOGNITION";

  public PromotionPublicRecognitionAudience( PromotionPublicRecognitionAudience promotionPublicRecognitionAudienceToCopy )
  {
    super();
    this.setAudience( promotionPublicRecognitionAudienceToCopy.getAudience() );
    this.setPromotion( promotionPublicRecognitionAudienceToCopy.getPromotion() );
  }

  public PromotionPublicRecognitionAudience()
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

    PromotionPublicRecognitionAudience clonedPromotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)super.clone();

    clonedPromotionPublicRecognitionAudience.setPromotion( null );
    clonedPromotionPublicRecognitionAudience.resetBaseDomain();

    return clonedPromotionPublicRecognitionAudience;

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
    sb.append( "promotionAudienceType = " + PromotionPublicRecognitionAudience.PROMOTION_AUDIENCE_TYPE );

    return sb.toString();

  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object instanceof PromotionPublicRecognitionAudience )
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
