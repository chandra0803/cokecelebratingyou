/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import java.util.Date;

import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.user.User;

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
 * <td>Oct 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class ClaimItem extends ApprovableItem
{
  private Claim claim;

  /**
   * @return value of claim property
   */
  public Claim getClaim()
  {
    return claim;
  }

  /**
   * @param claim value for claim property
   */
  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.ApprovableItem#getApprovable()
   */
  public Approvable getApprovable()
  {
    return getClaim();
  }

  public void addApprover( User approverUser,
                           Date dateApproved,
                           ApprovalStatusType approvalStatusType,
                           String approverComment,
                           PromotionApprovalOptionReasonType promotionApprovalOptionReasonType,
                           Date notificationDate,
                           Long timePeriodId )
  {
    ApprovableItemApprover approvableItemApprover = new ClaimItemApprover();
    approvableItemApprover.setApprovableItem( this );

    addApprover( approverUser, dateApproved, approvalStatusType, approverComment, promotionApprovalOptionReasonType, approvableItemApprover, notificationDate, timePeriodId );
  }
}
