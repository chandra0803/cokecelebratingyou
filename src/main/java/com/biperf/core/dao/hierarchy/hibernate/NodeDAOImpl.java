/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/hierarchy/hibernate/NodeDAOImpl.java,v $
 */

package com.biperf.core.dao.hierarchy.hibernate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.hierarchy.NodeSearchCriteria;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.FormattedValueBean;

/**
 * NodeDAOImpl.
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
 * <td>crosenquest</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class NodeDAOImpl extends BaseDAO implements NodeDAO
{

  private static final Log log = LogFactory.getLog( NodeDAOImpl.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#getChildNodesByParent(java.lang.Long)
   * @param parentNodeId
   * @return List
   */
  public List getChildNodesByParent( Long parentNodeId )
  {

    return getSession().getNamedQuery( "com.biperf.core.domain.node.ChildNodesByParent" ).setLong( "parentNodeId", parentNodeId.longValue() ).list();
  }

  /**
   * Gets the node from the DAO by id. Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#getNodeById(java.lang.Long)
   * @param id
   * @return Node
   */
  public Node getNodeById( Long id )
  {
    return (Node)getSession().get( Node.class, id );
  }

  /**
   * Get the node from the database by location code, which is stored in the node characteristic,
   * expected to be unique per node.
   * 
   * @param locCode
   * @return Node
   */
  public Node getNodeByRegistrationLocCode( String locCode )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.node.NodeByLocationCode" );

    query.setParameter( "locCode", locCode );

    return (Node)query.uniqueResult();

  }

  /**
   * Get the node from the database by the nodeName and Hierarchy.
   * 
   * @param nodeName
   * @param hierarchy
   * @return Node
   */
  public Node getNodeByNameAndHierarchy( String nodeName, Hierarchy hierarchy )
  {
    return (Node)getSession().getNamedQuery( "com.biperf.core.domain.node.GetNodeByNameAndHierarchy" ).setString( "name", nodeName ).setParameter( "hierarchy", hierarchy ).uniqueResult();
  }

  /**
   * Saves the node to the database. Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#saveNode(com.biperf.core.domain.hierarchy.Node)
   * @param node
   * @return Node
   */
  public Node saveNode( Node node )
  {
    return (Node)HibernateUtil.saveOrUpdateOrDeepMerge( node );
  }

  /**
   * Search for the nodes with the given criteria. Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#searchNode(java.lang.String, java.lang.String,
   *      java.lang.String)
   * @param name
   * @param description
   * @return List
   */
  public List searchNode( String name, String description, String deleted )
  {
    Criteria searchCriteria = HibernateSessionManager.getSession().createCriteria( Node.class );

    if ( null != name && !"".equals( name ) )
    {
      searchCriteria.add( Restrictions.ilike( "name", name, MatchMode.ANYWHERE ) );
    }

    if ( null != description && !"".equals( description ) )
    {
      searchCriteria.add( Restrictions.ilike( "description", description, MatchMode.ANYWHERE ) );
    }

    if ( null != deleted && !"".equals( deleted ) )
    {
      if ( deleted.equals( "true" ) )
      {
        searchCriteria.add( Restrictions.eq( "deleted", Boolean.TRUE ) );
      }
      else
      {
        searchCriteria.add( Restrictions.eq( "deleted", Boolean.FALSE ) );
      }

    }

    searchCriteria.addOrder( Order.asc( "name" ) );

    return searchCriteria.list();
  }

  /**
   * Search a node by criterion of hierarchyId/partialNodeName/nodeTypeId. Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#searchNode(java.lang.Long, java.lang.String,
   *      java.lang.Long)
   * @param hierarchyId
   * @param partialNodeName
   * @param nodeTypeId
   * @return List
   */
  public List searchNode( Long hierarchyId, String partialNodeName, Long nodeTypeId )
  {

    Criteria searchCriteria = HibernateSessionManager.getSession().createCriteria( Node.class );

    searchCriteria.createCriteria( "hierarchy" ).add( Restrictions.eq( "id", hierarchyId ) );
    searchCriteria.createCriteria( "nodeType" ).add( Restrictions.eq( "id", nodeTypeId ) );
    // Added boolean condition for the bug fix 20034
    searchCriteria.add( Restrictions.eq( "deleted", new Boolean( false ) ) );
    if ( null != partialNodeName && !"".equals( partialNodeName ) )
    {
      searchCriteria.add( Restrictions.ilike( "name", partialNodeName, MatchMode.ANYWHERE ) );
    }

    searchCriteria.addOrder( Order.asc( "name" ) );

    return searchCriteria.list();
  }

  /**
   * Get a set of ALL Nodes from the database. Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.node.AllNodeList" ).list();
  }

  /**
   * Get a list of all nodes from the database excluding the node with the id param. Overridden from
   * Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#getAllExcludingNode(com.biperf.core.domain.hierarchy.Node)
   * @param node
   * @return List
   */
  public List getAllExcludingNode( Node node )
  {

    Criteria criteria = getSession().createCriteria( Node.class );
    criteria.add( Restrictions.ne( "id", node.getId() ) );

    return criteria.list();

  }

  /**
   * Retrieves the root node for the specified hierarchy
   * 
   * @param hierarchy
   * @return Node
   */
  public Node getRootNode( Hierarchy hierarchy )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.node.HierarchyRootNode" );

    query.setParameter( "hierarchy", hierarchy );

    return (Node)query.uniqueResult();
  }

  /**
   * Gets all non-deleted Node objects which have an association to the given NodeType;
   * 
   * @param nodeType
   * @return List - list of nodes
   */
  public List getNodesByNodeType( NodeType nodeType )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.node.GetNodeByNodeType" ).setEntity( "nodeType", nodeType ).list();
  }

  public List getNodesByNodeTypeAndHierarchy( NodeType nodeType, Long hierarchyId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.node.GetNodeByNodeTypeAndHierarchy" );

    query.setParameter( "nodeType", nodeType );
    query.setParameter( "hierarchyId", hierarchyId );

    return query.list();
  }

  /**
   * Retrieves the list of nodes for the specified hierarchy
   * 
   * @param hierarchy
   * @return Node
   */
  public List getNodesByHierarchy( Hierarchy hierarchy )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.node.NodesByHierarchy" );

    query.setParameter( "hierarchy", hierarchy );

    return query.list();
  }

  /**
   * Get the hierarchy nodes
   * 
   * @param hierarchyId
   * @return List of Node objects
   */
  public List getNodesAsHierarchy( Long hierarchyId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.node.NodesAsHierarchy" );

    query.setParameter( "hierarchyId", hierarchyId );

    return query.list();
  }

  public List getListOfNodeIdsAndUserIdsByImportFile( Long importFileId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.fileload.getListOfNodeAndUserIdsByImportFile" );
    query.setParameter( "importFileId", importFileId );

    List results = query.list();
    // sometimes Hibernate gives nulls back. I think the issue is resolved, but just in case.
    for ( int i = 0; i < results.size(); i++ )
    {
      if ( results.get( i ) == null )
      {
        results.remove( i );
      }

    }
    return results;
  }

  /**
   * Retrieves the list of nodes which have the specified user in the sepecified hierarchyRoleType.
   * 
   * @param user
   * @param hierarchyRoleType
   * @return List of Node
   */
  public List getNodesWithUserHavingRole( User user, HierarchyRoleType hierarchyRoleType )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.GetAllNodesHavingUserInRole" );

    query.setParameter( "userId", user.getId() );
    query.setParameter( "hierarchyRoleTypeCode", hierarchyRoleType.getCode() );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.NodeDAO#updateDescendantsPath(com.biperf.core.domain.hierarchy.Node,
   *      java.lang.String, java.lang.String)
   * @param baseNode
   * @param oldPathSubstring
   * @param newPathSubstring
   */
  public void updateDescendantsPath( Node baseNode, String oldPathSubstring, String newPathSubstring )
  {
    // Using Direct sql since in hibernate 3.0, hibernate bulk updates can't be
    // run with native SQL and HQL bulk updates don't work with the classic parser (which we use).

    // Flush initially to bring the database up to date
    getSession().flush();

    PreparedStatement preparedStatement = null;
    try
    {
      Long userId = UserManager.getUserId();
      StringBuffer query = new StringBuffer();
      query.append( " UPDATE NODE " );
      query.append( " SET path = REPLACE(path,?,? ), MODIFIED_BY =  ?, DATE_MODIFIED = sysdate, VERSION = VERSION + 1  " );
      query.append( " WHERE " );
      query.append( " NODE_ID IN ( SELECT NODE_ID FROM NODE subqueryNode START WITH subqueryNode.node_id = ? " );
      query.append( " CONNECT BY PRIOR subqueryNode.NODE_ID = subqueryNode.PARENT_NODE_ID )" );

      preparedStatement = getSession().connection().prepareStatement( query.toString() );
      int index = 1;
      preparedStatement.setString( index++, oldPathSubstring );
      preparedStatement.setString( index++, newPathSubstring );
      preparedStatement.setLong( index++, userId != null ? userId : new Long( 0 ) );
      preparedStatement.setLong( index++, baseNode.getId().longValue() );

      preparedStatement.executeUpdate();
    }
    catch( HibernateException e )
    {
      throw new BeaconRuntimeException( "Exception running node path bulk update", e );
    }
    catch( SQLException e )
    {
      throw new BeaconRuntimeException( "Exception running node path bulk update", e );
    }
    finally
    {
      if ( preparedStatement != null )
      {
        try
        {
          preparedStatement.close();
        }
        catch( SQLException e )
        {
          // ignore
        }
      }
    }
  }

  /**
   * For live promotions that have an approval Node Type and hierarchy set, return all nodes
   * matching the promotion node type and hierarchy that have no owner for nodes.
   */
  public List getNodeTypeApproverPromotionApprovalNodeswithNoOwner()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.node.NodeTypeApproverPromotionApprovalNodeswithNoOwner" ).list();
  }

  /**
   * Gets all deleted Node objects;
   * 
   * @return List - list of nodes
   */
  public List getAllDeletedNodes()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.node.GetAllDeletedNodes" ).list();
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.hierarchy.NodeDAO#getNumberOfUserNodesById(java.lang.Long)
   * @param nodeId
   * @return
   */
  public Long getNumberOfUserNodesById( Long nodeId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.GetNumberOfUserNodesById" );

    query.setParameter( "nodeId", nodeId );
    return (Long)query.uniqueResult();

  }

  /**
   * Search the database with the given search criteria params. Overridden from
   * 
   * @see com.biperf.core.dao.hierachary.NodeDAO#searchNode(com.biperf.core.service.hierachary.NodeSearchCriteria)
   * @param searchCriteria
   * @return List
   */
  public List<Node> searchNode( NodeSearchCriteria searchCriteria )
  {
    boolean runQuery = false;
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Node.class );

    if ( searchCriteria.getNodeName() != null )
    {
      criteria.add( Restrictions.ilike( "name", searchCriteria.getNodeName(), MatchMode.START ) );
      runQuery = true;
    }

    if ( searchCriteria.getNodeId() != null )
    {
      if ( searchCriteria.isNodeIdAndBelow() )
      {
        // A little hokey but the only way I could figure out how to do a sql connect by in criteria
        // query
        criteria.add( Restrictions.sqlRestriction( "{alias}.node_id in (select inner.node_id from node inner start with inner.node_id in (" + searchCriteria.getNodeId()
            + ") connect by prior inner.node_id=inner.parent_node_id)" ) );
      }
      else
      {
        criteria.add( Restrictions.eq( "id", searchCriteria.getNodeId() ) );
      }
      runQuery = true;
    }
    if ( searchCriteria.getBudgetSegmentId() != null )
    {
      criteria.add( Restrictions.sqlRestriction( "{alias}.node_id in (select inner.node_id from budget inner where inner.budget_segment_id = ?)",
                                                 searchCriteria.getBudgetSegmentId(),
                                                 StandardBasicTypes.LONG ) );
      runQuery = true;
    }
    List result = null;
    if ( runQuery )
    {
      // Avoid nodes that are deleted
      criteria.add( Restrictions.eq( "deleted", Boolean.FALSE ) );
      criteria.addOrder( Order.asc( "name" ) );
      Set resultSet = new LinkedHashSet( criteria.list() );
      result = new ArrayList( resultSet );
    }
    else
    {
      result = new ArrayList(); // empty list
    }

    return result;
  }

  public User getNodeOwnerByLevel( Long nodeId )
  {
    User nodeOwner = null;

    Node node = getNodeById( nodeId );
    while ( null != node )
    {
      // Get the node owner
      nodeOwner = this.getNodeOwner( node.getId() );
      if ( null != nodeOwner )
      {
        // Found the node owner
        break;
      }
      node = node.getParentNode();
    }

    return nodeOwner;
  }

  private User getNodeOwner( Long nodeId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.node.getOwnerForNode" );
    query.setLong( "nodeId", nodeId );
    return (User)query.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  public List<Long> findOwnerNodeIds( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.OwnerNodesForUser" );
    query.setParameter( "userId", userId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<Long> findChildNodeIds( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.NextLevelChildNodesForNode" );
    query.setParameter( "nodeId", nodeId );
    return query.list();
  }

  public boolean nodeHasOwner( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.NodeHasOwner" );
    query.setParameter( "nodeId", nodeId );
    return null == query.uniqueResult() ? false : true;
  }

  @SuppressWarnings( "unchecked" )
  public List<Long> findNodesAsHierarchyByUser( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.NodesAsHierarchyByUser" );
    query.setParameter( "userId", userId );
    return query.list();
  }

  public boolean isLeafNode( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.NodeCountByParentNodeId" );
    query.setParameter( "nodeId", nodeId );
    Long count = (Long)query.uniqueResult();
    return count == 0 ? true : false;
  }

  public List allChildNodesUnderParentNodes( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.ChildNodesUnderParentNodes" );
    query.setParameter( "nodeId", nodeId );
    return query.list();
  }

  public boolean isAnyNodeWithOutEnrollmentCode()
  {
    Query query = (Query)getSession().createSQLQuery( " select count(*) from node where self_enrollment_code is null " );
    BigDecimal count = (BigDecimal)query.uniqueResult();
    return count.intValue() > 0;
  }

  public Integer getNodesHavingEnrollmentCodesCount()
  {
    Query query = (Query)getSession().createSQLQuery( " select count(*) from node where self_enrollment_code is not null " );
    BigDecimal count = (BigDecimal)query.uniqueResult();
    return count.intValue();
  }

  @SuppressWarnings( "unchecked" )
  public List<Node> getNodesHavingEnrollmentCodes( Integer pageNumber, Integer pageSize )
  {
    Criteria criteria = getSession().createCriteria( Node.class );
    if ( pageNumber != null )
    {
      criteria.setMaxResults( pageSize );
      if ( pageNumber > 1 )
      {
        criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
      }
    }
    criteria.add( Restrictions.isNotNull( "selfEnrollmentCode" ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<Node> getNodesNotHavingEnrollmentCodes( Integer pageNumber, Integer pageSize )
  {
    Criteria criteria = getSession().createCriteria( Node.class );
    if ( pageNumber != null )
    {
      criteria.setMaxResults( pageSize );
      if ( pageNumber > 1 )
      {
        criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
      }
    }
    criteria.add( Restrictions.isNull( "selfEnrollmentCode" ) );
    return criteria.list();
  }

  public Node getNodeByEnrollmentCode( String enrollmentCode )
  {
    Criteria criteria = getSession().createCriteria( Node.class );
    criteria.add( Restrictions.eq( "selfEnrollmentCode", enrollmentCode ) );
    return (Node)criteria.uniqueResult();
  }

  /**
   * Gets all deleted Node objects;
   * 
   * @return List - list of nodes
   */
  public List<FormattedValueBean> getUserNodesAndBelow( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.processParameterValueChoices.NodesAsHierarchyByUser" );
    query.setParameter( "param", userId );
    query.setResultTransformer( Transformers.aliasToBean( FormattedValueBean.class ) );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Set getUsersByRole( Long nodeId, String hierarchyRoleType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.getNodeUsersByRole" );
    query.setParameter( "nodeId", nodeId );
    query.setParameter( "hierarchyRoleType", hierarchyRoleType );
    return new HashSet( query.list() );
  }

  @Override
  public void deleteNodeCharacteristics( NodeCharacteristic nodeCharacteristic )
  {
    getSession().delete( nodeCharacteristic );
  }

  @Override
  public Integer getPaxCountBasedOnNodes( Long nodeId, Boolean isIncludeChild )
  {
    Query query = null;
    if ( isIncludeChild )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.node.PaxCountParentAndChild" );
    }
    else
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.node.PaxCountParentOnly" );
    }
    query.setParameter( "nodeId", nodeId );
    return (Integer)query.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Map<Long, String> getNodeDescription( List<Long> nodeIds )
  {
    if ( nodeIds == null || nodeIds.isEmpty() )
    {
      return Collections.EMPTY_MAP;
    }
    Criteria criteria = getSession().createCriteria( Node.class );
    criteria.setProjection( Projections.projectionList().add( Projections.property( "id" ), "id" ).add( Projections.property( "name" ), "name" ) );
    criteria.add( Restrictions.in( "id", nodeIds ) );
    criteria.setResultTransformer( new AliasToBeanResultTransformer( Node.class ) );
    return (Map<Long, String>)criteria.list().stream().collect( Collectors.toMap( Node::getId, Node::getName ) );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public int getCountByNodeName( String startsWith )
  {
    List results = getSession().createCriteria( Node.class ).setProjection( Projections.rowCount() ).add( Restrictions.eq( "deleted", false ) )
        .add( Restrictions.ilike( "name", startsWith, MatchMode.START ) ).list();
    return ( (Long)results.get( 0 ) ).intValue();
  }

  public boolean isUserInNodeorBelow( Long searchUserId, Long loggedInUserid )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.checkUserInNodeorBelow" );
    query.setParameter( "searchUserId", searchUserId );
    query.setParameter( "loggedInUserid", loggedInUserid );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue() > 0;
  }

  @Override
  public Node getNodeByHierarchyIdAndNodeId( Long hierarchyId, Long nodeId )
  {
    Criteria criteria = getSession().createCriteria( Node.class );
    criteria.add( Restrictions.eq( "id", nodeId ) );
    criteria.add( Restrictions.eq( "hierarchy.id", hierarchyId ) );
    return (Node)criteria.uniqueResult();
  }

  public boolean isOwnerAlreadyInUserNode( Long userId, Long nodeId )
  {
    Criteria criteria = getSession().createCriteria( UserNode.class );
    criteria.add( Restrictions.eq( "active", new Boolean( true ) ) );
    criteria.add( Restrictions.eq( "hierarchyRoleType", HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) ) );
    criteria.add( Restrictions.ne( "user.id", userId ) );
    criteria.add( Restrictions.eq( "node.id", nodeId ) );
    return criteria.list().size() > 0 ? true : false;
  }

  @Override
  public Long getNodeIdByRosterNodeId( UUID rosterNodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.getNodeIdByRosterNodeId" );
    query.setParameter( "rosterNodeId", rosterNodeId.toString() );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterNodeIdByNodeId( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.getRosterNodeIdByNodeId" );
    query.setParameter( "nodeId", nodeId );
    return (UUID)query.uniqueResult();
  }

  @Override
  public List<String> getRosterNodeIdsByNodeIds( List<Long> nodeIds )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.node.getRosterNodeIdsByNodeIds" );
    query.setParameterList( "nodeIds", nodeIds );
    return (List<String>)query.list();
  }

  @Override
  public Node getNodeByRosterNodeId( UUID rosterNodeId )
  {
    Criteria searchCriteria = getSession().createCriteria( Node.class );
    searchCriteria.add( Restrictions.eq( "rosterNodeId", rosterNodeId ) );
    return (Node)searchCriteria.uniqueResult();
  }

}
