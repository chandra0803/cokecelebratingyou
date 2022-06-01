/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/login/LoginController.java,v $
 */

package com.biperf.core.ui.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;

/**
 * LoginController.
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
 * <td>jenniget</td>
 * <td>Sep 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class LoginController extends BaseController
{

  /**
   * Overridden from
   *
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // retrieve the list of supported languages and put it on the request
    List languages = LanguageType.getList();
    if ( languages.size() == 1 )
    {
      // only display language selection if there is more than one active language.
      languages = new ArrayList();
    }
    request.setAttribute( "languages", languages );
    

    // This is normally the user friendly site name
    // BugFix 17933
    request.setAttribute( "welcomeTo", getSystemVariableService().getPropertyByName( SystemVariableService.LOGIN_PAGE_PERSONAL_DISPLAY_NAME ).getStringVal() );
    request.setAttribute( "displayTermsAndConditionsFooterLink", new Boolean( getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_DISPLAY_LOGIN ).getBooleanVal() ) );
    request.setAttribute( "allowSelfEnrollment", new Boolean( getSystemVariableService().getPropertyByName( SystemVariableService.SELF_ENROLL_ALLOWED ).getBooleanVal() ) );
    request.setAttribute( "isChallengepointInstalled", new Boolean( getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_CHALLENGEPOINT ).getBooleanVal() ) );
    request.setAttribute( "allowPasswordFieldAutoComplete", getSystemVariableService().getPropertyByName( SystemVariableService.ALLOW_PASSWORD_FIELD_AUTO_COMPLETE ).getBooleanVal() );

    if ( request.getSession().getAttribute( "contactUsEmailConfirmation" ) != null )
    {
      request.setAttribute( "contactUsEmailConfirmation", true );
      request.getSession().removeAttribute( "contactUsEmailConfirmation" );
    }
    else
    {
      request.setAttribute( "contactUsEmailConfirmation", false );
    }

    if ( Objects.nonNull( UserManager.getUser() ) )
    {
      response.sendRedirect( response.encodeRedirectURL( RequestUtils.getBaseURI( request ) + "/homePage.do" ) );
    }

  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
