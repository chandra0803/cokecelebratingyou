/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.claim;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ClaimApproversValue;

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
public interface ClaimApproverSnapshotService extends SAO
{
  public static final String BEAN_NAME = "claimApproverSnapshotService";

  public ClaimApproverSnapshot getSnapshot( Long approverUserId, Long approvableId, ApprovableTypeEnum approvableType );

  public void updateClaimApproverSnapshot( Approvable approvable, Long optionalClaimFormStepId, boolean isNewApprovable );

  public void updateClaimApproverSnapshot( Approvable approvable );
  /**
   * Returns the users who must approve this claim.
   * 
   * @return a ClaimApproversValue object which hold the users who must approve this claim, as a
   *         <code>Set</code> of {@link User} objects. If autoApprove is true, then the
   *         getApprovers calculation revealed that the approval round should be auto-approved. No
   *         approvers will be returned, but an approverUserId may be set (depending on the approver
   *         type) which can be set as the approver for the round.
   */
  public ClaimApproversValue getApprovers( Approvable approvable );
 
  public void deleteClaimApproverSnapshot( Approvable approvable );

}
