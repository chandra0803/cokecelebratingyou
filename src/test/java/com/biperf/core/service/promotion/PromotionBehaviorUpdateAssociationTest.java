/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/promotion/PromotionBehaviorUpdateAssociationTest.java,v $
 *
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;

/**
 * PromotionBehaviorUpdateAssociationTest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PromotionBehaviorUpdateAssociationTest extends BaseServiceTest
{
  public void testAddBehaviorsWithNoAttachedBehaviors()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.OUTSTANDING_CODE ) );
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    PromotionBehaviorUpdateAssociation updateAssociation = new PromotionBehaviorUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertEquals( detachedRecognitionPromotion.getPromotionBehaviors(), attachedRecognitionPromotion.getPromotionBehaviors() );
  }

  public void testRemoveAllAttachedBehaviors()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.OUTSTANDING_CODE ) );
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    PromotionBehaviorUpdateAssociation updateAssociation = new PromotionBehaviorUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( attachedRecognitionPromotion.getPromotionBehaviors().isEmpty() );
  }

  public void testAddOnePromotionBehavior()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.OUTSTANDING_CODE ) );

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.OUTSTANDING_CODE ) );
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    PromotionBehaviorUpdateAssociation updateAssociation = new PromotionBehaviorUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.GREAT_IDEA_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.OUTSTANDING_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.LEADERSHIP_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
  }

  public void testRemoveOnePromotionBehavior()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.OUTSTANDING_CODE ) );
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.OUTSTANDING_CODE ) );

    PromotionBehaviorUpdateAssociation updateAssociation = new PromotionBehaviorUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertFalse( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.GREAT_IDEA_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.OUTSTANDING_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.LEADERSHIP_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
  }

  public void testMergePromotionBehaviors()
  {
    RecognitionPromotion attachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.OUTSTANDING_CODE ) );
    attachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    RecognitionPromotion detachedRecognitionPromotion = buildSimpleRecognitionPromotion();
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.LEADERSHIP_CODE ) );
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.SUPPORTIVE_CODE ) );
    detachedRecognitionPromotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.TEAM_PLAYER_CODE ) );

    PromotionBehaviorUpdateAssociation updateAssociation = new PromotionBehaviorUpdateAssociation( detachedRecognitionPromotion );
    updateAssociation.execute( attachedRecognitionPromotion );

    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.LEADERSHIP_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertFalse( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.OUTSTANDING_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertFalse( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.GREAT_IDEA_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.SUPPORTIVE_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
    assertTrue( isPromoRecognitionBehaviorTypeInSet( PromoRecognitionBehaviorType.TEAM_PLAYER_CODE, attachedRecognitionPromotion.getPromotionBehaviors() ) );
  }

  private boolean isPromoRecognitionBehaviorTypeInSet( String promoRecognitionBehaviorCode, Set promoBehaviors )
  {
    PromoRecognitionBehaviorType behaviorType = PromoRecognitionBehaviorType.lookup( promoRecognitionBehaviorCode );
    for ( Iterator iterator = promoBehaviors.iterator(); iterator.hasNext(); )
    {
      PromotionBehavior behavior = (PromotionBehavior)iterator.next();
      if ( behavior.getPromotionBehaviorType().equals( behaviorType ) )
      {
        return true;
      }
    }
    return false;
  }

  public RecognitionPromotion buildSimpleRecognitionPromotion()
  {
    RecognitionPromotion promo = new RecognitionPromotion();
    promo.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    return promo;
  }

}