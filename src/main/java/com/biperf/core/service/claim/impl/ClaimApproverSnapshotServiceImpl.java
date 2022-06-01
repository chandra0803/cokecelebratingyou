/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/impl/ClaimApproverSnapshotServiceImpl.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.dao.claim.ClaimApproverSnapshotDAO;
import com.biperf.core.dao.claim.hibernate.ClaimApproverSnapshotQueryConstraint;
import com.biperf.core.dao.client.CokeClientDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.NominationClaimBehaviors;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ApproverCriteria;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.PromotionParticipantApprover;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.util.BaseDomainUtils;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.claim.ClaimApproverSnapshotService;
import com.biperf.core.service.email.EmailNotificationService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ClaimApproversValue;
import com.biperf.core.value.promotion.CustomApproverListValueBean;

/**
 * .
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
 * <td>Feb 9, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimApproverSnapshotServiceImpl implements ClaimApproverSnapshotService
{
	 private static final Log logger = LogFactory.getLog( ClaimApproverSnapshotServiceImpl.class );

  private EmailNotificationService emailNotificationService;
  private MailingService mailingService;
  private ClaimApproverSnapshotDAO claimApproverSnapshotDAO;
  private NodeDAO nodeDAO;
  private ParticipantService participantService;
  private PromotionService promotionService;
  // Client customizations for WIP #42701 starts
  private CokeClientDAO cokeClientDAO;
  // Client customizations for WIP #42701 ends
  private UserCharacteristicService userCharacteristicService;// Client customizations for WIP
  // #56492
  /**
   * Overridden from
   * 
   * @param approverUserId
   */
  public ClaimApproverSnapshot getSnapshot( Long approverUserId, Long approvableId, ApprovableTypeEnum approvableType )
  {
    ClaimApproverSnapshot claimApproverSnapshot = null;
    List<Long> approvableIdList = new ArrayList<Long>();
    approvableIdList.add( approvableId );

    ClaimApproverSnapshotQueryConstraint snapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    if ( !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
    {
      snapshotQueryConstraint.setApproverUserId( approverUserId );
    }
    snapshotQueryConstraint.setApprovableId( approvableIdList );
    snapshotQueryConstraint.setApprovableType( approvableType );

    List claimApproverSnapshotList = claimApproverSnapshotDAO.getClaimApproverSnapshotList( snapshotQueryConstraint );
    if ( !claimApproverSnapshotList.isEmpty() )
    {
      claimApproverSnapshot = (ClaimApproverSnapshot)claimApproverSnapshotList.get( 0 );
    }
    return claimApproverSnapshot;
  }


  private void updateSnapshotApprovers( Long approvableId, Set allClaimApproverUsers, List snapshotsForClaim, ApprovableTypeEnum approvableType )
  {
    // Remove existing snapshot records
    if ( snapshotsForClaim != null )
    {
      for ( Iterator iter = snapshotsForClaim.iterator(); iter.hasNext(); )
      {
        ClaimApproverSnapshot claimApproverSnapshot = (ClaimApproverSnapshot)iter.next();
        claimApproverSnapshotDAO.deleteClaimApproverSnapshot( claimApproverSnapshot );
      }
    }

    // Add new snapshot records
    List actualApproverUserIds = BaseDomainUtils.toIds( allClaimApproverUsers );
    List toAddApproverIds = new ArrayList( actualApproverUserIds );
    for ( Iterator iter = toAddApproverIds.iterator(); iter.hasNext(); )
    {
      Long approverUserId = (Long)iter.next();
      ClaimApproverSnapshot claimApproverSnapshot = new ClaimApproverSnapshot();
      claimApproverSnapshot.setApproverUserId( approverUserId );
      claimApproverSnapshot.setApprovableType( approvableType );
      claimApproverSnapshot.setApprovableId( approvableId );
      claimApproverSnapshotDAO.saveClaimApproverSnapshot( claimApproverSnapshot );
    }
  }

  public void updateClaimApproverSnapshot( Approvable approvable, Long optionalClaimFormStepId, boolean isNewApprovable )
  {

    List unnotifiedApprovers = new ArrayList();

    // Only analyze approvers if promotion type has approval as a concept (quiz doesn't, for
    // instance)
    if ( approvable.getPromotion().getApproverType() != null )
    {
      if ( !approvable.isOpen() )
      {
        // Remove any snapshot entries for this claim
        List snapshotsForClaim = getSnapshotsForClaim( approvable );
        for ( Iterator iter = snapshotsForClaim.iterator(); iter.hasNext(); )
        {
          ClaimApproverSnapshot claimApproverSnapshot = (ClaimApproverSnapshot)iter.next();
          claimApproverSnapshotDAO.deleteClaimApproverSnapshot( claimApproverSnapshot );
        }
      }
      else
      {
        // Claim still open, perform approver determination.
        // This determination may itself cause an approval.
        // TODO: Use approver type handler factory

        boolean additionalApprovalRoundDeterminationRequired = true;
        // May loop for those approver types that have multiple approval rounds.
        while ( additionalApprovalRoundDeterminationRequired )
        {
          ClaimApproversValue claimApproversValue = getApprovers( approvable );
          List actualClaimApproverUsers = new ArrayList( claimApproversValue.getApproverUsers() );

          if ( ClaimApproveUtils.isSnapshotKept( approvable ) )
          {
            Long sourceNodeId = claimApproversValue.getSourceNode() != null ? claimApproversValue.getSourceNode().getId() : null;
            unnotifiedApprovers = updateSnapshotsForClaim( approvable, actualClaimApproverUsers, sourceNodeId );
          }
          else
          {
            // snapshot not kept, so only notify on new claim, unless auto-approved, otherwise
            // notification would go out on every refresh.
            if ( isNewApprovable && !claimApproversValue.isAutoApprove() )
            {
              unnotifiedApprovers = actualClaimApproverUsers;
            }
          }

          if ( claimApproversValue.isAutoApprove() )
          {
            // TODO:(talk to Scott S to confirm):If nomination and in first approval round, don't
            // auto-approve, instead send
            // email saying no approver found for nomination.
            if ( disallowFirstRoundAutoApproval( approvable ) )
            {
              additionalApprovalRoundDeterminationRequired = false;
              throw new BeaconRuntimeException( "TODO:(talk to Scott S to confirm):If nomination " + "and in first approval round, " + "don't auto-approve, instead send"
                  + "email saying no approver found for nomination." );
            }
            // else
            // {
            // Approver Determination caused an auto approval, so save the round and run
            // another round if required.
            User autoApproveUser = claimApproversValue.getAutoApproveUser();
            ClaimApproveUtils.markUndeniedApprovableItemsApproved( approvable, autoApproveUser );
            ClaimApproveUtils.markApprovalRoundComplete( approvable, autoApproveUser, claimApproversValue.getSourceNode() );

            additionalApprovalRoundDeterminationRequired = claimApproversValue.isAdditionalApprovalRoundRequired();

            if ( ClaimApproveUtils.isNodeLevelUsed( approvable ) )
            {
              while ( !additionalApprovalRoundDeterminationRequired && approvable.getNodeLevelsRemaining() > 0 )
              {
                // round was auto-approved and no additional round required yet no node levels
                // remaining means that there are no nodes left for approval, so complete all rounds
                // until
                // no node levels remain
                ClaimApproveUtils.markUndeniedApprovableItemsApproved( approvable, null );
                ClaimApproveUtils.markApprovalRoundComplete( approvable, null, claimApproversValue.getSourceNode() );
              }
            }

            else
            {
              // round auto approved and only using one round, so mark claim closed
              approvable.setOpen( false );
            }
            // }
          }
          else
          {
            // claim round did not get auto approved, so continue with saving new approvers
            additionalApprovalRoundDeterminationRequired = false;
          }
        }

        // Mark claim closed if no node levels remain (if applicable)
        // if ( ( approvable.getNodeLevelsRemaining() == 0 ) && ClaimApproveUtils.isNodeLevelUsed(
        // approvable ) )

        // Changed so automatic_delayed approvals aren't closed automatically - product bug fix
        if ( approvable.getNodeLevelsRemaining() == 0 && ClaimApproveUtils.isNodeLevelUsed( approvable )
            && !approvable.getPromotion().getApprovalType().getCode().equals( ApprovalType.lookup( ApprovalType.AUTOMATIC_DELAYED ).getCode() ) )
        {
          approvable.setOpen( false );
        }
      }
    }

    // Send out any approver notifications for nomination only.
    if ( approvable.getPromotion().isNominationPromotion() && approvable.isOpen() && !unnotifiedApprovers.isEmpty() )
    {
      processSubmittedClaimNominationNotifications( approvable, optionalClaimFormStepId, unnotifiedApprovers );
    }
    else if ( approvable.isOpen() && !unnotifiedApprovers.isEmpty() ) // Send out any approver
                                                                      // notifications.
    {
      processSubmittedClaimNotifications( approvable, optionalClaimFormStepId, unnotifiedApprovers );
    }
  }

  private List updateSnapshotsForClaim( Approvable approvable, List actualApproverUsers, Long sourceNodeId )
  {
    List unnotifiedApprovers;
    List snapshotsForClaim = getSnapshotsForClaim( approvable );

    List addedApproverUsers = addSnapshotApprovers( approvable, actualApproverUsers, snapshotsForClaim, sourceNodeId );
    unnotifiedApprovers = addedApproverUsers;

    updateExistingSnapshotApproversForClaim( approvable.getId(), actualApproverUsers, snapshotsForClaim, sourceNodeId );

    deleteSnapshotApproversForClaim( snapshotsForClaim, actualApproverUsers );
    return unnotifiedApprovers;
  }

  private List extractApproverUserIds( List snapshotsForClaim )
  {
    List approverUserIds = new ArrayList();
    for ( Iterator iter = snapshotsForClaim.iterator(); iter.hasNext(); )
    {
      ClaimApproverSnapshot claimApproverSnapshot = (ClaimApproverSnapshot)iter.next();
      approverUserIds.add( claimApproverSnapshot.getApproverUserId() );
    }

    return approverUserIds;
  }

  private List addSnapshotApprovers( Approvable approvable, List allClaimApproverUsers, List snapshotsForClaim, Long sourceNodeId )
  {
    List actualApproverUserIds = BaseDomainUtils.toIds( allClaimApproverUsers );
    List snapshotApproverUserIds = extractApproverUserIds( snapshotsForClaim );

    List toAddApproverIds = new ArrayList( actualApproverUserIds );
    toAddApproverIds.removeAll( snapshotApproverUserIds );

    for ( Iterator iter = toAddApproverIds.iterator(); iter.hasNext(); )
    {
      Long approverUserId = (Long)iter.next();
      ClaimApproverSnapshot claimApproverSnapshot = new ClaimApproverSnapshot();
      claimApproverSnapshot.setApproverUserId( approverUserId );
      claimApproverSnapshot.setApprovableType( approvable.getApprovableType() );
      claimApproverSnapshot.setApprovableId( approvable.getId() );
      claimApproverSnapshot.setSourceNodeId( sourceNodeId );

      claimApproverSnapshotDAO.saveClaimApproverSnapshot( claimApproverSnapshot );
    }

    // Return added users
    List addedApproverUsers = new ArrayList();

    Map approverUsersKeyedById = BaseDomainUtils.getDomainObjectsKeyedById( allClaimApproverUsers );
    for ( Iterator iter = toAddApproverIds.iterator(); iter.hasNext(); )
    {
      Long approverUserId = (Long)iter.next();
      addedApproverUsers.add( approverUsersKeyedById.get( approverUserId ) );
    }
    return addedApproverUsers;
  }

  private void updateExistingSnapshotApproversForClaim( Long claimId, List allClaimApproverUsers, List snapshotsForClaim, Long sourceNodeId )
  {
    List actualApproverUserIds = BaseDomainUtils.toIds( allClaimApproverUsers );
    List snapshotApproverUserIds = extractApproverUserIds( snapshotsForClaim );

    List updateCandidateApproverIds = new ArrayList( actualApproverUserIds );
    updateCandidateApproverIds.retainAll( snapshotApproverUserIds );

    Map snapshotsKeyedByApproverId = getSnapshotKeyedByApproverUserId( snapshotsForClaim );

    for ( Iterator iter = updateCandidateApproverIds.iterator(); iter.hasNext(); )
    {
      Long approverUserId = (Long)iter.next();
      ClaimApproverSnapshot claimApproverSnapshot = (ClaimApproverSnapshot)snapshotsKeyedByApproverId.get( approverUserId );

      // only update if any non-business key data has changed (business key must be the same
      // already)
      if ( !ObjectUtils.equals( claimApproverSnapshot.getSourceNodeId(), sourceNodeId ) )
      {
        claimApproverSnapshot.setSourceNodeId( sourceNodeId );
      }

      claimApproverSnapshotDAO.saveClaimApproverSnapshot( claimApproverSnapshot );

    }
  }

  private void deleteSnapshotApproversForClaim( List snapshotsForClaim, List allClaimApproverUsers )
  {
    List actualApproverUserIds = BaseDomainUtils.toIds( allClaimApproverUsers );
    List snapshotApproverUserIds = extractApproverUserIds( snapshotsForClaim );

    List toDeleteApproverIds = new ArrayList( snapshotApproverUserIds );
    toDeleteApproverIds.removeAll( actualApproverUserIds );

    // Map snapshotsKeyedByApproverId = getSnapshotKeyedByApproverUserId( snapshotsForClaim );

    Set<Long> toDeleteApproverIdsSet = new HashSet<Long>( toDeleteApproverIds );

    for ( Iterator iter = toDeleteApproverIdsSet.iterator(); iter.hasNext(); )
    {
      Long approverUserId = (Long)iter.next();
      for ( Iterator iter1 = snapshotsForClaim.iterator(); iter1.hasNext(); )
      {
        ClaimApproverSnapshot claimApproverSnapshot = (ClaimApproverSnapshot)iter1.next();
        if ( claimApproverSnapshot.getApproverUserId() != null && claimApproverSnapshot.getApproverUserId().equals( approverUserId ) )
        {
          claimApproverSnapshotDAO.deleteClaimApproverSnapshot( claimApproverSnapshot );
        }
      }
    }
  }

  private Map getSnapshotKeyedByApproverUserId( List snapshotsForClaim )
  {
    Map snapshotsKeyedByApproverId = new LinkedHashMap();
    for ( Iterator iter = snapshotsForClaim.iterator(); iter.hasNext(); )
    {
      ClaimApproverSnapshot claimApproverSnapshot = (ClaimApproverSnapshot)iter.next();
      snapshotsKeyedByApproverId.put( claimApproverSnapshot.getApproverUserId(), claimApproverSnapshot );
    }
    return snapshotsKeyedByApproverId;
  }

  private List getSnapshotsForClaim( Approvable approvable )
  {
    ClaimApproverSnapshotQueryConstraint snapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    List<Long> approvableList = new ArrayList<Long>();
    if ( approvable instanceof ClaimGroup )
    {
      if ( approvable.getApprovalRound().longValue() == 1 )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;
        Set<Claim> claimSet = claimGroup.getClaims();
        for ( Claim claim : claimSet )
        {
          approvableList.add( claim.getId() );
        }
        snapshotQueryConstraint.setApprovableId( approvableList );
        snapshotQueryConstraint.setApprovableType( approvable.getApprovableType().CLAIM );
      }
      else
      {
        approvableList.add( approvable.getId() );
        snapshotQueryConstraint.setApprovableId( approvableList );
        snapshotQueryConstraint.setApprovableType( approvable.getApprovableType().CLAIM_GROUP );
      }
    }
    else
    {
      approvableList.add( approvable.getId() );
      snapshotQueryConstraint.setApprovableId( approvableList );
      snapshotQueryConstraint.setApprovableType( approvable.getApprovableType() );
    }
    return claimApproverSnapshotDAO.getClaimApproverSnapshotList( snapshotQueryConstraint );
  }

  /**
   * Process / send submitted claim notifications to approver
   * 
   * @param claimFormStepId
   */
  private void processSubmittedClaimNominationNotifications( Approvable approvable, Long claimFormStepId, List unnotifiedApprovers )
  {
    Claim claim;
    if ( approvable instanceof Claim )
    {
      claim = (Claim)approvable;

    }
    else
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      // Just use first claim, since this notification doesn't use anything unique to each claim.
      claim = (Claim)claimGroup.getClaims().iterator().next();
    }

    emailNotificationService.processNominationApproverNotification( claim, unnotifiedApprovers );
  }

  /**
   * Returns the users who must approve this claim.
   * 
   * @return a ClaimApproversValue object which hold the users who must approve this claim, as a
   *         <code>Set</code> of {@link User} objects. If autoApprove is true, then the
   *         getApprovers calculation revealed that the approval round should be auto-approved. No
   *         approvers will be returned, but an approverUserId may be set (depending on the approver
   *         type) which can be set as the approver for the round. Returns null if the claim's
   *         promotion doesn't have approval as a concept.
   */
  public ClaimApproversValue getApprovers( Approvable approvable )
  {
    ClaimApproversValue claimApproversValue = null;
    NominationPromotion nominationPromotion = null;
    if ( approvable.getPromotion().isNominationPromotion() )
    {
      nominationPromotion = (NominationPromotion)approvable.getPromotion();
    }

    ApproverType approverType = approvable.getPromotion().getApproverType();
    if ( approverType != null )
    {
      String approverTypeCode = approverType.getCode();
      if ( approverTypeCode.equals( ApproverType.NODE_OWNER_BY_LEVEL ) || approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL )
          || approverTypeCode.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_LEVEL ) )
      {
        Node startingNode = getStartingNode( approvable );
        claimApproversValue = getNodeOwnerByLevelApprovers( approvable, startingNode );
      }
      else if ( approverTypeCode.equals( ApproverType.NODE_OWNER_BY_TYPE ) || approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE )
          || approverTypeCode.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_TYPE ) )
      {
        Node startingNode = getStartingNode( approvable );
        if ( approvable.getPromotion().isNominationPromotion() )
        {
          claimApproversValue = getNodeOwnerByTypeApproversForNomination( approvable, startingNode );
        }
        else
        {
          claimApproversValue = getNodeOwnerByTypeApprovers( approvable, startingNode );
        }
      }
      // Client customization for WIP 58122
      else if ( approvable.getPromotion().isNominationPromotion() && (approverTypeCode.equals( ApproverType.SPECIFIC_APPROVERS ) || approverTypeCode.equals( ApproverType.CUSTOM_APPROVERS ))
          && ( (nominationPromotion.isLevelSelectionByApprover()||nominationPromotion.isLevelPayoutByApproverAvailable()) && ApprovalType.COKE_CUSTOM.equals( approvable.getPromotion().getApprovalType().getCode() ) ) )
      {
        claimApproversValue = getNominationCustomAndSpecificApprovers( approvable );
      }
      else if(approvable.getPromotion().getAdihCashOption() && (approverTypeCode.equals( ApproverType.SPECIFIC_APPROVERS ) || approverTypeCode.equals( ApproverType.CUSTOM_APPROVERS )))
      {
        claimApproversValue = getNominationCustomAndSpecificApprovers( approvable );
      }
      else if ( approverTypeCode.equals( ApproverType.SPECIFIC_APPROVERS ) )
      {
        claimApproversValue = getSpecificApprovers( approvable );
      }
      else if ( approverTypeCode.equals( ApproverType.CUSTOM_APPROVERS ) )
      {
        claimApproversValue = getNominationPromotionCustomApprovers( approvable );
      }
      else if ( approverTypeCode.equals( ApproverType.SUBMITTERS_MANAGER ) )
      {
        claimApproversValue = getSubmitterManagers( (Claim)approvable );
      }
      else
      {
        throw new BeaconRuntimeException( "unhandled approver type code: " + approverTypeCode );
      }
    }
    return claimApproversValue;
  }


  public void updateClaimApproverSnapshot( Approvable approvable )
  {
    // pull next snapshot approver
    ClaimApproversValue claimApproversValue = getApprovers( approvable );
    List actualClaimApproverUsers = new ArrayList( claimApproversValue.getApproverUsers() );
    List snapshotsForClaim = null;
    // if snapshot exists, pull the snapshot
    if ( ClaimApproveUtils.isSnapshotKept( approvable ) )
    {
      snapshotsForClaim = getSnapshotsForClaim( approvable.getId(), approvable.getApprovableType() );
    }
    updateSnapshotApprovers( approvable.getId(), claimApproversValue.getApproverUsers(), snapshotsForClaim, approvable.getApprovableType() );
    for ( Object object : claimApproversValue.getApproverUsers() )
    {
      Participant recipient = (Participant)object;
      Mailing mailing = mailingService.buildCashRecognitionPendingApprovalMailing( recipient );
      mailingService.submitMailing( mailing, null );
    }
  }
