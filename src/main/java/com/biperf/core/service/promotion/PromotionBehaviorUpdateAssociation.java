/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionBehaviorUpdateAssociation.java,v $
 *
 */

package com.biperf.core.service.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionBehaviorUpdateAssociation <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PromotionBehaviorUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionBehaviorUpdateAssociation( Promotion promotion )
  {
    super( promotion );
  }

  /**
   * execute the association request and update the specified promotion object into the associated
   * promotion object
   * 
   * @param attachedDomain
   */
  public void execute( BaseDomain attachedDomain )
  {
    Promotion attachedPromo = (Promotion)attachedDomain;

    updateRecognitionPromotion( (AbstractRecognitionPromotion)attachedPromo );

  }

  private void updateRecognitionPromotion( AbstractRecognitionPromotion attachedPromo )
  {
    AbstractRecognitionPromotion detachedPromo = (AbstractRecognitionPromotion)getDetachedDomain();
    Set<PromotionBehavior> behaviorsToAdd = new HashSet<PromotionBehavior>();
    for ( Iterator iterator = detachedPromo.getPromotionBehaviors().iterator(); iterator.hasNext(); )
    {
      PromotionBehavior promotionBehavior = (PromotionBehavior)iterator.next();
      behaviorsToAdd.add( promotionBehavior );
    }

    // remove all of the Behaviors that have been unchecked.
    Iterator attachedBehaviorsIterator = attachedPromo.getPromotionBehaviors().iterator();
    while ( attachedBehaviorsIterator.hasNext() )
    {
      PromotionBehavior promotionBehavior = (PromotionBehavior)attachedBehaviorsIterator.next();
      if ( !behaviorsToAdd.contains( promotionBehavior ) )
      {
        attachedBehaviorsIterator.remove();
      }
      else
      {
        // this is an existing behavior type - remove it from the add set
        behaviorsToAdd.remove( promotionBehavior );
      }
    }

    // now add in the new Behaviors
    Iterator behaviorsToAddIterator = behaviorsToAdd.iterator();
    while ( behaviorsToAddIterator.hasNext() )
    {
      PromotionBehavior promoBehavior = (PromotionBehavior)behaviorsToAddIterator.next();
      if ( attachedPromo.isRecognitionPromotion() )
      {
        PromoRecognitionBehaviorType behaviorType = (PromoRecognitionBehaviorType)promoBehavior.getPromotionBehaviorType();
        attachedPromo.addPromotionBehavior( behaviorType );
      }
      else
      {
        PromoNominationBehaviorType behaviorType = (PromoNominationBehaviorType)promoBehavior.getPromotionBehaviorType();
        String behaviorOrder = promoBehavior.getBehaviorOrder();
        attachedPromo.addPromotionBehavior( behaviorType, behaviorOrder );
      }

    }

    attachedPromo.setBehaviorActive( detachedPromo.isBehaviorActive() );
    if ( attachedPromo.isRecognitionPromotion() )
    {
      ( (RecognitionPromotion)attachedPromo ).setBehaviorRequired( ( (RecognitionPromotion)detachedPromo ).isBehaviorRequired() );
    }
  }

}
