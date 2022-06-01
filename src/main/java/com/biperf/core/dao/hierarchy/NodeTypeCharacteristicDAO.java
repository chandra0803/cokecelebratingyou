/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.hierarchy;

import java.util.List;

import com.biperf.core.dao.system.CharacteristicDAO;

/**
 * ProductCharacteristicDAO.
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
public interface NodeTypeCharacteristicDAO extends CharacteristicDAO
{
  /**
   * Gets all of the NodeTypeCharacteristicTypes for a nodeTypeId
   * 
   * @param nodeTypeId
   * @return List
   */
  public List getAllNodeTypeCharacteristicTypesByNodeTypeId( Long nodeTypeId );

}
