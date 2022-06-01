/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import java.util.Set;

import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.Promotion;

/**
 * Approvables hold one or more ApprovableItem objects. Each ApprovableItem holds an approval status.
 * Several Rounds of approval of the ApprovableItem objects may occur.
 * The approvable is not itself approved, rather it is closed after each of it's approvable items
 * are non-pending and all rounds of approval are complete (QuizClaim is an exception - in Quizzes,
 * no approvals happen instead the claim is closed when the quiz is finished).
 * <br/><br/>
 * Implementing Classes:<br/>
 * {@link com.biperf.core.domain.claim.Claim Claim}
 * {@link com.biperf.core.domain.claim.ClaimGroup ClaimGroup}
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
 * <td>Apr 19, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface Approvable
{

  public Long getId();

  /**
   * Sets the open status of this claim. If a claim has not been processed, then it is open;
   * otherwise it is not open.
   * 
   * @param open true if this claim has not been processed; returns false otherwise.
   */
  public void setOpen( boolean open );

  /**
   * Returns true if this claim has not been processed; returns false otherwise.
   * 
   * @return true if this claim has not been processed; returns false otherwise.
   */
  public boolean isOpen();

  /**
   * Returns the approver's comments.
   * 
   * @return the approver's comments.
   */
  public String getApproverComments();

  /**
   * Sets approver's comments.
   * 
   * @param approverComments sets approver's comments.
   */
  public void setApproverComments( String approverComments );

  public int getNodeLevelsRemaining();

  /**
   * @return value of approvalRound property
   */
  public Long getApprovalRound();

  /**
   * @param approvalRound value for approvalRound property
   */
  public void setApprovalRound( Long approvalRound );

  /**
   * @return value of lastApprovalNode property
   */
  public Node getLastApprovalNode();

  /**
   * @param lastApprovalNode value for lastApprovalNode property
   */
  public void setLastApprovalNode( Node lastApprovalNode );

  public Promotion getPromotion();

  public void setPromotion( Promotion promotion );

  public ApprovableTypeEnum getApprovableType();

  public Set getApprovableItems();

  /**
   * Internal only unique string representation of the approvable 
   */
  public String getApprovableUid();

  /**
   * Return the version of the domain obj.
   * 
   * @return Long version
   */
  public Long getVersion();

  /**
   * Set the version of the domain.
   * 
   * @param version
   */
  public void setVersion( Long version );
}
