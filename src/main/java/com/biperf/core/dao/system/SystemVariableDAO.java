/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/system/SystemVariableDAO.java,v $
 */

package com.biperf.core.dao.system;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.system.PropertySetItem;

/**
 * SystemVariableDAO.
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
 * <td>Apr 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface SystemVariableDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "systemVariableDAO";

  /**
   * Gets a list of all Properties from the database.
   * 
   * @return List
   */
  public abstract List getAllProperties();

  /**
   * Gets a PropertySetItem based on the property name from the database.
   * 
   * @param propertyName
   * @return PropertySetItem
   */
  public abstract PropertySetItem getPropertyByName( String propertyName );

  /**
   * Saves the propertySetItem to the database.
   * 
   * @param propertySetItem
   */
  public abstract void savePropertySetItem( PropertySetItem propertySetItem );

  /**
   * Deletes the propertySetItem from the database.
   * 
   * @param propertySetItem
   */
  public abstract void deletePropertySetItem( PropertySetItem propertySetItem );
}
