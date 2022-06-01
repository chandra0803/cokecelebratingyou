/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/utils/DisplayTagI18nCMAdapter.java,v $
 */

package com.biperf.core.ui.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.displaytag.localization.I18nResourceProvider;
import org.displaytag.localization.LocaleResolver;

import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Pull i18n content from Content Manager from a display tag.
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
 * <td>dunne</td>
 * <td>Apr 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DisplayTagI18nCMAdapter implements I18nResourceProvider, LocaleResolver
{

  /**
   * prefix/suffix for missing entries.
   */
  public static final String UNDEFINED_KEY = "???";

  /**
   * The the resource from CM. The CMAsset must be previously specified.
   * 
   * @param resourceKey
   * @param defaultValue
   * @param tag
   * @param pageContext
   * @return String
   */
  public String getResource( String resourceKey, String defaultValue, Tag tag, PageContext pageContext )
  {
    // if resourceKey isn't defined either, use defaultValue
    String key = resourceKey != null ? resourceKey : defaultValue;

    String message = null;
    try
    {
      message = CmsResourceBundle.getCmsBundle().getString( key );
    }
    catch( Exception e )
    {
      message = "????" + e.getMessage() + "????";
    }

    // if user explicitly added a titleKey we guess this is an error
    if ( message == null && resourceKey != null )
    {
      // log.debug(Messages.getString("Localization.missingkey", resourceKey)); //$NON-NLS-1$
      message = UNDEFINED_KEY + resourceKey + UNDEFINED_KEY;
    }

    return message;
  }

  public Locale resolveLocale( HttpServletRequest request )
  {
    Locale userLocale = null;
    HttpSession session = request.getSession( false );

    if ( session != null )
    {
      if ( null != UserManager.getUser() )
      {
        userLocale = UserManager.getLocale();
      }

    }
    if ( userLocale == null )
    {
      // Returns Locale based on Accept-Language header or the server default
      userLocale = request.getLocale();
    }

    return userLocale;
  }
}
