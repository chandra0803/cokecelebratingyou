/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.util.List;

import com.biperf.core.value.client.TcccLevelPayoutValueBean;

/**
 * 
 * @author poddutur
 * @since May 18, 2016
 */
public class NominationsApprovalPageDetailsValueBean
{
  private String promotionName;
  private boolean finalLevelApprover;
  private int totalPromotionCount;
  private boolean timePeriodEnabled;
  private boolean payoutAtEachLevel;
  private String currencyLabel;
  private String payoutType;
  private String awardType;
  private String rulesText;
  private List<TcccLevelPayoutValueBean> levelPayoutList;
  
  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public boolean isFinalLevelApprover()
  {
    return finalLevelApprover;
  }

  public void setFinalLevelApprover( boolean finalLevelApprover )
  {
    this.finalLevelApprover = finalLevelApprover;
  }

  public int getTotalPromotionCount()
  {
    return totalPromotionCount;
  }

  public void setTotalPromotionCount( int totalPromotionCount )
  {
    this.totalPromotionCount = totalPromotionCount;
  }

  public boolean isTimePeriodEnabled()
  {
    return timePeriodEnabled;
  }

  public void setTimePeriodEnabled( boolean timePeriodEnabled )
  {
    this.timePeriodEnabled = timePeriodEnabled;
  }

  public boolean isPayoutAtEachLevel()
  {
    return payoutAtEachLevel;
  }

  public void setPayoutAtEachLevel( boolean payoutAtEachLevel )
  {
    this.payoutAtEachLevel = payoutAtEachLevel;
  }

  public String getCurrencyLabel()
  {
    return currencyLabel;
  }

  public void setCurrencyLabel( String currencyLabel )
  {
    this.currencyLabel = currencyLabel;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public String getRulesText()
  {
    return rulesText;
  }

  public void setRulesText( String rulesText )
  {
    this.rulesText = rulesText;
  }

/**
 * @return the levelPayoutList
 */
public List<TcccLevelPayoutValueBean> getLevelPayoutList() {
	return levelPayoutList;
}

/**
 * @param levelPayoutList the levelPayoutList to set
 */
public void setLevelPayoutList(List<TcccLevelPayoutValueBean> levelPayoutList) {
	this.levelPayoutList = levelPayoutList;
}

}
