/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/promotion/hibernate/PromoMerchCountryDAOImplTest.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.dao.integration.hibernate.SupplierDAOImplTest;
import com.biperf.core.dao.promotion.PromoMerchCountryDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * PromoMerchCountryDAOImplTest.
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
 * <td>ernste</td>
 * <td>July 31, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromoMerchCountryDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link Country} object.
   * 
   * @param uniqueString used to make the returned country unique.
   * @return a {@link Country} object.
   */
  public static Country buildCountry( String suffix )
  {
    Country country = new Country();

    country.setCountryCode( "ba" + suffix );
    country.setCountryName( "Batavia" + suffix );
    country.setCampaignNbr( "NUMBER" + suffix );
    country.setCampaignPassword( "PASSWORD" + suffix );
    country.setAwardbanqAbbrev( "bat" );
    country.setAddressMethod( AddressMethodType.lookup( AddressMethodType.NORTH_AMERICAN ) );
    country.setStatus( CountryStatusType.lookup( CountryStatusType.ACTIVE ) );
    country.setDateStatus( new Date() );
    country.setAllowSms( Boolean.FALSE );
    country.setRequirePostalCode( Boolean.FALSE );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );

    return country;
  }

  public static CountrySupplier buildCountrySupplier( Country expectedCountry, Supplier expectedSupplier )
  {
    CountrySupplier countrySupplier = new CountrySupplier();
    countrySupplier.setCountry( expectedCountry );
    countrySupplier.setSupplier( expectedSupplier );
    countrySupplier.setPrimary( Boolean.TRUE );

    return countrySupplier;
  }

  public static RecognitionPromotion buildPromotion( String suffix )
  {
    return PromotionDAOImplTest.buildRecognitionPromotion( suffix );
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------
  public void testSaveAndGetPromoMerchCountryById()
  {
    CountryDAO countryDAO = getCountryDAO();
    SupplierDAO supplierDAO = getSupplierDAO();

    // Create a Supplier object to add to the Country object.
    Supplier expectedSupplier = SupplierDAOImplTest.buildSupplier( "Test Supplier1" );
    supplierDAO.saveSupplier( expectedSupplier );

    // Create a Country object
    Country expectedCountry = buildCountry( getUniqueString() );
    expectedCountry.addCountrySuppliers( buildCountrySupplier( expectedCountry, expectedSupplier ) );

    // Save the country object
    countryDAO.saveCountry( expectedCountry );

    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "PromoX" );
    promotion = getPromotionDAO().save( promotion );
    flushAndClearSession();

    PromoMerchCountry expectedPromoMerchCountry = new PromoMerchCountry();

    expectedPromoMerchCountry.setCountry( expectedCountry );
    expectedPromoMerchCountry.setPromotion( promotion );
    expectedPromoMerchCountry.setProgramId( "12345" );

    PromoMerchCountryDAO promoMerchCountryDAO = getPromoMerchCountryDAO();
    PromoMerchCountry pmc = promoMerchCountryDAO.savePromoMerchCountry( expectedPromoMerchCountry );

    flushAndClearSession();

    PromoMerchCountry actualPromoMerchCountry = promoMerchCountryDAO.getPromoMerchCountryById( pmc.getId() );

    // Check that the promo merch country fetched matches the one that was created.
    assertEquals( "Promotion Merch Country not equals", expectedPromoMerchCountry, actualPromoMerchCountry );

  }

  public void testSaveAndGetPromoMerchCountryByPromotionId()
  {
    CountryDAO countryDAO = getCountryDAO();
    SupplierDAO supplierDAO = getSupplierDAO();

    // Create a Supplier object to add to the Country object.
    Supplier expectedSupplier = SupplierDAOImplTest.buildSupplier( "Test Supplier1" + ( new Date() ).getTime() );
    supplierDAO.saveSupplier( expectedSupplier );

    // Create a Country object
    Country expectedCountry = buildCountry( getUniqueString() );
    expectedCountry.addCountrySuppliers( buildCountrySupplier( expectedCountry, expectedSupplier ) );

    // Save the country object
    countryDAO.saveCountry( expectedCountry );

    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "PromoX" );
    promotion = getPromotionDAO().save( promotion );
    flushAndClearSession();

    PromoMerchCountry expectedPromoMerchCountry = new PromoMerchCountry();

    expectedPromoMerchCountry.setCountry( expectedCountry );
    expectedPromoMerchCountry.setPromotion( promotion );
    expectedPromoMerchCountry.setProgramId( "12345" );

    PromoMerchCountryDAO promoMerchCountryDAO = getPromoMerchCountryDAO();
    PromoMerchCountry pmc = promoMerchCountryDAO.savePromoMerchCountry( expectedPromoMerchCountry );

    flushAndClearSession();

    PromoMerchCountry actualPromoMerchCountry = promoMerchCountryDAO.getPromoMerchCountryById( pmc.getId() );

    // Check that the promo merch country fetched matches the one that was created.
    assertEquals( "Promotion Merch Country not equals", expectedPromoMerchCountry, actualPromoMerchCountry );

    PromoMerchCountry promoMerchCountryByPromotionId = null;
    List promoMerchCountries = promoMerchCountryDAO.getPromoMerchCountriesByPromotionId( promotion.getId(), null );
    for ( Iterator iter = promoMerchCountries.iterator(); iter.hasNext(); )
    {
      promoMerchCountryByPromotionId = (PromoMerchCountry)iter.next();
    }

    assertEquals( "Promotion Merch Country by promotion ID not equals", expectedPromoMerchCountry, promoMerchCountryByPromotionId );

  }

  /**
   * Get the CountryDAO.
   * 
   * @return CountryDAO
   */
  private CountryDAO getCountryDAO()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( "countryDAO" );
  }

  /**
   * Returns a {@link PromotionDAO} object.
   * 
   * @return a {@link PromotionDAO} object.
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link PromoMerchCountryDAO} object.
   * 
   * @return a {@link PromoMerchCountryDAO} object.
   */
  private static PromoMerchCountryDAO getPromoMerchCountryDAO()
  {
    return (PromoMerchCountryDAO)ApplicationContextFactory.getApplicationContext().getBean( PromoMerchCountryDAO.BEAN_NAME );
  }

  /**
   * Get the SupplierDAO.
   * 
   * @return SupplierDAO
   */
  private SupplierDAO getSupplierDAO()
  {
    return (SupplierDAO)ApplicationContextFactory.getApplicationContext().getBean( "supplierDAO" );
  }

}
