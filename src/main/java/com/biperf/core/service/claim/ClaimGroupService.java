/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/ClaimGroupService.java,v $
 */

package com.biperf.core.service.claim;

import java.util.List;

import com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimGroupQueryConstraint;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.PromotionApprovableValue;

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
 * <td>Adam</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ClaimGroupService extends SAO
{
  /** BEAN_NAME for referencing in tests and spring config files. */
  public final String BEAN_NAME = "claimGroupService";

  /**
   * Update an existing claimGroup
   * 
   * @param claimGroup
   * @return claimGroup
   */
  public ClaimGroup saveClaimGroup( ClaimGroup claimGroup );

  /**
   * Get the given ClaimGroup.
   * 
   * @param claimGroupId
   * @return ClaimGroup
   */
  public ClaimGroup getClaimGroupById( Long claimGroupId );

  /**
   * Get the given ClaimGroup.
   * 
   * @param claimGroupId
   * @param associationRequestCollection
   * @return ClaimGroup
   */
  public ClaimGroup getClaimGroupByIdWithAssociations( Long claimGroupId, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns the claim groups specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned claim groups.
   * @return the specified claim groups, as a <code>List</code> of {@link ClaimGroup} objects.
   */
  public List getClaimGroupList( JournalClaimGroupQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns a list of claimGroups that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   * 
   * @param claimGroupQueryConstraint
   * @return List the claimGroup list
   */
  public List getClaimGroupList( ClaimGroupQueryConstraint claimGroupQueryConstraint );

  /**
   * Returns a count of claimGroups that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   * 
   * @param claimGroupQueryConstraint
   * @return int the claimGroup list count
   */
  public int getClaimGroupListCount( ClaimGroupQueryConstraint claimGroupQueryConstraint );

  // /**
  // * Saves the specific claimGroup.
  // *
  // * @param claimGroup
  // * @throws ServiceErrorException
  // */
  // public void saveClaimGroup( ClaimGroup claimGroup, User approverUser, boolean forceAutoApprove
  // ) throws ServiceErrorException;
  //
  // /**
  // * Saves the specific claimGroup and sends submitter and approver notifications (if applicable).
  // *
  // * @param claimGroup the claimGroup to save.
  // * @param claimGroupFormStepId required if notifications are to be sent
  // * @throws ServiceErrorException if claimGroup is invalid.
  // */
  // public void saveClaimGroup( ClaimGroup claimGroup,
  // Long claimGroupFormStepId,
  // User approverUser,
  // boolean forceAutoApprove ) throws ServiceErrorException;

  /**
   * Get all claimGroups that the specified approverUserId can approve.
   * @param approverUserId
   * @param includedPromotionIds
   * @param isOpen
   * @param claimGroupAssociationRequestCollection
   * @param expired TODO
   * @return List of PromotionclaimGroupsValue object,
   */
  public List getClaimGroupsForApprovalByUser( Long approverUserId,
                                               Long[] includedPromotionIds,
                                               Boolean isOpen,
                                               PromotionType promotionType,
                                               AssociationRequestCollection claimGroupAssociationRequestCollection,
                                               AssociationRequestCollection promotionAssociationRequestCollection,
                                               Boolean expired,
                                               Boolean skipOpenForTile );

  /**
   * Get pending claimGroup approval count by user id
   * 
   * @param userId
   * @param promotionType
   * @return number of pending claimGroup approvals
   */
  public int getClaimGroupsForApprovalByUserCount( Long userId, PromotionType promotionType );

  public int getNominationClaimGroupsForApprovalByUserCount( Long userId, PromotionType promotionType );

  /**
   * Get each open claimGroup of the specified approverType whose submitters node name is not found
   * in the claimGroup promotion's approval hierarchy.
   * 
   * @param approverType
   */
  public List getOpenClaimGroupsWithNoMatchingNodeInApproverHierarchy( ApproverType approverType );

  /**
   * Return false if the recipient has any journal transactions for the claimGroup with status of
   * approve or post
   * 
   * @param nomineeId
   * @param claimGroupId
   * @return boolean
   */
  public boolean hasPendingJournalForClaimGroup( Long nomineeId, Long claimGroupId );

  public int getUserInClaimGroupsApprovalAudienceCount( Long approverUserId, PromotionType promotionType );

  public int getUserNominationInClaimGroupsApprovalAudienceCount( Long approverUserId, PromotionType promotionType );

  public List<PromotionApprovableValue> getNominationClaimGroupsForApprovalByUser( Long approverUserId,
                                                                                   String claimIds,
                                                                                   Long[] includedPromotionIds,
                                                                                   Boolean isOpen,
                                                                                   PromotionType promotionType,
                                                                                   AssociationRequestCollection claimGroupAssociationRequestCollection,
                                                                                   AssociationRequestCollection promotionAssociationRequestCollection,
                                                                                   Boolean expired,
                                                                                   String filterApprovalStatusCode,
                                                                                   Long approvalRound );

  /**
   * Obtain the Journal for the given claim group. May be null.  Will not return the reverse journal entry.
   */
  public Journal getJournalForClaimGroup( Long claimGroupId );

}
