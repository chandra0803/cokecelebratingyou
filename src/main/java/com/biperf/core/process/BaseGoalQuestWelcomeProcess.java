
package com.biperf.core.process;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.FormattedValueBean;

public abstract class BaseGoalQuestWelcomeProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( BaseGoalQuestWelcomeProcess.class );

  protected PasswordResetService passwordResetService;

  @SuppressWarnings( "unchecked" )
  protected String buildTokenUrl( Long paxId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    User user = getUserService().getUserByIdWithAssociations( paxId, associationRequestCollection );
    // only add the token if the email address is unique
    String url = null;
    UserEmailAddress email = user.getPrimaryEmailAddress();

    if ( !Objects.isNull( email ) && getUserService().isUniqueEmail( email.getEmailAddr() ) )
    {
      UserToken token = getPasswordResetService().generateTokenAndSave( paxId, UserTokenType.WELCOME_EMAIL );
      url = mailingService.buildUserTokenURL( token.getUnencryptedTokenValue() ) + "&activation=true";
    }
    else
    {
      url = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    }

    return url;
  }

  protected Participant getParticipant( Object paxTypeUnknown )
  {
    Participant participant = null;
    // paxs might be AudienceParticipant objects or Participants or FormattedValueBeans
    if ( paxTypeUnknown instanceof Participant )
    {
      participant = (Participant)paxTypeUnknown;
    }
    else if ( paxTypeUnknown instanceof AudienceParticipant )
    {
      participant = ( (AudienceParticipant)paxTypeUnknown ).getParticipant();
    }
    else if ( paxTypeUnknown instanceof FormattedValueBean )
    {
      participant = participantService.getParticipantById( ( (FormattedValueBean)paxTypeUnknown ).getId() );
    }

    return participant;
  }

  protected MailingRecipient getMailingRecipient( User user )
  {
    MailingRecipient mailingRecipient = null;

    if ( null != user && !user.isWelcomeEmailSent().booleanValue() )
    {
      LanguageType languageType = user.getLanguageType();

      mailingRecipient = new MailingRecipient();
      mailingRecipient.setGuid( GuidUtils.generateGuid() );
      mailingRecipient.setLocale( languageType != null ? languageType.getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
      mailingRecipient.setUser( user );
    }

    return mailingRecipient;
  }

  protected void updateUser( Participant participant )
  {
    try
    {
      User user = userService.getUserById( participant.getId() );
      user.setWelcomeEmailSent( Boolean.TRUE );
      userService.saveUser( user );
    }
    catch( Exception e )
    {
      String message = "An exception occurred while updating the user information while sending Welcome Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")";
      log.error( message, e );
      addComment( message );
    }
  }

  public PasswordResetService getPasswordResetService()
  {
    return passwordResetService;
  }

  public void setPasswordResetService( PasswordResetService passwordResetService )
  {
    this.passwordResetService = passwordResetService;
  }
}
