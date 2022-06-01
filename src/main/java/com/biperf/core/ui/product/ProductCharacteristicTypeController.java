/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/product/ProductCharacteristicTypeController.java,v $
 */

package com.biperf.core.ui.product;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.product.Product;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.ui.characteristic.CharacteristicController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ProductCharacteristicTypeController.
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
 * <td>wadzinsk</td>
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCharacteristicTypeController extends CharacteristicController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicController#makeGetAllCharacteristicServiceCall(java.lang.Long)
   * @param domainId
   * @return List
   */
  public List makeGetAllCharacteristicServiceCall( Long domainId )
  {
    // TODO: Refactor all Char types controllers to perform need calls in their overriden excute
    // methods.
    return null;
  }

  /**
   * @return ProductCharacteristicService
   */
  public ProductCharacteristicService getProductCharacteristicService()
  {
    return (ProductCharacteristicService)getService( ProductCharacteristicService.BEAN_NAME );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // Product Id can either be "productID" or "domainId" if from "common char code" context or
    // product context, so try to fetch either
    Long productId = null;

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
        String productIdString = (String)clientStateMap.get( "domainId" );
        productId = new Long( productIdString );
      }
      catch( NumberFormatException e )
      {
        // do nothing as in next step it is checking if productId is null
      }
      if ( productId == null )
      {
        try
        {
          String productIdString = (String)clientStateMap.get( "productId" );
          productId = new Long( productIdString );
        }
        catch( ClassCastException e1 )
        {
          productId = (Long)clientStateMap.get( "productId" );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( productId == null )
    {
      throw new BeaconRuntimeException( "Either productId or domainId request parameter must not be null" );
    }

    // Load product for View page
    Product product = getProductService().getProductById( productId );

    request.setAttribute( "product", product );

    // set the list of characteristics in the request
    request.setAttribute( "charList", product.getProductCharacteristicTypes() );

    // set the list of available characteristic types in the request
    request.setAttribute( "charTypeList", CharacteristicDataType.getList() );

    // Get the available characteristics to add.
    request.setAttribute( "availableProductCharaceristicTypes", getProductCharacteristicService().getAllAvailableProductCharacteristicTypesByProductId( productId ) );
  }

  /**
   * Get the ProductService from the beanLocator.
   * 
   * @return ProductService
   * @throws Exception
   */
  protected ProductService getProductService()
  {
    return (ProductService)getService( ProductService.BEAN_NAME );
  }

}
