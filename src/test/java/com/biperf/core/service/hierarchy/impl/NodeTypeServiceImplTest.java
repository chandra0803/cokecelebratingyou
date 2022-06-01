/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/hierarchy/impl/NodeTypeServiceImplTest.java,v $
 */

package com.biperf.core.service.hierarchy.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;

/**
 * NodeTypeServiceImplTest.
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
public class NodeTypeServiceImplTest extends MockObjectTestCase
{

  private NodeTypeServiceImpl nodeTypeService = new NodeTypeServiceImpl();
  private Mock mockNodeTypeDAO = null;
  private Mock mockNodeDAO = null;
  private Mock mockCmAssetService = null;

  // Members for EasyMock
  private NodeDAO nodeDAOMock;
  private NodeTypeDAO nodeTypeDAOMock;

  private NodeTypeServiceImpl nodeTypeServiceImplUnderTest;
  private CMAssetService cmAssetServiceMock;

  public void setUp() throws Exception
  {

    mockNodeTypeDAO = new Mock( NodeTypeDAO.class );
    nodeTypeService.setNodeTypeDAO( (NodeTypeDAO)mockNodeTypeDAO.proxy() );
    mockNodeDAO = new Mock( NodeDAO.class );
    nodeTypeService.setNodeDAO( (NodeDAO)mockNodeDAO.proxy() );
    mockCmAssetService = new Mock( CMAssetService.class );
    nodeTypeService.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );

    // EasyMock Setup
    nodeTypeServiceImplUnderTest = new NodeTypeServiceImpl();

    nodeTypeDAOMock = EasyMock.createMock( NodeTypeDAO.class );

    nodeDAOMock = EasyMock.createMock( NodeDAO.class );

    nodeTypeServiceImplUnderTest.setNodeTypeDAO( nodeTypeDAOMock );
    nodeTypeServiceImplUnderTest.setNodeDAO( nodeDAOMock );

    cmAssetServiceMock = EasyMock.createMock( CMAssetService.class );
    nodeTypeServiceImplUnderTest.setCmAssetService( cmAssetServiceMock );

  }

  /**
   * Test saving a nodeType for editing.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveNodeTypeForEdit() throws ServiceErrorException
  {

    NodeType nodeType = new NodeType();
    nodeType.setName( "testName" );
    nodeType.setCmAssetCode( "testAssetName" );
    nodeType.setNameCmKey( "testKeyName" );
    nodeType.setDeleted( false );
    nodeType.setVersion( new Long( 0 ) );
    nodeType.setId( new Long( 5 ) );

    EasyMock.expect( nodeTypeDAOMock.getNodeTypeById( nodeType.getId() ) ).andReturn( nodeType );

    EasyMock.expect( nodeTypeDAOMock.saveNodeType( nodeType ) ).andReturn( nodeType );
    CMDataElement cmDataElement = new CMDataElement( "Node Type Name", nodeType.getNameCmKey(), nodeType.getName(), true );

    cmAssetServiceMock.createOrUpdateAsset( NodeTypeServiceImpl.NODE_TYPE_DATA_SECTION_NAME,
                                            NodeTypeServiceImpl.NODE_TYPE_ASSET_TYPE_NAME,
                                            nodeType.getName() + NodeTypeServiceImpl.NODE_TYPE_ASSET_NAME_SUFFIX,
                                            nodeType.getCmAssetCode(),
                                            cmDataElement );

    EasyMock.replay( nodeTypeDAOMock );
    EasyMock.replay( cmAssetServiceMock );

    NodeType actualNodeType = nodeTypeServiceImplUnderTest.saveNodeType( nodeType );
    assertEquals( "ActualNode wasnt equals to what was expected", actualNodeType, nodeType );

    EasyMock.verify( nodeTypeDAOMock );
    EasyMock.verify( cmAssetServiceMock );

  }

  /**
   * Test saving the nodeType by adding it.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveNodeTypeForAdd() throws ServiceErrorException
  {

    NodeType nodeType = new NodeType();
    nodeType.setName( "testName" );
    nodeType.setCmAssetCode( "test.asset.name" + System.currentTimeMillis() );
    nodeType.setNameCmKey( NodeTypeServiceImpl.NODE_TYPE_NAME_KEY );
    nodeType.setDeleted( false );
    nodeType.setVersion( new Long( 0 ) );
    nodeType.setId( null );

    EasyMock.expect( cmAssetServiceMock.getUniqueAssetCode( NodeTypeServiceImpl.NODE_TYPE_DATA_ASSET_PREFIX ) ).andReturn( nodeType.getCmAssetCode() );
    CMDataElement cmDataElement = new CMDataElement( "Node Type Name", nodeType.getNameCmKey(), nodeType.getName(), true );

    cmAssetServiceMock.createOrUpdateAsset( NodeTypeServiceImpl.NODE_TYPE_DATA_SECTION_NAME,
                                            NodeTypeServiceImpl.NODE_TYPE_ASSET_TYPE_NAME,
                                            nodeType.getName() + NodeTypeServiceImpl.NODE_TYPE_ASSET_NAME_SUFFIX,
                                            nodeType.getCmAssetCode(),
                                            cmDataElement );

    EasyMock.expect( nodeTypeDAOMock.saveNodeType( nodeType ) ).andReturn( nodeType );

    EasyMock.replay( nodeTypeDAOMock );
    EasyMock.replay( cmAssetServiceMock );

    NodeType actualNodeType = nodeTypeServiceImplUnderTest.saveNodeType( nodeType );
    assertEquals( "ActualNode wasnt equals to what was expected", actualNodeType, nodeType );

    EasyMock.verify( nodeTypeDAOMock );
    EasyMock.verify( cmAssetServiceMock );

  }

  public void testSavingANodeTypeWithAnExistingName()
  {
    NodeType nodeType = new NodeType();
    nodeType.setName( "testName" );
    nodeType.setCmAssetCode( "test.asset.name" + System.currentTimeMillis() );
    nodeType.setNameCmKey( NodeTypeServiceImpl.NODE_TYPE_NAME_KEY );
    nodeType.setDeleted( false );
    nodeType.setVersion( new Long( 0 ) );
    nodeType.setId( null );

    // Create a new NodeType with an existing name. Should throw an exception
    try
    {
      EasyMock.expect( cmAssetServiceMock.getUniqueAssetCode( NodeTypeServiceImpl.NODE_TYPE_DATA_ASSET_PREFIX ) ).andReturn( nodeType.getCmAssetCode() );
      CMDataElement cmDataElement = new CMDataElement( "Node Type Name", nodeType.getNameCmKey(), nodeType.getName(), true );

      cmAssetServiceMock.createOrUpdateAsset( NodeTypeServiceImpl.NODE_TYPE_DATA_SECTION_NAME,
                                              NodeTypeServiceImpl.NODE_TYPE_ASSET_TYPE_NAME,
                                              nodeType.getName() + NodeTypeServiceImpl.NODE_TYPE_ASSET_NAME_SUFFIX,
                                              nodeType.getCmAssetCode(),
                                              cmDataElement );
      EasyMock.expectLastCall().andThrow( new NonUniqueDataServiceErrorException( "message" ) ).once();

      EasyMock.replay( cmAssetServiceMock );
      EasyMock.replay( nodeTypeDAOMock );

      NodeType actualNodeType1 = nodeTypeServiceImplUnderTest.saveNodeType( nodeType );
      assertEquals( "ActualNode wasnt equals to what was expected", actualNodeType1, nodeType );

      EasyMock.verify( nodeTypeDAOMock );

    }
    catch( ServiceErrorException e )
    {

      // Do nothing as this should have happened.
      assertEquals( "ServiceException was thrown", e.getClass(), ServiceErrorExceptionWithRollback.class );
      return;
    }
    fail( "Excpected exception with duplicate type" );
  }

  public void testGetAll()
  {

    List<Object> list = new ArrayList<>();
    list.add( "itemOne" );

    mockNodeTypeDAO.expects( once() ).method( "getAll" ).will( returnValue( list ) );

    List<NodeType> results = nodeTypeService.getAll();
    mockNodeTypeDAO.verify();
    assertEquals( results.get( 0 ), "itemOne" );

  }

  public void testGetByID()
  {

    NodeType result = new NodeType();
    Long id = new Long( 10 );

    mockNodeTypeDAO.expects( once() ).method( "getNodeTypeById" ).with( same( id ) ).will( returnValue( result ) );

    NodeType found = nodeTypeService.getNodeTypeById( id );
    mockNodeTypeDAO.verify();
    assertSame( result, found );

  }

  public void testDeleteNodeTypeWithNoErrors() throws ServiceErrorException
  {

    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );

    EasyMock.expect( nodeTypeDAOMock.getNodeTypeById( nodeType.getId() ) ).andReturn( nodeType );

    EasyMock.expect( nodeDAOMock.getNodesByNodeType( nodeType ) ).andReturn( new ArrayList<>() );

    String deletedName = nodeType.getI18nName() + "_Deleted" + System.currentTimeMillis();
    CMDataElement cmDataElement = new CMDataElement( "Node Type Name", nodeType.getNameCmKey(), deletedName, true );

    cmAssetServiceMock.createOrUpdateAsset( NodeTypeServiceImpl.NODE_TYPE_DATA_SECTION_NAME,
                                            NodeTypeServiceImpl.NODE_TYPE_ASSET_TYPE_NAME,
                                            nodeType.getI18nName() + NodeTypeServiceImpl.NODE_TYPE_ASSET_NAME_SUFFIX,
                                            nodeType.getCmAssetCode(),
                                            cmDataElement );

    EasyMock.replay( nodeTypeDAOMock );
    EasyMock.replay( cmAssetServiceMock );
    EasyMock.replay( nodeDAOMock );

    List<Object> errors = new ArrayList<>();
    nodeTypeServiceImplUnderTest.deleteNodeType( nodeType.getId(), errors );
    EasyMock.verify( nodeTypeDAOMock );
    mockNodeDAO.verify();

    assertTrue( errors.isEmpty() );
  }

  public void testDeleteNodeTypeWithErrors() throws ServiceErrorException
  {

    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );

    mockNodeTypeDAO.expects( once() ).method( "getNodeTypeById" ).with( same( nodeType.getId() ) ).will( returnValue( nodeType ) );

    List<Object> existing = new ArrayList<>();
    existing.add( "something" );
    existing.add( "something2" );
    mockNodeDAO.expects( once() ).method( "getNodesByNodeType" ).with( same( nodeType ) ).will( returnValue( existing ) );

    List<Object> errors = new ArrayList<>();
    nodeTypeService.deleteNodeType( nodeType.getId(), errors );
    mockNodeTypeDAO.verify();
    mockNodeDAO.verify();

    assertEquals( 1, errors.size() );
  }

  public void testDeleteNodeTypesNoErrors() throws Exception
  {
    // mock out the call to deleteNodeType since that is tested separately, and I want to test the
    // deleteNodeTypes method
    NodeTypeServiceImpl service = new NodeTypeServiceImpl()
    {
      public void deleteNodeType( Long id, List errors )
      {
        return;
      }
    };

    // This is a rather mediocre test for the success scenario
    List<Long> ids = new ArrayList<>();
    ids.add( new Long( 10 ) );
    ids.add( new Long( 20 ) );
    service.deleteNodeTypes( ids );
  }

  public void testDeleteNodeTypesErrors()
  {
    // mock out the call to deleteNodeType since that is tested separately, and I want to test the
    // deleteNodeTypes method
    NodeTypeServiceImpl service = new NodeTypeServiceImpl()
    {
      public void deleteNodeType( Long id, List errors )
      {
        errors.add( "something" );
        return;
      }
    }; // make it return an error

    List<Long> ids = new ArrayList<>();
    ids.add( new Long( 10 ) );
    ids.add( new Long( 20 ) );
    try
    {
      service.deleteNodeTypes( ids );
    }
    catch( ServiceErrorException e )
    {
      return;
    }
    fail( "Should have thrown validation exception" );
  }

}
