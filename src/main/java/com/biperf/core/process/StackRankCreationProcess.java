/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.service.system.SystemVariableService;

/*
 * StackRankCreationProcess <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 8, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankCreationProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name used to get an object of this class from the Spring application context.
   */
  public static final String BEAN_NAME = "stackRankCreationProcess";

  /**
   * The name of the e-mail message associated with this process.
   */
  public static final String EMAIL_MESSAGE_NAME = "Stack Rank Creation Process";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog( StackRankCreationProcess.class );

  private StackRankService stackRankService;

  /**
   * The ID of the stack rank whose stack rank lists this process will create.
   */
  private Long stackRankId;

  // ---------------------------------------------------------------------------
  // Process Methods
  // ---------------------------------------------------------------------------

  /**
   * Creates stack rank lists for the specified stack rank.
   */
  protected void onExecute()
  {
    // Create the stack rank lists.
    stackRankService.createStackRankLists( stackRankId );

    // Notify the user who triggered this process.
    sendMessage();
  }

  // ---------------------------------------------------------------------------
  // Email Methods
  // ---------------------------------------------------------------------------

  /**
   * Sends an e-mail message to the user that launched this process telling him or her that the
   * stack rank creation process has finished.
   */
  private void sendMessage()
  {
    User runByUser = getRunByUser();
    StackRank stackRank = stackRankService.getStackRank( stackRankId );

    // Collect parameters.
    Map objectMap = new HashMap();

    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    objectMap.put( "promotionName", stackRank.getPromotion().getName() );
    objectMap.put( "startDate", stackRank.getStartDate() );
    objectMap.put( "endDate", stackRank.getEndDate() );
    objectMap.put( "dateCreated", stackRank.getAuditCreateInfo().getDateCreated() );
    objectMap.put( "calculatePayout", new Boolean( stackRank.isCalculatePayout() ).toString() );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the e-mail message.
    Mailing mailing = composeMail( MessageService.STACK_RANK_CREATION_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
    mailing.addMailingRecipient( addRecipient( runByUser ) );

    // Send the e-mail message.
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "------------------------------------------------------------------------------" );
      log.debug( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "Process " + BEAN_NAME + " was run by user " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "------------------------------------------------------------------------------" );

      addComment( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getStackRankId()
  {
    return stackRankId;
  }

  public StackRankService getStackRankService()
  {
    return stackRankService;
  }

  public void setStackRankId( Long stackRankId )
  {
    this.stackRankId = stackRankId;
  }

  public void setStackRankService( StackRankService stackRankService )
  {
    this.stackRankService = stackRankService;
  }
}
