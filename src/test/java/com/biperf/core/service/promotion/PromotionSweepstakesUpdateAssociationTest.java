/**
 * 
 */

package com.biperf.core.service.promotion;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;

/**
 * PromotionSweepstakesUpdateAssociationTest.
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
 * <td>asondgeroth</td>
 * <td>Nov 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakesUpdateAssociationTest extends BaseServiceTest
{
  public void testAddSweepstakes()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.addPromotionSweepstake( buildPromotionSweepstake() );

    PromotionSweepstakeUpdateAssociation updateAssociation = new PromotionSweepstakeUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertEquals( detachedRecognitionPromotion.getPromotionSweepstakes(), attachedRecognitionPromotion.getPromotionSweepstakes() );

    assertTrue( "Sweepstakes size should be 1 but is " + attachedRecognitionPromotion.getPromotionSweepstakes().size(), attachedRecognitionPromotion.getPromotionSweepstakes().size() == 1 );
  }

  public void testRemoveOnePromotionSweepstake()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();

    PromotionSweepstake sweepstakeToKeep = buildPromotionSweepstake();
    PromotionSweepstake sweepstakeToRemove = buildPromotionSweepstake();

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToKeep );
    detachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToRemove );

    // Setup the attached
    PromotionSweepstakeUpdateAssociation updateAssociation = new PromotionSweepstakeUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    // Reset the detached object
    detachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToKeep );
    detachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToRemove );
    detachedRecognitionPromotion.getPromotionSweepstakes().remove( sweepstakeToRemove );

    updateAssociation = new PromotionSweepstakeUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( "Sweepstakes size should be 1 but is " + attachedRecognitionPromotion.getPromotionSweepstakes().size(), attachedRecognitionPromotion.getPromotionSweepstakes().size() == 1 );
  }

  public void testMergeAndUpdatePromotionSweepstakes()
  {
    PromotionSweepstake sweepstakeToUpdate = buildPromotionSweepstake();
    PromotionSweepstake sweepstakeToLeave = buildPromotionSweepstake();

    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    attachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToUpdate );
    attachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToLeave );

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    sweepstakeToUpdate.setProcessed( true );
    detachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToUpdate );
    detachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToLeave );

    PromotionSweepstakeUpdateAssociation updateAssociation = new PromotionSweepstakeUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    Set sweepstakes = attachedRecognitionPromotion.getPromotionSweepstakes();

    assertTrue( "Attached sweepstakes should contain: " + sweepstakeToUpdate, sweepstakes.contains( sweepstakeToUpdate ) );
    assertTrue( "Attached sweepstakes should contain: " + sweepstakeToLeave, sweepstakes.contains( sweepstakeToLeave ) );
  }

  public void testAddSweepstakeWinners()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    PromotionSweepstake sweepstakeToSave = buildPromotionSweepstake();
    PromotionSweepstakeWinner winnerToSave = buildPromotionSweepstakeWinner();
    sweepstakeToSave.addWinner( winnerToSave );
    detachedRecognitionPromotion.addPromotionSweepstake( sweepstakeToSave );

    PromotionSweepstakeUpdateAssociation updateAssociation = new PromotionSweepstakeUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( "Sweepstakes size should be 1 but is " + attachedRecognitionPromotion.getPromotionSweepstakes().size(), attachedRecognitionPromotion.getPromotionSweepstakes().size() == 1 );

    Set sweepstakes = attachedRecognitionPromotion.getPromotionSweepstakes();
    Iterator sweepstakeIter = sweepstakes.iterator();
    while ( sweepstakeIter.hasNext() )
    {
      PromotionSweepstake sweepstake = (PromotionSweepstake)sweepstakeIter.next();
      assertEquals( sweepstakeToSave.getWinners(), sweepstake.getWinners() );
      assertTrue( "Sweepstake winners size should be 1 but is " + sweepstake.getWinners().size(), sweepstake.getWinners().size() == 1 );
    }
  }

  public void testMergeAndUpdateSweepstakeWinners()
  {
    PromotionSweepstake attachedSweepstake = buildPromotionSweepstake();
    PromotionSweepstakeWinner winnerToUpdate = buildPromotionSweepstakeWinner();
    PromotionSweepstakeWinner winnerToLeave = buildPromotionSweepstakeWinner();
    PromotionSweepstakeWinner winnerToRemove = buildPromotionSweepstakeWinner();
    PromotionSweepstakeWinner winnerToAdd = buildPromotionSweepstakeWinner();
    attachedSweepstake.addWinner( winnerToUpdate );
    attachedSweepstake.addWinner( winnerToLeave );
    attachedSweepstake.addWinner( winnerToRemove );

    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    attachedRecognitionPromotion.addPromotionSweepstake( attachedSweepstake );

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    PromotionSweepstake detachedSweepstake = buildPromotionSweepstake();
    detachedSweepstake.setId( attachedSweepstake.getId() );
    detachedSweepstake.setStartDate( attachedSweepstake.getStartDate() );
    detachedSweepstake.setEndDate( attachedSweepstake.getEndDate() );
    detachedSweepstake.setProcessed( attachedSweepstake.isProcessed() );
    detachedSweepstake.setPromotion( attachedSweepstake.getPromotion() );

    winnerToUpdate.setRemoved( true );
    detachedSweepstake.addWinner( winnerToUpdate );
    detachedSweepstake.addWinner( winnerToLeave );
    detachedSweepstake.addWinner( winnerToAdd );

    Set unchangedWinnersSet = new HashSet();
    unchangedWinnersSet.add( winnerToUpdate );
    unchangedWinnersSet.add( winnerToLeave );
    unchangedWinnersSet.add( winnerToAdd );

    detachedRecognitionPromotion.addPromotionSweepstake( detachedSweepstake );

    PromotionSweepstakeUpdateAssociation updateAssociation = new PromotionSweepstakeUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( "Sweepstakes size should be 1 but is " + attachedRecognitionPromotion.getPromotionSweepstakes().size(), attachedRecognitionPromotion.getPromotionSweepstakes().size() == 1 );

    Set sweepstakes = attachedRecognitionPromotion.getPromotionSweepstakes();
    Iterator sweepstakesIter = sweepstakes.iterator();
    while ( sweepstakesIter.hasNext() )
    {
      PromotionSweepstake sweepstake = (PromotionSweepstake)sweepstakesIter.next();

      assertEquals( unchangedWinnersSet, sweepstake.getWinners() );
      assertTrue( "Sweepstake winners size should be 3 but is " + sweepstake.getWinners().size(), sweepstake.getWinners().size() == 3 );
    }
  }

  public RecognitionPromotion buildSimpleRecognitionPromotion()
  {
    RecognitionPromotion promo = new RecognitionPromotion();
    promo.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    return promo;
  }

  private static int sweepstakeIdCounter;

  /**
   * Builds a PromotionSweepstake from scratch.
   * 
   * @return PromotionSweepstake
   */
  public static PromotionSweepstake buildPromotionSweepstake()
  {
    Long id = new Long( ++sweepstakeIdCounter );
    PromotionSweepstake promotionSweepstake = new PromotionSweepstake();
    promotionSweepstake.setId( id );
    promotionSweepstake.setStartDate( new Date() );
    promotionSweepstake.setEndDate( new Date() );
    promotionSweepstake.setProcessed( false );

    return promotionSweepstake;
  }

  private static int winnerIdCounter;

  /**
   * Builds a PromotionSweepstakeWinner from scratch.
   * 
   * @return PromotionSweepstakeWinner
   */
  public static PromotionSweepstakeWinner buildPromotionSweepstakeWinner()
  {
    Long id = new Long( ++winnerIdCounter );
    PromotionSweepstakeWinner sweepstakeWinner = new PromotionSweepstakeWinner();
    sweepstakeWinner.setId( id );

    String uniqueString = "TEST" + id;
    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    sweepstakeWinner.setParticipant( participant );

    return sweepstakeWinner;
  }
}
