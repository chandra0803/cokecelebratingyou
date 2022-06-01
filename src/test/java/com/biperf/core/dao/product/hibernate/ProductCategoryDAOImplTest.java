/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/product/hibernate/ProductCategoryDAOImplTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.product.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * ProductCategoryDAOImplTest.
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
public class ProductCategoryDAOImplTest extends BaseDAOTest
{

  /**
   * Get the ProductCategoryDAO through bean look-up.
   * 
   * @return ProductCategoryDAO
   */
  protected ProductCategoryDAO getProductCategoryDAO()
  {
    return (ProductCategoryDAO)ApplicationContextFactory.getApplicationContext().getBean( "productCategoryDAO" );
  }

  /**
   * Saves a ProductCategory in database
   */
  public void testSaveProductCategory()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );
    getProductCategoryDAO().saveProductCategory( productCategory );
    HibernateSessionManager.getSession().flush();

    assertNotNull( productCategory.getId() );
  }

  /**
   * Get Product Category by id
   */
  public void testGetProductCategoryById()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );
    getProductCategoryDAO().saveProductCategory( productCategory );
    HibernateSessionManager.getSession().flush();

    ProductCategory retrievedProductCategory = getProductCategoryDAO().getProductCategoryById( productCategory.getId() );
    assertTrue( retrievedProductCategory.getId().compareTo( productCategory.getId() ) == 0 );
  }

  /**
   * Get a list of all master (root) categories
   */
  public void testGetAllMasterCategories()
  {
    ProductCategory productCategory1 = getRootProductCategoryDomainObject( "1" );
    getProductCategoryDAO().saveProductCategory( productCategory1 );
    HibernateSessionManager.getSession().flush();

    ProductCategory productCategory2 = getRootProductCategoryDomainObject( "2" );
    getProductCategoryDAO().saveProductCategory( productCategory2 );
    HibernateSessionManager.getSession().flush();

    ProductCategory productCategory3 = getRootProductCategoryDomainObject( "3" );
    getProductCategoryDAO().saveProductCategory( productCategory3 );
    HibernateSessionManager.getSession().flush();

    List retrievedList = getProductCategoryDAO().getAllMasterCategories();
    Set retrievedSet = new HashSet( retrievedList );

    assertTrue( retrievedSet.contains( productCategory1 ) );
    assertTrue( retrievedSet.contains( productCategory2 ) );
    assertTrue( retrievedSet.contains( productCategory3 ) );
  }

  /**
   * Get the Master Category from database by its name
   */
  public void testGetMasterCategoryByName()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );
    getProductCategoryDAO().saveProductCategory( productCategory );
    HibernateSessionManager.getSession().flush();

    ProductCategory retrievedCategory = getProductCategoryDAO().getMasterCategoryByName( productCategory.getName() );
    assertTrue( retrievedCategory != null );
  }

  /**
   * Get a tree of product categories startin at its root category
   */
  public void testGetProductCategoryWithChildren()
  {
    ProductCategory rootCategory = getRootProductCategoryDomainObject( "1" );
    getProductCategoryDAO().saveProductCategory( rootCategory );
    HibernateSessionManager.getSession().flush();

    List subCategoryList = getChildCategoryList( rootCategory );
    for ( Iterator iter = subCategoryList.iterator(); iter.hasNext(); )
    {
      ProductCategory childCategory = (ProductCategory)iter.next();
      getProductCategoryDAO().saveProductCategory( childCategory );
      HibernateSessionManager.getSession().flush();
    }

    flushAndClearSession();
    ProductCategory prodCat = getProductCategoryDAO().getMasterCategoryByName( rootCategory.getName() );
    assertEquals( subCategoryList.size(), prodCat.getSubcategories().size() );
  }

  /**
   * Deletes a ProductCategory from database.
   */
  public void testDeleteProductCategory()
  {
    ProductCategory productCategory = getRootProductCategoryDomainObject( "1" );
    getProductCategoryDAO().saveProductCategory( productCategory );
    HibernateSessionManager.getSession().flush();

    getProductCategoryDAO().deleteProductCategory( productCategory );
    HibernateSessionManager.getSession().flush();

    ProductCategory retrievedProductCategory = getProductCategoryDAO().getProductCategoryById( productCategory.getId() );
    assertTrue( retrievedProductCategory == null );
  }

  private ProductCategory getRootProductCategoryDomainObject( String suffix )
  {
    ProductCategory productCategory = new ProductCategory();
    productCategory.setName( "testRootCategory" + suffix );
    productCategory.setDescription( "testRootCatgoryDescription" + suffix );
    productCategory.setParentProductCategory( null );
    return productCategory;
  }

  private ProductCategory getChildProductCategoryDomainObject( String suffix, ProductCategory parentProductCategory )
  {
    ProductCategory productCategory = new ProductCategory();
    productCategory.setName( "testCategory" + suffix );
    productCategory.setDescription( "testCatgoryDescription" + suffix );
    productCategory.setParentProductCategory( parentProductCategory );
    return productCategory;
  }

  private List getChildCategoryList( ProductCategory parentProductCategory )
  {
    List childList = new ArrayList();

    ProductCategory childCategory = getChildProductCategoryDomainObject( "1", parentProductCategory );
    childList.add( childCategory );
    childCategory.setParentProductCategory( parentProductCategory );

    childCategory = getChildProductCategoryDomainObject( "2", parentProductCategory );
    childList.add( childCategory );
    childCategory.setParentProductCategory( parentProductCategory );

    childCategory = getChildProductCategoryDomainObject( "3", parentProductCategory );
    childList.add( childCategory );
    childCategory.setParentProductCategory( parentProductCategory );

    return childList;
  }

  /**
   * Builds a static representation of a productCategory.
   * 
   * @param suffix
   * @return ProductCategory
   */
  public static ProductCategory buildProductCategory( String suffix )
  {
    ProductCategory productCategory = new ProductCategory();

    productCategory.setName( "testProductCategory" + suffix );
    productCategory.setDescription( "testProductCategoryDescription" + suffix );
    productCategory.setParentProductCategory( null );

    return productCategory;
  }

}
