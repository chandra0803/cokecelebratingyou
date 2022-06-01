
package com.biperf.core.dao.purl.hibernate;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.purl.PurlRecipientDAO;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.recognition.PurlRecipientValue;

public class PurlRecipientDAOImplTest extends BaseDAOTest
{
  public static PurlRecipient buildAndSaveUniquePurlRecipient( String suffix )
  {
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( suffix );
    getParticipantDAO().saveParticipant( pax );
    flushAndClearSession();

    RecognitionPromotion promotion = PromotionDAOImplTest.buildRecognitionPromotion( "Promo" + suffix );
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + suffix );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" + suffix );
    nodeType.setNameCmKey( "testkey" + suffix );

    Node newNode = NodeDAOImplTest.buildUniqueNode( "Node" + suffix, nodeType, hierarchy );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();

    PurlRecipient purlRecipient = new PurlRecipient();
    purlRecipient.setId( new Long( 1 ) );
    purlRecipient.setUser( pax );
    purlRecipient.setPromotion( promotion );
    purlRecipient.setNode( newNode );
    purlRecipient.setAwardAmount( new BigDecimal( 25 ) );
    purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.INVITATION ) );
    purlRecipient.setAwardDate( DateUtils.getNextDay( DateUtils.getCurrentDateTrimmed() ) );
    getPurlRecipientDAO().save( purlRecipient );
    flushAndClearSession();

    return purlRecipient;
  }

  public void testSaveGetPurlRecipientById()
  {
    PurlRecipient purlRecipient = buildAndSaveUniquePurlRecipient( getUniqueString() );

    PurlRecipient retrievedPurlRecipient = getPurlRecipientDAO().getPurlRecipientById( purlRecipient.getId() );
    assertNotNull( retrievedPurlRecipient );
  }

  public void testUpdatePurlRecipient()
  {
    PurlRecipient purlRecipient = buildAndSaveUniquePurlRecipient( getUniqueString() );

    PurlRecipient retrievedPurlRecipient = getPurlRecipientDAO().getPurlRecipientById( purlRecipient.getId() );
    assertNotNull( retrievedPurlRecipient );

    BigDecimal awardAmount = new BigDecimal( 50 );
    retrievedPurlRecipient.setAwardAmount( awardAmount );
    getPurlRecipientDAO().save( retrievedPurlRecipient );

    PurlRecipient updatedPurlRecipient = getPurlRecipientDAO().getPurlRecipientById( retrievedPurlRecipient.getId() );
    assertNotNull( updatedPurlRecipient );
  }

  public void testDeletePurlRecipient()
  {
    PurlRecipient purlRecipient = buildAndSaveUniquePurlRecipient( getUniqueString() );

    PurlRecipient retrievedPurlRecipient = getPurlRecipientDAO().getPurlRecipientById( purlRecipient.getId() );
    assertNotNull( retrievedPurlRecipient );

    getPurlRecipientDAO().delete( retrievedPurlRecipient );

    purlRecipient = getPurlRecipientDAO().getPurlRecipientById( retrievedPurlRecipient.getId() );
    assertNull( purlRecipient );
  }

  public void testGetUpComingCelebrationList()
  {
    PurlRecipient purlRecipient = buildAndSaveUniquePurlRecipient( getUniqueString() );
    List<PurlRecipientValue> purlRecipientValues = getPurlRecipientDAO().getUpComingCelebrationList();
    assertTrue( purlRecipientValues != null && purlRecipientValues.size() == 1 );
    assertTrue( purlRecipientValues.get( 0 ).getPurlRecipientId().longValue() == purlRecipient.getId().longValue() );
    assertTrue( purlRecipientValues.get( 0 ).getUserId().longValue() == purlRecipient.getUser().getId().longValue() );
    assertTrue( purlRecipientValues.get( 0 ).getPromotionId().longValue() == purlRecipient.getPromotion().getId().longValue() );
  }

  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( PromotionDAO.BEAN_NAME );
  }

  private static NodeDAO getNodeDAO()
  {
    return (NodeDAO)getDAO( NodeDAO.BEAN_NAME );
  }

  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }

  private static PurlRecipientDAO getPurlRecipientDAO()
  {
    return (PurlRecipientDAO)getDAO( PurlRecipientDAO.BEAN_NAME );
  }

  /*
   * public void testGetUpcomingPurlRecipients() { PurlRecipient purlRecipient =
   * buildAndSaveUniquePurlRecipient( getUniqueString() ); PurlRecipient retrievedPurlRecipient =
   * getPurlRecipientDAO().getPurlRecipientById( purlRecipient.getId() ); assertNotNull(
   * retrievedPurlRecipient ); retrievedPurlRecipient.setState( PurlRecipientState.lookup(
   * PurlRecipientState.INVITATION ) ); retrievedPurlRecipient.setAwardDate( DateUtils.getNextDay(
   * DateUtils.getCurrentDateTrimmed() ) ); List<PurlRecipient> purlRecipients =
   * getPurlRecipientDAO().getUpcomingPurlRecipients( retrievedPurlRecipient.getPromotion().getId(),
   * 100, "awardDate", 0 ); assertNotNull( purlRecipients ); assertTrue( purlRecipients.size() > 0
   * ); } public void testGetAwardedPurlRecipients() { PurlRecipient purlRecipient =
   * buildAndSaveUniquePurlRecipient( getUniqueString() ); PurlRecipient retrievedPurlRecipient =
   * getPurlRecipientDAO().getPurlRecipientById( purlRecipient.getId() ); assertNotNull(
   * retrievedPurlRecipient ); retrievedPurlRecipient.setState( PurlRecipientState.lookup(
   * PurlRecipientState.COMPLETE ) ); retrievedPurlRecipient.setAwardDate(
   * DateUtils.getPreviousDay() ); List<PurlRecipient> purlRecipients =
   * getPurlRecipientDAO().getAwardedPurlRecipients( retrievedPurlRecipient.getPromotion().getId(),
   * 100, "awardDate", 0 ); assertNotNull( purlRecipients ); assertTrue( purlRecipients.size() > 0
   * ); }
   */

  public void testGetPurlRecipientByUserId()
  {
    PurlRecipient purlRecipient = buildAndSaveUniquePurlRecipient( getUniqueString() );

    List<PurlRecipient> retrievedPurlRecipient = getPurlRecipientDAO().getPurlRecipientByUserId( purlRecipient.getUser().getId() );
    assertTrue( retrievedPurlRecipient != null && retrievedPurlRecipient.size() == 1 );
    assertTrue( retrievedPurlRecipient.get( 0 ).getId().longValue() == purlRecipient.getId().longValue() );
    assertTrue( retrievedPurlRecipient.get( 0 ).getUser().getId().longValue() == purlRecipient.getUser().getId().longValue() );
    assertTrue( retrievedPurlRecipient.get( 0 ).getPromotion().getId().longValue() == purlRecipient.getPromotion().getId().longValue() );

  }

}
