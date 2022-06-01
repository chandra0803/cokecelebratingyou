
package com.biperf.core.process;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.event.KinesisEventScheduler;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

public class KinesisListenerAutoRetryProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( KinesisListenerAutoRetryProcess.class );
  public static final String BEAN_NAME = "kinesisListenerAutoRetryProcess";
  public static final String EMAIL_MESSAGE_NAME = "Kinesis Listener Auto Retry Process";
  @Autowired
  KinesisEventScheduler kinesisEventScheduler;

  @Override
  protected void onExecute()
  {
    log.error( "Starting KinesisListenerAutoRetryProcess" );
    try
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {

        if ( !kinesisEventScheduler.kinesisSchedulerStatus() )
        {
          kinesisEventScheduler.startKinesisConsumer();
          addComment( "KinesisListener was down, now initailzed to consume" );
          sendMessage();
        }
        else
        {
          addComment( "KinesisListener already up and running" );
        }
      }
      else
      {
        addComment( "New version of Service Anniversary is NOT enabled" );
      }

    }
    catch( Exception e )
    {
      log.error( "KinesisListenerAutoRetryProcess failed: " + e );
      addComment( "An exception occurred while processing KinesisListenerAutoRetryProcess " );
    }

  }

  @SuppressWarnings( "rawtypes" )
  private void sendMessage()
  {
    User runByUser = getRunByUser();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd-MMM-YYYY HH:mm:ss" );
    LocalDateTime today = LocalDateTime.now();
    String sysDate = formatter.format( today ).toString() + " " + TimeZone.getDefault().getDisplayName();
    String instantName = System.getProperty( "com.sun.aas.instanceName" );
    String clientName = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();

    // Compose the mailing.
    Mailing mailing = composeMail( MessageService.KINESISLISTENER_AUTORETRY_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Collect e-mail message parameters.
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "clientName", clientName );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "instantName", instantName );
    objectMap.put( "sysDate", sysDate );

    // Add the recipient.
    MailingRecipient mailingRecipient = addRecipient( runByUser );
    mailing.addMailingRecipient( mailingRecipient );

    // Send the e-mail message.
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "------------------------------------------------------------------------------" );
      log.debug( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "Process " + BEAN_NAME + " was run by user " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "------------------------------------------------------------------------------" );

      addComment( clientName + " - " + instantName );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

}
