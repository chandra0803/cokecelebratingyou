/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.product;

import java.util.List;

import com.biperf.core.service.system.CharacteristicService;

/**
 * ProductCharacteristicService.
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
public interface ProductCharacteristicService extends CharacteristicService
{
  /** BEAN_NAME */
  public static final String BEAN_NAME = "productCharacteristicService";

  /**
   * @param productId
   * @return List
   */
  public List getAllAvailableProductCharacteristicTypesByProductId( Long productId );

  /**
   * Checks to see if characteristic is assigned to Active products, if it is, delete is now allowed
   * 
   * @param characteristicId
   * @return boolean canDelete
   */
  public boolean isDeleteProductCharacteristicTypeAllowed( Long characteristicId );

}
