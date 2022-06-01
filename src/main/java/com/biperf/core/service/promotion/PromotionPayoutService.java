/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/PromotionPayoutService.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.List;

import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.service.SAO;

/**
 * PromotionPayoutService.
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
 * <td>Sathish</td>
 * <td>June 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PromotionPayoutService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "promotionPayoutService";

  /**
   * Get the promotionPayoutGroup by the id param.
   * 
   * @param promotionPayoutGroupId
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup getPromotionPayoutGroupById( Long promotionPayoutGroupId );

  /**
   * Delete the promotionPayoutGroup based on the id param.
   * 
   * @param promotionPayoutGroupId
   */
  public void deletePromotionPayoutGroup( Long promotionPayoutGroupId );

  /**
   * Get a set of promotionPayouts by the promotionPayoutGroupId param.
   * 
   * @param promotionPayoutGroupId
   * @return List
   */
  public List getPromotionPayoutsByPromotionPayoutGroupId( Long promotionPayoutGroupId );

  /**
   * Save the promotionPayoutGroup param.
   * 
   * @param promotionPayoutGroup
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup savePromotionPayoutGroup( PromotionPayoutGroup promotionPayoutGroup );

  /**
   * Save or update the promotionPayout to the database.
   * 
   * @param promotionId
   * @param productCategoryId
   * @param productId
   * @param promotionPayout
   * @return Product
   */
  public PromotionPayout save( Long promotionId, Long productCategoryId, Long productId, PromotionPayout promotionPayout );

  /**
   * Delete the promotionPayout identified by promotionPayoutId
   * 
   * @param promotionPayoutId
   */
  public void delete( Long promotionPayoutId );

  /**
   * Delete the promotionPayout identified by promotionPayoutId
   * 
   * @param promotionId
   * @param promotionPayoutGroupId
   * @param promotionPayoutId
   */
  public void delete( Long promotionId, Long promotionPayoutGroupId, Long promotionPayoutId );

  /**
   * Returns the promotionPayout from the database by the id param.
   * 
   * @param promotionPayoutId
   * @return PromotionPayout
   */
  public PromotionPayout getPromotionPayoutById( Long promotionPayoutId );

  /**
   * Get all promotionPayouts from the database.
   * 
   * @return List
   */
  public List getAllPromotionPayouts();

}
