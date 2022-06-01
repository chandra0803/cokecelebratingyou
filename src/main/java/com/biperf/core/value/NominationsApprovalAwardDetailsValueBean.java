/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.math.BigDecimal;

/**
 * 
 * @author poddutur
 * @since May 20, 2016
 */
public class NominationsApprovalAwardDetailsValueBean
{
  private Long claimId;
  private int levelIndex;
  private boolean awardAmountTypeFixed;
  private BigDecimal awardAmountMin;
  private BigDecimal awardAmountMax;
  private Long calculatorId;
  private boolean awardAmountTypeRange;
  private boolean awardAmountTypeNone;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public int getLevelIndex()
  {
    return levelIndex;
  }

  public void setLevelIndex( int levelIndex )
  {
    this.levelIndex = levelIndex;
  }

  public boolean isAwardAmountTypeFixed()
  {
    return awardAmountTypeFixed;
  }

  public void setAwardAmountTypeFixed( boolean awardAmountTypeFixed )
  {
    this.awardAmountTypeFixed = awardAmountTypeFixed;
  }

  public BigDecimal getAwardAmountMin()
  {
    return awardAmountMin;
  }

  public void setAwardAmountMin( BigDecimal awardAmountMin )
  {
    this.awardAmountMin = awardAmountMin;
  }

  public BigDecimal getAwardAmountMax()
  {
    return awardAmountMax;
  }

  public void setAwardAmountMax( BigDecimal awardAmountMax )
  {
    this.awardAmountMax = awardAmountMax;
  }

  public Long getCalculatorId()
  {
    return calculatorId;
  }

  public void setCalculatorId( Long calculatorId )
  {
    this.calculatorId = calculatorId;
  }

  public boolean isAwardAmountTypeRange()
  {
    return awardAmountTypeRange;
  }

  public boolean isAwardAmountTypeNone()
  {
    return awardAmountTypeNone;
  }

  public void setAwardAmountTypeRange( boolean awardAmountTypeRange )
  {
    this.awardAmountTypeRange = awardAmountTypeRange;
  }

  public void setAwardAmountTypeNone( boolean awardAmountTypeNone )
  {
    this.awardAmountTypeNone = awardAmountTypeNone;
  }
}
