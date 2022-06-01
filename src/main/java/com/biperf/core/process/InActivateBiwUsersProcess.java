
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;

/**
 * InActivateBiwUsersProcess.
 * @author panneers
 * @since Mar 15, 2018
 * @version 1.0
 */
public class InActivateBiwUsersProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( InActivateBiwUsersProcess.class );

  public static final String BEAN_NAME = "inactivateBiwUsersProcess";
  public static final String MESSAGE_NAME = "InActivate BiwUsers Process";

  private ParticipantService participantService;

  public InActivateBiwUsersProcess()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    log.info( "processName: " + BEAN_NAME + " - Calling InActivate BiwUsers Procedure" );
    // Call the stored procedure
    Map resultMap = participantService.inActivateBiwUsers( getRunByUser().getId() );
    int resultCode = ( (Integer)resultMap.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 )// success
    {
      int inactiveBiwUsersCount = ( (Integer)resultMap.get( "p_out_count" ) ).intValue();
      addComment( "Total number of  inactive Biw users: " + inactiveBiwUsersCount );
      log.info( "processName: " + BEAN_NAME + " - Completed InActivate BiwUsers Procedure" );
      sendSummaryMessage( inactiveBiwUsersCount );
    }
    else
    { // fail
      log.info( "processName: " + BEAN_NAME + " -  InActivate BiwUsers Procedure is failed" );
      addComment( "processName: " + BEAN_NAME + " -InActivate BiwUsers Procedure is failed" );
    }
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  protected void sendSummaryMessage( long inactiveBiwUsersCount )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "count", new Long( inactiveBiwUsersCount ) );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.INACTIVE_BIW_USERS_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );
    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      // log.debug( "process: " + processType + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "Total number of  inactive Biw users: " + inactiveBiwUsersCount );
      log.debug( "--------------------------------------------------------------------------------" );

    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending . See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

}
