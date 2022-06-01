/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionNotificationForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionNotificationFrequencyType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.SAO;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;

/**
 * PromotionNotificationForm.
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
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionNotificationForm extends BaseActionForm
{
  private Long promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private String notificationMessage;
  private String method;
  private boolean hasParent = false;
  private String awardType;
  private String validationType;
  private boolean budgetSweepEnabled;
  private boolean budgetEnabled;
  private boolean purlEnabled;
  private Date endSubmissionDate = null;
  private List claimFormStepList;
  private List notificationList;
  private String[] promotionNotificationSurvey;
  private boolean publicRecPointsEnabled;
  private boolean celebrationsEnabled;
  private boolean allowActivityUpload;
  private boolean timePeriodActive;

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // PromotionNotificationFormBeans. If this is not done, the form wont initialize
    // properly.
    claimFormStepList = getEmptyValueListWithSublist( request, RequestUtils.getOptionalParamInt( request, "claimFormStepListCount" ) );
    notificationList = getEmptyValueList( request, RequestUtils.getOptionalParamInt( request, "notificationListCount" ) );
    promotionNotificationSurvey = request.getParameterValues( "promotionNotificationSurvey" );
  }

  /**
   * resets the notificationList with empty PromotionNotificationForm beans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( HttpServletRequest request, int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionNotificationFormBean
      PromotionNotificationFormBean promoNotificationBean = new PromotionNotificationFormBean();
      valueList.add( promoNotificationBean );
    }

    return valueList;
  }

  /**
   * resets the claimFormStepList with empty PromotionClaimFormStepBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueListWithSublist( HttpServletRequest request, int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionClaimFormStepBean
      PromotionClaimFormStepBean promoClaimFormStepBean = new PromotionClaimFormStepBean();
      List elementValueList = new ArrayList();
      int claimFormStepNotificationListSize = RequestUtils.getOptionalParamInt( request, "claimFormStepList[" + i + "].claimFormNotificationListCount" );
      for ( int j = 0; j < claimFormStepNotificationListSize; j++ )
      {
        // create and add empty PromotionNotificationFormBeans
        elementValueList.add( new PromotionNotificationFormBean() );
      }
      promoClaimFormStepBean.setClaimFormNotificationList( elementValueList );
      valueList.add( promoClaimFormStepBean );
    }

    return valueList;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    boolean isChildPromotion = false;
    if ( promotionTypeCode.equals( PromotionType.PRODUCT_CLAIM ) && hasParent )
    {
      isChildPromotion = true;
    }

    if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) || promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
    {
      Set<Long> formSurvey = new HashSet<Long>();
      String[] notificationSurvey = this.getPromotionNotificationSurvey();
      if ( notificationSurvey != null && notificationSurvey.length > 0 )
      {
        for ( int i = 0; i < notificationSurvey.length; i++ )
        {
          formSurvey.add( new Long( notificationSurvey[i] ) );
        }

        if ( formSurvey.contains( new Long( -1 ) ) )
        {
          if ( formSurvey.size() > 1 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.UNSELECT_NO_SURVEY" ) );
          }
          else
          {
            List surveysTaken = getSurveyService().getSurveysTakenByPromotionId( promotionId, "" );
            if ( surveysTaken != null && !surveysTaken.isEmpty() )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.SURVEY_TAKEN" ) );
            }
          }
        }
        else
        {
          List<PromotionGoalQuestSurvey> promotionSurveyNotifications = getSurveyService().getPromotionGoalQuestSurveysByPromotionId( promotionId );
          if ( promotionSurveyNotifications != null && !promotionSurveyNotifications.isEmpty() )
          {
            for ( int i = 0; i < promotionSurveyNotifications.size(); i++ )
            {
              PromotionGoalQuestSurvey gqSurvey = (PromotionGoalQuestSurvey)promotionSurveyNotifications.get( i );
              Long surveyId = gqSurvey.getSurvey().getId();
              if ( !formSurvey.contains( surveyId ) )
              {
                List surveysTaken = getSurveyService().getSurveysTakenByPromotionId( promotionId, surveyId.toString() );
                if ( surveysTaken != null && !surveysTaken.isEmpty() )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.CANNOT_REMOVE_SURVEY", gqSurvey.getSurvey().getName() ) );
                }
              }
            }
          }
        }
      }
      else
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.SURVEY_MISSING_REQUIRED" ) );
      }
    }

    if ( getNotificationListCount() > 0 )
    {
      Iterator it = getNotificationList().iterator();
      while ( it.hasNext() )
      {
        PromotionNotificationFormBean promoNotification = (PromotionNotificationFormBean)it.next();

        // validate for master all and child non-inactivity notifications (see bug 11718)
        boolean skipValidation = false;
        if ( isChildPromotion )
        {
          skipValidation = true;
        }

        if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
        {
          if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.APPROVER_REMINDER_APPROVAL_END_DATE )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.APPROVER_REQUEST_MORE_INFORMATION )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.NOMINATOR_REQUEST_MORE_INFORMATION )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TO_NOMINATOR_WHEN_WINNER )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TO_NOMINEE_WHEN_WINNER )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TO_NOMINATOR_WHEN_NON_WINNER )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_WINNER )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TO_APPROVER_WHEN_CLAIM_SUBMITTED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TO_NOMINEE_WHEN_CLAIM_SUBMITTED ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, promoNotification.getNotificationTypeName() ) );
            }
            if ( promoNotification.getNotificationMessageId() != -1 && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.APPROVER_REMINDER_APPROVAL_END_DATE ) )
            {
              if ( promoNotification.getNumberOfDays() == null || promoNotification.getNumberOfDays().length() == 0 )
              {
                actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
              else
              {
                String numeric = promoNotification.getNumberOfDays();
                validateNumeric( numeric, promoNotification.getNotificationTypeName(), actionErrors );
              }
            }
            skipValidation = true;
          }
          if ( !publicRecPointsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PUBLIC_RECOG_ADD_POINTS_FOR_NOMINATION ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }
            skipValidation = true;
          }
          if ( !timePeriodActive && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.APPROVER_REMINDER_TIME_PERIOD_EXPIRED ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }
            skipValidation = true;
          }
        }

        if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
        {
          if ( purlEnabled
              && ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION )
                  || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
                  || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION )
                  || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
                  || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE ) )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.RECOGNITION_RECEIVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, promoNotification.getNotificationTypeName() ) );
            }
            if ( promoNotification.getNotificationMessageId() != -1 && ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
                || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE ) ) )
            {
              if ( promoNotification.getNumberOfDays() == null || promoNotification.getNumberOfDays().length() == 0 )
              {
                actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
              else
              {
                String numeric = promoNotification.getNumberOfDays();
                validateNumeric( numeric, promoNotification.getNotificationTypeName(), actionErrors );

              }
            }
            skipValidation = true;
          }

          if ( publicRecPointsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }
            skipValidation = true;
          }

          if ( celebrationsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }
            skipValidation = true;
          }

          if ( celebrationsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }

            if ( promoNotification.getNotificationMessageId() != -1 && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE ) )

            {
              if ( promoNotification.getNumberOfDays() == null || promoNotification.getNumberOfDays().length() == 0 )
              {
                actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
              else
              {
                String numeric = promoNotification.getNumberOfDays();
                validateNumeric( numeric, promoNotification.getNotificationTypeName(), actionErrors );
              }
            }
            skipValidation = true;
          }

          if ( celebrationsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }
            skipValidation = true;
          }

          if ( celebrationsEnabled && purlEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }
            skipValidation = true;
          }
        }

        // SK - Added Survey notification type for GoalQuest
        if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) )
        {
          if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.GOAL_SELECTED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PROGRESS_UPDATED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.GOAL_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.GOAL_NOT_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PARTNER_SELECTED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
            }
            skipValidation = true;
          }
        }
        if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.BUDGET_SWEEP ) && !budgetSweepEnabled )
        {
          skipValidation = true;
        }
        if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.BUDGET_END ) && !budgetEnabled )
        {
          skipValidation = true;
        }
        if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.BUDGET_REMINDER ) && !budgetEnabled )
        {
          skipValidation = true;
        }
        if ( !purlEnabled && ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION )
            || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
            || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION )
            || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
            || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE )
            || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) ) )
        {
          skipValidation = true;
        }

        if ( !publicRecPointsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS ) )
        {
          skipValidation = true;
        }

        if ( !celebrationsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION ) )
        {
          skipValidation = true;
        }

        if ( !celebrationsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE ) )
        {
          skipValidation = true;
        }

        if ( !celebrationsEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED ) )
        {
          skipValidation = true;
        }

        if ( !celebrationsEnabled && !purlEnabled && promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) )
        {
          skipValidation = true;
        }

        if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
        {

          if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CHALLENGEPOINT_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CHALLENGEPOINT_PROGRESS_UPDATED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CHALLENGEPOINT_SELECTED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PROGRAM_END )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PROGRAM_LAUNCH )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_NOT_ACHIEVED )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CP_PARTNER_PROGRESS_UPDATED ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, promoNotification.getNotificationTypeName() ) );
            }
            if ( promoNotification.getNotificationMessageId() != -1
                && ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED )
                    || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PROGRAM_END )
                    || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PROGRAM_LAUNCH ) )
                && !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.BUDGET_REMINDER ) )

            {
              if ( promoNotification.getNumberOfDays() == null || promoNotification.getNumberOfDays().length() == 0 )
              {
                actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
              else
              {
                String numeric = promoNotification.getNumberOfDays();
                validateNumeric( numeric, promoNotification.getNotificationTypeName(), actionErrors );

              }
            }
            skipValidation = true;
          }
        }

        if ( promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
        {
          if ( promoNotification.getNotificationMessageId() == 0 )
          {
            actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, promoNotification.getNotificationTypeName() ) );
          }
          else if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP ) )
          {
            if ( promoNotification.getNotificationMessageId() != -1 )
            {
              if ( StringUtil.isEmpty( promoNotification.getDaysBeforeContestEnd() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
              else
              {
                actionErrors = validateNumeric( promoNotification.getDaysBeforeContestEnd(), promoNotification.getNotificationTypeName(), actionErrors );
              }
            }
          }
          else if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER ) )
          {
            if ( promoNotification.getNotificationMessageId() != -1 )
            {
              if ( StringUtil.isEmpty( promoNotification.getDaysAfterContestCreated() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
              else
              {
                actionErrors = validateNumeric( promoNotification.getDaysAfterContestCreated(), promoNotification.getNotificationTypeName(), actionErrors );
              }
              if ( StringUtil.isEmpty( promoNotification.getPromotionNotificationFrequencyType() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_FREQUENCY", promoNotification.getNotificationTypeName() ) );
              }
              else if ( PromotionNotificationFrequencyType.lookup( promoNotification.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.WEEKLY ) )
              {
                if ( StringUtil.isEmpty( promoNotification.getDayOfWeekType() ) )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_DAYOFWEEK", promoNotification.getDayOfWeekTypeName() ) );
                }
              }
              else if ( PromotionNotificationFrequencyType.lookup( promoNotification.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.MONTHLY ) )
              {
                if ( promoNotification.getDayOfMonth() == 0 )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_DAYOFMONTH" ) );
                }
              }
            }
          }
          else if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR ) )
          {
            if ( promoNotification.getNotificationMessageId() != -1 )
            {
              if ( StringUtil.isEmpty( promoNotification.getDaysAfterContestEnded() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
              else
              {
                actionErrors = validateNumeric( promoNotification.getDaysAfterContestEnded(), promoNotification.getNotificationTypeName(), actionErrors );
              }
              if ( StringUtil.isEmpty( promoNotification.getPromotionNotificationFrequencyType() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_FREQUENCY", promoNotification.getNotificationTypeName() ) );
              }
              else if ( PromotionNotificationFrequencyType.lookup( promoNotification.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.WEEKLY ) )
              {
                if ( StringUtil.isEmpty( promoNotification.getDayOfWeekType() ) )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_DAYOFWEEK", promoNotification.getDayOfWeekTypeName() ) );
                }
              }
              else if ( PromotionNotificationFrequencyType.lookup( promoNotification.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.MONTHLY ) )
              {
                if ( promoNotification.getDayOfMonth() == 0 )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_DAYOFMONTH" ) );
                }
              }
            }
          }
        }

        if ( promotionTypeCode.equals( PromotionType.THROWDOWN ) )
        {
          if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TD_MATCH_OUTCOME )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TD_PROGRESS_UPDATED ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, promoNotification.getNotificationTypeName() ) );
            }

            skipValidation = true;
          }
        }

        if ( promotionTypeCode.equals( PromotionType.ENGAGEMENT ) )
        {
          if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.KPM_MANAGERS_UPDATE )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.KPM_PARTICIPANT_UPDATE ) )
          {
            if ( promoNotification.getNotificationMessageId() == 0 )
            {
              actionErrors.add( promoNotification.getNotificationTypeName(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, promoNotification.getNotificationTypeName() ) );
            }

            skipValidation = true;
          }
        }

        if ( !skipValidation )
        {
          if ( promoNotification.getNotificationMessageId() != 0 && ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER )
              || promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER ) ) )
          {
            if ( StringUtils.isEmpty( promoNotification.getDescriminator() ) )
            {
              // default to days after issuance
              promoNotification.setDescriminator( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_ISSUANCE );
            }

            // assign defaults
            if ( !StringUtils.isEmpty( promoNotification.getDescriminator() ) )
            {
              String numeric = null;
              if ( promoNotification.getDescriminator().equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_ISSUANCE ) )
              {
                numeric = promoNotification.getEveryDaysAfterIssuance();
              }
              else if ( promoNotification.getDescriminator().equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_PROMO_END ) )
              {
                numeric = promoNotification.getNumberOfDaysAfterPromoEnd();
              }
              if ( StringUtils.isNumeric( numeric ) )
              {
                promoNotification.setNumberOfDays( numeric );
              }
              if ( promoNotification.getNotificationMessageId() > 0 )
              {
                actionErrors = validateNumeric( numeric, promoNotification.getNotificationTypeName(), actionErrors );
              }
            }
            continue;
          }

          if ( promoNotification.getNotificationMessageId() == 0
              || promoNotification.getNotificationMessageId() > 0 && ( !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER )
                  || !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER )
                  || !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER ) ) )
          {
            if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.APPROVER_REMINDER ) )
            {
              if ( promoNotification.getPromotionNotificationFrequencyType() == null || promoNotification.getPromotionNotificationFrequencyType().equals( "" ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_FREQUENCY", promoNotification.getNotificationTypeName() ) );
              }
            }
            if ( promoNotification.getNumberOfDays() == null || promoNotification.getNumberOfDays().equals( "" ) )
            {
              if ( ! ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_RECOGNITION ) && purlEnabled )
                  && !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.BUDGET_REMINDER )
                  && !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.KPM_MANAGERS_UPDATE )
                  && !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.KPM_PARTICIPANT_UPDATE ) && !promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES )
                  && !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.NOMINATION_APPROVER_NOTIFICATION )
                  && !promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_CLAIM_SUBMITTED ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", promoNotification.getNotificationTypeName() ) );
              }
            }
            else
            {
              actionErrors = validateNumeric( promoNotification.getNumberOfDays(), promoNotification.getNotificationTypeName(), actionErrors );
            }
          }
        }

        if ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.TD_NEXT_ROUND ) )
        {
          ThrowdownPromotion tdPromotion = (ThrowdownPromotion)getPromotionService().getPromotionById( promotionId );
          int matchScheduledPriorDays = 0;
          if ( tdPromotion != null )
          {
            matchScheduledPriorDays = tdPromotion.getDaysPriorToRoundStartSchedule();
          }
          if ( promoNotification.getNumberOfDays() != null && !StringUtils.isEmpty( promoNotification.getNumberOfDays() ) )
          {
            String numeric = promoNotification.getNumberOfDays();
            validatePriorDays( numeric, matchScheduledPriorDays, actionErrors );
          }
        }
        if ( promoNotification.getPromotionNotificationFrequencyType() != null && !promoNotification.getPromotionNotificationFrequencyType().equals( "" ) )
        {
          if ( ! ( promoNotification.getNotificationType().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_RECOGNITION ) && purlEnabled ) )
          {
            // validate Day of Week is selected if Frequency is Weekly
            if ( PromotionNotificationFrequencyType.lookup( promoNotification.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.WEEKLY ) )
            {
              if ( promoNotification.getDayOfWeekType() == null || promoNotification.getDayOfWeekType().equals( "" ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED_DAYOFWEEK", promoNotification.getDayOfWeekTypeName() ) );
              }
            }
          }

        }

      } // end while
    } // end if

    if ( claimFormStepList != null && !claimFormStepList.isEmpty() && !isChildPromotion )
    {
      for ( Iterator iter = claimFormStepList.iterator(); iter.hasNext(); )
      {
        PromotionClaimFormStepBean stepBean = (PromotionClaimFormStepBean)iter.next();
        if ( stepBean.getClaimFormNotificationListCount() > 0 )
        {
          Iterator it = stepBean.getClaimFormNotificationList().iterator();
          while ( it.hasNext() )
          {
            PromotionNotificationFormBean bean = (PromotionNotificationFormBean)it.next();
            if ( bean.getNotificationMessageId() == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.MISSING_REQUIRED", bean.getNotificationTypeName() ) );
            }
          }
        }
      }
    }
    return actionErrors;
  }

  /**
   * Load claim form step email notifications for promotions that use claim form.
   * 
   * @param promotion
   * @param claimFormTypeNotifications
   */
  private void loadClaimFormStepNotifications( Promotion promotion, List claimFormTypeNotifications )
  {
    if ( !promotionTypeCode.equals( PromotionType.QUIZ ) && !promotionTypeCode.equals( PromotionType.DIY_QUIZ ) && !promotionTypeCode.equals( PromotionType.SURVEY )
        && !promotionTypeCode.equals( PromotionType.GOALQUEST ) && !promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) && !promotionTypeCode.equals( PromotionType.THROWDOWN )
        && !promotionTypeCode.equals( PromotionType.ENGAGEMENT ) && !promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      if ( promotion.getClaimForm() != null && promotion.getClaimForm().getClaimFormSteps() != null && promotion.getClaimForm().getClaimFormSteps().size() > 0 )
      {
        // If there are claimFormSteps, iterate over them to see if they have emailNotifications
        Iterator claimFormStepIter = promotion.getClaimForm().getClaimFormSteps().iterator();
        while ( claimFormStepIter.hasNext() )
        {
          ClaimFormStep claimFormStep = (ClaimFormStep)claimFormStepIter.next();
          if ( claimFormStep.getClaimFormStepEmailNotifications() != null && claimFormStep.getClaimFormStepEmailNotifications().size() > 0 )
          {
            // If there are ClaimFOrmStepEmailNotifications, create a new PromotionClaimFormStepBean
            // to hold the claimFormStep information
            List claimFormNotificationList = new ArrayList();
            PromotionClaimFormStepBean promoClaimFormStepBean = new PromotionClaimFormStepBean();
            promoClaimFormStepBean.setClaimFormStepId( claimFormStep.getId() );
            promoClaimFormStepBean.setCmAssetCode( claimFormStep.getClaimForm().getCmAssetCode() );
            promoClaimFormStepBean.setCmName( claimFormStep.getCmKeyForName() );
            Iterator claimFormStepEmailNotificationIter = claimFormStep.getClaimFormStepEmailNotifications().iterator();
            while ( claimFormStepEmailNotificationIter.hasNext() )
            {
              // Iterate over the emailNotification and create a new PromotionNotificationFormBean
              // for each.
              ClaimFormStepEmailNotification claimFormStepEmailNotification = (ClaimFormStepEmailNotification)claimFormStepEmailNotificationIter.next();
              PromotionNotificationFormBean promoNotificationBean = new PromotionNotificationFormBean();
              promoNotificationBean.setPromotionNotificationType( "CLAIM_FORM_NOTIFICATION_TYPE" );
              promoNotificationBean.setClaimFormStepEmailId( claimFormStepEmailNotification.getId() );
              promoNotificationBean.setNotificationType( claimFormStepEmailNotification.getClaimFormStepEmailNotificationType().getCode() );
              promoNotificationBean.setNotificationTypeName( claimFormStepEmailNotification.getClaimFormStepEmailNotificationType().getName() );

              if ( claimFormTypeNotifications != null && claimFormTypeNotifications.size() > 0 )
              {
                // If there are existing notifications for this promotion, set the id, version
                // notificationMessage, and createAuditInfo
                Iterator claimFormTypeNotificationIter = claimFormTypeNotifications.iterator();
                while ( claimFormTypeNotificationIter.hasNext() )
                {
                  ClaimFormNotificationType claimFormNotificationType = (ClaimFormNotificationType)claimFormTypeNotificationIter.next();
                  if ( claimFormNotificationType.getClaimFormStepEmailNotification().getId().longValue() == claimFormStepEmailNotification.getId().longValue() )
                  {
                    promoNotificationBean.setPromotionNotificationId( claimFormNotificationType.getId() );
                    promoNotificationBean.setVersion( claimFormNotificationType.getVersion() );
                    promoNotificationBean.setNotificationMessageId( claimFormNotificationType.getNotificationMessageId() );
                    promoNotificationBean.setCreatedBy( claimFormNotificationType.getAuditCreateInfo().getCreatedBy().toString() );
                    promoNotificationBean.setDateCreated( claimFormNotificationType.getAuditCreateInfo().getDateCreated().getTime() );
                  }
                }
              }

              // Add the object to the claimFormNotificationList
              claimFormNotificationList.add( promoNotificationBean );
            }
            promoClaimFormStepBean.setClaimFormNotificationList( claimFormNotificationList );
            claimFormStepList.add( promoClaimFormStepBean );
          }
        }
      }
    }
  }

  public void loadPromotionSurveyNotifications( List<PromotionGoalQuestSurvey> promotionSurveyNotifications )
  {
    if ( promotionNotificationSurvey == null )
    {
      promotionNotificationSurvey = new String[promotionSurveyNotifications.size()];
    }

    int i = 0;
    for ( PromotionGoalQuestSurvey promotionGoalQuestSurvey : promotionSurveyNotifications )
    {
      promotionNotificationSurvey[i] = promotionGoalQuestSurvey.getSurvey().getId().toString();
      i++;
    }

  }

  /**
   * Load promotion level email notifications.
   * 
   * @param promotion
   * @param promotionTypeNotifications
   */
  private void loadPromotionNotifications( Promotion promotion, List promotionTypeNotifications )
  {
    boolean awardThemNowContestSelected = false;
    boolean doThisGetThatContestSelected = false;
    boolean objectivesContestSelected = false;
    boolean stackRankContestSelected = false;
    boolean stepItUpContestSelected = false;

    if ( promotion.isSSIPromotion() )
    {
      SSIPromotion ssiPromo = (SSIPromotion)promotion;
      awardThemNowContestSelected = ssiPromo.isAwardThemNowSelected();
      doThisGetThatContestSelected = ssiPromo.isDoThisGetThatSelected();
      objectivesContestSelected = ssiPromo.isObjectivesSelected();
      stackRankContestSelected = ssiPromo.isStackRankSelected();
      stepItUpContestSelected = ssiPromo.isStepItUpSelected();
    }

    Iterator availablePromotionNotificationTypes = PromotionEmailNotificationType.getList().iterator();

    // Iterate over the list of promotionEmailNotificationTypes and create a
    // PromotionNotificationFormBean for each of them
    while ( availablePromotionNotificationTypes.hasNext() )
    {
      PromotionEmailNotificationType promoEmailNotificationType = (PromotionEmailNotificationType)availablePromotionNotificationTypes.next();

      // skip budget-sweep notification if the promotion does not have sweep-enabled
      if ( !this.budgetSweepEnabled && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_SWEEP ) )
      {
        continue;
      }
      /*
       * The below if condition to exclude the message type for gamification badge received message.
       * This message should not use with any of the promotion modules
       */
      if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BADGE_RECEIVED ) )
      {
        continue;
      }

      // skip budget-end notification if the promotion does not have budget-enabled
      if ( !this.budgetEnabled && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_END ) )
      {
        continue;
      }

      // skip budget-reminder notification if the promotion does not have budget-enabled
      if ( !this.budgetEnabled && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_REMINDER ) )
      {
        continue;
      }

      // skip purl related notification if the promotion does not have purl-enabled
      if ( !this.purlEnabled && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) ) )
      {
        continue;
      }

      if ( !this.publicRecPointsEnabled && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS ) )
      {
        continue;
      }

      if ( !this.publicRecPointsEnabled && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PUBLIC_RECOG_ADD_POINTS_FOR_NOMINATION ) )
      {
        continue;
      }

      if ( !this.timePeriodActive && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER_TIME_PERIOD_EXPIRED ) )
      {
        continue;
      }

      // skip celebration emails if celebration is not turned on
      if ( !this.celebrationsEnabled && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) ) )
      {
        continue;
      }

      // skip budget-sweep notification if the promotion is not recognition type
      if ( !promotionTypeCode.equals( PromotionType.RECOGNITION ) && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_SWEEP ) )
      {
        continue;
      }
      if ( !promotionTypeCode.equals( PromotionType.RECOGNITION ) && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_RECOGNITION ) )
      {
        continue;
      }
      if ( !promotionTypeCode.equals( PromotionType.NOMINATION ) && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_NOMINATION ) )
      {
        continue;
      }

      if ( ( promotionTypeCode.equals( PromotionType.NOMINATION ) || promotionTypeCode.equals( PromotionType.RECOGNITION ) )
          && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY ) )
      {
        continue;
      }

      // skip budget-end notification if the promotion is not recognition type
      if ( !promotionTypeCode.equals( PromotionType.RECOGNITION ) && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_END ) )
      {
        continue;
      }

      // skip budget-end notification if the promotion is not recognition type
      if ( !promotionTypeCode.equals( PromotionType.RECOGNITION ) && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_REMINDER ) )
      {
        continue;
      }

      // skip throwdown related notification if the promotion is not throwdown type
      if ( !promotionTypeCode.equals( PromotionType.THROWDOWN ) && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TD_MATCH_OUTCOME )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TD_NEXT_ROUND )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TD_PROGRESS_UPDATED ) ) )
      {
        continue;
      }

      // skip purl related notification if the promotion is not recognition type
      if ( !promotionTypeCode.equals( PromotionType.RECOGNITION ) && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.RECOGNITION_RECEIVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) ) )
      {
        continue;
      }

      // skip nomination related notification if the promotion is not nomination type
      if ( !promotionTypeCode.equals( PromotionType.NOMINATION ) && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER_TIME_PERIOD_EXPIRED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER_APPROVAL_END_DATE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REQUEST_MORE_INFORMATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.NOMINATOR_REQUEST_MORE_INFORMATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TO_NOMINATOR_WHEN_WINNER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TO_NOMINEE_WHEN_WINNER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TO_NOMINATOR_WHEN_NON_WINNER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_WINNER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TO_APPROVER_WHEN_CLAIM_SUBMITTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TO_NOMINEE_WHEN_CLAIM_SUBMITTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_CLAIM_SUBMITTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.NOMINATION_APPROVER_NOTIFICATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PUBLIC_RECOG_ADD_POINTS_FOR_NOMINATION ) ) )
      {
        continue;
      }

      // skip non-redemption notifications if the promotion is not Award Level/Merchandise
      if ( !isAwardLevelPromotion() && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER ) )
      {
        continue;
      }
      // to fix bug 18834 skip non-redemption notifications if the promotion is GoalQuest
      if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) || promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER ) )
        {
          continue;
        }
      }

      // skip GoalQuest specific Notifications if not GQ
      if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_NOT_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_SELECTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_NOT_SELECTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRESS_UPDATED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_SELECTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER ) )
      {
        if ( !promotionTypeCode.equals( PromotionType.GOALQUEST ) )
        {
          continue;
        }
      }

      // skip ChallengePoint specific Notifications if not CP
      if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_PROGRESS_UPDATED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_SELECTED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER ) )
      {
        if ( !promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
        {
          continue;
        }
      }

      if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_NOT_ACHIEVED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_PROGRESS_UPDATED )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED ) )
      {
        if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
        {
          if ( promotion.getPartnerAudienceType() == null )
          {
            continue;
          }
        }
        else if ( !promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
        {
          continue;
        }
      }

      // skip Pax Inactivity Notification/Approver Reminder - not applicable for Survey or GoalQuest
      // or Throwdown
      // Promotion
      if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) || promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) || promotionTypeCode.equals( PromotionType.THROWDOWN ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY )
            || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_NOMINATION )
            || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_RECOGNITION )
            || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER ) )
        {
          continue;
        }
      }

      if ( promotionTypeCode.equals( PromotionType.SURVEY ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER ) )
        {
          continue;
        }
      }
      // skip Approver Reminder - not applicable for Quiz Promotion
      if ( promotionTypeCode.equals( PromotionType.QUIZ ) || promotionTypeCode.equals( PromotionType.DIY_QUIZ ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER ) )
        {
          continue;
        }
      }
      // skip Participant Inactivity Notification for diy quiz
      if ( promotionTypeCode.equals( PromotionType.DIY_QUIZ ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY ) )
        {
          continue;
        }
      }

      // Skip KPM Manager Update Notification and Participant update notification for
      // KPM(Engagement) Promotion
      if ( !promotionTypeCode.equals( PromotionType.ENGAGEMENT ) && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.KPM_MANAGERS_UPDATE )
          || !promotionTypeCode.equals( PromotionType.ENGAGEMENT ) && promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.KPM_PARTICIPANT_UPDATE ) )
      {
        continue;
      }

      // skip Inactivity, Program Launch, Program End, Approver Reminder Notification for
      // KPM(Engagement) Promotion, SSI Promotion
      if ( ( promotionTypeCode.equals( PromotionType.ENGAGEMENT ) || promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
          && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_END )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_LAUNCH )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER ) ) )
      {
        continue;
      }

      if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) && !isAwardLevelPromotion() )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER ) )
        {
          continue;
        }
      }

      if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) && !isCPAwardLevelPromotion( promotion ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER ) )
        {
          continue;
        }
      }

      // skip Approver Reminder - not applicable for any promotions whose approval type is automatic
      // approval (i.e.automaticImmediate or automaticDelayed)
      if ( promotion.getApprovalType() != null && promotion.getApprovalType().isAutomaticApproved() )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER ) )
        {
          continue;
        }
      }
      // skip Participant Inactivity Notification for diy quiz
      if ( promotionTypeCode.equals( PromotionType.DIY_QUIZ ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY ) )
        {
          continue;
        }
      }

      // skip SSI specific Notifications if not SSI
      if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_NOTIFY_APPROVER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER )

          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW )

          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT )

          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES )

          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK )

          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_CLAIM_APPROVAL_NOTIFY_APPROVER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER ) )
      {
        if ( !promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
        {
          continue;
        }
        else
        {
          if ( !awardThemNowContestSelected && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW ) ) )
          {
            continue;
          }
          if ( !doThisGetThatContestSelected && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT ) ) )
          {
            continue;
          }
          if ( !objectivesContestSelected && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES ) ) )
          {
            continue;
          }
          if ( !stackRankContestSelected && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK ) ) )
          {
            continue;
          }
          if ( !stepItUpContestSelected && ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP )
              || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP ) ) )
          {
            continue;
          }
        }
      }

      PromotionNotificationFormBean promoNotificationBean = new PromotionNotificationFormBean();

      // Set the basic info from the promotionEmailNotificationType
      promoNotificationBean.setPromotionNotificationType( "PROMOTION_NOTIFICATION_TYPE" );
      promoNotificationBean.setNotificationType( promoEmailNotificationType.getCode() );
      promoNotificationBean.setNotificationTypeName( promoEmailNotificationType.getName() );

      // GQ: Set sequence nbr so that the promotion notifications appear on the jsp in a specific
      // order
      if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER ) )
        {
          promoNotificationBean.setSequenceNbr( 12 );
        }
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 11 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 10 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED ) )
        {
          promoNotificationBean.setSequenceNbr( 9 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTNER_SELECTED ) )
        {
          promoNotificationBean.setSequenceNbr( 8 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_NOT_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 7 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 6 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_END ) )
        {
          promoNotificationBean.setSequenceNbr( 5 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRESS_UPDATED ) )
        {
          promoNotificationBean.setSequenceNbr( 4 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_NOT_SELECTED ) )
        {
          promoNotificationBean.setSequenceNbr( 3 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.GOAL_SELECTED ) )
        {
          promoNotificationBean.setSequenceNbr( 2 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_LAUNCH ) )
        {
          promoNotificationBean.setSequenceNbr( 1 );
        }
      }

      // SSI: Set sequence nbr so that the promotion notifications appear on the jsp in a specific
      // order
      setSequenceNumbersForSSINotifications( promoEmailNotificationType, promoNotificationBean );

      /*
       * setSequenceNumbersForSSINotifications( awardThemNowContestSelected,
       * doThisGetThatContestSelected, objectivesContestSelected, stackRankContestSelected,
       * stepItUpContestSelected, promoEmailNotificationType, promoNotificationBean );
       */

      // CP: Set sequence nbr so that the promotion notifications appear on the jsp in a specific
      // order
      if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 8 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 7 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_END ) )
        {
          promoNotificationBean.setSequenceNbr( 6 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED ) )
        {
          promoNotificationBean.setSequenceNbr( 5 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_PROGRESS_UPDATED ) )
        {
          promoNotificationBean.setSequenceNbr( 4 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED ) )
        {
          promoNotificationBean.setSequenceNbr( 3 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CHALLENGEPOINT_SELECTED ) )
        {
          promoNotificationBean.setSequenceNbr( 2 );
        }

        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_LAUNCH ) )
        {
          promoNotificationBean.setSequenceNbr( 1 );
        }

        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_NOT_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 12 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_ACHIEVED ) )
        {
          promoNotificationBean.setSequenceNbr( 11 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_PROGRESS_UPDATED ) )
        {
          promoNotificationBean.setSequenceNbr( 10 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED ) )
        {
          promoNotificationBean.setSequenceNbr( 9 );
        }
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER ) )
        {
          promoNotificationBean.setSequenceNbr( 14 );
        }
      }

      if ( promotionTypeCode.equals( PromotionType.THROWDOWN ) )
      {
        if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_LAUNCH ) )
        {
          promoNotificationBean.setSequenceNbr( 1 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TD_PROGRESS_UPDATED ) )
        {
          promoNotificationBean.setSequenceNbr( 2 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TD_NEXT_ROUND ) )
        {
          promoNotificationBean.setSequenceNbr( 3 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.TD_MATCH_OUTCOME ) )
        {
          promoNotificationBean.setSequenceNbr( 4 );
        }
        else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PROGRAM_END ) )
        {
          promoNotificationBean.setSequenceNbr( 5 );
        }
      }

      // code to set inactivity alerts type notifications
      if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.APPROVER_REMINDER ) || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_END )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_NOMINATION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_RECOGNITION )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE )
          || promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.BUDGET_REMINDER ) )
      {
        promoNotificationBean.setInactiveAlert( true );
      }

      if ( promotionTypeNotifications != null && promotionTypeNotifications.size() > 0 )
      {

        // If there are existing promotionTypeNotifications, set the id, version,
        // notificationMessage, and numberOfDays, and if any, frequency and dayOfWeek
        Iterator promoTypeNotificationIter = promotionTypeNotifications.iterator();
        while ( promoTypeNotificationIter.hasNext() )
        {
          PromotionNotificationType promoNotificationType = (PromotionNotificationType)promoTypeNotificationIter.next();
          if ( promoNotificationType.getPromotionEmailNotificationType().getCode().equals( promoEmailNotificationType.getCode() ) )
          {
            promoNotificationBean.setPromotionNotificationId( promoNotificationType.getId() );
            promoNotificationBean.setVersion( promoNotificationType.getVersion() );
            promoNotificationBean.setNotificationMessageId( promoNotificationType.getNotificationMessageId() );
            if ( promoNotificationType.getNumberOfDays() != null )
            {
              promoNotificationBean.setNumberOfDays( String.valueOf( promoNotificationType.getNumberOfDays() ) );
            }
            if ( promoNotificationType.getPromotionNotificationFrequencyType() != null )
            {
              promoNotificationBean.setPromotionNotificationFrequencyType( promoNotificationType.getPromotionNotificationFrequencyType().getCode() );
              promoNotificationBean.setPromotionNotificationFrequencyTypeName( promoNotificationType.getPromotionNotificationFrequencyType().getName() );
            }
            if ( promoNotificationType.getDayOfWeekType() != null )
            {
              promoNotificationBean.setDayOfWeekType( promoNotificationType.getDayOfWeekType().getCode() );
              promoNotificationBean.setDayOfWeekTypeName( promoNotificationType.getDayOfWeekType().getName() );
            }
            if ( promoNotificationType.getDayOfMonth() > 0 )
            {
              promoNotificationBean.setDayOfMonth( promoNotificationType.getDayOfMonth() );
            }
            promoNotificationBean.setCreatedBy( promoNotificationType.getAuditCreateInfo().getCreatedBy().toString() );
            promoNotificationBean.setDateCreated( promoNotificationType.getAuditCreateInfo().getDateCreated().getTime() );
            promoNotificationBean.setDescriminator( promoNotificationType.getDescriminator() );

            // assign the property thankq online gift code variables
            if ( !StringUtils.isEmpty( promoNotificationType.getDescriminator() ) && promoNotificationType.getDescriminator().equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_ISSUANCE )
                && promoNotificationType.getNumberOfDays() != null )
            {
              promoNotificationBean.setEveryDaysAfterIssuance( promoNotificationType.getNumberOfDays() + "" );
            }
            else if ( !StringUtils.isEmpty( promoNotificationType.getDescriminator() )
                && promoNotificationType.getDescriminator().equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_PROMO_END ) && promoNotificationType.getNumberOfDays() != null )
            {
              promoNotificationBean.setNumberOfDaysAfterPromoEnd( promoNotificationType.getNumberOfDays() + "" );
            }

            // ssi promotion changes
            if ( promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
                || promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP ) )
            {
              if ( promoNotificationType.getNumberOfDays() != null )
              {
                promoNotificationBean.setDaysBeforeContestEnd( String.valueOf( promoNotificationType.getNumberOfDays() ) );
              }
            }

            if ( promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR ) )
            {
              if ( promoNotificationType.getNumberOfDays() != null )
              {
                promoNotificationBean.setDaysAfterContestEnded( String.valueOf( promoNotificationType.getNumberOfDays() ) );
              }
              if ( promoNotificationType.getPromotionNotificationFrequencyType() != null )
              {
                promoNotificationBean.setPromotionNotificationFrequencyType( promoNotificationType.getPromotionNotificationFrequencyType().getCode() );
                promoNotificationBean.setPromotionNotificationFrequencyTypeName( promoNotificationType.getPromotionNotificationFrequencyType().getName() );
              }
            }

            if ( promoNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER ) )
            {
              if ( promoNotificationType.getNumberOfDays() != null )
              {
                promoNotificationBean.setDaysAfterContestCreated( String.valueOf( promoNotificationType.getNumberOfDays() ) );
              }
              if ( promoNotificationType.getPromotionNotificationFrequencyType() != null )
              {
                promoNotificationBean.setPromotionNotificationFrequencyType( promoNotificationType.getPromotionNotificationFrequencyType().getCode() );
                promoNotificationBean.setPromotionNotificationFrequencyTypeName( promoNotificationType.getPromotionNotificationFrequencyType().getName() );
              }
            }
          }
        }
      }

      // Add the promoTypeNotification to the list
      notificationList.add( promoNotificationBean );
    }
    // gift code/merch notifications should always appear last
    Collections.sort( notificationList, new RedemptionPromotionNotificationComparator() );
  }

  private void setSequenceNumbersForSSINotifications( PromotionEmailNotificationType promoEmailNotificationType, PromotionNotificationFormBean promoNotificationBean )
  {
    // Change the sequence numbers accordingly if the notification order changes

    if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR ) )
    {
      promoNotificationBean.setSequenceNbr( 1 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR ) )
    {
      promoNotificationBean.setSequenceNbr( 2 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR ) )
    {
      promoNotificationBean.setSequenceNbr( 3 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR ) )
    {
      promoNotificationBean.setSequenceNbr( 4 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_NOTIFY_APPROVER ) )
    {
      promoNotificationBean.setSequenceNbr( 5 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER ) )
    {
      promoNotificationBean.setSequenceNbr( 6 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER ) )
    {
      promoNotificationBean.setSequenceNbr( 7 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_CLAIM_APPROVAL_NOTIFY_APPROVER ) )
    {
      promoNotificationBean.setSequenceNbr( 8 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER ) )
    {
      promoNotificationBean.setSequenceNbr( 9 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW ) )
    {
      promoNotificationBean.setSequenceNbr( 10 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW ) )
    {
      promoNotificationBean.setSequenceNbr( 11 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW ) )
    {
      promoNotificationBean.setSequenceNbr( 12 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW ) )
    {
      promoNotificationBean.setSequenceNbr( 13 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 14 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 15 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 16 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 17 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 18 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 19 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 20 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 21 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 22 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 23 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 24 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT ) )
    {
      promoNotificationBean.setSequenceNbr( 25 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 26 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 27 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 28 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 29 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 30 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 31 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 32 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 33 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 34 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 35 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 36 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES ) )
    {
      promoNotificationBean.setSequenceNbr( 37 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 38 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 39 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 40 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 41 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 42 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 43 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 44 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 45 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 46 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 47 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 48 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK ) )
    {
      promoNotificationBean.setSequenceNbr( 49 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 50 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 51 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 52 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 53 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 54 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 55 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 56 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 57 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 58 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 59 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 60 );
    }
    else if ( promoEmailNotificationType.getCode().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP ) )
    {
      promoNotificationBean.setSequenceNbr( 61 );
    }
  }

  /**
   * Load all the information needed for displaying the page
   * 
   * @param promotion - the looked up promotion object
   * @param promotionTypeNotifications - the list of existing promotionTypeNotifications
   * @param claimFormTypeNotifications - the list of existing claimFormTypeNotifications
   */
  public void load( Promotion promotion, List promotionTypeNotifications, List claimFormTypeNotifications )
  {
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.hasParent = promotion.hasParent();
    if ( promotion.getAwardType() != null )
    {
      this.awardType = promotion.getAwardType().getCode();
    }

    this.endSubmissionDate = promotion.getSubmissionEndDate();
    if ( promotion.isRecognitionPromotion() )
    {
      this.budgetSweepEnabled = ( (RecognitionPromotion)promotion ).isBudgetSweepEnabled();
      this.budgetEnabled = ( (RecognitionPromotion)promotion ).isBudgetUsed();
      this.purlEnabled = ( (RecognitionPromotion)promotion ).isIncludePurl();
      this.publicRecPointsEnabled = ( (RecognitionPromotion)promotion ).isAllowPublicRecognition();
      this.celebrationsEnabled = ( (RecognitionPromotion)promotion ).isIncludeCelebrations();
    }
    if ( promotion.isNominationPromotion() )
    {
      this.timePeriodActive = ( (NominationPromotion)promotion ).isTimePeriodActive();
    }

    if ( promotion.isSSIPromotion() )
    {
      this.allowActivityUpload = Boolean.TRUE;
    }

    // **** Claim Form Step Email Notifications ****//
    loadClaimFormStepNotifications( promotion, claimFormTypeNotifications );

    // **** Promotion Email Notifications ****//
    loadPromotionNotifications( promotion, promotionTypeNotifications );
    // Sort Promotion Email Notifications by sequenceNbr so that the items appear in the order
    // desired
    if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) || promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) || promotionTypeCode.equals( PromotionType.THROWDOWN )
        || promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      Collections.sort( notificationList, new PromotionNotificationComparator() );
    }

  }

  public Set<PromotionGoalQuestSurvey> toDomainSurveyObjects( Promotion promotion )
  {
    Set<PromotionGoalQuestSurvey> surveySet = new HashSet<PromotionGoalQuestSurvey>();
    Set<Long> promoSurveyIds = new HashSet<Long>();
    String[] notificationSurvey = this.getPromotionNotificationSurvey();
    for ( int i = 0; i < notificationSurvey.length; i++ )
    {
      promoSurveyIds.add( new Long( notificationSurvey[i] ) );
    }
    if ( !promoSurveyIds.contains( new Long( -1 ) ) )
    {
      GoalQuestPromotion gqPromotion = (GoalQuestPromotion)promotion;

      for ( Long surveyId : promoSurveyIds )
      {
        PromotionGoalQuestSurvey promoSurvey = new PromotionGoalQuestSurvey();

        promoSurvey.setPromotion( gqPromotion );
        // get survey by id
        Survey survey = getSurveyService().getSurveyById( surveyId );
        promoSurvey.setSurvey( survey );
        surveySet.add( promoSurvey );
      }
    }

    return surveySet;
  }

  /**
   * Build a list of detached PromotionNotification domain objects to be persisted
   * 
   * @param promotion
   * @return List
   */
  public List toDomainObjects( Promotion promotion )
  {
    List promotionNotificationList = new ArrayList();

    promotion.setId( promotionId );

    boolean isChildPromotion = false;
    if ( promotionTypeCode.equals( PromotionType.PRODUCT_CLAIM ) && ( (ProductClaimPromotion)promotion ).hasParent() )
    {
      // child doesn't have claim form notificiations, just references parent.
      isChildPromotion = true;
    }

    if ( !isChildPromotion && claimFormStepList != null && claimFormStepList.size() > 0 )
    {
      Iterator claimFormStepIter = claimFormStepList.iterator();
      while ( claimFormStepIter.hasNext() )
      {
        // Iterate over the ClaimFormList and build detached PromotionNotificatoin objects
        PromotionClaimFormStepBean claimFormStepBean = (PromotionClaimFormStepBean)claimFormStepIter.next();
        if ( claimFormStepBean.getClaimFormNotificationList() != null && claimFormStepBean.getClaimFormNotificationList().size() > 0 )
        {
          Iterator claimFormNotificationIter = claimFormStepBean.getClaimFormNotificationList().iterator();
          while ( claimFormNotificationIter.hasNext() )
          {
            PromotionNotificationFormBean promoNotificationFormBean = (PromotionNotificationFormBean)claimFormNotificationIter.next();
            ClaimFormNotificationType claimFormNotificationType = new ClaimFormNotificationType();
            if ( promoNotificationFormBean.getPromotionNotificationId().longValue() != 0 )
            {
              claimFormNotificationType.setId( promoNotificationFormBean.getPromotionNotificationId() );
              claimFormNotificationType.setVersion( promoNotificationFormBean.getVersion() );
              claimFormNotificationType.getAuditCreateInfo().setCreatedBy( Long.valueOf( promoNotificationFormBean.getCreatedBy() ) );
              claimFormNotificationType.getAuditCreateInfo().setDateCreated( new Timestamp( promoNotificationFormBean.getDateCreated() ) );
            }
            ClaimFormStepEmailNotification claimFormStepEmailNotification = new ClaimFormStepEmailNotification();
            claimFormStepEmailNotification.setId( promoNotificationFormBean.getClaimFormStepEmailId() );

            claimFormNotificationType.setPromotion( promotion );
            claimFormNotificationType.setNotificationMessageId( promoNotificationFormBean.getNotificationMessageId() );
            claimFormNotificationType.setClaimFormStepEmailNotification( claimFormStepEmailNotification );
            claimFormNotificationType.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.lookup( promoNotificationFormBean.getNotificationType() ) );

            promotionNotificationList.add( claimFormNotificationType );
          }
        }
      }
    }

    if ( notificationList != null && notificationList.size() > 0 )
    {
      Iterator promoNotificationTypeIter = notificationList.iterator();
      while ( promoNotificationTypeIter.hasNext() )
      {
        // Iterate over the notificationList and build detached PromotionNotification objects
        PromotionNotificationType promoNotificationType = new PromotionNotificationType();
        PromotionNotificationFormBean promoNotificationFormBean = (PromotionNotificationFormBean)promoNotificationTypeIter.next();
        boolean skipLoad = false;
        if ( isChildPromotion )
        {
          // inactivity type for child promos is a reference to parent, so don't persist.
          skipLoad = true;
        }

        if ( !skipLoad )
        {
          if ( promoNotificationFormBean.getPromotionNotificationId().longValue() != 0 )
          {
            promoNotificationType.setId( promoNotificationFormBean.getPromotionNotificationId() );
            promoNotificationType.setVersion( promoNotificationFormBean.getVersion() );
            promoNotificationType.getAuditCreateInfo().setCreatedBy( Long.valueOf( promoNotificationFormBean.getCreatedBy() ) );
            promoNotificationType.getAuditCreateInfo().setDateCreated( new Timestamp( promoNotificationFormBean.getDateCreated() ) );
          }
          promoNotificationType.setPromotion( promotion );
          promoNotificationType.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( promoNotificationFormBean.getNotificationType() ) );
          promoNotificationType.setNotificationMessageId( promoNotificationFormBean.getNotificationMessageId() );
          if ( !StringUtils.isEmpty( promoNotificationFormBean.getNumberOfDays() ) )
          {
            promoNotificationType.setNumberOfDays( new Integer( promoNotificationFormBean.getNumberOfDays() ) );
          }
          if ( !StringUtil.isNullOrEmpty( promoNotificationFormBean.getPromotionNotificationFrequencyType() ) )
          {
            promoNotificationType.setPromotionNotificationFrequencyType( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ) );
            if ( promoNotificationFormBean.getDayOfWeekType() != null )
            {
              if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.DAILY ) )
              {
                promoNotificationType.setDayOfWeekType( null );
              }
              else if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.MONTHLY ) )
              {
                promoNotificationType.setDayOfWeekType( null );
              }
              else
              {
                promoNotificationType.setDayOfWeekType( DayOfWeekType.lookup( promoNotificationFormBean.getDayOfWeekType() ) );
              }
            }
            if ( promoNotificationFormBean.getDayOfMonth() > 0 )
            {
              if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.DAILY ) )
              {
                promoNotificationType.setDayOfMonth( 0 );
              }
              else if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.WEEKLY ) )
              {
                promoNotificationType.setDayOfMonth( 0 );
              }
              else
              {
                promoNotificationType.setDayOfMonth( promoNotificationFormBean.getDayOfMonth() );
              }
            }
          }
          promoNotificationType.setDescriminator( promoNotificationFormBean.getDescriminator() );

          // make adjustments for the non-redemption type of notification
          if ( promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER ) )
          {
            if ( !StringUtils.isEmpty( promoNotificationFormBean.getDescriminator() ) )
            {
              String numeric = null;
              if ( promoNotificationFormBean.getDescriminator().equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_ISSUANCE )
                  && !StringUtils.isEmpty( promoNotificationFormBean.getEveryDaysAfterIssuance() ) )
              {
                numeric = promoNotificationFormBean.getEveryDaysAfterIssuance();
              }
              else if ( promoNotificationFormBean.getDescriminator().equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_PROMO_END )
                  && !StringUtils.isEmpty( promoNotificationFormBean.getNumberOfDaysAfterPromoEnd() ) )
              {
                numeric = promoNotificationFormBean.getNumberOfDaysAfterPromoEnd();
              }
              if ( StringUtils.isNumeric( numeric ) )
              {
                promoNotificationType.setNumberOfDays( new Integer( numeric ) );
              }
            }
          }
          // ssi promotion
          if ( promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER )
              || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR ) )
          {
            if ( ( promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
                || promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP ) )
                && !StringUtil.isNullOrEmpty( promoNotificationFormBean.getDaysBeforeContestEnd() ) && StringUtils.isNumeric( promoNotificationFormBean.getDaysBeforeContestEnd() ) )
            {
              promoNotificationType.setNumberOfDays( new Integer( promoNotificationFormBean.getDaysBeforeContestEnd() ) );
            }
            if ( promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER )
                && !StringUtil.isNullOrEmpty( promoNotificationFormBean.getDaysAfterContestCreated() ) && StringUtils.isNumeric( promoNotificationFormBean.getDaysAfterContestCreated() ) )
            {
              promoNotificationType.setNumberOfDays( new Integer( promoNotificationFormBean.getDaysAfterContestCreated() ) );
            }
            if ( promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER )
                && !StringUtils.isEmpty( promoNotificationFormBean.getPromotionNotificationFrequencyType() ) )
            {
              promoNotificationType.setPromotionNotificationFrequencyType( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ) );
              if ( !StringUtils.isEmpty( promoNotificationFormBean.getDayOfWeekType() ) )
              {
                if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.DAILY ) )
                {
                  promoNotificationType.setDayOfWeekType( null );
                }
                else if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode()
                    .equals( PromotionNotificationFrequencyType.MONTHLY ) )
                {
                  promoNotificationType.setDayOfWeekType( null );
                }
                else
                {
                  promoNotificationType.setDayOfWeekType( DayOfWeekType.lookup( promoNotificationFormBean.getDayOfWeekType() ) );
                }
              }
            }
            if ( promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR )
                && StringUtils.isNumeric( promoNotificationFormBean.getDaysAfterContestEnded() ) )
            {
              promoNotificationType.setNumberOfDays( new Integer( promoNotificationFormBean.getDaysAfterContestEnded() ) );
            }
            if ( promoNotificationFormBean.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR )
                && !StringUtils.isEmpty( promoNotificationFormBean.getPromotionNotificationFrequencyType() ) )
            {
              promoNotificationType.setPromotionNotificationFrequencyType( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ) );
              if ( !StringUtils.isEmpty( promoNotificationFormBean.getDayOfWeekType() ) )
              {
                if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode().equals( PromotionNotificationFrequencyType.DAILY ) )
                {
                  promoNotificationType.setDayOfWeekType( null );
                }
                else if ( PromotionNotificationFrequencyType.lookup( promoNotificationFormBean.getPromotionNotificationFrequencyType() ).getCode()
                    .equals( PromotionNotificationFrequencyType.MONTHLY ) )
                {
                  promoNotificationType.setDayOfWeekType( null );
                }
                else
                {
                  promoNotificationType.setDayOfWeekType( DayOfWeekType.lookup( promoNotificationFormBean.getDayOfWeekType() ) );
                }
              }
            }
          }
          promotionNotificationList.add( promoNotificationType );
        }
      }
    }

    return promotionNotificationList;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return List of PromotionNotificationFormBean objects
   */
  public List getNotificationList()
  {
    return notificationList;
  }

  public void setNotificationList( List notificationList )
  {
    this.notificationList = notificationList;
  }

  /**
   * Accessor for the number of PromotionNotificationFormBean objects in the list.
   * 
   * @return int
   */
  public int getNotificationListCount()
  {
    if ( notificationList == null )
    {
      return 0;
    }

    return notificationList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionNotificationFormBean from the value list
   */
  public PromotionNotificationFormBean getNotificationList( int index )
  {
    try
    {
      return (PromotionNotificationFormBean)notificationList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getNotificationMessage()
  {
    return notificationMessage;
  }

  public void setNotificationMessage( String notificationMessage )
  {
    this.notificationMessage = notificationMessage;
  }

  /**
   * @return List of PromotionClaimFormStepBean objects
   */
  public List getClaimFormStepList()
  {
    return claimFormStepList;
  }

  public void setClaimFormStepList( List claimFormStepList )
  {
    this.claimFormStepList = claimFormStepList;
  }

  /**
   * Accessor for the number of PromotionClaimFormStepBean objects in the list.
   * 
   * @return int
   */
  public int getClaimFormStepListCount()
  {
    if ( claimFormStepList == null )
    {
      return 0;
    }

    return claimFormStepList.size();
  }

  private ActionErrors validateNumeric( String stringValue, String attributeName, ActionErrors actionErrors )
  {
    int value = 0;
    try
    {
      value = Integer.parseInt( stringValue );
      if ( value < 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.SUB_ZERO", attributeName ) );
      }
    }
    catch( NumberFormatException e )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.NAN", attributeName ) );
    }
    return actionErrors;
  }

  private ActionErrors validatePriorDays( String notificationPriorDays, int matchScheduledPriorDays, ActionErrors actionErrors )
  {
    int priorDays = 0;
    try
    {
      priorDays = Integer.parseInt( notificationPriorDays );
      if ( priorDays > matchScheduledPriorDays )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.ROUND_NOTIFICATION_DAYS", matchScheduledPriorDays ) );
      }
    }
    catch( NumberFormatException e )
    {
      if ( actionErrors.isEmpty() )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.NAN" ) );
      }
    }
    return actionErrors;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionClaimFormStepBean from the value list
   */
  public PromotionClaimFormStepBean getClaimFormStepList( int index )
  {
    try
    {
      return (PromotionClaimFormStepBean)claimFormStepList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public boolean isHasParent()
  {
    return hasParent;
  }

  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public boolean isAwardLevelPromotion()
  {
    return getAwardType() != null && getAwardType().equals( "merchandise" ) ? true : false;
  }

  public boolean isCPAwardLevelPromotion( Promotion promotion )
  {
    return ( (ChallengePointPromotion)promotion ).getChallengePointAwardType() != null && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equals( "merchTra" )
        ? true
        : false;
  }

  public Date getEndSubmissionDate()
  {
    return endSubmissionDate;
  }

  public void setEndSubmissionDate( Date endDate )
  {
    this.endSubmissionDate = endDate;
  }

  public boolean isBudgetSweepEnabled()
  {
    return budgetSweepEnabled;
  }

  public void setBudgetSweepEnabled( boolean budgetSweepEnabled )
  {
    this.budgetSweepEnabled = budgetSweepEnabled;
  }

  public String getValidationType()
  {
    return validationType;
  }

  public void setValidationType( String validationType )
  {
    this.validationType = validationType;
  }

  public boolean isBudgetEnabled()
  {
    return budgetEnabled;
  }

  public void setBudgetEnabled( boolean budgetEnabled )
  {
    this.budgetEnabled = budgetEnabled;
  }

  public boolean isPurlEnabled()
  {
    return purlEnabled;
  }

  public void setPurlEnabled( boolean purlEnabled )
  {
    this.purlEnabled = purlEnabled;
  }

  public String[] getPromotionNotificationSurvey()
  {
    return promotionNotificationSurvey;
  }

  public void setPromotionNotificationSurvey( String[] promotionNotificationSurvey )
  {
    this.promotionNotificationSurvey = promotionNotificationSurvey;
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  public boolean isPublicRecPointsEnabled()
  {
    return publicRecPointsEnabled;
  }

  public void setPublicRecPointsEnabled( boolean publicRecPointsEnabled )
  {
    this.publicRecPointsEnabled = publicRecPointsEnabled;
  }

  public boolean isCelebrationsEnabled()
  {
    return celebrationsEnabled;
  }

  public void setCelebrationsEnabled( boolean celebrationsEnabled )
  {
    this.celebrationsEnabled = celebrationsEnabled;
  }

  public boolean isAllowActivityUpload()
  {
    return allowActivityUpload;
  }

  public void setAllowActivityUpload( boolean allowActivityUpload )
  {
    this.allowActivityUpload = allowActivityUpload;
  }

  public boolean isTimePeriodActive()
  {
    return timePeriodActive;
  }

  public void setTimePeriodActive( boolean timePeriodActive )
  {
    this.timePeriodActive = timePeriodActive;
  }

}
