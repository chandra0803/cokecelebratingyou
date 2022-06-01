/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/system/CharacteristicDAO.java,v $
 */

package com.biperf.core.dao.system;

import java.util.List;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.characteristic.Characteristic;

/**
 * CharacteristicDAO.
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
 * <td>Jason</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface CharacteristicDAO extends DAO
{

  /**
   * Get the Characteristic by id for a particular Characteristic Type.
   * 
   * @param id
   * @return Characteristic
   */
  public Characteristic getCharacteristicById( Long id );

  /**
   * Save or update the Characteristic.
   * 
   * @param characteristic
   * @return characteristic
   */
  public Characteristic saveCharacteristic( Characteristic characteristic );

  /**
   * Gets all of the Characteristics for a particular Characteristic Type.
   * 
   * @return List
   */
  public List<Characteristic> getAllCharacteristics();

  public Long getCharacteristicIdByRosterCharacteristicId( UUID rosterCharacteristicId );

  public UUID getRosterCharacteristicIdByCharacteristicId( Long characteristicId );
  
  public Characteristic getCharacteristicByRosterCharacteristicId( UUID rosterCharacteristicId );
}
