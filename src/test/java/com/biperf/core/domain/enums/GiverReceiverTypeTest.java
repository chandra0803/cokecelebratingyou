
package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * GiverReceiverTypeTest.
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
 * <td>robinsra</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GiverReceiverTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List giverReceiverList = GiverReceiverType.getList();
    assertNotNull( giverReceiverList );
    assertNotSame( giverReceiverList, Collections.EMPTY_LIST );
    assertTrue( giverReceiverList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List giverReceiverList = GiverReceiverType.getList();
    GiverReceiverType giverReceiver = (GiverReceiverType)giverReceiverList.get( 0 );
    GiverReceiverType giverReceiver2 = GiverReceiverType.lookup( giverReceiver.getCode() );
    assertEquals( giverReceiver, giverReceiver2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List giverReceiverList = GiverReceiverType.getList();
    for ( int i = 0; i < giverReceiverList.size(); i++ )
    {
      GiverReceiverType giverReceiver = (GiverReceiverType)giverReceiverList.get( i );
      assertNotNull( giverReceiver );
      assertNotNull( giverReceiver.getName() );
    }
  }

}