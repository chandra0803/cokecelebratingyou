/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/CmsContentReaderFactory.java,v $
 *
 */

package com.biperf.core.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.WebApplicationContext;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.security.AuthenticationService;
import com.objectpartners.cms.domain.enums.ContentStatusEnum;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.service.impl.ContentReaderImpl;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderFactory;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * CmsContentReaderFactory <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Aug 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CmsContentReaderFactory implements ContentReaderFactory
{

  private static final String CM_WAR_CONTEXT_PATH_SUFFIX = "-cm";

  private CmsConfiguration cmsConfiguration = null;

  /**
   * This parameter can be set in the request or session. If it is set in the request, it is
   * automatically set in the session. Set to the locale string, i.e. "en" or "de".
   */
  public static String CMS_LOCALE_CODE = "cmsLocaleCode";

  /**
   * This parameter can be set in the request or session. If it is set in the request, it is
   * automatically set in the session. Set to either "Live" or "Latest". The default is Live.
   */
  public static String CMS_CONTENT_STATUS = "cmsContentStatus";

  /**
   * Create a new ContentReader using the HttpServletRequest
   * 
   * @param request
   * @return ContentReader
   * @throws CmsServiceException
   */
  public ContentReader createContentReader( HttpServletRequest request ) throws CmsServiceException
  {
    SecurityContext acegiContext = (SecurityContext)request.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
    AuthenticatedUser user = null;
    if ( acegiContext != null )
    {
      Authentication auth = acegiContext.getAuthentication();
      Object obj = null;
      if ( auth != null )
      {
        obj = auth.getPrincipal();
      }
      if ( obj != null && obj instanceof AuthenticatedUser )
      {
        user = (AuthenticatedUser)obj;
      }
    }

    Set audienceNames = null;
    String applicationCode = cmsConfiguration.getDefaultApplication();
    String parentApplicationCode = cmsConfiguration.getDefaultParentApplication();
    Locale locale = null;
    ContentStatusEnum contentStatus = ContentStatusEnum.LIVE;

    if ( user == null )
    {
      // set up the contentReader with the defaults
      audienceNames = new HashSet( cmsConfiguration.getDefaultAudienceNames() );
    }
    else
    {
      // set up the contentReader with user settings
      if ( user.getLocale() != null )
      {
        locale = user.getLocale();
        // if(locale.getLanguage().equalsIgnoreCase( "en" )){
        // locale = CmsUtil.getLocale( getParameterFromServletRequest( request, CMS_LOCALE_CODE,
        // locale.toString() ) );
        // if(!locale.getLanguage().equalsIgnoreCase( "en" ))
        user.setLocale( locale );
        // }

      }
      audienceNames = user.getAudienceNames();
    }

    if ( null == locale )
    {
      // hook up default content reader so that system variable can be read for default language.
      // system variable has picklist in itself and picklist cant work without content reader.
      javax.servlet.ServletContext cmsServletContext = request.getSession().getServletContext().getContext( request.getContextPath() );
      ApplicationContext cmsAppContext = (ApplicationContext)cmsServletContext.getAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE );
      ContentReader reader = new ContentReaderImpl( applicationCode, parentApplicationCode, Locale.US, cmsConfiguration.getDefaultLocale(), contentStatus, audienceNames );
      reader.setApplicationContext( cmsAppContext );
      ContentReaderManager.setContentReader( reader );

      // hook up code ends.

      locale = UserManager.getDefaultLocale();
      locale = CmsUtil.getLocale( getParameterFromServletRequest( request, CMS_LOCALE_CODE, locale.toString() ) );
    }

    contentStatus = ContentStatusEnum.getInstance( getParameterFromServletRequest( request, CMS_CONTENT_STATUS, contentStatus.getName() ) );

    if ( null != request.getSession().getAttribute( CMS_LOCALE_CODE ) && ! ( (String)request.getSession().getAttribute( CMS_LOCALE_CODE ) ).equalsIgnoreCase( "en_US" ) && user != null )
    {
      if ( request.getSession().getAttribute( CMS_LOCALE_CODE + "_userSelected" ) != null
          && ( (String)request.getSession().getAttribute( CMS_LOCALE_CODE + "_userSelected" ) ).equalsIgnoreCase( "true" ) )
      {
        user.setLocale( CmsUtil.getLocale( (String)request.getSession().getAttribute( CMS_LOCALE_CODE ) ) );
        locale = CmsUtil.getLocale( (String)request.getSession().getAttribute( CMS_LOCALE_CODE ) );
      }

    }

    return new ContentReaderImpl( applicationCode, parentApplicationCode, locale, cmsConfiguration.getDefaultLocale(), contentStatus, audienceNames );
  }

  /**
   * Check the request for a parameter. If parameter is in the request, set a session varable If
   * parameter is in the session - use it else use the default.
   * 
   * @param request
   * @param parameter
   * @param defaultValue
   * @return String
   */
  private String getParameterFromServletRequest( HttpServletRequest request, String parameter, String defaultValue )
  {
    String userSelected = "false";
    if ( StringUtils.isNotEmpty( request.getParameter( parameter ) ) )
    {
      request.getSession().setAttribute( parameter, request.getParameter( parameter ) );
      userSelected = "true";
      request.getSession().setAttribute( parameter + "_userSelected", userSelected );
      return request.getParameter( parameter );
    }
    if ( StringUtils.isNotEmpty( (String)request.getSession().getAttribute( parameter ) ) )
    {
      return (String)request.getSession().getAttribute( parameter );
    }

    request.getSession().setAttribute( parameter, defaultValue );

    return defaultValue;

  }

  public void setCmsConfiguration( CmsConfiguration cmsConfig )
  {
    cmsConfiguration = cmsConfig;
  }

}
