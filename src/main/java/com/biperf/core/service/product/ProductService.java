/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/product/ProductService.java,v $
 */

package com.biperf.core.service.product;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.product.Product;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * ProductService.
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
public interface ProductService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "productService";

  /**
   * Get all products from the database.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Get all products in the specified product category from the database.
   * 
   * @param productCategoryId
   * @return List
   */
  public List getAll( Long productCategoryId );

  /**
   * Save or update the product to the database.
   * 
   * @param productCategoryId
   * @param product
   * @return Product
   * @throws UniqueConstraintViolationException
   */
  public Product save( Long productCategoryId, Product product ) throws UniqueConstraintViolationException;

  /**
   * Gets the product from the database by the id param.
   * 
   * @param id
   * @return Product
   */
  public Product getProductById( Long id );

  /**
   * Gets the product from the database by the name and id params.
   * 
   * @param productName
   * @param categoryId
   * @return Product
   */
  public Product getProductByNameAndCategoryId( String productName, Long categoryId );

  /**
   * Search products based on ProductSearchCriteria
   * 
   * @param productSearchCriteria
   * @return List
   */
  public List searchProducts( ProductSearchCriteria productSearchCriteria );

  /**
   * Delete the product identified by productId
   * 
   * @param productId
   * @throws ServiceErrorException
   */
  public void deleteProduct( Long productId ) throws ServiceErrorException;

  /**
   * get list of products assigned to a promotion.
   * 
   * @param promotionId
   * @return List
   */
  public List getProductsByPromotion( Long promotionId );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#getProductsByPromotionAndDateRange(java.lang.Long)
   * @param promotionId
   * @return List of products
   */
  public List getProductsByPromotionAndDateRange( Long promotionId );

  public List<Product> getProductsByPromotionAndDateRange( Long promotionId, String TimezoneId );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#getProductsByPromotionAndDateRange(java.lang.Long, java.util.Date)
   * @param promotionId
   * @param submissionDate
   * @return List of products
   */
  public List<Product> getProductsByPromotionAndDateRange( Long promotionId, Date submissionDate );

  /**
   * See if the database already contains a product with the given name
   * 
   * @param productCategoryId
   * @param productId
   * @param productName
   * @return boolean
   */
  public boolean isUniqueProductName( Long productCategoryId, Long productId, String productName );

  /**
   * Gets the product with associations from the database by the id param.
   * 
   * @param id
   * @param associationRequestCollection
   * @return Product
   */
  public Product getProductById( Long id, AssociationRequestCollection associationRequestCollection );
}
