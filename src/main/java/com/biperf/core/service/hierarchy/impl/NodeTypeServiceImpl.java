/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/hierarchy/impl/NodeTypeServiceImpl.java,v $
 */

package com.biperf.core.service.hierarchy.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;

/**
 * NodeTypeServiceImpl <p/> <b>Change History:</b><br>
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
public class NodeTypeServiceImpl implements NodeTypeService
{

  private static final Log log = LogFactory.getLog( NodeTypeServiceImpl.class );
  private NodeTypeDAO nodeTypeDAO = null;
  private NodeDAO nodeDAO = null;
  private CMAssetService cmAssetService = null;

  public static final String NODE_TYPE_DATA_SECTION_NAME = "node_type_data";
  public static final String NODE_TYPE_DATA_ASSET_PREFIX = "node_type_data.nodetype.";
  public static final String NODE_TYPE_NAME_KEY = "NODE_TYPE_NAME";
  public static final String NODE_TYPE_ASSET_NAME_SUFFIX = " Node Type";
  public static final String NODE_TYPE_ASSET_TYPE_NAME = "_NodeTypeData";

  /**
   * Will throw an exception if the node type name is already taken
   * 
   * @param nodeType
   * @return NodeType
   * @see com.biperf.core.service.hierarchy.NodeTypeService#saveNodeType(com.biperf.core.domain.hierarchy.NodeType)
   */
  public NodeType saveNodeType( NodeType nodeType ) throws ServiceErrorException
  {
    NodeType current;

    if ( !nodeType.isNew() )
    {
      current = getNodeTypeDAO().getNodeTypeById( nodeType.getId() );
      current.setVersion( nodeType.getVersion() );
    }
    else
    {
      current = nodeType;
      current.setCmAssetCode( cmAssetService.getUniqueAssetCode( NODE_TYPE_DATA_ASSET_PREFIX ) );
      current.setNameCmKey( NODE_TYPE_NAME_KEY );
    }
    CMDataElement cmDataElement = new CMDataElement( "Node Type Name", current.getNameCmKey(), nodeType.getName(), true );

    try
    {
      cmAssetService.createOrUpdateAsset( NODE_TYPE_DATA_SECTION_NAME, NODE_TYPE_ASSET_TYPE_NAME, current.getName() + NODE_TYPE_ASSET_NAME_SUFFIX, current.getCmAssetCode(), cmDataElement );
    }
    catch( NonUniqueDataServiceErrorException e )
    {
      //
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.NODE_TYPE_NAME_IN_USE, nodeType.getName() );
      throw new ServiceErrorExceptionWithRollback( serviceError );
    }
    return getNodeTypeDAO().saveNodeType( current );

  }

  /**
   * for IoC
   * 
   * @return NoteTypeDAO
   */
  protected NodeTypeDAO getNodeTypeDAO()
  {
    return nodeTypeDAO;
  }

  /**
   * for IoC
   * 
   * @param nodeTypeDAO
   */
  public void setNodeTypeDAO( NodeTypeDAO nodeTypeDAO )
  {
    this.nodeTypeDAO = nodeTypeDAO;
  }

  /**
   * Overridden from
   * 
   * @return List
   * @see com.biperf.core.service.hierarchy.NodeTypeService#getAll()
   */
  public List<NodeType> getAll()
  {
    return getNodeTypeDAO().getAll();
  }

  /**
   * Gets a NodeType by id <p/> Overridden from
   * 
   * @param id
   * @return NodeType or null
   */
  public NodeType getNodeTypeById( Long id )
  {
    return getNodeTypeDAO().getNodeTypeById( id );
  }

  /**
   * Will logically delete the NodeTypes in the List, provided there are no nodes assigned to those
   * node types
   * 
   * @param list
   * @throws ServiceErrorException
   */
  public void deleteNodeTypes( List list ) throws ServiceErrorException
  {
    List errors = new ArrayList();

    for ( int i = 0; i < list.size(); i++ )
    {
      Long id = (Long)list.get( i );
      deleteNodeType( id, errors );
    }
    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorExceptionWithRollback( errors );
    }

  }

  /**
   * logically deletes the node type with the given ID. If there are active Nodes dependent on this
   * NodeType, it will add an error to the list and not delete.
   * 
   * @param id
   * @param errors
   */
  public void deleteNodeType( Long id, List errors ) throws ServiceErrorException
  {
    log.debug( "NodeTypeServiceImpl.deleteNodeType" );
    NodeType nodeType = getNodeTypeDAO().getNodeTypeById( id );

    List associatedNodes = getNodeDAO().getNodesByNodeType( nodeType );
    if ( !associatedNodes.isEmpty() )
    {
      errors.add( new ServiceError( ServiceErrorMessageKeys.NODE_TYPE_HAS_NODES_ASSIGNED, nodeType.getName() ) );
    }
    else
    {
      nodeType.setDeleted( true );
      String deletedName = nodeType.getI18nName() + "_Deleted" + System.currentTimeMillis();
      try
      {
        CMDataElement cmDataElement = new CMDataElement( "Node Type Name", nodeType.getNameCmKey(), deletedName, true );
        cmAssetService.createOrUpdateAsset( NODE_TYPE_DATA_SECTION_NAME, NODE_TYPE_ASSET_TYPE_NAME, nodeType.getI18nName() + NODE_TYPE_ASSET_NAME_SUFFIX, nodeType.getCmAssetCode(), cmDataElement );
      }
      catch( ServiceErrorException e )
      {
        throw new ServiceErrorExceptionWithRollback( ServiceErrorMessageKeys.NODE_TYPE_DELETE_ERROR, e );
      }
    }
  }

  public NodeType getDefaultNodeType()
  {
    return getNodeTypeDAO().getDefaultNodeType();
  }

  protected NodeDAO getNodeDAO()
  {
    return nodeDAO;
  }

  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

}
