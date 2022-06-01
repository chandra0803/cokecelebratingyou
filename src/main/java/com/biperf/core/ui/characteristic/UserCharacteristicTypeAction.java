/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/characteristic/UserCharacteristicTypeAction.java,v $
 */

package com.biperf.core.ui.characteristic;

import java.util.Date;

import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.UserCharacteristicService;

/**
 * UserCharacteristicTypeAction.
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
public class UserCharacteristicTypeAction extends CharacteristicAction
{

  /**
   * Overridden from
   * 
   * @param charForm
   * @throws ServiceErrorException
   */
  public void makeDeleteCharacteristicServiceCall( CharacteristicForm charForm ) throws ServiceErrorException
  {
    String[] idsToDelete = charForm.getDeleteValues();
    for ( int i = 0; i < idsToDelete.length; i++ )
    {
      UserCharacteristicType charItem = (UserCharacteristicType)getUserCharacteristicService().getCharacteristicById( new Long( idsToDelete[i] ) );
      charItem.setActive( false );
      charItem.setCharacteristicName( charItem.getCharacteristicName().toString() + "" + new Date() );
      getUserCharacteristicService().saveCharacteristic( charItem );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#makeSaveCharacteristicServiceCall(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   * @throws ServiceErrorException
   */
  public void makeSaveCharacteristicServiceCall( CharacteristicForm charForm ) throws ServiceErrorException
  {
    getUserCharacteristicService().saveCharacteristic( charForm.toDomainObjectUserCharacteristicType() );

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#makeGetCharacteristicServiceCallAndLoad(java.lang.Long,
   *      com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param characteristicId
   * @param charForm
   */
  public void makeGetCharacteristicServiceCallAndLoad( Long characteristicId, CharacteristicForm charForm )
  {
    UserCharacteristicType charToUpdate = (UserCharacteristicType)getUserCharacteristicService().getCharacteristicById( characteristicId );
    charForm.loadUserCharacteristicType( charToUpdate );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicAction#onDisplayCreate(com.biperf.core.ui.characteristic.CharacteristicForm)
   * @param charForm
   */
  public void onDisplayCreate( CharacteristicForm charForm )
  {
    // nothing to do.
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
}
