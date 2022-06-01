
package com.biperf.core.value.participant;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Transient;

import com.biperf.core.value.indexing.Searchable;
import com.biw.digs.rest.response.PersonPronouns;

public class PaxIndexRosterData extends PaxIndexData implements Serializable, Searchable
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
  private UUID personUUID;
  @Transient
  private boolean active;

  @SuppressWarnings( "unused" )
  private String name;

  @Override
  public PersonPronouns getPronouns()
  {
    return pronouns;
  }

  @Override
  public void setPronouns( PersonPronouns pronouns )
  {
    this.pronouns = pronouns;
  }

  @Override
  public String getUserName()
  {
    return userName;
  }

  @Override
  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  @Override
  public String getState()
  {
    return state;
  }

  @Override
  public void setState( String state )
  {
    this.state = state;
  }

  @Override
  public String getPersonCountry()
  {
    return personCountry;
  }

  @Override
  public void setPersonCountry( String personCountry )
  {
    this.personCountry = personCountry;
  }

  @Override
  public String getRoleType()
  {
    return roleType;
  }

  @Override
  public void setRoleType( String roleType )
  {
    this.roleType = roleType;
  }

  @Override
  public String getLanguagePreference()
  {
    return languagePreference;
  }

  @Override
  public void setLanguagePreference( String languagePreference )
  {
    this.languagePreference = languagePreference;
  }

  public PaxIndexRosterData()
  {
  }

  public PaxIndexRosterData( Long userId, String firstname, String lastName )
  {
    this.userId = userId;
    this.firstname = firstname;
    this.lastname = lastName;
  }

  private boolean allowPublicRecognitions;

  @Override
  public Long getUserId()
  {
    return userId;
  }

  @Override
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  @Override
  public String getFirstname()
  {
    return firstname;
  }

  @Override
  public void setFirstname( String firstNameDisplay )
  {
    this.firstname = firstNameDisplay;
  }

  @Override
  public String getLastname()
  {
    return lastname;
  }

  @Override
  public void setLastname( String lastNameDisplay )
  {
    this.lastname = lastNameDisplay;
  }

  @Override
  public String getPositionTypeCode()
  {
    return positionTypeCode;
  }

  @Override
  public void setPositionTypeCode( String positionTypeCode )
  {
    this.positionTypeCode = positionTypeCode;
  }

  @Override
  public String getDepartmentTypeCode()
  {
    return departmentTypeCode;
  }

  @Override
  public void setDepartmentTypeCode( String departmentTypeCode )
  {
    this.departmentTypeCode = departmentTypeCode;
  }

  @Override
  public Long getPrimaryNodeId()
  {
    return primaryNodeId;
  }

  @Override
  public void setPrimaryNodeId( Long nodeId )
  {
    this.primaryNodeId = nodeId;
  }

  @Override
  public String getAvatar()
  {
    return avatar;
  }

  @Override
  public void setAvatar( String avatarUrl )
  {
    this.avatar = avatarUrl;
  }

  @Override
  public List<Long> getAudienceIds()
  {
    return Objects.isNull( audienceIds ) ? Arrays.asList() : audienceIds;
  }

  @Override
  public void setAudienceIds( List<Long> audienceIds )
  {
    this.audienceIds = audienceIds;
  }

  @Override
  public boolean isAllowPublicRecognitions()
  {
    return allowPublicRecognitions;
  }

  @Override
  public void setAllowPublicRecognitions( boolean allowPublicRecognitions )
  {
    this.allowPublicRecognitions = allowPublicRecognitions;
  }

  @Override
  public Long getCountryId()
  {
    return countryId;
  }

  @Override
  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  @Override
  public String getName()
  {
    return getLastname() + " " + getFirstname();
  }

  @Override
  public List<String> getPaths()
  {
    return Objects.isNull( paths ) ? Arrays.asList() : paths;
  }

  @Override
  public void setPaths( List<String> paths )
  {
    this.paths = paths;
  }

  @Override
  public boolean isOnSameNode( Long nodeId )
  {
    return this.getPrimaryNodeId().equals( nodeId );
  }

  @Override
  public boolean isOnSameOrBelowMyNode( Long nodeId )
  {
    return isOnSameNode( nodeId ) || this.getPaths().stream().anyMatch( p -> p.indexOf( String.valueOf( nodeId ) ) != -1 );

  }

  @Override
  public List<Long> getAllNodeIds()
  {
    return Objects.isNull( allNodeIds ) ? Arrays.asList() : allNodeIds;
  }

  @Override
  public void setAllNodeIds( List<Long> allNodeIds )
  {
    this.allNodeIds = allNodeIds;
  }

  @Override
  public boolean isActive()
  {
    return active;
  }

  @Override
  public void setActive( boolean status )
  {
    this.active = status;
  }

  @Override
  public boolean isOptOutAwards()
  {
    return isOptOutAwards;
  }

  @Override
  public void setOptOutAwards( boolean isOptOutAwards )
  {
    this.isOptOutAwards = isOptOutAwards;
  }

  @Override
  public List<PhoneNumbers> getPhoneNumbers()
  {
    return phoneNumbers;
  }

  @Override
  public void setPhoneNumbers( List<PhoneNumbers> phoneNumbers )
  {
    this.phoneNumbers = phoneNumbers;
  }

  @Override
  public List<EmailAddress> getEmailAddress()
  {
    return emailAddress;
  }

  @Override
  public void setEmailAddress( List<EmailAddress> emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  @Override
  public List<PersonAttributes> getPersonAttributes()
  {
    return personAttributes;
  }

  @Override
  public void setPersonAttributes( List<PersonAttributes> personAttributes )
  {
    this.personAttributes = personAttributes;
  }

  @Override
  public List<PersonAddresses> getPersonAddresses()
  {
    return personAddresses;
  }

  @Override
  public void setPersonAddresses( List<PersonAddresses> personAddresses )
  {
    this.personAddresses = personAddresses;
  }

  @Override
  public UUID getRosterUserId()
  {
    return rosterUserId;
  }

  @Override
  public void setRosterUserId( UUID rosterUserId )
  {
    this.rosterUserId = rosterUserId;
  }

  @Override
  public String toString()
  {
    return "PaxIndexData [userId=" + userId + ", firstname=" + firstname + ", lastname=" + lastname + ", positionTypeCode=" + positionTypeCode + ", departmentTypeCode=" + departmentTypeCode
        + ", nodeId=" + primaryNodeId + ", countryId=" + countryId + ", avatarUrl=" + avatar + ", audienceIds=" + audienceIds + "]";
  }
}
