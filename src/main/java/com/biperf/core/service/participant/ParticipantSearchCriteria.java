
package com.biperf.core.service.participant;

import java.io.Serializable;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;

@SuppressWarnings( "serial" )
public class ParticipantSearchCriteria implements Serializable
{
  private String userName;
  private String firstName;
  private String lastName;
  private String emailAddr;
  private String ssn;
  private String country;
  private String postalCode;
  private String department;
  private String jobPosition;
  private List<Node> nodeList;
  private Long nodeId;
  private Long budgetMasterId;
  private boolean nodeIdAndBelow;
  private boolean sortByLastNameFirstName = false;
  private Long budgetSegmentId;
  private boolean active = true;
  private String groupName;

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
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

  public String getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode( String postalCode )
  {
    this.postalCode = postalCode;
  }

  public String getSsn()
  {
    return ssn;
  }

  public void setSsn( String ssn )
  {
    this.ssn = ssn;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public List<Node> getNodeList()
  {
    return nodeList;
  }

  public void setNodeList( List<Node> nodeList )
  {
    this.nodeList = nodeList;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public boolean isNodeIdAndBelow()
  {
    return nodeIdAndBelow;
  }

  public void setNodeIdAndBelow( boolean nodeIdAndBelow )
  {
    this.nodeIdAndBelow = nodeIdAndBelow;
  }

  public boolean isSortByLastNameFirstName()
  {
    return sortByLastNameFirstName;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  /**
   * If this is true then results will be sorted by last name then first name
   * otherwise will sort on username
   * @param sortByLastNameFirstName
   */
  public void setSortByLastNameFirstName( boolean sortByLastNameFirstName )
  {
    this.sortByLastNameFirstName = sortByLastNameFirstName;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName( String groupName )
  {
    this.groupName = groupName;
  }
}
