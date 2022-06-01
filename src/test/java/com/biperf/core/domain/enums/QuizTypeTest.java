/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/QuizTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * QuizTypeTest.
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
 * <td>crosenquest</td>
 * <td>Oct 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List quizTypeList = QuizType.getList();
    assertNotNull( quizTypeList );
    assertNotSame( quizTypeList, Collections.EMPTY_LIST );
    assertTrue( quizTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List quizTypeList = QuizType.getList();
    QuizType quizType = (QuizType)quizTypeList.get( 0 );
    QuizType quizType2 = QuizType.lookup( quizType.getCode() );
    assertEquals( quizType, quizType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List quizTypeList = QuizType.getList();
    for ( int i = 0; i < quizTypeList.size(); i++ )
    {
      QuizType quizType = (QuizType)quizTypeList.get( i );
      assertNotNull( quizType );
      assertNotNull( quizType.getName() );
    }
  }
}