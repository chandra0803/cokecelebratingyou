
package com.biperf.core.value.ssi;

import java.sql.Date;

/**
 * 
 * SSIContestPaxClaimValueBean.
 * 
 * @author patelP
 * @since June 16, 2015
 * @version 1.0
 */

public class SSIContestPaxClaimValueBean
{
  private Long id;
  private Long contestId;
  private Long submitterId;
  private String claimNumber;
  private String status;
  private String statusDescription;
  private Date submissionDate;
  private String approvedBy;
  private String submittedBy;
  private String activityDescription;
  private int claimCount;

  public Long getId()
  {
    return id;
  }

  public int getClaimCount()
  {
    return claimCount;
  }

  public void setClaimCount( int claimCount )
  {
    this.claimCount = claimCount;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public String getApprovedBy()
  {
    return approvedBy;
  }

  public void setApprovedBy( String approvedBy )
  {
    this.approvedBy = approvedBy;
  }

  public String getSubmittedBy()
  {
    return submittedBy;
  }

  public void setSubmittedBy( String submittedBy )
  {
    this.submittedBy = submittedBy;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getStatusDescription()
  {
    return statusDescription;
  }

  public void setStatusDescription( String statusDescription )
  {
    this.statusDescription = statusDescription;
  }
}
