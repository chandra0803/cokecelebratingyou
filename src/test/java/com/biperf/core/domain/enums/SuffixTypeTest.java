/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/SuffixTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * SuffixTypeTest.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SuffixTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List suffixList = SuffixType.getList();
    assertNotNull( suffixList );
    assertNotSame( suffixList, Collections.EMPTY_LIST );
    assertTrue( suffixList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List suffixList = SuffixType.getList();
    SuffixType suffix = (SuffixType)suffixList.get( 0 );
    SuffixType suffix2 = SuffixType.lookup( suffix.getCode() );
    assertEquals( suffix, suffix2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List suffixList = SuffixType.getList();
    for ( int i = 0; i < suffixList.size(); i++ )
    {
      SuffixType suffix = (SuffixType)suffixList.get( i );
      assertNotNull( suffix );
      assertNotNull( suffix.getName() );
    }
  }

}