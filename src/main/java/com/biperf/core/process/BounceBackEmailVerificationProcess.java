
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * BounceBackEmailVerificationProcess. 
 * 
 * This process sends an email to an undeliverable address, with the program site email
 * address as the Sender, which is specified in a system variable.
 * Upon non-delivery, the email server (in prod) is expected to bounce back the undeliverable
 * to the sender. The admin is expected to go into the program mailbox and look for the bounce 
 * back mail, the admin will click the link in the email to verify bounce back has worked. 
 * When clicked, it goes to an action to update a system variable BOUNCEBACK_EMAIL_VERIFIED
 * marking that bounce back has been verified. 
 * 
 * The Welcome Email Process cannot be run unless this variable is marked verified.
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
 * <td>Tammy Cheng</td>
 * <td>Oct 26, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BounceBackEmailVerificationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( BounceBackEmailVerificationProcess.class );

  public static final String BEAN_NAME = "bounceBackEmailVerificationProcess";

  public static final String MESSAGE_NAME = "Bounce Back Email Verification Process";

  public static final String NON_DELIVERABLE_EMAIL_ADDRESS = "bad@dkfyisofd1.com";

  /**
   * Sends an email to the address in os_propertyset
   */
  public BounceBackEmailVerificationProcess()
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
    // Sends an email to a bogus address
    sendBounceBackVerificationMessage( NON_DELIVERABLE_EMAIL_ADDRESS );
  }

  /**
   * Composes and sends the email to an undeliverable address to cause bounceback
   * 
   * @param emailAddress
   */
  private void sendBounceBackVerificationMessage( String emailAddress )
  {

    // Add personalization to the objectMap in the email message
    Map objectMap = new HashMap();
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    objectMap.put( "bounceBackUrl",
                   systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/userBounceBackEmailVerification.do?method=update" );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.BOUNCEBACK_EMAIL_VERIFICATION_MESSAGE_CM_ASSET_CODE, MailingType.SYSTEM );

    // Set Sender to the Program mailbox, where a bounce back verification is expected
    String programEmailAddress = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS_INCENTIVE ).getStringVal();
    mailing.setSender( programEmailAddress );

    // Add the recipient
    MailingRecipient mailingRecipient = addRecipient( emailAddress );
    mailing.addMailingRecipient( mailingRecipient );

    try
    {
      // Send the e-mail message with personalization
      mailing = mailingService.submitMailing( mailing, objectMap, getRunByUser().getId() );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to an undeliverable email address. " + " Expects a bounce back at " + programEmailAddress );
      log.debug( "--------------------------------------------------------------------------------" );
      addComment( "processName: " + BEAN_NAME + " has been sent to an undeliverable email address. " + " Expects a bounce back at " + programEmailAddress );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending an undeliverable email to: " + emailAddress + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending an undeliverable email to: " + emailAddress + " See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

}
