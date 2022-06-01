/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/product/impl/ProductServiceImpl.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.product.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductSearchCriteria;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;

/**
 * ProductServiceImpl.
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
public class ProductServiceImpl implements ProductService
{
  private ProductDAO productDAO;
  private ProductCategoryDAO productCategoryDAO;
  private PromotionPayoutDAO promotionPayoutDAO;
  private PromotionDAO promotionDAO;
  private SystemVariableService systemVariableService;

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
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
   * Set the ProductDAO through dependency injection (IoC).
   * 
   * @param productCategoryDAO
   */
  public void setProductCategoryDAO( ProductCategoryDAO productCategoryDAO )
  {
    this.productCategoryDAO = productCategoryDAO;
  }

  /**
   * Set the PromotionDAO through dependency injection (IoC).
   * 
   * @param promotionPayoutDAO
   */
  public void setPromotionPayoutDAO( PromotionPayoutDAO promotionPayoutDAO )
  {
    this.promotionPayoutDAO = promotionPayoutDAO;
  }

  /**
   * Get all products from the database.
   * 
   * @see com.biperf.core.service.product.ProductService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.productDAO.getAll();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#save(java.lang.Long,
   *      com.biperf.core.domain.product.Product)
   * @param productCategoryId
   * @param product
   * @return Product
   */
  public Product save( Long productCategoryId, Product product ) throws UniqueConstraintViolationException
  {

    // Get the productCategory and add it to the product.
    ProductCategory productCategory = product.getProductCategory();
    if ( productCategoryId != null )
    {
      productCategory = this.productCategoryDAO.getProductCategoryById( productCategoryId );
      product.setProductCategory( productCategory );
    }

    if ( !isUniqueProductName( productCategoryId, product.getId(), product.getName() ) )
    {
      // throw a UniqueConstraintViolationException if the name is not unique
      throw new UniqueConstraintViolationException();
    }

    return this.productDAO.save( product );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#checkForUniqueProductName(java.lang.Long,
   *      java.lang.Long, java.lang.String)
   * @param productCategoryId
   * @param productId
   * @param productName
   * @return boolean
   */
  public boolean isUniqueProductName( Long productCategoryId, Long productId, String productName )
  {
    boolean isUnique = true;
    ProductCategory productCategory = null;
    if ( productCategoryId != null )
    {
      productCategory = this.productCategoryDAO.getProductCategoryById( productCategoryId );
    }

    Set categoryIds = new HashSet();
    // Get all category ids (parent or subcategories)
    if ( productCategory != null )
    {
      categoryIds.add( productCategory.getId() );

      Set subcategories = null;

      // If there is a parent category, then get all subcategories
      if ( productCategory.getParentProductCategory() != null )
      {
        Long parentCategoryId = productCategory.getParentProductCategory().getId();
        categoryIds.add( parentCategoryId );
        ProductCategory parentCategory = this.productCategoryDAO.getProductCategoryById( parentCategoryId );
        subcategories = parentCategory.getSubcategories();
      }
      else
      {
        // get any subcategories
        subcategories = productCategory.getSubcategories();
      }

      // put all subcategories in the set of ids to search
      if ( subcategories != null )
      {
        for ( Iterator iter = subcategories.iterator(); iter.hasNext(); )
        {
          ProductCategory subcategory = (ProductCategory)iter.next();
          categoryIds.add( subcategory.getId() );
        }
      }
    }

    for ( Iterator iter = categoryIds.iterator(); iter.hasNext(); )
    {
      // Check for uniqueness of promotion_name throughout all categories and subcategories
      boolean isPromotionNameUnique = this.productDAO.isProductNameUnique( productName, productId, (Long)iter.next() );
      if ( !isPromotionNameUnique )
      {
        isUnique = false;
        break;
      }
    }
    return isUnique;
  }

  /**
   * Gets the product from the database by the id param.
   * 
   * @see com.biperf.core.service.product.ProductService#getProductById( java.lang.Long )
   * @param id
   * @return Product
   */
  public Product getProductById( Long id )
  {
    return this.productDAO.getProductById( id );
  }

  /**
   * Get all products in the specified product category from the database.
   * 
   * @param productCategoryId
   * @return List
   */
  public List getAll( Long productCategoryId )
  {
    ProductCategory productCategory = productCategoryDAO.getProductCategoryById( productCategoryId );

    return this.productDAO.getAll( productCategory );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#searchProducts(com.biperf.core.service.product.ProductSearchCriteria)
   * @param productSearchCriteria
   * @return List
   */
  public List searchProducts( ProductSearchCriteria productSearchCriteria )
  {
    return this.productDAO.searchProducts( productSearchCriteria );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#deleteProduct(java.lang.Long)
   * @param productId
   * @throws ServiceErrorException
   */
  public void deleteProduct( Long productId ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();

    // Check to see if the budgetMaster has any budgets.
    Product product = this.productDAO.getProductById( productId );
    if ( product != null )
    {
      // If we found a record in the database with the given productId
      // check to see if there are any budgets for it.
      if ( !isProductDeleteable( product ) )
      {
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.PRODUCT_CANNOT_DELETE );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }

      this.productDAO.deleteProduct( product );
    }

  }

  /**
   * This method checks if the product can be deleted. The product can only be deleted if there are
   * no promotions attached to it or its productCategory.
   * 
   * @return boolean
   */
  private boolean isProductDeleteable( Product product )
  {
    // check for promotions for this product and its productCategory and return true or false based
    // on that check.
    boolean isDeletable = true;

    PropertySetItem pcSysVar = systemVariableService.getPropertyByName( SystemVariableService.INSTALL_PRODUCTCLAIMS );
    boolean isProductClaimInstalled = pcSysVar != null && pcSysVar.getBooleanVal();
    if ( isProductClaimInstalled )
    {
      isDeletable = this.promotionPayoutDAO.isProductAssignedToPayout( product.getId() );
      if ( isDeletable )
      {
        isDeletable = this.promotionPayoutDAO.isProductCategoryAssignedToPayout( product.getProductCategory().getId() );
        if ( isDeletable && product.getProductCategory().getParentProductCategory() != null )
        {
          isDeletable = this.promotionPayoutDAO.isProductCategoryAssignedToPayout( product.getProductCategory().getParentProductCategory().getId() );
        }
      }
    }

    return isDeletable;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#getProductsByPromotion(java.lang.Long)
   * @param promotionId
   * @return List of products
   */
  public List getProductsByPromotion( Long promotionId )
  {
    return this.productDAO.getProductsByPromotion( promotionId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#getProductsByPromotionAndDateRange(java.lang.Long)
   * @param promotionId
   * @return List of products
   */
  public List getProductsByPromotionAndDateRange( Long promotionId )
  {
    // Get the Products valid as of today
    return getProductsByPromotionAndDateRange( promotionId, new Date() );
  }

  public List<Product> getProductsByPromotionAndDateRange( Long promotionId, String timeZoneId )
  {
    // Get the Products valid as of today
    return getProductsByPromotionAndDateRange( promotionId, DateUtils.applyTimeZone( new Date(), timeZoneId ) );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#getProductsByPromotionAndDateRange(java.lang.Long, java.util.Date)
   * @param promotionId
   * @param onThisDate
   * @return List of products
   */
  public List<Product> getProductsByPromotionAndDateRange( Long promotionId, Date onThisDate )
  {
    List products = this.productDAO.getProductsByPromotionAndDateRange( promotionId, onThisDate );
    // remove any products from search that came from categories where specific product date is not
    // yet in range. This comes
    // from use case: ' When both a product and the category it belongs to are added to the
    // promotion, then the product dates overrides the category dates.'
    // This would have been difficult to include in the query, so remove from query results now.
    ProductClaimPromotion promotion = (ProductClaimPromotion)promotionDAO.getPromotionById( promotionId );
    List promotionPayoutGroups = promotion.getPromotionPayoutGroups();
    for ( Iterator iter = promotionPayoutGroups.iterator(); iter.hasNext(); )
    {
      PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iter.next();
      List promotionPayouts = promotionPayoutGroup.getPromotionPayouts();
      for ( Iterator iterator = promotionPayouts.iterator(); iterator.hasNext(); )
      {
        PromotionPayout promotionPayout = (PromotionPayout)iterator.next();
        Product promotionProduct = promotionPayout.getProduct();
        if ( promotionProduct != null && !DateUtils.isDateBetween( onThisDate, promotionPayout.getProductOrCategoryStartDate(), promotionPayout.getProductOrCategoryEndDate() ) )
        {
          // specific product not currently time valid, so remove from query results
          products.remove( promotionProduct );
        }

      }

    }
    return products;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductService#getProductByNameAndCategoryId(java.lang.String,
   *      java.lang.Long)
   * @param productName
   * @param categoryId
   * @return Product
   */
  public Product getProductByNameAndCategoryId( String productName, Long categoryId )
  {
    return this.productDAO.getProductByNameAndCategoryId( productName, categoryId );
  }

  /**
   * @param promotionDAO value for promotionDAO property
   */
  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  /**
   * Gets the product with associations from the database by the id param.
   * 
   * @see com.biperf.core.service.product.ProductService#getProductById( java.lang.Long )
   * @param id
   * @param associationRequestCollection
   * @return Product
   */
  public Product getProductById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Product product = this.getProductById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( product );
    }
    return product;
  }
}
