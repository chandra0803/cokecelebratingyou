/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.hierarchy;

import java.util.List;

import com.biperf.core.service.system.CharacteristicService;

/**
 * NodeTypeCharacteristicService.
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
public interface NodeTypeCharacteristicService extends CharacteristicService
{
  /** BEAN_NAME */
  public static final String BEAN_NAME = "nodeTypeCharacteristicService";

  /**
   * Get all NodeTypeCharacteristicTypes for a given nodeTypeId.
   * 
   * @param nodeTypeId
   * @return List
   */
  public List getAllNodeTypeCharacteristicTypesByNodeTypeId( Long nodeTypeId );

}
