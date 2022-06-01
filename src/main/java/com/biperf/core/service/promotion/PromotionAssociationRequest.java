/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionAssociationRequest.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.ApproverCriteria;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalOption;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.StackRankPayoutGroup;
import com.biperf.core.domain.promotion.StackStandingPayoutGroup;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * PromotionAssociationRequest.
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
 * <td>May 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * <tr>
 * <td>NagarjunaReddy</td>
 * <td>Feb14,2008</td>
 * <td>1.0</td>
 * <td>Modified</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionAssociationRequest extends BaseAssociationRequest
{
  private static final Log log = LogFactory.getLog( PromotionAssociationRequest.class );

  @Override
  public String toString()
  {
    return "PromoReq[" + hydrateLevel + "]";
  }

  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hyrate Level: CLAIM_FORM_DEFINITION
   */
  public static final int CLAIM_FORM_DEFINITION = 2;

  /**
   * Hyrate Level: APPROVAL_OPTION
   */
  public static final int APPROVAL_OPTION = 3;

  /**
   * Hyrate Level: NOTIFICATIONS
   */
  public static final int NOTIFICATIONS = 4;

  /**
   * Hyrate Level: APPROVAL_PARTICIPANTS
   */
  public static final int APPROVAL_PARTICIPANTS = 5;

  /**
   * Hyrate Level: WEB_RULES_AUDIENCES
   */
  public static final int WEB_RULES_AUDIENCES = 6;

  public static final int WEB_RULES_MANAGERS = 700;

  public static final int WEB_RULES_PARTNERS = 702;

  /**
   * Hyrate Level: CLAIM_FORM_STEP_ELEMENT_VALIDATION
   */
  public static final int CLAIM_FORM_STEP_ELEMENT_VALIDATION = 7;

  /**
   * Hyrate Level: JOURNALS
   */
  public static final int JOURNALS = 8;

  //
  // PRODUCT CLAIM PROMOTION HYDRATES
  //

  /**
   * Hyrate Level: PROMO_PAYOUT
   */
  public static final int PROMO_PAYOUT = 100;

  /**
   * Hyrate Level: SUBMITTER_AUDIENCES
   */
  public static final int PRIMARY_AUDIENCES = 101;

  /**
   * Hyrate Level: TEAM_AUDIENCES
   */
  public static final int SECONDARY_AUDIENCES = 102;

  /**
   * Hyrate Level: TEAM_POSITIONS
   */
  public static final int TEAM_POSITIONS = 103;

  /**
   * Hyrate Level: CHILD_PROMOTIONS
   */
  public static final int CHILD_PROMOTIONS = 104;

  //
  // RECOGNITION PROMOTION HYDRATES
  //
  /**
   * Hyrate Level: PROMOTION_BEHAVIORS
   */
  public static final int PROMOTION_BEHAVIORS = 200;

  /**
   * Hyrate Level: ECARDS
   */
  public static final int ECARDS = 203;

  /**
   * Hyrate Level: CERTIFICATES
   */
  public static final int CERTIFICATES = 204;

  /**
   * Hyrate Level: PROMOTION_SWEEPSTAKES
   */
  public static final int PROMOTION_SWEEPSTAKES = 205;

  /**
   * Hyrate Level: PROMOTION_BUDGET_MASTER
   */
  public static final int PROMOTION_BUDGET_MASTER = 206;

  /**
   * Hyrate Level: PROMOTION_BUDGET_MASTER
   */
  public static final int ALL_PROMOTION_BUDGET_MASTER = 208;

  /**
   * Hydrate Level: PROMOTION MERCHANDISE COUNTRIES
   */
  public static final int PROMOTION_MERCHANDISE_COUNTRIES = 209;

  /**
   * Hyrate Level: Home Page Items
   */
  public static final int HOME_PAGE_ITEMS = 210;
  /**
   * Hyrate Level: PROMOTION_SWEEPSTAKES_WINNER_ADDRESS
   */
  public static final int PROMOTION_SWEEPSTAKES_ADDRESS = 211;

  public static final int PROMOTION_NOMINATION_TIME_PERIODS = 212;

  public static final int PROMOTION_NOMINATION_WIZARD = 213;

  //
  // QUIZ PROMOTION HYDRATES
  //
  /**
   * Hyrate Level: QUIZ_QUESTIONS_AND_ANSWERS
   */
  public static final int QUIZ_QUESTIONS_AND_ANSWERS = 301;

  //
  // Calculator Promotion Hydrate
  //
  /**
   * Hydrate Level: CALCULATOR_CRITERION AND CALCULATOR_CRITERION_RATINGS
   */
  public static final int CALCULATOR_CRITERION_AND_RATINGS = 401;

  /**
   * Hydrate Level: CALCULATOR_PAYOUTS
   */
  public static final int CALCULATOR_PAYOUTS = 402;

  //
  // GoalQuest Promotion Hydrate
  //
  /**
   * Hydrate Level: GOAL LEVELS
   */
  public static final int GOAL_LEVELS = 501;
  /**
     * Hyrate Level: PARTNER_AUDIENCES
     */
  public static final int PARTNER_AUDIENCES = 601;

  /**
   * Hyrate Level: ALL_AUDIENCES_WITH_PAXS
   */
  public static final int ALL_AUDIENCES_WITH_PAXS = 1000;

  public static final int PROMOTION_PARTICIPANT_PARTNERS = 602;
  public static final int PARTNER_GOAL_LEVELS = 1001;

  public static final int CP_LEVELS = 701;

  public static final int PROMOTION_PUBLIC_RECOGNITION_AUDIENCE = 805;

  /* Throwdown Hydrations */
  public static final int THROWDOWN_DIVISIONS = 850;
  public static final int THROWDOWN_PAYOUTS = 851;
  public static final int THROWDOWN_DIVISION_ROUNDS = 852;
  public static final int THROWDOWN_DIVISION_MATCH_OUTCOMES = 853;

  /**
   * Hyrate Level: PUBLIC_RECOGNITION_BUDGET_MASTER
   */
  public static final int ALL_PROMOTION_PUBLIC_REC_BUDGET_MASTER = 808;

  public static final int SURVEY = 555;

  /* SSI Hydrations */
  public static final int SSI_CONTEST_APP_LVL_1_AUDIENCE = 1100;
  public static final int SSI_CONTEST_APP_LVL_2_AUDIENCE = 1101;

  /* Promotion BillCodes Hydration */
  public static final int NONSWEEP_PROMO_BILLCODES = 1102;

  /* Promotion BillCodes Hydration */
  public static final int SWEEP_PROMO_BILLCODES = 1103;

  /** Nomination promotion budget approver hydration */
  public static final int NOM_BUDGET_APPROVER = 1200;

  /** Nomination promotion custom approver*/
  public static final int CUSTOM_APPROVER_OPTIONS = 1220;

  /** Nomination promotion default approver */
  public static final int NOM_DEFAULT_APPROVER = 1221;

  public static final int NOMINATION_PROMOTION_LEVEL = 1230;

  public static final int NOMINATION_PROMOTION_LEVEL_CALCULATOR = 1231;

  // ALL Excluding Journal
  public static final int ALL_EXCLUDE_JOURNAL = 1501;

  /**
   * Constructor with hydrateLevel as arg
   *
   * @param hydrateLevel
   */
  public PromotionAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  public boolean equals( Object object )
  {
    return true;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Promotion promotion = (Promotion)domainObject;
    promotion = BaseAssociationRequest.initializeAndUnproxy( promotion );

    // TODO - Remove below log statement alter. Added to find class cast exception
    log.info( "promotionId is = " + promotion.getId() );

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateClaimFormDefinition( promotion );
        hydrateApprovalOption( promotion );
        hydrateNotifications( promotion );
        hydrateApprovalParticipants( promotion );
        hydrateWebRulesAudiences( promotion );
        hydrateClaimFormStepElementValidation( promotion );
        if ( !promotion.isEngagementPromotion() )
        {
          hydrateJournals( promotion );
        }
        hydratePrimaryAudiences( promotion );
        hydrateSecondaryAudiences( promotion );
        hydratePartnerAudiences( promotion );

        if ( promotion.isProductClaimPromotion() )
        {
          hydratePromotionPayoutGroups( (ProductClaimPromotion)promotion );
          hydrateTeamPositions( (ProductClaimPromotion)promotion );
          hydrateChildPromotions( (ProductClaimPromotion)promotion );
          hydratePromotionSweepstakes( promotion );
        }
        else if ( promotion.isAbstractRecognitionPromotion() )
        {
          hydratePromotionBehaviors( (AbstractRecognitionPromotion)promotion );
          hydratePromotionECards( (AbstractRecognitionPromotion)promotion );
          hydratePromotionSweepstakes( promotion );
          hydratePromotionSweepstakesWinnerAddress( promotion );
          hydratePromotionBudgetMaster( promotion );
          hydratePromoMerchCountries( promotion );
          if ( promotion.getCalculator() != null )
          {
            hydrateCalculatorCriterionAndRatings( (AbstractRecognitionPromotion)promotion );
            hydrateCalculatorPayouts( (AbstractRecognitionPromotion)promotion );
          }
          if ( promotion.isRecognitionPromotion() )
          {
            hydrateHomePageItems( (RecognitionPromotion)promotion );
            hydratePromotionCertificates( (RecognitionPromotion)promotion );
            hydratePromotionPublicRecognitionAudiences( (RecognitionPromotion)promotion );
          }
          if ( promotion.isNominationPromotion() )
          {
            hydratePromotionCertificates( (NominationPromotion)promotion );
            hydrateCustomApproverOption( (NominationPromotion)promotion );
            hydrateNominationPromotionLevel( (NominationPromotion)promotion );

          }
        }
        else if ( promotion.isQuizPromotion() )
        {
          hydrateQuizQuestionsAndAnswers( (QuizPromotion)promotion );
        }
        else if ( promotion.isDIYQuizPromotion() )
        {
          hydratePromotionCertificates( (QuizPromotion)promotion );
        }
        else if ( promotion.isGoalQuestPromotion() )
        {
          hydratePromotionGoalLevels( (GoalQuestPromotion)promotion );
        }
        else if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydrateSurvey( promotion );
          hydrateWebRulesManagers( (GoalQuestPromotion)promotion );
          hydrateWebRulesPartners( (GoalQuestPromotion)promotion );
        }
        else if ( promotion.isThrowdownPromotion() )
        {
          hydratePromotionDivisions( (ThrowdownPromotion)promotion );
          hydratePromotionDivisionRounds( (ThrowdownPromotion)promotion );
          hydratePromotionPayouts( (ThrowdownPromotion)promotion );
        }
        else if ( promotion.isSSIPromotion() )
        {
          hydrateSSIContestApprovalLevel1Audiences( (SSIPromotion)promotion );
          hydrateSSIContestApprovalLevel2Audiences( (SSIPromotion)promotion );
        }

        break;

      case ALL_EXCLUDE_JOURNAL:
        hydrateClaimFormDefinition( promotion );
        hydrateApprovalOption( promotion );
        hydrateNotifications( promotion );
        hydrateApprovalParticipants( promotion );
        hydrateWebRulesAudiences( promotion );
        hydrateClaimFormStepElementValidation( promotion );

        hydratePrimaryAudiences( promotion );
        hydrateSecondaryAudiences( promotion );
        hydratePartnerAudiences( promotion );

        if ( promotion.isProductClaimPromotion() )
        {
          hydratePromotionPayoutGroups( (ProductClaimPromotion)promotion );
          hydrateTeamPositions( (ProductClaimPromotion)promotion );
          hydrateChildPromotions( (ProductClaimPromotion)promotion );
          hydratePromotionSweepstakes( promotion );
        }
        else if ( promotion.isAbstractRecognitionPromotion() )
        {
          hydratePromotionBehaviors( (AbstractRecognitionPromotion)promotion );
          hydratePromotionECards( (AbstractRecognitionPromotion)promotion );
          hydratePromotionSweepstakes( promotion );
          hydratePromotionSweepstakesWinnerAddress( promotion );
          hydratePromotionBudgetMaster( promotion );
          hydratePromoMerchCountries( promotion );
          if ( promotion.getCalculator() != null )
          {
            hydrateCalculatorCriterionAndRatings( (AbstractRecognitionPromotion)promotion );
            hydrateCalculatorPayouts( (AbstractRecognitionPromotion)promotion );
          }
          if ( promotion.isRecognitionPromotion() )
          {
            hydrateHomePageItems( (RecognitionPromotion)promotion );
            hydratePromotionCertificates( (RecognitionPromotion)promotion );
            hydratePromotionPublicRecognitionAudiences( (RecognitionPromotion)promotion );
          }
          if ( promotion.isNominationPromotion() )
          {
            hydratePromotionCertificates( (NominationPromotion)promotion );
            hydrateCustomApproverOption( (NominationPromotion)promotion );
            hydrateNominationPromotionLevel( (NominationPromotion)promotion );

          }
        }
        else if ( promotion.isQuizPromotion() )
        {
          hydrateQuizQuestionsAndAnswers( (QuizPromotion)promotion );
        }
        else if ( promotion.isDIYQuizPromotion() )
        {
          hydratePromotionCertificates( (QuizPromotion)promotion );
        }
        else if ( promotion.isGoalQuestPromotion() )
        {
          hydratePromotionGoalLevels( (GoalQuestPromotion)promotion );
        }
        else if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydrateSurvey( promotion );
          hydrateWebRulesManagers( (GoalQuestPromotion)promotion );
          hydrateWebRulesPartners( (GoalQuestPromotion)promotion );
        }
        else if ( promotion.isThrowdownPromotion() )
        {
          hydratePromotionDivisions( (ThrowdownPromotion)promotion );
          hydratePromotionDivisionRounds( (ThrowdownPromotion)promotion );
          hydratePromotionPayouts( (ThrowdownPromotion)promotion );
        }
        else if ( promotion.isSSIPromotion() )
        {
          hydrateSSIContestApprovalLevel1Audiences( (SSIPromotion)promotion );
          hydrateSSIContestApprovalLevel2Audiences( (SSIPromotion)promotion );
        }

        break;

      case SSI_CONTEST_APP_LVL_1_AUDIENCE:
        if ( promotion.isSSIPromotion() )
        {
          hydrateSSIContestApprovalLevel1Audiences( (SSIPromotion)promotion );
        }
        break;

      case SSI_CONTEST_APP_LVL_2_AUDIENCE:
        if ( promotion.isSSIPromotion() )
        {
          hydrateSSIContestApprovalLevel2Audiences( (SSIPromotion)promotion );
        }
        break;

      case CLAIM_FORM_DEFINITION:
        hydrateClaimFormDefinition( promotion );
        break;

      case APPROVAL_OPTION:
        hydrateApprovalOption( promotion );
        break;

      case NOTIFICATIONS:
        hydrateNotifications( promotion );
        break;
      case SURVEY:
        hydrateSurvey( promotion );
        break;
      case APPROVAL_PARTICIPANTS:
        hydrateApprovalParticipants( promotion );
        break;

      case WEB_RULES_AUDIENCES:
        hydrateWebRulesAudiences( promotion );
        break;

      case WEB_RULES_MANAGERS:
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydrateWebRulesManagers( (GoalQuestPromotion)promotion );
        }
        break;

      case WEB_RULES_PARTNERS:
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydrateWebRulesPartners( (GoalQuestPromotion)promotion );
        }
        break;

      case CLAIM_FORM_STEP_ELEMENT_VALIDATION:
        hydrateClaimFormStepElementValidation( promotion );
        break;

      case JOURNALS:
        hydrateJournals( promotion );
        break;

      case PROMO_PAYOUT:
        if ( promotion.isProductClaimPromotion() )
        {
          hydratePromotionPayoutGroups( (ProductClaimPromotion)promotion );
        }
        break;

      case PRIMARY_AUDIENCES:
        hydratePrimaryAudiences( promotion );
        break;

      case SECONDARY_AUDIENCES:
        hydrateSecondaryAudiences( promotion );
        break;

      case PARTNER_AUDIENCES:
        hydratePartnerAudiences( promotion );
        break;

      case TEAM_POSITIONS:
        if ( promotion.isProductClaimPromotion() )
        {
          hydrateTeamPositions( (ProductClaimPromotion)promotion );
        }
        break;

      case CHILD_PROMOTIONS:
        if ( promotion.isProductClaimPromotion() )
        {
          hydrateChildPromotions( (ProductClaimPromotion)promotion );
        }
        break;

      case PROMOTION_BEHAVIORS:
        if ( promotion.isAbstractRecognitionPromotion() )
        {
          hydratePromotionBehaviors( (AbstractRecognitionPromotion)promotion );
        }
        break;

      case HOME_PAGE_ITEMS:
        if ( promotion.isAbstractRecognitionPromotion() )
        {
          hydrateHomePageItems( (RecognitionPromotion)promotion );
        }
        break;

      case ECARDS:
        if ( promotion.isAbstractRecognitionPromotion() )
        {
          hydratePromotionECards( (AbstractRecognitionPromotion)promotion );
        }
        break;

      case CERTIFICATES:
        if ( promotion.isRecognitionPromotion() )
        {
          hydratePromotionCertificates( (RecognitionPromotion)promotion );
        }
        else if ( promotion.isDIYQuizPromotion() )
        {
          hydratePromotionCertificates( (QuizPromotion)promotion );
        }
        else if ( promotion.isNominationPromotion() )
        {
          hydratePromotionCertificates( (NominationPromotion)promotion );
        }
        break;

      case CUSTOM_APPROVER_OPTIONS:
        if ( promotion.isNominationPromotion() )
        {
          hydrateCustomApproverOption( (NominationPromotion)promotion );
        }
        break;

      case NOMINATION_PROMOTION_LEVEL:
        if ( promotion.isNominationPromotion() )
        {
          hydrateNominationPromotionLevel( (NominationPromotion)promotion );
        }
        break;
      case PROMOTION_SWEEPSTAKES:
        hydratePromotionSweepstakes( promotion );
        break;

      case THROWDOWN_DIVISIONS:
        if ( promotion.isThrowdownPromotion() )
        {
          hydratePromotionDivisions( (ThrowdownPromotion)promotion );
        }
        break;

      case THROWDOWN_DIVISION_ROUNDS:
        if ( promotion.isThrowdownPromotion() )
        {
          hydratePromotionDivisionRounds( (ThrowdownPromotion)promotion );
        }
        break;

      case THROWDOWN_PAYOUTS:
        if ( promotion.isThrowdownPromotion() )
        {
          hydratePromotionPayouts( (ThrowdownPromotion)promotion );
        }
        break;

      case THROWDOWN_DIVISION_MATCH_OUTCOMES:
        if ( promotion.isThrowdownPromotion() )
        {
          hydratePromotionDivisionMatchOutcomes( (ThrowdownPromotion)promotion );
        }
        break;

      case PROMOTION_SWEEPSTAKES_ADDRESS:
        hydratePromotionSweepstakesWinnerAddress( promotion );
        break;

      case PROMOTION_BUDGET_MASTER:
        if ( promotion.isBudgetUsed() || promotion.isCashBudgetUsed() )
        {
          hydratePromotionBudgetMaster( promotion );
        }
        break;

      case ALL_PROMOTION_BUDGET_MASTER:
        if ( promotion.isBudgetUsed() || promotion.isCashBudgetUsed() )
        {
          hydrateAllPromotionBudgetMaster( promotion );
        }
        break;

      case ALL_PROMOTION_PUBLIC_REC_BUDGET_MASTER:
        if ( promotion.isRecognitionPromotion() )
        {
          RecognitionPromotion recPromo = (RecognitionPromotion)promotion;

          if ( recPromo.isPublicRecBudgetUsed() )
          {
            hydrateAllPromotionPublicRecBudgetMaster( recPromo );
          }
        }
        else if ( promotion.isNominationPromotion() )
        {
          NominationPromotion nomPromo = (NominationPromotion)promotion;

          if ( nomPromo.isPublicRecBudgetUsed() )
          {
            hydrateAllPromotionPublicRecBudgetMaster( nomPromo );
          }
        }
        break;

      case QUIZ_QUESTIONS_AND_ANSWERS:
        if ( promotion.isQuizPromotion() )
        {
          hydrateQuizQuestionsAndAnswers( (QuizPromotion)promotion );
        }
        break;

      case CALCULATOR_CRITERION_AND_RATINGS:
        if ( promotion.isAbstractRecognitionPromotion() )
        {
          if ( promotion.getCalculator() != null )
          {
            hydrateCalculatorCriterionAndRatings( (AbstractRecognitionPromotion)promotion );
          }
        }
        break;

      case CALCULATOR_PAYOUTS:
        if ( promotion.isAbstractRecognitionPromotion() )
        {
          if ( promotion.getCalculator() != null )
          {
            hydrateCalculatorPayouts( (AbstractRecognitionPromotion)promotion );
          }
        }
        break;
      case ALL_AUDIENCES_WITH_PAXS:
        hydratePrimaryAudiences( promotion );
        for ( Iterator iter = promotion.getPromotionPrimaryAudiences().iterator(); iter.hasNext(); )
        {
          PromotionAudience temp = (PromotionAudience)iter.next();
          Audience audience = temp.getAudience();
          if ( audience instanceof PaxAudience )
          {
            initialize( ( (PaxAudience)audience ).getAudienceParticipants() );
          }
        }

        hydrateSecondaryAudiences( promotion );
        for ( Iterator iter = promotion.getPromotionSecondaryAudiences().iterator(); iter.hasNext(); )
        {
          PromotionAudience temp = (PromotionAudience)iter.next();
          Audience audience = temp.getAudience();
          if ( audience instanceof PaxAudience )
          {
            initialize( ( (PaxAudience)audience ).getAudienceParticipants() );
          }
        }

        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydratePartnerAudiences( promotion );
          for ( Iterator iter = promotion.getPromotionPartnerAudiences().iterator(); iter.hasNext(); )
          {
            PromotionAudience temp = (PromotionAudience)iter.next();
            Audience audience = temp.getAudience();
            if ( audience instanceof PaxAudience )
            {
              initialize( ( (PaxAudience)audience ).getAudienceParticipants() );
            }
          }
        }
        if ( promotion.isThrowdownPromotion() )
        {
          hydratePromotionDivisions( (ThrowdownPromotion)promotion );
        }
        break;

      case GOAL_LEVELS:
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydratePromotionGoalLevels( (GoalQuestPromotion)promotion );
        }
        break;
      case PARTNER_GOAL_LEVELS:
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydratePartnerGoalLevels( (GoalQuestPromotion)promotion );
        }
        break;
      case CP_LEVELS:
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydratePromotionCPLevels( (GoalQuestPromotion)promotion );
        }
        break;
      case PROMOTION_MERCHANDISE_COUNTRIES:
        if ( promotion.isAbstractRecognitionPromotion() )
        {
          hydratePromoMerchCountries( promotion );
        }
        break;
      case PROMOTION_PARTICIPANT_PARTNERS:
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          hydratePromoParticipantPartners( (GoalQuestPromotion)promotion );
        }
        break;
      case PROMOTION_PUBLIC_RECOGNITION_AUDIENCE:
        if ( promotion.isRecognitionPromotion() )
        {
          hydratePromotionPublicRecognitionAudiences( (RecognitionPromotion)promotion );
        }
        else if ( promotion.isNominationPromotion() )
        {
          hydratePromotionPublicRecognitionAudiences( (NominationPromotion)promotion );
        }
        break;
      case NONSWEEP_PROMO_BILLCODES:
        hydratePromotionBillCodes( promotion );
        break;
      case SWEEP_PROMO_BILLCODES:
        hydrateSweepstakesBillCodes( promotion );
        break;

      case NOM_BUDGET_APPROVER:
        if ( promotion.isNominationPromotion() )
        {
          hydrateBudgetApprover( (NominationPromotion)promotion );
        }
        break;

      case NOM_DEFAULT_APPROVER:
        if ( promotion.isNominationPromotion() )
        {
          hydrateDefaultApprover( (NominationPromotion)promotion );
        }
        break;

      case PROMOTION_NOMINATION_TIME_PERIODS:
        if ( promotion.isNominationPromotion() )
        {
          hydrateNominationTimePeriods( (NominationPromotion)promotion );
        }
        break;
      case PROMOTION_NOMINATION_WIZARD:
        if ( promotion.isNominationPromotion() )
        {
          hydrateNominationWizards( promotion );
        }
        break;
      case NOMINATION_PROMOTION_LEVEL_CALCULATOR:
        if ( promotion.isNominationPromotion() )
        {
          hydrateNominationPromotionLevelCalculator( (NominationPromotion)promotion );
        }
        break;
      default:
        break;
    }
  }

  /**
   * @param promotion
   */
  private void hydrateQuizQuestionsAndAnswers( QuizPromotion promotion )
  {
    Quiz quiz = promotion.getQuiz();
    initialize( quiz );
    List quizQuestions = quiz.getQuizQuestions();
    initialize( quizQuestions );
    for ( Iterator iter = quizQuestions.iterator(); iter.hasNext(); )
    {
      QuizQuestion quizQuestion = (QuizQuestion)iter.next();
      initialize( quizQuestion.getQuizQuestionAnswers() );
    }

  }

  /**
   * @param promotion
   */
  private void hydrateCalculatorCriterionAndRatings( AbstractRecognitionPromotion promotion )
  {
    Calculator calculator = promotion.getCalculator();
    initialize( calculator );
    List calculatorCriterion = calculator.getCalculatorCriterion();
    initialize( calculatorCriterion );
    for ( Iterator iter = calculatorCriterion.iterator(); iter.hasNext(); )
    {
      CalculatorCriterion criterion = (CalculatorCriterion)iter.next();
      initialize( criterion.getCriterionRatings() );
    }

  }

  /**
   * @param promotion
   */
  private void hydrateCalculatorPayouts( AbstractRecognitionPromotion promotion )
  {
    Calculator calculator = promotion.getCalculator();
    initialize( calculator );
    Set calculatorpayouts = calculator.getCalculatorPayouts();
    initialize( calculatorpayouts );
  }

  /**
   * Loads the claim form definition attached on this promotion
   *
   * @param promotion
   */
  private void hydrateClaimFormStepElementValidation( Promotion promotion )
  {
    initialize( promotion.getPromotionClaimFormStepElementValidations() );
  }

  /**
   * Hydrate the webRulesAudience set.
   *
   * @param promotion
   */
  private void hydrateWebRulesAudiences( Promotion promotion )
  {
    initialize( promotion.getPromotionWebRulesAudiences() );
  }

  /**
   * @param promotion
   */
  private void hydrateClaimFormDefinition( Promotion promotion )
  {
    initialize( promotion.getClaimForm() );
    if ( promotion.getClaimForm() != null )
    {
      initialize( promotion.getClaimForm().getClaimFormSteps() );

      Iterator iter = promotion.getClaimForm().getClaimFormSteps().iterator();
      while ( iter.hasNext() )
      {
        ClaimFormStep claimFormStep = (ClaimFormStep)iter.next();
        if ( claimFormStep != null )
        {
          initialize( claimFormStep.getClaimFormStepElements() );
        }
      }
    }
  }

  /**
   * @param promotion
   */
  private void hydrateApprovalOption( Promotion promotion )
  {
    initialize( promotion.getPromotionApprovalOptions() );

    Iterator iter = promotion.getPromotionApprovalOptions().iterator();
    while ( iter.hasNext() )
    {
      PromotionApprovalOption promotionApprovalOption = (PromotionApprovalOption)iter.next();
      initialize( promotionApprovalOption.getPromotionApprovalOptionReasons() );
    }
  }

  private void hydrateCustomApproverOption( NominationPromotion promotion )
  {
    initialize( promotion.getCustomApproverOptions() );
    /*
     * for( ApproverOption approverOption : promotion.getCustomApproverOptions() ){
     * initialize(approverOption.getApproverCriteria()); for(ApproverCriteria ApproverCriteria :
     * approverOption.getApproverCriteria() ){ initialize(ApproverCriteria.getApprovers()); } }
     */
    // Down the rabbit hole... initialize the sets within sets
    Iterator<ApproverOption> optionIterator = promotion.getCustomApproverOptions().iterator();
    while ( optionIterator.hasNext() )
    {
      ApproverOption option = optionIterator.next();

      initialize( option.getApproverCriteria() );

      Iterator<ApproverCriteria> criteriaIterator = option.getApproverCriteria().iterator();
      while ( criteriaIterator.hasNext() )
      {
        ApproverCriteria criteria = criteriaIterator.next();

        initialize( criteria.getApprovers() );
      }
    }
  }

  private void hydrateNominationPromotionLevel( NominationPromotion promotion )
  {
    initialize( promotion.getNominationLevels() );
  }

  /**
   * @param promotion
   */
  private void hydrateNotifications( Promotion promotion )
  {
    initialize( promotion.getPromotionNotifications() );
  }

  private void hydrateSurvey( Promotion promotion )
  {
    initialize( ( (GoalQuestPromotion)promotion ).getPromotionGoalQuestSurveys() );
  }

  /**
   * @param promotion
   */
  private void hydrateApprovalParticipants( Promotion promotion )
  {
    initialize( promotion.getPromotionParticipantApprovers() );
    initialize( promotion.getPromotionParticipantSubmitters() );
  }

  private void hydratePromoParticipantPartners( GoalQuestPromotion promotion )
  {
    initialize( promotion.getPromotionParticipantPartners() );
    Iterator paxPartners = promotion.getPromotionParticipantPartners().iterator();
    while ( paxPartners.hasNext() )
    {
      ParticipantPartner paxPartner = (ParticipantPartner)paxPartners.next();
      initialize( paxPartner.getParticipant() );
      initialize( paxPartner.getPartner() );
    }
  }

  private void hydrateWebRulesPartners( GoalQuestPromotion promotion )
  {
    initialize( promotion.getPromotionPartnerWebRulesAudience() );
  }

  private void hydrateWebRulesManagers( GoalQuestPromotion promotion )
  {
    initialize( promotion.getPromotionManagerWebRulesAudience() );
  }

  /**
   * @param promotion
   */
  private void hydrateJournals( Promotion promotion )
  {
    initialize( promotion.getJournals() );
    Iterator journals = promotion.getJournals().iterator();
    while ( journals.hasNext() )
    {
      Journal journal = (Journal)journals.next();
      Set activityJournals = journal.getActivityJournals();
      initialize( activityJournals );
      Iterator activityJournalIter = activityJournals.iterator();
      while ( activityJournalIter.hasNext() )
      {
        ActivityJournal activityJournal = (ActivityJournal)activityJournalIter.next();
        Activity activity = activityJournal.getActivity();
        initialize( activity );
      }
    }
  }

  /**
   * Loads the list of childPromotions attached to this promotion
   *
   * @param promotion
   */
  private void hydrateChildPromotions( ProductClaimPromotion promotion )
  {
    if ( promotion.getParentPromotion() != null )
    {
      initialize( promotion.getParentPromotion().getChildPromotions() );
    }
    else
    {
      initialize( promotion.getChildPromotions() );
    }
  }

  /**
   * Hydrate the teamPositions set.
   *
   * @param promotion
   */
  private void hydrateTeamPositions( ProductClaimPromotion promotion )
  {
    initialize( promotion.getPromotionTeamPositions() );

    if ( promotion.getParentPromotion() != null )
    {
      initialize( promotion.getParentPromotion().getPromotionTeamPositions() );
    }
  }

  /**
   * Hydrate the secondaryAudience set.
   *
   * @param promotion
   */
  private void hydrateSecondaryAudiences( Promotion promotion )
  {
    initialize( promotion.getPromotionSecondaryAudiences() );

    if ( promotion.isProductClaimPromotion() )
    {
      if ( ( (ProductClaimPromotion)promotion ).getParentPromotion() != null )
      {
        initialize( ( (ProductClaimPromotion)promotion ).getParentPromotion().getPromotionSecondaryAudiences() );
      }
    }
  }

  /**
   * Hydrate the primaryAudience set.
   *
   * @param promotion
   */
  private void hydratePrimaryAudiences( Promotion promotion )
  {
    initialize( promotion.getPromotionPrimaryAudiences() );

    if ( promotion.isProductClaimPromotion() )
    {
      if ( ( (ProductClaimPromotion)promotion ).getParentPromotion() != null )
      {
        initialize( ( (ProductClaimPromotion)promotion ).getParentPromotion().getPromotionPrimaryAudiences() );
      }
    }
  }

  private void hydratePartnerAudiences( Promotion promotion )
  {
    initialize( promotion.getPromotionPartnerAudiences() );
  }

  /**
   * @param promotion
   */
  private void hydratePromotionGoalLevels( GoalQuestPromotion promotion )
  {
    initialize( promotion.getGoalLevels() );
    initialize( promotion.getManagerOverrideGoalLevels() );
    initialize( promotion.getPartnerGoalLevels() );
  }

  private void hydratePromotionDivisions( ThrowdownPromotion promotion )
  {
    initialize( promotion.getDivisions() );
    for ( Division division : promotion.getDivisions() )
    {
      initialize( division.getCompetitorsAudience() );
      for ( DivisionCompetitorsAudience audience : division.getCompetitorsAudience() )
      {
        initialize( audience );
      }
    }
  }

  private void hydratePromotionDivisionRounds( ThrowdownPromotion promotion )
  {
    initialize( promotion.getDivisions() );
    for ( Division division : promotion.getDivisions() )
    {
      initialize( division.getRounds() );
    }
  }

  private void hydratePromotionPayouts( ThrowdownPromotion promotion )
  {
    initialize( promotion.getStackStandingPayoutGroups() );
    Iterator<StackStandingPayoutGroup> stackPayoutGroupIter = promotion.getStackStandingPayoutGroups().iterator();
    while ( stackPayoutGroupIter.hasNext() )
    {
      StackStandingPayoutGroup stackPayout = stackPayoutGroupIter.next();
      initialize( stackPayout.getStackStandingPayouts() );
      initialize( stackPayout.getNodeType() );
    }

    initialize( promotion.getDivisions() );
    for ( Division division : promotion.getDivisions() )
    {
      initialize( division.getPayouts() );
    }
  }

  private void hydratePromotionDivisionMatchOutcomes( ThrowdownPromotion promotion )
  {
    initialize( promotion.getDivisions() );
    for ( Division division : promotion.getDivisions() )
    {
      initialize( division.getRounds() );
      for ( Round round : division.getRounds() )
      {
        initialize( round.getMatches() );
        for ( Match match : round.getMatches() )
        {
          initialize( match.getTeamOutcomes() );
          for ( MatchTeamOutcome matchTeamOutCome : match.getTeamOutcomes() )
          {
            initialize( matchTeamOutCome.getTeam() );
            initialize( matchTeamOutCome.getTeam().getParticipant() );
          }
        } // End of Match
      } // End of Round
    } // End of Division
  }

  private void hydratePartnerGoalLevels( GoalQuestPromotion promotion )
  {
    initialize( promotion.getPartnerGoalLevels() );
  }

  /**
   * @param promotion
   */
  private void hydratePromotionCPLevels( GoalQuestPromotion promotion )
  {
    if ( promotion.isChallengePointPromotion() )
    {
      hydratePromotionGoalLevels( promotion );
    }
  }

  private void hydratePromotionPublicRecognitionAudiences( RecognitionPromotion promotion )
  {
    initialize( promotion.getPromotionPublicRecognitionAudiences() );
  }

  private void hydratePromotionPublicRecognitionAudiences( NominationPromotion promotion )
  {
    initialize( promotion.getPromotionPublicRecognitionAudiences() );
  }

  /**
   * @param promotion
   */
  private void hydratePromotionPayoutGroups( ProductClaimPromotion promotion )
  {
    initialize( promotion.getPromotionPayoutGroups() );

    Iterator iter = promotion.getPromotionPayoutGroups().iterator();
    while ( iter.hasNext() )
    {
      PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iter.next();
      // BugFix 19223 Do not continue when this object was null
      if ( promotionPayoutGroup == null )
      {
        continue;
      }
      initialize( promotionPayoutGroup.getPromotionPayouts() );

      Iterator cIter = promotionPayoutGroup.getPromotionPayouts().iterator();
      while ( cIter.hasNext() )
      {
        PromotionPayout promotionPayout = (PromotionPayout)cIter.next();
        if ( null != promotionPayout.getProductCategory() )
        {
          initialize( promotionPayout.getProductCategory() );
          initialize( promotionPayout.getProductCategory().getParentProductCategory() );
        }
        if ( null != promotionPayout.getProduct() )
        {
          initialize( promotionPayout.getProduct() );
          if ( null != promotionPayout.getProduct().getProductCategory() )
          {
            initialize( promotionPayout.getProduct().getProductCategory() );
            initialize( promotionPayout.getProduct().getProductCategory().getParentProductCategory() );
          }
        }
      }
    }

    if ( promotion.getParentPromotion() != null )
    {
      initialize( promotion.getParentPromotion().getPromotionPayoutGroups() );

      Iterator parentIter = promotion.getParentPromotion().getPromotionPayoutGroups().iterator();
      while ( parentIter.hasNext() )
      {
        PromotionPayoutGroup parentPromoPayoutGroup = (PromotionPayoutGroup)parentIter.next();
        // BugFix 19223 Do not continue when this object was null
        if ( parentPromoPayoutGroup == null )
        {
          continue;
        }
        initialize( parentPromoPayoutGroup.getPromotionPayouts() );

        Iterator cIter = parentPromoPayoutGroup.getPromotionPayouts().iterator();
        while ( cIter.hasNext() )
        {
          PromotionPayout promotionPayout = (PromotionPayout)cIter.next();
          if ( null != promotionPayout.getProductCategory() )
          {
            initialize( promotionPayout.getProductCategory() );
            initialize( promotionPayout.getProductCategory().getParentProductCategory() );
          }
          if ( null != promotionPayout.getProduct() )
          {
            initialize( promotionPayout.getProduct() );
            if ( null != promotionPayout.getProduct().getProductCategory() )
            {
              initialize( promotionPayout.getProduct().getProductCategory() );
              initialize( promotionPayout.getProduct().getProductCategory().getParentProductCategory() );
            }
          }
        }
      }
    }
    if ( promotion.getPayoutType() != null && PromotionPayoutType.STACK_RANK.equals( promotion.getPayoutType().getCode() ) )
    {
      hydratePromotionStackRankPayoutGroups( promotion );
    }
    else if ( promotion.getParentPromotion() != null && PromotionPayoutType.STACK_RANK.equals( promotion.getParentPromotion().getPayoutType().getCode() ) )
    {
      // this is require when parent payout type is stack rank and child is not stack rank or not
      // defined yet
      initialize( promotion.getParentPromotion().getStackRankPayoutGroups() );

      Iterator parentIter = promotion.getParentPromotion().getStackRankPayoutGroups().iterator();
      while ( parentIter.hasNext() )
      {
        StackRankPayoutGroup parentPromoPayoutGroup = (StackRankPayoutGroup)parentIter.next();
        initialize( parentPromoPayoutGroup.getStackRankPayouts() );
        initialize( parentPromoPayoutGroup.getNodeType() );
      }
    }

  }

  /**
   * hydratePromotionStackRankPayoutGroups
   *
   * @param promotion
   */
  private void hydratePromotionStackRankPayoutGroups( ProductClaimPromotion promotion )
  {
    initialize( promotion.getStackRankPayoutGroups() );

    Iterator iter = promotion.getStackRankPayoutGroups().iterator();
    while ( iter.hasNext() )
    {
      StackRankPayoutGroup promotionPayoutGroup = (StackRankPayoutGroup)iter.next();
      initialize( promotionPayoutGroup.getStackRankPayouts() );
    }

    if ( promotion.getParentPromotion() != null )
    {
      initialize( promotion.getParentPromotion().getStackRankPayoutGroups() );

      Iterator parentIter = promotion.getParentPromotion().getStackRankPayoutGroups().iterator();
      while ( parentIter.hasNext() )
      {
        StackRankPayoutGroup parentPromoPayoutGroup = (StackRankPayoutGroup)parentIter.next();
        initialize( parentPromoPayoutGroup.getStackRankPayouts() );
        initialize( parentPromoPayoutGroup.getNodeType() );
      }
    }
  }

  /**
   * Hydrate the set of PromotionBehaviors.
   *
   * @param abstractRecognitionPromotion
   */
  private void hydratePromotionBehaviors( AbstractRecognitionPromotion abstractRecognitionPromotion )
  {
    initialize( abstractRecognitionPromotion.getPromotionBehaviors() );
  }

  /**
   * Hydrate the set of GiverAudiences.
   *
   * @param abstractRecognitionPromotion
   */
  private void hydratePromotionECards( AbstractRecognitionPromotion abstractRecognitionPromotion )
  {
    initialize( abstractRecognitionPromotion.getPromotionECard() );
  }

  private void hydratePromoMerchCountries( Promotion promotion )
  {
    initialize( promotion.getPromoMerchCountries() );
    for ( Iterator iter = promotion.getPromoMerchCountries().iterator(); iter.hasNext(); )
    {
      PromoMerchCountry temp = (PromoMerchCountry)iter.next();
      if ( temp.getLevels() != null )
      {
        initialize( temp.getLevels() );
      }
    }

  }

  private void hydrateHomePageItems( RecognitionPromotion recognitionPromotion )
  {
    initialize( recognitionPromotion.getHomePageItems() );
  }

  /**
   * Hydrate the set of Certificates.
   *
   * @param recognitionPromotion
   */
  private void hydratePromotionCertificates( RecognitionPromotion recognitionPromotion )
  {
    initialize( recognitionPromotion.getPromotionCertificates() );
  }

  /**
   * Hydrate the set of Certificates.
   *
   * @param quizPromotion
   */
  private void hydratePromotionCertificates( QuizPromotion quizPromotion )
  {
    initialize( quizPromotion.getPromotionCertificates() );
  }

  /**
   * Hydrate the set of Certificates.
   *
   * @param nominationPromotion
   */
  private void hydratePromotionCertificates( NominationPromotion nominationPromotion )
  {
    initialize( nominationPromotion.getPromotionCertificates() );
  }

  /**
   * Hydrate the SSI Contest Approval Level 1 Audiences.
   *
   * @param ssiPromotion
   */
  private void hydrateSSIContestApprovalLevel1Audiences( SSIPromotion ssiPromotion )
  {
    initialize( ssiPromotion.getContestApprovalLevel1Audiences() );
  }

  /**
   * Hydrate the SSI Contest Approval Level 2 Audiences.
   *
   * @param ssiPromotion
   */
  private void hydrateSSIContestApprovalLevel2Audiences( SSIPromotion ssiPromotion )
  {
    initialize( ssiPromotion.getContestApprovalLevel2Audiences() );
  }

  /**
   * Hydrate the set of PromotionSweepstakes.
   *
   * @param promotion
   */
  private void hydratePromotionSweepstakes( Promotion promotion )
  {
    initialize( promotion.getPromotionSweepstakes() );
    Iterator iter = promotion.getPromotionSweepstakes().iterator();
    while ( iter.hasNext() )
    {
      PromotionSweepstake promotionSweepstake = (PromotionSweepstake)iter.next();
      initialize( promotionSweepstake.getWinners() );
    }
  }

  /**
   * Hydrate the set of PromotionSweepstakes winner addresses.
   *
   * @param promotion
   */
  private void hydratePromotionSweepstakesWinnerAddress( Promotion promotion )
  {
    initialize( promotion.getPromotionSweepstakes() );
    Iterator iter = promotion.getPromotionSweepstakes().iterator();
    while ( iter.hasNext() )
    {
      PromotionSweepstake promotionSweepstake = (PromotionSweepstake)iter.next();
      initialize( promotionSweepstake.getWinners() );
      for ( Iterator winnerIter = (Iterator)promotionSweepstake.getWinners().iterator(); winnerIter.hasNext(); )
      {
        PromotionSweepstakeWinner promotionSweepstakeWinner = (PromotionSweepstakeWinner)winnerIter.next();
        if ( promotionSweepstakeWinner.getParticipant() != null )
        {
          initialize( promotionSweepstakeWinner.getParticipant() );
          initialize( promotionSweepstakeWinner.getParticipant().getPrimaryAddress() );
        }
      }
    }
  }

  /**
   * Hydrate the Promotion BudgetMaster and Budgets
   *
   * @param promotion
   */
  private void hydratePromotionBudgetMaster( Promotion promotion )
  {

    initialize( promotion.getBudgetMaster() );
    initialize( promotion.getCashBudgetMaster() );
    /* Bug # 35756 fix start *********************************** */
    /*
     * Bug fix - central budgets do not have nodes or users attached to them so the following code
     * is unnecessary. Plus it slows down the login experience. if ( promotion.getBudgetMaster() !=
     * null && promotion.getBudgetMaster().getBudgetType().getCode().equals(
     * BudgetType.CENTRAL_BUDGET_TYPE ) ) { initialize( promotion.getBudgetMaster().getBudgets() );
     * Iterator iter = promotion.getBudgetMaster().getBudgets().iterator(); while ( iter.hasNext() )
     * { Budget budget = (Budget)iter.next(); if (
     * promotion.getBudgetMaster().getBudgetType().getCode() .equals( BudgetType.NODE_BUDGET_TYPE )
     * ) { initialize( budget.getNode() ); } else { initialize( budget.getUser() ); } } } Bug #
     * 35756 fix end ****************************************
     */
  }

  /**
   * Hydrate the Promotion BudgetMaster, Budget Segments and Budgets
   *
   * @param promotion
   */
  private void hydrateAllPromotionBudgetMaster( Promotion promotion )
  {

    initialize( promotion.getBudgetMaster() );

    if ( promotion.getBudgetMaster() != null )
    {
      initialize( promotion.getBudgetMaster().getBudgetSegments() );

      Iterator<BudgetSegment> iterSegment = promotion.getBudgetMaster().getBudgetSegments().iterator();
      while ( iterSegment.hasNext() )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iterSegment.next();
        initialize( budgetSegment.getBudgets() );

        Iterator<Budget> iterBudget = budgetSegment.getBudgets().iterator();
        while ( iterBudget.hasNext() )
        {
          Budget budget = (Budget)iterBudget.next();

          if ( promotion.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
          {
            initialize( budget.getNode() );
          }
          else
          {
            initialize( budget.getUser() );
          }
        }
      }
    }

    initialize( promotion.getCashBudgetMaster() );

    if ( promotion.getCashBudgetMaster() != null )
    {
      initialize( promotion.getCashBudgetMaster().getBudgetSegments() );

      Iterator<BudgetSegment> iterSegment = promotion.getCashBudgetMaster().getBudgetSegments().iterator();
      while ( iterSegment.hasNext() )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iterSegment.next();
        initialize( budgetSegment.getBudgets() );

        Iterator<Budget> iterBudget = budgetSegment.getBudgets().iterator();
        while ( iterBudget.hasNext() )
        {
          Budget budget = (Budget)iterBudget.next();

          if ( promotion.getCashBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
          {
            initialize( budget.getNode() );
          }
          else
          {
            initialize( budget.getUser() );
          }
        }
      }
    }
  }

  /**
   * Hydrate the PublicRecognition BudgetMaster, Budget Segments and Budgets
   *
   * @param promotion
   */
  private void hydrateAllPromotionPublicRecBudgetMaster( RecognitionPromotion recPromo )
  {

    initialize( recPromo.getPublicRecogBudgetMaster() );

    if ( recPromo.getPublicRecogBudgetMaster() != null )
    {
      initialize( recPromo.getPublicRecogBudgetMaster().getBudgetSegments() );

      Iterator<BudgetSegment> iterSegment = recPromo.getPublicRecogBudgetMaster().getBudgetSegments().iterator();
      while ( iterSegment.hasNext() )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iterSegment.next();
        initialize( budgetSegment.getBudgets() );

        Iterator<Budget> iterBudget = budgetSegment.getBudgets().iterator();
        while ( iterBudget.hasNext() )
        {
          Budget budget = (Budget)iterBudget.next();

          if ( recPromo.getPublicRecogBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
          {
            initialize( budget.getNode() );
          }
          else
          {
            initialize( budget.getUser() );
          }
        }
      }
    }
  }

  /**
   * Hydrate the PublicRecognition BudgetMaster, Budget Segments and Budgets
   *
   * @param promotion
   */
  private void hydrateAllPromotionPublicRecBudgetMaster( NominationPromotion nomPromo )
  {

    initialize( nomPromo.getPublicRecogBudgetMaster() );

    if ( nomPromo.getPublicRecogBudgetMaster() != null )
    {
      initialize( nomPromo.getPublicRecogBudgetMaster().getBudgetSegments() );

      Iterator<BudgetSegment> iterSegment = nomPromo.getPublicRecogBudgetMaster().getBudgetSegments().iterator();
      while ( iterSegment.hasNext() )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iterSegment.next();
        initialize( budgetSegment.getBudgets() );

        Iterator<Budget> iterBudget = budgetSegment.getBudgets().iterator();
        while ( iterBudget.hasNext() )
        {
          Budget budget = (Budget)iterBudget.next();

          if ( nomPromo.getPublicRecogBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
          {
            initialize( budget.getNode() );
          }
          else
          {
            initialize( budget.getUser() );
          }
        }
      }
    }
  }

  private void hydratePromotionBillCodes( Promotion promotion )
  {
    initialize( promotion.getPromotionBillCodes() );
  }

  private void hydrateSweepstakesBillCodes( Promotion promotion )
  {
    initialize( promotion.getSweepstakesBillCodes() );
  }

  private void hydrateBudgetApprover( NominationPromotion promotion )
  {
    initialize( promotion.getBudgetApprover() );
  }

  private void hydrateDefaultApprover( NominationPromotion promotion )
  {
    initialize( promotion.getDefaultApprover() );
  }

  private void hydrateNominationTimePeriods( NominationPromotion promotion )
  {
    initialize( promotion.getNominationTimePeriods() );
  }

  private void hydrateNominationWizards( Promotion promotion )
  {
    initialize( promotion.getPromotionWizardOrder() );
  }

  private void hydrateNominationPromotionLevelCalculator( NominationPromotion promotion )
  {
    Set<NominationPromotionLevel> promotionLevels = promotion.getNominationLevels();
    initialize( promotionLevels );

    for ( NominationPromotionLevel promotionLevel : promotionLevels )
    {
      initialize( promotionLevel.getCalculator() );
      if ( promotionLevel.getCalculator() != null )
      {
        List<CalculatorCriterion> calculatorCriterions = promotionLevel.getCalculator().getCalculatorCriterion();
        initialize( calculatorCriterions );
        for ( CalculatorCriterion criterion : calculatorCriterions )
        {
          initialize( criterion.getCriterionRatings() );
        }
        initialize( promotionLevel.getCalculator().getCalculatorPayouts() );
      }
    }
  }

}
