/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/product/ProductCategoryDAO.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.product;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.product.ProductCategory;

/**
 * ProductCategoryDAO.
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
 * <td>sharma</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ProductCategoryDAO extends DAO
{

  public static final String BEAN_NAME = "productCategoryDAO";

  /**
   * Get a Product Category by ProductCategoryId
   * 
   * @param productCategoryId
   * @return ProductCategory
   */
  public ProductCategory getProductCategoryById( Long productCategoryId );

  /**
   * Get a list of all master (root) categories
   * 
   * @return List
   */
  public List getAllMasterCategories();

  /**
   * Get the Master Category from database by its name
   * 
   * @param categoryName
   * @return ProductCategory
   */
  public ProductCategory getMasterCategoryByName( String categoryName );

  /**
   * Saves a ProductCategory in database
   * 
   * @param productCategory
   * @return ProductCategory
   */
  public ProductCategory saveProductCategory( ProductCategory productCategory );

  /**
   * Deletes a ProductCategory from database.
   * 
   * @param productCategory
   */
  public void deleteProductCategory( ProductCategory productCategory );
}
