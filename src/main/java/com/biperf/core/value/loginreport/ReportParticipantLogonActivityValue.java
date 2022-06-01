/**
 * 
 */

package com.biperf.core.value.loginreport;

import java.util.Date;

/**
 * @author poddutur
 *
 */
public class ReportParticipantLogonActivityValue
{

  private String participant;
  private String organization;
  private String department;
  private String position;
  private String country;
  // private String loginDate;
  private Date loginDate;

  public ReportParticipantLogonActivityValue()
  {
    super();
  }

  public ReportParticipantLogonActivityValue( String participant, String department, String position, String country, String organization, Date loginDate )
  {
    super();
    this.participant = participant;
    this.organization = organization;
    this.department = department;
    this.position = position;
    this.country = country;
    this.loginDate = loginDate;
  }

  public String getParticipant()
  {
    return participant;
  }

  public void setParticipant( String participant )
  {
    this.participant = participant;
  }

  public String getOrganization()
  {
    return organization;
  }

  public void setOrganization( String organization )
  {
    this.organization = organization;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public Date getLoginDate()
  {
    return loginDate;
  }

  public void setLoginDate( Date loginDate )
  {
    this.loginDate = loginDate;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

}
