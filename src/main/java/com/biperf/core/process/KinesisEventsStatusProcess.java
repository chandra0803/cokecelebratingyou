
package com.biperf.core.process;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.eventreference.EventReference;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.eventreference.EventReferenceService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

public class KinesisEventsStatusProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( KinesisListenerAutoRetryProcess.class );
  public static final String BEAN_NAME = "KinesisEventsStatusProcess";
  public static final String EMAIL_MESSAGE_NAME = "Kinesis Events Status Process";
  private static final int recordLimit = 10;
  private static final int minusDays = 1;
  @Autowired
  EventReferenceService eventReferenceService;

  @Override
  protected void onExecute()
  {
    log.error( "Starting KinesisEventsStatusProcess" );
    try
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        int eventsConsumedCnt = eventReferenceService.getCountOfLastDayEventsByState( null );
        int eventsFailedCnt = eventReferenceService.getCountOfLastDayEventsByState( "ERROR" );
        List<EventReference> eventsFailedList = eventReferenceService.getLastDayEventsByState( "ERROR" );
        sendSummaryMessage( eventsConsumedCnt, eventsFailedCnt, eventsFailedList );
      }
      else
      {
        addComment( "New version of Service Anniversary is NOT enabled" );
      }

    }
    catch( Exception e )
    {
      log.error( "KinesisEventsStatusProcess failed: " + e );
      addComment( "An exception occurred while processing KinesisEventsStatusProcess " );
    }

  }

  private void sendSummaryMessage( int eventsConsumedCnt, int eventsFailedCnt, List<EventReference> eventsFailedList )
  {
    User runByUser = getRunByUser();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd-MMM-YYYY" );
    LocalDateTime today = LocalDateTime.now();
    String date = formatter.format( today.minusDays( minusDays ) ).toString() + " " + TimeZone.getDefault().getDisplayName();
    String clientName = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();

    // Compose the mailing.
    Mailing mailing = composeMail( MessageService.KINESIS_EVENTS_STATUS_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Collect e-mail message parameters.
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "clientName", clientName );
    objectMap.put( "eventsConsumedCnt", eventsConsumedCnt );
    objectMap.put( "eventsFailedCnt", eventsFailedCnt );
    objectMap.put( "date", date );
    objectMap.put( "siteUrl",
                   systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                       + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_URL ).getStringVal() );
    if ( Objects.nonNull( eventsFailedList ) && eventsFailedList.size() > 0 )
    {
      objectMap.put( "showErrorRecord", true );
      String eventsFailedDisplayList = buildEventsFailedDisplayList( eventsFailedList );
      objectMap.put( "eventsFailedDisplayList", eventsFailedDisplayList );

    }
    addComment( "Events Consumed Count : " + eventsConsumedCnt + " eventsFailedCnt : " + eventsFailedCnt );

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
      addComment( "Kinesis Event Status process summary email sent successfully" );
      addComment( clientName );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  private String buildEventsFailedDisplayList( List<EventReference> eventsFailedList )
  {
    StringBuilder sb = new StringBuilder( "" );

    try
    {
      sb.append( "<table border = 1 id=\"\" class=\"table table-bordered table-striped\"> <tbody> <thead> <tr><th style=\"text-align:left\">" ).append( "Source" ).append( "</th>" )
          .append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ).append( "<th>&nbsp;&nbsp;" ).append( "Schema Name" ).append( "</th>" )
          .append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ).append( "<th>&nbsp;&nbsp;" ).append( "Event Code" ).append( "</th>" )
          .append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ).append( "<th>&nbsp;&nbsp;" ).append( "State" ).append( "</th>" ).append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" )
          .append( "<th>&nbsp;&nbsp;" ).append( "Message" ).append( "</th>" ).append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ).append( "</tr> </thead>" );
      eventsFailedList.stream().limit( recordLimit ).forEach( events ->
      {
        // events.getCompanyId()
        sb.append( " <tr><td> " ).append( events.getApplicationName() ).append( " </td>" );
        sb.append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );
        sb.append( "  <td>&nbsp;&nbsp;" ).append( events.getSchemaName() ).append( "</td>" ).append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ).append( "  <td>&nbsp;&nbsp;" )
            .append( events.getEventName() ).append( "</td>" ).append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ).append( "  <td>&nbsp;&nbsp;" ).append( events.getState() ).append( "</td>" )
            .append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" ).append( "  <td>&nbsp;&nbsp;" ).append( events.getMessage() ).append( "</td> </tr>" );

      } );
      sb.append( "</tbody></table>" );

    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }

    return sb.toString();
  }

}
