
package com.biperf.core.ui.selfEnrollment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.ParticipantEnrollmentSource;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.PhoneNumberValidator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.AboutMeValueBean;
import com.biperf.core.value.CharacteristicValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SelfEnrollmentPaxRegistrationForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String registrationCode = "";
  private String method;
  private String firstName;
  private String middleName;
  private String lastName;
  private String ssn;
  private String userName;
  private String termsAndConditions;
  private String inputPassword;
  private String jobTitle;
  private String department;
  private String addressType;
  private String country;
  private AddressFormBean addressFormBean = new AddressFormBean();
  private String emailType;
  private String emailAddress;
  private String phoneType;
  private String countryPhoneCode;
  private String phoneNumber;
  private String phoneExtension;
  private boolean showTermsAndConditions;
  private ArrayList<AboutMeValueBean> aboutMeQuestions = new ArrayList<AboutMeValueBean>();
  private String nodeId;
  private String nodeName;
  private String smsPhoneNumber;
  private String[] activeSMSGroupTypes;
  private String avatarUrl;
  private String telephoneCountryCode;
  private String txtAlertTerms;
  private FormFile profileImage;
  private String captchaResponse;
  private List userCharacteristicValueList = new ArrayList();
  private List userCharacteristics = new ArrayList();
  private String selfEnrolledLanguageCode;

  // bug fix 56289 in bugzilla
  private boolean countryAllowSms;

  private long version;
  private long id;

  public List getUserCharacteristics()
  {
    return userCharacteristics;
  }

  public void setUserCharacteristics( List userCharacteristics )
  {
    this.userCharacteristics = userCharacteristics;
  }

  public List getUserCharacteristicValueList()
  {
    return userCharacteristicValueList;
  }

  public void setUserCharacteristicValueList( List userCharacteristicValueList )
  {
    this.userCharacteristicValueList = userCharacteristicValueList;
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

  public String getRegistrationCode()
  {
    return registrationCode;
  }

  public void setRegistrationCode( String registrationCode )
  {
    this.registrationCode = registrationCode;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
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

  public String getTermsAndConditions()
  {
    return termsAndConditions;
  }

  public void setTermsAndConditions( String termsAndConditions )
  {
    this.termsAndConditions = termsAndConditions;
  }

  public String getInputPassword()
  {
    return inputPassword;
  }

  public void setInputPassword( String inputPassword )
  {
    this.inputPassword = inputPassword;
  }

  public String getJobTitle()
  {
    return jobTitle;
  }

  public void setJobTitle( String jobTitle )
  {
    this.jobTitle = jobTitle;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getAddressType()
  {
    return addressType;
  }

  public void setAddressType( String addressType )
  {
    this.addressType = addressType;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public AddressFormBean getAddressFormBean()
  {
    return addressFormBean;
  }

  public void setAddressFormBean( AddressFormBean addressFormBean )
  {
    this.addressFormBean = addressFormBean;
  }

  public String getEmailType()
  {
    return emailType;
  }

  public void setEmailType( String emailType )
  {
    this.emailType = emailType;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getPhoneType()
  {
    return phoneType;
  }

  public void setPhoneType( String phoneType )
  {
    this.phoneType = phoneType;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneExtension()
  {
    return phoneExtension;
  }

  public void setPhoneExtension( String phoneExtension )
  {
    this.phoneExtension = phoneExtension;
  }

  public boolean isShowTermsAndConditions()
  {
    return showTermsAndConditions;
  }

  public void setShowTermsAndConditions( boolean showTermsAndConditions )
  {
    this.showTermsAndConditions = showTermsAndConditions;
  }

  public ArrayList<AboutMeValueBean> getAboutMeQuestions()
  {
    return aboutMeQuestions;
  }

  public void setAboutMeQuestions( ArrayList<AboutMeValueBean> aboutMeQuestions )
  {
    this.aboutMeQuestions = aboutMeQuestions;
  }

  public String getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( String nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  // NodeName2-------------------------------------------
  /*
   * NOTE: Apparently javascript or jquery gets choked up having an element whose name is
   * "nodeName". Sounds like it has to do with versions of jquery 1.8+. See:
   * http://stackoverflow.com/questions/17329025/is-id-nodename-reserved-in-html5 <html:hidden
   * property="nodeName"/> renders <input type="hidden" value="Tucson" name="nodeName"> (this causes
   * issues) <html:hidden property="nodeName2"/> renders <input type="hidden" value="Tucson"
   * name="nodeName2"> (this works) Therefore, I added getter/setter for nodeName2 to get beyond
   * this issue. But left the original getter/setter for backward compatibility
   */
  public String getNodeName2()
  {
    return nodeName;
  }

  public void setNodeName2( String nodeName )
  {
    this.nodeName = nodeName;
  }
  // NodeName2-------------------------------------------

  public String getSmsPhoneNumber()
  {
    return smsPhoneNumber;
  }

  public void setSmsPhoneNumber( String smsPhoneNumber )
  {
    this.smsPhoneNumber = smsPhoneNumber;
  }

  public String[] getActiveSMSGroupTypes()
  {
    return activeSMSGroupTypes;
  }

  public void setActiveSMSGroupTypes( String[] activeSMSGroupTypes )
  {
    this.activeSMSGroupTypes = activeSMSGroupTypes;
  }

  public String getTelephoneCountryCode()
  {
    return telephoneCountryCode;
  }

  public void setTelephoneCountryCode( String telephoneCountryCode )
  {
    this.telephoneCountryCode = telephoneCountryCode;
  }

  public String getTxtAlertTerms()
  {
    return txtAlertTerms;
  }

  public void setTxtAlertTerms( String txtAlertTerms )
  {
    this.txtAlertTerms = txtAlertTerms;
  }

  public FormFile getProfileImage()
  {
    return profileImage;
  }

  public void setProfileImage( FormFile profileImage )
  {
    this.profileImage = profileImage;
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public String getCaptchaResponse()
  {
    return captchaResponse;
  }

  public void setCaptchaResponse( String captchaResponse )
  {
    this.captchaResponse = captchaResponse;
  }

  public boolean isCountryAllowSms()
  {
    return countryAllowSms;
  }

  public void setCountryAllowSms( boolean countryAllowSms )
  {
    this.countryAllowSms = countryAllowSms;
  }

  public String getSelfEnrolledLanguageCode()
  {
    return selfEnrolledLanguageCode;
  }

  public void setSelfEnrolledLanguageCode( String selfEnrolledLanguageCode )
  {
    this.selfEnrolledLanguageCode = selfEnrolledLanguageCode;
  }

  public Participant createParticipantDomainObj( Employer employer )
  {
    Participant pax = new Participant();
    setUserInfo( pax );
    setParticipantPersonalInfo( pax );
    setCommunicationPreferences( pax );
    // Make sure these are null for the insert
    pax.setId( null );
    pax.setVersion( null );

    if ( employer != null )
    {

      List<ParticipantEmployer> participantEmployers = new ArrayList<ParticipantEmployer>();

      ParticipantEmployer participantEmployer = new ParticipantEmployer();
      participantEmployer.setDepartmentType( department );
      participantEmployer.setPositionType( jobTitle );
      participantEmployer.setParticipant( pax );
      participantEmployer.setEmployer( employer );
      AuditCreateInfo info = new AuditCreateInfo();
      info.setCreatedBy( (long)0 );
      info.setDateCreated( new Timestamp( DateUtils.getCurrentDate().getTime() ) );
      participantEmployer.setAuditCreateInfo( info );

      participantEmployers.add( participantEmployer );

      pax.setParticipantEmployers( participantEmployers );

    }

    return pax;
  }

  protected void setCommunicationPreferences( Participant pax )
  {
    if ( activeSMSGroupTypes != null )
    {
      for ( String code : activeSMSGroupTypes )
      {
        ParticipantCommunicationPreference preference = new ParticipantCommunicationPreference();
        preference.setMessageSMSGroupType( MessageSMSGroupType.lookup( code ) );
        preference.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) );
        pax.addParticipantCommunicationPreference( preference );
      }
    }
    if ( isAllowEstatements() )
    {
      ParticipantCommunicationPreference participantCommunicationPref = new ParticipantCommunicationPreference();
      participantCommunicationPref.setParticipant( pax );
      participantCommunicationPref.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.E_STATEMENTS ) );
      pax.addParticipantCommunicationPreference( participantCommunicationPref );
    }
  }

  private void setUserInfo( User user )
  {
    setPersonalInfo( user );
    setLoginInfo( user );
    setUserNodesInfo( user );

    if ( !StringUtil.isNullOrEmpty( addressType ) )
    {
      UserAddress userAddress = new UserAddress();
      userAddress.setAddressType( AddressType.lookup( this.addressType ) );
      userAddress.setAddress( this.addressFormBean.toDomainObject() );
      userAddress.setIsPrimary( Boolean.TRUE );
      user.addUserAddress( userAddress );
    }

    if ( !StringUtil.isNullOrEmpty( emailType ) )
    {
      UserEmailAddress userEmail = new UserEmailAddress();
      userEmail.setEmailType( EmailAddressType.lookup( emailType ) );
      userEmail.setEmailAddr( emailAddress );
      userEmail.setIsPrimary( Boolean.valueOf( true ) );
      userEmail.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      user.addUserEmailAddress( userEmail );
    }

    if ( !StringUtil.isNullOrEmpty( phoneType ) )
    {
      UserPhone userPhone = new UserPhone();
      userPhone.setPhoneType( PhoneType.lookup( phoneType ) );
      userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      userPhone.setPhoneNbr( phoneNumber );
      userPhone.setPhoneExt( phoneExtension );
      userPhone.setCountryPhoneCode( countryPhoneCode );
      userPhone.setIsPrimary( Boolean.valueOf( true ) );
      user.addUserPhone( userPhone );
    }

    if ( activeSMSGroupTypes != null )
    {
      UserPhone userPhone = new UserPhone();
      userPhone.setCountryPhoneCode( countryPhoneCode );
      userPhone.setIsPrimary( false );
      userPhone.setPhoneNbr( smsPhoneNumber );
      userPhone.setPhoneType( PhoneType.lookup( PhoneType.MOBILE ) );
      userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
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

  /**
   * Sets the user node on the User object
   * 
   * @param user
   */
  public void setUserNodesInfo( User user )
  {
    if ( nodeId != null )
    {
      UserNode userNode = new UserNode();
      userNode.setUser( user );
      userNode.setActive( Boolean.TRUE );
      userNode.setIsPrimary( Boolean.TRUE );
      // Business Assumption: Use case expects all self enrolling participants are member
      // manager and node owners will be setup manually by admin.
      userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
      userNode.setNode( getNodeService().getNodeById( new Long( nodeId ) ) );
      user.addUserNode( userNode );
    }
  }

  /**
   * Sets the form data for personal info on the User object
   * 
   * @param user
   */
  public void setPersonalInfo( User user )
  {
    user.setActive( Boolean.TRUE );

    // set the welcome email sent flag to false for new users
    user.setWelcomeEmailSent( Boolean.FALSE );

    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    user.setFirstName( firstName );
    user.setMiddleName( middleName );
    user.setLastName( lastName );
    // user.setSuffixType( SuffixType.lookup( suffix ) );
    user.setSsn( ssn.replaceAll( "-", "" ) );
    // user.setBirthDate( DateUtils.toDate( dateOfBirth ) );
    // user.setGenderType( GenderType.lookup( gender ) );
    user.setEnrollmentSource( ParticipantEnrollmentSource.lookup( ParticipantEnrollmentSource.SELF_ENROLL ) );
    user.setEnrollmentDate( new Date() );
    user.setLanguageType( LanguageType.lookup( getSystemVariableService().getDefaultLanguage().getStringVal() ) );

    user.setId( new Long( id ) );
    user.setVersion( new Long( version ) );
    if ( !StringUtil.isEmpty( this.selfEnrolledLanguageCode ) )
    {
      user.setLanguageType( LanguageType.lookup( selfEnrolledLanguageCode ) );
    }
    else
    {
      user.setLanguageType( LanguageType.lookup( getSystemVariableService().getDefaultLanguage().getStringVal() ) );
    }
  }

  /**
   * Sets the form data for login on the User object
   * 
   * @param user
   */
  public void setLoginInfo( User user )
  {
    user.setUserName( userName );
    user.setPassword( inputPassword );
    user.setForcePasswordChange( false );
    user.setLastResetDate( new Date() );
    user.setId( new Long( id ) );
    user.setVersion( new Long( version ) );
  }

  /**
   * Sets the form data for pax personal info on the User object
   * 
   * @param pax
   */
  public void setParticipantPersonalInfo( Participant pax )
  {
    setPersonalInfo( pax );

    pax.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) );
    pax.setStatusChangeDate( DateUtils.getCurrentDate() );
    pax.setDepartmentType( department );
    pax.setPositionType( jobTitle );
    pax.setAllowPublicInformation( true );

    // Already accepted T&Cs when this form is loaded, just set to accepted on the pax
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal() )
    {
      pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.ACCEPTED ) );
      pax.setTermsAcceptedDate( DateUtils.getCurrentDate() );
    }

    pax.setId( new Long( id ) );
    pax.setVersion( new Long( version ) );
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of UserCharacteristicFormBeans. If this is
    // not done, the form wont initialize properly.
    userCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "userCharacteristicValueListCount" ) );
    /* Dexter# 46388 Start */
    int count = AboutMeQuestionType.getList().size();
    aboutMeQuestions = (ArrayList<AboutMeValueBean>)buildEmptyAboutmMeValueBean( count );
    /* Dexter# 46388 End */
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( "save".equals( method ) )
    {
      if ( null == captchaResponse || captchaResponse.length() == 0 )
      {
        actionErrors.add( "captchaResponse", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "purl.tnc.error.params.CAPTCHA_CODE" ) ) );
      }

      // Validate only if it is entered.
      if ( !StringUtil.isEmpty( addressType ) )
      {
        actionErrors = addressFormBean.validateAddress( actionErrors );
      }

      if ( !StringUtil.isEmpty( emailType ) )
      {
        if ( StringUtil.isEmpty( emailAddress ) )
        {
          actionErrors.add( "emailAddress", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.EMAIL_ADDRESS" ) ) );
        }
        else if ( !GenericValidator.isEmail( this.emailAddress ) )
        {
          actionErrors.add( "emailAddress", new ActionMessage( "system.errors.EMAIL", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.EMAIL_ADDRESS" ) ) );
        }
      }

      if ( !StringUtil.isEmpty( phoneType ) )
      {
        if ( StringUtil.isEmpty( phoneNumber ) )
        {
          actionErrors.add( "phoneNumber", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.TELEPHONE_NUMBER" ) ) );
        }
        else if ( !PhoneNumberValidator.checkPhoneNumberFormat( this.phoneNumber ) )
        {
          actionErrors.add( "phoneNumber",
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.TELEPHONE_NUMBER" ) ) );
        }
        if ( StringUtil.isEmpty( countryPhoneCode ) )
        {
          actionErrors.add( "countryPhoneCode", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.COUNTRY_AND_CODE" ) ) );
        }
        if ( StringUtil.isEmpty( phoneType ) )
        {
          if ( !StringUtil.isEmpty( phoneNumber ) || !StringUtil.isEmpty( countryPhoneCode ) )
          {
            actionErrors.add( "phoneType", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.PRIMARY_TELEPHONE_TYPE" ) ) );
          }
        }
      }

      if ( getUserCharacteristicValueListCount() > 0 )
      {
        CharacteristicUtils.validateCharacteristicValueList( userCharacteristicValueList, actionErrors );
      }

      if ( !StringUtils.isBlank( ssn ) )
      {
        if ( !isSSNValid() )
        {
          actionErrors.add( "ssn", new ActionMessage( "self.enrollment.pax.registration.ENTER_VALID_SSN" ) );
        }
      }
    }
    else if ( "savePreferences".equals( method ) )
    {
      if ( activeSMSGroupTypes != null && activeSMSGroupTypes.length > 0 )
      {
        if ( StringUtils.isBlank( telephoneCountryCode ) )
        {
          actionErrors.add( "telephoneCountryCode", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.COUNTRY_AND_CODE" ) ) );
        }
        if ( StringUtils.isBlank( smsPhoneNumber ) )
        {
          actionErrors.add( "smsPhoneNumber", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.TELEPHONE_NUMBER" ) ) );
        }
        if ( StringUtils.isBlank( txtAlertTerms ) )
        {
          actionErrors.add( "txtAlertTerms", new ActionMessage( "profile.personal.info.ACCEPT_TERMS_TXT_MSG" ) );
        }
      }
      else
      {
        if ( !StringUtils.isBlank( telephoneCountryCode ) || !StringUtils.isBlank( smsPhoneNumber ) )
        {
          actionErrors.add( "activeSMSGroupTypes", new ActionMessage( "profile.personal.info.NO_ALERTS_PICKED" ) );
        }
      }
    }

    return actionErrors;
  }

  private boolean isSSNValid()
  {
    boolean isValid = false;
    String expression = "^\\d{3}[-]?\\d{2}[-]?\\d{4}$";
    Pattern pattern = Pattern.compile( expression );
    Matcher matcher = pattern.matcher( ssn );
    if ( matcher.matches() )
    {
      isValid = true;
    }
    String numeric = ssn.replaceAll( "[^\\d]", "" );
    if ( isValid )
    {
      if ( numeric.substring( 0, 3 ).equalsIgnoreCase( "000" ) )
      {
        isValid = false;
      }
      else if ( numeric.length() == 9 && numeric.substring( 5 ).equals( "0000" ) )
      {
        isValid = false;
      }
    }
    return isValid;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private List<AboutMeValueBean> buildEmptyAboutmMeValueBean( int count )
  {
    List<AboutMeValueBean> aboutMeValueBeans = new ArrayList<AboutMeValueBean>();
    for ( int index1 = 0; index1 < count; index1++ )
    {
      AboutMeValueBean aboutMeValueBean = new AboutMeValueBean();
      aboutMeValueBeans.add( aboutMeValueBean );
    }
    return aboutMeValueBeans;
  }

  public void load( List<AboutMe> listaboutme )
  {
    List<AboutMeValueBean> aboutMevaluebean = new ArrayList<AboutMeValueBean>();
    if ( listaboutme != null && listaboutme.size() > 0 )
    {
      for ( Iterator<AboutMe> iterator = listaboutme.iterator(); iterator.hasNext(); )
      {
        AboutMe aboutMe = iterator.next();
        AboutMeValueBean aValueBean = new AboutMeValueBean();
        aValueBean.setAboutmeAnswer( aboutMe.getAnswer() );
        aValueBean.setAboutmeQuestioncode( aboutMe.getAboutMeQuestionType().getCode() );
        aValueBean.setAboutmeQuestion( aboutMe.getAboutMeQuestionType().getName() );
        aboutMevaluebean.add( aValueBean );
      }
    }

    loadValueBean( aboutMevaluebean );
  }

  public void loadValueBean( List<AboutMeValueBean> listaboutMeValueBean )
  {
    List<AboutMeValueBean> returnAboutmeQuestions = new ArrayList<AboutMeValueBean>();
    List<AboutMeQuestionType> aboutMeQuestionTypes = AboutMeQuestionType.getList();
    boolean checked = false;
    if ( aboutMeQuestionTypes != null && aboutMeQuestionTypes.size() > 0 )
    {
      for ( Iterator<AboutMeQuestionType> iterateor = aboutMeQuestionTypes.iterator(); iterateor.hasNext(); )
      {
        AboutMeQuestionType meQuestionType = iterateor.next();
        if ( listaboutMeValueBean != null && listaboutMeValueBean.size() > 0 )
        {
          for ( Iterator<AboutMeValueBean> aboutmevaluebean = listaboutMeValueBean.iterator(); aboutmevaluebean.hasNext(); )
          {
            AboutMeValueBean abvaluebean = aboutmevaluebean.next();
            if ( abvaluebean.getAboutmeQuestioncode().equals( meQuestionType.getCode() ) )
            {
              checked = true;
              returnAboutmeQuestions.add( abvaluebean );
              break;
            }
          }
        }
        if ( !checked )
        {
          AboutMeValueBean abvaluebean = new AboutMeValueBean();
          abvaluebean.setAboutmeQuestioncode( meQuestionType.getCode() );
          abvaluebean.setAboutmeAnswer( "" );
          abvaluebean.setAboutmeQuestion( meQuestionType.getName() );
          returnAboutmeQuestions.add( abvaluebean );
        }
        checked = false;
      }
    }
    this.aboutMeQuestions = (ArrayList<AboutMeValueBean>)returnAboutmeQuestions;
  }

  public int getaboutMeQuestionsListSize()
  {
    if ( this.aboutMeQuestions != null )
    {
      return this.aboutMeQuestions.size();
    }

    return 0;
  }

  private boolean isAllowEstatements()
  {
    boolean isAllowEstatements = getSystemVariableService().getPropertyByName( SystemVariableService.PARTICIPANT_ALLOW_ESTATEMENTS ).getBooleanVal();
    return isAllowEstatements;
  }
}
