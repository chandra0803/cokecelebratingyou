/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionAwardsUpdateAssociation.java,v $
 *
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.HomePageItem;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.WellnessPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionAwardsUpdateAssociation <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PromotionAwardsUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionAwardsUpdateAssociation( Promotion promotion )
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

    if ( attachedPromo.isAbstractRecognitionPromotion() )
    {
      updateAbstractRecognitionPromotion( (AbstractRecognitionPromotion)attachedPromo );
    }
    else if ( attachedPromo.isQuizPromotion() )
    {
      updateQuizPromotion( (QuizPromotion)attachedPromo );
    }
    else if ( attachedPromo.isWellnessPromotion() )
    {
      updateWellnessPromotion( (WellnessPromotion)attachedPromo );
    }
    else if ( attachedPromo.isSSIPromotion() )
    {
      updateSSIPromotion( (SSIPromotion)attachedPromo );
    }
  }

  private void updateAbstractRecognitionPromotion( AbstractRecognitionPromotion attachedPromo )
  {
    AbstractRecognitionPromotion detachedPromo = (AbstractRecognitionPromotion)getDetachedDomain();
    attachedPromo.setAwardActive( detachedPromo.isAwardActive() );
    attachedPromo.setFileloadBudgetAmount( detachedPromo.isFileloadBudgetAmount() );
    attachedPromo.setAwardStructure( detachedPromo.getAwardStructure() );
    attachedPromo.setApqConversion( detachedPromo.isApqConversion() );
    attachedPromo.setFeaturedAwardsEnabled( detachedPromo.isFeaturedAwardsEnabled() );
    
    /* client customization start WIP#25589 */
    attachedPromo.setUtilizeParentBudgets( detachedPromo.isUtilizeParentBudgets() ) ;
    /* client customization end WIP#25589 */

    if ( detachedPromo.isNominationPromotion() )
    {
      updateNominationPromotion( attachedPromo, detachedPromo );
    }
    else if ( detachedPromo.isRecognitionPromotion() )
    {
      updateRecognitionPromotion( attachedPromo, detachedPromo );
    }
  }

  private void updateNominationPromotion( AbstractRecognitionPromotion attachedPromo, AbstractRecognitionPromotion detachedPromo )
  {
    NominationPromotion attachedNomPromo = (NominationPromotion)attachedPromo;
    NominationPromotion detachedNomPromo = (NominationPromotion)detachedPromo;
    attachedPromo.setScoreBy( detachedPromo.getScoreBy() );
    attachedPromo.setCalculator( detachedPromo.getCalculator() );
     
    ( (NominationPromotion)attachedPromo )
    .setAwardSpecifierType( ( ( (NominationPromotion)detachedPromo ) )
        .getAwardSpecifierType() );
    
    attachedNomPromo.setTimePeriodActive( detachedNomPromo.isTimePeriodActive() );
    if ( detachedNomPromo.isTimePeriodActive() )
    {
      updateTimePeriods( attachedNomPromo, detachedNomPromo );
    }
    else
    {
      attachedNomPromo.getNominationTimePeriods().clear();
    }

    if ( !detachedNomPromo.isAwardActive() )
    {
      attachedNomPromo.setBillCodesActive( false );
      attachedNomPromo.setPayoutLevel( null );
    }
    else
    {
      attachedPromo.setAwardType( detachedPromo.getAwardType() );
      attachedPromo.setTaxable( detachedPromo.isTaxable() );
    }

    attachedNomPromo.setPayoutLevel( detachedNomPromo.getPayoutLevel() );

    attachedNomPromo.setNominatorRecommendedAward( detachedNomPromo.isNominatorRecommendedAward() );

    if ( detachedNomPromo.getNominationLevels() != null && detachedNomPromo.getNominationLevels().size() > 0 )
    {
      updatePayoutLevels( attachedNomPromo, detachedNomPromo );
    }
    attachedNomPromo.setRequestMoreBudget( detachedNomPromo.isRequestMoreBudget() );
    attachedNomPromo.setBudgetApprover( detachedNomPromo.getBudgetApprover() );

    // When the promotion is cumulative approval, recommended award must be no
    if ( attachedNomPromo.isCumulative() )
    {
      attachedNomPromo.setNominatorRecommendedAward( false );
    }
  }

  private void updatePayoutLevels( NominationPromotion attachedNomPromo, NominationPromotion detachedNomPromo )
  {
    Set<NominationPromotionLevel> updatedNominationPromotionLevels = new LinkedHashSet<NominationPromotionLevel>();
    for ( NominationPromotionLevel attachedNominationPromotionLevel : attachedNomPromo.getNominationLevels() )
    {
      for ( NominationPromotionLevel detachedNominationPromotionLevel : detachedNomPromo.getNominationLevels() )
      {
        boolean found = false;
        if ( attachedNominationPromotionLevel != null && detachedNominationPromotionLevel.getId().equals( attachedNominationPromotionLevel.getId() ) )
        {
          found = true;
          updatedNominationPromotionLevels.add( attachedNominationPromotionLevel );
          break;
        }
      }
    }
    if ( updatedNominationPromotionLevels.isEmpty() )
    {
      attachedNomPromo.getNominationLevels().clear();
    }
    else
    {
      attachedNomPromo.getNominationLevels().clear();
      attachedNomPromo.getNominationLevels().addAll( updatedNominationPromotionLevels );
    }

    Iterator detachedPromoNominationLevels = detachedNomPromo.getNominationLevels().iterator();
    while ( detachedPromoNominationLevels.hasNext() )
    {
      NominationPromotionLevel detachedPromNomLevel = (NominationPromotionLevel)detachedPromoNominationLevels.next();

      if ( attachedNomPromo.getNominationLevels() != null && !attachedNomPromo.getNominationLevels().isEmpty() && detachedPromNomLevel.getId() != null && detachedPromNomLevel.getId() != 0 )
      {
        boolean isExistingEntry = false;
        Iterator attachedNominationPromotionNomLevels = attachedNomPromo.getNominationLevels().iterator();
        while ( attachedNominationPromotionNomLevels.hasNext() )
        {
          NominationPromotionLevel attachedNomPromoLevel = (NominationPromotionLevel)attachedNominationPromotionNomLevels.next();
          if ( attachedNomPromoLevel != null && detachedPromNomLevel != null && attachedNomPromoLevel.getId() != null && attachedNomPromoLevel.getId().equals( detachedPromNomLevel.getId() ) )
          {
            attachedNomPromoLevel.setLevelIndex( detachedPromNomLevel.getLevelIndex() );
            attachedNomPromoLevel.setLevelLabel( detachedPromNomLevel.getLevelLabel() );
            attachedNomPromoLevel.setLevelLabelAssetCode( detachedPromNomLevel.getLevelLabelAssetCode() );
            if ( detachedPromNomLevel.getNominationAwardAmountFixed() != null )
            {
              attachedNomPromoLevel.setNominationAwardAmountFixed( detachedPromNomLevel.getNominationAwardAmountFixed() );
            }
            else
            {
              attachedNomPromoLevel.setNominationAwardAmountFixed( null );
            }
            if ( detachedPromNomLevel.getNominationAwardAmountMin() != null )
            {
              attachedNomPromoLevel.setNominationAwardAmountMin( detachedPromNomLevel.getNominationAwardAmountMin() );
            }
            else
            {
              attachedNomPromoLevel.setNominationAwardAmountMin( null );
            }
            if ( detachedPromNomLevel.getNominationAwardAmountMax() != null )
            {
              attachedNomPromoLevel.setNominationAwardAmountMax( detachedPromNomLevel.getNominationAwardAmountMax() );
            }
            else
            {
              attachedNomPromoLevel.setNominationAwardAmountMax( null );
            }
            attachedNomPromoLevel.setNominationAwardAmountTypeFixed( detachedPromNomLevel.isNominationAwardAmountTypeFixed() );
            attachedNomPromoLevel.setNominationPromotion( detachedPromNomLevel.getNominationPromotion() );
            attachedNomPromoLevel.setPayoutCurrency( detachedPromNomLevel.getPayoutCurrency() );
            attachedNomPromoLevel.setPayoutDescription( detachedPromNomLevel.getPayoutDescription() );
            attachedNomPromoLevel.setPayoutDescriptionAssetCode( detachedPromNomLevel.getPayoutDescriptionAssetCode() );
            attachedNomPromoLevel.setPayoutCurrency( detachedPromNomLevel.getPayoutCurrency() );
            attachedNomPromoLevel.setPayoutValue( detachedPromNomLevel.getPayoutValue() );
            attachedNomPromoLevel.setQuantity( detachedPromNomLevel.getQuantity() );
            attachedNomPromoLevel.setAwardPayoutType( detachedPromNomLevel.getAwardPayoutType() );
            attachedNomPromoLevel.setCalculator( detachedPromNomLevel.getCalculator() );
            isExistingEntry = true;
            break;
          }
          if ( !attachedNominationPromotionNomLevels.hasNext() && !isExistingEntry )
          {
            attachedNomPromo.addNominationLevels( detachedPromNomLevel );
          }
        }
      }
      else
      {
        attachedNomPromo.addNominationLevels( detachedPromNomLevel );
      }
    }
  }

  private void updateTimePeriods( NominationPromotion attachedNomPromo, NominationPromotion detachedNomPromo )
  {
    if ( detachedNomPromo.getNominationTimePeriods() != null && detachedNomPromo.getNominationTimePeriods().size() > 0 )
    {
      Set<NominationPromotionTimePeriod> attachTimePeriods = attachedNomPromo.getNominationTimePeriods();
      Iterator<NominationPromotionTimePeriod> attachedNominationTimePeriod = attachTimePeriods.iterator();
      while ( attachedNominationTimePeriod.hasNext() )
      {
        NominationPromotionTimePeriod timePeriod = attachedNominationTimePeriod.next();
        if ( timePeriod != null && !detachedNomPromo.getNominationTimePeriods().contains( timePeriod ) )
        {
          attachedNominationTimePeriod.remove();
        }
      }

      Iterator<NominationPromotionTimePeriod> detachedNominationTimePeriods = detachedNomPromo.getNominationTimePeriods().iterator();
      while ( detachedNominationTimePeriods.hasNext() )
      {
        NominationPromotionTimePeriod detachedTimePeriod = detachedNominationTimePeriods.next();

        if ( attachedNomPromo.getNominationTimePeriods().contains( detachedTimePeriod ) )
        {
          Iterator<NominationPromotionTimePeriod> attachedNominationPromotionTimePeriods = attachedNomPromo.getNominationTimePeriods().iterator();
          while ( attachedNominationPromotionTimePeriods.hasNext() )
          {
            NominationPromotionTimePeriod attachedTimePeriod = attachedNominationPromotionTimePeriods.next();
            if ( attachedTimePeriod != null && detachedTimePeriod != null && attachedTimePeriod.equals( detachedTimePeriod ) )
            {
              attachedTimePeriod.setTimePeriodName( detachedTimePeriod.getTimePeriodName() );
              attachedTimePeriod.setTimePeriodNameAssetCode( detachedTimePeriod.getTimePeriodNameAssetCode() );
              attachedTimePeriod.setTimePeriodStartDate( detachedTimePeriod.getTimePeriodStartDate() );
              attachedTimePeriod.setTimePeriodEndDate( detachedTimePeriod.getTimePeriodEndDate() );
              attachedTimePeriod.setMaxSubmissionAllowed( detachedTimePeriod.getMaxSubmissionAllowed() );
              attachedTimePeriod.setMaxNominationsAllowed( detachedTimePeriod.getMaxNominationsAllowed() );
              attachedTimePeriod.setMaxWinsllowed( detachedTimePeriod.getMaxWinsllowed() );
            }
          }
        }
        else
        {
          detachedTimePeriod.setId( null ); // Guarantee that Hibernate will treat it as a new
                                            // record, to allow editing name.
          attachedNomPromo.addNominationTimePeriods( detachedTimePeriod );
        }
      }
    }
  }

  private void updateRecognitionPromotion( AbstractRecognitionPromotion attachedPromo, AbstractRecognitionPromotion detachedPromo )
  {
    attachedPromo.setAwardAmountMin( detachedPromo.getAwardAmountMin() );
    attachedPromo.setAwardAmountMax( detachedPromo.getAwardAmountMax() );
    attachedPromo.setAwardAmountFixed( detachedPromo.getAwardAmountFixed() );
    attachedPromo.setAwardAmountTypeFixed( detachedPromo.isAwardAmountTypeFixed() );
    // Calculator add on..
    attachedPromo.setScoreBy( detachedPromo.getScoreBy() );
    attachedPromo.setCalculator( detachedPromo.getCalculator() );
    // merch levels
    attachedPromo.setSweepstakesPrimaryAwardLevel( detachedPromo.getSweepstakesPrimaryAwardLevel() );
    attachedPromo.setSweepstakesSecondaryAwardLevel( detachedPromo.getSweepstakesSecondaryAwardLevel() );

    RecognitionPromotion attachedRecogPromo = (RecognitionPromotion)attachedPromo;
    RecognitionPromotion detachedRecogPromo = (RecognitionPromotion)detachedPromo;

    attachedRecogPromo.setBudgetSweepEnabled( detachedRecogPromo.isBudgetSweepEnabled() );

    attachedRecogPromo.setShowInBudgetTracker( detachedRecogPromo.isShowInBudgetTracker() );

    // home page item (there can be zero or one but not more)
    if ( !detachedRecogPromo.getHomePageItems().isEmpty() )
    {
      HomePageItem item = (HomePageItem)detachedRecogPromo.getHomePageItems().iterator().next();
      if ( !attachedRecogPromo.getHomePageItems().isEmpty() )
      {
        HomePageItem attachedItem = (HomePageItem)attachedRecogPromo.getHomePageItems().iterator().next();
        if ( null == item.getId() || !item.equals( attachedItem ) )
        {
          attachedRecogPromo.getHomePageItems().clear();
          item.setPromotion( attachedRecogPromo );
          attachedRecogPromo.getHomePageItems().add( item );
        }
      }
      else
      {
        item.setPromotion( attachedRecogPromo );
        attachedRecogPromo.getHomePageItems().add( item );
      }
    }
    else
    {
      if ( !attachedRecogPromo.getHomePageItems().isEmpty() )
      {
        attachedRecogPromo.getHomePageItems().clear();
      }
    }

    if ( !detachedRecogPromo.isAwardActive() )
    {
      attachedRecogPromo.setBillCodesActive( false );
    }

    attachedPromo.setNoNotification( detachedPromo.isNoNotification() );
  }

  private void updateQuizPromotion( QuizPromotion attachedPromo )
  {
    QuizPromotion detachedPromo = (QuizPromotion)getDetachedDomain();
    attachedPromo.setAwardActive( detachedPromo.isAwardActive() );
    attachedPromo.setAwardAmount( detachedPromo.getAwardAmount() );
    if ( !detachedPromo.isAwardActive() )
    {
      attachedPromo.setBillCodesActive( false );
    }
  }

  private void updateWellnessPromotion( WellnessPromotion attachedPromo )
  {
    WellnessPromotion detachedPromo = (WellnessPromotion)getDetachedDomain();
    attachedPromo.setAwardActive( detachedPromo.isAwardActive() );
    attachedPromo.setAwardAmountMin( detachedPromo.getAwardAmountMin() );
    attachedPromo.setAwardAmountMax( detachedPromo.getAwardAmountMax() );
    attachedPromo.setAwardAmountFixed( detachedPromo.getAwardAmountFixed() );
    attachedPromo.setAwardAmountTypeFixed( detachedPromo.isAwardAmountTypeFixed() );
  }

  private void updateSSIPromotion( SSIPromotion attachedPromotion )
  {
    SSIPromotion detachedPromotion = (SSIPromotion)getDetachedDomain();
    attachedPromotion.setAllowAwardPoints( detachedPromotion.getAllowAwardPoints() );
    attachedPromotion.setAllowAwardOther( detachedPromotion.getAllowAwardOther() );
    attachedPromotion.setTaxable( detachedPromotion.isTaxable() );
    attachedPromotion.setBillCodesActive( detachedPromotion.isBillCodesActive() );
    if ( detachedPromotion.getPromotionBillCodes().isEmpty() )
    {
      attachedPromotion.getPromotionBillCodes().clear();
    }
    // updateSSIMerchandise(attachedPromotion,detachedPromotion);
  }

  // private void updateSSIMerchandise( SSIPromotion attachedPromotion,SSIPromotion detachedPromo )
  // {
  // attachedPromotion.setAllowAwardMerchandise( detachedPromo.getAllowAwardMerchandise() );
  // if ( detachedPromo.getAllowAwardMerchandise() )
  // {
  // updatePromoMerchCountries( attachedPromotion );
  // }
  // }

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
}
