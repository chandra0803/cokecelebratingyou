/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantMaintainController.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.domain.enums.AdminLanguageType;
import com.biperf.core.domain.enums.ContactMethod;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.TitleType;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseAdminUserController;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.utils.AddressUtil;
import com.biperf.core.utils.UserManager;

/* ************************************************************************
 * 
 * ParticipantMaintainCustomController.
 * 
 * @author esakkimu
 * @since Jan 25, 2019
 * @version 1.0
 * 
 * THIS IS A CLONE OF ParticipantMaintainController. 
 * Created to tune Admin screens use ParticipantMaintainController.
 ************************************************************************ */
public class ParticipantMaintainCustomController extends BaseAdminUserController
{
  private static final Log LOG = LogFactory.getLog( ParticipantMaintainCustomController.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    final String METHOD_NAME = "execute";

    LOG.info( ">>> " + METHOD_NAME );

    // request.setAttribute( "termsAndCondition",
    // getTermsAndConditionService().getTermsAndConditionText() );
    // request.setAttribute( "paxStatusList", ParticipantStatus.getList() );
    // request.setAttribute( "suspensionStatusList", ParticipantSuspensionStatus.getList() );
    // request.setAttribute( "paxTermsAcceptList", ParticipantTermsAcceptance.getList() );
    request.setAttribute( "titleList", TitleType.getList() );
    // request.setAttribute( "suffixList", SuffixType.getList() );
    // request.setAttribute( "genderList", GenderType.getList() );
    // request.setAttribute( "addressTypeList", AddressType.getList() );
    request.setAttribute( "countryList", getCountryList() );
    // request.setAttribute( "countryListAll", getAllCountryList() );
    request.setAttribute( "emailTypeList", EmailAddressType.getPrimaryList() );
    request.setAttribute( "phoneTypeList", PhoneType.getPrimaryList() );
    // request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );
    // request.setAttribute( "jobPositionList", PositionType.getList() );
    // request.setAttribute( "departmentList", DepartmentType.getList() );
    // request.setAttribute( "nodeRelationshipList", HierarchyRoleType.getList() );
    request.setAttribute( "languageList", LanguageType.getList() );
    request.setAttribute( "adminLanguageList", AdminLanguageType.getList() );
    request.setAttribute( "contactMethodsList", ContactMethod.getList() );
    request.setAttribute( "contactMethodsTypeList", ParticipantPreferenceCommunicationsType.getList() );

    // request.setAttribute( "employerList", getEmployerService().getAll() );
    request.setAttribute( "messageSMSGroupTypeList", MessageSMSGroupType.getPlateuOnlyList() );

    UserForm participantForm = (UserForm)request.getAttribute( "userForm" );

    request.setAttribute( "countryCode", participantForm.getAddressFormBean().getCountryCode() );
    // request.setAttribute( "requirePostalCode", new Boolean( requirePostalCodeByCountryCode(
    // participantForm.getAddressFormBean().getCountryCode(), getAllCountryList() ) ) );
    // request.setAttribute( "stateList", AddressFormBean.getStateListByCountryInRequest( request )
    // );

    // Set the default for participant to be member
    if ( participantForm.getNodeRelationship() == null || participantForm.getNodeRelationship().equals( "" ) )
    {
      participantForm.setNodeRelationship( HierarchyRoleType.MEMBER );
    }

    // this.setContactMethods( request );

    // Set the default language
    if ( participantForm.getLanguage() == null || participantForm.getLanguage().equals( "" ) )
    {
      String locale = UserManager.getLocale().toString();
      participantForm.setLanguage( locale );
      request.setAttribute( "locale", participantForm.getLanguage() );
    }
    /*
     * Set userCharacteristics = new LinkedHashSet(); // This is the set of all UserCharacteristic
     * objects List availableCharacteristics =
     * getUserCharacteristicService().getAllCharacteristics(); List characteristicList =
     * CharacteristicUtils.getUserCharacteristicValueList( userCharacteristics,
     * availableCharacteristics ); if ( participantForm.getUserCharacteristicValueListCount() > 0 )
     * { CharacteristicUtils.loadExistingValues( characteristicList,
     * participantForm.getUserCharacteristicValueList() ); }
     * participantForm.setUserCharacteristicValueList( characteristicList ); // This is a little
     * different than the above Iteration because this iteration is // dealing with Characteristic
     * Objects rather than UserCharacteristic Objects Iterator availableIt =
     * availableCharacteristics.iterator(); while ( availableIt.hasNext() ) { Characteristic
     * characteristic = (Characteristic)availableIt.next(); if ( characteristic.getPlName() != null
     * && !characteristic.getPlName().equals( "" ) ) { request.setAttribute(
     * characteristic.getPlName(), DynaPickListType.getList( characteristic.getPlName() ) ); } }
     */
    // SMSOnOff change request start
    Long userId;
    if ( participantForm.getUserId() != null && !participantForm.getUserId().equals( "" ) )
    {
      userId = new Long( participantForm.getUserId() );
    }
    else
    {
      userId = UserManager.getUser().getUserId();
    }
    UserAddress userPrimaryAddress = getUserService().getPrimaryUserAddress( userId );
    UserEmailAddress userEmailAddress = getUserService().getPrimaryUserEmailAddress( userId );

    // Allow eStatments?
    PropertySetItem allowEstatements = getSystemVariableService().getPropertyByName( SystemVariableService.PARTICIPANT_ALLOW_ESTATEMENTS );
    if ( userEmailAddress != null && userPrimaryAddress != null && allowEstatements.getBooleanVal() && AddressUtil.isSupplierBIBank( userPrimaryAddress.getAddress().getCountry() ) )
    {
      request.setAttribute( "allowEstatements", new Boolean( allowEstatements.getBooleanVal() ) );
    }
    else
    {
      request.setAttribute( "allowEstatements", new Boolean( false ) );
    }

    boolean allowsms = false;
    if ( userPrimaryAddress != null )
    {
      allowsms = userPrimaryAddress.getAddress().getCountry().getAllowSms();
    }
    request.getSession().setAttribute( "allowTextMessages", allowsms );
    // SMSOnOff change request end

    // Allow contacts?
    PropertySetItem allowContacts = getSystemVariableService().getPropertyByName( SystemVariableService.PARTICIPANT_ALLOW_CONTACTS );
    request.setAttribute( "allowContacts", new Boolean( allowContacts.getBooleanVal() ) );
    // request.setAttribute( "allowContacts", new Boolean( true ) );

    // for displaying name
    if ( participantForm.getUserId() != null && !"".equals( participantForm.getUserId() ) )
    {
      request.setAttribute( "displayNameUserId", participantForm.getUserId() );
      /*
       * User user = getUserService().getUserById( new Long( participantForm.getUserId() ) );
       * request.setAttribute("user", user);//for name display
       */
    }

    // Enable Opt Out Awards - Start
    PropertySetItem allowOptOutAwards = getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_OPT_OUT_AWARDS );
    if ( allowOptOutAwards.getBooleanVal() )
    {
      request.setAttribute( "allowOptOutAwards", new Boolean( true ) );
    }
    else
    {
      request.setAttribute( "allowOptOutAwards", new Boolean( false ) );
    }
    // Enable Opt Out Awards - End

