/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ApproverTypeTest.
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
 * <td>asondgeroth</td>
 * <td>Jul 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApproverTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List approverTypeList = ApproverType.getList();
    assertNotNull( approverTypeList );
    assertNotSame( approverTypeList, Collections.EMPTY_LIST );
    assertTrue( approverTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List approverTypeList = ApproverType.getList();
    ApproverType approverType = (ApproverType)approverTypeList.get( 0 );
    ApproverType approverType2 = ApproverType.lookup( approverType.getCode() );
    assertEquals( approverType, approverType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List approverTypeList = ApproverType.getList();
    for ( int i = 0; i < approverTypeList.size(); i++ )
    {
      ApproverType approverType = (ApproverType)approverTypeList.get( i );
      assertNotNull( approverType );
      assertNotNull( approverType.getName() );
    }
  }

}
