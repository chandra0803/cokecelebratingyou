
package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApprovalsIndexCollectionView
{

  private String[] messages = {};
  private List<ApprovalsIndexCollectionItem> nominationApprovals = new ArrayList<ApprovalsIndexCollectionItem>();
  private List<ApprovalsIndexCollectionItem> recognitionApprovals = new ArrayList<ApprovalsIndexCollectionItem>();
  private List<ApprovalsIndexCollectionItem> claimApprovals = new ArrayList<ApprovalsIndexCollectionItem>();
  private List<ApprovalsIndexCollectionItem> ssiApprovals = new ArrayList<ApprovalsIndexCollectionItem>();

  // adding pending counts for all modules here
  private long nominationapprovalsCount;
  private long recognitionapprovalsCount;
  private long claimapprovalsCount;
  private long ssiapprovalsCount;
  private long ssiapprovalsPendingCount;

  private String claimUrl;

  public long getSsiapprovalsPendingCount()
  {
    return ssiapprovalsPendingCount;
  }

  public void setSsiapprovalsPendingCount( long pendingApprovalOfSSI )
  {
    this.ssiapprovalsPendingCount = pendingApprovalOfSSI;
  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<ApprovalsIndexCollectionItem> getNominationApprovals()
  {
    return nominationApprovals;
  }

  public void setNominationApprovals( List<ApprovalsIndexCollectionItem> nominationApprovals )
  {
    this.nominationApprovals = nominationApprovals;
  }

  @JsonProperty( "RecognitionApprovals" )
  public List<ApprovalsIndexCollectionItem> getRecognitionApprovals()
  {
    return recognitionApprovals;
  }

  public void setRecognitionApprovals( List<ApprovalsIndexCollectionItem> recognitionApprovals )
  {
    this.recognitionApprovals = recognitionApprovals;
  }

  public List<ApprovalsIndexCollectionItem> getClaimApprovals()
  {
    return claimApprovals;
  }

  public void setClaimApprovals( List<ApprovalsIndexCollectionItem> claimApprovals )
  {
    this.claimApprovals = claimApprovals;
  }

  public List<ApprovalsIndexCollectionItem> getSsiApprovals()
  {
    return ssiApprovals;
  }

  public void setSsiApprovals( List<ApprovalsIndexCollectionItem> ssiApprovals )
  {
    this.ssiApprovals = ssiApprovals;
  }

  public long getNominationapprovalsCount()
  {
    return nominationapprovalsCount;
  }

  public void setNominationapprovalsCount( long nominationapprovalsCount )
  {
    this.nominationapprovalsCount = nominationapprovalsCount;
  }

  public long getRecognitionapprovalsCount()
  {
    return recognitionapprovalsCount;
  }

  public void setRecognitionapprovalsCount( long recognitionapprovalsCount )
  {
    this.recognitionapprovalsCount = recognitionapprovalsCount;
  }

  public long getClaimapprovalsCount()
  {
    return claimapprovalsCount;
  }

  public void setClaimapprovalsCount( long claimapprovalsCount )
  {
    this.claimapprovalsCount = claimapprovalsCount;
  }

  public long getSsiapprovalsCount()
  {
    return ssiapprovalsCount;
  }

  public void setSsiapprovalsCount( long ssiapprovalsCount )
  {
    this.ssiapprovalsCount = ssiapprovalsCount;
  }

  public String getClaimUrl()
  {
    return claimUrl;
  }

  public void setClaimUrl( String claimUrl )
  {
    this.claimUrl = claimUrl;
  }

}
