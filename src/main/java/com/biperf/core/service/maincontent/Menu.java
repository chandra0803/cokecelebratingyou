/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/maincontent/Menu.java,v $
 */

package com.biperf.core.service.maincontent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu.
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
 * <td>sharma</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Menu implements Serializable
{
  private String asset;
  private String key;
  private String target;
  private String url;
  private List menuOptionsList = new ArrayList();
  private boolean admin;
  private boolean javascript;

  /**
   * Constructor to create a Menu Object.
   * 
   * @param asset
   * @param key
   * @param url
   * @param isAdmin
   */
  public Menu( String asset, String key, String url, boolean isAdmin )
  {
    this( asset, key, url, null, isAdmin );
  }

  /**
   * Constructor to create a Menu Object.
   * 
   * @param asset
   * @param key
   * @param url
   * @param target
   * @param isAdmin
   */
  public Menu( String asset, String key, String url, String target, boolean isAdmin )
  {
    this.asset = asset;
    this.key = key;
    this.url = url;
    this.target = target;
    this.admin = isAdmin;
  }

  /**
   * @return value of menuOptionsList property
   */
  public final List getMenuOptionsList()
  {
    return menuOptionsList;
  }

  /**
   * Add a menu option to this menu
   * 
   * @param menuOption
   */
  public void addMenuOption( MenuOption menuOption )
  {
    if ( menuOption != null )
    {
      menuOptionsList.add( menuOption );
    }
  }

  /**
   * @return value of asset property
   */
  public String getAsset()
  {
    return asset;
  }

  /**
   * @return value of key property
   */
  public String getKey()
  {
    return key;
  }

  /**
   * @return value of url property
   */
  public String getUrl()
  {
    return url;
  }

  public String getTarget()
  {
    return target;
  }

  public boolean isAdmin()
  {
    return admin;
  }

  public void setAdmin( boolean admin )
  {
    this.admin = admin;
  }

  public boolean isEmpty()
  {
    return menuOptionsList.isEmpty();
  }

  public boolean isJavascript()
  {
    return javascript;
  }

  public void setJavascript( boolean javascript )
  {
    this.javascript = javascript;
  }
}
