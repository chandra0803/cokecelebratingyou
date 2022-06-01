/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/budget/limits/DecimalCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import java.math.BigDecimal;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;

/**
 * DecimalCharacteristicConstraintLimits is a concrete CharacteristicConstraintLimits for Decimal type characteristics.
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
public class DecimalCharacteristicConstraintLimits extends AbstractCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{

  private BigDecimal minDecimalValue;
  private BigDecimal maxDecimalValue;

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType()
  {
    return CharacteristicDataType.DECIMAL;
  }

  /**
   * @return an BigDecimal containing the maximum decimal value allowed.  This should be null if no limit.
   */
  public BigDecimal getMaxDecimalValue()
  {
    return maxDecimalValue;
  }

  /**
   * @param maxDecimalValue an BigDecimal containing the maximum decimal value allowed.  This should be null if no limit.
   */
  public void setMaxDecimalValue( BigDecimal maxDecimalValue )
  {
    this.maxDecimalValue = maxDecimalValue;
  }

  /**
   * @return an BigDecimal containing the minimum decimal value allowed.  This should be null if no limit.
   */
  public BigDecimal getMinDecimalValue()
  {
    return minDecimalValue;
  }

  /**
   * @param minDecimalValue an BigDecimal containing the minimum decimal value allowed.  This should be null if no limit.
   */
  public void setMinDecimalValue( BigDecimal minDecimalValue )
  {
    this.minDecimalValue = minDecimalValue;
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
    BigDecimal decimalValue;
    try
    {
      decimalValue = new BigDecimal( characteristicValue );
    }
    catch( NumberFormatException e )
    {
      return false;
    }
    if ( getMinDecimalValue() != null )
    {
      if ( decimalValue.compareTo( getMinDecimalValue() ) < 0 )
      {
        return false;
      }
    }
    if ( getMaxDecimalValue() != null )
    {
      if ( decimalValue.compareTo( getMaxDecimalValue() ) > 0 )
      {
        return false;
      }
    }
    return true;
  }

}
