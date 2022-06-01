/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionApprovalUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalOption;
import com.biperf.core.domain.promotion.PromotionApprovalOptionReason;
import com.biperf.core.domain.promotion.PromotionParticipantApprover;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionApprovalUpdateAssociation.
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
 * <td>sedey</td>
 * <td>August 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionApprovalUpdateAssociation( Promotion promotion )
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
    Promotion detachedPromo = (Promotion)getDetachedDomain();

    // make sure this is product claim promotion, and then get the children and set approval type
    // and all dependent properties
    // on approval type
    if ( attachedPromo instanceof ProductClaimPromotion && detachedPromo instanceof ProductClaimPromotion )
    {
      ProductClaimPromotion attachedPromotion = (ProductClaimPromotion)attachedPromo;
      this.executePromotion( attachedPromotion, detachedPromo );

      for ( Iterator iter = attachedPromotion.getChildPromotions().iterator(); iter.hasNext(); )
      {
        ProductClaimPromotion childPromotion = (ProductClaimPromotion)iter.next();
        // need to copy all properties from parent to all of its children
        this.executePromotion( childPromotion, detachedPromo );
      }
    }
    else
    {
      this.executePromotion( attachedPromo, detachedPromo );
    }

  }

  /**
   * execute the association request and update the specified promotion object into the associated
   * promotion object
   * 
   * @param attachedPromo
   * @param detachedPromo
   */
  private void executePromotion( Promotion attachedPromo, Promotion detachedPromo )
  {
    // set all basic promotion information
    attachedPromo.setApprovalType( detachedPromo.getApprovalType() );
    attachedPromo.setApprovalNodeType( detachedPromo.getApprovalNodeType() );
    attachedPromo.setApprovalHierarchy( detachedPromo.getApprovalHierarchy() );
    attachedPromo.setApproverType( detachedPromo.getApproverType() );
    attachedPromo.setApprovalAutoDelayDays( detachedPromo.getApprovalAutoDelayDays() );
    attachedPromo.setApprovalConditionalClaimCount( detachedPromo.getApprovalConditionalClaimCount() );
    attachedPromo.setApprovalConditionalAmountOperator( detachedPromo.getApprovalConditionalAmountOperator() );
    attachedPromo.setApprovalConditionalAmount( detachedPromo.getApprovalConditionalAmount() );
    if ( !detachedPromo.isNominationPromotion() )
    {
      attachedPromo.setApprovalNodeLevels( detachedPromo.getApprovalNodeLevels() );
    }
    attachedPromo.setApprovalStartDate( detachedPromo.getApprovalStartDate() );
    attachedPromo.setApprovalEndDate( detachedPromo.getApprovalEndDate() );
    attachedPromo.setApproverNode( detachedPromo.getApproverNode() );
    attachedPromo.setApprovalConditionalAmountField( detachedPromo.getApprovalConditionalAmountField() );

    if ( detachedPromo.isNominationPromotion() )
    {
      NominationPromotion detachedNomPromo = (NominationPromotion)detachedPromo;
      NominationPromotion attachedNomPromo = (NominationPromotion)attachedPromo;

      attachedNomPromo.setDefaultApprover( detachedNomPromo.getDefaultApprover() );

      if ( attachedNomPromo.getApprovalNodeLevels() != null && detachedNomPromo.getApprovalNodeLevels() != null
          && !attachedNomPromo.getApprovalNodeLevels().equals( detachedNomPromo.getApprovalNodeLevels() ) )
      {
        attachedNomPromo.setPayoutLevel( null );
        attachedNomPromo.setAwardActive( false );
        if ( !attachedNomPromo.getNominationLevels().isEmpty() )
        {
          attachedNomPromo.getNominationLevels().clear();
        }
      }

      attachedPromo.setApprovalNodeLevels( detachedPromo.getApprovalNodeLevels() );

      // Do not update custom approver options for a live promotion
      // The fields on screen are disabled, and the detached object won't have the info from the
      // file load
      if ( attachedNomPromo.isLive() || attachedNomPromo.isUnderConstruction() )
      {
        // removed.
        if ( attachedNomPromo.getCustomApproverOptions() != null && attachedNomPromo.getCustomApproverOptions().size() > 0 )
        {
          Iterator attachedPromoCustomApproversIter = attachedNomPromo.getCustomApproverOptions().iterator();
          while ( attachedPromoCustomApproversIter.hasNext() )
          {
            ApproverOption attachedPromoCustomApprovers = (ApproverOption)attachedPromoCustomApproversIter.next();
            if ( !detachedNomPromo.getCustomApproverOptions().contains( attachedPromoCustomApprovers ) )
            {
              attachedPromoCustomApproversIter.remove();
            }
          }
        }

        // Iterate over the custom approver options
        if ( detachedNomPromo.getCustomApproverOptions() != null && detachedNomPromo.getCustomApproverOptions().size() > 0 )
        {
          Iterator detachedPromoCustomApproversIter = detachedNomPromo.getCustomApproverOptions().iterator();
          while ( detachedPromoCustomApproversIter.hasNext() )
          {
            ApproverOption detachedPromoCustomApprovers = (ApproverOption)detachedPromoCustomApproversIter.next();

            // If the attachedPromo does not contain the approver option, then add it.
            // Otherwise, no need to do anything.
            if ( !attachedNomPromo.getCustomApproverOptions().contains( detachedPromoCustomApprovers ) )
            {
              // add new obj every time so it will not be referrenced from parent and child, for
              // product
              // claim promotion
              // where you could have parent and child.
              attachedNomPromo.addCustomApproverOptions( detachedPromoCustomApprovers );
            }

          }
        }
      }
    }

    if ( attachedPromo.getPromotionApprovalOptions() != null && attachedPromo.getPromotionApprovalOptions().size() > 0 )
    {
      if ( !attachedPromo.isNominationPromotion() )
      {
        // if there are approval options, iterate over them to see if any need to be deleted.
        Iterator attachedPromoOptions = attachedPromo.getPromotionApprovalOptions().iterator();
        while ( attachedPromoOptions.hasNext() )
        {
          // If the attached promotionApprovalOption does not exist in the detached promotion
          // approval set, then it has been deleted and should be removed.
          PromotionApprovalOption attachedPromoOption = (PromotionApprovalOption)attachedPromoOptions.next();
          if ( !detachedPromo.getPromotionApprovalOptions().contains( attachedPromoOption ) )
          {
            attachedPromoOptions.remove();
          }
          else
          {
            // If the attached promotionApprovalOption does exist in the detached promotion
            // approval set, then a check should be done to see if any of its reasons need
            // to be deleted.
            Iterator detachedPromoOptions = detachedPromo.getPromotionApprovalOptions().iterator();
            while ( detachedPromoOptions.hasNext() )
            {
              // All that is known at this point is that the attached object is contained
              // in the detached set, so first find the specific detached object that matches
              // the current attached object to compare.
              PromotionApprovalOption detachedPromoOption = (PromotionApprovalOption)detachedPromoOptions.next();
              if ( attachedPromoOption.getPromotionApprovalOptionType().getCode().equals( detachedPromoOption.getPromotionApprovalOptionType().getCode() ) )
              {
                if ( attachedPromoOption.getPromotionApprovalOptionReasons() != null && attachedPromoOption.getPromotionApprovalOptionReasons().size() > 0 )
                {
                  // If there are reasons for the attached object iterate over them and see if
                  // the detached object contains them, if not, then the reason has been removed
                  // and should be deleted.
                  Iterator attachedPromoOptionReasons = attachedPromoOption.getPromotionApprovalOptionReasons().iterator();
                  while ( attachedPromoOptionReasons.hasNext() )
                  {
                    PromotionApprovalOptionReason attachedPromoOptionReason = (PromotionApprovalOptionReason)attachedPromoOptionReasons.next();
                    if ( !detachedPromoOption.getPromotionApprovalOptionReasons().contains( attachedPromoOptionReason ) )
                    {
                      attachedPromoOptionReasons.remove();
                    }
                  }
                }
                // Since the object has been found and processed we can break out of this loop
                break;
              }
            }
          }
        }
      } // IsNomination promotion..

    }

    // The last thing to do is iterate over the set of detached objects and add them to the
    // attached object set. If they already exist, they wont be added.
    if ( detachedPromo.getPromotionApprovalOptions() != null && detachedPromo.getPromotionApprovalOptions().size() > 0 )
    {
      Iterator detachedPromotionApprovalOptionsIter = detachedPromo.getPromotionApprovalOptions().iterator();
      while ( detachedPromotionApprovalOptionsIter.hasNext() )
      {
        PromotionApprovalOption detachedPromotionApprovalOption = (PromotionApprovalOption)detachedPromotionApprovalOptionsIter.next();

        // If the attachedPromo already contains the approval option, we still need to iterate
        // through and
        // add any new approval reasons. Otherwise, we can just add the whole approval option.
        if ( attachedPromo.getPromotionApprovalOptions().contains( detachedPromotionApprovalOption ) )
        {
          Iterator attachedPromotionApprovalOptionsIter = attachedPromo.getPromotionApprovalOptions().iterator();
          while ( attachedPromotionApprovalOptionsIter.hasNext() )
          {
            PromotionApprovalOption attachedPromotionApprovalOption = (PromotionApprovalOption)attachedPromotionApprovalOptionsIter.next();
            if ( attachedPromotionApprovalOption.equals( detachedPromotionApprovalOption ) )
            {
              Iterator detachedPromoOptionReasons = detachedPromotionApprovalOption.getPromotionApprovalOptionReasons().iterator();
              while ( detachedPromoOptionReasons.hasNext() )
              {
                PromotionApprovalOptionReason detachedPromoOptionReason = (PromotionApprovalOptionReason)detachedPromoOptionReasons.next();
                attachedPromotionApprovalOption.addPromotionApprovalOptionReason( new PromotionApprovalOptionReason( detachedPromoOptionReason ) );
              }
            }

          }
        }
        else
        {
          // add new obj every time so it will not be referrenced from parent AND child, for product
          // claim promotion
          // where you could have parent and child.
          attachedPromo.addPromotionApprovalOption( new PromotionApprovalOption( detachedPromotionApprovalOption ) );
        }
      }
    }

    // Iterate over the attached promotionParticipantApprovers to see if any should be
    // removed.
    if ( attachedPromo.getPromotionParticipantApprovers() != null && attachedPromo.getPromotionParticipantApprovers().size() > 0 )
    {
      Iterator attachedPromoPaxApproversIter = attachedPromo.getPromotionParticipantApprovers().iterator();
      while ( attachedPromoPaxApproversIter.hasNext() )
      {
        PromotionParticipantApprover attachedPromoPaxApprover = (PromotionParticipantApprover)attachedPromoPaxApproversIter.next();
        if ( !detachedPromo.getPromotionParticipantApprovers().contains( attachedPromoPaxApprover ) )
        {
          attachedPromoPaxApproversIter.remove();
        }
      }
    }

    // Iterate over the promotionParticipantApprovers
    if ( detachedPromo.getPromotionParticipantApprovers() != null && detachedPromo.getPromotionParticipantApprovers().size() > 0 )
    {
      Iterator detachedPromotionParticipantApproversIter = detachedPromo.getPromotionParticipantApprovers().iterator();
      while ( detachedPromotionParticipantApproversIter.hasNext() )
      {
        PromotionParticipantApprover detachedPromotionParticipantApprover = (PromotionParticipantApprover)detachedPromotionParticipantApproversIter.next();

        // If the attachedPromo does not contain the participant approver, then add it.
        // Otherwise, no need to do anything.
        if ( !attachedPromo.getPromotionParticipantApprovers().contains( detachedPromotionParticipantApprover ) )
        {
          // add new obj every time so it will not be referrenced from parent and child, for product
          // claim promotion
          // where you could have parent and child.
          attachedPromo.addPromotionParticipantApprover( new PromotionParticipantApprover( detachedPromotionParticipantApprover ) );
        }
        else
        {
          int index = attachedPromo.getPromotionParticipantApprovers().indexOf( detachedPromotionParticipantApprover );
          PromotionParticipantApprover attachedPromotionParticipantApprover = (PromotionParticipantApprover)attachedPromo.getPromotionParticipantApprovers().get( index );
        }
      }
    }

    // Iterate over the attached promotionParticipantSubmitters to see if any should be
    // removed.
    if ( attachedPromo.getPromotionParticipantSubmitters() != null && attachedPromo.getPromotionParticipantSubmitters().size() > 0 )
    {
      Iterator attachedPromoPaxSubmittersIter = attachedPromo.getPromotionParticipantSubmitters().iterator();
      while ( attachedPromoPaxSubmittersIter.hasNext() )
      {
        PromotionParticipantSubmitter attachedPromoPaxSubmitter = (PromotionParticipantSubmitter)attachedPromoPaxSubmittersIter.next();
        if ( !detachedPromo.getPromotionParticipantSubmitters().contains( attachedPromoPaxSubmitter ) )
        {
          attachedPromoPaxSubmittersIter.remove();
        }
      }
    }

    // Iterate over the promotionParticipantSubmitters
    if ( detachedPromo.getPromotionParticipantSubmitters() != null && detachedPromo.getPromotionParticipantSubmitters().size() > 0 )
    {
      Iterator detachedPromotionParticipantSubmittersIter = detachedPromo.getPromotionParticipantSubmitters().iterator();
      while ( detachedPromotionParticipantSubmittersIter.hasNext() )
      {
        PromotionParticipantSubmitter detachedPromotionParticipantSubmitter = (PromotionParticipantSubmitter)detachedPromotionParticipantSubmittersIter.next();

        // If the attachedPromo does not contain the participant Submitter, then add it.
        // Otherwise, no need to do anything.
        if ( !attachedPromo.getPromotionParticipantSubmitters().contains( detachedPromotionParticipantSubmitter ) )
        {
          attachedPromo.addPromotionParticipantSubmitter( detachedPromotionParticipantSubmitter );
        }
      }
    }
  }

}
