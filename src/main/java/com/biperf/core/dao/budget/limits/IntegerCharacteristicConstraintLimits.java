/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/IntegerCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;

/**
 * IntegerCharacteristicConstraintLimits is a concrete CharacteristicConstraintLimits for Integer type characteristics.
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
public class IntegerCharacteristicConstraintLimits extends AbstractCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{

  private Integer minIntegerValue;
  private Integer maxIntegerValue;

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType()
  {
    return CharacteristicDataType.INTEGER;
  }

  /**
   * @return an Integer containing the maximum integer value allowed.  This should be null if no limit.
   */
  public Integer getMaxIntegerValue()
  {
    return maxIntegerValue;
  }

  /**
   * @param maxIntegerValue an Integer containing the maximum integer value allowed.  This should be null if no limit.
   */
  public void setMaxIntegerValue( Integer maxIntegerValue )
  {
    this.maxIntegerValue = maxIntegerValue;
  }

  /**
   * @return an Integer containing the minimum integer value allowed.  This should be null if no limit.
   */
  public Integer getMinIntegerValue()
  {
    return minIntegerValue;
  }

  /**
   * @param minIntegerValue an Integer containing the minimum integer value allowed.  This should be null if no limit.
   */
  public void setMinIntegerValue( Integer minIntegerValue )
  {
    this.minIntegerValue = minIntegerValue;
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
    int intValue;
    try
    {
      intValue = Integer.parseInt( characteristicValue );
    }
    catch( NumberFormatException e )
    {
      return false;
    }
    if ( getMinIntegerValue() != null )
    {
      if ( intValue < getMinIntegerValue().intValue() )
      {
        return false;
      }
    }
    if ( getMaxIntegerValue() != null )
    {
      if ( intValue > getMaxIntegerValue().intValue() )
      {
        return false;
      }
    }
    return true;
  }

}
