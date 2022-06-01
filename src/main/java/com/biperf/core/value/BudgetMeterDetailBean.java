
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BudgetMeterDetailBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Long budgetId;
  private String budgetType;
  private Long budgetMasterId;
  private String nodeName;
  private Long nodeId;
  private Boolean isPrimaryNode = Boolean.FALSE;
  private String budgetDisplayName;
  private Integer usedBudget;
  private Integer totalBudget;
  // Bug 67645 Start
  private Integer remainingBudget;
  // Bug 67645 end
  private Date startDate;
  private Date endDate;
  private List<BudgetMeterDetailPromoBean> promoList = new ArrayList<BudgetMeterDetailPromoBean>();

  private static final String NODE = "node";

  @JsonIgnore
  public Boolean getIsShared()
  {
    return promoList != null && promoList.size() > 1;
  }

  @JsonProperty( "id" )
  public Long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( Long budgetId )
  {
    this.budgetId = budgetId;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  @JsonIgnore
  public String getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  @JsonIgnore
  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  @JsonIgnore
  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  @JsonIgnore
  public Long getNodeId()
  {
    return nodeId;
  }

  public void setIsPrimaryNode( Boolean isPrimaryNode )
  {
    this.isPrimaryNode = isPrimaryNode;
  }

  @JsonIgnore
  public Boolean getIsPrimaryNode()
  {
    return isPrimaryNode;
  }

  public void setBudgetDisplayName( String budgetDisplayName )
  {
    this.budgetDisplayName = budgetDisplayName;
  }

  @JsonProperty( "name" )
  public String getBudgetDisplayName()
  {
    return budgetDisplayName;
  }

  @JsonProperty( "used" )
  public Integer getUsedBudget()
  {
    return usedBudget;
  }

  public void setUsedBudget( Integer usedBudget )
  {
    this.usedBudget = usedBudget;
  }

  @JsonProperty( "total" )
  public Integer getTotalBudget()
  {
    return totalBudget;
  }

  public void setTotalBudget( Integer totalBudget )
  {
    this.totalBudget = totalBudget;
  }

  // Bug 67645 Start
  @JsonProperty( "remaining" )
  public Integer getRemainingBudget()
  {
    return remainingBudget;
  }

  public void setRemainingBudget( Integer remainingBudget )
  {
    this.remainingBudget = remainingBudget;
  }

  // Bug 67645 end
  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  @JsonProperty( "startDate" )
  public String getStartDateString()
  {
    if ( startDate != null )
    {
      return DateUtils.toDisplayString( startDate, UserManager.getLocale() );
    }
    return null;
  }

  @JsonProperty( "endDate" )
  public String getEndDateString()
  {
    if ( endDate != null )
    {
      return DateUtils.toDisplayString( endDate, UserManager.getLocale() );
    }
    return null;
  }

  @JsonProperty( "promotions" )
  public List<BudgetMeterDetailPromoBean> getPromoList()
  {
    return promoList;
  }

  public void setPromoList( List<BudgetMeterDetailPromoBean> promoList )
  {
    this.promoList = promoList;
  }
}
