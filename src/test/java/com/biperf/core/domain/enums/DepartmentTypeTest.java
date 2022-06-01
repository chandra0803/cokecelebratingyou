/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/DepartmentTypeTest.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * DepartmentTypeTest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class DepartmentTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List department = DepartmentType.getList();
    assertNotNull( department );
    assertNotSame( department, Collections.EMPTY_LIST );
    assertTrue( department.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List departmentList = DepartmentType.getList();
    DepartmentType department = (DepartmentType)departmentList.get( 0 );
    DepartmentType department2 = DepartmentType.lookup( department.getCode() );
    assertEquals( department, department2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List departmentList = DepartmentType.getList();
    for ( int i = 0; i < departmentList.size(); i++ )
    {
      DepartmentType department = (DepartmentType)departmentList.get( i );
      assertNotNull( department );
      assertNotNull( department.getName() );
    }
  }

}