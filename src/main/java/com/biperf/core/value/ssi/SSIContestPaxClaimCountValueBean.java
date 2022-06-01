
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestPaxClaimCountValueBean.
 * 
 * @author patelP
 * @since May 25, 2015
 * @version 1.0
 */

public class SSIContestPaxClaimCountValueBean
{

  private int claimsSubmittedCount;
  private int claimsWaitingForApprovalCount;
  private int claimsApprovedCount;
  private int claimsDeniedCount;

  public SSIContestPaxClaimCountValueBean()
  {
    super();
  }

  public SSIContestPaxClaimCountValueBean( int claimsSubmittedCount, int claimsWaitingForApprovalCount, int claimsApprovedCount, int claimsDeniedCount )
  {
    super();
    this.claimsSubmittedCount = claimsSubmittedCount;
    this.claimsWaitingForApprovalCount = claimsWaitingForApprovalCount;
    this.claimsApprovedCount = claimsApprovedCount;
    this.claimsDeniedCount = claimsDeniedCount;
  }

  public int getClaimsSubmittedCount()
  {
    return claimsSubmittedCount;
  }

  public void setClaimsSubmittedCount( int claimsSubmittedCount )
  {
    this.claimsSubmittedCount = claimsSubmittedCount;
  }

  public int getClaimsWaitingForApprovalCount()
  {
    return claimsWaitingForApprovalCount;
  }

  public void setClaimsWaitingForApprovalCount( int claimsWaitingForApprovalCount )
  {
    this.claimsWaitingForApprovalCount = claimsWaitingForApprovalCount;
  }

  public int getClaimsApprovedCount()
  {
    return claimsApprovedCount;
  }

  public void setClaimsApprovedCount( int claimsApprovedCount )
  {
    this.claimsApprovedCount = claimsApprovedCount;
  }

  public int getClaimsDeniedCount()
  {
    return claimsDeniedCount;
  }

  public void setClaimsDeniedCount( int claimsDeniedCount )
  {
    this.claimsDeniedCount = claimsDeniedCount;
  }

}
