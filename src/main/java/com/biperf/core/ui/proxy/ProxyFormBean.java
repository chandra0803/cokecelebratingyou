/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/proxy/ProxyFormBean.java,v $
 */

package com.biperf.core.ui.proxy;

import java.io.Serializable;

/**
 * ProxyFormBean.
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
 * <td>Nov 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyFormBean implements Serializable
{
  private String proxyModulePromoId;
  private String promotionId;
  private String promotionName;
  private boolean selected;

  public ProxyFormBean()
  {
    // empty constructor
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getProxyModulePromoId()
  {
    return proxyModulePromoId;
  }

  public void setProxyModulePromoId( String proxyModulePromoId )
  {
    this.proxyModulePromoId = proxyModulePromoId;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

}
