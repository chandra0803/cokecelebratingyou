/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/budget/BudgetQueryConstraint.java,v $
 */

package com.biperf.core.dao.budget;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * BudgetQueryConstraint is a concrete QueryConstraint for criteria queries on Budget.
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
 * <td>meadows</td>
 * <td>Aug 4, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class BudgetQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /* Only budgets with this budget segment id */
  private Long budgetSegmentId;

  /* If true the constraint will use user values, if false node values */
  private boolean userBasedConstraints;

  /*
   * A Map of CharacteristicConstraintLimits to constrain the query with. The key is the
   * characteristic id. If userBasedConstraints is set then these will be on user characteristics,
   * otherwise on node characteristics.
   */
  private Map characteristicConstraintLimits;

  /* A List of node types to include */
  private List nodeTypeIds;

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "budget" );
    if ( budgetSegmentId != null )
    {
      criteria.add( Restrictions.eq( "budget.budgetSegment.id", budgetSegmentId ) );
    }

    if ( isUserBasedConstraints() )
    {
      criteria.add( Restrictions.isNotNull( "budget.user" ) );
    }
    else
    {
      criteria.add( Restrictions.isNotNull( "budget.node" ) );
      criteria.setFetchMode( "node", FetchMode.JOIN );
      criteria.createAlias( "node.nodeType", "nodeType" );
      criteria.add( Restrictions.in( "nodeType.id", nodeTypeIds ) );
      // createAliasIfNotAlreadyCreated( criteria,
      // "node.nodeType",
      // "nodeType" ).add( Restrictions
      // .in( "nodeType.id", nodeTypeIds ) );

    }

    // Not sure if it is possible to do criteria search based on characteristics. If it is
    // I suspect it would be very complicated. For now we will return the entire list
    // and then filter from the DAO. Could maybe experiment with sqlRestrictions but I suspect
    // this may be more performent anyways.

    return criteria;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Return the class of the objects selected by this query constraint.
   *
   * @return the class of the objects selected by this query constraint.
   */
  public Class getResultClass()
  {
    return Budget.class;
  }

  /**
   * @return a boolean indicating if the constraints are user related.  If this value
   *            is false then the constraint is node related.
   */
  public boolean isUserBasedConstraints()
  {
    return userBasedConstraints;
  }

  /**
   * @param userBasedConstraints a boolean indicating if the constraints are user related.  If 
   *            this value is false then the constraint is node related.
   */
  public void setUserBasedConstraints( boolean userBasedConstraints )
  {
    this.userBasedConstraints = userBasedConstraints;
  }

  /**
   * @return a Long containing the budgetMasterId to search for
   */
  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  /**
   * @param budgetMasterId a Long containing the budgetMasterId to search for
   */
  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public Map getCharacteristicConstraintLimits()
  {
    if ( characteristicConstraintLimits == null )
    {
      characteristicConstraintLimits = new TreeMap();
    }
    return characteristicConstraintLimits;
  }

  public void setCharacteristicConstraintLimits( Map characteristicConstraintLimits )
  {
    this.characteristicConstraintLimits = characteristicConstraintLimits;
  }

  public List getNodeTypeIds()
  {
    return nodeTypeIds;
  }

  public void setNodeTypeIds( List nodeTypeIds )
  {
    this.nodeTypeIds = nodeTypeIds;
  }

}
