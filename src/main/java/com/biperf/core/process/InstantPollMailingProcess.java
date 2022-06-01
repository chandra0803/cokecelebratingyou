/**
 * 
 */

package com.biperf.core.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.InstantPollAudience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.survey.SurveyAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.StringUtil;

/**
 * @author poddutur
 *
 */
public class InstantPollMailingProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( InstantPollMailingProcess.class );

  public static final String PROCESS_NAME = "InstantPoll Mailing Process";
  public static final String BEAN_NAME = "instantPollMailingProcess";

  public static final String MESSAGE_NAME = "InstantPoll Mailing Process";

  private InstantPollService instantPollService;
  private ProcessService processService;
  private MailingService mailingService;

  private String instantPollId;
  private String createIP;
  private boolean createNewIP;

  @Override
  protected void onExecute()
  {
    InstantPoll instantPoll = null;

    if ( !StringUtil.isEmpty( instantPollId ) )
    {
      try
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.SURVEY_QUESTION ) );
        instantPoll = instantPollService.getInstantPollByIdWithAssociations( new Long( instantPollId ), associationRequestCollection );
        processInstantPollEmails( instantPoll );
      }
      catch( ServiceErrorException e )
      {
        log.error( "An exception occurred while saving InstantPoll" );
      }
    }
  }

  private void processInstantPollEmails( InstantPoll instantPoll ) throws ServiceErrorException
  {
    if ( instantPoll != null && instantPoll.getNotifyPax() )
    {
      List<Participant> participants = new ArrayList<Participant>();
      AssociationRequestCollection reqCollection = new AssociationRequestCollection();
      reqCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );

      if ( instantPoll.getAudienceType().equals( "allactivepaxaudience" ) )
      {
        participants = participantService.getAllActiveWithAssociations( reqCollection );
      }
      else
      {
        List<BigDecimal> instantPollUsersList = instantPollService.getUsersListOfSpecifyAudienceByInstantPollId( instantPoll.getId() );
        for ( int i = 0; i < instantPollUsersList.size(); i++ )
        {
          BigDecimal bd = instantPollUsersList.get( i );
          Long userId = bd.longValue();
          Participant eachParticipant = participantService.getParticipantByIdWithAssociations( userId, reqCollection );
          participants.add( eachParticipant );
        }
      }

      if ( null != participants )
      {
        String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
        String websiteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        String instantPollName = instantPoll.getQuestion();
        String submissionEndDate = com.biperf.core.utils.DateUtils.toDisplayString( instantPoll.getSubmissionEndDate() );

        for ( Participant participant : participants )
        {
          Mailing mailing = composeMail( MessageService.INSTANT_POLL_NOTIFY_PARTICIPANT_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
          mailing.addMailingRecipient( getMailingRecipient( participant ) );

          Map objectMap = new HashMap();
          objectMap.put( "firstName", participant.getFirstName() );
          objectMap.put( "lastName", participant.getLastName() );
          objectMap.put( "pollName", instantPollName );
          objectMap.put( "submissionEndDate", submissionEndDate );
          objectMap.put( "programName", programName );
          objectMap.put( "siteLink", websiteUrl );
          try
          {
            mailingService.submitMailing( mailing, objectMap );
          }
          catch( Exception e )
          {
            log.error( "An exception occurred while sending InstantPoll Email" );
            addComment( "An exception occurred while sending InstantPoll Email for. See the log file for additional information." );
          }
        }
        instantPoll.setIsEmailAlreadySent( true );
        instantPollService.saveInstantPoll( instantPoll, new HashSet<InstantPollAudience>() );
      }
    }
  }

  private MailingRecipient getMailingRecipient( Participant pax )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setLocale( pax.getLanguageType() != null ? pax.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    mailingRecipient.setUser( pax );

    return mailingRecipient;
  }

  public String getInstantPollId()
  {
    return instantPollId;
  }

  public void setInstantPollId( String instantPollId )
  {
    this.instantPollId = instantPollId;
  }

  public String getCreateIP()
  {
    return createIP;
  }

  public void setCreateIP( String createIP )
  {
    this.createIP = createIP;
  }

  public boolean isCreateNewIP()
  {
    return createNewIP;
  }

  public void setCreateNewIP( boolean createNewIP )
  {
    this.createNewIP = createNewIP;
  }

  public ProcessService getProcessService()
  {
    return processService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public InstantPollService getInstantPollService()
  {
    return instantPollService;
  }

  public void setInstantPollService( InstantPollService instantPollService )
  {
    this.instantPollService = instantPollService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}
