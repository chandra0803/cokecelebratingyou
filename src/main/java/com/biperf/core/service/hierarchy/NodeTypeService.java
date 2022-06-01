/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/hierarchy/NodeTypeService.java,v $
 */

package com.biperf.core.service.hierarchy;

import java.util.List;

import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

/**
 * NodeTypeService.
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
 * <td>tennant</td>
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface NodeTypeService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "nodeTypeService";

  /**
   * Saves the given node type Will throw an exception if the node type name is already taken
   * 
   * @param nodeType
   * @return NodeType
   * @throws ServiceErrorException
   */
  public NodeType saveNodeType( NodeType nodeType ) throws ServiceErrorException;

  /**
   * Get all NodeTypes
   * 
   * @return List
   */
  public List<NodeType> getAll();

  /**
   * @param id
   * @return NodeType or null
   */
  public NodeType getNodeTypeById( Long id );

  /**
   * Will logically delete the NodeTypes in the List, provided there are no nodes assigned to those
   * node types
   * 
   * @param list
   * @throws ServiceErrorException
   */
  void deleteNodeTypes( List list ) throws ServiceErrorException;

  public NodeType getDefaultNodeType();
}
