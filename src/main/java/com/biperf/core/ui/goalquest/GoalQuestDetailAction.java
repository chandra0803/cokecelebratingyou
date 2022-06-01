/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/GoalQuestDetailAction.java,v $
 */

package com.biperf.core.ui.goalquest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.challengepoint.ChallengepointReviewProgress;
import com.biperf.core.domain.enums.PartnerPayoutStructure;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.awardbanq.impl.ProductEntryVO;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.goalquest.view.AwardProductView;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.biperf.core.value.PromotionMenuBean;

public class GoalQuestDetailAction extends BaseGoalQuestAction
{
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return showDetail( mapping, form, request, response );
    }
  }

  public ActionForward showProgress( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return showDetail( mapping, form, request, response );
  }

  public ActionForward showDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    GoalQuestDetailsForm detailsForm = (GoalQuestDetailsForm)form;

    PaxGoal goal = getPaxGoalService().getPaxGoalById( detailsForm.getPaxGoalId() );
    // get the promotion, appropriately hydrated
    GoalQuestPromotion promotion = buildPromotion( request );
    detailsForm.setPromotion( promotion );
    detailsForm.setPartner( isPartner( request, promotion ) );
    detailsForm.setPartnersEnabled( promotion.isPartnersEnabled() );

    if ( goal.getCurrentValue() == null )
    {
      goal.setCurrentValue( new BigDecimal( 0 ) );
    }

    if ( detailsForm.getPaxGoalId() != null && detailsForm.getPaxGoalId().longValue() > 0 )
    {
      detailsForm.setPaxGoal( goal );
      // get the partners
      detailsForm.setPartners( buildPartnerList( promotion, detailsForm ) );
      // get progress
      buildProgress( detailsForm );
    }

    // build the product, if necessary
    buildProduct( detailsForm );

    // set the partner payout as per the locale of the logged in user.
    if ( detailsForm.isPartner() )
    {
      detailsForm.setPartnerPayout( getPartnerEarnings( detailsForm.getPaxGoal(), detailsForm.getPromotion(), null ) );
    }
    else
    {
      detailsForm.setPartnerPayout( null );
    }

    if ( promotion.isChallengePointPromotion() )
    {
      ChallengepointPaxValueBean challengepointPaxValueBean = getChallengepointProgressService().getChallengepointProgressSummary( promotion.getId(), goal.getParticipant().getId() );
      // BugFix 21473.
      if ( challengepointPaxValueBean.getPaxGoal() != null && challengepointPaxValueBean.getPaxGoal().getCurrentValue() == null )
      {
        challengepointPaxValueBean.getPaxGoal().setCurrentValue( new BigDecimal( 0 ) );
      }
      request.setAttribute( "cpPaxBean", challengepointPaxValueBean );
      request.setAttribute( "thresholdApplicable", isAwardThresholdApplicable( challengepointPaxValueBean ) );
    }

    // get the status/progress
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  protected List<ParticipantPartner> buildPartnerList( GoalQuestPromotion promotion, GoalQuestDetailsForm detailsForm )
  {
    ParticipantAssociationRequest associations = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
    return getPromotionService().getPartnersByPromotionAndParticipantWithAssociations( promotion.getId(), detailsForm.getPaxGoal().getParticipant().getId(), associations );
  }

  protected void buildProgress( GoalQuestDetailsForm detailsForm )
  {
    PaxGoal paxGoal = detailsForm.getPaxGoal();
    Long participantId = paxGoal.getParticipant().getId();
    Long promotionId = detailsForm.getPromotion().getId();

    Map<String, Object> progressMap = null;

    if ( detailsForm.getPromotion().isGoalQuestPromotion() )
    {
      progressMap = getGoalQuestService().getGoalQuestProgressByPromotionIdAndUserId( promotionId, participantId );

      List<GoalQuestReviewProgress> progressList = (List<GoalQuestReviewProgress>)progressMap.get( "goalQuestProgressList" );

      if ( progressList != null && progressList.size() > 0 )
      {
        GoalQuestReviewProgress goalQuestReviewProgress = (GoalQuestReviewProgress)progressList.get( progressList.size() - 1 );
        if ( goalQuestReviewProgress.getPercentToGoal() != null && detailsForm.getPromotion().getProgressLoadType() != null
            && detailsForm.getPromotion().getProgressLoadType().getCode().equals( ProgressLoadType.AUTOMOTIVE ) )
        {
          detailsForm.setGqAutoProgressList( progressList );
        }
        detailsForm.setGqProgress( goalQuestReviewProgress );
        detailsForm.setProgressDate( getLastProgressDate( promotionId, participantId ) );

        if ( paxGoal.getCurrentValue() != null )
        {
          if ( ( goalQuestReviewProgress.getAmountToAchieve() != null ) && ( paxGoal.getCurrentValue().compareTo( goalQuestReviewProgress.getAmountToAchieve() ) >= 0 ) )
          {
            detailsForm.setGoalAchieved( true );
          }
        }
      }
      String calAchievementAmount = SelectGoalUtil.getCalculatedGoalAmount( (GoalLevel)paxGoal.getGoalLevel(), paxGoal ) != null
          ? NumberFormatUtil.getLocaleBasedBigDecimalFormat( SelectGoalUtil.getCalculatedGoalAmount( (GoalLevel)paxGoal.getGoalLevel(),
                                                                                                     paxGoal ),
                                                             detailsForm.getPromotion().getAchievementPrecision().getPrecision(),
                                                             UserManager.getLocale() )
          : "";
      detailsForm.setCalculatedAchievementAmount( calAchievementAmount );
    }

    else if ( detailsForm.getPromotion().isChallengePointPromotion() )
    {
      try
      {
        progressMap = getChallengepointProgressService().getChallengepointProgressByPromotionIdAndUserId( promotionId, participantId );
      }
      catch( Exception e )
      {
        throw new BeaconRuntimeException( e.getMessage() );
      }
      if ( progressMap != null )
      {
        List<ChallengepointReviewProgress> progressList = (List<ChallengepointReviewProgress>)progressMap.get( "challengepointProgressList" );

        if ( progressList != null && progressList.size() > 0 )
        {
          ChallengepointReviewProgress cpReviewProgress = (ChallengepointReviewProgress)progressList.get( 0 );

          if ( cpReviewProgress.getPercentToGoal() != null && detailsForm.getPromotion().getProgressLoadType() != null
              && detailsForm.getPromotion().getProgressLoadType().getCode().equals( ProgressLoadType.AUTOMOTIVE ) )
          {
            detailsForm.setCpAutoProgressList( progressList );
          }
          if ( paxGoal.getCurrentValue() != null )
          {
            if ( paxGoal.getCurrentValue().compareTo( cpReviewProgress.getAmountToAchieve() ) >= 0 )
            {
              detailsForm.setGoalAchieved( true );
            }
          }
          detailsForm.setCpProgress( cpReviewProgress );
          detailsForm.setProgressDate( getLastProgressDate( promotionId, participantId ) );
        }
      }
      String calAchievementAmount = getChallengePointService().getCalculatedAchievementAmount( (GoalLevel)paxGoal.getGoalLevel(), paxGoal ) != null
          ? NumberFormatUtil.getLocaleBasedBigDecimalFormat( getChallengePointService().getCalculatedAchievementAmount( (GoalLevel)paxGoal.getGoalLevel(), paxGoal ),
                                                             detailsForm.getPromotion().getAchievementPrecision().getPrecision(),
                                                             UserManager.getLocale() )
          : "";
      detailsForm.setCalculatedAchievementAmount( calAchievementAmount );
    }
  }

  @SuppressWarnings( "unchecked" )
  protected GoalQuestPromotion buildPromotion( HttpServletRequest request )
  {
    Long promotionId = buildPromotionId( request );
    AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
    ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
    ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
    ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    return (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, ascReqColl );
  }

  protected void buildProduct( GoalQuestDetailsForm detailsForm ) throws ServiceErrorException
  {
    if ( !detailsForm.getIsProductOnly() || detailsForm.getPaxGoal() == null )
    {
      return;
    }

    PaxGoal paxGoal = detailsForm.getPaxGoal();
    AwardBanqMerchResponseValueObject levelData = getMerchLevelService().getMerchlinqLevelDataWebService( detailsForm.getPromotion().getProgramId(), true, true );

    for ( MerchLevelValueObject merchLevel : levelData.getMerchLevel() )
    {
      // find the correct level
      if ( merchLevel.getMaxValue() == ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().intValue() )
      {

        for ( MerchLevelProductValueObject product : merchLevel.getMerchLevelProduct() )
        {
          if ( product.getProductSetId().equals( paxGoal.getProductSetId() ) )
          {
            AwardProductView productView = new AwardProductView();
            Set<Locale> localeList = new HashSet<Locale>();
            for ( Iterator<ProductEntryVO> iter = product.getProductGroupDescriptions().getEntry().iterator(); iter.hasNext(); )
            {
              ProductEntryVO vo = iter.next();
              localeList.add( LocaleUtils.getLocale( vo.getKey() ) );
            }

            Locale locale = LocaleUtils.findClosestMatchingLocale( UserManager.getLocale(), localeList );

            for ( Iterator<ProductEntryVO> entryIter = product.getProductGroupDescriptions().getEntry().iterator(); entryIter.hasNext(); )
            {
              ProductEntryVO entryVO = entryIter.next();

              if ( LocaleUtils.getLocale( entryVO.getKey() ).equals( locale ) )
              {
                if ( entryVO != null )
                {
                  productView.setDesc( entryVO.getValue().getCopy() );
                  productView.setName( entryVO.getValue().getDescription() );
                }
                productView.setId( product.getProductSetId() );
                productView.setThumbnail( product.getThumbnailImageURL() );
                productView.setImg( product.getDetailImageURL() );
                detailsForm.setProductView( productView );
                break;
              }
            }
          }
        }
      }
    }
  }

  protected boolean isPartner( HttpServletRequest request, GoalQuestPromotion promotion )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    Long promotionId = promotion.getId();

    for ( PromotionMenuBean promoMenuBean : eligiblePromotions )
    {
      if ( promoMenuBean.getPromotion().getId().equals( promotionId ) )
      {
        return promoMenuBean.isPartner();
      }
    }
    return false;
  }

  public boolean isAwardThresholdApplicable( ChallengepointPaxValueBean challengepointPaxValueBean )
  {
    return ! ( challengepointPaxValueBean != null && challengepointPaxValueBean.getPromotion() != null && challengepointPaxValueBean.getPromotion().getAwardThresholdType() != null
        && challengepointPaxValueBean.getPromotion().getAwardThresholdType().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE ) );
  }

  private String getPartnerEarnings( PaxGoal paxGoal, GoalQuestPromotion goalQuestPromotion, Long paxPayout )
  {
    BigDecimal partnerPayout = null;
    String partnerPayoutLocaleBased = "";
    int seqNumber = paxGoal.getGoalLevel().getSequenceNumber();
    BigDecimal partnerAwdAmount = getPromotionService().getPartnerAwardAmountByPromotionAndSequenceNo( goalQuestPromotion.getId(), seqNumber );
    String partnerPayStructure = goalQuestPromotion.getPartnerPayoutStructure().getCode();
    if ( PartnerPayoutStructure.FIXED.equals( partnerPayStructure ) )
    {
      partnerPayout = partnerAwdAmount;
    }
    else if ( PartnerPayoutStructure.PERCENTAGE.equals( partnerPayStructure ) )
    {
      double baseAward = ( (GoalLevel)paxGoal.getGoalLevel() ).getAward().doubleValue();
      if ( null != paxPayout )
      {
        baseAward = paxPayout.doubleValue();
      }
      int roundingMode = goalQuestPromotion.getRoundingMethod().getBigDecimalRoundingMode();
      partnerPayout = new BigDecimal( baseAward ).multiply( partnerAwdAmount ).divide( new BigDecimal( 100 ), roundingMode );
    }

    partnerPayoutLocaleBased = NumberFormatUtil.getLocaleBasedBigDecimalFormat( partnerPayout, goalQuestPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    return partnerPayoutLocaleBased;
  }
}
