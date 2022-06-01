
package com.biperf.core.value.client;

import java.util.Date;

public class CokeCareerMomentsValue
{
  private Long userId;
  private String lastName;
  private String firstName;
  private Date enrollmentDate;
  private Date jobChangeDate;
  private String divisionName;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
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

  public Date getEnrollmentDate()
  {
    return enrollmentDate;
  }

  public void setEnrollmentDate( Date enrollmentDate )
  {
    this.enrollmentDate = enrollmentDate;
  }

  public Date getJobChangeDate()
  {
    return jobChangeDate;
  }

  public void setJobChangeDate( Date jobChangeDate )
  {
    this.jobChangeDate = jobChangeDate;
  }

  public String getDivisionName()
  {
    return divisionName;
  }

  public void setDivisionName( String divisionName )
  {
    this.divisionName = divisionName;
  }

}