/**
 * 
 */
  public void deleteClaimApproverSnapshot( Approvable approvable )
  {
    // Remove existing snapshot records
    List snapshotsForClaim = getSnapshotsForClaim( approvable.getId(), approvable.getApprovableType() );
    for ( Iterator iter = snapshotsForClaim.iterator(); iter.hasNext(); )
    {
      ClaimApproverSnapshot claimApproverSnapshot = (ClaimApproverSnapshot)iter.next();
      claimApproverSnapshotDAO.deleteClaimApproverSnapshot( claimApproverSnapshot );
    }
  }
  /**
   * 
   * @param claimId
   * @param approvableType
   * @return
   */
  private List getSnapshotsForClaim( Long claimId, ApprovableTypeEnum approvableType )
  {
    ClaimApproverSnapshotQueryConstraint snapshotQueryConstraint = new ClaimApproverSnapshotQueryConstraint();
    List<Long> approvableIdList = new ArrayList<Long>();
    approvableIdList.add( claimId );
    snapshotQueryConstraint.setApprovableId( approvableIdList );
    snapshotQueryConstraint.setApprovableType( approvableType );

    return claimApproverSnapshotDAO.getClaimApproverSnapshotList( snapshotQueryConstraint );
  }
  
  /**
   * Return the node to start searching for the first approver.  Only applicable
   * for node based approver types.
   */
  private Node getStartingNode( Approvable approvable )
  {
    Node startingNode = null;
    if ( approvable.getApprovableType().isClaim() )
    {
      Claim claim = (Claim)approvable;
      if ( !claim.getPromotion().isNominationPromotion() )
      {
        // use submitter's node as starting point.
        startingNode = claim.getNode();
        Long claimRecipientId = new Long( 0 );
        Set claimItems = claim.getApprovableItems();

        if ( claim.getPromotion().isProductClaimPromotion() )
        {
          claimRecipientId = claim.getSubmitter().getId();
        }
        else
        {
          claimRecipientId = ( (ClaimRecipient)claimItems.iterator().next() ).getRecipient().getId();
        }

        if ( startingNode != null && null != startingNode.getNodeOwner()
            && ( startingNode.getNodeOwner().getId().equals( claimRecipientId ) || startingNode.getNodeOwner().getId().equals( ( (Claim)approvable ).getSubmitter().getId() ) ) )
        {
          startingNode = startingNode.getParentNode();
        }
      }
      else
      {
        String approverTypeCode = approvable.getPromotion().getApproverType().getCode();
        if ( approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL ) || approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE ) )
        {
          // Use the single claim recipient's node
          Set claimItems = approvable.getApprovableItems();
          // Always only one claim item
          startingNode = ( (ClaimRecipient)claimItems.iterator().next() ).getNode();
          Long claimRecipientId = null;
          if ( approvable instanceof NominationClaim && ! ( (NominationClaim)approvable ).getClaimRecipients().isEmpty() )
          {
            ClaimRecipient claimRecipient = (ClaimRecipient) ( (NominationClaim)approvable ).getClaimRecipients().iterator().next();
            claimRecipientId = claimRecipient.getRecipient().getId();
          }
          else if ( ( (ClaimRecipient)claimItems.iterator().next() ).getRecipient() != null )
          {
            claimRecipientId = ( (ClaimRecipient)claimItems.iterator().next() ).getRecipient().getId();
          }
          if ( startingNode != null && null != startingNode.getNodeOwner()
              && ( startingNode.getNodeOwner().getId().equals( claimRecipientId ) || startingNode.getNodeOwner().getId().equals( ( (Claim)approvable ).getSubmitter().getId() ) ) )
          {
            startingNode = startingNode.getParentNode();
          }
        }
        else if ( approverTypeCode.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_LEVEL ) || approverTypeCode.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_TYPE ) )
        {
          // use nominator's node as starting point.
          Set claimItems = approvable.getApprovableItems();
          startingNode = ( (Claim)approvable ).getNode();
          Long claimRecipientId = null;
          if ( approvable instanceof NominationClaim && ! ( (NominationClaim)approvable ).getClaimRecipients().isEmpty() )
          {
            ClaimRecipient claimRecipient = (ClaimRecipient) ( (NominationClaim)approvable ).getClaimRecipients().iterator().next();
            claimRecipientId = claimRecipient.getRecipient().getId();
          }
          else if ( ( (ClaimRecipient)claimItems.iterator().next() ).getRecipient() != null )
          {
            claimRecipientId = ( (ClaimRecipient)claimItems.iterator().next() ).getRecipient().getId();
          }
          if ( startingNode != null && null != startingNode.getNodeOwner()
              && ( startingNode.getNodeOwner().getId().equals( claimRecipientId ) || startingNode.getNodeOwner().getId().equals( ( (Claim)approvable ).getSubmitter().getId() ) ) )
          {
            startingNode = startingNode.getParentNode();
          }
        }
        else
        {
          throw new BeaconRuntimeException( "Unhandled approverTypeCode: " + approverTypeCode );
        }
      }
    }
    else if ( approvable.getApprovableType().isClaimGroup() )
    {
      String approverTypeCode = approvable.getPromotion().getApproverType().getCode();
      if ( approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL ) || approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE ) )
      {
        Set claimItems = approvable.getApprovableItems();
        startingNode = ( (ClaimGroup)approvable ).getNode();
        Set<Claim> claimSet = ( (ClaimGroup)approvable ).getClaims();
        List<Long> submitters = new ArrayList<Long>();
        for ( Claim claim : claimSet )
        {
          submitters.add( claim.getSubmitter().getId() );
        }

        Long claimRecipientId = ( (ClaimGroup)claimItems.iterator().next() ).getParticipant().getId();
        if ( startingNode != null && !submitters.isEmpty() && null != startingNode.getNodeOwner()
            && ( startingNode.getNodeOwner().getId().equals( claimRecipientId ) || submitters.contains( startingNode.getNodeOwner().getId() ) ) )
        {
          startingNode = startingNode.getParentNode();
        }
      }
      else
      {
        throw new BeaconRuntimeException( "Unhandled approverTypeCode: " + approverTypeCode );
      }
    }
    else
    {
      throw new BeaconRuntimeException( "Unhandled ApprovableType: " + approvable.getApprovableType() );
    }
    return startingNode;
  }

  /**
   * @return the users who must approve this claim, as a <code>Set</code> of {@link User} objects.
   */
  private ClaimApproversValue getNodeOwnerByTypeApprovers( Approvable approvable, Node startingNode )
  {
    Set approvers = new HashSet();
    ClaimApproversValue claimApproversValue = new ClaimApproversValue();

    int nodeLevelsRemaining = approvable.getNodeLevelsRemaining();
    if ( nodeLevelsRemaining <= 0 )
    {
      claimApproversValue.setAdditionalApprovalRoundRequired( false );
    }
    else
    {
      Node approverNode = null;
      if ( approvable.getLastApprovalNode() != null )
      {
        approverNode = approvable.getLastApprovalNode().getParentNode();
      }
      else
      {
        if ( approvable.getApprovalRound().longValue() > 1 )
        {
          throw new BeaconRuntimeException( "last approval node must be set is claim is past first approval round" );
        }

        // We are in round 1 so, find matching node that matches the starting node name in
        // approval hierarchy.
        // This will be the starting point for approver determination.
        Node submitterNode = startingNode;
        approverNode = nodeDAO.getNodeByNameAndHierarchy( submitterNode.getName(), approvable.getPromotion().getApprovalHierarchy() );
        if ( approverNode == null )
        {
          NominationPromotion nomPromo = (NominationPromotion)approvable.getPromotion();
          Participant defaultApprover = nomPromo.getDefaultApprover();
          if ( !defaultApprover.isActive() )
          {
            sendAdminNotificationIfDefaultApproverInactive( nomPromo.getName(), defaultApprover );

          }
          approvers.add( defaultApprover );
          claimApproversValue.setApproverUsers( approvers );
          claimApproversValue.setSourceNode( approvable.getLastApprovalNode() );
          if ( nodeLevelsRemaining > 1 )
          {
            // mark that another approval round is required.
            claimApproversValue.setAdditionalApprovalRoundRequired( true );
          }
          // do nothing, admin will have already been notified that no matching node exists in
          // approval hierarchy
          return claimApproversValue;
        }
      }

      while ( approvers.isEmpty() )
      {
        if ( approverNode == null )
        {
          handleNoNodeApproverCase( approvable, claimApproversValue, startingNode );
          break;
        }

        if ( approverNode.getNodeType().getNodeTypeName().equals( approvable.getPromotion().getApprovalNodeType().getNodeTypeName() ) )
        {
          // We are at approval node so use that node's owner, if one found

          approvers = new HashSet();// approverNode.getUsersByRole( HierarchyRoleType.OWNER );
          User nodeOwner = getParticipantService().getNodeOwner( approverNode.getId() );
          if ( nodeOwner != null )
          {
            approvers.add( nodeOwner );
          }

          if ( approvers.isEmpty() )
          {
            // no owner for the approver node. email will have already gone out to report on this.
            // Leave as no approver.
            handleNoNodeApproverCase( approvable, claimApproversValue, startingNode );
            break;
          }
          if ( !disallowFirstRoundAutoApproval( approvable ) && approvable.getApprovableType().isClaim() && approvers.contains( ( (Claim)approvable ).getSubmitter() )
              && approvable.getPromotion().getCalculator() == null )
          {
            // in Claim case, submitter is approver, so auto-approve
            claimApproversValue.setAutoApprove( true );
            claimApproversValue.setAutoApproveUser( ( (Claim)approvable ).getSubmitter() );
            claimApproversValue.setSourceNode( approverNode );
          }
          else
          {
            // set approvers and the node where they came from.
            claimApproversValue.setApproverUsers( approvers );
            claimApproversValue.setSourceNode( approverNode );
          }

          if ( nodeLevelsRemaining > 1 )
          {
            // mark that another approval round is required.
            claimApproversValue.setAdditionalApprovalRoundRequired( true );
          }
        }
        else
        {
          approverNode = approverNode.getParentNode();

          if ( approverNode == null )
          {
            handleNoNodeApproverCase( approvable, claimApproversValue, startingNode );
            break;
          }

        }
      }
    }

    return claimApproversValue;
  }

  /**
   * @return the users who must approve this claim, as a <code>Set</code> of {@link User} objects.
   */
  private ClaimApproversValue getNodeOwnerByTypeApproversForNomination( Approvable approvable, Node startingNode )
  {
    Set approvers = new HashSet();
    ClaimApproversValue claimApproversValue = new ClaimApproversValue();

    int nodeLevelsRemaining = approvable.getNodeLevelsRemaining();
    if ( nodeLevelsRemaining <= 0 )
    {
      claimApproversValue.setAdditionalApprovalRoundRequired( false );
    }
    else
    {
      Node approverNode = null;
      if ( approvable.getLastApprovalNode() != null )
      {
        approverNode = approvable.getLastApprovalNode().getParentNode();
      }
      else
      {
        if ( approvable.getApprovalRound().longValue() > 1 )
        {
          throw new BeaconRuntimeException( "last approval node must be set is claim is past first approval round" );
        }

        // We are in round 1 so, find matching node that matches the starting node name in
        // approval hierarchy.
        // This will be the starting point for approver determination.
        Node submitterNode = startingNode;
        approverNode = nodeDAO.getNodeByNameAndHierarchy( submitterNode.getName(), approvable.getPromotion().getApprovalHierarchy() );
        if ( approverNode == null )
        {
          // do nothing, admin will have already been notified that no matching node exists in
          // approval hierarchy
          return claimApproversValue;
        }
      }

      while ( approvers.isEmpty() )
      {
        // check approver node type and approval node type in promotion set up equal
        if ( approverNode != null )
        {
          if ( approverNode.getNodeType().equals( approvable.getPromotion().getApprovalNodeType() ) )
          {
            User nodeOwner = approverNode.getNodeOwner();
            if ( nodeOwner != null )
            {
              approvers.add( nodeOwner );
              // set approvers and the node where they came from.
              claimApproversValue.setApproverUsers( approvers );
              claimApproversValue.setSourceNode( approverNode );
              if ( nodeLevelsRemaining > 1 )
              {
                // mark that another approval round is required.
                claimApproversValue.setAdditionalApprovalRoundRequired( true );
              }
            }
            else
            {
              approverNode = approverNode.getParentNode();
            }
          }
          else
          {
            approverNode = approverNode.getParentNode();
          }
        }

        if ( approverNode == null )
        {
          // set default approver from promotion setup as approver
          NominationPromotion nomPromo = (NominationPromotion)approvable.getPromotion();
          Participant defaultApprover = nomPromo.getDefaultApprover();
          if ( !defaultApprover.isActive() )
          {
            sendAdminNotificationIfDefaultApproverInactive( nomPromo.getName(), defaultApprover );
            break;
          }
          approvers.add( defaultApprover );
          claimApproversValue.setApproverUsers( approvers );
          claimApproversValue.setSourceNode( approvable.getLastApprovalNode() );
          if ( nodeLevelsRemaining > 1 )
          {
            // mark that another approval round is required.
            claimApproversValue.setAdditionalApprovalRoundRequired( true );
          }
          break;
        }
      }
    }
    return claimApproversValue;
  }

  /**
   * @return the users who must approve this claim, as a <code>Set</code> of {@link User} objects.
   */
  private ClaimApproversValue getNodeOwnerByLevelApprovers( Approvable approvable, Node startingNode )
  {
    Set approvers = new HashSet();
    ClaimApproversValue claimApproversValue = new ClaimApproversValue();

    int nodeLevelsRemaining = approvable.getNodeLevelsRemaining();
    if ( nodeLevelsRemaining <= 0 )
    {
      claimApproversValue.setAdditionalApprovalRoundRequired( false );
    }
    else
    {
      Node approverNode = null;
      if ( approvable.getLastApprovalNode() != null )
      {
        approverNode = approvable.getLastApprovalNode().getParentNode();
      }
      else
      {
        if ( approvable.getApprovalRound().longValue() > 1 )
        {
          throw new BeaconRuntimeException( "last approval node must be set if claim is past first approval round" );
        }

        // We are in round 1 so use the selected starting point.
        approverNode = startingNode;
      }

      while ( approvers.isEmpty() )
      {
        approvers = new HashSet();
        if ( approverNode == null )
        {
          if ( approvable.getPromotion().isNominationPromotion() )
          {
            NominationPromotion nomPromo = (NominationPromotion)approvable.getPromotion();
            Participant defaultApprover = nomPromo.getDefaultApprover();
            if ( !defaultApprover.isActive() )
            {
              sendAdminNotificationIfDefaultApproverInactive( nomPromo.getName(), defaultApprover );
              break;
            }
            approvers.add( defaultApprover );
            claimApproversValue.setApproverUsers( approvers );
            claimApproversValue.setSourceNode( approvable.getLastApprovalNode() );
            break;
          }
          else
          {
            handleNoNodeApproverCase( approvable, claimApproversValue, startingNode );
            break;
          }
        }
        else
        {
          User nodeOwner = getParticipantService().getNodeOwner( approverNode.getId() );
          if ( nodeOwner != null )
          {
            approvers.add( nodeOwner );
          }
          else if ( nodeOwner == null && approvable.getPromotion().isNominationPromotion() )
          {
            NominationPromotion nomPromo = (NominationPromotion)approvable.getPromotion();
            Participant defaultApprover = nomPromo.getDefaultApprover();
            if ( !defaultApprover.isActive() )
            {
              sendAdminNotificationIfDefaultApproverInactive( nomPromo.getName(), defaultApprover );
              break;
            }
            approvers.add( defaultApprover );
          }
          else // Node is not null, node owner is null, not nomination promotion
          {
            // no owners for the examined node, so move to parent node
            approverNode = approverNode.getParentNode();
            if ( approverNode == null )
            {
              handleNoNodeApproverCase( approvable, claimApproversValue, startingNode );
              break;
            }
          }

          if ( !approvers.isEmpty() )
          {
            if ( !disallowFirstRoundAutoApproval( approvable ) && approvable.getApprovableType().isClaim() && approvers.contains( ( (Claim)approvable ).getSubmitter() )
                && approvable.getPromotion().getCalculator() == null )
            {
              // submitter is approver, so auto-approve
              claimApproversValue.setAutoApprove( true );
              claimApproversValue.setAutoApproveUser( ( (Claim)approvable ).getSubmitter() );
              claimApproversValue.setSourceNode( approverNode );
            }
            else
            {
              // set approvers and the node where they came from.
              claimApproversValue.setApproverUsers( approvers );
              claimApproversValue.setSourceNode( approverNode );
            }

            if ( nodeLevelsRemaining > 1 )
            {
              // mark that another approval round is required.
              claimApproversValue.setAdditionalApprovalRoundRequired( true );
            }
          }
        }
      }
    }

    return claimApproversValue;
  }

  private void handleNoNodeApproverCase( Approvable approvable, ClaimApproversValue claimApproversValue, Node node )
  {
    if ( !disallowFirstRoundAutoApproval( approvable ) )
    {
      // Reached the top of the hierarchy, so no one else left to approve, so mark as
      // auto-approved with no approver
      claimApproversValue.setAutoApprove( true );
    }
    else
    {
      // notify admin that no first round node owner
      // exists to approve the claim
      notifyPMThatNoFirstRoundNominationApproverExists( approvable, node );

      // do nothing else, as the approvable can't be approved until the node situation is resolved
    }
  }

  /**
   * @param approvable
   */
  private void notifyPMThatNoFirstRoundNominationApproverExists( Approvable approvable, Node node )
  {
    String idTypeString;
    if ( approvable instanceof ClaimGroup )
    {
      idTypeString = "ClaimGroup";
    }
    else
    {
      idTypeString = "Claim";
    }

    String noApproverString = idTypeString + " ID : " + approvable.getId() + " / " + node.getName();

    mailingService.submitSystemMailing( "Node Approval Configuration Error - Nomination Claim First Round has no approver",
                                        "Claim or ClaimGroup that was no approver:<br/>" + "(id/node) " + noApproverString + "<br/><br/>Assign an owner to an approver "
                                            + "node, then wait for the daily " + "ClaimApproverSnapshotRefreshProcess to run (or run it manually)",
                                        "Claim or ClaimGroup that was no approver::\r\n" + "(id/node) " + noApproverString + "<br/><br/>Assign an owner to an approver "
                                            + "node, then wait for the daily " + "ClaimApproverSnapshotRefreshProcess to run (or run it manually)" );

  }

  /**
   * Returns the users who must approve this claim when the approvers are users associated with the
   * promotion.
   * 
   * @return the users who must approve this claim, as a <code>Set</code> of {@link User} objects.
   */
  private ClaimApproversValue getSpecificApprovers( Approvable approvable )
  {
    Set approvers = new HashSet();

    if ( approvable.getPromotion().isNominationPromotion() )
    {
      Long approvalRound;
      NominationPromotion nominationPromotion = (NominationPromotion)approvable.getPromotion();
      if ( approvable instanceof Claim )
      {
        Claim claim = (Claim)approvable;
        approvalRound = claim.getApprovalRound();
      }
      else
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;
        approvalRound = claimGroup.getApprovalRound() + 1;
      }
      for ( ApproverOption approverOption : nominationPromotion.getCustomApproverOptions() )
      {
        if ( approvalRound.equals( approverOption.getApprovalLevel() ) )
        {
          for ( ApproverCriteria approverCriteria : approverOption.getApproverCriteria() )
          {
            approvers.add( approverCriteria.getApprovers().iterator().next().getParticipant() );
          }
          break;
        }
      }
    }
    else
    {
      List promotionParticipantApprovers = approvable.getPromotion().getPromotionParticipantApprovers();

      Claim claim = (Claim)approvable;
      Long claimRecipientId = new Long( 0 );
      Set claimItems = claim.getApprovableItems();
      if ( claim.getPromotion().isProductClaimPromotion() )
      {
        claimRecipientId = claim.getSubmitter().getId();
      }
      else
      {
        claimRecipientId = ( (ClaimRecipient)claimItems.iterator().next() ).getRecipient().getId();
      }

      for ( Iterator iter = promotionParticipantApprovers.iterator(); iter.hasNext(); )
      {
        PromotionParticipantApprover approver = (PromotionParticipantApprover)iter.next();
        if ( promotionParticipantApprovers.size() > 1 && approver.getParticipant().getId().equals( claimRecipientId ) )
        {
          continue;
        }
        else
        {
          approvers.add( approver.getParticipant() );
        }
      }
    }

    ClaimApproversValue claimApproversValue = new ClaimApproversValue( approvers );

    // Auto-approve if this is manual approval and the submitter is an approver.
    // This should not be applicable for Nomination Promotion
    String approvalTypeCode = approvable.getPromotion().getApprovalType().getCode();
    if ( !disallowFirstRoundAutoApproval( approvable ) && approvable.getApprovableType().isClaim() && approvalTypeCode.equals( ApprovalType.MANUAL )
        && approvers.contains( ( (Claim)approvable ).getSubmitter() ) && approvable.getPromotion().getCalculator() == null && !approvable.getPromotion().isNominationPromotion() )
    {
      claimApproversValue.setAutoApprove( true );
      claimApproversValue.setAutoApproveUser( ( (Claim)approvable ).getSubmitter() );
    }

    return claimApproversValue;
  }

  private ClaimApproversValue getNominationPromotionCustomApprovers( Approvable approvable )
  {

    Long currentLevel = approvable.getApprovalRound();
    Long submitter = UserManager.getUser().getUserId();
    if ( approvable instanceof NominationClaim )
    {
      submitter = ( (NominationClaim)approvable ).getSubmitter().getId();
    }
    String option = promotionService.getApproverTypeByLevel( currentLevel, approvable.getPromotion().getId() );

    if ( option.equals( CustomApproverType.SPECIFIC_APPROVERS ) )
    {
      return getSpecificApprovers( approvable );
    }

    Set<Participant> approvers = new HashSet<Participant>();
    String behavior = "";

    NominationPromotion promo = (NominationPromotion)approvable.getPromotion();
    if ( option != null && option.equals( CustomApproverType.BEHAVIOR ) )
    {
      NominationClaim claim = (NominationClaim)approvable;
      for ( Iterator<NominationClaimBehaviors> iter = claim.getNominationClaimBehaviors().iterator(); iter.hasNext(); )
      {
        NominationClaimBehaviors claimBehavior = iter.next();
        behavior = behavior + claimBehavior.getBehavior().getCode();
        if ( iter.hasNext() )
        {
          behavior += ",";
        }
      }
    }

    String userIds = "";
    String awardQuantity = "";

    NominationPromotion nominationPromotion = (NominationPromotion)approvable.getPromotion();

    NominationClaim claim = (NominationClaim)approvable;
    for ( Iterator<ClaimRecipient> recipientIter = claim.getClaimRecipients().iterator(); recipientIter.hasNext(); )
    {
      ClaimRecipient recipient = recipientIter.next();
      userIds = userIds + recipient.getRecipient().getId();
      if ( recipientIter.hasNext() )
      {
        userIds += ",";
      }

      if ( option != null && option.equals( CustomApproverType.AWARD ) )
      {
        if ( recipient.getAwardQuantity() != null && recipient.getAwardQuantity() != 0 )
        {
          awardQuantity = awardQuantity + recipient.getAwardQuantity();
        }
        else if ( recipient.getCashAwardQuantity() != null && recipient.getCashAwardQuantity().compareTo( new BigDecimal( 0 ) ) != 0 )
        {
          awardQuantity = awardQuantity + recipient.getCashAwardQuantity();
        }
        if ( recipientIter.hasNext() )
        {
          awardQuantity = awardQuantity + ",";
        }
      }
    }
    Long isTeam = promo.isTeam() ? new Long( 1 ) : new Long( 0 );
    List<CustomApproverListValueBean> result = claimApproverSnapshotDAO.getCustomApproverList( promo.getId(), currentLevel, userIds, behavior, awardQuantity, submitter, isTeam );
    ClaimApproversValue claimApproversValue = null;
    if ( result != null && result.size() > 0 )
    {
      for ( Iterator<CustomApproverListValueBean> participantIter = result.iterator(); participantIter.hasNext(); )
      {
        CustomApproverListValueBean bean = participantIter.next();
        approvers.add( participantService.getParticipantById( bean.getUserId() ) );
      }
      claimApproversValue = new ClaimApproversValue( approvers );
    }
    else
    {
      // set default approver if custom approver procedure result set is empty
      approvers.add( nominationPromotion.getDefaultApprover() );
      claimApproversValue = new ClaimApproversValue( approvers );
    }

    return claimApproversValue;
  }
  
  /**
   * Returns true if auto auto approval not allowed during the first approval round for this
   * approvable.
   */
  private boolean disallowFirstRoundAutoApproval( Approvable approvable )
  {
    boolean disallow = false;

    if ( approvable.getPromotion().isNominationPromotion() && ( approvable.getApprovalRound() == null || approvable.getApprovalRound().longValue() == 1 ) )
    {
      disallow = true;
    }

    return disallow;
  }

  /**
   * Returns the users are the submitter's manager. Rule 1) If no manager exists, use the owner. if
   * no owner exists, use the parent node's owner until an owner is found. If none is found,
   * auto-approve with no approver.<br/> Rule 2), if the submitter is one of the managers, follow
   * Rule 1 as if there were no managers (i.e. use the node owner.<br/> Rule 3) if no manager
   * exists and the submitter is one of the owners, ignore that owner and proceed to the parent
   * node's owner.
   * 
   * @return the users who must approve this claim, as a <code>Set</code> of {@link User} objects.
   */
  private ClaimApproversValue getSubmitterManagers( Claim claim )
  {
    Set approverUsers = new HashSet();

    Node approvalNode = claim.getSubmittersNode();

    approverUsers = approvalNode.getNodeManagersForUser( claim.getSubmitter() );

    Long userId = ( (User)approverUsers.iterator().next() ).getId();
    Long claimRecipientId = new Long( 0 );

    Set claimItems = claim.getApprovableItems();

    if ( claim.getPromotion().isProductClaimPromotion() )
    {
      claimRecipientId = claim.getSubmitter().getId();
    }
    else
    {

      Iterator iterator = claimItems.iterator();

      claimRecipientId = ( (ClaimRecipient)iterator.next() ).getRecipient().getId();
    }

    if ( claimRecipientId != null && claimRecipientId.equals( userId ) )
    {
      approverUsers = approvalNode.getNodeManagersForUser( (User)approverUsers.iterator().next() );
    }

    ClaimApproversValue claimApproversValue = new ClaimApproversValue( approverUsers );
    claimApproversValue.setApproverUsers( approverUsers );

    if ( approverUsers.isEmpty() )
    {
      claimApproversValue.setAutoApprove( true );
    }
    // TODO: pull approvalNode.getNodeManagersForUser( claim.getSubmitter() ) to here so we can get
    // actual
    // node where approver came from. Find if if the other callers to getNodeManagersForUser() need
    // really
    // use the same node managers (looking to owners, parents, etc).
    // else
    // {
    // claimApproversValue.setSourceNode(approverNode);
    // }
    return claimApproversValue;
  }

  /**
   * Process / send submitted claim notifications to approver
   * 
   * @param claimFormStepId
   */
  private void processSubmittedClaimNotifications( Approvable approvable, Long claimFormStepId, List unnotifiedApprovers )
  {
    Claim claim;
    if ( approvable instanceof Claim )
    {
      claim = (Claim)approvable;

    }
    else
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      // Just use first claim, since this notification doesn't use
      // anything unique to each claim.
      claim = (Claim)claimGroup.getClaims().iterator().next();
    }

    emailNotificationService.processSubmittedClaimNotifications( claim, claimFormStepId, unnotifiedApprovers, true );
  }

  
  /** /**
   * Returns the users who must approve this claim when the approvers are users associated with the
   * promotion in approver matrix.
   * 
   * @return the users who must approve this claim, as a <code>Set</code> of {@link User} objects.
   */
  public ClaimApproversValue getNominationCustomAndSpecificApprovers( Approvable approvable )
  {

	    // Client customizations for WIP #42701 starts
	    Claim claim = ( (Claim)approvable );
	    NominationPromotion nominationPromotion = null;
	    if(claim.getPromotion().isNominationPromotion())
	    {
	      nominationPromotion = (NominationPromotion)claim.getPromotion();
	    }
	    else
	    { 
	      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)claim.getPromotion();
	    }
	    
	    // Client customization for WIP 58122
	    if ( ((nominationPromotion!=null && (nominationPromotion.isLevelSelectionByApprover()||nominationPromotion.isLevelPayoutByApproverAvailable())) ||
	    		nominationPromotion!=null && claim.getPromotion().getAdihCashOption()) && ApprovalType.COKE_CUSTOM.equals( approvable.getPromotion().getApprovalType().getCode() ) ) // Client   
	    {

	      Set<Participant> approvers = new HashSet<Participant>();
	      AbstractRecognitionClaim recognitionClaim = (AbstractRecognitionClaim)claim;
	      if ( approvable.getApprovalRound().longValue() == 1 ) // Round is #1
	      {
	        Long userId = claim.getSubmitter().getId();
            String divisionNumber = null;
            if ( claim.getPromotion().getAdihCashOption() )
            {
              divisionNumber = ( recognitionClaim.getClaimRecipients().iterator().next() ).getCashPaxDivisionNumber();
            }
            else
            {
              divisionNumber = getUserService().getUserDivisionKeyCharValue( userId );
            }
	        
	        String approverUserName = cokeClientDAO.getApproverUserName( claim.getPromotion().getId(), divisionNumber );
	        // If approver matrix is null for the division key, then add the specific approver
	        if ( !StringUtil.isNullOrEmpty( approverUserName ) )
	        {
	          approvers.add( participantService.getParticipantByUserName( approverUserName ) );
	        }
	        else
	        {
	          for ( Iterator iter = approvable.getPromotion().getPromotionParticipantApprovers().iterator(); iter.hasNext(); )
	          {
	            PromotionParticipantApprover approver = (PromotionParticipantApprover)iter.next();
	            approvers.add( approver.getParticipant() );
	          }
	        }

	      }
	      // Client customization for WIP 58122
	      else if ( approvable.getApprovalRound().longValue() == 2 && nominationPromotion.isLevelPayoutByApproverAvailable()) // Round is #2
	      {
	       // NominationClaim nominationClaim = (NominationClaim)claim;
	        	Long userId = claim.getSubmitter().getId();
	        	String divisionNumber = userCharacteristicService.getCharacteristicValueByDivisionKeyAndUserId( userId );
	            String approverUserName = cokeClientDAO.getApproverUserNameForLevel( claim.getPromotion().getId(), divisionNumber,approvable.getApprovalRound().longValue() ); // If approver matrix is null for the division key, then add the specific approver
	          if ( !StringUtil.isNullOrEmpty( approverUserName ) )
	          {
	            approvers.add( participantService.getParticipantByUserName( approverUserName ) );
	          }
	          else
	          {
	            for ( Iterator iter = approvable.getPromotion().getPromotionParticipantApprovers().iterator(); iter.hasNext(); )
	            {
	              PromotionParticipantApprover approver = (PromotionParticipantApprover)iter.next();
	              approvers.add( approver.getParticipant() );
	            }
	          }
	        
	      }
	      else if ( approvable.getApprovalRound().longValue() == 2 ) // Round is #2
	      {
	        NominationClaim nominationClaim = (NominationClaim)claim;
	        for ( Iterator recipients = nominationClaim.getTeamMembers().iterator(); recipients.hasNext(); )
	        {
	          ProductClaimParticipant productClaimParticipant = (ProductClaimParticipant)recipients.next();
	          // String divisionNumber =
	          // userCharacteristicService.getCharacteristicValueByDivisionKeyAndUserId( userId );
	          String divisionNumber = userCharacteristicService.getCharacteristicValueByDivisionKeyAndUserId( productClaimParticipant.getParticipant().getId() );
	          String approverUserName = cokeClientDAO.getApproverUserName( claim.getPromotion().getId(), divisionNumber );
	          // If approver matrix is null for the division key, then add the specific approver
	          if ( !StringUtil.isNullOrEmpty( approverUserName ) )
	          {
	            approvers.add( participantService.getParticipantByUserName( approverUserName ) );
	          }
	          else
	          {
	            for ( Iterator iter = approvable.getPromotion().getPromotionParticipantApprovers().iterator(); iter.hasNext(); )
	            {
	              PromotionParticipantApprover approver = (PromotionParticipantApprover)iter.next();
	              approvers.add( approver.getParticipant() );
	            }
	          }
	        }
	      }
	      else if ( approvable.getApprovalRound().longValue() == 3 ) // Round is #3
	      {
	        /*
	         * //------------------- // Approval Round 3 //------------------ //find recipient's primary
	         * Node Participant recipient =
	         * (recognitionClaim.getClaimRecipients().iterator().next()).getRecipient(); Node
	         * recipientNode = recipient.getPrimaryUserNode().getNode(); if (recipientNode!=null) {
	         * logger.debug( "NodeId=" + recipientNode.getId() + " for claim recipient userId:" +
	         * recipient.getId() + " claimId=" + approvable.getId() ); //Get Recipient's Manager Set
	         * recipientMgrs = new HashSet(); recipientMgrs = recipientNode.getNodeManagersForUser(
	         * recipient ); Iterator recipientMgrsIter = recipientMgrs.iterator(); if (
	         * recipientMgrsIter.hasNext() ) { //Use first manager from the list
	         * approvers.add((Participant)recipientMgrsIter.next());
	         * logger.debug("Using Recipeints Mgr for round 3 for claim recipient userId:" +
	         * recipient.getId() + " claimId=" + approvable.getId() ); } else { //Use Default Approver
	         * logger.debug("Using Default approver for round 3 for claim recipient userId:" +
	         * recipient.getId() + " claimId=" + approvable.getId() ); for ( Iterator iter =
	         * approvable.getPromotion().getPromotionParticipantApprovers().iterator(); iter.hasNext();
	         * ) { PromotionParticipantApprover approver = (PromotionParticipantApprover)iter.next();
	         * approvers.add( approver.getParticipant() ); } } } else { logger.error(
	         * "No Primary Node found for claim recipient userId:" + recipient.getId() + " claimId=" +
	         * approvable.getId() ); }
	         */
	        // Any additional approval rounds
	        logger.error( "Invalid approval round for claimId:" + approvable.getId() + " approvalRound:" + approvable.getApprovalRound().longValue() );
	      }
	      else
	      {
	        // Any additional approval rounds
	        logger.error( "Invalid approval round for claimId:" + approvable.getId() + " approvalRound:" + approvable.getApprovalRound().longValue() );
	      }
	      return new ClaimApproversValue( approvers );
	      // Client customizations for WIP #56492 ends
	    }
	    else
	    {

	      Set approvers = new HashSet();
	      List promotionParticipantApprovers = approvable.getPromotion().getPromotionParticipantApprovers();
	      Long claimRecipientId = new Long( 0 );
	      Set claimItems = claim.getApprovableItems();
	      if ( claim.getPromotion().isProductClaimPromotion() )
	      {
	        claimRecipientId = claim.getSubmitter().getId();
	      }
	      else if ( claim.getPromotion().isNominationPromotion() )
	      {
	        claimRecipientId = ( (ClaimRecipient)claimItems.iterator().next() ).getId();
	      }
	      else
	      {
	        claimRecipientId = ( (ClaimRecipient)claimItems.iterator().next() ).getRecipient().getId();
	      }
	      for ( Iterator iter = promotionParticipantApprovers.iterator(); iter.hasNext(); )
	      {
	        PromotionParticipantApprover approver = (PromotionParticipantApprover)iter.next();
	        if ( promotionParticipantApprovers.size() > 1 && approver.getParticipant().equals( claimRecipientId ) )
	        {
	          continue;
	        }
	        else
	        {
	          approvers.add( approver.getParticipant() );
	        }
	      }
	      ClaimApproversValue claimApproversValue = new ClaimApproversValue( approvers );
	      // Auto-approve if this is manual approval and the submitter is an approver.
	      String approvalTypeCode = approvable.getPromotion().getApprovalType().getCode();
	      if ( !disallowFirstRoundAutoApproval( approvable ) && ApprovableTypeEnum.CLAIM.equals( approvable.getApprovableType() ) && approvalTypeCode.equals( ApprovalType.MANUAL )
	          && approvers.contains( ( (Claim)approvable ).getSubmitter() ) && approvable.getPromotion().getCalculator() == null )
	      {
	        claimApproversValue.setAutoApprove( true );
	        claimApproversValue.setAutoApproveUser( ( (Claim)approvable ).getSubmitter() );
	      }
	      return claimApproversValue;

	    }

	    
	  
  }
  
  /** @param emailNotificationService value for emailNotificationService property
   */
  public void setEmailNotificationService( EmailNotificationService emailNotificationService )
  {
    this.emailNotificationService = emailNotificationService;
  }

  /**
   * @param claimApproverSnapshotDAO value for claimApproverSnapshotDAO property
   */
  public void setClaimApproverSnapshotDAO( ClaimApproverSnapshotDAO claimApproverSnapshotDAO )
  {
    this.claimApproverSnapshotDAO = claimApproverSnapshotDAO;
  }

  /**
   * @param nodeDAO value for nodeDAO property
   */
  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  private void sendAdminNotificationIfDefaultApproverInactive( String promotionName, Participant defaultApprover )
  {
    mailingService.submitSystemMailing( "Default Approver is inactive",
                                        "The default approver ( " + defaultApprover.getLastName() + "," + defaultApprover.getFirstName() + " ) for the promotion " + promotionName + " is inactive",
                                        "The default approver ( " + defaultApprover.getLastName() + "," + defaultApprover.getFirstName() + " ) for the promotion " + promotionName + " is inactive" );

  }

  // Client customizations for WIP #56492 starts
  public UserCharacteristicService getUserCharacteristicService()
  {
    return userCharacteristicService;
  }

  public void setUserCharacteristicService( UserCharacteristicService userCharacteristicService )
  {
    this.userCharacteristicService = userCharacteristicService;
  }
  // Client customizations for WIP #56492 ends

  public void setCokeClientDAO( CokeClientDAO cokeClientDAO )
  {
    this.cokeClientDAO = cokeClientDAO;
  }
  
  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
}
