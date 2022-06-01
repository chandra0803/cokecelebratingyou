/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * NodeIncludeTypeTest.
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
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class FileImportApprovalTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List pickList = FileImportApprovalType.getList();
    assertNotNull( pickList );
    assertNotSame( pickList, Collections.EMPTY_LIST );
    assertTrue( pickList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List pickList = FileImportApprovalType.getList();
    FileImportApprovalType element = (FileImportApprovalType)pickList.get( 0 );
    String elementCode = element.getCode();
    FileImportApprovalType element2 = FileImportApprovalType.lookup( elementCode );
    assertEquals( element, element2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List pickList = FileImportApprovalType.getList();
    for ( int i = 0; i < pickList.size(); i++ )
    {
      FileImportApprovalType element = (FileImportApprovalType)pickList.get( i );
      assertNotNull( element );
      assertNotNull( element.getName() );
    }
  }

}