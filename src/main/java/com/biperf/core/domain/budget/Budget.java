/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/budget/Budget.java,v $
 */

package com.biperf.core.domain.budget;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.BudgetUtils;

/**
 * Budget domain object which represents a budget within the Beacon application.
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
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Budget extends BaseDomain
{

  private static final long serialVersionUID = 3256726164994536240L;

  private BudgetSegment budgetSegment;
  private User user;
  private Node node;
  private BigDecimal originalValue;
  private BigDecimal currentValue;
  private String overdrawn;
  private BudgetStatusType status;
  // used in budget file load to determine whether to replace or add budget amount
  private Date effectiveDate;
  private BudgetActionType actionType;
  private Long referenceVariableForClaimId;
  private boolean budgetDeletable;
  @Transient
  private Long fromBudget;
  
//Client customization start
  private boolean parentBudgetUsed;  

  public boolean isParentBudgetUsed()
  {
    return parentBudgetUsed;
  }

  public void setParentBudgetUsed( boolean parentBudgetUsed )
  {
    this.parentBudgetUsed = parentBudgetUsed;
  }
  //Client customization end

  public Long getFromBudget()
  {
    return fromBudget;
  }

  public void setFromBudget( Long fromBudget )
  {
    this.fromBudget = fromBudget;
  }

  public void setActionType( BudgetActionType actionType )
  {
    this.actionType = actionType;
  }

  public BudgetActionType getActionType()
  {
    return actionType;
  }

  public Date getEffectiveDate()
  {
    return effectiveDate;
  }

  public void setEffectiveDate( Date effectiveDate )
  {
    this.effectiveDate = effectiveDate;
  }

  /**
     * Builds a String representation of this class. Overridden from
     * 
     * @see java.lang.Object#toString()
     * @return String
     */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Budget [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{budgetMaster=" ).append( this.getBudgetSegment() ).append( "}, " );
    buf.append( "{user=" ).append( this.getUser() ).append( "}, " );
    buf.append( "{node=" ).append( this.getNode() ).append( "}, " );
    buf.append( "{originalValue=" ).append( this.getOriginalValue().doubleValue() ).append( "}, " );
    buf.append( "{currentValue=" ).append( this.getCurrentValue().doubleValue() ).append( "}" );
    buf.append( "{overdrawn=" ).append( this.getOverdrawn() ).append( "}" );
    buf.append( "{status=" ).append( this.getStatus().getCode() ).append( "}" );
    buf.append( "{actionType=" ).append( this.getActionType().getCode() ).append( "}" );
    if ( this.getEffectiveDate() != null )
    {
      buf.append( "{effectiveDate=" ).append( this.getEffectiveDate() ).append( "}" );
    }
    buf.append( "]" );
    return buf.toString();
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    else if ( ! ( o instanceof Budget ) )
    {
      return false;
    }
    else
    {
      final Budget budget = (Budget)o;
      if ( ( budgetSegment != null ? budgetSegment.equals( budget.getBudgetSegment() ) : budget.getBudgetSegment() == null )
          && ( node != null ? node.equals( budget.getNode() ) : budget.getNode() == null ) && ( user != null ? user.equals( budget.getUser() ) : budget.getUser() == null )
          && ( originalValue != null ? originalValue.equals( budget.getOriginalValue() ) : budget.getOriginalValue() == null )
          && ( currentValue != null ? currentValue.equals( budget.getCurrentValue() ) : budget.getCurrentValue() == null )
          && ( overdrawn != null ? overdrawn.equals( budget.getOverdrawn() ) : budget.getOverdrawn() == null ) && ( status != null ? status.equals( budget.getStatus() ) : budget.getStatus() == null )
          && ( effectiveDate != null ? effectiveDate.equals( budget.getEffectiveDate() ) : budget.getEffectiveDate() == null )
          && ( actionType != null ? actionType.equals( budget.getActionType() ) : budget.getActionType() == null ) )
      {
        return true;
      }

      return false;
    }
  }

  public int hashCode()
  {
    int result;
    result = getBudgetSegment() != null ? getBudgetSegment().hashCode() : 0;
    result = 29 * result + ( getUser() != null ? getUser().hashCode() : 0 );
    result = 29 * result + ( getNode() != null ? getNode().hashCode() : 0 );
    return result;
  }

  public BigDecimal getCurrentValue()
  {
    return currentValue;
  }

  public void setCurrentValue( BigDecimal currentValue )
  {
    this.currentValue = currentValue;
  }

  public BigDecimal getOriginalValue()
  {
    return originalValue;
  }

  public void setOriginalValue( BigDecimal originalValue )
  {
    this.originalValue = originalValue;
  }

  public int getCurrentValueDisplay()
  {
    if ( currentValue != null )
    {
      return BudgetUtils.getBudgetDisplayValue( currentValue );
    }
    return 0;
  }

  public int getOriginalValueDisplay()
  {
    if ( originalValue != null )
    {
      return BudgetUtils.getBudgetDisplayValue( originalValue );
    }
    return 0;
  }

  public String getOverdrawn()
  {
    return overdrawn;
  }

  public void setOverdrawn( String overdrawn )
  {
    this.overdrawn = overdrawn;
  }

  public BudgetStatusType getStatus()
  {
    return status;
  }

  public void setStatus( BudgetStatusType status )
  {
    this.status = status;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public BudgetSegment getBudgetSegment()
  {
    return budgetSegment;
  }

  public void setBudgetSegment( BudgetSegment budgetSegment )
  {
    this.budgetSegment = budgetSegment;
  }

  public Long getReferenceVariableForClaimId()
  {
    return referenceVariableForClaimId;
  }

  public void setReferenceVariableForClaimId( Long referenceVariableForClaimId )
  {
    this.referenceVariableForClaimId = referenceVariableForClaimId;
  }

  public User getBudgetOwner()
  {
    User budgetOwner;
    String BudgetTypeCode = budgetSegment.getBudgetMaster().getBudgetType().getCode();
    if ( BudgetTypeCode.equals( BudgetType.NODE_BUDGET_TYPE ) )
    {
      budgetOwner = node.getNodeOwner();
    }
    else if ( BudgetTypeCode.equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      budgetOwner = user;
    }
    else if ( BudgetTypeCode.equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
    {
      budgetOwner = null;
    }
    else
    {
      throw new BeaconRuntimeException( "Unknown Budget Type Code: " + BudgetTypeCode );
    }

    return budgetOwner;
  }

  public boolean isDeletable()
  {
    boolean isDeletable = false;

    if ( !budgetSegment.getBudgetMaster().isCentralBudget() )
    {
      if ( currentValue.equals( new BigDecimal( 0 ) ) && originalValue.equals( new BigDecimal( 0 ) ) )
      {
        isDeletable = true;
      }
    }

    return isDeletable;
  }

  public boolean isBudgetDeletable()
  {
    return budgetDeletable;
  }

  public void setBudgetDeletable( boolean budgetDeletable )
  {
    this.budgetDeletable = budgetDeletable;
  }

}