    // Enable Opt Out Program - Start
    PropertySetItem allowOptOutOfProgram = getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_OPT_OUT_PROGRAM );
    if ( allowOptOutOfProgram.getBooleanVal() )
    {
      request.setAttribute( "allowOptOutOfProgram", new Boolean( true ) );
    }
    else
    {
      request.setAttribute( "allowOptOutOfProgram", new Boolean( false ) );
    }
    // Enable Opt Out Program - End

    // Choose Sub Nav
    String subNavSelected = (String)componentContext.getAttribute( "subNavSelected" );
    request.setAttribute( "subNavSelected", subNavSelected );
    LOG.debug( "subNavSelected=" + subNavSelected );

    LOG.info( "<<< " + METHOD_NAME );

    moveSessionAttributeToRequest( request );
  }

  private void moveSessionAttributeToRequest( HttpServletRequest request )
  {
    Boolean preferenceSaved = (Boolean)request.getSession().getAttribute( "preferenceSaved" );
    request.setAttribute( "preferenceSaved", preferenceSaved );
    request.getSession().removeAttribute( "preferenceSaved" );
  }

  /**
   * Returns a list of all countries supported by this application in alphabetical order by country
   * name.
   * 
   * @return a list of all countries supported by this application, as a <code>List</code> of
   *         {@link com.biperf.core.domain.country.Country} objects.
   */
  private List getCountryList()
  {
    List countryList = getCountryService().getActiveCountriesForSmsChecked();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }

  // private List getAllCountryList()
  // {
  // List countryList = getCountryService().getAllActive();
  // Collections.sort( countryList, new CountryComparator() );
  //
  // return countryList;
  // }

  // private boolean requirePostalCodeByCountryCode( String countryCode, List countryList )
  // {
  // Country country = null;
  // for ( int i = 0; i < countryList.size(); i++ )
  // {
  // country = (Country)countryList.get( i );
  // if ( country.getCountryCode().equals( countryCode ) )
  // {
  // return country.getRequirePostalCode();
  // }
  // }
  //
  // return false;
  // }

  /**
   * Returns a reference to the Country service.
   * 
   * @return a reference to the Country service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  // /**
  // * Returns a reference to the Employer service.
  // *
  // * @return a reference to the Employer service.
  // */
  // private EmployerService getEmployerService() throws Exception
  // {
  // return (EmployerService)getService( EmployerService.BEAN_NAME );
  // }

  // /**
  // * Returns a reference to the User Characteristic service.
  // *
  // * @return a reference to the User Characteristic service.
  // */
  // private UserCharacteristicService getUserCharacteristicService()
  // {
  // return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  // }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  // private MessageService getMessageService()
  // {
  // return (MessageService)getService( MessageService.BEAN_NAME );
  // }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  // private TermsAndConditionService getTermsAndConditionService()
  // {
  // return (TermsAndConditionService)getService( TermsAndConditionService.BEAN_NAME );
  // }
}
