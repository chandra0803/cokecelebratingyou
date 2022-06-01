/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/HierarchyRoleTypeTest.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * HierarchyTypeTest <p/> <b>Change History:</b><br>
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
public class HierarchyRoleTypeTest extends BaseEnumTest
{
  private static final Log logger = LogFactory.getLog( HierarchyRoleTypeTest.class );

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List hierarchy = HierarchyRoleType.getList();
    assertNotNull( hierarchy );
    assertNotSame( hierarchy, Collections.EMPTY_LIST );
    assertTrue( hierarchy.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List hierarchyList = HierarchyRoleType.getList();
    HierarchyRoleType hierarchy = (HierarchyRoleType)hierarchyList.get( 0 );

    HierarchyRoleType hierarchy2 = HierarchyRoleType.lookup( hierarchy.getCode() );
    assertEquals( hierarchy, hierarchy2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List hierarchyList = HierarchyRoleType.getList();
    for ( int i = 0; i < hierarchyList.size(); i++ )
    {
      HierarchyRoleType hierarchy = (HierarchyRoleType)hierarchyList.get( i );

      logger.info( "hierarchy = " + hierarchy );
      assertNotNull( hierarchy );
      assertNotNull( hierarchy.getName() );
    }
  }

}