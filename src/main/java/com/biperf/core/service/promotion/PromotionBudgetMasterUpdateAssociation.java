/**
 * 
 */

package com.biperf.core.service.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.WellnessPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.budget.BudgetMasterToOverdrawApproverAssociation;
import com.biperf.core.utils.BeanLocator;

/**
 * PromotionBudgetMasterUpdateAssociation.
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
 * <td>Oct 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionBudgetMasterUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionBudgetMasterUpdateAssociation( Promotion promotion )
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

    updatePromotionBudgetMaster( attachedPromo );
    updatePromotionCashBudgetMaster( attachedPromo );
  }

  private void updatePromotionBudgetMaster( Promotion attachedPromo )
  {
    Promotion detachedPromo = (Promotion)getDetachedDomain();
    BudgetMaster attachedBudgetMaster = attachedPromo.getBudgetMaster();
    BudgetMaster detachedBudgetMaster = detachedPromo.getBudgetMaster();

    updatePointsPromotion( attachedPromo, detachedPromo, attachedBudgetMaster, detachedBudgetMaster );
  }

  private void updatePromotionCashBudgetMaster( Promotion attachedPromo )
  {
    Promotion detachedPromo = (Promotion)getDetachedDomain();
    BudgetMaster attachedBudgetMaster = attachedPromo.getCashBudgetMaster();
    BudgetMaster detachedBudgetMaster = detachedPromo.getCashBudgetMaster();

    updateCashPromotion( attachedPromo, detachedPromo, attachedBudgetMaster, detachedBudgetMaster );
  }

  private void updatePointsPromotion( Promotion attachedPromo, Promotion detachedPromo, BudgetMaster attachedBudgetMaster, BudgetMaster detachedBudgetMaster )
  {
    if ( attachedBudgetMaster == null || detachedBudgetMaster == null )
    {
      if ( detachedBudgetMaster == null || !isAwardActive( detachedPromo ) )
      {
        if ( attachedBudgetMaster != null )
        {
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
          associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
          BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( attachedBudgetMaster.getId(), associationRequestCollection );

          if ( budgetMaster != null && budgetMaster.getBudgetSegments() != null )
          {
            for ( BudgetSegment budgetSegment : budgetMaster.getBudgetSegments() )
            {
              for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
              {
                promotionBudgetSweep.setStatus( false );
              }
            }
          }
        }
        attachedPromo.setBudgetMaster( null );
      }
      else
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMaster.getId(), associationRequestCollection );
        if ( budgetMaster != null && budgetMaster.getBudgetSegments() != null )
        {
          for ( BudgetSegment budgetSegment : budgetMaster.getBudgetSegments() )
          {
            for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
            {
              promotionBudgetSweep.setRecognitionPromotion( (RecognitionPromotion)detachedPromo );
              promotionBudgetSweep.setStatus( true );
            }
          }
        }
        attachedPromo.setBudgetMaster( budgetMaster );
      }
    }
    else
    {
      Long detachedBudgetMasterId = detachedBudgetMaster.getId();
      Long attachedBudgetMasterId = attachedBudgetMaster.getId();

      if ( detachedBudgetMasterId == null )
      {
        // New Budget Master
        attachedPromo.setBudgetMaster( detachedBudgetMaster );
      }
      else if ( !detachedBudgetMasterId.equals( attachedBudgetMaster.getId() ) )
      {
        // Lookup new master
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMasterId, associationRequestCollection );

        if ( budgetMaster != null && budgetMaster.getBudgetSegments() != null )
        {
          for ( BudgetSegment budgetSegment : budgetMaster.getBudgetSegments() )
          {
            for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
            {
              promotionBudgetSweep.setRecognitionPromotion( (RecognitionPromotion)detachedPromo );
              promotionBudgetSweep.setStatus( true );
            }
          }
        }

        AssociationRequestCollection associationRequestCollection1 = new AssociationRequestCollection();
        associationRequestCollection1.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection1.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster1 = getBudgetMasterService().getBudgetMasterById( attachedBudgetMasterId, associationRequestCollection );

        if ( budgetMaster1 != null && budgetMaster1.getBudgetSegments() != null )
        {
          for ( BudgetSegment budgetSegment : budgetMaster1.getBudgetSegments() )
          {
            for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
            {
              promotionBudgetSweep.setStatus( false );
            }
          }
        }
        attachedPromo.setBudgetMaster( budgetMaster );
      }
    }
  }

  // Only difference between this and updatePointsPromotion is *which* budget master it sets.
  // This calls setCashBudgetMaster. Really would've liked to keep one method and pass in a Consumer
  // method reference...
  private void updateCashPromotion( Promotion attachedPromo, Promotion detachedPromo, BudgetMaster attachedBudgetMaster, BudgetMaster detachedBudgetMaster )
  {
    if ( attachedBudgetMaster == null || detachedBudgetMaster == null )
    {
      if ( detachedBudgetMaster == null || !isAwardActive( detachedPromo ) )
      {
        if ( attachedBudgetMaster != null )
        {
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
          associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
          BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( attachedBudgetMaster.getId(), associationRequestCollection );

          if ( budgetMaster != null && budgetMaster.getBudgetSegments() != null )
          {
            for ( BudgetSegment budgetSegment : budgetMaster.getBudgetSegments() )
            {
              for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
              {
                promotionBudgetSweep.setStatus( false );
              }
            }
          }
        }
        attachedPromo.setCashBudgetMaster( null );
      }
      else
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMaster.getId(), associationRequestCollection );
        if ( budgetMaster != null && budgetMaster.getBudgetSegments() != null )
        {
          for ( BudgetSegment budgetSegment : budgetMaster.getBudgetSegments() )
          {
            for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
            {
              promotionBudgetSweep.setRecognitionPromotion( (RecognitionPromotion)detachedPromo );
              promotionBudgetSweep.setStatus( true );
            }
          }
        }
        attachedPromo.setCashBudgetMaster( budgetMaster );
      }
    }
    else
    {
      Long detachedBudgetMasterId = detachedBudgetMaster.getId();
      Long attachedBudgetMasterId = attachedBudgetMaster.getId();

      if ( detachedBudgetMasterId == null )
      {
        // New Budget Master
        attachedPromo.setCashBudgetMaster( detachedBudgetMaster );
      }
      else if ( !detachedBudgetMasterId.equals( attachedBudgetMaster.getId() ) )
      {
        // Lookup new master
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMasterId, associationRequestCollection );

        if ( budgetMaster != null && budgetMaster.getBudgetSegments() != null )
        {
          for ( BudgetSegment budgetSegment : budgetMaster.getBudgetSegments() )
          {
            for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
            {
              promotionBudgetSweep.setRecognitionPromotion( (RecognitionPromotion)detachedPromo );
              promotionBudgetSweep.setStatus( true );
            }
          }
        }

        AssociationRequestCollection associationRequestCollection1 = new AssociationRequestCollection();
        associationRequestCollection1.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection1.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster1 = getBudgetMasterService().getBudgetMasterById( attachedBudgetMasterId, associationRequestCollection );

        if ( budgetMaster1 != null && budgetMaster1.getBudgetSegments() != null )
        {
          for ( BudgetSegment budgetSegment : budgetMaster1.getBudgetSegments() )
          {
            for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
            {
              promotionBudgetSweep.setStatus( false );
            }
          }
        }
        attachedPromo.setCashBudgetMaster( budgetMaster );
      }
    }
  }

  private boolean isAwardActive( Promotion promotion )
  {
    if ( promotion.isQuizPromotion() )
    {
      return ( (QuizPromotion)promotion ).isAwardActive();
    }
    if ( promotion.isWellnessPromotion() )
    {
      return ( (WellnessPromotion)promotion ).isAwardActive();
    }
    if ( promotion.isProductClaimPromotion() )
    {
      return true; // ProductClaimPromotion uses Payout which is equivalent to award active
    }
    return ( (AbstractRecognitionPromotion)promotion ).isAwardActive();
  }

  /**
   * Retrieves a Budget Master Service
   * 
   * @return BudgetMasterService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)BeanLocator.getBean( BudgetMasterService.BEAN_NAME );
  } // end getBudgetMasterService

}
