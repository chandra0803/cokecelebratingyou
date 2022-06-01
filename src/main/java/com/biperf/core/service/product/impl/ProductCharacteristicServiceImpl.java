/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.product.impl;

import java.util.List;

import com.biperf.core.dao.product.ProductCharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.product.Product;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.system.impl.CharacteristicServiceImpl;

/**
 * ProductCharacteristicServiceImpl.
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
 * <td>June 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCharacteristicServiceImpl extends CharacteristicServiceImpl implements ProductCharacteristicService
{
  private ProductService productService;

  private ProductCharacteristicDAO getProductCharacteristicDAO()
  {
    return (ProductCharacteristicDAO)characteristicDAO;
  }

  /**
   * TODO: Remove this since we access the char list by the owner... Overridden from
   * 
   * @see com.biperf.core.service.product.ProductCharacteristicService#getAllAvailableProductCharacteristicTypesByProductId(java.lang.Long)
   * @param productId
   * @return List
   */
  public List getAllAvailableProductCharacteristicTypesByProductId( Long productId )
  {
    List availableCharacteristics = getProductCharacteristicDAO().getAllCharacteristics();

    Product product = productService.getProductById( productId );

    availableCharacteristics.removeAll( product.getProductCharacteristicTypes() );

    return availableCharacteristics;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.product.ProductCharacteristicService#isDeleteProductCharacteristicTypeAllowed(java.lang.Long)
   * @param characteristicId
   * @return boolean is Delete allowed
   */
  public boolean isDeleteProductCharacteristicTypeAllowed( Long characteristicId )
  {
    boolean deleteAllowed = false;
    int activeProductCount = getProductCharacteristicDAO().getActiveProductsWithCharacteristicCount( characteristicId );

    if ( activeProductCount == 0 )
    {
      deleteAllowed = true;
    }

    return deleteAllowed;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.system.CharacteristicService#saveCharacteristic(com.biperf.core.domain.characteristic.Characteristic)
   * @param characteristic
   * @return Characteristic
   * @throws ServiceErrorException
   */
  public Characteristic saveCharacteristic( Characteristic characteristic ) throws ServiceErrorException
  {
    // todo jjd Check to see if the record already exists in the database.
    createOrUpdateCMAssetData( characteristic );
    return getProductCharacteristicDAO().saveCharacteristic( characteristic );
  }

  public void setProductService( ProductService productService )
  {
    this.productService = productService;
  }
}
