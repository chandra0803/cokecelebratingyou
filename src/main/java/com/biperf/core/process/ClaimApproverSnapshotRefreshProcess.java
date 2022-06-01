/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ClaimApproverSnapshotRefreshProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;

/**
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
public class ClaimApproverSnapshotRefreshProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ClaimApproverSnapshotRefreshProcess.class );

  public static final String BEAN_NAME = "claimApproverSnapshotRefreshProcess";

  private ClaimService claimService;
  private NodeService nodeService;

  private String NODE_CONFIGURATION_MESSAGE_CM_ASSET_CODE = "message_data.message.101813";

  // properties set from jobDataMap - none

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {

    log.warn( "Starting ClaimApproverSnapshotRefreshProcess" );

    sendNotificationOnInvalidApprovalNodeConfigurations();

    // TODO:OPTIMIZE build list of all UserNodeMembershipValue values for all (manager or
    // owner)/Node/hierarchy values
    // and pass it in to the method claim.getApprovers(), wchi now does much hyd
    // UserNodeMembershipValue userNodeMembershipValue = new UserNodeMembershipValue();

    ClaimQueryConstraint openClaimsConstraint = new ClaimQueryConstraint();
    openClaimsConstraint.setOpen( Boolean.TRUE );

    List openClaims = claimService.getClaimList( openClaimsConstraint );
    for ( Iterator iter = openClaims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      // Just refresh claim, will update snapshot and perform any other clean up such as
      // notificications/payout if snapshot
      // determination causes an auto-approval to occur. If no change, no sql updates will occur
      try
      {
        log.warn( "Refreshing claimId: " + claim.getId() + " if needed" );
        claimService.saveClaim( claim, null, null, false, true );
      }
      catch( ServiceErrorException e )
      {
        addComment( "Got unexpected Service error exception on claimID:" + claim.getId() + ". Exception details can be found in server log" );
        for ( Iterator iterator = e.getServiceErrors().iterator(); iterator.hasNext(); )
        {
          ServiceError serviceError = (ServiceError)iterator.next();
          log.error( "Got unexpected Service error exception on claimID:" + claim.getId() + serviceError.toString() );

        }
      }

    }

    // Email on failure?? on success??

    log.warn( "Finishing ClaimApproverSnapshotRefreshProcess" );

  }

  private void sendNotificationOnInvalidApprovalNodeConfigurations()
  {
    // Get all claims where approval hierarchies don't contain submitted nodes
    List noMatchingApprovalHierarchyClaims = claimService.getOpenClaimsWithNoMatchingNodeInApproverHierarchy( ApproverType.lookup( ApproverType.NODE_OWNER_BY_TYPE ) );

    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

    String noMatchingNodeClaimsString = "";
    if ( noMatchingApprovalHierarchyClaims.isEmpty() )
    {
      noMatchingNodeClaimsString = "NONE";
    }
    for ( Iterator iter = noMatchingApprovalHierarchyClaims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      noMatchingNodeClaimsString += claim.getClaimNumber() + "/" + claim.getSubmitter().getUserName() + ", ";
    }

    // Get all approval nodes that don't contain an owner.
    List noOwnerApprovalNodeNodes = nodeService.getNodeTypeApproverPromotionApprovalNodeswithNoOwner();

    String noOwnerNodesString = "";
    if ( noOwnerApprovalNodeNodes.isEmpty() )
    {
      noOwnerNodesString = "NONE";
    }
    for ( Iterator iter = noOwnerApprovalNodeNodes.iterator(); iter.hasNext(); )
    {
      Node node = (Node)iter.next();
      noOwnerNodesString += node.getName() + "/" + node.getHierarchy().getI18nName() + ", ";
    }

    if ( !noMatchingApprovalHierarchyClaims.isEmpty() || !noOwnerApprovalNodeNodes.isEmpty() )
    {
      // failures found, email the details to the process owner.
      User recipientUser = getRunByUser();

      // Compose the mailing
      Mailing mailing = composeMail( NODE_CONFIGURATION_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

      // Add the recipient
      MailingRecipient mr = addRecipient( recipientUser );
      mr.setLocale( localeCode );

      Map objectMap = new HashMap();
      objectMap.put( "noOwnerNodes", noOwnerNodesString );
      objectMap.put( "noMatchingNodeClaims", noMatchingNodeClaimsString );
      objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
      objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

      mr.addMailingRecipientData( new MailingRecipientData() );
      mailing.addMailingRecipient( mr );

      try
      {
        // Send the e-mail message with personalization
        mailingService.submitMailing( mailing, objectMap );

        addComment( "processName: node configuration error email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending node configuration error email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
        addComment( "An exception occurred while sending node configuration error email Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
            + getProcessInvocationId() + ")" );
      }
    }

  }

  /**
   * @return value of claimService property
   */
  public ClaimService getClaimService()
  {
    return claimService;
  }

  /**
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  /**
   * @param nodeService value for nodeService property
   */
  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

}
