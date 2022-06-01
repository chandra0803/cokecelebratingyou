/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/product/ProductDAO.java,v $
 */

package com.biperf.core.dao.product;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.product.ProductSearchCriteria;

/**
 * ProductDAO.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ProductDAO extends DAO
{

  public static final String BEAN_NAME = "productDAO";

  /**
   * Get the Product from the database by the id.
   * 
   * @param id
   * @return Product
   */
  public Product getProductById( Long id );

  /**
   * Get the Product from the database by name and the categoryId.
   * 
   * @param productName
   * @param categoryId
   * @return Product
   */
  public Product getProductByNameAndCategoryId( String productName, Long categoryId );

  /**
   * Saves the hierarchy to the database.
   * 
   * @param product
   * @return Product
   */
  public Product save( Product product );

  /**
   * Retrieves all the products from the database.
   * 
   * @return List a list of products
   */
  public List getAll();

  /**
   * Retrieves all products in a product category from the database.
   * 
   * @param productCategory
   * @return List a list of products
   */
  public List getAll( ProductCategory productCategory );

  /**
   * Search products based on ProductSearchCriteria
   * 
   * @param productSearchCriteria
   * @return List
   */
  public List searchProducts( ProductSearchCriteria productSearchCriteria );

  /**
   * Delete a product from database
   * 
   * @param product
   */
  public void deleteProduct( Product product );

  /**
   * Load a list of products assigned to a promotion.
   * 
   * @param promotionId
   * @return List
   */
  public List getProductsByPromotion( Long promotionId );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#getProductsByPromotionAndDateRange(java.lang.Long)
   * @param promotionId
   * @return List of products
   */
  public List getProductsByPromotionAndDateRange( Long promotionId );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#getProductsByPromotionAndDateRange(java.lang.Long, java.util.Date)
   * @param promotionId
   * @param onThisDate
   * @return List of products
   */
  public List getProductsByPromotionAndDateRange( Long promotionId, Date onThisDate );

  /**
   * Check the database to make sure the product_name is unique for non-deleted products within a
   * specified category
   * 
   * @param productName
   * @param productId
   * @param categoryId
   * @return boolean
   */
  public boolean isProductNameUnique( String productName, Long productId, Long categoryId );

}
