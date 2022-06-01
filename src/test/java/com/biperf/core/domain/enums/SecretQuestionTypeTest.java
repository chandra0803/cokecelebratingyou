/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/SecretQuestionTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * SecretQuestionTypeTest.
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
public class SecretQuestionTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List secretQuestionList = SecretQuestionType.getList();
    assertNotNull( secretQuestionList );
    assertNotSame( secretQuestionList, Collections.EMPTY_LIST );
    assertTrue( secretQuestionList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List secretQuestionList = SecretQuestionType.getList();
    SecretQuestionType secretQuestion = (SecretQuestionType)secretQuestionList.get( 0 );
    SecretQuestionType secretQuestion2 = SecretQuestionType.lookup( secretQuestion.getCode() );
    assertEquals( secretQuestion, secretQuestion2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List secretQuestionList = SecretQuestionType.getList();
    for ( int i = 0; i < secretQuestionList.size(); i++ )
    {
      SecretQuestionType secretQuestion = (SecretQuestionType)secretQuestionList.get( i );
      assertNotNull( secretQuestion );
      assertNotNull( secretQuestion.getName() );
    }
  }

}