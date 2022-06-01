/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/BooleanCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import org.apache.commons.lang3.BooleanUtils;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;

/**
 * BooleanCharacteristicConstraintLimits is a concrete CharacteristicConstraintLimits for Boolean type characteristics.
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
public class BooleanCharacteristicConstraintLimits extends AbstractCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{

  private Boolean booleanValue;

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType()
  {
    return CharacteristicDataType.BOOLEAN;
  }

  /**
   * @return a Boolean containing the value allowed.  This should be null if no limit.
   */
  public Boolean getBooleanValue()
  {
    return booleanValue;
  }

  /**
   * @param booleanValue an Boolean containing the value allowed.  This should be null if no limit.
   */
  public void setBooleanValue( Boolean booleanValue )
  {
    this.booleanValue = booleanValue;
  }

  /**
   * Checks if the userCharacteristic passes the constraint
   * @param userCharacteristic to check 
   * @return true if userCharacteristic falls in the constraints; false if not
   */
  public boolean doInConstraints( UserCharacteristic userCharacteristic )
  {
    if ( userCharacteristic == null )
    {
      return false;
    }
    return inConstraints( userCharacteristic.getCharacteristicValue() );
  }

  /**
   * Checks if the nodeCharacteristic passes the constraint
   * @param nodeCharacteristic to check 
   * @return true if userCharacteristic falls in the constraints; false if not
   */
  public boolean doInConstraints( NodeCharacteristic nodeCharacteristic )
  {
    if ( nodeCharacteristic == null )
    {
      return false;
    }
    return inConstraints( nodeCharacteristic.getCharacteristicValue() );
  }

  /**
   * Checks if the userCharacteristic passes the constraint
   * @param characteristicValue to check 
   * @return true if characteristicValue falls in the constraints; false if not
   */
  private boolean inConstraints( String characteristicValue )
  {
    if ( characteristicValue == null )
    {
      return false;
    }
    boolean aBooleanValue;
    aBooleanValue = BooleanUtils.toBoolean( characteristicValue );
    if ( getBooleanValue() != null )
    {
      return aBooleanValue == getBooleanValue().booleanValue();
    }
    return true;
  }

}
