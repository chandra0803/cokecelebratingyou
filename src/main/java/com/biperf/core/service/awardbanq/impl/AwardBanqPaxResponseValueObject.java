
package com.biperf.core.service.awardbanq.impl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.awardbanq.value.participant.AwardbanqEmailAddressVO;
import com.biperf.awardbanq.value.participant.AwardbanqParticipantAddressVO;
import com.biperf.awardbanq.value.participant.AwardbanqPhoneNumberVO;
import com.biperf.awardbanq.value.participant.AwardbanqSecurityChallengeVO;
import com.biperf.awardbanq.value.participant.BRTCredentials;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class AwardBanqPaxResponseValueObject implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;

  // AwardbanqParticipantVO participantVO;
  private BRTCredentials brtCredentials;
  private AwardbanqParticipantAddressVO address;
  private String participantVarData;
  private String campaignNumber;
  private String programId;
  private String callerParticipantId;
  private String firstName;
  private String lastName;
  private String verificationId;
  private String omParticipantId;
  private String accountNbr;
  private String organizationNumber;
  private String sourceSystem;
  private String sourceSystemUserId;
  private String phone;
  private String emailAddress;
  private List<AwardbanqEmailAddressVO> emailAddresses;
  private List<AwardbanqPhoneNumberVO> phoneNumbers;
  private List<AwardbanqSecurityChallengeVO> securityChallenges;
  private String ssn;
  private String participantPlatformStatus;
  private String participantPlatformTerminationDate;
  private String participantDepartment;
  private String participantBankCharacteristics1;
  private String participantBankCharacteristics2;

  public AwardBanqPaxResponseValueObject()
  {

  }

  public AwardBanqPaxResponseValueObject( AwardbanqParticipantVO participantVO, BRTCredentials brtCredentials )
  {
    this.brtCredentials = brtCredentials;
    this.address = participantVO.getAddress();
    this.participantVarData = participantVO.getParticipantVarData();
    this.campaignNumber = participantVO.getCampaignNumber();
    this.programId = participantVO.getProgramId();
    this.callerParticipantId = participantVO.getCallerParticipantId();
    this.firstName = participantVO.getFirstName();
    this.lastName = participantVO.getLastName();
    this.verificationId = participantVO.getVerificationId();
    this.omParticipantId = participantVO.getOMParticipantId();
    this.accountNbr = participantVO.getAccountNbr();
    this.organizationNumber = participantVO.getOrganizationNumber();
    this.sourceSystem = participantVO.getSourceSystem();
    this.sourceSystemUserId = participantVO.getSourceSystemUserId();
    this.phone = participantVO.getPhone();
    this.emailAddress = participantVO.getEmailAddress();
    this.setEmailAddresses( getEmailAddress( participantVO.getEmailAddresses() ) );
    this.setPhoneNumbers( getPhoneNumbers( participantVO.getPhoneNumbers() ) );
    this.securityChallenges = participantVO.getSecurityChallenges();
    this.participantPlatformStatus = participantVO.getParticipantPlatformStatus();
    this.participantPlatformTerminationDate = participantVO.getParticipantPlatformTerminationDate();
    this.participantDepartment = participantVO.getParticipantDepartment();
    this.participantBankCharacteristics1 = participantVO.getParticipantBankCharacteristics1();
    this.participantBankCharacteristics2 = participantVO.getParticipantBankCharacteristics2();
  }

  private List<AwardbanqPhoneNumberVO> getPhoneNumbers( List<AwardbanqPhoneNumberVO> phoneNumbers )
  {
    List<AwardbanqPhoneNumberVO> phones = new ArrayList<AwardbanqPhoneNumberVO>();
    AwardbanqPhoneNumberVO phoneNumber = new AwardbanqPhoneNumberVO();

    phoneNumbers.stream().forEach( e ->
    {
      if ( !e.getPhoneType().equalsIgnoreCase( "PRIM" ) )
      {
        phoneNumber.setPhoneNumber( e.getPhoneNumber() );
      }
    } );
    phoneNumber.setPhoneType( "ALT1" );
    phoneNumber.setPrimaryIndicator( "N" );
    phones.add( phoneNumber );
    return phones;
  }

  private List<AwardbanqEmailAddressVO> getEmailAddress( List<AwardbanqEmailAddressVO> emailAddressList )
  {
    List<AwardbanqEmailAddressVO> emails = new ArrayList<AwardbanqEmailAddressVO>();
    AwardbanqEmailAddressVO email = new AwardbanqEmailAddressVO();

    emailAddressList.stream().forEach( e ->
    {
      if ( !e.getEmailType().equalsIgnoreCase( "PRIM" ) )
      {
        email.setEmailAddress( e.getEmailAddress() );
      }
    } );

    email.setEmailType( "ALT1" );
    email.setPrimaryIndicator( "N" );
    emails.add( email );
    return emails;
  }

  public BRTCredentials getBrtCredentials()
  {
    return brtCredentials;
  }

  public void setBrtCredentials( BRTCredentials brtCredentials )
  {
    this.brtCredentials = brtCredentials;
  }

  public AwardbanqParticipantAddressVO getAddress()
  {
    return address;
  }

  public void setAddress( AwardbanqParticipantAddressVO address )
  {
    this.address = address;
  }

  public String getParticipantVarData()
  {
    return participantVarData;
  }

  public void setParticipantVarData( String participantVarData )
  {
    this.participantVarData = participantVarData;
  }

  public String getCampaignNumber()
  {
    return campaignNumber;
  }

  public void setCampaignNumber( String campaignNumber )
  {
    this.campaignNumber = campaignNumber;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public String getCallerParticipantId()
  {
    return callerParticipantId;
  }

  public void setCallerParticipantId( String callerParticipantId )
  {
    this.callerParticipantId = callerParticipantId;
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

  public String getVerificationId()
  {
    return verificationId;
  }

  public void setVerificationId( String verificationId )
  {
    this.verificationId = verificationId;
  }

  public String getOmParticipantId()
  {
    return omParticipantId;
  }

  public void setOmParticipantId( String omParticipantId )
  {
    this.omParticipantId = omParticipantId;
  }

  public String getAccountNbr()
  {
    return accountNbr;
  }

  public void setAccountNbr( String accountNbr )
  {
    this.accountNbr = accountNbr;
  }

  public String getOrganizationNumber()
  {
    return organizationNumber;
  }

  public void setOrganizationNumber( String organizationNumber )
  {
    this.organizationNumber = organizationNumber;
  }

  public String getSourceSystem()
  {
    return sourceSystem;
  }

  public void setSourceSystem( String sourceSystem )
  {
    this.sourceSystem = sourceSystem;
  }

  public String getSourceSystemUserId()
  {
    return sourceSystemUserId;
  }

  public void setSourceSystemUserId( String sourceSystemUserId )
  {
    this.sourceSystemUserId = sourceSystemUserId;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone( String phone )
  {
    this.phone = phone;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public List<AwardbanqEmailAddressVO> getEmailAddresses()
  {
    return emailAddresses;
  }

  public void setEmailAddresses( List<AwardbanqEmailAddressVO> emailAddresses )
  {
    this.emailAddresses = emailAddresses;
  }

  public List<AwardbanqPhoneNumberVO> getPhoneNumbers()
  {
    return phoneNumbers;
  }

  public void setPhoneNumbers( List<AwardbanqPhoneNumberVO> phoneNumbers )
  {
    this.phoneNumbers = phoneNumbers;
  }

  public List<AwardbanqSecurityChallengeVO> getSecurityChallenges()
  {
    return securityChallenges;
  }

  public void setSecurityChallenges( List<AwardbanqSecurityChallengeVO> securityChallenges )
  {
    this.securityChallenges = securityChallenges;
  }

  public String getSsn()
  {
    return ssn;
  }

  public void setSsn( String ssn )
  {
    this.ssn = ssn;
  }

  public String getParticipantPlatformStatus()
  {
    return participantPlatformStatus;
  }

  public void setParticipantPlatformStatus( String participantPlatformStatus )
  {
    this.participantPlatformStatus = participantPlatformStatus;
  }

  public String getParticipantPlatformTerminationDate()
  {
    return participantPlatformTerminationDate;
  }

  public void setParticipantPlatformTerminationDate( String participantPlatformTerminationDate )
  {
    this.participantPlatformTerminationDate = participantPlatformTerminationDate;
  }

  public String getParticipantDepartment()
  {
    return participantDepartment;
  }

  public void setParticipantDepartment( String participantDepartment )
  {
    this.participantDepartment = participantDepartment;
  }

  public String getParticipantBankCharacteristics1()
  {
    return participantBankCharacteristics1;
  }

  public void setParticipantBankCharacteristics1( String participantBankCharacteristics1 )
  {
    this.participantBankCharacteristics1 = participantBankCharacteristics1;
  }

  public String getParticipantBankCharacteristics2()
  {
    return participantBankCharacteristics2;
  }

  public void setParticipantBankCharacteristics2( String participantBankCharacteristics2 )
  {
    this.participantBankCharacteristics2 = participantBankCharacteristics2;
  }

  @Override
  public String toString()
  {
    String t_lineSeparator = System.getProperty( "line.separator" );

    StringBuffer t_representation = new StringBuffer( "Class: AwardbanqParticipantVO" );

    t_representation.append( t_lineSeparator );

    t_representation.append( "Last Name: [" );
    t_representation.append( getLastName() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "First Name: [" );
    t_representation.append( getFirstName() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "organizationNumber:" );
    t_representation.append( getOrganizationNumber() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Account Number: [" );
    t_representation.append( getAccountNbr() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "callerParticipantId:" );
    t_representation.append( getCallerParticipantId() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "CampaignNumber: [" );
    t_representation.append( getCampaignNumber() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "CallerParticipant Id: " );
    t_representation.append( getCallerParticipantId() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Verfication Id: [" );
    t_representation.append( getVerificationId() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Participant Var Data: [" );
    t_representation.append( getParticipantVarData() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "programId:" );
    t_representation.append( getProgramId() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "sourceSystem:" );
    t_representation.append( getSourceSystem() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "sourceSystemUserId:" );
    t_representation.append( getSourceSystemUserId() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "participantPlatformStatus:" );
    t_representation.append( getParticipantPlatformStatus() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "participantPlatformTerminationDate:" );
    t_representation.append( getParticipantPlatformTerminationDate() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "participantDepartment:" );
    t_representation.append( getParticipantDepartment() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "participantBankCharacteristics1" );
    t_representation.append( getParticipantBankCharacteristics1() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "participantBankCharacteristics2" );
    t_representation.append( getParticipantBankCharacteristics2() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "brtCredentials: {" );
    t_representation.append( "password:" + getBrtCredentials() );
    t_representation.append( "}" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "emailAddresses:[ {" );
    t_representation.append( getEmailAddressesAsString() );
    t_representation.append( " }]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "emailAddress:" );
    t_representation.append( getEmailAddress() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Address: [" );
    t_representation.append( "addr1:" + getAddress().getAddr1() + " ," );
    t_representation.append( "addr2:" + getAddress().getAddr2() + " ," );
    t_representation.append( "addr3:" + getAddress().getAddr3() + " ," );
    t_representation.append( "addr4:" + getAddress().getAddr4() + " ," );
    t_representation.append( "addr5:" + getAddress().getAddr5() + " ," );
    t_representation.append( "addr6:" + getAddress().getAddr6() + " ," );
    t_representation.append( "addressDesc:" + getAddress().getAddressDesc() + " ," );
    t_representation.append( "City:" + getAddress().getCity() + " ," );
    t_representation.append( "Country:" + getAddress().getCountry() + " ," );
    t_representation.append( "Country:" + getAddress().getCounty() + " ," );
    t_representation.append( "state:" + getAddress().getState() + " ," );
    t_representation.append( "Zip:" + getAddress().getZip() + " ," );
    t_representation.append( "id:" + getAddress().getId() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "phone:" );
    t_representation.append( getPhone() );
    t_representation.append( t_lineSeparator );

    t_representation.append( "phoneNumbers[" );
    t_representation.append( "{" );
    t_representation.append( getPhoneNumbersAsString() );
    t_representation.append( "}" );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "SecurityChallenges:[" );
    t_representation.append( getSecurityChallenges() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    return t_representation.toString();
  }

  public String getEmailAddressesAsString()
  {
    StringBuffer emails = new StringBuffer();
    for ( AwardbanqEmailAddressVO email : this.getEmailAddresses() )
    {
      if ( email.getEmailAddress() != null && !email.getEmailAddress().trim().isEmpty() )
      {
        appendField( emails.append( "emailType:" ), email.getEmailType(), true );
        appendField( emails.append( "emailAddress:" ), email.getEmailAddress(), true );
        appendField( emails.append( "primaryIndicator:" ), email.getPrimaryIndicator(), false );
      }
    }

    return emails.toString();
  }

  public String getPhoneNumbersAsString()
  {
    StringBuffer phones = new StringBuffer();
    for ( AwardbanqPhoneNumberVO phone : this.getPhoneNumbers() )
    {
      if ( phone.getPhoneNumber() != null && !phone.getPhoneNumber().trim().isEmpty() )
      {
        appendField( phones.append( "phoneType:" ), phone.getPhoneType(), true );
        appendField( phones.append( "phoneNumber:" ), phone.getPhoneNumber(), true );
        appendField( phones.append( "CountryCode:" ), phone.getCountryCode(), true );
        appendField( phones.append( "extension: " ), phone.getExtension(), true );
        appendField( phones.append( "primaryIndicator:" ), phone.getPrimaryIndicator(), false );
      }
    }

    return phones.toString();
  }

  private void appendField( StringBuffer buffer, String field, boolean toBeContinued )
  {
    for ( int index = 0; index < field.length(); index++ )
    {
      char c = field.charAt( index );
      if ( c == ',' || c == '|' )
      {
        c = ' ';
      }
      buffer.append( c );
    }

    buffer.append( toBeContinued ? "," : "|" );
  }

}
