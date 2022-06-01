/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/CharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;

/**
 * CharacteristicConstraintLimits is the interface to be used by constraints on characteristics used in the 
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
public interface CharacteristicConstraintLimits
{

  /**
   * @return a Long containing the CharacteristicId for this constraint.
   */
  public Long getCharacteristicId();

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType();

  /**
   * Checks if the characteristic passes the constraint
   * @param userCharacteristic to check 
   * @return true if characteristic falls in the constraints; false if not
   */
  public boolean inConstraints( UserCharacteristic userCharacteristic );

  /**
   * Checks if the characteristic passes the constraint
   * @param nodeTypeId a Long containing the node type id of the item 
   * @param nodeCharacteristic to check 
   * @return true if characteristic falls in the constraints; false if not
   */
  public boolean inConstraints( Long nodeTypeId, NodeCharacteristic nodeCharacteristic );

}
