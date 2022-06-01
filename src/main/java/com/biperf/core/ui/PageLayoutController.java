/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/PageLayoutController.java,v $
 */

package com.biperf.core.ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserCookiesAcceptance;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.participant.UserCookiesAcceptanceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

/*
 * PageLayoutController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Nov
 * 22, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 *
 *
 */

public class PageLayoutController extends BaseController
{
  private static final Log logger = LogFactory.getLog( PageLayoutController.class );
  private static final String DEFAULT_CLIENT_NAME = "G5 Application";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Sets up the environment for the page layout tiles.
   *
   * @param tileContext the tileContext for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the tileContext for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    logger.debug( "In PageLayoutController" );

    String quicksearchbody = "";
    if ( UserManager.getUser() != null && UserManager.getUser().isUser() )
    {
      quicksearchbody = "quicksearch";
    }
    logger.debug( "quicksearchbody=" + quicksearchbody );
    request.setAttribute( "quicksearchbody", quicksearchbody );

    request.setAttribute( "designTheme", getDesignTheme() );
    request.setAttribute( "webappTitle", getClientName() );
    request.setAttribute( "siteUrlPrefix", getSiteUrlPrefix() );
    request.setAttribute( "textEditorDictionaries", getTextEditorDictionaries() );

    // New Service Anniversary Celebration Module Enabling.
    if ( isPurlAvailable() && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      request.setAttribute( "purlUrlPrefix", getPurlUrlPrefix() );
    }

    // Bug Fix # 21169 - It does not display menu option other than home page
    request.setAttribute( "displayTandC", displayTandC() );

    // PURL properties
    request.setAttribute( "isUserLoggedIn", new Boolean( UserManager.isUserLoggedIn() ) );

    Locale locale = UserManager.getLocale();
    request.setAttribute( "LOCALE_SET_IN_REQUEST", locale );
    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( locale ) );
    request.setAttribute( "JstlDateTimePattern", DateFormatterUtil.getDateTimeSecPattern( locale ) );
    request.setAttribute( "JstlDateTimeTZPattern", DateFormatterUtil.getDateTimeSecTZPattern( locale ) );
    request.setAttribute( "TinyMceDatePattern", DateFormatterUtil.getTinyMceDatePattern( locale ) );

    request.setAttribute( "TinyMceDateTimePattern", DateFormatterUtil.getTinyMceDateTimePattern( locale ) );

    // ROSTER MGMT properties
    request.setAttribute( "isRosterMgmtAvailable", isRosterMgmtAvailable() );
    Long rosterManagerId = getRosterManagerId( request );
    request.setAttribute( "rosterManagerId", rosterManagerId );
    request.setAttribute( "isRosterMgmtAccessible", isRosterMgmtAccessible( rosterManagerId ) );

    request.setAttribute( "isIncludeBalance", isIncludeBalance() );
    request.setAttribute( "displayTermsAndConditionsFooterLink", isTermsAndConditionsFooterLinkUsed() );
    request.setAttribute( "allowSelfEnrollment", new Boolean( getSystemVariableService().getPropertyByName( SystemVariableService.SELF_ENROLL_ALLOWED ).getBooleanVal() ) );

    // Client and program logos
    request.setAttribute( "clientLogo", getDesignThemeService().getSkinContentByKey( getDesignTheme(), DesignThemeService.CLIENT_LOGO ) );

    Boolean showProgramLogo = getBooleanPropertyValue( SystemVariableService.SECONDARY_LOGO_ENABLE );
    request.setAttribute( "showProgramLogoAndName", showProgramLogo );
    request.setAttribute( "programLogo", getDesignThemeService().getSkinContentByKey( getDesignTheme(), DesignThemeService.PROGRAM_LOGO ) );
    request.setAttribute( "programName", getDesignThemeService().getSkinContentByKey( getDesignTheme(), DesignThemeService.PROGRAM_NAME ) );

    // GDPR Compliance start
    request.setAttribute( "cookiesAccepted", true );
    UserCookiesAcceptance userAccpObj = getUserCookiesAcceptanceService().getUserCookiesAcceptanceDetailsByPaxID( UserManager.getUserId() );
    if ( Objects.isNull( userAccpObj ) )
    {
      String envUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      String policyUrl = MessageFormat.format( ContentReaderManager.getText( "recognition.submit", "COOKIES_BODY" ), new Object[] { envUrl } );
      request.setAttribute( "policyUrl", policyUrl );
      request.setAttribute( "cookiesAccepted", false );
    }
    // GDPR Compliance end

    // Hide settings tab for delegators (They have nothing to see there). Participant themselves and
    // admin can see it.
    if ( UserManager.isUserLoggedIn() && UserManager.getUser().isDelegate() )
    {
      request.setAttribute( "hideSettingsTab", true );
    }
  }

  private boolean isTermsAndConditionsFooterLinkUsed()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_DISPLAY_LOGIN ).getBooleanVal();
  }

  private boolean isIncludeBalance()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.BOOLEAN_INCLUDE_BALANCE ).getBooleanVal();
  }

  private Long getRosterManagerId( HttpServletRequest request )
  {
    if ( UserManager.isUserLoggedIn() )
    {
      return UserManager.getUserId();
    }
    return null;
  }

  private Boolean isRosterMgmtAccessible( Long rosterManagerId )
  {
    if ( null != rosterManagerId )
    {
      return new Boolean( UserManager.getUser().isManager() );
    }
    return Boolean.FALSE;
  }

  private Boolean isRosterMgmtAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.ROSTER_MGMT_AVAILABLE );
  }

  private Boolean displayTandC()
  {
    return getBooleanPropertyValue( SystemVariableService.TERMS_CONDITIONS_USED );
  }

  private Boolean isPurlAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.PURL_AVAILABLE );
  }

  private Boolean getBooleanPropertyValue( String propertyName )
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( propertyName );
    return property != null ? new Boolean( property.getBooleanVal() ) : Boolean.FALSE;
  }

  private String getClientName()
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME );
    return property != null ? property.getStringVal() : DEFAULT_CLIENT_NAME;
  }

  private String getTextEditorDictionaries()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.TEXT_EDITOR_DICTIONARIES ).getStringVal();
  }

  private String getSiteUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  private String getPurlUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
  }

  private String getDesignTheme()
  {
    return getDesignThemeService().getDesignTheme( UserManager.getUser() );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private DesignThemeService getDesignThemeService()
  {
    return (DesignThemeService)getService( DesignThemeService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private static UserCookiesAcceptanceService getUserCookiesAcceptanceService()
  {
    return (UserCookiesAcceptanceService)getService( UserCookiesAcceptanceService.BEAN_NAME );
  }

}
