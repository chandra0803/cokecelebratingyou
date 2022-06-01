
package com.biperf.core.service.participant.impl;

import java.io.Serializable;
import java.util.UUID;

public class AudienceListValueBean implements Serializable
{
  private Long audienceId;
  private String audienceName;
  private String audienceType;
  private String dateModified;
  private Boolean publicAudience;
  private UUID rosterAudienceId;

  public Long getAudienceId()
  {
    return audienceId;
  }

  public void setAudienceId( Long audienceId )
  {
    this.audienceId = audienceId;
  }

  public String getAudienceName()
  {
    return audienceName;
  }

  public void setAudienceName( String audienceName )
  {
    this.audienceName = audienceName;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public String getDateModified()
  {
    return dateModified;
  }

  public void setDateModified( String dateModified )
  {
    this.dateModified = dateModified;
  }

  public Boolean getPublicAudience()
  {
    return publicAudience;
  }

  public void setPublicAudience( Boolean publicAudience )
  {
    this.publicAudience = publicAudience;
  }

  public UUID getRosterAudienceId()
  {
    return rosterAudienceId;
  }

  public void setRosterAudienceId( UUID rosterAudienceId )
  {
    this.rosterAudienceId = rosterAudienceId;
  }

}
