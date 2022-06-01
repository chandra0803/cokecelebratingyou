/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/CalculatorResponse.java,v $
 */

package com.biperf.core.domain.claim;

import java.io.Serializable;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;

/**
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
 * <td>June 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorResponse extends BaseDomain implements Serializable
{

  private CalculatorCriterion criterion;
  private AbstractRecognitionClaim claim;
  private CalculatorCriterionRating selectedRating;
  private int sequenceNumber;

  /**
   * Represents the rating value at the time the calculator was filled out (actual rating value
   * could change).
   */
  private int ratingValue;

  /**
   * Represents the criteionWeight at the time the calculator was filled out (actual criterionWeight
   * could change).
   */
  private Integer criterionWeight;

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof CalculatorResponse ) )
    {
      return false;
    }

    CalculatorResponse calcResponse = (CalculatorResponse)object;

    if ( criterion != null ? !criterion.equals( calcResponse.getCriterion() ) : calcResponse.getCriterion() != null )
    {
      return false;
    }

    if ( claim != null ? !claim.equals( calcResponse.getClaim() ) : calcResponse.getClaim() != null )
    {
      return false;
    }

    return true;

  } // end equals

  public int hashCode()
  {
    int result = 0;

    result += criterion != null ? criterion.hashCode() : 0;
    result += claim != null ? claim.hashCode() : 13;

    return result;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toProcessString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( this.getCriterion().getId().toString() ).append( "," );
    if ( this.getCriterionWeight() == null )
    {
      buf.append( "," );
    }
    else
    {
      buf.append( Integer.toString( this.getCriterionWeight() ) ).append( "," );
    }
    buf.append( this.getSelectedRating().getId().toString() );
    return buf.toString();
  }

  public AbstractRecognitionClaim getClaim()
  {
    return claim;
  }

  public void setClaim( AbstractRecognitionClaim claim )
  {
    this.claim = claim;
  }

  public CalculatorCriterion getCriterion()
  {
    return criterion;
  }

  public void setCriterion( CalculatorCriterion criterion )
  {
    this.criterion = criterion;
  }

  public Integer getCriterionWeight()
  {
    return criterionWeight;
  }

  public void setCriterionWeight( Integer criterionWeight )
  {
    this.criterionWeight = criterionWeight;
  }

  public int getRatingValue()
  {
    return ratingValue;
  }

  public void setRatingValue( int ratingValue )
  {
    this.ratingValue = ratingValue;
  }

  public CalculatorCriterionRating getSelectedRating()
  {
    return selectedRating;
  }

  public void setSelectedRating( CalculatorCriterionRating selectedRating )
  {
    this.selectedRating = selectedRating;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

}
