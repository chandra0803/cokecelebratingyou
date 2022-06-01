/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/impl/PromotionPayoutServiceImpl.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.promotion.impl;

import java.util.List;

import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.service.promotion.PromotionPayoutService;

/**
 * PromotionPayoutServiceImpl.
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
 * <td>Leep</td>
 * <td>June 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutServiceImpl implements PromotionPayoutService
{
  private PromotionPayoutDAO promotionPayoutDAO = null;
  private PromotionDAO promotionDAO = null;
  private ProductCategoryDAO productCategoryDAO = null;
  private ProductDAO productDAO = null;

  /**
   * Set the ProductDAO through dependency injection (IoC).
   * 
   * @param promotionPayoutDAO
   */
  public void setPromotionPayoutDAO( PromotionPayoutDAO promotionPayoutDAO )
  {
    this.promotionPayoutDAO = promotionPayoutDAO;
  }

  /**
   * Set the ProductDAO through dependency injection (IoC).
   * 
   * @param promotionDAO
   */
  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  /**
   * Set the ProductDAO through dependency injection (IoC).
   * 
   * @param productCategoryDAO
   */
  public void setProductCategoryDAO( ProductCategoryDAO productCategoryDAO )
  {
    this.productCategoryDAO = productCategoryDAO;
  }

  /**
   * Set the ProductDAO through dependency injection (IoC).
   * 
   * @param productDAO
   */
  public void setProductDAO( ProductDAO productDAO )
  {
    this.productDAO = productDAO;
  }

  /**
   * Save or update the promotionPayout to the database.
   * 
   * @param promotionId
   * @param productCategoryId
   * @param productId
   * @param promotionPayout
   * @return PromotionPayout
   */
  public PromotionPayout save( Long promotionId, Long productCategoryId, Long productId, PromotionPayout promotionPayout )
  {

    // Get the productCategory and add it to the promotionPayout.
    if ( productCategoryId != null )
    {
      ProductCategory productCategory = this.productCategoryDAO.getProductCategoryById( productCategoryId );
      promotionPayout.setProductCategory( productCategory );
    }

    // Get the product and add it to the promotionPayout.
    if ( productId != null )
    {
      Product product = this.productDAO.getProductById( productId );
      promotionPayout.setProduct( product );
    }

    return this.promotionPayoutDAO.save( promotionPayout );
  }

  /**
   * Delete the promotionPayout identified by promotionPayoutId Overridden from
   * 
   * @see com.biperf.core.service.promotion.PromotionPayoutService#delete(java.lang.Long,
   *      java.lang.Long, java.lang.Long)
   * @param promotionId
   * @param promotionPayoutGroupId
   * @param promotionPayoutId
   */
  public void delete( Long promotionId, Long promotionPayoutGroupId, Long promotionPayoutId )
  {
    ProductClaimPromotion promotion = (ProductClaimPromotion)promotionDAO.getPromotionById( promotionId );

    PromotionPayoutGroup ppGroup = (PromotionPayoutGroup)BaseDomain.getBusinessObjectWithId( promotion.getPromotionPayoutGroups(), promotionPayoutGroupId );

    PromotionPayout payout = (PromotionPayout)BaseDomain.getBusinessObjectWithId( ppGroup.getPromotionPayouts(), promotionPayoutId );

    ppGroup.getPromotionPayouts().remove( payout );

    if ( ppGroup.getPromotionPayouts().isEmpty() )
    {
      promotion.getPromotionPayoutGroups().remove( ppGroup );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#deleteProduct(java.lang.Long)
   * @param promotionPayoutId
   */
  public void delete( Long promotionPayoutId )
  {
    PromotionPayout promotionPayout = this.promotionPayoutDAO.getPromotionPayoutById( promotionPayoutId );

    if ( promotionPayout != null )
    {
      this.promotionPayoutDAO.delete( promotionPayout );
    }
  }

  /**
   * Gets the PromotionPayout from the database by the id param.
   * 
   * @see com.biperf.core.service.product.ProductService#getProductById( java.lang.Long )
   * @param promotionPayoutId
   * @return PromotionPayout
   */
  public PromotionPayout getPromotionPayoutById( Long promotionPayoutId )
  {
    return this.promotionPayoutDAO.getPromotionPayoutById( promotionPayoutId );
  }

  /**
   * Get all promotionPayouts from the database.
   * 
   * @return List
   */
  public List getAllPromotionPayouts()
  {
    return this.promotionPayoutDAO.getAllPromotionPayouts();
  }

  /**
   * Get the promotionPayoutGroup by the id param.
   * 
   * @param promotionPayoutGroupId
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup getPromotionPayoutGroupById( Long promotionPayoutGroupId )
  {
    return this.promotionPayoutDAO.getGroupById( promotionPayoutGroupId );
  }

  /**
   * Delete the promotionPayoutGroup based on the id param.
   * 
   * @param promotionPayoutGroupId
   */
  public void deletePromotionPayoutGroup( Long promotionPayoutGroupId )
  {
    PromotionPayoutGroup promotionPayoutGroup = this.promotionPayoutDAO.getGroupById( promotionPayoutGroupId );
    this.promotionPayoutDAO.deleteGroup( promotionPayoutGroup );
  }

  /**
   * Get a set of promotionPayouts by the promotionPayoutGroupId param.
   * 
   * @param promotionPayoutGroupId
   * @return List
   */
  public List getPromotionPayoutsByPromotionPayoutGroupId( Long promotionPayoutGroupId )
  {
    return this.promotionPayoutDAO.getPromotionPayoutsByGroupId( promotionPayoutGroupId );
  }

  /**
   * Save the promotionPayoutGroup param.
   * 
   * @param promotionPayoutGroup
   * @return PromotionPayoutGroup
   */
  public PromotionPayoutGroup savePromotionPayoutGroup( PromotionPayoutGroup promotionPayoutGroup )
  {
    this.promotionPayoutDAO.saveGroup( promotionPayoutGroup );
    return promotionPayoutGroup;
  }

}
