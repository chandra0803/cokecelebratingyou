/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionNotificationController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionNotificationFrequencyType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.ui.BaseController;

/**
 * PromotionNotificationController.
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
public class PromotionNotificationController extends BaseController
{

  private static Comparator<PromotionNotificationFrequencyType> FREQ_COMPARATOR = new Comparator<PromotionNotificationFrequencyType>()
  {
    public int compare( PromotionNotificationFrequencyType a, PromotionNotificationFrequencyType b )
    {
      return a.getSortOrder() - b.getSortOrder();
    }
  };

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionNotificationForm promoNotificationForm = (PromotionNotificationForm)request.getAttribute( "promotionNotificationForm" );
    if ( promoNotificationForm.getNotificationList() != null && promoNotificationForm.getNotificationList().size() > 0 )
    {
      java.util.Collections.sort( promoNotificationForm.getNotificationList() );
    }

    if ( ObjectUtils.equals( promoNotificationForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    // in case of an error, re-fetch the promotion end date
    Promotion promotion = getPromotionService().getPromotionById( promoNotificationForm.getPromotionId() );
    promoNotificationForm.setEndSubmissionDate( promotion.getSubmissionEndDate() );

    request.setAttribute( "hasParent", Boolean.valueOf( promoNotificationForm.isHasParent() ) );

    request.setAttribute( "promotionStatus", promoNotificationForm.getPromotionStatus() );
    List activeMessages = new ArrayList();
    if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
    {
      request.setAttribute( "pageNumber", "7" );
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.PRODUCT_CLAIM );
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) )
    {
      // Remove the Notifications Related to Public Recognition Add points if Public Recognition is
      // set to false in promotion basics page
      if ( ! ( (RecognitionPromotion)promotion ).isAllowPublicRecognitionPoints() )
      {
        if ( promoNotificationForm.getNotificationList() != null && promoNotificationForm.getNotificationList().size() > 0 )
        {
          java.util.Collections.sort( promoNotificationForm.getNotificationList() );
          for ( Iterator iter = promoNotificationForm.getNotificationList().iterator(); iter.hasNext(); )
          {
            PromotionNotificationFormBean promotionNotificationFormBean = (PromotionNotificationFormBean)iter.next();
            if ( promotionNotificationFormBean.getNotificationType() != null
                && promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS ) )
            {
              iter.remove();
            }
          }
        }
      }

      if ( ! ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
      {
        if ( promoNotificationForm.getNotificationList() != null && promoNotificationForm.getNotificationList().size() > 0 )
        {
          java.util.Collections.sort( promoNotificationForm.getNotificationList() );
          for ( Iterator iter = promoNotificationForm.getNotificationList().iterator(); iter.hasNext(); )
          {
            PromotionNotificationFormBean promotionNotificationFormBean = (PromotionNotificationFormBean)iter.next();
            if ( promotionNotificationFormBean.getNotificationType() != null
                && ( promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) ) )
            {
              iter.remove();
            }
          }
        }
      }
      else
      {
        if ( promoNotificationForm.getNotificationList() != null && promoNotificationForm.getNotificationList().size() > 0 )
        {
          java.util.Collections.sort( promoNotificationForm.getNotificationList() );
          for ( Iterator iter = promoNotificationForm.getNotificationList().iterator(); iter.hasNext(); )
          {
            PromotionNotificationFormBean promotionNotificationFormBean = (PromotionNotificationFormBean)iter.next();
            if ( promotionNotificationFormBean.getNotificationType() != null
                && ( promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.RECOGNITION_RECEIVED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION ) ) )
            {
              iter.remove();
            }
          }
        }
      }

      if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
        {
          request.setAttribute( "pageNumber", "81" );
        }
        else
        {
          request.setAttribute( "pageNumber", "63" );
        }
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
        request.setAttribute( "isBackToApproval", Boolean.TRUE );
      }
      else
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
        {
          request.setAttribute( "pageNumber", "76" );
        }
        else
        {
          request.setAttribute( "pageNumber", "9" );
        }
      }
      if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
      {
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.RECOGNITION );

    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "pageNumber", "10" );

      if ( ! ( (NominationPromotion)promotion ).isAllowPublicRecognitionPoints() || ! ( (NominationPromotion)promotion ).isTimePeriodActive() )
      {
        if ( promoNotificationForm.getNotificationList() != null && promoNotificationForm.getNotificationList().size() > 0 )
        {
          java.util.Collections.sort( promoNotificationForm.getNotificationList() );
          for ( Iterator iter = promoNotificationForm.getNotificationList().iterator(); iter.hasNext(); )
          {
            PromotionNotificationFormBean promotionNotificationFormBean = (PromotionNotificationFormBean)iter.next();
            if ( promotionNotificationFormBean.getNotificationType() != null )
            {
              if ( ! ( (NominationPromotion)promotion ).isAllowPublicRecognitionPoints()
                  && promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS ) )
              {
                iter.remove();
              }
              else if ( ! ( (NominationPromotion)promotion ).isTimePeriodActive()
                  && promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.APPROVER_REMINDER_TIME_PERIOD_EXPIRED ) )
              {
                iter.remove();
              }
            }
          }
        }
      }

      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.NOMINATIONS );
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
    {
      request.setAttribute( "pageNumber", "5" );
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.QUIZ );

      for ( int i = 0; i < activeMessages.size(); i++ )
      {
        if ( activeMessages.get( i ) != null )
        {
          Message message = (Message)activeMessages.get( i );

          if ( message.getCmAssetCode().equals( MessageService.DIY_QUIZ_NOTIFY_PARTICIPANT_MESSAGE_CM_ASSET_CODE ) )
          {
            activeMessages.remove( i );
            break;
          }
        }
      }
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ ) )
    {
      request.setAttribute( "pageNumber", "20" );
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.QUIZ );

      for ( int i = 0; i < activeMessages.size(); i++ )
      {
        if ( activeMessages.get( i ) != null )
        {
          Message message = (Message)activeMessages.get( i );
          if ( message.getCmAssetCode().equals( MessageService.QUIZ_NOTIFY_PARTICIPANT_MESSAGE_CM_ASSET_CODE ) )
          {
            activeMessages.remove( i );
            break;
          }
        }
      }

      request.setAttribute( "isLastPage", Boolean.TRUE );
      request.setAttribute( "isBackToAudience", Boolean.TRUE );
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.ENGAGEMENT ) )
    {
      request.setAttribute( "pageNumber", "3" );
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.ENGAGEMENT );

      request.setAttribute( "isLastPage", Boolean.TRUE );
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.SURVEY ) )
    {
      request.setAttribute( "pageNumber", "4" );
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.SURVEY );
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      if ( promotion.getAwardType() != null && promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) && ( (GoalQuestPromotion)promotion ).getPartnerAudienceType() != null )
      {
        request.setAttribute( "pageNumber", "6" );
        request.setAttribute( "isPartnersEnabled", "true" );
      }
      else
      {
        request.setAttribute( "pageNumber", "5" );
        request.setAttribute( "isPartnersEnabled", "false" );
        // Remove the Notifications Related to Partner
        if ( promoNotificationForm.getNotificationList() != null && promoNotificationForm.getNotificationList().size() > 0 )
        {
          java.util.Collections.sort( promoNotificationForm.getNotificationList() );
          for ( Iterator iter = promoNotificationForm.getNotificationList().iterator(); iter.hasNext(); )
          {
            PromotionNotificationFormBean promotionNotificationFormBean = (PromotionNotificationFormBean)iter.next();
            if ( promotionNotificationFormBean.getNotificationType() != null
                && ( promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_SELECTED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED ) ) )
            {
              iter.remove();
            }

          }
        }
      }

      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.GOALQUEST );
      List surveyList = new ArrayList();
      surveyList = getSurveyService().getAllSurveyFormsNotUnderConstructionByModuleType( PromotionType.GOALQUEST );
      Set completeSet = new LinkedHashSet();
      Iterator it = surveyList.iterator();
      while ( it.hasNext() )
      {
        Survey survey = (Survey)it.next();
        if ( !survey.getPromotionModuleType().getCode().equals( PromotionType.SURVEY ) )
        {
          if ( survey.isComplete() || survey.isAssigned() )
          {
            completeSet.add( survey );
          }
        }
      }
      Survey survey = new Survey();
      survey.setId( new Long( -1 ) );
      survey.setName( "No Survey" );
      completeSet.add( survey );
      request.setAttribute( "notificationSurvey", completeSet );
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      if ( ( (ChallengePointPromotion)promotion ).getChallengePointAwardType() != null
          && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equalsIgnoreCase( ChallengePointAwardType.POINTS )
          && ( (ChallengePointPromotion)promotion ).getPartnerAudienceType() != null )
      {
        request.setAttribute( "isPartnersEnabled", "true" );
      }
      else
      {
        request.setAttribute( "isPartnersEnabled", "false" );
        // Remove the Notifications Related to Partner
        if ( promoNotificationForm.getNotificationList() != null && promoNotificationForm.getNotificationList().size() > 0 )
        {
          java.util.Collections.sort( promoNotificationForm.getNotificationList() );
          for ( Iterator iter = promoNotificationForm.getNotificationList().iterator(); iter.hasNext(); )
          {
            PromotionNotificationFormBean promotionNotificationFormBean = (PromotionNotificationFormBean)iter.next();
            if ( promotionNotificationFormBean.getNotificationType() != null
                && ( promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_GOAL_ACHIEVED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_GOAL_NOT_ACHIEVED )
                    || promotionNotificationFormBean.getNotificationType().equalsIgnoreCase( PromotionEmailNotificationType.CP_PARTNER_PROGRESS_UPDATED ) ) )
            {
              iter.remove();
            }
          }
        }
      }
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.CHALLENGEPOINT );
      request.setAttribute( "pageNumber", "5" );

      List surveyList = new ArrayList();
      List defaultGQSurveyList = new ArrayList();
      surveyList = getSurveyService().getAllSurveyFormsNotUnderConstructionByModuleType( PromotionType.CHALLENGE_POINT );
      defaultGQSurveyList = getSurveyService().getAllSurveyFormsNotUnderConstructionByModuleType( PromotionType.GOALQUEST );

      Set completeSet = new LinkedHashSet();
      Iterator it = surveyList.iterator();
      while ( it.hasNext() )
      {
        Survey survey = (Survey)it.next();
        if ( !survey.getPromotionModuleType().getCode().equals( PromotionType.SURVEY ) )
        {
          if ( survey.isComplete() || survey.isAssigned() )
          {
            completeSet.add( survey );
          }
        }
      }
      Iterator it2 = defaultGQSurveyList.iterator();
      while ( it2.hasNext() )
      {
        Survey goalquestDefaultSurvey = (Survey)it2.next();
        if ( !goalquestDefaultSurvey.getPromotionModuleType().getCode().equals( PromotionType.SURVEY ) )
        {
          if ( goalquestDefaultSurvey.isComplete() || goalquestDefaultSurvey.isAssigned() )
          {
            completeSet.add( goalquestDefaultSurvey );
          }
        }
      }
      Survey survey = new Survey();
      survey.setId( new Long( -1 ) );
      survey.setName( "No Survey" );
      completeSet.add( survey );
      request.setAttribute( "notificationSurvey", completeSet );

    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
    {
      request.setAttribute( "pageNumber", "4" );
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.THROWDOWN );
    }
    else if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      request.setAttribute( "pageNumber", "6" );
      activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.SSI );
    }
    // Put the messages on the request
    request.setAttribute( "notificationMessageMap", getActiveMessageMap( activeMessages ) );
    // Put the frequency and day of week lists on the request

    List<NameIdBean> dayOfMonths = new ArrayList<NameIdBean>();
    for ( int i = 1; i <= 31; i++ )
    {
      NameIdBean monthBean = new NameIdBean();
      monthBean.setId( new Long( i ) );
      monthBean.setName( i + "" );
      dayOfMonths.add( monthBean );
    }
    List frequencyList = PromotionNotificationFrequencyType.getList();
    request.setAttribute( "frequencyList", getSortedFrequencyList( frequencyList ) );
    // request.setAttribute( "frequencyList",PromotionNotificationFrequencyType.getSortedList());
    request.setAttribute( "dayOfWeekList", DayOfWeekType.getList() );
    request.setAttribute( "dayOfMonthList", dayOfMonths );
    request.setAttribute( "isLive", promotion.isLive() );

  }

  private Map getActiveMessageMap( List activeMessages )
  {
    Map activeMessageMap = new HashMap();

    for ( Iterator iter = activeMessages.iterator(); iter.hasNext(); )
    {
      Message message = (Message)iter.next();
      if ( null != message.getMessageTypeCode() )
      {
        String messageTypeCode = message.getMessageTypeCode().getCode();
        List activeMessageListByTypeCode = (List)activeMessageMap.get( messageTypeCode );

        if ( null == activeMessageListByTypeCode )
        {
          activeMessageListByTypeCode = new ArrayList();
          activeMessageMap.put( messageTypeCode, activeMessageListByTypeCode );
        }

        activeMessageListByTypeCode.add( message );
      }
    }

    Set<String> keys = activeMessageMap.keySet();
    for ( String messageTypeCode : keys )
    {
      List messages = (List)activeMessageMap.get( messageTypeCode );

      // Add the "Select One" message
      Message selectMessage = new Message();
      selectMessage.setId( new Long( 0 ) );
      selectMessage.setName( "Select One" );
      messages.add( 0, selectMessage );

      if ( !messageTypeCode.equals( "recognition_received" ) && !messageTypeCode.equals( "celebration_recognition_received" ) && !messageTypeCode.equals( "contest_award_issuance_to_pax" )
          && !messageTypeCode.equals( "contest_award_issuance_to_manager" ) )
      {
        // Add the "No Notification" message
        Message noMessage = new Message();
        noMessage.setId( new Long( -1 ) );
        noMessage.setName( "No Notification" );
        messages.add( 1, noMessage );
      }
    }

    return activeMessageMap;
  }

  private List<PromotionNotificationFrequencyType> getSortedFrequencyList( List<PromotionNotificationFrequencyType> inputList )
  {
    List<PromotionNotificationFrequencyType> sortedList = new ArrayList<PromotionNotificationFrequencyType>();

    for ( PromotionNotificationFrequencyType frequency : inputList )
    {
      if ( frequency.isDaily() )
      {
        frequency.setSortOrder( 1 );
      }
      else if ( frequency.isWeekly() )
      {
        frequency.setSortOrder( 2 );
      }
      else if ( frequency.isMonthly() )
      {
        frequency.setSortOrder( 3 );
      }
      else
      {
        frequency.setSortOrder( 4 );
      }
      sortedList.add( frequency );
    }
    java.util.Collections.sort( sortedList, FREQ_COMPARATOR );
    return sortedList;
  }

  /**
   * Get the MessageService.
   * 
   * @return MessageService.
   */
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
