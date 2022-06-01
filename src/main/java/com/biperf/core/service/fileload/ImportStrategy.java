/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/ImportStrategy.java,v $
 */

package com.biperf.core.service.fileload;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecord;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.fileload.ParticipantImportRecord;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.MailingBatchHolder;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/*
 * ImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep 9, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public abstract class ImportStrategy
{
  public static final String HIERARCHY_CHARACTERISTIC_VALUE = "hierarchy.hierarchylabel.CHARACTERISTIC_VALUE";

  /**
   * Imports the specified import file records.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @throws ServiceErrorException
   */
  public abstract void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException;

  /**
   * Imports the specified import file records.  This method fullfills the batch mailing ability,
   * which by default - isn't there. Subclasses should implement this method if they wish
   * to leverage the batch email functionality.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @param the batch mailing holder.
   * @throws ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records, MailingBatchHolder mailingBatchHolder ) throws ServiceErrorException
  {
    importImportFile( importFile, records );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @throws ServiceErrorException 
   */
  public abstract void verifyImportFile( ImportFile importFile, List records ) throws ServiceErrorException;

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @param validateNodeRelationship (justForPaxRightNow)
   * @throws ServiceErrorException
   */
  public abstract void verifyImportFile( ImportFile importFile, List records, boolean validateNodeRelationship ) throws ServiceErrorException;

  /**
   * Creates ImportRecordErrors from a collection of ServiceErrors, and associates it with the
   * importRecord.
   * 
   * @param file
   * @param record
   * @param errors
   */
  public static void createAndAddImportRecordErrors( ImportFile file, ImportRecord record, Collection errors )
  {
    for ( Iterator iterator = errors.iterator(); iterator.hasNext(); )
    {
      ServiceError error = (ServiceError)iterator.next();
      createAndAddImportRecordError( file, record, error );
    }
  }

  /**
   * Creates an ImportRecordError from a ServiceError, and associates it with the importRecord.
   * 
   * @param file
   * @param record
   * @param error
   */
  public static void createAndAddImportRecordError( ImportFile file, ImportRecord record, ServiceError error )
  {
    ImportRecordError importError = new ImportRecordError();
    importError.setItemKey( error.getKey() );
    importError.setParam1( error.getArg1() );
    importError.setParam2( error.getArg2() );
    importError.setParam3( error.getArg3() );
    file.addImportRecordError( importError, record );
  }

  // A simple helper method to reduce line count in the validate methods
  protected void checkRequired( Object field, String cmKeyName, Collection errors )
  {
    ServiceError error = checkRequired( field, cmKeyName );
    if ( error != null )
    {
      errors.add( error );
    }
  }

  // A simple helper method to reduce line count in the validate methods
  // Note that when ServiceErrors are created, we translate parameter names at this time
  protected ServiceError checkRequired( Object field, String cmKeyName )
  {
    ServiceError error = null;
    if ( field == null )
    {
      error = new ServiceError( "system.errors.REQUIRED", getCmValue( cmKeyName ) );
    }
    else if ( field instanceof String && ( (String)field ).trim().equals( "" ) )
    {
      error = new ServiceError( "system.errors.REQUIRED", getCmValue( cmKeyName ) );
    }
    return error;
  }

  // Another simple helper method to reduce line count in the validate methods
  protected static String getCmValue( String cmKeyName )
  {
    return CmsResourceBundle.getCmsBundle().getString( cmKeyName );
  }

  /**
   * This checks to be sure both parameters are either null, or both are non null. If one is null
   * and the other isn't, it will return an error.
   * 
   * @param field1
   * @param cmkey1
   * @param field2
   * @param cmkey2
   * @param errors
   */

  public static void checkBothRequiredOrBothEmpty( Object field1, String cmkey1, Object field2, String cmkey2, Collection errors )
  {
    // if they are both null, return
    if ( field1 == null && field2 == null )
    {
      return;
    }

    // if they are both not-null, return
    if ( field1 != null && field2 != null )
    {
      return;
    }

    // this means one is null and one isn't. Add an error
    errors.add( new ServiceError( ServiceErrorMessageKeys.DEPENDENCY_REQUIRED, getCmValue( cmkey1 ), getCmValue( cmkey2 ) ) );
  }

  /**
   * This checks to be sure both parameters are either null, or all 3 are non null. If one is null
   * and another isn't, it will return an error.
   * 
   * @param field1
   * @param field2
   * @param errorKey
   * @param errors
   */

  public static void checkAllRequiredOrAllEmpty( String field1, Long field2, String errorKey, Collection errors )
  {
    // if they are all null, return
    if ( StringUtils.isEmpty( field1 ) && field2 == null )
    {
      return;
    }

    // if they are all not-null, return
    if ( !StringUtils.isEmpty( field1 ) && field2 != null )
    {
      return;
    }

    // this means one is null and another one isn't. Add an error
    errors.add( new ServiceError( errorKey, field1 ) );

  }

  /**
   * This checks to be sure all 3 parameters are either null, or all 3 are non null. If one is null
   * and another isn't, it will return an error.
   * 
   * @param field1
   * @param field2
   * @param field3
   * @param errorKey
   * @param errors
   */

  public static void checkAllRequiredOrAllEmpty( String field1, String field2, Object field3, String errorKey, Collection errors )
  {
    // if they are all null, return
    if ( StringUtils.isEmpty( field1 ) && StringUtils.isEmpty( field2 ) && field3 == null )
    {
      return;
    }

    // if they are all not-null, return
    if ( !StringUtils.isEmpty( field1 ) && !StringUtils.isEmpty( field2 ) && field3 != null )
    {
      return;
    }

    // this means one is null and another one isn't. Add an error
    errors.add( new ServiceError( errorKey, field1 ) );

  }

  /**
   * validates the characteristics, if there is an error then it will add the error to the
   * importFile and return a 1 (for an error count). If there is no error, it will return a 0. See
   * other signitures of this method for other options
   * 
   * @param characteristic
   * @param value
   * @param importFile
   * @param record
   * @return int
   */
  protected int validateCharacteristic( Characteristic characteristic, String value, ImportFile importFile, ImportRecord record )
  {
    ServiceError error = validateCharacteristic( characteristic, value, record );
    if ( error != null )
    {
      createAndAddImportRecordError( importFile, record, error );
      return 1;
    }
    return 0;
  }

  /**
   * Returns a ServiceError if there is a problem with the characteristic validation. Returns null
   * if there is no error.
   * 
   * @param characteristic
   * @param value
   * @param record
   * @return ServiceError
   */
  protected ServiceError validateCharacteristic( Characteristic characteristic, String value, ImportRecord record )
  {
    if ( characteristic.getIsRequired().booleanValue() && ( value == null || value.trim().equals( "" ) ) )
    {
      return new ServiceError( "system.errors.REQUIRED", HIERARCHY_CHARACTERISTIC_VALUE );
    }
    // Already checked required fields, if a value is not entered, no need to do any
    // more validation.
    if ( value != null && !value.trim().equals( "" ) )
    {
      // Text fields cannot have a length > than the maxSize specified.
      if ( characteristic.getMaxSize() != null && characteristic.getMaxSize().longValue() > 0 )
      {
        if ( value.length() > characteristic.getMaxSize().intValue() )
        {
          return new ServiceError( "user.characteristic.errors.SIZE", HIERARCHY_CHARACTERISTIC_VALUE );
        }
      }

      // Boolean fields must be true or false
      if ( characteristic.getCharacteristicDataType().getCode().equals( CharacteristicDataType.BOOLEAN ) )
      {
        if ( !value.equalsIgnoreCase( "true" ) && !value.equalsIgnoreCase( "false" ) )
        {
          return new ServiceError( "user.characteristic.errors.BOOLEAN", HIERARCHY_CHARACTERISTIC_VALUE );
        }
      }

      // Integer fields should be integers and if there is a minimum and maximum
      // value specified, the characteristic value should be within that range.
      if ( characteristic.getCharacteristicDataType().getCode().equals( CharacteristicDataType.INTEGER ) )
      {
        int intValue = 0;
        try
        {
          intValue = Integer.parseInt( value );
        }
        catch( NumberFormatException e )
        {
          return new ServiceError( "user.characteristic.errors.INTEGER", HIERARCHY_CHARACTERISTIC_VALUE );
        }

        if ( characteristic.getMinValue() != null )
        {
          if ( intValue < characteristic.getMinValue().intValue() )
          {
            return new ServiceError( "user.characteristic.errors.RANGE", HIERARCHY_CHARACTERISTIC_VALUE, characteristic.getMinValue().toString(), characteristic.getMaxValue().toString() );
          }
        }
        if ( characteristic.getMaxValue() != null )
        {
          if ( intValue > characteristic.getMaxValue().intValue() )
          {
            return new ServiceError( "user.characteristic.errors.RANGE", HIERARCHY_CHARACTERISTIC_VALUE, characteristic.getMinValue().toString(), characteristic.getMaxValue().toString() );
          }
        }
      }
      // Decimal fields should be decimal, and if there is a minimum and maximum
      // value specified, the characteristic value should be within that range.
      if ( characteristic.getCharacteristicDataType().getCode().equals( CharacteristicDataType.DECIMAL ) )
      {
        BigDecimal decimalValue = null;
        try
        {
          decimalValue = new BigDecimal( value );
        }
        catch( NumberFormatException nfe )
        {
          return new ServiceError( "user.characteristic.errors.DECIMAL", HIERARCHY_CHARACTERISTIC_VALUE );
        }

        if ( characteristic.getMinValue() != null )
        {
          if ( decimalValue.compareTo( characteristic.getMinValue() ) < 0 )
          {
            return new ServiceError( "user.characteristic.errors.RANGE", HIERARCHY_CHARACTERISTIC_VALUE, characteristic.getMinValue().toString(), characteristic.getMaxValue().toString() );
          }
        }
        if ( characteristic.getMaxValue() != null )
        {
          if ( decimalValue.compareTo( characteristic.getMaxValue() ) > 0 )
          {
            return new ServiceError( "user.characteristic.errors.RANGE", HIERARCHY_CHARACTERISTIC_VALUE, characteristic.getMinValue().toString(), characteristic.getMaxValue().toString() );
          }
        }
      }
      // Date fields should be a valid date, and if there is a date start and date
      // end value, the characteristic value should be within that range.
      if ( characteristic.getCharacteristicDataType().getCode().equals( CharacteristicDataType.DATE ) )
      {
        Date dateValue = DateUtils.toDate( value );

        if ( dateValue == null )
        {
          return new ServiceError( "user.characteristic.errors.DATE_INVALID", HIERARCHY_CHARACTERISTIC_VALUE );
        }

        if ( characteristic.getDateStart() != null )
        {
          Date startDate = characteristic.getDateStart();
          if ( dateValue.before( startDate ) )
          {
            return new ServiceError( "user.characteristic.errors.DATE_RANGE", HIERARCHY_CHARACTERISTIC_VALUE, characteristic.getDateStart().toString(), characteristic.getDateEnd().toString() );
          }
        }
        if ( characteristic.getDateEnd() != null )
        {
          Date endDate = characteristic.getDateEnd();
          if ( dateValue.after( endDate ) )
          {
            return new ServiceError( "user.characteristic.errors.DATE_RANGE", HIERARCHY_CHARACTERISTIC_VALUE, characteristic.getDateStart().toString(), characteristic.getDateEnd().toString() );
          }
        }
      }
    } // if characteristicValue is not null
    return null;
  }

  /**
   * Validates a characteristic, and adds a ServiceError to the errors collection
   * 
   * @param characteristic
   * @param value
   * @param record
   * @param errors
   */
  protected void validateCharacteristic( Characteristic characteristic, String value, ParticipantImportRecord record, Collection errors )
  {
    ServiceError error = validateCharacteristic( characteristic, value, record );
    if ( error != null )
    {
      errors.add( error );
    }
  }
}
