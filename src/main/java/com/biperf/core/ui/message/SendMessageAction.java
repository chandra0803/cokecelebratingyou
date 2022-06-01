/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/SendMessageAction.java,v $
 */

package com.biperf.core.ui.message;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.AudienceToParticipantAssociationRequest;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.MessageUtils;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.util.StringUtils;

/**
 * SendMessageAction
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
 * <td>zahler</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SendMessageAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( SendMessageAction.class );

  public static final String SESSION_SEND_MESSAGE_FORM = "sessionSendMessageForm";

  // Constants for the message audience
  public static final String PREVIEW = "preview";
  public static final String PROGRAM_PARTICIPANTS = "programParticipants";
  public static final String PROMOTION_AUDIENCE = "promotionAudience";
  public static final String SPECIFIC_AUDIENCE = "specificAudience";

  // Constants for the message subaudience
  public static final String ALL_PAX_IN_PROGRAM = "allPaxInProgram";
  public static final String ALL_PAX_IN_PROGRAM_WHO = "allPaxInProgramWho";
  public static final String ALL_PAX_IN_PROMOTION = "allPaxInPromotion";
  public static final String ALL_PAX_IN_PROMOTION_WHO = "allPaxInPromotionWho";

  // Constants for the program activities
  public static final String EARNED_AWARD_PERQS = "earnedPoints";
  public static final String SIGNED_UP_TEXT = "signedUpText";

  // Constants for the promotion activities
  public static final String GIVEN_RECOGNITION = "givenRecognition";
  public static final String RECEIVED_RECOGNITION = "receivedRecognition";
  public static final String PASSED_QUIZ = "passedQuiz";
  public static final String TAKEN_QUIZ = "takenQuiz";
  public static final String SUBMITTED_CLAIM = "submittedClaim";
  public static final String USED_ANY_BUDGET = "usedAnyBudget";
  public static final String USED_ALL_BUDGET = "usedAllBudget";

  public static final String DELIVERY_METHOD_SCHEDULED = "scheduled";
  public static final String DELIVERY_METHOD_IMMEDIATE = "immediate";

  /**
   * Prepares the send message page
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareSend( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    SendMessageForm sendMessageForm = (SendMessageForm)actionForm;
    sendMessageForm.setMethod( "send" );
    sendMessageForm.setDeliveryMethod( DELIVERY_METHOD_IMMEDIATE );
    sendMessageForm.setMessageAudience( PREVIEW );

    Long messageId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && clientState.length() > 0 )
      {
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
          String messageIdString = (String)clientStateMap.get( "messageId" );
          messageId = new Long( messageIdString );
        }
        catch( ClassCastException e )
        {
          messageId = (Long)clientStateMap.get( "messageId" );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing
    }
    if ( messageId != null && messageId.longValue() > 0 )
    {
      Message message = getMessageService().getMessageById( messageId );
      sendMessageForm.load( message );
    }
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Sends the email
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward send( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD ); // EARLY EXIT
    }

    SendMessageForm sendMessageForm = (SendMessageForm)actionForm;
    boolean isAdHoc = false;

    try
    {
      Mailing mailing = formToDomainObject( sendMessageForm, errors );

      int paxCount = mailing.getMailingRecipients().size();

      HashMap objectMap;
      String selectedPromotionId = sendMessageForm.getSelectedPromotionId();

      if ( selectedPromotionId == null || selectedPromotionId.equals( "" ) )
      {
        if ( mailing.getMessage() != null && !StringUtils.isEmpty( sendMessageForm.getMessageAudience() ) && sendMessageForm.getMessageAudience().equals( PREVIEW ) )
        {
          objectMap = new HashMap();
          objectMap.put( "canAddGeneralData", "true" );
          buildObjectMap( objectMap );
        }
        else
        {
          objectMap = null;
        }
      }
      else
      {
        Promotion promotion = getPromotionService().getPromotionById( new Long( Long.parseLong( selectedPromotionId ) ) );

        objectMap = new HashMap();
        objectMap.put( "promotionName", promotion.getName() );

      }

      try
      {
        mailing = getMailingService().submitMailing( mailing, objectMap );
        if ( mailing == null )
        {
          errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, new ActionMessage( "admin.send.message.SEND_MAILING_FAILED" ) );
        }
      }
      catch( Exception e )
      {
        errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, new ActionMessage( "admin.send.message.SEND_MAILING_FAILED" ) );
      }

      request.setAttribute( "participantsCount", String.valueOf( paxCount ) );
      if ( sendMessageForm.getDeliveryMethod().equals( DELIVERY_METHOD_SCHEDULED ) )
      {
        request.setAttribute( "confirmationType", "scheduled" );
      }
      else
      {
        request.setAttribute( "confirmationType", "sent" );
      }
      if ( mailing != null && mailing.getMessage() == null )
      {
        request.setAttribute( "messageType", "adhoc" );
        isAdHoc = true;
      }
      else
      {
        request.setAttribute( "messageType", "predefined" );
      }
    }
    catch( ParseException e )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.DATE", "admin.send.message.SCHEDULED_DELIVERY_DATE" ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    if ( sendMessageForm.getMessageAudience().equals( PREVIEW ) && isAdHoc )
    {
      forward = actionMapping.findForward( "previewReturn" );
    }
    return forward;
  }

  private Mailing formToDomainObject( SendMessageForm sendMessageForm, ActionMessages actionErrors ) throws ParseException, BeaconRuntimeException
  {
    Mailing mailing = new Mailing();

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setSender( sendMessageForm.getSender() );

    if ( sendMessageForm.getDeliveryMethod().equals( DELIVERY_METHOD_SCHEDULED ) )
    {
      Date date = DateUtils.toDateWithTime( sendMessageForm.getDeliveryDate() );
      mailing.setDeliveryDate( new Timestamp( date.getTime() ) );
    }
    else
    {
      Date date = DateUtils.getCurrentDate();
      mailing.setDeliveryDate( new Timestamp( date.getTime() ) );
    }

    Set recipients = getMailingRecipients( sendMessageForm );

    if ( !sendMessageForm.getMessageAudience().equals( PREVIEW ) && sendMessageForm.isExcludePreviousRecipients() )
    {
      List previousRecipients = getMailingService().getUsersWhoReceivedMessage( new Long( sendMessageForm.getMessageId() ) );
      removePreviousRecipients( recipients, previousRecipients );

    }
    else if ( !sendMessageForm.getMessageAudience().equals( PREVIEW ) )
    {
      // iterate over recipients and load recipient data
      Iterator iter = recipients.iterator();
      while ( iter.hasNext() )
      {
        MailingRecipient mailingRecipient = (MailingRecipient)iter.next();

        MailingRecipientData firstName = new MailingRecipientData();
        firstName.setKey( "firstName" );
        firstName.setValue( mailingRecipient.getUser().getFirstName() );
        firstName.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( firstName );

        MailingRecipientData lastName = new MailingRecipientData();
        lastName.setKey( "lastName" );
        lastName.setValue( mailingRecipient.getUser().getLastName() );
        lastName.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( lastName );
      }
    }

    mailing.addMailingRecipients( recipients );

    if ( !StringUtils.isEmpty( sendMessageForm.getMessageId() ) )
    {
      Long messageId = new Long( sendMessageForm.getMessageId() );
      Message message = getMessageService().getMessageById( messageId );
      boolean allowPromotionName = !StringUtils.isEmpty( sendMessageForm.getSelectedPromotionId() );
      if ( !MessageUtils.isMessageTextWizardSendable( message.getI18nSubject(), allowPromotionName ) )
      {
        actionErrors.add( "subject", new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", "admin.message.details.SUBJECT" ) );
      }
      if ( !StringUtils.isEmpty( message.getI18nHtmlBody() ) && !MessageUtils.isMessageTextWizardSendable( message.getI18nHtmlBody(), allowPromotionName ) )
      {
        actionErrors.add( "messageName", new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", "admin.send.message.HTML_MESSAGE" ) );
      }
      if ( !StringUtils.isEmpty( message.getI18nPlainTextBody() ) && !MessageUtils.isMessageTextWizardSendable( message.getI18nPlainTextBody(), allowPromotionName ) )
      {
        actionErrors.add( "messageName", new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", "admin.send.message.PLAIN_TEXT_MESSAGE" ) );
      }
      if ( !StringUtils.isEmpty( message.getI18nTextBody() ) && !MessageUtils.isMessageTextWizardSendable( message.getI18nTextBody(), allowPromotionName ) )
      {
        actionErrors.add( "messageName", new ActionMessage( "admin.send.message.MESSAGE_NOT_WIZARD_SENDABLE", "admin.send.message.TEXT_MESSAGE" ) );
      }
      mailing.setMessage( message );
      addMailingLocales( recipients, message, mailing );
    }
    else
    {
      addAdHocMailingLocale( mailing, sendMessageForm.getSubject(), sendMessageForm.getHtmlMsg(), sendMessageForm.getPlainTextMsg(), sendMessageForm.getTextMsg() );
    }

    if ( sendMessageForm.getMessageAudience().equals( PREVIEW ) )
    {
      mailing.setMailingType( MailingType.lookup( MailingType.EMAIL_WIZARD_PREVIEW ) );
    }
    else
    {
      mailing.setMailingType( MailingType.lookup( MailingType.EMAIL_WIZARD ) );
    }

    return mailing;
  }

  private void removePreviousRecipients( Set recipients, List previousRecipients )
  {
    if ( previousRecipients != null && !previousRecipients.isEmpty() )
    {
      Set userIds = new HashSet( previousRecipients.size() );
      for ( Iterator iter = previousRecipients.iterator(); iter.hasNext(); )
      {
        User user = (User)iter.next();
        userIds.add( user.getId() );
      }
      for ( Iterator iterator = recipients.iterator(); iterator.hasNext(); )
      {
        MailingRecipient temp = (MailingRecipient)iterator.next();
        if ( userIds.contains( temp.getUser().getId() ) )
        {
          iterator.remove();
        }
      }
    }
  }

  private void addPreviewRecipients( Set recipients, SendMessageForm sendMessageForm )
  {
    if ( !StringUtils.isEmpty( sendMessageForm.getPreviewEmailAddress1() ) )
    {
      MailingRecipient mailingRecipient = new MailingRecipient();
      mailingRecipient.setGuid( GuidUtils.generateGuid() );
      mailingRecipient.setPreviewEmailAddress( sendMessageForm.getPreviewEmailAddress1() );
      mailingRecipient.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
      recipients.add( mailingRecipient );
    }
    if ( !StringUtils.isEmpty( sendMessageForm.getPreviewEmailAddress2() ) )
    {
      MailingRecipient mailingRecipient2 = new MailingRecipient();
      mailingRecipient2.setGuid( GuidUtils.generateGuid() );
      mailingRecipient2.setPreviewEmailAddress( sendMessageForm.getPreviewEmailAddress2() );
      mailingRecipient2.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
      recipients.add( mailingRecipient2 );
    }
    if ( !StringUtils.isEmpty( sendMessageForm.getPreviewEmailAddress3() ) )
    {
      MailingRecipient mailingRecipient3 = new MailingRecipient();
      mailingRecipient3.setGuid( GuidUtils.generateGuid() );
      mailingRecipient3.setPreviewEmailAddress( sendMessageForm.getPreviewEmailAddress3() );
      mailingRecipient3.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
      recipients.add( mailingRecipient3 );
    }
  }

  private void addProgramRecipients( Set recipients, SendMessageForm sendMessageForm )
  {
    AssociationRequestCollection reqs = new AssociationRequestCollection();
    reqs.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    Set<Participant> paxs = new HashSet<Participant>();
    paxs.addAll( getParticipantService().getAllActiveWithAssociations( reqs ) );
    if ( sendMessageForm.getMessageSubAudience().equals( ALL_PAX_IN_PROGRAM_WHO ) )
    {
      // all active participants with conditions
      if ( sendMessageForm.getProgramActivity().equals( EARNED_AWARD_PERQS ) )
      {
        String start = sendMessageForm.getProgramDateRangeStart();
        String end = sendMessageForm.getProgramDateRangeEnd();
        List userIds = getJournalService().getUserIdsByAwardTypeWithinRange( PromotionAwardsType.POINTS, start, end );
        filterPaxs( paxs, userIds, sendMessageForm.isProgramHaveOrHaveNot() );
      }
      else if ( sendMessageForm.getProgramActivity().equals( SIGNED_UP_TEXT ) )
      {
        for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
        {
          Object temp = iter.next();
          if ( ( ( (Participant)temp ).getParticipantCommunicationPreferences() == null || ( (Participant)temp ).getParticipantCommunicationPreferences().isEmpty() )
              && sendMessageForm.isProgramHaveOrHaveNot() )
          {
            iter.remove();
          }
          else
          {
            for ( Iterator commIter = ( (Participant)temp ).getParticipantCommunicationPreferences().iterator(); commIter.hasNext(); )
            {
              ParticipantCommunicationPreference pref = (ParticipantCommunicationPreference)commIter.next();
              if ( !sendMessageForm.isProgramHaveOrHaveNot() && pref.getParticipantPreferenceCommunicationsType().getCode().equals( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) )
              {
                iter.remove();
              }
              else if ( sendMessageForm.isProgramHaveOrHaveNot() && !pref.getParticipantPreferenceCommunicationsType().getCode().equals( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) )
              {
                iter.remove();
              }
            }
          }
        }
      }
    }
    recipients.addAll( createMailingRecipients( paxs ) );
  }

  private Set getMailingRecipients( SendMessageForm sendMessageForm )
  {
    Set recipients = new HashSet();

    String messageAudience = sendMessageForm.getMessageAudience();

    if ( messageAudience.equals( PREVIEW ) )
    {
      addPreviewRecipients( recipients, sendMessageForm );
    }
    else if ( messageAudience.equals( PROGRAM_PARTICIPANTS ) )
    {
      addProgramRecipients( recipients, sendMessageForm );
    }
    else if ( messageAudience.equals( PROMOTION_AUDIENCE ) )
    {
      addPromotionRecipients( recipients, sendMessageForm );
    }
    else if ( messageAudience.equals( SPECIFIC_AUDIENCE ) )
    {
      AssociationRequestCollection reqCollection = new AssociationRequestCollection();
      reqCollection.add( new AudienceToParticipantAssociationRequest() );

      List audiences = sendMessageForm.getAudienceAsList();

      AssociationRequestCollection reqs = new AssociationRequestCollection();
      reqs.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );

      for ( Iterator iter = audiences.iterator(); iter.hasNext(); )
      {
        PromotionAudienceFormBean bean = (PromotionAudienceFormBean)iter.next();
        Audience audience = getAudienceService().getAudienceById( bean.getAudienceId(), reqCollection );

        Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
        Long primaryHierarchyId = primaryHierarchy.getId();

        Set audienceSet = new LinkedHashSet();
        audienceSet.add( audience );
        List userIdList = getListBuilderService().searchParticipants( audienceSet, primaryHierarchyId, true, null, true );
        Set mailingRecipients = new HashSet();
        for ( Iterator userIdListIterator = userIdList.iterator(); userIdListIterator.hasNext(); )
        {
          FormattedValueBean fvb = (FormattedValueBean)userIdListIterator.next();
          Participant pax = null;
          pax = getParticipantService().getParticipantByIdWithAssociations( fvb.getId(), reqs );
          if ( pax != null )
          {
            MailingRecipient mr = new MailingRecipient();
            mr.setGuid( GuidUtils.generateGuid() );

            if ( pax.getLanguageType() != null )
            {
              mr.setLocale( pax.getLanguageType().getCode() );
            }
            else
            {
              mr.setLocale( getSystemVariableService().getDefaultLanguage().getStringVal() );
            }
            if ( pax.getPrimaryEmailAddress() != null )
            {
              mr.setPreviewEmailAddress( pax.getPrimaryEmailAddress().getEmailAddr() );
            }
            mr.setUser( pax );
            mailingRecipients.add( mr );
          }
          recipients.addAll( mailingRecipients );
        }
      }
    }
    return recipients;
  }

  private void addPromotionRecipients( Set recipients, SendMessageForm sendMessageForm )
  {
    AssociationRequestCollection reqCollection = new AssociationRequestCollection();

    // reqCollection.add( new PromotionAssociationRequest(
    // PromotionAssociationRequest.ALL_AUDIENCES_WITH_PAXS ) );
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );

    Long promoId = new Long( sendMessageForm.getSelectedPromotionId() );
    Set paxs = null;

    if ( sendMessageForm.getMessageSubAudience().equals( ALL_PAX_IN_PROMOTION_WHO ) )
    {
      Date start = DateUtils.toStartDate( DateUtils.toDate( sendMessageForm.getPromotionDateRangeStart() ) );
      Date end = DateUtils.toEndDate( DateUtils.toDate( sendMessageForm.getPromotionDateRangeEnd() ) );
      if ( sendMessageForm.getPromotionActivity().equals( GIVEN_RECOGNITION ) )
      {
        if ( sendMessageForm.isPromotionHaveOrHaveNot() )
        {
          paxs = getParticipantService().getAllPaxWhoHaveGivenRecognition( promoId, start, end );
        }
        else
        {
          paxs = getParticipantService().getAllPaxWhoHaveNotGivenRecognition( promoId, start, end );
        }
      }
      else if ( sendMessageForm.getPromotionActivity().equals( RECEIVED_RECOGNITION ) )
      {
        if ( sendMessageForm.isPromotionHaveOrHaveNot() )
        {
          paxs = getParticipantService().getAllPaxWhoHaveReceivedRecognition( promoId, start, end );
        }
        else
        {
          paxs = getParticipantService().getAllPaxWhoHaveNotReceivedRecognition( promoId, start, end );
        }
      }
      else if ( sendMessageForm.getPromotionActivity().equals( PASSED_QUIZ ) )
      {
        if ( sendMessageForm.isPromotionHaveOrHaveNot() )
        {
          paxs = getParticipantService().getAllPaxWhoHavePassedQuiz( promoId, start, end );
        }
        else
        {
          paxs = getParticipantService().getAllPaxWhoHaveNotPassedQuiz( promoId, start, end );
        }
      }
      else if ( sendMessageForm.getPromotionActivity().equals( TAKEN_QUIZ ) )
      {
        if ( sendMessageForm.isPromotionHaveOrHaveNot() )
        {
          paxs = getParticipantService().getAllPaxWhoHaveTakenQuiz( promoId, start, end );
        }
        else
        {
          paxs = getParticipantService().getAllPaxWhoHaveNotTakenQuiz( promoId, start, end );
        }
      }
      else if ( sendMessageForm.getPromotionActivity().equals( SUBMITTED_CLAIM ) )
      {
        if ( sendMessageForm.isPromotionHaveOrHaveNot() )
        {
          paxs = getParticipantService().getAllPaxWhoHaveSubmittedClaim( promoId, start, end );
        }
        else
        {
          paxs = getParticipantService().getAllPaxWhoHaveNotSubmittedClaim( promoId, start, end );
        }
      }
      else if ( sendMessageForm.getPromotionActivity().equals( USED_ANY_BUDGET ) )
      {
        if ( sendMessageForm.isPromotionHaveOrHaveNot() )
        {
          paxs = getParticipantService().getAllPaxWhoHaveUsedBudget( promoId, start, end, false );
        }
        else
        {
          paxs = getParticipantService().getAllPaxWhoHaveNotUsedBudget( promoId, start, end, false );
        }
      }
      else if ( sendMessageForm.getPromotionActivity().equals( USED_ALL_BUDGET ) )
      {
        if ( sendMessageForm.isPromotionHaveOrHaveNot() )
        {
          paxs = getParticipantService().getAllPaxWhoHaveUsedBudget( promoId, start, end, true );
        }
        else
        {
          paxs = getParticipantService().getAllPaxWhoHaveNotUsedBudget( promoId, start, end, true );
        }
      }
    }
    else
    {
      paxs = getParticipantService().getAllEligiblePaxForPromotion( promoId, true );
      paxs.addAll( getParticipantService().getAllEligiblePaxForPromotion( promoId, false ) );
    }
    if ( paxs != null )
    {
      recipients.addAll( createMailingRecipients( paxs ) );
    }
  }

  private void filterPaxs( Set paxs, List userIds, boolean isHave )
  {
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Object temp = iter.next();
      if ( temp instanceof AudienceParticipant )
      {
        if ( isHave && !userIds.contains( ( (AudienceParticipant)temp ).getParticipant().getId() ) )
        {
          iter.remove();
        }
        else if ( !isHave && userIds.contains( ( (AudienceParticipant)temp ).getParticipant().getId() ) )
        {
          iter.remove();
        }
      }
      else
      {
        if ( isHave && !userIds.contains( ( (Participant)temp ).getId() ) )
        {
          iter.remove();
        }
        else if ( !isHave && userIds.contains( ( (Participant)temp ).getId() ) )
        {
          iter.remove();
        }
      }
    }
  }

  private Set createMailingRecipients( Set paxs )
  {
    Set mailingRecipients = new HashSet();
    AssociationRequestCollection reqs = new AssociationRequestCollection();
    reqs.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      MailingRecipient mr = new MailingRecipient();
      mr.setGuid( GuidUtils.generateGuid() );
      Object temp = iter.next();
      Participant pax = null;
      // paxs might be AudienceParticipant objects or Participants
      try
      {
        pax = (Participant)temp;
      }
      catch( ClassCastException cce )
      {
        pax = ( (AudienceParticipant)temp ).getParticipant();
      }

      pax = getParticipantService().getParticipantByIdWithAssociations( pax.getId(), reqs );
      if ( pax.getLanguageType() != null )
      {
        mr.setLocale( pax.getLanguageType().getCode() );
      }
      else
      {
        mr.setLocale( getSystemVariableService().getDefaultLanguage().getStringVal() );
      }
      if ( pax.getPrimaryEmailAddress() != null )
      {
        mr.setPreviewEmailAddress( pax.getPrimaryEmailAddress().getEmailAddr() );
      }
      mr.setUser( pax );
      mailingRecipients.add( mr );
    }
    return mailingRecipients;
  }

  private void addAdHocMailingLocale( Mailing mailing, String subject, String html, String plain, String text )
  {
    MailingMessageLocale locale = new MailingMessageLocale();
    if ( html != null && html.equals( "" ) )
    {
      locale.setHtmlMessage( plain );
    }
    else
    {
      locale.setHtmlMessage( html );
    }
    locale.setPlainMessage( plain );
    locale.setTextMessage( text );
    locale.setSubject( subject );
    locale.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
    mailing.addMailingMessageLocale( locale );
  }

  private void addMailingLocales( Set recipients, Message message, Mailing mailing )
  {
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient temp = (MailingRecipient)iter.next();
      MailingMessageLocale locale = new MailingMessageLocale();
      locale.setHtmlMessage( message.getI18nHtmlBody( temp.getLocale() ) );
      locale.setPlainMessage( message.getI18nPlainTextBody( temp.getLocale() ) );
      locale.setTextMessage( message.getI18nTextBody( temp.getLocale() ) );
      locale.setSubject( message.getI18nSubject( temp.getLocale() ) );
      locale.setLocale( temp.getLocale() );
      mailing.addMailingMessageLocale( locale );
    }
  }

  /**
   * Add an Audience
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addAudience( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    SendMessageForm sendMessageForm = (SendMessageForm)actionForm;

    // If there was no audience selected, then return an error
    if ( sendMessageForm.getSelectedAudienceId() == null || sendMessageForm.getSelectedAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Long audienceId = new Long( sendMessageForm.getSelectedAudienceId() );

    addAudienceToForm( audienceId, sendMessageForm );
    sendMessageForm.setMethod( "send" );
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * removes any audiences selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward removeAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    SendMessageForm sendMessageForm = (SendMessageForm)form;

    for ( Iterator iter = sendMessageForm.getAudienceAsList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }

    return forward;
  }

  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendMessageForm sendMessageForm = (SendMessageForm)form;

    ActionForward returnForward = mapping.findForward( "audienceLookup" );

    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnAudienceLookup" } );

    String queryString = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_SEND_MESSAGE_FORM, sendMessageForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString } );

    return forward;
  }

  /**
   * Handles the return from the audience builder. This will look for the AudienceId on the request,
   * load the audience and the promotion and build a new PromotionWebRulesAudience which is set onto
   * the form in preparation to saving the webRules.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward returnAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendMessageForm sendMessageForm = (SendMessageForm)form;

    // Get the form back out of the Session to redisplay.
    SendMessageForm sessionSendMessageForm = (SendMessageForm)request.getSession().getAttribute( SESSION_SEND_MESSAGE_FORM );

    if ( sessionSendMessageForm != null )
    {
      try
      {
        BeanUtils.copyProperties( sendMessageForm, sessionSendMessageForm );

        Long audienceId = null;
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
            audienceId = (Long)clientStateMap.get( "audienceId" );
          }
          catch( ClassCastException cce )
          {
            String id = (String)clientStateMap.get( "audienceId" );
            if ( id != null && id.length() > 0 )
            {
              audienceId = new Long( id );
            }
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }

        if ( audienceId != null )
        {
          addAudienceToForm( audienceId, sendMessageForm );
        }
        sendMessageForm.setMethod( "send" );
      }
      catch( Exception e )
      {
        logger.error( "returnAudienceLookup: Copy Properties failed." );
      }
    }

    request.getSession().removeAttribute( SESSION_SEND_MESSAGE_FORM );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addAudienceToForm( Long audienceId, SendMessageForm sendMessageForm )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    sendMessageForm.getAudienceAsList().add( audienceFormBean );
  }

  private Map buildObjectMap( Map objectMap )
  {

    String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
        + getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_URL ).getStringVal();
    objectMap.put( "company", getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "websiteUrl", systemUrl );
    objectMap.put( "siteLink", systemUrl );
    objectMap.put( "url", systemUrl );
    objectMap.put( "siteUrl", systemUrl );
    objectMap.put( "siteURL", systemUrl );
    objectMap.put( "contactUsUrl",
                   getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                       + getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );
    return objectMap;
  }

  /**
   * Retrieves a Message Service
   * 
   * @return MessageService
   */
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  /**
   * Retrieves a Mailing Service
   * 
   * @return MailingService
   */
  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
