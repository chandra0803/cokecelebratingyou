
package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.BeanLocator;

public class PromotionBadgeRulesUpdateAssociation extends UpdateAssociationRequest
{

  public PromotionBadgeRulesUpdateAssociation( Promotion promotion )
  {
    super( promotion );
  }

  @Override
  public void execute( BaseDomain attachedDomain )
  {
    Badge attachedBadge = (Badge)attachedDomain;
    Badge detachedBadge = (Badge)getDetachedDomain();

    // Add all the newly added badges from the detached Promotion Object to the attached
    if ( detachedBadge.getBadgeRules() != null && detachedBadge.getBadgeRules().size() > 0 )
    {
      Iterator<BadgeRule> detachedBadgeIter = detachedBadge.getBadgeRules().iterator();

      int attacheBadgeRuleListSize = attachedBadge.getBadgeRules().size();
      while ( detachedBadgeIter.hasNext() )
      {
        BadgeRule detachedBadgeRule = (BadgeRule)detachedBadgeIter.next();

        if ( attacheBadgeRuleListSize > 0 )
        {
          // update the existing badge rules
          for ( Iterator<BadgeRule> attachedBadgeIter = attachedBadge.getBadgeRules().iterator(); attachedBadgeIter.hasNext(); )
          {
            BadgeRule attachedBadgeRule = (BadgeRule)attachedBadgeIter.next();
            if ( detachedBadgeRule.getId() != null && detachedBadgeRule.getId().equals( attachedBadgeRule.getId() ) )
            {
              if ( detachedBadge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.PROGRESS ) )
              {
                // attachedBadgeRule.setMaximumQualifier( detachedBadgeRule.getMaximumQualifier() );
                // attachedBadgeRule.setBadgeLibraryCMKey( detachedBadgeRule.getBadgeLibraryCMKey()
                // );
                attachedBadgeRule.setBadgeName( detachedBadgeRule.getBadgeName() );
                attachedBadgeRule.setEligibleForSweepstake( detachedBadgeRule.isEligibleForSweepstake() );
                attachedBadgeRule.setBadgePoints( detachedBadgeRule.getBadgePoints() );
                attachedBadgeRule.setBadgeDescription( detachedBadgeRule.getBadgeDescription() );
              }
              else if ( detachedBadge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.BEHAVIOR ) )
              {
                // attachedBadgeRule.setBehaviorName( detachedBadgeRule.getBehaviorName() );
                // attachedBadgeRule.setBadgeLibraryCMKey( detachedBadgeRule.getBadgeLibraryCMKey()
                // );
                attachedBadgeRule.setBadgeName( detachedBadgeRule.getBadgeName() );
                attachedBadgeRule.setEligibleForSweepstake( detachedBadgeRule.isEligibleForSweepstake() );
                attachedBadgeRule.setBadgePoints( detachedBadgeRule.getBadgePoints() );
                attachedBadgeRule.setBadgeDescription( detachedBadgeRule.getBadgeDescription() );
              }
              else if ( detachedBadge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.EARNED_OR_NOT_EARNED ) )
              {
                attachedBadgeRule.setBadgeName( detachedBadgeRule.getBadgeName() );
                attachedBadgeRule.setBadgeDescription( detachedBadgeRule.getBadgeDescription() );
              }
              else if ( detachedBadge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.FILELOAD ) )
              {
                String isShowFileloadNoPromoDiv = "N";
                for ( Iterator<BadgePromotion> badgePromotionIter = detachedBadge.getBadgePromotions().iterator(); badgePromotionIter.hasNext(); )
                {
                  BadgePromotion bp = badgePromotionIter.next();
                  Promotion promotion = getPromotionDao().getPromotionById( bp.getEligiblePromotion().getId() );
                  if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) || promotion.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
                  {
                    isShowFileloadNoPromoDiv = "Y";
                    break;
                  }
                }

                if ( detachedBadge.getBadgePromotions() != null && detachedBadge.getBadgePromotions().size() > 0 && "N".equals( isShowFileloadNoPromoDiv ) )
                {
                  // attachedBadgeRule.setBadgeLibraryCMKey(
                  // detachedBadgeRule.getBadgeLibraryCMKey() );
                  attachedBadgeRule.setBadgeName( detachedBadgeRule.getBadgeName() );
                  attachedBadgeRule.setBadgeDescription( detachedBadgeRule.getBadgeDescription() );
                }
                else
                {
                  // attachedBadgeRule.setBadgeLibraryCMKey(
                  // detachedBadgeRule.getBadgeLibraryCMKey() );
                  attachedBadgeRule.setBadgeName( detachedBadgeRule.getBadgeName() );
                  attachedBadgeRule.setEligibleForSweepstake( detachedBadgeRule.isEligibleForSweepstake() );
                  attachedBadgeRule.setBadgePoints( detachedBadgeRule.getBadgePoints() );
                  attachedBadgeRule.setBadgeDescription( detachedBadgeRule.getBadgeDescription() );
                }
              }
              detachedBadgeIter.remove();
              break;
            }
          }
        }
        else
        {
          detachedBadgeIter.remove();
          attachedBadge.addBadgeRule( detachedBadgeRule );
        }
      }

      for ( Iterator<BadgeRule> detachedBadgeIterator = detachedBadge.getBadgeRules().iterator(); detachedBadgeIterator.hasNext(); )
      {
        BadgeRule detachedBadgeRule = (BadgeRule)detachedBadgeIterator.next();
        attachedBadge.addBadgeRule( detachedBadgeRule );
      }

    }
  }

  private PromotionDAO getPromotionDao()
  {
    return (PromotionDAO)BeanLocator.getBean( PromotionDAO.BEAN_NAME );
  }
}
