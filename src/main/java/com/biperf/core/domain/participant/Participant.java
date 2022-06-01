/*
 * Copyright 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.ContactMethod;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.ParticipantRelationshipType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserFacebook;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserTwitter;
import com.objectpartners.cms.util.CmsResourceBundle;

public class Participant extends User
{
  private static final String NOT_INITIALIZED = "Set not initialized";

  /** User ID within the Honeycomb database */
  private Long honeycombUserId;

  /** PickList item to manage the relationship type of this participant */
  private ParticipantRelationshipType relationshipType;

  /** pickList enum to manage the participant suspension status. */
  private ParticipantSuspensionStatus suspensionStatus;

  /** pickList enum to manage the participant status. */
  private ParticipantStatus status;

  /** Association to participant and their available contact methods. */
  private Set participantContactMethods = new LinkedHashSet();

  /** Association to participant and their prefered communications. */
  private Set participantCommunicationPreferences = new LinkedHashSet();

  /** Association to participant and their prefered communications. */
  private Set participantAddressBooks = new LinkedHashSet();

  /** Award Banq account number */
  private String awardBanqNumber;
  private String awardBanqNumberDecrypted;

  /** Award Banq batch upload date */
  private Date awardBanqExtractDate;

  /** Award Banq id associated to this Participant */
  private String centraxId;
  private String centraxIdDecrypted;

  /** Date representation of when this ParticipantStatus has changed. */
  private Date statusChangeDate;

  /** position */
  private String positionType;

  /** department */
  private String departmentType;

  /** participantEmployers */
  private List<ParticipantEmployer> participantEmployers = new ArrayList<ParticipantEmployer>();

  /** pickList enum to manage the participant's Terms & Conditions (T&C) Acceptance * */
  private ParticipantTermsAcceptance termsAcceptance;

  /** The user id of the pax/user who accepted the T&C * */
  private String userIDAcceptedTerms;

  /** The date the T&C was accepted * */
  private Date termsAcceptedDate;

  private UserTwitter userTwitter;
  private UserFacebook userFacebook;

  private boolean giftCodeOnly;

  private boolean allowPublicRecognition;

  // This is to store pax banq info for campaign move process
  private String awardBanqNumberStored;
  private Date awardBanqExtractDateStored;
  private String centraxIdStored;

  // This is to store pax avatar image url
  private String avatarOriginal;
  private String avatarSmall;

  private boolean allowPublicInformation;
  private boolean allowPublicBirthDate;
  private boolean allowPublicHireDate;

  private String ssoId;

  private String sourceType;

  private Date uaAuthorizedDate;

  private Date terminationDate;

  private Boolean optOutAwards = new Boolean( false );
  private Boolean optOutOfProgram = new Boolean( false );
  private Date optOutOfAwardsDate;
  private Date optOutOfProgramDate;
  //tccc customization start
