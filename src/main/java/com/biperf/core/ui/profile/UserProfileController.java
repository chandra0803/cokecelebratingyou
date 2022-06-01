/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/UserProfileController.java,v $
 */

package com.biperf.core.ui.profile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

/**
 * ChangePasswordController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>May 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class UserProfileController extends BaseController
{
  private static final Log logger = LogFactory.getLog( UserProfileController.class );

  /**
   * Prepares the form and request for the userProfile page.
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "webappTitle", getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    
    // fetch the User and place on the request
    Long userId = UserManager.getUserId();
    AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
    userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ALL ) );
    User user = getUserService().getUserByIdWithAssociations( userId, userAssociationRequestCollection );
    request.setAttribute( "user", user );
    boolean hasNodes = !user.getUserNodes().isEmpty();
    request.setAttribute( "hasNodes", String.valueOf( hasNodes ) );

    // NodeToUserNodesAssociationRequest nodeToUserNodesAssociationRequest = new
    // NodeToUserNodesAssociationRequest();
    // associationRequestCollection.add( nodeToUserNodesAssociationRequest );

    // create HashMaps for owners and managers of each Node and place on the request
    HashMap ownerMap = new HashMap( user.getUserNodes().size() );
    HashMap managerSets = new HashMap( user.getUserNodes().size() );
    for ( Iterator iter = user.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();

      // Node node = getNodeService().getNodeWithAssociationsById( userNode.getNode().getId(),
      // associationRequestCollection );

      Node node = getNodeService().getNodeById( userNode.getNode().getId() );

      List owners = getUserService().getAllUsersOnNodeHavingRole( node.getId(), HierarchyRoleType.lookup( HierarchyRoleType.OWNER ), userAssociationRequestCollection );

      // Set owners = node.getUsersByRole( HierarchyRoleType.OWNER );
      if ( owners != null && !owners.isEmpty() )
      {
        ownerMap.put( node.getName(), getFormattedName( (User)owners.iterator().next() ) );
      }

      List managerObjs = getUserService().getAllUsersOnNodeHavingRole( node.getId(), HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ), userAssociationRequestCollection );

      // Set managerObjs = node.getUsersByRole( HierarchyRoleType.MANAGER );
      List managers = new ArrayList( managerObjs.size() );
      for ( Iterator manIter = managerObjs.iterator(); manIter.hasNext(); )
      {
        User mgr = (User)manIter.next();
        managers.add( getFormattedName( mgr ) );
      }
      managerSets.put( node.getName(), managers );
    }
    request.setAttribute( "nodeOwners", ownerMap );
    request.setAttribute( "nodeManagers", managerSets );

    ChangePasswordForm form = (ChangePasswordForm)request.getAttribute( "changePasswordForm" );
    if ( form != null )
    {
      form.setNewPassword( "" );
      form.setConfirmNewPassword( "" );
    }
    else
    {
      ChangePasswordForm changePasswordForm = new ChangePasswordForm();
      UserEmailAddress userEmailAddress = user.getEmailAddressByType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
      if ( userEmailAddress != null )
      {
        changePasswordForm.setEmailAddress( userEmailAddress.getEmailAddr() );
        changePasswordForm.setEmailVerified( VerificationStatusType.VERIFIED.equals( userEmailAddress.getVerificationStatus().getCode() ) );
      }
      else
      {
        changePasswordForm.setEmailAddress( "" );
      }

      UserPhone userPhone = user.getPhoneByType( PhoneType.lookup( PhoneType.RECOVERY ) );
      if ( userPhone != null )
      {
        changePasswordForm.setPhoneNumber( userPhone.getPhoneNbr() );
        changePasswordForm.setCountryPhoneCode( userPhone.getCountryPhoneCode() );
        changePasswordForm.setPhoneVerified( VerificationStatusType.VERIFIED.equals( userPhone.getVerificationStatus().getCode() ) );
      }
      else
      {
        changePasswordForm.setPhoneNumber( "" );
        changePasswordForm.setCountryPhoneCode( "" );
      }
      changePasswordForm.setDisplayOldPassword( !StringUtils.isEmpty( user.getPassword() ) );
      request.setAttribute( "changePasswordForm", changePasswordForm );
    }
    request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );

    request.setAttribute( "passwordRequirements", buildPasswordRequirementsCopy() );

    // security constraint - Launched As / Delegate should not be allowed to mutate/see security
    // info
    boolean maskSecurityQA = UserManager.isUserDelegateOrLaunchedAs();
    request.setAttribute( "maskSecurityQA", maskSecurityQA );
    // don't want to complicate the cmData.xml for previous builds, so this is a yucky work-around
    request.setAttribute( "mask", "*****************" );

    // Choose Sub Nav
    String subNavSelected = (String)tileContext.getAttribute( "subNavSelected" );
    request.setAttribute( "subNavSelected", subNavSelected );
    request.setAttribute( "countryList", getCountryList() );
    logger.debug( "subNavSelected=" + subNavSelected );
  }

  private String buildPasswordRequirementsCopy()
  {
    PasswordRequirements requirements = getPasswordPolicyStrategy().getPasswordRequirements();
    String lengthRequirement = String.valueOf( requirements.getMinLength() );

    // if custom regex, this will need to be a custom message
    if ( requirements.isIgnoreValidation() )
    {
      String lengthMessage = getCMAssetService().getTextFromCmsResourceBundle( "profile.security.tab.PASSWORD_MIN_LENGTH" );
      return MessageFormat.format( lengthMessage, new Object[] { lengthRequirement } );
    }

    String distinctChars = getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_DISTINCT_CHARACTER_TYPES ).getIntVal() + "";
    String allowedCharacters = getPasswordPolicyStrategy().buildCharacterTypesAvailableList( requirements );
    String cmsMessage = null;

    Object[] args = null;
    // message changes based on the X of Y requirements
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

  private String getFormattedName( User user )
  {
    String name = user.getLastName() + ", " + user.getFirstName();
    if ( user.getMiddleName() != null && !user.getMiddleName().equals( "" ) )
    {
      name = name + " " + user.getMiddleName().substring( 0, 1 );
    }
    return name;
  }

  @SuppressWarnings( "unchecked" )
  private List<Country> getCountryList()
  {
    List<Country> countryList = getCountryService().getAllActive();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }

  /**
   * Get the UserService.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
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