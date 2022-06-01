
package com.biperf.core.service.participant.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.participant.ParticipantIdentifierDAO;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.ParticipantIdentifierType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantIdentifier;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.ParticipantActivationService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.ui.user.ActivationField;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.objectpartners.cms.util.Environment;

public class ParticipantActivationServiceImpl implements ParticipantActivationService
{
  public static final String PARTICIPANT_IDENTIFIERS_SECTION = "participant_identifiers_data";
  private static final String UNDERSCORE = "_";
  private ParticipantIdentifierDAO participantIdentifierDAO = null;
  private ParticipantService participantService = null;
  private UserService userService;
  private CMAssetService cmAssetService = null;
  private PasswordResetService passwordResetService;
  private MailingService mailingService;

  @SuppressWarnings( "unused" )
  private static final Log log = LogFactory.getLog( ParticipantActivationServiceImpl.class );

  @Override
  public List<ParticipantIdentifier> getParticipantIdentifiers()
  {
    return participantIdentifierDAO.getAll();
  }

  @Override
  public boolean isValidActivationFields( Long userId, List<ActivationField> activationFields )
  {
    Participant pax = participantService.getParticipantById( userId );
    return isValidActivationFields( pax, activationFields );
  }

  @Override
  public PaxContactType sendActivationLinkToParticipant( Long contactId, ContactType contactType ) throws ServiceErrorException
  {
    User user = null;
    UserToken token = null;
    PaxContactType type = new PaxContactType();

    type.setContactType( contactType );
    if ( contactType == ContactType.EMAIL )
    {
      UserEmailAddress email = userService.getUserEmailAddressById( contactId );
      user = email.getUser();
      token = passwordResetService.generateTokenAndSave( user.getId(), UserTokenType.EMAIL );
      // send activation email
      mailingService.submitMailing( mailingService.buildPAXActivationNotification( user.getId(), email.getId(), token.getUnencryptedTokenValue() ), null, user.getId() );
      type.setValue( email.getEmailAddr() );
    }
    else
    {
      String environment = Environment.getEnvironment();
      UserPhone phone = userService.getUserPhoneById( contactId );
      if ( Environment.ENV_PROD.equals( environment ) || Environment.ENV_PRE.equals( environment ) )
      {
        user = phone.getUser();
        token = passwordResetService.generateTokenAndSave( user.getId(), UserTokenType.PHONE );
        // send activation text to phone
        mailingService.buildPAXActivationText( phone, token.getUnencryptedTokenValue() );
      }
      type.setValue( phone.getPhoneNbr() );
    }
    return type;
  }

  @Override
  public boolean isValidActivationFields( Participant participant, List<ActivationField> activationFields )
  {
    for ( ActivationField field : activationFields )
    {
      ParticipantIdentifier pi = getParticipantIdentifier( field.getParticipantIdentifierId() );
      if ( !isValidPaxActivationIdentifier( participant, pi, field.getValue() ) )
      {
        return false;
      }
    }
    return true;
  }

