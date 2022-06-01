/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/calculator/CalculatorPayout.java,v $
 */

package com.biperf.core.domain.calculator;

import com.biperf.core.domain.BaseDomain;

/**
 * CalculatorPayout.
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
 * <td>May 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorPayout extends BaseDomain
{
  private Calculator calculator;
  private int lowScore;
  private int highScore;
  private int lowAward;
  private int highAward;

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

    if ( ! ( object instanceof CalculatorPayout ) )
    {
      return false;
    }

    final CalculatorPayout calcPayout = (CalculatorPayout)object;

    if ( calcPayout.getLowScore() != this.getLowScore() )
    {
      return false;
    }
    if ( calcPayout.getHighScore() != this.getHighScore() )
    {
      return false;
    }
    if ( calcPayout.getCalculator() != null && !calcPayout.getCalculator().equals( this.getCalculator() ) )
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

    result += this.getLowScore();
    result += this.getHighScore();
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

    sb.append( "[CALC_PAYOUT {" );
    sb.append( "Calculator.id - " + this.getCalculator().getId() + ", " );
    sb.append( "lowScore - " + this.getLowScore() + ", " );
    sb.append( "highScore - " + this.getHighScore() + ", " );
    sb.append( "lowAward - " + this.getLowAward() + ", " );
    sb.append( "highAward - " + this.getHighAward() );
    sb.append( "}]" );
    return sb.toString();
  }

  /**
   * Does a deep copy of the CalculatorCriterion and its children if specified. This is a customized
   * implementation of java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @return Object
   */
  public Object deepCopy()
  {
    CalculatorPayout payout = new CalculatorPayout();
    payout.setHighAward( this.getHighAward() );
    payout.setLowAward( this.getLowAward() );
    payout.setHighScore( this.getHighScore() );
    payout.setLowScore( this.getLowScore() );
    return payout;
  }

  public Calculator getCalculator()
  {
    return calculator;
  }

  public void setCalculator( Calculator calculator )
  {
    this.calculator = calculator;
  }

  public int getHighAward()
  {
    return highAward;
  }

  public void setHighAward( int highAward )
  {
    this.highAward = highAward;
  }

  public int getHighScore()
  {
    return highScore;
  }

  public void setHighScore( int highScore )
  {
    this.highScore = highScore;
  }

  public int getLowAward()
  {
    return lowAward;
  }

  public void setLowAward( int lowAward )
  {
    this.lowAward = lowAward;
  }

  public int getLowScore()
  {
    return lowScore;
  }

  public void setLowScore( int lowScore )
  {
    this.lowScore = lowScore;
  }

}
