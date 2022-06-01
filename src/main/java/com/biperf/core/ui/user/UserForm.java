/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserForm.java,v $
 */

package com.biperf.core.ui.user;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.GenderType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantEnrollmentSource;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.StatusType;
import com.biperf.core.domain.enums.SuffixType;
import com.biperf.core.domain.enums.TitleType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.enums.YesNoType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.participant.ParticipantContactMethod;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserFacebook;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.domain.user.UserTwitter;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToUserNodesAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CharacteristicValueBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;

/**
 * UserForm used for the AddUser screen.
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
 * <td>zahler</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserForm extends BaseActionForm
{
  public static final String NAME = "userForm";
  private static final String SSO_ID = "SSO ID";

  private String method;
  private String active;
  private String activeDesc;
  private String userType;
  private String userTypeDesc;
  private String paxStatus;
  private String paxStatusDesc;
  private String suspensionStatus;
  private String suspensionStatusDesc;
  private String title;
  private String titleDesc;
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffix;
  private String suffixDesc;
  private String ssn;
  private String maskedSsn;
  private boolean ssnEditable;
  private String dateOfBirth = DateUtils.displayDateFormatMask;
  private String dateOfBirthDisplayString = DateUtils.displayDateFormatMask;
  private String gender;
  private String genderDesc;
  private String addressType;
  private String addressTypeDesc;
  private AddressFormBean addressFormBean = new AddressFormBean();
  private String emailType;
  private String emailTypeDesc;
  private String emailAddress;
  private String enrollmentSourceDesc;
  private String enrollmentDate;
  private String phoneType;
  private String phoneTypeDesc;
  private String phoneNumber;
  private String phoneExtension;
  private String countryPhoneCode;
  private String displayCountryPhoneCode;
  private String userId;
  private String userName;
  private String password;
  private String confirmPassword;
  private String password2;
  private String confirmPassword2;
  private Boolean passwordSystemGenerated = null;
  private String passwordStatus;
  private String secretQuestion;
  private String secretQuestionDesc;
  private String secretAnswer;
  private String secretAnswerDesc;
  private String employerId;
  private String position;
  private String department;
  private String hireDate = DateUtils.displayDateFormatMask;
  private String terminationDate = DateUtils.displayDateFormatMask;
  private String nodeId;
  private String nameOfNode;
  private String nodeRelationship = HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ).getCode();
  private String awardbanqNumber;
  private String centraxId;
  private String language = UserManager.getLocale().toString();
  private String[] contactMethods = new String[10]; // Maximum is 10.
  private String primaryContactMethod;
  private String role;
  private String[] roles;
  private String textMessageAddress;
  private boolean hasFax;
  private boolean viewCurrentUser = true;

  private boolean termsConditionsRequired;
  private boolean userAllowedToAcceptForPax;
  private String paxTermsAccept;
  private String paxTermsAcceptDesc;
  private String termsAcceptedDate;
  private String termsAcceptedBy;
  private String originalPaxTermsAcceptFromDB;
  private String originalPaxStatusFromDB;
  private boolean welcomeEmailSent;
  private boolean raWelcomeEmailSent;
  private boolean optOutAwards;
  private String optOutAwardsStatusDesc;
  private boolean optOutOfProgram;
  private boolean activationComplete;
  private String activationCompleteDesc;
  private String optOutOfProgramStatusDesc;
  private boolean allowPublicRecognition = true;
  private boolean allowPublicInformation = true;
  // Client customziations for wip #26532 starts
  private boolean allowSharePurlToOutsiders = true;
  //  Client customziations for wip #26532 ends
  private boolean allowPublicBirthDate = true;
  private boolean allowPublicHireDate = true;

  private List userCharacteristics = new ArrayList();
  private List userNodes = new ArrayList();
  private List userRoles = new ArrayList();
  private List userCharacteristicValueList = new ArrayList();
  private Long rosterPaxuserId;

  // List of selected textMessage IDs
  private String[] activeSMSGroupTypes;

  // Not to be confused with contactMethods
  private String[] contactMethodTypes;

  private long version;
  private long id;

  private String textPhoneNbr;

  private String acceptTermsOnTextMessages;

  private UserTwitter userTwitter;
  private UserFacebook userFacebook;

  private String uid;
  private String oauthToken;
  private String oauthVerifier;

  private Long rosterManagerId;
  private Long proxyUserId;

  private String ssoId;
  private boolean showSSOId;

  private boolean showThrowdownPlayerStats;
  private String ssiAdminAction;

  private boolean uaEnabled;
  private boolean uaAuthorized;
  private String uaOuthUrl;
  private boolean uaDeAuthorize;
  private String uaLogOutUrl;
  private Long honeycombUserId;
  //tccc customziations start
  private boolean allowPurlContributionsToSeeOthers = true;
  //tccc customziations end
  /**
   * Load the full participant
   * 
   * @param participant
   */
  public void loadParticipant( Participant participant )
  {
	  if( null != participant){
    loadUser( participant );
    loadParticipantPersonalInfo( participant );
    loadPreferences( participant );

    this.awardbanqNumber = participant.getAwardBanqNumber();
    this.centraxId = participant.getCentraxId();
    this.honeycombUserId = participant.getHoneycombUserId();

    this.id = participant.getId().longValue();
    this.version = participant.getVersion().longValue();
	  }
  }

  /**
   * Load all of the User Information in the form
   * 
   * @param user
   */
  public void loadUser( User user )
  {
 
    loadPersonalInfo( user );

    loadLoginInfo( user );

    boolean currentUserMatchesView = user.getId().equals( UserManager.getUserId() );

    this.userNodes.addAll( user.getUserNodes() );
    this.userRoles.addAll( user.getUserRoles() );

    this.userCharacteristics.addAll( user.getUserCharacteristics() );

    UserAddress primaryAddress = user.getPrimaryAddress();
    if ( primaryAddress != null )
    {
      this.addressType = primaryAddress.getAddressType().getCode();
      this.addressTypeDesc = primaryAddress.getAddressType().getName();
      this.addressFormBean.load( primaryAddress.getAddress() );
    }

    UserEmailAddress primaryEmail = user.getPrimaryEmailAddress();
    if ( primaryEmail != null )
    {
      this.emailType = primaryEmail.getEmailType().getCode();
      this.emailTypeDesc = primaryEmail.getEmailType().getName();
      if ( !currentUserMatchesView && EmailAddressType.RECOVERY.equals( emailType ) )
      {
        this.emailAddress = StringUtil.maskEmailAddress( primaryEmail.getEmailAddr() );
      }
      else
      {
        this.emailAddress = primaryEmail.getEmailAddr();
      }
    }

    UserPhone primaryPhone = user.getPrimaryPhone();
    if ( primaryPhone != null )
    {
      this.phoneType = primaryPhone.getPhoneType().getCode();
      this.phoneTypeDesc = primaryPhone.getPhoneType().getName();
      if ( !currentUserMatchesView && PhoneType.RECOVERY.equals( phoneType ) )
      {
        this.phoneNumber = StringUtil.maskPhoneNumber( primaryPhone.getPhoneNbr() );
      }
      else
      {
        this.phoneNumber = primaryPhone.getPhoneNbr();
      }
      this.phoneExtension = primaryPhone.getPhoneExt();
      this.countryPhoneCode = primaryPhone.getCountryPhoneCode();
      if ( this.countryPhoneCode != null )
      {
        Country country = getCountryService().getCountryByCode( this.countryPhoneCode );
        if ( country != null )
        {
          this.displayCountryPhoneCode = country.getPhoneCountryCode();
        }
      }
    }
  }
  
  /**
   * Load the participant personal info
   * 
   * @param participant
   */
  public void loadParticipantPersonalInfo( Participant participant )
  {
    loadPersonalInfo( participant );

    this.originalPaxTermsAcceptFromDB = PresentationUtils.getPickListTypeCode( participant.getTermsAcceptance() );
    this.paxTermsAccept = PresentationUtils.getPickListTypeCode( participant.getTermsAcceptance() );
    this.paxTermsAcceptDesc = PresentationUtils.getPickListTypeDescription( participant.getTermsAcceptance() );
    if ( participant.getUserIDAcceptedTerms() != null )
    {
      User userAcceptedOnBehalfOfPax = getUserService().getUserById( new Long( participant.getUserIDAcceptedTerms() ) );
      if ( userAcceptedOnBehalfOfPax != null )
      {
        this.termsAcceptedBy = userAcceptedOnBehalfOfPax.getNameLFMWithComma();
      }
    }
    this.termsAcceptedDate = DateUtils.toDisplayTimeString( participant.getTermsAcceptedDate() );

    this.originalPaxStatusFromDB = PresentationUtils.getPickListTypeCode( participant.getStatus() );
    this.paxStatus = PresentationUtils.getPickListTypeCode( participant.getStatus() );
    this.paxStatusDesc = PresentationUtils.getPickListTypeDescription( participant.getStatus() );
    this.suspensionStatus = PresentationUtils.getPickListTypeCode( participant.getSuspensionStatus() );
    this.suspensionStatusDesc = PresentationUtils.getPickListTypeDescription( participant.getSuspensionStatus() );
    this.optOutAwards = participant.getOptOutAwards();
    this.optOutOfProgram = participant.getOptOutOfProgram();
    this.activationComplete = participant.isActivationComplete();
    this.setActivationCompleteDesc( activationComplete ? YesNoType.lookup( YesNoType.YES ).getDesc() : YesNoType.lookup( YesNoType.NO ).getDesc() );
    this.optOutAwardsStatusDesc = optOutAwards ? YesNoType.lookup( YesNoType.YES ).getDesc() : YesNoType.lookup( YesNoType.NO ).getDesc();
    this.optOutOfProgramStatusDesc = optOutOfProgram ? YesNoType.lookup( YesNoType.YES ).getDesc() : YesNoType.lookup( YesNoType.NO ).getDesc();
    this.id = participant.getId().longValue();
    this.version = participant.getVersion().longValue();
    this.allowPublicRecognition = participant.isAllowPublicRecognition();
    this.allowPublicInformation = participant.isAllowPublicInformation();
 // Client customizations for wip #26532 starts
    this.allowSharePurlToOutsiders = participant.isAllowSharePurlToOutsiders();
    // Client customizations for wip #26532 ends
    this.allowPurlContributionsToSeeOthers = participant.isAllowPurlContributionsToSeeOthers();
    if ( participant.getSsoId() != null && !StringUtils.isEmpty( participant.getSsoId() ) )
    {
      this.ssoId = participant.getSsoId();
    }

    if ( isShowSSOId() )
    {
      this.setShowSSOId( true );
    }
    else
    {
      this.setShowSSOId( false );
    }
  }

  /**
   * Load the User Personal Information onto the form
   * 
   * @param user
   */
  public void loadPersonalInfo( User user )
  {
    if ( null == user || user.isActive() == null || user.isActive().booleanValue() )
    {
      this.active = "1";
    }
    else
    {
      this.active = "0";
    }
    if( null != user){
    this.activeDesc = PresentationUtils.getPickListTypeDescription( StatusType.lookup( active ) );
    this.title = PresentationUtils.getPickListTypeCode( user.getTitleType() );
    this.titleDesc = PresentationUtils.getPickListTypeDescription( user.getTitleType() );
    this.userType = PresentationUtils.getPickListTypeCode( user.getUserType() );
    this.userTypeDesc = PresentationUtils.getPickListTypeDescription( user.getUserType() );
    this.firstName = user.getFirstName();
    this.middleName = user.getMiddleName();
    this.lastName = user.getLastName();
    this.suffix = PresentationUtils.getPickListTypeCode( user.getSuffixType() );
    this.suffixDesc = PresentationUtils.getPickListTypeDescription( user.getSuffixType() );
    this.ssn = user.getSsn();
    this.maskedSsn = PresentationUtils.getMaskedSSN( user );
    this.ssnEditable = PresentationUtils.hasSSNEditPermission( user );
    this.dateOfBirth = user.getBirthDate() != null ? DateUtils.toDisplayString( user.getBirthDate() ) : DateUtils.displayDateFormatMask;
    this.dateOfBirthDisplayString = user.getBirthDate() != null ? formatDateOfBirthDisplayString( this.dateOfBirth ) : DateUtils.displayDateFormatMask;
    this.gender = PresentationUtils.getPickListTypeCode( user.getGenderType() );
    this.genderDesc = PresentationUtils.getPickListTypeDescription( user.getGenderType() );
    this.enrollmentSourceDesc = PresentationUtils.getPickListTypeDescription( user.getEnrollmentSource() );
    this.enrollmentDate = DateUtils.toDisplayString( user.getEnrollmentDate() );
    this.welcomeEmailSent = user.getWelcomeEmailSent();

    this.id = user.getId().longValue();
    this.version = user.getVersion().longValue();
    }
  }

  private String formatDateOfBirthDisplayString( String dateOfBirth )
  {
    // Hide year for those with only view_participants role
    if ( getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_VIEW_PARTICIPANTS ) )
    {
      SimpleDateFormat dateFormat = DateUtils.displayStringDateFormat( UserManager.getLocale() );
      DateFormat maskFormat = new SimpleDateFormat( dateFormat.toPattern().replaceAll( "\\W?[Yy]+\\W?", "" ) );
      String maskedDate = DateUtils.toConvertDateFormatString( dateFormat, maskFormat, dateOfBirth );
      return maskedDate;
    }
    else
    {
      return dateOfBirth;
    }
  }

  /**
   * Load the User Login Information onto the form
   * 
   * @param user
   */
  public void loadLoginInfo( User user )
  {
	if(null != user){
    this.userId = String.valueOf( user.getId() );
    this.userName = user.getUserName();
    this.password = "~NO_PASSWORD~";
    this.confirmPassword = "~NO_PASSWORD~";

    this.id = user.getId().longValue();
    this.version = user.getVersion().longValue();
    loadSecurityQA( user );
	  }
  }

  private void loadSecurityQA( User user )
  {
    if ( null != user &&  user.getSecretQuestionType() != null )
    {
      this.secretQuestion = user.getSecretQuestionType().getCode();
      if ( user.getId().equals( UserManager.getOriginalUserId() ) )
      {
        this.secretQuestionDesc = user.getSecretQuestionType().getName();
      }
      else
      {
        this.secretQuestionDesc = "**********";
      }
    }

    if (null != user &&  user.getSecretAnswer() != null )
    {
      this.secretAnswer = user.getSecretAnswer();
      if ( user.getId().equals( UserManager.getOriginalUserId() ) )
      {
        this.secretAnswerDesc = user.getSecretAnswer();
      }
      else
      {
        this.secretAnswerDesc = "*******";
      }
    }
  }

  /**
   * Load the participant's preference information onto the form
   * 
   * @param participant
   */
  public void loadPreferences( Participant participant )
  {
    this.language = PresentationUtils.getPickListTypeCode( participant.getLanguageType() );
    this.allowPublicRecognition = participant.isAllowPublicRecognition();
    this.allowPublicInformation = participant.isAllowPublicInformation();
    // Client customizations for wip #26532 starts
    this.allowSharePurlToOutsiders = participant.isAllowSharePurlToOutsiders();
    // Client customizations for wip #26532 ends
    this.allowPurlContributionsToSeeOthers = participant.isAllowPurlContributionsToSeeOthers();
    if ( this.language == null || this.language.length() == 0 )
    {
      this.language = CmsUtil.getCmsProperty( "defaultLocale" );
    }

    Iterator it = participant.getParticipantContactMethods().iterator();
    int i = 0;
    while ( it.hasNext() )
    {
      ParticipantContactMethod participantContactMethod = (ParticipantContactMethod)it.next();
      this.contactMethods[i] = participantContactMethod.getContactMethodCode().getCode();
      if ( participantContactMethod.getPrimary().booleanValue() )
      {
        this.primaryContactMethod = participantContactMethod.getContactMethodCode().getCode();
      }
      i++;
    }

    Set communicationPreferences = participant.getParticipantCommunicationPreferences();
    int activeMessageCount = 0;
    int contactMethodTypesCount = 0;
    for ( Iterator iter = communicationPreferences.iterator(); iter.hasNext(); )
    {
      ParticipantCommunicationPreference preference = (ParticipantCommunicationPreference)iter.next();
      // If the preference has a textMessageId, then add the id to the list of selected messages
      if ( preference.getMessageSMSGroupType() != null )
      {
        activeMessageCount++;
      }
      else
      {
        contactMethodTypesCount++;
      }
    }

    this.activeSMSGroupTypes = new String[activeMessageCount];
    this.contactMethodTypes = new String[contactMethodTypesCount];
    activeMessageCount = 0;
    contactMethodTypesCount = 0;

    // Loop again to set the data
    for ( Iterator iter = communicationPreferences.iterator(); iter.hasNext(); )
    {
      ParticipantCommunicationPreference preference = (ParticipantCommunicationPreference)iter.next();
      // If the preference has a textMessageId, then add the id to the list of selected messages
      if ( preference.getMessageSMSGroupType() != null )
      {
        this.activeSMSGroupTypes[activeMessageCount] = preference.getMessageSMSGroupType().getCode();
        activeMessageCount++;
      }
      else
      {
        this.contactMethodTypes[contactMethodTypesCount] = preference.getParticipantPreferenceCommunicationsType().getCode();
        contactMethodTypesCount++;
      }

    }

    if ( participant.getUserTwitter() != null )
    {
      this.userTwitter = participant.getUserTwitter();
    }
    if ( participant.getUserFacebook() != null )
    {
      this.userFacebook = participant.getUserFacebook();
    }

    this.id = participant.getId().longValue();
    this.version = participant.getVersion().longValue();

    this.allowPublicBirthDate = participant.isAllowPublicBirthDate();
    this.allowPublicHireDate = participant.isAllowPublicHireDate();
  }

  public void loadParticipantEmployer( ParticipantEmployer participantEmployer )
  {
    this.employerId = participantEmployer.getEmployer().getId().toString();
    if ( participantEmployer.getPositionType() != null )
    {
      this.position = participantEmployer.getPositionType();
    }
    if ( participantEmployer.getDepartmentType() != null )
    {
      this.department = participantEmployer.getDepartmentType();
    }
    this.hireDate = participantEmployer.getHireDate() != null ? DateUtils.toDisplayString( participantEmployer.getHireDate() ) : DateUtils.displayDateFormatMask;
    this.terminationDate = participantEmployer.getTerminationDate() != null ? DateUtils.toDisplayString( participantEmployer.getTerminationDate() ) : DateUtils.displayDateFormatMask;
  }

  /**
   * Returns fully loaded Participant
   * 
   * @return Participant
   */
  public Participant toDomainObjectParticipant()
  {
    Participant pax = new Participant();

    setUserInfo( pax );
    setParticipantPersonalInfo( pax );

    if ( !StringUtils.isEmpty( employerId ) )
    {
      ParticipantEmployer paxEmployer = new ParticipantEmployer();
      Employer employer = new Employer();
      employer.setId( new Long( employerId ) );
      paxEmployer.setPositionType( position );
      paxEmployer.setDepartmentType( department );
      paxEmployer.setTerminationDate( DateUtils.toDate( terminationDate ) );
      paxEmployer.setEmployer( employer );
      pax.addParticipantEmployer( paxEmployer );

      // if hire date is blank, set it to the current date (see bug #8633)
      if ( !StringUtils.isEmpty( hireDate ) )
      {
        paxEmployer.setHireDate( DateUtils.toDate( hireDate ) );
      }
      else
      {
        paxEmployer.setHireDate( new Date() );
      }
    }

    if ( optOutOfProgram )
    {
      pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) ); // Turn Pax to
                                                                               // InActive
      pax.setStatusChangeDate( DateUtils.getCurrentDate() );
    }

    // Make sure these are null for the insert
    pax.setId( null );
    pax.setVersion( null );
    if ( this.ssoId != null )
    {
      pax.setSsoId( this.ssoId );
    }

    return pax;
  }

  /**
   * Sets the form data for user info on the User object
   * 
   * @param user
   */
  public void setUserInfo( User user )
  {
   if(null != user ){
	setPersonalInfo( user );
    setLoginInfo( user );

    if ( !StringUtils.isEmpty( addressType ) )
    {
      UserAddress userAddress = new UserAddress();
      userAddress.setAddressType( AddressType.lookup( this.addressType ) );
      userAddress.setAddress( this.addressFormBean.toDomainObject() );
      userAddress.setIsPrimary( Boolean.TRUE );
      user.addUserAddress( userAddress );
    }

    if ( !StringUtils.isEmpty( emailType ) )
    {
      UserEmailAddress userEmail = new UserEmailAddress();
      userEmail.setEmailType( EmailAddressType.lookup( emailType ) );
      userEmail.setEmailAddr( emailAddress );
      userEmail.setIsPrimary( Boolean.TRUE );
      userEmail.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      user.addUserEmailAddress( userEmail );
    }

    if ( !StringUtils.isEmpty( phoneType ) )
    {
      UserPhone userPhone = new UserPhone();
      userPhone.setPhoneType( PhoneType.lookup( phoneType ) );
      userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      userPhone.setPhoneNbr( phoneNumber );
      userPhone.setPhoneExt( phoneExtension );
      userPhone.setCountryPhoneCode( countryPhoneCode );
      userPhone.setIsPrimary( Boolean.TRUE );
      user.addUserPhone( userPhone );
    }
  
    Collection userCharacteristics = CharacteristicUtils.toListOfUserCharacteristicDomainObjects( userCharacteristicValueList );

    Iterator userCharIterator = userCharacteristics.iterator();
    while ( userCharIterator.hasNext() )
    {
      user.addUserCharacteristic( (UserCharacteristic)userCharIterator.next() );
    }

    // make sure it is null for insert
    user.setId( null );
    user.setVersion( null );
  }
  }

  public Participant returnUserCharacteristics( User user )
{
	if (null != user) {
		Collection userCharacteristics = CharacteristicUtils
				.returnListOfUserCharacteristicDomainObjects(this.getUserCharacteristicValueList());

		Iterator userCharIterator = userCharacteristics.iterator();
		while (userCharIterator.hasNext()) {
			user.addUserCharacteristic((UserCharacteristic) userCharIterator.next());
		}
		return (Participant) user;
	} else {
		return null;
	}
}

  /**
   * Sets the form data for personal info on the User object
   * 
   * @param pax
   */
  public void setParticipantPersonalInfo( Participant pax )
  {
    setPersonalInfo( pax );

    pax.setUserType( UserType.lookup( UserType.PARTICIPANT ) );

    if ( suspensionStatus != null )
    {
      pax.setSuspensionStatus( ParticipantSuspensionStatus.lookup( suspensionStatus ) );
    }

    // ---- Participant Status ----
    if ( paxStatus == null ) // this should not happen but just in case
    {
      if ( this.isTermsConditionsRequired() ) // if T&Cs are required by client program
      {
        paxStatus = ParticipantStatus.INACTIVE; // default to INACTIVE
        pax.setStatus( ParticipantStatus.lookup( paxStatus ) );
      }
    }
    // Has the Status field changed since we retrieved from the DB? If so capture changes
    else if ( !paxStatus.equals( originalPaxStatusFromDB ) )
    {
      // Save Pax Status and Status Change Date
      pax.setStatus( ParticipantStatus.lookup( paxStatus ) );
      pax.setStatusChangeDate( DateUtils.getCurrentDate() );
      // set default paxTermAcceptance if paxTermsAccept is null and activate/in activate user
      setDefaultPaxTermAcceptance();

      // Make sure if they're active, they are not terminated
      if ( paxStatus.equals( ParticipantStatus.ACTIVE ) )
      {
        pax.setTerminationDate( null );
      }
      else if ( paxStatus.equals( ParticipantStatus.INACTIVE ) )
      {
        // Probably don't terminate new participants right out of the gate
        if ( pax.getId() != null && pax.getId().longValue() != 0 )
        {
          pax.setTerminationDate( DateUtils.getCurrentDate() );
        }
      }
    }
    else if ( paxStatus.equals( originalPaxStatusFromDB ) )
    {
      // for bug 39113
      pax.setStatus( ParticipantStatus.lookup( paxStatus ) );
    }

    // ---- Participant T&Cs Acceptance ----
    if ( paxTermsAccept == null ) // this could be true when userAllowedToAcceptForPax is false
    {
      paxTermsAccept = ParticipantTermsAcceptance.NOTACCEPTED; // defaults to NOT ACCEPTED
      pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( paxTermsAccept ) );
    }
    // Has the Acceptance field changed since we retrieved from the DB? If so capture changes
    else if ( !originalPaxTermsAcceptFromDB.equals( paxTermsAccept ) )
    {

      if ( originalPaxTermsAcceptFromDB != null && !StringUtil.isNullOrEmpty( originalPaxTermsAcceptFromDB ) )
      {
        addPaxTNCHistory( pax );
      }

      //
      if ( StringUtils.isEmpty( userId ) ) // if creating then use what getting passed in
      {
        if ( paxTermsAccept.equals( ParticipantTermsAcceptance.ACCEPTED ) || paxTermsAccept.equals( ParticipantTermsAcceptance.DECLINED ) )
        {
          pax.setTermsAcceptedDate( DateUtils.getCurrentDate() ); // Capture when T&Cs was accepted
          pax.setUserIDAcceptedTerms( UserManager.getUserId().toString() ); // Capture the User who
        }
        // Save T&C Acceptance
        pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( paxTermsAccept ) );
      }
      else // update
      {
        if ( paxTermsAccept.equals( ParticipantTermsAcceptance.ACCEPTED ) )
        {
          pax.setTermsAcceptedDate( DateUtils.getCurrentDate() ); // Capture when T&Cs was accepted
          pax.setUserIDAcceptedTerms( UserManager.getUserId().toString() ); // Capture the User who
                                                                            // accepted on behalf of
                                                                            // Pax
          pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) ); // Turn Pax to
                                                                                 // Active
          pax.setStatusChangeDate( DateUtils.getCurrentDate() );
        }
        else if ( paxTermsAccept.equals( ParticipantTermsAcceptance.NOTACCEPTED ) )
        {
          pax.setTermsAcceptedDate( null ); // Capture when T&Cs was accepted
          pax.setUserIDAcceptedTerms( null ); // Capture the User who
          pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) ); // Turn Pax to
                                                                                   // InActive
          pax.setStatusChangeDate( DateUtils.getCurrentDate() );
        }
        else if ( paxTermsAccept.equals( ParticipantTermsAcceptance.DECLINED ) )
        {
          pax.setTermsAcceptedDate( DateUtils.getCurrentDate() ); // Capture when T&Cs was accepted
          pax.setUserIDAcceptedTerms( UserManager.getUserId().toString() ); // Capture the User who
          pax.setTermsAcceptedDate( DateUtils.getCurrentDate() ); // Capture when T&Cs was accepted
          pax.setUserIDAcceptedTerms( UserManager.getUserId().toString() ); // Capture the User who
                                                                            // accepted on behalf of
                                                                            // Pax
        }
        // Save T&C Acceptance
        pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( paxTermsAccept ) );
      }
    }
    UserTNCHistory userTNCHistory = null;
    if ( pax.getOptOutOfProgram() != null && ! ( pax.getOptOutOfProgram().equals( optOutOfProgram ) ) && optOutOfProgram )
    {
      pax.setOptOutOfProgramDate( new Date() );
      userTNCHistory = addPaxOptOutHistory( pax, UserTNCHistory.OPT_OUT_OF_PROGRAM_ON );
      getParticipantService().saveTNCHistory( userTNCHistory );
    }
    else if ( ( !optOutOfProgram ) && ( ! ( pax.getOptOutOfProgram().equals( optOutOfProgram ) ) ) )
    {
      pax.setOptOutOfProgramDate( null );
      userTNCHistory = addPaxOptOutHistory( pax, UserTNCHistory.OPT_OUT_OF_PROGRAM_OFF );
      getParticipantService().saveTNCHistory( userTNCHistory );
    }

    if ( pax.getOptOutAwards() != null && ! ( pax.getOptOutAwards().equals( optOutAwards ) ) && optOutAwards )
    {
      pax.setOptOutOfAwardsDate( new Date() );
      userTNCHistory = addPaxOptOutHistory( pax, UserTNCHistory.OPT_OUT_OF_AWARDS_ON );
      getParticipantService().saveTNCHistory( userTNCHistory );

    }
    else if ( ( !optOutAwards ) && ( ! ( pax.getOptOutAwards().equals( optOutAwards ) ) ) )

    {
      pax.setOptOutOfAwardsDate( null );
      userTNCHistory = addPaxOptOutHistory( pax, UserTNCHistory.OPT_OUT_OF_AWARDS_ON );
      getParticipantService().saveTNCHistory( userTNCHistory );
    }

    pax.setOptOutAwards( optOutAwards );
    pax.setOptOutOfProgram( optOutOfProgram );

    pax.setId( new Long( id ) );
    pax.setVersion( new Long( version ) );

    if ( this.ssoId != null )
    {
      pax.setSsoId( this.ssoId );
    }
  }

  public UserTNCHistory addPaxTNCHistory( Participant pax )
  {
    UserTNCHistory userTNCHistory = new UserTNCHistory();
    userTNCHistory.setUser( pax );
    if ( !StringUtils.isEmpty( originalPaxTermsAcceptFromDB ) )
    {
      userTNCHistory.setTncAction( ParticipantTermsAcceptance.lookup( originalPaxTermsAcceptFromDB ).getCode() );
    }
    else if ( pax.getTermsAcceptance() != null )
    {
      userTNCHistory.setTncAction( pax.getTermsAcceptance().getCode() );
    }
    else
    {
      userTNCHistory.setTncAction( paxTermsAccept );
    }

    if ( pax.getUserIDAcceptedTerms() != null )
    {
      userTNCHistory.setHistoryCreatedBy( new Long( pax.getUserIDAcceptedTerms() ) );
    }
    else
    {
      userTNCHistory.setHistoryCreatedBy( pax.getAuditCreateInfo().getCreatedBy() );
    }
    if ( pax.getTermsAcceptedDate() != null )
    {
      userTNCHistory.setHistoryDateCreated( new Timestamp( pax.getTermsAcceptedDate().getTime() ) );
    }
    else
    {
      userTNCHistory.setHistoryDateCreated( pax.getAuditCreateInfo().getDateCreated() );
    }
    return userTNCHistory;
  }

  private UserTNCHistory addPaxOptOutHistory( User pax, String optOutOfProgramOn )
  {
    UserTNCHistory userTNCHistory = new UserTNCHistory();
    userTNCHistory.setUser( pax );
    userTNCHistory.setTncAction( optOutOfProgramOn );
    userTNCHistory.setHistoryCreatedBy( UserManager.getUserId() );
    userTNCHistory.setHistoryDateCreated( new Timestamp( System.currentTimeMillis() ) );
    return userTNCHistory;
  }

  private void setDefaultPaxTermAcceptance()
  {
    if ( this.isTermsConditionsRequired() ) // if T&Cs are required by client program
    {
      // need to update paxTermAccept since it can change when user is change to active or inactive
      if ( paxTermsAccept == null && paxStatus != null && originalPaxStatusFromDB != null )
      {
        if ( ParticipantStatus.INACTIVE.equals( originalPaxStatusFromDB ) && ParticipantStatus.ACTIVE.equals( paxStatus ) )
        {
          paxTermsAccept = ParticipantTermsAcceptance.NOTACCEPTED;
        }
        else if ( ParticipantStatus.ACTIVE.equals( originalPaxStatusFromDB ) && ParticipantStatus.INACTIVE.equals( paxStatus ) )
        {
          paxTermsAccept = ParticipantTermsAcceptance.ACCEPTED;
        }
      }
    }
  }

  /**
   * Sets the form data for personal info on the User object
   * 
   * @param user
   */
  public void setPersonalInfo( User user )
  {
    if ( active == null )
    {
      active = "1";
    }
    if ( active.equals( "1" ) )
    {
      user.setActive( Boolean.TRUE );
    }
    else
    {
      user.setActive( Boolean.FALSE );
    }

    // set the welcome email sent flag to false for new users
    if ( id == 0 )
    {
      user.setWelcomeEmailSent( Boolean.FALSE );
    }
    else
    {
      user.setWelcomeEmailSent( welcomeEmailSent );
    }

    user.setUserType( UserType.lookup( userType ) );
    user.setTitleType( TitleType.lookup( title ) );
    user.setFirstName( firstName );
    user.setMiddleName( middleName );
    user.setLastName( lastName );
    user.setSuffixType( SuffixType.lookup( suffix ) );
    // Bug fix for 18001 as suggested in bugzilla
    try
    {
      Integer.parseInt( ssn );
      user.setSsn( ssn );
    }
    catch( Exception e )
    {
      user.setSsn( null );
    }
    // user.setSsn( ssn );
    // To fix 20618
    user.setBirthDate( DateUtils.toDate( dateOfBirth ) );
    user.setGenderType( GenderType.lookup( gender ) );
    user.setEnrollmentSource( ParticipantEnrollmentSource.lookup( "web" ) );
    if ( null == user.getEnrollmentDate() )
    {
      user.setEnrollmentDate( new Date() );
    }
    user.setId( new Long( id ) );
    user.setVersion( new Long( version ) );
  }

  /**
   * Sets the form data for login on the User object
   * 
   * @param user
   */
  public void setLoginInfo( User user )
  {
    user.setUserName( userName );
    /*
     * user.setPassword( password ); user.setSecretQuestionType( SecretQuestionType.lookup(
     * secretQuestion ) ); user.setSecretAnswer( secretAnswer );
     */
    user.setId( new Long( id ) );
    user.setVersion( new Long( version ) );

    // if password was system generated then set user flag to force the
    // user to change his/her password next time he/she logs in
    /*
     * if ( this.getPasswordSystemGenerated().booleanValue() ) { user.setPassword( password2 ); if (
     * getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_FORCE_RESET
     * ).getBooleanVal() ) { user.setForcePasswordChange( Boolean.TRUE.booleanValue() ); } else {
     * user.setForcePasswordChange( Boolean.FALSE.booleanValue() ); } }
     */
  }

  /**
   * Sets the form data for preferences on the Participant object
   * 
   * @param participant
   */
  public void setPreferences( Participant participant )
  {
    participant.setLanguageType( LanguageType.lookup( language ) );

    /*
     * //Create a list of selected ContactMethods ArrayList contactMethodPreferences = new
     * ArrayList(); for ( int j = 0; j < contactMethods.length; j++ ) { String contactMethodCode =
     * contactMethods[j]; ParticipantContactMethod contactMethod = new ParticipantContactMethod();
     * contactMethod.setContactMethodCode( ContactMethod.lookup(contactMethodCode) );
     * contactMethod.setParticipant( participant ); contactMethodPreferences.add( contactMethod ); }
     * //Remove any ContactMethods on the pax that are not selected Set contactMethods =
     * participant.getParticipantContactMethods(); for ( Iterator iter = contactMethods.iterator();
     * iter.hasNext(); ) { ParticipantContactMethod paxPreference =
     * (ParticipantContactMethod)iter.next(); if( !contactMethodPreferences.contains( paxPreference
     * ) ){ iter.remove(); } } //Add the selected ContactMethods for ( Iterator iter =
     * contactMethodPreferences.iterator(); iter.hasNext(); ) { ParticipantContactMethod preference
     * = (ParticipantContactMethod)iter.next(); participant.addParticipantContactMethod( preference
     * ); } //Create a list of selected ContactMethodTypes HashSet contactPreferences = new
     * HashSet(); if ( contactMethodTypes!= null){ for ( int i = 0; i < contactMethodTypes.length;
     * i++ ) { String code = contactMethodTypes[i]; ParticipantCommunicationPreference preference =
     * new ParticipantCommunicationPreference();
     * preference.setParticipantPreferenceCommunicationsType(
     * ParticipantPreferenceCommunicationsType.lookup( code ) ); preference.setParticipant(
     * participant ); contactPreferences.add( preference ); } } //Create a list of selected (active
     * text message) ContactMethodTypes ArrayList activeTextMessagePreferences = new ArrayList(); if
     * ( activeSMSGroupTypes!= null){ for ( int i = 0; i < activeSMSGroupTypes.length; i++) { String
     * messageId = activeSMSGroupTypes[i]; ParticipantCommunicationPreference preference = new
     * ParticipantCommunicationPreference(); preference.setParticipantPreferenceCommunicationsType(
     * ParticipantPreferenceCommunicationsType.lookup(
     * ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) ); preference.setTextMessageId( new
     * Long( messageId) ); preference.setParticipant( participant );
     * activeTextMessagePreferences.add( preference ); } } //remove contact preferences not in the
     * new lists Set communicationPreferences =
     * participant.getParticipantCommunicationPreferences(); for ( Iterator iter =
     * communicationPreferences.iterator(); iter.hasNext(); ) { ParticipantCommunicationPreference
     * paxPreference = (ParticipantCommunicationPreference)iter.next(); if(
     * !contactPreferences.contains( paxPreference ) && !activeTextMessagePreferences.contains(
     * paxPreference )){ iter.remove(); } } //Add all the selected ContactMethodTypes for ( Iterator
     * iter = contactPreferences.iterator(); iter.hasNext(); ) { ParticipantCommunicationPreference
     * preference = (ParticipantCommunicationPreference)iter.next();
     * participant.addCommunicationPreference( preference ); } //Add all the selected (active text
     * message) ContactMethodTypes for ( Iterator iter = activeTextMessagePreferences.iterator();
     * iter.hasNext(); ) { ParticipantCommunicationPreference preference =
     * (ParticipantCommunicationPreference)iter.next(); participant.addCommunicationPreference(
     * preference ); }
     */

    participant.setId( new Long( id ) );
    participant.setVersion( new Long( version ) );
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  // TODO :need to revisit, same validate method is being used for multiple actions
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( actionMapping.getPath().equalsIgnoreCase( "/participantMaintainUpdatePersonalInfo" ) || actionMapping.getPath().equalsIgnoreCase( "/participantMaintainCreate" ) )
    {
      Date formatDate = null;
      if ( dateOfBirth != null && dateOfBirth.length() > 0 )
      {
        formatDate = DateUtils.toDate( dateOfBirth );
        if ( !DateUtils.toDisplayString( formatDate ).equals( dateOfBirth ) )
        {
          actionErrors.add( "dateOfBirth", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.participant.DATE_OF_BIRTH" ) ) );
        }
      }
      if ( isShowSSOId() )
      {
        if ( !StringUtils.isEmpty( this.ssoId ) )
        {
          List<Participant> participants = getParticipantService().getUserBySSOId( this.ssoId );
          if ( participants != null && ( ( participants.size() == 1 && !participants.get( 0 ).getId().equals( new Long( this.userId ) ) ) || participants.size() > 1 ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.UNIQUE_SSO" ) );
          }
        }
        else
        {
          actionErrors.add( "ssoId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.participant.SSO_ID" ) ) );
        }
      }

      if ( !StringUtils.isEmpty( this.firstName ) && firstName.contains( "\"" ) )
      {
        actionErrors.add( "firstName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "participant.participant.FIRST_NAME" ) ) );
      }

      if ( !StringUtils.isEmpty( this.middleName ) && middleName.contains( "\"" ) )
      {
        actionErrors.add( "middleName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "participant.participant.MIDDLE_NAME" ) ) );
      }

      if ( !StringUtils.isEmpty( this.lastName ) && lastName.contains( "\"" ) )
      {
        actionErrors.add( "lastName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "participant.participant.LAST_NAME" ) ) );
      }

    }

    if ( actionMapping.getPath().equalsIgnoreCase( "/participantMaintainCreate" ) )
    {
      Date formatDate = null;
      if ( hireDate != null && hireDate.length() > 0 )
      {
        formatDate = DateUtils.toDate( hireDate );
        if ( !DateUtils.toDisplayString( formatDate ).equals( hireDate ) )
        {
          actionErrors.add( "hireDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.participant.HIRE_DATE" ) ) );

        }
      }
      if ( terminationDate != null && terminationDate.length() > 0 )
      {
        formatDate = DateUtils.toDate( terminationDate );
        if ( !DateUtils.toDisplayString( formatDate ).equals( terminationDate ) )
        {
          actionErrors.add( "terminationDate",
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.participant.TERMINATION_DATE" ) ) );

        }
      }
    }

    if ( actionMapping.getPath().equalsIgnoreCase( "/rosterMgmtAction" ) || actionMapping.getPath().equalsIgnoreCase( "/participantMaintainCreate" ) )
    {
      if ( !StringUtils.isEmpty( this.emailType ) )
      {
        if ( StringUtils.isEmpty( this.emailAddress ) )
        {
          actionErrors.add( "emailAddress",
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.roster.management.modify.EMAIL_ADDRESS" ) ) );
        }
        else if ( !GenericValidator.isEmail( this.emailAddress ) )
        {
          actionErrors.add( "emailAddress", new ActionMessage( "system.errors.EMAIL", CmsResourceBundle.getCmsBundle().getString( "participant.roster.management.modify.EMAIL_ADDRESS" ) ) );
        }
      }
      if ( !StringUtils.isEmpty( this.emailAddress ) )
      {
        if ( StringUtils.isEmpty( this.emailType ) )
        {
          actionErrors
              .add( "emailType",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.roster.management.modify.PRIMARY_EMAIL_TYPE" ) ) );
        }
      }
    }

    if ( actionMapping.getPath().equalsIgnoreCase( "/rosterMgmtAction" ) )
    {
      if ( StringUtils.isEmpty( this.userName ) )
      {
        actionErrors.add( "userName",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.roster.management.modify.LOGIN_ID" ) ) );
      }
      if ( this.method != null && this.method.equals( "rosterAdd" ) )
      {
        if ( getUserService().getUserByUserName( userName ) != null )
        {
          actionErrors.add( "userName", new ActionMessage( ServiceErrorMessageKeys.PARTICIPANT_DUPLICATE_USER_NAME, userName ) );
        }
      }
    }

    // If password has been system generated, no need to validate
    /*
     * if ( this.passwordSystemGenerated != null && !this.passwordSystemGenerated.booleanValue() ) {
     * if ( password != null && password.length() < 8 ) { actionErrors.add( "password", new
     * ActionMessage( "participant.errors.PASSWORD_LENGTH" ) ); } if ( password != null &&
     * !password.equals( confirmPassword ) ) { actionErrors.add( "password", new ActionMessage(
     * "participant.errors.PASSWORDS_DO_NOT_MATCH" ) ); } }
     */
    // get system variables on how T&Cs are used and set them on the Form
    this.setTermsConditionsRequired();
    this.setUserAllowedToAcceptForPax();

    // If T&Cs are required and the Pax Status has changed do validation
    if ( this.isTermsConditionsRequired() && this.paxStatus != null && this.originalPaxStatusFromDB != null && !this.paxStatus.equals( this.originalPaxStatusFromDB ) )
    {
      // Validate Pax Status can't be Active if T&Cs not accepted

      if ( this.paxStatus.equals( ParticipantStatus.ACTIVE ) && this.paxTermsAccept != null && this.paxTermsAccept.equals( ParticipantTermsAcceptance.NOTACCEPTED ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.PAX_STATUS_INVALID" ) );
      }
    }

    if ( this.paxStatus != null && this.paxStatus.equals( ParticipantStatus.ACTIVE ) && this.originalPaxStatusFromDB != null && !this.paxStatus.equals( this.originalPaxStatusFromDB )
        && this.optOutOfProgram )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.PAX_STATUS_INVALID_OPT_OUT_PROGRAM" ) );
    }

    // if the paxStatus changed to active, validate node relationship rules since they will all be
    // reactivated
    if ( !StringUtils.isEmpty( this.originalPaxStatusFromDB ) && !this.paxStatus.equals( this.originalPaxStatusFromDB ) && this.paxStatus.equals( ParticipantStatus.ACTIVE ) )
    {
      for ( Iterator iter = getUserService().getUserNodes( new Long( this.userId ) ).iterator(); iter.hasNext(); )
      {
        UserNode userNode = (UserNode)iter.next();
        /*
         * // AssociationRequestCollection nodeAssociationRequestCollection = new //
         * AssociationRequestCollection(); // nodeAssociationRequestCollection.add( new
         * NodeToUsersAssociationRequest() ); // Node node =
         * getNodeService().getNodeWithAssociationsById( userNode.getNode().getId(), //
         * nodeAssociationRequestCollection ); // // if ( node != null && node.hasOwner() &&
         * userNode.getHierarchyRoleType().getCode().equals( // HierarchyRoleType.OWNER ) ) // { //
         * Set owners = node.getUsersByRole( HierarchyRoleType.OWNER ); // for ( Iterator iterator =
         * owners.iterator(); iterator.hasNext(); ) // { // User owner = (User)iterator.next(); //
         * if ( Long.parseLong( getUserId() ) != owner.getId().longValue() ) // { //
         * actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( //
         * "participant.errors.NODE_ALREADY_HAS_OWNER_DETAILED", userNode.getNode().getName() ) );
         * // } // } // }
         */
        if ( userNode.getHierarchyRoleType().getCode().equals( HierarchyRoleType.OWNER ) && getNodeService().isOwnerAlreadyInUserNode( Long.parseLong( getUserId() ), userNode.getNode().getId() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.NODE_ALREADY_HAS_OWNER_DETAILED", userNode.getNode().getName() ) );
        }
      }
    }

    // Get pax's termination date, if any
    if ( !StringUtil.isEmpty( this.userId ) && this.paxStatus != null )
    {
      ParticipantEmployer paxEmployer = getParticipantService().getCurrentParticipantEmployer( new Long( this.userId ) );
      if ( paxEmployer != null && paxEmployer.getTerminationDate() != null && paxEmployer.getTerminationDate().getTime() < new Date().getTime() )
      {
        // Validate Pax Status can't be Active if Pax has been terminated
        if ( this.paxStatus != null && this.originalPaxStatusFromDB != null && !this.paxStatus.equals( this.originalPaxStatusFromDB ) && this.paxStatus.equals( ParticipantStatus.ACTIVE ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.CANNOT_CHANGE_PAX_STATUS" ) );
        }
      }
    }

    if ( !StringUtils.isEmpty( addressType ) )
    {
      actionErrors = addressFormBean.validateAddress( actionErrors );
    }

    if ( DateUtils.toDate( hireDate ) != null && DateUtils.toDate( terminationDate ) != null && DateUtils.toDate( hireDate ).after( DateUtils.toDate( terminationDate ) ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.START_DATE_AFTER_END_DATE" ) );
    }

    if ( getUserCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.validateCharacteristicValueList( userCharacteristicValueList, actionErrors );
    }

    if ( !StringUtils.isEmpty( this.phoneType ) )
    {
      if ( StringUtils.isEmpty( this.phoneNumber ) )
      {
        actionErrors.add( "phoneNumber", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.participant.PHONE_NUMBER" ) ) );
      }
    }

    if ( !StringUtils.isEmpty( this.phoneNumber ) )
    {
      if ( StringUtils.isEmpty( this.phoneType ) )
      {
        actionErrors.add( "phoneType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.errors.PHONE_TYPE_REQUIRE" ) ) );
      }
    }

    // If user wants eStatements, then an email address must be specified
    if ( contactMethodTypes != null && contactMethodTypes.length > 0 )
    {
      for ( int i = 0; i < contactMethodTypes.length; i++ )
      {
        String contactMethodType = contactMethodTypes[i];
        if ( contactMethodType.equals( ParticipantPreferenceCommunicationsType.E_STATEMENTS ) )
        {
          if ( StringUtils.isEmpty( emailAddress ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.E_STATEMENT_ADDRESS" ) );
          }
        }
      }
    }

    if ( getNodeRelationship() != null && getNodeRelationship().equals( HierarchyRoleType.OWNER ) )
    {
      Node node = null;
      if ( !StringUtils.isEmpty( nodeId ) )
      {
        AssociationRequestCollection ascReqCollection = new AssociationRequestCollection();
        ascReqCollection.add( new NodeToUserNodesAssociationRequest() );
        node = getNodeService().getNodeWithAssociationsById( new Long( getNodeId() ), ascReqCollection );
      }
      else
      {
        Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
        // BugFix 19193 Hydrate UserNodes in node object
        AssociationRequestCollection ascReqCollection = new AssociationRequestCollection();
        ascReqCollection.add( new NodeToUserNodesAssociationRequest() );
        node = getNodeService().getNodeByNameAndHierarchy( nameOfNode, primaryHierarchy, ascReqCollection );
      }
      if ( node != null && node.hasOwner() )
      {
        AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
        userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ROLE ) );

        List owners = getUserService().getAllUsersOnNodeHavingRole( node.getId(), HierarchyRoleType.lookup( HierarchyRoleType.OWNER ), userAssociationRequestCollection );

        User owner = (User)owners.iterator().next();
        if ( getId() == 0 || getId() > 0 && owner.getId().longValue() != getId() )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.NODE_ALREADY_HAS_OWNER", getUserName() ) );
        }
      }
    }
    if ( !StringUtils.isEmpty( language ) )
    {
      LanguageType selectedLanguage = LanguageType.lookup( language );
      if ( selectedLanguage == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.SELECTED_LANGUAGE_IS_INACTIVE", getUserName() ) );
      }
    }

    return actionErrors;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // UserCharacteristicFormBeans. If this is not done, the form wont initialize
    // properly.
    userCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "userCharacteristicValueListCount" ) );
  } // end reset

  public String getDateOfBirth()
  {
    return dateOfBirth;
  }

  public void setDateOfBirth( String dateOfBirth )
  {
    this.dateOfBirth = dateOfBirth;
  }

  public String getDateOfBirthDisplayString()
  {
    return dateOfBirthDisplayString;
  }

  public void setDateOfBirthDisplayString( String dateOfBirthDisplayString )
  {
    this.dateOfBirthDisplayString = dateOfBirthDisplayString;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getEmailType()
  {
    return emailType;
  }

  public void setEmailType( String emailType )
  {
    this.emailType = emailType;
  }

  public String getEmployerId()
  {
    return employerId;
  }

  public void setEmployerId( String employerId )
  {
    this.employerId = employerId;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getGender()
  {
    return gender;
  }

  public void setGender( String gender )
  {
    this.gender = gender;
  }

  public String getHireDate()
  {
    return hireDate;
  }

  public void setHireDate( String hireDate )
  {
    this.hireDate = hireDate;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( String nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeRelationship()
  {
    return nodeRelationship;
  }

  public void setNodeRelationship( String nodeRelationship )
  {
    this.nodeRelationship = nodeRelationship;
  }

  /*
   * public String getPassword() { return password; } public void setPassword( String password ) {
   * this.password = password; }
   */
  public String getPaxStatus()
  {
    return paxStatus;
  }

  public void setPaxStatus( String paxStatus )
  {
    this.paxStatus = paxStatus;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public String getPhoneType()
  {
    return phoneType;
  }

  public void setPhoneType( String phoneType )
  {
    this.phoneType = phoneType;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }

  public String getSecretAnswer()
  {
    return secretAnswer;
  }

  public void setSecretAnswer( String secretAnswer )
  {
    this.secretAnswer = secretAnswer;
  }

  public String getSecretAnswerDesc()
  {
    return secretAnswerDesc;
  }

  public void setSecretAnswerDesc( String secretAnswerDesc )
  {
    this.secretAnswerDesc = secretAnswerDesc;
  }

  public String getSecretQuestion()
  {
    return secretQuestion;
  }

  public void setSecretQuestion( String secretQuestion )
  {
    this.secretQuestion = secretQuestion;
  }

  public String getSsn()
  {
    return ssn;
  }

  public String getMaskedSsn()
  {
    return maskedSsn;
  }

  public void setSsn( String ssn )
  {
    this.ssn = ssn;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public void setSuffix( String suffix )
  {
    this.suffix = suffix;
  }

  public String getTerminationDate()
  {
    return terminationDate;
  }

  public void setTerminationDate( String terminationDate )
  {
    this.terminationDate = terminationDate;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getSuspensionStatus()
  {
    return suspensionStatus;
  }

  public void setSuspensionStatus( String suspensionStatus )
  {
    this.suspensionStatus = suspensionStatus;
  }

  public String getNameOfNode()
  {
    return nameOfNode;
  }

  public void setNameOfNode( String nameOfNode )
  {
    this.nameOfNode = nameOfNode;
  }

  public String getAwardbanqNumber()
  {
    return awardbanqNumber;
  }

  public void setAwardbanqNumber( String awardbanqNumber )
  {
    this.awardbanqNumber = awardbanqNumber;
  }

  public String getCentraxId()
  {
    return centraxId;
  }

  public void setCentraxId( String centraxId )
  {
    this.centraxId = centraxId;
  }

  public String getPasswordStatus()
  {
    return passwordStatus;
  }

  public void setPasswordStatus( String passwordStatus )
  {
    this.passwordStatus = passwordStatus;
  }

  public List getUserCharacteristics()
  {
    return userCharacteristics;
  }

  public void setUserCharacteristics( List userCharacteristics )
  {
    this.userCharacteristics = userCharacteristics;
  }

  public List getUserNodes()
  {
    return userNodes;
  }

  public void setUserNodes( List userNodes )
  {
    this.userNodes = userNodes;
  }

  public String getGenderDesc()
  {
    return genderDesc;
  }

  public void setGenderDesc( String genderDesc )
  {
    this.genderDesc = genderDesc;
  }

  public String getPaxStatusDesc()
  {
    return paxStatusDesc;
  }

  public void setPaxStatusDesc( String paxStatusDesc )
  {
    this.paxStatusDesc = paxStatusDesc;
  }

  public String getSuffixDesc()
  {
    return suffixDesc;
  }

  public void setSuffixDesc( String suffixDesc )
  {
    this.suffixDesc = suffixDesc;
  }

  public String getSuspensionStatusDesc()
  {
    return suspensionStatusDesc;
  }

  public void setSuspensionStatusDesc( String suspensionStatusDesc )
  {
    this.suspensionStatusDesc = suspensionStatusDesc;
  }

  public String getTitleDesc()
  {
    return titleDesc;
  }

  public void setTitleDesc( String titleDesc )
  {
    this.titleDesc = titleDesc;
  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public List getUserCharacteristicValueList()
  {
    return userCharacteristicValueList;
  }

  public void setUserCharacteristicValueList( List valueList )
  {
    this.userCharacteristicValueList = valueList;
  }

  public int getUserCharacteristicValueListCount()
  {
    if ( userCharacteristicValueList != null )
    {
      return userCharacteristicValueList.size();
    }
    return 0;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CharacteristicFormBean from the value list
   */
  public CharacteristicValueBean getUserCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)userCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getConfirmPassword()
  {
    return confirmPassword;
  }

  public void setConfirmPassword( String confirmPassword )
  {
    this.confirmPassword = confirmPassword;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage( String language )
  {
    this.language = language;
  }

  public String[] getContactMethods()
  {
    return contactMethods;
  }

  public void setContactMethods( String[] contactMethods )
  {
    this.contactMethods = contactMethods;
  }

  public String getPrimaryContactMethod()
  {
    return primaryContactMethod;
  }

  public void setPrimaryContactMethod( String primaryContactMethod )
  {
    this.primaryContactMethod = primaryContactMethod;
  }

  public AddressFormBean getAddressFormBean()
  {
    return addressFormBean;
  }

  public void setAddressFormBean( AddressFormBean addressFormBean )
  {
    this.addressFormBean = addressFormBean;
  }

  public String getAddressType()
  {
    return addressType;
  }

  public void setAddressType( String addressType )
  {
    this.addressType = addressType;
  }

  public String getAddressTypeDesc()
  {
    return addressTypeDesc;
  }

  public void setAddressTypeDesc( String addressTypeDesc )
  {
    this.addressTypeDesc = addressTypeDesc;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public String getActive()
  {
    return active;
  }

  public void setActive( String active )
  {
    this.active = active;
  }

  public String getSecretQuestionDesc()
  {
    return secretQuestionDesc;
  }

  public void setSecretQuestionDesc( String secretQuestionDesc )
  {
    this.secretQuestionDesc = secretQuestionDesc;
  }

  public String getEmailTypeDesc()
  {
    return emailTypeDesc;
  }

  public void setEmailTypeDesc( String emailTypeDesc )
  {
    this.emailTypeDesc = emailTypeDesc;
  }

  public String getPhoneTypeDesc()
  {
    return phoneTypeDesc;
  }

  public void setPhoneTypeDesc( String phoneTypeDesc )
  {
    this.phoneTypeDesc = phoneTypeDesc;
  }

  public String[] getRoles()
  {
    return roles;
  }

  public void setRoles( String[] roles )
  {
    this.roles = roles;
  }

  public String getUserType()
  {
    return userType;
  }

  public void setUserType( String userType )
  {
    this.userType = userType;
  }

  public List getUserRoles()
  {
    return userRoles;
  }

  public void setUserRoles( List userRoles )
  {
    this.userRoles = userRoles;
  }

  public String getActiveDesc()
  {
    return activeDesc;
  }

  public void setActiveDesc( String activeDesc )
  {
    this.activeDesc = activeDesc;
  }

  public String getUserTypeDesc()
  {
    return userTypeDesc;
  }

  public void setUserTypeDesc( String userTypeDesc )
  {
    this.userTypeDesc = userTypeDesc;
  }

  /**
   * Accessor for Internal UserTypeCode
   * 
   * @return String userType Constant for Internal
   */
  public String getUserTypeCodeInternal()
  {
    return UserType.BI;
  }

  public String getEnrollmentSourceDesc()
  {
    return enrollmentSourceDesc;
  }

  public void setEnrollmentSourceDesc( String enrollmentSourceDesc )
  {
    this.enrollmentSourceDesc = enrollmentSourceDesc;
  }

  public String getEnrollmentDate()
  {
    return enrollmentDate;
  }

  public void setEnrollmentDate( String enrollmentDate )
  {
    this.enrollmentDate = enrollmentDate;
  }

  public String getTextMessageAddress()
  {
    return textMessageAddress;
  }

  public void setTextMessageAddress( String textMessageAddress )
  {
    this.textMessageAddress = textMessageAddress;
  }

  public boolean isHasFax()
  {
    return hasFax;
  }

  public void setHasFax( boolean hasFax )
  {
    this.hasFax = hasFax;
  }

  public String[] getactiveSMSGroupTypes()
  {
    return activeSMSGroupTypes;
  }

  public void setactiveSMSGroupTypes( String[] activeSMSGroupTypes )
  {
    this.activeSMSGroupTypes = activeSMSGroupTypes;
  }

  public String[] getContactMethodTypes()
  {
    return contactMethodTypes;
  }

  public void setContactMethodTypes( String[] contactMethodTypes )
  {
    this.contactMethodTypes = contactMethodTypes;
  }

  public boolean isViewCurrentUser()
  {
    return viewCurrentUser;
  }

  public void setViewCurrentUser( boolean viewCurrentUser )
  {
    this.viewCurrentUser = viewCurrentUser;
  }

  public String getPhoneExtension()
  {
    return phoneExtension;
  }

  public void setPhoneExtension( String phoneExtension )
  {
    this.phoneExtension = phoneExtension;
  }

  /**
   * Check Sysvar
   * 
   * @return true if this client program requires Paxs to accept Terms & Conditions otherwise return
   *         false if T&Cs not required
   */
  private boolean isTermsAndConditionsUsed()
  {
    boolean isTermsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
    return isTermsAndConditionsUsed;
  }

  /**
   * Check Sysvar
   * 
   * @return true if an User can accept the Terms & Conditions on behalf of the Pax otherwise return
   *         false if user cannot accept for the pax.
   */
  private boolean canUserAcceptTermsAndConditionsForPax()
  {
    boolean canUserAcceptTermsAndConditionsForPax = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USER_CAN_ACCEPT ).getBooleanVal();
    return canUserAcceptTermsAndConditionsForPax;
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)BeanLocator.getBean( HierarchyService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  /**
   * Default Pax Status and T&Cs Acceptance based on Sysvars
   */
  public void defaultTermsAndConditions()
  {

    // get system variables on how T&Cs are used and set them on the Form
    this.setTermsConditionsRequired();
    this.setUserAllowedToAcceptForPax();

    if ( this.getPaxTermsAccept() == null || !this.getPaxTermsAccept().equals( ParticipantTermsAcceptance.ACCEPTED ) )
    {
      // if T&Cs on,
      // defaults the Pax Status to Inactive and T&Cs Acceptance to Not Accepted
      if ( this.isTermsConditionsRequired() )
      {
        this.setPaxStatus( ParticipantStatus.INACTIVE );
        this.setPaxStatusDesc( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ).getName() );
        this.setPaxTermsAccept( ParticipantTermsAcceptance.NOTACCEPTED );
        this.setPaxTermsAcceptDesc( "not accepted" );
      }
      else
      {
        // if T&Cs off,
        // defaults the Pax Status to Active and T&Cs Acceptance to Not Accepted
        this.setPaxStatus( ParticipantStatus.ACTIVE );
        this.setPaxStatusDesc( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ).getName() );
        this.setPaxTermsAccept( ParticipantTermsAcceptance.NOTACCEPTED );
        this.setPaxTermsAcceptDesc( "not accepted" );
      }
    }
  }

  public Boolean getPasswordSystemGenerated()
  {
    return passwordSystemGenerated;
  }

  public void setPasswordSystemGenerated( Boolean passwordSystemGenerated )
  {
    this.passwordSystemGenerated = passwordSystemGenerated;
  }

  public String getPassword2()
  {
    return password2;
  }

  public void setPassword2( String password2 )
  {
    this.password2 = password2;
  }

  public String getConfirmPassword2()
  {
    return confirmPassword2;
  }

  public void setConfirmPassword2( String confirmPassword2 )
  {
    this.confirmPassword2 = confirmPassword2;
  }

  public String getPaxTermsAccept()
  {
    return paxTermsAccept;
  }

  public void setPaxTermsAccept( String paxTermsAccept )
  {
    this.paxTermsAccept = paxTermsAccept;
  }

  public String getPaxTermsAcceptDesc()
  {
    return paxTermsAcceptDesc;
  }

  public void setPaxTermsAcceptDesc( String paxTermsAcceptDesc )
  {
    this.paxTermsAcceptDesc = paxTermsAcceptDesc;
  }

  public boolean isTermsConditionsRequired()
  {
    return termsConditionsRequired;
  }

  public void setTermsConditionsRequired()
  {
    this.termsConditionsRequired = this.isTermsAndConditionsUsed();
  }

  public boolean isUserAllowedToAcceptForPax()
  {
    return userAllowedToAcceptForPax;
  }

  public void setUserAllowedToAcceptForPax()
  {
    this.userAllowedToAcceptForPax = this.canUserAcceptTermsAndConditionsForPax();
  }

  public String getTermsAcceptedBy()
  {
    return termsAcceptedBy;
  }

  public void setTermsAcceptedBy( String termsAcceptedBy )
  {
    this.termsAcceptedBy = termsAcceptedBy;
  }

  public String getTermsAcceptedDate()
  {
    return termsAcceptedDate;
  }

  public void setTermsAcceptedDate( String termsAcceptedDate )
  {
    this.termsAcceptedDate = termsAcceptedDate;
  }

  public String getOriginalPaxTermsAcceptFromDB()
  {
    return originalPaxTermsAcceptFromDB;
  }

  public void setOriginalPaxTermsAcceptFromDB( String originalPaxTermsAcceptFromDB )
  {
    this.originalPaxTermsAcceptFromDB = originalPaxTermsAcceptFromDB;
  }

  public String getOriginalPaxStatusFromDB()
  {
    return originalPaxStatusFromDB;
  }

  public void setOriginalPaxStatusFromDB( String originalPaxStatusFromDB )
  {
    this.originalPaxStatusFromDB = originalPaxStatusFromDB;
  }

  public String getAcceptTermsOnTextMessages()
  {
    return acceptTermsOnTextMessages;
  }

  public void setAcceptTermsOnTextMessages( String acceptTermsOnTextMessages )
  {
    this.acceptTermsOnTextMessages = acceptTermsOnTextMessages;
  }

  public String getTextPhoneNbr()
  {
    return textPhoneNbr;
  }

  public void setTextPhoneNbr( String textPhoneNbr )
  {
    this.textPhoneNbr = textPhoneNbr;
  }

  public boolean isWelcomeEmailSent()
  {
    return welcomeEmailSent;
  }

  public void setWelcomeEmailSent( boolean welcomeEmailSent )
  {
    this.welcomeEmailSent = welcomeEmailSent;
  }

  public boolean isRaWelcomeEmailSent()
  {
    return raWelcomeEmailSent;
  }

  public void setRaWelcomeEmailSent( boolean raWelcomeEmailSent )
  {
    this.raWelcomeEmailSent = raWelcomeEmailSent;
  }

  public boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

  public String getUid()
  {
    return uid;
  }

  public void setUid( String uid )
  {
    this.uid = uid;
  }

  public String getOauth_token()
  {
    return oauthToken;
  }

  public void setOauth_token( String oauthToken )
  {
    this.oauthToken = oauthToken;
  }

  public String getOauth_verifier()
  {
    return oauthVerifier;
  }

  public void setOauth_verifier( String outhVerifier )
  {
    this.oauthVerifier = outhVerifier;
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

  public String getAwardbanqNumberEncrypted()
  {
    String s = new String();
    // To Fix the bug 20617
    if ( this.awardbanqNumber != null )
    {
      for ( int i = 0; i < this.awardbanqNumber.length(); i++ )
      {
        s = s + "*";
      }
    }
    return s;
  }

  public String getCentraxIdEncrypted()
  {
    String s = new String();
    // To Fix the bug 20617
    if ( this.centraxId != null )
    {
      for ( int i = 0; i < this.centraxId.length(); i++ )
      {
        s = s + "*";
      }
    }
    return s;
  }

  public Long getRosterManagerId()
  {
    return rosterManagerId;
  }

  public void setRosterManagerId( Long rosterManagerId )
  {
    this.rosterManagerId = rosterManagerId;
  }

  public Long getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( Long proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public void setAllowPublicRecognition( boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public String getDisplayCountryPhoneCode()
  {
    return displayCountryPhoneCode;
  }

  public void setDisplayCountryPhoneCode( String displayCountryPhoneCode )
  {
    this.displayCountryPhoneCode = displayCountryPhoneCode;
  }

  public Long getRosterPaxuserId()
  {
    return rosterPaxuserId;
  }

  public void setRosterPaxuserId( Long rosterPaxuserId )
  {
    this.rosterPaxuserId = rosterPaxuserId;
  }

  public boolean isAllowPublicInformation()
  {
    return allowPublicInformation;
  }

  public void setAllowPublicInformation( boolean allowPublicInformation )
  {
    this.allowPublicInformation = allowPublicInformation;
  }

  public String getSsoId()
  {
    return ssoId;
  }

  public void setSsoId( String ssoId )
  {
    this.ssoId = ssoId;
  }

  public boolean isShowSSOId()
  {
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_UNIQUE_ID ).getStringVal().equalsIgnoreCase( SSO_ID ) )
    {
      showSSOId = true;
    }
    return showSSOId;
  }

  public void setShowSSOId( boolean showSSOId )
  {
    this.showSSOId = showSSOId;
  }

  public void setSsnEditable( boolean ssnEditable )
  {
    this.ssnEditable = ssnEditable;
  }

  public boolean isSsnEditable()
  {
    return ssnEditable;
  }

  public boolean isShowThrowdownPlayerStats()
  {
    return showThrowdownPlayerStats;
  }

  public void setShowThrowdownPlayerStats( boolean showThrowdownPlayerStats )
  {
    this.showThrowdownPlayerStats = showThrowdownPlayerStats;
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

  public String getSsiAdminAction()
  {
    return ssiAdminAction;
  }

  public void setSsiAdminAction( String ssiAdminAction )
  {
    this.ssiAdminAction = ssiAdminAction;
  }

  public String getOptOutAwardsStatusDesc()
  {
    return optOutAwardsStatusDesc;
  }

  public void setOptOutAwardsStatusDesc( String optOutAwardsStatusDesc )
  {
    this.optOutAwardsStatusDesc = optOutAwardsStatusDesc;
  }

  public boolean isOptOutOfProgram()
  {
    return optOutOfProgram;
  }

  public void setOptOutOfProgram( boolean optOutOfProgram )
  {
    this.optOutOfProgram = optOutOfProgram;
  }

  public String getOptOutOfProgramStatusDesc()
  {
    return optOutOfProgramStatusDesc;
  }

  public void setOptOutOfProgramStatusDesc( String optOutOfProgramStatusDesc )
  {
    this.optOutOfProgramStatusDesc = optOutOfProgramStatusDesc;
  }

  public boolean isUaAuthorized()
  {
    return uaAuthorized;
  }

  public void setUaAuthorized( boolean uaAuthorized )
  {
    this.uaAuthorized = uaAuthorized;
  }

  public boolean isUaEnabled()
  {
    return uaEnabled;
  }

  public void setUaEnabled( boolean uaEnabled )
  {
    this.uaEnabled = uaEnabled;
  }

  public String getUaOuthUrl()
  {
    return uaOuthUrl;
  }

  public void setUaOuthUrl( String uaOuthUrl )
  {
    this.uaOuthUrl = uaOuthUrl;
  }

  public boolean isUaDeAuthorize()
  {
    return uaDeAuthorize;
  }

  public void setUaDeAuthorize( boolean uaDeAuthorize )
  {
    this.uaDeAuthorize = uaDeAuthorize;
  }

  public boolean isActivationComplete()
  {
    return activationComplete;
  }

  public void setActivationComplete( boolean activationComplete )
  {
    this.activationComplete = activationComplete;
  }

  public String getActivationCompleteDesc()
  {
    return activationCompleteDesc;
  }

  public void setActivationCompleteDesc( String activationCompleteDesc )
  {
    this.activationCompleteDesc = activationCompleteDesc;
  }

  public String getUaLogOutUrl()
  {
    return uaLogOutUrl;
  }

  public void setUaLogOutUrl( String uaLogOutUrl )
  {
    this.uaLogOutUrl = uaLogOutUrl;
  }

  public Long getHoneycombUserId()
  {
    return honeycombUserId;
  }

  public void setHoneycombUserId( Long honeycombUserId )
  {
    this.honeycombUserId = honeycombUserId;
  }

  // Client customizations for wip #26532 starts
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
}
