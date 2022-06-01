/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/UserCharacteristicService.java,v $
 */

package com.biperf.core.service.participant;

import java.util.List;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.service.system.CharacteristicService;

/**
 * UserCharacteristicService.
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
public interface UserCharacteristicService extends CharacteristicService
{
  /** BEAN_NAME */
  public static final String BEAN_NAME = "userCharacteristicService";

  /**
   * @return A list containing the name of each characteristic designated as a bank enrollment characteristic
   */
  public List<String> getBankEnrollmentCharacteristicNames();

  public List<Characteristic> getAvailbleParticipantIdentifierCharacteristics();
  
  /* WIP 37311 customization starts */
  public String getCharacteristicValueByUserAndCharacterisiticId(Long userId, Long characteristicId);
  /* WIP 37311 customization ends */
  // Client customizations for WIP #56492 starts
  public String getCharacteristicValueByDivisionKeyAndUserId( Long userId );
  
  public List<String> getAllDivisionKeyValues();
  // Client customizations for WIP #56492 ends
}
