/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductMaintainViewController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ProductMaintainViewController.
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
 * <td>Jun 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductMaintainViewController extends BaseController
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

    ProductForm productForm = (ProductForm)request.getAttribute( "productForm" );

    if ( RequestUtils.containsAttribute( request, "productCategoryId" ) )
    {
      RequestUtils.getOptionalAttributeLong( request, "productCategoryId" );
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
            String productCategoryIdString = (String)clientStateMap.get( "productCategoryId" );
            productCategoryId = new Long( productCategoryIdString );
          }
          catch( NumberFormatException e )
          {
            // do nothing
          }
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    if ( productCategoryId == null || productCategoryId.equals( (Long)0L ) )
    {
      if ( productForm.getProductCategoryId() != null && !productForm.getProductCategoryId().equals( "" ) )
      {
        productCategoryId = new Long( productForm.getProductCategoryId() );
      }
    }

    if ( productCategoryId != null && !productCategoryId.equals( (Long)0L ) )
    {

      ProductCategory prodCategory = getProductCategoryService().getProductCategoryById( productCategoryId, null );

      request.setAttribute( "productCategory", prodCategory );
    }

    ProductUtils.setProductCategoryList( request, false );
    ProductUtils.setProductSubcategoryList( request, productCategoryId, false );
  }

  /**
   * Gets a ProductCategoryService
   * 
   * @return ProductCategoryService
   * @throws Exception
   */
  private ProductCategoryService getProductCategoryService()
  {
    return (ProductCategoryService)getService( ProductCategoryService.BEAN_NAME );
  } // end

}
