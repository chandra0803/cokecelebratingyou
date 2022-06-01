/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimGroupQueryConstraint;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * ClaimDAO will handle processing a claimForm submission.
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
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ClaimGroupDAO extends DAO
{

  /**
   * Bean name
   */
  public static final String BEAN_NAME = "claimGroupDAO";

  /**
   * Returns the claim groups specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned claim groups.
   * @return the specified claim groups, as a <code>List</code> of {@link ClaimGroup}
   */
  public List getClaimGroupList( JournalClaimGroupQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns a list of claimGroups that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimGroupQueryConstraint
   * @return List the claimGroup list
   */
  public List getClaimGroupList( ClaimGroupQueryConstraint claimGroupQueryConstraint );

  /**
   * Returns a count of claimGroups that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimGroupQueryConstraint
   * @return List the claimGroup list
   */
  public int getClaimGroupListCount( ClaimGroupQueryConstraint claimGroupQueryConstraint );

  /**
   * Get claimGroup by claimGroupId
   * 
   * @return Claim
   */
  public ClaimGroup getClaimGroupById( Long claimGroupId );

  /**
   * Get claimGroup by claimGroupId
   * 
   * @param claimGroupId
   * @param associationRequestCollection
   * @return claimGroup
   */
  public ClaimGroup getClaimGroupByIdWithAssociations( Long claimGroupId, AssociationRequestCollection associationRequestCollection );

  /**
   * Update an existing claimGroup
   * 
   * @param claimGroup
   * @return claimGroup
   */
  public ClaimGroup saveClaimGroup( ClaimGroup claimGroup );

  public Journal getJournalForClaimGroup( Long claimGroupId );

  // /**
  // * Get each open claimGroup of the specified approverType whose submitters node name is not
  // found in
  // * the claimGroup promotion's approval hierarchy.
  // *
  // * @param approverType
  // */
  // public List getOpenClaimGroupsWithNoMatchingNodeInApproverHierarchy( ApproverType approverType
  // );

  // /**
  // * Return a count of journal transactions for the recipient and claimGroup with a status of
  // approve or
  // * post
  // *
  // * @param recipientId
  // * @param claimGroupId
  // * @return int
  // */
  // public int getNonPostedJournalCount( Long recipientId, Long claimGroupId );
}
