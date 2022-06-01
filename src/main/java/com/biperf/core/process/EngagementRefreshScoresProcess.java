
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.engagement.EngagementDAO;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.UserManager;

/**
 * 
 * EngagementRefreshScoresProcess.
 * 
 * @author kandhi
 * @since Apr 21, 2014
 * @version 1.0
 */
public class EngagementRefreshScoresProcess extends BaseProcessImpl
{

  /**
   * The name used to get an object of this class from the Spring application context.
   */
  public static final String BEAN_NAME = "engagementRefreshScoresProcess";
  public static final String PROCESS_NAME = "Engagement Promotion Refresh Scores Process";

  private static final Log log = LogFactory.getLog( EngagementRefreshScoresProcess.class );

  private EngagementDAO engagementDAO;

  protected void onExecute()
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "Starting RPM Table Refresh." );
    }
    Map<String, Object> returnMap = getEngagementDAO().refreshEngagementScores( UserManager.getUserId() );
    if ( log.isDebugEnabled() )
    {
      log.debug( "Finished RPM Table Refresh: " + returnMap );
    }
    sendMessage( returnMap );
  }

  @SuppressWarnings( "rawtypes" )
  private void sendMessage( Map returnMap )
  {
    User runByUser = getRunByUser();

    // Compose the mailing.
    Mailing mailing = composeMail( MessageService.ENGAGEMENT_SCORE_REFRESH_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Collect e-mail message parameters.
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    objectMap.put( "resultMessage", (String)returnMap.get( "po_error_message" ) );

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

      addComment( (String)returnMap.get( "po_error_message" ) );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending a " + PROCESS_NAME + " email message. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while sending a " + PROCESS_NAME + " email message. " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public EngagementDAO getEngagementDAO()
  {
    return engagementDAO;
  }

  public void setEngagementDAO( EngagementDAO engagementDAO )
  {
    this.engagementDAO = engagementDAO;
  }

}
