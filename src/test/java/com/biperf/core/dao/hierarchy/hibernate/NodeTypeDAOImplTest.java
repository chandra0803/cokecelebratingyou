/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/hierarchy/hibernate/NodeTypeDAOImplTest.java,v $
 */

package com.biperf.core.dao.hierarchy.hibernate;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * NodeTypeDAOImpleTest.
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
 *
 */
public class NodeTypeDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link NodeType} object.
   * 
   * @param uniqueName
   * @return a new {@link NodeType} object.
   */
  public static NodeType buildNodeType( String uniqueName )
  {
    NodeType nodeType = new NodeType();

    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    return nodeType;
  }

  public static NodeType buildNodeType( String uniqueName, String nameCmKey )
  {
    NodeType nodeType = new NodeType();

    nodeType.setName( "testNodeType" + uniqueName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( nameCmKey );

    return nodeType;
  }

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  protected NodeTypeDAO getNodeTypeDAO()
  {
    return (NodeTypeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeTypeDAO" );
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Test saving a nodeType then getting that nodeType by the unique Id.
   */
  public void testSaveAndGetById()
  {

    NodeType nodeToSave = new NodeType();
    nodeToSave.setCmAssetCode( "asset1" );
    nodeToSave.setNameCmKey( "key50" );
    nodeToSave.setName( "key501" );

    NodeTypeDAO nodeTypeDAO = getNodeTypeDAO();
    NodeType saved = nodeTypeDAO.saveNodeType( nodeToSave );

    NodeType fetchedNodeType = nodeTypeDAO.getNodeTypeById( saved.getId() );

    assertEquals( fetchedNodeType.getCmAssetCode(), "asset1" );
    assertEquals( fetchedNodeType.getNameCmKey(), "key50" );
  }

  /**
   * Tests saving a bunch of nodeTypes then getting them all as a list.
   */
  /*
   * Commenting as it is depending on CM data in getAll query public void testGetAll() { NodeType
   * nodeToSave = new NodeType(); nodeToSave.setCmAssetCode( "asset1" ); nodeToSave.setNameCmKey(
   * "key50" ); nodeToSave.setName( "key501" ); NodeTypeDAO nodeTypeDAO = getNodeTypeDAO();
   * nodeTypeDAO.saveNodeType( nodeToSave ); List results = nodeTypeDAO.getAll(); assertTrue(
   * results.size() > 0 ); }
   */

}
