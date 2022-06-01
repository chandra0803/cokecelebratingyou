
package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.enums.PromotionAwardsType;

public class NominationPayoutFinalLevelBean implements Serializable
{

  private static final long serialVersionUID = 1L;

  private String awardsType;
  private String awardsTypeDesc;
  private String payoutDescription;
  private String payoutValue;
  private String payoutCurrency;
  private String quantity;
  private String awardAmountTypeFixed = "true";
  private String fixedAmount;
  private String rangeAmountMin = null;
  private String rangeAmountMax = null;
  private Long levelId;
  private Long finalLevelCalculatorId;
  private String levelLabel;
  private List<PromotionAwardsType> awardsTypeList = new ArrayList<PromotionAwardsType>();
  private List<Calculator> calculatorList = new ArrayList<Calculator>();

  public String getAwardsType()
  {
    return awardsType;
  }

  public void setAwardsType( String awardsType )
  {
    this.awardsType = awardsType;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public String getPayoutValue()
  {
    return payoutValue;
  }

  public void setPayoutValue( String payoutValue )
  {
    this.payoutValue = payoutValue;
  }

  public String getPayoutCurrency()
  {
    return payoutCurrency;
  }

  public void setPayoutCurrency( String payoutCurrency )
  {
    this.payoutCurrency = payoutCurrency;
  }

  public String getAwardsTypeDesc()
  {
    return awardsTypeDesc;
  }

  public void setAwardsTypeDesc( String awardsTypeDesc )
  {
    this.awardsTypeDesc = awardsTypeDesc;
  }

  public String getAwardAmountTypeFixed()
  {
    return awardAmountTypeFixed;
  }

  public void setAwardAmountTypeFixed( String awardAmountTypeFixed )
  {
    this.awardAmountTypeFixed = awardAmountTypeFixed;
  }

  public String getFixedAmount()
  {
    return fixedAmount;
  }

  public void setFixedAmount( String fixedAmount )
  {
    this.fixedAmount = fixedAmount;
  }

  public String getRangeAmountMin()
  {
    return rangeAmountMin;
  }

  public void setRangeAmountMin( String rangeAmountMin )
  {
    this.rangeAmountMin = rangeAmountMin;
  }

  public String getRangeAmountMax()
  {
    return rangeAmountMax;
  }

  public void setRangeAmountMax( String rangeAmountMax )
  {
    this.rangeAmountMax = rangeAmountMax;
  }

  public String getQuantity()
  {
    return quantity;
  }

  public void setQuantity( String quantity )
  {
    this.quantity = quantity;
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

  public Long getFinalLevelCalculatorId()
  {
    return finalLevelCalculatorId;
  }

  public void setFinalLevelCalculatorId( Long finalLevelCalculatorId )
  {
    this.finalLevelCalculatorId = finalLevelCalculatorId;
  }

  public String getLevelLabel()
  {
    return levelLabel;
  }

  public void setLevelLabel( String levelLabel )
  {
    this.levelLabel = levelLabel;
  }

  public List<PromotionAwardsType> getAwardsTypeList()
  {
    return awardsTypeList;
  }

  public void setAwardsTypeList( List<PromotionAwardsType> awardsTypeList )
  {
    this.awardsTypeList = awardsTypeList;
  }

  public List getCalculatorList()
  {
    return calculatorList;
  }

  public void setCalculatorList( List calculatorList )
  {
    this.calculatorList = calculatorList;
  }

}
