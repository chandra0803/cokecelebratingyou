
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SSIContestPayoutHistoryAwardThemNowValueBean
{

  private String id;
  private String sortedOn;
  private String sortedBy;
  private String name;
  private String status;
  private String statusDescription;
  private String payoutType;
  private String totalActivity;
  private String totalPayoutAmount;
  private List<SSIContestApprovalLevelsView> approvalLevels = new ArrayList<SSIContestApprovalLevelsView>();
  @JsonProperty( "isApproveMode" )
  private boolean isApproveMode;
  @JsonProperty( "approvalRequired" )
  private boolean approvalRequired;
  @JsonProperty( "approved" )
  private boolean approved;
  private String backButtonUrl;
  @JsonProperty( "canApprove" )
  private boolean canApprove = Boolean.FALSE;

  public boolean isApproved()
  {
    return approved;
  }

  public void setApproved( boolean approved )
  {
    this.approved = approved;
  }

  public String getApprovedBy()
  {
    return approvedBy;
  }

  public void setApprovedBy( String approvedBy )
  {
    this.approvedBy = approvedBy;
  }

  private String approvedBy;

  public String getId()
  {
    return id;
  }

  public boolean getApprovalRequired()
  {
    return approvalRequired;
  }

  public void setApprovalRequired( boolean approvalRequired )
  {
    this.approvalRequired = approvalRequired;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public boolean isApproveMode()
  {
    return isApproveMode;
  }

  public void setApproveMode( boolean approveMode )
  {
    this.isApproveMode = approveMode;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getTotalActivity()
  {
    return totalActivity;
  }

  public String getStatusDescription()
  {
    return statusDescription;
  }

  public void setStatusDescription( String statusDescription )
  {
    this.statusDescription = statusDescription;
  }

  public void setTotalActivity( String totalActivity )
  {
    this.totalActivity = totalActivity;
  }

  public String getTotalPayoutAmount()
  {
    return totalPayoutAmount;
  }

  public void setTotalPayoutAmount( String totalPayoutAmount )
  {
    this.totalPayoutAmount = totalPayoutAmount;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public List<SSIContestApprovalLevelsView> getApprovalLevels()
  {
    return approvalLevels;
  }

  public void setApprovalLevels( List<SSIContestApprovalLevelsView> approvalLevels )
  {
    this.approvalLevels = approvalLevels;
  }

  public String getBackButtonUrl()
  {
    return backButtonUrl;
  }

  public void setBackButtonUrl( String string )
  {
    this.backButtonUrl = string;
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
