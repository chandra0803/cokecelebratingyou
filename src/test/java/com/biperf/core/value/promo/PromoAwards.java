
package com.biperf.core.value.promo;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;

public class PromoAwards
{

  private boolean awardsactive;
  private PromotionAwardsType awardType;
  private boolean awardsTaxable;
  private boolean payOutEachLevel;
  private boolean payOutFinalLevel;
  private boolean awardAmountFixed = true;
  private Long awardAmount;

  private boolean timePeriodActive;

  private List<NominationPromotionTimePeriod> timePeriods = new ArrayList<NominationPromotionTimePeriod>();

  public boolean isAwardsactive()
  {
    return awardsactive;
  }

  public void setAwardsactive( boolean awardsactive )
  {
    this.awardsactive = awardsactive;
  }

  public PromotionAwardsType getAwardType()
  {
    return awardType;
  }

  public void setAwardType( PromotionAwardsType awardType )
  {
    this.awardType = awardType;
  }

  public boolean isAwardsTaxable()
  {
    return awardsTaxable;
  }

  public void setAwardsTaxable( boolean awardsTaxable )
  {
    this.awardsTaxable = awardsTaxable;
  }

  public boolean isPayOutEachLevel()
  {
    return payOutEachLevel;
  }

  public void setPayOutEachLevel( boolean payOutEachLevel )
  {
    this.payOutEachLevel = payOutEachLevel;
  }

  public boolean isPayOutFinalLevel()
  {
    return payOutFinalLevel;
  }

  public void setPayOutFinalLevel( boolean payOutFinalLevel )
  {
    this.payOutFinalLevel = payOutFinalLevel;
  }

  public boolean isAwardAmountFixed()
  {
    return awardAmountFixed;
  }

  public void setAwardAmountFixed( boolean awardAmountFixed )
  {
    this.awardAmountFixed = awardAmountFixed;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public boolean isTimePeriodActive()
  {
    return timePeriodActive;
  }

  public void setTimePeriodActive( boolean timePeriodActive )
  {
    this.timePeriodActive = timePeriodActive;
  }

  public List<NominationPromotionTimePeriod> getTimePeriods()
  {
    return timePeriods;
  }

  public void setTimePeriods( List<NominationPromotionTimePeriod> timePeriods )
  {
    this.timePeriods = timePeriods;
  }

}