// Client customizations for wip #26532 starts
  private boolean allowSharePurlToOutsiders; 
  private boolean allowPurlContributionsToSeeOthers;
  // Client customizations for wip #26532 ends
  
  private boolean termsAndConditionsdeclined;
  
  public Long getHoneycombUserId()
  {
    return honeycombUserId;
  }

  public void setHoneycombUserId( Long honeycombUserId )
  {
    this.honeycombUserId = honeycombUserId;
  }

  /**
   * get participantSuspensionStatus
   * 
   * @return ParticipantSuspensionStatus
   */
  public ParticipantSuspensionStatus getSuspensionStatus()
  {
    return this.suspensionStatus;
  }

  /**
   * set suspensionStatus
   * 
   * @param suspensionStatus
   */
  public void setSuspensionStatus( ParticipantSuspensionStatus suspensionStatus )
  {
    this.suspensionStatus = suspensionStatus;
  }

  /**
   * get RelationshipType
   * 
   * @return RelationshipType
   */
  public ParticipantRelationshipType getRelationshipType()
  {
    return this.relationshipType;
  }

  /**
   * set relationshipType
   * 
   * @param relationshipType
   */
  public void setRelationshipType( ParticipantRelationshipType relationshipType )
  {
    this.relationshipType = relationshipType;
  }

  /**
   * Assign a ParticipantEmployer to this Participant.
   * 
   * @param participantEmployer
   */
  public void addParticipantEmployer( ParticipantEmployer participantEmployer )
  {
    participantEmployer.setParticipant( this );
    this.participantEmployers.add( participantEmployer );
  }

  /**
   * Get the participantEmployers Participant.
   * 
   * @return Set
   */
  public List<ParticipantEmployer> getParticipantEmployers()
  {
    return this.participantEmployers;
  }

  /**
   * Sets the participantEmployers onto this Participant.
   * 
   * @param participantEmployers
   */
  public void setParticipantEmployers( List<ParticipantEmployer> participantEmployers )
  {
    this.participantEmployers = participantEmployers;
  }

  /**
   * Assign a contactMethod to this Participant.
   * 
   * @param contactMethod
   * @param primary
   */
  public void addContactMethod( ContactMethod contactMethod, Boolean primary )
  {
    this.participantContactMethods.add( new ParticipantContactMethod( this, contactMethod, primary ) );
  }

  /**
   * Assign a participantContactMethod to this Participant.
   * 
   * @param participantContactMethod
   */
  public void addParticipantContactMethod( ParticipantContactMethod participantContactMethod )
  {
    participantContactMethod.setParticipant( this );
    this.participantContactMethods.add( participantContactMethod );
  }

  /**
   * Get the contactMethods for this.
   * 
   * @return Set
   */
  public Set getParticipantContactMethods()
  {
    return this.participantContactMethods;
  }

  public Set getParticipantAddressBooks()
  {
    return this.participantAddressBooks;
  }

  public void setParticipantAddressBooks( Set participantAddressBooks )
  {
    this.participantAddressBooks = participantAddressBooks;
  }

  /**
   * Add participant address book item  for this participant.
   * 
   * @param participantAddressBook
   */
  public void addParticipantAddressBook( ParticipantAddressBook participantAddressBook )
  {
    participantAddressBook.setParticipant( this );
    this.participantAddressBooks.add( participantAddressBook );
  }

  /**
   * Sets the participantContactMethods onto this Participant.
   * 
   * @param participantContactMethods
   */
  public void setParticipantContactMethods( Set participantContactMethods )
  {
    this.participantContactMethods = participantContactMethods;
  }

  /**
   * Assign a ParticipantCommunicationPreference to this Participant.
   * 
   * @param participantCommunicationPreference
   */
  public void addParticipantCommunicationPreference( ParticipantCommunicationPreference participantCommunicationPreference )
  {
    participantCommunicationPreference.setParticipant( this );
    this.participantCommunicationPreferences.add( participantCommunicationPreference );
  }

  /**
   * Get the participantEStatements for this.
   * 
   * @return Set
   */
  public Set getParticipantCommunicationPreferences()
  {
    return this.participantCommunicationPreferences;
  }

  /**
   * Sets the ParticipantEStatements onto this Participant.
   * 
   * @param participantEStatements
   */
  public void setParticipantCommunicationPreferences( Set participantEStatements )
  {
    this.participantCommunicationPreferences = participantEStatements;
  }

  public Date getAwardBanqExtractDate()
  {
    return awardBanqExtractDate;
  }

  public void setAwardBanqExtractDate( Date awardBanqExtractDate )
  {
    this.awardBanqExtractDate = awardBanqExtractDate;
  }

  public String getAwardBanqNumber()
  {
    return awardBanqNumberDecrypted;
  }

  public void setAwardBanqNumber( String awardBanqNumber )
  {
    this.awardBanqNumber = awardBanqNumber;
    this.awardBanqNumberDecrypted = awardBanqNumber;
  }

  public String getCentraxId()
  {
    return centraxIdDecrypted;
  }

  public void setCentraxId( String centraxId )
  {
    this.centraxId = centraxId;
    this.centraxIdDecrypted = centraxId;
  }

  public ParticipantStatus getStatus()
  {
    return status;
  }

  public void setStatus( ParticipantStatus status )
  {
    this.status = status;
    if ( status != null )
    {
      if ( status.getCode().equals( ParticipantStatus.ACTIVE ) )
      {
        super.setActive( Boolean.TRUE );
      }
      else if ( status.getCode().equals( ParticipantStatus.INACTIVE ) )
      {
        super.setActive( Boolean.FALSE );
      }
    }
  }

  public void setStatusChangeDate( Date statusChangeDate )
  {
    this.statusChangeDate = statusChangeDate;
  }

  public Date getStatusChangeDate()
  {
    return this.statusChangeDate;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  public PositionType getPositionTypePickList()
  {
    return PositionType.lookup( this.positionType );
  }

  public String getDepartmentType()
  {
    return departmentType;
  }

  public void setDepartmentType( String departmentType )
  {
    this.departmentType = departmentType;
  }

  public DepartmentType getDepartmentTypePickList()
  {
    return DepartmentType.lookup( this.departmentType );
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  @Override
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "PARTICIPANT [" );
    buf.append( "{USER " ).append( super.toString() ).append( "}, " );
    buf.append( "{awardBanqNumber=" ).append( this.getAwardBanqNumber() ).append( "}, " );
    buf.append( "{awardBanqExtractDate=" ).append( this.getAwardBanqExtractDate() ).append( "}, " );
    buf.append( "{relationshipType=" ).append( this.getRelationshipType() ).append( "}, " );
    buf.append( "{centraxId=" ).append( this.getCentraxId() ).append( "}, " );
    buf.append( "{participantContactMethods=" ).append( this.getParticipantContactMethods() ).append( "}, " );
    buf.append( "{participantCommunicationPreferences=" ).append( this.getParticipantCommunicationPreferences() ).append( "}, " );
    buf.append( "{status=" ).append( this.getStatus() ).append( "}, " );
    buf.append( "{statusChangeDate=" ).append( this.getStatusChangeDate() ).append( "}, " );
    buf.append( "{suspensionStatus=" ).append( this.getSuspensionStatus() ).append( "} " );
    buf.append( "{giftCodeOnly=" ).append( this.isGiftCodeOnly() ).append( "} " );
    buf.append( "{postionType=" ).append( this.getPositionType() ).append( "} " );
    buf.append( "{departmentType=" ).append( this.getDepartmentType() ).append( "} " );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  @Override
  public boolean equals( Object o )
  {
    return super.equals( o );
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  @Override
  public int hashCode()
  {
    return super.hashCode();
  }

  /**
   * Returns the e-mail address of the given type, or null if it does not yet exist
   * 
   * @param type
   * @return UserEmailAddress
   */
  public UserEmailAddress getUserEmailAddress( EmailAddressType type )
  {
    for ( Iterator iterator = getUserEmailAddresses().iterator(); iterator.hasNext(); )
    {
      UserEmailAddress emailAddress = (UserEmailAddress)iterator.next();
      if ( emailAddress.getEmailType().equals( type ) )
      {
        return emailAddress;
      }
    }

    return null;
  }

  /**
   * Returns the address of the given type, or null if it does not yet exist
   * 
   * @param type
   * @return UserAddress
   */
  public UserAddress getUserAddress( AddressType type )
  {
    for ( Iterator iterator = getUserAddresses().iterator(); iterator.hasNext(); )
    {
      UserAddress address = (UserAddress)iterator.next();
      if ( address.getAddressType().equals( type ) )
      {
        return address;
      }
    }

    return null;
  }

  /**
   * Returns the phone of the given type, or null if it does not yet exist
   * 
   * @param type
   * @return UserPhone
   */
  public UserPhone getUserPhone( PhoneType type )
  {
    for ( Iterator iterator = getUserPhones().iterator(); iterator.hasNext(); )
    {
      UserPhone phone = (UserPhone)iterator.next();
      if ( phone.getPhoneType().equals( type ) )
      {
        return phone;
      }
    }

    return null;
  }

  /**
   * Gets the primary UserPhone record
   * 
   * @return UserPhone
   */

  @Override
  public UserPhone getPrimaryPhone()
  {
    UserPhone primaryUserPhone = null;
    if ( this.getUserPhones() != null )
    {
      Iterator iter = this.getUserPhones().iterator();
      while ( iter.hasNext() )
      {
        UserPhone savedUserPhone = (UserPhone)iter.next();
        if ( savedUserPhone.isPrimary() )
        {
          primaryUserPhone = savedUserPhone;
        }
      } // while
    } // if userAddresses != null

    return primaryUserPhone;
  } // end getPrimaryAddress

  /**
   * Returns the userCharacteristic of the given type, or null if it does not yet exist
   * 
   * @param type
   * @return UserCharacteristic
   */
  public UserCharacteristic getUserCharacteristic( UserCharacteristicType type )
  {
    for ( Iterator iterator = getUserCharacteristics().iterator(); iterator.hasNext(); )
    {
      UserCharacteristic characteristic = (UserCharacteristic)iterator.next();
      if ( characteristic.getUserCharacteristicType().equals( type ) )
      {
        return characteristic;
      }
    }

    return null;
  }

  /**
   * Returns the active participantEmployer for the given employer, or null if it does not yet exist
   * 
   * @param employer
   * @return employment (ParticipantEmployer)
   */
  public ParticipantEmployer getActiveParticipantEmployer( Employer employer )
  {
    for ( Iterator iterator = getParticipantEmployers().iterator(); iterator.hasNext(); )
    {
      ParticipantEmployer participantEmployer = (ParticipantEmployer)iterator.next();
      if ( participantEmployer.getEmployer().equals( employer ) && participantEmployer.getTerminationDate() == null )
      {
        return participantEmployer;
      }
    }
    return null;
  }

  /**
   * Returns true if the user is active, false if the user is inactive, and null is the active
   * status is not set. Subclasses override this method if they use a mechanism other than the field
   * User.active to track their active status.
   * 
   * @return true if the user is active, false if the user is inactive, and null is the active
   *         status is not set.
   */
  @Override
  public Boolean isActiveClassAware()
  {
    Boolean isActive = null;

    if ( status != null )
    {
      if ( status.equals( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) ) )
      {
        isActive = Boolean.TRUE;
      }
      else if ( status.equals( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) ) )
      {
        isActive = Boolean.FALSE;
      }
    }

    return isActive;
  }

  public String getUserIDAcceptedTerms()
  {
    return userIDAcceptedTerms;
  }

  public void setUserIDAcceptedTerms( String userIDAcceptedTerms )
  {
    this.userIDAcceptedTerms = userIDAcceptedTerms;
  }

  public Date getTermsAcceptedDate()
  {
    return termsAcceptedDate;
  }

  public void setTermsAcceptedDate( Date termsAcceptedDate )
  {
    this.termsAcceptedDate = termsAcceptedDate;
  }

  public ParticipantTermsAcceptance getTermsAcceptance()
  {
    return termsAcceptance;
  }

  public void setTermsAcceptance( ParticipantTermsAcceptance termsAcceptance )
  {
    this.termsAcceptance = termsAcceptance;
  }

  public UserTwitter getUserTwitter()
  {
    return userTwitter;
  }

  public void setUserTwitter( UserTwitter userTwitter )
  {
    this.userTwitter = userTwitter;
  }

  public UserFacebook getUserFacebook()
  {
    return userFacebook;
  }

  public void setUserFacebook( UserFacebook userFacebook )
  {
    this.userFacebook = userFacebook;
  }

  public void setAllowPublicRecognition( boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public void setGiftCodeOnly( boolean giftCodeOnly )
  {
    this.giftCodeOnly = giftCodeOnly;
  }

  public boolean isGiftCodeOnly()
  {
    return giftCodeOnly;
  }

  public void setAwardBanqNumberStored( String awardBanqNumberStored )
  {
    this.awardBanqNumberStored = awardBanqNumberStored;
  }

  public String getAwardBanqNumberStored()
  {
    return awardBanqNumberStored;
  }

  public void setAwardBanqExtractDateStored( Date awardBanqExtractDateStored )
  {
    this.awardBanqExtractDateStored = awardBanqExtractDateStored;
  }

  public Date getAwardBanqExtractDateStored()
  {
    return awardBanqExtractDateStored;
  }

  public void setCentraxIdStored( String centraxIdStored )
  {
    this.centraxIdStored = centraxIdStored;
  }

  public String getCentraxIdStored()
  {
    return centraxIdStored;
  }

  public String getAvatarOriginal()
  {
    return avatarOriginal;
  }

  public void setAvatarOriginal( String avatarOriginal )
  {
    this.avatarOriginal = avatarOriginal;
  }

  public String getAvatarSmall()
  {
    return avatarSmall;
  }

  public void setAvatarSmall( String avatarSmall )
  {
    this.avatarSmall = avatarSmall;
  }

  public boolean isAllowPublicInformation()
  {
    return allowPublicInformation;
  }

  public void setAllowPublicInformation( boolean allowPublicInformation )
  {
    this.allowPublicInformation = allowPublicInformation;
  }

  public String getPaxOrgName()
  {
    String orgName = CmsResourceBundle.getCmsBundle().getString( "system.general.NOT_AVAILABLE" );
    if ( Hibernate.isInitialized( this.getUserNodes() ) )
    {
      for ( Object obj : this.getUserNodes() )
      {
        if ( obj instanceof UserNode )
        {
          orgName = ( (UserNode)obj ).getNode().getName();
          break;
        }
      }
    }
    else
    {
      orgName = NOT_INITIALIZED;
    }
    return orgName;
  }

  public String getPaxJobName()
  {
    String jobName = CmsResourceBundle.getCmsBundle().getString( "system.general.NOT_AVAILABLE" );
    /*
     * Bugfix#55632 We need not check partcipant_employer because the required fields position and
     * department are already loaded in Participant object
     */
    /*
     * if(Hibernate.isInitialized( this.getParticipantEmployers() )) { for ( ParticipantEmployer
     * paxEmployer : this.getParticipantEmployers() ) { if ( paxEmployer.getTerminationDate() ==
     * null ) { if ( paxEmployer.getPositionType() != null ) { jobName =
     * paxEmployer.getPositionType().getName(); } break; } } } else{ jobName = NOT_INITIALIZED; }
     */
    if ( StringUtils.isNotBlank( positionType ) )
    {
      jobName = PositionType.lookup( positionType ).getName();
      // jobName = positionType;
    }
    return jobName;
  }

  public String getPaxDeptName()
  {
    String departmentName = CmsResourceBundle.getCmsBundle().getString( "system.general.NOT_AVAILABLE" );
    /*
     * Bugfix#55632 We need not check partcipant_employer because the required fields position and
     * department are already loaded in Participant object
     */
    /*
     * if(Hibernate.isInitialized( this.getParticipantEmployers() )){ for ( ParticipantEmployer
     * paxEmployer : this.getParticipantEmployers() ) { if ( paxEmployer.getTerminationDate() ==
     * null ) { if ( paxEmployer.getDepartmentType() != null ) { departmentName =
     * paxEmployer.getDepartmentType().getName(); } break; } } } else{ departmentName =
     * NOT_INITIALIZED; }
     */
    if ( StringUtils.isNotBlank( departmentType ) )
    {
      departmentName = DepartmentType.lookup( departmentType ).getName();
      // departmentName = departmentType;
    }
    return departmentName;
  }

  public String getAvatarSmallFullPath()
  {
    return this.getAvatarSmall();
  }

  public String getAvatarOriginalFullPath()
  {
    return this.getAvatarOriginal();
  }

  public String getAvatarSmallFullPath( String defaultAvatar )
  {
    return this.getAvatarSmall();
  }

  public String getAvatarOriginalFullPath( String defaultAvatar )
  {
    return this.getAvatarOriginal();
  }

  public String getSsoId()
  {
    return ssoId;
  }

  public void setSsoId( String ssoId )
  {
    this.ssoId = ssoId;
  }

  public boolean isAllowPublicBirthDate()
  {
    return allowPublicBirthDate;
  }

  public void setAllowPublicBirthDate( boolean allowPublicBirthDate )
  {
    this.allowPublicBirthDate = allowPublicBirthDate;
  }

  public boolean isAllowPublicHireDate()
  {
    return allowPublicHireDate;
  }

  public void setAllowPublicHireDate( boolean allowPublicHireDate )
  {
    this.allowPublicHireDate = allowPublicHireDate;
  }

  public String getSourceType()
  {
    return sourceType;
  }

  public void setSourceType( String sourceType )
  {
    this.sourceType = sourceType;
  }

  public Date getUaAuthorizedDate()
  {
    return uaAuthorizedDate;
  }

  public void setUaAuthorizedDate( Date uaAuthorizedDate )
  {
    this.uaAuthorizedDate = uaAuthorizedDate;
  }

  public Date getTerminationDate()
  {
    return terminationDate;
  }

  public void setTerminationDate( Date terminationDate )
  {
    this.terminationDate = terminationDate;
  }

  public Boolean getOptOutAwards()
  {
    return optOutAwards;
  }

  public Boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( Boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

  public Boolean getOptOutOfProgram()
  {
    return optOutOfProgram;
  }

  public void setOptOutOfProgram( Boolean optOutOfProgram )
  {
    this.optOutOfProgram = optOutOfProgram;
  }

  public Date getOptOutOfAwardsDate()
  {
    return optOutOfAwardsDate;
  }

  public void setOptOutOfAwardsDate( Date optOutOfAwardsDate )
  {
    this.optOutOfAwardsDate = optOutOfAwardsDate;
  }

  public Date getOptOutOfProgramDate()
  {
    return optOutOfProgramDate;
  }

  public void setOptOutOfProgramDate( Date optOutOfProgramDate )
  {
    this.optOutOfProgramDate = optOutOfProgramDate;
  }
  
//Client customizations for wip #26532 starts
 public boolean isAllowSharePurlToOutsiders()
 {
   return allowSharePurlToOutsiders;
 }

 public void setAllowSharePurlToOutsiders( boolean allowSharePurlToOutsiders )
 {
   this.allowSharePurlToOutsiders = allowSharePurlToOutsiders;
 }
 // Client customizations for wip #26532 ends
 //tccc customization start
 public boolean isAllowPurlContributionsToSeeOthers() 
 {
	return allowPurlContributionsToSeeOthers;
 }

 public void setAllowPurlContributionsToSeeOthers( boolean allowPurlContributionsToSeeOthers )
 {
	this.allowPurlContributionsToSeeOthers = allowPurlContributionsToSeeOthers;
 }
 //tccc customization end
 // Client customization for WIP #42683 starts
 public boolean isTermsAccepted()
 {
   return getTermsAcceptance() != null && getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.ACCEPTED );
 }
 // Client customization for WIP #42683 ends

 public boolean isTermsAndConditionsdeclined() 
 {
	return termsAndConditionsdeclined;
 }

 public void setTermsAndConditionsdeclined(boolean termsAndConditionsdeclined) 
 {
	this.termsAndConditionsdeclined = termsAndConditionsdeclined;
 }
}
