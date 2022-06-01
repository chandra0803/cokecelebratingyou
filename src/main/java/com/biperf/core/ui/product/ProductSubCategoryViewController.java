/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/product/ProductSubCategoryViewController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.service.product.ProductCategoryToParentCategoryAssociation;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ProductSubCategoryViewController.
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
 * <td>Jun 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductSubCategoryViewController extends BaseController
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
      try
      {
        String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
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
          productCategoryId = new Long( (String)clientStateMap.get( "id" ) );
        }
        catch( ClassCastException cce )
        {
          productCategoryId = (Long)clientStateMap.get( "id" );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ProductCategoryToParentCategoryAssociation() );
    ProductCategory productCategory = getProductCategoryService().getProductCategoryById( productCategoryId, null );
    request.setAttribute( "productCategory", productCategory );
  }

  /**
   * Gets a ProductCategoryService
   * 
   * @return ProductCategoryService
   * @throws Exception
   */
  private ProductCategoryService getProductCategoryService() throws Exception
  {
    return (ProductCategoryService)getService( ProductCategoryService.BEAN_NAME );
  } // end
}
