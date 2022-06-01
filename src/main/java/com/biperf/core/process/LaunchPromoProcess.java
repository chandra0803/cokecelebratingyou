/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/process/LaunchPromoProcess.java,v $
 */

package com.biperf.core.process;

import com.biperf.core.service.promotion.PromotionService;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LaunchPromoProcess extends BaseProcessImpl
{
  public static final String PROCESS_NAME = "Expire Promotions Process";
  public static final String BEAN_NAME = "lanchPromotionsProcess";
  // public static final String PARAM_NAME = "mailingId";

  // // properties set from jobDataMap
  // private String mailingId;

  private PromotionService promotionService;

  // public void setMailingId( String mailingId )
  // {
  // this.mailingId = mailingId;
  // }

  public void onExecute()
  {

    // promotionService.process
    promotionService.launchPromotions();

  }

  /**
   * @param promotionService value for claimService property
   */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

}
