
package com.biperf.core.ui.search;

import java.util.List;
import java.util.UUID;

public class PaxSearchQueryModel
{
  private String name;
  private String department;
  private String jobTitle;
  private Long location; // this is nothing but nodeId.
  private Long country;
  private int fromIndex = 0;
  private boolean all;
  private Long nodeId;

  private String query;
  private String filter;
  private List<Long> audienceId;

  private Long contestId;
  private Long promotionId;
  private Long orgUnit;
  private Long path;

  private List<String> excludeUserIds;
  private List<String> includeUserIds;
  private Long paxGroup;

  // Roster Related Attributes

  // For Roster Search Pagination
  private Integer paginationRowStart;
  private Integer paginationRowEnd;

  private UUID rosterUserId;
  private String userName;
  private String personCountry;
  private String languagePreference;
  private String roleType;
  private String emailAddress;
  private String phoneNumber;
  private String personAddressCountry;
  private String state;

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getJobTitle()
  {
    return jobTitle;
  }

  public void setJobTitle( String jobTitle )
  {
    this.jobTitle = jobTitle;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    if ( null != name )
    {
      name = name.toLowerCase().trim();
    }
    this.name = name;
  }

  public int getFromIndex()
  {
    return fromIndex;
  }

  public void setFromIndex( int nextPage )
  {
    this.fromIndex = nextPage;
  }

  public Long getCountry()
  {
    return country;
  }

  public void setCountry( Long country )
  {
    this.country = country;
  }

  public Long getLocation()
  {
    if ( nodeId != null )
    {
      return nodeId;
    }

    return location;
  }

  public void setLocation( Long location )
  {
    this.location = location;
  }

  public boolean isAll()
  {
    return all;
  }

  public void setAll( boolean all )
  {
    this.all = all;
  }

  public String getQuery()
  {
    return query;
  }

  public void setQuery( String query )
  {
    this.query = query;
  }

  public String getFilter()
  {
    return filter;
  }

  public void setFilter( String filter )
  {
    this.filter = filter;
  }

  public List<Long> getAudienceId()
  {
    return audienceId;
  }

  public void setAudienceId( List<Long> audienceId )
  {
    this.audienceId = audienceId;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getOrgUnit()
  {
    return orgUnit;
  }

  public void setOrgUnit( Long orgUnit )
  {
    this.orgUnit = orgUnit;
  }

  public boolean isAudienceSpecific()
  {

    if ( promotionId != null || contestId != null )

    {
      return true;
    }

    return false;
  }

  public Long getPromotionIdOrContestId()
  {
    return promotionId != null ? promotionId : contestId;
  }

  public boolean promotionIdOrContestIdExist()
  {
    return promotionId != null || contestId != null;
  }

  public Long getPath()
  {
    return path;
  }

  public void setPath( Long path )
  {
    this.path = path;
  }

  public List<String> getExcludeUserIds()
  {
    return excludeUserIds;
  }

  public void setExcludeUserIds( List<String> excludeUserIds )
  {
    this.excludeUserIds = excludeUserIds;
  }

  public List<String> getIncludeUserIds()
  {
    return includeUserIds;
  }

  public void setIncludeUserIds( List<String> includeUserIds )
  {
    this.includeUserIds = includeUserIds;
  }

  public Long getPaxGroup()
  {
    return paxGroup;
  }

  public void setPaxGroup( Long paxGroup )
  {
    this.paxGroup = paxGroup;
  }

  public UUID getRosterUserId()
  {
    return rosterUserId;
  }

  public void setRosterUserId( UUID rosterUserId )
  {
    this.rosterUserId = rosterUserId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public Integer getPaginationRowStart()
  {
    return paginationRowStart;
  }

  public void setPaginationRowStart( Integer paginationRowStart )
  {
    this.paginationRowStart = paginationRowStart;
  }

  public Integer getPaginationRowEnd()
  {
    return paginationRowEnd;
  }

  public void setPaginationRowEnd( Integer paginationRowEnd )
  {
    this.paginationRowEnd = paginationRowEnd;
  }

  public String getPersonCountry()
  {
    return personCountry;
  }

  public void setPersonCountry( String personCountry )
  {
    this.personCountry = personCountry;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getLanguagePreference()
  {
    return languagePreference;
  }

  public void setLanguagePreference( String languagePreference )
  {
    this.languagePreference = languagePreference;
  }

  public String getRoleType()
  {
    return roleType;
  }

  public void setRoleType( String roleType )
  {
    this.roleType = roleType;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getPersonAddressCountry()
  {
    return personAddressCountry;
  }

  public void setPersonAddressCountry( String personAddressCountry )
  {
    this.personAddressCountry = personAddressCountry;
  }

  public String getState()
  {
    return state;
  }

  public void setState( String state )
  {
    this.state = state;
  }

}
