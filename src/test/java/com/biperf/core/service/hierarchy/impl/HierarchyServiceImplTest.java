/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/hierarchy/impl/HierarchyServiceImplTest.java,v $
 */

package com.biperf.core.service.hierarchy.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;

/**
 * HierarchyServiceImplTest.
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
 * <td>kumars</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyServiceImplTest extends MockObjectTestCase
{
  private HierarchyServiceImpl hierarchyServiceImpl = new HierarchyServiceImpl();

  private Mock mockHierarchyDAO = null;
  private Mock mockCmAssetService = null;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public HierarchyServiceImplTest( String test )
  {
    super( test );
  }

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockHierarchyDAO = new Mock( HierarchyDAO.class );
    hierarchyServiceImpl.setHierarchyDAO( (HierarchyDAO)mockHierarchyDAO.proxy() );

    mockCmAssetService = new Mock( CMAssetService.class );
    hierarchyServiceImpl.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );
  }

  /**
   * Test to save a hierarchy.
   * 
   * @throws ServiceErrorException
   */
  public void testSave() throws ServiceErrorException
  {
    // create a hierarchy object
    Hierarchy expectedHierarchy = new Hierarchy();
    expectedHierarchy.setId( new Long( 1 ) );
    expectedHierarchy.setName( "testNAME" );
    expectedHierarchy.setDescription( "testDESCRIPTION" );

    // create a hierarchy object
    Hierarchy primaryHierarchy = new Hierarchy();
    primaryHierarchy.setId( new Long( 1 ) );
    primaryHierarchy.setName( "testNAME" );
    primaryHierarchy.setDescription( "testDESCRIPTION" );

    // HierarchyDAO expected to call save once with the Hierarchy
    // which will return the Hierarchy expected
    mockHierarchyDAO.expects( once() ).method( "save" ).with( same( expectedHierarchy ) ).will( returnValue( expectedHierarchy ) );

    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // make the service call
    Hierarchy actualHierarchy = hierarchyServiceImpl.save( expectedHierarchy );

    assertEquals( "Actual hierarchy didn't match with what is expected", expectedHierarchy, actualHierarchy );

    mockHierarchyDAO.verify();
  }

  /**
   * Tests getting the hierarchy from the service through the DAO.
   */
  public void testGetById()
  {
    Hierarchy expectedHierarchy = new Hierarchy();
    expectedHierarchy.setId( new Long( 1 ) );
    expectedHierarchy.setName( "testNAME" );
    expectedHierarchy.setDescription( "testDESCRIPTION" );

    mockHierarchyDAO.expects( once() ).method( "getById" ).with( same( expectedHierarchy.getId() ) ).will( returnValue( expectedHierarchy ) );

    Hierarchy actualHierarchy = hierarchyServiceImpl.getById( expectedHierarchy.getId() );

    assertEquals( "Actual Hierarchy didn't match to what was expected", expectedHierarchy, actualHierarchy );

    mockHierarchyDAO.verify();
  }

  /**
   * Tests deleting the hierachy from the database through the service.
   */
  public void testDelete() throws ServiceErrorException
  {
    Hierarchy expectedHierarchy = new Hierarchy();
    expectedHierarchy.setId( new Long( 1 ) );
    expectedHierarchy.setName( "testNAME" );
    expectedHierarchy.setDescription( "testDESCRIPTION" );

    mockHierarchyDAO.expects( once() ).method( "save" ).with( same( expectedHierarchy ) );

    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    hierarchyServiceImpl.delete( expectedHierarchy );

    mockHierarchyDAO.verify();
  }

  /**
   * Test getting all nodes from the database through the service.
   */
  public void testGetAll()
  {
    List expectedList = getHierarchyList();

    mockHierarchyDAO.expects( once() ).method( "getAll" ).will( returnValue( expectedList ) );

    List actualList = hierarchyServiceImpl.getAll();

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedList ) );

    mockHierarchyDAO.verify();
  }

  /**
   * Build a list of Hierarchy objects for testing.
   * 
   * @return List
   */
  private List getHierarchyList()
  {
    List hierarchyList = new ArrayList();

    Hierarchy hierarchy1 = new Hierarchy();
    hierarchy1.setId( new Long( 1 ) );
    hierarchy1.setName( "testNAME-ABC" );
    hierarchy1.setDescription( "testDESCRIPTION-ABC" );

    Hierarchy hierarchy2 = new Hierarchy();
    hierarchy2.setId( new Long( 2 ) );
    hierarchy2.setName( "testNAME-XYZ" );
    hierarchy2.setDescription( "testDESCRIPTION-XYZ" );

    Hierarchy hierarchy3 = new Hierarchy();
    hierarchy3.setId( new Long( 2 ) );
    hierarchy3.setName( "testNAME-XAZ" );
    hierarchy3.setDescription( "testDESCRIPTION-XAZ" );

    hierarchyList.add( hierarchy1 );
    hierarchyList.add( hierarchy2 );
    hierarchyList.add( hierarchy3 );

    return hierarchyList;

  }

}
