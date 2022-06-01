/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/calculator/CalculatorCriterion.java,v $
 */

package com.biperf.core.domain.calculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.StatusType;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * CalculatorCriterion.
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
 * <td>May 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorCriterion extends BaseDomain
{
  private Calculator calculator;
  private StatusType criterionStatus;
  private String cmAssetName = "";
  private int weightValue;
  private int sequenceNum;
  private List criterionRatings = new ArrayList();

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

    if ( ! ( object instanceof CalculatorCriterion ) )
    {
      return false;
    }

    final CalculatorCriterion calcCriterion = (CalculatorCriterion)object;

    if ( this.getCmAssetName() != null ? !this.getCmAssetName().equals( calcCriterion.getCmAssetName() ) : calcCriterion.getCmAssetName() != null )
    {
      return false;
    }

    if ( this.getCalculator() != null ? !this.getCalculator().equals( calcCriterion.getCalculator() ) : calcCriterion.getCalculator() != null )
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
    result += this.getCalculator() != null ? this.getCalculator().hashCode() * 13 : 0;

    return result;
  }

  /**
   * Builds a String representation of this.
   * 
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[CALC_CRITERION {" );
    sb.append( "Calculator.id - " + this.getCalculator().getId() + ", " );
    sb.append( "status - " + this.getCriterionStatus().getName() + ", " );
    sb.append( "cmAssetName - " + this.getCmAssetName() + ", " );
    sb.append( "sequenceNum - " + this.getSequenceNum() + ", " );
    sb.append( "}]" );
    return sb.toString();
  }

  /**
   * Does a deep copy of the CalculatorCriterion and its children if specified. This is a customized
   * implementation of java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @return Object
   */
  public Object deepCopy( boolean cloneWithChildren )
  {
    CalculatorCriterion criterion = new CalculatorCriterion();
    criterion.setCmAssetName( this.getCmAssetName() );
    criterion.setSequenceNum( this.getSequenceNum() );
    criterion.setCriterionStatus( this.getCriterionStatus() );
    criterion.setWeightValue( this.getWeightValue() );

    if ( cloneWithChildren )
    {
      Iterator iter = this.getCriterionRatings().iterator();
      while ( iter.hasNext() )
      {
        CalculatorCriterionRating ratingToCopy = (CalculatorCriterionRating)iter.next();
        criterion.addCriterionRating( (CalculatorCriterionRating)ratingToCopy.deepCopy() );
      }
    }
    else
    {
      criterion.setCriterionRatings( new ArrayList() );
    }
    return criterion;
  }

  public Calculator getCalculator()
  {
    return calculator;
  }

  public void setCalculator( Calculator calculator )
  {
    this.calculator = calculator;
  }

  public String getCmAssetName()
  {
    return cmAssetName;
  }

  public void setCmAssetName( String cmAssetName )
  {
    this.cmAssetName = cmAssetName;
  }

  public List<CalculatorCriterionRating> getCriterionRatings()
  {
    return criterionRatings;
  }

  public void addCriterionRating( CalculatorCriterionRating calcCriterionRating )
  {
    calcCriterionRating.setCalculatorCriterion( this );
    this.criterionRatings.add( calcCriterionRating );
  }

  public void setCriterionRatings( List criterionRatings )
  {
    this.criterionRatings = criterionRatings;
  }

  public CalculatorCriterionRating getRating( Long calculatorCriterionRatingId )
  {
    for ( CalculatorCriterionRating ccr : getCriterionRatings() )
    {
      if ( ccr.getId().equals( calculatorCriterionRatingId ) )
      {
        return ccr;
      }
    }
    return null;
  }

  public StatusType getCriterionStatus()
  {
    return criterionStatus;
  }

  public void setCriterionStatus( StatusType criterionStatus )
  {
    this.criterionStatus = criterionStatus;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public int getWeightValue()
  {
    return weightValue;
  }

  public void setWeightValue( int weightValue )
  {
    this.weightValue = weightValue;
  }

  public String getCriterionText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetName(), Calculator.CM_CRITERION_NAME_KEY );
  }

}
