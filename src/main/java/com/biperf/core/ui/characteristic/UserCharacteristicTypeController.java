/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/characteristic/UserCharacteristicTypeController.java,v $
 */

package com.biperf.core.ui.characteristic;

import java.util.List;

import com.biperf.core.service.participant.UserCharacteristicService;

/**
 * UserCharacteristicTypeController.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserCharacteristicTypeController extends CharacteristicController
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
    return getUserCharacteristicService().getAllCharacteristics();
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
}
