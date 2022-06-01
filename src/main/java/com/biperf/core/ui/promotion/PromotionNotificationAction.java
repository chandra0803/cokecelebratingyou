/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionNotificationAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.process.InactivityAlertsProcess;
import com.biperf.core.process.ProactiveEmailProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionNotificationService;
import com.biperf.core.service.promotion.PromotionNotificationsUpdateAssociation;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * PromotionNotificationAction.
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
public class PromotionNotificationAction extends PromotionBaseDispatchAction
{

  /** Log */
  private static final Log logger = LogFactory.getLog( PromotionNotificationAction.class );

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionNotificationForm promoNotificationForm = (PromotionNotificationForm)form;

    Promotion promotion;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {

      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = getWizardPromotion( request );

      request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
    }
    else
    {
      // Get the promotionId from the request and get the promotion
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
          catch( ClassCastException e )
          {
            promotionId = (Long)clientStateMap.get( "promotionId" );
          }
          if ( promotionId == null )
          {
            ActionMessages errors = new ActionMessages();
            errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "promotionId as part of clientState" ) );
            saveErrors( request, errors );
            return mapping.findForward( forwardTo );
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }
      }

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );

      promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
    }

    List promotionTypeNotifications = getPromotionNotificationService().getPromotionTypeNotificationsByPromotionId( promotion.getId() );

    Long claimFormTypeNotificationsPromotionId = promotion.getId();
    if ( promotion.isProductClaimPromotion() && ( (ProductClaimPromotion)promotion ).hasParent() )
    {
      // display parent's claim form notification instead (won't be persisted)
      claimFormTypeNotificationsPromotionId = ( (ProductClaimPromotion)promotion ).getParentPromotion().getId();

      // also temporarily replace/add a child inactivity with parent inactivity's version;
      // BuFix 18246 replace/add a child inactivity with parent version;
      promotionTypeNotifications = getPromotionNotificationService().getPromotionTypeNotificationsByPromotionId( ( (ProductClaimPromotion)promotion ).getParentPromotion().getId() );
    }
    if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) || promoNotificationForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      List<PromotionGoalQuestSurvey> promotionSurveyNotifications = getSurveyService().getPromotionGoalQuestSurveysByPromotionId( promotion.getId() );
      promoNotificationForm.loadPromotionSurveyNotifications( promotionSurveyNotifications );
    }

    List claimFormTypeNotifications = getPromotionNotificationService().getClaimFormTypeNotificationsByPromotionId( claimFormTypeNotificationsPromotionId );

    promoNotificationForm.load( promotion, promotionTypeNotifications, claimFormTypeNotifications );

    return mapping.findForward( forwardTo );

  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();
    // Bug # 19598 Start
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }
    // Bug # 19598 End

    PromotionNotificationForm promoNotificationForm = (PromotionNotificationForm)form;

    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }

      return forward;
    }

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      if ( promoNotificationForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) || promoNotificationForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SURVEY ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( promoNotificationForm.getPromotionId(), associationRequestCollection );
      }
      else
      {
        promotion = getPromotionService().getPromotionById( promoNotificationForm.getPromotionId() );
      }

    }

    // SKG2 check for goal quest promotion..
    // Adding/updating/deleting the goal quest Survey..
    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion gqPromotion = (GoalQuestPromotion)promotion;
      Set<PromotionGoalQuestSurvey> surveyToSave = promoNotificationForm.toDomainSurveyObjects( gqPromotion );

      gqPromotion.setPromotionGoalQuestSurveys( surveyToSave );
    }

    List notificationsToSave = promoNotificationForm.toDomainObjects( promotion );

    promotion.setPromotionNotifications( notificationsToSave );

    // bug 37011 - Promotion Notification - Double up Seq - cause logins to whitescreen
    Long promotionId = promotion.getId();
    PromotionNotificationsUpdateAssociation pnua = new PromotionNotificationsUpdateAssociation( promotion );
    try
    {
      promotion = getPromotionService().savePromotion( promotionId, pnua );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    /*
     * try { promotion = getPromotionService().savePromotion( promotion ); } catch(
     * UniqueConstraintViolationException e ) { errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY,
     * new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) ); } // SK: Save Goal Quest Survey
     * - Store each survey type and extended survey type item
     * getPromotionNotificationService().savePromotionNotifications(
     * promoNotificationForm.getPromotionId(), notificationsToSave ); /*
     * getPromotionNotificationService().savePromotionNotifications( promoNotificationForm
     * .getPromotionId(), notificationsToSave );
     */
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      if ( isWizardMode( request ) && !promotion.isDIYQuizPromotion() )
      {
        setPromotionInWizardManager( request, promotion );

        if ( promotion.isRecognitionPromotion() )
        {
          if ( isSaveAndExit( request ) )
          {
            forward = saveAndExit( mapping, request, promotion );
          }
          else
          {
            forward = getWizardNextPage( mapping, request, promotion );
          }
        }

        else
        {
          forward = getWizardNextPage( mapping, request, promotion );
        }
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

  /**
   * Continue or exit without saving
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionFoward
   */
  public ActionForward continueOrExit( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    // ActionForward forward = mapping.findForward(ActionConstants.SUCCESS_FORWARD);
    Promotion promotion = getWizardPromotion( request );
    setPromotionInWizardManager( request, promotion );

    ActionForward forward = getWizardNextPage( mapping, request, promotion );

    return forward;
  }

  public ActionForward openLaunchProcess( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      String promotionId = request.getParameter( "promotionId" );
      String messageId = request.getParameter( "messageId" );
      String notificationName = request.getParameter( "notificationName" );
      if ( notificationName.lastIndexOf( "?" ) != -1 )
      {
        notificationName = notificationName.substring( 0, notificationName.lastIndexOf( "?" ) );
      }
      request.setAttribute( "notificationName", notificationName );
      request.setAttribute( "promotionId", promotionId );
      request.setAttribute( "messageId", messageId );
    }
    catch( Exception e )
    {
      logger.error( "error occured: " + e );
    }

    return mapping.findForward( "showLaunch" );
  }

  public ActionForward backToApproval( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.WIZARD_BACK_TO_APPROVAL );
  }

  public ActionForward launchProcessCount( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      PrintWriter out = response.getWriter();
      // PromotionNotificationType promoNotification= null;
      ClaimFormNotificationType claimPromoNotification = null;
      PromotionNotification promoNotification = null;
      String promotionId = request.getParameter( "promotionId" );
      String messageId = request.getParameter( "messageId" );
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );

      Promotion currentPromotion = getPromotionService().getPromotionByIdWithAssociations( Long.valueOf( promotionId ), associationRequestCollection );
      if ( currentPromotion.getPromotionNotifications() != null && currentPromotion.getPromotionNotifications().size() > 0 )
      {
        Iterator notItr = currentPromotion.getPromotionNotifications().iterator();

        while ( notItr.hasNext() )
        {
          Object obj = notItr.next();

          if ( obj instanceof PromotionNotification )
          {
            promoNotification = (PromotionNotification)obj;
          }

          if ( promoNotification.getNotificationMessageId() == Long.valueOf( messageId ) )
          {
            break;
          }
        }
        ProactiveEmailProcess proactiveEmail = (ProactiveEmailProcess)BeanLocator.getBean( "proactiveEmailProcessTarget" );
        int numberOfPaxes = proactiveEmail.processProActive( currentPromotion, (PromotionNotificationType)promoNotification, true, false );
        out.println( numberOfPaxes );
        /*
         * request.setAttribute( "numberOfPaxes",numberOfPaxes ); request.setAttribute(
         * "notificationName",notificationName.substring( 0, notificationName.length()-1 ) );
         * request.setAttribute( "promotionId",promotionId ); request.setAttribute(
         * "messageId",messageId );
         */
      }
    }
    catch( Exception e )
    {
      logger.error( "error occured: " + e );
    }

    return null;
  }

  public ActionForward doLaunchProcess( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {

    String promotionId = request.getParameter( "promotionId" );
    String messageId = request.getParameter( "messageId" );
    String notificationName = request.getParameter( "notificationName" );
    Process process = getProcessService().createOrLoadSystemProcess( InactivityAlertsProcess.PROCESS_NAME, InactivityAlertsProcess.BEAN_NAME );
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "promotionId", new String[] { promotionId } );
    parameterValueMap.put( "messageId", new String[] { messageId } );

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );
    getProcessService().scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );
    request.setAttribute( "launched", true );
    request.setAttribute( "notificationName", notificationName );
    return mapping.findForward( "showLaunch" );
  }

  public ActionForward backDIYQuiz( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.WIZARD_BACK_DIY_QUIZ_FORWARD );
  }

  /**
   * Get a promotionNotificationService from the request.
   * 
   * @return PromotionNotificationService
   */
  private PromotionNotificationService getPromotionNotificationService()
  {
    return (PromotionNotificationService)getService( PromotionNotificationService.BEAN_NAME );
  }

  /**
   * Get a GoalQuestService() from the request.
   * 
   * @return GoalQuestService
   */
  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

}
