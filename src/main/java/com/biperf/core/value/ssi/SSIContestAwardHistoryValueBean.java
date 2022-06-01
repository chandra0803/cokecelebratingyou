
package com.biperf.core.value.ssi;

import java.util.Date;

/**
 * 
 * SSIContestAwardHistoryValueBean.
 * 
 * @author kandhi
 * @since Feb 11, 2015
 * @version 1.0
 */
public class SSIContestAwardHistoryValueBean
{
  private Date dateCreated;
  private Double amount;
  private String payoutDescription;
  private Double payoutAmount;
  private String status;
  private String denialReason;
  private int participantsCount;
  private Short issuanceNumber;
  private boolean isPayoutDescriptionSame;
  private Integer approvalLevelActionTaken;
  private Long approvedByLevel1;
  private String clientStateId;
  private boolean canApprove;

  public Long getApprovedByLevel1()
  {
    return approvedByLevel1;
  }

  public void setApprovedByLevel1( Long approvedByLevel1 )
  {
    this.approvedByLevel1 = approvedByLevel1;
  }

  public Date getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( Date dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public Double getAmount()
  {
    return amount;
  }

  public void setAmount( Double amount )
  {
    this.amount = amount;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public Double getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Double payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getDenialReason()
  {
    return denialReason;
  }

  public void setDenialReason( String denialReason )
  {
    this.denialReason = denialReason;
  }

  public int getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( int participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public Short getIssuanceNumber()
  {
    return issuanceNumber;
  }

  public void setIssuanceNumber( Short issuanceNumber )
  {
    this.issuanceNumber = issuanceNumber;
  }

  public boolean isPayoutDescriptionSame()
  {
    return isPayoutDescriptionSame;
  }

  public void setPayoutDescriptionSame( boolean isPayoutDescriptionSame )
  {
    this.isPayoutDescriptionSame = isPayoutDescriptionSame;
  }

  public Integer getApprovalLevelActionTaken()
  {
    return approvalLevelActionTaken;
  }

  public void setApprovalLevelActionTaken( Integer approvalLevelActionTaken )
  {
    this.approvalLevelActionTaken = approvalLevelActionTaken;
  }

  public String getClientStateId()
  {
    return clientStateId;
  }

  public void setClientStateId( String clientStateId )
  {
    this.clientStateId = clientStateId;
  }

  public boolean isCanApprove()
  {
    return canApprove;
  }

  public void setCanApprove( boolean canApprove )
  {
    this.canApprove = canApprove;
  }

}
