
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * SSIContestClaimDetailView.
 * 
 * @author dudam
 * @since Jun 3, 2015
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestClaimDetailView
{
  private String id;
  private String clientState;
  private String name;
  private String status;
  private String dateApproved;
  private String approvedBy;
  private String dateDenied;
  private String deniedBy;
  private String deniedReason;
  private String dateSubmitted;
  private String submittedBy;
  private String measureType;
  private String backButtonUrl;
  private String contestType;
  private boolean showApproveDeny = Boolean.FALSE;
  private List<SSIContestClaimActivity> activities = new ArrayList<SSIContestClaimActivity>();
  private List<SSIContestClaimFormField> fields = new ArrayList<SSIContestClaimFormField>();

  public class SSIContestClaimActivity
  {

    private String activityDescription;
    private String activityAmount;

    public String getActivityDescription()
    {
      return activityDescription;
    }

    public void setActivityDescription( String activityDescription )
    {
      this.activityDescription = activityDescription;
    }

    public String getActivityAmount()
    {
      return activityAmount;
    }

    public void setActivityAmount( String activityAmount )
    {
      this.activityAmount = activityAmount;
    }

  }

  @JsonInclude( value = Include.NON_NULL )
  public class SSIContestClaimFormField
  {
    private String name;
    private String description;
    private String docURL;

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    public String getDescription()
    {
      return description;
    }

    public void setDescription( String description )
    {
      this.description = description;
    }

    public String getDocURL()
    {
      return docURL;
    }

    public void setDocURL( String docURL )
    {
      this.docURL = docURL;
    }

  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
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

  public String getDateApproved()
  {
    return dateApproved;
  }

  public void setDateApproved( String dateApproved )
  {
    this.dateApproved = dateApproved;
  }

  public String getApprovedBy()
  {
    return approvedBy;
  }

  public void setApprovedBy( String approvedBy )
  {
    this.approvedBy = approvedBy;
  }

  public String getDateDenied()
  {
    return dateDenied;
  }

  public void setDateDenied( String dateDenied )
  {
    this.dateDenied = dateDenied;
  }

  public String getDeniedBy()
  {
    return deniedBy;
  }

  public void setDeniedBy( String deniedBy )
  {
    this.deniedBy = deniedBy;
  }

  public String getDeniedReason()
  {
    return deniedReason;
  }

  public void setDeniedReason( String deniedReason )
  {
    this.deniedReason = deniedReason;
  }

  public String getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( String dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public String getSubmittedBy()
  {
    return submittedBy;
  }

  public void setSubmittedBy( String submittedBy )
  {
    this.submittedBy = submittedBy;
  }

  public List<SSIContestClaimActivity> getActivities()
  {
    return activities;
  }

  public void setActivities( List<SSIContestClaimActivity> activities )
  {
    this.activities = activities;
  }

  public String getBackButtonUrl()
  {
    return backButtonUrl;
  }

  public boolean isShowApproveDeny()
  {
    return showApproveDeny;
  }

  public void setShowApproveDeny( boolean showApproveDeny )
  {
    this.showApproveDeny = showApproveDeny;
  }

  public void setBackButtonUrl( String backButtonUrl )
  {
    this.backButtonUrl = backButtonUrl;
  }

  public List<SSIContestClaimFormField> getFields()
  {
    return fields;
  }

  public void setFields( List<SSIContestClaimFormField> fields )
  {
    this.fields = fields;
  }

  public String getMeasureType()
  {
    return measureType;
  }

  public void setMeasureType( String measureType )
  {
    this.measureType = measureType;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

}
