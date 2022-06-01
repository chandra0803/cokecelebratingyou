
package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.managertoolkit.AlertMessageService;
import com.biperf.core.service.managertoolkit.ParticipantAlertService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ManagerSendAlertProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ManagerSendAlertProcess.class );

  public static final String PROCESS_NAME = "Manager Send Alert Process";
  public static final String BEAN_NAME = "managerAlertProcess";
  private static final String EMAIL_SUBJECT = "Manager Send Alert Failed";

  private ParticipantService participantService;
  private AlertMessageService alertMessageService;
  private ParticipantAlertService participantAlertService;

  private String managerId;
  private String orgUnitAndBelow;
  private String subject;
  private String date;
  private String message;
  private String messageTo;
  private String sendEmail;
  private boolean includeChildNode;
  private int recipientCount = 0;
  private String proxyId;

  protected void onExecute()
  {

    Participant manager = null;
    Participant proxy = null;
    if ( !StringUtil.isEmpty( managerId ) )
    {
      manager = participantService.getParticipantById( new Long( managerId ) );
    }
    else
    {
      manager = participantService.getParticipantById( UserManager.getUserId() );
    }

    if ( !StringUtil.isEmpty( proxyId ) )
    {
      proxy = participantService.getParticipantById( new Long( proxyId ) );
    }
    includeChildNode = Boolean.valueOf( orgUnitAndBelow ).booleanValue();

    AlertMessage alertMessage = new AlertMessage();
    alertMessage.setSubject( subject );
    alertMessage.setExpiryDate( DateUtils.toDate( date ) );
    alertMessage.setMessageTo( messageTo );
    alertMessage.setSubmitter( manager );
    alertMessage.setMessage( message );
    alertMessage.setProxyUser( proxy );

    if ( manager != null )
    {
      // create and add to node list if the participant is owner or manager of a given node
      List<Node> nodes = new ArrayList<Node>( manager.getUserNodes().size() );
      for ( Iterator iterator = manager.getUserNodes().iterator(); iterator.hasNext(); )
      {
        UserNode userNode = (UserNode)iterator.next();
        if ( userNode.getHierarchyRoleType().isOwner() || userNode.getHierarchyRoleType().isManager() )
        {
          nodes.add( userNode.getNode() );
        }
      }

      Set paxList = participantService.getPaxInNodes( nodes, includeChildNode );
      recipientCount = paxList.size();

      UserNode userNode = null;

      for ( Iterator iterator = paxList.iterator(); iterator.hasNext(); )
      {
        Participant participant = (Participant)iterator.next();

        if ( participant.getId().equals( UserManager.getUserId() ) )
        {
          continue;
        }
        ParticipantAlert paxAlert = new ParticipantAlert();
        paxAlert.setAlertMessage( alertMessage );
        paxAlert.setUser( participant );

        for ( Iterator userNodeIterator = participant.getUserNodes().iterator(); userNodeIterator.hasNext(); )
        {
          userNode = (UserNode)userNodeIterator.next();

          for ( Iterator nodeIter = nodes.iterator(); nodeIter.hasNext(); )
          {
            Node node = (Node)nodeIter.next();
            if ( node.getId().equals( userNode.getNode().getId() ) )
            {
              paxAlert.setNode( userNode.getNode() );
              break;
            }
          }
        }
        paxAlert = participantAlertService.saveParticipantAlert( paxAlert );
      }

      if ( Boolean.valueOf( sendEmail ).booleanValue() )
      {
        String companyName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
        String companyUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        String companyWebsite = "<a href=\"" + companyUrl + "\">" + companyUrl + "</a>";

        try
        {
          Message message = messageService.getMessageByCMAssetCode( MessageService.MANAGER_ALERT_MESSAGE_CM_ASSET_CODE );

          long startTimeForMailing = System.currentTimeMillis();
          for ( Iterator iterator = paxList.iterator(); iterator.hasNext(); )
          {
            Participant participant = (Participant)iterator.next();

            if ( participant.getId().equals( UserManager.getUserId() ) )
            {
              continue;
            }

            Mailing mailing = buildManagerAlertMail( alertMessage, participant, companyName, companyWebsite, message );

            mailingService.submitMailing( mailing, null );
          }
          long endTimeForMailing = System.currentTimeMillis();
          log.info( "Execution time for send manager alert message: " + ( endTimeForMailing - startTimeForMailing ) );
        }
        catch( Exception e )
        {
          logErrorMessage( e );

          // custom comment
          addComment( "An exception occurred while sending manager alert sent by " + manager.getFirstName() + " " + manager.getLastName() );

          sendSummaryMessage( manager );
        }
        addComment( "Manager Send Alert email has been sent to " + recipientCount + " recipients by " + manager.getFirstName() + " " + manager.getLastName() );
      }
    }

  }

  /**
   * 
   * @param alertMessage
   * @param participant
   * @return Mailing
   */
  private Mailing buildManagerAlertMail( AlertMessage alertMessage, Participant participant, String companyName, String companyWebsite, Message message )
  {

    Mailing mailing = new Mailing();
    String name = alertMessage.getSubmitter().getLastName() + " ," + alertMessage.getSubmitter().getFirstName();
    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setSender( name );
    mailing.setMailingType( MailingType.lookup( MailingType.EMAIL_WIZARD ) );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDate().getTime() ) );
    MailingRecipient mailingRecipient = mailingService.buildMailingRecipientForManagerAlert( alertMessage, participant, companyName, companyWebsite );
    mailing.setMessage( message );
    mailing.addMailingRecipient( mailingRecipient );
    addMailingLocales( mailing.getMailingRecipients(), message, mailing );
    return mailing;
  }

  private void addMailingLocales( Set recipients, Message message, Mailing mailing )
  {
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient temp = (MailingRecipient)iter.next();
      MailingMessageLocale locale = new MailingMessageLocale();
      String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
      if ( temp.getLocale() != null )
      {
        localeCode = temp.getLocale();
      }
      locale.setHtmlMessage( message.getI18nHtmlBody( localeCode ) );
      locale.setPlainMessage( message.getI18nPlainTextBody( localeCode ) );
      String cmTextMessage = message.getI18nTextBody( localeCode );
      String textMessage = "";
      if ( !StringUtil.isEmpty( cmTextMessage ) )
      {
        if ( ( (String)StringUtil.escapeHTML( cmTextMessage ) ).length() > 139 )
        {
          textMessage = ( (String)StringUtil.escapeHTML( cmTextMessage ) ).substring( 0, 139 );
          textMessage = textMessage + " " + CmsResourceBundle.getCmsBundle().getString( "manager.alert.send.SEE_THE_DETAILS" );
        }
        else
        {
          textMessage = (String)StringUtil.escapeHTML( cmTextMessage ) + " " + CmsResourceBundle.getCmsBundle().getString( "manager.alert.send.SEE_THE_DETAILS" );
        }
      }
      locale.setTextMessage( textMessage );
      locale.setSubject( message.getI18nSubject( localeCode ) );
      locale.setLocale( localeCode );
      mailing.addMailingMessageLocale( locale );
    }
  }

  private void sendSummaryMessage( Participant manager )
  {
    try
    {
      String message = "A manager alert failed for " + manager.getFirstName() + " " + manager.getLastName() + " " + "Total number of recipients were " + recipientCount;
      mailingService.submitSystemMailing( EMAIL_SUBJECT, message, message );
      addComment( "Summary email has been sent to system user" );
    }
    catch( Exception e )
    {
      log.error( "Unable to send Manager send alert summary email for process invocation " + getProcessInvocationId(), e );
      addComment( "An exception occurred while sending Manager send alert summary email.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId()
          + ")" );
    }
  }

  public void setManagerId( String managerId )
  {
    this.managerId = managerId;
  }

  public void setOrgUnitAndBelow( String orgUnitAndBelow )
  {
    this.orgUnitAndBelow = orgUnitAndBelow;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public void setMessageTo( String messageTo )
  {
    this.messageTo = messageTo;
  }

  public void setIncludeChildNode( boolean includeChildNode )
  {
    this.includeChildNode = includeChildNode;
  }

  public void setProxyId( String proxyId )
  {
    this.proxyId = proxyId;
  }

  public void setSendEmail( String sendEmail )
  {
    this.sendEmail = sendEmail;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setAlertMessageService( AlertMessageService alertMessageService )
  {
    this.alertMessageService = alertMessageService;
  }

  public void setParticipantAlertService( ParticipantAlertService participantAlertService )
  {
    this.participantAlertService = participantAlertService;
  }

}
