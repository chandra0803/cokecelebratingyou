/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/proxy/Proxy.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.proxy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ProxyCoreAccessType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.UserProxyUtils;

/**
 * Proxy.
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
 * <td>sedey</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Proxy extends BaseDomain
{
  private Participant proxyUser;
  private Participant user;
  private boolean allModules;
  private String coreAccess;
  private Set proxyModules = new LinkedHashSet();
  private boolean allowLeaderboard;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( object instanceof Proxy )
    {
      Proxy proxy = (Proxy)object;

      if ( proxy.getProxyUser() != null && !proxy.getProxyUser().equals( this.getProxyUser() ) )
      {
        equals = false;
      }
      if ( proxy.getUser() != null && !proxy.getUser().equals( this.getUser() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = this.getProxyUser() != null ? this.getProxyUser().hashCode() : 0;
    result = 29 * result + ( getUser() != null ? getUser().hashCode() : 0 );

    return result;
  }

  public Set getProxyModules()
  {
    return proxyModules;
  }

  public void setProxyModules( Set proxyModules )
  {
    this.proxyModules = proxyModules;
  }

  public void addProxyModule( ProxyModule proxyModule )
  {
    proxyModule.setProxy( this );
    this.proxyModules.add( proxyModule );
  }

  public Participant getProxyUser()
  {
    return proxyUser;
  }

  public void setProxyUser( Participant proxyUser )
  {
    this.proxyUser = proxyUser;
  }

  public Participant getUser()
  {
    return user;
  }

  public void setUser( Participant user )
  {
    this.user = user;
  }

  public boolean isAllModules()
  {
    return allModules;
  }

  public void setAllModules( boolean allModules )
  {
    this.allModules = allModules;
  }

  public String getCoreAccess()
  {
    return coreAccess;
  }

  public void setCoreAccess( String coreAccess )
  {
    this.coreAccess = coreAccess;
  }

  public List<ProxyCoreAccessType> getCoreAccessList()
  {
    return UserProxyUtils.toCoreAccessPickList( coreAccess );
  }

  public boolean isAllowLeaderboard()
  {
    return allowLeaderboard;
  }

  public void setAllowLeaderboard( boolean allowLeaderboard )
  {
    this.allowLeaderboard = allowLeaderboard;
  }

}
