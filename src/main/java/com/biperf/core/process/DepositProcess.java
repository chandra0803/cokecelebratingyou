/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/DepositProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

/*
 * DepositProcess <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Dec 1, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class DepositProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name of the Spring bean for this class.
   */
  public static final String BEAN_NAME = "depositProcess";
  public static final String DEPOSIT_MESSAGE_NAME = "Deposit Notice";
  public static final String ADMIN_MESSAGE_NAME = "Deposit Notice Process";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( DepositProcess.class );

  /**
   * The ID of the promotion whose approved journals of type "Points" this process will post.
   */
  private String promotionId;

  /**
   * A reference to the journal service.
   */
  private JournalService journalService;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Posts the approved journals of type "Points" of the given promotion.
   */
  public void onExecute()
  {
    if ( isValid() )
    {
      DepositProcessStatistics statistics = new DepositProcessStatistics();

      // Fetch the journals for the given promotion that are approved, but not posted.
      JournalQueryConstraint queryConstraint = new JournalQueryConstraint();
      queryConstraint.setPromotionId( new Long( promotionId ) );
      queryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.APPROVE ) } );

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

      List journals = journalService.getJournalList( queryConstraint, associationRequestCollection );

      // Post the journals.
      Iterator iter = journals.iterator();
      while ( iter.hasNext() )
      {
        Journal journal = (Journal)iter.next();

        try
        {
          if ( journal.getParticipant().getOptOutAwards() )
          {
            journal.setTransactionAmount( new Long( 0 ) );
          }
          // Post the journal.
          journalService.postJournal( journal );

          // Update the deposit process statistics.
          statistics.accumulatePointsDeposited( journal.getTransactionAmount().intValue() );
          statistics.incrementSuccessfulDeposits();

          // Notify the participant
          if ( systemVariableService.getPropertyByName( SystemVariableService.DEPOSIT_PROCESS_SEND_PAX_DEPOSIT_MAIL ).getBooleanVal() )
          {
            sendPaxMessage( journal );
          }

        }
        catch( Exception e )
        {
          statistics.incrementFailedDeposits();

          log.error( "An exception occurred while posting a journal.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
          addComment( "An exception occurred while posting a journal.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
        }

        if ( isInterrupted() )
        {
          log.error( "Deposit process interrupted.  (process invocation ID = " + getProcessInvocationId() + ")" );
          addComment( "Deposit process interrupted.  (process invocation ID = " + getProcessInvocationId() + ")" );
          break;
        }
      }

      // Notify the administrator.
      sendSummaryMessage( statistics.getSuccessfulDeposits(), statistics.getFailedDeposits(), statistics.getDepositsAttempted(), statistics.getPointsDeposited() );
    }
  }

  /**
   * Sets the journal service
   * 
   * @param journalService a reference to the journal service.
   */
  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  /**
   * Sets the ID of the promotion whose approved journals this process will post.
   * 
   * @param promotionId the ID of the promotion whose approved journals this process will post.
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Composes and sends an email to Participant the # of points deposited
   * 
   * @param journal
   */
  private void sendPaxMessage( Journal journal )
  {
    Map objectMap = new HashMap();

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.DEPOSIT_NOTICE_MESSAGE_CM_ASSET_CODE, MailingType.SYSTEM );

    MailingRecipient mr = addRecipient( journal.getParticipant() );
    // Add mailing recipient
    mailing.addMailingRecipient( mr );

    // Add the mailing level data to the objectMap
    objectMap.put( "firstName", journal.getParticipant().getFirstName() );
    objectMap.put( "lastName", journal.getParticipant().getLastName() );
    if ( journal.getTransactionAmount().longValue() > 1 )
    {
      objectMap.put( "manyAwardAmount", "TRUE" );
    }
    objectMap.put( "awardAmount", String.valueOf( journal.getTransactionAmount() ) );
    objectMap.put( "mediaType", getDepositMediaType( journal ) );
    objectMap.put( "programName", journal.getPromotion().getName() );
    // Message Library Insert Field is using promotionName for this token
    objectMap.put( "promotionName", journal.getPromotion().getName() );
    objectMap.put( "websiteUrl",
                   systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                       + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_URL ).getStringVal() );

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( DEPOSIT_MESSAGE_NAME + " attempted for " + ": " + journal.getParticipant().getFirstName() + " " + journal.getParticipant().getLastName() );
      addComment( DEPOSIT_MESSAGE_NAME + " attempted for " + ": " + journal.getParticipant().getFirstName() + " " + journal.getParticipant().getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + DEPOSIT_MESSAGE_NAME + " to " + journal.getParticipant().getFirstName() + " " + journal.getParticipant().getLastName()
          + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + DEPOSIT_MESSAGE_NAME + " to " + journal.getParticipant().getFirstName() + " " + journal.getParticipant().getLastName()
          + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }

  }

  /**
   * Get the award type for the journal for the deposit process
   */
  private String getDepositMediaType( Journal journal )
  {
    if ( journal.getPromotion().getAwardType() != null )
    {
      return journal.getPromotion().getAwardType().getAbbr();
    }
    else if ( journal.getAwardPayoutType() != null )
    {
      return journal.getAwardPayoutType().getAbbr();
    }
    // This case could possibly come up for a nomination reversal, but those do not send mailings
    else
    {
      return null;
    }
  }

  /**
   * Composes and sends a summary e-mail to the "run by" user the number of deposits attempted, and
   * of that how many were success and failures. Also email the total number of points deposited
   */
  private void sendSummaryMessage( int successCount, int failCount, int depositAttempted, int totalPointsDeposited )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "failCount", new Integer( failCount ) );
    objectMap.put( "depositAttempted", new Integer( depositAttempted ) );
    objectMap.put( "pointsDeposited", new Integer( totalPointsDeposited ) );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.DEPOSIT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + ADMIN_MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + ADMIN_MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Returns true if this <code>DepositProcess</code> object is in a valid state; returns false
   * otherwise.
   * 
   * @return true if this <code>DepositProcess</code> object is in a valid state; returns false
   *         otherwise.
   */
  private boolean isValid()
  {
    boolean isValid = true;

    if ( promotionId == null )
    {
      isValid = false;
      log.error( "Invalid state: Field \"promotionId\" is null." );
    }

    try
    {
      new Long( promotionId );
    }
    catch( NumberFormatException e )
    {
      isValid = false;
      log.error( "Invalid state: Field \"promotionId\" does not represent an integer." );
    }

    return isValid;
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private static class DepositProcessStatistics
  {
    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * The total number of points deposited.
     */
    private int pointsDeposited;

    /**
     * The number of deposits that failed.
     */
    private int failedDeposits;

    /**
     * The number of deposits that succeeded.
     */
    private int successfulDeposits;

    // ------------------------------------------------------------------------
    // Getter and Setter Methods
    // ------------------------------------------------------------------------

    public int getFailedDeposits()
    {
      return failedDeposits;
    }

    public int getPointsDeposited()
    {
      return pointsDeposited;
    }

    public int getSuccessfulDeposits()
    {
      return successfulDeposits;
    }

    public int getDepositsAttempted()
    {
      return successfulDeposits + failedDeposits;
    }

    public void incrementFailedDeposits()
    {
      failedDeposits++;
    }

    public void accumulatePointsDeposited( int pointsDeposited )
    {
      this.pointsDeposited += pointsDeposited;
    }

    public void incrementSuccessfulDeposits()
    {
      successfulDeposits++;
    }
  }
}
