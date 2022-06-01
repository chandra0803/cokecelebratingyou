
package com.biperf.core.value.participant;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Transient;

import com.biperf.core.value.indexing.Searchable;
import com.biw.digs.rest.response.PersonPronouns;

public class PaxIndexData implements Serializable, Searchable
{

  private static final long serialVersionUID = 8331128109488149663L;

  private Long userId;
  private String firstname;
  private String lastname;
  private String positionTypeCode;
  private String departmentTypeCode;
  private Long primaryNodeId;
  private Long countryId;
  private String avatar;
  private List<Long> audienceIds;
  private List<String> paths;
  private List<Long> allNodeIds;
  private boolean isOptOutAwards;

  // Roster Related Attributes
  private String userName;
  private String roleType;
  private PersonPronouns pronouns;
  private String personCountry;
  private String languagePreference;
  private String state;
  private List<PhoneNumbers> phoneNumbers;
  private List<EmailAddress> emailAddress;
  private List<PersonAttributes> personAttributes;
  private List<PersonAddresses> personAddresses;
  protected UUID rosterUserId;
  @Transient
  private boolean active;
  private String name;
  private String title;
  private String suffix;
  private String hireDate;
  private String terminationDate;
  private String middleName;
  private String gender;

  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

  public String getUserName()
  {
    return userName;
  }

  public String getRoleType()
  {
    return roleType;
  }

  public PersonPronouns getPronouns()
  {
    return pronouns;
  }

  public String getPersonCountry()
  {
    return personCountry;
  }

  public String getLanguagePreference()
  {
    return languagePreference;
  }

  public String getState()
  {
    return state;
  }

  public List<PhoneNumbers> getPhoneNumbers()
  {
    return phoneNumbers;
  }

  public List<EmailAddress> getEmailAddress()
  {
    return emailAddress;
  }

  public List<PersonAttributes> getPersonAttributes()
  {
    return personAttributes;
  }

  public List<PersonAddresses> getPersonAddresses()
  {
    return personAddresses;
  }

