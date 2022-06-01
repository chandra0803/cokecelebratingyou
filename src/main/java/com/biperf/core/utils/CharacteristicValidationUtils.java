/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/CharacteristicValidationUtils.java,v $
 */

package com.biperf.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.value.CharacteristicValueBean;

/**
 * CharacteristicUtils - Utility class for maintaining the UserCharacteristics.
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
 * <td>zahler</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CharacteristicValidationUtils
{

  public static List validateProductCharacteristicValueList( ProductClaim claim )
  {
    List characteristicValueBeanList = new ArrayList();

    for ( Iterator iter = claim.getClaimProducts().iterator(); iter.hasNext(); )
    {
      ClaimProduct claimProduct = (ClaimProduct)iter.next();
      Set claimProductCharacteristics = claimProduct.getClaimProductCharacteristics();
      for ( Iterator iterator = claimProductCharacteristics.iterator(); iterator.hasNext(); )
      {
        ClaimProductCharacteristic claimProductCharacteristic = (ClaimProductCharacteristic)iterator.next();

        CharacteristicValueBean valueBean = new CharacteristicValueBean();
        loadCharacteristicValueBeanData( valueBean, claimProductCharacteristic.getProductCharacteristicType(), null );
        valueBean.setCharacteristicValue( claimProductCharacteristic.getValue() );
        characteristicValueBeanList.add( valueBean );
      }
    }

    return validateCharacteristicValueList( characteristicValueBeanList );
  }

  /**
   * Validate the values entered for characteristics.
   * 
   * @param characteristicValueList is a list of CharacteristicValueBeans
   */
  public static List validateCharacteristicValueList( List characteristicValueList )
  {
    List validationErrors = new ArrayList();
    // Validate characteristic value based on meta data fields for
    // the characteristicType.
    if ( characteristicValueList != null && characteristicValueList.size() > 0 )
    {
      CharacteristicValueBean valueInfo = null;

      String characteristicValue = "";

      for ( int i = 0; i < characteristicValueList.size(); i++ )
      {
        valueInfo = getValueInfo( i, characteristicValueList );

        characteristicValue = valueInfo.getCharacteristicValue();

        // Check first to see if this characteristic is required.
        if ( valueInfo.getIsRequired().booleanValue() )
        {
          // If the characteristicType is a multi_select list, the values are
          // passed in the String[] characteristicValues, so that is what should
          // be checked.
          if ( valueInfo.getCharacteristicDataType().equals( "multi_select" ) )
          {
            if ( valueInfo.getCharacteristicValues() == null || valueInfo.getCharacteristicValues().length < 1 )
            {
              validationErrors.add( new ServiceError( "user.characteristic.errors.REQUIRED", valueInfo.getCharacteristicName() ) );

              break;
            }
          }
          // Everything else is passed in the String characteristicValue.
          else
          {
            if ( characteristicValue == null || characteristicValue.equals( "" ) )
            {
              validationErrors.add( new ServiceError( "user.characteristic.errors.REQUIRED", valueInfo.getCharacteristicName() ) );
              break;
            }
          }
        }

        // Already checked required fields, if a value is not entered, no need to do any
        // more validation.
        if ( characteristicValue != null && !characteristicValue.equals( "" ) )
        {
          // Text fields cannot have a length > than the maxSize specified.
          if ( valueInfo.getMaxSize() != null && valueInfo.getMaxSize().longValue() > 0 )
          {
            if ( characteristicValue.length() > valueInfo.getMaxSize().intValue() )
            {
              validationErrors.add( new ServiceError( "user.characteristic.errors.SIZE", valueInfo.getCharacteristicName() ) );
              break;
            }
          }

          // Integer fields should be integers and if there is a minimum and maximum
          // value specified, the characteristic value should be within that range.
          if ( valueInfo.getCharacteristicDataType().equals( "int" ) )
          {
            int intValue;
            try
            {
              intValue = Integer.parseInt( characteristicValue );
            }
            catch( NumberFormatException e )
            {
              validationErrors.add( new ServiceError( "user.characteristic.errors.INTEGER", valueInfo.getCharacteristicName() ) );
              break;
            }

            if ( !StringUtils.isBlank( valueInfo.getMinValue() ) && !StringUtils.isBlank( valueInfo.getMaxValue() ) )
            {
              // TODO: Can there just be a min value OR a MAX value, not both???

              int minValue = Integer.parseInt( valueInfo.getMinValue() );
              int maxValue = Integer.parseInt( valueInfo.getMaxValue() );
              if ( intValue < minValue || intValue > maxValue )
              {
                validationErrors.add( new ServiceError( "user.characteristic.errors.RANGE", valueInfo.getCharacteristicName(), valueInfo.getMinValue(), valueInfo.getMaxValue() ) );
                break;
              }
            }
          }
          // Decimal fields should be decimal, and if there is a minimum and maximum
          // value specified, the characteristic value should be within that range.
          if ( valueInfo.getCharacteristicDataType().equals( "decimal" ) )
          {
            BigDecimal decimalValue;
            try
            {
              decimalValue = new BigDecimal( characteristicValue );
            }
            catch( NumberFormatException nfe )
            {
              validationErrors.add( new ServiceError( "user.characteristic.errors.DECIMAL", valueInfo.getCharacteristicName() ) );
              break;
            }

            if ( !StringUtils.isBlank( valueInfo.getMinValue() ) && !StringUtils.isBlank( valueInfo.getMaxValue() ) )
            {
              // TODO: Can there just be a min value OR a MAX value, not both???

              BigDecimal minValue = new BigDecimal( valueInfo.getMinValue() );
              BigDecimal maxValue = new BigDecimal( valueInfo.getMaxValue() );
              if ( decimalValue.compareTo( minValue ) < 0 || decimalValue.compareTo( maxValue ) > 0 )
              {
                validationErrors.add( new ServiceError( "user.characteristic.errors.RANGE", valueInfo.getCharacteristicName(), valueInfo.getMinValue(), valueInfo.getMaxValue() ) );
                break;
              }
            }
          }
          // Date fields should be a valid date, and if there is a date start and date
          // end value, the characteristic value should be within that range.
          if ( valueInfo.getCharacteristicDataType().equals( "date" ) )
          {
            Date dateValue = DateUtils.toDate( characteristicValue );

            if ( dateValue == null )
            {
              validationErrors.add( new ServiceError( "user.characteristic.errors.DATE_INVALID", valueInfo.getCharacteristicName(), valueInfo.getDateStart(), valueInfo.getDateEnd() ) );
              break;
            }

            if ( valueInfo.getDateStart() != null && !valueInfo.getDateStart().equals( "" ) )
            {
              Date startDate = DateUtils.toDate( valueInfo.getDateStart() );
              if ( dateValue.before( startDate ) )
              {
                validationErrors.add( new ServiceError( "user.characteristic.errors.DATE_RANGE", valueInfo.getCharacteristicName(), valueInfo.getDateStart(), valueInfo.getDateEnd() ) );
                break;
              }
            }
            if ( valueInfo.getDateEnd() != null && !valueInfo.getDateEnd().equals( "" ) )
            {
              Date endDate = DateUtils.toDate( valueInfo.getDateEnd() );
              if ( dateValue.after( endDate ) )
              {
                validationErrors.add( new ServiceError( "user.characteristic.errors.DATE_RANGE", valueInfo.getCharacteristicName(), valueInfo.getDateStart(), valueInfo.getDateEnd() ) );
                break;
              }
            }
          } // if type=date
        } // if characteristicValue is not null

      } // for
    } // if valueList != null

    return validationErrors;
  }

  /**
   * @param valueBean
   * @param characteristic
   */
  public static void loadCharacteristicValueBeanData( CharacteristicValueBean valueBean, Characteristic characteristic, UUID rosterId )
  {
    valueBean.setCharacteristicId( characteristic.getId() );
    if ( characteristic.getId() != null )
    {
      valueBean.setCharacteristicName( characteristic.getCharacteristicName() );
    }
    
    if ( rosterId != null )
    {
      valueBean.setRosterId( rosterId.toString() );
    }
    
    if ( characteristic.getCharacteristicDataType() != null )
    {
      valueBean.setCharacteristicDataType( characteristic.getCharacteristicDataType().getCode() );
    }
    valueBean.setMaxSize( characteristic.getMaxSize() );
    BigDecimal maxValue = characteristic.getMaxValue();
    valueBean.setMaxValue( maxValue != null ? maxValue.toString() : "" );
    BigDecimal minValue = characteristic.getMinValue();
    valueBean.setMinValue( minValue != null ? minValue.toString() : "" );
    valueBean.setDateStart( DateUtils.toDisplayString( characteristic.getDateStart() ) );
    valueBean.setDateEnd( DateUtils.toDisplayString( characteristic.getDateEnd() ) );
    valueBean.setPlName( characteristic.getPlName() );
    valueBean.setIsRequired( characteristic.getIsRequired() );
    if ( characteristic.getAuditCreateInfo().getCreatedBy() != null )
    {
      valueBean.setCreatedBy( characteristic.getAuditCreateInfo().getCreatedBy().toString() );
    }
    valueBean.setCmAssetCode( characteristic.getCmAssetCode() );
    valueBean.setNameCmKey( characteristic.getNameCmKey() );
    if ( characteristic.getAuditCreateInfo().getDateCreated() != null )
    {
      valueBean.setDateCreated( characteristic.getAuditCreateInfo().getDateCreated().getTime() );
    }
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @param valueList
   * @return Single instance of CharacteristicFormBean from the value list
   */
  private static CharacteristicValueBean getValueInfo( int index, List valueList )
  {
    try
    {
      return (CharacteristicValueBean)valueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

}
