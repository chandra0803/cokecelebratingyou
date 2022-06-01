/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/SingleSelectCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;

/**
 * SingleSelectCharacteristicConstraintLimits is a concrete CharacteristicConstraintLimits for SingleSelect type characteristics.
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
public class SingleSelectCharacteristicConstraintLimits extends AbstractCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{

  private String[] valuesAllowed;

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType()
  {
    return CharacteristicDataType.SINGLE_SELECT;
  }

  /**
   * @return a String[] constaining the values allowed.  This should be null if no limit.
   */
  public String[] getValuesAllowed()
  {
    return valuesAllowed;
  }

  /**
   * @param valuesAllowed a String[] containing the values allowed.  This should be null if no limit.
   */
  public void setValuesAllowed( String[] valuesAllowed )
  {
    this.valuesAllowed = valuesAllowed;
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
    return doInConstraints( userCharacteristic.getCharacteristicValue() );
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
    return doInConstraints( nodeCharacteristic.getCharacteristicValue() );
  }

  /**
   * Checks if the userCharacteristic passes the constraint
   * @param characteristicValue to check 
   * @return true if characteristicValue falls in the constraints; false if not
   */
  public boolean doInConstraints( String characteristicValue )
  {
    if ( characteristicValue == null )
    {
      return false;
    }
    if ( getValuesAllowed() != null )
    {
      for ( int i = 0; i < getValuesAllowed().length; i++ )
      {
        if ( characteristicValue.equals( getValuesAllowed()[i] ) )
        {
          return true;
        }
      }
      return false;
    }
    return true;
  }

}
