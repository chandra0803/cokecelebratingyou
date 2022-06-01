/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/hierarchy/NodeTypeDAO.java,v $
 */

package com.biperf.core.dao.hierarchy;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.hierarchy.NodeType;

/**
 * NodeTypeDAO.
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
 * <td>Apr 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface NodeTypeDAO extends DAO
{
  public static final String BEAN_NAME = "nodeTypeDAO";

  /**
   * save the nodeType
   * 
   * @param nodeType
   * @return NodeType the saved Node
   */
  public NodeType saveNodeType( NodeType nodeType );

  /**
   * @param id
   * @return NodeType
   */
  public NodeType getNodeTypeById( Long id );

  /**
   * @return List
   */
  public List<NodeType> getAll();

  public NodeType getDefaultNodeType();

}
