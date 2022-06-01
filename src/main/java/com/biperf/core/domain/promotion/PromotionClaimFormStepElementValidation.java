/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionClaimFormStepElementValidation.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.PromotionClaimFormStepElementValidationType;

/**
 * PromotionClaimFormStepElementValidation <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sathish</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionClaimFormStepElementValidation extends BaseDomain implements Cloneable
{
  private Promotion promotion;

  private ClaimFormStepElement claimFormStepElement;

  private PromotionClaimFormStepElementValidationType validationType;

  private Integer maxValue;
  private Integer minValue;
  private Integer maxLength;
  private Date startDate;
  private Date endDate;
  private String startsWith;
  private String notStartWith;
  private String endsWith;
  private String notEndWith;
  private String contains;
  private String notContain;

  /**
   * Constructor
   */
  public PromotionClaimFormStepElementValidation()
  {
    // empty default constructor
  }

  /**
   * Constructor
   * 
   * @param pcfsev
   */
  public PromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation pcfsev )
  {

    this.claimFormStepElement = pcfsev.getClaimFormStepElement();
    this.validationType = pcfsev.getValidationType();
    this.maxValue = pcfsev.getMaxValue();
    this.minValue = pcfsev.getMinValue();
    this.maxLength = pcfsev.getMaxLength();
    this.startDate = pcfsev.getStartDate();
    this.endDate = pcfsev.getEndDate();
    this.startsWith = pcfsev.getStartsWith();
    this.notStartWith = pcfsev.getNotStartWith();
    this.endsWith = pcfsev.getEndsWith();
    this.notEndWith = pcfsev.getNotEndWith();
    this.contains = pcfsev.getContains();
    this.notContain = pcfsev.getNotContain();
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

    PromotionClaimFormStepElementValidation promotionClaimFormStepElementValidation = (PromotionClaimFormStepElementValidation)super.clone();

    promotionClaimFormStepElementValidation.resetBaseDomain();
    promotionClaimFormStepElementValidation.setPromotion( null );

    return promotionClaimFormStepElementValidation;
  }

  /**
   * Contructor composes this object with the required promotion, claimFormStepElement and
   * validationType.
   * 
   * @param promotion
   * @param claimFormStepElement
   * @param validationType
   */
  public PromotionClaimFormStepElementValidation( Promotion promotion, ClaimFormStepElement claimFormStepElement, PromotionClaimFormStepElementValidationType validationType )
  {
    this.promotion = promotion;
    this.claimFormStepElement = claimFormStepElement;
    this.validationType = validationType;
  }

  /**
   * Overridden from
   * 
   * @param object
   * @return boolean
   * @see Object#equals(Object)
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( object instanceof PromotionClaimFormStepElementValidation )
    {
      PromotionClaimFormStepElementValidation promotionCFEValidationStep = (PromotionClaimFormStepElementValidation)object;

      if ( !this.getPromotion().equals( promotionCFEValidationStep.getPromotion() ) )
      {
        equals = false;
      }

      if ( !this.getClaimFormStepElement().equals( promotionCFEValidationStep.getClaimFormStepElement() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @return int
   * @see Object#hashCode()
   */
  public int hashCode()
  {
    int result;

    result = this.getPromotion() != null ? this.getPromotion().hashCode() * 13 : 0;
    result += this.getClaimFormStepElement() != null ? this.getClaimFormStepElement().hashCode() : 0;

    return result;
  }

  /**
   * Builds a string representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( "PromotionClaimFormStepElementValidation [" );
    sb.append( "{id=" ).append( super.getId() ).append( "}, " );
    sb.append( "{validationType=" ).append( this.getValidationType().getCode() ).append( "}, " );
    sb.append( "{promotion.id=" ).append( this.getPromotion().getId() ).append( "}, " );
    sb.append( "{claimFormStepElement.id=" ).append( this.getClaimFormStepElement().getId() ).append( "}, " );
    sb.append( "]" );

    return sb.toString();
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public ClaimFormStepElement getClaimFormStepElement()
  {
    return claimFormStepElement;
  }

  public void setClaimFormStepElement( ClaimFormStepElement claimFormStepElement )
  {
    this.claimFormStepElement = claimFormStepElement;
  }

  public PromotionClaimFormStepElementValidationType getValidationType()
  {
    return this.validationType;
  }

  public void setValidationType( PromotionClaimFormStepElementValidationType validationType )
  {
    this.validationType = validationType;
  }

  public String getContains()
  {
    return contains;
  }

  public void setContains( String contains )
  {
    this.contains = contains;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public String getEndsWith()
  {
    return endsWith;
  }

  public void setEndsWith( String endsWith )
  {
    this.endsWith = endsWith;
  }

  public Integer getMaxLength()
  {
    return maxLength;
  }

  public void setMaxLength( Integer maxLength )
  {
    this.maxLength = maxLength;
  }

  public Integer getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue( Integer maxValue )
  {
    this.maxValue = maxValue;
  }

  public Integer getMinValue()
  {
    return minValue;
  }

  public void setMinValue( Integer minValue )
  {
    this.minValue = minValue;
  }

  public String getNotContain()
  {
    return notContain;
  }

  public void setNotContain( String notContain )
  {
    this.notContain = notContain;
  }

  public String getNotEndWith()
  {
    return notEndWith;
  }

  public void setNotEndWith( String notEndWith )
  {
    this.notEndWith = notEndWith;
  }

  public String getNotStartWith()
  {
    return notStartWith;
  }

  public void setNotStartWith( String notStartWith )
  {
    this.notStartWith = notStartWith;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public String getStartsWith()
  {
    return startsWith;
  }

  public void setStartsWith( String startsWith )
  {
    this.startsWith = startsWith;
  }

}
