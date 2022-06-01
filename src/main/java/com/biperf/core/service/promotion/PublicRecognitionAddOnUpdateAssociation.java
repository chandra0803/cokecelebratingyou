
package com.biperf.core.service.promotion;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPublicRecognitionAudience;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.budget.BudgetMasterToOverdrawApproverAssociation;
import com.biperf.core.utils.BeanLocator;

public class PublicRecognitionAddOnUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PublicRecognitionAddOnUpdateAssociation( Promotion promotion )
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
    Promotion promotion = (Promotion)attachedDomain;

    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion attachedPromo = (RecognitionPromotion)attachedDomain;

      updateAwardAmount( attachedPromo );

      updatePublicRecognitionBudget( attachedPromo );

      updatePublicRecogAudience( attachedPromo );
    }
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion attachedPromo = (NominationPromotion)attachedDomain;

      updateAwardAmount( attachedPromo );

      updatePublicRecognitionBudget( attachedPromo );

      updatePublicRecogAudience( attachedPromo );
    }
  }

  private void updatePublicRecogAudience( RecognitionPromotion attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    RecognitionPromotion detachedPromo = (RecognitionPromotion)getDetachedDomain();

    attachedPromo.setPublicRecognitionAudienceType( detachedPromo.getPublicRecognitionAudienceType() );

    if ( detachedPromo.getPublicRecognitionAudienceType() != null
        && detachedPromo.getPublicRecognitionAudienceType().equals( PublicRecognitionAudienceType.lookup( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      attachedPromo.getPromotionPublicRecognitionAudiences().clear();
    }
    else
    {
      Set detachedPublicRecogAudience = detachedPromo.getPromotionPublicRecognitionAudiences();
      Iterator detachedPublicRecogAudienceIter = detachedPromo.getPromotionPublicRecognitionAudiences().iterator();

      PromotionPublicRecognitionAudience promotionPublicRecognitionAudience;
      Set attachedPublicRecogAudiences = attachedPromo.getPromotionPublicRecognitionAudiences();

      // If the attached promotion contains any audiences not in the detached set
      // then it should be removed from the promotion
      Iterator attachedPublicRecogAudienceIter = attachedPublicRecogAudiences.iterator();
      while ( attachedPublicRecogAudienceIter.hasNext() )
      {
        promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)attachedPublicRecogAudienceIter.next();
        if ( !detachedPublicRecogAudience.contains( promotionPublicRecognitionAudience ) )
        {
          attachedPublicRecogAudienceIter.remove();
        }
      }

      // This will attempt to add all detached audiences to the promotion.
      // Since it is a set, then only non-duplicates will be added.
      while ( detachedPublicRecogAudienceIter.hasNext() )
      {
        promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)detachedPublicRecogAudienceIter.next();
        promotionPublicRecognitionAudience.setAudience( audienceDAO.getAudienceById( promotionPublicRecognitionAudience.getAudience().getId() ) );
        attachedPromo.addPromotionPublicRecognitionAudience( promotionPublicRecognitionAudience );
      }
    }

  }

  private void updateAwardAmount( RecognitionPromotion attachedPromo )
  {
    RecognitionPromotion detachedPromo = (RecognitionPromotion)getDetachedDomain();

    attachedPromo.setAllowPublicRecognitionPoints( detachedPromo.isAllowPublicRecognitionPoints() );
    attachedPromo.setPublicRecogAwardAmountMin( detachedPromo.getPublicRecogAwardAmountMin() );
    attachedPromo.setPublicRecogAwardAmountMax( detachedPromo.getPublicRecogAwardAmountMax() );
    attachedPromo.setPublicRecogAwardAmountFixed( detachedPromo.getPublicRecogAwardAmountFixed() );
    attachedPromo.setPublicRecogAwardAmountTypeFixed( detachedPromo.isPublicRecogAwardAmountTypeFixed() );
  }

  private void updatePublicRecognitionBudget( RecognitionPromotion attachedPromo )
  {
    RecognitionPromotion detachedPromo = (RecognitionPromotion)getDetachedDomain();

    BudgetMaster detachedBudgetMaster = detachedPromo.getPublicRecogBudgetMaster();
    BudgetMaster attachedBudgetMaster = attachedPromo.getPublicRecogBudgetMaster();

    if ( attachedBudgetMaster == null || detachedBudgetMaster == null )
    {
      if ( detachedBudgetMaster == null || !isPublicRecognitionAddOnActive( detachedPromo ) )
      {
        attachedPromo.setPublicRecogBudgetMaster( null );
      }
      else
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMaster.getId(), associationRequestCollection );
        attachedPromo.setPublicRecogBudgetMaster( budgetMaster );
      }
    }
    else
    {
      Long detachedBudgetMasterId = detachedBudgetMaster.getId();
      if ( detachedBudgetMasterId == null )
      {
        // New Budget Master
        attachedPromo.setPublicRecogBudgetMaster( detachedBudgetMaster );
      }
      else if ( !detachedBudgetMasterId.equals( attachedBudgetMaster.getId() ) )
      {
        // Lookup new master
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMasterId, associationRequestCollection );
        attachedPromo.setPublicRecogBudgetMaster( budgetMaster );
      }
    }
  }

  private boolean isPublicRecognitionAddOnActive( RecognitionPromotion promotion )
  {
    return promotion.isAllowPublicRecognitionPoints();
  }

  private void updatePublicRecogAudience( NominationPromotion attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    NominationPromotion detachedPromo = (NominationPromotion)getDetachedDomain();

    attachedPromo.setPublicRecognitionAudienceType( detachedPromo.getPublicRecognitionAudienceType() );

    if ( detachedPromo.getPublicRecognitionAudienceType() != null
        && detachedPromo.getPublicRecognitionAudienceType().equals( PublicRecognitionAudienceType.lookup( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      attachedPromo.getPromotionPublicRecognitionAudiences().clear();
    }
    else
    {
      Set detachedPublicRecogAudience = detachedPromo.getPromotionPublicRecognitionAudiences();
      Iterator detachedPublicRecogAudienceIter = detachedPromo.getPromotionPublicRecognitionAudiences().iterator();

      PromotionPublicRecognitionAudience promotionPublicRecognitionAudience;
      Set attachedPublicRecogAudiences = attachedPromo.getPromotionPublicRecognitionAudiences();

      // If the attached promotion contains any audiences not in the detached set
      // then it should be removed from the promotion
      Iterator attachedPublicRecogAudienceIter = attachedPublicRecogAudiences.iterator();
      while ( attachedPublicRecogAudienceIter.hasNext() )
      {
        promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)attachedPublicRecogAudienceIter.next();
        if ( !detachedPublicRecogAudience.contains( promotionPublicRecognitionAudience ) )
        {
          attachedPublicRecogAudienceIter.remove();
        }
      }

      // This will attempt to add all detached audiences to the promotion.
      // Since it is a set, then only non-duplicates will be added.
      while ( detachedPublicRecogAudienceIter.hasNext() )
      {
        promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)detachedPublicRecogAudienceIter.next();
        promotionPublicRecognitionAudience.setAudience( audienceDAO.getAudienceById( promotionPublicRecognitionAudience.getAudience().getId() ) );
        attachedPromo.addPromotionPublicRecognitionAudience( promotionPublicRecognitionAudience );
      }
    }

  }

  private void updateAwardAmount( NominationPromotion attachedPromo )
  {
    NominationPromotion detachedPromo = (NominationPromotion)getDetachedDomain();

    attachedPromo.setAllowPublicRecognitionPoints( detachedPromo.isAllowPublicRecognitionPoints() );
    attachedPromo.setPublicRecogAwardAmountMin( detachedPromo.getPublicRecogAwardAmountMin() );
    attachedPromo.setPublicRecogAwardAmountMax( detachedPromo.getPublicRecogAwardAmountMax() );
    attachedPromo.setPublicRecogAwardAmountFixed( detachedPromo.getPublicRecogAwardAmountFixed() );
    attachedPromo.setPublicRecogAwardAmountTypeFixed( detachedPromo.isPublicRecogAwardAmountTypeFixed() );
  }

  private void updatePublicRecognitionBudget( NominationPromotion attachedPromo )
  {
    NominationPromotion detachedPromo = (NominationPromotion)getDetachedDomain();

    BudgetMaster detachedBudgetMaster = detachedPromo.getPublicRecogBudgetMaster();
    BudgetMaster attachedBudgetMaster = attachedPromo.getPublicRecogBudgetMaster();

    if ( attachedBudgetMaster == null || detachedBudgetMaster == null )
    {
      if ( detachedBudgetMaster == null || !isPublicRecognitionAddOnActive( detachedPromo ) )
      {
        attachedPromo.setPublicRecogBudgetMaster( null );
      }
      else
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMaster.getId(), associationRequestCollection );
        attachedPromo.setPublicRecogBudgetMaster( budgetMaster );
      }
    }
    else
    {
      Long detachedBudgetMasterId = detachedBudgetMaster.getId();
      if ( detachedBudgetMasterId == null )
      {
        // New Budget Master
        attachedPromo.setPublicRecogBudgetMaster( detachedBudgetMaster );
      }
      else if ( !detachedBudgetMasterId.equals( attachedBudgetMaster.getId() ) )
      {
        // Lookup new master
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
        associationRequestCollection.add( new BudgetMasterToOverdrawApproverAssociation() );
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( detachedBudgetMasterId, associationRequestCollection );
        attachedPromo.setPublicRecogBudgetMaster( budgetMaster );
      }
    }
  }

  private boolean isPublicRecognitionAddOnActive( NominationPromotion promotion )
  {
    return promotion.isAllowPublicRecognitionPoints();
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

  /**
   * Returns a reference to the Audience DAO service.
   * 
   * @return a reference to the Audience DAO service.
   */
  private AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)BeanLocator.getBean( AudienceDAO.BEAN_NAME );
  }

}
