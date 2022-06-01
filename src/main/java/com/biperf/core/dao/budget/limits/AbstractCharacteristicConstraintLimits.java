/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/AbstractCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;

/**
 * AbstractCharacteristicConstraintLimits is an abstract for the CharacteristicConstraintLimits used by the 
 * BudgetQueryConstraint.

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
public abstract class AbstractCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{

  private Long characteristicId;
  private Long nodeTypeId;

  /**
   * @return a Long containing the id of the characteristic to be constrained
   */
  public Long getCharacteristicId()
  {
    return characteristicId;
  }

  /**
   * @param characteristicId
   */
  public void setCharacteristicId( Long characteristicId )
  {
    this.characteristicId = characteristicId;
  }

  /**
   * @return
   */
  public Long getNodeTypeId()
  {
    return nodeTypeId;
  }

  /**
   * @param nodeType
   */
  public void setNodeTypeId( Long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  /**
   * Checks if the characteristic passes the constraint
   * @param userCharacteristic to check 
   * @return true if characteristic falls in the constraints; false if not
   */
  public boolean inConstraints( UserCharacteristic userCharacteristic )
  {
    return doInConstraints( userCharacteristic );
  }

  /**
   * Checks if the characteristic passes the constraint
   * @param nodeCharacteristic to check 
   * @return true if characteristic falls in the constraints; false if not
   */
  public boolean inConstraints( Long nodeTypeId, NodeCharacteristic nodeCharacteristic )
  {
    if ( this.nodeTypeId.equals( nodeTypeId ) )
    {
      return doInConstraints( nodeCharacteristic );
    }
    return true;
  }

  /**
   * Checks if the characteristic passes the constraint
   * @param userCharacteristic to check 
   * @return true if characteristic falls in the constraints; false if not
   */
  abstract public boolean doInConstraints( UserCharacteristic userCharacteristic );

  /**
   * Checks if the characteristic passes the constraint
   * @param nodeCharacteristic to check 
   * @return true if characteristic falls in the constraints; false if not
   */
  abstract public boolean doInConstraints( NodeCharacteristic nodeCharacteristic );

}
