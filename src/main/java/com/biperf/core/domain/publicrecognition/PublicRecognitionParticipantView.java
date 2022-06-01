
package com.biperf.core.domain.publicrecognition;

import com.biperf.core.domain.participant.Participant;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author dudam
 * @since Dec 11, 2012
 * @version 1.0
 */
public class PublicRecognitionParticipantView
{
  private Long id;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String avatarSmall;

  private Long teamId;
  private Long claimId;

  private String jobName;
  private String departmentName;
  private String orgName;

  @JsonIgnore
  private boolean optOutAwards;

  public PublicRecognitionParticipantView()
  {

  }

  public PublicRecognitionParticipantView( Participant participant )
  {
    this.id = participant.getId();
    this.firstName = participant.getFirstName();
    this.lastName = participant.getLastName();
    this.avatarUrl = participant.getAvatarSmallFullPath();
    this.optOutAwards = participant.getOptOutAwards();
  }

  public PublicRecognitionParticipantView( Long id, String firstName, String lastName, String avatarUrl )
  {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( id == null ? 0 : id.hashCode() );
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
    PublicRecognitionParticipantView other = (PublicRecognitionParticipantView)obj;
    if ( id == null )
    {
      if ( other.id != null )
      {
        return false;
      }
    }
    else if ( !id.equals( other.id ) )
    {
      return false;
    }
    return true;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getAvatarUrl()
  {
    if ( avatarUrl != null )
    {
      return avatarUrl;
    }
    else
    {
      return avatarSmall;
    }
  }

  public String getAvatarSmall()
  {
    return avatarSmall;
  }

  public void setAvatarSmall( String avatarSmall )
  {
    this.avatarSmall = avatarSmall;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

}
