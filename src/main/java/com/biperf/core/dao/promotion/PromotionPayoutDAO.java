/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/promotion/PromotionPayoutDAO.java,v $
 */

package com.biperf.core.dao.promotion;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;

/**
 * PromotionPayoutDAO.
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
 * <td>leep</td>
 * <td>July 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface PromotionPayoutDAO extends DAO
{

  /** name of bean in factory * */
  public static final String BEAN_NAME = "promotionPayoutDAO";

  /**
   * Get a promotionPayoutGroup by Id.
   * 
   * @param promotionPayoutGroupId
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup getGroupById( Long promotionPayoutGroupId );

  /**
   * Save a PromotionPayoutGroup.
   * 
   * @param promotionPayoutGroup
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup saveGroup( PromotionPayoutGroup promotionPayoutGroup );

  /**
   * Delete a PromotionPayoutGroup.
   * 
   * @param promotionPayoutGroup
   */
  public void deleteGroup( PromotionPayoutGroup promotionPayoutGroup );

  /**
   * Saves the promotionPayout to the database.
   * 
   * @param promotionPayout
   * @return PromotionPayout
   */
  public PromotionPayout save( PromotionPayout promotionPayout );

  /**
   * Deletes the promotionPayout from the database.
   * 
   * @param promotionPayout
   */
  public void delete( PromotionPayout promotionPayout );

  /**
   * Get the promotionPayout from the database by the id.
   * 
   * @param id
   * @return PromotionPayout
   */
  public PromotionPayout getPromotionPayoutById( Long id );

  /**
   * Retrieves all the promotionPayouts from the database.
   * 
   * @return List a list of promotionPayouts
   */
  public List getAllPromotionPayouts();

  /**
   * Checks if a given product id assigned to any promotion payout
   * 
   * @param productId
   * @return boolean true or false
   */
  public boolean isProductAssignedToPayout( Long productId );

  /**
   * Checks if a given product category id assigned to any promotion payout
   * 
   * @param productCategoryId
   * @return boolean true or false
   */
  public boolean isProductCategoryAssignedToPayout( Long productCategoryId );

  /**
   * Get a set of promotionPayouts by the PromotionPayoutGroup promotionPayoutGroupId.
   * 
   * @param promotionPayoutGroupId
   * @return Set
   */
  public List getPromotionPayoutsByGroupId( Long promotionPayoutGroupId );
}