  public UUID getRosterUserId()
  {
    return rosterUserId;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public void setRoleType( String roleType )
  {
    this.roleType = roleType;
  }

  public void setPronouns( PersonPronouns pronouns )
  {
    this.pronouns = pronouns;
  }

  public void setPersonCountry( String personCountry )
  {
    this.personCountry = personCountry;
  }

  public void setLanguagePreference( String languagePreference )
  {
    this.languagePreference = languagePreference;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  public void setPhoneNumbers( List<PhoneNumbers> phoneNumbers )
  {
    this.phoneNumbers = phoneNumbers;
  }

  public void setEmailAddress( List<EmailAddress> emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public void setPersonAttributes( List<PersonAttributes> personAttributes )
  {
    this.personAttributes = personAttributes;
  }

  public void setPersonAddresses( List<PersonAddresses> personAddresses )
  {
    this.personAddresses = personAddresses;
  }

  public void setRosterUserId( UUID rosterUserId )
  {
    this.rosterUserId = rosterUserId;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public PaxIndexData()
  {
  }

  public PaxIndexData( Long userId, String firstname, String lastName )
  {
    this.userId = userId;
    this.firstname = firstname;
    this.lastname = lastName;
  }

  private boolean allowPublicRecognitions;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getFirstname()
  {
    return firstname;
  }

  public void setFirstname( String firstNameDisplay )
  {
    this.firstname = firstNameDisplay;
  }

  public String getLastname()
  {
    return lastname;
  }

  public void setLastname( String lastNameDisplay )
  {
    this.lastname = lastNameDisplay;
  }

  public String getPositionTypeCode()
  {
    return positionTypeCode;
  }

  public void setPositionTypeCode( String positionTypeCode )
  {
    this.positionTypeCode = positionTypeCode;
  }

  public String getDepartmentTypeCode()
  {
    return departmentTypeCode;
  }

  public void setDepartmentTypeCode( String departmentTypeCode )
  {
    this.departmentTypeCode = departmentTypeCode;
  }

  public Long getPrimaryNodeId()
  {
    return primaryNodeId;
  }

  public void setPrimaryNodeId( Long nodeId )
  {
    this.primaryNodeId = nodeId;
  }

  public String getAvatar()
  {
    return avatar;
  }

  public void setAvatar( String avatarUrl )
  {
    this.avatar = avatarUrl;
  }

  public List<Long> getAudienceIds()
  {
    return Objects.isNull( audienceIds ) ? Arrays.asList() : audienceIds;
  }

  public void setAudienceIds( List<Long> audienceIds )
  {
    this.audienceIds = audienceIds;
  }

  public boolean isAllowPublicRecognitions()
  {
    return allowPublicRecognitions;
  }

  public void setAllowPublicRecognitions( boolean allowPublicRecognitions )
  {
    this.allowPublicRecognitions = allowPublicRecognitions;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public String getName()
  {
    return getLastname() + " " + getFirstname();
  }

  public List<String> getPaths()
  {
    return Objects.isNull( paths ) ? Arrays.asList() : paths;
  }

  public void setPaths( List<String> paths )
  {
    this.paths = paths;
  }

  public boolean isOnSameNode( Long nodeId )
  {
    return this.getPrimaryNodeId().equals( nodeId );
  }

  public boolean isOnSameOrBelowMyNode( Long nodeId )
  {
    return isOnSameNode( nodeId ) || this.getPaths().stream().anyMatch( p -> p.indexOf( String.valueOf( nodeId ) ) != -1 );

  }

  public List<Long> getAllNodeIds()
  {
    return Objects.isNull( allNodeIds ) ? Arrays.asList() : allNodeIds;
  }

  public void setAllNodeIds( List<Long> allNodeIds )
  {
    this.allNodeIds = allNodeIds;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean status )
  {
    this.active = status;
  }

  public boolean isOptOutAwards()
  {
    return isOptOutAwards;
  }

  public void setOptOutAwards( boolean isOptOutAwards )
  {
    this.isOptOutAwards = isOptOutAwards;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public void setSuffix( String suffix )
  {
    this.suffix = suffix;
  }

  public String getHireDate()
  {
    return hireDate;
  }

  public void setHireDate( String hireDate )
  {
    this.hireDate = hireDate;
  }

  public String getTerminationDate()
  {
    return terminationDate;
  }

  public void setTerminationDate( String terminationDate )
  {
    this.terminationDate = terminationDate;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getGender()
  {
    return gender;
  }

  public void setGender( String gender )
  {
    this.gender = gender;
  }

  @Override
  public String toString()
  {
    return "PaxIndexData [userId=" + userId + ", firstname=" + firstname + ", lastname=" + lastname + ", positionTypeCode=" + positionTypeCode + ", departmentTypeCode=" + departmentTypeCode
        + ", primaryNodeId=" + primaryNodeId + ", countryId=" + countryId + ", avatar=" + avatar + ", audienceIds=" + audienceIds + ", paths=" + paths + ", allNodeIds=" + allNodeIds
        + ", isOptOutAwards=" + isOptOutAwards + ", userName=" + userName + ", roleType=" + roleType + ", pronouns=" + pronouns + ", personCountry=" + personCountry + ", languagePreference="
        + languagePreference + ", state=" + state + ", phoneNumbers=" + phoneNumbers + ", emailAddress=" + emailAddress + ", personAttributes=" + personAttributes + ", personAddresses="
        + personAddresses + ", rosterUserId=" + rosterUserId + ", active=" + active + ", name=" + name + ", title=" + title + ", suffix=" + suffix + ", hireDate=" + hireDate + ", terminationDate="
        + terminationDate + ", middleName=" + middleName + ", gender=" + gender + ", allowPublicRecognitions=" + allowPublicRecognitions + "]";
  }
}
