/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetInformationValueObject.java,v $
 */

package com.biperf.core.ui.budget;

import java.io.Serializable;
import java.util.Collection;

import com.biperf.core.domain.budget.Budget;

/**
 * BudgetInformationValueObject.
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
 * <td>Todd</td>
 * <td>Aug 21, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetInformationValueObject implements Serializable
{
  /* The number of awards per participant */
  private Long awardsPerPax;
  /* The number of participants for the given budget */
  private Integer paxCount;
  /* The Budget */
  private Budget budget;
  /* characteristics */
  private Collection characteristics;

  public Integer getAmount()
  {
    if ( getAwardsPerPax() != null && getPaxCount() != null )
    {
      return new Integer( getAwardsPerPax().intValue() * getPaxCount().intValue() );
    }
    return null;
  }

  public Long getAwardsPerPax()
  {
    return awardsPerPax;
  }

  public void setAwardsPerPax( Long awardsPerPax )
  {
    this.awardsPerPax = awardsPerPax;
  }

  public Budget getBudget()
  {
    return budget;
  }

  public void setBudget( Budget budget )
  {
    this.budget = budget;
  }

  public Integer getPaxCount()
  {
    return paxCount;
  }

  public void setPaxCount( Integer paxCount )
  {
    this.paxCount = paxCount;
  }

  public Collection getCharacteristics()
  {
    return characteristics;
  }

  public void setCharacteristics( Collection characteristics )
  {
    this.characteristics = characteristics;
  }
}