  @Override
  public ParticipantIdentifier getParticipantIdentifier( Long participantIdentifierId )
  {
    return participantIdentifierDAO.getById( participantIdentifierId );
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public boolean isValidPaxActivationIdentifier( Participant participant, ParticipantIdentifier pi, String valueToCheck )
  {
    if ( pi.getParticipantIdentifierType() != null )
    {
      switch ( pi.getParticipantIdentifierType().getCode() )
      {
        case ParticipantIdentifierType.CITY:
          return validateCityField( participant.getPrimaryAddress(), valueToCheck );
        case ParticipantIdentifierType.COUNTRY:
          return validateField( participant.getPrimaryCountryName(), valueToCheck ) || validateField( participant.getPrimaryCountryCode(), valueToCheck );
        case ParticipantIdentifierType.DEPARTMENT:
          return validateField( participant.getPaxDeptName(), valueToCheck );
        case ParticipantIdentifierType.DOB:
          return validateFieldForDate( participant.getBirthDate(), valueToCheck );
        case ParticipantIdentifierType.EMAIL:
          return validateEmailAddressField( participant.getPrimaryEmailAddress(), valueToCheck );
        case ParticipantIdentifierType.HIRE_DATE:
          return validateFieldForDate( participantService.getActiveHireDate( participant.getId() ), valueToCheck );
        case ParticipantIdentifierType.JOB_TITLE:
          return validateField( participant.getPaxJobName(), valueToCheck );
        case ParticipantIdentifierType.LOGIN_ID:
          return validateField( participant.getUserName(), valueToCheck );
        case ParticipantIdentifierType.POSTAL_CODE:
          return validatePostalCodeField( participant.getPrimaryAddress(), valueToCheck );
        case ParticipantIdentifierType.STATE:
          return validateStateField( participant.getPrimaryAddress(), valueToCheck );
        default:
          return false;
      }
    }
    else
    {
      // user characteristic identifier
      Set<UserCharacteristic> userCharacteristics = userService.getUserCharacteristics( participant.getId() );
      for ( UserCharacteristic userCharacteristic : userCharacteristics )
      {
        if ( userCharacteristic.getUserCharacteristicType().getId().equals( pi.getCharacteristic().getId() ) )
        {
          CharacteristicDataType dataType = userCharacteristic.getUserCharacteristicType().getCharacteristicDataType();
          if ( dataType.isDateType() )
          {
            // TODO: Do we need to do something DIFFERENT for a date here?
            return userCharacteristic.getCharacteristicValue().equalsIgnoreCase( valueToCheck.trim().toLowerCase() );
          }
          else if ( dataType.isSingleSelect() || dataType.isMultiSelect() )
          {
            for ( DynaPickListType item : userCharacteristic.getCharacteristicDisplayValueList() )
            {
              if ( validateField( item.getName(), valueToCheck ) )
              {
                return true;
              }
            }
          }
          else
          {
            return validateField( userCharacteristic.getCharacteristicValue(), valueToCheck );
          }
        }
      }
    }
    return false;
  }

  private boolean validateEmailAddressField( UserEmailAddress email, String valueToCheck )
  {
    if ( email == null )
    {
      return false;
    }
    return validateField( email.getEmailAddr(), valueToCheck );
  }

  private boolean validatePostalCodeField( UserAddress address, String valueToCheck )
  {
    if ( !validateAddress( address ) || address.getAddress().getStateType() == null )
    {
      return false;
    }
    return validateField( address.getAddress().getPostalCode(), valueToCheck );
  }

  private boolean validateCityField( UserAddress address, String valueToCheck )
  {
    if ( !validateAddress( address ) || address.getAddress().getCity() == null )
    {
      return false;
    }
    return validateField( address.getAddress().getCity(), valueToCheck );
  }

  private boolean validateStateField( UserAddress address, String valueToCheck )
  {
    if ( !validateAddress( address ) || address.getAddress().getStateType() == null )
    {
      return false;
    }
    return validateField( address.getAddress().getStateType().getName(), valueToCheck )
        || validateField( StringUtils.substringAfter( address.getAddress().getStateType().getCode(), UNDERSCORE ), valueToCheck );
  }

  private boolean validateAddress( UserAddress address )
  {
    if ( address == null || address.getAddress() == null )
    {
      return false;
    }
    return true;
  }

  private boolean validateField( String correctValue, String valueToCheck )
  {
    if ( Objects.isNull( valueToCheck ) )
    {
      return false;
    }
    return correctValue.trim().equalsIgnoreCase( valueToCheck.trim() );
  }

  private boolean validateFieldForDate( Date correctValue, String valueToCheck )
  {
    if ( Objects.isNull( valueToCheck ) )
    {
      return false;
    }
    return convertDateToString( correctValue ).equalsIgnoreCase( valueToCheck.trim() );
  }

  private String convertDateToString( Date date )
  {
    // Does the format need to be a system variable?
    Format formatter = new SimpleDateFormat( "MM/dd/yyyy" );
    return formatter.format( date );
  }

  @Override
  public void save( List<ParticipantIdentifier> pis ) throws ServiceErrorException
  {
    for ( ParticipantIdentifier pi : pis )
    {
      save( pi );
    }
  }

  @Override
  public void save( ParticipantIdentifier pi ) throws ServiceErrorException
  {
    if ( pi.getId() != null )
    {
      ParticipantIdentifier update = getParticipantIdentifierDAO().getById( pi.getId() );
      // do we even need to update the cm content?
      boolean requiresCmUpdate = !update.getCMDescriptionValue().equals( pi.getDescription() ) || !update.getCMLabelValue().equals( pi.getLabel() );
      update.setCharacteristic( pi.getCharacteristic() );
      update.setSelected( pi.isSelected() );
      update.setLabel( pi.getLabel() );
      update.setDescription( pi.getDescription() );
      update.setParticipantIdentifierType( ( null != pi.getParticipantIdentifierType() ) ? pi.getParticipantIdentifierType() : null );
      if ( requiresCmUpdate )
      {
        saveParticipantIdentifierCMData( update );
      }
      getParticipantIdentifierDAO().save( update );
    }
    else
    {
      saveParticipantIdentifierCMData( pi );
      getParticipantIdentifierDAO().save( pi );
    }
  }

  private void saveParticipantIdentifierCMData( ParticipantIdentifier pi ) throws ServiceErrorException
  {
    if ( StringUtils.isEmpty( pi.getCmAssetCode() ) )
    {
      pi.setCmAssetCode( cmAssetService.getUniqueAssetCode( ParticipantIdentifier.CM_PAX_IDENTIFIER_SECTION ) );
    }

    CMDataElement cmLabelDataElement = new CMDataElement( "Participant Identifier Label", ParticipantIdentifier.PAX_IDENTIFIER_LABEL_KEY, pi.getLabel(), false );
    CMDataElement cmDescriptionDataElement = new CMDataElement( "Participant Identifier Description", ParticipantIdentifier.PAX_IDENTIFIER_DESCRIPTION_KEY, pi.getDescription(), false );
    List<CMDataElement> elements = Arrays.asList( cmLabelDataElement, cmDescriptionDataElement );

    cmAssetService.createOrUpdateAsset( ParticipantIdentifier.CM_PAX_IDENTIFIER_SECTION,
                                        ParticipantIdentifier.PAX_IDENTIFIER_ASSET_TYPE,
                                        ParticipantIdentifier.CM_PAX_IDENTIFIER_ASSET_NAME,
                                        pi.getCmAssetCode(),
                                        elements );
  }

  public ParticipantIdentifierDAO getParticipantIdentifierDAO()
  {
    return participantIdentifierDAO;
  }

  public void setParticipantIdentifierDAO( ParticipantIdentifierDAO participantIdentifierDAO )
  {
    this.participantIdentifierDAO = participantIdentifierDAO;
  }

  @Override
  public List<ParticipantIdentifier> getActiveParticipantIdentifiers()
  {
    return participantIdentifierDAO.getSelected();
  }

  public CMAssetService getCmAssetService()
  {
    return cmAssetService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
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

  public PasswordResetService getPasswordResetService()
  {
    return passwordResetService;
  }

  public void setPasswordResetService( PasswordResetService passwordResetService )
  {
    this.passwordResetService = passwordResetService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

}
