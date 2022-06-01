
package com.biperf.core.value.recognitionadvisor;

import java.util.Date;

import com.biperf.core.domain.enums.BudgetType;

public class RecognitionAdvisorUnusedBudgetBean
{
  private String promotionName;
  private Date lastRecDate;
  private Long daysSinceLastRec;
  private Long points;
  private BudgetType budgetType;
  private Long daysToPromoExp;
  private String dayssincelastcolor;
  private String daystopromoexpcolor;

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getLastRecDate()
  {
    return lastRecDate;
  }

  public void setLastRecDate( Date lastRecDate )
  {
    this.lastRecDate = lastRecDate;
  }

  public Long getDaysSinceLastRec()
  {
    return daysSinceLastRec;
  }

  public void setDaysSinceLastRec( Long daysSinceLastRec )
  {
    this.daysSinceLastRec = daysSinceLastRec;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public BudgetType getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( BudgetType budgetType )
  {
    this.budgetType = budgetType;
  }

  public Long getDaysToPromoExp()
  {
    return daysToPromoExp;
  }

  public void setDaysToPromoExp( Long daysToPromoExp )
  {
    this.daysToPromoExp = daysToPromoExp;
  }

  public String getDayssincelastcolor()
  {
    return dayssincelastcolor;
  }

  public void setDayssincelastcolor( String dayssincelastcolor )
  {
    this.dayssincelastcolor = dayssincelastcolor;
  }

  public String getDaystopromoexpcolor()
  {
    return daystopromoexpcolor;
  }

  public void setDaystopromoexpcolor( String daystopromoexpcolor )
  {
    this.daystopromoexpcolor = daystopromoexpcolor;
  }
}
