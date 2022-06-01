/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/QuizQuestionStatusTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * QuizQuestionStatusTypeTest.
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
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizQuestionStatusTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List statusList = QuizQuestionStatusType.getList();
    assertNotNull( statusList );
    assertNotSame( statusList, Collections.EMPTY_LIST );
    assertTrue( statusList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List statusList = QuizQuestionStatusType.getList();
    QuizQuestionStatusType status = (QuizQuestionStatusType)statusList.get( 0 );
    QuizQuestionStatusType status2 = QuizQuestionStatusType.lookup( status.getCode() );
    assertEquals( status, status2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List statusList = QuizQuestionStatusType.getList();
    QuizQuestionStatusType status = (QuizQuestionStatusType)statusList.get( 0 );
    assertNotNull( status );
    assertNotNull( status.getName() );
  }

}