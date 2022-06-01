/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/impl/UserCharacteristicServiceImpl.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.participant.UserCharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.system.impl.CharacteristicServiceImpl;

/**
 * UserCharacteristicServiceImpl.
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
 * <td>Jun 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserCharacteristicServiceImpl extends CharacteristicServiceImpl implements UserCharacteristicService
{

  protected SystemVariableService systemVariableService;

  protected UserCharacteristicDAO userCharacteristicDAO;

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
    // Characteristic dbCharacteristic =
    // getUserCharacteristicDAO().getCharacteristicByName(characteristic.getCharacteristicName() );
    createOrUpdateCMAssetData( characteristic );
    return this.characteristicDAO.saveCharacteristic( characteristic );
  }

  @Override
  public List<Characteristic> getAvailbleParticipantIdentifierCharacteristics()
  {
    return userCharacteristicDAO.getAvailbleParticipantIdentifierCharacteristics();
  }

  @Override
  public List<String> getBankEnrollmentCharacteristicNames()
  {
    List<String> names = new ArrayList<>();
    names.add( systemVariableService.getPropertyByName( SystemVariableService.BANK_ENROLLMENT_CHARACTERISTIC1 ).getStringVal() );
    names.add( systemVariableService.getPropertyByName( SystemVariableService.BANK_ENROLLMENT_CHARACTERISTIC2 ).getStringVal() );
    return names;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setUserCharacteristicDAO( UserCharacteristicDAO userCharacteristicDAO )
  {
    this.userCharacteristicDAO = userCharacteristicDAO;
  }
  
  /* WIP 37311 customization starts */
  public String getCharacteristicValueByUserAndCharacterisiticId(Long userId, Long characteristicId)
  {
      return this.userCharacteristicDAO.getCharacteristicValueByUserAndCharacterisiticId(userId, characteristicId);
  }
  /* WIP 37311 customization ends */

  // Client customizations for WIP #56492 starts
  public String getCharacteristicValueByDivisionKeyAndUserId( Long userId )
  {
    return this.userCharacteristicDAO.getCharacteristicValueByDivisionKeyAndUserId( userId );
  }
  
  public List<String> getAllDivisionKeyValues()
  {
    return this.userCharacteristicDAO.getAllDivisionKeyValues();
  }
  // Client customizations for WIP #56492 ends
  
}
