/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/calculator/CalculatorCriterionRating.java,v $
 */

package com.biperf.core.domain.calculator;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * CalculatorCriterionRating.
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
 * <td>sedey</td>
 * <td>May 20, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorCriterionRating extends BaseDomain
{
  private CalculatorCriterion calculatorCriterion = null;
  private String cmAssetName = "";
  private int ratingValue;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof CalculatorCriterionRating ) )
    {
      return false;
    }

    final CalculatorCriterionRating criterionRating = (CalculatorCriterionRating)object;

    if ( criterionRating.getCmAssetName() != null && !criterionRating.getCmAssetName().equals( this.getCmAssetName() ) )
    {
      return false;
    }

    if ( criterionRating.getCalculatorCriterion() != null && !criterionRating.getCalculatorCriterion().equals( this.getCalculatorCriterion() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result = 0;

    result += this.getCmAssetName() != null ? this.getCmAssetName().hashCode() : 0;
    result += this.getCalculatorCriterion() != null ? this.getCalculatorCriterion().hashCode() * 13 : 0;

    return result;
  }

  /**
   * Builds and returns a String representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[CALCULATOR_CRITERION_RATING {" );
    sb.append( "id - " + this.getId() + ", " );
    sb.append( "calculatorCriterion.id - " + this.calculatorCriterion.getId() + ", " );
    sb.append( "cmAssetCode - " + this.cmAssetName + ", " );
    sb.append( "}]" );

    return sb.toString();
  }

  /**
   * Does a deep copy of the CalculatorCriterionRating. This is a customized implementation of
   * java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @return Object
   */
  public Object deepCopy()
  {
    CalculatorCriterionRating rating = new CalculatorCriterionRating();
    rating.setCmAssetName( this.getCmAssetName() );
    rating.setRatingValue( this.getRatingValue() );

    return rating;
  }

  public CalculatorCriterion getCalculatorCriterion()
  {
    return calculatorCriterion;
  }

  public void setCalculatorCriterion( CalculatorCriterion calculatorCriterion )
  {
    this.calculatorCriterion = calculatorCriterion;
  }

  public String getCmAssetName()
  {
    return cmAssetName;
  }

  public void setCmAssetName( String cmAssetName )
  {
    this.cmAssetName = cmAssetName;
  }

  public int getRatingValue()
  {
    return ratingValue;
  }

  public void setRatingValue( int ratingValue )
  {
    this.ratingValue = ratingValue;
  }

  public String getRatingText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetName(), Calculator.CM_CRITERION_RATING_KEY );
  }
}
