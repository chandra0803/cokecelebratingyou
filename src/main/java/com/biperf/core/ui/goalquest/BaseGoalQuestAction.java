
package com.biperf.core.ui.goalquest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.awardbanq.impl.ProductEntryVO;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.goalquest.GoalLevelService;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.goalquest.impl.PaxGoalAssociationRequest;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.participantsurvey.ParticipantSurveyService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.goalquest.view.AwardProductView;
import com.biperf.core.ui.goalquest.view.GoalAwardView;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.UserManager;

public abstract class BaseGoalQuestAction extends BaseDispatchAction
{
  @SuppressWarnings( "unchecked" )
  protected PaxGoal getPaxGoal( GoalQuestPromotion promotion, Long ownerUserId )
  {
    AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
    ascReqColl.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.PROMOTION ) );
    return getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotion.getId(), ownerUserId, ascReqColl );
  }

  protected String getLastProgressDate( Long promotionId, Long participantId )
  {
    GoalQuestParticipantActivity goalQuestParticipantActivity = getGoalQuestPaxActivityService().getLatestPaxActivityByPromotionIdAndUserId( promotionId, participantId );

    if ( goalQuestParticipantActivity != null )
    {
      return DateUtils.toDisplayString( goalQuestParticipantActivity.getSubmissionDate() );
    }
    else
    {
      return DateUtils.toDisplayString( new Date() );
    }
  }

  protected Long buildPromotionId( HttpServletRequest request )
  {
    String value = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" );
    // check the request, just in case
    if ( null != value )
    {
      return new Long( value );
    }
    else
    {
      return (Long)request.getAttribute( "promotionId" );
    }
  }

  protected String buildIsPartner( HttpServletRequest request )
  {
    String value = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "isPartner" );
    // check the request, just in case
    if ( null != value )
    {
      return value;
    }
    else
    {
      return (String)request.getAttribute( "isPartner" );
    }
  }

  protected List<GoalAwardView> buildGoalAwardLevels( GoalQuestPromotion promo ) throws ServiceErrorException
  {
    List<GoalAwardView> levels = new ArrayList<GoalAwardView>();

    AwardBanqMerchResponseValueObject levelData = getMerchLevelService().getMerchlinqLevelDataWebService( promo.getProgramId(), true, true );
    for ( AbstractGoalLevel level : promo.getGoalLevels() )
    {
      GoalLevel goalLevel = (GoalLevel)level;
      GoalAwardView levelView = new GoalAwardView();
      levelView.setDesc( goalLevel.getGoalLevelDescription() );
      levelView.setId( goalLevel.getId().toString() );
      levelView.setName( goalLevel.getGoalLevelName() );
      levelView.setProducts( buildProducts( levelData, goalLevel ) );
      levels.add( levelView );
    }
    return levels;
  }

  protected List<AwardProductView> buildProducts( AwardBanqMerchResponseValueObject levelData, GoalLevel goalLevel )
  {
    List<AwardProductView> products = new ArrayList<AwardProductView>();

    for ( MerchLevelValueObject merchLevel : levelData.getMerchLevel() )
    {
      // find the correct level
      if ( merchLevel.getMaxValue() == goalLevel.getAward().intValue() )
      {
        for ( MerchLevelProductValueObject product : merchLevel.getMerchLevelProduct() )
        {
          AwardProductView productView = new AwardProductView();
          Set<Locale> localeList = new HashSet<Locale>();
          for ( Iterator<ProductEntryVO> iter = product.getProductGroupDescriptions().getEntry().iterator(); iter.hasNext(); )
          {
            ProductEntryVO vo = iter.next();
            localeList.add( LocaleUtils.getLocale( vo.getValue().getLocale() ) );
          }

          Locale locale = LocaleUtils.findClosestMatchingLocale( UserManager.getLocale(), localeList );

          for ( Iterator<ProductEntryVO> entryIter = product.getProductGroupDescriptions().getEntry().iterator(); entryIter.hasNext(); )
          {
            ProductEntryVO entryVO = entryIter.next();

            if ( LocaleUtils.getLocale( entryVO.getValue().getLocale() ).equals( locale ) )
            {
              if ( entryVO != null )
              {
                productView.setDesc( entryVO.getValue().getCopy() );
                productView.setName( entryVO.getValue().getDescription() );
              }
              productView.setId( product.getProductSetId() );
              productView.setThumbnail( product.getThumbnailImageURL() );
              productView.setImg( product.getDetailImageURL() );
              products.add( productView );
            }
          }

        }
        break;
      }
    }
    return products;
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected PaxGoalService getPaxGoalService()
  {
    return (PaxGoalService)getService( PaxGoalService.BEAN_NAME );
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected GoalLevelService getGoalLevelService()
  {
    return (GoalLevelService)getService( GoalLevelService.BEAN_NAME );
  }

  protected AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  protected MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  protected ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  protected UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  protected GoalQuestPaxActivityService getGoalQuestPaxActivityService()
  {
    return (GoalQuestPaxActivityService)getService( GoalQuestPaxActivityService.BEAN_NAME );
  }

  protected GoalQuestService getGoalQuestService()
  {
    return (GoalQuestService)getService( GoalQuestService.BEAN_NAME );
  }

  protected ChallengepointProgressService getChallengepointProgressService()
  {
    return (ChallengepointProgressService)getService( ChallengepointProgressService.BEAN_NAME );
  }

  protected ChallengePointService getChallengePointService()
  {
    return (ChallengePointService)getService( ChallengePointService.BEAN_NAME );
  }

  protected SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  protected ParticipantSurveyService getParticipantSurveyService()
  {
    return (ParticipantSurveyService)getService( ParticipantSurveyService.BEAN_NAME );
  }
}
