/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/fileload/ParticipantImportRecord.java,v $
 */

package com.biperf.core.domain.fileload;

import java.util.Date;

/**
 * Country domain object which represents a country within the Beacon application.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /* participant attributes */
  private Long userId;
  private String userName;
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffix;
  private String ssn;
  private Date birthDate;
  private String gender;
  private Boolean active;
  private String status;
  private String termsAcceptance;
  private String userIDAcceptedTerms;
  private Date termsAcceptedDate;

  private String emailAddress;
  private String emailAddressType;
  private String textMessageAddress;

  private String address1;
  private String address2;
  private String address3;
  private String city;
  private String state;
  private Long countryId;
  private String countryCode;
  private String postalCode;
  private String addressType;

  private String personalPhoneNumber;
  private String businessPhoneNumber;
  private String cellPhoneNumber;

  private Long employerId;
  private String employerName;
  private String jobPosition;
  private String department;
  private String languageId;
  private Date hireDate;
  private Date terminationDate;

  private Long nodeId1;
  private Long nodeId2;
  private Long nodeId3;
  private Long nodeId4;
  private Long nodeId5;

  private String nodeName1;
  private String nodeName2;
  private String nodeName3;
  private String nodeName4;
  private String nodeName5;

  private String nodeRelationship1;
  private String nodeRelationship2;
  private String nodeRelationship3;
  private String nodeRelationship4;
  private String nodeRelationship5;

  private Long characteristicId1;
  private Long characteristicId2;
  private Long characteristicId3;
  private Long characteristicId4;
  private Long characteristicId5;
  private Long characteristicId6;
  private Long characteristicId7;
  private Long characteristicId8;
  private Long characteristicId9;
  private Long characteristicId10;
  private Long characteristicId11;
  private Long characteristicId12;
  private Long characteristicId13;
  private Long characteristicId14;
  private Long characteristicId15;
  private Long characteristicId16;
  private Long characteristicId17;
  private Long characteristicId18;
  private Long characteristicId19;
  private Long characteristicId20;

  private String characteristicName1;
  private String characteristicName2;
  private String characteristicName3;
  private String characteristicName4;
  private String characteristicName5;
  private String characteristicName6;
  private String characteristicName7;
  private String characteristicName8;
  private String characteristicName9;
  private String characteristicName10;
  private String characteristicName11;
  private String characteristicName12;
  private String characteristicName13;
  private String characteristicName14;
  private String characteristicName15;
  private String characteristicName16;
  private String characteristicName17;
  private String characteristicName18;
  private String characteristicName19;
  private String characteristicName20;
  private String characteristicName21;
  private String characteristicName22;
  private String characteristicName23;
  private String characteristicName24;
  private String characteristicName25;
  private String characteristicName26;
  private String characteristicName27;
  private String characteristicName28;
  private String characteristicName29;
  private String characteristicName30;
  private String characteristicName31;
  private String characteristicName32;
  private String characteristicName33;
  private String characteristicName34;
  private String characteristicName35;

  private String characteristicValue1;
  private String characteristicValue2;
  private String characteristicValue3;
  private String characteristicValue4;
  private String characteristicValue5;
  private String characteristicValue6;
  private String characteristicValue7;
  private String characteristicValue8;
  private String characteristicValue9;
  private String characteristicValue10;
  private String characteristicValue11;
  private String characteristicValue12;
  private String characteristicValue13;
  private String characteristicValue14;
  private String characteristicValue15;
  private String characteristicValue16;
  private String characteristicValue17;
  private String characteristicValue18;
  private String characteristicValue19;
  private String characteristicValue20;
  private String characteristicValue21;
  private String characteristicValue22;
  private String characteristicValue23;
  private String characteristicValue24;
  private String characteristicValue25;
  private String characteristicValue26;
  private String characteristicValue27;
  private String characteristicValue28;
  private String characteristicValue29;
  private String characteristicValue30;
  private String characteristicValue31;
  private String characteristicValue32;
  private String characteristicValue33;
  private String characteristicValue34;
  private String characteristicValue35;


  private Long roleId1;
  private Long roleId2;
  private Long roleId3;
  private Long roleId4;
  private Long roleId5;

  private String roleDescription1;
  private String roleDescription2;
  private String roleDescription3;
  private String roleDescription4;
  private String roleDescription5;

  // tccc customization start WIP 30460
  private Long nodeId6;
  private Long nodeId7;
  private Long nodeId8;
  private Long nodeId9;
  private Long nodeId10;
  private Long nodeId11;
  
  private String nodeName6;
  private String nodeName7;
  private String nodeName8;
  private String nodeName9;
  private String nodeName10;
  private String nodeName11;
  
  private String nodeRelationship6;
  private String nodeRelationship7;
  private String nodeRelationship8;
  private String nodeRelationship9;
  private String nodeRelationship10;
  private String nodeRelationship11;
  //tccc customization end WIP 30460

  private String ssoId;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Boolean getActive()
  {
    return active;
  }

  public String getAddress1()
  {
    return address1;
  }

  public String getAddress2()
  {
    return address2;
  }

  public String getAddress3()
  {
    return address3;
  }

  public String getAddressType()
  {
    return addressType;
  }

  public Date getBirthDate()
  {
    return birthDate;
  }

  public String getBusinessPhoneNumber()
  {
    return businessPhoneNumber;
  }

  public String getCellPhoneNumber()
  {
    return cellPhoneNumber;
  }

  public Long getCharacteristicId1()
  {
    return characteristicId1;
  }

  public Long getCharacteristicId2()
  {
    return characteristicId2;
  }

  public Long getCharacteristicId3()
  {
    return characteristicId3;
  }

  public Long getCharacteristicId4()
  {
    return characteristicId4;
  }

  public Long getCharacteristicId5()
  {
    return characteristicId5;
  }

  public String getCharacteristicName1()
  {
    return characteristicName1;
  }

  public String getCharacteristicName2()
  {
    return characteristicName2;
  }

  public String getCharacteristicName3()
  {
    return characteristicName3;
  }

  public String getCharacteristicName4()
  {
    return characteristicName4;
  }

  public String getCharacteristicName5()
  {
    return characteristicName5;
  }

  public String getCharacteristicValue1()
  {
    return characteristicValue1;
  }

  public String getCharacteristicValue2()
  {
    return characteristicValue2;
  }

  public String getCharacteristicValue3()
  {
    return characteristicValue3;
  }

  public String getCharacteristicValue4()
  {
    return characteristicValue4;
  }

  public String getCharacteristicValue5()
  {
    return characteristicValue5;
  }

  public String getCity()
  {
    return city;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public String getDepartment()
  {
    return department;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public String getEmailAddressType()
  {
    return emailAddressType;
  }

  public Long getEmployerId()
  {
    return employerId;
  }

  public String getEmployerName()
  {
    return employerName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getGender()
  {
    return gender;
  }

  public Date getHireDate()
  {
    return hireDate;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public Long getNodeId1()
  {
    return nodeId1;
  }

  public Long getNodeId2()
  {
    return nodeId2;
  }

  public Long getNodeId3()
  {
    return nodeId3;
  }

  public Long getNodeId4()
  {
    return nodeId4;
  }

  public Long getNodeId5()
  {
    return nodeId5;
  }

  public String getNodeName1()
  {
    return nodeName1;
  }

  public String getNodeName2()
  {
    return nodeName2;
  }

  public String getNodeName3()
  {
    return nodeName3;
  }

  public String getNodeName4()
  {
    return nodeName4;
  }

  public String getNodeName5()
  {
    return nodeName5;
  }

  public String getNodeRelationship1()
  {
    return nodeRelationship1;
  }

  public String getNodeRelationship2()
  {
    return nodeRelationship2;
  }

  public String getNodeRelationship3()
  {
    return nodeRelationship3;
  }

  public String getNodeRelationship4()
  {
    return nodeRelationship4;
  }

  public String getNodeRelationship5()
  {
    return nodeRelationship5;
  }

  public String getPersonalPhoneNumber()
  {
    return personalPhoneNumber;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public String getRoleDescription1()
  {
    return roleDescription1;
  }

  public String getRoleDescription2()
  {
    return roleDescription2;
  }

  public String getRoleDescription3()
  {
    return roleDescription3;
  }

  public String getRoleDescription4()
  {
    return roleDescription4;
  }

  public String getRoleDescription5()
  {
    return roleDescription5;
  }

  public Long getRoleId1()
  {
    return roleId1;
  }

  public Long getRoleId2()
  {
    return roleId2;
  }

  public Long getRoleId3()
  {
    return roleId3;
  }

  public Long getRoleId4()
  {
    return roleId4;
  }

  public Long getRoleId5()
  {
    return roleId5;
  }

  public String getSsn()
  {
    return ssn;
  }

  public String getState()
  {
    return state;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public Date getTerminationDate()
  {
    return terminationDate;
  }

  public String getTextMessageAddress()
  {
    return textMessageAddress;
  }

  public Long getUserId()
  {
    return userId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setActive( Boolean active )
  {
    this.active = active;
  }

  public void setAddress1( String address1 )
  {
    this.address1 = address1;
  }

  public void setAddress2( String address2 )
  {
    this.address2 = address2;
  }

  public void setAddress3( String address3 )
  {
    this.address3 = address3;
  }

  public void setAddressType( String addressType )
  {
    this.addressType = addressType;
  }

  public void setBirthDate( Date birthDate )
  {
    this.birthDate = birthDate;
  }

  public void setBusinessPhoneNumber( String businessPhoneNumber )
  {
    this.businessPhoneNumber = businessPhoneNumber;
  }

  public void setCellPhoneNumber( String cellPhoneNumber )
  {
    this.cellPhoneNumber = cellPhoneNumber;
  }

  public void setCharacteristicId1( Long characteristicId1 )
  {
    this.characteristicId1 = characteristicId1;
  }

  public void setCharacteristicId2( Long characteristicId2 )
  {
    this.characteristicId2 = characteristicId2;
  }

  public void setCharacteristicId3( Long characteristicId3 )
  {
    this.characteristicId3 = characteristicId3;
  }

  public void setCharacteristicId4( Long characteristicId4 )
  {
    this.characteristicId4 = characteristicId4;
  }

  public void setCharacteristicId5( Long characteristicId5 )
  {
    this.characteristicId5 = characteristicId5;
  }

  public void setCharacteristicName1( String characteristicName1 )
  {
    this.characteristicName1 = characteristicName1;
  }

  public void setCharacteristicName2( String characteristicName2 )
  {
    this.characteristicName2 = characteristicName2;
  }

  public void setCharacteristicName3( String characteristicName3 )
  {
    this.characteristicName3 = characteristicName3;
  }

  public void setCharacteristicName4( String characteristicName4 )
  {
    this.characteristicName4 = characteristicName4;
  }

  public void setCharacteristicName5( String characteristicName5 )
  {
    this.characteristicName5 = characteristicName5;
  }

  public void setCharacteristicValue1( String characteristicValue1 )
  {
    this.characteristicValue1 = characteristicValue1;
  }

  public void setCharacteristicValue2( String characteristicValue2 )
  {
    this.characteristicValue2 = characteristicValue2;
  }

  public void setCharacteristicValue3( String characteristicValue3 )
  {
    this.characteristicValue3 = characteristicValue3;
  }

  public void setCharacteristicValue4( String characteristicValue4 )
  {
    this.characteristicValue4 = characteristicValue4;
  }

  public void setCharacteristicValue5( String characteristicValue5 )
  {
    this.characteristicValue5 = characteristicValue5;
  }

  public void setCity( String city )
  {
    this.city = city;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public void setEmailAddressType( String emailAddressType )
  {
    this.emailAddressType = emailAddressType;
  }

  public void setEmployerId( Long employerId )
  {
    this.employerId = employerId;
  }

  public void setEmployerName( String employerName )
  {
    this.employerName = employerName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public void setGender( String gender )
  {
    this.gender = gender;
  }

  public void setHireDate( Date hireDate )
  {
    this.hireDate = hireDate;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public void setNodeId1( Long nodeId1 )
  {
    this.nodeId1 = nodeId1;
  }

  public void setNodeId2( Long nodeId2 )
  {
    this.nodeId2 = nodeId2;
  }

  public void setNodeId3( Long nodeId3 )
  {
    this.nodeId3 = nodeId3;
  }

  public void setNodeId4( Long nodeId4 )
  {
    this.nodeId4 = nodeId4;
  }

  public void setNodeId5( Long nodeId5 )
  {
    this.nodeId5 = nodeId5;
  }

  public void setNodeName1( String nodeName1 )
  {
    this.nodeName1 = nodeName1;
  }

  public void setNodeName2( String nodeName2 )
  {
    this.nodeName2 = nodeName2;
  }

  public void setNodeName3( String nodeName3 )
  {
    this.nodeName3 = nodeName3;
  }

  public void setNodeName4( String nodeName4 )
  {
    this.nodeName4 = nodeName4;
  }

  public void setNodeName5( String nodeName5 )
  {
    this.nodeName5 = nodeName5;
  }

  public void setNodeRelationship1( String nodeRelationship1 )
  {
    this.nodeRelationship1 = nodeRelationship1;
  }

  public void setNodeRelationship2( String nodeRelationship2 )
  {
    this.nodeRelationship2 = nodeRelationship2;
  }

  public void setNodeRelationship3( String nodeRelationship3 )
  {
    this.nodeRelationship3 = nodeRelationship3;
  }

  public void setNodeRelationship4( String nodeRelationship4 )
  {
    this.nodeRelationship4 = nodeRelationship4;
  }

  public void setNodeRelationship5( String nodeRelationship5 )
  {
    this.nodeRelationship5 = nodeRelationship5;
  }

  public void setPersonalPhoneNumber( String personalPhoneNumber )
  {
    this.personalPhoneNumber = personalPhoneNumber;
  }

  public void setPostalCode( String postalCode )
  {
    this.postalCode = postalCode;
  }

  public void setRoleDescription1( String roleDescription1 )
  {
    this.roleDescription1 = roleDescription1;
  }

  public void setRoleDescription2( String roleDescription2 )
  {
    this.roleDescription2 = roleDescription2;
  }

  public void setRoleDescription3( String roleDescription3 )
  {
    this.roleDescription3 = roleDescription3;
  }

  public void setRoleDescription4( String roleDescription4 )
  {
    this.roleDescription4 = roleDescription4;
  }

  public void setRoleDescription5( String roleDescription5 )
  {
    this.roleDescription5 = roleDescription5;
  }

  public void setRoleId1( Long roleId1 )
  {
    this.roleId1 = roleId1;
  }

  public void setRoleId2( Long roleId2 )
  {
    this.roleId2 = roleId2;
  }

  public void setRoleId3( Long roleId3 )
  {
    this.roleId3 = roleId3;
  }

  public void setRoleId4( Long roleId4 )
  {
    this.roleId4 = roleId4;
  }

  public void setRoleId5( Long roleId5 )
  {
    this.roleId5 = roleId5;
  }

  public void setSsn( String ssn )
  {
    this.ssn = ssn;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  public void setSuffix( String suffix )
  {
    this.suffix = suffix;
  }

  public void setTerminationDate( Date terminationDate )
  {
    this.terminationDate = terminationDate;
  }

  public void setTextMessageAddress( String textMessageAddress )
  {
    this.textMessageAddress = textMessageAddress;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Ensure equality between this and the object param. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ParticipantImportRecord ) )
    {
      return false;
    }

    ParticipantImportRecord participantImportRecord = (ParticipantImportRecord)object;

    if ( this.getId() != null && this.getId().equals( participantImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "ParticipantImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public String getTermsAcceptance()
  {
    return termsAcceptance;
  }

  public void setTermsAcceptance( String termsAcceptance )
  {
    this.termsAcceptance = termsAcceptance;
  }

  public Date getTermsAcceptedDate()
  {
    return termsAcceptedDate;
  }

  public void setTermsAcceptedDate( Date termsAcceptedDate )
  {
    this.termsAcceptedDate = termsAcceptedDate;
  }

  public String getUserIDAcceptedTerms()
  {
    return userIDAcceptedTerms;
  }

  public void setUserIDAcceptedTerms( String userIDAcceptedTerms )
  {
    this.userIDAcceptedTerms = userIDAcceptedTerms;
  }

  public void setLanguageId( String languageId )
  {
    this.languageId = languageId;
  }

  public String getLanguageId()
  {
    return languageId;
  }

  public String getCharacteristicName6()
  {
    return characteristicName6;
  }

  public void setCharacteristicName6( String characteristicName6 )
  {
    this.characteristicName6 = characteristicName6;
  }

  public String getCharacteristicName7()
  {
    return characteristicName7;
  }

  public void setCharacteristicName7( String characteristicName7 )
  {
    this.characteristicName7 = characteristicName7;
  }

  public String getCharacteristicName8()
  {
    return characteristicName8;
  }

  public void setCharacteristicName8( String characteristicName8 )
  {
    this.characteristicName8 = characteristicName8;
  }

  public String getCharacteristicName9()
  {
    return characteristicName9;
  }

  public void setCharacteristicName9( String characteristicName9 )
  {
    this.characteristicName9 = characteristicName9;
  }

  public String getCharacteristicName10()
  {
    return characteristicName10;
  }

  public void setCharacteristicName10( String characteristicName10 )
  {
    this.characteristicName10 = characteristicName10;
  }

  public String getCharacteristicName11()
  {
    return characteristicName11;
  }

  public void setCharacteristicName11( String characteristicName11 )
  {
    this.characteristicName11 = characteristicName11;
  }

  public String getCharacteristicName12()
  {
    return characteristicName12;
  }

  public void setCharacteristicName12( String characteristicName12 )
  {
    this.characteristicName12 = characteristicName12;
  }

  public String getCharacteristicName13()
  {
    return characteristicName13;
  }

  public void setCharacteristicName13( String characteristicName13 )
  {
    this.characteristicName13 = characteristicName13;
  }

  public String getCharacteristicName14()
  {
    return characteristicName14;
  }

  public void setCharacteristicName14( String characteristicName14 )
  {
    this.characteristicName14 = characteristicName14;
  }

  public String getCharacteristicName15()
  {
    return characteristicName15;
  }

  public void setCharacteristicName15( String characteristicName15 )
  {
    this.characteristicName15 = characteristicName15;
  }

  public String getCharacteristicValue6()
  {
    return characteristicValue6;
  }

  public void setCharacteristicValue6( String characteristicValue6 )
  {
    this.characteristicValue6 = characteristicValue6;
  }

  public String getCharacteristicValue7()
  {
    return characteristicValue7;
  }

  public void setCharacteristicValue7( String characteristicValue7 )
  {
    this.characteristicValue7 = characteristicValue7;
  }

  public String getCharacteristicValue8()
  {
    return characteristicValue8;
  }

  public void setCharacteristicValue8( String characteristicValue8 )
  {
    this.characteristicValue8 = characteristicValue8;
  }

  public String getCharacteristicValue9()
  {
    return characteristicValue9;
  }

  public void setCharacteristicValue9( String characteristicValue9 )
  {
    this.characteristicValue9 = characteristicValue9;
  }

  public String getCharacteristicValue10()
  {
    return characteristicValue10;
  }

  public void setCharacteristicValue10( String characteristicValue10 )
  {
    this.characteristicValue10 = characteristicValue10;
  }

  public String getCharacteristicValue11()
  {
    return characteristicValue11;
  }

  public void setCharacteristicValue11( String characteristicValue11 )
  {
    this.characteristicValue11 = characteristicValue11;
  }

  public String getCharacteristicValue12()
  {
    return characteristicValue12;
  }

  public void setCharacteristicValue12( String characteristicValue12 )
  {
    this.characteristicValue12 = characteristicValue12;
  }

  public String getCharacteristicValue13()
  {
    return characteristicValue13;
  }

  public void setCharacteristicValue13( String characteristicValue13 )
  {
    this.characteristicValue13 = characteristicValue13;
  }

  public String getCharacteristicValue14()
  {
    return characteristicValue14;
  }

  public void setCharacteristicValue14( String characteristicValue14 )
  {
    this.characteristicValue14 = characteristicValue14;
  }

  public String getCharacteristicValue15()
  {
    return characteristicValue15;
  }

  public void setCharacteristicValue15( String characteristicValue15 )
  {
    this.characteristicValue15 = characteristicValue15;
  }

  public Long getCharacteristicId6()
  {
    return characteristicId6;
  }

  public void setCharacteristicId6( Long characteristicId6 )
  {
    this.characteristicId6 = characteristicId6;
  }

  public Long getCharacteristicId7()
  {
    return characteristicId7;
  }

  public void setCharacteristicId7( Long characteristicId7 )
  {
    this.characteristicId7 = characteristicId7;
  }

  public Long getCharacteristicId8()
  {
    return characteristicId8;
  }

  public void setCharacteristicId8( Long characteristicId8 )
  {
    this.characteristicId8 = characteristicId8;
  }

  public Long getCharacteristicId9()
  {
    return characteristicId9;
  }

  public void setCharacteristicId9( Long characteristicId9 )
  {
    this.characteristicId9 = characteristicId9;
  }

  public Long getCharacteristicId10()
  {
    return characteristicId10;
  }

  public void setCharacteristicId10( Long characteristicId10 )
  {
    this.characteristicId10 = characteristicId10;
  }

  public Long getCharacteristicId11()
  {
    return characteristicId11;
  }

  public void setCharacteristicId11( Long characteristicId11 )
  {
    this.characteristicId11 = characteristicId11;
  }

  public Long getCharacteristicId12()
  {
    return characteristicId12;
  }

  public void setCharacteristicId12( Long characteristicId12 )
  {
    this.characteristicId12 = characteristicId12;
  }

  public Long getCharacteristicId13()
  {
    return characteristicId13;
  }

  public void setCharacteristicId13( Long characteristicId13 )
  {
    this.characteristicId13 = characteristicId13;
  }

  public Long getCharacteristicId14()
  {
    return characteristicId14;
  }

  public void setCharacteristicId14( Long characteristicId14 )
  {
    this.characteristicId14 = characteristicId14;
  }

  public Long getCharacteristicId15()
  {
    return characteristicId15;
  }

  public void setCharacteristicId15( Long characteristicId15 )
  {
    this.characteristicId15 = characteristicId15;
  }

  public void setSsoId( String ssoId )
  {
    this.ssoId = ssoId;
  }

  public String getSsoId()
  {
    return ssoId;
  }

  public String getCharacteristicName16()
  {
    return characteristicName16;
  }

  public void setCharacteristicName16( String characteristicName16 )
  {
    this.characteristicName16 = characteristicName16;
  }

  public String getCharacteristicName17()
  {
    return characteristicName17;
  }

  public void setCharacteristicName17( String characteristicName17 )
  {
    this.characteristicName17 = characteristicName17;
  }

  public String getCharacteristicName18()
  {
    return characteristicName18;
  }

  public void setCharacteristicName18( String characteristicName18 )
  {
    this.characteristicName18 = characteristicName18;
  }

  public String getCharacteristicName19()
  {
    return characteristicName19;
  }

  public void setCharacteristicName19( String characteristicName19 )
  {
    this.characteristicName19 = characteristicName19;
  }

  public String getCharacteristicName20()
  {
    return characteristicName20;
  }

  public void setCharacteristicName20( String characteristicName20 )
  {
    this.characteristicName20 = characteristicName20;
  }

  public String getCharacteristicValue16()
  {
    return characteristicValue16;
  }

  public void setCharacteristicValue16( String characteristicValue16 )
  {
    this.characteristicValue16 = characteristicValue16;
  }

  public String getCharacteristicValue17()
  {
    return characteristicValue17;
  }

  public void setCharacteristicValue17( String characteristicValue17 )
  {
    this.characteristicValue17 = characteristicValue17;
  }

  public String getCharacteristicValue18()
  {
    return characteristicValue18;
  }

  public void setCharacteristicValue18( String characteristicValue18 )
  {
    this.characteristicValue18 = characteristicValue18;
  }

  public String getCharacteristicValue19()
  {
    return characteristicValue19;
  }

  public void setCharacteristicValue19( String characteristicValue19 )
  {
    this.characteristicValue19 = characteristicValue19;
  }

  public String getCharacteristicValue20()
  {
    return characteristicValue20;
  }

  public void setCharacteristicValue20( String characteristicValue20 )
  {
    this.characteristicValue20 = characteristicValue20;
  }

  public Long getCharacteristicId16()
  {
    return characteristicId16;
  }

  public void setCharacteristicId16( Long characteristicId16 )
  {
    this.characteristicId16 = characteristicId16;
  }

  public Long getCharacteristicId17()
  {
    return characteristicId17;
  }

  public void setCharacteristicId17( Long characteristicId17 )
  {
    this.characteristicId17 = characteristicId17;
  }

  public Long getCharacteristicId18()
  {
    return characteristicId18;
  }

  public void setCharacteristicId18( Long characteristicId18 )
  {
    this.characteristicId18 = characteristicId18;
  }

  public Long getCharacteristicId19()
  {
    return characteristicId19;
  }

  public void setCharacteristicId19( Long characteristicId19 )
  {
    this.characteristicId19 = characteristicId19;
  }

  public Long getCharacteristicId20()
  {
    return characteristicId20;
  }

  public void setCharacteristicId20( Long characteristicId20 )
  {
    this.characteristicId20 = characteristicId20;
  }

  public String getCharacteristicName21() {
		return characteristicName21;
	}

	public void setCharacteristicName21(String characteristicName21) {
		this.characteristicName21 = characteristicName21;
	}

	public String getCharacteristicName22() {
		return characteristicName22;
	}

	public void setCharacteristicName22(String characteristicName22) {
		this.characteristicName22 = characteristicName22;
	}

	public String getCharacteristicName23() {
		return characteristicName23;
	}

	public void setCharacteristicName23(String characteristicName23) {
		this.characteristicName23 = characteristicName23;
	}

	public String getCharacteristicName24() {
		return characteristicName24;
	}

	public void setCharacteristicName24(String characteristicName24) {
		this.characteristicName24 = characteristicName24;
	}

	public String getCharacteristicName25() {
		return characteristicName25;
	}

	public void setCharacteristicName25(String characteristicName25) {
		this.characteristicName25 = characteristicName25;
	}

	public String getCharacteristicName26() {
		return characteristicName26;
	}

	public void setCharacteristicName26(String characteristicName26) {
		this.characteristicName26 = characteristicName26;
	}

	public String getCharacteristicName27() {
		return characteristicName27;
	}

	public void setCharacteristicName27(String characteristicName27) {
		this.characteristicName27 = characteristicName27;
	}

	public String getCharacteristicName28() {
		return characteristicName28;
	}

	public void setCharacteristicName28(String characteristicName28) {
		this.characteristicName28 = characteristicName28;
	}

	public String getCharacteristicName29() {
		return characteristicName29;
	}

	public void setCharacteristicName29(String characteristicName29) {
		this.characteristicName29 = characteristicName29;
	}

	public String getCharacteristicName30() {
		return characteristicName30;
	}

	public void setCharacteristicName30(String characteristicName30) {
		this.characteristicName30 = characteristicName30;
	}

	public String getCharacteristicName31() {
		return characteristicName31;
	}

	public void setCharacteristicName31(String characteristicName31) {
		this.characteristicName31 = characteristicName31;
	}

	public String getCharacteristicName32() {
		return characteristicName32;
	}

	public void setCharacteristicName32(String characteristicName32) {
		this.characteristicName32 = characteristicName32;
	}

	public String getCharacteristicName33() {
		return characteristicName33;
	}

	public void setCharacteristicName33(String characteristicName33) {
		this.characteristicName33 = characteristicName33;
	}

	public String getCharacteristicName34() {
		return characteristicName34;
	}

	public void setCharacteristicName34(String characteristicName34) {
		this.characteristicName34 = characteristicName34;
	}

	public String getCharacteristicName35() {
		return characteristicName35;
	}

	public void setCharacteristicName35(String characteristicName35) {
		this.characteristicName35 = characteristicName35;
	}

	public String getCharacteristicValue21() {
		return characteristicValue21;
	}

	public void setCharacteristicValue21(String characteristicValue21) {
		this.characteristicValue21 = characteristicValue21;
	}

	public String getCharacteristicValue22() {
		return characteristicValue22;
	}

	public void setCharacteristicValue22(String characteristicValue22) {
		this.characteristicValue22 = characteristicValue22;
	}

	public String getCharacteristicValue23() {
		return characteristicValue23;
	}

	public void setCharacteristicValue23(String characteristicValue23) {
		this.characteristicValue23 = characteristicValue23;
	}

	public String getCharacteristicValue24() {
		return characteristicValue24;
	}

	public void setCharacteristicValue24(String characteristicValue24) {
		this.characteristicValue24 = characteristicValue24;
	}

	public String getCharacteristicValue25() {
		return characteristicValue25;
	}

	public void setCharacteristicValue25(String characteristicValue25) {
		this.characteristicValue25 = characteristicValue25;
	}

	public String getCharacteristicValue26() {
		return characteristicValue26;
	}

	public void setCharacteristicValue26(String characteristicValue26) {
		this.characteristicValue26 = characteristicValue26;
	}

	public String getCharacteristicValue27() {
		return characteristicValue27;
	}

	public void setCharacteristicValue27(String characteristicValue27) {
		this.characteristicValue27 = characteristicValue27;
	}

	public String getCharacteristicValue28() {
		return characteristicValue28;
	}

	public void setCharacteristicValue28(String characteristicValue28) {
		this.characteristicValue28 = characteristicValue28;
	}

	public String getCharacteristicValue29() {
		return characteristicValue29;
	}

	public void setCharacteristicValue29(String characteristicValue29) {
		this.characteristicValue29 = characteristicValue29;
	}

	public String getCharacteristicValue30() {
		return characteristicValue30;
	}

	public void setCharacteristicValue30(String characteristicValue30) {
		this.characteristicValue30 = characteristicValue30;
	}

	public String getCharacteristicValue31() {
		return characteristicValue31;
	}

	public void setCharacteristicValue31(String characteristicValue31) {
		this.characteristicValue31 = characteristicValue31;
	}

	public String getCharacteristicValue32() {
		return characteristicValue32;
	}

	public void setCharacteristicValue32(String characteristicValue32) {
		this.characteristicValue32 = characteristicValue32;
	}

	public String getCharacteristicValue33() {
		return characteristicValue33;
	}

	public void setCharacteristicValue33(String characteristicValue33) {
		this.characteristicValue33 = characteristicValue33;
	}

	public String getCharacteristicValue34() {
		return characteristicValue34;
	}

	public void setCharacteristicValue34(String characteristicValue34) {
		this.characteristicValue34 = characteristicValue34;
	}

	public String getCharacteristicValue35() {
		return characteristicValue35;
	}

	public void setCharacteristicValue35(String characteristicValue35) {
		this.characteristicValue35 = characteristicValue35;
	}
	//tccc customization start WIP 30460
	public String getNodeName6() {
		return nodeName6;
	}

	public void setNodeName6(String nodeName6) {
		this.nodeName6 = nodeName6;
	}

	public String getNodeName7() {
		return nodeName7;
	}

	public void setNodeName7(String nodeName7) {
		this.nodeName7 = nodeName7;
	}

	public String getNodeName8() {
		return nodeName8;
	}

	public void setNodeName8(String nodeName8) {
		this.nodeName8 = nodeName8;
	}

	public String getNodeName9() {
		return nodeName9;
	}

	public void setNodeName9(String nodeName9) {
		this.nodeName9 = nodeName9;
	}

	public String getNodeName10() {
		return nodeName10;
	}

	public void setNodeName10(String nodeName10) {
		this.nodeName10 = nodeName10;
	}

	public String getNodeName11() {
		return nodeName11;
	}

	public void setNodeName11(String nodeName11) {
		this.nodeName11 = nodeName11;
	}

	public Long getNodeId6() {
		return nodeId6;
	}

	public void setNodeId6(Long nodeId6) {
		this.nodeId6 = nodeId6;
	}

	public Long getNodeId7() {
		return nodeId7;
	}

	public void setNodeId7(Long nodeId7) {
		this.nodeId7 = nodeId7;
	}

	public Long getNodeId8() {
		return nodeId8;
	}

	public void setNodeId8(Long nodeId8) {
		this.nodeId8 = nodeId8;
	}

	public Long getNodeId9() {
		return nodeId9;
	}

	public void setNodeId9(Long nodeId9) {
		this.nodeId9 = nodeId9;
	}

	public Long getNodeId10() {
		return nodeId10;
	}

	public void setNodeId10(Long nodeId10) {
		this.nodeId10 = nodeId10;
	}

	public Long getNodeId11() {
		return nodeId11;
	}

	public void setNodeId11(Long nodeId11) {
		this.nodeId11 = nodeId11;
	}

	public String getNodeRelationship6() {
		return nodeRelationship6;
	}

	public void setNodeRelationship6(String nodeRelationship6) {
		this.nodeRelationship6 = nodeRelationship6;
	}

	public String getNodeRelationship7() {
		return nodeRelationship7;
	}

	public void setNodeRelationship7(String nodeRelationship7) {
		this.nodeRelationship7 = nodeRelationship7;
	}

	public String getNodeRelationship8() {
		return nodeRelationship8;
	}

	public void setNodeRelationship8(String nodeRelationship8) {
		this.nodeRelationship8 = nodeRelationship8;
	}

	public String getNodeRelationship9() {
		return nodeRelationship9;
	}

	public void setNodeRelationship9(String nodeRelationship9) {
		this.nodeRelationship9 = nodeRelationship9;
	}

	public String getNodeRelationship10() {
		return nodeRelationship10;
	}

	public void setNodeRelationship10(String nodeRelationship10) {
		this.nodeRelationship10 = nodeRelationship10;
	}

	public String getNodeRelationship11() {
		return nodeRelationship11;
	}

	public void setNodeRelationship11(String nodeRelationship11) {
		this.nodeRelationship11 = nodeRelationship11;
	}
	//tccc customization end WIP 30460

}
