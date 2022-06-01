/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/impl/ProfileServiceImpl.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.dao.participant.AboutMeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.UserPasswordHistoryDAO;
import com.biperf.core.dao.security.EncryptionDAO;
import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPasswordHistory;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.profileavatar.ProfileAvatarRepositoryFactory;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.ProfileAvatarUploadUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.biperf.core.value.AboutMeValueBean;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;
import com.biperf.core.value.profile.v1.uploadavatar.UploadAvatarRequest;

/**
 * Hibernate implementation of the profile service interface.
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
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProfileServiceImpl implements ProfileService
{
  private UserDAO userDAO = null;
  private ParticipantDAO participantDAO = null;
  private AuthenticationService authenticationService = null;
  private PasswordPolicyStrategy passwordPolicyStrategy = null;
  private AboutMeDAO aboutMeDAO = null;

  private List<String> acceptableExtentions;
  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;
  private SystemVariableService systemVariableService;
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private ImageCropStrategy imageCropStrategy;
  private ImageResizeStrategy imageResizeStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  private UserPasswordHistoryDAO userPasswordHistoryDAO;
  private EncryptionDAO encryptionDAO;
  private ParticipantService participantService;
  private UserService userService;

  @Autowired
  private ProfileAvatarRepositoryFactory profileAvatarRepositoryFactory;

  private static final Log log = LogFactory.getLog( ProfileServiceImpl.class );

  /**
   * Looks up the given user, and sets the new password. Overridden from
   * 
   * @see com.biperf.core.service.participant.ProfileService#changePassword(java.lang.String,
   *      java.lang.String, java.lang.String)
   * @param userId
   * @param newPassword
   * @param secretQuestion
   * @param secretAnswer
   * @throws ServiceErrorException
   */
  public void changePassword( Long userId, String newPassword, String secretQuestion, String secretAnswer, boolean savePasswordToHistory ) throws ServiceErrorException
  {
    User user = getUserDAO().getUserById( userId );

    List passwordErrors = getPasswordPolicyStrategy().getPasswordValidationErrors( user.getPassword(), newPassword );

    if ( !passwordErrors.isEmpty() )
    {
      throw new ServiceErrorException( passwordErrors );
    }

    // blank out the loginToken since the user has changed the password
    user.setLoginToken( null );

    if ( secretQuestion != null && !secretQuestion.isEmpty() && secretAnswer != null && !secretAnswer.isEmpty() )
    {
      user.setSecretQuestionType( SecretQuestionType.lookup( secretQuestion ) );
      user.setSecretAnswer( secretAnswer );
    }
    int passwordHistoryCount = 0;
    // SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean(
    // SystemVariableService.BEAN_NAME );

    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.USER_PASSWORD_HISTORY_CHECK_COUNT );
    if ( property != null )
    {
      if ( property.getIntVal() < 1 )
      {
        passwordHistoryCount = 0;
      }
      else
      {
        passwordHistoryCount = property.getIntVal();
      }

      String pwd = new SHA256Hash().encryptDefault( newPassword );
      List<UserPasswordHistory> passwordHistory = getUserPasswordHistoryDAO().getUserPasswords( userId );

      if ( passwordHistory != null )
      {
        if ( passwordHistoryCount > 0 )
        {
          passwordHistory.add( new UserPasswordHistory( user, null, user.getPassword() ) );
          for ( UserPasswordHistory password : passwordHistory )
          {
            if ( pwd != null && pwd.equals( password.getPassword() ) )
            {
              throw new ServiceErrorException( new ServiceError( ServiceErrorMessageKeys.PREVIOUS_PASSWORD_CANT_BE_REUSED ) );
            }
          }

          if ( savePasswordToHistory )
          {
            Set<UserPasswordHistory> userPasswords = user.getUserPasswords();

            UserPasswordHistory userPasswordHistory = new UserPasswordHistory();
            userPasswordHistory.setUser( user );
            userPasswordHistory.setPassword( new SHA256Hash().encryptDefault( newPassword ) );
            userPasswordHistory.setPasswordSetDate( new Date() );
            userPasswordHistory.setSequenceNumber( getUserPasswordHistoryDAO().getMaxSequenceNumber() + 1 );

            if ( userPasswords != null )
            {
              if ( userPasswords.size() >= passwordHistoryCount )
              {
                getUserPasswordHistoryDAO().deletePassword( user.getId() );
              }
              getUserPasswordHistoryDAO().savePassword( userPasswordHistory );
            }
          }
        }
      }
      user.setPassword( newPassword );
      user.setLastResetDate( new Date() );
      user.setForcePasswordChange( false );
      user.setOneTimePassword( false );
      user.setLoginFailuresCount( 0 );
      // Security Patch 3 - start
      // user.setReissuedPasswordExpireDate( null );
      // Security Patch 3 - end
    }
    getUserDAO().saveUser( user );
  }

  public void saveFirstTimeLoggedInUserInfo( Participant user,
                                             Long participantId,
                                             SecretQuestionType secretQuestionType,
                                             String secretAnswer,
                                             List<AboutMeValueBean> aboutMeValueBeans,
                                             String[] preferences,
                                             UserPhone userPhone,
                                             String newPassword,
                                             boolean termsAndConditionUsed )
      throws ServiceErrorException
  {

    List passwordErrors;

    if ( user == null )
    {
      user = this.participantDAO.getParticipantById( participantId );
    }
    if ( !StringUtil.isEmpty( newPassword ) )
    {
      passwordErrors = getPasswordPolicyStrategy().getPasswordValidationErrors( user.getPassword(), newPassword );
      if ( !passwordErrors.isEmpty() )
      {
        throw new ServiceErrorException( passwordErrors );
      }
      changePassword( UserManager.getUserId(), newPassword, secretQuestionType.getCode(), secretAnswer, true );
    }

    try
    {
      for ( Iterator<AboutMeValueBean> iterator = aboutMeValueBeans.iterator(); iterator.hasNext(); )
      {
        AboutMeValueBean aboutMeValueBean = iterator.next();
        AboutMe aboutMe = aboutMeDAO.getAboutMeByUserIdAndCode( user.getId(), aboutMeValueBean.getAboutmeQuestioncode() );

        if ( aboutMe != null )
        {
          if ( StringUtils.isNotEmpty( aboutMeValueBean.getAboutmeAnswer() ) )
          {
            if ( !aboutMeValueBean.getAboutmeAnswer().equals( aboutMe.getAnswer() ) )
            {
              aboutMe.setAnswer( aboutMeValueBean.getAboutmeAnswer() );
              aboutMeDAO.saveAboutMe( aboutMe ); // update
            }
          }
          else if ( StringUtils.isEmpty( aboutMeValueBean.getAboutmeAnswer() ) )
          {
            aboutMeDAO.deleteAboutMe( aboutMe );
          }
        }
        else if ( StringUtils.isNotEmpty( aboutMeValueBean.getAboutmeAnswer() ) )
        {
          aboutMe = new AboutMe();
          aboutMe.setAnswer( aboutMeValueBean.getAboutmeAnswer() );
          aboutMe.setAboutMeQuestionType( AboutMeQuestionType.lookup( aboutMeValueBean.getAboutmeQuestioncode() ) );
          aboutMe.setUser( user );

          aboutMeDAO.saveAboutMe( aboutMe );
        }
      }

      if ( !StringUtil.isEmpty( newPassword ) )
      {
        user.setPassword( newPassword );
        user.setLoginToken( null );
        user.setOneTimePassword( false );
        user.setForcePasswordChange( false );
        // Security Patch 3 - start
        // user.setReissuedPasswordExpireDate( null );
        // Security Patch 3 - end
      }
      user.setSecretQuestionType( secretQuestionType );
      user.setSecretAnswer( secretAnswer );
      user.setProfileSetupDone( true );
      user.setLastResetDate( new Date() );
      user.setOneTimePassword( false );
      user.setForcePasswordChange( false );

      if ( UserManager.getUser().isParticipant() )
      {
        Set<ParticipantCommunicationPreference> commPreferences = user.getParticipantCommunicationPreferences();
        if ( preferences != null )
        {
          for ( String code : preferences )
          {
            if ( commPreferences.stream().noneMatch( pref -> pref.getParticipantPreferenceCommunicationsType().getCode().equals( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES )
                && pref.getMessageSMSGroupType().getCode().equals( code ) ) )
            {
              ParticipantCommunicationPreference preference = new ParticipantCommunicationPreference();
              preference.setMessageSMSGroupType( MessageSMSGroupType.lookup( code ) );
              preference.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) );
              user.addParticipantCommunicationPreference( preference );
            }
          }
          List<String> enabledMessageTypes = Arrays.asList( preferences );
          commPreferences.removeIf( pref -> pref.getParticipantPreferenceCommunicationsType().getCode().equals( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES )
              && !enabledMessageTypes.contains( pref.getMessageSMSGroupType().getCode() ) );
        }
        else
        {
          commPreferences.removeIf( preference -> ParticipantPreferenceCommunicationsType.TEXT_MESSAGES.equals( preference.getParticipantPreferenceCommunicationsType().getCode() ) );
        }

        if ( userPhone != null )
        {
          user.addUserPhone( userPhone );
          user.setAcceptTermsOnTextMessages( true );
        }
        if ( termsAndConditionUsed )
        {
          user.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.ACCEPTED ) );
          user.setTermsAcceptedDate( DateUtils.getCurrentDate() ); // Capture when T&Cs was accepted
          user.setUserIDAcceptedTerms( UserManager.getUserId().toString() ); // Capture the User who
                                                                             // accepted
          user.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) ); // Turn Pax to
                                                                                  // Active
          user.setStatusChangeDate( DateUtils.getCurrentDate() );

          /* bug fix start */
          UserManager.getUser().setPaxStatus( ParticipantStatus.ACTIVE );
          UserManager.getUser().setPaxTermsAcceptance( ParticipantTermsAcceptance.ACCEPTED );
          UserManager.getUser().setPaxTermsAcceptedDate( DateUtils.getCurrentDate() );
          /* bug fix end */

          getUserService().updateUserNodeStatus( user.getId(), user.isActive() );
        }
      }
      getParticipantDAO().saveParticipant( user );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Looks up the given user, and sets the new security details. Overridden from
   * 
   * @see com.biperf.core.service.participant.ProfileService#setSecretQuestionDetails(java.lang.String,
   *      java.lang.String)
   * @param userId
   * @param secretQuestion
   * @param secretAnswer
   * @throws ServiceErrorException
   */
  public void setSecretQuestionDetails( Long userId, String secretQuestion, String secretAnswer ) throws ServiceErrorException
  {
    User user = getUserDAO().getUserById( userId );

    if ( secretQuestion != null && secretAnswer != null )
    {
      user.setSecretQuestionType( SecretQuestionType.lookup( secretQuestion ) );
      user.setSecretAnswer( secretAnswer );
    }

    getUserDAO().saveUser( user );
  }

  /**
   * Looks up the userId of the logged in user, and sets the new terms and conditions acceptance
   * details. Overridden from
   * 
   * @see com.biperf.core.service.participant.ProfileService#setTermsAndConditions(Long, boolean)
   * @param userId
   * @param accept
   * @throws ServiceErrorException
   */
  public void setTermsAndConditions( Long userId, boolean accept ) throws ServiceErrorException
  {
    log.debug( "userID passed in: " + userId );
    log.debug( "accept option passed in: " + accept );

    Participant pax = getParticipantDAO().getParticipantById( userId );

    if ( accept )
    {
      pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.ACCEPTED ) );
      pax.setTermsAcceptedDate( DateUtils.getCurrentDate() ); // Capture when T&Cs was accepted
      pax.setUserIDAcceptedTerms( userId.toString() ); // Capture the User who accepted
      pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) ); // Turn Pax to Active
      pax.setStatusChangeDate( DateUtils.getCurrentDate() );
    }
    else
    {
      pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.NOTACCEPTED ) );
      pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) ); // Turn Pax to
      // InActive
      pax.setStatusChangeDate( DateUtils.getCurrentDate() );
    }

    getParticipantDAO().saveParticipant( pax );
  }

  @Override
  public AboutMe saveAboutMe( AboutMe aboutMe )
  {
    return aboutMeDAO.saveAboutMe( aboutMe );
  }

  @Override
  public void deleteAboutMe( AboutMe aboutMe )
  {
    aboutMeDAO.deleteAboutMe( aboutMe );
  }

  @Override
  public AboutMe getAboutMeById( Long id )
  {
    return aboutMeDAO.getAboutMeById( id );
  }

  public List<AboutMe> getAllAboutMeByUserId( Long userId )
  {
    return aboutMeDAO.getAllAboutMeByUserId( userId );
  }

  public AboutMe getAboutMeByUserIdAndCode( Long userId, String code )
  {
    return aboutMeDAO.getAboutMeByUserIdAndCode( userId, code );
  }

  public PersonalInfoFileUploadValue uploadAvatar( PersonalInfoFileUploadValue data, Long participantId ) throws ServiceErrorException
  {
    Participant participant = participantDAO.getParticipantById( participantId );

    if ( validFileData( data ) )
    {
      try
      {
        ResponseEntity<AvatarView> avatarView = null;
        int targetCropDimension = 0;

        // Upload Avatar Full Url
        ImageUtils imgInstance = new ImageUtils();
        BufferedImage inputImage = imgInstance.readImage( data.getData() );
        BufferedImage full = inputImage;

        // get the crop dimension based on the image height and width
        targetCropDimension = full.getHeight() < full.getWidth() ? full.getHeight() : full.getWidth();
        full = imageCropStrategy.process( full, targetCropDimension, targetCropDimension );
        full = imageResizeStrategy.process( full, PersonalInfoFileUploadValue.FULL_DIMENSION );

        String imageInBytes = Base64.getEncoder().encodeToString( ImageUtils.convertToByteArray( full, ImageUtils.getFileExtension( data.getName() ) ) );
        UploadAvatarRequest uploadOrgAvatarRequest = new UploadAvatarRequest();
        uploadOrgAvatarRequest.setImageData( ProfileAvatarUploadUtil.getImageData( data.getContentType(), imageInBytes ) );

        // Upload Original Avatar To Service.
        avatarView = profileAvatarRepositoryFactory.getRepo().uploadAvatar( participant.getRosterUserId().toString(), uploadOrgAvatarRequest );
        if ( ProfileAvatarUploadUtil.isValidResponse( avatarView ) )
        {
          String opUrl = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/profile/public/avatars/image/" + MeshServicesUtil.getCompanyId().toString() + "/"
              + participant.getRosterUserId().toString();
          data.setFull( opUrl );
          data.setThumb( opUrl );
        }
        else
        {
          throw new ServiceErrorException( "personalInfo.IMAGE_UPLOAD_FAILED" );
        }

        participant.setAvatarOriginal( data.getFull() );
        participant.setAvatarSmall( data.getThumb() );
        getParticipantDAO().saveParticipant( participant );
      }
      catch( HttpStatusCodeException httpException )
      {
        log.error( "httpException while uploading avatar : " + httpException.getMessage() );
        throw new ServiceErrorException( httpException.getResponseBodyAsString() );
      }
      catch( Exception e )
      {
        throw new ServiceErrorException( "personalInfo.IMAGE_UPLOAD_FAILED" );
      }
    }
    else
    {
      throw new ServiceErrorException( "personalInfo.IMAGE_UPLOAD_INVALID" );
    }

    return data;
  }

  public boolean deleteAvatar( Long participantId )
  {
    Participant participant = getParticipantDAO().getParticipantById( participantId );
    participant.setAvatarOriginal( null );
    participant.setAvatarSmall( null );
    getParticipantDAO().saveParticipant( participant );

    return true;
  }

  public boolean deleteMediaFromWebdav( String filePath )
  {
    if ( StringUtil.isEmpty( filePath ) )
    {
      return false;
    }

    try
    {
      try
      {
        byte[] data = webdavFileUploadStrategy.getFileData( filePath );
        if ( null == data )
        {
          // If file data does not exist, there is nothing to delete
          // Might have got deleted in previous attempt
          return true;
        }
      }
      catch( Throwable e )
      {
        // If file data does not exist, there is nothing to delete
        // Might have got deleted in previous attempt
        return true;
      }

      // Delete Media from Stage(WebDav)
      return webdavFileUploadStrategy.delete( filePath );
    }
    catch( Throwable e )
    {
      return false;
    }
  }

  private boolean validFileData( PersonalInfoFileUploadValue data )
  {
    // Check file type
    String extension = ImageUtils.getFileExtension( data.getName() );
    if ( !acceptableExtentions.contains( extension.toLowerCase() ) )
    {
      return false;
    }

    // Check file size
    if ( PersonalInfoFileUploadValue.TYPE_PICTURE.equals( data.getType() ) || PersonalInfoFileUploadValue.TYPE_AVATAR.equals( data.getType() ) && Objects.nonNull( data.getData() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public void changePassword( Long userId, String newPassword, String oldPasswordProvided, String emailAddress, String phoneNumber, String countryPhoneCode, boolean savePasswordToHistory )
      throws ServiceErrorException
  {
    User user = getUserDAO().getUserById( userId );
    UserPhone userPhone;
    UserEmailAddress userEmailAddress;

    List<ServiceError> passwordErrors = getPasswordPolicyStrategy().getPasswordValidationErrors( user.getPassword(), newPassword );

    if ( !passwordErrors.isEmpty() )
    {
      throw new ServiceErrorException( passwordErrors );
    }

    // blank out the loginToken since the user has changed the password
    user.setLoginToken( null );

    if ( phoneNumber != null && !phoneNumber.isEmpty() && countryPhoneCode != null && !countryPhoneCode.isEmpty() )
    {
      boolean isPhoneUpdated = getUserDAO().updateRecoveryPhone( userId, phoneNumber, countryPhoneCode );
      if ( !isPhoneUpdated )
      {
        userPhone = new UserPhone();
        userPhone.setCountryPhoneCode( countryPhoneCode );
        userPhone.setIsPrimary( Boolean.FALSE );
        userPhone.setPhoneNbr( phoneNumber );
        userPhone.setPhoneType( PhoneType.lookup( PhoneType.RECOVERY ) );
        userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
        user.addUserPhone( userPhone );
      }
    }

    if ( emailAddress != null && !emailAddress.isEmpty() )
    {
      boolean isEmailUpdated = getUserDAO().updateRecoveryEmail( userId, emailAddress );
      if ( !isEmailUpdated )
      {
        userEmailAddress = new UserEmailAddress();
        userEmailAddress.setEmailAddr( emailAddress );
        userEmailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
        userEmailAddress.setIsPrimary( Boolean.FALSE );
        userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
        user.addUserEmailAddress( userEmailAddress );
      }
    }
    int passwordHistoryCount = 0;

    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.USER_PASSWORD_HISTORY_CHECK_COUNT );
    if ( property != null )
    {
      if ( property.getIntVal() < 1 )
      {
        passwordHistoryCount = 0;
      }
      else
      {
        passwordHistoryCount = property.getIntVal();
      }

      String pwd = new SHA256Hash().encryptDefault( newPassword );
      List<UserPasswordHistory> passwordHistory = getUserPasswordHistoryDAO().getUserPasswords( userId );

      if ( passwordHistory != null )
      {
        if ( passwordHistoryCount > 0 )
        {
          passwordHistory.add( new UserPasswordHistory( user, null, user.getPassword() ) );

          UserPasswordHistory userPasswordHis = passwordHistory.stream().filter( e ->
          {
            if ( pwd != null && pwd.equals( e.getPassword() ) )
            {
              return true;
            }
            return false;
          } ).findAny().orElse( null );

          if ( userPasswordHis != null )
          {
            throw new ServiceErrorException( new ServiceError( ServiceErrorMessageKeys.PREVIOUS_PASSWORD_CANT_BE_REUSED ) );
          }

          if ( savePasswordToHistory )
          {
            Set<UserPasswordHistory> userPasswords = user.getUserPasswords();

            UserPasswordHistory userPasswordHistory = new UserPasswordHistory();
            userPasswordHistory.setUser( user );
            userPasswordHistory.setPassword( new SHA256Hash().encryptDefault( newPassword ) );
            userPasswordHistory.setPasswordSetDate( new Date() );
            userPasswordHistory.setSequenceNumber( getUserPasswordHistoryDAO().getMaxSequenceNumber() + 1 );

            if ( userPasswords != null )
            {
              if ( userPasswords.size() >= passwordHistoryCount )
              {
                getUserPasswordHistoryDAO().deletePassword( user.getId() );
              }
              getUserPasswordHistoryDAO().savePassword( userPasswordHistory );
            }
          }
        }
      }
      user.setPassword( newPassword );
      user.setLastResetDate( new Date() );
      user.setForcePasswordChange( false );
      user.setOneTimePassword( false );
    }
    getUserDAO().saveUser( user );
  }

  @Override
  public List<ServiceError> validateOldPassword( Long userId, String oldPasswordProvided )
  {
    User user = getUserDAO().getUserById( userId );
    String oldPasswordActual = user.getPassword();
    return getPasswordPolicyStrategy().isValidOldPassword( oldPasswordActual, oldPasswordProvided );
  }

  public void saveAccRecoveryInfo( Long userId, String emailAddress, String phoneNumber, String countryPhoneCode ) throws ServiceErrorException
  {
    User user = getUserDAO().getUserById( userId );

    if ( StringUtils.isBlank( emailAddress ) && StringUtils.isBlank( phoneNumber ) )
    {
      throw new ServiceErrorException( new ServiceError( "profile.pesronal.info.NO_RECOVERY_METHOD_ERROR" ) );
    }

    if ( StringUtils.isNotBlank( phoneNumber ) && StringUtils.isBlank( countryPhoneCode ) )
    {
      throw new ServiceErrorException( new ServiceError( "login.forgotpwd.COUNTRY_CODE_REQ" ) );
    }

    String previousEmailAddress = null;
    // Already validated they're not both blank. Onto saving
    Optional<UserEmailAddress> currentRecoveryEmail = user.getUserEmailAddresses().stream().filter( email -> EmailAddressType.RECOVERY.equals( email.getEmailType().getCode() ) ).findFirst();
    if ( StringUtils.isBlank( emailAddress ) )
    {
      if ( currentRecoveryEmail.isPresent() )
      {
        previousEmailAddress = currentRecoveryEmail.get().getEmailAddr();
        user.getUserEmailAddresses().remove( currentRecoveryEmail.get() );
      }
    }
    else
    {
      if ( !currentRecoveryEmail.isPresent() )
      {
        UserEmailAddress recoveryEmail = new UserEmailAddress();
        recoveryEmail.setEmailAddr( emailAddress );
        recoveryEmail.setEmailType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
        recoveryEmail.setIsPrimary( Boolean.FALSE );
        recoveryEmail.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
        user.addUserEmailAddress( recoveryEmail );
      }
      else
      {
        UserEmailAddress recoveryEmail = currentRecoveryEmail.get();
        previousEmailAddress = recoveryEmail.getEmailAddr();
        if ( !recoveryEmail.getEmailAddr().equals( emailAddress ) )
        {
          recoveryEmail.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
        }
        recoveryEmail.setEmailAddr( emailAddress );
      }
    }

    String previousPhoneCountryCode = null;
    String previousPhoneNbr = null;
    Optional<UserPhone> currentRecoveryPhone = user.getUserPhones().stream().filter( phone -> PhoneType.RECOVERY.equals( phone.getPhoneType().getCode() ) ).findFirst();
    if ( StringUtils.isBlank( phoneNumber ) )
    {
      if ( currentRecoveryPhone.isPresent() )
      {
        previousPhoneCountryCode = currentRecoveryPhone.get().getCountryPhoneCode();
        previousPhoneNbr = currentRecoveryPhone.get().getPhoneNbr();
        user.getUserPhones().remove( currentRecoveryPhone.get() );
      }
    }
    else
    {
      if ( !currentRecoveryPhone.isPresent() )
      {
        UserPhone recoveryPhone = new UserPhone();
        recoveryPhone.setCountryPhoneCode( countryPhoneCode );
        recoveryPhone.setIsPrimary( Boolean.FALSE );
        recoveryPhone.setPhoneNbr( phoneNumber );
        recoveryPhone.setPhoneType( PhoneType.lookup( PhoneType.RECOVERY ) );
        recoveryPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
        user.addUserPhone( recoveryPhone );
      }
      else
      {
        UserPhone recoveryPhone = currentRecoveryPhone.get();
        previousPhoneCountryCode = recoveryPhone.getCountryPhoneCode();
        previousPhoneNbr = recoveryPhone.getPhoneNbr();
        if ( !recoveryPhone.getPhoneNbr().equals( phoneNumber ) )
        {
          recoveryPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
        }
        recoveryPhone.setCountryPhoneCode( countryPhoneCode );
        recoveryPhone.setPhoneNbr( phoneNumber );
      }
    }
    getUserDAO().saveUser( user );

    // This service method will take care of "should we send" logic and actually sending
    participantService.sendRecoveryChangeNotification( userId, previousPhoneCountryCode, previousPhoneNbr, phoneNumber, previousEmailAddress, emailAddress );
  }

  /**
   * used for dependency injection
   * 
   * @return AuthenticationService
   */
  public AuthenticationService getAuthenticationService()
  {
    return authenticationService;
  }

  /**
   * used for dependency injection
   * 
   * @param authenticationService
   */
  public void setAuthenticationService( AuthenticationService authenticationService )
  {
    this.authenticationService = authenticationService;
  }

  /**
   * used for dependency injection
   * 
   * @return UserDAO
   */
  public UserDAO getUserDAO()
  {
    return userDAO;
  }

  /**
   * used for dependency injection
   * 
   * @param userDAO
   */
  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  /**
   * used for dependency injection
   * 
   * @return ParticipantDAO
   */
  public ParticipantDAO getParticipantDAO()
  {
    return participantDAO;
  }

  /**
   * used for dependency injection
   * 
   * @param participantDAO
   */
  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  /**
   * @return PasswordPolicyStrategy
   */
  public PasswordPolicyStrategy getPasswordPolicyStrategy()
  {
    return passwordPolicyStrategy;
  }

  /**
   * @param passwordPolicy
   */
  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicy )
  {
    this.passwordPolicyStrategy = passwordPolicy;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setImageCropStrategy( ImageCropStrategy imageCropStrategy )
  {
    this.imageCropStrategy = imageCropStrategy;
  }

  public void setImageResizeStrategy( ImageResizeStrategy imageResizeStrategy )
  {
    this.imageResizeStrategy = imageResizeStrategy;
  }

  public void setAcceptableExtentions( List<String> acceptableExtentions )
  {
    this.acceptableExtentions = acceptableExtentions;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

  public void setAboutMeDAO( AboutMeDAO aboutMeDAO )
  {
    this.aboutMeDAO = aboutMeDAO;
  }

  public UserPasswordHistoryDAO getUserPasswordHistoryDAO()
  {
    return userPasswordHistoryDAO;
  }

  public void setUserPasswordHistoryDAO( UserPasswordHistoryDAO userPasswordHistoryDAO )
  {
    this.userPasswordHistoryDAO = userPasswordHistoryDAO;
  }

  public EncryptionDAO getEncryptionDAO()
  {
    return encryptionDAO;
  }

  public void setEncryptionDAO( EncryptionDAO encryptionDAO )
  {
    this.encryptionDAO = encryptionDAO;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  @Override
  public List<PaxAvatarData> getUpdatedRosterUserIdPaxAvatarData()
  {
    return participantDAO.getUpdatedRosterUserIdPaxAvatarData();
  }

  @Override
  public List<PaxAvatarData> getNotMigratedPaxAvatarData()
  {
    return participantDAO.getNotMigratedPaxAvatarData();
  }

  @Override
  public void updateMigratedPaxAvatarData( Long userId, String avatarOriginal, String avatarSmall )
  {
    participantDAO.updateMigratedPaxAvatarData( userId, avatarOriginal, avatarSmall );
  }

}
