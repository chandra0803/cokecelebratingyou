/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/product/impl/ProductCategoryServiceImpl.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.product.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;

import com.biperf.core.dao.product.ProductCategoryDAO;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;

// import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;

/**
 * ProductCategoryServiceImpl.
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
public class ProductCategoryServiceImpl implements ProductCategoryService
{
  private ProductCategoryDAO productCategoryDAO;
  private PromotionPayoutDAO promotionPayoutDAO;
  private SystemVariableService systemVariableService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductCategoryService#saveProductCategory(ProductCategory
   *      productCategory, Long parentCategoryId)
   * @param productCategory
   * @param parentCategoryId
   * @return ProductCategory
   * @throws UniqueConstraintViolationException
   */
  public ProductCategory saveProductCategory( ProductCategory productCategory, Long parentCategoryId ) throws UniqueConstraintViolationException
  {

    // Business Rule: The product category name should be unique within parent (root/master)
    // category tree

    if ( parentCategoryId != null )
    {
      // product subcategory
      ProductCategory parentProductCategory = this.productCategoryDAO.getProductCategoryById( parentCategoryId );

      Hibernate.initialize( parentProductCategory.getSubcategories() );

      Set productSubcategoryList = parentProductCategory.getSubcategories();

      // check if any of the product category has the same name
      for ( Iterator iter = productSubcategoryList.iterator(); iter.hasNext(); )
      {
        ProductCategory subCategory = (ProductCategory)iter.next();
        if ( subCategory.getName().equalsIgnoreCase( productCategory.getName() ) )
        {
          if ( productCategory.getId() == null || productCategory.getId().compareTo( subCategory.getId() ) != 0 )
          {
            throw new UniqueConstraintViolationException();
          }
        }
      }

      // name is not used already, hence save it
      this.productCategoryDAO.saveProductCategory( productCategory );
    }
    else
    {
      // master category

      // check if there is any other master category with the same name
      ProductCategory category = this.productCategoryDAO.getMasterCategoryByName( productCategory.getName() );
      if ( category == null || category.getId().equals( productCategory.getId() ) )
      {
        // this master category can be added without a name conflict
        // name is not used already, hence save it
        this.productCategoryDAO.saveProductCategory( productCategory );
      }
      else
      {
        // name already exist
        throw new UniqueConstraintViolationException();
      }
    }
    return productCategory;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductCategoryService#deleteProductCategory(java.lang.Long)
   * @param productCategoryId
   * @throws ServiceErrorException
   */
  public void deleteProductCategory( Long productCategoryId ) throws ServiceErrorException
  {
    ProductCategory productCategory = this.getProductCategoryById( productCategoryId, null );

    List errors = getProductCategoryDeleteableError( productCategory );
    if ( errors != null && errors.isEmpty() )
    {
      this.productCategoryDAO.deleteProductCategory( productCategory );
    }
    else
    {
      throw new ServiceErrorException( errors );
    }
  }

  /**
   * Iterates over an array of ProductCategory ids, deleting the ProductCategory if possible.
   * 
   * @param deleteIds
   * @throws ServiceErrorException
   */
  public void deleteProductCategoryList( Long[] deleteIds ) throws ServiceErrorException
  {

    Long productCategoryId = null;

    List serviceErrors = new ArrayList();

    if ( deleteIds != null )
    {
      for ( int i = 0; i < deleteIds.length; i++ )
      {
        productCategoryId = deleteIds[i];

        // ProductCategory productCategory = this.getProductCategoryById( productCategoryId, null );

        try
        {
          deleteProductCategory( productCategoryId );
        }
        catch( ServiceErrorException e )
        {
          serviceErrors.add( e.getServiceErrors().get( 0 ) );
        }
      }

      if ( !serviceErrors.isEmpty() )
      {
        throw new ServiceErrorException( serviceErrors );
      }

    }

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductCategoryService#getProductCategoryById(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return ProductCategory
   */
  public ProductCategory getProductCategoryById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    ProductCategory productCategory = this.productCategoryDAO.getProductCategoryById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( productCategory );
    }
    return productCategory;
  }

  /**
   * This method checks if the product can be deleted. The product can only be deleted if there are
   * no promotions attached to it or its productCategory.
   * 
   * @return List of errors that cause it not to deleteble
   */
  private List getProductCategoryDeleteableError( ProductCategory productCategory )
  {
    List errors = new ArrayList();
    if ( productCategory.getSubcategories() != null && productCategory.getSubcategories().size() > 0 )
    {
      errors.add( new ServiceError( ServiceErrorMessageKeys.PRODUCT_CANNOT_DELETE_HAS_SUBCATEGORIES, productCategory.getName() ) );
    }
    else if ( productCategory.getProducts() != null && productCategory.getProducts().size() > 0 )
    {
      errors.add( new ServiceError( ServiceErrorMessageKeys.PRODUCT_CANNOT_DELETE_HAS_PRODUCTS, productCategory.getName() ) );
    }
    else
    {
      PropertySetItem pcSysVar = systemVariableService.getPropertyByName( SystemVariableService.INSTALL_PRODUCTCLAIMS );
      boolean isProductClaimInstalled = pcSysVar != null && pcSysVar.getBooleanVal();
      if ( isProductClaimInstalled && this.promotionPayoutDAO.isProductCategoryAssignedToPayout( productCategory.getId() ) )
      {
        errors.add( new ServiceError( ServiceErrorMessageKeys.PRODUCT_CANT_DEL_AS_PROMO_ASSOCIATION, productCategory.getName() ) );
      }
    }

    return errors;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductCategoryService#getAllMasterCategories()
   * @return List
   */
  public List getAllMasterCategories()
  {
    return this.productCategoryDAO.getAllMasterCategories();
  }

  public void setProductCategoryDAO( ProductCategoryDAO productCategoryDAO )
  {
    this.productCategoryDAO = productCategoryDAO;
  }

  public ProductCategoryDAO getProductCategoryDAO()
  {
    return productCategoryDAO;
  }

  public void setPromotionPayoutDAO( PromotionPayoutDAO promotionPayoutDAO )
  {
    this.promotionPayoutDAO = promotionPayoutDAO;
  }

  public PromotionPayoutDAO getPromotionPayoutDAO()
  {
    return promotionPayoutDAO;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

}
