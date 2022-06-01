/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * RoundingMethodTest.
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
public class RoundingMethodTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List roundingMethodList = RoundingMethod.getList();
    assertNotNull( roundingMethodList );
    assertNotSame( roundingMethodList, Collections.EMPTY_LIST );
    assertTrue( roundingMethodList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List roundingMethodList = RoundingMethod.getList();
    RoundingMethod roundingMethod1 = (RoundingMethod)roundingMethodList.get( 0 );
    RoundingMethod roundingMethod2 = RoundingMethod.lookup( roundingMethod1.getCode() );
    assertEquals( roundingMethod1, roundingMethod2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List roundingMethodList = RoundingMethod.getList();
    for ( int i = 0; i < roundingMethodList.size(); i++ )
    {
      RoundingMethod roundingMethod = (RoundingMethod)roundingMethodList.get( i );
      assertNotNull( roundingMethod );
      assertNotNull( roundingMethod.getName() );
    }
  }

}
