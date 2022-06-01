
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.BudgetType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BudgetMeter implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String asOfDate;
  private String timezone;
  private List<BudgetMeterDetailBean> budgetMeterDetails = new ArrayList<BudgetMeterDetailBean>();
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  /**
   * Sorts the budget meter list in correct display order.  And if the list contains more than 1 Node (ie. user belongs
   * to multiple nodes), the budget display names will be updated to display that Node name
   */
  public void sortAndPopulateDisplayNames()
  {
    // Sort budgets
    for ( BudgetMeterDetailBean budgetMeterDetail : budgetMeterDetails )
    {
      Collections.sort( budgetMeterDetail.getPromoList() );
    }
    if ( this.containsMultipleNodes() )
    {
      Collections.sort( budgetMeterDetails, new BudgetMeterDetailBeanMultipleNodeComparator() );
    }
    else
    {
      Collections.sort( budgetMeterDetails, new BudgetMeterDetailBeanSingleNodeComparator() );
    }

    // Populate node names where needed
    if ( this.containsMultipleNodes() )
    {
      Long lastNodeId = null;
      for ( BudgetMeterDetailBean budgetMeterDetail : budgetMeterDetails )
      {
        if ( BudgetType.NODE_BUDGET_TYPE.equals( budgetMeterDetail.getBudgetType() ) )
        {
          if ( lastNodeId == null || !budgetMeterDetail.getNodeId().equals( lastNodeId ) )
          {
            budgetMeterDetail.setBudgetDisplayName( budgetMeterDetail.getNodeName() );
            lastNodeId = budgetMeterDetail.getNodeId();
          }
        }
      }
    }
  }

  /**
   * Searches the <code>budgetMeterDetails</code> list for any budgets which match the given <code>budgetId</code>
   * There should be only one entry in <code>budgetMeterDetails</code> for each budget.
   * @param budgetId
   * @return <strong>BudgetMeterDetailBean</strong> - null if none found
   */
  @JsonIgnore
  public BudgetMeterDetailBean getBudgetMeterDetailForBudgetId( Long budgetId )
  {
    for ( BudgetMeterDetailBean budgetMeterDetail : budgetMeterDetails )
    {
      if ( budgetMeterDetail.getBudgetId().equals( budgetId ) )
      {
        return budgetMeterDetail;
      }
    }

    return null;
  }

  /**
   * Searches the <code>budgetMeterDetails</code> list to see if more than one node id exists.  This would indicate
   * that the pax belongs to more than one node.
   * @return Boolean
   */
  @JsonIgnore
  public Boolean containsMultipleNodes()
  {
    Set<Long> nodeIds = new HashSet<Long>();
    for ( BudgetMeterDetailBean budgetMeterDetail : budgetMeterDetails )
    {
      if ( BudgetType.NODE_BUDGET_TYPE.equals( budgetMeterDetail.getBudgetType() ) )
      {
        nodeIds.add( budgetMeterDetail.getNodeId() );
      }
      if ( nodeIds.size() > 1 )
      {
        return true;
      }
    }

    return false;
  }

  @JsonIgnore
  public String getAsOfDate()
  {
    return asOfDate;
  }

  public void setAsOfDate( String asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  public void setTimezone( String timezone )
  {
    this.timezone = timezone;
  }

  @JsonProperty( "budgetAsOfTimestamp" )
  public String getAsOfDateAsString()
  {
    return this.asOfDate + " " + this.timezone;
  }

  @JsonProperty( "budgets" )
  public List<BudgetMeterDetailBean> getBudgetMeterDetails()
  {
    return budgetMeterDetails;
  }

  public void setBudgetMeterDetails( List<BudgetMeterDetailBean> budgetMeterDetails )
  {
    this.budgetMeterDetails = budgetMeterDetails;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

}
