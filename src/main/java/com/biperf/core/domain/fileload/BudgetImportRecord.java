/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/fileload/BudgetImportRecord.java,v $
 */

package com.biperf.core.domain.fileload;

import java.math.BigDecimal;

/*
 * BudgetImportRecord <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 27, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class BudgetImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The ID of the node that owns this budget. A budget is owned by either a node or a user.
   */
  private Long nodeId;

  /**
   * The ID of the user who owns this budget. A budget is owned by either a node or a user.
   */
  private Long userId;

  /**
   * The name of the entity--either a node or a user--that owns this budget.
   */
  private String budgetOwner;

  /**
   * The amount of the budget.
   */
  private BigDecimal budgetAmount;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the budget amount.
   * 
   * @return the budget amount.
   */
  public BigDecimal getBudgetAmount()
  {
    return budgetAmount;
  }

  /**
   * Returns the budget owner.
   * 
   * @return the budget owner.
   */
  public String getBudgetOwner()
  {
    return budgetOwner;
  }

  /**
   * Returns the node ID.
   * 
   * @return the node ID.
   */
  public Long getNodeId()
  {
    return nodeId;
  }

  /**
   * Returns the user ID.
   * 
   * @return the user ID.
   */
  public Long getUserId()
  {
    return userId;
  }

  /**
   * Sets the budget amount.
   * 
   * @param budgetAmount the budget amount.
   */
  public void setBudgetAmount( BigDecimal budgetAmount )
  {
    this.budgetAmount = budgetAmount;
  }

  /**
   * Sets the budget owner.
   * 
   * @param budgetOwner the budget owner.
   */
  public void setBudgetOwner( String budgetOwner )
  {
    this.budgetOwner = budgetOwner;
  }

  /**
   * Sets the node ID.
   * 
   * @param nodeId the node ID.
   */
  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  /**
   * Sets the user ID.
   * 
   * @param userId the user ID.
   */
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the given object and this object are equal; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if the given object and this object are equal; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof BudgetImportRecord ) )
    {
      return false;
    }

    BudgetImportRecord budgetImportRecord = (BudgetImportRecord)object;

    if ( this.getId() != null && this.getId().equals( budgetImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Returns a string representation of this object.
   * 
   * @see java.lang.Object#toString()
   * @return a string representation of this object.
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "BudgetImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }
}
