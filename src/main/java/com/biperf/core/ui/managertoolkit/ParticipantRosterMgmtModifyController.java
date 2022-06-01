
package com.biperf.core.ui.managertoolkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ParticipantRosterMgmtModifyController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    UserForm form = (UserForm)request.getAttribute( UserForm.NAME );

    Long participantId = UserManager.getUserId();
    if ( form != null && StringUtils.isEmpty( form.getUserName() ) )
    {
      boolean isTermsConditionsRequired = isTermsAndConditionsUsed();
      if ( isTermsConditionsRequired )
      {
        form.setPaxStatus( ParticipantStatus.INACTIVE );
        form.setPaxStatusDesc( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ).getName() );
        form.setPaxTermsAccept( ParticipantTermsAcceptance.NOTACCEPTED );
        form.setPaxTermsAcceptDesc( "not accepted" );
      }
      else
      {
        // if T&Cs off,
        // defaults the Pax Status to Active and T&Cs Acceptance to Not Accepted
        form.setPaxStatus( ParticipantStatus.ACTIVE );
        form.setPaxStatusDesc( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ).getName() );
        form.setPaxTermsAccept( ParticipantTermsAcceptance.NOTACCEPTED );
        form.setPaxTermsAcceptDesc( "not accepted" );
      }
    }

    if ( form != null && StringUtils.isEmpty( form.getNameOfNode() ) && !StringUtils.isEmpty( form.getNodeId() ) )
    {
      form.setNameOfNode( getNodeService().getNodeById( new Long( form.getNodeId() ) ).getName() );
    }

    // Set the default employer
    if ( form != null && StringUtils.isEmpty( form.getEmployerId() ) )
    {
      ParticipantEmployer participantEmployer = getParticipantService().getCurrentParticipantEmployer( participantId );
      if ( null != participantEmployer )
      {
        form.setEmployerId( String.valueOf( participantEmployer.getEmployer().getId() ) );
      }
    }

    // Set the default country
    if ( form != null && form.getAddressFormBean() != null && StringUtils.isEmpty( form.getAddressFormBean().getCountryCode() ) )
    {
      Country country = getCountryService().getCountryByCode( Country.UNITED_STATES );
      if ( CountryStatusType.ACTIVE.equalsIgnoreCase( country.getStatus().getCode() ) )
      {
        form.getAddressFormBean().setCountryCode( country.getCountryCode() );
        form.getAddressFormBean().setCountryName( country.getI18nCountryName() );
      }
      else
      {
        List activeCountryList = getCountryService().getAllActive();
        if ( activeCountryList != null )
        {
          if ( activeCountryList.size() == 1 )
          {
            country = (Country)activeCountryList.get( 0 );
            form.getAddressFormBean().setCountryCode( country.getCountryCode() );
            form.getAddressFormBean().setCountryName( country.getI18nCountryName() );
          }
          else
          {
            request.setAttribute( "multiple", new Boolean( true ) );
          }
        }
      }
    }

    if ( form != null && form.getAddressFormBean() != null && !StringUtils.isEmpty( form.getAddressFormBean().getCountryCode() ) )
    {
      Country country = getCountryService().getCountryByCode( form.getAddressFormBean().getCountryCode() );
      // fix for bug# 40946
      form.setCountryPhoneCode( country.getPhoneCountryCode() );
      request.setAttribute( "editconuntryPhoneCode", form.getCountryPhoneCode() ); // edit page
                                                                                   // alone
      form.setCountryPhoneCode( country.getPhoneCountryCode() );
    }

    List countryList = getCountryList();
    if ( form != null && form.getAddressFormBean() != null )
    {
      request.setAttribute( "countryCode", form.getAddressFormBean().getCountryCode() );
    }
    request.setAttribute( "countryList", countryList );
    request.setAttribute( "stateList", AddressFormBean.getStateListByCountryInRequest( request ) );
    if ( form != null && form.getAddressFormBean() != null )
    {
      request.setAttribute( "requirePostalCode", new Boolean( requirePostalCodeByCountryCode( form.getAddressFormBean().getCountryCode(), countryList ) ) );
    }
    request.setAttribute( "paxStatusList", ParticipantStatus.getList() );
    request.setAttribute( "suspensionStatusList", ParticipantSuspensionStatus.getList() );
    request.setAttribute( "addressTypeList", AddressType.getList() );
    request.setAttribute( "emailTypeList", EmailAddressType.getList() );
    request.setAttribute( "phoneTypeList", PhoneType.getList() );
    request.setAttribute( "jobPositionList", PositionType.getList() );
    request.setAttribute( "languageList", LanguageType.getList() );
    request.setAttribute( "nodeRelationshipList", getNodeRelationshipList() );
    request.setAttribute( "departmentList", DepartmentType.getList() );
    // Set the default for participant to be member
    if ( form != null )
    {
      if ( StringUtils.isEmpty( form.getNodeRelationship() ) )
      {
        form.setNodeRelationship( HierarchyRoleType.MEMBER );
      }
      else if ( HierarchyRoleType.OWNER.equals( form.getNodeRelationship() ) )
      {
        request.setAttribute( "nodeOwner", HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
      }
    }

    // User Characteristics
    Set userCharacteristics = new LinkedHashSet();
    User user = null;
    if ( form != null && form.getRosterPaxuserId() != null )
    {
      user = getUserService().getUserById( form.getRosterPaxuserId() );
      userCharacteristics = getUserService().getUserCharacteristics( form.getRosterPaxuserId() );
    }

    // This is the set of UserCharacteristic objects the user has values for.
    List availableCharacteristics = getUserCharacteristicService().getAllCharacteristics();
    List characteristicList = CharacteristicUtils.getUserCharacteristicValueList( userCharacteristics, availableCharacteristics );
    if ( form != null && form.getUserCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, form.getUserCharacteristicValueList() );
    }

    if ( form != null )
    {
      form.setUserCharacteristicValueList( characteristicList );
    }

    // If there is a Dynamic (Dyna) Pick List for any of the Characteristics, that characteristic
    // will have a value for plName. Each of these dyna pick lists needs to be set
    // in the request. This needs to be done for the userCharacteristics Set, and
    // the availableCharacteristics Set.
    String DELETE_VALUE = "delete_option";
    Iterator currentIt = userCharacteristics.iterator();
    while ( currentIt.hasNext() )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)currentIt.next();
      if ( userCharacteristic.getUserCharacteristicType().getPlName() != null && !userCharacteristic.getUserCharacteristicType().getPlName().equals( "" ) )
      {
        request.setAttribute( userCharacteristic.getUserCharacteristicType().getPlName(), DynaPickListType.getList( userCharacteristic.getUserCharacteristicType().getPlName() ) );
      }
      else
      {
        if ( userCharacteristic.getUserCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.SINGLE_SELECT )
            || userCharacteristic.getUserCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.MULTI_SELECT ) )
        {
          request.setAttribute( userCharacteristic.getUserCharacteristicType().getPlName(), DELETE_VALUE );
        }
      }
    }
    Iterator availableIt = availableCharacteristics.iterator();
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( !StringUtils.isEmpty( characteristic.getPlName() ) )
      {
        request.setAttribute( characteristic.getPlName(), DynaPickListType.getList( characteristic.getPlName() ) );
      }
    }
    if ( form != null && form.getRosterPaxuserId() != null )
    {
      request.setAttribute( "user", user );
    }

    if ( form != null && form.getId() != 0 )
    {
      request.setAttribute( "pageTitle", CmsResourceBundle.getCmsBundle().getString( "participant.roster.management.add.EDIT_TITLE" ) );
      request.setAttribute( "newParticipant", false );
    }
    else
    {
      request.setAttribute( "pageTitle", CmsResourceBundle.getCmsBundle().getString( "participant.roster.management.add.ADD_TITLE" ) );
      request.setAttribute( "newParticipant", true );
    }

  }

  private List getNodeRelationshipList()
  {
    // Only Menagaer and Member options are available
    List list = new ArrayList();
    list.add( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );
    list.add( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    return list;
  }

  private List getCountryList()
  {
    List countryList = getCountryService().getAllActive();
    Collections.sort( countryList, new CountryComparator() );
    return countryList;
  }

  private boolean requirePostalCodeByCountryCode( String countryCode, List countryList )
  {
    Country country = null;
    for ( int i = 0; i < countryList.size(); i++ )
    {
      country = (Country)countryList.get( i );
      if ( country.getCountryCode().equals( countryCode ) )
      {
        return country.getRequirePostalCode().booleanValue();
      }
    }
    return false;
  }

  private boolean isTermsAndConditionsUsed()
  {
    boolean isTermsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
    return isTermsAndConditionsUsed;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private NodeService getNodeService() throws Exception
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }
}
