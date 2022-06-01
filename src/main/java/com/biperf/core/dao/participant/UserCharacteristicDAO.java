/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/UserCharacteristicDAO.java,v $
 */

package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;

/**
 * UserCharacteristicDAO.
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
 * <td>Jun 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface UserCharacteristicDAO extends CharacteristicDAO
{
  // The only method here was removed, leaving DAO for future additions.
  public List<Characteristic> getAvailbleParticipantIdentifierCharacteristics();
  
  /* WIP 37311 customization starts */
  public String getCharacteristicValueByUserAndCharacterisiticId(Long userId, Long characteristicId);
  /* WIP 37311 customization end */
  // Client customizations for WIP #56492 starts
  public String getCharacteristicValueByDivisionKeyAndUserId( Long userId );
  // Client customizations for WIP #56492 ends
  
  public List<String> getAllDivisionKeyValues();
}
