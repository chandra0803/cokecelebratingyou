/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/WebRulesAudienceTypeTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * WebRulesAudienceTypeTest.
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
public class WebRulesAudienceTypeTest extends BaseEnumTest
{
  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List audienceTypeList = WebRulesAudienceType.getList();
    assertNotNull( audienceTypeList );
    assertNotSame( audienceTypeList, Collections.EMPTY_LIST );
    assertTrue( audienceTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List audienceTypeList = WebRulesAudienceType.getList();
    WebRulesAudienceType audienceType = (WebRulesAudienceType)audienceTypeList.get( 0 );
    WebRulesAudienceType audienceType2 = WebRulesAudienceType.lookup( audienceType.getCode() );
    assertEquals( audienceType, audienceType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List audienceTypeList = WebRulesAudienceType.getList();
    for ( int i = 0; i < audienceTypeList.size(); i++ )
    {
      WebRulesAudienceType audienceType = (WebRulesAudienceType)audienceTypeList.get( i );
      assertNotNull( audienceType );
      assertNotNull( audienceType.getName() );
    }
  }
}
