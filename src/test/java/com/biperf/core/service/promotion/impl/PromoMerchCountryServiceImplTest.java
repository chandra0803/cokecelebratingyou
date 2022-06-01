/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/promotion/impl/PromoMerchCountryServiceImplTest.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.jmock.Mock;
import org.jmock.core.constraint.IsAnything;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.participant.ListBuilderDAO;
import com.biperf.core.dao.promotion.PromoMerchCountryDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.ParticipantService;

/**
 * PromoMerchCountryServiceImplTest.java - made to test the retrieval of active countries in 
 *  the promotion receivers audience as well the the active countries not in the promotion
 *  receivers audience
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
 * <td>Jul 31, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromoMerchCountryServiceImplTest extends BaseServiceTest
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public PromoMerchCountryServiceImplTest( String test )
  {
    super( test );
  }

  /** PromoMerchCountryServiceImplTest */
  private PromoMerchCountryServiceImpl promoMerchCountryService = new PromoMerchCountryServiceImpl();

  /** mocks */
  private Mock mockPromoMerchCountryDAO = null;
  private Mock mockCountryDAO = null;
  private Mock mockPromotionDAO = null;
  private Mock mockListBuilderDAO = null;
  private Mock mockHierarchyDAO = null;
  private Mock mockParticipantService = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockPromoMerchCountryDAO = new Mock( PromoMerchCountryDAO.class );
    mockPromotionDAO = new Mock( PromotionDAO.class );
    mockCountryDAO = new Mock( CountryDAO.class );
    mockPromotionDAO = new Mock( PromotionDAO.class );
    mockListBuilderDAO = new Mock( ListBuilderDAO.class );
    mockHierarchyDAO = new Mock( HierarchyDAO.class );
    mockParticipantService = new Mock( ParticipantService.class );

    promoMerchCountryService.setPromoMerchCountryDAO( (PromoMerchCountryDAO)mockPromoMerchCountryDAO.proxy() );
    promoMerchCountryService.setCountryDAO( (CountryDAO)mockCountryDAO.proxy() );
    promoMerchCountryService.setPromotionDAO( (PromotionDAO)mockPromotionDAO.proxy() );
    promoMerchCountryService.setListBuilderDAO( (ListBuilderDAO)mockListBuilderDAO.proxy() );
    promoMerchCountryService.setHierarchyDAO( (HierarchyDAO)mockHierarchyDAO.proxy() );
    promoMerchCountryService.setParticipantService( (ParticipantService)mockParticipantService.proxy() );

  }

  public void testGetPromoMerchCountryById()
  {
    Promotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    Country country = buildCountry( "1" );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setId( new Long( 1 ) );

    mockPromoMerchCountryDAO.expects( once() ).method( "getPromoMerchCountryById" ).with( same( promoMerchCountry.getId() ) ).will( returnValue( promoMerchCountry ) );

    promoMerchCountryService.getPromoMerchCountryById( promoMerchCountry.getId() );

    mockPromoMerchCountryDAO.verify();

  }

  public void testSavePromoMerchCountry()
  {
    Promotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    Country country = buildCountry( "1" );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setId( new Long( 1 ) );

    // Create the saved PromoMerchCountry.
    PromoMerchCountry savedPromoMerchCountry = new PromoMerchCountry();
    savedPromoMerchCountry.setId( new Long( 1 ) );
    savedPromoMerchCountry.setPromotion( promotion );

    mockPromoMerchCountryDAO.expects( once() ).method( "savePromoMerchCountry" ).with( same( promoMerchCountry ) ).will( returnValue( savedPromoMerchCountry ) );

    try
    {
      promoMerchCountryService.savePromoMerchCountry( promoMerchCountry );
    }
    catch( Exception e )
    {

    }

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockPromoMerchCountryDAO.verify();

  }

  public void testGetPromoMerchCountriesByPromotionId()
  {
    Promotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    Country country = buildCountry( "1" );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setId( new Long( 1 ) );

    List promoMerchCountries = new ArrayList();
    promoMerchCountries.add( promoMerchCountry );

    mockPromoMerchCountryDAO.expects( once() ).method( "getPromoMerchCountriesByPromotionId" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promoMerchCountries ) );

    List returnedPromoMerchCountries = promoMerchCountryService.getPromoMerchCountriesByPromotionId( promotion.getId() );
    assertTrue( returnedPromoMerchCountries.size() > 0 );

  }

  public void testGetActiveCountriesInPromoRecAudienceAllActivePaxAudience()
  {
    Promotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    Country country = buildCountry( "1" );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setId( new Long( 1 ) );

    List promoMerchCountries = new ArrayList();
    promoMerchCountries.add( promoMerchCountry );

    mockPromoMerchCountryDAO.expects( once() ).method( "getPromoMerchCountriesByPromotionId" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promoMerchCountries ) );

    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promotion ) );

    promoMerchCountries = new ArrayList();

    List countries = new ArrayList();

    mockCountryDAO.expects( once() ).method( "getActiveCountriesForAllActivePax" ).will( returnValue( countries ) );

    List returnedPromoMerchCountries = promoMerchCountryService.getActiveCountriesInPromoRecAudience( promotion.getId(), null, null, null, null );
    assertFalse( returnedPromoMerchCountries.size() > 0 );

  }

  public void testGetActiveCountriesInPromoRecAudienceSpecifyPaxBasedAudience()
  {
    Promotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    Country country = buildCountry( "1" );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setId( new Long( 1 ) );

    List promoMerchCountries = new ArrayList();
    promoMerchCountries.add( promoMerchCountry );

    mockPromoMerchCountryDAO.expects( once() ).method( "getPromoMerchCountriesByPromotionId" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promoMerchCountries ) );

    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promotion ) );

    promoMerchCountries = new ArrayList();

    List returnedPromoMerchCountries = promoMerchCountryService.getActiveCountriesInPromoRecAudience( promotion.getId(), null, null, null, null );
    assertFalse( returnedPromoMerchCountries.size() > 0 );

  }

  public void testGetActiveCountriesInPromoRecAudienceSpecifyCritAudience()
  {
    Promotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    Country country = buildCountry( "1" );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setId( new Long( 1 ) );

    List promoMerchCountries = new ArrayList();
    promoMerchCountries.add( promoMerchCountry );

    mockPromoMerchCountryDAO.expects( once() ).method( "getPromoMerchCountriesByPromotionId" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promoMerchCountries ) );

    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionByIdWithAssociations" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promotion ) );

    promoMerchCountries = new ArrayList();

    List returnedPromoMerchCountries = promoMerchCountryService.getActiveCountriesInPromoRecAudience( promotion.getId(), null, null, null, null );
    assertFalse( returnedPromoMerchCountries.size() > 0 );

  }

  public void testGetActiveCountriesNotInPromoRecAudience()
  {
    Promotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    Country country = buildCountry( "1" );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setId( new Long( 1 ) );

    List promoMerchCountries = new ArrayList();
    promoMerchCountries.add( promoMerchCountry );

    mockPromoMerchCountryDAO.expects( atLeastOnce() ).method( "getPromoMerchCountriesByPromotionId" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promoMerchCountries ) );

    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) );

    mockPromotionDAO.expects( atLeastOnce() ).method( "getPromotionByIdWithAssociations" ).with( same( promotion.getId() ), new IsAnything() ).will( returnValue( promotion ) );

    promoMerchCountries = new ArrayList();

    List countries = new ArrayList();

    mockCountryDAO.expects( atLeastOnce() ).method( "getActiveCountriesForAllActivePax" ).will( returnValue( countries ) );

    mockCountryDAO.expects( once() ).method( "getAllActive" ).will( returnValue( countries ) );

    List returnedPromoMerchCountries = promoMerchCountryService.getActiveCountriesNotInPromoRecAudience( promotion.getId(), null, null, null, null );
    assertFalse( returnedPromoMerchCountries.size() > 0 );

  }

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
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    country.setAddressMethod( AddressMethodType.lookup( AddressMethodType.NORTH_AMERICAN ) );
    country.setStatus( CountryStatusType.lookup( CountryStatusType.ACTIVE ) );
    country.setDateStatus( new Date() );

    return country;
  }

}