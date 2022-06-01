/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/PromotionParticipantPartnerUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionParticipantPartnerUpdateAssociation.
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
 * <td>reddy</td>
 * <td>Mar 3, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionParticipantPartnerUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionParticipantPartnerUpdateAssociation( Promotion promotion )
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
    GoalQuestPromotion attachedPromo = (GoalQuestPromotion)attachedDomain;
    GoalQuestPromotion detachedPromo = (GoalQuestPromotion)getDetachedDomain();

    this.executePromotion( attachedPromo, detachedPromo );

  }

  /**
   * execute the association request and update the specified promotion object into the associated
   * promotion object
   * 
   * @param attachedPromo
   * @param detachedPromo
   */
  private void executePromotion( GoalQuestPromotion attachedPromo, GoalQuestPromotion detachedPromo )
  {

    // Iterate over the attached promotionParticipantApprovers to see if any should be
    // removed.
    if ( attachedPromo.getPromotionParticipantPartners() != null && attachedPromo.getPromotionParticipantPartners().size() > 0 )
    {
      Iterator attachedPromoPaxApproversIter = attachedPromo.getPromotionParticipantPartners().iterator();
      while ( attachedPromoPaxApproversIter.hasNext() )
      {
        ParticipantPartner attachedPromoPaxApprover = (ParticipantPartner)attachedPromoPaxApproversIter.next();
        if ( !detachedPromo.getPromotionParticipantPartners().contains( attachedPromoPaxApprover ) )
        {
          attachedPromoPaxApproversIter.remove();
        }
      }
    }

    // Iterate over the promotionParticipantApprovers
    if ( detachedPromo.getPromotionParticipantPartners() != null && detachedPromo.getPromotionParticipantPartners().size() > 0 )
    {
      Iterator detachedPromotionParticipantApproversIter = detachedPromo.getPromotionParticipantPartners().iterator();
      while ( detachedPromotionParticipantApproversIter.hasNext() )
      {
        ParticipantPartner detachedPromotionParticipantApprover = (ParticipantPartner)detachedPromotionParticipantApproversIter.next();

        // If the attachedPromo does not contain the participant approver, then add it.
        // Otherwise, no need to do anything.
        if ( !attachedPromo.getPromotionParticipantPartners().contains( detachedPromotionParticipantApprover ) )
        {
          // add new obj every time so it will not be referrenced from parent and child, for product
          // claim promotion
          // where you could have parent and child.
          attachedPromo.addPromotionParticipantPartners( detachedPromotionParticipantApprover );
        }

      }
    }
  }

}
