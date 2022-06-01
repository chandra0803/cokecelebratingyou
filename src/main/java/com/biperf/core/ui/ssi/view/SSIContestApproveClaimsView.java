
package com.biperf.core.ui.ssi.view;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPaxClaimCountValueBean;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * SSIContestApproveClaimsView.
 * 
 * @author patelp
 * @since May 22, 2015
 * @version 1.0
 */

public class SSIContestApproveClaimsView
{
  private String id;
  private String name;
  private String sortedOn;
  private String sortedBy;
  private String contestCreator;
  private String contestId;
  private int claimsSubmittedCount;
  private int claimsPendingCount;
  private int claimsApprovedCount;
  private int claimsDeniedCount;
  @JsonProperty( "canApprove" )
  private boolean canApprove = Boolean.FALSE;

  public SSIContestApproveClaimsView( SSIContest contest,
                                      SSIContestPaxClaimCountValueBean ssiContestPaxClaimCountValueBean,
                                      String contestCreator,
                                      String sortedOn,
                                      String sortedBy,
                                      Boolean canApprove )
  {
    this.id = SSIContestUtil.getClientState( contest.getId() );
    this.name = contest.getContestNameFromCM();
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
    this.contestCreator = contestCreator;
    this.contestId = SSIContestUtil.getClientState( contest.getId() );
    this.claimsSubmittedCount = ssiContestPaxClaimCountValueBean.getClaimsSubmittedCount();
    this.claimsPendingCount = ssiContestPaxClaimCountValueBean.getClaimsWaitingForApprovalCount();
    this.claimsApprovedCount = ssiContestPaxClaimCountValueBean.getClaimsApprovedCount();
    this.claimsDeniedCount = ssiContestPaxClaimCountValueBean.getClaimsDeniedCount();
    this.canApprove = canApprove;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getContestId()
  {
    return contestId;
  }

  public void setContestId( String contestId )
  {
    this.contestId = contestId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getSortedOn()
  {
    return sortedOn;
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

  public String getContestCreator()
  {
    return contestCreator;
  }

  public void setContestCreator( String contestCreator )
  {
    this.contestCreator = contestCreator;
  }

  public int getClaimsSubmittedCount()
  {
    return claimsSubmittedCount;
  }

  public void setClaimsSubmittedCount( int claimsSubmittedCount )
  {
    this.claimsSubmittedCount = claimsSubmittedCount;
  }

  public int getClaimsApprovedCount()
  {
    return claimsApprovedCount;
  }

  public int getClaimsPendingCount()
  {
    return claimsPendingCount;
  }

  public void setClaimsPendingCount( int claimsPendingCount )
  {
    this.claimsPendingCount = claimsPendingCount;
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

  public boolean isCanApprove()
  {
    return canApprove;
  }

  public void setCanApprove( boolean canApprove )
  {
    this.canApprove = canApprove;
  }

}
