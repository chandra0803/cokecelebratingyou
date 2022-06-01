/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/product/ProductCategoryService.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.product;

import java.util.List;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * ProductCategoryService.
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
 *
 */
public interface ProductCategoryService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "productCategoryService";

  /**
   * Saves the ProductCategory in database
   * 
   * @param productCategory
   * @param parentCategoryId
   * @return ProductCategory
   * @throws UniqueConstraintViolationException
   */
  public ProductCategory saveProductCategory( ProductCategory productCategory, Long parentCategoryId ) throws UniqueConstraintViolationException;

  /**
   * delete the product category from database
   * 
   * @param productCategoryId
   * @throws ServiceErrorException
   */
  public void deleteProductCategory( Long productCategoryId ) throws ServiceErrorException;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductCategoryService#deleteProductCategory(java.lang.Long)
   * @param deleteIds
   * @throws ServiceErrorException
   */
  public void deleteProductCategoryList( Long[] deleteIds ) throws ServiceErrorException;

  /**
   * get the product category by its id
   * 
   * @param id
   * @param associationRequestCollection
   * @return ProductCategory
   */
  public ProductCategory getProductCategoryById( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get list of all master (root) product categories
   * 
   * @return List
   */
  public List getAllMasterCategories();
}
