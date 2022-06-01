/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/DelayedClaimApprovalProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimItem;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ClaimApproveUtils;

/**
 * For approving any claim activities for a promotion whose approval type is delayed. After approval
 * claims will be closed which will invoke payout if processing mode is real_time.
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
 * <td>wadzinsk</td>
 * <td>Nov 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DelayedClaimApprovalProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "delayedClaimApprovalProcess";
  public static final String MESSAGE_NAME = "Delayed Approvals Process";

  private ClaimService claimService;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( DelayedClaimApprovalProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    int totalAttempted = 0;
    int totalSuccess = 0;
    String errorMessage = "Process completed successfully.";

    List<Long> delayedApprovalClaimIds = claimService.getDelayedApprovalClaimIds();

    if ( delayedApprovalClaimIds != null && !delayedApprovalClaimIds.isEmpty() )
    {
      log.error( MESSAGE_NAME + " : " + delayedApprovalClaimIds.size() + " claim ids to be processed." );

      try
      {
        for ( Long claimId : delayedApprovalClaimIds )
        {
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
          Claim claim = claimService.getClaimByIdWithAssociations( claimId, associationRequestCollection );
          if ( claim.isApprovableClaimType() )
          {
            approveUntouchedClaimItems( claim );
            totalAttempted++;
            log.error( "Processing claimId " + claim.getId() );
            claimService.saveClaim( claim, null, null, false, true );
            totalSuccess++;
          }
        }
      }
      catch( ServiceErrorException e )
      {
        errorMessage = "The process did not complete successfully.";
        addComment( "System Error updating claims. See application log for stack trace" );
        throw new BeaconRuntimeException( "Error updating claims during job with processInvocationId: " + getProcessInvocationId(), e );
      }
      catch( Exception e )
      {
        errorMessage = "The process did not complete successfully.";
        logErrorMessage( e );
      }
    }
    // Notify the administrator.
    sendSummaryMessage( totalAttempted, totalSuccess, errorMessage );
  }

  /**
   * Set ClaimItem objects on untouched claim to approved. An untouched claim is a claim where all
   * claim items are in state pending. According to the process Use Case, if any item has been
   * manually shifted from pending to any other state (i.e. has been touched), delayed approval
   * should not occur for that claim.
   * 
   * @param claim
   */
  private void approveUntouchedClaimItems( Claim claim )
  {
    boolean touched = false;

    Set claimItems = claim.getApprovableItems();
    for ( Iterator iter = claimItems.iterator(); iter.hasNext(); )
    {
      ClaimItem claimItem = (ClaimItem)iter.next();
      if ( !claimItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
      {
        touched = true;
        break;
      }
    }

    if ( !touched )
    {
      ClaimApproveUtils.markUndeniedApprovableItemsApproved( claim, null );
    }

  }

  /**
   * Composes and sends a summary e-mail to the "run by" user the number of claims attempted to
   * approve 'untouched' claims and the number of actual success updates
   */
  private void sendSummaryMessage( long totalAttempted, long totalSuccess, String errorMessage )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "totalAttempted", new Long( totalAttempted ) );
    objectMap.put( "totalSuccess", new Long( totalSuccess ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    objectMap.put( "errorMessage", errorMessage );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.DELAYED_APPROVALS_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

}
