
package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.UpdateAssociationRequest;

public class PromotionBadgeUpdateAssociation extends UpdateAssociationRequest
{

  public PromotionBadgeUpdateAssociation( Promotion promotion )
  {
    super( promotion );
  }

  @Override
  public void execute( BaseDomain attachedDomain )
  {
    Promotion attachedPromo = (Promotion)attachedDomain;
    Promotion detachedPromo = (Promotion)getDetachedDomain();

    if ( attachedPromo instanceof QuizPromotion && detachedPromo instanceof QuizPromotion )
    {
      attachedPromo = (QuizPromotion)attachedDomain;
      detachedPromo = (QuizPromotion)getDetachedDomain();
    }
    else if ( attachedPromo instanceof SSIPromotion && detachedPromo instanceof SSIPromotion )
    {
      attachedPromo = (SSIPromotion)attachedDomain;
      detachedPromo = (SSIPromotion)getDetachedDomain();
    }

    this.executePromotion( attachedPromo, detachedPromo );
  }

  public void executePromotion( Promotion attachedPromo, Promotion detachedPromo )
  {
    Badge attachedBadge = attachedPromo.getBadge();
    Badge detachedBadge = detachedPromo.getBadge();

    // Remove any badges from the attached promotion that are not present in the detached
    if ( attachedBadge != null && attachedBadge.getBadgeRules() != null && !attachedBadge.getBadgeRules().isEmpty() )
    {
      if ( detachedBadge != null )
      {
        Iterator attachedBadgeIter = attachedBadge.getBadgeRules().iterator();
        while ( attachedBadgeIter.hasNext() )
        {
          BadgeRule attachedBadgeRule = (BadgeRule)attachedBadgeIter.next();
          Iterator detachedBadgeIter = detachedBadge.getBadgeRules().iterator();
          boolean found = false;
          while ( detachedBadgeIter.hasNext() )
          {
            BadgeRule detachedBadgeRule = (BadgeRule)detachedBadgeIter.next();
            if ( detachedBadgeRule.getId() != null && attachedBadgeRule.getId().equals( detachedBadgeRule.getId() ) )
            {
              attachedBadgeRule.setBadgeName( detachedBadgeRule.getBadgeName() );
              found = true;
              break;
            }
          }
          if ( !found )
          {
            attachedBadgeIter.remove();
          }
        }
      }
      else
      {
        attachedBadge.getBadgeRules().clear();
      }
    }

    // Add all the newly added badges from the detached Promotion Object to the attached
    if ( detachedBadge != null && detachedBadge.getBadgeRules() != null && detachedBadge.getBadgeRules().size() > 0 )
    {
      if ( attachedBadge != null )
      {
        Iterator detachedBadgeIter = detachedBadge.getBadgeRules().iterator();
        while ( detachedBadgeIter.hasNext() )
        {
          BadgeRule detachedBadgeRule = (BadgeRule)detachedBadgeIter.next();
          Iterator attachedBadgeIter = attachedBadge.getBadgeRules().iterator();
          boolean found = false;
          while ( attachedBadgeIter.hasNext() )
          {
            BadgeRule attachedBadgeRule = (BadgeRule)attachedBadgeIter.next();
            if ( detachedBadgeRule.getId() != null && detachedBadgeRule.getId().equals( attachedBadgeRule.getId() ) )
            {
              found = true;
              break;
            }
          }
          if ( !found )
          {
            attachedBadge.addBadgeRule( detachedBadgeRule );
          }
        }
      }
      else
      {
        attachedPromo.setBadge( detachedBadge );
      }
    }
    else if ( attachedBadge != null && attachedBadge.getBadgeRules() != null )
    {
      attachedBadge.getBadgeRules().clear();
    }
  }
}
