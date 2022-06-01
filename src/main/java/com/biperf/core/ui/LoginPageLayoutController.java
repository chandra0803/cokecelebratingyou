/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/LoginPageLayoutController.java,v $
 */

package com.biperf.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.StringUtil;

public class LoginPageLayoutController extends BaseController
{
  /**
   * Sets up the environment for the login page layout tile.
   *
   * @param tileContext the tileContext for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the tileContext for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    List languages = LanguageType.getList();
    String languageCode;
    if ( languages.size() == 1 )
    {
      // only display language selection if there is more than one active language.
      languages = new ArrayList();
    }
    request.setAttribute( "languages", languages );
    request.setAttribute( "defaultDesignTheme", getDefaultDesignTheme() );
    request.setAttribute( "designTheme", getDefaultDesignTheme() );
    request.setAttribute( "webappTitle", getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    request.setAttribute( "siteUrlPrefix", getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

    // Client and program logos
    request.setAttribute( "clientLogo", getDesignThemeService().getSkinContentByKey( getDefaultDesignTheme(), DesignThemeService.CLIENT_LOGO ) );

    Boolean showProgramLogo = getSystemVariableService().getPropertyByName( SystemVariableService.SECONDARY_LOGO_ENABLE ).getBooleanVal();
    request.setAttribute( "showProgramLogoAndName", showProgramLogo );
    request.setAttribute( "programLogo", getDesignThemeService().getSkinContentByKey( getDefaultDesignTheme(), DesignThemeService.PROGRAM_LOGO ) );
    request.setAttribute( "programName", getDesignThemeService().getSkinContentByKey( getDefaultDesignTheme(), DesignThemeService.PROGRAM_NAME ) );

    if ( !StringUtil.isEmpty( request.getParameter( "cmsLocaleCode" ) ) )
    {
      languageCode = request.getParameter( "cmsLocaleCode" );
      request.getSession().setAttribute( "languageCodeSelfEnrolled", languageCode );
    }
  }

  private String getDefaultDesignTheme()
  {
    return getDesignThemeService().getDefaultDesignTheme();
  }

  private DesignThemeService getDesignThemeService()
  {
    return (DesignThemeService)getService( DesignThemeService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
