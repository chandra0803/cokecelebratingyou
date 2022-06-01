/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * OverrideStructureTest.
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
public class OverrideStructureTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List overrideStructureList = OverrideStructure.getList();
    assertNotNull( overrideStructureList );
    assertNotSame( overrideStructureList, Collections.EMPTY_LIST );
    assertTrue( overrideStructureList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List overrideStructureList = OverrideStructure.getList();
    OverrideStructure overrideStructure1 = (OverrideStructure)overrideStructureList.get( 0 );
    OverrideStructure overrideStructure2 = OverrideStructure.lookup( overrideStructure1.getCode() );
    assertEquals( overrideStructure1, overrideStructure2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List overrideStructureList = OverrideStructure.getList();
    for ( int i = 0; i < overrideStructureList.size(); i++ )
    {
      OverrideStructure overrideStructure = (OverrideStructure)overrideStructureList.get( i );
      assertNotNull( overrideStructure );
      assertNotNull( overrideStructure.getName() );
    }
  }

}
