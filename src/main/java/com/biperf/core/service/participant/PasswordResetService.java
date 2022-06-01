
package com.biperf.core.service.participant;

import com.biperf.core.domain.user.UserToken;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxPasswordResetView;

public interface PasswordResetService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "passwordResetService";

  public UserToken generateTokenAndSave( Long paxID, UserTokenType type );

  public PaxPasswordResetView getUserDetailsByContactInformation( String emailOrPhone );

  public PaxPasswordResetView getContactsAutocomplete( String initialQuery, String searchQuery );

  public PaxPasswordResetView sendForgotUserIdNotification( Long contactId, ContactType contactType, boolean sendFlag );

  public PaxPasswordResetView resetPasswordByEmail( String email, String userName );

  public PaxPasswordResetView validateToken( String token );

  public void resetPassword( String userName, String password, String token, String message ) throws ServiceErrorException;

  public PaxPasswordResetView sendUserToken( Long contactId, ContactType contactType );

  public PasswordRequirements getPasswordValidationRules();

  public void purgeUserTokens();
}