
package com.biperf.core.service.awardbanq.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.awardbanq.value.participant.AwardbanqEmailAddressVO;
import com.biperf.awardbanq.value.participant.AwardbanqParticipantAddressVO;
import com.biperf.awardbanq.value.participant.AwardbanqPhoneNumberVO;
import com.biperf.awardbanq.value.participant.AwardbanqSecurityChallengeVO;
import com.biperf.awardbanq.value.participant.BRTCredentials;
import com.biperf.awardbanq.value.participant.ParticipantValidationException;

/**
 * Value object for the the OM participant
 *
 * @author Connie Thomsen
 *
 */
// @XmlRootElement( name = "AwardbanqParticipantVO" )
public class AwardbanqParticipantVO implements Serializable
{
  /** participantPlatformStatus when active.  The value in this object should correspond to this constant */
  public static final String PLATFORM_STATUS_ACTIVE = "A";
  /** participantPlatformStatus when inactive.  The value in this object should correspond to this constant */
  public static final String PLATFORM_STATUS_INACTIVE = "I";

  private String programId = " ";
  private String campaignNumber = " ";
  private String existingCampaignNumber = " ";
  private String firstName = " ";
  private String lastName = " ";
  private String ssn = " ";
  private String middleInitial = " ";
  private String verificationId = " ";
  private String organizationNumber = " ";
  private String omParticipantId = " ";
  private String accountNbr = " ";
  private String sourceSystem = " ";
  private String sourceSystemUserId = " ";
  private String callerParticipantId = " ";
  private String participantVarData = " ";
  private String locale = "en-us";
  // @XmlElement( name = "address" )
  private AwardbanqParticipantAddressVO address;
  // @XmlElement( name = "brtCredentials" )
  private BRTCredentials brtCredentials;
  // @XmlElement( name = "emailAddresses" )
  private List<AwardbanqEmailAddressVO> emailAddresses;
  // @XmlElement( name = "phoneNumbers" )
  private List<AwardbanqPhoneNumberVO> phoneNumbers;
  // @XmlElement( name = "securityChallenges" )
  private List<AwardbanqSecurityChallengeVO> securityChallenges;

  private String participantType = "I";
  private String participantOrganization = " ";

  private String participantPlatformStatus = PLATFORM_STATUS_ACTIVE;
  private String participantPlatformTerminationDate = " ";
  private String participantDepartment = " ";
  private String participantBankCharacteristics1 = " ";
  private String participantBankCharacteristics2 = " ";

  public String getParticipantType()
  {
    return participantType;
  }

  public void setParticipantType( String participantType )
  {
    this.participantType = participantType;
  }

  public String getParticipantOrganization()
  {
    return participantOrganization;
  }

  public void setParticipantOrganization( String participantOrganization )
  {
    this.participantOrganization = participantOrganization;
  }

  /**
   * Participant default constructor
   */
  public AwardbanqParticipantVO()
  {

  }

  /**
   * Retrieve the Campaign Number
   *
   * @return String
   */
  public String getCampaignNumber()
  {
    return campaignNumber;
  }

  /**
   * Alter the Campaign Number
   *
   * @param aCampaignNumber
   * @throws ParticipantValidationException 
   *
   */
  public void setCampaignNumber( String aCampaignNumber ) throws ParticipantValidationException
  {
    if ( aCampaignNumber != null && aCampaignNumber.length() > 6 )
    {
      throw new ParticipantValidationException( "Campaign number is too long: [ " + aCampaignNumber + "]" );
    }
    this.campaignNumber = aCampaignNumber;

    campaignNumber = aCampaignNumber;
  }

  /**
   * Retrieve the first name
   *
   * @return String
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Alter the first name
   *
   * @param aFirstName
   * @throws ParticipantValidationException 
   *
   */
  public void setFirstName( String aFirstName ) throws ParticipantValidationException
  {
    if ( aFirstName != null && aFirstName.length() > 40 )
    {
      throw new ParticipantValidationException( "First name is too long: [ " + aFirstName + "]" );
    }
    firstName = aFirstName;
  }

  /**
   * Retrieve the last name
   *
   * @return String
   */
  public String getLastName()
  {
    return lastName;
  }

  public String getOmParticipantId()
  {
    return omParticipantId;
  }

  public void setOmParticipantId( String omParticipantId )
  {
    this.omParticipantId = omParticipantId;
  }

  public void setLastName( String lastName ) throws ParticipantValidationException
  {
    if ( lastName != null && lastName.length() > 40 )
    {
      throw new ParticipantValidationException( "Last name is too long: [ " + lastName + "]" );
    }
    this.lastName = lastName;
  }

