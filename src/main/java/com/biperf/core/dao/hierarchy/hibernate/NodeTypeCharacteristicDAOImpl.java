/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.hierarchy.hibernate;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.hierarchy.NodeTypeCharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * NodeTypeCharacteristicDAOImpl.
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
 */
public class NodeTypeCharacteristicDAOImpl extends BaseDAO implements NodeTypeCharacteristicDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeTypeCharacteristicDAO#getAllNodeTypeCharacteristicTypesByNodeTypeId(java.lang.Long)
   * @param nodeTypeId
   * @return List
   */
  public List getAllNodeTypeCharacteristicTypesByNodeTypeId( Long nodeTypeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.characteristic.AllNodeTypeCharacteristicTypeByNodeTypeIdList" );
    query.setLong( "nodeTypeId", nodeTypeId.longValue() );
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.system.CharacteristicDAO#getCharacteristicById(java.lang.Long)
   * @param id
   * @return Characteristic
   */
  public Characteristic getCharacteristicById( Long id )
  {
    return (Characteristic)getSession().get( NodeTypeCharacteristicType.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.system.CharacteristicDAO#saveCharacteristic(com.biperf.core.domain.characteristic.Characteristic)
   * @param characteristic
   * @return Characteristic
   */
  public Characteristic saveCharacteristic( Characteristic characteristic )
  {
    return (Characteristic)HibernateUtil.saveOrUpdateOrShallowMerge( characteristic );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.system.CharacteristicDAO#getAllCharacteristics()
   * @return List
   */
  public List getAllCharacteristics()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.characteristic.AllNodeTypeCharacteristicTypeList" ).list();
  }

  @Override
  public Long getCharacteristicIdByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.characteristic.getCharacteristicIdByRosterCharacteristicId" );
    query.setParameter( "rosterCharacteristicId", rosterCharacteristicId );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterCharacteristicIdByCharacteristicId( Long characteristicId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.characteristic.getRosterCharacteristicIdByCharacteristicId" );
    query.setParameter( "characteristicId", characteristicId );

    return (UUID)query.uniqueResult();
  }

  @Override
  public Characteristic getCharacteristicByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    Criteria searchCriteria = getSession().createCriteria( NodeTypeCharacteristicType.class );
    searchCriteria.add( Restrictions.eq( "rosterCharacteristicId", rosterCharacteristicId ) );
    return (NodeTypeCharacteristicType)searchCriteria.uniqueResult();
  }

}
