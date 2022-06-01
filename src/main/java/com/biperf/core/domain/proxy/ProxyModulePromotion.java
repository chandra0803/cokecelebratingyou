/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/proxy/ProxyModulePromotion.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.proxy;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;

/**
 * ProxyModulePromotion.
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
public class ProxyModulePromotion extends BaseDomain
{
  private ProxyModule proxyModule;
  private Promotion promotion;

  // Budget Allocator option
  private boolean showBudgetAllocator = false;
  private String nodeId;

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( nodeId == null ? 0 : nodeId.hashCode() );
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    result = prime * result + ( proxyModule == null ? 0 : proxyModule.hashCode() );
    result = prime * result + ( showBudgetAllocator ? 1231 : 1237 );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    ProxyModulePromotion other = (ProxyModulePromotion)obj;
    if ( nodeId == null )
    {
      if ( other.nodeId != null )
      {
        return false;
      }
    }
    else if ( !nodeId.equals( other.nodeId ) )
    {
      return false;
    }
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( promotion != null )
    {
      if ( promotion.getId() != null && other.getPromotion() != null && promotion.getId().longValue() != other.getPromotion().getId().longValue() )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }
    if ( proxyModule == null )
    {
      if ( other.proxyModule != null )
      {
        return false;
      }
    }
    else if ( !proxyModule.equals( other.proxyModule ) )
    {
      return false;
    }
    if ( showBudgetAllocator != other.showBudgetAllocator )
    {
      return false;
    }
    return true;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public ProxyModule getProxyModule()
  {
    return proxyModule;
  }

  public void setProxyModule( ProxyModule proxyModule )
  {
    this.proxyModule = proxyModule;
  }

  public void setShowBudgetAllocator( boolean showBudgetAllocator )
  {
    this.showBudgetAllocator = showBudgetAllocator;
  }

  public boolean isShowBudgetAllocator()
  {
    return showBudgetAllocator;
  }

  public void setNodeId( String nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeId()
  {
    return nodeId;
  }
}
