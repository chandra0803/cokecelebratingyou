/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/TextCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;

/**
 * TextCharacteristicConstraintLimits is a concrete CharacteristicConstraintLimits for Text type characteristics.
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
public class TextCharacteristicConstraintLimits extends AbstractCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{
  public static final int TYPE_STARTS_WITH = 1;
  public static final int TYPE_DOES_NOT_START_WITH = 2;
  public static final int TYPE_CONTAINS = 3;
  public static final int TYPE_DOES_NOT_CONTAIN = 4;
  public static final int TYPE_ENDS_WITH = 5;
  public static final int TYPE_DOES_NOT_END_WITH = 6;

  private String textValue;
  private int type;

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType()
  {
    return CharacteristicDataType.TEXT;
  }

  /**
   * @return
   */
  public String getTextValue()
  {
    return textValue;
  }

  /**
   * @param textValue
   */
  public void setTextValue( String textValue )
  {
    this.textValue = textValue;
  }

  /**
   * @return
   */
  public int getType()
  {
    return type;
  }

  /**
   * @param type Should be set to one of the static final types for the compare type to do
   */
  public void setType( int type )
  {
    this.type = type;
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
  private boolean doInConstraints( String characteristicValue )
  {
    if ( characteristicValue == null )
    {
      return false;
    }
    if ( getTextValue() == null )
    {
      return true;
    }
    switch ( getType() )
    {
      case TYPE_CONTAINS:
        return characteristicValue.toLowerCase().indexOf( getTextValue().toLowerCase() ) >= 0;
      case TYPE_DOES_NOT_CONTAIN:
        return characteristicValue.toLowerCase().indexOf( getTextValue().toLowerCase() ) < 0;
      case TYPE_STARTS_WITH:
        return characteristicValue.toLowerCase().startsWith( getTextValue().toLowerCase() );
      case TYPE_DOES_NOT_START_WITH:
        return !characteristicValue.toLowerCase().startsWith( getTextValue().toLowerCase() );
      case TYPE_ENDS_WITH:
        return characteristicValue.toLowerCase().endsWith( getTextValue().toLowerCase() );
      case TYPE_DOES_NOT_END_WITH:
        return !characteristicValue.toLowerCase().endsWith( getTextValue().toLowerCase() );
      default:
        return true;
    }
  }
}
