
package com.biperf.core.ui.selfEnrollment;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.util.MessageResources;

import com.biperf.core.domain.Fields;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.termsandcondition.TermsAndConditionService;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.ui.utils.ActionFormUtils;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SelfEnrollmentPaxRegistrationController extends BaseController
{
  private Locale locale;
  private static Locale[] availableLocales = null;
  
  @SuppressWarnings( { "rawtypes" } )
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "termsAndCondition", getTermsAndConditionService().getTermsAndConditionText() );
    request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );
    request.setAttribute( "aboutmeQuestionList", AboutMeQuestionType.getList() );
    request.setAttribute( "countryList", getCountryList() );
    request.setAttribute( "messageSMSGroupTypeList", MessageSMSGroupType.getPlateuOnlyList() );
    request.setAttribute( "emailTypeList", EmailAddressType.getList() );
    request.setAttribute( "addressTypeList", AddressType.getList() );
    request.setAttribute( "phoneTypeList", PhoneType.getList() );
    request.setAttribute( "departmentTypeList", DepartmentType.getList() );
    request.setAttribute( "jobTitleTypeList", PositionType.getList() );
    request.setAttribute( "stateList", AddressFormBean.getStateListByCountryInRequest( request ) );

    SelfEnrollmentPaxRegistrationForm selfEnrollmentPaxRegistrationForm = (SelfEnrollmentPaxRegistrationForm)ActionFormUtils.getActionForm( request,
                                                                                                                                            servletContext,
                                                                                                                                            "selfEnrollmentPaxRegistrationForm" );

    if ( selfEnrollmentPaxRegistrationForm == null )
    {
      selfEnrollmentPaxRegistrationForm = new SelfEnrollmentPaxRegistrationForm();
      request.setAttribute( "selfEnrollmentPaxRegistrationForm", selfEnrollmentPaxRegistrationForm );
    }

    // ***************************************************************************************************
    /*
     * Defect 3600 desires to have Select One as the default. Commented out the below code around in
     * case it gets changed back to US. Looked like it had been done for a reason.
     */
    /*
     * if ( selfEnrollmentPaxRegistrationForm.getAddressFormBean().getCountryCode() == null ) { //
     * default country to us. Country country = getCountryService().getCountryByCode(
     * Country.UNITED_STATES );
     * selfEnrollmentPaxRegistrationForm.getAddressFormBean().setCountryCode(
     * country.getCountryCode() ); }
     */
    // ***************************************************************************************************

    // This is the set of all UserCharacteristic objects
    List availableCharacteristics = getUserCharacteristicService().getAllCharacteristics();

    List characteristicList = CharacteristicUtils.getUserCharacteristicValueList( new HashSet(), availableCharacteristics );

    if ( selfEnrollmentPaxRegistrationForm.getUserCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, selfEnrollmentPaxRegistrationForm.getUserCharacteristicValueList() );
    }

    selfEnrollmentPaxRegistrationForm.setUserCharacteristicValueList( characteristicList );

    // This is a little different than the above Iteration because this iteration is
    // dealing with Characteristic Objects rather than UserCharacteristic Objects
    Iterator availableIt = availableCharacteristics.iterator();
    // Fix for bug# 40727
    if ( availableLocales == null )
    {
      availableLocales = Locale.getAvailableLocales();
      Comparator<Locale> localeComparator = new Comparator<Locale>()
      {
        public int compare( Locale locale1, Locale locale2 )
        {
          return locale1.toString().compareTo( locale2.toString() );
        }
      };
      Arrays.sort( availableLocales, localeComparator );
    }

    for ( Locale availableLocale : availableLocales )
    {
      if ( availableLocale.getCountry().equalsIgnoreCase( selfEnrollmentPaxRegistrationForm.getAddressFormBean().getCountryCode() ) )
      {
        locale = availableLocale;
        break;
      }
      else
      {
        locale = UserManager.getDefaultLocale();
      }
    }
    // Fix for bug# 40727 ends
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( characteristic.getPlName() != null && !characteristic.getPlName().equals( "" ) )
      {
        request.setAttribute( characteristic.getPlName(), DynaPickListType.getList( characteristic.getPlName() ) );
      }
    }
    moveLabelsToRequest( request );

    // Get the ActionMessages
    Object o = request.getAttribute( Globals.ERROR_KEY );
    if ( o != null )
    {
      ActionMessages ae = (ActionMessages)o;

      MessageResources messages = (MessageResources)request.getAttribute( Globals.MESSAGES_KEY );
      List<Fields> messageProperties = new ArrayList<Fields>();
      // Loop thru all the labels in the ActionMessage's
      for ( Iterator i = ae.properties(); i.hasNext(); )
      {
        String property = (String)i.next();
        // Get all messages for this label
        for ( Iterator it = ae.get( property ); it.hasNext(); )
        {
          Fields field = new Fields();
          field.setName( property );

          ActionMessage a = (ActionMessage)it.next();
          String key = a.getKey();
          Object[] values = a.getValues();
          field.setText( messages.getMessage( locale, key, values ) );
          messageProperties.add( field );
        }
      }
      request.setAttribute( "actionMessages", messageProperties );
    }
    
    request.setAttribute( "passwordRequirements", buildPasswordRequirementsCopy() );

  }

  private void moveLabelsToRequest( HttpServletRequest request )
  {
    request.setAttribute( "firstName", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.FIRST_NAME" ) );
    request.setAttribute( "lastName", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.LAST_NAME" ) );
    request.setAttribute( "loginID", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.LOGIN_ID" ) );
    request.setAttribute( "paType", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.PRIMARY_ADDRESS_TYPE" ) );
    request.setAttribute( "add1", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.ADDR1" ) );
    request.setAttribute( "city", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.CITY" ) );
    request.setAttribute( "zipCode", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.ZIP_CODE" ) );
    request.setAttribute( "ssn", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.SSN" ) );
    request.setAttribute( "country", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.COUNTRY" ) );
    request.setAttribute( "state", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.STATE" ) );
  }

  @SuppressWarnings( { "unchecked", "rawtypes", } )
  private List getCountryList()
  {
    List countryList = getCountryService().getAllActive();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }
  
  private String buildPasswordRequirementsCopy()
  {
    PasswordRequirements requirements = getPasswordPolicyStrategy().getPasswordRequirements();

    // if custom regex, this will need to be a custom message
    if ( requirements.isIgnoreValidation() )
    {
      return "";
    }

    String lengthRequirement = String.valueOf( requirements.getMinLength() );
    String distinctChars = getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_DISTINCT_CHARACTER_TYPES ).getIntVal() + "";
    String allowedCharacters = getPasswordPolicyStrategy().buildCharacterTypesAvailableList( requirements );

    Object[] args = null;
    // message changes based on the X of Y requirements
    String cmsMessage = null;
    if ( requirements.getDistinctCharacterTypesRequired() == requirements.getTypesAvailable().size() )
    {
      args = new Object[] { lengthRequirement, allowedCharacters };
      cmsMessage = getCMAssetService().getTextFromCmsResourceBundle( "profile.security.tab.PASSWORD_ALL_TYPES" );
    }
    else
    {
      args = new Object[] { lengthRequirement, distinctChars, allowedCharacters };
      cmsMessage = getCMAssetService().getTextFromCmsResourceBundle( "profile.security.tab.PASSWORD_AT_LEAST_TYPES" );
    }

    return MessageFormat.format( cmsMessage, args );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
  
  private TermsAndConditionService getTermsAndConditionService()
  {
    return (TermsAndConditionService)getService( TermsAndConditionService.BEAN_NAME );
  } 
  
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
  
  private PasswordPolicyStrategy getPasswordPolicyStrategy()
  {
    return (PasswordPolicyStrategy)BeanLocator.getBean( PasswordPolicyStrategy.BEAN_NAME );
  }
  
  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }
}
