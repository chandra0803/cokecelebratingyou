/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/system/CharacteristicService.java,v $
 */

package com.biperf.core.service.system;

import java.util.List;
import java.util.UUID;

import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.cms.CMAssetService;

/**
 * CharacteristicService.
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
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface CharacteristicService extends SAO
{
  // No BEAN_NAME defined since there is no bean definition.

  /**
   * Get all Characteristics for a particular Characteristic Type.
   * 
   * @return List
   */
  public List getAllCharacteristics();

  /**
   * Get the Characteristic by id
   * 
   * @param id
   * @return Characteristic
   */
  public Characteristic getCharacteristicById( Long id );

  /**
   * Set the CharacteristicDAO through IoC.
   * 
   * @param characteristicDAO
   */
  public void setCharacteristicDAO( CharacteristicDAO characteristicDAO );

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService );

  /**
   * Saves or updates the Characteristic to the database.
   * 
   * @param characteristic
   * @return Characteristic
   * @throws ServiceErrorException
   */
  public Characteristic saveCharacteristic( Characteristic characteristic ) throws ServiceErrorException;

  public Long getCharacteristicIdByRosterCharacteristicId( UUID rosterCharacteristicId );

  public UUID getRosterCharacteristicIdByCharacteristicId( Long characteristicId );
  
  public Characteristic getCharacteristicByRosterCharacteristicId( UUID rosterCharacteristicId );

}
