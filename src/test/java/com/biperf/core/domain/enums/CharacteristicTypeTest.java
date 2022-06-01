/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CharacteristicTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * CharacteristicTypeTest.
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
public class CharacteristicTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List charTypeList = CharacteristicDataType.getList();
    assertNotNull( charTypeList );
    assertNotSame( charTypeList, Collections.EMPTY_LIST );
    assertTrue( charTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List charTypeList = CharacteristicDataType.getList();
    CharacteristicDataType charType = (CharacteristicDataType)charTypeList.get( 0 );
    CharacteristicDataType charType2 = CharacteristicDataType.lookup( charType.getCode() );
    assertEquals( charType, charType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List charTypeList = CharacteristicDataType.getList();
    CharacteristicDataType charType = (CharacteristicDataType)charTypeList.get( 0 );
    assertNotNull( charType );
    assertNotNull( charType.getName() );
  }

  /**
   * Test to make sure the items are not null - verify description asset is named right.
   */
  public void testPickListItems()
  {
    List charTypeList = CharacteristicDataType.getList();
    for ( int i = 0; i < charTypeList.size(); i++ )
    {
      CharacteristicDataType charType = (CharacteristicDataType)charTypeList.get( i );
      assertNotNull( charType.getName() );
    }
  }

}
