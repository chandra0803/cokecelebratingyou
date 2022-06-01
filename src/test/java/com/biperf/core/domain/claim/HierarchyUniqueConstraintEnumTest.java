/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/claim/HierarchyUniqueConstraintEnumTest.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.List;

import junit.framework.TestCase;

/**
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
 * <td>Jun 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyUniqueConstraintEnumTest extends TestCase
{
  public void testEnum()
  {
    HierarchyUniqueConstraintEnum enumeration = HierarchyUniqueConstraintEnum.getEnum( HierarchyUniqueConstraintEnum.HIERARCHY_CODE );
    assertNotNull( enumeration );
  }

  public void testList()
  {
    List list = HierarchyUniqueConstraintEnum.getEnumList();
    assertEquals( 4, list.size() );

    // HierarchyUniqueConstraintEnum enum1 = (HierarchyUniqueConstraintEnum)list.get( 0 );
  }
}
