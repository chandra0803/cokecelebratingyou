
package com.biperf.core.domain.ssi;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SSIClaimStatus;
import com.biperf.core.utils.DateUtils;

/**
 * 
 * @author Ravi Kancherla
 * @since May 19, 2015
 * @version 1.0
 */

public class SSIContestPaxClaim extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private Long contestId;
  private Long submitterId;
  private String claimNumber;
  private SSIClaimStatus status;
  private Date submissionDate;
  private String deniedReason;
  private String approvedBy;
  private String submittedBy;
  private Long approverId;
  private Date approveDenyDate;
  private Date dateResultsPosted;
  private double claimAmountQuantity;
  private String activitiesAmountQuantity;
  private Set<SSIContestPaxClaimField> paxClaimFields = new LinkedHashSet<SSIContestPaxClaimField>();
  private List<String> activities = new ArrayList<String>();

  public void addPaxClaimField( SSIContestPaxClaimField paxClaimField )
  {
    paxClaimField.setPaxClaim( this );
    this.paxClaimFields.add( paxClaimField );
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

  public SSIClaimStatus getStatus()
  {
    return status;
  }

  public void setStatus( SSIClaimStatus status )
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

  public String getDisplaySubmissionDate()
  {
    return DateUtils.toDisplayString( this.submissionDate );
  }

  public String getDeniedReason()
  {
    return deniedReason;
  }

  public void setDeniedReason( String deniedReason )
  {
    this.deniedReason = deniedReason;
  }

  public Long getApproverId()
  {
    return approverId;
  }

  public void setApproverId( Long approverId )
  {
    this.approverId = approverId;
  }

  public Date getApproveDenyDate()
  {
    return approveDenyDate;
  }

  public void setApproveDenyDate( Date approveDenyDate )
  {
    this.approveDenyDate = approveDenyDate;
  }

  public String getDisplayApproveDenyDate()
  {
    return DateUtils.toDisplayString( this.approveDenyDate );
  }

  public Date getDateResultsPosted()
  {
    return dateResultsPosted;
  }

  public void setDateResultsPosted( Date dateResultsPosted )
  {
    this.dateResultsPosted = dateResultsPosted;
  }

  public String getApprovedBy()
  {
    return approvedBy;
  }

  public void setApprovedBy( String approvedBy )
  {
    this.approvedBy = approvedBy;
  }

  public double getClaimAmountQuantity()
  {
    return claimAmountQuantity;
  }

  public void setClaimAmountQuantity( double claimAmountQuantity )
  {
    this.claimAmountQuantity = claimAmountQuantity;
  }

  public String getActivitiesAmountQuantity()
  {
    return activitiesAmountQuantity;
  }

  public void setActivitiesAmountQuantity( String activitiesAmountQuantity )
  {
    this.activitiesAmountQuantity = activitiesAmountQuantity;
  }

  public Set<SSIContestPaxClaimField> getPaxClaimFields()
  {
    return paxClaimFields;
  }

  public void setPaxClaimFields( Set<SSIContestPaxClaimField> paxClaimFields )
  {
    this.paxClaimFields = paxClaimFields;
  }

  public List<String> getActivities()
  {
    return activities;
  }

  public void setActivities( List<String> activities )
  {
    this.activities = activities;
  }

  public String getSubmittedBy()
  {
    return submittedBy;
  }

  public void setSubmittedBy( String submittedBy )
  {
    this.submittedBy = submittedBy;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( claimNumber == null ? 0 : claimNumber.hashCode() );
    result = prime * result + ( contestId == null ? 0 : contestId.hashCode() );
    result = prime * result + ( submitterId == null ? 0 : submitterId.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    SSIContestPaxClaim other = (SSIContestPaxClaim)obj;
    if ( claimNumber == null )
    {
      if ( other.claimNumber != null )
      {
        return false;
      }
    }
    else if ( !claimNumber.equals( other.claimNumber ) )
    {
      return false;
    }
    if ( contestId == null )
    {
      if ( other.contestId != null )
      {
        return false;
      }
    }
    else if ( !contestId.equals( other.contestId ) )
    {
      return false;
    }
    if ( submitterId == null )
    {
      if ( other.submitterId != null )
      {
        return false;
      }
    }
    else if ( !submitterId.equals( other.submitterId ) )
    {
      return false;
    }
    return true;
  }

}
