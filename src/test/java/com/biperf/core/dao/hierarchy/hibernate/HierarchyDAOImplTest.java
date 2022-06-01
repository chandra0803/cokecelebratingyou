/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/hierarchy/hibernate/HierarchyDAOImplTest.java,v $
 */

package com.biperf.core.dao.hierarchy.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * HierarchyDAOImplTest.
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
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  public static Hierarchy buildPrimaryHierarchy( String uniqueName )
  {
    Hierarchy hierarchy = new Hierarchy();

    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( true );
    hierarchy.setActive( true );
    hierarchy.setDeleted( false );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" + uniqueName );

    return hierarchy;
  }

  /**
   * Builds a {@link Hierarchy} object.
   * 
   * @return a new {@link Hierarchy} object.
   * @param uniqueName
   */
  public static Hierarchy buildHierarchy( String uniqueName )
  {
    Hierarchy hierarchy = new Hierarchy();

    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setDeleted( false );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" + uniqueName );

    return hierarchy;
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests create, update and selecting the hierarchy by the Id.
   */
  public void testSaveAndGetById()
  {
    // create a new hierarchy
    HierarchyDAO hierarchyDAO = getHierarchyDAO();

    Hierarchy expectedHierarchy = new Hierarchy();
    expectedHierarchy.setName( "Test Hierarchy Name55" );
    expectedHierarchy.setDescription( "description goes here" );
    expectedHierarchy.setPrimary( false );
    expectedHierarchy.setActive( true );
    expectedHierarchy.setCmAssetCode( "CM name ASSET" );
    expectedHierarchy.setNameCmKey( "CM name KEY" );

    hierarchyDAO.save( expectedHierarchy );

    System.out.println( "-----saved ID: " + expectedHierarchy.getId() );
    assertEquals( "Actual hierarchy doesn't match with expected", expectedHierarchy, hierarchyDAO.getById( expectedHierarchy.getId() ) );

    // do an update on the saved hierarchy
    expectedHierarchy.setName( "test Hierarchy Name-UPDATED" );
    hierarchyDAO.save( expectedHierarchy );

    // retrieve the hierarchy
    Hierarchy actualHierarchy = hierarchyDAO.getById( expectedHierarchy.getId() );
    assertEquals( "Actual hierarchy doesn't match with expected", expectedHierarchy, actualHierarchy );

    assertTrue( "Updated Hierarchy Name doesn't match the actual name", expectedHierarchy.getName().equals( actualHierarchy.getName() ) );
  }

  public void testSaveAndGetByIdWithNoPrimary()
  {
    // create a new hierarchy
    HierarchyDAO hierarchyDAO = getHierarchyDAO();
    clearOutCurrentPrimaryHierarchy();

    Hierarchy expectedHierarchy = new Hierarchy();
    expectedHierarchy.setName( "Test Hierarchy Name55" );
    expectedHierarchy.setDescription( "description goes here" );
    expectedHierarchy.setPrimary( false );
    expectedHierarchy.setActive( true );
    expectedHierarchy.setCmAssetCode( "CM name ASSET" );
    expectedHierarchy.setNameCmKey( "CM name KEY" );

    hierarchyDAO.save( expectedHierarchy );

    System.out.println( "-----saved ID: " + expectedHierarchy.getId() );
    assertEquals( "Actual hierarchy doesn't match with expected", expectedHierarchy, hierarchyDAO.getById( expectedHierarchy.getId() ) );

    // do an update on the saved hierarchy
    expectedHierarchy.setName( "test Hierarchy Name-UPDATED" );
    hierarchyDAO.save( expectedHierarchy );

    // retrieve the hierarchy
    Hierarchy actualHierarchy = hierarchyDAO.getById( expectedHierarchy.getId() );
    assertEquals( "Actual hierarchy doesn't match with expected", expectedHierarchy, actualHierarchy );

    assertTrue( "Updated Hierarchy Name doesn't match the actual name", expectedHierarchy.getName().equals( actualHierarchy.getName() ) );
  }

  /**
   * Tests getting all active, not deleted hierarchies.
   */
  public void testGetActiveHierarchies()
  {
    List expectedList = new ArrayList();
    List notExpectedList = new ArrayList();

    // Add some active hierarchies.
    Hierarchy hierarchy1 = buildHierarchy( getUniqueString() );
    hierarchy1.setActive( true );
    hierarchy1.setDeleted( false );
    getHierarchyDAO().save( hierarchy1 );
    expectedList.add( hierarchy1 );

    Hierarchy hierarchy2 = buildHierarchy( getUniqueString() );
    hierarchy2.setActive( false );
    hierarchy2.setDeleted( false );
    getHierarchyDAO().save( hierarchy2 );
    notExpectedList.add( hierarchy2 );

    Hierarchy hierarchy3 = buildHierarchy( getUniqueString() );
    hierarchy3.setActive( true );
    hierarchy3.setDeleted( true );
    getHierarchyDAO().save( hierarchy3 );
    notExpectedList.add( hierarchy3 );

    Hierarchy hierarchy4 = buildHierarchy( getUniqueString() );
    hierarchy4.setActive( false );
    hierarchy4.setDeleted( true );
    getHierarchyDAO().save( hierarchy4 );
    notExpectedList.add( hierarchy4 );

    // Fetch the active hierarchies.
    List actualList = getHierarchyDAO().getActiveHierarchies();
    assertTrue( actualList.containsAll( expectedList ) );

    Iterator iter = notExpectedList.iterator();
    while ( iter.hasNext() )
    {
      assertTrue( !actualList.contains( iter.next() ) );
    }
  }

  /**
   * Test getting all hierarchies in the database.
   */
  public void testGetAll()
  {

    HierarchyDAO hierarchyDAO = getHierarchyDAO();

    List expectedList = new ArrayList();

    Hierarchy hierarchy1 = new Hierarchy();
    hierarchy1.setName( "testGETALLNAME" );
    hierarchy1.setPrimary( false );
    hierarchy1.setActive( true );
    hierarchy1.setCmAssetCode( "CM name ASSET" );
    hierarchy1.setNameCmKey( "CM name KEY" );
    hierarchyDAO.save( hierarchy1 );

    expectedList.add( hierarchy1 );

    Hierarchy hierarchy2 = new Hierarchy();
    hierarchy2.setName( "testGETALLNAME2" );
    hierarchy2.setPrimary( false );
    hierarchy2.setActive( true );
    hierarchy2.setCmAssetCode( "CM name ASSET" );
    hierarchy2.setNameCmKey( "CM name KEY" );
    hierarchyDAO.save( hierarchy2 );

    expectedList.add( hierarchy2 );

    Hierarchy hierarchy3 = new Hierarchy();
    hierarchy3.setName( "testGETALLNAME3" );
    hierarchy3.setPrimary( false );
    hierarchy3.setActive( true );
    hierarchy3.setCmAssetCode( "CM name ASSET" );
    hierarchy3.setNameCmKey( "CM name KEY" );
    hierarchyDAO.save( hierarchy3 );

    expectedList.add( hierarchy3 );

    List actualList = hierarchyDAO.getAll();

    assertTrue( "Actual set of hierarchies after getting all from the database doesn't contain the expected set", actualList.containsAll( expectedList ) );

  }

  /**
   * Test for delete the hierarchy from the database.
   */
  public void testDelete()
  {

    HierarchyDAO hierarchyDAO = getHierarchyDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "test hierarchy NAME" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    hierarchyDAO.save( hierarchy );

    // Flush the session to ensure the commit of saving the node has been completed.
    HibernateSessionManager.getSession().flush();

    // Ensure the hierarchy has been saved to the database.
    assertEquals( "Hierarchy was not saved to the database as expected", hierarchy, hierarchyDAO.getById( hierarchy.getId() ) );

    Long hierarchyId = hierarchy.getId();
    hierarchyDAO.delete( hierarchy );

    // Flush the session to make sure the delete occured.
    HibernateSessionManager.getSession().flush();

    // check if the hierarchy got deleted
    assertTrue( "Hierarchy was not deleted from the database.", hierarchyDAO.getById( hierarchyId ) == null );

  }

  /**
   * Test to get the primary hierarchy
   */
  public void testPrimaryHierarchy()
  {
    clearOutCurrentPrimaryHierarchy();

    HierarchyDAO hierarchyDAO = getHierarchyDAO();

    Hierarchy hierarchy1 = new Hierarchy();
    hierarchy1.setName( "Test NodeHierarchy2" );
    hierarchy1.setPrimary( false );
    hierarchy1.setActive( true );
    hierarchy1.setCmAssetCode( "CM name ASSET" );
    hierarchy1.setNameCmKey( "CM name KEY" );
    hierarchyDAO.save( hierarchy1 );

    HibernateSessionManager.getSession().flush();

    Hierarchy hierarchy2 = new Hierarchy();
    hierarchy2.setName( "test-XYZ" );
    hierarchy2.setPrimary( false );
    hierarchy2.setActive( true );
    hierarchy2.setCmAssetCode( "CM name ASSET" );
    hierarchy2.setNameCmKey( "CM name KEY" );
    hierarchyDAO.save( hierarchy2 );

    Hierarchy hierarchy3 = new Hierarchy();
    hierarchy3.setName( "test-XYZ2" );
    hierarchy3.setPrimary( false );
    hierarchy3.setActive( true );
    hierarchy3.setCmAssetCode( "CM name ASSET" );
    hierarchy3.setNameCmKey( "CM name KEY" );
    hierarchyDAO.save( hierarchy3 );

    Hierarchy primaryHierarchy = hierarchyDAO.getPrimaryHierarchy();
    assertTrue( "Primary Hierarchy didn't match the expected hierarchy", primaryHierarchy.equals( hierarchy1 ) );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private void clearOutCurrentPrimaryHierarchy()
  {
    HierarchyDAO hierarchyDAO = getHierarchyDAO();

    Hierarchy primary = hierarchyDAO.getPrimaryHierarchy();
    if ( primary != null )
    {
      primary.setPrimary( false );
      HibernateSessionManager.getSession().save( primary );
      HibernateSessionManager.getSession().flush();
      HibernateSessionManager.getSession().evict( primary );
    }

  }

  /**
   * Uses the ApplicationContextFactory to look up the HierarchyDAO implementation.
   * 
   * @return HierarchyDAO
   */
  private HierarchyDAO getHierarchyDAO()
  {
    return (HierarchyDAO)ApplicationContextFactory.getApplicationContext().getBean( "hierarchyDAO" );
  }
}
