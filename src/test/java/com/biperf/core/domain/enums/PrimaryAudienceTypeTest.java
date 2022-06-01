/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/PrimaryAudienceTypeTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * PrimaryAudienceTypeTest.
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
 * <td>jenniget</td>
 * <td>Oct 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PrimaryAudienceTypeTest extends BaseEnumTest
{
  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List audienceTypeList = PrimaryAudienceType.getList();
    assertNotNull( audienceTypeList );
    assertNotSame( audienceTypeList, Collections.EMPTY_LIST );
    assertTrue( audienceTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List audienceTypeList = PrimaryAudienceType.getList();
    PrimaryAudienceType audienceType = (PrimaryAudienceType)audienceTypeList.get( 0 );
    PrimaryAudienceType audienceType2 = PrimaryAudienceType.lookup( audienceType.getCode() );
    assertEquals( audienceType, audienceType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List audienceTypeList = PrimaryAudienceType.getList();
    for ( int i = 0; i < audienceTypeList.size(); i++ )
    {
      PrimaryAudienceType audienceType = (PrimaryAudienceType)audienceTypeList.get( i );
      assertNotNull( audienceType );
      assertNotNull( audienceType.getName() );
    }
  }
}
