/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
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
public abstract class ApprovableItemApprover extends BaseDomain
{
  private User approverUser;
  private Date dateApproved;
  private ApprovableItem approvableItem;
  private Long approvalRound;
  private ApprovalStatusType approvalStatusType;
  private PromotionApprovalOptionReasonType promotionApprovalOptionReasonType;
  private Date notificationDate;
  private Long timePeriodId;
  private String approverComments = "";

  /**
   * @return value of approverUser property
   */
  public User getApproverUser()
  {
    return approverUser;
  }

  /**
   * @param approverUser value for approverUser property
   */
  public void setApproverUser( User approverUser )
  {
    this.approverUser = approverUser;
  }

  /**
   * @return value of claimItem property
   */
  public ApprovableItem getApprovableItem()
  {
    return approvableItem;
  }

  /**
   * @param approvableItem value for claimItem property
   */
  public void setApprovableItem( ApprovableItem approvableItem )
  {
    this.approvableItem = approvableItem;
  }

  /**
   * @return value of dateApproved property
   */
  public Date getDateApproved()
  {
    return dateApproved;
  }

  /**
   * @param dateApproved value for dateApproved property
   */
  public void setDateApproved( Date dateApproved )
  {
    this.dateApproved = dateApproved;
  }

  /**
   * @return value of approvalRound property
   */
  public Long getApprovalRound()
  {
    return approvalRound;
  }

  /**
   * @param approvalRound value for approvalRound property
   */
  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }

  /**
   * @return value of approvalStatusType property
   */
  public ApprovalStatusType getApprovalStatusType()
  {
    return approvalStatusType;
  }

  /**
   * @param approvalStatusType value for approvalStatusType property
   */
  public void setApprovalStatusType( ApprovalStatusType approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  /**
   * @return value of promotionApprovalOptionReasonType property
   */
  public PromotionApprovalOptionReasonType getPromotionApprovalOptionReasonType()
  {
    return promotionApprovalOptionReasonType;
  }

  /**
   * @param promotionApprovalOptionReasonType value for promotionApprovalOptionReasonType property
   */
  public void setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType promotionApprovalOptionReasonType )
  {
    this.promotionApprovalOptionReasonType = promotionApprovalOptionReasonType;
  }

  public Date getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( Date notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public Long getTimePeriodId()
  {
    return timePeriodId;
  }

  public void setTimePeriodId( Long timePeriodId )
  {
    this.timePeriodId = timePeriodId;
  }

  public String getApproverComments()
  {
    return approverComments;
  }

  public void setApproverComments( String approverComments )
  {
    this.approverComments = approverComments;
  }
}
