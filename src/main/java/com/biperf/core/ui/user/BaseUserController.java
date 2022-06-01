
package com.biperf.core.ui.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantIdentifier;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.ParticipantActivationService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

public abstract class BaseUserController extends SpringBaseController
{
  protected static final String CM_KEY_PREFIX = "login.account.activation.messages.";
  protected static final String SECURITY_TOKEN = "bi_security_token";
  public static final String TERMED_USER_SHOPPING_ON_NO_POINTS = "NO_POINTS";
  public static final String TERMED_USER_SHOPPING_OFF = "SV_OFF";
  protected @Autowired CountryService countryService;
  protected @Autowired UserService userService;
  protected @Autowired ParticipantActivationService participantActivationService;
  protected @Autowired AuthenticationService authenticationService;
  protected @Autowired SystemVariableService systemVariableService;
  private @Autowired AwardBanQServiceFactory awardBanQServiceFactory;

  protected ResponseEntity<PageRedirectMessage> buildHomePageRedirectEntity( HttpServletRequest request )
  {
    return new ResponseEntity<PageRedirectMessage>( buildHomePageRedirect( request ), HttpStatus.OK );
  }

  protected ResponseEntity<PageRedirectMessage> buildFirstTimeLoginRedirectEntity( HttpServletRequest request )
  {
    return new ResponseEntity<PageRedirectMessage>( buildFirstTimeLoginRedirect( request ), HttpStatus.OK );
  }

  protected ResponseEntity<PageRedirectMessage> buildResponseForInActivePaxWithBalance( HttpServletRequest request, Long userId )
  {
    return new ResponseEntity<PageRedirectMessage>( buildPageRedirectForInActivePaxWithBalance( request, userId ), HttpStatus.OK );
  }

  protected PageRedirectMessage buildHomePageRedirect( HttpServletRequest request )
  {
    PageRedirectMessage redirect = buildPageRedirect( request.getContextPath() + "/homePage.do" );
    return redirect;
  }

  protected PageRedirectMessage buildFirstTimeLoginRedirect( HttpServletRequest request )
  {
    PageRedirectMessage redirect = buildPageRedirect( request.getContextPath() + PageConstants.FIRST_TIME_LOGIN_PAGE );
    return redirect;
  }

  protected PageRedirectMessage buildPageRedirectForInActivePaxWithBalance( HttpServletRequest request, Long userId )
  {
    String shoppingUrlForInactiveUser = authenticationService.getShoppingUrlForInactiveUser( PageConstants.INACTIVE_SHOPPING_URL, PageConstants.MULTISUPPLIER_SHOPPING_URL, userId );

    if ( shoppingUrlForInactiveUser.contains( "https" ) )
    {
      return buildPageRedirect( shoppingUrlForInactiveUser );
    }

    return buildPageRedirect( RequestUtils.getBaseURI( request ) + shoppingUrlForInactiveUser );
  }

  protected List<ActivationField> buildActivationFields()
  {
    List<ActivationField> fields = new ArrayList<ActivationField>();
    participantActivationService.getActiveParticipantIdentifiers().stream().forEach( p -> fields.add( buildActivationField( p ) ) );
    return fields;
  }

  protected ActivationField buildActivationField( ParticipantIdentifier pi )
  {
    ActivationField field = new ActivationField();
    if ( !StringUtil.isEmpty( pi.getCMDescriptionValue() ) && !pi.getCMDescriptionValue().startsWith( "???" + ParticipantIdentifier.CM_PAX_IDENTIFIER_SECTION ) )
    {
      field.setDescription( pi.getCMDescriptionValue() );
    }
    field.setLabel( pi.getCMLabelValue() );
    field.setParticipantIdentifierId( pi.getId() );
    return field;
  }

  // Facility for the termed user to do the account activation, to redeem his/her points.
  protected boolean isTermedUserAllowToRedeem( Participant pax )
  {
    boolean isTermedUserAllowToRedeemSysVal = systemVariableService.getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal();
    // Get awardBanQ Balance for the Pax
    Long balance = awardBanQServiceFactory.getAwardBanQService().getAccountBalanceForParticipantId( pax.getId() );

    if ( isTermedUserAllowToRedeemSysVal && balance != null && balance.longValue() > 0 )
    {
      return true;
    }

    return false;
  }

  protected boolean isTermedUserAndInActive( Participant pax )
  {
    if ( Objects.nonNull( pax.getTerminationDate() ) && Objects.nonNull( pax.getStatus() ) && Objects.nonNull( pax.getStatus().getCode() )
        && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
    {
      return true;
    }
    return false;
  }

  protected List<PaxContactType> addNoneOptionToPaxContactType( List<PaxContactType> paxContactType )
  {
    PaxContactType nonePaxContactType = new PaxContactType();
    nonePaxContactType.setContactType( ContactType.NONE );
    nonePaxContactType.setValue( CmsResourceBundle.getCmsBundle().getString( CM_KEY_PREFIX + "TERMED_USER_NONE_OF_THESE_WORK" ) );
    // Placing the user id in contact id which required, if the user select none option.
    nonePaxContactType.setContactId( paxContactType.stream().findFirst().get().getUserId() );
    nonePaxContactType.setUserId( paxContactType.stream().findFirst().get().getUserId() );
    nonePaxContactType.setTotalRecords( paxContactType.stream().findFirst().get().getTotalRecords() + 1L );
    nonePaxContactType.setUnique( false );
    nonePaxContactType.setInputContact( false );

    paxContactType.forEach( p -> p.setTotalRecords( p.getTotalRecords() + 1L ) );
    paxContactType.add( nonePaxContactType );

    return paxContactType;
  }

  protected List<CountryPhoneView> getSMSCountryList()
  {
    List<CountryPhoneView> countryList = new ArrayList<CountryPhoneView>();
    List<Country> countries = countryService.getActiveCountriesForSmsAvailable();
    Collections.sort( countries, new Comparator<Country>()
    {
      @Override
      public int compare( Country country1, Country country2 )
      {
        if ( country1.getCountryCode().equals( "us" ) )
        {
          return -1;
        }
        else if ( country2.getCountryCode().equals( "us" ) )
        {
          return 1;
        }
        else
        {
          return ( country1.getI18nCountryName() ).compareTo( country2.getI18nCountryName() );
        }
      }
    } );
    countries.stream().forEachOrdered( c -> countryList.add( new CountryPhoneView( c ) ) );
    return countryList;
  }

  protected UserActivationRecoveryView getUserActivationRecoveryInfo( HttpServletRequest request, Long userId, String emailOrMobile )
  {
    UserActivationRecoveryView view = new UserActivationRecoveryView();

    view.setTokenValidation( request.getSession().getAttribute( SECURITY_TOKEN ).toString() );
    view.setHasRecoveryMethods( false );
    view.setCountryPhones( getSMSCountryList() );
    view.setTermedUserId( userId );

    if ( Objects.nonNull( emailOrMobile ) )
    {
      if ( emailOrMobile.equalsIgnoreCase( "EMAIL" ) || emailOrMobile.equalsIgnoreCase( "PHONE" ) )
      {
        view.setTermedUserAllowToRedeem( false );
        view.setTermedUserToolTip( true );
      }

    }
    else
    {
      view.setTermedUserAllowToRedeem( true );
      view.setTermedUserToolTip( false );

    }

    return view;

  }
}
