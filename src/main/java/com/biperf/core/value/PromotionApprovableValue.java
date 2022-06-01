/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/PromotionApprovableValue.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.Promotion;

/**
 * PromotionClaimsValue.
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
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovableValue implements Serializable
{
  Promotion promotion;
  List approvables = new ArrayList();
  private boolean activePromoAppValue = true;

  /**
   * @return claims
   */
  public List getApprovables()
  {
    return approvables;
  }

  /**
   * @param claims
   */
  public void setApprovables( List claims )
  {
    this.approvables = claims;
  }

  /**
   * @return promotion
   */
  public Promotion getPromotion()
  {
    return promotion;
  }

  /**
   * @param promotion
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean isActivePromoAppValue()
  {
    return activePromoAppValue;
  }

  public void setActivePromoAppValue( boolean activePromoAppValue )
  {
    this.activePromoAppValue = activePromoAppValue;
  }
}
