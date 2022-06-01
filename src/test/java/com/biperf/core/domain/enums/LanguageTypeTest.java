/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/LanguageTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * LanguageTypeTest.
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
public class LanguageTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List languageList = LanguageType.getList();
    assertNotNull( languageList );
    assertNotSame( languageList, Collections.EMPTY_LIST );
    assertTrue( languageList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List languageList = LanguageType.getList();
    LanguageType language = (LanguageType)languageList.get( 0 );
    LanguageType language2 = LanguageType.lookup( language.getCode() );
    assertEquals( language, language2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List languageList = LanguageType.getList();
    for ( int i = 0; i < languageList.size(); i++ )
    {
      LanguageType language = (LanguageType)languageList.get( i );
      assertNotNull( language );
      assertNotNull( language.getName() );
    }
  }

}