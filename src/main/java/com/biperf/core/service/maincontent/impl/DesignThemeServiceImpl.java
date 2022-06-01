/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/maincontent/impl/DesignThemeServiceImpl.java,v $
 */

package com.biperf.core.service.maincontent.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.system.SystemVariableService;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * DesignThemeServiceImpl.
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
public class DesignThemeServiceImpl implements DesignThemeService
{
  /* Skin Constants */
  private static final String DESIGN_THEME = "DESIGN_THEME";

  @SuppressWarnings( "rawtypes" )
  private Map skinCache = new ConcurrentHashMap();
  private SystemVariableService systemVariableService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.maincontent.DesignThemeService#processLogout(com.biperf.core.security.AuthenticatedUser)
   * @param authenticatedUser
   */
  public void processLogout( AuthenticatedUser authenticatedUser )
  {
    clearSkinCache( authenticatedUser );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.maincontent.DesignThemeService#clearSkinCache(com.biperf.core.security.AuthenticatedUser)
   * @param user
   */
  public void clearSkinCache( AuthenticatedUser user )
  {
    skinCache.remove( user.getUserId() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.maincontent.DesignThemeService#getDefaultDesignTheme()
   * @return String
   */
  public String getDefaultDesignTheme()
  {
    String designTheme = null;

    Content skin = getDefaultSkin();
    if ( skin != null )
    {
      designTheme = (String)skin.getContentDataMap().get( DESIGN_THEME );
    }

    if ( designTheme == null )
    {
      designTheme = DEFAULT_DESIGN_THEME;
    }

    return designTheme;
  }

  // fetch value from system.skin if designtheme is something other than 'default'.
  // when system.skin designtheme value is set to 'default, system.skin.default
  // and system.skin content values will be same
  public String getSkinContentByKey( String designTheme, String key )
  {
    String assetCode = "system.skin.default";
    String contentValue = null;

    if ( !designTheme.equalsIgnoreCase( getDefaultDesignTheme() ) )
    {
      assetCode = "system.skin";
    }

    contentValue = ContentReaderManager.getText( assetCode, key );

    return contentValue;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.maincontent.DesignThemeService#getDesignTheme(com.biperf.core.security.AuthenticatedUser)
   * @param user
   * @return String
   */
  public String getDesignTheme( AuthenticatedUser user )
  {
    String designTheme = null;
    Content skin = null;

    if ( user != null )
    {
      skin = (Content)skinCache.get( user.getUserId() );
    }

    if ( skin == null )
    {
      skin = buildSkin( user );
      if ( skin != null && user != null )
      {
        skinCache.put( user.getUserId(), skin );
      }
    }

    if ( skin != null )
    {
      designTheme = (String)skin.getContentDataMap().get( DESIGN_THEME );
    }

    return designTheme;
  }

  /**
   * Returns the skin for the authenticated user. A skin is a collection of style sheets and images
   * that define the look of an application.
   * 
   * @return the skin for the authenticated user.
   */
  private Content buildSkin( AuthenticatedUser user )
  {
    Content skin = null;

    if ( skin == null )
    {
      skin = getSkin();
    }

    if ( skin == null )
    {
      skin = getDefaultSkin();
    }

    return skin;
  }

  /**
   * Returns the Content Manager asset that describes the skin.
   * 
   * @return the skin.
   */
  private Content getSkin()
  {
    Content skin = null;

    List skinList = (List)ContentReaderManager.getContentReader().getContent( "system.skin" );
    if ( skinList != null && skinList.size() > 0 )
    {
      skin = (Content)skinList.get( 0 );
    }

    return skin;
  }

  /**
   * Returns the Content Manager asset that describes the default skin.
   * 
   * @return the default skin.
   */
  private Content getDefaultSkin()
  {
    Content defaultSkin = null;

    List defaultSkinList = (List)ContentReaderManager.getContentReader().getContent( "system.skin.default" );
    if ( defaultSkinList != null && defaultSkinList.size() > 0 )
    {
      defaultSkin = (Content)defaultSkinList.get( 0 );
    }

    return defaultSkin;
  }

//  public void setCacheFactory( CacheFactory cacheFactory )
//  {
//    skinCache = cacheFactory.getCache( "skins" );
//  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }
}
