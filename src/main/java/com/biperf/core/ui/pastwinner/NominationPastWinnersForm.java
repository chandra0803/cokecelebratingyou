
package com.biperf.core.ui.pastwinner;

import com.biperf.core.ui.BaseActionForm;

public class NominationPastWinnersForm extends BaseActionForm
{

  private String participantId;
  private String nominationId;
  private String lastName;
  private String firstName;
  private String country;
  private String department;
  private String teamName;
  private String startDate;
  private String endDate;
  private String pageNumber;
  private String resultsPerPage;
  private String winnerId;

  public void validate()
  {

  }

  public String getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( String participantId )
  {
    this.participantId = participantId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber( String pageNumber )
  {
    this.pageNumber = pageNumber;
  }

  public String getResultsPerPage()
  {
    return resultsPerPage;
  }

  public void setResultsPerPage( String resultsPerPage )
  {
    this.resultsPerPage = resultsPerPage;
  }

  public String getNominationId()
  {
    return nominationId;
  }

  public void setNominationId( String nominationId )
  {
    this.nominationId = nominationId;
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

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public String getWinnerId()
  {
    return winnerId;
  }

  public void setWinnerId( String winnerId )
  {
    this.winnerId = winnerId;
  }
}
