
package com.biperf.core.domain.challengepoint;

import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.utils.DateUtils;

/**
 * ChallengepointReviewProgress.
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
 * <td>reddy</td>
 * <td>Jul 17, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengepointReviewProgress
{

  private Date submissionDate;
  private BigDecimal quantity;
  private BigDecimal cumulativeTotal;
  private BigDecimal percentToGoal;
  private BigDecimal quantityRemaining;
  private BigDecimal amountToAchieve;
  private String loadType;
  private int roundingType;
  private int achievementPrecision;

  public BigDecimal getCumulativeTotal()
  {
    return cumulativeTotal;
  }

  public void setCumulativeTotal( BigDecimal cumulativeTotal )
  {
    this.cumulativeTotal = cumulativeTotal;
  }

  public BigDecimal getPercentToGoal()
  {
    if ( percentToGoal != null )
    {
      return percentToGoal;
    }
    if ( cumulativeTotal != null && cumulativeTotal.doubleValue() != 0 && amountToAchieve != null && amountToAchieve.doubleValue() != 0 )
    {
      percentToGoal = cumulativeTotal.divide( amountToAchieve, 8, BigDecimal.ROUND_UP ).movePointRight( 2 ).setScale( achievementPrecision, roundingType );
    }
    return percentToGoal;
  }

  public void setPercentToGoal( BigDecimal percentToGoal )
  {
    this.percentToGoal = percentToGoal;
  }

  public BigDecimal getQuantity()
  {
    return quantity;
  }

  public void setQuantity( BigDecimal quantity )
  {
    this.quantity = quantity;
  }

  public BigDecimal getQuantityRemaining()
  {
    return quantityRemaining != null
        ? quantityRemaining
        : amountToAchieve == null ? null : getAmountToAchieve().subtract( getCumulativeTotal() ).intValue() < 0 ? new BigDecimal( "0.00" ) : getAmountToAchieve().subtract( getCumulativeTotal() );
  }

  public void setQuantityRemaining( BigDecimal quantityRemaining )
  {
    this.quantityRemaining = quantityRemaining;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public String getDisplaySubmissionDate()
  {
    return DateUtils.toDisplayString( this.getSubmissionDate() );
  }

  public String getDisplaySubmissionTimeStamp()
  {
    return DateUtils.toDisplayTimeString( this.getSubmissionDate() );
  }

  public String getDisplayPercentToGoal()
  {
    StringBuffer sb = new StringBuffer( getPercentToGoal() == null ? "" : getPercentToGoal().toString() );
    return sb.toString().equals( "" ) ? "" : sb.append( "%" ).toString();
  }

  public BigDecimal getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( BigDecimal amountToAchieve )
  {
    this.amountToAchieve = amountToAchieve;
  }

  public String getLoadType()
  {
    return loadType;
  }

  public void setLoadType( String loadType )
  {
    this.loadType = loadType;
  }

  public int getRoundingType()
  {
    return roundingType;
  }

  public void setRoundingType( int roundingType )
  {
    this.roundingType = roundingType;
  }

  public int getAchievementPrecision()
  {
    return achievementPrecision;
  }

  public void setAchievementPrecision( int achievementPrecision )
  {
    this.achievementPrecision = achievementPrecision;
  }

}
