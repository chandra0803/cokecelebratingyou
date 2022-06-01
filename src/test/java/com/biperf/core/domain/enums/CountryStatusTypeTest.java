/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/CountryStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * CountryStatusTypeTest.
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
 * <td>sedey</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CountryStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List countryStatusList = CountryStatusType.getList();
    assertNotNull( countryStatusList );
    assertNotSame( countryStatusList, Collections.EMPTY_LIST );
    assertTrue( countryStatusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List countryStatusList = CountryStatusType.getList();
    CountryStatusType countryStatus = (CountryStatusType)countryStatusList.get( 0 );
    CountryStatusType countryStatus2 = CountryStatusType.lookup( countryStatus.getCode() );
    assertEquals( countryStatus, countryStatus2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List countryStatusList = CountryStatusType.getList();
    for ( int i = 0; i < countryStatusList.size(); i++ )
    {
      CountryStatusType countryStatus = (CountryStatusType)countryStatusList.get( i );
      assertNotNull( countryStatus );
      assertNotNull( countryStatus.getName() );
    }
  }

}