/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/budget/limits/DateCharacteristicConstraintLimits.java,v $
 */

package com.biperf.core.dao.budget.limits;

import java.util.Date;
import java.util.Locale;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.utils.DateUtils;

/**
 * DateCharacteristicConstraintLimits is a concrete CharacteristicConstraintLimits for Date type characteristics.
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
public class DateCharacteristicConstraintLimits extends AbstractCharacteristicConstraintLimits implements CharacteristicConstraintLimits
{

  private Date minDateValue;
  private Date maxDateValue;

  /**
   * @return a String containing the characteristic type.  This should be a valid type from CharacteristicDataType.
   */
  public String getCharacteristicDataType()
  {
    return CharacteristicDataType.DATE;
  }

  /**
   * @return a Date containing the maximum date value allowed.  This should be null if no limit.
   */
  public Date getMaxDateValue()
  {
    return maxDateValue;
  }

  /**
   * @param maxDateValue a Date containing the maximum date value allowed.  This should be null if no limit.
   */
  public void setMaxDateValue( Date maxDateValue )
  {
    this.maxDateValue = maxDateValue;
  }

  /**
   * @return a Date containing the minimum date value allowed.  This should be null if no limit.
   */
  public Date getMinDateValue()
  {
    return minDateValue;
  }

  /**
   * @param minDateValue a Date containing the minimum date value allowed.  This should be null if no limit.
   */
  public void setMinDateValue( Date minDateValue )
  {
    this.minDateValue = minDateValue;
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
    Date dateValue;
    dateValue = DateUtils.toDate( characteristicValue, Locale.US );
    if ( dateValue == null )
    {
      return false;
    }
    if ( getMinDateValue() != null )
    {
      if ( dateValue.before( getMinDateValue() ) )
      {
        return false;
      }
    }
    if ( getMaxDateValue() != null )
    {
      if ( dateValue.after( getMaxDateValue() ) )
      {
        return false;
      }
    }
    return true;
  }

}
