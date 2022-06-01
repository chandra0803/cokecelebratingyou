/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductCategorySearchController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * ProductCategorySearchController.
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
 * <td>June 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCategorySearchController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long productCategoryId = null;

    if ( RequestUtils.containsAttribute( request, "productCategoryId" ) )
    {
      productCategoryId = RequestUtils.getOptionalAttributeLong( request, "productCategoryId" );
    }
    else
    {
      ProductCategorySearchForm searchForm = (ProductCategorySearchForm)request.getAttribute( "productCategorySearchForm" );
      productCategoryId = searchForm.getCategoryId();
    }
    if ( productCategoryId != null )
    {
      ProductUtils.setProductSubcategoryList( request, productCategoryId, true );
    }
    else
    {
      List subCategoryList = new ArrayList();
      ProductCategory defCat = new ProductCategory();
      defCat.setId( ProductCategory.ALL_PRODUCT_CATEGORIES );
      defCat.setName( "All" );
      subCategoryList.add( 0, defCat );
      request.setAttribute( "subCategoryList", subCategoryList );
    }
    ProductUtils.setProductCategoryList( request, true );
  }
}
