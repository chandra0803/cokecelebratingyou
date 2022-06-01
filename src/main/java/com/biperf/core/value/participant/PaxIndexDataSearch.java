
package com.biperf.core.value.participant;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Transient;

import com.biperf.core.value.indexing.Searchable;
import com.biw.digs.rest.response.PersonPronouns;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PaxIndexDataSearch extends PaxIndexData implements Serializable, Searchable
{

  private static final long serialVersionUID = 8331128109488149663L;

  @JsonIgnore
  private String userName;
  @JsonIgnore
  private String roleType;
  @JsonIgnore
  private PersonPronouns pronouns;

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

  @JsonIgnore
  private String personCountry;
  @JsonIgnore
  private String languagePreference;
  @JsonIgnore
  private String state;
  @JsonIgnore
  private List<PhoneNumbers> phoneNumbers;
  @JsonIgnore
  private List<EmailAddress> emailAddress;
  @JsonIgnore
  private List<PersonAttributes> personAttributes;
  @JsonIgnore
  private List<PersonAddresses> personAddresses;
  @JsonIgnore
  private UUID personUUID;

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

  @Transient
  private boolean active;

  @SuppressWarnings( "unused" )
  private String name;

  public PaxIndexDataSearch()
  {
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
  public boolean isActive()
  {
    return active;
  }

  public PaxIndexDataSearch( String userName,
                             String roleType,
                             PersonPronouns pronouns,
                             String personCountry,
                             String languagePreference,
                             String state,
                             List<PhoneNumbers> phoneNumbers,
                             List<EmailAddress> emailAddress,
                             List<PersonAttributes> personAttributes,
                             List<PersonAddresses> personAddresses,
                             UUID personUUID,
                             boolean active,
                             String name )
  {
    super();
    this.userName = userName;
    this.roleType = roleType;
    this.pronouns = pronouns;
    this.personCountry = personCountry;
    this.languagePreference = languagePreference;
    this.state = state;
    this.phoneNumbers = phoneNumbers;
    this.emailAddress = emailAddress;
    this.personAttributes = personAttributes;
    this.personAddresses = personAddresses;
    this.personUUID = personUUID;
    this.active = active;
    this.name = name;
  }

  @Override
  public void setActive( boolean status )
  {
    this.active = status;
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
    return "PaxIndexDataSearch [userName=" + userName + ", roleType=" + roleType + ", pronouns=" + pronouns + ", personCountry=" + personCountry + ", languagePreference=" + languagePreference
        + ", state=" + state + ", phoneNumbers=" + phoneNumbers + ", emailAddress=" + emailAddress + ", personAttributes=" + personAttributes + ", personAddresses=" + personAddresses + ", personUUID="
        + personUUID + ", active=" + active + ", name=" + name + "]";
  }

}
