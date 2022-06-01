/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/system/impl/SystemVariablesServiceImplTest.java,v $
 */

package com.biperf.core.service.system.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import com.biperf.core.dao.system.SystemVariableDAO;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.system.SystemVariableService;

/**
 * SystemVariablesServiceImplTest.
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
 * <td>Adam</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SystemVariablesServiceImplTest extends MockObjectTestCase
{
  public SystemVariablesServiceImplTest( String test )
  {
    super( test );
  }

  private SystemVariableService service = new SystemVariableServiceImpl();
  private Mock mockDAO = null;

  protected void setUp() throws Exception
  {
    mockDAO = new Mock( SystemVariableDAO.class );
    service.setSystemVariableDAO( (SystemVariableDAO)mockDAO.proxy() );
  }

  @Test
  public void testGetAllProperties()
  {
    mockDAO.expects( once() ).method( "getAllProperties" );
    service.getAllProperties();
    mockDAO.verify();
  }

  @Test
  public void testGetPropertiesByName()
  {
    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setEntityName( "" );
    propertySetItem.setEntityId( 0L );
    propertySetItem.setKey( "" );
    mockDAO.expects( once() ).method( "getPropertyByName" ).with( same( "a" ) ).will( returnValue( propertySetItem ) );
    assertEquals( propertySetItem, service.getPropertyByName( "a" ) );
    mockDAO.verify();
  }

  @Test
  public void testSavePropertySucceess()
  {
    PropertySetItem prop = new PropertySetItem();
    prop.setEntityName( "TestName" );
    prop.setEntityId( 0L );
    prop.setKey( "key" );
    String propertyName = "TestName";
    mockDAO.expects( once() ).method( "getPropertyByName" ).with( same( propertyName ) );
    mockDAO.expects( once() ).method( "savePropertySetItem" ).with( same( prop ) );
    try
    {
      service.saveProperty( prop );
    }
    catch( ServiceErrorException e )
    {
      fail();
    }
    mockDAO.verify();
  }

  @Test
  public void testSavePropertyFail()
  {
    PropertySetItem prop = new PropertySetItem();
    prop.setEntityName( "duplicates" );
    prop.setEntityId( 0L );
    prop.setKey( "key" );
    String propertyName = "duplicates";
    mockDAO.expects( once() ).method( "getPropertyByName" ).with( same( propertyName ) ).will( returnValue( prop ) );
    try
    {
      service.saveProperty( prop );
    }
    catch( ServiceErrorException e )
    {
      // yeah! this is what we're testing should happen
      mockDAO.verify();
      return;
    }
    fail();
  }

  public void testDeleteProperty()
  {
    String propertyName = "TestName";
    PropertySetItem prop = new PropertySetItem();
    prop.setEntityName( propertyName );
    prop.setEntityId( 0L );
    prop.setKey( "key" );
    mockDAO.expects( once() ).method( "deletePropertySetItem" ).with( same( prop ) );
    service.deleteProperty( prop );
    mockDAO.verify();
  }

}
