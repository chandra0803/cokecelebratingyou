
package com.biperf.core.ui.recognition.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevelComparator;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.recognition.BaseRecognitionAction;

public class CalculatorInfoAction extends BaseRecognitionAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    CalculatorInfoForm form = (CalculatorInfoForm)actionForm;

    // get the calculator hydrated with criteria and ratings
    AbstractRecognitionPromotion promotion = getPromotion( form.getPromotionId() );
    Calculator calculator = promotion.getCalculator();

    CriteriaInfoBean criteriaInfoBean = new CriteriaInfoBean();

    BigDecimal totalScore = new BigDecimal( 0 );

    for ( CriteriaBean bean : form.getCriteriaBeans() )
    {
      CalculatorCriterion cc = calculator.getCriterion( bean.getCriteriaId() );
      CalculatorCriterionRating ccr = getRating( cc, bean.getCriteriaRating() );

      BigDecimal ratingScore = new BigDecimal( getCalculatorService().getCalculatorRatingScore( form.getPromotionId(), bean.getCriteriaId(), bean.getCriteriaRating() ) );

      totalScore = totalScore.add( ratingScore );
      criteriaInfoBean.setTotalScore( totalScore.intValue() );

      criteriaInfoBean.addCriteria( cc.getId(), cc.getWeightValue(), ratingScore.intValue(), String.valueOf( ccr.getRatingValue() ), ccr.getId() );
    }

    calculatePayout( criteriaInfoBean, promotion, form.getParticipantId() );

    writeAsJsonToResponse( criteriaInfoBean, response );

    return null;
  }

  private void calculatePayout( CriteriaInfoBean bean, AbstractRecognitionPromotion promotion, Long participantId )
  {
    Calculator calculator = promotion.getCalculator();
    CalculatorPayout payout = getCalculatorService().getCalculatorPayoutByScore( calculator.getId(), bean.getTotalScore() );
    bean.setAwardRange( payout.getLowAward(), payout.getHighAward() );

    CalculatorAwardType awardType = calculator.getCalculatorAwardType();

    if ( awardType.isFixedAward() )
    {
      bean.setFixedAward( payout.getLowAward() );
    }
    else
    {
      if ( awardType.isMerchLevelAward() )
      {
        PromoMerchProgramLevel promoMerchProgramLevel = getPromoMerchProgramLevelForCalculatorPayout( participantId, promotion, payout );

        bean.addAwardLevel( promoMerchProgramLevel.getId(), promoMerchProgramLevel.getDisplayLevelName(), promoMerchProgramLevel.getMaxValue() );
      }
    }

  }

  private PromoMerchProgramLevel getPromoMerchProgramLevelForCalculatorPayout( Long recipientId, AbstractRecognitionPromotion promotion, CalculatorPayout calculatorPayout )
  {
    Participant pax = getParticipant( recipientId );
    PromoMerchCountry promoMerchCountry = promotion.getPromoMerchCountryForCountryCode( pax.getPrimaryCountryCode() );

    // hydrate the levels
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
    promoMerchCountry = getPromoMerchCountryService().getPromoMerchCountryByIdWithAssociations( promoMerchCountry.getId(), arc );

    List tempPromoMerchCountryList = new ArrayList();
    tempPromoMerchCountryList.add( promoMerchCountry );

    try
    {
      getMerchLevelService().mergeMerchLevelWithOMList( tempPromoMerchCountryList );
    }
    catch( ServiceErrorException se )
    {
      // No need to do anything on exception - just wont display level info
    }

    Calculator calculator = promotion.getCalculator();
    if ( promoMerchCountry != null && promoMerchCountry.getLevels().size() == calculator.getCalculatorPayouts().size() )
    {
      SortedSet sortedLevels = new TreeSet( new PromoMerchProgramLevelComparator() );
      sortedLevels.addAll( promoMerchCountry.getLevels() );
      ArrayList sortedLevelList = new ArrayList( sortedLevels );
      int index = 0;
      for ( CalculatorPayout currentCalculatorPayout : calculator.getCalculatorPayouts() )
      {
        if ( currentCalculatorPayout.equals( calculatorPayout ) )
        {
          return (PromoMerchProgramLevel)sortedLevelList.get( index );
        }
        index++;
      }
    }

    return null;
  }

  private AbstractRecognitionPromotion getPromotion( Long promotionId )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_CRITERION_AND_RATINGS ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_PAYOUTS ) );

    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, arc );
    return promotion;
  }

  private Participant getParticipant( Long participantId )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    return getParticipantService().getParticipantByIdWithAssociations( participantId, arc );
  }

  private CalculatorCriterionRating getRating( CalculatorCriterion cc, int rating )
  {
    for ( CalculatorCriterionRating ccr : cc.getCriterionRatings() )
    {
      if ( ccr.getRatingValue() == rating )
      {
        return ccr;
      }
    }
    return null;
  }

  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }

  private MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }
}
