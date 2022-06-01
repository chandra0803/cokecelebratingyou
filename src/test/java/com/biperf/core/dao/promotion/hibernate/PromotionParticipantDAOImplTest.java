/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/promotion/hibernate/PromotionParticipantDAOImplTest.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.hibernate.AudienceDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionParticipantDAO;
import com.biperf.core.domain.enums.PromotionJobPositionType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalParticipant;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionParticipantApprover;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.domain.promotion.PromotionWebRulesAudience;

/**
 * PromotionAudienceDAOTest.
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
 * <td>crosenquest</td>
 * <td>Aug 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionParticipantDAOImplTest extends BaseDAOTest
{

  /**
   * Tests saving to and getting from the database a promotionAudience of type SubmitterAudience.
   */
  public void testSaveGetByIdAndDeletePromotionSubmitterAudience()
  {

    String uniqueString = "TEST" + ( System.currentTimeMillis() % 2341241 );

    Promotion promotion = getSavedPromotionForTesting( uniqueString );
    Audience audience = getSavedAudienceForTesting( uniqueString );

    BaseDAOTest.flushAndClearSession();

    PromotionPrimaryAudience expectedPromotionSubmitterAudience = buildPromotionSubmitterAudience( promotion, audience );

    PromotionParticipantDAO promotionAudienceDAO = getPromotionParticipantDAO();

    promotionAudienceDAO.savePromotionAudience( expectedPromotionSubmitterAudience );

    BaseDAOTest.flushAndClearSession();

    PromotionAudience actualPromotionSubmitterAudience = promotionAudienceDAO.getPromotionAudienceById( expectedPromotionSubmitterAudience.getId() );

    BaseDAOTest.flushAndClearSession();

    assertEquals( "Actual PromotionPrimaryAudience wasn't equals to expected", expectedPromotionSubmitterAudience, actualPromotionSubmitterAudience );

    BaseDAOTest.flushAndClearSession();

    promotionAudienceDAO.deletePromotionAudience( actualPromotionSubmitterAudience );

    BaseDAOTest.flushAndClearSession();
  }

  /**
   * Tests saving to and getting from the database a promotionAudience of type RulesAudience.
   */
  public void testSaveGetByIdAndDeletePromotionRulesTextAudience()
  {

    String uniqueString = "TEST" + ( System.currentTimeMillis() % 2341241 );

    Promotion promotion = getSavedPromotionForTesting( uniqueString );
    Audience audience = getSavedAudienceForTesting( uniqueString );

    BaseDAOTest.flushAndClearSession();

    PromotionWebRulesAudience expectedPromotionWebRulesAudience = buildPromotionWebRulesAudience( promotion, audience );

    PromotionParticipantDAO promotionAudienceDAO = getPromotionParticipantDAO();

    promotionAudienceDAO.savePromotionAudience( expectedPromotionWebRulesAudience );

    BaseDAOTest.flushAndClearSession();

    PromotionAudience actualPromotionWebRulesAudience = promotionAudienceDAO.getPromotionAudienceById( expectedPromotionWebRulesAudience.getId() );

    BaseDAOTest.flushAndClearSession();

    assertEquals( "Actual PromotionRulesTextAudience wasn't equals to expected", expectedPromotionWebRulesAudience, actualPromotionWebRulesAudience );

    BaseDAOTest.flushAndClearSession();

    promotionAudienceDAO.deletePromotionAudience( actualPromotionWebRulesAudience );

    BaseDAOTest.flushAndClearSession();
  }

  /**
   * Tests saving to and getting from the database a promotionAudience of type Team Audience.
   */
  public void testSaveGetByIdAndDeletePromotionTeamAudience()
  {

    String uniqueString = "TEST" + ( System.currentTimeMillis() % 2341241 );

    Promotion promotion = getSavedPromotionForTesting( uniqueString );
    Audience audience = getSavedAudienceForTesting( uniqueString );

    BaseDAOTest.flushAndClearSession();

    PromotionSecondaryAudience expectedPromotionTeamAudience = buildPromotionTeamAudience( promotion, audience );

    PromotionParticipantDAO promotionParticipantDAO = getPromotionParticipantDAO();

    promotionParticipantDAO.savePromotionAudience( expectedPromotionTeamAudience );

    BaseDAOTest.flushAndClearSession();

    PromotionAudience actualPromotionTeamAudience = promotionParticipantDAO.getPromotionAudienceById( expectedPromotionTeamAudience.getId() );

    BaseDAOTest.flushAndClearSession();

    assertEquals( "Actual PromotionSecondaryAudience wasn't equals to expected", expectedPromotionTeamAudience, actualPromotionTeamAudience );

    BaseDAOTest.flushAndClearSession();

    promotionParticipantDAO.deletePromotionAudience( actualPromotionTeamAudience );

    BaseDAOTest.flushAndClearSession();
  }

  /**
   * Tests saving to and getting from the database a promotionApprovalParticipant of type
   * PromotionParticipantSubmitter.
   */
  public void testSaveGetByIdAndDeletePromotionParticipantSubmitter()
  {

    String uniqueString = "TEST" + ( System.currentTimeMillis() % 2341241 );

    Promotion promotion = getSavedPromotionForTesting( uniqueString );
    Long promotionId = promotion.getId();

    Participant participant = ParticipantDAOImplTest.getSavedParticipantForTesting( uniqueString );

    BaseDAOTest.flushAndClearSession();

    PromotionParticipantSubmitter expectedPromotionParticipantSubmitter = buildPromotionParticipantSubmitter( participant );

    // refetch the promotion, otherwise hibernate complains about a duplicate collection (even after
    // clearing the session)
    promotion = getPromotionDAO().getPromotionById( promotionId );
    promotion.addPromotionParticipantSubmitter( expectedPromotionParticipantSubmitter );
    getPromotionDAO().save( promotion );

    BaseDAOTest.flushAndClearSession();

    promotion = getPromotionDAO().getPromotionById( promotionId );
    PromotionApprovalParticipant actualPromotionApprovalParticipant = (PromotionApprovalParticipant)promotion.getPromotionParticipantSubmitters().get( 0 );
    Long paxId = actualPromotionApprovalParticipant.getId();

    BaseDAOTest.flushAndClearSession();

    assertEquals( "Actual PromotionApprovalParticipant wasn't equals to expected", expectedPromotionParticipantSubmitter, actualPromotionApprovalParticipant );

    BaseDAOTest.flushAndClearSession();

    promotion = getPromotionDAO().getPromotionById( promotionId );
    promotion.getPromotionParticipantSubmitters().remove( 0 );
    getPromotionDAO().save( promotion );

    BaseDAOTest.flushAndClearSession();

    actualPromotionApprovalParticipant = getPromotionParticipantDAO().getPromotionApprovalParticipantById( paxId );
    assertNull( "Participant was not deleted.", actualPromotionApprovalParticipant );

    BaseDAOTest.flushAndClearSession();
  }

  /**
   * Tests saving to and getting from the database a promotionApprovalParticipant of type
   * PromotionParticipantApprover.
   */
  public void testSaveGetByIdAndDeletePromotionParticipantApprover()
  {

    String uniqueString = "TEST" + ( System.currentTimeMillis() % 2341241 );

    Promotion promotion = getSavedPromotionForTesting( uniqueString );
    Long promotionId = promotion.getId();

    Participant participant = ParticipantDAOImplTest.getSavedParticipantForTesting( uniqueString );

    BaseDAOTest.flushAndClearSession();

    PromotionParticipantApprover expectedPromotionParticipantApprover = buildPromotionParticipantApprover( promotion, participant );

    // refetch the promotion, otherwise hibernate complains about a duplicate collection (even after
    // clearing the session)
    promotion = getPromotionDAO().getPromotionById( promotionId );
    promotion.addPromotionParticipantApprover( expectedPromotionParticipantApprover );
    getPromotionDAO().save( promotion );

    BaseDAOTest.flushAndClearSession();

    promotion = getPromotionDAO().getPromotionById( promotionId );
    PromotionApprovalParticipant actualPromotionApprovalParticipant = (PromotionApprovalParticipant)promotion.getPromotionParticipantApprovers().get( 0 );
    Long paxId = actualPromotionApprovalParticipant.getId();

    BaseDAOTest.flushAndClearSession();

    assertEquals( "Actual PromotionApprovalParticipant wasn't equals to expected", expectedPromotionParticipantApprover, actualPromotionApprovalParticipant );

    BaseDAOTest.flushAndClearSession();

    promotion = getPromotionDAO().getPromotionById( promotionId );
    promotion.getPromotionParticipantApprovers().remove( 0 );
    getPromotionDAO().save( promotion );

    BaseDAOTest.flushAndClearSession();

    actualPromotionApprovalParticipant = getPromotionParticipantDAO().getPromotionApprovalParticipantById( paxId );
    assertNull( "Participant was not deleted.", actualPromotionApprovalParticipant );

    BaseDAOTest.flushAndClearSession();
  }

  /**
   * Tests saving, getting by id and deleting a promotionTeamPosition.
   */
  public void testSaveGetByIdAndDeletePromotionTeamPosition()
  {

    String uniqueString = "TEST" + ( System.currentTimeMillis() % 2341241 );
    Promotion promotion = getSavedPromotionForTesting( uniqueString );

    PromotionTeamPosition expectedPromotionTeamPosition = buildPromotionTeamPosition( promotion );

    flushAndClearSession();

    PromotionParticipantDAO promotionParticipantDAO = this.getPromotionParticipantDAO();

    // Save the promotionTeamPosition
    promotionParticipantDAO.savePromotionTeamPosition( expectedPromotionTeamPosition );

    flushAndClearSession();

    // Assert the ID isn't null
    assertTrue( "Expected promotionTeamPosition wasn't saved properly", expectedPromotionTeamPosition.getId() != null );

    // Get the promotionTeamPosition by Id and check equality
    PromotionTeamPosition actualPromotionTeamPosition = promotionParticipantDAO.getPromotionTeamPositionById( expectedPromotionTeamPosition.getId() );

    flushAndClearSession();

    assertEquals( "Expected promotionTeamPosition wasn't equals to what was expected.", expectedPromotionTeamPosition, actualPromotionTeamPosition );

    // Delete the promotionTeamPosition
    promotionParticipantDAO.deletePromotionTeamPosition( actualPromotionTeamPosition );

  }

  /**
   * Builds a promotionTeamPositionType with a previously saved Promotion.
   * 
   * @param promotion
   * @return PromotionTeamPosition
   */
  public static PromotionTeamPosition buildPromotionTeamPosition( Promotion promotion )
  {

    PromotionTeamPosition promotionTeamPosition = new PromotionTeamPosition();
    promotionTeamPosition.setRequired( true );
    promotionTeamPosition.setPromotion( promotion );
    promotionTeamPosition.setPromotionJobPositionType( PromotionJobPositionType.lookup( "sales_rep" ) );

    return promotionTeamPosition;
  }

  /**
   * Builds a promotionSubmitterAudience with an already saved promotion and already saved audience.
   * 
   * @param promotion
   * @param audience
   * @return PromotionPrimaryAudience
   */
  public static PromotionPrimaryAudience buildPromotionSubmitterAudience( Promotion promotion, Audience audience )
  {

    PromotionPrimaryAudience promotionSubmitterAudience = new PromotionPrimaryAudience();

    promotionSubmitterAudience.setPromotion( promotion );
    promotionSubmitterAudience.setAudience( audience );

    return promotionSubmitterAudience;
  }

  /**
   * Calls the buildPromotionSubmitterAudience with a null promotion.
   * 
   * @param audience
   * @return PromotionPrimaryAudience
   */
  public static PromotionPrimaryAudience buildPromotionSubmitterAudience( Audience audience )
  {
    return buildPromotionSubmitterAudience( null, audience );
  }

  /**
   * Builes a promotionRulesTextAudience with an already saved promotion and already saved audience.
   * 
   * @param promotion
   * @param audience
   * @return PromotionWebRulesAudience
   */
  public static PromotionWebRulesAudience buildPromotionWebRulesAudience( Promotion promotion, Audience audience )
  {

    PromotionWebRulesAudience promotionWebRulesAudience = new PromotionWebRulesAudience();

    promotionWebRulesAudience.setPromotion( promotion );
    promotionWebRulesAudience.setAudience( audience );

    return promotionWebRulesAudience;
  }

  /**
   * Calls buildPromotionWebRulesAudience with a null promotion.
   * 
   * @param audience
   * @return PromotionWebRulesAudience
   */
  public static PromotionWebRulesAudience buildPromotionWebRulesAudience( Audience audience )
  {
    return buildPromotionWebRulesAudience( null, audience );
  }

  /**
   * Builds a promotionParticipantApprover with an already saved promotion and already saved user.
   * 
   * @param promotion
   * @param participant
   * @return PromotionParticipantApprover
   */
  public static PromotionParticipantApprover buildPromotionParticipantApprover( Promotion promotion, Participant participant )
  {

    PromotionParticipantApprover promotionParticipantApprover = new PromotionParticipantApprover();

    promotionParticipantApprover.setPromotion( promotion );
    promotionParticipantApprover.setParticipant( participant );

    return promotionParticipantApprover;
  }

  /**
   * Builds a promotionParticipantApprover with an already saved user.
   * 
   * @param participant
   * @return PromotionParticipantApprover
   */
  public static PromotionParticipantApprover buildPromotionParticipantApprover( Participant participant )
  {

    return buildPromotionParticipantApprover( null, participant );

    // PromotionParticipantApprover promotionParticipantApprover = new
    // PromotionParticipantApprover();
    //
    // promotionParticipantApprover.setParticipant( participant );
    //
    // return promotionParticipantApprover;
  }

  /**
   * Builds a promotionParticipantSubmitter with an already saved user.
   * 
   * @param participant
   * @return PromotionParticipantSubmitter
   */
  public static PromotionParticipantSubmitter buildPromotionParticipantSubmitter( Participant participant )
  {

    PromotionParticipantSubmitter promotionParticipantSubmitter = new PromotionParticipantSubmitter();

    promotionParticipantSubmitter.setParticipant( participant );

    return promotionParticipantSubmitter;
  }

  /**
   * Builds a static promotion with a unique string, saves the promotion and returns it.
   * 
   * @param uniqueString
   * @return Promotion
   */
  private Promotion getSavedPromotionForTesting( String uniqueString )
  {
    return PromotionDAOImplTest.getSavedPromotionForTesting( uniqueString );
  }

  /**
   * Builds a participant audience and saves it for testing.
   * 
   * @param uniqueString
   * @return Audience
   */
  private static Audience getSavedAudienceForTesting( String uniqueString )
  {
    return AudienceDAOImplTest.getSavedPaxAudience( uniqueString );
  }

  /**
   * Builds promotionTeamAudience for testing using a uniqueString.
   * 
   * @param promotion
   * @param audience
   * @return PromotionSecondaryAudience
   */
  public static PromotionSecondaryAudience buildPromotionTeamAudience( Promotion promotion, Audience audience )
  {

    PromotionSecondaryAudience promotionTeamAudience = new PromotionSecondaryAudience();
    promotionTeamAudience.setAudience( audience );
    promotionTeamAudience.setPromotion( promotion );

    return promotionTeamAudience;

  }

  /**
   * Calls buildPromotionTeamAudiance with a null promotion.
   * 
   * @param audience
   * @return PromotionSecondaryAudience
   */
  public static PromotionSecondaryAudience buildPromotionTeamAudience( Audience audience )
  {
    return buildPromotionTeamAudience( null, audience );
  }

  /**
   * Get the promotionDAO.
   * 
   * @return PromotionDAO
   */
  private PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( "promotionDAO" );
  }

  /**
   * Get the promotionParticipantDAO.
   * 
   * @return PromotionParticipantDAO
   */
  private PromotionParticipantDAO getPromotionParticipantDAO()
  {
    return (PromotionParticipantDAO)getDAO( "promotionParticipantDAO" );
  }
}
