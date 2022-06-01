/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/ProfileService.java,v $
 */

package com.biperf.core.service.participant;

import java.util.List;

import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.value.AboutMeValueBean;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.biperf.core.value.participant.PaxAvatarData;

/**
 * This service handles the profile add/edit/delete information for user attributes, such as
 * password.
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
public interface ProfileService extends SAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "profileService";

  /**
   * Changes the password for the given user, provided the secretAnswer is correct.
   * 
   * @param userId
   * @param newPassword
   * @param secretQuestion
   * @param secretAnswer
   * @throws ServiceErrorException
   */
  public void changePassword( Long userId, String newPassword, String secretQuestion, String secretAnswer, boolean savePasswordToHistory ) throws ServiceErrorException;

  /**
   * Changes the password for the given user, provided the secretAnswer is correct.
   * 
   * @param userId
   * @param secretQuestion
   * @param secretAnswer
   * @throws ServiceErrorException
   */
  public void setSecretQuestionDetails( Long userId, String secretQuestion, String secretAnswer ) throws ServiceErrorException;

  /**
   * Changes the Participant's T&Cs Acceptance (accepted or notaccepted)
   * 
   * @param userId
   * @param accept
   * @throws ServiceErrorException
   */
  public void setTermsAndConditions( Long userId, boolean accept ) throws ServiceErrorException;

  /**
   * used for dependency injection
   * 
   * @param authenticationService
   */
  public void setAuthenticationService( AuthenticationService authenticationService );

  /**
   * used for dependency injection
   * 
   * @param userDAO
   */
  public void setUserDAO( UserDAO userDAO );

  public void saveFirstTimeLoggedInUserInfo( Participant user,
                                             Long participantId,
                                             SecretQuestionType secretQuestionType,
                                             String secretAnswer,
                                             List<AboutMeValueBean> aboutMeValueBeans,
                                             String[] preferences,
                                             UserPhone userPhone,
                                             String newPassword,
                                             boolean termsAndConditionUsed )
      throws ServiceErrorException;

  /**
   * used for dependency injection
   * 
   * @param participantDAO
   */
  public void setParticipantDAO( ParticipantDAO participantDAO );

  /**
   * @return PasswordPolicyStrategy
   */
  public PasswordPolicyStrategy getPasswordPolicyStrategy();

  /**
   * @param passwordPolicy
   */
  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicy );

  public PersonalInfoFileUploadValue uploadAvatar( PersonalInfoFileUploadValue data, Long participantId ) throws ServiceErrorException;

  public boolean deleteAvatar( Long participantId );

  public boolean deleteMediaFromWebdav( String filePath );

  public List<AboutMe> getAllAboutMeByUserId( Long userId );

  public AboutMe saveAboutMe( AboutMe aboutMe );

  public void deleteAboutMe( AboutMe aboutMe );

  public AboutMe getAboutMeById( Long id );

  public AboutMe getAboutMeByUserIdAndCode( Long userId, String code );

  void changePassword( Long userId, String newPassword, String oldPasswordProvided, String emailAddress, String phoneNumber, String countryPhoneCode, boolean savePasswordToHistory )
      throws ServiceErrorException;

  public List<ServiceError> validateOldPassword( Long userId, String oldPasswordProvided );

  void saveAccRecoveryInfo( Long userId, String emailAddress, String phoneNumber, String countryPhoneCode ) throws ServiceErrorException;

  public List<PaxAvatarData> getNotMigratedPaxAvatarData();

  public void updateMigratedPaxAvatarData( Long userId, String avatarOriginal, String avatarSmall );

  public List<PaxAvatarData> getUpdatedRosterUserIdPaxAvatarData();

}
