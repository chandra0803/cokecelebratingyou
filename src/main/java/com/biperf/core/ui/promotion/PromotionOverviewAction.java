/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionOverviewAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalOption;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.PromotionPartnerPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.promotion.PromotionWebRulesAudience;
import com.biperf.core.domain.promotion.PromotionWizard;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.process.AudienceExtractionProcess;
import com.biperf.core.process.MailingProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.claim.ClaimFormAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionNotificationService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.throwdown.ParticipantAudienceConflictResult;
import com.biperf.core.service.throwdown.ThrowdownAudienceValidationResult;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionOverviewAction.
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
 * <td>July 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PromotionOverviewAction extends BaseDispatchAction
{

  /**
   * Default method will handle calls to this Action without a 'Method' request param. Overridden
   * from
   * 
   * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long promotionId;

    request.getSession().setAttribute( ViewAttributeNames.PAGE_MODE, ViewAttributeNames.NORMAL_MODE );
    request.getSession().removeAttribute( PromotionWizardManager.SESSION_KEY );
    if ( RequestUtils.containsAttribute( request, "promotionId" ) )
    {
      promotionId = RequestUtils.getRequiredAttributeLong( request, "promotionId" );
    }
    else
    {
      try
      {
        String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
        }
        if ( promotionId == null )
        {
          ActionMessages errors = new ActionMessages();
          errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "promotionId as part of clientState" ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    Promotion promotion = getPromotionWithAssociations( promotionId );

    PromotionOverviewForm promoOverviewForm = (PromotionOverviewForm)form;

    promoOverviewForm.loadPromotion( promotion );

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion gqPromo = (GoalQuestPromotion)promotion;
      boolean isPointsType = false;
      boolean isPartnerAudienceDefined = promotion.getPartnerAudienceType() != null ? true : false;
      if ( promotion.isGoalQuestPromotion() )
      {
        isPointsType = promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      }
      else if ( promotion.isChallengePointPromotion() )
      {
        isPointsType = ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      }
      if ( isPointsType && isPartnerAudienceDefined )
      {
        if ( gqPromo.getPartnerPayoutStructure() != null )
        {
          promoOverviewForm.setPartnerPayoutStructure( gqPromo.getPartnerPayoutStructure().getName() );
        }
      }
      // BugFix 20294.
      if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
      {
        promoOverviewForm.setPayoutStructure( null );
      }
    }

    setFormRules( promotion, request );
    if ( promotion.isProductClaimPromotion() )
    {
      setProductCount( promotion.getId(), promoOverviewForm );
    }

    setAudiences( promotion, promoOverviewForm );

    setNotifications( promotion, request, promoOverviewForm );

    setWebRules( promotion, promoOverviewForm );
    setWarningMessagesIfAny( request );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );

  }

  @SuppressWarnings( "unchecked" )
  private Promotion getPromotionWithAssociations( Long promotionId )
  {
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();

    // promoAssociationRequestCollection.add( new PromotionAssociationRequest(
    // PromotionAssociationRequest.ALL_AUDIENCES_WITH_PAXS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CHILD_PROMOTIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_STEP_ELEMENT_VALIDATION ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMO_PAYOUT ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    // Commenting Sweeps association for Bug 78477
    // promoAssociationRequestCollection.add( new PromotionAssociationRequest(
    // PromotionAssociationRequest.PROMOTION_SWEEPSTAKES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.QUIZ_QUESTIONS_AND_ANSWERS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.TEAM_POSITIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_PUBLIC_RECOGNITION_AUDIENCE ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_PARTNERS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_1_AUDIENCE ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_2_AUDIENCE ) );
    return getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward markAsComplete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionOverviewForm promoForm = (PromotionOverviewForm)form;
    ActionMessages errors = new ActionMessages();

    Long promotionId = null;
    if ( RequestUtils.containsAttribute( request, "promotionId" ) )
    {
      promotionId = RequestUtils.getRequiredAttributeLong( request, "promotionId" );
    }
    else
    {
      try
      {
        String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        try
        {
          if ( clientStateMap.get( "promotionId" ) != null )
          {
            promotionId = Long.parseLong( clientStateMap.get( "promotionId" ).toString() );
          }
          if ( clientStateMap.get( "claimFormId" ) != null )
          {
            String claimFormId = clientStateMap.get( "claimFormId" ).toString();

            if ( StringUtils.isNotBlank( claimFormId ) )
            {
              promoForm.setClaimFormId( Long.parseLong( claimFormId ) );
            }
          }
          if ( clientStateMap.get( "version" ) != null )
          {
            String version = clientStateMap.get( "version" ).toString();

            if ( StringUtils.isNotBlank( version ) )
            {
              promoForm.setVersion( Long.parseLong( version ) );
            }
          }
        }
        catch( ClassCastException cce )
        {
          throw cce;
        }

      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_PARTNERS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_WIZARD ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_PUBLIC_RECOGNITION_AUDIENCE ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CUSTOM_APPROVER_OPTIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );

    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    // Only one live or completed diyQuizPromo or Engagement or SSI promotion is allowed
    if ( promotion.isDIYQuizPromotion() || promotion.isEngagementPromotion() || promotion.isSSIPromotion() )
    {
      QuizPromotion liveOrCompletedDIYQuizpromotion = null;
      EngagementPromotion liveOrCompletedEngagementPromotion = null;
      SSIPromotion liveOrCompletedSsiPromotion = null;
      if ( promotion.isDIYQuizPromotion() )
      {
        liveOrCompletedDIYQuizpromotion = getPromotionService().getLiveDIYQuizPromotion();
        if ( liveOrCompletedDIYQuizpromotion != null )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.MANAGER_DIY_EXISTS" ) );
        }
      }
      else if ( promotion.isEngagementPromotion() )
      {
        liveOrCompletedEngagementPromotion = getPromotionService().getLiveOrCompletedEngagementPromotion();
        if ( liveOrCompletedEngagementPromotion != null )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.ENGAGEMENT_PROMO_EXISTS" ) );
        }
      }
      else if ( promotion.isSSIPromotion() )
      {
        liveOrCompletedSsiPromotion = getSsiPromotionService().getLiveOrCompletedSSIPromotion();
        if ( liveOrCompletedSsiPromotion != null )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.SSI_PROMO_EXISTS" ) );
        }
      }
      if ( liveOrCompletedDIYQuizpromotion != null || liveOrCompletedEngagementPromotion != null || liveOrCompletedSsiPromotion != null )
      {
        saveErrors( request, errors );
        loadPromotionOverviewForm( promoForm, promotion, request );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }

    promotion.setVersion( promoForm.getVersion() );

    try
    {
      if ( !isCompletePromotion( promotion, request ) )
      {
        setFormRules( promotion, request );
        loadPromotionOverviewForm( promoForm, promotion, request );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }
    catch( ServiceErrorException see )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
      saveErrors( request, errors );
      promoForm.loadPromotion( promotion );
      setFormRules( promotion, request );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    ActionForward forward = null;

    if ( promotion.isThrowdownPromotion() )
    {
      // first, re-validate the Division Audiences again -it might have changed if an admin saved
      // the
      // promotion and came back later.
      ThrowdownAudienceValidationResult result = getThrowdownService().getAudienceValidationResults( ( (ThrowdownPromotion)promotion ).getDivisions() );
      if ( !result.isValidPromotionAudience() )
      {
        StringBuilder sb = new StringBuilder();
        for ( ParticipantAudienceConflictResult conflict : result.getConflictingAudienceMembers() )
        {
          sb.append( "<br/> " + conflict.getParticipant().getId() + " " + conflict.getParticipant().getNameFLNoComma() + ": " );
          for ( Division division : conflict.getDivisions() )
          {
            sb.append( division.getDivisionName() + "," );
          }
        }
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.audience.DUPLICATE_PAX_IN_AUDIENCES", sb.toString() ) );
        saveErrors( request, errors );
        loadPromotionOverviewForm( promoForm, promotion, request );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
      else
      {
        // because this process call services that initialize additional process (in different
        // threads) - we need to make
        // sure the mailing process has been created. Realistically, this should never need to be
        // run, but this should
        // ensure that the MailingProcess has be initialized.
        getProcessService().createOrLoadSystemProcess( MailingProcess.PROCESS_NAME, MailingProcess.BEAN_NAME );
        getThrowdownService().scheduleFirstRound( promotion.getId(), Calendar.getInstance().getTime() );
      }
    }

    if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isMobAppEnabled() )
    {
      boolean requiredFields = false;
      AssociationRequestCollection arc = new AssociationRequestCollection();
      arc.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );

      ClaimForm claimForm = getClaimFormService().getClaimFormByIdWithAssociations( ( (RecognitionPromotion)promotion ).getClaimForm().getId(), arc );

      for ( ClaimFormStep claimFormStep : claimForm.getClaimFormSteps() )
      {
        for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
        {
          if ( claimFormStepElement.isRequired() )
          {
            requiredFields = true;
            break;
          }
        }
      }
      if ( requiredFields )
      {
        errors.add( "activityForm", new ActionMessage( "promotion.basics.errors.MOB_APP_ACTIVITY_FORM" ) );
      }

      // Restrict Plateau type award if MobAPP enabled
      if ( ( (RecognitionPromotion)promotion ).getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
      {
        errors.add( "awardsType", new ActionMessage( "promotion.basics.errors.MOB_APP_AWARDS_TYPE" ) );
      }

      // Restrict File load entry if MobAPP enabled
      if ( ( (RecognitionPromotion)promotion ).isFileLoadEntry() )
      {
        errors.add( "issuanceMethod", new ActionMessage( "promotion.basics.errors.MOB_APP_ISSUANCE_METHOD" ) );
      }

      // Restrict PURL if MobAPP enabled
      if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
      {
        errors.add( "includePurl", new ActionMessage( "promotion.basics.errors.MOB_APP_PURL" ) );
      }

      if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) && promotion.getCalculator() != null && ( (RecognitionPromotion)promotion ).getAwardAmountFixed() == null
          && ( (RecognitionPromotion)promotion ).getAwardAmountMax() == null && ( (RecognitionPromotion)promotion ).getAwardAmountMin() == null )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.basics.errors.MOB_APP_CALCULATOR" ) );
      }

      if ( !errors.isEmpty() && errors.size() > 0 )
      {
        saveErrors( request, errors );
        loadPromotionOverviewForm( promoForm, promotion, request );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }

    Date submissionStartDate = promotion.getSubmissionStartDate();
    Date currentDate = new Date();
    // if the submission start date is has passed, automatically set the promotion to live
    if ( submissionStartDate.before( currentDate ) )
    {
      promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    }
    else
    {
      promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) );
    }

    try
    {
      getPromotionService().savePromotion( promotion );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      // Survey Promotion - email audience extract to the user who marks the promotion as complete
      if ( promotion.isSurveyPromotion() )
      {
        extractAudience( (SurveyPromotion)promotion );
      }

      forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }
    return forward;
  }

  private void loadPromotionOverviewForm( PromotionOverviewForm form, Promotion promotion, HttpServletRequest request )
  {
    form.loadPromotion( promotion );
    setNotifications( promotion, request, form );
  }

  private void extractAudience( SurveyPromotion promotion )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "audienceExtractionProcess", AudienceExtractionProcess.BEAN_NAME );

    LinkedHashMap parameterMap = new LinkedHashMap();

    parameterMap.put( "promotionId", new String[] { promotion.getId().toString() } );

    // Launch the Audience Extraction Process to creates a .csv file of audience data and email it
    // to the user
    getProcessService().launchProcess( process, parameterMap, UserManager.getUserId() );

  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward launchPromo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long promotionId = getPromotionId( request );

    Promotion promotion = getPromotionService().getPromotionById( promotionId );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );

    ActionMessages errors = new ActionMessages();
    try
    {
      getPromotionService().savePromotion( promotion );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    ActionForward forward = null;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }

    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward endPromo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    Long promotionId = getPromotionId( request );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();

    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CHILD_PROMOTIONS ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
    promotion.setSubmissionEndDate( new Date() );
    // Bug 54567 - Start
    if ( promotion.isThrowdownPromotion() )
    {
      promotion.setTileDisplayEndDate( new Date() );
    }
    // Bug 54567 - Start
    // Bug fix 36166 - Start
    if ( promotion.isWebRulesActive() && promotion.getWebRulesEndDate() == null )
    {
      promotion.setWebRulesEndDate( new Date() );
    }
    // Bug fix 36166 - End

    // Check to see if there are any children for this promotion to expire as well
    if ( promotion.getChildrenCount() > 0 )
    {
      Iterator childIterator = ( (ProductClaimPromotion)promotion ).getChildPromotions().iterator();
      while ( childIterator.hasNext() )
      {
        Promotion childPromotion = (Promotion)childIterator.next();
        childPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
        childPromotion.setSubmissionEndDate( new Date() );
        if ( promotion.isThrowdownPromotion() )
        {
          promotion.setTileDisplayEndDate( new Date() );
        }
      }
    }

    try
    {
      getPromotionService().savePromotion( promotion );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    getAudienceService().clearPromoEligibilityCache();
    ActionForward forward = null;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }

    return forward;
  }

  /**
   * Sets a list of ClaimFormStepValidation Lists into the request
   * 
   * @param promotion
   * @param request
   */
  private void setFormRules( Promotion promotion, HttpServletRequest request )
  {
    // get the list of form rules
    // List formRules = new ArrayList();

    if ( !promotion.isDIYQuizPromotion() && !promotion.isEngagementPromotion() && !promotion.isQuizPromotion() && !promotion.isWellnessPromotion() && !promotion.isSurveyPromotion()
        && !promotion.isGoalQuestOrChallengePointPromotion() && !promotion.isThrowdownPromotion() && !promotion.isSSIPromotion() )
    {
      List claimFormSteps = promotion.getClaimForm().getClaimFormSteps();

      /*
       * if ( claimFormSteps != null && claimFormSteps.size() > 0 ) { Iterator it =
       * claimFormSteps.iterator(); while ( it.hasNext() ) { ClaimFormStep claimFormStep =
       * (ClaimFormStep)it.next(); List claimFormStepValidations =
       * getPromotionService().getAllPromotionClaimFormStepElementValidations( promotion,
       * claimFormStep ); if ( claimFormStepValidations.size() > 0 ) { formRules.add(
       * claimFormStepValidations ); } } }
       */

      request.setAttribute( "formRules", claimFormSteps );
    }
  }

  private void setProductCount( Long promotionId, PromotionOverviewForm promoOverviewForm )
  {
    int productCount = getProductService().getProductsByPromotion( promotionId ).size();
    promoOverviewForm.setNumberOfProducts( productCount );
  }

  private void setNotifications( Promotion promotion, HttpServletRequest request, PromotionOverviewForm promoOverviewForm )
  {
    if ( getPromotionNotificationService().getPromotionTypeNotificationsByPromotionId( promotion.getId() ).size() == 0
        && getPromotionNotificationService().getClaimFormTypeNotificationsByPromotionId( promotion.getId() ).size() == 0 )
    {
      promoOverviewForm.setNotificationExists( false );
    }
    else
    {
      promoOverviewForm.setNotificationExists( true );
    }

    if ( promotion.isSurveyPromotion() )
    {
      List<PromotionGoalQuestSurvey> promotionSurveyNotifications = getSurveyService().getPromotionGoalQuestSurveysByPromotionId( promotion.getId() );

      if ( promotionSurveyNotifications != null && !promotionSurveyNotifications.isEmpty() )
      {
        request.setAttribute( "promotionSurveyNotifications", true );
      }
    }

    List notificationlist = getPromotionNotificationService().getPromotionTypeNotificationsByPromotionId( promotion.getId() );
    if ( promotion.isGoalQuestPromotion() )
    {
      boolean isPointsType = promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      if ( ( !isPointsType || promotion.getPartnerAudienceType() == null ) && notificationlist != null && notificationlist.size() > 0 )
      {
        for ( Iterator iter = notificationlist.iterator(); iter.hasNext(); )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)iter.next();
          if ( promotionNotificationType.getPromotionEmailNotificationType() != null
              && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_SELECTED )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED ) ) )
          {
            iter.remove();
          }

        }
      }
      List<PromotionGoalQuestSurvey> promotionSurveyNotifications = getSurveyService().getPromotionGoalQuestSurveysByPromotionId( promotion.getId() );
      if ( promotionSurveyNotifications != null && !promotionSurveyNotifications.isEmpty() )
      {
        request.setAttribute( "promotionSurveyNotifications", true );
      }
    }

    if ( promotion.isChallengePointPromotion() )
    {
      boolean isPointsType = ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      if ( ( !isPointsType || promotion.getPartnerAudienceType() == null ) && notificationlist != null && notificationlist.size() > 0 )
      {
        for ( Iterator iter = notificationlist.iterator(); iter.hasNext(); )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)iter.next();
          if ( promotionNotificationType.getPromotionEmailNotificationType() != null
              && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_GOAL_ACHIEVED )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_GOAL_NOT_ACHIEVED )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_PROGRESS_UPDATED ) ) )
          {
            iter.remove();
          }

        }
      }
    }
    if ( promotion.isRecognitionPromotion() && notificationlist != null && notificationlist.size() > 0 )
    {
      if ( ! ( (RecognitionPromotion)promotion ).isIncludePurl() )
      {
        for ( Iterator iter = notificationlist.iterator(); iter.hasNext(); )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)iter.next();
          if ( promotionNotificationType.getPromotionEmailNotificationType() != null
              && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE )
                  || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION ) ) )
          {
            iter.remove();
          }

        }
      }
    }
    setSsiPromotionNotifications( promotion, notificationlist, promoOverviewForm );
    request.setAttribute( "promoNotificationList", notificationlist );
    request.setAttribute( "claimFormNotificationList", getPromotionNotificationService().getClaimFormTypeNotificationsByPromotionId( promotion.getId() ) );
  }

  // Commented out ssi claim notifications as claim submission and approval moved to SSI_Phase_2
  private void setSsiPromotionNotifications( Promotion promotion, List notificationList, PromotionOverviewForm form )
  {
    if ( promotion.isSSIPromotion() && notificationList != null && notificationList.size() > 0 )
    {
      int creatorNotificationsEmailCount = 0;
      int approverNotificationsEmailCount = 0;
      int awardContestEmailCount = 0;
      int doThisContestEmailCount = 0;
      int objectiveContestEmailCount = 0;
      int stackRankContestEmailCount = 0;
      int stepItUpContestEmailCount = 0;

      for ( Iterator iter = notificationList.iterator(); iter.hasNext(); )
      {
        PromotionNotificationType promotionNotificationType = (PromotionNotificationType)iter.next();

        if ( promotionNotificationType.getPromotionEmailNotificationType() != null
            && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR ) )
            && promotionNotificationType.getNotificationMessageId() != -1 )
        {
          creatorNotificationsEmailCount++;
        }
        else if ( promotionNotificationType.getPromotionEmailNotificationType() != null
            && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_APPROVAL_NOTIFY_APPROVER )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_CLAIM_APPROVAL_NOTIFY_APPROVER ) )
            && promotionNotificationType.getNotificationMessageId() != -1 )
        {
          approverNotificationsEmailCount++;
        }
        else if ( promotionNotificationType.getPromotionEmailNotificationType() != null
            && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW ) )
            && promotionNotificationType.getNotificationMessageId() != -1 )
        {
          awardContestEmailCount++;
        }
        else if ( promotionNotificationType.getPromotionEmailNotificationType() != null
            && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT ) )
            && promotionNotificationType.getNotificationMessageId() != 0 && promotionNotificationType.getNotificationMessageId() != -1 )
        {
          doThisContestEmailCount++;
        }
        else if ( promotionNotificationType.getPromotionEmailNotificationType() != null
            && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES ) )
            && promotionNotificationType.getNotificationMessageId() != 0 && promotionNotificationType.getNotificationMessageId() != -1 )
        {
          objectiveContestEmailCount++;
        }
        else if ( promotionNotificationType.getPromotionEmailNotificationType() != null
            && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK ) )
            && promotionNotificationType.getNotificationMessageId() != 0 && promotionNotificationType.getNotificationMessageId() != -1 )
        {
          stackRankContestEmailCount++;
        }
        else if ( promotionNotificationType.getPromotionEmailNotificationType() != null
            && ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
                    | promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP )
                || promotionNotificationType.getPromotionEmailNotificationType().getCode().equalsIgnoreCase( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP ) )
            && promotionNotificationType.getNotificationMessageId() != 0 && promotionNotificationType.getNotificationMessageId() != -1 )
        {
          stepItUpContestEmailCount++;
        }
      }
      SSIPromotion ssiPromotion = (SSIPromotion)promotion;

      form.setCreatorNotificationsEmailCount( String.valueOf( creatorNotificationsEmailCount ) );
      form.setApproverNotificationsEmailCount( String.valueOf( approverNotificationsEmailCount ) );

      if ( ssiPromotion.isAwardThemNowSelected() )
      {
        form.setAwardThemNowContestEmailCount( String.valueOf( awardContestEmailCount ) );
      }
      else
      {
        form.setAwardThemNowContestEmailCount( null );
      }

      if ( ssiPromotion.isDoThisGetThatSelected() )
      {
        form.setDoThisGetThatContestEmailCount( String.valueOf( doThisContestEmailCount ) );
      }
      else
      {
        form.setDoThisGetThatContestEmailCount( null );
      }

      if ( ssiPromotion.isObjectivesSelected() )
      {
        form.setObjectivesContestEmailCount( String.valueOf( objectiveContestEmailCount ) );
      }
      else
      {
        form.setObjectivesContestEmailCount( null );
      }

      if ( ssiPromotion.isStackRankSelected() )
      {
        form.setStackRankContestEmailCount( String.valueOf( stackRankContestEmailCount ) );
      }
      else
      {
        form.setStackRankContestEmailCount( null );
      }

      if ( ssiPromotion.isStepItUpSelected() )
      {
        form.setStepItUpContestEmailCount( String.valueOf( stepItUpContestEmailCount ) );
      }
      else
      {
        form.setStepItUpContestEmailCount( null );
      }
    }
  }

  private void setAudiences( Promotion promotion, PromotionOverviewForm promoOverviewForm )
  {
    List promotionAudienceSetWithPaxSize = new ArrayList();
    if ( promoOverviewForm.getSubmitterAudienceList() != null && promoOverviewForm.getSubmitterAudienceList().size() > 0 )
    {
      Iterator promotionAudienceIterator = promoOverviewForm.getSubmitterAudienceList().iterator();
      while ( promotionAudienceIterator.hasNext() )
      {
        PromotionAudience promoAudience = (PromotionAudience)promotionAudienceIterator.next();
        Audience audience = promoAudience.getAudience();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
        promotionAudienceSetWithPaxSize.add( promoAudience );
      }
      promoOverviewForm.setSubmitterAudienceList( promotionAudienceSetWithPaxSize );
    }

    List promotionAudienceSecondarySetWithPaxSize = new ArrayList();
    if ( promoOverviewForm.getTeamAudienceList() != null && promoOverviewForm.getTeamAudienceList().size() > 0 )
    {
      Iterator promotionAudienceIterator = promoOverviewForm.getTeamAudienceList().iterator();
      while ( promotionAudienceIterator.hasNext() )
      {
        PromotionAudience promoAudience = (PromotionAudience)promotionAudienceIterator.next();
        Audience audience = promoAudience.getAudience();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
        promotionAudienceSecondarySetWithPaxSize.add( promoAudience );
      }
      promoOverviewForm.setTeamAudienceList( promotionAudienceSecondarySetWithPaxSize );
    }

    Map promotionAudienceDivisionMapWithPaxSize = new HashMap();
    if ( promotion.isThrowdownPromotion() && promoOverviewForm.getDivisionAudienceMap() != null && promoOverviewForm.getDivisionAudienceMap().size() > 0 )
    {
      Iterator divsIter = promoOverviewForm.getDivisionAudienceMap().keySet().iterator();
      while ( divsIter.hasNext() )
      {
        String divName = (String)divsIter.next();
        List audiences = (List)promoOverviewForm.getDivisionAudienceMap().get( divName );
        Iterator audIter = audiences.iterator();
        List promotionAudienceDivisionListWithPaxSize = new ArrayList();
        while ( audIter.hasNext() )
        {
          DivisionCompetitorsAudience divCompAudience = (DivisionCompetitorsAudience)audIter.next();
          Audience audience = divCompAudience.getAudience();
          audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
          promotionAudienceDivisionListWithPaxSize.add( divCompAudience );
        }
        promotionAudienceDivisionMapWithPaxSize.put( divName, promotionAudienceDivisionListWithPaxSize );
      }
      promoOverviewForm.setDivisionAudienceMap( promotionAudienceDivisionMapWithPaxSize );
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      boolean isPointsType = false;
      if ( promotion.isGoalQuestPromotion() )
      {
        isPointsType = promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      }
      else if ( promotion.isChallengePointPromotion() )
      {
        isPointsType = ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      }

      boolean isPartnerAudienceDefined = promotion.getPartnerAudienceType() != null ? true : false;
      if ( isPointsType && isPartnerAudienceDefined )
      {
        promoOverviewForm.setPartnerAudienceExists( true );
        if ( promotion.getPartnerAudienceType().getCode().equalsIgnoreCase( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE )
            || promotion.getPartnerAudienceType().getCode().equalsIgnoreCase( PartnerAudienceType.USER_CHAR ) )
        {
          promoOverviewForm.setPreSelectedPartner( promotion.getPartnerAudienceType().getCode() );
        }
        else
        {
          List promotionPartnerAudienceSetWithPaxSize = new ArrayList();
          if ( promoOverviewForm.getPartnerAudienceList() != null )
          {
            Iterator promotionPartnerAudIterator = promoOverviewForm.getPartnerAudienceList().iterator();
            while ( promotionPartnerAudIterator.hasNext() )
            {
              PromotionAudience promoAudience = (PromotionAudience)promotionPartnerAudIterator.next();
              Audience audience = promoAudience.getAudience();
              audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
              promotionPartnerAudienceSetWithPaxSize.add( promoAudience );
            }
            promoOverviewForm.setPartnerAudienceList( promotionPartnerAudienceSetWithPaxSize );
          }
        }
        promoOverviewForm.setAutoCompletePartners( ( (GoalQuestPromotion)promotion ).isAutoCompletePartners() );
      }
      else
      {
        promoOverviewForm.setPartnerAudienceExists( false );
      }
    }
  }

  private void setWebRules( Promotion promotion, PromotionOverviewForm promoOverviewForm )
  {
    List promotionAudienceSetWithPaxSize = new ArrayList();
    if ( promoOverviewForm.getWebRulesAudienceList() != null && promoOverviewForm.getWebRulesAudienceList().size() > 0 )
    {
      Iterator promotionAudienceIterator = promoOverviewForm.getWebRulesAudienceList().iterator();
      while ( promotionAudienceIterator.hasNext() )
      {
        PromotionWebRulesAudience promoWebRulesAudience = (PromotionWebRulesAudience)promotionAudienceIterator.next();
        Audience audience = promoWebRulesAudience.getAudience();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
        promotionAudienceSetWithPaxSize.add( promoWebRulesAudience );
      }
      promoOverviewForm.setWebRulesAudienceList( promotionAudienceSetWithPaxSize );
    }
  }

  private int getNbrOfPaxsInCriteriaAudience( Audience audience )
  {
    int nbrOfPaxInCriteriaAudience = 0;

    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
    nbrOfPaxInCriteriaAudience = paxFormattedValueList.size();

    return nbrOfPaxInCriteriaAudience;
  }

  /**
   * Checks to see if the promotion is complete based on if all the required stages have been
   * completed
   * 
   * @param promotion
   * @param request
   * @return boolean
   */
  private boolean isCompletePromotion( Promotion promotion, HttpServletRequest request ) throws ServiceErrorException
  {
    ActionMessages errors = new ActionMessages();

    // Validate Basics Step
    if ( !isValidBasics( promotion ) )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                  new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "BASICS_LABEL" ) ) );

    }

    // Validate Claim Form Step
    if ( !promotion.isQuizPromotion() && !promotion.isDIYQuizPromotion() && !promotion.isGoalQuestOrChallengePointPromotion() && !promotion.isSurveyPromotion() && !promotion.isWellnessPromotion()
        && !promotion.isThrowdownPromotion() && !promotion.isEngagementPromotion() && !promotion.isSSIPromotion() )
    {
      if ( !promotion.hasParent() )
      {
        if ( !isValidRules( promotion ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                      new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "FORM_RULES" ) ) );

        }
      }
    }

    // Validate Budgets
    if ( !promotion.isSurveyPromotion() && !promotion.isWellnessPromotion() && !promotion.isThrowdownPromotion() && !promotion.isSSIPromotion() )
    {
      if ( !isValidNonMultiBudget( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.BUDGET_DUPLICATE_ERR" ) );
      }
    }

    // Validate Audience Step
    if ( !isValidAudience( promotion ) )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                  new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "AUDIENCES_LABEL" ) ) );

    }

    // Validate Payout Step
    if ( promotion.isProductClaimPromotion() )
    {
      if ( !isValidPayout( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "PRODUCTS_AND_PAYOUTS" ) ) );
      }
    }

    if ( promotion.isThrowdownPromotion() )
    {
      if ( !isValidPayout( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "PAYOUTS_AND_AWARDS" ) ) );
      }
    }

    if ( promotion.isNominationPromotion() )
    {
      if ( !isValidPayout( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.PAYOUT_LEVEL_TYPE" ) ) );
      }
    }

    if ( promotion.isGoalQuestPromotion() )
    {
      if ( !isValidGoals( (GoalQuestPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "GOALS_AND_PAYOUTS" ) ) );
      }
      if ( !isValidManagerOverride( (GoalQuestPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "MANAGER_OVERRIDE" ) ) );
      }
    }

    if ( promotion.isChallengePointPromotion() )
    {
      if ( !isValidChallengePointLevels( (ChallengePointPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "CHALLENGEPOINTS_AND_PAYOUT" ) ) );
      }
      if ( !isValidManagerOverride( (ChallengePointPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "MANAGER_OVERRIDE" ) ) );
      }

    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) && ( (GoalQuestPromotion)promotion ).getPartnerAudienceType() != null
          && !isValidPartnerPayout( (GoalQuestPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "PARTNER_PAYOUT" ) ) );
      }
    }

    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
      if ( !isValidECards( recPromo ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "ECARDS" ) ) );
      }

      if ( promotion.isWebRulesActive() && !isValidWebRulesTextAudience( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "WEBRULES_TEXT" ) ) );
      }
    }

    if ( promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() )
    {
      if ( !isValidProgramLevels( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.awards", "AWARD_LEVEL_NAMES" ) ) );
      }
      if ( !isValidSweepstakes( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "SWEEPSTAKES" ) ) );
      }
      if ( !isValidCalculator( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "AWARDS" ) ) );
      }
    }

    // validate ssi promotion awards stage, activity submission stage & approvals stage here
    if ( promotion.isSSIPromotion() )
    {
      if ( !isValidSsiAward( (SSIPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "AWARDS" ) ) );
      }
      if ( !isValidSsiActivitySubmission( (SSIPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.activitysubmission", "ACTIVITY_SUB" ) ) );
      }
      if ( !isValidSsiApprovals( (SSIPromotion)promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "APPROVALS_LABEL" ) ) );
      }
    }

    // no approval so no need to validate
    if ( !promotion.isQuizPromotion() && !promotion.isDIYQuizPromotion() && !promotion.isGoalQuestPromotion() && !promotion.isSurveyPromotion() && !promotion.isChallengePointPromotion()
        && !promotion.isWellnessPromotion() && !promotion.isThrowdownPromotion() && !promotion.isEngagementPromotion() && !promotion.isSSIPromotion() )
    {
      if ( !isValidApprovals( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "APPROVALS_LABEL" ) ) );
      }
    }
    if ( !promotion.isWellnessPromotion() )
    {
      if ( !isValidNotification( promotion ) || !isValidNotificationForCopyPromo( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "NOTIFICATIONS_LABEL" ) ) );
      }
    }
    if ( !promotion.isWellnessPromotion() && !promotion.isEngagementPromotion() )
    {
      if ( !isValidRulesText( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "RULES_TEXT" ) ) );
      }
    }

    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;
      if ( nomPromo.isPublicationDateActive() )
      {
        if ( nomPromo.getApprovalStartDate() != null && nomPromo.getPublicationDate().before( nomPromo.getApprovalStartDate() )
            || nomPromo.getApprovalEndDate() != null && nomPromo.getPublicationDate().before( DateUtils.getNextDay( nomPromo.getApprovalEndDate() ) ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.PUB_AFTER_APPROVAL" ) );
        }
      }

      if ( !isAwardsPayoutTypeValid( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "AWARDS" ) ) );
      }

      if ( !isValidWizardOrder( promotion ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "WIZARD" ) ) );
      }

      // Default approver field cannot be null, but the column cannot be not-null (no reasonable
      // default)
      if ( nomPromo.getDefaultApprover() == null )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "APPROVALS" ) ) );
      }

      // Validate rules for having custom approver level 1 as award
      validateFirstLevelAward( nomPromo, errors );
    }

    // validate if celebrations step is complete
    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
      if ( recPromo.isIncludeCelebrations() && recPromo.getCelebrationDisplayPeriod() == null )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
                    new ActionMessage( "promotion.errors.STAGE_INCOMPLETE", CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "CELEBRATIONS" ) ) );
      }
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      return false;
    }

    return true;
  }

  /**
   * Validates all basic promotion information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidBasics( Promotion promotion )
  {
    boolean valid = true;

    if ( StringUtil.isEmpty( promotion.getName() ) )
    {
      valid = false;
    }

    if ( promotion.getSubmissionStartDate() == null )
    {
      valid = false;
    }

    if ( !promotion.isSSIPromotion() && promotion.getClaimForm() == null )
    {
      if ( promotion.isRecognitionPromotion() || promotion.isProductClaimPromotion() )
      {
        valid = false;
      }
    }

    if ( promotion.isProductClaimPromotion() && ( (ProductClaimPromotion)promotion ).getPromotionProcessingMode() == null )
    {
      valid = false;
    }

    if ( promotion.isQuizPromotion() && ( (QuizPromotion)promotion ).getQuiz() == null )
    {
      valid = false;
    }

    if ( promotion.isSSIPromotion() && ( (SSIPromotion)promotion ).getContestGuideUrl() == null )
    {
      valid = false;
    }

    return valid;
  }

  /**
   * Validates all form rule information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidRules( Promotion promotion )
  {
    // Get the list of claimFormSteps for the given promotion
    List claimFormSteps = promotion.getClaimForm().getClaimFormSteps();
    // If the list of claimFormSteps is not null and > 0 iterate over that list
    // and get the list of claimFormStepElements for each claimFormStep
    if ( claimFormSteps != null && claimFormSteps.size() > 0 )
    {
      Iterator it = claimFormSteps.iterator();
      while ( it.hasNext() )
      {
        ClaimFormStep claimFormStep = (ClaimFormStep)it.next();
        List claimFormStepElements = claimFormStep.getClaimFormStepElements();
        if ( claimFormStepElements != null && claimFormStepElements.size() > 0 )
        {
          int elementsNeedingValidation = 0;
          Iterator elementIt = claimFormStepElements.iterator();
          while ( elementIt.hasNext() )
          {
            ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)elementIt.next();
            // Only claimFormStepElements that are of type number, text, or date need
            // to have a validation record.
            if ( claimFormStepElement.getClaimFormElementType().getCode().equals( "number" ) || claimFormStepElement.getClaimFormElementType().getCode().equals( "text" )
                || claimFormStepElement.getClaimFormElementType().getCode().equals( "text_box" ) || claimFormStepElement.getClaimFormElementType().getCode().equals( "date" ) )
            {
              elementsNeedingValidation = elementsNeedingValidation + 1;
            }
          }
          // For now we are just making sure that the number of elements needing
          // validation matches the number of validation records this may need to
          // change later.
          if ( elementsNeedingValidation > 0 )
          {
            List claimFormStepValidations = getPromotionService().getAllPromotionClaimFormStepElementValidations( promotion, claimFormStep );
            if ( claimFormStepValidations != null )
            {
              if ( claimFormStepValidations.size() != elementsNeedingValidation )
              {
                return false;
              }
            }
            else
            {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Validates all audience information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidAudience( Promotion promotion )
  {
    if ( promotion.getPrimaryAudienceType() != null && promotion.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.SELF_ENROLL_ONLY ) && promotion.getSecondaryAudienceType() == null )
    {
      return false;
    }
    if ( promotion.getPrimaryAudienceType() == null && promotion.getSecondaryAudienceType() == null && !promotion.isEngagementPromotion() )
    {
      return false;
    }

    // for a recognition promotion, if any promotion merch country program ID's are null, do not
    // complete
    if ( promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() )
    {
      List promoMerchCountries = getPromoMerchCountryService().getActiveCountriesInPromoRecAudience( promotion.getId(), null, null, null, null );
      for ( Iterator pmcIter = promoMerchCountries.iterator(); pmcIter.hasNext(); )
      {
        PromoMerchCountry pmc = (PromoMerchCountry)pmcIter.next();
        if ( pmc.getProgramId() == null || pmc.getProgramId().length() == 0 )
        {
          return false;
        }
      }
    }

    if ( promotion.isDIYQuizPromotion() && promotion.getPrimaryAudienceType() == null )
    {
      return false;
    }

    if ( promotion.isThrowdownPromotion() )
    {
      ThrowdownPromotion tdPromotion = (ThrowdownPromotion)promotion;
      if ( tdPromotion.getDivisions().size() == 0 )
      {
        return false;
      }
      else
      {
        Set<Division> divisions = tdPromotion.getDivisions();
        for ( Division division : divisions )
        {
          // each division need at least one competitor audience in it
          if ( division.getCompetitorsAudience() == null || division.getCompetitorsAudience().isEmpty() )
          {
            return false;
          }
        }
      }
    }

    if ( promotion.isSSIPromotion() && ( promotion.getPrimaryAudienceType() == null || promotion.getSecondaryAudienceType() == null ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Validates that the award step is complete
   */
  private boolean isValidAwards( Promotion promotion )
  {
    // Nomination promotions require that awards are active
    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      if ( !nominationPromotion.isAwardActive() )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Validates the web rules audience based on the receiver type listed on the audience. If the
   * audience is 'non-participant' - then the web rules audience cannot contain 'all eligible givers
   * and receivers' or 'all eligible receivers'
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidWebRulesTextAudience( Promotion promotion )
  {
    return true;
  }

  /**
   * Validates the sweepstakes. If this is a merchandise award type promotion then the structure
   * must be level and the number of levels must be the same for all programs and the sweepstakes
   * level must be valid.
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidSweepstakes( Promotion promotion )
  {
    if ( promotion.isSweepstakesActive() )
    {
      if ( promotion.isAbstractRecognitionPromotion() )
      {
        AbstractRecognitionPromotion abstractRecognitionPromotion = (AbstractRecognitionPromotion)promotion;
        if ( abstractRecognitionPromotion.getAwardType() != null && promotion.getAwardType().isMerchandiseAwardType() )
        {
          // Number of levels must be equal for sweepstakes
          if ( !PromotionWizardUtil.numberOfLevelsEqualForAllCountries( promotion ) )
          {
            return false;
          }
          // Check if level selected is valid
          int numberOfLevels = ( (PromoMerchCountry)abstractRecognitionPromotion.getPromoMerchCountries().iterator().next() ).getNumberOfLevels();
          if ( abstractRecognitionPromotion.getSweepstakesPrimaryAwardLevel() != null && abstractRecognitionPromotion.getSweepstakesPrimaryAwardLevel().longValue() > numberOfLevels )
          {
            return false;
          }
          if ( abstractRecognitionPromotion.getSweepstakesSecondaryAwardLevel() != null && abstractRecognitionPromotion.getSweepstakesSecondaryAwardLevel().longValue() > numberOfLevels )
          {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Validates the calculator. If this is a merchandise award type promotion then the structure must
   * be level and the number of levels must be the same for all programs and the calculator levels
   * must be valid.
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidCalculator( Promotion promotion )
  {
    if ( promotion.getCalculator() != null )
    {
      if ( promotion.isAbstractRecognitionPromotion() )
      {
        AbstractRecognitionPromotion abstractRecognitionPromotion = (AbstractRecognitionPromotion)promotion;
        if ( abstractRecognitionPromotion.getAwardType() != null && promotion.getAwardType().isMerchandiseAwardType() && abstractRecognitionPromotion.isAwardActive() )
        {
          // Calculator must be setup as merchandise calculator
          if ( promotion.getCalculator().getCalculatorAwardType() == null || !promotion.getCalculator().getCalculatorAwardType().isMerchLevelAward() )
          {
            return false;
          }
          // Number of levels must be equal for calculator
          if ( !PromotionWizardUtil.numberOfLevelsEqualForAllCountries( abstractRecognitionPromotion ) )
          {
            return false;
          }
          // Check if levels match the calculator
          int numberOfLevels = ( (PromoMerchCountry)abstractRecognitionPromotion.getPromoMerchCountries().iterator().next() ).getNumberOfLevels();
          if ( abstractRecognitionPromotion.getCalculator().getCalculatorPayouts() == null || abstractRecognitionPromotion.getCalculator().getCalculatorPayouts().size() != numberOfLevels )
          {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Validates all payout information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidPayout( Promotion promotion )
  {
    if ( promotion.isProductClaimPromotion() )
    {
      ProductClaimPromotion pcPromotion = (ProductClaimPromotion)promotion;

      if ( pcPromotion.getPayoutType() == null || pcPromotion.getPromotionPayoutGroups() == null || pcPromotion.getPromotionPayoutGroups().size() < 1 )
      {
        return false;
      }
      // For Cross Sell payouts each group must have at least 2 product to be considered
      // complete.
      if ( pcPromotion.getPayoutType().getCode().equals( PromotionPayoutType.CROSS_SELL ) )
      {
        Iterator groupIter = pcPromotion.getPromotionPayoutGroups().iterator();
        while ( groupIter.hasNext() )
        {
          PromotionPayoutGroup promoPayoutGroup = (PromotionPayoutGroup)groupIter.next();
          int productCount = getProductService().getProductsByPromotion( promotion.getId() ).size();
          if ( productCount < 2 && promoPayoutGroup.getPromotionPayoutsCount() < 2 )
          {
            return false;
          }
        }
      }
      // Bug #13398 - For team based One to One or Tiered promotions check that
      // team payout has been filled
      if ( pcPromotion.isTeamUsed() )
      {
        if ( pcPromotion.getPayoutType().getCode().equals( PromotionPayoutType.ONE_TO_ONE ) || pcPromotion.getPayoutType().getCode().equals( PromotionPayoutType.TIERED ) )
        {
          Iterator groupIter = pcPromotion.getPromotionPayoutGroups().iterator();
          while ( groupIter.hasNext() )
          {
            PromotionPayoutGroup promoPayoutGroup = (PromotionPayoutGroup)groupIter.next();
            if ( promoPayoutGroup.getTeamMemberPayout() == null )
            {
              return false;
            }
          }
        }
      }
    }
    else if ( promotion.isThrowdownPromotion() )
    {
      ThrowdownPromotion tdPromotion = (ThrowdownPromotion)promotion;
      if ( tdPromotion.getDivisions().size() == 0 )
      {
        return false;
      }
      else
      {
        Set<Division> divisions = tdPromotion.getDivisions();
        for ( Division division : divisions )
        {
          // each division need division pay out for win outcome at least.
          if ( division.getPayoutForOutcome( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.WIN ) ) == null )
          {
            return false;
          }
        }
      }
    }
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nPromotion = (NominationPromotion)promotion;
      if ( nPromotion.getPayoutLevel() == null && nPromotion.isLevelPayoutByApproverAvailable() )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Validates all goal level information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidGoals( GoalQuestPromotion promotion )
  {
    if ( promotion.getAchievementRule() == null || promotion.getGoalLevels() == null || promotion.getGoalLevels().size() < 1 )
    {
      return false;
    }

    return true;
  }

  /**
   * Validates all goal level information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidChallengePointLevels( ChallengePointPromotion promotion )
  {
    if ( promotion.getGoalLevels() == null || promotion.getGoalLevels().size() < 1 )
    {
      return false;
    }
    if ( promotion.getAwardIncrementValue() == null || promotion.getAwardPerIncrement() == null || promotion.getAwardIncrementType() == null )
    {
      return false;
    }
    if ( promotion.getAwardThresholdType() != null && promotion.getAwardThresholdType().compareTo( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE ) != 0
        && promotion.getAwardThresholdValue() == null )
    {
      return false;
    }

    return true;
  }

  /**
   * Validates all managerOverride information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidManagerOverride( ChallengePointPromotion promotion )
  {
    if ( promotion.getOverrideStructure() != null )
    {
      if ( !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) && !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.STACK_RANKING_LEVEL ) )
      {
        if ( promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT )
            || promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) )
        {
          if ( promotion.getLevelOneMgrAward() == null && promotion.getLevelOneMgrAward().intValue() < 0 )
          {
            return false;
          }
          // LevelTwo is not requried.
          /*
           * if(promotion.getLevelTwoMgrAward() == null &&
           * promotion.getLevelTwoMgrAward().intValue() < 0 ) { return false; }
           */
        }
        /*
         * else if ( promotion.getOverrideStructure().getCode().equals(
         * OverrideStructure.MANAGER_TEAM_ACHIEVEMENT_PERCENTAGE ) ) { if (
         * promotion.getOverridePercent() == null ) { return false; } } else if (
         * promotion.getOverrideStructure().getCode().equals(
         * OverrideStructure.MANAGER_ACTUAL_TEAM_ACHIEVEMENT ) ) { if ( promotion.getManagerAward()
         * == null || promotion.getTotalTeamProduction() == null ) { return false; } }
         */
        else
        {
          Set<AbstractGoalLevel> cpLevels = promotion.getGoalLevels();
          if ( cpLevels != null && cpLevels.size() > 0 )
          {
            Iterator<AbstractGoalLevel> cpLevelIter = cpLevels.iterator();
            while ( cpLevelIter.hasNext() )
            {
              GoalLevel cpLevel = (GoalLevel)cpLevelIter.next();
              if ( cpLevel.getManagerAward() == null )
              {
                return false;
              }
            }
          }
        }
      }
    }
    else
    {
      return false;
    }

    return true;
  }

  /**
   * Validates all managerOverride information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidManagerOverride( GoalQuestPromotion promotion )
  {
    if ( promotion.getOverrideStructure() != null )
    {
      if ( !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) && !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.STACK_RANKING_LEVEL ) )
      {
        if ( promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT )
            || promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.AWARD_PER_ACHIEVER ) )
        {
          if ( promotion.getLevelOneMgrAward() == null && promotion.getLevelOneMgrAward().intValue() < 0 )
          {
            return false;
          }
          // LevelTwo is not requried.
          /*
           * if(promotion.getLevelTwoMgrAward() == null &&
           * promotion.getLevelTwoMgrAward().intValue() < 0 ) { return false; }
           */

        }
        /*
         * else if ( promotion.getOverrideStructure().getCode().equals(
         * OverrideStructure.MANAGER_ACTUAL_TEAM_ACHIEVEMENT ) ||
         * promotion.getOverrideStructure().getCode().equals(
         * ManagerOverrideStructure.OVERRIDE_PERCENT ) ) { if (
         * promotion.getManagerOverrideGoalLevels() == null ||
         * promotion.getManagerOverrideGoalLevels().size() < 1 ) { return false; } }
         */
        else
        {
          Set paxGoalLevels = promotion.getGoalLevels();
          if ( paxGoalLevels != null && paxGoalLevels.size() > 0 )
          {
            Iterator paxGoalIter = paxGoalLevels.iterator();
            while ( paxGoalIter.hasNext() )
            {
              GoalLevel paxGoalLevel = (GoalLevel)paxGoalIter.next();
              if ( paxGoalLevel.getManagerAward() == null )
              {
                return false;
              }
            }
          }
        }
      }
    }
    else
    {
      return false;
    }

    return true;
  }

  private boolean isValidPartnerPayout( GoalQuestPromotion promotion )
  {
    if ( promotion.getPartnerPayoutStructure() == null || promotion.getPartnerGoalLevels() == null || promotion.getPartnerGoalLevels().size() < 1 )
    {
      return false;
    }
    Set partnerGoalLevels = promotion.getPartnerGoalLevels();
    if ( partnerGoalLevels != null )
    {
      for ( Iterator i = partnerGoalLevels.iterator(); i.hasNext(); )
      {
        PromotionPartnerPayout partnerGoallevel = (PromotionPartnerPayout)i.next();
        if ( partnerGoallevel.getPartnerAwardAmount() == null )
        {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Validates all approval information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidApprovals( Promotion promotion )
  {

    // NOTE: Extra criteria such as Claim Approval Dates, Conditional nth Claim, etc. can only be
    // set
    // via the Approvals screen which would have handled the extra validation. Thus, the
    // same validation is not repeated here.

    if ( promotion.getApprovalType() == null )
    {
      return false;
    }

    // If it is automatic immediate, then there is nothing else to check
    if ( promotion.getApprovalType().getCode().equals( ApprovalType.AUTOMATIC_IMMEDIATE ) )
    {
      return true;
    }

    // First check for approval options
    if ( promotion.getPromotionApprovalOptions() == null || promotion.getPromotionApprovalOptions().size() < 1 )
    {
      return false;
    }

    // Now, check that if 'held' or 'denied' exist then they have at least one reason.
    Iterator approvalOptions = promotion.getPromotionApprovalOptions().iterator();
    while ( approvalOptions.hasNext() )
    {
      PromotionApprovalOption approvalOption = (PromotionApprovalOption)approvalOptions.next();
      if ( approvalOption.getPromotionApprovalOptionType().getCode().equals( "held" ) || approvalOption.getPromotionApprovalOptionType().getCode().equals( "denied" ) )
      {
        if ( approvalOption.getPromotionApprovalOptionReasons() == null || approvalOption.getPromotionApprovalOptionReasons().size() < 1 )
        {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Validates Budgets that have been selected for quizzes with single promotion budgets
   * 
   * @param promo
   * @return boolean
   */
  private boolean isValidNonMultiBudget( Promotion promo )
  {
    if ( !promo.isBudgetUsed() && !promo.isCashBudgetUsed() )
    {
      return true;
    }
    BudgetMaster bm = promo.getBudgetMaster();
    BudgetMaster cbm = promo.getCashBudgetMaster();

    List budgetMasterList = getBudgetMasterService().getAllActiveWithPromotions();
    for ( Iterator iter = budgetMasterList.iterator(); iter.hasNext(); )
    {
      BudgetMaster budgetMaster = (BudgetMaster)iter.next();
      if ( bm != null && bm.getId().equals( budgetMaster.getId() ) )
      {
        if ( !budgetMaster.isMultiPromotion() && budgetMaster.getPromotions() != null && !budgetMaster.getPromotions().isEmpty() && budgetMaster.getPromotions().size() > 1 )
        {
          // This budget has multiple promotions but is not a multi promotion budget, error out.
          if ( ! ( budgetMaster.isCentralBudget() && promo.isChild() ) )
          {
            return false;
          }
        }
      }
      if ( cbm != null && cbm.getId().equals( budgetMaster.getId() ) )
      {
        if ( !budgetMaster.isMultiPromotion() && budgetMaster.getCashPromotions() != null && !budgetMaster.getCashPromotions().isEmpty() && budgetMaster.getCashPromotions().size() > 1 )
        {
          // This budget has multiple promotions but is not a multi promotion budget, error out.
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Validates eCards have been selected for RecognitionPromotions
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidECards( RecognitionPromotion promotion )
  {

    // Only check if eCards are active
    if ( promotion.isCardActive() )
    {

      // If there are no eCards selected, the check certificates
      Set cards = promotion.getPromotionECard();
      // BugFix 19033
      Set certificates = promotion.getPromotionCertificates();
      if ( cards != null && cards.size() > 0 || certificates != null && certificates.size() > 0 )
      {
        return true;
      }
      return false;
    }
    return true;
  }

  /**
   * Validates that award information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidSsiAward( SSIPromotion ssiPromotion )
  {
    boolean valid = true;
    if ( !ssiPromotion.getAllowAwardPoints() && !ssiPromotion.getAllowAwardOther() )
    {
      valid = false;
    }
    return valid;
  }

  /**
   * Validates that activity submission information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidSsiActivitySubmission( SSIPromotion ssiPromotion )
  {
    boolean valid = true;
    if ( ssiPromotion.getAllowActivityUpload() == null )
    {
      valid = false;
    }
    return valid;
  }

  /**
   * Validates that activity submission information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidSsiApprovals( SSIPromotion ssiPromotion )
  {
    boolean valid = true;
    if ( ssiPromotion.getRequireContestApproval() && ssiPromotion.getContestApprovalLevels() == 0 )
    {
      valid = false;
    }
    return valid;
  }

  /**
   * Validates that notification information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidNotification( Promotion promotion )
  {
    // Validate that we have some notification records
    boolean valid = promotion.getPromotionNotifications() != null && promotion.getPromotionNotifications().size() > 0;

    List notifications = promotion.getPromotionNotifications();

    for ( int i = 0; i < notifications.size(); i++ )
    {
      if ( notifications.get( i ) instanceof PromotionNotificationType )
      {
        PromotionNotificationType promoNotificationType = (PromotionNotificationType)notifications.get( i );
        String descriminator = promoNotificationType.getDescriminator();
        // validation that the end it there if this non redemption requires one
        if ( descriminator != null && descriminator.equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_PROMO_END ) )
        {
          if ( promotion.getSubmissionEndDate() == null )
          {
            valid = false;
          }
        }
      }
    }
    return valid;
  }

  private boolean isValidNotificationForCopyPromo( Promotion promotion )
  {
    if ( promotion.isRecognitionPromotion() )
    {
      Boolean errorExists = false;
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
      List<PromotionNotification> promoNotificationList = recPromo.getPromotionNotifications();
      for ( PromotionNotification promoNotification : promoNotificationList )
      {
        if ( recPromo.isIncludePurl() && !recPromo.isIncludeCelebrations() && promoNotification.isPromotionNotificationType() )
        {
          PromotionNotificationType promoNotifications = (PromotionNotificationType)promoNotification;
          if ( promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE ) )
          {
            errorExists = true;
          }
        }
        else if ( recPromo.isIncludeCelebrations() && !recPromo.isIncludePurl() && promoNotification.isPromotionNotificationType() )
        {
          PromotionNotificationType promoNotifications = (PromotionNotificationType)promoNotification;
          if ( promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED ) )

          {
            errorExists = true;
          }
        }
        else if ( recPromo.isIncludeCelebrations() && recPromo.isIncludePurl() && promoNotification.isPromotionNotificationType() )
        {
          PromotionNotificationType promoNotifications = (PromotionNotificationType)promoNotification;
          if ( promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE )
              || promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) )
          {
            errorExists = true;
          }
        }
        else if ( !recPromo.isIncludeCelebrations() && !recPromo.isIncludePurl() && promoNotification.isPromotionNotificationType() )
        {
          PromotionNotificationType promoNotifications = (PromotionNotificationType)promoNotification;
          if ( promoNotifications.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.RECOGNITION_RECEIVED ) )
          {
            errorExists = true;
          }
        }
      }
      return errorExists;
    }
    return true;
  }

  private boolean isValidWizardOrder( Promotion promotion )
  {
    boolean valid = promotion.getPromotionWizardOrder() != null && promotion.getPromotionWizardOrder().size() > 0;

    if ( !valid )
    {
      return valid;
    }

    int wizardsSize = 3; // since team or individual, nominee and why are always enabled
    if ( ( (NominationPromotion)promotion ).isBehaviorActive() )
    {
      wizardsSize++;
    }
    if ( ( (NominationPromotion)promotion ).isCardActive() || ( (NominationPromotion)promotion ).isCertificateActive() && ! ( (NominationPromotion)promotion ).isOneCertPerPromotion() )
    {
      wizardsSize++;
    }

    Set<PromotionWizard> wizards = promotion.getPromotionWizardOrder();

    Set<String> wizardOrder = new HashSet<String>();
    List<String> duplicates = new ArrayList<String>();
    List<Integer> order = new ArrayList<Integer>();

    for ( PromotionWizard promoWizard : wizards )
    {
      String wizOrder = promoWizard.getWizardOrder();
      if ( wizOrder != null )
      {
        order.add( Integer.parseInt( wizOrder ) );
      }

      if ( !wizardOrder.add( wizOrder ) )
      {
        duplicates.add( wizOrder );
      }
    }

    Integer maxOrder = Collections.max( order );

    if ( maxOrder != wizardsSize )
    {
      valid = false;
    }

    if ( duplicates.size() > 0 )
    {
      valid = false;
    }

    return valid;
  }

  public boolean isAwardsPayoutTypeValid( Promotion promotion )
  {
    final String EACH_LEVEL = "eachLevel";
    List<NominationPromotionLevelBean> nominationPayoutEachLevelList = new ArrayList<NominationPromotionLevelBean>();
    NominationPromotion nominationPromotion = (NominationPromotion)promotion;

    if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getNominationLevels() != null && nominationPromotion.getNominationLevels().size() > 0 )
    {
      List<NominationPromotionLevel> nomLevels = new ArrayList<NominationPromotionLevel>( nominationPromotion.getNominationLevels() );
      int levelIndex = 1;
      for ( NominationPromotionLevel nominationPromotionLevel : nomLevels )
      {
        NominationPromotionLevelBean nominationPromotionLevelBean = new NominationPromotionLevelBean();

        if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getPayoutLevel().equals( EACH_LEVEL ) )
        {
          nominationPromotionLevelBean.setLevelId( nominationPromotionLevel.getId() );
          nominationPromotionLevelBean.setLevelLabel( nominationPromotionLevel.getLevelLabel() );
          nominationPromotionLevelBean.setAwardsType( nominationPromotionLevel.getAwardPayoutType().getCode() );
          nominationPayoutEachLevelList.add( nominationPromotionLevelBean );
        }
        levelIndex++;
      }
    }

    boolean otherLevelsAward = false;
    int customApproverAwardLevel = 0;
    if ( !nominationPromotion.getCustomApproverOptions().isEmpty() )
    {
      Iterator<ApproverOption> iterator = nominationPromotion.getCustomApproverOptions().iterator();
      if ( iterator.hasNext() )
      {
        iterator.next();
      }

      while ( iterator.hasNext() )
      {
        ApproverOption approverOption = iterator.next();
        if ( approverOption.getApproverType() != null )
        {
          if ( approverOption.getApproverType().getCode().equals( CustomApproverType.AWARD ) )
          {
            otherLevelsAward = true;
            customApproverAwardLevel = approverOption.getApprovalLevel().intValue();
          }
        }
      }
    }

    String currentLevelAwardType = null;
    String previousLevelAwardType = null;
    int levelIndex = 0;

    for ( NominationPromotionLevelBean nomPromoLevelBean : nominationPayoutEachLevelList )
    {
      levelIndex++;

      if ( levelIndex > 1 && otherLevelsAward && customApproverAwardLevel == levelIndex )
      {
        currentLevelAwardType = nomPromoLevelBean.getAwardsType();
        if ( previousLevelAwardType != null && currentLevelAwardType != null )
        {
          if ( !previousLevelAwardType.equals( currentLevelAwardType ) )
          {
            return false;
          }
        }
      }
      previousLevelAwardType = nomPromoLevelBean.getAwardsType();
    }
    return true;
  }

  /**
   * Validates all rules text information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  private boolean isValidRulesText( Promotion promotion )
  {
    // NOTE: If there are no rules, then there is nothing to validate. However, if there are Web
    // Rules,
    // then they could only have been set via the Web Rules page. Any validation would have been
    // handled by the Web Rules page and thus, the same validation is not repeated here.

    return true;
  }

  /**
   * Validate the rules surrounding a nomination promotion with award as custom approver level 1. 
   * The rules change what can be set for awards, but need to validate it here rather than on the approvals screen, 
   * otherwise the admin cannot change approval options after first setting them
   */
  private void validateFirstLevelAward( NominationPromotion nominationPromotion, ActionMessages errors )
  {
    boolean firstLevelAward = nominationPromotion.getCustomApproverOptionsByLevel( 1L ).stream().anyMatch( ( option ) -> CustomApproverType.AWARD.equals( option.getApproverType().getCode() ) );

    // First level award and payout each level
    if ( firstLevelAward && PromotionAwardsForm.EACH_LEVEL.equals( nominationPromotion.getPayoutLevel() ) )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.AWARD_PAYOUT_EACH_LEVEL" ) );
    }
    // First level award, payout final level, not recommended award
    if ( firstLevelAward && PromotionAwardsForm.FINAL_LEVEL.equals( nominationPromotion.getPayoutLevel() ) && !nominationPromotion.isNominatorRecommendedAward() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.AWARD_FINAL_NOT_RECOMMENDED" ) );
    }
    // First level award, payout final level, award type other
    if ( firstLevelAward && PromotionAwardsForm.FINAL_LEVEL.equals( nominationPromotion.getPayoutLevel() )
        && nominationPromotion.getNominationLevels().stream().anyMatch( ( level ) -> PromotionAwardsType.OTHER.equals( level.getAwardPayoutType().getCode() ) ) )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.AWARD_FINAL_OTHER" ) );
    }
    // First level award, payout final level, award type cash/points, fixed payout
    if ( firstLevelAward && PromotionAwardsForm.FINAL_LEVEL.equals( nominationPromotion.getPayoutLevel() )
        && nominationPromotion.getNominationLevels().stream()
            .filter( ( level ) -> PromotionAwardsType.CASH.equals( level.getAwardPayoutType().getCode() ) || PromotionAwardsType.POINTS.equals( level.getAwardPayoutType().getCode() ) )
            .anyMatch( ( level ) -> level.isNominationAwardAmountTypeFixed() ) )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.AWARD_FINAL_FIXED_PAYOUT" ) );
    }
    // Not first level award, payout final level, recommended award
    if ( nominationPromotion.getApproverType().getCode().equals( ApproverType.CUSTOM_APPROVERS ) && !firstLevelAward && PromotionAwardsForm.FINAL_LEVEL.equals( nominationPromotion.getPayoutLevel() )
        && nominationPromotion.isNominatorRecommendedAward() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.NOT_AWARD_FINAL_RECOMMENDED" ) );
    }
  }

  /**
   * Get the promotionService from the beanFactory.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ssiPromotionService from the beanFactory.
   * 
   * @return SSIPromotionService
   */
  private SSIPromotionService getSsiPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  /**
   * Get the productService from the beanFactory.
   * 
   * @return ProductService
   */
  private ProductService getProductService()
  {
    return (ProductService)getService( ProductService.BEAN_NAME );
  }

  /**
   * Get a promotionNotificationService from the beanFactory.
   * 
   * @return PromotionNotificationService
   */
  private PromotionNotificationService getPromotionNotificationService()
  {
    return (PromotionNotificationService)getService( PromotionNotificationService.BEAN_NAME );
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
   * @return ProcessService
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  /**
   * @return PromoMerchCountryService
   */
  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  private Long getPromotionId( HttpServletRequest request )
  {
    Long promotionId;
    if ( RequestUtils.containsAttribute( request, "promotionId" ) )
    {
      promotionId = RequestUtils.getRequiredAttributeLong( request, "promotionId" );
    }
    else
    {
      try
      {
        String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        try
        {
          String promotionIdString = (String)clientStateMap.get( "promotionId" );
          promotionId = new Long( promotionIdString );
        }
        catch( ClassCastException cce )
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }

      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    return promotionId;
  }

  private boolean isValidProgramLevels( Promotion promotion ) throws ServiceErrorException
  {
    return getPromoMerchCountryService().validateAndDeleteInvalidProgramLevels( promotion, true );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Get the throwdownService from the beanFactory.
   * 
   * @return ThrowdownService
   */
  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }
}
