/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/MultiSelectCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import java.util.List;
import java.util.StringTokenizer;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.utils.ArrayUtil;

/**
 * MultiSelectCharacteristicConstraintLimits is a concrete CharacteristicConstraintLimits for MultiSelect type characteristics.
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
public class MultiSelectCharacteristicConstraintLimits extends SingleSelectCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{

  private boolean isAllof;

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType()
  {
    return CharacteristicDataType.MULTI_SELECT;
  }

  /**
   * @return a boolean indicating if the constraint should require all of the values in
   *        the valuesAllowed array.  If this is false then only one of the values is required.
   */
  public boolean isAllof()
  {
    return isAllof;
  }

  /**
   * @param isAllof a boolean indicating if the constraint should require all of the values in
   *        the valuesAllowed array.  If this is false then only one of the values is required.
   */
  public void setAllof( boolean isAllof )
  {
    this.isAllof = isAllof;
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
    if ( getValuesAllowed() == null )
    {
      return true;
    }
    String[] values = parseCharacteristicValues( characteristicValue );
    List valueList = ArrayUtil.stringArrayToList( values );
    for ( int i = 0; i < getValuesAllowed().length; i++ )
    {
      if ( valueList.contains( getValuesAllowed()[i] ) )
      {
        if ( !isAllof() )
        {
          return true;
        }
      }
      else
      {
        if ( isAllof() )
        {
          return false;
        }
      }
    }
    if ( isAllof() )
    {
      return true;
    }
    return false;
  }

  /**
   * @param values
   * @return String array of the characteristic values
   */
  private static String[] parseCharacteristicValues( String values )
  {
    String[] characteristicValues;

    if ( values.indexOf( "," ) != -1 )
    {
      StringTokenizer tokens = new StringTokenizer( values, "," );
      characteristicValues = new String[tokens.countTokens()];
      int i = 0;
      while ( tokens.hasMoreTokens() )
      {
        characteristicValues[i] = tokens.nextToken();
        i++;
      }
    }
    else
    {
      characteristicValues = new String[1];
      characteristicValues[0] = values;
    }

    return characteristicValues;
  }

}
