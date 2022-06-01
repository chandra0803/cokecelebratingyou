/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/SystemVariableTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * SystemVariableTypeTest.
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
 * <td>dunne</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SystemVariableTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List sysVarList = SystemVariableType.getList();
    assertNotNull( sysVarList );
    assertNotSame( sysVarList, Collections.EMPTY_LIST );
    assertTrue( sysVarList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List sysVarList = SystemVariableType.getList();
    SystemVariableType sysVar = (SystemVariableType)sysVarList.get( 0 );
    SystemVariableType sysVar2 = SystemVariableType.lookup( sysVar.getCode() );
    assertEquals( sysVar, sysVar2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List sysVarList = SystemVariableType.getList();
    SystemVariableType sysVar = (SystemVariableType)sysVarList.get( 0 );
    assertNotNull( sysVar );
    assertNotNull( sysVar.getName() );
  }

}