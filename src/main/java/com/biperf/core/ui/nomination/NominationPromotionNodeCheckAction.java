
package com.biperf.core.ui.nomination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.threads.CallableFactory;
import com.biperf.core.value.NominationBean;

public class NominationPromotionNodeCheckAction extends BaseRecognitionAction
{
  private static final Log logger = LogFactory.getLog( NominationPromotionNodeCheckAction.class );

  // unspecified(mapping, form, request, response);

  @Override
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    this.nominationPromo( mapping, actionForm, request, response );
    return null;
  }

  public ActionForward nominationPromo( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final NominationPromotionNodeCheckBean BEAN = getNodeCheckBean( request );
    NominationPromotionNodeCheckBean result = new NominationPromotionNodeCheckBean();
    result.setMessages( BEAN.getMessages() );
    result.setNominationsCalculator( BEAN.getNominationsCalculator() );
    writeAsJsonToResponse( BEAN, response );
    return null;
  }

  private NominationPromotionNodeCheckBean getNodeCheckBean( HttpServletRequest request )
  {
    final Long USER_ID = UserManager.getUserId();
    final Long PROMOTION_ID = RequestUtils.getRequiredParamLong( request, REQUEST_PARAM_PROMOTION_ID );
    final Long NODE_ID = RequestUtils.getRequiredParamLong( request, REQUEST_PARAM_NODE_ID );

    final NominationPromotionNodeCheckBean BEAN = new NominationPromotionNodeCheckBean();

    List<Callable<Object>> callables = new ArrayList<Callable<Object>>();

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        checkSelectedPromotionAndNode( NODE_ID, PROMOTION_ID, USER_ID, BEAN );
        return null;
      }
    } ) );

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        getAvailableBudget( PROMOTION_ID, USER_ID, NODE_ID, BEAN );
        return null;
      }
    } ) );

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        getCalculator( USER_ID, PROMOTION_ID, BEAN );
        return null;
      }
    } ) );

    try
    {
      getExecutorService().invokeAll( callables );
    }
    catch( InterruptedException ie )
    {
      logger.error( "\n\nERROR in " + this.getClass().getName() + " when running the executor service: \n\n", ie );
    }
    return BEAN;
  }

  public static void getAvailableBudget( Long promotionId, Long participantId, Long nodeId, NominationPromotionNodeCheckBean bean )
  {
    final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    final BigDecimal USER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( participantId );
    Budget budget = null;

    try
    {
      Promotion promotion = getPromotionService().getPromotionById( promotionId );
      budget = getPromotionService().getAvailableBudget( promotionId, participantId, nodeId );
      if ( !promotion.getBudgetMaster().isCentralBudget() )
      {
        budget.setOriginalValue( BudgetUtils.applyMediaConversion( budget.getOriginalValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
        budget.setCurrentValue( BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
      }
    }
    catch( BeaconRuntimeException e )
    {
      // ignore; if the promotion has no budget this exception is thrown
    }
  }

  private void checkSelectedPromotionAndNode( Long nodeId, Long promotionId, Long userId, NominationPromotionNodeCheckBean bean )
  {
    boolean isValidCombination = getPromotionService().isNodeClaimableForRecognitionPromotion( nodeId, promotionId, userId );

    if ( !isValidCombination )
    {
      WebErrorMessage message = new WebErrorMessage( "error", "promoNodeCheckError", "Invalid Node", "fix me", null, null );
    }
  }

  private void getCalculator( Long userId, Long promotionId, NominationPromotionNodeCheckBean bean )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_CRITERION_AND_RATINGS ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_PAYOUTS ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL_CALCULATOR ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, arc );
    Set<NominationPromotionLevel> nominationPromotionLevels = ( (NominationPromotion)promotion ).getNominationLevels();

    for ( NominationPromotionLevel promotionLevel : nominationPromotionLevels )
    {
      bean.setCalculatorData( promotionLevel.getCalculator() );

      if ( promotionLevel.getCalculator() != null && promotionLevel.getCalculator().isDisplayScores() && promotionLevel.getCalculator().getCalculatorAwardType().isMerchLevelAward() )
      {
        // get the user's country
        UserAddress ua = getUserService().getPrimaryUserAddress( userId );
        if ( ua != null && ua.getAddress() != null && ua.getAddress().getCountry() != null )
        {
          String countryCode = ua.getAddress().getCountry().getCountryCode();

          for ( PromoMerchCountry promoMerchCountry : promotion.getPromoMerchCountries() )
          {
            if ( countryCode.equals( promoMerchCountry.getCountry().getCountryCode() ) )
            {
              AwardBanqMerchResponseValueObject merchlinqLevelData = null;
              try
              {
                merchlinqLevelData = getMerchLevelService().getMerchlinqLevelDataWebService( promoMerchCountry.getProgramId(), false, false );
              }
              catch( Throwable t )
              {
                t.printStackTrace();
              }

              if ( merchlinqLevelData != null )
              {
                List<NominationBean.AwardLevelBean> awardBeans = new ArrayList<NominationBean.AwardLevelBean>();
                Collection<MerchLevelValueObject> levels = merchlinqLevelData.getMerchLevel();
                if ( levels != null )
                {
                  for ( PromoMerchProgramLevel level : promoMerchCountry.getLevels() )
                  {
                    awardBeans.add( new NominationBean.AwardLevelBean( level, levels ) );
                  }
                }

                bean.setAwardLevelDataForPayTable( awardBeans );
              }
              break;
            }
          }
        }

      }
    }
  }

  private MerchLevelService getMerchLevelService() throws Exception
  {
    return (MerchLevelService)BeanLocator.getBean( MerchLevelService.BEAN_NAME );
  }

}
