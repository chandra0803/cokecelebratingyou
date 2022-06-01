/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/maincontent/MenuOption.java,v $
 */

package com.biperf.core.service.maincontent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * MenuOption.
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
public class MenuOption implements Serializable
{
  private String key;
  private String url;
  private String target;
  private boolean displayAsLink = true;

  private List subMenuOptionList = new ArrayList();

  // Set to TRUE (default) if the key value represents a CM key. if its FALSE, the key represents
  // the actual text
  private boolean shouldLookupKeyInCM = true;

  /**
   * Constructor to create MenuOption
   * 
   * @param key
   * @param url
   */
  public MenuOption( String key, String url )
  {
    this( key, url, null );
  }

  /**
   * Constructor to create MenuOption
   * 
   * @param key
   * @param url
   * @param target
   */
  public MenuOption( String key, String url, String target )
  {
    this.key = key;
    this.url = url;
    this.target = target;
  }

  /**
   * Constructor to create MenuOption
   * 
   * @param key
   * @param link
   */
  public MenuOption( String key, boolean link )
  {
    super();
    this.key = key;
    displayAsLink = link;
  }

  /**
   * @return value of key property
   */
  public String getKey()
  {
    return key;
  }

  /**
   * @return value of subMenuOptionList property
   */
  public final List getSubMenuOptionList()
  {
    return subMenuOptionList;
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

  /**
   * Add a sub menuOption to this menuOption
   * 
   * @param menuOption
   */
  public void addSubMenuOption( MenuOption menuOption )
  {
    subMenuOptionList.add( menuOption );
  }

  public boolean shouldLookupKeyInCM()
  {
    return shouldLookupKeyInCM;
  }

  public void setShouldLookupKeyInCM( boolean shouldLookupKeyInCM )
  {
    this.shouldLookupKeyInCM = shouldLookupKeyInCM;
  }

  /**
   * @return value of displayAsLink property
   */
  public boolean isDisplayAsLink()
  {
    return displayAsLink;
  }

  public boolean isEmpty()
  {
    return subMenuOptionList.isEmpty();
  }

  public void setDisplayAsLink( boolean displayAsLink )
  {
    this.displayAsLink = displayAsLink;
  }
}
