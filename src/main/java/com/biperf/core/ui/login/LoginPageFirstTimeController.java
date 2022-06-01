
package com.biperf.core.ui.login;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.termsandcondition.TermsAndConditionService;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * LoginPageFirstTimeController.
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
 * <td>doodam</td>
 * <td>Oct 29, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LoginPageFirstTimeController extends BaseController
{
    
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    LoginPageFirstTimeForm loginPageFirstTimeForm = (LoginPageFirstTimeForm)request.getAttribute( "loginPageFirstTimeForm" );

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PHONES ) );
    Participant sessionUser = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );

    if ( null == loginPageFirstTimeForm.getTextPhoneNbr() )
    {
      Set<UserPhone> userPhones = sessionUser.getUserPhones();
      for ( UserPhone userPhone : userPhones )
      {
        if ( userPhone.isPrimary() )
        {
          loginPageFirstTimeForm.setCountryPhoneCode( userPhone.getCountryPhoneCode() );
          loginPageFirstTimeForm.setTextPhoneNbr( userPhone.getPhoneNbr() );
        }
      }
    }
    
    request.setAttribute( "termsAndCondition", getTermsAndConditionService().getTermsAndConditionText() );
    request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );
    request.setAttribute( "aboutmeQuestionList", AboutMeQuestionType.getList() );
    request.setAttribute( "countryList", getCountryList() );
    request.setAttribute( "messageSMSGroupTypeList", MessageSMSGroupType.getPlateuOnlyList() );

    // Bug 3863
    UserAddress userAddress = getUserService().getPrimaryUserAddress( UserManager.getUserId() );
    boolean allowSmsForUserCountry = false;
    if ( userAddress != null )
    {
      if ( userAddress.getAddress() != null && userAddress.getAddress().getCountry() != null )
      {
        allowSmsForUserCountry = userAddress.getAddress().getCountry().getAllowSms();
      }
    }
    /*Client customization starts for WIP #23784*/
    List<Content> termsAndCondInMulLanguages = getResourceCenter();
        
    request.setAttribute("termsAndCondInMulLanguages", termsAndCondInMulLanguages);
    /*Client customization ends for WIP #23784*/
    // END Bug 3863
    request.setAttribute( "allowSmsForUserCountry", allowSmsForUserCountry );
    
    request.setAttribute( "passwordRequirements", buildPasswordRequirementsCopy() );
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

  private List getCountryList()
  {
    List countryList = getCountryService().getActiveCountriesForSmsChecked();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
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
  /*Client customization starts for WIP #23784*/
  private List<Content> getResourceCenter()
  {
     ContentReader contentReader = ContentReaderManager.getContentReader();
     List<Content> resourceCenter = (List<Content>)contentReader.getContent( "participant.termsAndCondition" );
     return resourceCenter;
  }
  /*Client customization ends for WIP #23784*/
}
