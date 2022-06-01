/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/system/hibernate/SystemVariableDAOImpl.java,v $
 */

package com.biperf.core.dao.system.hibernate;

import java.util.List;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;

import com.biperf.core.dao.system.SystemVariableDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * SystemVariableDAOImpl.
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
 * <td>Feb 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class SystemVariableDAOImpl implements SystemVariableDAO
{
  public static String CACHE_REGION_NAME = "com.biperf.core.dao.system.hibernate.PropertySetItem.getPropertyByName";

  /**
   * Get the propertySetItem for the propertyName parameter. Overridden from
   * 
   * @see com.biperf.core.dao.system.SystemVariableDAO#getPropertyByName(java.lang.String)
   * @param propertyName
   * @return PropertySetItem
   */
  public PropertySetItem getPropertyByName( String propertyName )
  {
    Session session = HibernateSessionManager.getSession();
    if ( null != propertyName )
    {
      return (PropertySetItem)session.get( PropertySetItem.class, propertyName );
    }
    else
    {
      return null;
    }
  }

  /**
   * Save or update the propertySetItem. Overridden from
   * 
   * @see com.biperf.core.dao.system.SystemVariableDAO#savePropertySetItem(com.biperf.core.domain.system.PropertySetItem)
   * @param propertySetItem
   */
  public void savePropertySetItem( PropertySetItem propertySetItem )
  {
    Session session = HibernateSessionManager.getSession();
    // TODO Why doesn't session.contains(propertySetItem) work?
    // If the object already exists in the session, the
    // NonUniqueObjectException will occur, if that is the
    // case, try to merge.
    try
    {
      session.saveOrUpdate( propertySetItem );
    }
    catch( NonUniqueObjectException e )
    {
      propertySetItem = (PropertySetItem)session.merge( propertySetItem );
    }
  }

  /**
   * Delete the propertySetItem. Overridden from
   * 
   * @see com.biperf.core.dao.system.SystemVariableDAO#deletePropertySetItem(com.biperf.core.domain.system.PropertySetItem)
   * @param propertySetItem
   */
  public void deletePropertySetItem( PropertySetItem propertySetItem )
  {
    Session session = HibernateSessionManager.getSession();
    session.delete( propertySetItem );
  }

  /**
   * Gets a list of all the propertySetItems Overridden from
   * 
   * @see com.biperf.core.dao.system.SystemVariableDAO#getAllProperties
   * @return list
   */
  public List<Characteristic> getAllProperties()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "all_properties" ).list();
  }
}
