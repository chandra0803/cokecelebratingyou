/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ApprovalConditionalAmmountOperatorTypeTest.
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
 *          Exp $
 */
public class ApprovalConditionalAmmountOperatorTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List approvalConditionalAmmountOperatorTypeList = ApprovalConditionalAmmountOperatorType.getList();
    assertNotNull( approvalConditionalAmmountOperatorTypeList );
    assertNotSame( approvalConditionalAmmountOperatorTypeList, Collections.EMPTY_LIST );
    assertTrue( approvalConditionalAmmountOperatorTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List approvalConditionalAmmountOperatorTypeList = ApprovalConditionalAmmountOperatorType.getList();
    ApprovalConditionalAmmountOperatorType approvalType = (ApprovalConditionalAmmountOperatorType)approvalConditionalAmmountOperatorTypeList.get( 0 );
    ApprovalConditionalAmmountOperatorType approvalType2 = ApprovalConditionalAmmountOperatorType.lookup( approvalType.getCode() );
    assertEquals( approvalType, approvalType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List approvalConditionalAmmountOperatorTypeList = ApprovalConditionalAmmountOperatorType.getList();
    for ( int i = 0; i < approvalConditionalAmmountOperatorTypeList.size(); i++ )
    {
      ApprovalConditionalAmmountOperatorType approvalType = (ApprovalConditionalAmmountOperatorType)approvalConditionalAmmountOperatorTypeList.get( i );
      assertNotNull( approvalType );
      assertNotNull( approvalType.getName() );
    }
  }

}
