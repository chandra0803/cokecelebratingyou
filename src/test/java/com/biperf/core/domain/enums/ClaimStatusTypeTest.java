
package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * ClaimStatusTypeTest.
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
 * <td>Nov 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List claimStatusList = ClaimStatusType.getList();
    assertNotNull( claimStatusList );
    assertNotSame( claimStatusList, Collections.EMPTY_LIST );
    assertTrue( claimStatusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List claimStatusList = ClaimStatusType.getList();
    ClaimStatusType claimStatus = (ClaimStatusType)claimStatusList.get( 0 );
    ClaimStatusType claimStatus2 = ClaimStatusType.lookup( claimStatus.getCode() );
    assertEquals( claimStatus, claimStatus2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List claimStatusList = ClaimStatusType.getList();
    for ( int i = 0; i < claimStatusList.size(); i++ )
    {
      ClaimStatusType claimStatus = (ClaimStatusType)claimStatusList.get( i );
      assertNotNull( claimStatus );
      assertNotNull( claimStatus.getName() );
    }
  }

}