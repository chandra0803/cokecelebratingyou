/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/budget/BudgetHistory.java,v $
 */

package com.biperf.core.domain.budget;

import java.math.BigDecimal;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.enums.BudgetActionType;

/**
 * BudgetHistory domain object which represents a budget_history within the Beacon application.
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
 * <td>sedey</td>
 * <td>May 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetHistory implements AuditCreateInterface
{
  private Long id;
  private Long budgetId;
  private BigDecimal originalValueBeforeTransaction;
  private BigDecimal currentValueBeforeTransaction;
  private BigDecimal originalValueAfterTransaction;
  private BigDecimal currentValueAfterTransaction;
  private Long claimId;
  private BudgetActionType actionType;
  private Long fromBudgetId;
  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "BudgetHistory" );
    buf.append( "{id=" ).append( id );
    buf.append( ",budgetId=" ).append( budgetId );
    buf.append( ",originalValueBeforeTransaction=" ).append( originalValueBeforeTransaction.doubleValue() );
    buf.append( ",currentValueBeforeTransaction=" ).append( currentValueBeforeTransaction.doubleValue() );
    buf.append( ",originalValueAfterTransaction=" ).append( originalValueAfterTransaction.doubleValue() );
    buf.append( ",currentValueAfterTransaction=" ).append( currentValueAfterTransaction.doubleValue() );
    buf.append( ",actionType=" ).append( actionType.getCode() );
    buf.append( ",auditCreateInfo=" ).append( auditCreateInfo );
    buf.append( '}' );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof BudgetHistory ) )
    {
      return false;
    }

    final BudgetHistory budgetHistory = (BudgetHistory)o;

    if ( getBudgetId() != null && getAuditCreateInfo().getDateCreated() != null )
    {
      if ( !getBudgetId().equals( budgetHistory.getBudgetId() ) && !getAuditCreateInfo().getDateCreated().equals( budgetHistory.getAuditCreateInfo().getDateCreated() ) )
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Does hashCode on the Business Key Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    if ( getBudgetId() != null && getAuditCreateInfo().getDateCreated() != null )
    {
      return getBudgetId().hashCode() + getAuditCreateInfo().getDateCreated().hashCode();
    }
    return 0;
  }

  public AuditCreateInfo getAuditCreateInfo()
  {
    return auditCreateInfo;
  }

  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  public Long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( Long budgetId )
  {
    this.budgetId = budgetId;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public BudgetActionType getActionType()
  {
    return actionType;
  }

  public void setActionType( BudgetActionType actionType )
  {
    this.actionType = actionType;
  }

  public BigDecimal getOriginalValueBeforeTransaction()
  {
    return originalValueBeforeTransaction;
  }

  public void setOriginalValueBeforeTransaction( BigDecimal originalValueBeforeTransaction )
  {
    this.originalValueBeforeTransaction = originalValueBeforeTransaction;
  }

  public BigDecimal getCurrentValueBeforeTransaction()
  {
    return currentValueBeforeTransaction;
  }

  public void setCurrentValueBeforeTransaction( BigDecimal currentValueBeforeTransaction )
  {
    this.currentValueBeforeTransaction = currentValueBeforeTransaction;
  }

  public BigDecimal getOriginalValueAfterTransaction()
  {
    return originalValueAfterTransaction;
  }

  public void setOriginalValueAfterTransaction( BigDecimal originalValueAfterTransaction )
  {
    this.originalValueAfterTransaction = originalValueAfterTransaction;
  }

  public BigDecimal getCurrentValueAfterTransaction()
  {
    return currentValueAfterTransaction;
  }

  public void setCurrentValueAfterTransaction( BigDecimal currentValueAfterTransaction )
  {
    this.currentValueAfterTransaction = currentValueAfterTransaction;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getFromBudgetId()
  {
    return fromBudgetId;
  }

  public void setFromBudgetId( Long fromBudgetId )
  {
    this.fromBudgetId = fromBudgetId;
  }
}
