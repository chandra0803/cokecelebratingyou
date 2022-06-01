/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/hierarchy/hibernate/NodeTypeDAOImpl.java,v $
 */

package com.biperf.core.dao.hierarchy.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.domain.hierarchy.NodeType;

/**
 * NodeTypeDAOImpl.
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
public class NodeTypeDAOImpl extends BaseDAO implements NodeTypeDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeTypeDAO#saveNodeType(com.biperf.core.domain.hierarchy.NodeType)
   * @param nodeType
   * @return NodeType
   */
  public NodeType saveNodeType( NodeType nodeType )
  {
    getSession().saveOrUpdate( nodeType );
    return nodeType;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeTypeDAO#getNodeTypeById(java.lang.Long)
   * @param id
   * @return NodeType
   */
  public NodeType getNodeTypeById( Long id )
  {
    NodeType nodeType = (NodeType)getSession().get( NodeType.class, id );
    return nodeType;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeTypeDAO#getAll()
   * @return List
   */
  public List<NodeType> getAll()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.dao.hierarchy.hibernate.NodeTypeDAOImpl.AllNodeTypes" );
    return query.list();
  }

  public NodeType getDefaultNodeType()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.dao.hierarchy.DefaultNodeType" );
    return (NodeType)query.uniqueResult();
  }

}
