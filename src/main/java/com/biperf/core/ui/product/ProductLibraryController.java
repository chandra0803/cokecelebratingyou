/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductLibraryController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.product.ProductSearchCriteria;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ProductLibraryController.
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
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductLibraryController extends BaseController
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
    Long parentCategoryId = null;
    ProductLibraryForm form = (ProductLibraryForm)request.getAttribute( "productLibraryForm" );

    if ( RequestUtils.containsAttribute( request, "parentCategoryId" ) )
    {
      parentCategoryId = RequestUtils.getOptionalAttributeLong( request, "parentCategoryId" );
    }
    if ( RequestUtils.containsAttribute( request, "productCategoryId" ) )
    {
      productCategoryId = RequestUtils.getOptionalAttributeLong( request, "productCategoryId" );
    }
    else if ( form.getCategoryId() != null && form.getCategoryId().longValue() > 0 )
    {
      if ( form.getSubCategoryId() != null && form.getSubCategoryId().longValue() > 0 )
      {
        parentCategoryId = form.getCategoryId();
        productCategoryId = form.getSubCategoryId();
      }
      else
      {
        productCategoryId = form.getCategoryId();
      }
    }
    else
    {
      try
      {
        String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
        if ( clientState != null && clientState.length() > 0 )
        {
          String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
          String password = ClientStatePasswordManager.getPassword();

          if ( cryptoPass != null && cryptoPass.equals( "1" ) )
          {
            password = ClientStatePasswordManager.getGlobalPassword();
          }
          // Deserialize the client state.
          Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
          try
          {
            String productCategoryIdString = (String)clientStateMap.get( "categoryId" );
            productCategoryId = new Long( productCategoryIdString );
          }
          catch( NumberFormatException e )
          {
            // Do nothing already set to null
          }
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    if ( productCategoryId == null || productCategoryId.longValue() == 0 )
    {
      try
      {
        String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
        if ( clientState != null && clientState.length() > 0 )
        {
          String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
          String password = ClientStatePasswordManager.getPassword();

          if ( cryptoPass != null && cryptoPass.equals( "1" ) )
          {
            password = ClientStatePasswordManager.getGlobalPassword();
          }
          // Deserialize the client state.
          Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
          try
          {
            String productCategoryIdString = (String)clientStateMap.get( "productCategoryId" );
            productCategoryId = new Long( productCategoryIdString );
          }
          catch( NumberFormatException e1 )
          {
            // do nothing
          }
          catch( ClassCastException e2 )
          {
            productCategoryId = (Long)clientStateMap.get( "productCategoryId" );
          }
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    if ( productCategoryId != null && productCategoryId.longValue() != 0 )
    {
      if ( parentCategoryId != null && parentCategoryId.longValue() != 0 )
      {
        form.setCategoryId( parentCategoryId );
        form.setSubCategoryId( productCategoryId );
      }
      else
      {
        form.setCategoryId( productCategoryId );
        form.setSubCategoryId( new Long( -1 ) );
      }
    }

    ProductUtils.setProductCategoryList( request, true );

    if ( parentCategoryId != null && parentCategoryId.longValue() != 0 )
    {
      ProductUtils.setProductSubcategoryList( request, parentCategoryId, true );
    }
    else
    {
      ProductUtils.setProductSubcategoryList( request, productCategoryId, true );
    }

    // Get the productList for this productCategory
    List productList = new ArrayList();
    ProductSearchCriteria searchCriteria = new ProductSearchCriteria();

    Object oldCategoryId = request.getSession().getAttribute( "categoryId" );
    if ( oldCategoryId != null )
    {
      ProductUtils.setProductSubcategoryList( request, (Long)oldCategoryId, true );

      Object oldSubCategoryId = request.getSession().getAttribute( "subCategoryId" );
      if ( oldSubCategoryId != null )
      {
        searchCriteria.setCategoryId( (Long)oldCategoryId );
        searchCriteria.setSubCategoryId( (Long)oldSubCategoryId );
      }
      else
      {
        searchCriteria.setCategoryId( (Long)oldCategoryId );
        searchCriteria.setSubCategoryId( new Long( -1 ) );
      }
      productList = getProductService().searchProducts( searchCriteria );
    }
    else if ( productCategoryId != null && productCategoryId.longValue() > 0 )
    {
      if ( parentCategoryId != null && parentCategoryId.longValue() > 0 )
      {
        searchCriteria.setCategoryId( parentCategoryId );
        searchCriteria.setSubCategoryId( productCategoryId );
      }
      else
      {
        searchCriteria.setCategoryId( productCategoryId );
        searchCriteria.setSubCategoryId( new Long( -1 ) );
      }

      productList = getProductService().searchProducts( searchCriteria );
    }

    if ( !RequestUtils.containsAttribute( request, "productList" ) )
    {
      request.setAttribute( "productList", productList );
    }

    request.getSession().setAttribute( "categoryId", form.getCategoryId() );
    request.getSession().setAttribute( "subCategoryId", form.getSubCategoryId() );

    request.setAttribute( "productCategoryId", productCategoryId );
  }

  /**
   * Get a productService.
   * 
   * @return ProductService
   */
  private ProductService getProductService()
  {
    return (ProductService)getService( ProductService.BEAN_NAME );
  }

}
