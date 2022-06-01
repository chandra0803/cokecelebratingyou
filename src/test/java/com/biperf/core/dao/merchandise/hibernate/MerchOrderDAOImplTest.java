/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise.hibernate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromoMerchCountryDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevelComparator;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * MerchOrderDAOImplTest.
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
 * <td>March 12, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class MerchOrderDAOImplTest extends BaseDAOTest
{
  /**
   * Creates a recognition promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */

  /**
   * Tests create and selecting the merchorder by id
   */
  public void testMerchOrderSaveAndGetById()
  {
    MerchOrderDAO merchOrderDAO = getMerchOrderDAO();
    String uniqueString = buildUniqueString();
    // create a new Merch Gift Code object
    MerchOrder merchOrder = MerchOrderDAOImplTest.buildMerchOrder( uniqueString );
    merchOrderDAO.saveMerchOrder( merchOrder );

    assertEquals( "Actual MerchOrder doesn't match with expected", merchOrder, merchOrderDAO.getMerchOrderById( merchOrder.getId() ) );
  }

  public void testsavePlateauRedemptionTracking()
  {
    MerchOrderDAO merchOrderDAO = getMerchOrderDAO();
    String uniqueString = buildUniqueString();
    // create a new Merch Gift Code object
    MerchOrder merchOrder = MerchOrderDAOImplTest.buildMerchOrder( uniqueString );
    merchOrderDAO.saveMerchOrder( merchOrder );

    assertNotNull( merchOrder );

    PlateauRedemptionTracking testpleteauRedemptionTracking = getPlateauRedemptionTracking( merchOrder.getId() );
    PlateauRedemptionTracking testpleteauRedemptionTrackingId = merchOrderDAO.savePlateauRedemptionTracking( testpleteauRedemptionTracking );
    assertEquals( testpleteauRedemptionTracking.getMerchOrderId(), testpleteauRedemptionTrackingId.getMerchOrderId() );
  }

  public static MerchOrder buildMerchOrder( String suffix )
  {
    // RecognitionPromotion promotion = new RecognitionPromotion();

    Promotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "Promo" + suffix );
    promotion = getPromotionDAO().save( promotion );
    flushAndClearSession();

    MerchOrder merchOrder = new MerchOrder();

    Participant newParticipant = ParticipantDAOImplTest.buildUniqueParticipant( "Pax" + suffix );
    newParticipant = getParticipantDAO().saveParticipant( newParticipant );
    flushAndClearSession();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );
    // flushAndClearSession();

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );
    // flushAndClearSession();

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    Claim claim = ClaimDAOImplTest.buildStaticRecognitionClaim();
    claim.setPromotion( promotion );
    claim.setSubmitter( newParticipant );
    claim.setNode( newNode );
    claim.setSubmissionDate( new Date() );
    claim.setClaimNumber( new Long( getOracleSequenceDao().getOracleSequenceNextValue( "claim_nbr_sq" ) ).toString() );
    claim = getClaimDAO().saveClaim( claim );
    flushAndClearSession();

    merchOrder.setClaim( claim );
    merchOrder.setParticipant( newParticipant );
    merchOrder.setId( new Long( "1" ) );
    merchOrder.setGiftCode( "12345678" );
    merchOrder.setGiftCodeKey( "87654321" );
    merchOrder.setProductId( "40" );
    merchOrder.setMerchGiftCodeType( MerchGiftCodeType.lookup( MerchGiftCodeType.PRODUCT ) );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    Country country = getCountryDAO().getCountryByCode( Country.UNITED_STATES );
    promoMerchCountry.setCountry( country );
    promoMerchCountry.setPromotion( promotion );
    promoMerchCountry.setProgramId( "12345" );
    PromoMerchProgramLevel promoMerchProgramLevel = new PromoMerchProgramLevel();
    promoMerchProgramLevel.setLevelName( "Test Level" );
    promoMerchProgramLevel.setPromoMerchCountry( promoMerchCountry );
    promoMerchProgramLevel.setOrdinalPosition( 1 );
    SortedSet<PromoMerchProgramLevel> levels = new TreeSet<PromoMerchProgramLevel>( new PromoMerchProgramLevelComparator() );
    levels.add( promoMerchProgramLevel );
    promoMerchCountry.setLevels( levels );
    PromoMerchCountryDAO promoMerchCountryDAO = getPromoMerchCountryDAO();
    /* PromoMerchCountry pmc = */promoMerchCountryDAO.savePromoMerchCountry( promoMerchCountry );
    flushAndClearSession();

    merchOrder.setPromoMerchProgramLevel( promoMerchProgramLevel );

    // merchOrder = getMerchOrderDAO().saveMerchOrder( merchOrder );
    // flushAndClearSession();

    return merchOrder;
  }

  /**
   * Uses the ApplicationContextFactory to look up the MerchOrderDAO implementation.
   * 
   * @return MerchOrderDAO
   */
  private static MerchOrderDAO getMerchOrderDAO()
  {
    return (MerchOrderDAO)getDAO( MerchOrderDAO.BEAN_NAME );
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
   * Returns a {@link ParticipantDAO} object.
   * 
   * @return a {@link ParticipantDAO} object.
   */
  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link NodeDAO} object.
   * 
   * @return a {@link NodeDAO} object.
   */
  private static NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( NodeDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the oracle sequence DAO.
   * 
   * @return a reference to the oracle sequence DAO.
   */
  private static OracleSequenceDAO getOracleSequenceDao()
  {
    return (OracleSequenceDAO)ApplicationContextFactory.getApplicationContext().getBean( OracleSequenceDAO.BEAN_NAME );
  }

  /**
   * Returns a {@link ClaimDAO} object.
   * 
   * @return a {@link ClaimDAO} object.
   */
  private static ClaimDAO getClaimDAO()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
  }

  /**
   * Get the CountryDAO.
   * 
   * @return CountryDAO
   */
  private static CountryDAO getCountryDAO()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( "countryDAO" );
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

  private static PlateauRedemptionTracking getPlateauRedemptionTracking( Long MerchOrderId )
  {
    PlateauRedemptionTracking plateauRedemptionTracking = new PlateauRedemptionTracking();
    plateauRedemptionTracking.setUserId( new Long( 12345 ) );
    plateauRedemptionTracking.setMerchOrderId( MerchOrderId );
    plateauRedemptionTracking.setCreatedBy( new Long( 12345 ) );
    plateauRedemptionTracking.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
    plateauRedemptionTracking.setVersion( new Long( 0 ) );
    return plateauRedemptionTracking;
  }

  /**
   * Tests get the merchorder by id and projections
   */
  @Test
  public void testGetMerchOrderByIdWithProjections()
  {
    MerchOrderDAO merchOrderDAO = getMerchOrderDAO();
    String uniqueString = buildUniqueString();
    // create a new Merch Gift Code object
    MerchOrder merchOrder = MerchOrderDAOImplTest.buildMerchOrder( uniqueString );
    ProjectionCollection projections = new ProjectionCollection();
    projections.add( new ProjectionAttribute( "giftCode" ) );

    assertNotNull( merchOrderDAO.getMerchOrderByIdWithProjections( merchOrder.getId(), projections ) );
  }

}
