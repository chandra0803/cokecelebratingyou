/**
 * 
 */

package com.biperf.core.ui.approvals;

/**
 * @author poddutur
 *
 */
public class ApprovalsClaimsBean
{
  private String approvalStatusType;
  private String claimProductIdString;
  private String denyPromotionApprovalOptionReasonType;

  public String getApprovalStatusType()
  {
    return approvalStatusType;
  }

  public void setApprovalStatusType( String approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  public String getClaimProductIdString()
  {
    return claimProductIdString;
  }

  public void setClaimProductIdString( String claimProductIdString )
  {
    this.claimProductIdString = claimProductIdString;
  }

  public String getDenyPromotionApprovalOptionReasonType()
  {
    return denyPromotionApprovalOptionReasonType;
  }

  public void setDenyPromotionApprovalOptionReasonType( String denyPromotionApprovalOptionReasonType )
  {
    this.denyPromotionApprovalOptionReasonType = denyPromotionApprovalOptionReasonType;
  }
}
