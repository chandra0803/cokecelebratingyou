/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/ChangePasswordController.java,v $
 */

package com.biperf.core.ui.profile;

import java.text.MessageFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

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

public class ChangePasswordController extends BaseController
{
  /**
   * Prepares the form for the changePassword page.
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    ChangePasswordForm form = (ChangePasswordForm)request.getAttribute( "changePasswordForm" );
    if ( form != null )
    {
      form.setNewPassword( "" );
      form.setConfirmNewPassword( "" );
    }

    User user = getUserService().getUserById( UserManager.getUser().getUserId() );

    if ( secretDetailsIncomplete( user ) )
    {
      request.setAttribute( "collectSecurityDetailsOnly", "YES" );
    }
    else
    {
      if ( user.getSecretAnswer() != null && user.getSecretQuestionType() != null )
      {
        String secretAnswer = user.getSecretAnswer();
        String secretQuestion = user.getSecretQuestionType().getCode();
        if ( form == null )
        {
          form = new ChangePasswordForm();
          if ( !StringUtils.isEmpty( secretQuestion ) )
          {
            form.setSecretAnswer( secretAnswer );
            form.setSecretQuestion( secretQuestion );
            request.setAttribute( "changePasswordForm", form );
          }
        }
      }
    }
    request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );

    // Can't use the CredentialsNonExpired from authentication because that flag is set
    // when the password expired and other conditions as well, so use password strategy again
    // here...
    if ( getUserService().getPasswordPolicyStrategy().isPasswordExpired( user ) )
    {
      request.setAttribute( "displayExpiredPasswordMessage", "YES" );
    }

    request.setAttribute( "allowPasswordFieldAutoComplete", getSystemVariableService().getPropertyByName( SystemVariableService.ALLOW_PASSWORD_FIELD_AUTO_COMPLETE ).getBooleanVal() );
    
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

  /**
   * Return true if the user has a null question type or a blank answer.
   */
  private boolean secretDetailsIncomplete( User user )
  {
    return !user.isForcePasswordChange() && ( user.getSecretQuestionType() == null || StringUtils.isEmpty( user.getSecretAnswer() ) );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
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
