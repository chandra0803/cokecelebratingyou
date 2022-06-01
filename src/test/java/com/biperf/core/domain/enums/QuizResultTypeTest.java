
package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * QuizResultTypeTest.
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
public class QuizResultTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List quizResultList = QuizResultType.getList();
    assertNotNull( quizResultList );
    assertNotSame( quizResultList, Collections.EMPTY_LIST );
    assertTrue( quizResultList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List quizResultList = QuizResultType.getList();
    QuizResultType quizResult = (QuizResultType)quizResultList.get( 0 );
    QuizResultType quizResult2 = QuizResultType.lookup( quizResult.getCode() );
    assertEquals( quizResult, quizResult2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List quizResultList = QuizResultType.getList();
    for ( int i = 0; i < quizResultList.size(); i++ )
    {
      QuizResultType quizResult = (QuizResultType)quizResultList.get( i );
      assertNotNull( quizResult );
      assertNotNull( quizResult.getName() );
    }
  }

}