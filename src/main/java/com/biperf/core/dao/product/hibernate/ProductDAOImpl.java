/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/product/hibernate/ProductDAOImpl.java,v $
 */

package com.biperf.core.dao.product.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.product.ProductSearchCriteria;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ProductDAOImpl.
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
public class ProductDAOImpl extends BaseDAO implements ProductDAO
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#deleteProduct(com.biperf.core.domain.product.Product)
   * @param product
   */
  public void deleteProduct( Product product )
  {
    getSession().delete( product );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#searchProducts(com.biperf.core.service.product.ProductSearchCriteria)
   * @param productSearchCriteria
   * @return List
   */
  public List searchProducts( ProductSearchCriteria productSearchCriteria )
  {
    boolean runQuery = false;
    Criteria criteria = getSession().createCriteria( Product.class );

    String productName = productSearchCriteria.getProductName();
    if ( null != productName && !"".equals( productName ) )
    {
      criteria.add( Restrictions.ilike( "name", productName, MatchMode.ANYWHERE ) );
      runQuery = true;
    }

    String itemNumber = productSearchCriteria.getItemNumber();
    if ( null != itemNumber && !"".equals( itemNumber ) )
    {
      criteria.add( Restrictions.ilike( "code", itemNumber, MatchMode.ANYWHERE ) );
      runQuery = true;
    }

    Long categoryId = productSearchCriteria.getCategoryId();
    if ( null != categoryId )
    {
      if ( categoryId.compareTo( ProductCategory.ALL_PRODUCT_CATEGORIES ) != 0 )
      {
        Long subCategoryId = productSearchCriteria.getSubCategoryId();
        if ( subCategoryId.compareTo( ProductCategory.ALL_PRODUCT_CATEGORIES ) == 0 )
        {
          // all subcategories of the selected master category, to be considered for search
          criteria.createCriteria( "productCategory" ).add( Restrictions.or( Restrictions.eq( "id", categoryId ), Restrictions.eq( "parentProductCategory.id", categoryId ) ) );
        }
        else
        {
          // specific subcategory for the search
          criteria.add( Restrictions.eq( "productCategory.id", subCategoryId ) );
        }
      }

      runQuery = true;
    }

    List result = null;
    if ( runQuery )
    {
      criteria.addOrder( Order.asc( "name" ) );
      Set resultSet = new LinkedHashSet( criteria.list() );
      result = new ArrayList( resultSet );
    }
    else
    {
      result = new ArrayList(); // empty list
    }

    return result;
  }

  /**
   * Get the hierarchy from the database by the id.
   * 
   * @param id
   * @return Product
   */
  public Product getProductById( Long id )
  {
    Product product = (Product)getSession().get( Product.class, id );

    return product;
  }

  /**
   * Saves the hierarchy to the database.
   * 
   * @param product
   * @return Product
   */
  public Product save( Product product )
  {
    return (Product)HibernateUtil.saveOrUpdateOrShallowMerge( product );
  }

  /**
   * Retrieves all the products from the database.
   * 
   * @return List a list of products
   */
  public List getAll()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.AllProducts" );

    return query.list();
  }

  /**
   * Retrieves all products in a product category from the database.
   * 
   * @param productCategory
   * @return List a list of products
   */
  public List getAll( ProductCategory productCategory )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.CategoryProducts" );
    query.setParameter( "productCategory", productCategory );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#getProductsByPromotion(java.lang.Long)
   * @param promotionId
   * @return List of products
   */
  public List getProductsByPromotion( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.GetProductsByPromotion" );
    query.setLong( "promotionId", promotionId.longValue() );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#getProductsByPromotionAndDateRange(java.lang.Long)
   * @param promotionId
   * @return List of products
   */
  public List getProductsByPromotionAndDateRange( Long promotionId )
  {
    return getProductsByPromotionAndDateRange( promotionId, new Date() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#getProductsByPromotionAndDateRange(java.lang.Long, java.util.Date)
   * @param promotionId
   * @param onThisDate
   * @return List of products
   */
  public List getProductsByPromotionAndDateRange( Long promotionId, Date onThisDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.GetProductsByPromotionAndDateRange" );
    query.setLong( "promotionId", promotionId.longValue() );
    query.setDate( "onThisDate", onThisDate );
    query.setParameter( "currentDate", UserManager.getCurrentDateWithTimeZoneID() );
    // query.setParameter( "endDate", endDate );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductDAO#isProductNameUnique(java.lang.String,java.lang.Long,java.lang.Long)
   * @param productName
   * @param productId
   * @param categoryId
   * @return boolean
   */
  public boolean isProductNameUnique( String productName, Long productId, Long categoryId )
  {
    boolean isUnique = true;

    if ( productId == null )
    {
      productId = new Long( 0 );
    }

    if ( categoryId == null )
    {
      categoryId = new Long( 0 );
    }

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.ProductByNameCount" );

    query.setParameter( "productName", productName.toLowerCase() );
    query.setParameter( "productId", productId );
    query.setParameter( "categoryId", categoryId );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;

    return isUnique;
  }

  public Product getProductByNameAndCategoryId( String productName, Long categoryId )
  {
    if ( categoryId == null )
    {
      categoryId = new Long( 0 );
    }

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.ProductByNameAndCategory" );

    query.setParameter( "productName", productName.toLowerCase() );
    query.setParameter( "categoryId", categoryId );

    return (Product)query.uniqueResult();
  }

}
