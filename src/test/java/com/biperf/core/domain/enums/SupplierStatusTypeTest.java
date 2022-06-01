/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/SupplierStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * SupplierStatusTypeTest.
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
 * <td>sedey</td>
 * <td>June 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SupplierStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List supplierStatusList = SupplierStatusType.getList();
    assertNotNull( supplierStatusList );
    assertNotSame( supplierStatusList, Collections.EMPTY_LIST );
    assertTrue( supplierStatusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List supplierStatusList = SupplierStatusType.getList();
    SupplierStatusType supplierStatus = (SupplierStatusType)supplierStatusList.get( 0 );
    SupplierStatusType supplierStatus2 = SupplierStatusType.lookup( supplierStatus.getCode() );
    assertEquals( supplierStatus, supplierStatus2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List supplierStatusList = SupplierStatusType.getList();
    for ( int i = 0; i < supplierStatusList.size(); i++ )
    {
      SupplierStatusType supplierStatus = (SupplierStatusType)supplierStatusList.get( i );
      assertNotNull( supplierStatus );
      assertNotNull( supplierStatus.getName() );
    }
  }

}