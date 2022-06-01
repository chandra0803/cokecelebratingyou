
package com.biperf.core.ui.recognition.state;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class PurlContributorBean implements Serializable
{
  private String id;
  private String countryCode;
  private String countryName;
  private String firstName;
  private String lastName;
  private String email;
  private String orgName;
  private String departmentName;
  private String jobName;
  private String contribType;
  private String sourceType;
  private String invitationSentDate;

  public String getContribType()
  {
    return contribType;
  }

  public void setContribType( String contribType )
  {
    this.contribType = contribType;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public void setEmail( String email )
  {
    this.email = email;
  }

  public String getEmail()
  {
    return email;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getInvitationSentDate()
  {
    return invitationSentDate;
  }

  public void setInvitationSentDate( String invitationSentDate )
  {
    this.invitationSentDate = invitationSentDate;
  }

  public String getSourceType()
  {
    return sourceType;
  }

  public void setSourceType( String sourceType )
  {
    this.sourceType = sourceType;
  }
}
