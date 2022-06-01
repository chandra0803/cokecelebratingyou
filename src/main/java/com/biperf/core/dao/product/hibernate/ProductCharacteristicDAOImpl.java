/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.product.hibernate;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.product.ProductCharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ProductCharacteristicDAOImpl.
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
public class ProductCharacteristicDAOImpl extends BaseDAO implements ProductCharacteristicDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.product.ProductCharacteristicDAO#getActiveProductsWithCharacteristicCount(java.lang.Long)
   * @param characteristicId
   * @return count of active products with the given characteristic
   */
  public int getActiveProductsWithCharacteristicCount( Long characteristicId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.product.ActiveProductsWithCharacteristicCount" );
    query.setParameter( "characteristicId", characteristicId );

    Integer count = (Integer)query.uniqueResult();

    return count.intValue();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.system.CharacteristicDAO#getAllCharacteristics()
   * @return List
   */
  public List getAllCharacteristics()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.characteristic.AllProductCharacteristicTypeList" ).list();
  }

  /**
   * Save or update the Characteristic.
   * 
   * @param characteristic
   * @return characteristic
   */
  public Characteristic saveCharacteristic( Characteristic characteristic )
  {
    return (Characteristic)HibernateUtil.saveOrUpdateOrShallowMerge( characteristic );
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
    return (Characteristic)getSession().get( ProductCharacteristicType.class, id );
  }

  @Override
  public Long getCharacteristicIdByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    // No implementation is required
    return null;
  }

  @Override
  public UUID getRosterCharacteristicIdByCharacteristicId( Long characteristicId )
  {
    // No implementation is required
    return null;
  }

  @Override
  public Characteristic getCharacteristicByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    Criteria searchCriteria = getSession().createCriteria( ProductCharacteristicType.class );
    searchCriteria.add( Restrictions.eq( "rosterCharacteristicId", rosterCharacteristicId ) );
    return (ProductCharacteristicType)searchCriteria.uniqueResult();
  }
}
