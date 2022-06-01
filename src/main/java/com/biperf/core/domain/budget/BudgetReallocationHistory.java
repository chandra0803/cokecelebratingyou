
package com.biperf.core.domain.budget;

import java.math.BigDecimal;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;

public class BudgetReallocationHistory implements AuditCreateInterface
{
  private Long id;
  private Long txNodeId;
  private Long txUserId;
  private Long budgetId;
  private BigDecimal amount;
  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "BudgetReallocationHistory" );
    buf.append( "{id=" ).append( id );
    buf.append( ",txNodeId=" ).append( txNodeId );
    buf.append( ",txUserId=" ).append( txUserId );
    buf.append( ",budgetId=" ).append( budgetId );
    buf.append( ",amount=" ).append( amount.doubleValue() );
    buf.append( ",auditCreateInfo=" ).append( auditCreateInfo );
    buf.append( '}' );
    return buf.toString();
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof BudgetReallocationHistory ) )
    {
      return false;
    }

    final BudgetReallocationHistory budgetHistory = (BudgetReallocationHistory)o;

    if ( getBudgetId() != null && getAuditCreateInfo().getDateCreated() != null )
    {
      if ( !getBudgetId().equals( budgetHistory.getBudgetId() ) && !getAuditCreateInfo().getDateCreated().equals( budgetHistory.getAuditCreateInfo().getDateCreated() ) )
      {
        return false;
      }
    }
    return true;
  }

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

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount( BigDecimal amount )
  {
    this.amount = amount;
  }

  public Long getTxNodeId()
  {
    return txNodeId;
  }

  public void setTxNodeId( Long txNodeId )
  {
    this.txNodeId = txNodeId;
  }

  public Long getTxUserId()
  {
    return txUserId;
  }

  public void setTxUserId( Long txUserId )
  {
    this.txUserId = txUserId;
  }

}
