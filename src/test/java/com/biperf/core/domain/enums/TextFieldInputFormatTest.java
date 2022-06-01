
package com.biperf.core.domain.enums;/*
                                     * (c) 2005 BI, Inc.  All rights reserved.
                                     * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/TextFieldInputFormatTest.java,v $
                                     */

import java.util.Collections;
import java.util.List;

/**
 * com.biperf.core.domain.enums.TextFieldInputFormatTest.
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
 * <td>tennant</td>
 * <td>June 01, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TextFieldInputFormatTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List textFieldInputFormatList = TextFieldInputFormatType.getList();
    assertNotNull( textFieldInputFormatList );
    assertNotSame( textFieldInputFormatList, Collections.EMPTY_LIST );
    assertTrue( textFieldInputFormatList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List textFieldInputFormatList = TextFieldInputFormatType.getList();
    TextFieldInputFormatType item1 = (TextFieldInputFormatType)textFieldInputFormatList.get( 0 );
    TextFieldInputFormatType item2 = TextFieldInputFormatType.lookup( item1.getCode().toUpperCase() );
    assertEquals( item1, item2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List textFieldInputFormatList = TextFieldInputFormatType.getList();
    for ( int i = 0; i < textFieldInputFormatList.size(); i++ )
    {
      TextFieldInputFormatType item = (TextFieldInputFormatType)textFieldInputFormatList.get( i );
      assertNotNull( item );
      assertNotNull( item.getName() );
    }
  }

}