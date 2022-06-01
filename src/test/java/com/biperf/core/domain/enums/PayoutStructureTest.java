/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PayoutStructureTest.
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
 * <td>meadows</td>
 * <td>Dec 6, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutStructureTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List payoutStructureList = PayoutStructure.getList();
    assertNotNull( payoutStructureList );
    assertNotSame( payoutStructureList, Collections.EMPTY_LIST );
    assertTrue( payoutStructureList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List payoutStructureList = PayoutStructure.getList();
    PayoutStructure payoutStructure1 = (PayoutStructure)payoutStructureList.get( 0 );
    PayoutStructure payoutStructure2 = PayoutStructure.lookup( payoutStructure1.getCode() );
    assertEquals( payoutStructure1, payoutStructure2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List payoutStructureList = PayoutStructure.getList();
    for ( int i = 0; i < payoutStructureList.size(); i++ )
    {
      PayoutStructure payoutStructure = (PayoutStructure)payoutStructureList.get( i );
      assertNotNull( payoutStructure );
      assertNotNull( payoutStructure.getName() );
    }
  }

}
