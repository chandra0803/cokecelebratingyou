/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/product/hibernate/ProductCategoryDAOImpl.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.product.hibernate;

import java.util.List;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ProductCategoryDAOImpl.
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
public class ProductCategoryDAOImpl extends BaseDAO implements ProductCategoryDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductCategoryDAO#getMasterCategoryByName(java.lang.String)
   * @param categoryName
   * @return product category
   */
  public ProductCategory getMasterCategoryByName( String categoryName )
  {
    return (ProductCategory)getSession().getNamedQuery( "com.biperf.core.domain.product.MasterCategoryByName" ).setString( "productCategoryName", categoryName.toLowerCase() ).uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductCategoryDAO#deleteProductCategory(com.biperf.core.domain.product.ProductCategory)
   * @param productCategory
   */
  public void deleteProductCategory( ProductCategory productCategory )
  {
    getSession().delete( productCategory );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductCategoryDAO#getAllMasterCategories()
   * @return List
   */
  public List getAllMasterCategories()
  {

    return getSession().getNamedQuery( "com.biperf.core.domain.product.AllMasterCategoryList" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductCategoryDAO#getProductCategoryById(java.lang.Long)
   * @param productCategoryId
   * @return ProductCategory
   */
  public ProductCategory getProductCategoryById( Long productCategoryId )
  {
    return (ProductCategory)getSession().get( ProductCategory.class, productCategoryId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductCategoryDAO#saveProductCategory(com.biperf.core.domain.product.ProductCategory)
   * @param productCategory
   * @return ProductCategory
   */
  public ProductCategory saveProductCategory( ProductCategory productCategory )
  {
    return (ProductCategory)HibernateUtil.saveOrUpdateOrShallowMerge( productCategory );
  }
}
