/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/multimedia/Card.java,v $
 */

package com.biperf.core.domain.multimedia;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.UserManager;

/**
 * Card.
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
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class Card extends BaseDomain
{
  private String name;
  private String smallImageName;

  private String largeImageName;
  private boolean active;
  private boolean mobile;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Card ) )
    {
      return false;
    }

    final Card card = (Card)object;

    if ( getName() != null )
    {
      if ( !getName().equals( card.getName() ) )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return hashCode
   */
  public int hashCode()
  {
    return getName() != null ? getName().hashCode() : 0;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getLargeImageName()
  {
    return largeImageName;
  }

  public void setLargeImageName( String largeImageName )
  {
    this.largeImageName = largeImageName;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getSmallImageName()
  {
    return smallImageName;
  }

  public void setSmallImageName( String smallImageName )
  {
    this.smallImageName = smallImageName;
  }

  public boolean isMobile()
  {
    return mobile;
  }

  public void setMobile( boolean mobile )
  {
    this.mobile = mobile;
  }

  public String getLargeImageNameLocale()
  {
    String replaceString = "_" + UserManager.getLocale() + ".";
    String ecardname = getLargeImageName();
    String ecardNameWithLocale = ecardname.replace( ".", replaceString );
    return ecardNameWithLocale;
  }

  public String getLargeImageNameLocale( String useLocale )
  {
    String replaceString = "_" + UserManager.getLocale() + ".";
    if ( !useLocale.equals( "" ) )
    {
      replaceString = "_" + useLocale + ".";
    }
    String ecardname = getLargeImageName();
    String ecardNameWithLocale = ecardname.replace( ".", replaceString );
    return ecardNameWithLocale;
  }

  /*
   * public String getSmallImageNameLocale() { String replaceString = "_" +UserManager.getLocale() +
   * "."; String ecardname = getSmallImageName(); String ecardNameWithLocale = ecardname.replace(
   * ".", replaceString ); return ecardNameWithLocale; }
   */

}