  /**
   * Alter the last name
   *
   * @param aLastName
   *
   *            / public void setLastName(String aLastName) { if (aLastName !=
   *            null && aLastName.length() > 40) {
   *            logger.info("Last name is too long: [ " + aLastName + "]"); }
   * 
   *            lastName = aLastName; }
   * 
   *            /** Retrieve the middle initial
   *
   * @return String
   */
  public String getMiddleInitial()
  {
    return middleInitial;
  }

  /**
   * Alter the middle initial
   *
   * @param aMiddleInitial
   * @throws ParticipantValidationException 
   *
   */
  public void setMiddleInitial( String aMiddleInitial ) throws ParticipantValidationException
  {
    if ( aMiddleInitial != null && aMiddleInitial.length() > 40 )
    {
      throw new ParticipantValidationException( "Middle initial is too long: [ " + aMiddleInitial + "]" );
    }

    middleInitial = aMiddleInitial;
  }

  /**
   * Retrieve the address
   *
   * @return 
   *         com.biperf.awardbanq.value.participant.AwardbanqParticipantAddressVO
   */
  public AwardbanqParticipantAddressVO getAddress()
  {
    if ( address == null )
    {
      // TODO: Should we actually be setting this to an empty
      // value object? Probably not.
      setAddress( new AwardbanqParticipantAddressVO() );
    }
    return address;
  }

  /**
   * Alter the address
   *
   * @param anAddress
   */
  public void setAddress( AwardbanqParticipantAddressVO anAddress )
  {
    address = anAddress;
  }

  /**
   * Retrieve the organization number
   *
   * @return String
   */
  public String getOrganizationNumber()
  {
    return organizationNumber;
  }

  /**
   * Alter the organization number
   *
   * @param value
   */
  public void setOrganizationNumber( String value )
  {
    organizationNumber = value;
  }

  /**
   * Retrieve the OM Services participant id. This represents the primary key
   * identifier to Order Management Services
   *
   * @return String
   */
  public String getOMParticipantId()
  {
    return omParticipantId;
  }

  /**
   * Alter the OM Services participant id
   *
   * @param anParticipantId
   * @throws ParticipantValidationException 
   *
   */
  public void setOMParticipantId( String anParticipantId ) throws ParticipantValidationException
  {
    if ( anParticipantId != null && anParticipantId.length() > 8 )
    {
      throw new ParticipantValidationException( "OM Participant Id is too long: [ " + anParticipantId + "]" );
    }

    omParticipantId = anParticipantId;
  }

  /**
   * Retrieve the account number
   *
   * @return String
   */
  public String getAccountNbr()
  {
    return accountNbr;
  }

  /**
   * Alter the account number
   *
   * @param anAccountNumber
   * @throws ParticipantValidationException 
   *
   */
  public void setAccountNbr( String anAccountNumber ) throws ParticipantValidationException
  {
    if ( anAccountNumber != null && anAccountNumber.length() > 9 )
    {
      throw new ParticipantValidationException( "Account number is too long: [ " + anAccountNumber + "]" );
    }
    accountNbr = anAccountNumber;
  }

  /**
   * Retrieve the verification identifier
   *
   * @return String
   */
  public String getVerificationId()
  {
    return verificationId;
  }

  /**
   * Alter the verification id
   *
   * @param aVerificationId
   * @throws ParticipantValidationException 
   *
   */
  public void setVerificationId( String aVerificationId ) throws ParticipantValidationException
  {
    if ( aVerificationId != null && aVerificationId.length() > 20 )
    {
      throw new ParticipantValidationException( "Verification identifier is too long: [ " + aVerificationId + "]" );
    }
    verificationId = aVerificationId;
  }

  /**
   * Alter the participant var data
   *
   * @param aParticipantVarData
   * @throws ParticipantValidationException 
   *
   */
  public void setParticipantVarData( String aParticipantVarData ) throws ParticipantValidationException
  {
    if ( aParticipantVarData != null && aParticipantVarData.length() > 75 )
    {
      throw new ParticipantValidationException( "Participant Var Data is too long: [ " + aParticipantVarData + "]" );
    }
    participantVarData = aParticipantVarData;
  }

  /**
   * Retrieve the participant var data
   *
   * @return String
   */
  public String getParticipantVarData()
  {
    return participantVarData;
  }

  /**
   * Provide debugging ability to dump contents of object instance
   *
   * @return String
   */

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

