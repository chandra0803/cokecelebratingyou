
package com.biperf.core.domain.budget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class BudgetReallocationHistoryBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Long id;
  private Long txNodeId;
  private Long txUserId;
  private Long budgetId;
  private BigDecimal amount;
  private Long createdBy;
  private Timestamp dateCreated;
  private String source;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
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

  public Long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( Long budgetId )
  {
    this.budgetId = budgetId;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount( BigDecimal amount )
  {
    this.amount = amount;
  }

  public Long getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( Long createdBy )
  {
    this.createdBy = createdBy;
  }

  public Timestamp getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( Timestamp dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource( String source )
  {
    this.source = source;
  }

}
