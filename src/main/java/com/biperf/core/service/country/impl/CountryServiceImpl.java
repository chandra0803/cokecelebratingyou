/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/country/impl/CountryServiceImpl.java,v $
 */

package com.biperf.core.service.country.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.transaction.Transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.biperf.awardslinqDataRetriever.delegate.AwardslinqFeaturedItemsDelegate;
import com.biperf.awardslinqDataRetriever.hibernate.entity.Program;
import com.biperf.cache.Cache;
import com.biperf.cache.CacheFactory;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.value.CountryValueBean;

/**
 * CountryServiceImpl.
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
 * <td>sedey</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CountryServiceImpl implements CountryService
{
  private static final Log logger = LogFactory.getLog( CountryServiceImpl.class );
  private static final String COUNTRY_NAMES_LIST = "COUNTRY_NAMES_LIST";

  private static final String COUNTRY_DATA_SECTION_NAME = "country_data";
  private static final String COUNTRY_NAME_KEY = "COUNTRY_NAME";
  private static final String COUNTRY_ASSET_NAME_SUFFIX = " Data";
  private static final String COUNTRY_ASSET_TYPE_NAME = "_CountryDataType";
  private static final String COUNTRY_DATA_ASSET_PREFIX = "country_data.country.";

  /** CountryDAO * */
  private CountryDAO countryDAO;

  /** SupplierDAO * */
  private SupplierDAO supplierDAO;

  private CMAssetService cmAssetService = null;

  private Cache cacheProgram;
  private Cache countryNamesList;

  private PlatformTransactionManager transactionManager;

  private AwardBanQServiceFactory awardBanQServiceFactory;

  /**
   * Set the CountryDAO through IoC
   * 
   * @param countryDAO
   */
  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }

  /**
   * Set the SupplierDAO through IoC
   * 
   * @param supplierDAO
   */
  public void setSupplierDAO( SupplierDAO supplierDAO )
  {
    this.supplierDAO = supplierDAO;
  }

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setCacheFactory( CacheFactory cacheFactory )
  {
    cacheProgram = cacheFactory.getCache( "countryProgram" );
    this.countryNamesList = cacheFactory.getCache( "countryNames" );
  }

  public List<String> getCountryNameList()
  {
    List<String> names = new CopyOnWriteArrayList<String>();// fast for reads..this should never
                                                            // really need to be updated frequently
    List<Country> countries = this.countryDAO.getAll();

    for ( Country country : countries )
    {
      names.add( country.getI18nCountryName().toLowerCase() );
    }
    // sort, cause why not?
    Collections.sort( names );
    this.countryNamesList.put( COUNTRY_NAMES_LIST, names );

    return names;
  }

  public void setTransactionManager( PlatformTransactionManager transactionManager )
  {
    this.transactionManager = transactionManager;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.country.CountryService#saveCountry(Long,com.biperf.core.domain.country.Country)
   * @param supplierId
   * @param countryToSave
   * @return Country
   * @throws ServiceErrorException
   */
  public Country saveCountry( Long supplierId, Country countryToSave ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();

    // Check if there is a duplicate countryCode
    if ( isDuplicateCode( countryToSave ) )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.COUNTRY_DUPLICATE_CODE );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors );
    }
    // Check if there is a duplicate awardbanqAbbrev
    if ( isDuplicateAwardbanqAbbrev( countryToSave ) )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.COUNTRY_DUPLICATE_AWARDBANQ_ABBREV );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors );
    }

    if ( countryToSave.getId() == null )
    {
      // new country - set the asset and key
      countryToSave.setNameCmKey( COUNTRY_NAME_KEY );
      countryToSave.setCmAssetCode( cmAssetService.getUniqueAssetCode( COUNTRY_DATA_ASSET_PREFIX ) );
    }

    // Set the dateStatus
    setDateStatus( countryToSave );

    // Long dummySupplierId = 161L;

    /*
     * // Get the supplier and add it to the country. Supplier supplier =
     * this.supplierDAO.getSupplierById( dummySupplierId ); countryToSave.setSupplier( supplier );
     */

    CMDataElement cmDataElement = new CMDataElement( "Country Name", countryToSave.getNameCmKey(), countryToSave.getCountryName(), true );
    try
    {
      cmAssetService.createOrUpdateAsset( COUNTRY_DATA_SECTION_NAME,
                                          COUNTRY_ASSET_TYPE_NAME,
                                          countryToSave.getCountryName() + COUNTRY_ASSET_NAME_SUFFIX,
                                          countryToSave.getCmAssetCode(),
                                          cmDataElement );
    }
    catch( NonUniqueDataServiceErrorException e )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.COUNTRY_DUPLICATE_NAME );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, e );
    }

    // Call the saveCountry method to save the record.
    countryToSave = this.countryDAO.saveCountry( countryToSave );

    // QC bug #3196 fix
    Double mediaRation = this.awardBanQServiceFactory.getAwardBanQService().getNullableMediaValueForCountry( countryToSave.getCountryCode() );
    if ( mediaRation != null && mediaRation != 0 )
    {
      countryToSave.setBudgetMediaValue( BigDecimal.valueOf( mediaRation ) );
    }
    return countryToSave;
  }

  /*
   * Checks to see if the country is a duplicate record based on the countryCode @param country
   * @return boolean
   */
  private boolean isDuplicateCode( Country country )
  {
    // Check to see if the country already exists in the database with the same code.
    // calls getCountryByCode to get a country record
    Country dbCountryByCode = this.countryDAO.getCountryByCode( country.getCountryCode() );

    if ( dbCountryByCode != null )
    {
      // if we found a record in the database with the given countryCode,
      // and our country ID is null, we are trying to insert a duplicate record.
      if ( country.getId() == null )
      {
        return true;
      }

      // if we found a record in the database with the given countryCode, but the
      // ID's are not equal, we are trying to update to a countryCode that already
      // exists.
      if ( dbCountryByCode.getId().compareTo( country.getId() ) != 0 )
      {
        return true;
      }
    }
    return false;
  }

  /*
   * Checks to see if the country is a duplicate record based on the AwardbanqAbbrev @param country
   * @return boolean
   */
  private boolean isDuplicateAwardbanqAbbrev( Country country )
  {
    // Check to see if the country already exists in the database with the same awardbanqAbbrev.
    // calls getCountryByAwardbanqAbbrev to get a country record
    Country dbCountryByAwardbanqAbbrev = this.countryDAO.getCountryByAwardbanqAbbrev( country.getAwardbanqAbbrev() );

    if ( dbCountryByAwardbanqAbbrev != null )
    {
      // if we found a record in the database with the given AwardbanqAbbrev,
      // and our country ID is null, we are trying to insert a duplicate record.
      if ( country.getId() == null )
      {
        return true;
      }

      // if we found a record in the database with the given AwardbanqAbbrev, but the
      // ID's are not equal, we are trying to update to a countryCode that already
      // exists.
      if ( dbCountryByAwardbanqAbbrev.getId().compareTo( country.getId() ) != 0 )
      {
        return true;
      }
    }
    return false;
  }

  /*
   * Handles setting the dateStatus @param country
   */
  private void setDateStatus( Country country )
  {
    // If the country ID is null, this is a new country record so just set the date
    // status with today's date.
    if ( country.getId() == null )
    {
      country.setDateStatus( new Date() );
    }
    else
    {
      // First call getCountryById to get the country record
      Country dbCountryById = this.countryDAO.getCountryById( country.getId() );

      // compare the status of the country and the dbCountryById. If they are
      // different, then set the dateStatus to the current date, otherwise
      // set the dateStatus to the dbCountryById dateStatus
      if ( !dbCountryById.getStatus().equals( country.getStatus() ) )
      {
        country.setDateStatus( new Date() );
      }
      else
      {
        country.setDateStatus( dbCountryById.getDateStatus() );
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.country.CountryService#getCountryById(java.lang.Long)
   * @param id
   * @return Country
   */
  public Country getCountryById( Long id )
  {
    return this.countryDAO.getCountryById( id );
  }

  /**
   * Gets a Country object by country code.
   * 
   * @param countryCode a country code.
   * @return the specified Country object.
   */
  public Country getCountryByCode( String countryCode )
  {
    return this.countryDAO.getCountryByCode( countryCode );
  }

  @Override
  public BigDecimal getBudgetMediaValueByCountryCode( String countryCode )
  {
    return getCountryByCode( countryCode ).getBudgetMediaValue();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.country.CountryService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.countryDAO.getAll();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.country.CountryService#getAllActive()
   * @return List
   */
  public List<Country> getAllActive()
  {
    return this.countryDAO.getAllActive();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.country.CountryService#getAllActive()
   * @return List
   */
  public List getActiveCountriesForSmsChecked()
  {
    return this.countryDAO.getActiveCountriesForSmsChecked();
  }

  @Override
  public List<Country> getActiveCountriesForSmsAvailable()
  {
    return this.countryDAO.getActiveCountriesForSmsAvailable();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.country.CountryService#getActiveCountriesForAllActivePax()
   * @return List
   */
  public List getActiveCountriesForAllActivePax()
  {
    return this.countryDAO.getActiveCountriesForAllActivePax();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.country.CountryService#getActiveCountriesForPaxBasedAudience(java.lang.Long)
   * @param audienceId
   * @return
   */
  public List getActiveCountriesForPaxBasedAudience( Long audienceId )
  {
    return this.countryDAO.getActiveCountriesForPaxBasedAudience( audienceId );
  }

  /**
   * Gets the Country's Program object.
   * 
   * @param countryCode a country code.
   * @return a Program object.
   */
  public Program getProgramByCountryCode( String countryCode )
  {
    JtaTransactionManager jtaTransactionManager = (JtaTransactionManager)transactionManager;
    Transaction trx = null;

    // Check cache
    Program program = (Program)cacheProgram.get( countryCode );
    if ( program != null )
    {
      return program;
    }

    // Not in cache, retrieve from awardlinq
    Country country = getCountryByCode( countryCode );
    if ( country == null || StringUtils.isBlank( country.getProgramId() ) )
    {
      return null;
    }

    try
    {
      // Suspend the transaction
      trx = jtaTransactionManager.getTransactionManager().suspend();

      // Retrieve from awardslinq
      program = AwardslinqFeaturedItemsDelegate.getInstance().getProgramData( country.getProgramId() );

      // Save in cache
      cacheProgram.put( countryCode, program );
    }
    catch( Exception e )
    {
      logger.error( "Unable to get Program for " + countryCode, e );
    }
    finally
    {
      try
      {
        if ( trx != null )
        {
          // Resume the transaction
          jtaTransactionManager.getTransactionManager().resume( trx );
        }
      }
      catch( Exception e )
      {
        logger.error( "Unable to get Program for " + countryCode, e );
      }
    }

    return program;
  }

  private Date convertDateForTimezone( Date srcDate, String srcTimeZoneId, String destTimeZoneId )
  {
    Calendar srcCal = new GregorianCalendar( TimeZone.getTimeZone( destTimeZoneId ) );
    srcCal.setTimeInMillis( srcDate.getTime() );

    Calendar destCal = Calendar.getInstance( TimeZone.getTimeZone( srcTimeZoneId ) );
    destCal.set( Calendar.YEAR, srcCal.get( Calendar.YEAR ) );
    destCal.set( Calendar.MONTH, srcCal.get( Calendar.MONTH ) );
    destCal.set( Calendar.DAY_OF_MONTH, srcCal.get( Calendar.DAY_OF_MONTH ) );
    destCal.set( Calendar.HOUR_OF_DAY, srcCal.get( Calendar.HOUR_OF_DAY ) );
    destCal.set( Calendar.MINUTE, srcCal.get( Calendar.MINUTE ) );
    destCal.set( Calendar.SECOND, srcCal.get( Calendar.SECOND ) );
    destCal.set( Calendar.MILLISECOND, srcCal.get( Calendar.MILLISECOND ) );
    return destCal.getTime();
  }

  @Override
  public Date getCurrentTimeForCountry( Country destCountry )
  {
    Date date = null;
    if ( destCountry.getTimeZoneId() == null )
    {
      destCountry = getCountryById( destCountry.getId() );
    }

    if ( destCountry.getTimeZoneId() != null )
    {
      Date srcDate = new Date();
      String srcTimeZoneId = TimeZone.getDefault().getID();
      String destTimeZoneId = destCountry.getTimeZoneId().getCode();
      date = convertDateForTimezone( srcDate, srcTimeZoneId, destTimeZoneId );
    }

    return date;
  }

  @Override
  public Date convertTimeForCountry( Date srcDate, Country srcCountry, Country destCountry )
  {
    Date date = null;
    if ( srcCountry.getTimeZoneId() == null )
    {
      srcCountry = getCountryById( srcCountry.getId() );
    }
    if ( destCountry.getTimeZoneId() == null )
    {
      destCountry = getCountryById( destCountry.getId() );
    }

    if ( srcDate != null && srcCountry.getTimeZoneId() != null && destCountry.getTimeZoneId() != null )
    {
      String srcTimeZoneId = srcCountry.getTimeZoneId().getCode();
      String destTimeZoneId = destCountry.getTimeZoneId().getCode();
      date = convertDateForTimezone( srcDate, srcTimeZoneId, destTimeZoneId );
    }

    return date;
  }

  @Override
  public int getCountryCountByStartsWith( String startWith )
  {
    int count = 0;
    List<String> names = (List<String>)this.countryNamesList.get( COUNTRY_NAMES_LIST );
    if ( names == null || names.isEmpty() )
    {
      names = this.getCountryNameList();
    }
    for ( String name : names )
    {
      if ( name.startsWith( startWith ) )
      {
        count++;
      }
    }
    return count;
  }

  /**
   * {@inheritDoc}
   */
  public boolean checkUserSupplier( String countryCode, String supplierName )
  {
    return this.countryDAO.checkUserSupplier( countryCode, supplierName );
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<CountryValueBean> getAllActiveCountriesCodesAbbrevs()
  {
    return this.countryDAO.getAllActiveCountriesCodesAbbrevs();
  }
  
  /**
   * {@inheritDoc}
   */
  public boolean checkBiiExperience( String countryCode, String programId )
  {
    return this.countryDAO.checkBiiExperience( countryCode, programId );
  }
  
}
