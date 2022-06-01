/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductUtils.java,v $
 */

package com.biperf.core.ui.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.service.product.ProductSubcategoryAssociation;
import com.biperf.core.utils.BeanLocator;

/**
 * ProductUtils.
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
 * <td>sedey</td>
 * <td>Aug 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductUtils
{
  /**
   * Builds a list of ProductCategories that includes the "all" option at the top
   * 
   * @param request
   * @param includeAllOption
   * @throws Exception
   */
  public static void setProductCategoryList( HttpServletRequest request, boolean includeAllOption ) throws Exception
  {
    // Get the list of Master Categories
    List categoryList = getProductCategoryService().getAllMasterCategories();

    // Add a default "All" Category to the top
    if ( includeAllOption )
    {
      categoryList.add( 0, getDefaultCategory() );
    }

    // Place the list in the request for display on the page
    request.setAttribute( "categoryList", categoryList );
  }

  /**
   * Builds a list of ProductSubcategories that includes the "all" option at the top
   * 
   * @param request
   * @param productCategoryId
   * @param includeAllOption
   * @throws Exception
   */
  @SuppressWarnings( "unchecked" )
  public static void setProductSubcategoryList( HttpServletRequest request, Long productCategoryId, boolean includeAllOption ) throws Exception
  {
    List<ProductCategory> subCategoryList = new ArrayList<ProductCategory>();

    // If the productCategoryId is not null, get the subcategory list for that
    // productCategoryId
    if ( productCategoryId != null && productCategoryId.longValue() > 0 )
    {
      AssociationRequestCollection subcategoryAssociationRequestCollection = new AssociationRequestCollection();
      subcategoryAssociationRequestCollection.add( new ProductSubcategoryAssociation() );

      ProductCategory prodCategory = getProductCategoryService().getProductCategoryById( productCategoryId, subcategoryAssociationRequestCollection );

      subCategoryList = new ArrayList<ProductCategory>( prodCategory.getSubcategories() );
      Collections.sort( subCategoryList, new Comparator<ProductCategory>()
      {

        @Override
        public int compare( ProductCategory pc1, ProductCategory pc2 )
        {
          return pc1.getName().compareTo( pc2.getName() );
        }
      } );
    }

    // Add a default "All" Sub Category to the top
    if ( includeAllOption )
    {
      subCategoryList.add( 0, getDefaultCategory() );
    }

    // Place the list in the request for display on the page
    request.setAttribute( "subCategoryList", subCategoryList );
  }

  /**
   * Builds a ProductCategory object to hold the "all" value
   * 
   * @return defCat
   */
  private static ProductCategory getDefaultCategory()
  {
    ProductCategory defCat = new ProductCategory();
    defCat.setId( ProductCategory.ALL_PRODUCT_CATEGORIES );
    defCat.setName( "All" );

    return defCat;
  }

  /**
   * Gets a ProductCategoryService
   * 
   * @return ProductCategoryService
   * @throws Exception
   */
  private static ProductCategoryService getProductCategoryService() throws Exception
  {
    return (ProductCategoryService)BeanLocator.getBean( ProductCategoryService.BEAN_NAME );
  }
}
