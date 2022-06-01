/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/goalquest/GoalQuestReviewProgress.java,v $
 */

package com.biperf.core.domain.goalquest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.domain.enums.ProgressTransactionType;
import com.biperf.core.utils.DateUtils;

/**
 * GoalQuestReviewProgress contains the results of a GoalPayoutStrategy
 * 
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
 * <td>Satish Viswanathan</td>
 * <td>Jan 23, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class GoalQuestReviewProgress implements Serializable
{

  private Date submissionDate;
  private BigDecimal quantity;
  private BigDecimal cumulativeTotal;
  private BigDecimal percentToGoal;
  private BigDecimal quantityRemaining;
  private BigDecimal amountToAchieve;
  private String loadType;
  private boolean automotive;
  private Date saleDate;
  private Date deliveryDate;
  private String model;
  private String vin;
  private ProgressTransactionType transactionType;
  private String dealerCode;
  private String dealerName;
  private int roundingType;

  public int getRoundingType()
  {
    return roundingType;
  }

  public void setRoundingType( int roundingType )
  {
    this.roundingType = roundingType;
  }

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
    if ( cumulativeTotal != null && amountToAchieve != null && !amountToAchieve.equals( new BigDecimal( 0 ) ) )
    {
      percentToGoal = cumulativeTotal.divide( amountToAchieve, 2, roundingType ).movePointRight( 2 );
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

  public boolean isAutomotive()
  {
    return automotive;
  }

  public void setAutomotive( boolean automotive )
  {
    this.automotive = automotive;
  }

  public String getDealerCode()
  {
    return dealerCode;
  }

  public void setDealerCode( String dealerCode )
  {
    this.dealerCode = dealerCode;
  }

  public String getDealerName()
  {
    return dealerName;
  }

  public void setDealerName( String dealerName )
  {
    this.dealerName = dealerName;
  }

  public Date getDeliveryDate()
  {
    return deliveryDate;
  }

  public void setDeliveryDate( Date deliveryDate )
  {
    this.deliveryDate = deliveryDate;
  }

  public String getDisplayDeliveryDate()
  {
    return DateUtils.toDisplayString( this.getDeliveryDate() );
  }

  public String getModel()
  {
    return model;
  }

  public void setModel( String model )
  {
    this.model = model;
  }

  public Date getSaleDate()
  {
    return saleDate;
  }

  public void setSaleDate( Date saleDate )
  {
    this.saleDate = saleDate;
  }

  public String getDisplaySaleDate()
  {
    return DateUtils.toDisplayString( this.getSaleDate() );
  }

  public ProgressTransactionType getTransactionType()
  {
    return transactionType;
  }

  public void setTransactionType( ProgressTransactionType transactionType )
  {
    this.transactionType = transactionType;
  }

  public String getVin()
  {
    return vin;
  }

  public void setVin( String vin )
  {
    this.vin = vin;
  }
}
