/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/multimedia/OddCastCategory.java,v $
 */

package com.biperf.core.domain.multimedia;

import com.biperf.core.domain.BaseDomain;

/**
 * OddCastCategory.
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
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OddCastCategory extends BaseDomain
{
  String name;
  String smallImageName;
  String largeImageName;
  boolean active;

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

    if ( ! ( object instanceof OddCastCategory ) )
    {
      return false;
    }

    final OddCastCategory category = (OddCastCategory)object;

    if ( getName() != null )
    {
      if ( !getName().equals( category.getName() ) )
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

  public String getLargeImageName()
  {
    return largeImageName;
  }

  public void setLargeImageName( String largeImageName )
  {
    this.largeImageName = largeImageName;
  }

}
