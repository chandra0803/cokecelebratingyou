
package com.biperf.core.value;

import java.io.Serializable;

import com.biperf.core.utils.ImageUtils;

public class PurlContributorInviteValue implements Serializable
{
  public static final String STATUS_SUCCESS = "success";
  public static final String STATUS_EXISTS = "exists";
  public static final String STATUS_FAIL = "fail";

  private String id;
  private String lastName;
  private String firstName;
  private String emailAddr;
  private String status;
  private Long paxId;
  private boolean sendLater = false;
  private String department;
  private String location;
  private Long contributorId;
  private String avatarUrl;
  private String displayAvatarUrl;
  private Long purlRecipientId;
  private String jobName;
  private String orgName;
  private String countryName;
  private String countryCode;
  private Long invitingContributorId;
  private boolean defaultInvitee = false;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public boolean isSendLater()
  {
    return sendLater;
  }

  public void setSendLater( boolean sendLater )
  {
    this.sendLater = sendLater;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getLocation()
  {
    return location;
  }

  public void setLocation( String location )
  {
    this.location = location;
  }

  public Long getContributorId()
  {
    return contributorId;
  }

  public void setContributorId( Long contributorId )
  {
    this.contributorId = contributorId;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public String getDisplayAvatarUrl()
  {
    if ( avatarUrl != null )
    {
      if ( avatarUrl.indexOf( "cm3dam" ) > 0 )
      {
        displayAvatarUrl = avatarUrl;
      }
      else
      {
        displayAvatarUrl = ImageUtils.getFullImageUrlPath( avatarUrl );
      }
    }
    else
    {
      displayAvatarUrl = ImageUtils.getFullImageUrlPath( avatarUrl );
    }

    return displayAvatarUrl;
  }

  public void setDisplayAvatarUrl( String displayAvatarUrl )
  {
    this.displayAvatarUrl = displayAvatarUrl;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public Long getInvitingContributorId()
  {
    return invitingContributorId;
  }

  public void setInvitingContributorId( Long invitingContributorId )
  {
    this.invitingContributorId = invitingContributorId;
  }

  public Boolean isDefaultInvitee()
  {
    return defaultInvitee;
  }

  public void setDefaultInvitee( boolean defaultInvitee )
  {
    this.defaultInvitee = defaultInvitee;
  }

}
