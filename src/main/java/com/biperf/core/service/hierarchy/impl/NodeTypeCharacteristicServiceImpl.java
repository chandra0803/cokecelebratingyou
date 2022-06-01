/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.hierarchy.impl;

import java.util.List;

import com.biperf.core.dao.hierarchy.NodeTypeCharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.system.impl.CharacteristicServiceImpl;

/**
 * NodeTypeCharacteristicServiceImpl.
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
public class NodeTypeCharacteristicServiceImpl extends CharacteristicServiceImpl implements NodeTypeCharacteristicService
{

  private NodeTypeCharacteristicDAO getNodeTypeCharacteristicDAO()
  {
    return (NodeTypeCharacteristicDAO)characteristicDAO;
  }

  /**
   * Overridden from
   * 
   * @param nodeTypeId
   * @return List
   */
  public List getAllNodeTypeCharacteristicTypesByNodeTypeId( Long nodeTypeId )
  {
    return getNodeTypeCharacteristicDAO().getAllNodeTypeCharacteristicTypesByNodeTypeId( nodeTypeId );
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
    createOrUpdateCMAssetData( characteristic );
    return this.characteristicDAO.saveCharacteristic( characteristic );
  }
}
