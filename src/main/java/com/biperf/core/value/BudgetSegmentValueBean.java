
package com.biperf.core.value;

import java.util.Date;

import com.biperf.core.domain.enums.BudgetStatusType;
import com.objectpartners.cms.util.DateUtils;

public class BudgetSegmentValueBean
{
  private Long id;
  private Boolean status; // segment status
  private String startDateStr;
  private String endDateStr;
  private String segmentName;
  private String originalValue;
  private String currentValue;
  private String addOnValue;
  private BudgetStatusType budgetStatus; // for central budget only
  private boolean allowBudgetReallocation;
  private String budgetReallocationEligTypeCode;
  private String budgetSweepDate;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Boolean getStatus()
  {
    return status;
  }

  public void setStatus( Boolean status )
  {
    this.status = status;
  }

  public String getStartDateStr()
  {
    return startDateStr;
  }

  public void setStartDateStr( String startDateStr )
  {
    this.startDateStr = startDateStr;
  }

  public String getEndDateStr()
  {
    return endDateStr;
  }

  public void setEndDateStr( String endDateStr )
  {
    this.endDateStr = endDateStr;
  }

  public String getSegmentName()
  {
    return segmentName;
  }

  public void setSegmentName( String segmentName )
  {
    this.segmentName = segmentName;
  }

  public String getOriginalValue()
  {
    return originalValue;
  }

  public void setOriginalValue( String originalValue )
  {
    this.originalValue = originalValue;
  }

  public String getCurrentValue()
  {
    return currentValue;
  }

  public void setCurrentValue( String currentValue )
  {
    this.currentValue = currentValue;
  }

  public BudgetStatusType getBudgetStatus()
  {
    return budgetStatus;
  }

  public void setBudgetStatus( BudgetStatusType budgetStatus )
  {
    this.budgetStatus = budgetStatus;
  }

  public String getAddOnValue()
  {
    return addOnValue;
  }

  public void setAddOnValue( String addOnValue )
  {
    this.addOnValue = addOnValue;
  }

  public boolean isAllowBudgetReallocation()
  {
    return allowBudgetReallocation;
  }

  public void setAllowBudgetReallocation( boolean allowBudgetReallocation )
  {
    this.allowBudgetReallocation = allowBudgetReallocation;
  }

  public String getBudgetReallocationEligTypeCode()
  {
    return budgetReallocationEligTypeCode;
  }

  public void setBudgetReallocationEligTypeCode( String budgetReallocationEligTypeCode )
  {
    this.budgetReallocationEligTypeCode = budgetReallocationEligTypeCode;
  }

  public void setBudgetSweepDate( String budgetSweepDate )
  {
    this.budgetSweepDate = budgetSweepDate;
  }

  public String getBudgetSweepDate()
  {
    return budgetSweepDate;
  }

  public Date getStartDate()
  {
    return DateUtils.toDate( this.getStartDateStr() );
  }

  public Date getEndDate()
  {
    return DateUtils.toDate( this.getEndDateStr() );
  }

  public Date getCurrentDate()
  {
    return DateUtils.getCurrentDate();
  }

  public Boolean isStartDatePast()
  {
    return getStartDate().before( getCurrentDate() ) && !org.apache.commons.lang3.time.DateUtils.isSameDay( getStartDate(), getCurrentDate() );
  }

  public Boolean isEndDatePast()
  {
    return getEndDate().before( getCurrentDate() ) && !org.apache.commons.lang3.time.DateUtils.isSameDay( getEndDate(), getCurrentDate() );
  }

}
