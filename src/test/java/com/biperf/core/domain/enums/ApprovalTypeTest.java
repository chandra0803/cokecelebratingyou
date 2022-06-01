/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ApprovalTypeTest.
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
public class ApprovalTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List approvalTypeList = ApprovalType.getList();
    assertNotNull( approvalTypeList );
    assertNotSame( approvalTypeList, Collections.EMPTY_LIST );
    assertTrue( approvalTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List approvalTypeList = ApprovalType.getList();
    ApprovalType approvalType = (ApprovalType)approvalTypeList.get( 0 );
    ApprovalType approvalType2 = ApprovalType.lookup( approvalType.getCode() );
    assertEquals( approvalType, approvalType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List approvalTypeList = ApprovalType.getList();
    for ( int i = 0; i < approvalTypeList.size(); i++ )
    {
      ApprovalType approvalType = (ApprovalType)approvalTypeList.get( i );
      assertNotNull( approvalType );
      assertNotNull( approvalType.getName() );
    }
  }

}