    t_representation.append( "Middle Initial: [" );
    t_representation.append( getMiddleInitial() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Account Number: [" );
    t_representation.append( getAccountNbr() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "OM Participant Id: [" );
    t_representation.append( getOMParticipantId() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Verfication Id: [" );
    t_representation.append( getVerificationId() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Participant Var Data: [" );
    t_representation.append( getParticipantVarData() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    t_representation.append( "Address: [" );
    t_representation.append( getAddress() );
    t_representation.append( "]" );
    t_representation.append( t_lineSeparator );

    return t_representation.toString();
  }

  /**
   * @return String
   */
  public String getProgramId()
  {
    return programId;
  }

  /**
   * @param string
   */
  public void setProgramId( String string )
  {
    programId = string;
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

  public String getCallerParticipantId()
  {
    return callerParticipantId;
  }

  public void setCallerParticipantId( String callerParticipantId )
  {
    this.callerParticipantId = callerParticipantId;
  }

  public void setSsn( String ssn ) throws ParticipantValidationException
  {
    if ( ssn != null && ssn.length() > 9 )
    {
      throw new ParticipantValidationException( "SSN is too long: [ " + ssn + "]" );
    }
    this.ssn = ssn;
  }

  public String getSsn()
  {
    return ssn;
  }

  public String getExistingCampaignNumber()
  {
    return existingCampaignNumber;
  }

  public void setExistingCampaignNumber( String existingCampaignNumber )
  {
    this.existingCampaignNumber = existingCampaignNumber;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public List<AwardbanqEmailAddressVO> getEmailAddresses()
  {
    if ( emailAddresses == null )
    {
      emailAddresses = new ArrayList<AwardbanqEmailAddressVO>();
    }

    return emailAddresses;
  }

  public void setEmailAddresses( List<AwardbanqEmailAddressVO> emailAddresses ) throws ParticipantValidationException
  {
    for ( AwardbanqEmailAddressVO email : emailAddresses )
    {
      if ( email.getEmailAddress() != null && email.getEmailAddress().length() > 70 )
      {
        throw new ParticipantValidationException( "Email Address is too long: [ " + email.getEmailAddress() + "]" );
      }
      if ( email.getEmailType() == null || email.getEmailType().length() != 4 )
      {
        throw new ParticipantValidationException( "Email Type is invalid: [ " + email.getEmailType() + "]" );
      }
      if ( email.getPrimaryIndicator() == null || email.getPrimaryIndicator().length() != 1 )
      {
        throw new ParticipantValidationException( "Email Primary Indicator is invalid: [ " + email.getPrimaryIndicator() + "]" );
      }
    }
    String emails = getEmailAddressesAsString();
    if ( emails.length() > 4000 )
    {
      throw new ParticipantValidationException( "Email Addresses are too long: [ " + emails + "]" );
    }

    this.emailAddresses = emailAddresses;
  }

  public String getEmailAddressesAsString()
  {
    StringBuffer emails = new StringBuffer();
    for ( AwardbanqEmailAddressVO email : this.getEmailAddresses() )
    {
      if ( email.getEmailAddress() != null && !email.getEmailAddress().trim().isEmpty() )
      {
        appendField( emails, email.getEmailType(), true );
        appendField( emails, email.getEmailAddress(), true );
        appendField( emails, email.getPrimaryIndicator(), false );
      }
    }

    return emails.toString();
  }

  // original getter for single email address
  // (now translates to new list of email addresses)
  public String getEmailAddress()
  {
    String emailAddress = null;

    List<AwardbanqEmailAddressVO> emails = getEmailAddresses();
    if ( emails.size() > 0 )
    {
      emailAddress = emails.get( 0 ).getEmailAddress();
    }

    return emailAddress;
  }

  // original setter for single email address
  // (now translates to new list of email addresses)
  public void setPrimaryEmailAddress( String emailAddress ) throws ParticipantValidationException
  {
    AwardbanqEmailAddressVO email = new AwardbanqEmailAddressVO();
    email.setEmailAddress( emailAddress );
    email.setEmailType( "PRIM" );
    email.setPrimaryIndicator( "Y" );

    List<AwardbanqEmailAddressVO> list = getEmailAddresses();
    list.add( email );
    setEmailAddresses( list );
  }

  public void setRecoveryEmailAddress( String emailAddress ) throws ParticipantValidationException
  {
    AwardbanqEmailAddressVO email = new AwardbanqEmailAddressVO();
    email.setEmailAddress( emailAddress );
    email.setEmailType( "ALT1" );
    email.setPrimaryIndicator( "N" );

    List<AwardbanqEmailAddressVO> list = getEmailAddresses();
    list.add( email );
    setEmailAddresses( list );
  }

  public List<AwardbanqPhoneNumberVO> getPhoneNumbers()
  {
    if ( phoneNumbers == null )
    {
      phoneNumbers = new ArrayList<AwardbanqPhoneNumberVO>();
    }

    return phoneNumbers;
  }

  public void setPhoneNumbers( List<AwardbanqPhoneNumberVO> phoneNumbers ) throws ParticipantValidationException
  {
    for ( AwardbanqPhoneNumberVO phone : phoneNumbers )
    {
      if ( phone.getCountryCode() != null && phone.getCountryCode().length() > 3 )
      {
        throw new ParticipantValidationException( "Phone Country Code is too long: [ " + phone.getCountryCode() + "]" );
      }
      if ( phone.getPhoneNumber() != null && phone.getPhoneNumber().length() > 24 )
      {
        throw new ParticipantValidationException( "Phone Number is too long: [ " + phone.getPhoneNumber() + "]" );
      }
      if ( phone.getExtension() != null && phone.getExtension().length() > 6 )
      {
        throw new ParticipantValidationException( "Phone Extension is too long: [ " + phone.getExtension() + "]" );
      }
      if ( phone.getPhoneType() == null || phone.getPhoneType().length() != 4 )
      {
        throw new ParticipantValidationException( "Phone Type is invalid: [ " + phone.getPhoneType() + "]" );
      }
      if ( phone.getPrimaryIndicator() == null || phone.getPrimaryIndicator().length() != 1 )
      {
        throw new ParticipantValidationException( "Phone Primary Indicator is invalid: [ " + phone.getPrimaryIndicator() + "]" );
      }
    }
    String phones = getPhoneNumbersAsString();
    if ( phones.length() > 4000 )
    {
      throw new ParticipantValidationException( "Phone Numbers are too long: [ " + phones + "]" );
    }
    this.phoneNumbers = phoneNumbers;
  }

  public String getPhoneNumbersAsString()
  {
    StringBuffer phones = new StringBuffer();
    for ( AwardbanqPhoneNumberVO phone : this.getPhoneNumbers() )
    {
      if ( phone.getPhoneNumber() != null && !phone.getPhoneNumber().trim().isEmpty() )
      {
        appendField( phones, phone.getPhoneType(), true );
        appendField( phones, phone.getCountryCode(), true );
        appendField( phones, phone.getPhoneNumber(), true );
        appendField( phones, phone.getExtension(), true );
        appendField( phones, phone.getPrimaryIndicator(), false );
      }
    }

    return phones.toString();
  }

  // original getter for single phone number
  // (now translates to new list of phone numbers)
  public String getPhone()
  {
    String phone = null;

    List<AwardbanqPhoneNumberVO> phones = getPhoneNumbers();
    if ( phones.size() > 0 )
    {
      phone = phones.get( 0 ).getPhoneNumber();
    }

    return phone;
  }

  // original setter for single phone number
  // (now translates to new list of phone numbers)
  public void setPrimaryPhone( String phone ) throws ParticipantValidationException
  {
    AwardbanqPhoneNumberVO phoneNbr = new AwardbanqPhoneNumberVO();
    phoneNbr.setPhoneNumber( phone );
    phoneNbr.setPhoneType( "PRIM" );
    phoneNbr.setPrimaryIndicator( "Y" );

    List<AwardbanqPhoneNumberVO> list = getPhoneNumbers();
    list.add( phoneNbr );
    setPhoneNumbers( list );
  }

  public void setRecoveryPhone( String phone ) throws ParticipantValidationException
  {
    AwardbanqPhoneNumberVO phoneNbr = new AwardbanqPhoneNumberVO();
    phoneNbr.setPhoneNumber( phone );
    phoneNbr.setPhoneType( "ALT1" );
    phoneNbr.setPrimaryIndicator( "N" );

    List<AwardbanqPhoneNumberVO> list = getPhoneNumbers();
    list.add( phoneNbr );
    setPhoneNumbers( list );
  }

  public List<AwardbanqSecurityChallengeVO> getSecurityChallenges()
  {
    if ( securityChallenges == null )
    {
      securityChallenges = new ArrayList<AwardbanqSecurityChallengeVO>();
    }

    return securityChallenges;
  }

  public void setSecurityChallenges( List<AwardbanqSecurityChallengeVO> securityChallenges ) throws ParticipantValidationException
  {
    for ( AwardbanqSecurityChallengeVO securityChallenge : securityChallenges )
    {
      if ( securityChallenge.getQuestion() != null && securityChallenge.getQuestion().length() > 100 )
      {
        throw new ParticipantValidationException( "Security Challenge Question is too long: [ " + securityChallenge.getQuestion() + "]" );
      }
      if ( securityChallenge.getAnswer() != null && securityChallenge.getAnswer().length() > 254 )
      {
        throw new ParticipantValidationException( "Security Challenge Answer is too long: [ " + securityChallenge.getAnswer() + "]" );
      }
    }
    this.securityChallenges = securityChallenges;
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

  public BRTCredentials getBrtCredentials()
  {
    return brtCredentials;
  }

  public void setBrtCredentials( BRTCredentials brtCredentials )
  {
    this.brtCredentials = brtCredentials;
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

  public void setParticipantPlatformTerminationDate( String date )
  {
    this.participantPlatformTerminationDate = date;
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

}
