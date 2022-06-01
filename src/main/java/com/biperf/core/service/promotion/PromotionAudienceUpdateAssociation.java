/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionAudienceUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPartnerAudience;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BeanLocator;

/**
 * PromotionAudienceUpdateAssociation.
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
 * <td>sathish</td>
 * <td>September 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAudienceUpdateAssociation extends UpdateAssociationRequest
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If true, then when the application adds a primary audience to a parent promotion, it also adds
   * the primary audience to the parent promotion's child promotions.
   */
  private boolean addPrimaryAudiencesToChildPromotions;

  /**
   * If true, then when the application adds a secondary audience to a parent promotion, it also
   * adds the secondary audience to the parent promotion's child promotions.
   */
  private boolean addSecondaryAudiencesToChildPromotions;

  /**
   * For Challengepoint, if true, this will allow Node Owners to participant and earn points
   * They will be included in the audience
   */
  private boolean managerCanSelect;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>PromotionAudienceUpdateAssociation</code> object.
   * 
   * @param detachedPromotion a detached {@link Promotion} object.
   */
  public PromotionAudienceUpdateAssociation( Promotion detachedPromotion )
  {
    super( detachedPromotion );
  }

  /**
   * Use the detached {@link Promotion} object to update the attached {@link Promotion} object.
   * 
   * @param attachedDomain the attached version of the {@link Promotion} object.
   */
  public void execute( BaseDomain attachedDomain )
  {
    Promotion attachedPromotion = (Promotion)attachedDomain;

    if ( attachedPromotion.isProductClaimPromotion() )
    {
      updateProductClaimPromotion( (ProductClaimPromotion)attachedPromotion );
    }
    else if ( attachedPromotion.isRecognitionPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
      updateSecondaryAudience( attachedPromotion );
      updateRecognitionPromotion( (RecognitionPromotion)attachedPromotion );
    }
    else if ( attachedPromotion.isNominationPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
      updateSecondaryAudience( attachedPromotion );
    }
    else if ( attachedPromotion.isEngagementPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
      updateSecondaryAudience( attachedPromotion );
    }
    else if ( attachedPromotion.isSSIPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
      updateSecondaryAudience( attachedPromotion );
    }
    else if ( attachedPromotion.isQuizPromotion() || attachedPromotion.isDIYQuizPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
    }
    else if ( attachedPromotion.isWellnessPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
    }
    else if ( attachedPromotion.isSurveyPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
    }
    else if ( attachedPromotion.isGoalQuestOrChallengePointPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
      updateSecondaryAudience( attachedPromotion );
      updateSelfEnrollInfo( attachedPromotion );
      updatePartnerAudience( attachedPromotion );
      if ( attachedPromotion.isChallengePointPromotion() )
      {
        ( (ChallengePointPromotion)attachedPromotion ).setManagerCanSelect( new Boolean( managerCanSelect ) );
      }
    }
    else if ( attachedPromotion.isThrowdownPromotion() )
    {
      updatePrimaryAudience( attachedPromotion );
      updateDivisionAudiences( (ThrowdownPromotion)attachedPromotion );
    }
  }

  private void updateDivisionAudiences( ThrowdownPromotion attachedPromotion )
  {
    DivisionDAO divisionDAO = getDivisionDAO();
    ThrowdownPromotion detachedPromo = (ThrowdownPromotion)getDetachedDomain();

    // now reassign the new ones
    Iterator<Division> detachedDivisionIterator = detachedPromo.getDivisions().iterator();
    while ( detachedDivisionIterator.hasNext() )
    {
      Division detachedDivision = detachedDivisionIterator.next();
      Division attachedDivision = new Division();
      if ( detachedDivision.getId() == null || detachedDivision.getId().longValue() < 1 )
      {
        detachedDivision.setMinimumQualifier( BigDecimal.ZERO );
        attachedPromotion.getDivisions().add( detachedDivision );
        attachedDivision.setPromotion( attachedPromotion );
        saveDivisionNameInCM( attachedPromotion, attachedDivision, detachedDivision );
        attachedDivision = divisionDAO.save( detachedDivision );
        detachedDivision.setId( attachedDivision.getId() );
      }
      else
      {
        for ( Division attachedDiv : attachedPromotion.getDivisions() )
        {
          if ( detachedDivision.getId() != null && attachedDiv.getId().equals( detachedDivision.getId() ) )
          {
            attachedDivision = attachedDiv;
            saveDivisionNameInCM( attachedPromotion, attachedDivision, detachedDivision );
            attachedDivision.getCompetitorsAudience().clear();
            attachedDivision.getCompetitorsAudience().addAll( detachedDivision.getCompetitorsAudience() );
          }
        }
      }
    }
  }

  private void saveDivisionNameInCM( ThrowdownPromotion attachedPromotion, Division attachedDivision, Division detachedDivision )
  {
    // CM updates
    try
    {
      getPromotionService().saveDivisionNamesInCM( attachedPromotion, detachedDivision );
    }
    catch( ServiceErrorException e )
    {
      throw new BeaconRuntimeException( e );
    }
  }

  /**
   * Uses attached and detached versions of a {@link Promotion} object to perform additional
   * validation on the object.
   * 
   * @param attachedDomain the attached version of the {@link Promotion} object.
   * @throws ServiceErrorExceptionWithRollback if a validation error occurs.
   */
  public void validate( BaseDomain attachedDomain ) throws ServiceErrorExceptionWithRollback
  {
    List validationErrors = new ArrayList();

    // Currently, only product claim promotions can have child promotions.
    Object detachedDomain = getDetachedDomain();
    if ( attachedDomain instanceof ProductClaimPromotion && detachedDomain instanceof ProductClaimPromotion )
    {
      ProductClaimPromotion attachedPromotion = (ProductClaimPromotion)attachedDomain;
      ProductClaimPromotion detachedPromotion = (ProductClaimPromotion)detachedDomain;

      if ( attachedPromotion.getChildrenCount() > 0 ) // This is a parent promotion with children.
      {
        PrimaryAudienceType primaryAudienceTypeFromAttachedPromotion = attachedPromotion.getPrimaryAudienceType();
        PrimaryAudienceType primaryAudienceTypeFromDetachedPromotion = detachedPromotion.getPrimaryAudienceType();

        if ( primaryAudienceTypeFromAttachedPromotion != null && primaryAudienceTypeFromAttachedPromotion.isSpecifyAudienceType() && primaryAudienceTypeFromDetachedPromotion != null
            && primaryAudienceTypeFromDetachedPromotion.isSpecifyAudienceType() )
        {
          // When the application removes primary audiences from a parent promotion, it also removes
          // them from the parent's child promotions. Ensure that each child promotion will have at
          // least one primary audience after the application removes primary audiences from the
          // parent promotion.
          Set audiencesFromAttachedPromotion = attachedPromotion.getPrimaryAudiences();
          Set audiencesFromDetachedPromotion = detachedPromotion.getPrimaryAudiences();

          Set audiencesToRemove = getAudiencesToRemove( audiencesFromAttachedPromotion, audiencesFromDetachedPromotion );

          if ( audiencesToRemove.size() > 0 )
          {
            for ( Iterator iter = attachedPromotion.getChildPromotions().iterator(); iter.hasNext(); )
            {
              ProductClaimPromotion childPromotion = (ProductClaimPromotion)iter.next();

              if ( childPromotion.getPrimaryAudienceType() != null && childPromotion.getPrimaryAudienceType().isSpecificParentAudiencesType() )
              {
                Set childAudiences = new HashSet();
                childAudiences.addAll( childPromotion.getPrimaryAudiences() );
                childAudiences.removeAll( audiencesToRemove );

                // Constraint: A product claim promotion whose primary audience type is "specify
                // audience" must have at least one primary audience.
                if ( childAudiences.size() == 0 )
                {
                  validationErrors.add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_CHILD_PROMOTION_HAS_NO_SUBMITTER_AUDIENCE, childPromotion.getName() ) );
                }
              }
            }
          }
        }

        SecondaryAudienceType secondaryAudienceTypeFromAttachedPromotion = attachedPromotion.getSecondaryAudienceType();
        SecondaryAudienceType secondaryAudienceTypeFromDetachedPromotion = detachedPromotion.getSecondaryAudienceType();

        if ( secondaryAudienceTypeFromAttachedPromotion != null && secondaryAudienceTypeFromAttachedPromotion.isSpecifyAudienceType() && secondaryAudienceTypeFromDetachedPromotion != null
            && secondaryAudienceTypeFromDetachedPromotion.isSpecifyAudienceType() )
        {
          // When the application removes secondary audiences from a parent promotion, it also
          // removes them from the parent's child promotions. Ensure that each child promotion will
          // have at least one secondary audience after the application removes secondary audiences
          // from the parent promotion.
          Set audiencesFromAttachedPromotion = attachedPromotion.getSecondaryAudiences();
          Set audiencesFromDetachedPromotion = detachedPromotion.getSecondaryAudiences();

          Set audiencesToBeRemoved = getAudiencesToRemove( audiencesFromAttachedPromotion, audiencesFromDetachedPromotion );

          if ( audiencesToBeRemoved.size() > 0 )
          {
            for ( Iterator iter = attachedPromotion.getChildPromotions().iterator(); iter.hasNext(); )
            {
              ProductClaimPromotion childPromotion = (ProductClaimPromotion)iter.next();

              if ( childPromotion.getSecondaryAudienceType() != null && childPromotion.getSecondaryAudienceType().isSpecifyAudienceType() )
              {
                Set childAudiences = new HashSet();
                childAudiences.addAll( childPromotion.getSecondaryAudiences() );
                childAudiences.removeAll( audiencesToBeRemoved );

                // Constraint: A product claim promotion whose secondary audience type is "specify
                // audience" must have at least one secondary audience.
                if ( childAudiences.size() == 0 )
                {
                  validationErrors.add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_CHILD_PROMOTION_HAS_NO_TEAM_AUDIENCE, childPromotion.getName() ) );
                }
              }
            }
          }
        }
      }

      if ( !validationErrors.isEmpty() )
      {
        throw new ServiceErrorExceptionWithRollback( validationErrors );
      }
    }
  }

  public void setAddPrimaryAudiencesToChildPromotions( boolean addPrimaryAudiencesToChildPromotions )
  {
    this.addPrimaryAudiencesToChildPromotions = addPrimaryAudiencesToChildPromotions;
  }

  public void setAddSecondaryAudiencesToChildPromotions( boolean addSecondaryAudiencesToChildPromotions )
  {
    this.addSecondaryAudiencesToChildPromotions = addSecondaryAudiencesToChildPromotions;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Adds to child promotions the primary audiences that were added to the parent promotion.
   * 
   * @param attachedPromotion the attached version of the promotion.
   * @param detachedPromotion the detached version of the promotion.
   */
  private void addPrimaryAudiencesToChildPromotions( ProductClaimPromotion attachedPromotion, ProductClaimPromotion detachedPromotion )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    if ( attachedPromotion.getChildrenCount() > 0 )
    {
      if ( attachedPromotion.getPrimaryAudienceType().isSpecifyAudienceType() && detachedPromotion.getPrimaryAudienceType().isSpecifyAudienceType() )
      {
        Set audiencesToAdd = getAudiencesToAdd( attachedPromotion.getPrimaryAudiences(), detachedPromotion.getPrimaryAudiences() );
        if ( audiencesToAdd.size() > 0 )
        {
          for ( Iterator promotionIter = attachedPromotion.getChildPromotions().iterator(); promotionIter.hasNext(); )
          {
            ProductClaimPromotion childPromotion = (ProductClaimPromotion)promotionIter.next();
            if ( childPromotion.getPrimaryAudienceType().isSpecificParentAudiencesType() )
            {
              for ( Iterator audienceIter = audiencesToAdd.iterator(); audienceIter.hasNext(); )
              {
                Long audienceId = ( (Audience)audienceIter.next() ).getId();
                Audience audience = audienceDAO.getAudienceById( audienceId );
                childPromotion.getPromotionPrimaryAudiences().add( new PromotionPrimaryAudience( audience, childPromotion ) );
              }
            }
          }
        }
      }
    }
  }

  /**
   * Removes from child promotions the primary audiences that were removed from the parent
   * promotion.
   * 
   * @param attachedPromotion the attached version of the promotion.
   * @param detachedPromotion the detached version of the promotion.
   */
  private void removePrimaryAudiencesFromChildPromotions( ProductClaimPromotion attachedPromotion, ProductClaimPromotion detachedPromotion )
  {
    if ( attachedPromotion.getChildrenCount() > 0 )
    {
      if ( attachedPromotion.getPrimaryAudienceType().isSpecifyAudienceType() && detachedPromotion.getPrimaryAudienceType().isSpecifyAudienceType() )
      {
        Set audiencesToRemove = getAudiencesToRemove( attachedPromotion.getPrimaryAudiences(), detachedPromotion.getPrimaryAudiences() );
        if ( audiencesToRemove.size() > 0 )
        {
          for ( Iterator promotionIter = attachedPromotion.getChildPromotions().iterator(); promotionIter.hasNext(); )
          {
            ProductClaimPromotion childPromotion = (ProductClaimPromotion)promotionIter.next();
            if ( childPromotion.getPrimaryAudienceType().isSpecificParentAudiencesType() )
            {
              for ( Iterator audienceIter = childPromotion.getPromotionPrimaryAudiences().iterator(); audienceIter.hasNext(); )
              {
                PromotionPrimaryAudience childAudience = (PromotionPrimaryAudience)audienceIter.next();
                if ( audiencesToRemove.contains( childAudience.getAudience() ) )
                {
                  audienceIter.remove();
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * Adds to child promotions the secondary audiences that were added to the parent promotion.
   * 
   * @param attachedPromotion the attached version of the promotion.
   * @param detachedPromotion the detached version of the promotion.
   */
  private void addSecondaryAudiencesToChildPromotions( ProductClaimPromotion attachedPromotion, ProductClaimPromotion detachedPromotion )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    if ( attachedPromotion.getChildrenCount() > 0 )
    {
      if ( attachedPromotion.getSecondaryAudienceType().isSpecifyAudienceType() && detachedPromotion.getSecondaryAudienceType().isSpecifyAudienceType() )
      {
        Set audiencesToAdd = getAudiencesToAdd( attachedPromotion.getSecondaryAudiences(), detachedPromotion.getSecondaryAudiences() );
        if ( audiencesToAdd.size() > 0 )
        {
          for ( Iterator promotionIter = attachedPromotion.getChildPromotions().iterator(); promotionIter.hasNext(); )
          {
            ProductClaimPromotion childPromotion = (ProductClaimPromotion)promotionIter.next();
            if ( childPromotion.getSecondaryAudienceType().isSpecifyAudienceType() )
            {
              for ( Iterator audienceIter = audiencesToAdd.iterator(); audienceIter.hasNext(); )
              {
                Long audienceId = ( (Audience)audienceIter.next() ).getId();
                Audience audience = audienceDAO.getAudienceById( audienceId );
                childPromotion.getPromotionSecondaryAudiences().add( new PromotionSecondaryAudience( audience, childPromotion ) );
              }
            }
          }
        }
      }
    }
  }

  /**
   * Removes from child promotions the secondary audiences that were removed from the parent
   * promotion.
   * 
   * @param attachedPromotion the attached version of the promotion.
   * @param detachedPromotion the detached version of the promotion.
   */
  private void removeSecondaryAudiencesFromChildPromotions( ProductClaimPromotion attachedPromotion, ProductClaimPromotion detachedPromotion )
  {
    if ( attachedPromotion.getChildrenCount() > 0 )
    {
      if ( attachedPromotion.getSecondaryAudienceType().isSpecifyAudienceType() && detachedPromotion.getSecondaryAudienceType().isSpecifyAudienceType() )
      {
        Set audiencesToRemove = getAudiencesToRemove( attachedPromotion.getSecondaryAudiences(), detachedPromotion.getSecondaryAudiences() );
        if ( audiencesToRemove.size() > 0 )
        {
          for ( Iterator promotionIter = attachedPromotion.getChildPromotions().iterator(); promotionIter.hasNext(); )
          {
            ProductClaimPromotion childPromotion = (ProductClaimPromotion)promotionIter.next();
            if ( childPromotion.getSecondaryAudienceType().isSpecifyAudienceType() )
            {
              for ( Iterator audienceIter = childPromotion.getPromotionSecondaryAudiences().iterator(); audienceIter.hasNext(); )
              {
                PromotionSecondaryAudience childAudience = (PromotionSecondaryAudience)audienceIter.next();
                if ( audiencesToRemove.contains( childAudience.getAudience() ) )
                {
                  audienceIter.remove();
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * Returns a reference to the Audience DAO service.
   * 
   * @return a reference to the Audience DAO service.
   */
  private AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)BeanLocator.getBean( AudienceDAO.BEAN_NAME );
  }

  private DivisionDAO getDivisionDAO()
  {
    return (DivisionDAO)BeanLocator.getBean( DivisionDAO.BEAN_NAME );
  }

  /**
   * Returns the audiences to be added to the promotion.
   * 
   * @param audiencesFromAttachedPromotion the audiences from the attached version of the
   *          {@link Promotion} object, as a <code>Set</code> of {@link Audience} objects.
   * @param audiencesFromDetachedPromotion the audiences from the detached version of the
   *          {@link Promotion} object, as a <code>Set</code> of {@link Audience} objects.
   * @return the audiences to be added to the promotion, as a <code>Set</code> of {@link Audience}
   *         objects.
   */
  private Set getAudiencesToAdd( Set audiencesFromAttachedPromotion, Set audiencesFromDetachedPromotion )
  {
    Set audiencesToAdd = new HashSet();

    if ( audiencesFromAttachedPromotion != null && audiencesFromDetachedPromotion != null )
    {
      for ( Iterator iter = audiencesFromDetachedPromotion.iterator(); iter.hasNext(); )
      {
        Audience submitterAudience = (Audience)iter.next();
        if ( !audiencesFromAttachedPromotion.contains( submitterAudience ) )
        {
          audiencesToAdd.add( submitterAudience );
        }
      }
    }

    return audiencesToAdd;
  }

  /**
   * Returns the audiences to be removed from the promotion.
   * 
   * @param audiencesFromAttachedPromotion the audiences from the attached version of the
   *          {@link Promotion} object, as a <code>Set</code> of {@link Audience} objects.
   * @param audiencesFromDetachedPromotion the audiences from the detached version of the
   *          {@link Promotion} object, as a <code>Set</code> of {@link Audience} objects.
   * @return the audiences to be removed from the promotion, as a <code>Set</code> of
   *         {@link Audience} objects.
   */
  private Set getAudiencesToRemove( Set audiencesFromAttachedPromotion, Set audiencesFromDetachedPromotion )
  {
    Set audiencesToRemove = new HashSet();

    if ( audiencesFromAttachedPromotion != null && audiencesFromDetachedPromotion != null )
    {
      for ( Iterator iter = audiencesFromAttachedPromotion.iterator(); iter.hasNext(); )
      {
        Audience submitterAudience = (Audience)iter.next();
        if ( !audiencesFromDetachedPromotion.contains( submitterAudience ) )
        {
          audiencesToRemove.add( submitterAudience );
        }
      }
    }

    return audiencesToRemove;
  }

  /**
   * Update the product claim promotion.
   * 
   * @param attachedPromotion
   */
  private void updateProductClaimPromotion( ProductClaimPromotion attachedPromotion )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    ProductClaimPromotion detachedPromotion = (ProductClaimPromotion)getDetachedDomain();
    attachedPromotion.setPrimaryAudienceType( detachedPromotion.getPrimaryAudienceType() );
    attachedPromotion.setSecondaryAudienceType( detachedPromotion.getSecondaryAudienceType() );
    if ( detachedPromotion.getPrimaryAudienceType() != null && detachedPromotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      attachedPromotion.getPromotionPrimaryAudiences().clear();
    }
    else
    {
      Set detachedSubmitterAudiences = detachedPromotion.getPromotionPrimaryAudiences();
      Iterator detachedSubmitterAudienceIter = detachedPromotion.getPromotionPrimaryAudiences().iterator();

      PromotionPrimaryAudience promotionSubmitterAudience;
      Set attachedSubmitterAudiences = attachedPromotion.getPromotionPrimaryAudiences();

      // Remove from child promotions the primary audiences removed from the parent promotion.
      removePrimaryAudiencesFromChildPromotions( attachedPromotion, detachedPromotion );

      // Add to child promotions the primary audiences added to the parent promotion.
      if ( addPrimaryAudiencesToChildPromotions )
      {
        addPrimaryAudiencesToChildPromotions( attachedPromotion, detachedPromotion );
      }

      // If the attached promotion contains any audiences not in the detached set
      // then it should be removed from the promotion
      Iterator attachedSubmitterAudienceIter = attachedSubmitterAudiences.iterator();
      while ( attachedSubmitterAudienceIter.hasNext() )
      {
        promotionSubmitterAudience = (PromotionPrimaryAudience)attachedSubmitterAudienceIter.next();
        if ( !detachedSubmitterAudiences.contains( promotionSubmitterAudience ) )
        {
          attachedSubmitterAudienceIter.remove();
        }
      }

      // This will attempt to add all detached audiences to the promotion.
      // Since it is a set, then only non-duplicates will be added.
      while ( detachedSubmitterAudienceIter.hasNext() )
      {
        promotionSubmitterAudience = (PromotionPrimaryAudience)detachedSubmitterAudienceIter.next();
        promotionSubmitterAudience.setAudience( audienceDAO.getAudienceById( promotionSubmitterAudience.getAudience().getId() ) );
        attachedPromotion.addPromotionPrimaryAudience( promotionSubmitterAudience );
      }
    }

    attachedPromotion.setTeamUsed( detachedPromotion.isTeamUsed() );
    if ( !attachedPromotion.isTeamUsed() )
    {
      attachedPromotion.setTeamMaxCount( null );
      attachedPromotion.getPromotionSecondaryAudiences().clear();
    }
    else
    {
      if ( attachedPromotion.getSecondaryAudienceType() != null && ( attachedPromotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
          || attachedPromotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) ) ) )
      {
        attachedPromotion.getPromotionSecondaryAudiences().clear();
      }
      else
      {
        Set detachedTeamAudiences = detachedPromotion.getPromotionSecondaryAudiences();
        Iterator detachedTeamAudienceIter = detachedPromotion.getPromotionSecondaryAudiences().iterator();

        PromotionSecondaryAudience teamAudience;
        Set attachedTeamAudiences = attachedPromotion.getPromotionSecondaryAudiences();

        // Remove from child promotions the secondary audiences removed from the parent promotion.
        removeSecondaryAudiencesFromChildPromotions( attachedPromotion, detachedPromotion );

        // Add to child promotions the secondary audiences added to the parent promotion.
        if ( addSecondaryAudiencesToChildPromotions )
        {
          addSecondaryAudiencesToChildPromotions( attachedPromotion, detachedPromotion );
        }

        // If the attached promotion contains any audiences not in the detached set
        // then it should be removed from the promotion
        Iterator attachedTeamAudienceIter = attachedTeamAudiences.iterator();
        while ( attachedTeamAudienceIter.hasNext() )
        {
          teamAudience = (PromotionSecondaryAudience)attachedTeamAudienceIter.next();
          if ( !detachedTeamAudiences.contains( teamAudience ) )
          {
            attachedTeamAudienceIter.remove();
          }
        }

        // This will attempt to add all team audiences to the promotion.
        // Since it is a set, then only non-duplicates will be added.
        while ( detachedTeamAudienceIter.hasNext() )
        {
          teamAudience = (PromotionSecondaryAudience)detachedTeamAudienceIter.next();
          teamAudience.setAudience( audienceDAO.getAudienceById( teamAudience.getAudience().getId() ) );
          attachedPromotion.addPromotionSecondaryAudience( teamAudience );
        }
      }

      attachedPromotion.setTeamCollectedAsGroup( detachedPromotion.isTeamCollectedAsGroup() );
      if ( attachedPromotion.isTeamCollectedAsGroup() )
      {
        attachedPromotion.getPromotionTeamPositions().clear();
        attachedPromotion.setTeamHasMax( detachedPromotion.isTeamHasMax() );
        if ( attachedPromotion.isTeamHasMax() )
        {
          attachedPromotion.setTeamMaxCount( detachedPromotion.getTeamMaxCount() );
        }
        else
        {
          attachedPromotion.setTeamMaxCount( null );
        }
      }
      else
      {
        Set detachedJobPositions = detachedPromotion.getPromotionTeamPositions();
        Iterator detachedJobPositonIter = detachedPromotion.getPromotionTeamPositions().iterator();

        PromotionTeamPosition promotionTeamPosition;
        Set attachedJobPositions = attachedPromotion.getPromotionTeamPositions();

        // If the attached promotion contains any job positions not in the detached set
        // then it should be removed from the promotion
        Iterator attachedJobPositionIter = attachedJobPositions.iterator();
        while ( attachedJobPositionIter.hasNext() )
        {
          promotionTeamPosition = (PromotionTeamPosition)attachedJobPositionIter.next();
          if ( !detachedJobPositions.contains( promotionTeamPosition ) )
          {
            attachedJobPositionIter.remove();
          }
        }

        // This will attempt to add all job positions to the promotion.
        // Since it is a set, then only non-duplicates will be added.
        while ( detachedJobPositonIter.hasNext() )
        {
          promotionTeamPosition = (PromotionTeamPosition)detachedJobPositonIter.next();
          attachedPromotion.addPromotionTeamPosition( promotionTeamPosition );
        }
      }
    }
  }

  private void updateRecognitionPromotion( RecognitionPromotion attachedPromotion )
  {
    updatePromoMerchCountries( attachedPromotion );

    RecognitionPromotion detachedPromo = (RecognitionPromotion)getDetachedDomain();
    attachedPromotion.setOpenEnrollmentEnabled( detachedPromo.isOpenEnrollmentEnabled() );
    attachedPromotion.setSelfRecognitionEnabled( detachedPromo.isSelfRecognitionEnabled() );
  }

  /**
   * Update the primary audience.
   * 
   * @param attachedPromo
   */
  private void updatePrimaryAudience( Promotion attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    Promotion detachedPromo = (Promotion)getDetachedDomain();
    attachedPromo.setPrimaryAudienceType( detachedPromo.getPrimaryAudienceType() );

    if ( detachedPromo.getPrimaryAudienceType() != null && ( detachedPromo.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        || detachedPromo.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SELF_ENROLL_ONLY ) ) ) )
    {
      attachedPromo.getPromotionPrimaryAudiences().clear();
    }
    else
    {
      Set detachedPrimaryAudience = detachedPromo.getPromotionPrimaryAudiences();
      Iterator detachedPrimaryAudienceIter = detachedPromo.getPromotionPrimaryAudiences().iterator();

      PromotionPrimaryAudience promotionPrimaryAudience;
      Set attachedPrimaryAudiences = attachedPromo.getPromotionPrimaryAudiences();

      // If the attached promotion contains any audiences not in the detached set
      // then it should be removed from the promotion
      Iterator attachedPrimaryAudienceIter = attachedPrimaryAudiences.iterator();
      while ( attachedPrimaryAudienceIter.hasNext() )
      {
        promotionPrimaryAudience = (PromotionPrimaryAudience)attachedPrimaryAudienceIter.next();
        if ( !detachedPrimaryAudience.contains( promotionPrimaryAudience ) )
        {
          attachedPrimaryAudienceIter.remove();
        }
      }

      // This will attempt to add all detached audiences to the promotion.
      // Since it is a set, then only non-duplicates will be added.
      while ( detachedPrimaryAudienceIter.hasNext() )
      {
        promotionPrimaryAudience = (PromotionPrimaryAudience)detachedPrimaryAudienceIter.next();
        promotionPrimaryAudience.setAudience( audienceDAO.getAudienceById( promotionPrimaryAudience.getAudience().getId() ) );
        attachedPromo.addPromotionPrimaryAudience( promotionPrimaryAudience );
      }
    }
  }

  /**
   * Update the secondary audience.
   * 
   * @param attachedPromo
   */
  private void updateSecondaryAudience( Promotion attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    Promotion detachedPromo = (Promotion)getDetachedDomain();
    attachedPromo.setSecondaryAudienceType( detachedPromo.getSecondaryAudienceType() );

    if ( attachedPromo.getSecondaryAudienceType() != null && ( attachedPromo.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
        || attachedPromo.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) )
        || attachedPromo.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) )
        || attachedPromo.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.CREATOR_ORG_AND_BELOW_CODE ) )
        || attachedPromo.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.CREATOR_ORG_ONLY_CODE ) ) ) )
    {
      attachedPromo.getPromotionSecondaryAudiences().clear();
    }
    else
    {
      Set detachedSecondaryAudience = detachedPromo.getPromotionSecondaryAudiences();
      Iterator detachedSecondaryAudienceIter = detachedPromo.getPromotionSecondaryAudiences().iterator();

      PromotionSecondaryAudience secondaryAudience;
      Set attachedSecondaryAudiences = attachedPromo.getPromotionSecondaryAudiences();

      // If the attached promotion contains any audiences not in the detached set
      // then it should be removed from the promotion
      Iterator attachedSecondaryAudienceIter = attachedSecondaryAudiences.iterator();
      while ( attachedSecondaryAudienceIter.hasNext() )
      {
        secondaryAudience = (PromotionSecondaryAudience)attachedSecondaryAudienceIter.next();
        if ( !detachedSecondaryAudience.contains( secondaryAudience ) )
        {
          attachedSecondaryAudienceIter.remove();
        }
      }

      // This will attempt to add all team audiences to the promotion.
      // Since it is a set, then only non-duplicates will be added.
      while ( detachedSecondaryAudienceIter.hasNext() )
      {
        secondaryAudience = (PromotionSecondaryAudience)detachedSecondaryAudienceIter.next();
        secondaryAudience.setAudience( audienceDAO.getAudienceById( secondaryAudience.getAudience().getId() ) );
        attachedPromo.addPromotionSecondaryAudience( secondaryAudience );
      }
    }
  }

  /**
   * Update goal quest specific self enrollment info.
   * @param attachedPromo
   */
  private void updateSelfEnrollInfo( Promotion attachedPromo )
  {
    Promotion detachedPromo = (Promotion)getDetachedDomain();
    attachedPromo.setSecondaryAudienceType( detachedPromo.getSecondaryAudienceType() );

    // update attached promo with the user's self enrollment selections from the detached object
    if ( attachedPromo.isGoalQuestOrChallengePointPromotion() )
    {
      attachedPromo.setAllowSelfEnroll( detachedPromo.isAllowSelfEnroll() );
      attachedPromo.setEnrollProgramCode( detachedPromo.getEnrollProgramCode() );
    }
  }

  private void updatePromoMerchCountries( Promotion attachedPromo )
  {
    Promotion detachedPromo = (Promotion)getDetachedDomain();

    // loop through the old list of promo merch countries and remove any that have been removed from
    // the new promo merch list
    for ( Iterator attIter = attachedPromo.getPromoMerchCountries().iterator(); attIter.hasNext(); )
    {
      PromoMerchCountry attPromoMerchCountry = (PromoMerchCountry)attIter.next();
      if ( !containsPromoMerchCountry( attPromoMerchCountry, detachedPromo.getPromoMerchCountries() ) )
      {
        attIter.remove();
      }
    }

    // loop through the new list of promo merch countries
    for ( Iterator detachIter = detachedPromo.getPromoMerchCountries().iterator(); detachIter.hasNext(); )
    {
      PromoMerchCountry detachPromoMerchCountry = (PromoMerchCountry)detachIter.next();
      // if ( attachedPromo.getPromoMerchCountries().contains( detachPromoMerchCountry ) )
      if ( containsPromoMerchCountry( detachPromoMerchCountry, attachedPromo.getPromoMerchCountries() ) )
      {
        // loop through and set the program id
        for ( Iterator attachedIter = attachedPromo.getPromoMerchCountries().iterator(); attachedIter.hasNext(); )
        {
          PromoMerchCountry attachedPromoMerchCountry = (PromoMerchCountry)attachedIter.next();
          if ( attachedPromoMerchCountry.getCountry().getId().equals( detachPromoMerchCountry.getCountry().getId() )
              && attachedPromoMerchCountry.getPromotion().getId().equals( detachPromoMerchCountry.getPromotion().getId() ) )
          {
            attachedPromoMerchCountry.setProgramId( detachPromoMerchCountry.getProgramId() );
            break;
          }
        }
      }
      else
      {
        attachedPromo.getPromoMerchCountries().add( detachPromoMerchCountry );
      }
    }
  }

  @SuppressWarnings( "rawtypes" )
  private boolean containsPromoMerchCountry( PromoMerchCountry promoMerchCountry, Set<PromoMerchCountry> promoMerchCountries )
  {
    Iterator iter = promoMerchCountries.iterator();
    while ( iter.hasNext() )
    {
      PromoMerchCountry aPromoMerchCountry = (PromoMerchCountry)iter.next();
      if ( promoMerchCountry.getPromotion().getId().equals( aPromoMerchCountry.getPromotion().getId() ) && promoMerchCountry.getCountry().getId().equals( aPromoMerchCountry.getCountry().getId() ) )
      {
        return true;
      }
    }
    return false;
  }

  private void updatePartnerAudience( Promotion attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    Promotion detachedPromo = (Promotion)getDetachedDomain();

    if ( detachedPromo.getPartnerAudienceType() != null || attachedPromo.getPartnerAudienceType() != null )
    {
      attachedPromo.setPartnerAudienceType( detachedPromo.getPartnerAudienceType() );
      if ( attachedPromo.isGoalQuestOrChallengePointPromotion() )
      {
        ( (GoalQuestPromotion)attachedPromo ).setAutoCompletePartners( ( (GoalQuestPromotion)detachedPromo ).isAutoCompletePartners() );

        if ( ( (GoalQuestPromotion)detachedPromo ).isPartnersEnabled() )
        {
          ( (GoalQuestPromotion)attachedPromo ).setPartnerCount( ( (GoalQuestPromotion)detachedPromo ).getPartnerCount() );
        }
      }

      Set detachedPartnerAudience = detachedPromo.getPromotionPartnerAudiences();
      Set attachedPartnerAudiences = attachedPromo.getPromotionPartnerAudiences();

      // If the attached promotion contains any audiences not in the detached set
      // then it should be removed from the promotion
      Iterator attachedPartnerAudienceIter = attachedPartnerAudiences.iterator();
      while ( attachedPartnerAudienceIter.hasNext() )
      {
        PromotionPartnerAudience partnerAudience = (PromotionPartnerAudience)attachedPartnerAudienceIter.next();
        if ( !detachedPartnerAudience.contains( partnerAudience ) )
        {
          attachedPartnerAudienceIter.remove();
        }
      }

      // This will attempt to add all team audiences to the promotion.
      // Since it is a set, then only non-duplicates will be added.
      Iterator detachedPartnerAudienceIter = detachedPartnerAudience.iterator();
      while ( detachedPartnerAudienceIter.hasNext() )
      {
        PromotionPartnerAudience partnerAudience = (PromotionPartnerAudience)detachedPartnerAudienceIter.next();
        partnerAudience.setAudience( audienceDAO.getAudienceById( partnerAudience.getAudience().getId() ) );
        attachedPromo.addPromotionPartnerAudience( partnerAudience );
      }
    }
    else // partners not enabled, reset autocomplete partner selection to false
    {
      if ( attachedPromo.isGoalQuestOrChallengePointPromotion() )
      {
        ( (GoalQuestPromotion)attachedPromo ).setAutoCompletePartners( false );
      }
    }

    ( (GoalQuestPromotion)attachedPromo ).setPreSelectedPartnerChars( ( (GoalQuestPromotion)detachedPromo ).getPreSelectedPartnerChars() );
  }

  public boolean isManagerCanSelect()
  {
    return managerCanSelect;
  }

  public void setManagerCanSelect( boolean managerCanSelect )
  {
    this.managerCanSelect = managerCanSelect;
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }
}
