/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/proxy/ProxyModule.java,v $
 */

package com.biperf.core.domain.proxy;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PromotionType;

/**
 * ProxyModule.
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
public class ProxyModule extends BaseDomain
{
  private Proxy proxy;
  private PromotionType promotionType;
  private boolean allPromotions;
  private Set proxyModulePromotions = new LinkedHashSet();

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

    if ( object instanceof ProxyModule )
    {
      ProxyModule proxyModule = (ProxyModule)object;

      if ( proxyModule.getProxy() != null && !proxyModule.getProxy().equals( this.getProxy() ) )
      {
        equals = false;
      }
      if ( proxyModule.getPromotionType() != null && !proxyModule.getPromotionType().equals( this.getPromotionType() ) )
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
    result = this.getProxy() != null ? this.getProxy().hashCode() : 0;
    result = 29 * result + ( getPromotionType() != null ? getPromotionType().hashCode() : 0 );

    return result;
  }

  public boolean isAllPromotions()
  {
    return allPromotions;
  }

  public void setAllPromotions( boolean allPromotions )
  {
    this.allPromotions = allPromotions;
  }

  public PromotionType getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( PromotionType promotionType )
  {
    this.promotionType = promotionType;
  }

  public Proxy getProxy()
  {
    return proxy;
  }

  public void setProxy( Proxy proxy )
  {
    this.proxy = proxy;
  }

  public Set getProxyModulePromotions()
  {
    return proxyModulePromotions;
  }

  public void setProxyModulePromotions( Set proxyModulePromotions )
  {
    this.proxyModulePromotions = proxyModulePromotions;
  }

  public void addProxyModulePromotion( ProxyModulePromotion proxyModulePromotion )
  {
    proxyModulePromotion.setProxyModule( this );
    this.proxyModulePromotions.add( proxyModulePromotion );
  }

}
