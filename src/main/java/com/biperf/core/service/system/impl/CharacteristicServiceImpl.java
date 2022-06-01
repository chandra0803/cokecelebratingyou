/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/system/impl/CharacteristicServiceImpl.java,v $
 */

package com.biperf.core.service.system.impl;

import java.util.List;
import java.util.UUID;

import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.system.CharacteristicService;

/**
 * CharacteristicServiceImpl.
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
public abstract class CharacteristicServiceImpl implements CharacteristicService
{

  /** CharacteristicDAO */
  protected CharacteristicDAO characteristicDAO;
  private CMAssetService cmAssetService = null;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.system.CharacteristicService#setCharacteristicDAO(com.biperf.core.dao.system.CharacteristicDAO)
   * @param characteristicDAO
   */
  public void setCharacteristicDAO( CharacteristicDAO characteristicDAO )
  {
    this.characteristicDAO = characteristicDAO;
  }

  /**
   * Overridden from
   * 
   * @return List
   */
  public List<Characteristic> getAllCharacteristics()
  {
    return characteristicDAO.getAllCharacteristics();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.system.CharacteristicService#getCharacteristicById(java.lang.Long)
   * @param id
   * @return Characteristic
   */
  public Characteristic getCharacteristicById( Long id )
  {
    return characteristicDAO.getCharacteristicById( id );
  }

  /**
   * Create or update the characteristic cm data.
   * 
   * @param characteristic
   * @throws ServiceErrorException
   */
  protected void createOrUpdateCMAssetData( Characteristic characteristic ) throws ServiceErrorException
  {
    if ( characteristic.getId() == null )
    {
      // new characteristic - set the asset and key
      characteristic.setNameCmKey( characteristic.getCmKeyName() );
      characteristic.setCmAssetCode( cmAssetService.getUniqueAssetCode( characteristic.getCmAssetNamePrefix() ) );
    }
    boolean uniqueCharacteristicName = true;

    if ( characteristic instanceof NodeTypeCharacteristicType )
    {
      uniqueCharacteristicName = false;
    }
    CMDataElement cmDataElement = new CMDataElement( "Characteristic Name", characteristic.getCmKeyName(), characteristic.getCharacteristicName(), uniqueCharacteristicName );

    cmAssetService.createOrUpdateAsset( characteristic.getCmSectionName(),
                                        characteristic.getCmAssetTypeName(),
                                        characteristic.getCharacteristicName() + characteristic.getCmAssetTitleSuffix(),
                                        characteristic.getCmAssetCode(),
                                        cmDataElement );
  }

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  @Override
  public Long getCharacteristicIdByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    return characteristicDAO.getCharacteristicIdByRosterCharacteristicId( rosterCharacteristicId );
  }

  @Override
  public UUID getRosterCharacteristicIdByCharacteristicId( Long characteristicId )
  {
    return characteristicDAO.getRosterCharacteristicIdByCharacteristicId( characteristicId );
  }
  
  public Characteristic getCharacteristicByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    return characteristicDAO.getCharacteristicByRosterCharacteristicId( rosterCharacteristicId );
  }

}
