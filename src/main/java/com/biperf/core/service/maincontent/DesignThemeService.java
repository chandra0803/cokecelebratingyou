/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/maincontent/DesignThemeService.java,v $
 */

package com.biperf.core.service.maincontent;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.SAO;

/**
 * DesignThemeService.
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
 * <td>zahler</td>
 * <td>Feb 21, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface DesignThemeService extends SAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "designThemeService";
  public static final String DEFAULT_DESIGN_THEME = "default";
  public static final String SKINS_FOLDER = "/assets/skins/";
  public static final String IMAGES_FOLDER = "/img/";
  public static final String STYLES_FOLDER = "/css/";

  public static final String CLIENT_LOGO = "CLIENT_LOGO";
  public static final String PROGRAM_LOGO = "PROGRAM_LOGO";
  public static final String PROGRAM_NAME = "PROGRAM_NAME";
  public static final String EMAIL_LOGO = "EMAIL_CLIENT_LOGO";
  public static final String EMAIL_BACKGROUND = "EMAIL_PHOTO";

  /**
   * Flushs from caches objects associated with the specified user.
   * @param authenticatedUser
   */
  public void processLogout( AuthenticatedUser authenticatedUser );

  /**
   * Clears the skin cache.
   * 
   * @param user an authenticated user.
   */
  public void clearSkinCache( AuthenticatedUser user );

  /**
   * Returns the name of the default design theme.
   *
   * @return the name of the default design theme.
   */
  public String getDefaultDesignTheme();

  public String getSkinContentByKey( String designTheme, String key );

  /**
   * Returns the name of the design theme for the specified user.
   * 
   * @param user an authenticated user.
   * @return the name of the design theme for the specified user.
   */
  public String getDesignTheme( AuthenticatedUser user );
}
