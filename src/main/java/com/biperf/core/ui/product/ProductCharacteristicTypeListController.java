/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.product;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.ui.characteristic.CharacteristicController;

/**
 * ProductCharacteristicTypeListController.
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
 * <td>zahler</td>
 * <td>Mar 7, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCharacteristicTypeListController extends CharacteristicController
{
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
    // set the list of characteristics in the request
    request.setAttribute( "charList", getProductCharacteristicService().getAllCharacteristics() );

    // set the list of available characteristic types in the request
    request.setAttribute( "charTypeList", CharacteristicDataType.getList() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicController#makeGetAllCharacteristicServiceCall(java.lang.Long)
   * @param domainId
   * @return List
   */
  public List makeGetAllCharacteristicServiceCall( Long domainId )
  {
    return getProductCharacteristicService().getAllCharacteristics();
  }

  /**
   * @return ProductCharacteristicService
   */
  public ProductCharacteristicService getProductCharacteristicService()
  {
    return (ProductCharacteristicService)getService( ProductCharacteristicService.BEAN_NAME );
  }
}
