/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/system/hibernate/SystemVariableDAOTest.java,v $
 */

package com.biperf.core.dao.system.hibernate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.system.SystemVariableDAO;
import com.biperf.core.domain.enums.SystemVariableType;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * Unit test to test methods on SystemVariableDAO.
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
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SystemVariableDAOTest extends BaseDAOTest
{
  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   * 
   * @return SystemVariableDAO
   */
  protected SystemVariableDAO getSystemVariableDAO()
  {
    return (SystemVariableDAO)ApplicationContextFactory.getApplicationContext().getBean( "systemVariableDAO" );
  }

  /**
   * Test fetching all records.
   */
  @Test
  @SuppressWarnings( "rawtypes" )
  public void testGetAllSystemVariables()
  {
    SystemVariableDAO systemVariableDAO = getSystemVariableDAO();
    List propertyList = systemVariableDAO.getAllProperties();
    assertNotNull( "properyList is null", propertyList );
    assertTrue( "propertyList is empty", !propertyList.isEmpty() );
  }

  /**
   * Test fetching all viewable records.
   */
  @Test
  @SuppressWarnings( "rawtypes" )
  public void testGetAllSystemVariablesThatCanbeViewed()
  {
    SystemVariableDAO systemVariableDAO = getSystemVariableDAO();
    List<PropertySetItem> propertyList = systemVariableDAO.getAllProperties();
    List<PropertySetItem> items = propertyList.stream().filter( p -> !p.isViewable() ).collect( Collectors.toList() );
    assertEquals( "There are non viewable items", 0, items.size() );
  }

  /**
   * Test creating/updating, fetching and deleting the propertySet.
   */
  @Test
  public void testSaveRetrieveDeletePropertySet()
  {
    SystemVariableDAO systemVariableDAO = getSystemVariableDAO();
    PropertySetItem expectedPropertySet = buildPropertySetItem();

    systemVariableDAO.savePropertySetItem( expectedPropertySet );

    PropertySetItem actualPropertySet = systemVariableDAO.getPropertyByName( expectedPropertySet.getEntityName() );

    assertEquals( "Property Set Item Create not equals", expectedPropertySet, actualPropertySet );

    expectedPropertySet.setDoubleVal( new Double( 1.6 ) );

    systemVariableDAO.savePropertySetItem( expectedPropertySet );

    actualPropertySet = systemVariableDAO.getPropertyByName( expectedPropertySet.getEntityName() );

    assertEquals( "Property Set Item Update not equals", expectedPropertySet, actualPropertySet );

    systemVariableDAO.deletePropertySetItem( actualPropertySet );

    PropertySetItem checkPropertySet = systemVariableDAO.getPropertyByName( expectedPropertySet.getEntityName() );

    if ( checkPropertySet != null )
    {
      assertEquals( "Property Set Item still there after delete", expectedPropertySet, checkPropertySet );
    }
  }

  private PropertySetItem buildPropertySetItem()
  {
    PropertySetItem expectedPropertySet = new PropertySetItem();
    expectedPropertySet.setBooleanVal( Boolean.valueOf( true ) );
    expectedPropertySet.setDateVal( new Date() );
    expectedPropertySet.setDoubleVal( new Double( 1.3 ) );
    expectedPropertySet.setEntityName( "TestEntityName" );
    expectedPropertySet.setIntVal( new Integer( 1 ) );
    expectedPropertySet.setKey( "TestKey" );
    expectedPropertySet.setLongVal( new Long( 1 ) );
    expectedPropertySet.setStringVal( "TestStringValue" );
    expectedPropertySet.setType( SystemVariableType.lookup( SystemVariableType.STRING ) );
    return expectedPropertySet;
  }
}