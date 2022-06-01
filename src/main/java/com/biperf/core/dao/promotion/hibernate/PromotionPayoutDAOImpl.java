/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/hibernate/PromotionPayoutDAOImpl.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * PromotionPayoutDAOImpl.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionPayoutDAOImpl extends BaseDAO implements PromotionPayoutDAO
{
  /**
   * Saves the hierarchy to the database.
   * 
   * @param promotionPayout
   * @return PromotionPayout
   */
  public PromotionPayout save( PromotionPayout promotionPayout )
  {
    return (PromotionPayout)HibernateUtil.saveOrUpdateOrShallowMerge( promotionPayout );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#deleteProduct(com.biperf.core.domain.product.Product)
   * @param promotionPayout
   */
  public void delete( PromotionPayout promotionPayout )
  {
    getSession().delete( promotionPayout );
  }

  /**
   * Get the hierarchy from the database by the id.
   * 
   * @param id
   * @return Product
   */
  public PromotionPayout getPromotionPayoutById( Long id )
  {
    PromotionPayout promotionPayout = (PromotionPayout)getSession().get( PromotionPayout.class, id );

    return promotionPayout;
  }

  /**
   * Retrieves all the products from the database.
   * 
   * @return List a list of products
   */
  public List getAllPromotionPayouts()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllPromotionPayouts" );

    return query.list();
  }

  /**
   * Checks if a given product id assigned to any promotion payout
   * 
   * @param productId
   * @return boolean true or false
   */
  public boolean isProductAssignedToPayout( Long productId )
  {
    Long count = (Long)getSession().getNamedQuery( "com.biperf.core.domain.activity.GetPromotionPayoutsByProductCount" ).setLong( "productId", productId.longValue() ).uniqueResult();

    return count.intValue() <= 0;
  }

  /**
   * Checks if a given product category id assigned to any promotion payout
   * 
   * @param productCategoryId
   * @return boolean true or false
   */
  public boolean isProductCategoryAssignedToPayout( Long productCategoryId )
  {
    Long count = (Long)getSession().getNamedQuery( "com.biperf.core.domain.activity.GetPromotionPayoutsByProductCategoryCount" ).setLong( "productCategoryId", productCategoryId.longValue() )
        .uniqueResult();

    return count.intValue() <= 0;
  }

  /**
   * Get a PromotionPayoutGroup by it's id. Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionPayoutDAO#getGroupById(java.lang.Long)
   * @param promotionPayoutGroupId
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup getGroupById( Long promotionPayoutGroupId )
  {
    return (PromotionPayoutGroup)getSession().get( PromotionPayoutGroup.class, promotionPayoutGroupId );
  }

  /**
   * Save a promotionPayoutGroup. Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionPayoutDAO#saveGroup(com.biperf.core.domain.promotion.PromotionPayoutGroup)
   * @param promotionPayoutGroup
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup saveGroup( PromotionPayoutGroup promotionPayoutGroup )
  {
    getSession().save( promotionPayoutGroup );
    return promotionPayoutGroup;
  }

  /**
   * Delete a PromotionPayoutGroup. Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionPayoutDAO#deleteGroup(com.biperf.core.domain.promotion.PromotionPayoutGroup)
   * @param promotionPayoutGroup
   */
  public void deleteGroup( PromotionPayoutGroup promotionPayoutGroup )
  {
    getSession().delete( promotionPayoutGroup );
  }

  /**
   * Get a set of PromotionPayouts by the PromotionPayoutGroup.id Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionPayoutDAO#getPromotionPayoutsByGroupId(java.lang.Long)
   * @param groupId
   * @return Set
   */
  public List getPromotionPayoutsByGroupId( Long groupId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.GetPromotionPayoutsByGroupId" );
    query.setLong( "groupId", groupId.longValue() );

    return query.list();
  }
}
