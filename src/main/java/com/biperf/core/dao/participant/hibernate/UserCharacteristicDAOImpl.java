/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/hibernate/UserCharacteristicDAOImpl.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.participant.UserCharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * UserCharacteristicDAOImpl.
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
 * <td>tom</td>
 * <td>Jun 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class UserCharacteristicDAOImpl implements UserCharacteristicDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.system.CharacteristicDAO#getCharacteristicById(java.lang.Long)
   * @param id
   * @return Characteristic
   */
  public Characteristic getCharacteristicById( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    return (UserCharacteristicType)session.get( UserCharacteristicType.class, id );
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
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.characteristic.AllUserCharacteristicTypeList" ).list();

  }

  @SuppressWarnings( "unchecked" )
  public List<Characteristic> getAvailbleParticipantIdentifierCharacteristics()
  {
    return HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.characteristic.UnusedParticipantIdentifierUserCharacteristics" ).list();
  }
  
  /* WIP 37311 customization starts */
  public String getCharacteristicValueByUserAndCharacterisiticId(Long userId, Long characteristicId)
  { 
       Session session = HibernateSessionManager.getSession();
       Query query = session.getNamedQuery( "com.biperf.core.domain.user.usercharacteristic.getCharacteristicValueByUserAndCharacterisiticId" );
       query.setParameter( "userId", userId );
       query.setParameter( "characteristicId", characteristicId);
       return (String)query.uniqueResult();
  }
  /* WIP 37311 customization ends */

  @Override
  public Long getCharacteristicIdByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.characteristic.getCharacteristicIdByRosterCharacteristicId" );
    query.setParameter( "rosterCharacteristicId", rosterCharacteristicId );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterCharacteristicIdByCharacteristicId( Long characteristicId )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.characteristic.getRosterCharacteristicIdByCharacteristicId" );
    query.setParameter( "characteristicId", characteristicId );

    return (UUID)query.uniqueResult();
  }

  @Override
  public Characteristic getCharacteristicByRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria searchCriteria = session.createCriteria( UserCharacteristicType.class );
    searchCriteria.add( Restrictions.eq( "rosterCharacteristicId", rosterCharacteristicId ) );
    return (UserCharacteristicType)searchCriteria.uniqueResult();

  }
  // Client customizations for WIP #56492 starts
  public String getCharacteristicValueByDivisionKeyAndUserId( Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.usercharacteristic.getCharacteristicValueByDivisionKeyAndUserId" );
    query.setParameter( "userId", userId );
    return (String)query.uniqueResult();
  }
  // Client customizations for WIP #56492 ends
  
  //Client customizations for WIP #56492 starts
  public List<String> getAllDivisionKeyValues()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.usercharacteristic.getAllDivisionKeyValues" );
   
    return (List<String>)query.list();
  }
  // Client customizations for WIP #56492 ends
}
